package com.amgems.terrastoriesbackend.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Estructura estándar de respuesta de error para toda la API.
 */
@Getter
@Builder
@Schema(description = "Estructura estándar de error de la API")
public class ApiError {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Momento en que ocurrió el error")
    private final LocalDateTime timestamp;

    @Schema(description = "Código HTTP asociado")
    private final int status;

    @Schema(description = "Nombre corto del error", example = "Recurso no encontrado")
    private final String error;

    @Schema(description = "Mensaje descriptivo del error")
    private final String message;

    @Schema(description = "Ruta donde ocurrió el error")
    private final String path;

    @Schema(description = "Detalle de errores de validación de campos, si aplica")
    private final List<String> detalles;
}
