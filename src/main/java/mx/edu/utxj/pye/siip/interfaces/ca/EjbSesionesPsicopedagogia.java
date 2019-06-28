/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.AreasConflicto;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.OtrosTiposSesionesPsicopedagogia;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.SesionIndividualMensualPsicopedogia;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbSesionesPsicopedagogia {
    
    /**
     * Método que devuelve una lista de tipo AreasCconflicto la cual se ocupará para guardar las sesiones individuales de Psicopedagogia
     * @return Lista de tipo AreasConflicto
     */
    public List<AreasConflicto> getListaAreasDeConflicto();
    
    /**
     * Método que devuelve una lista de tipo OtrosTiposSesionesPsicopedagogia la cual se ocupará para asignar a las sesiones de Psicopedagogia
     * @return Lista de tipo OtrosTiposSesionesPsicopedagogia
     */
    public List<OtrosTiposSesionesPsicopedagogia> getListaOtrosTiposSesionesPsicopedagogia();
    
    /**
     * Método que busca en base de datos el registro de SesionIndividualMensualPsicopedogia
     * @param simPsicopedagogia Utilizado como referencia para la búsqueda en base de datos
     * @return List SesionIndividualMensualPsicopedogia el cual contiene los valores completos de los registros encontrados.
     */
    public List<SesionIndividualMensualPsicopedogia> buscaSesionIndividualMensualPsicopedagogia(SesionIndividualMensualPsicopedogia simPsicopedagogia);
    
    /**
     * Método que busca en base de datos el registro de SesionIndividualMensualPsicopedogia que no están relacionados con programas educativos
     * @param simPsicopedagogia Utilizado como referencia para la búsqueda en base de datos
     * @return List SesionIndividualMensualPsicopedogia el cual contiene los valores completos de los registros encontrados.
     */
    public List<SesionIndividualMensualPsicopedogia> buscaSesionIndividualMensualPsicopedagogiaSPE(SesionIndividualMensualPsicopedogia simPsicopedagogia);
    
    /**
     * Método que almacena en base de datos el registro de una SesionIndividualMensualPsicopedogia
     * @param simPsicopedagogia Utilizado para almacenar el registro de tipo SesionIndividualMensualPsicopedogia
     * @param registrosTipo
     * @param ejesRegistro
     * @param area
     * @param eventosRegistros
     * @return Valor String, El cual contiene información del resultado de la operación solicitada
     */
    public String guardaSesionIndividualMensualPsicopedagogia(SesionIndividualMensualPsicopedogia simPsicopedagogia, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    
    /**
     * Método que edita en la base de datos el registro de una SesionIndividualMensualPsicopedogia
     * @param simPsicopedagogia Utilizado para actualizar los datos que ya se encuentran en la base de datos
     * @return Valor String, El cual contiene información del resultado de la operación solicitada
     */
    public String editaSesionIndividualMensualPsicopedagogia(SesionIndividualMensualPsicopedogia simPsicopedagogia);
    
}
