package com.amgems.terrastoriesbackend.service.impl;

import com.amgems.terrastoriesbackend.domain.Zona;
import com.amgems.terrastoriesbackend.exception.ResourceNotFoundException;
import com.amgems.terrastoriesbackend.repository.ZonaRepository;
import com.amgems.terrastoriesbackend.service.IZonaService;
import com.amgems.terrastoriesbackend.utils.ZonaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ZonaServiceImpl implements IZonaService {

    private final ZonaRepository zonaRepository;
    private final ZonaMapper zonaMapper;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerZonaComoGeoJson(UUID id) {
        Zona zona = zonaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zona no encontrada con el ID: " + id));

        return zonaMapper.toGeoJsonFeature(zona);
    }
}