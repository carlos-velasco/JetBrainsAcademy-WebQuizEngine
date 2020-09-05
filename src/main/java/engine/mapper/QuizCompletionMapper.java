package engine.mapper;

import engine.dto.QuizCompletionDto;
import engine.model.quiz.QuizCompletion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuizCompletionMapper {

    QuizCompletionDto toQuizCompletionMapperDto(QuizCompletion quizCompletion);
}
