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
import uk.co.cadogsoftware.api.database.repositories.BookRepository;
import uk.co.cadogsoftware.api.dtos.BookDTO;
import uk.co.cadogsoftware.api.exceptions.BookAlreadyExistsException;
import uk.co.cadogsoftware.api.exceptions.BookIdAlreadyInUseException;
import uk.co.cadogsoftware.api.exceptions.BookNotFoundException;

/**
 * Test class for {@link BookService}.
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

  private static final BookDTO TEST_BOOK_1 = new BookDTO("Animal Farm", "George Orwell");
  private static final BookDTO TEST_BOOK_2 = new BookDTO("Farming World", "Tom Smith");
  private static final BookDTO TEST_BOOK_3 = new BookDTO("Happy Days", "The Fonz");
  private static final Long BOOK_ID = 1L;

  @InjectMocks
  private BookService bookService;

  @Mock
  private BookRepository bookRepository;

  @Test
  void getBook_ReturnsCorrectBook() {
    when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(TEST_BOOK_1));

    BookDTO bookDTO = bookService.getBook(BOOK_ID);

    assertEquals(TEST_BOOK_1, bookDTO);
  }

  @Test
  void getBook_NotFound() {
    Long bookIdNotFound = 999L;
    when(bookRepository.findById(bookIdNotFound)).thenReturn(Optional.empty());

    BookNotFoundException bookNotFoundException = assertThrows(BookNotFoundException.class,
        () -> bookService.getBook(bookIdNotFound));

    assertEquals("Book cannot be found for id: " + bookIdNotFound, bookNotFoundException.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " "})
  @NullSource
    // Tests for a null input. You can't do this in the @ValueSource annotation.
    //@NullAndEmptySource // This tests for null and "" but not a String containing all spaces.
  void getBooks_NoTitle(String input) {
    List<BookDTO> expectedBookList = List.of(TEST_BOOK_1, TEST_BOOK_2);

    when(bookRepository.findAll()).thenReturn(expectedBookList);

    List<BookDTO> books = bookService.getBooks(input);
    assertEquals(expectedBookList, books);
  }

  @ParameterizedTest
  @ValueSource(strings = {"Farm", "   Farm", "  Farm     "})
  @DisplayName("Tests for books with the input in the title. Also tests the trimming of the input.")
  void getBooks_WithTitle(String input) {
    List<BookDTO> expectedBookList = List.of(TEST_BOOK_1, TEST_BOOK_2);

    when(bookRepository.findAll()).thenReturn(expectedBookList);

    List<BookDTO> books = bookService.getBooks(input);
    assertEquals(expectedBookList, books);
  }

  @Test
  void addBook_BookAlreadyExistsById() {
    TEST_BOOK_1.setId(BOOK_ID);
    when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(TEST_BOOK_1));

    BookIdAlreadyInUseException bookIdAlreadyInUseException = assertThrows(BookIdAlreadyInUseException.class,
        () -> bookService.addBook(TEST_BOOK_1));
    assertEquals("Book id is already in use: " + BOOK_ID, bookIdAlreadyInUseException.getMessage());
  }

  @Test
  void addBook_BookAlreadyExistsByTitleAndAuthor() {
    List<BookDTO> testBookList = List.of(TEST_BOOK_1, TEST_BOOK_2);

    when(bookRepository.findAll()).thenReturn(testBookList);

    BookAlreadyExistsException bookAlreadyExistsException = assertThrows(BookAlreadyExistsException.class,
        () -> bookService.addBook(TEST_BOOK_1));
    assertEquals("Book already exists: " + TEST_BOOK_1, bookAlreadyExistsException.getMessage());
  }

  @Test
  void addBook() {
    List<BookDTO> testBookList = List.of(TEST_BOOK_1, TEST_BOOK_2);

    when(bookRepository.findAll()).thenReturn(testBookList);

    BookDTO returnedBook = bookService.addBook(TEST_BOOK_3);
    verify(bookRepository).save(TEST_BOOK_3);
    assertEquals(TEST_BOOK_3, returnedBook);
  }

  @Test
  void removeBook() {
    bookService.removeBook(BOOK_ID);

    verify(bookRepository).deleteById(BOOK_ID);
  }

}