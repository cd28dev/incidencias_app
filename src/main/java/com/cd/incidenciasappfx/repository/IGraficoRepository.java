package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.dto.*;

import java.sql.SQLException;
import java.util.List;

public interface IGraficoRepository {

    List<OcurrenciaDTO> obtenerIncidenciasPorTipoOcurrencia() throws SQLException;

    List<UrbanizacionDTO> obtenerIncidenciasPorUrbanizacion() throws SQLException;

    List<IntervencionDTO> obtenerIntervencionesPorTipo() throws SQLException;

    List<ServicioDTO> obtenerServiciosPorTipo() throws SQLException;

    List<ApoyoDTO> obtenerIncidenciasConApoyoPolicial() throws SQLException;

    List<DelitoDTO> obtenerIncidenciasPorDelito() throws SQLException;

    List<TemporalDTO> obtenerEvolucionMensualDeIncidencias() throws SQLException;

    List<SectorDTO> obtenerIncidenciasPorSector() throws SQLException;

}
