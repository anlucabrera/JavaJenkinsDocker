/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAcademicos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAcademicosPersonal;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.ca.DTOProductosAcademicos;
import mx.edu.utxj.pye.siip.dto.ca.DTOProductosAcademicosPersonal;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbProductosAcademicos {
    /**
     * Método que devuelve dos listas de entidades:
     * Entidades de - Productos Académicos y - DTO Productos Académicos
     * Los cuales son utilizados para mostrar la previsualización de los datos ingresados en el archivo de excel y el almacenamiento de los mismos.
     * @param rutaArchivo   Parámetro que contiene la ruta del archivo que se ocupará para el llenado de las listas
     * @param ejercicio Parámetro que incluye el ejercicio del registro
     * @param area  Parámetro que contiene el área, la cual registra la información
     * @param mes   Parámetro que permite la clasificación por mes
     * @return
     * @throws Throwable 
     */
    public List<DTOProductosAcademicos> getListaProductosAcademicos(String rutaArchivo) throws Throwable;
    public List<DTOProductosAcademicosPersonal> getListaProductosAcademicosPersonal(String rutaArchivo) throws Throwable;
    public void guardaProductosAcademicos( List<DTOProductosAcademicos> listaProductosAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    public void guardaProductosAcademicosPersonal(List<DTOProductosAcademicosPersonal> listaProductosAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    public Integer getRegistroProductoAcademicoEspecifico(String productoAcademico);
    public ProductosAcademicos getProductoAcademico(ProductosAcademicos productoAcademico);
    public ProductosAcademicosPersonal getProductoAcademicoPersonal(ProductosAcademicosPersonal productoAcademicoPersonal);
    
    /**
     * Método que se ocupa para el filtrado de Productos Académicos por Ejercicio, Mes y Área el cual es mostrado para la consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return  Regresa una lista de registros de Productos Académicos que serán ocupado para consulta y eliminación
     * @throws Throwable 
     */
    public List<DTOProductosAcademicos> getFiltroProductosAcademicosEjercicioMesArea(Short ejercicio, String mes, Short area) throws Throwable;
    
    /**
     * Método que se ocupa para el filtrado de Productos Académicos Personal por Ejercicio, Mes y Área el cual es mostrado para la consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return  Regresa una lista de registros de Productos Académicos Personal que serán ocupado para consulta y eliminación
     * @throws Throwable 
     */
    public List<DTOProductosAcademicosPersonal> getFiltroProductosAcademicosPersonalEjercicioMesArea(Short ejercicio, String mes, Short area)throws Throwable;
    
    /**
     * Método que devuelve la lista de registros encontrados en caso de que el registro principal contenga participantes, esta lista será ocupada para eliminar los participantes
     * @param productoAcademico Entity que permite la búsqueda de los participantes
     * @return  Devuelve una lista de enteros que contiene las claves de los registros encontrados
     * @throws Throwable 
     */
    public List<Integer> buscaRegistrosPersonalProductosAcademicos(ProductosAcademicos productoAcademico) throws Throwable;
}
