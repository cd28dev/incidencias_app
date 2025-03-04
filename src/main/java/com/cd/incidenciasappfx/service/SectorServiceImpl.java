
package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.Sector;
import com.cd.incidenciasappfx.repository.ISectorRepository;
import com.cd.incidenciasappfx.repository.SectorRepositoryImpl;
import java.util.List;
import java.util.Optional;


/**
 * SectorServiceImpl.java
 * 
 * @author CDAA
 */
public class SectorServiceImpl implements ISectorService{
    
    private ISectorRepository sectorRepository;
    
    public SectorServiceImpl(){
        sectorRepository = new SectorRepositoryImpl();
    }

    @Override
    public Optional<Sector> save(Sector s) {
        return sectorRepository.save(s);
    }

    @Override
    public Optional<Sector> findById(Integer idSector) {
        return sectorRepository.findById(idSector);
    }

    @Override
    public List<Sector> findAll() {
        return sectorRepository.findAll();
    }

    @Override
    public Optional<Sector> update(Sector s) {
        return sectorRepository.update(s);
    }

    @Override
    public boolean delete(int id) {
        return sectorRepository.delete(id);
    }

}
