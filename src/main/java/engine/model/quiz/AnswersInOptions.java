package engine.model.quiz;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AnswersInOptionsValidator.class)
@Documented
public @interface AnswersInOptions {
    String message() default "{AnswersMatchOptions.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
