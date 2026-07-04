package com.amgems.terrastoriesbackend.utils;

import com.amgems.terrastoriesbackend.domain.Zona;
import com.amgems.terrastoriesbackend.repository.ZonaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeoJsonZoneLoader implements CommandLineRunner {

    private final ZonaRepository zonaRepository;
    private final ResourceLoader resourceLoader;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Override
    public void run(String... args) throws Exception {
        if (zonaRepository.count() > 0) {
            log.info("La base de datos ya contiene zonas/municipios. Se omite la carga inicial.");
            return;
        }

        log.info("Iniciando la carga de polígonos municipales desde el archivo GeoJSON...");

        ObjectMapper mapper = new ObjectMapper();
        Resource resource = resourceLoader.getResource("classpath:data/municipios.geojson");

        if (!resource.exists()) {
            log.error("No se encontró el archivo municipios.geojson en src/main/resources/data/");
            return;
        }

        try (InputStream inputStream = resource.getInputStream()) {
            JsonNode root = mapper.readTree(inputStream);
            JsonNode features = root.get("features");

            if (features == null || !features.isArray()) {
                log.error("El formato del archivo GeoJSON no es válido (falta el arreglo 'features').");
                return;
            }

            int contador = 0;
            for (JsonNode feature : features) {
                JsonNode properties = feature.get("properties");
                JsonNode geometry = feature.get("geometry");

                if (properties == null || geometry == null) continue;

                String nombreMunicipio = properties.has("NAM") ? properties.get("NAM").asText() : "Desconocido";
                String codigoMunicipio = properties.has("NA3") ? properties.get("NA3").asText() : null;

                String geometryType = geometry.get("type").asText();
                Geometry geomResult = null;
                if ("MultiPolygon".equals(geometryType)) {
                    JsonNode coordinatesNode = geometry.get("coordinates");
                    List<Polygon> polygons = new ArrayList<>();

                    for (JsonNode polygonNode : coordinatesNode) {
                        List<LinearRing> rings = new ArrayList<>();
                        for (JsonNode ringNode : polygonNode) {
                            List<Coordinate> coordsList = new ArrayList<>();
                            for (JsonNode coord : ringNode) {
                                double lon = coord.get(0).asDouble();
                                double lat = coord.get(1).asDouble();
                                coordsList.add(new Coordinate(lon, lat));
                            }

                            if (coordsList.isEmpty()) continue;
                            if (!coordsList.get(0).equals(coordsList.get(coordsList.size() - 1))) {
                                coordsList.add(coordsList.get(0));
                            }

                            Coordinate[] coordsArray = coordsList.toArray(new Coordinate[0]);
                            rings.add(geometryFactory.createLinearRing(coordsArray));
                        }

                        if (!rings.isEmpty()) {
                            LinearRing exterior = rings.remove(0);
                            LinearRing[] interiors = rings.toArray(new LinearRing[0]);
                            polygons.add(geometryFactory.createPolygon(exterior, interiors));
                        }
                    }

                    if (!polygons.isEmpty()) {
                        geomResult = geometryFactory.createMultiPolygon(polygons.toArray(new Polygon[0]));
                    }
                }

                if (geomResult != null) {
                    geomResult.setSRID(4326);
                    Zona zona = Zona.builder()
                            .nombre(nombreMunicipio)
                            .codigoMunicipio(codigoMunicipio)
                            .geom(geomResult)
                            .build();

                    zonaRepository.save(zona);
                    contador++;
                }
            }
            log.info("¡Carga masiva finalizada con éxito! Se registraron {} municipios.", contador);
        } catch (Exception e) {
            log.error("Error al procesar el archivo GeoJSON", e);
        }
    }
}