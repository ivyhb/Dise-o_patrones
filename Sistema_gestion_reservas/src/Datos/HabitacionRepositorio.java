/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Datos;

import Negocio.entidades.Habitacion;
import Negocio.enums.EstadoHabitacion;
import java.util.List;
import java.sql.SQLException;

/**
 *
 * @author LUIS
 */
public interface HabitacionRepositorio {
    
    int guardar(Habitacion habitacion) throws SQLException;

    List<Habitacion> listar() throws SQLException;

    Habitacion buscarPorId(int id) throws SQLException;

    boolean actualizar(Habitacion habitacion) throws SQLException;

    boolean cambiarEstado(int id, EstadoHabitacion estado) throws SQLException; //cambie parámetro de String a Enum

    boolean eliminar(int id) throws SQLException;
}
