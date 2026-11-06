/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import Negocio.entidades.Huesped;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author LUIS
 */
public class HuespedRepositorioMySQL implements HuespedRepositorio{
    // REGISTRAR HUESPED
    @Override
    public int guardar(Huesped huesped) throws SQLException {

        String sql = "INSERT INTO huespedes "
                + "(nombre, documento, telefono, email) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection cn = ConexionMySQL.getConnection()) {

            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }

            try (PreparedStatement ps =
                    cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, huesped.getNombre());
                ps.setString(2, huesped.getDocumento());
                ps.setString(3, huesped.getTelefono());
                ps.setString(4, huesped.getEmail());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        int id = rs.getInt(1);
                        huesped.setId(id);
                        return id;
                    }
                }
            }
        }
        throw new SQLException("No se obtuvo el id generado");    
    }

    // LISTAR HUESPEDES
    @Override
    public List<Huesped> listar() throws SQLException {

        List<Huesped> lista = new ArrayList<>();

        String sql = "SELECT * FROM huespedes";

        try (Connection cn = ConexionMySQL.getConnection()) {

            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }

            try (PreparedStatement ps = cn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Huesped h = new Huesped();

                    h.setId(rs.getInt("id"));
                    h.setNombre(rs.getString("nombre"));
                    h.setDocumento(rs.getString("documento"));
                    h.setTelefono(rs.getString("telefono"));
                    h.setEmail(rs.getString("email"));

                    lista.add(h);
                }
            }
        }

        return lista;
    }

    // BUSCAR POR ID
    @Override
    public Huesped buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM huespedes WHERE id = ?";

        try (Connection cn = ConexionMySQL.getConnection()) {

            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }

            try (PreparedStatement ps = cn.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        return new Huesped(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("documento"),
                                rs.getString("telefono"),
                                rs.getString("email")
                        );
                    }
                }
            }
        }

        return null;
    }
    
    // BUSCAR POR DOCUMENTO
    @Override
    public Huesped buscarPorDocumento(String documento) throws SQLException {
        String sql = "SELECT * FROM huespedes WHERE documento = ?";

        try (Connection cn = ConexionMySQL.getConnection()) {

            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }

            try (PreparedStatement ps = cn.prepareStatement(sql)) {

                ps.setString(1, documento);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        return new Huesped(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("documento"),
                                rs.getString("telefono"),
                                rs.getString("email")
                        );
                    }
                }
            }
        }

        return null;
    }
    
    // ACTUALIZAR HUESPED
    @Override
    public boolean actualizar(Huesped huesped) throws SQLException {

        String sql = "UPDATE huespedes SET nombre=?, documento=?, "
                + "telefono=?, email=? WHERE id=?";

        try (Connection cn = ConexionMySQL.getConnection()) {

            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }

            try (PreparedStatement ps = cn.prepareStatement(sql)) {

                ps.setString(1, huesped.getNombre());
                ps.setString(2, huesped.getDocumento());
                ps.setString(3, huesped.getTelefono());
                ps.setString(4, huesped.getEmail());
                ps.setInt(5, huesped.getId());

                return ps.executeUpdate() > 0;
            }
        }
    }

    // ELIMINAR HUESPED
    @Override
    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM huespedes WHERE id=?";

        try (Connection cn = ConexionMySQL.getConnection()) {

            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }

            try (PreparedStatement ps = cn.prepareStatement(sql)) {

                ps.setInt(1, id);

                return ps.executeUpdate() > 0;
            }
        }
    }
}
