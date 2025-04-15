package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.Incidencia;

import java.util.List;
import java.util.Optional;

public interface IIncidenciaService {
    Optional<Incidencia> save(Incidencia incidencia);

    Optional<Incidencia> findById(Integer id);

    List<Incidencia> findAll();

    Optional<Incidencia> update(Incidencia incidencia);

    boolean delete(Incidencia incidencia);
}
