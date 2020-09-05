package engine.controller;

import engine.dto.QuizCompletionDto;
import engine.dto.QuizDto;
import engine.mapper.QuizCompletionMapper;
import engine.mapper.QuizMapper;
import engine.model.quiz.Quiz;
import engine.model.quiz.QuizAnswer;
import engine.model.quiz.QuizCompletion;
import engine.model.quiz.QuizResult;
import engine.service.quiz.QuizService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;
    private final QuizMapper quizMapper;
    private final QuizCompletionMapper quizCompletionMapper;

    @GetMapping("{id}")
    public QuizDto findById(@PathVariable("id") Long quizId) {
        Quiz quiz = quizService.getQuiz(quizId);
        return quizMapper.toQuizDto(quiz);
    }

    @GetMapping
    public Page<QuizDto> findAll(@RequestParam(defaultValue = "0", name = "page") Long pageNumber) {
        Page<Quiz> quizzes = quizService.getQuizzes(pageNumber);
        return quizzes.map(quizMapper::toQuizDto);
    }

    @GetMapping("completed")
    public Page<QuizCompletionDto> findAllCompletedByUser(@RequestParam(defaultValue = "0", name = "page") Long pageNumber) {
        Page<QuizCompletion> quizCompletionsByUser = quizService.findQuizCompletionsByUser(pageNumber);
        return quizCompletionsByUser.map(quizCompletionMapper::toQuizCompletionMapperDto);
    }

    @PostMapping("{id}/solve")
    public QuizResult solveQuiz(@PathVariable("id") Long quizId, @RequestBody QuizAnswer quizAnswer) {
        return quizService.solveQuiz(quizId, quizAnswer);
    }

    @PostMapping
    public QuizDto create(@RequestBody @Valid QuizDto quizDto) {
        Quiz quiz = quizService.createQuiz(quizMapper.toQuiz(quizDto));
        return quizMapper.toQuizDto(quiz);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long quizId) {
        quizService.deleteQuiz(quizId);
    }
}
