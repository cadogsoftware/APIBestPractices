package uk.co.cadogsoftware.api.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import uk.co.cadogsoftware.api.validators.BookValidation;

/**
 * Details of the book(s) exposed to the end clients.
 */
@RequiredArgsConstructor
@BookValidation
@EqualsAndHashCode
public class BookDTO {

    private final String author; // Left here to support backwards compatibility.

    private final String authorFirstName; // Introduced in version 2 of the API.

    private final String authorLastName; // Introduced in version 2 of the API.

    @Getter
    @NotEmpty(message = "isbn must be provided")
    private final String isbn;

    @Getter
    @NotEmpty(message = "title must be provided")
    private final String title;

    /**
     * If we have an author return it, otherwise get if from first and last names.
     * Added for backwards compatibility.
     * @return the author.
     */
    public String getAuthor() {
        String authorToReturn = "";
        if (StringUtils.hasText(author)) {
            authorToReturn = author;
        } else if (StringUtils.hasText(authorFirstName) && StringUtils.hasText(authorLastName)) {
            authorToReturn = authorFirstName + " " + authorLastName;
        }
        return authorToReturn;
    }

    /**
     * If we have an author first name return it, otherwise get if from author.
     * Added for backwards compatibility.
     * @return the author first name.
     */
    public String getAuthorFirstName() {
        String authorFirstNameToReturn = "";
        if (StringUtils.hasText(authorFirstName)) {
            authorFirstNameToReturn = authorFirstName;
        } else if (StringUtils.hasText(author)) {
            String[] parts = author.split(" ");
            if (parts.length > 0) {
                authorFirstNameToReturn = parts[0];
            }
        }
        return authorFirstNameToReturn;
    }

    /**
     * If we have an author last name return it, otherwise get if from author.
     * Added for backwards compatibility.
     * @return the author last name.
     */
    public String getAuthorLastName() {
        String authorLastNameToReturn = "";
        if (StringUtils.hasText(authorLastName)) {
            authorLastNameToReturn = authorLastName;
        } else if (StringUtils.hasText(author)) {
            String[] parts = author.split(" ");
            if (parts.length > 1) {
                authorLastNameToReturn = parts[1];
            }
        }
        return authorLastNameToReturn;
    }

}