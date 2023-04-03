package uk.co.cadogsoftware.api.database.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Represents the Book in the database.
 */
@Entity
@EqualsAndHashCode
@ToString
public class Book {

  private @Id
  @GeneratedValue Long id;
  private String author;
  private String isbn;
  private String title;

  public Book() {
  }

  public Book(String author, String isbn, String title) {
    this.author = author;
    this.isbn = isbn;
    this.title = title;
  }

  public Long getId() {
    return id;
  }

  public String getAuthor() {
    return author;
  }

  public String getIsbn() {
    return isbn;
  }

  public String getTitle() {
    return title;
  }

}
