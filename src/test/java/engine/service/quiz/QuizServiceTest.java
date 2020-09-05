package engine.service.quiz;

import engine.model.quiz.*;
import engine.model.user.User;
import engine.repository.QuizCompletionRepository;
import engine.repository.QuizRepository;
import engine.repository.UserRepository;
import engine.security.AuthenticationFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuizCompletionRepository quizCompletionRepository;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private QuizService target;

    @Test
    public void givenQuizExists_whenGettingQuizById_QuizIsReturned() {

        // GIVEN
        long id = 5L;
        Optional<Quiz> expectedQuiz = Optional.of(Quiz.builder()
                .id(id)
                .text("Quiz text")
                .title("Quiz title")
                .options(List.of("Option 1", "Option 2"))
                .answer(Set.of(0, 1))
                .build());
        when(quizRepository.findById(any(Long.class))).thenReturn(expectedQuiz);

        // WHEN
        Quiz observedQuiz = target.getQuiz(id);

        // THEN
        assertThat(observedQuiz).isEqualTo(expectedQuiz.get());
        verify(quizRepository).findById(id);
    }

    @Test
    public void givenQuizNotExists_whenGettingQuizById_thenQuizNotFoundExceptionIsThrown() {

        // GIVEN
        long id = 5L;
        Optional<Quiz> expectedQuiz = Optional.empty();
        when(quizRepository.findById(any(Long.class))).thenReturn(expectedQuiz);

        // WHEN
        Throwable thrown = catchThrowable(() -> target.getQuiz(id));

        // THEN
        assertThat(thrown).isInstanceOf(QuizNotFoundException.class)
                .hasMessageContaining("not found")
                .hasMessageContaining("Quiz");
        verify(quizRepository).findById(id);
    }

    @Test
    public void givenQuizExistsAndBelongsToLoggedUser_whenDeletingQuiz_thenQuizIsDeleted() {

        // GIVEN
        long id = 5L;
        String userEmail = "quizOwner@example.com";
        Optional<Quiz> expectedQuiz = Optional.of(Quiz.builder()
                .id(id)
                .user(User.builder().email(userEmail).build())
                .build());
        when(quizRepository.findById(any(Long.class))).thenReturn(expectedQuiz);

        Authentication authentication = new TestingAuthenticationToken(userEmail, "password");
        when(authenticationFacade.getAuthentication()).thenReturn(authentication);

        // WHEN
        target.deleteQuiz(id);

        // THEN
        verify(quizRepository).deleteById(id);
    }

    @Test
    public void givenQuizExistsAndNotBelongsToLoggedUser_whenDeletingQuiz_thenExceptionIsThrown() {
        // GIVEN
        long id = 5L;
        String userEmail = "quizOwner@example.com";
        Optional<Quiz> expectedQuiz = Optional.of(Quiz.builder()
                .id(id)
                .user(User.builder().email(userEmail).build())
                .build());
        when(quizRepository.findById(any(Long.class))).thenReturn(expectedQuiz);

        Authentication authentication = new TestingAuthenticationToken("otherEmail", "password");
        when(authenticationFacade.getAuthentication()).thenReturn(authentication);

        // WHEN
        Throwable thrown = catchThrowable(() -> target.deleteQuiz(id));

        // THEN
        assertThat(thrown).isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Logged in user is not the author of the quiz");
        ResponseStatusException exception = (ResponseStatusException) thrown;
        assertThat(exception.getStatus()).isEqualByComparingTo(HttpStatus.FORBIDDEN);
        verify(quizRepository, never()).deleteById(id);
    }

    @Test
    public void givenQuizDoesNotExist_whenDeletingQuiz_thenQuizNotFoundExceptionIsThrown() {

        // GIVEN
        long id = 5L;
        Optional<Quiz> expectedQuiz = Optional.empty();
        when(quizRepository.findById(any(Long.class))).thenReturn(expectedQuiz);

        // WHEN
        Throwable thrown = catchThrowable(() -> target.getQuiz(id));

        // THEN
        assertThat(thrown).isInstanceOf(QuizNotFoundException.class)
                .hasMessageContaining("not found")
                .hasMessageContaining("Quiz");
        verify(quizRepository, never()).deleteById(id);
    }

    @Test
    public void givenQuiz_whenCreatingIt_thenItIsSavedToRepositoryAndAssociatedToLoggedInUser() {

        // GIVEN
        Quiz quiz = Quiz.builder()
                .id(5L)
                .build();

        User user = User.builder()
                .email("quizOwner@example.com")
                .password("pass123")
                .id(20L)
                .build();

        Quiz expectedQuiz = Quiz.builder()
                .id(5L)
                .user(user)
                .build();

        Authentication authentication = new TestingAuthenticationToken(user.getEmail(), user.getPassword());
        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(quizRepository.save(quiz)).thenReturn(expectedQuiz);

        // WHEN
        Quiz createdQuiz = target.createQuiz(quiz);

        // THEN
        assertThat(createdQuiz).isEqualTo(expectedQuiz);
        verify(quizRepository).save(createdQuiz);
    }

    @Test
    public void givenQuizDoesNotExist_whenSolvingQuiz_thenQuizNotFoundExceptionIsThrown() {
        // GIVEN
        long id = 5L;
        Optional<Quiz> expectedQuiz = Optional.empty();
        when(quizRepository.findById(id)).thenReturn(expectedQuiz);

        // WHEN
        Throwable thrown = catchThrowable(() -> target.solveQuiz(id, any(QuizAnswer.class)));

        // THEN
        assertThat(thrown).isInstanceOf(QuizNotFoundException.class)
                .hasMessageContaining("not found")
                .hasMessageContaining("Quiz");
        verify(quizCompletionRepository, never()).save(any(QuizCompletion.class));
    }

    @Test
    public void givenQuiz_whenSolvingQuizCorrectly_thenCompletionIsSavedToRepositoryAndSuccessfulResultReturned() {
        // GIVEN
        long id = 5L;
        User user = User.builder()
                .email("quizOwner@example.com")
                .password("pass123")
                .id(20L)
                .build();

        Optional<Quiz> expectedQuiz = Optional.of(Quiz.builder()
                .id(id)
                .answer(Set.of(0, 1))
                .build());
        when(quizRepository.findById(id)).thenReturn(expectedQuiz);
        Authentication authentication = new TestingAuthenticationToken(user.getEmail(), user.getPassword());
        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        QuizCompletion expectedQuizCompletion = QuizCompletion.builder()
                .quiz(expectedQuiz.get())
                .user(user)
                .completedAt(LocalDateTime.now())
                .build();

        // WHEN
        QuizAnswer quizAnswer = new QuizAnswer();
        quizAnswer.setAnswer(Set.of(0, 1));
        QuizResult quizResult = target.solveQuiz(id, quizAnswer);

        // THEN
        assertThat(quizResult).isEqualTo(QuizResultFactory.buildQuizResult(true));
        verify(quizCompletionRepository)
                .save(argThat(argument -> argument.getUser().equals(expectedQuizCompletion.getUser())
                        && argument.getQuiz().equals(expectedQuizCompletion.getQuiz())
                        && argument.getCompletedAt().isBefore(LocalDateTime.now())
                        && argument.getCompletedAt().isAfter(expectedQuizCompletion.getCompletedAt())));
    }

    @Test
    public void givenQuiz_whenSolvingQuizInCorrectly_thenCompletionIsNotSavedToRepositoryAndFailResultReturned() {
        // GIVEN
        long id = 5L;
        Optional<Quiz> expectedQuiz = Optional.of(Quiz.builder()
                .id(id)
                .answer(Set.of(0, 1))
                .build());
        when(quizRepository.findById(id)).thenReturn(expectedQuiz);

        // WHEN
        QuizAnswer quizAnswer = new QuizAnswer();
        quizAnswer.setAnswer(Set.of(0));
        QuizResult quizResult = target.solveQuiz(id, quizAnswer);

        // THEN
        assertThat(quizResult).isEqualTo(QuizResultFactory.buildQuizResult(false));
        verify(quizCompletionRepository, never()).save(any(QuizCompletion.class));
    }

    @Test
    public void givenPageNumber_whenAllQuizzesPageIsRequested_thenPageQuizRequestForPageSizeAndOrderIsDone() {

        // GIVEN
        Long pageNumber = 1L;
        PageRequest expectedPageRequest = PageRequest.of(pageNumber.intValue(), QuizService.PAGE_SIZE, Sort.by("id"));

        // WHEN
        target.getQuizzes(pageNumber);

        // THEN
        verify(quizRepository).findAll(expectedPageRequest);
    }

    @Test
    public void givenPageNumber_whenAllCompletedQuizzesPageIsRequested_thenPageQuizRequestForPageSizeAndOrderIsDone() {

        // GIVEN
        Long pageNumber = 1L;
        PageRequest expectedPageRequest = PageRequest.of(pageNumber.intValue(), QuizService.PAGE_SIZE, Sort.by("completedAt").descending());
        Authentication authentication = new TestingAuthenticationToken("user", "password");
        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        User user = User.builder().build();
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        // WHEN
        target.findQuizCompletionsByUser(pageNumber);

        // THEN
        verify(quizCompletionRepository).findAllByUser(user, expectedPageRequest);
    }
}
