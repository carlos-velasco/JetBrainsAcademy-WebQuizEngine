package engine.model.quiz;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@AllArgsConstructor @NoArgsConstructor // Needed when using Lombok's builder, and Jackson serialization is done
@Entity
@Table(name = "quizCompletions")
public class QuizCompletion {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "QUIZ_ID")
    @JsonIgnore
    private Quiz quiz;

    @Column(name="QUIZ_ID", updatable = false, insertable = false)
    @JsonProperty("id")
    private Long quiz_id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private User user;

    private LocalDateTime completedAt;
}
