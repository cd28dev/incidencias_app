package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.Delito;

import java.util.List;
import java.util.Optional;

public interface IDelitoService {
    Optional<Delito> save(Delito delito);

    Optional<Delito> findById(Integer id);

    List<Delito> findAll();

    Optional<Delito> update(Delito delito);

    boolean delete(Delito delito);
}
