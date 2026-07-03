package com.amgems.terrastoriesbackend.repository;

import com.amgems.terrastoriesbackend.domain.ReporteBasurero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReporteBasureroRepository extends JpaRepository<ReporteBasurero, UUID> {

    /**
     * Obtiene todos los reportes cuya ubicación (Point) cae dentro del polígono
     * de la zona indicada, usando la función espacial ST_Contains de PostGIS.
     * <p>
     * Se usa consulta nativa porque las funciones espaciales de PostGIS
     * (ST_Contains, ST_Within, etc.) no forman parte del estándar JPQL.
     *
     * @param zonaId identificador de la zona/municipio
     * @return listado de reportes ordenados por fecha descendente
     */
    @Query(value = """
            SELECT r.*
            FROM reportes_basurero r
            INNER JOIN zonas z ON ST_Contains(z.geom, r.ubicacion)
            WHERE z.id = :zonaId
            ORDER BY r.fecha_reporte DESC
            """, nativeQuery = true)
    List<ReporteBasurero> findReportesDentroDeZona(@Param("zonaId") UUID zonaId);
}
