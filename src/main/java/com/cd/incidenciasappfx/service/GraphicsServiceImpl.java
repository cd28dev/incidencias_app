package com.cd.incidenciasappfx.service;

import com.cd.incidenciasappfx.dto.*;
import com.cd.incidenciasappfx.repository.GraficoRepositoryImpl;
import com.cd.incidenciasappfx.repository.IGraficoRepository;

import java.sql.SQLException;
import java.util.List;

public class GraphicsServiceImpl implements IGraphicService{

    private  IGraficoRepository graficoRepository;

    public GraphicsServiceImpl() {
        this.graficoRepository = new GraficoRepositoryImpl();
    }

    @Override
    public List<OcurrenciaDTO> obtenerIncidenciasPorTipoOcurrencia() throws SQLException {
        return graficoRepository.obtenerIncidenciasPorTipoOcurrencia();
    }

    @Override
    public List<UrbanizacionDTO> obtenerIncidenciasPorUrbanizacion() {
        return List.of();
    }

    @Override
    public List<IntervencionDTO> obtenerIntervencionesPorTipo() throws SQLException {

        return graficoRepository.obtenerIntervencionesPorTipo();
    }

    @Override
    public List<ServicioDTO> obtenerServiciosPorTipo() throws SQLException {

        return graficoRepository.obtenerServiciosPorTipo();
    }

    @Override
    public List<ApoyoDTO> obtenerIncidenciasConApoyoPolicial() throws SQLException {

        return graficoRepository.obtenerIncidenciasConApoyoPolicial();
    }

    @Override
    public List<DelitoDTO> obtenerIncidenciasPorDelito() {
        return List.of();
    }

    @Override
    public List<TemporalDTO> obtenerEvolucionMensualDeIncidencias(){
        return List.of();
    }

    @Override
    public List<SectorDTO> obtenerIncidenciasPorSector() {
        return List.of();
    }
}
