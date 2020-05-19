package engine.model.quiz;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AnswersInOptionsValidator implements ConstraintValidator<AnswersInOptions, Quiz> {

    @Override
    public boolean isValid(Quiz quiz, ConstraintValidatorContext context) {
        if (quiz.getAnswer() ==  null || quiz.getOptions() == null) return true;

        boolean isValid = quiz.getAnswer().stream()
                .noneMatch(answer -> answer > quiz.getOptions().size() - 1);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("answer")
                    .addConstraintViolation();
        }
        return isValid;
    }
}
