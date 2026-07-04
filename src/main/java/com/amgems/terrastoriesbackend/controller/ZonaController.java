package com.amgems.terrastoriesbackend.controller;

import com.amgems.terrastoriesbackend.domain.DTO.ZonaMinimaDTO;
import com.amgems.terrastoriesbackend.service.IZonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/zonas")
public class ZonaController {

    private final IZonaService zonaService;

    @GetMapping("/{id}/geojson")
    public ResponseEntity<Map<String, Object>> obtenerZonaGeoJson(@PathVariable UUID id) {
        Map<String, Object> geoJsonFeature = zonaService.obtenerZonaComoGeoJson(id);
        return ResponseEntity.ok(geoJsonFeature);
    }

    @GetMapping
    public ResponseEntity<List<ZonaMinimaDTO>> listarZonas() {
        return ResponseEntity.ok(zonaService.listarZonasMinimas());
    }
}