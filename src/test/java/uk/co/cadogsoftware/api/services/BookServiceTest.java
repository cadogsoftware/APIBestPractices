package uk.co.cadogsoftware.api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

  private static final String AUTHOR_1 = "George Orwell";
  private static final String AUTHOR_2 = "Tom Smith";
  private static final String AUTHOR_3 = "The Fonz";

  private static final String ISBN_1 = "123";
  private static final String ISBN_2 = "456";
  private static final String ISBN_3 = "789";

  private static final String TITLE_1 = "Animal Farm";
  private static final String TITLE_2 = "Farming World";
  private static final String TITLE_3 = "Happy Days";

  private static final BookDTO TEST_BOOKDTO_1 = new BookDTO(AUTHOR_1, ISBN_1, TITLE_1);
  private static final BookDTO TEST_BOOKDTO_2 = new BookDTO(AUTHOR_2, ISBN_2, TITLE_2);
  private static final BookDTO TEST_BOOKDTO_3 = new BookDTO(AUTHOR_3, ISBN_3, TITLE_3);
  private static final Book TEST_BOOK_1 = new Book(AUTHOR_1, ISBN_1, TITLE_1);
  private static final Book TEST_BOOK_2 = new Book(AUTHOR_2, ISBN_2, TITLE_2);

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
    List<BookDTO> testBookDtoList = List.of(TEST_BOOKDTO_1, TEST_BOOKDTO_2);

    when(bookRepository.findByTitleAndAuthor(TEST_BOOKDTO_1.title(),
        TEST_BOOKDTO_1.author())).thenReturn(testBookList);

    BookAlreadyExistsException bookAlreadyExistsException = assertThrows(
        BookAlreadyExistsException.class,
        () -> bookService.addBook(TEST_BOOKDTO_1));
    assertEquals("Book already exists for title: Animal Farm and author: George Orwell",
        bookAlreadyExistsException.getMessage());
  }

  @Test
  void addBook_DoesNotAlreadyExist() {
    List<Book> testBookList = List.of(TEST_BOOK_1, TEST_BOOK_2);

    when(bookRepository.findByTitleAndAuthor(TEST_BOOKDTO_3.title(),
        TEST_BOOKDTO_3.author())).thenReturn(Collections.emptyList());

    BookDTO returnedBook = bookService.addBook(TEST_BOOKDTO_3);
    verify(bookRepository).save(bookConverter.convertToBook(TEST_BOOKDTO_3));
    assertEquals(TEST_BOOKDTO_3, returnedBook);
  }

  @Test
  void removeBook() {
    bookService.removeBook(ISBN_1);

    verify(bookRepository).deleteByIsbn(ISBN_1);
  }

}