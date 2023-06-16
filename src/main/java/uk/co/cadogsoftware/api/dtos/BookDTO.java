package uk.co.cadogsoftware.api.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import uk.co.cadogsoftware.api.validators.BookValidation;

/**
 * Details of the book(s) exposed to the end clients.
 */
@Data
@BookValidation
public class BookDTO {

    private final String author; // Left here to support backwards compatibility.

    private final String authorFirstName; // Introduced in version 2 of the API.

    private final String authorLastName; // Introduced in version 2 of the API.

    @NotEmpty(message = "isbn must be provided")
    private final String isbn;

    @NotEmpty(message = "title must be provided")
    private final String title;

}