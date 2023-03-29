package uk.co.cadogsoftware.api.database.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.Objects;

/**
 * Represents the Book in the database.
 */
@Entity
public class Book {

  private @Id
  @GeneratedValue Long id;
  private String title;
  private String author;

  public Book() {
  }

  public Book(String title, String author) {

    this.title = title;
    this.author = author;
  }

  public Long getId() {
    return this.id;
  }

  public String getTitle() {
    return this.title;
  }

  public String getAuthor() {
    return this.author;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (!(o instanceof Book)) {
      return false;
    }
    Book book = (Book) o;
    return Objects.equals(this.id, book.id) && Objects.equals(this.title, book.title)
        && Objects.equals(this.author, book.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.title, this.author);
  }

  @Override
  public String toString() {
    return "Book{" + "id=" + this.id + ", title='" + this.title + '\'' + ", author='" + this.author
        + '\'' + '}';
  }

}
