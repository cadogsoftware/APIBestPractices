package uk.co.cadogsoftware.api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.co.cadogsoftware.api.dtos.BookDTO;
import uk.co.cadogsoftware.api.exceptions.BookNotFoundException;
import uk.co.cadogsoftware.api.exceptions.TooManyBooksFoundException;

/**
 * Test class for {@link BookService}.
 */
class BookServiceTest {

  /*
  Store this here for now. It will change when we have methods to add and remove books.
   */
  private static final List<BookDTO> EXPECTED_ALL_BOOKS = Collections.synchronizedList(List.of(
      new BookDTO(1, "Animal Farm", "George Orwell"),
      new BookDTO(2, "1984", "George Orwell"),
      new BookDTO(3, "Harper Lee", "To Kill a Mockingbird"),
      new BookDTO(3, "Harper Lee", "DUPLICATE ID - To Kill a Mockingbird"),
      new BookDTO(4, "Farming World", "Tom Smith")));

  private BookService bookService;

  @BeforeEach
  public void setup() {
    bookService = new BookService();
  }

  @Test
  void getBook_ReturnsCorrectBook() {
    BookDTO expectedBookDTO = new BookDTO(1, "Animal Farm", "George Orwell");
    BookDTO bookDTO = bookService.getBook(1);

    assertEquals(expectedBookDTO, bookDTO);
  }

  @Test
  void getBook_NotFound() {
    int bookId = 999;
    BookNotFoundException bookNotFoundException = assertThrows(BookNotFoundException.class,
        () -> bookService.getBook(bookId));
    assertEquals("Book cannot be found for id: " + bookId, bookNotFoundException.getMessage());
  }

  @Test
  void getBook_MultipleFound() {
    int bookId = 3;
    TooManyBooksFoundException tooManyBooksFoundException = assertThrows(TooManyBooksFoundException.class,
        () -> bookService.getBook(bookId));
    assertEquals("Too many books found for id: " + bookId, tooManyBooksFoundException.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " "})
  @NullSource // Tests for a null input. You can't do this in the @ValueSource annotation.
  //@NullAndEmptySource // This tests for null and "" but not a String containing all spaces.
  void getBooks_NoTitle(String input) {
    List<BookDTO> books = bookService.getBooks(input);
    assertEquals(EXPECTED_ALL_BOOKS, books);
  }

  @ParameterizedTest
  @ValueSource(strings = {"Farm", "   Farm", "  Farm     "})
  @DisplayName("Tests for books with the input in the title. Also tests the trimming of the input.")
  void getBooks_WithTitle(String input) {
    BookDTO expectedBook1  = new BookDTO(1, "Animal Farm", "George Orwell");
    BookDTO expectedBook2 = new BookDTO(4, "Farming World", "Tom Smith");
    List<BookDTO> expectedBookList = List.of(expectedBook1, expectedBook2);

    List<BookDTO> books = bookService.getBooks(input);
    assertEquals(expectedBookList, books);
  }

}