package engine.model.mapper;

import engine.model.dto.QuizDto;
import engine.model.quiz.Quiz;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuizMapper {

    QuizDto toQuizDto(Quiz quiz);

    Quiz toQuiz(QuizDto quizDto);
}
