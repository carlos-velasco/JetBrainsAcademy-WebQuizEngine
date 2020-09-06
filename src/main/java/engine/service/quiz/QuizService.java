package engine.service.quiz;

import engine.model.quiz.*;
import engine.model.user.User;
import engine.repository.QuizCompletionRepository;
import engine.repository.QuizRepository;
import engine.repository.UserRepository;
import engine.security.AuthenticationFacade;
import engine.service.user.QuizUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Service("quizService")
@RequiredArgsConstructor
@Validated
public class QuizService {

    static final int PAGE_SIZE = 10;

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizCompletionRepository quizCompletionRepository;
    private final AuthenticationFacade authenticationFacade;

    public Quiz getQuiz(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId));
    }

    public Page<Quiz> getQuizzes(Long pageNumber) {
        Pageable paging = PageRequest.of(pageNumber.intValue(), PAGE_SIZE, Sort.by("id"));
        return quizRepository.findAll(paging);
    }

    public Page<QuizCompletion> findQuizCompletionsByUser(Long pageNumber) {
        Pageable paging = PageRequest.of(pageNumber.intValue(), PAGE_SIZE, Sort.by("completedAt").descending());
        return quizCompletionRepository.findAllByUser(getLoggedInUser(), paging);
    }

    public QuizResult solveQuiz(Long quizId, QuizAnswer quizAnswer) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId));
        boolean solvedSuccessfully = quiz.checkAnswer(quizAnswer);

        if (solvedSuccessfully) {
            QuizCompletion quizCompletion = QuizCompletion.builder()
                    .quiz(quiz)
                    .user(getLoggedInUser())
                    .completedAt(LocalDateTime.now())
                    .build();
            quizCompletionRepository.save(quizCompletion);
        }
        return QuizResultFactory.buildQuizResult(solvedSuccessfully);
    }

    public Quiz createQuiz(@Valid Quiz quiz) {
        quiz.setUser(getLoggedInUser());
        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId));

        String currentUserEmail = authenticationFacade.getAuthentication().getName();
        if (!currentUserEmail.equals(quiz.getUser().getEmail())) {
            throw new QuizNotOwnedByUserException(quizId, currentUserEmail);
        }
        quizRepository.deleteById(quizId);
    }

    private User getLoggedInUser() {
        String currentUserEmail = authenticationFacade.getAuthentication().getName();
        User user = userRepository.findByEmail(currentUserEmail);
        if (user == null) {
            throw  new QuizUserNotFoundException(currentUserEmail);
        }
        return user;
    }
}
