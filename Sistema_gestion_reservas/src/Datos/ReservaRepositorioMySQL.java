/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import Negocio.entidades.*;
import Negocio.enums.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IVELISSE
 */
public class ReservaRepositorioMySQL implements ReservaRepositorio {

    @Override
    public int guardarSiDisponible(Reserva reserva) throws SQLException {
        //Verificar cruce de fechas
        if (existeCruceFechas(reserva.getHabitacion().getId(), 
            reserva.getFechaEntrada(), reserva.getFechaSalida())) {
            throw new IllegalStateException("La habitación ya está reservada en esas fechas");
        }
        //Guardar reserva
        String sql = "INSERT INTO reservas (huesped_id,habitacion_id, "
                + "fecha_entrada, fecha_salida, numero_huespedes, precio_noche, "
                + "observaciones,estado) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection cn = ConexionMySQL.getConnection()) {
            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }
            try (PreparedStatement ps = cn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, reserva.getHuesped().getId());
                ps.setInt(2, reserva.getHabitacion().getId());
                ps.setDate(3, Date.valueOf(reserva.getFechaEntrada()));
                ps.setDate(4, Date.valueOf(reserva.getFechaSalida()));
                ps.setInt(5, reserva.getNumeroHuespedes());
                ps.setDouble(6, reserva.getPrecioNoche());
                ps.setString(7, reserva.getObservaciones());
                ps.setString(8, reserva.getEstado().name());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        reserva.setId(id);
                        return id;
                    }
                }
            }
        }
        throw new SQLException("No se obtuvo el id generado");
    }

    @Override
    public List<Reserva> listar() throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.id, r.fecha_entrada, r.fecha_salida, r.numero_huespedes, "
                + "r.precio_noche, r.observaciones, r.estado AS estado_reserva, "
                + "h.id AS huesped_id, h.nombre, h.documento, h.telefono, h.email, "
                + "hab.id AS habitacion_id, hab.numero, hab.tipo, "
                + "hab.estado AS estado_habitacion "
                + "FROM reservas r "
                + "JOIN huespedes h ON r.huesped_id = h.id "
                + "JOIN habitaciones hab ON r.habitacion_id = hab.id ";
        try (Connection cn = ConexionMySQL.getConnection()) {
            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }
            try (PreparedStatement ps = cn.prepareStatement(sql); 
                ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(construirReserva(rs));
                }
            }
            return lista;
        }
    }

    @Override
    public Reserva buscarPorId(int id) throws SQLException {
        String sql = "SELECT r.id, r.fecha_entrada, r.fecha_salida, r.numero_huespedes, "
                + "r.precio_noche, r.observaciones, r.estado AS estado_reserva, "
                + "h.id AS huesped_id, h.nombre, h.documento, h.telefono, h.email, "
                + "hab.id AS habitacion_id, hab.numero, hab.tipo, "
                + "hab.estado AS estado_habitacion "
                + "FROM reservas r "
                + "JOIN huespedes h ON r.huesped_id = h.id "
                + "JOIN habitaciones hab ON r.habitacion_id = hab.id "
                + "WHERE r.id = ?";
        try (Connection cn = ConexionMySQL.getConnection()) {
            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return (construirReserva(rs));
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean actualizarEstado(int id, EstadoReserva estado) throws SQLException {
        String sql= "UPDATE reservas SET estado = ? WHERE id = ?";
        
       try (Connection cn = ConexionMySQL.getConnection()) {
            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }
            try (PreparedStatement ps = cn.prepareStatement(sql)) {  
                ps.setString(1, estado.name());
                ps.setInt(2,id);
                return ps.executeUpdate()>0;
            }
       }
    }

    @Override
    public boolean existeCruceFechas(int habitacionId, LocalDate entrada, LocalDate salida) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservas"
                + "WHERE habitacion_id=?"
                + "AND estado IN('PENDIENTE','CONFIRMADA')"
                + "AND fecha_entrada < ? AND fecha_salida > ?";
        try (Connection cn = ConexionMySQL.getConnection()) {
            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, habitacionId);
                ps.setDate(2, Date.valueOf(salida));
                ps.setDate(3, Date.valueOf(entrada));
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getInt(1) > 0;
                }
            }
        }
    }

    //Convertir fila actual del ResultSet en objeto Reserva
    private Reserva construirReserva(ResultSet rs) throws SQLException {
        Huesped huesped = new Huesped(
                rs.getInt("huesped_id"),
                rs.getString("nombre"),
                rs.getString("documento"),
                rs.getString("telefono"),
                rs.getString("email"));

        Habitacion habitacion = new Habitacion(
                rs.getInt("habitacion_id"),
                rs.getString("numero"),
                TipoHabitacion.valueOf(rs.getString("tipo")),
                EstadoHabitacion.valueOf(rs.getString("estado_habitacion")));

        return new Reserva(
                rs.getInt("id"),
                huesped,
                habitacion,
                rs.getDate("fecha_entrada").toLocalDate(),
                rs.getDate("fecha_salida").toLocalDate(),
                rs.getInt("numero_huespedes"),
                rs.getDouble("precio_noche"),
                rs.getString("observaciones"),
                EstadoReserva.valueOf(rs.getString("estado_reserva")));
    }
}
