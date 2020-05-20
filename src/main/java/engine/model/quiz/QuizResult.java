package engine.model.quiz;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class QuizResult {

    private final boolean isSuccess;
    private final String feedback;
}
