package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.ServicioSerenazgo;
import com.cd.incidenciasappfx.repository.IServiciosSerenazgoRepository;
import com.cd.incidenciasappfx.repository.ServiciosSerenazgoRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class ServicioServiceImpl implements IServiciosService{

    private IServiciosSerenazgoRepository repository;

    public ServicioServiceImpl(){
        repository = new ServiciosSerenazgoRepositoryImpl();
    }

    @Override
    public Optional<ServicioSerenazgo> save(ServicioSerenazgo servicioSerenazgo) {
        return repository.save(servicioSerenazgo);
    }

    @Override
    public Optional<ServicioSerenazgo> findById(Integer idServicioSerenazgo) {
        return repository.findById(idServicioSerenazgo);
    }

    @Override
    public List<ServicioSerenazgo> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ServicioSerenazgo> update(ServicioSerenazgo servicioSerenazgo) {
        return repository.update(servicioSerenazgo);
    }

    @Override
    public boolean delete(ServicioSerenazgo servicioSerenazgo) {
        int idServicioSerenazgo = servicioSerenazgo.getIdServicio();

        return repository.delete(idServicioSerenazgo)   ;
    }
}
