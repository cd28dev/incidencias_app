package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.TipoIntervencion;
import com.cd.incidenciasappfx.repository.ITipoIntRepository;
import com.cd.incidenciasappfx.repository.TipoIntRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class TipoIntServiceImpl implements ITipoIntService{
    private final ITipoIntRepository tipoIntRepository;

    public TipoIntServiceImpl() {
        this.tipoIntRepository = new TipoIntRepositoryImpl();
    }

    @Override
    public Optional<TipoIntervencion> save(TipoIntervencion d) {
        return tipoIntRepository.save(d);
    }

    @Override
    public Optional<TipoIntervencion> findById(Integer idDelito) {
        return tipoIntRepository.findById(idDelito);
    }

    @Override
    public List<TipoIntervencion> findAll() {
        return tipoIntRepository.findAll();
    }

    @Override
    public Optional<TipoIntervencion> update(TipoIntervencion d) {
        return tipoIntRepository.save(d);
    }

    @Override
    public boolean delete(TipoIntervencion d) {
        int id = d.getIdIntervencion();
        return tipoIntRepository.delete(id);
    }
}
