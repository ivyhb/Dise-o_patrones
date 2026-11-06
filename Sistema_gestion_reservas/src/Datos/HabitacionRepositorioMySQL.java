/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import Negocio.entidades.Habitacion;
import Negocio.enums.EstadoHabitacion;
import Negocio.enums.TipoHabitacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LUIS
 */
public class HabitacionRepositorioMySQL implements HabitacionRepositorio{
    // REGISTRAR HABITACION
    @Override
    public int guardar(Habitacion habitacion) throws SQLException {

        String sql = "INSERT INTO habitaciones (numero, tipo, estado) "
                + "VALUES (?, ?, ?)";

        try (Connection cn = ConexionMySQL.getConnection()) {

            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }

            try (PreparedStatement ps = cn.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, habitacion.getNumero());
                ps.setString(2, habitacion.getTipo().name());
                ps.setString(3, habitacion.getEstado().name());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        int id = rs.getInt(1);
                        habitacion.setId(id);
                        return id;
                    }
                }
            }
        }
        throw new SQLException("No se obtuvo el id generado"); // agregué throw en vez de return 0
    }

    // LISTAR HABITACIONES
    @Override
    public List<Habitacion> listar() throws SQLException {

        List<Habitacion> lista = new ArrayList<>();

        String sql = "SELECT * FROM habitaciones";

        try (Connection cn = ConexionMySQL.getConnection()) {

            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }

            try (PreparedStatement ps = cn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Habitacion h = new Habitacion();

                    h.setId(rs.getInt("id"));
                    h.setNumero(rs.getString("numero"));

                    h.setTipo(TipoHabitacion.valueOf(
                            rs.getString("tipo")));

                    h.setEstado(EstadoHabitacion.valueOf(
                            rs.getString("estado")));

                    lista.add(h);
                }
            }
        }

        return lista;
    }

    // BUSCAR HABITACION POR ID
    @Override
    public Habitacion buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM habitaciones WHERE id = ?";

        try (Connection cn = ConexionMySQL.getConnection()) {

            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }

            try (PreparedStatement ps = cn.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        Habitacion h = new Habitacion();

                        h.setId(rs.getInt("id"));
                        h.setNumero(rs.getString("numero"));

                        h.setTipo(TipoHabitacion.valueOf(
                                rs.getString("tipo")));

                        h.setEstado(EstadoHabitacion.valueOf(
                                rs.getString("estado")));

                        return h;
                    }
                }
            }
        }

        return null;
    }

    // ACTUALIZAR HABITACION
    @Override
    public boolean actualizar(Habitacion habitacion)
            throws SQLException {

        String sql = "UPDATE habitaciones SET numero=?, tipo=?, "
                + "estado=? WHERE id=?";

        try (Connection cn = ConexionMySQL.getConnection()) {

            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }

            try (PreparedStatement ps = cn.prepareStatement(sql)) {

                ps.setString(1, habitacion.getNumero());
                ps.setString(2, habitacion.getTipo().name());
                ps.setString(3, habitacion.getEstado().name());
                ps.setInt(4, habitacion.getId());

                return ps.executeUpdate() > 0;
            }
        }
    }

    // CAMBIAR ESTADO
    @Override
    public boolean cambiarEstado(int id, EstadoHabitacion estado)
            throws SQLException {

        String sql = "UPDATE habitaciones SET estado=? WHERE id=?";

        try (Connection cn = ConexionMySQL.getConnection()) {

            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }

            try (PreparedStatement ps = cn.prepareStatement(sql)) {

                ps.setString(1, estado.name()); //pasa de enum a texto
                ps.setInt(2, id);

                return ps.executeUpdate() > 0;
            }
        }
    }

    // ELIMINAR HABITACION
    @Override
    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM habitaciones WHERE id=?";

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
