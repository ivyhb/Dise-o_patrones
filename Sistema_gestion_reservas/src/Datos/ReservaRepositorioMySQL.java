/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import Negocio.entidades.Reserva;
import Negocio.enums.EstadoReserva;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author IVELISSE
 */
public class ReservaRepositorioMySQL implements ReservaRepositorio {

    @Override
    public int guardarSiDisponible(Reserva reserva) throws SQLException {
    //Verificar cruce de fechas
    if (existeCruceFechas(reserva.getHabitacion().getId(), reserva.getFechaEntrada(), reserva.getFechaSalida())){
        throw new IllegalStateException("La habitación ya está reservada en esas fechas");
    }
    //Guardar reserva
    String sql= "INSERT INTO reservas (huesped_id,habitacion_id, "
            +"fecha_entrada, fecha_salida, numero_huespedes, precio_noche, "
            +"observaciones,estado) VALUES (?,?,?,?,?,?,?,?)";
        
    try (Connection cn = ConexionMySQL.getConnection()) {
        if (cn == null) {
            throw new SQLException("No hay conexion con MySQL");
        }
        try (PreparedStatement ps = cn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,reserva.getHuesped().getId());
            ps.setInt(2,reserva.getHabitacion().getId());
            ps.setDate(3,Date.valueOf(reserva.getFechaEntrada()));
            ps.setDate(4,Date.valueOf(reserva.getFechaSalida()));
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
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    @Override
    public Reserva buscarPorId(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean actualizarEstado(int id, EstadoReserva estado) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean existeCruceFechas(int habitacionId, LocalDate entrada, LocalDate salida) throws SQLException {
        String sql= "SELECT COUNT(*) FROM reservas"
                +"WHERE habitacion_id=?"
                +"AND estado IN('PENDIENTE','CONFIRMADA')"
                +"AND fecha_entrada < ? AND fecha_salida > ?";
        try (Connection cn = ConexionMySQL.getConnection()) {
            if (cn == null) {
                throw new SQLException("No hay conexion con MySQL");
            }
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, habitacionId);
                ps.setDate(2, Date.valueOf(salida));
                ps.setDate(3, Date.valueOf(entrada));
                try (ResultSet rs= ps.executeQuery()){
                    rs.next();
                    return rs.getInt(1)>0;
                }
            }
        }
    }
    
}
