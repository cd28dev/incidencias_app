/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.Rol;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author CDAA
 */
public interface IRolesService {
    Optional<Rol> save(Rol r);
    Optional<Rol> findById(Integer idRol);
    List<Rol> findAll();
    Optional<Rol> update(Rol r);
    boolean delete(Rol r);
}
