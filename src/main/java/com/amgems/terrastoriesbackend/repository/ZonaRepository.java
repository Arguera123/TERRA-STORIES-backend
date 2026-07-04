package com.amgems.terrastoriesbackend.repository;

import com.amgems.terrastoriesbackend.domain.DTO.ZonaMinimaDTO;
import com.amgems.terrastoriesbackend.domain.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, UUID> {

    Optional<Zona> findByCodigoMunicipio(String codigoMunicipio);
    @Query("SELECT new com.amgems.terrastoriesbackend.domain.DTO.ZonaMinimaDTO(z.id, z.nombre) FROM Zona z ORDER BY z.nombre ASC")
    List<ZonaMinimaDTO> findAllZonasMinimas();
}
