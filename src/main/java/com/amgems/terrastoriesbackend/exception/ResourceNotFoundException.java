package com.amgems.terrastoriesbackend.exception;

/**
 * Se lanza cuando un recurso solicitado (zona, reporte, etc.) no existe.
 * Es capturada centralmente por {@link GlobalExceptionHandler} y traducida a un HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
