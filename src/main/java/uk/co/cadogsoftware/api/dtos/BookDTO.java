package uk.co.cadogsoftware.api.dtos;

/**
 * Details of the book(s) exposed to the end clients.
 */
public record BookDTO(String author, String isbn, String title) {

}