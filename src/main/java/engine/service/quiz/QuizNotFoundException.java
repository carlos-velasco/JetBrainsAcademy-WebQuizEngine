package engine.service.quiz;

public class QuizNotFoundException extends RuntimeException {

    public QuizNotFoundException() {
        super();
    }

    public QuizNotFoundException(Long quizId) {
        super(String.format("Quiz with id %d not found", quizId));
    }

    public QuizNotFoundException(Long quizId, Throwable cause) {
        super(String.format("Quiz with id %d not found", quizId), cause);
    }
}
