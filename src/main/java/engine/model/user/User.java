package engine.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import engine.model.quiz.Quiz;
import engine.model.quiz.QuizCompletion;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor  // Needed when using Lombok's builder, and Jackson serialization is done
@ToString(exclude = {"quizzes", "quizCompletions"}) // Avoid recursion in toString() method with one-to-many entities
@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    @ExtendedEmailValidator(message = "email should be valid")
    private String email;

    @Size(min = 5, message = "password must have at least 5 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    @EqualsAndHashCode.Exclude // avoid recursion on cascade deletion
    private Set<Quiz> quizzes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    @EqualsAndHashCode.Exclude // avoid recursion on cascade deletion
    private Set<QuizCompletion> quizCompletions;
}
