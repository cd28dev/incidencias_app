package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.Delito;
import com.cd.incidenciasappfx.repository.DelitosRepositoryImpl;
import com.cd.incidenciasappfx.repository.IDelitoRepository;

import java.util.List;
import java.util.Optional;

public class DelitoServiceImpl implements IDelitoService {

    IDelitoRepository delitoRepository;

    public DelitoServiceImpl() {
        delitoRepository = new DelitosRepositoryImpl();
    }

    @Override
    public Optional<Delito> save(Delito delito) {
        return delitoRepository.save(delito);
    }

    @Override
    public Optional<Delito> findById(Integer id) {
        return delitoRepository.findById(id);
    }

    @Override
    public List<Delito> findAll() {
        return delitoRepository.findAll();
    }

    @Override
    public Optional<Delito> update(Delito delito) {
        return delitoRepository.update(delito);
    }

    @Override
    public boolean delete(Delito delito) {
        int id = delito.getIdDelito();
        return delitoRepository.delete(id);
    }
}
