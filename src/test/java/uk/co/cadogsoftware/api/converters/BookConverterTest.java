package uk.co.cadogsoftware.api.converters;

import static org.junit.jupiter.api.Assertions.*;
import static uk.co.cadogsoftware.api.testutils.TestFixtures.TEST_BOOKDTO_1;
import static uk.co.cadogsoftware.api.testutils.TestFixtures.TEST_BOOKDTO_2;
import static uk.co.cadogsoftware.api.testutils.TestFixtures.TEST_BOOK_1;
import static uk.co.cadogsoftware.api.testutils.TestFixtures.TEST_BOOK_2;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.cadogsoftware.api.database.entities.Book;
import uk.co.cadogsoftware.api.dtos.BookDTO;

/**
 * Test class for {@link BookConverter}.
 */
@RequiredArgsConstructor
class BookConverterTest {

  private final BookConverter bookConverter = new BookConverter();

  @Test
  void convertToBook() {
    Book book = bookConverter.convertToBook(TEST_BOOKDTO_1);
    assertNotNull(book);
    assertEquals(TEST_BOOKDTO_1.author(), book.getAuthor());
    assertEquals(TEST_BOOKDTO_1.title(), book.getTitle());
    assertEquals(TEST_BOOKDTO_1.isbn(), book.getIsbn());
  }

  @Test
  void convertToBookDTO() {
    BookDTO bookDto = bookConverter.convertToBookDTO(TEST_BOOK_1);
    assertNotNull(bookDto);
    assertEquals(TEST_BOOKDTO_1, bookDto);
  }

  @Test
  void convertToBookDTOList() {
    List<BookDTO> bookDTOList = bookConverter.convertToBookDTOList(List.of(TEST_BOOK_1, TEST_BOOK_2));
    assertNotNull(bookDTOList);
    assertEquals(2, bookDTOList.size());

    BookDTO bookDto1 = bookDTOList.get(0);
    assertNotNull(bookDto1);
    assertEquals(TEST_BOOKDTO_1, bookDto1);

    BookDTO bookDto2 = bookDTOList.get(1);
    assertNotNull(bookDto2);
    assertEquals(TEST_BOOKDTO_2, bookDto2);
  }
}