package uk.co.cadogsoftware.api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.co.cadogsoftware.api.testutils.TestFixtures.ISBN_1;
import static uk.co.cadogsoftware.api.testutils.TestFixtures.TEST_BOOKDTO_1;
import static uk.co.cadogsoftware.api.testutils.TestFixtures.TEST_BOOKDTO_2;
import static uk.co.cadogsoftware.api.testutils.TestFixtures.TEST_BOOKDTO_3;
import static uk.co.cadogsoftware.api.testutils.TestFixtures.TEST_BOOK_1;
import static uk.co.cadogsoftware.api.testutils.TestFixtures.TEST_BOOK_2;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.cadogsoftware.api.converters.BookConverter;
import uk.co.cadogsoftware.api.database.entities.Book;
import uk.co.cadogsoftware.api.database.repositories.BookRepository;
import uk.co.cadogsoftware.api.dtos.BookDTO;
import uk.co.cadogsoftware.api.exceptions.BookAlreadyExistsException;
import uk.co.cadogsoftware.api.exceptions.BookNotFoundException;

/**
 * Test class for {@link BookService}.
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

  @InjectMocks
  private BookService bookService;

  @Mock
  private BookRepository bookRepository;

  @Mock
  private BookConverter bookConverter;

  @Test
  void getBook_ReturnsCorrectBook() {
    when(bookRepository.findByIsbn(ISBN_1)).thenReturn(TEST_BOOK_1);
    when(bookConverter.convertToBookDTO(TEST_BOOK_1)).thenReturn(TEST_BOOKDTO_1);

    BookDTO bookDTO = bookService.getBook(ISBN_1);

    assertEquals(TEST_BOOKDTO_1, bookDTO);
  }

  @Test
  void getBook_NotFound() {
    String bookIsbnNotFound = "777";
    when(bookRepository.findByIsbn(bookIsbnNotFound)).thenReturn(null);

    BookNotFoundException bookNotFoundException = assertThrows(BookNotFoundException.class,
        () -> bookService.getBook(bookIsbnNotFound));

    assertEquals("Book cannot be found for ISBN: " + bookIsbnNotFound,
        bookNotFoundException.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " "})
  @NullSource
    // Tests for a null input. You can't do this in the @ValueSource annotation.
    //@NullAndEmptySource // This tests for null and "" but not a String containing all spaces.
  void getBooks_NoTitle(String input) {
    List<Book> expectedBookList = List.of(TEST_BOOK_1, TEST_BOOK_2);
    List<BookDTO> expectedBookDtoList = List.of(TEST_BOOKDTO_1, TEST_BOOKDTO_2);

    when(bookRepository.findAll()).thenReturn(expectedBookList);
    when(bookConverter.convertToBookDTOList(expectedBookList)).thenReturn(expectedBookDtoList);

    List<BookDTO> books = bookService.findBooks(input);
    assertEquals(expectedBookDtoList, books);
  }

  @ParameterizedTest
  @ValueSource(strings = {"Farm", "   Farm", "  Farm     "})
  @DisplayName("Tests for books with the input in the title. Also tests the trimming of the input.")
  void getBooks_WithTitle(String input) {
    List<Book> expectedBookList = List.of(TEST_BOOK_1, TEST_BOOK_2);
    List<BookDTO> expectedBookDtoList = List.of(TEST_BOOKDTO_1, TEST_BOOKDTO_2);

    when(bookRepository.findByTitleContaining(input)).thenReturn(expectedBookList);
    when(bookConverter.convertToBookDTOList(expectedBookList)).thenReturn(expectedBookDtoList);

    List<BookDTO> books = bookService.findBooks(input);
    assertEquals(expectedBookDtoList, books);
  }

  @Test
  void addBook_BookAlreadyExistsByTitleAndAuthor() {
    List<Book> testBookList = List.of(TEST_BOOK_1, TEST_BOOK_2);

    when(bookRepository.findByTitleAndAuthorFirstNameAndAuthorLastName(TEST_BOOKDTO_1.getTitle(),
        TEST_BOOKDTO_1.getAuthorFirstName(),
        TEST_BOOKDTO_1.getAuthorLastName())).thenReturn(testBookList);

    BookAlreadyExistsException bookAlreadyExistsException = assertThrows(
        BookAlreadyExistsException.class,
        () -> bookService.addBook(TEST_BOOKDTO_1));
    assertEquals("Book already exists with title: Animal Farm and author: George Orwell",
        bookAlreadyExistsException.getMessage());
  }

  @Test
  void addBook_DoesNotAlreadyExist() {
    when(bookRepository.findByTitleAndAuthorFirstNameAndAuthorLastName(TEST_BOOKDTO_3.getTitle(),
        TEST_BOOKDTO_3.getAuthorFirstName(), TEST_BOOKDTO_3.getAuthorLastName())).thenReturn(
        Collections.emptyList());

    BookDTO returnedBook = bookService.addBook(TEST_BOOKDTO_3);
    verify(bookRepository).save(bookConverter.convertToBook(TEST_BOOKDTO_3));
    assertEquals(TEST_BOOKDTO_3, returnedBook);
  }

  @Test
  void removeBook_Exists() {
    when(bookRepository.findByIsbn(ISBN_1)).thenReturn(TEST_BOOK_1);

    bookService.removeBook(ISBN_1);

    verify(bookRepository).deleteById(TEST_BOOK_1.getId());
  }

  @Test
  void removeBook_DoesNotExist() {
    when(bookRepository.findByIsbn(ISBN_1)).thenReturn(null);

    bookService.removeBook(ISBN_1);

    verifyNoMoreInteractions(bookRepository);
  }

}