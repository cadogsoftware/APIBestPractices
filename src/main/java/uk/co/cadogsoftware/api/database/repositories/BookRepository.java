package uk.co.cadogsoftware.api.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.cadogsoftware.api.database.entities.Book;

/**
 * Used to interact with Book entries in the database.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

}
