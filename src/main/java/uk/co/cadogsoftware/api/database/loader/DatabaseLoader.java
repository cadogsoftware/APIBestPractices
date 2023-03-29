package uk.co.cadogsoftware.api.database.loader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.cadogsoftware.api.database.entities.Book;
import uk.co.cadogsoftware.api.database.repositories.BookRepository;

/**
 * Load the database entries on startup.
 */
@Configuration
@Slf4j
public class DatabaseLoader {

  @Bean
  CommandLineRunner initDatabase(BookRepository repository) {

    return args -> {
      log.info("Preloading " + repository.save(new Book("George Orwell", "Animal Farm")));
      log.info("Preloading " + repository.save(new Book("The Lord of the Rings", "J. R. R. Tolkien")));
      log.info("Preloading " + repository.save(new Book("To Kill a Mockingbird", "Harper Lee")));
    };
  }
}
