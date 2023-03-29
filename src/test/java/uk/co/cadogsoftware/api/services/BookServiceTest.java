package uk.co.cadogsoftware.api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
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

  private static final BookDTO TEST_BOOKDTO_1 = new BookDTO("Animal Farm", "George Orwell");
  private static final BookDTO TEST_BOOKDTO_2 = new BookDTO("Farming World", "Tom Smith");
  private static final BookDTO TEST_BOOKDTO_3 = new BookDTO("Happy Days", "The Fonz");

  private static final Book TEST_BOOK_1 = new Book("Animal Farm", "George Orwell");
  private static final Book TEST_BOOK_2 = new Book("Farming World", "Tom Smith");
  private static final Long BOOK_ID = 1L;

  @InjectMocks
  private BookService bookService;

  @Mock
  private BookRepository bookRepository;

  @Mock
  private BookConverter bookConverter;

  @Test
  void getBook_ReturnsCorrectBook() {
    when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(TEST_BOOK_1));
    when(bookConverter.convertToBookDTO(TEST_BOOK_1)).thenReturn(TEST_BOOKDTO_1);

    BookDTO bookDTO = bookService.getBook(BOOK_ID);

    assertEquals(TEST_BOOKDTO_1, bookDTO);
  }

  @Test
  void getBook_NotFound() {
    Long bookIdNotFound = 999L;
    when(bookRepository.findById(bookIdNotFound)).thenReturn(Optional.empty());

    BookNotFoundException bookNotFoundException = assertThrows(BookNotFoundException.class,
        () -> bookService.getBook(bookIdNotFound));

    assertEquals("Book cannot be found for id: " + bookIdNotFound,
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

    List<BookDTO> books = bookService.getBooks(input);
    assertEquals(expectedBookDtoList, books);
  }

  @ParameterizedTest
  @ValueSource(strings = {"Farm", "   Farm", "  Farm     "})
  @DisplayName("Tests for books with the input in the title. Also tests the trimming of the input.")
  void getBooks_WithTitle(String input) {
    List<Book> expectedBookList = List.of(TEST_BOOK_1, TEST_BOOK_2);
    List<BookDTO> expectedBookDtoList = List.of(TEST_BOOKDTO_1, TEST_BOOKDTO_2);

    when(bookRepository.findAll()).thenReturn(expectedBookList);
    when(bookConverter.convertToBookDTOList(expectedBookList)).thenReturn(expectedBookDtoList);

    List<BookDTO> books = bookService.getBooks(input);
    assertEquals(expectedBookDtoList, books);
  }

  @Test
  void addBook_BookAlreadyExistsByTitleAndAuthor() {
    List<Book> testBookList = List.of(TEST_BOOK_1, TEST_BOOK_2);
    List<BookDTO> testBookDtoList = List.of(TEST_BOOKDTO_1, TEST_BOOKDTO_2);

    when(bookRepository.findAll()).thenReturn(testBookList);
    when(bookConverter.convertToBookDTOList(testBookList)).thenReturn(testBookDtoList);

    BookAlreadyExistsException bookAlreadyExistsException = assertThrows(
        BookAlreadyExistsException.class,
        () -> bookService.addBook(TEST_BOOKDTO_1));
    assertEquals("Book already exists: " + TEST_BOOKDTO_1, bookAlreadyExistsException.getMessage());
  }

  @Test
  void addBook() {
    List<Book> testBookList = List.of(TEST_BOOK_1, TEST_BOOK_2);

    when(bookRepository.findAll()).thenReturn(testBookList);

    BookDTO returnedBook = bookService.addBook(TEST_BOOKDTO_3);
    verify(bookRepository).save(bookConverter.convertToBook(TEST_BOOKDTO_3));
    assertEquals(TEST_BOOKDTO_3, returnedBook);
  }

  @Test
  void removeBook() {
    bookService.removeBook(BOOK_ID);

    verify(bookRepository).deleteById(BOOK_ID);
  }

}