package com.damdung.banking.exception;
import com.damdung.banking.exception.ResponseError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvisor {
    /**
     * Xử lý exceptions bên DTO
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        /*err.getField() + ": "*/
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        String message = String.join(", ", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseError(message));
    }

    /**
     * Xử lý sai định dạng ngày tháng năm
     * */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ResponseError> handleDateTimeException(DateTimeParseException ex, HttpServletRequest rq) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError("Sai định dạng ngày tháng năm yyyy-mm-dd"));
    }

    /**
     * Xử lý đăng ký trùng email
     * */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ResponseError> handleSQLException(SQLIntegrityConstraintViolationException ex, HttpServletRequest rq) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError("Có thể email đã được đăng kí trước đó"));
    }
}
