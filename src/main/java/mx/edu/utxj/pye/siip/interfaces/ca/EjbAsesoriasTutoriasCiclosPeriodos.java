/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasMensualPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.siip.dto.ca.DTOAsesoriasTutoriasCicloPeriodos;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbAsesoriasTutoriasCiclosPeriodos {
    
    public List<DTOAsesoriasTutoriasCicloPeriodos> getListaAsesoriasTutorias(String rutaArchivo) throws Throwable;
    
    public void guardaAsesoriasTutorias(List<DTOAsesoriasTutoriasCicloPeriodos> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros, Integer claveTutor) throws Throwable;
    
    public AsesoriasTutoriasMensualPeriodosEscolares getRegistroAsesoriaTutoriaCicloPeriodo(AsesoriasTutoriasMensualPeriodosEscolares asesoriaTutoriaCicloPeriodo);
    
    /**
     * Obtiene la lista de periodos con registros de tutorías y asesorías.
     * @param registrosTipo Permite filtrar los registros por el tipo necesario.
     * @param eventoRegistro
     * @return Lista de periodos.
     */
    public List<PeriodosEscolares> getPeriodosConregistro(RegistrosTipo registrosTipo, EventosRegistros eventoRegistro);
    
    /**
     * Obtiene la lista de registros correspondientes al evento seleccionado.
     * @param evento Evento seleccionado.
     * @param claveArea Área a la que pertenece el usuario y que servirá como filtro.
     * @param periodo Periodo del evento y que servirá como filtro
     * @param registrosTipo Permite filtrar los registros por el tipo necesario.
     * @param actividad
     * @param claveTutor
     * @param claveAreaEmpleado
     * @return Lista de registros mensuales.
     */
    public List<DTOAsesoriasTutoriasCicloPeriodos> getListaRegistrosPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo, Short actividad, Integer claveTutor, Short claveAreaEmpleado);
    
    public List<AsesoriasTutoriasMensualPeriodosEscolares> getListaReporteGeneralAsesoriasTutorias();
    
    /**
     * Comprueba si el periodo del evento actual se encuentra en el periodo mas reciente, en caso de no encontrarlo obtiene el periodo correspondiente.
     * @param periodos Lista de periodos obtenidos con registros.
     * @param eventos Eventos del periodo mas reciente.
     * @param eventoActual Evento de registro actual.
     * @param registrosTipo Permite filtrar los registros por el tipo necesario.
     * @return Lista de periodos actualizados y eventos que inclyen evento actual del periodo correspondiente.
     * @throws mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException Se lanza en casi que se requiera un periodo que no existe aún en la base de datos.
     */
    public Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoActual, RegistrosTipo registrosTipo) throws PeriodoEscolarNecesarioNoRegistradoException;
    
    /**
     * Elimina un registro especificado.
     * @param registro Registro a eliminar.
     * @return Devuelve TRUE si se eliminó el registro o FALSE de lo contrario.
     */
    public Boolean eliminarRegistro(DTOAsesoriasTutoriasCicloPeriodos registro);
    
    /**
     * Obtiene la lista de evidencias del registro correspondiente.
     * @param registro Registro a obtener sus evidencias
     * @return Lista de evidencias detalle
     */
    public List<EvidenciasDetalle> getListaEvidenciasPorRegistro(DTOAsesoriasTutoriasCicloPeriodos registro);
    
    /**
     * Registra múltiples archivos como evidencias del registro especificado.
     * @param registro Registro al que se van a agreegar las evidencias.
     * @param archivos Lista de archivos de evidencias.
     * @return Regresa una entrada de mapa con la clave tipo boleana indicando si todos las evidencias se almacenaron y como valor la cantidad de evidencias registradas.
     */
    public Map.Entry<Boolean, Integer> registrarEvidenciasARegistro(DTOAsesoriasTutoriasCicloPeriodos registro, List<Part> archivos);
    
    /**
     * Elimina una evidencia de un registro, si la evidencia solo está asignada al registro especificado la elimina de la base de datos y del disco duro, de lo contrario solo la 
     * desliga del registro.
     * @param registro Registro al que está ligada la evidencia.
     * @param evidenciasDetalle Evidencia a desligar o eliminar
     * @return Regresa TRUE si la evidencia se eliminó, FALSE de lo contrario o NULL si solo se desligó.
     */
    public Boolean eliminarEvidenciaEnRegistro(DTOAsesoriasTutoriasCicloPeriodos registro, EvidenciasDetalle evidenciasDetalle);
    
    /**
     * Obtiene la referencia a la actividad que está alineada con el registro.
     * @param registro Registro del cual se desea conocer su actividad alineada.
     * @return Devuelve la referencia a la actividad alineada o null si es que no ha sido alineada.
     */
    public ActividadesPoa getActividadAlineada(DTOAsesoriasTutoriasCicloPeriodos registro);
    
    /**
     * Alinea el registro con la actividad para que compartan evidencias.
     * @param actividad Actividad con la que se desea alinear.
     * @param registro Registro que se desea alinear.
     * @return Devuelve TRUE si la alineación se completó o FALSE de lo contrario.
     */
    public Boolean alinearRegistroActividad(ActividadesPoa actividad, DTOAsesoriasTutoriasCicloPeriodos registro);
    
    public Boolean eliminarAlineacion(DTOAsesoriasTutoriasCicloPeriodos registro);
    
    public AsesoriasTutoriasMensualPeriodosEscolares editaAsesoriaTutoriaMensualPeriodoEscolar(AsesoriasTutoriasMensualPeriodosEscolares asesoriaTutoriaMensual);
    
    public Boolean buscaAsesoriaTutoriaExistente(AsesoriasTutoriasMensualPeriodosEscolares asesoriaTutoria);
}
