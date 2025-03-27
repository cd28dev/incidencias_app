/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.Urbanizacion;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author CDAA
 */
public interface IUrbService {

    Optional<Urbanizacion> save(Urbanizacion u);

    Optional<Urbanizacion> findById(Integer id);

    List<Urbanizacion> findAll();

    Optional<Urbanizacion> update(Urbanizacion u);

    boolean delete(Urbanizacion u);

    List<Urbanizacion> findBySector(String nameSector);

}
