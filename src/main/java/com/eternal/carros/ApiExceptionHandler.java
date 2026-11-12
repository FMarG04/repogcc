package com.eternal.carros;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> validar(MethodArgumentNotValidException error) {
        Map<String, String> campos = new LinkedHashMap<>();
        error.getBindingResult().getFieldErrors().forEach(
            campo -> campos.putIfAbsent(campo.getField(), campo.getDefaultMessage()));
        return Map.of("mensaje", "Revisa los datos del carro", "errores", campos);
    }
}
