package uk.co.cadogsoftware.api.database.loader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.cadogsoftware.api.database.repositories.BookRepository;
import uk.co.cadogsoftware.api.dtos.BookDTO;

@Configuration
@Slf4j
public class DatabaseLoader {

  @Bean
  CommandLineRunner initDatabase(BookRepository repository) {

    return args -> {
      log.info("Preloading " + repository.save(new BookDTO("Bilbo Baggins", "burglar")));
      log.info("Preloading " + repository.save(new BookDTO("Frodo Baggins", "thief")));
    };
  }
}
