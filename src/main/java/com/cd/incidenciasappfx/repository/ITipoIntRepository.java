package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.TipoIntervencion;

import java.util.List;
import java.util.Optional;

public interface ITipoIntRepository {
    Optional<TipoIntervencion> save(TipoIntervencion d);
    Optional<TipoIntervencion> findById(Integer idDelito);
    List<TipoIntervencion> findAll();
    Optional<TipoIntervencion> update(TipoIntervencion d);
    boolean delete(int id);
}
