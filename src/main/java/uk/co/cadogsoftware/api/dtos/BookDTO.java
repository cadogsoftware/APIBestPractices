package uk.co.cadogsoftware.api.dtos;

import jakarta.validation.constraints.NotEmpty;

/**
 * Details of the book(s) exposed to the end clients.
 */
public record BookDTO(
    @NotEmpty(message = "author must be provided") String author,
    @NotEmpty(message = "isbn must be provided") String isbn,
    @NotEmpty(message = "title must be provided") String title) {

}