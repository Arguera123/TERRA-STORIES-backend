package com.amgems.terrastoriesbackend.service.impl;

import com.amgems.terrastoriesbackend.domain.DTO.CreateReporteRequestDTO;
import com.amgems.terrastoriesbackend.domain.DTO.ReporteResponseDTO;
import com.amgems.terrastoriesbackend.domain.EstadoReporte;
import com.amgems.terrastoriesbackend.domain.ReporteBasurero;
import com.amgems.terrastoriesbackend.exception.ResourceNotFoundException;
import com.amgems.terrastoriesbackend.repository.ReporteBasureroRepository;
import com.amgems.terrastoriesbackend.repository.ZonaRepository;
import com.amgems.terrastoriesbackend.service.IReporteService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements IReporteService {

    /** SRID correspondiente a WGS84, el sistema de coordenadas estándar usado por GPS. */
    private static final int SRID_WGS84 = 4326;

    /**
     * GeometryFactory de JTS es thread-safe y reutilizable: se instancia una sola vez
     * para construir todas las geometrías puntuales de los reportes.
     */
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), SRID_WGS84);

    private final ReporteBasureroRepository reporteBasureroRepository;
    private final ZonaRepository zonaRepository;

    @Override
    @Transactional
    public ReporteResponseDTO crearReporte(CreateReporteRequestDTO request) {
        Point ubicacion = construirPunto(request.getLongitud(), request.getLatitud());

        ReporteBasurero reporte = ReporteBasurero.builder()
                .descripcion(request.getDescripcion())
                .fechaReporte(LocalDateTime.now())
                .estado(EstadoReporte.PENDIENTE)
                .usuarioId(request.getUsuarioId())
                .ubicacion(ubicacion)
                .build();

        ReporteBasurero reporteGuardado = reporteBasureroRepository.save(reporte);
        return mapToResponseDTO(reporteGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> obtenerReportesPorZona(UUID zonaId) {
        if (!zonaRepository.existsById(zonaId)) {
            throw new ResourceNotFoundException("No se encontró la zona con id: " + zonaId);
        }

        return reporteBasureroRepository.findReportesDentroDeZona(zonaId).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public ReporteResponseDTO actualizarEstadoReporte(UUID reporteId, EstadoReporte nuevoEstado) {
        ReporteBasurero reporte = reporteBasureroRepository.findById(reporteId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el reporte con id: " + reporteId));

        reporte.setEstado(nuevoEstado);
        ReporteBasurero actualizado = reporteBasureroRepository.save(reporte);
        return mapToResponseDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarReporte(UUID reporteId) {
        if (!reporteBasureroRepository.existsById(reporteId)) {
            throw new ResourceNotFoundException("No se encontró el reporte con id: " + reporteId);
        }
        reporteBasureroRepository.deleteById(reporteId);
    }

    @Override
    @Transactional
    public ReporteResponseDTO asignarReporte(UUID reporteId, String usuarioId) {
        ReporteBasurero reporte = reporteBasureroRepository.findById(reporteId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el reporte con id: " + reporteId));
        
        reporte.setUsuarioAsignadoId(usuarioId);
        ReporteBasurero actualizado = reporteBasureroRepository.save(reporte);
        return mapToResponseDTO(actualizado);
    }

    /**
     * Construye un Point de JTS a partir de longitud y latitud.
     * IMPORTANTE: JTS/PostGIS esperan el orden (x=longitud, y=latitud), no al revés.
     */
    private Point construirPunto(double longitud, double latitud) {
        Point punto = GEOMETRY_FACTORY.createPoint(new Coordinate(longitud, latitud));
        punto.setSRID(SRID_WGS84);
        return punto;
    }

    private ReporteResponseDTO mapToResponseDTO(ReporteBasurero reporte) {
        return ReporteResponseDTO.builder()
                .id(reporte.getId())
                .descripcion(reporte.getDescripcion())
                .fechaReporte(reporte.getFechaReporte())
                .estado(reporte.getEstado())
                .usuarioId(reporte.getUsuarioId())
                .usuarioAsignadoId(reporte.getUsuarioAsignadoId())
                .latitud(reporte.getUbicacion().getY())
                .longitud(reporte.getUbicacion().getX())
                .build();
    }
}
