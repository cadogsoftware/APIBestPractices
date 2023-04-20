package uk.co.cadogsoftware.api.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class BookValidator
    implements ConstraintValidator<BookValidation, Object> {

  private String author;
  private String authorFirstName;
  private String authorLastName;

  @Override
  public void initialize(BookValidation constraintAnnotation) {
    this.author = constraintAnnotation.author();
    this.authorFirstName = constraintAnnotation.authorFirstName();
    this.authorLastName = constraintAnnotation.authorLastName();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {

    boolean valid = false;

    String authorValue = (String) new BeanWrapperImpl(value)
        .getPropertyValue(author);
    String authorFirstNameValue = (String) new BeanWrapperImpl(value)
        .getPropertyValue(authorFirstName);
    String authorLastNameValue = (String) new BeanWrapperImpl(value)
        .getPropertyValue(authorLastName);

    if (StringUtils.hasText(authorValue) ||
        (StringUtils.hasText(authorFirstNameValue) && StringUtils.hasText(authorLastNameValue))) {
      valid = true;
    }
    return valid;
  }
}
