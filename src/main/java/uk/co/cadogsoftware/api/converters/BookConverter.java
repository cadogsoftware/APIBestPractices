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
    Book book = new Book();
    book.setAuthor(bookDTO.getAuthor());
    book.setTitle(bookDTO.getTitle());
    return book;
  }

  public BookDTO convertToBookDTO(Book book) {
    BookDTO bookDTO = new BookDTO();
    bookDTO.setAuthor(book.getAuthor());
    bookDTO.setTitle(book.getTitle());
    return bookDTO;
  }

  public List<BookDTO> convertToBookDTOList(List<Book> books) {
    return books.stream().map(this::convertToBookDTO).toList();
  }

  public List<Book> convertToBookList(List<BookDTO> bookDtos) {
    return bookDtos.stream().map(this::convertToBook).toList();
  }

}
