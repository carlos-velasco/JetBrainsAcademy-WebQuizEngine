package engine.repository;

import engine.model.quiz.Quiz;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("quizRepository")
public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long> {

}