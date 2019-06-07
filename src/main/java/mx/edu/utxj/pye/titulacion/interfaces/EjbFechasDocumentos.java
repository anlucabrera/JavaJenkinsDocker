/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.interfaces;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.FechasDocumentos;
import mx.edu.utxj.pye.titulacion.dto.dtoFechasDocumentos;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbFechasDocumentos {
    
     /**
     * Obtiene la lista de fechas de documentos que se encuentran registrados
     * @param generacionSeleccionada
     * @return Lista de dtoFechasDocumentos
     */
    public List<dtoFechasDocumentos> getListaFechasDocumentosGeneracion(Generaciones generacionSeleccionada);
    
     /**
     * Elimina de la base el registro seleccionado
     * @param fecDocs
     * @return valor falso o verdadero según sea el caso
     */
    public Boolean eliminarFecDocsGeneracion(dtoFechasDocumentos fecDocs);
    
     /**
     * Busca si hay expedientes que tienen asignado fechas de documentos
     * @param fecDocs
     * @return expedientes de titulación
     */
    public List<ExpedientesTitulacion> buscarExpConFecDocs(dtoFechasDocumentos fecDocs);
    
    /**
     * Actualiza el registro seleccionado
     * @param dtoFecDocs
     * @return entity.
     * @throws java.lang.Throwable
     */
    public dtoFechasDocumentos actualizarFecDocumentos(dtoFechasDocumentos dtoFecDocs) throws Throwable;
    
    /**
     * Guarda un registro nuevo en Fechas Documentos
     * @param fechasDocumentos
     * @return entity.
     * @throws java.lang.Throwable
     */
    public FechasDocumentos guardarFecDocumentos(FechasDocumentos fechasDocumentos) throws Throwable;
    
    /**
     * Obtiene la lista de generaciones que tienen registros en fechas de documentos
     * @return Lista de generaciones
     */
    public List<Generaciones> getGeneracionesRegistradas();
}
