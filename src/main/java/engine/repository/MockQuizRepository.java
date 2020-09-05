package engine.repository;

import engine.model.quiz.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MockQuizRepository implements QuizRepository {

    private volatile AtomicLong quizId = new AtomicLong(1);
    private final List<Quiz> quizzes = new ArrayList<>();

    @Override
    public Quiz save(Quiz quiz) {
        Quiz newQuiz = Quiz.builder()
                .id(quizId.getAndIncrement())
                .title(quiz.getTitle())
                .text(quiz.getText())
                .options(quiz.getOptions())
                .answer(quiz.getAnswer())
                .build();

        quizzes.add(newQuiz);
        return newQuiz;
    }

    @Override
    @Nullable
    public Optional<Quiz> findById(Long quizId) {
        return quizzes.stream()
                .filter(quiz -> quiz.getId().equals(quizId))
                .findFirst();
    }

    @Override
    public List<Quiz> findAll() {
        return List.copyOf(quizzes);
    }

    @Override
    public <S extends Quiz> Iterable<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existsById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<Quiz> findAllById(Iterable<Long> longs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Quiz entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends Quiz> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<Quiz> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Quiz> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }
}
