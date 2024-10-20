package co.edu.unbosque.complejidad.exceptions;

import co.edu.unbosque.complejidad.model.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProcesarArchivoException.class)
    public ResponseEntity<BaseResponse> handleArchivoException(ProcesarArchivoException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(new BaseResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(BusquedaException.class)
    public ResponseEntity<BaseResponse> handleBusquedaException(BusquedaException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(new BaseResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(ArchivoVacioException.class)
    public ResponseEntity<BaseResponse> handleArchivoVacioException(ArchivoVacioException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(new BaseResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
