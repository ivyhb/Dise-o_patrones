/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import java.time.LocalDate;
import Negocio.entidades.Reserva;
import Negocio.enums.EstadoReserva;
import java.util.List;
import java.sql.SQLException;
/**
 *
 * @author IVELISSE
 */
public interface ReservaRepositorio {
    
    //Guarda reserva nueva si la habitación está libre.
    int guardarSiDisponible (Reserva reserva) throws SQLException;
    
    //Listar reservas
    List <Reserva> listar() throws SQLException;
    
    //Buscar reserva por ID 
    Reserva buscarPorId(int id) throws SQLException;
    
    //Cambiar estado de reserva 
    boolean actualizarEstado(int id, EstadoReserva estado) throws SQLException;
    
    //Consultar si una habitación ya está ocupada en un rango de fechas
    boolean existeCruceFechas(int habitacionId, LocalDate entrada, LocalDate salida)throws SQLException;
    
}
