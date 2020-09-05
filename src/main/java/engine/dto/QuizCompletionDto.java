package engine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.NONE)
public class QuizCompletionDto {

    @JsonProperty("id")
    private Long quizId;
    private LocalDateTime completedAt;
}
