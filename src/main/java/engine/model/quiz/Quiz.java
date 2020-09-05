package engine.model.quiz;

import engine.model.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "quizCompletions")  // Avoid recursion in toString() method with one-to-many entities
@AnswersInOptions
@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String text;

    @Size(min = 2, message = "must have size equal or greater than 2")
    @ElementCollection
    @Builder.Default
    private List<String> options = List.of();

    @ElementCollection
    @Builder.Default
    private Set<Integer> answer = Set.of();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude  // Avoid recursion on cascade deletion
    private Set<QuizCompletion> quizCompletions;

    public boolean checkAnswer(QuizAnswer answer) {
        if (answer == null) return false;

        return this.answer.equals(answer.getAnswer());
    }
}
