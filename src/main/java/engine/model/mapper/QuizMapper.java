package engine.model.mapper;

import engine.model.dto.QuizDto;
import engine.model.quiz.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuizMapper {

    QuizDto toQuizDto(Quiz quiz);

    @Mapping(target = "quizCompletions", ignore = true)
    @Mapping(target = "user", ignore = true)
    Quiz toQuiz(QuizDto quizDto);
}
