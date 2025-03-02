package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.Rol;
import com.cd.incidenciasappfx.repository.IRolesRepository;
import com.cd.incidenciasappfx.repository.RolesRepositoryImpl;
import java.util.List;

/**
 * RolesServiceImpl.java
 *
 * @author CDAA
 */
public class RolesServiceImpl implements IRolesService {

    private IRolesRepository rolesRepository;

    public RolesServiceImpl() {
        rolesRepository = new RolesRepositoryImpl();
    }

    @Override
    public List<Rol> findAll() {
        return rolesRepository.findAll();

    }

}
