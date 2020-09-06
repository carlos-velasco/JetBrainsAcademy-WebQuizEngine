package engine.service.user;

public class QuizUserNotFoundException extends RuntimeException {

    public QuizUserNotFoundException(String userEmail) {
        super(String.format("User with email %s not found", userEmail));
    }
}
