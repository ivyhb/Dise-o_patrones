/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Datos;

import Negocio.entidades.Huesped;
import java.util.List;
import java.sql.SQLException;


/**
 *
 * @author LUIS
 */
public interface HuespedRepositorio {
    // Registrar huésped
    int guardar(Huesped huesped) throws SQLException;

    // Listar todos los huéspedes
    List<Huesped> listar() throws SQLException;

    // Buscar huésped por ID
    Huesped buscarPorId(int id) throws SQLException;

    //Buscar huésped por documento
    Huesped buscarPorDocumento(String documento) throws SQLException;
    
    // Actualizar huésped
    boolean actualizar(Huesped huesped) throws SQLException;

    // Eliminar huésped
    boolean eliminar(int id) throws SQLException;
}
