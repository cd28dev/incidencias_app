package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.Delito;
import com.cd.incidenciasappfx.models.ServicioSerenazgo;

import java.util.List;
import java.util.Optional;

public interface IServiciosSerenazgoRepository {
    Optional<ServicioSerenazgo> save(ServicioSerenazgo servicioSerenazgo);
    Optional<ServicioSerenazgo> findById(Integer idServicioSerenazgo);
    List<ServicioSerenazgo> findAll();
    Optional<ServicioSerenazgo> update(ServicioSerenazgo servicioSerenazgo);
    boolean delete(int id);
}
