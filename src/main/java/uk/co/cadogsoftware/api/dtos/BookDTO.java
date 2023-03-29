package uk.co.cadogsoftware.api.dtos;

import java.util.Objects;

/**
 * Details of the book(s) exposed to the end clients.
 *
 */
public class BookDTO {

  private String title;
  private String author;

  public BookDTO() {
  }

  public BookDTO(String title, String author) {

    this.title = title;
    this.author = author;
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
    if (!(o instanceof BookDTO)) {
      return false;
    }
    BookDTO book = (BookDTO) o;
    return Objects.equals(this.title, book.title)
        && Objects.equals(this.author, book.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.title, this.author);
  }

  @Override
  public String toString() {
    return "BookDTO{name='" + this.title + '\'' + ", role='" + this.author
        + '\'' + '}';
  }
}