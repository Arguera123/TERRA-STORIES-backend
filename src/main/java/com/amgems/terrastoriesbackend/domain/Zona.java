package com.amgems.terrastoriesbackend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

import java.util.UUID;

/**
 * Representa una zona o municipio de El Salvador, con su límite geográfico
 * almacenado como un polígono (SRID 4326 - WGS84).
 * <p>
 * Esta entidad se usa como referencia espacial para filtrar los reportes
 * ciudadanos que caen dentro de sus límites (ver {@link com.amgems.terrastoriesbackend.repository.ReporteBasureroRepository}).
 */
@Entity
@Table(name = "zonas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "codigo_municipio", nullable = false, unique = true, length = 20)
    private String codigoMunicipio;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    /**
     * Límite geográfico de la zona. El tipo de columna geometry(Geometry,4326)
     * es interpretado por Hibernate Spatial gracias a la presencia de JTS en el classpath.
     */
    @Column(name = "geom", columnDefinition = "geometry(Geometry,4326)", nullable = false)
    private Geometry geom;
}
