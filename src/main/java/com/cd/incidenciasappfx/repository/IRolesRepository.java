/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.models.Rol;
import com.cd.incidenciasappfx.models.Usuario;
import java.util.List;

/**
 *
 * @author CDAA
 */
public interface IRolesRepository {

    List<Rol> findAll();
}
