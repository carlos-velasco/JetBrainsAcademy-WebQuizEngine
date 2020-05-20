package engine.service.quiz;

public class QuizNotFoundException extends RuntimeException {

    public QuizNotFoundException(Long quizId) {
        super(String.format("Quiz with id %d not found", quizId));
    }
}
