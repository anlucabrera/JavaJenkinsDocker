/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadAreasEstudio;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadDisciplinas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadIntegrantes;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadLineas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerposAcademicosRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerpAcadIntegrantes;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerposAcademicosR;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbCuerposAcademicos {

    /**
     * Método que devuelve dos listas de entidades: Entidades de - Cuerpo
     * Académico y - DTO Cuerpo Académico Los cuales son utilizados para mostrar
     * la previsualización de los datos ingresados en el archivo de excel y el
     * almacenamiento de los mismos.
     *
     * @param rutaArchivo Parámetro que contiene la ruta del archivo que se
     * ocupará para el llenado de las listas
     * @param ejercicio Parámetro que incluye el ejercicio del registro
     * @param mes Parámetro que permite la clasificación por mes
     * @return
     * @throws Throwable
     */
    public List<DTOCuerposAcademicosR> getListaCuerposAcademicos(String rutaArchivo) throws Throwable;

    public List<DTOCuerpAcadIntegrantes> getListaCuerpAcadIntegrantes(String rutaArchivo) throws Throwable;

    public List<CuerpacadLineas> getListaCuerpAcadLineas(String rutaArchivo) throws Throwable;

    public void guardaCuerposAcademicos(List<DTOCuerposAcademicosR> listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;

    public void guardaCuerpAcadIntegrantes(List<DTOCuerpAcadIntegrantes> listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;

    public void guardaCuerpAcadLineas(List<CuerpacadLineas> listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;

    public Integer getRegistroCuerpoAcademicoEspecifico(String cuerpoAcademico);

    public CuerposAcademicosRegistro getCuerpoAcademico(CuerposAcademicosRegistro cuerpoAcademico);

    public CuerpacadIntegrantes getCuerpacadIntegrantes(CuerpacadIntegrantes cuerpacadIntegrante);

    public CuerpacadLineas getCuerpacadLineas(CuerpacadLineas cuerpacadLinea);

    /**
     * Método que devuelve la lista completa del catálogo CuerpacadDisciplinas
     * para el llenado y actualización de la plantilla correspondiente
     *
     * @return Devuelve una lista general de CuerpacadDisciplinas
     */
    public List<CuerpacadDisciplinas> getCuerpacadDisciplinas();

    /**
     * Método que devuelve la lista completa del catálogo CuerpacadAreasEstudio
     * para el llenado y actualización de la plantilla correspondiente
     *
     * @return Devuelve una lista general de CuerpacadAreasEstudio
     */
    public List<CuerpacadAreasEstudio> getCuerpacadAreasEstudio();

    /**
     * Método que devuelve la lista completa del catálogo
     * CuerposAcademicosRegistro para el llenado y actualización de la plantilla
     * correspondiente
     *
     * @return Devuelve una lista general de CuerposAcademicosRegistro
     */
    public List<CuerposAcademicosRegistro> getCuerposAcademicosAct();

    /**
     *  Método que filtra el registro de los cuerpos académicos por medio de Ejercicio, Mes y Área el cual es mostrado para la consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return  Regresa una lista de registros de Cuerpos Académicos que serán ocupados para consulta y eliminación
     * @throws Throwable
     */
    public List<DTOCuerposAcademicosR> getFiltroCuerposAcademicosEjercicioMesArea(Short ejercicio, Short area) throws Throwable;
    
    /**
     *  Método que filtra el registro de DTOCuerpAcadIntegrantes por medio de Ejercicio, Mes y Área el cual es mostrado para la consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return  Regresa una lista de registros de DTOCuerpAcadIntegrantes que serán ocupados para consulta y eliminación
     * @throws Throwable
     */
    public List<DTOCuerpAcadIntegrantes> getFiltroCuerpAcadIntegrantesEjercicioMesArea(Short ejercicio, Short area) throws Throwable;
    
    /**
     *  Método que filtra el registro de los CuerpacadLineas por medio de Ejercicio, Mes y Área el cual es mostrado para la consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return  Regresa una lista de registros de CuerpacadLineas que serán ocupados para consulta y eliminación
     * @throws Throwable
     */
    public List<CuerpacadLineas> getFiltroCuerpAcadLineasEjercicioMesArea(Short ejercicio, Short area) throws Throwable;
    
    /**
     * Método que devuelve la lista de registros encontrados en caso de que el registro principal contenga participantes, esta lista será ocupada para eliminar los participantes
     * @param cuerposAcademicosRegistro Entity que permite la búsqueda de los participantes
     * @return  Devuelve una lista de enteros que contiene las claves de los registros encontrados
     * @throws Throwable 
     */
    public List<Integer> buscaRegistrosCuerpAcadIntegrantesByCuerpAcad(CuerposAcademicosRegistro cuerposAcademicosRegistro) throws Throwable;
    
    /**
     * Método que devuelve la lista de registros encontrados en caso de que el registro principal contenga participantes, esta lista será ocupada para eliminar los participantes
     * @param cuerposAcademicosRegistro Entity que permite la búsqueda de los participantes
     * @return  Devuelve una lista de enteros que contiene las claves de los registros encontrados
     * @throws Throwable 
     */
    public List<Integer> buscaRegistrosCuerpAcadLineasByCuerpAcad(CuerposAcademicosRegistro cuerposAcademicosRegistro) throws Throwable;
}
