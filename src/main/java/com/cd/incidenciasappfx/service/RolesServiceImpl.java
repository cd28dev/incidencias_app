package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.Rol;
import com.cd.incidenciasappfx.repository.IRolesRepository;
import com.cd.incidenciasappfx.repository.RolesRepositoryImpl;

import java.util.List;
import java.util.Optional;

/**
 * RolesServiceImpl.java
 *
 * @author CDAA
 */
public class RolesServiceImpl implements IRolesService {

    private final IRolesRepository rolesRepository;

    public RolesServiceImpl() {
        rolesRepository = new RolesRepositoryImpl();
    }

    @Override
    public List<Rol> findAll() {
        return rolesRepository.findAll();

    }

    @Override
    public Optional<Rol> save(Rol r) {
        return rolesRepository.save(r);
    }

    @Override
    public Optional<Rol> findById(Integer idRol) {
        return rolesRepository.findById(idRol);
    }

    @Override
    public Optional<Rol> update(Rol r) {
        return rolesRepository.update(r);
    }

    @Override
    public boolean delete(Rol r) {
        int id = r.getIdRol();
        return rolesRepository.delete(id);
    }

}
