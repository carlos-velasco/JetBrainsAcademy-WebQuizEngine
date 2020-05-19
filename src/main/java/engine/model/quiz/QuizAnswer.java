package engine.model.quiz;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class QuizAnswer {
    @NotNull
    Set<Integer> answer;
}
