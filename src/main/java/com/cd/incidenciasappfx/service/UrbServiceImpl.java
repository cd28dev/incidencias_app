package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.models.Urbanizacion;
import com.cd.incidenciasappfx.repository.IUrbRepository;
import com.cd.incidenciasappfx.repository.UrbRepositoryImpl;
import java.util.List;
import java.util.Optional;

/**
 * UrbServiceImpl.java
 *
 * @author CDAA
 */
public class UrbServiceImpl implements IUrbService {

    private IUrbRepository urbRepository;

    public UrbServiceImpl() {
        urbRepository = new UrbRepositoryImpl();
    }

    @Override
    public Optional<Urbanizacion> save(Urbanizacion u) {
        return urbRepository.save(u);
    }

    @Override
    public Optional<Urbanizacion> findById(Integer id) {
        return urbRepository.findById(id);
    }

    @Override
    public List<Urbanizacion> findAll() {
        return urbRepository.findAll();
    }

    @Override
    public Optional<Urbanizacion> update(Urbanizacion u) {
        switch (u.getSector().getNombre()) {
            case "Zona N°1" -> {
                u.getSector().setId(1);
            }
            case "Zona N°2" -> {
                u.getSector().setId(2);
            }
            case "Zona N°3" -> {
                u.getSector().setId(3);
            }
            case "Zona N°4" -> {
                u.getSector().setId(4);
            }
            case "Zona N°5" -> {
                u.getSector().setId(5);
            }
            case "Zona N°6" -> {
                u.getSector().setId(6);
            }
            default -> {
            }
        }
        return urbRepository.update(u);
    }

    @Override
    public boolean delete(Urbanizacion u) {
        int id = u.getId();
        return urbRepository.delete(id);
    }

}
