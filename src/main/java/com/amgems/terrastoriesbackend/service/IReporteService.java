package com.amgems.terrastoriesbackend.service;

import com.amgems.terrastoriesbackend.domain.DTO.CreateReporteRequestDTO;
import com.amgems.terrastoriesbackend.domain.DTO.ReporteResponseDTO;
import com.amgems.terrastoriesbackend.domain.EstadoReporte;

import java.util.List;
import java.util.UUID;

public interface IReporteService {

    /**
     * Registra un nuevo reporte de basurero clandestino, construyendo la
     * geometría espacial (Point) a partir de la latitud y longitud recibidas.
     */
    ReporteResponseDTO crearReporte(CreateReporteRequestDTO request);

    /**
     * Obtiene todos los reportes cuya ubicación cae dentro de los límites de la zona indicada.
     *
     * @throws com.amgems.terrastoriesbackend.exception.ResourceNotFoundException si la zona no existe
     */
    List<ReporteResponseDTO> obtenerReportesPorZona(UUID zonaId);
    ReporteResponseDTO actualizarEstadoReporte(UUID reporteId, EstadoReporte nuevoEstado);
    void eliminarReporte(UUID reporteId);
}
