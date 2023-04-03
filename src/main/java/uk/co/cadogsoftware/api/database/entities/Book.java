package uk.co.cadogsoftware.api.database.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents the Book in the database.
 */
@Entity
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Book {

  @Getter
  private @Id @GeneratedValue Long id;
  @Getter
  private String author;
  @Getter
  private String isbn;
  @Getter
  private String title;

  public Book(String author, String isbn, String title) {
    this.author = author;
    this.isbn = isbn;
    this.title = title;
  }

}
