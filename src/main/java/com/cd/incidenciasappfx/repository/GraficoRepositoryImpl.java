package com.cd.incidenciasappfx.repository;

import com.cd.incidenciasappfx.dto.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GraficoRepositoryImpl implements IGraficoRepository {

    @Override
    public List<OcurrenciaDTO> obtenerIncidenciasPorTipoOcurrencia() throws SQLException {
        List<OcurrenciaDTO> lista = new ArrayList<>();
        try (Connection conn = JpaUtil.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL sp_incidencias_por_ocurrencia()}");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new OcurrenciaDTO(
                        rs.getString("tipo_ocurrencia"),
                        rs.getInt("total")
                ));
            }
        }
        return lista;
    }

    @Override
    public List<UrbanizacionDTO> obtenerIncidenciasPorUrbanizacion() throws SQLException {
        List<UrbanizacionDTO> lista = new ArrayList<>();
        try (Connection conn = JpaUtil.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL sp_incidencias_por_urbanizacion()}");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new UrbanizacionDTO(
                        rs.getString("urbanizacion"),
                        rs.getInt("total")
                ));
            }
        }
        return lista;
    }

    @Override
    public List<IntervencionDTO> obtenerIntervencionesPorTipo() throws SQLException {
        List<IntervencionDTO> lista = new ArrayList<>();
        try (Connection conn = JpaUtil.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL sp_incidencias_por_intervencion()}");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new IntervencionDTO(
                        rs.getString("tipo_intervencion"),
                        rs.getInt("total")
                ));
            }
        }
        return lista;
    }

    @Override
    public List<ServicioDTO> obtenerServiciosPorTipo() throws SQLException {
        List<ServicioDTO> lista = new ArrayList<>();
        try (Connection conn = JpaUtil.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL sp_incidencias_por_servicio()}");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new ServicioDTO(
                        rs.getString("servicio"),
                        rs.getInt("total")
                ));
            }
        }
        return lista;
    }

    @Override
    public List<ApoyoDTO> obtenerIncidenciasConApoyoPolicial() throws SQLException {
        List<ApoyoDTO> lista = new ArrayList<>();
        try (Connection conn = JpaUtil.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL sp_incidencias_con_apoyo()}");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new ApoyoDTO(
                        rs.getString("tipo_apoyo"),
                        rs.getInt("total")
                ));
            }
        }
        return lista;
    }

    @Override
    public List<DelitoDTO> obtenerIncidenciasPorDelito() throws SQLException {
        List<DelitoDTO> lista = new ArrayList<>();
        try (Connection conn = JpaUtil.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL sp_incidencias_por_delito()}");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new DelitoDTO(
                        rs.getString("delito"),
                        rs.getInt("total")
                ));
            }
        }
        return lista;
    }

    @Override
    public List<TemporalDTO> obtenerEvolucionMensualDeIncidencias() throws SQLException {
        List<TemporalDTO> lista = new ArrayList<>();
        try (Connection conn = JpaUtil.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL sp_incidencias_por_mes()}");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new TemporalDTO(
                        rs.getInt("anio"),
                        rs.getInt("mes"),
                        rs.getInt("total")
                ));
            }
        }
        return lista;
    }

    @Override
    public List<SectorDTO> obtenerIncidenciasPorSector() throws SQLException {
        List<SectorDTO> lista = new ArrayList<>();
        try (Connection conn = JpaUtil.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL sp_incidencias_por_sector()}");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new SectorDTO(
                        rs.getString("sector"),
                        rs.getInt("total")
                ));
            }
        }
        return lista;
    }

}

