package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.Delito;

import java.util.List;
import java.util.Optional;

public interface IDelitoRepository {
    Optional<Delito> save(Delito d);

    Optional<Delito> findById(Integer idDelito);

    List<Delito> findAll();

    Optional<Delito> update(Delito d);

    boolean delete(int id);
}
