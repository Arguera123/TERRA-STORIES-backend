package com.amgems.terrastoriesbackend.domain.DTO;

import com.amgems.terrastoriesbackend.domain.EstadoReporte;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResponseDTO {

    private UUID id;

    private String descripcion;

    private LocalDateTime fechaReporte;

    private EstadoReporte estado;

    private String usuarioId;

    private String usuarioAsignadoId;

    private Double latitud;

    private Double longitud;
}
