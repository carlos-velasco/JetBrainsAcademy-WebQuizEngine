package engine.repository;

import engine.model.quiz.QuizCompletion;
import engine.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository("quizCompletionRepository")
public interface QuizCompletionRepository extends PagingAndSortingRepository<QuizCompletion, Long> {

    Page<QuizCompletion> findAllByUser(User user, Pageable pageable);
}
