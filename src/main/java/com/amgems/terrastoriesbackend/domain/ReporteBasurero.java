package com.amgems.terrastoriesbackend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Reporte ciudadano de un basurero clandestino.
 * <p>
 * El campo {@code usuarioId} referencia el ID (UUID) del usuario autenticado en Keycloak
 * que generó el reporte. No se guarda ninguna credencial ni dato sensible de identidad:
 * esa información vive exclusivamente en el servidor de Keycloak.
 */
@Entity
@Table(name = "reportes_basurero")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteBasurero {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Column(name = "fecha_reporte", nullable = false, updatable = false)
    private LocalDateTime fechaReporte;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoReporte estado;

    /**
     * UUID del usuario en Keycloak. Puede ser nulo si en el futuro se permiten
     * reportes completamente anónimos.
     */
    @Column(name = "usuario_id", length = 36)
    private String usuarioId;

    @Column(name = "usuario_asignado_id", length = 36)
    private String usuarioAsignadoId;

    /**
     * Ubicación puntual del basurero reportado (SRID 4326 - WGS84).
     */
    @Column(name = "ubicacion", columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point ubicacion;

    @PrePersist
    protected void alPersistir() {
        if (this.fechaReporte == null) {
            this.fechaReporte = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado = EstadoReporte.PENDIENTE;
        }
    }
}
