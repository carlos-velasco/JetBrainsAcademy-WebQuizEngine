package engine.model.quiz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import engine.model.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

// Lombok annotations
@Builder
@Data
@NoArgsConstructor @AllArgsConstructor  // Needed when using Lombok's builder, and Jackson serialization is done
@ToString(exclude = "quizCompletions")  // Avoid recursion in toString() method with one-to-many entities
// Validation annotations
@AnswersInOptions
// JPA annotations
@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String text;

    @Size(min = 2, message = "must have size equal or greater than 2")
    @ElementCollection
    private List<String> options = List.of();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ElementCollection
    private Set<Integer> answer = Set.of();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE)
    @JsonIgnore
    @EqualsAndHashCode.Exclude  // Avoid recursion on cascade deletion
    private Set<QuizCompletion> quizCompletions;
}
