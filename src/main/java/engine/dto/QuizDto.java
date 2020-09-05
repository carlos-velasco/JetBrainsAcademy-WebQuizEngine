package engine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.NONE)
public class QuizDto {

    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String text;

    @Size(min = 2, message = "must have size equal or greater than 2")
    @Builder.Default
    private final List<String> options = List.of();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Builder.Default
    private final Set<Integer> answer = Set.of();
}
