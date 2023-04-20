package uk.co.cadogsoftware.api.converters;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.co.cadogsoftware.api.database.entities.Book;
import uk.co.cadogsoftware.api.dtos.BookDTO;

/**
 * Converts {@link BookDTO}s to {@link Book}s and vice versa.
 *
 */
@Service
public class BookConverter {

  public Book convertToBook(BookDTO bookDTO) {
    // Create the book from the new structure.
    Book book = new Book(bookDTO.getAuthorFirstName(), bookDTO.getAuthorLastName(),
        bookDTO.getIsbn(), bookDTO.getTitle());

    // If the old format has been passed in use that.
    if (StringUtils.hasText(bookDTO.getAuthor())) {
      book.setAuthor(bookDTO.getAuthor());
    }

    return book;
  }

  public BookDTO convertToBookDTO(Book book) {
    return new BookDTO(book.getAuthor(), book.getAuthorFirstName(), book.getAuthorLastName(),
        book.getIsbn(), book.getTitle());
  }

  public List<BookDTO> convertToBookDTOList(List<Book> books) {
    return books.stream().map(this::convertToBookDTO).toList();
  }

}
