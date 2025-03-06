/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.Sector;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author CDAA
 */
public interface ISectorService {
    Optional<Sector> save(Sector r);
    Optional<Sector> findById(Integer idSector);
    List<Sector> findAll();
    Optional<Sector> update(Sector s);
    boolean delete(Sector s);
}
