package com.amgems.terrastoriesbackend.domain.DTO;

import com.amgems.terrastoriesbackend.domain.EstadoReporte;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representación de salida de un reporte. Expone latitud/longitud como
 * dobles simples en lugar de exponer directamente el tipo Point de JTS,
 * manteniendo el contrato JSON limpio y desacoplado de la capa de persistencia.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Reporte de un basurero clandestino")
public class ReporteResponseDTO {

    @Schema(description = "Identificador único del reporte")
    private UUID id;

    @Schema(description = "Descripción del basurero reportado")
    private String descripcion;

    @Schema(description = "Fecha y hora en la que se registró el reporte")
    private LocalDateTime fechaReporte;

    @Schema(description = "Estado actual del reporte")
    private EstadoReporte estado;

    @Schema(description = "UUID del usuario en Keycloak que generó el reporte (puede ser nulo)")
    private String usuarioId;

    @Schema(description = "Latitud del punto reportado (WGS84)")
    private Double latitud;

    @Schema(description = "Longitud del punto reportado (WGS84)")
    private Double longitud;
}
