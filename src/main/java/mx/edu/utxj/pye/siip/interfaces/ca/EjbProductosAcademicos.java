/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAcademicos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAcademicosPersonal;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaProductosAcademicos;

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
    public ListaProductosAcademicos getListaProductosAcademicos(String rutaArchivo) throws Throwable;
    public ListaProductosAcademicos getListaProductosAcademicosPersonal(String rutaArchivo) throws Throwable;
    public void guardaProductosAcademicos(ListaProductosAcademicos listaProductosAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    public void guardaProductosAcademicosPersonal(ListaProductosAcademicos listaProductosAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    public Integer getRegistroProductoAcademicoEspecifico(String productoAcademico);
    public ProductosAcademicos getProductoAcademico(ProductosAcademicos productoAcademico);
    public ProductosAcademicosPersonal getProductoAcademicoPersonal(ProductosAcademicosPersonal productoAcademicoPersonal);
}
