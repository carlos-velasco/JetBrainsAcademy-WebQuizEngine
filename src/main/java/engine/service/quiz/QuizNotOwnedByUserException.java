package engine.service.quiz;

public class QuizNotOwnedByUserException extends RuntimeException {

    public QuizNotOwnedByUserException(Long quizId, String userEmail) {
        super(String.format("User %s is not the owner of quiz %d", userEmail, quizId));
    }
}
