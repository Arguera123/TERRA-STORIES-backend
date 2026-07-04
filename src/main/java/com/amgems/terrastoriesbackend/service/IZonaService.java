package com.amgems.terrastoriesbackend.service;

import java.util.Map;
import java.util.UUID;

public interface IZonaService {
    Map<String, Object> obtenerZonaComoGeoJson(UUID id);
}