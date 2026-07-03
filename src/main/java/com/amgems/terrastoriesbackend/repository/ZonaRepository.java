package com.amgems.terrastoriesbackend.repository;

import com.amgems.terrastoriesbackend.domain.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, UUID> {

    Optional<Zona> findByCodigoMunicipio(String codigoMunicipio);
}
