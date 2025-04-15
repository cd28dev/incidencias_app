package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.TipoOcurrencia;

import java.util.List;
import java.util.Optional;

public interface ITipoOcurrenciaService {
    Optional<TipoOcurrencia> save(TipoOcurrencia d);

    Optional<TipoOcurrencia> findById(Integer idDelito);

    List<TipoOcurrencia> findAll();

    Optional<TipoOcurrencia> update(TipoOcurrencia d);

    boolean delete(TipoOcurrencia tipoOcurrencia);
}
