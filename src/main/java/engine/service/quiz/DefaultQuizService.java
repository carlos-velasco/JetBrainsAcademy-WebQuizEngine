package engine.service.quiz;

import engine.model.quiz.*;
import engine.model.user.User;
import engine.repository.QuizCompletionRepository;
import engine.repository.QuizRepository;
import engine.repository.UserRepository;
import engine.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Service("quizService")
@Validated
public class DefaultQuizService implements QuizService {

    static int PAGE_SIZE = 10;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizCompletionRepository quizCompletionRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public Quiz getQuiz(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId));
    }

    @Override
    public Page<Quiz> getQuizzes(Long pageNumber) {
        Pageable paging = PageRequest.of(pageNumber.intValue(), PAGE_SIZE, Sort.by("id"));
        return quizRepository.findAll(paging);
    }

    @Override
    public Page<QuizCompletion> findQuizCompletionsByUser(Long pageNumber) {
        Pageable paging = PageRequest.of(pageNumber.intValue(), PAGE_SIZE, Sort.by("completedAt").descending());
        return quizCompletionRepository.findAllByUser(getLoggedInUser(), paging);
    }

    @Override
    public QuizResult solveQuiz(Long quizId, QuizAnswer quizAnswer) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId));
        boolean solvedSuccessfully = quiz.getAnswer().equals(quizAnswer.getAnswer());

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

    @Override
    public Quiz createQuiz(@Valid Quiz quiz) {
        quiz.setUser(getLoggedInUser());
        return quizRepository.save(quiz);
    }

    @Override
    public void deleteQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId));

        String currentUserEmail = authenticationFacade.getAuthentication().getName();
        if (!currentUserEmail.equals(quiz.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Logged in user is not the author of the quiz");
        }
        quizRepository.deleteById(quizId);
    }

    private User getLoggedInUser() {
        String currentUserEmail = authenticationFacade.getAuthentication().getName();
        return Optional.ofNullable(userRepository.findByEmail(currentUserEmail))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Logged in user not found in data store"));
    }
}
