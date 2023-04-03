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
      log.info("Preloading " + repository.save(new Book("George Orwell", "1-2-3", "Animal Farm")));
      log.info("Preloading " + repository.save(new Book("J. R. R. Tolkien", "4-5-6", "The Lord of the Rings")));
      log.info("Preloading " + repository.save(new Book("Harper Lee", "7-8-9","To Kill a Mockingbird")));
    };
  }
}
