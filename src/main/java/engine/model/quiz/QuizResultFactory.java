package engine.model.quiz;

public class QuizResultFactory {

    public static QuizResult buildQuizResult(boolean isSuccess) {
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
