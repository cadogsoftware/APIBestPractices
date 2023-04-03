package uk.co.cadogsoftware.api.database.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.cadogsoftware.api.database.entities.Book;

/**
 * Used to interact with Book entries in the database.
 */
public interface BookRepository extends JpaRepository<Book, String> {

  Book findByIsbn(String isbn);
  List<Book> findByTitleContaining(String titleToMatch);

  void deleteByIsbn(String isbn);

  List<Book> findByTitleAndAuthor(String title, String author);

}
