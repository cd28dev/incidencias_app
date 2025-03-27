package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.Incidencia;
import com.cd.incidenciasappfx.models.Sector;

import java.util.List;
import java.util.Optional;

public interface IIncidenciaRepository {
    Optional<Incidencia> save(Incidencia incidencia);
    Optional<Incidencia> findById(Integer id);
    List<Incidencia> findAll();
    Optional<Incidencia> update(Incidencia incidencia);
    boolean delete(int id);
}
