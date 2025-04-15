package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.ServicioSerenazgo;

import java.util.List;
import java.util.Optional;

public interface IServiciosService {

    Optional<ServicioSerenazgo> save(ServicioSerenazgo servicioSerenazgo);

    Optional<ServicioSerenazgo> findById(Integer idServicioSerenazgo);

    List<ServicioSerenazgo> findAll();

    Optional<ServicioSerenazgo> update(ServicioSerenazgo servicioSerenazgo);

    boolean delete(ServicioSerenazgo servicioSerenazgo);
}
