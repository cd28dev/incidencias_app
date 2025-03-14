package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.TipoOcurrencia;
import com.cd.incidenciasappfx.repository.ITipoOcurrenciaRepository;
import com.cd.incidenciasappfx.repository.TipoOcurrenciaImpl;

import java.util.List;
import java.util.Optional;

public class TipoOcurrenciaServiceImpl implements ITipoOcurrenciaService {
    private final ITipoOcurrenciaRepository tipoOcurrenciaRepository;

    public TipoOcurrenciaServiceImpl() {
        this.tipoOcurrenciaRepository = new TipoOcurrenciaImpl();
    }

    @Override
    public Optional<TipoOcurrencia> save(TipoOcurrencia d) {
        return tipoOcurrenciaRepository.save(d);
    }

    @Override
    public Optional<TipoOcurrencia> findById(Integer id) {
        return tipoOcurrenciaRepository.findById(id);
    }

    @Override
    public List<TipoOcurrencia> findAll() {
        return tipoOcurrenciaRepository.findAll();
    }

    @Override
    public Optional<TipoOcurrencia> update(TipoOcurrencia d) {
        return tipoOcurrenciaRepository.save(d);
    }

    @Override
    public boolean delete(TipoOcurrencia tipoOcurrencia) {
        int id = tipoOcurrencia.getIdOcurrencia();
        return tipoOcurrenciaRepository.delete(id);
    }
}
