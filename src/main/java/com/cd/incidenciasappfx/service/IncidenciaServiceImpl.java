package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.Incidencia;
import com.cd.incidenciasappfx.repository.IIncidenciaRepository;
import com.cd.incidenciasappfx.repository.IncidenciaRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class IncidenciaServiceImpl implements IIncidenciaService {

    private final IIncidenciaRepository incidenciaRepository;

    public IncidenciaServiceImpl() {
        this.incidenciaRepository = new IncidenciaRepositoryImpl();
    }
    @Override
    public Optional<Incidencia> save(Incidencia incidencia) {
        return incidenciaRepository.save(incidencia);
    }

    @Override
    public Optional<Incidencia> findById(Integer id) {
        return incidenciaRepository.findById(id);
    }

    @Override
    public List<Incidencia> findAll() {
        return incidenciaRepository.findAll();
    }

    @Override
    public Optional<Incidencia> update(Incidencia incidencia) {
        return incidenciaRepository.update(incidencia);
    }

    @Override
    public boolean delete(Incidencia incidencia) {
        int id = incidencia.getIdIncidencia();
        return incidenciaRepository.delete(id);
    }
}
