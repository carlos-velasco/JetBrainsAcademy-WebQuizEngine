package engine.model.quiz;

import lombok.experimental.UtilityClass;

@UtilityClass
public class QuizResultFactory {

    public QuizResult buildQuizResult(boolean isSuccess) {
        QuizResult.QuizResultBuilder builder = QuizResult.builder()
                .isSuccess(isSuccess);

        if (isSuccess) {
            return builder.feedback("Congratulations, you're right!")
                    .build();
        }
        return builder.feedback("Wrong answer! Please, try again.")
                .build();
    }
}
