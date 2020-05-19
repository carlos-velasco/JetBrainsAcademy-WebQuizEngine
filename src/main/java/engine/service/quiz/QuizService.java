package engine.service.quiz;

import engine.model.quiz.Quiz;
import engine.model.quiz.QuizAnswer;
import engine.model.quiz.QuizCompletion;
import engine.model.quiz.QuizResult;
import org.springframework.data.domain.Page;

import javax.validation.Valid;


public interface QuizService {
    Quiz getQuiz(Long quizId);

    Page<Quiz> getQuizzes(Long pageNumber);

    QuizResult solveQuiz(Long quizId, QuizAnswer quizAnswer);

    Quiz createQuiz(@Valid Quiz quiz);

    void deleteQuiz(Long quizId);

    Page<QuizCompletion> findQuizCompletionsByUser(Long pageNumber);
}
