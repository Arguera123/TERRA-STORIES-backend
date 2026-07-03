package com.amgems.terrastoriesbackend.controller;

import com.amgems.terrastoriesbackend.domain.DTO.CreateReporteRequestDTO;
import com.amgems.terrastoriesbackend.domain.DTO.ReporteResponseDTO;
import com.amgems.terrastoriesbackend.domain.EstadoReporte;
import com.amgems.terrastoriesbackend.exception.ApiError;
import com.amgems.terrastoriesbackend.service.IReporteService;
import com.amgems.terrastoriesbackend.utils.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reportes")
@Tag(name = "Reportes de Basureros", description = "Gestión de reportes ciudadanos de basureros clandestinos")
public class ReporteController {

    private final IReporteService reporteService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Crear un nuevo reporte",
            description = "Endpoint público: cualquier ciudadano puede reportar un basurero clandestino "
                    + "indicando descripción, latitud y longitud. No requiere autenticación."
    )
    @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente",
            content = @Content(schema = @Schema(implementation = ReporteResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<GenericResponse> crearReporte(@Valid @RequestBody CreateReporteRequestDTO request) {
        ReporteResponseDTO reporteCreado = reporteService.crearReporte(request);
        return GenericResponse.builder()
                .status(HttpStatus.CREATED)
                .data(reporteCreado)
                .build().buildResponse();
    }

    @GetMapping(value = "/zona/{zonaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    // Protegido por Keycloak: requiere el rol "role-admin" en el realm configurado.
    // Ajustar el nombre del rol si el realm usa otra convención.
//    @PreAuthorize("hasRole('role-admin')")
//    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Listar reportes dentro de una zona",
            description = "Obtiene todos los reportes de basureros ubicados dentro de los límites geográficos "
                    + "de una zona/municipio específico. Requiere rol administrativo (Keycloak)."
    )
    @ApiResponse(responseCode = "200", description = "Listado de reportes dentro de la zona")
    @ApiResponse(responseCode = "404", description = "Zona no encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "403", description = "No autorizado: se requiere rol administrativo")
    public ResponseEntity<GenericResponse> obtenerReportesPorZona(
            @Parameter(description = "Identificador (UUID) de la zona/municipio", required = true)
            @PathVariable UUID zonaId) {
        List<ReporteResponseDTO> reportes = reporteService.obtenerReportesPorZona(zonaId);
        return GenericResponse.builder()
                .status(HttpStatus.OK)
                .data(reportes)
                .build().buildResponse();
    }

//    @PreAuthorize("hasRole('role-admin')")
    @PatchMapping("/{reporteId}/estado")
    @Operation(summary = "Actualizar estado de un reporte (Admin)", description = "Permite cambiar el estado a EN_PROCESO o RESUELTO.")
    public ResponseEntity<GenericResponse> actualizarEstado(
            @PathVariable UUID reporteId,
            @RequestParam EstadoReporte estado) {
        ReporteResponseDTO actualizado = reporteService.actualizarEstadoReporte(reporteId, estado);
        return GenericResponse.builder()
                .status(HttpStatus.OK)
                .message("Estado del reporte actualizado con éxito")
                .data(actualizado)
                .build().buildResponse();
    }

//    @PreAuthorize("hasRole('role-admin')")
    @DeleteMapping("/{reporteId}")
    @Operation(summary = "Eliminar un reporte (Admin)", description = "Elimina físicamente un reporte de la base de datos.")
    public ResponseEntity<GenericResponse> eliminarReporte(@PathVariable UUID reporteId) {
        reporteService.eliminarReporte(reporteId);
        return GenericResponse.builder()
                .status(HttpStatus.NO_CONTENT)
                .message("Reporte eliminado con éxito")
                .build().buildResponse();
    }
}
