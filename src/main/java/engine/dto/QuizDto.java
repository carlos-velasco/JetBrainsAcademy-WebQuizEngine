package engine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.NONE)
public class QuizDto {

    private Long id;
    private String title;
    private String text;

    @Builder.Default
    private final List<String> options = List.of();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Builder.Default
    private final Set<Integer> answer = Set.of();
}
