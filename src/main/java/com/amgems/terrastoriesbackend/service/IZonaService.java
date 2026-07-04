package com.amgems.terrastoriesbackend.service;

import com.amgems.terrastoriesbackend.domain.DTO.ZonaMinimaDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IZonaService {
    Map<String, Object> obtenerZonaComoGeoJson(UUID id);
    List<ZonaMinimaDTO> listarZonasMinimas();
}