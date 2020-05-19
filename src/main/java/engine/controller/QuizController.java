package engine.controller;

import engine.model.quiz.Quiz;
import engine.model.quiz.QuizAnswer;
import engine.model.quiz.QuizCompletion;
import engine.model.quiz.QuizResult;
import engine.service.quiz.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("{id}")
    public Quiz findById(@PathVariable("id") Long quizId) {
        return quizService.getQuiz(quizId);
    }

    @GetMapping
    public Page<Quiz> findAll(@RequestParam(defaultValue = "0", name = "page") Long pageNumber) {
        return quizService.getQuizzes(pageNumber);
    }

    @GetMapping("completed")
    public Page<QuizCompletion> findAllCompletedByUser(@RequestParam(defaultValue = "0", name = "page") Long pageNumber) {
        return quizService.findQuizCompletionsByUser(pageNumber);
    }

    @PostMapping("{id}/solve")
    public QuizResult solveQuiz(@PathVariable("id") Long quizId, @RequestBody QuizAnswer quizAnswer) {
        return quizService.solveQuiz(quizId, quizAnswer);
    }

    @PostMapping
    public Quiz create(@RequestBody Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long quizId) {
        quizService.deleteQuiz(quizId);
    }
}
