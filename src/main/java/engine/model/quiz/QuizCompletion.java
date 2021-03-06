package engine.model.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import engine.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "quizCompletions")
public class QuizCompletion {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "QUIZ_ID")
    private Quiz quiz;

    @Column(name = "QUIZ_ID", updatable = false, insertable = false)
    @JsonProperty("id")
    private Long quizId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private LocalDateTime completedAt;
}
