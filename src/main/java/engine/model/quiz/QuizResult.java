package engine.model.quiz;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizResult {

    private final boolean isSuccess;
    private final String feedback;
}
