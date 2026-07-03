package com.amgems.terrastoriesbackend.domain.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Payload de entrada para que un ciudadano registre un reporte de basurero clandestino.
 * La latitud y longitud se reciben como dobles simples; la capa de servicio
 * se encarga de construir la geometría JTS (Point) a partir de estos valores.
 */
@Data
@Schema(description = "Datos necesarios para reportar un basurero clandestino")
public class CreateReporteRequestDTO {

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    @Schema(description = "Descripción del basurero reportado", example = "Acumulación de desechos sólidos junto a la quebrada El Piro")
    private String descripcion;

    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-90.0", message = "La latitud debe estar entre -90 y 90")
    @DecimalMax(value = "90.0", message = "La latitud debe estar entre -90 y 90")
    @Schema(description = "Latitud del punto reportado (WGS84)", example = "13.6929")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin(value = "-180.0", message = "La longitud debe estar entre -180 y 180")
    @DecimalMax(value = "180.0", message = "La longitud debe estar entre -180 y 180")
    @Schema(description = "Longitud del punto reportado (WGS84)", example = "-89.2182")
    private Double longitud;

    @Schema(description = "UUID del usuario en Keycloak que genera el reporte (opcional, permite reportes anónimos)",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String usuarioId;
}
