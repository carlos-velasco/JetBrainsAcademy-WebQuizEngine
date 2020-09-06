package engine.service.quiz;

import engine.model.quiz.*;
import engine.model.user.User;
import engine.repository.QuizCompletionRepository;
import engine.repository.QuizRepository;
import engine.repository.UserRepository;
import engine.security.AuthenticationFacade;
import engine.service.user.QuizUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Service("quizService")
@RequiredArgsConstructor
@Validated
public class QuizService {

    @Value("${page-size}")
    private int pageSize = 10;

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizCompletionRepository quizCompletionRepository;
    private final AuthenticationFacade authenticationFacade;

    public Quiz getQuiz(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId));
    }

    public Page<Quiz> getQuizzes(Long pageNumber) {
        Pageable paging = PageRequest.of(pageNumber.intValue(), pageSize, Sort.by("id"));
        return quizRepository.findAll(paging);
    }

    public Page<QuizCompletion> findQuizCompletionsByUser(Long pageNumber) {
        Pageable paging = PageRequest.of(pageNumber.intValue(), pageSize, Sort.by("completedAt").descending());
        return quizCompletionRepository.findAllByUser(findCurrentlyLoggedInUser(), paging);
    }

    public QuizResult solveQuiz(Long quizId, QuizAnswer quizAnswer) {
        Quiz quiz = getQuiz(quizId);
        boolean solvedSuccessfully = quiz.checkAnswer(quizAnswer);

        if (solvedSuccessfully) {
            QuizCompletion quizCompletion = QuizCompletion.builder()
                    .quiz(quiz)
                    .user(findCurrentlyLoggedInUser())
                    .completedAt(LocalDateTime.now())
                    .build();
            quizCompletionRepository.save(quizCompletion);
        }
        return QuizResultFactory.buildQuizResult(solvedSuccessfully);
    }

    public Quiz createQuiz(@Valid Quiz quiz) {
        quiz.setUser(findCurrentlyLoggedInUser());
        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long quizId) {
        Quiz quiz = getQuiz(quizId);
        String loggedInUserEmail = getLoggedInUserEmail();

        if (!loggedInUserEmail.equals(quiz.getUser().getEmail())) {
            throw new QuizNotOwnedByUserException(quizId, loggedInUserEmail);
        }
        quizRepository.deleteById(quizId);
    }

    private String getLoggedInUserEmail() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return authentication.getName();
    }

    private User findCurrentlyLoggedInUser() {
        String loggedInUserEmail = getLoggedInUserEmail();
        return userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new QuizUserNotFoundException(loggedInUserEmail));
    }
}
