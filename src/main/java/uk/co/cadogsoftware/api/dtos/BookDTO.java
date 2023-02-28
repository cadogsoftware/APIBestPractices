package uk.co.cadogsoftware.api.dtos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class BookDTO {

  private @Id @GeneratedValue Long id;
  private String title;
  private String author;

  public BookDTO() {}

  public BookDTO(String title, String author) {

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

  public void setId(Long id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (!(o instanceof BookDTO))
      return false;
    BookDTO employee = (BookDTO) o;
    return Objects.equals(this.id, employee.id) && Objects.equals(this.title, employee.title)
        && Objects.equals(this.author, employee.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.title, this.author);
  }

  @Override
  public String toString() {
    return "BookDTO{" + "id=" + this.id + ", name='" + this.title + '\'' + ", role='" + this.author
        + '\'' + '}';
  }
}