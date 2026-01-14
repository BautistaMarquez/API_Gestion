package com.gestion.erp.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo de 404 - Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("RECURSO_NO_ENCONTRADO", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 2. Manejo de 409 - Conflict (Reglas #7 y #8: Vehículo/Conductor ocupado)
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ResourceConflictException ex) {
        ErrorResponse error = new ErrorResponse("CONFLICTO_NEGOCIO", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // 3. Manejo de 400 - Errores de validación @Valid (ej: cantidad < 0)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errores = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse("DATOS_INVALIDOS", "Error en la validación de los campos", errores);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 4. Manejo de 400 - BusinessException (Regla #6: Carga mayor a 0)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        ErrorResponse error = new ErrorResponse("ERROR_LOGICA_NEGOCIO", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 5. El "Seguro de Vida" - Captura cualquier error no controlado (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // En producción, aquí deberías loguear el stacktrace para auditoría
        ErrorResponse error = new ErrorResponse("ERROR_INTERNO", "Ha ocurrido un error inesperado en el servidor");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}