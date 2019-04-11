package BugTracker.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Делаем возвращаемые спрингбутом ошибки валидации несколько читаемее.
 */
@RestControllerAdvice
public class CustomExceptionHandler
{
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<Object> handleConstraintViolation(BindException ex, WebRequest request)
    {
        return validationErrorsResponse(ex.getFieldErrors().stream(), x -> x.getField() + " " + x.getDefaultMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request)
    {
        return validationErrorsResponse(ex.getConstraintViolations().stream(), x -> x.getPropertyPath() + " " + x.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request)
    {
        return validationErrorsResponse(ex.getBindingResult().getFieldErrors().stream(), x -> x.getField() + " " + x.getDefaultMessage());
    }

    private <T> ResponseEntity<Object> validationErrorsResponse(Stream<T> errorStream, Function<? super T, ? extends String> mapper)
    {
        Map<String, Object> body = new LinkedHashMap<>();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        List<String> errors = errorStream.map(mapper)
                                         .collect(Collectors.toList());
        body.put("message", "Validation failed");
        body.put("errors", errors);
        return ResponseEntity.status(status).body(body);
    }

}
