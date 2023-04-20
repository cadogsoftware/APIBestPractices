package uk.co.cadogsoftware.api.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BookValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BookValidation {

  String message() default "Author is required";

  String author() default "author";

  String authorFirstName() default "authorFirstName";

  String authorLastName() default "authorLastName";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
