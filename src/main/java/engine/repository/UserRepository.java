package engine.repository;

import engine.model.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
