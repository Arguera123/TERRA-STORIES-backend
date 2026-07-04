package com.amgems.terrastoriesbackend.controller;

import com.amgems.terrastoriesbackend.domain.DTO.CreateReporteRequestDTO;
import com.amgems.terrastoriesbackend.domain.DTO.ReporteResponseDTO;
import com.amgems.terrastoriesbackend.domain.EstadoReporte;
import com.amgems.terrastoriesbackend.service.IReporteService;
import com.amgems.terrastoriesbackend.utils.GenericResponse;
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
public class ReporteController {

    private final IReporteService reporteService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> crearReporte(@Valid @RequestBody CreateReporteRequestDTO request) {
        ReporteResponseDTO reporteCreado = reporteService.crearReporte(request);
        return GenericResponse.builder()
                .status(HttpStatus.CREATED)
                .data(reporteCreado)
                .build().buildResponse();
    }

    @GetMapping(value = "/zona/{zonaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> obtenerReportesPorZona(@PathVariable UUID zonaId) {
        List<ReporteResponseDTO> reportes = reporteService.obtenerReportesPorZona(zonaId);
        return GenericResponse.builder()
                .status(HttpStatus.OK)
                .data(reportes)
                .message("Listado de reportes dentro de la zona " + zonaId)
                .build().buildResponse();
    }

    @PatchMapping("/{reporteId}/estado")
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

    @DeleteMapping("/{reporteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> eliminarReporte(@PathVariable UUID reporteId) {
        reporteService.eliminarReporte(reporteId);
        return GenericResponse.builder()
                .status(HttpStatus.NO_CONTENT)
                .message("Reporte eliminado con éxito")
                .build().buildResponse();
    }

    @PatchMapping("/{reporteId}/asignar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse> asignarReporte(
            @PathVariable UUID reporteId,
            @RequestParam String usuarioId) {
        ReporteResponseDTO actualizado = reporteService.asignarReporte(reporteId, usuarioId);
        return GenericResponse.builder()
                .status(HttpStatus.OK)
                .message("Reporte asignado con éxito")
                .data(actualizado)
                .build().buildResponse();
    }
}
