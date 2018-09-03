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
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaCuerposAcademicos;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbCuerposAcademicos {
    /**
     * Método que devuelve dos listas de entidades:
     * Entidades de - Cuerpo Académico y - DTO Cuerpo Académico
     * Los cuales son utilizados para mostrar la previsualización de los datos ingresados en el archivo de excel y el almacenamiento de los mismos.
     * @param rutaArchivo   Parámetro que contiene la ruta del archivo que se ocupará para el llenado de las listas
     * @param ejercicio Parámetro que incluye el ejercicio del registro
     * @param mes   Parámetro que permite la clasificación por mes
     * @return
     * @throws Throwable 
     */
    public ListaCuerposAcademicos getListaCuerposAcademicos(String rutaArchivo) throws  Throwable;
    public ListaCuerposAcademicos getListaCuerpAcadIntegrantes(String rutaArchivo) throws Throwable;
    public ListaCuerposAcademicos getListaCuerpAcadLineas(String rutaArchivo) throws Throwable;
    public void guardaCuerposAcademicos(ListaCuerposAcademicos listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaCuerpAcadIntegrantes(ListaCuerposAcademicos listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaCuerpAcadLineas(ListaCuerposAcademicos listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public Integer getRegistroCuerpoAcademicoEspecifico(String cuerpoAcademico);
    public CuerposAcademicosRegistro getCuerpoAcademico(CuerposAcademicosRegistro cuerpoAcademico);
    public CuerpacadIntegrantes getCuerpacadIntegrantes(CuerpacadIntegrantes cuerpacadIntegrante);
    public CuerpacadLineas getCuerpacadLineas(CuerpacadLineas cuerpacadLinea);
    /**
     * Método que devuelve la lista completa del catalogo CuerpacadDisciplinas para el llenado y actualización de la plantilla correspondiente
     * @return 
     */
    public List<CuerpacadDisciplinas> getCuerpacadDisciplinas();
    /**
     * Método que devuelve la lista completa del catalogo CuerpacadAreasEstudio para el llenado y actualización de la plantilla correspondiente
     * @return 
     */
    public List<CuerpacadAreasEstudio> getCuerpacadAreasEstudio();
    
     public List<CuerposAcademicosRegistro> getCuerposAcademicosAct();
}
