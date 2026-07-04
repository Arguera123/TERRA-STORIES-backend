package com.amgems.terrastoriesbackend.utils;

import com.amgems.terrastoriesbackend.domain.Zona;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZonaMapper {

    public Map<String, Object> toGeoJsonFeature(Zona zona) {
        if (zona == null) {
            return null;
        }

        Map<String, Object> feature = new HashMap<>();
        feature.put("type", "Feature");

        Map<String, Object> properties = new HashMap<>();
        properties.put("id", zona.getId());
        properties.put("codigoMunicipio", zona.getCodigoMunicipio());
        properties.put("nombre", zona.getNombre());
        feature.put("properties", properties);

        if (zona.getGeom() != null) {
            Map<String, Object> geometryMap = new HashMap<>();

            // Verificamos si la geometría es un MultiPolygon (caso común en municipios con islas/exclaves)
            if (zona.getGeom() instanceof MultiPolygon multiPolygon) {
                geometryMap.put("type", "MultiPolygon");
                List<List<List<double[]>>> multiCoords = new ArrayList<>();

                for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
                    Polygon polygon = (Polygon) multiPolygon.getGeometryN(i);
                    multiCoords.add(procesarPoligono(polygon));
                }
                geometryMap.put("coordinates", multiCoords);

            } else if (zona.getGeom() instanceof Polygon polygon) {
                geometryMap.put("type", "Polygon");
                geometryMap.put("coordinates", procesarPoligono(polygon));
            }

            feature.put("geometry", geometryMap);
        } else {
            feature.put("geometry", null);
        }

        return feature;
    }

    private List<List<double[]>> procesarPoligono(Polygon polygon) {
        List<List<double[]>> rings = new ArrayList<>();

        // 1. Anillo exterior (Límite principal del polígono)
        List<double[]> exteriorCoords = new ArrayList<>();
        for (Coordinate coord : polygon.getExteriorRing().getCoordinates()) {
            exteriorCoords.add(new double[]{coord.x, coord.y}); // [longitud, latitud]
        }
        rings.add(exteriorCoords);

        // 2. Anillos interiores (Por si el municipio tiene "huecos" o lagunas por dentro)
        for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
            List<double[]> interiorCoords = new ArrayList<>();
            for (Coordinate coord : polygon.getInteriorRingN(i).getCoordinates()) {
                interiorCoords.add(new double[]{coord.x, coord.y});
            }
            rings.add(interiorCoords);
        }

        return rings;
    }
}