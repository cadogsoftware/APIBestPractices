package uk.co.cadogsoftware.api.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.co.cadogsoftware.api.dtos.BookDTO;

public interface BookRepository extends JpaRepository<BookDTO, Long> {

}
