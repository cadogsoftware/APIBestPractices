package uk.co.cadogsoftware.api.converters;

import java.util.List;
import org.springframework.stereotype.Service;
import uk.co.cadogsoftware.api.database.entities.Book;
import uk.co.cadogsoftware.api.dtos.BookDTO;

/**
 * Converts {@link BookDTO}s to {@link Book}s and vice versa.
 *
 */
@Service
public class BookConverter {

  public Book convertToBook(BookDTO bookDTO) {
    return new Book(bookDTO.author(), bookDTO.isbn(), bookDTO.title());
  }

  public BookDTO convertToBookDTO(Book book) {
    return new BookDTO(book.getAuthor(), book.getIsbn(), book.getTitle());
  }

  public List<BookDTO> convertToBookDTOList(List<Book> books) {
    return books.stream().map(this::convertToBookDTO).toList();
  }

  public List<Book> convertToBookList(List<BookDTO> bookDtos) {
    return bookDtos.stream().map(this::convertToBook).toList();
  }

}
