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
  private String authorFirstName;

  @Getter
  private String authorLastName;
  @Getter
  private String isbn;
  @Getter
  private String title;

  public String getAuthor() {
    return this.authorFirstName + " " + this.authorLastName;
  }

  public void setAuthor(String author) {
    String[] parts = author.split(" ");
    this.authorFirstName = parts[0];
    this.authorLastName = parts[1];
  }

  public Book(String authorFirstName, String authorLastName, String isbn, String title) {
    this.authorFirstName = authorFirstName;
    this.authorLastName = authorLastName;
    this.isbn = isbn;
    this.title = title;
  }

}
