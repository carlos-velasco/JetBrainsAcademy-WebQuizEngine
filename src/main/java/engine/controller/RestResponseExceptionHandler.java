package engine.controller;

import engine.service.quiz.QuizNotFoundException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(QuizNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    protected Map<String, String> handleEntityNotFound(QuizNotFoundException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected Map<String, String> handleConstraintViolationException(ConstraintViolationException e) {
        return e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        // Get property name to display it in response's body
                        violation -> ((PathImpl) violation.getPropertyPath()).getLeafNode().asString(),
                        ConstraintViolation::getMessage,
                        (msg1, msg2) -> String.join(", ", msg1, msg2)));
    }

    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected Map<String, String> handleHibernateConstraintViolationException(org.hibernate.exception.ConstraintViolationException e) {
        String sqlExceptionMessage = e.getSQLException().getMessage();
        if (sqlExceptionMessage.contains("primary key") && sqlExceptionMessage.contains("email"))
            return Map.of("email", "duplicate user email");
        return Map.of();
    }
}
