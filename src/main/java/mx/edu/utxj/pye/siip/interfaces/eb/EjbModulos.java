/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.eb;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistroUsuario;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbModulos {

    /**
     * Interfaz para mostrar a los usuarios los Ejes Habilitados
     *
     * @param clavepersonal
     * @return
     * @throws java.lang.Throwable
     */
    public List<ModulosRegistroUsuario> getEjesRectores(Integer clavepersonal) throws Throwable;

    /**
     * Interfaz para mostrar a los usuario los registros habilitados
     *
     * @param eje
     * @param clavepersonal
     * @return
     * @throws java.lang.Throwable
     */
    public List<ModulosRegistroUsuario> getModulosRegistroUsuario(Integer eje, Integer clavepersonal) throws  Throwable;

    public Registros getRegistro(RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);

    /**
     * Obtiene el evento de registro según la fecha actual
     * @return Evento de registro actual
     * @throws EventoRegistroNoExistenteException Se lanza en caso que no exista un evento de registro para la fecha actual
     */
    public EventosRegistros getEventoRegistro() throws EventoRegistroNoExistenteException;

    public Boolean validaPeriodoRegistro(PeriodosEscolares periodoEscolar, Integer periodoRegistro);

    /**
     * Método que permite la eliminación del registro encontrado y listado en la
     * vista
     *
     * @param registro
     * @return
     */
    public Boolean eliminarRegistro(Integer registro);
    
    /**
     * Método que devuelve ejercicios fiscales en los cuales exista únicamente
     * información de módulos de registro, puede ser reutilizado para todos los
     * registros
     *
     * @param registrosTipo Lista de tipo de registros que se mostrarán en la
     * interfaz
     * @param area Determina la visualización del área principal de usuario
     * logueado
     * @return Devuelve una lista de tipo string que contiene únicamente
     * ejercicios fiscales
     */
    public List<Short> getEjercicioRegistros(List<Short> registrosTipo, AreasUniversidad area);

    /**
     * Método que devuelve meses en los cuales exista únicamente información de
     * módulos de registro, puede ser reutilizado para todos los registros en
     * los cuales aplique la consulta mensual
     *
     * @param ejercicio Determina que meses se mostrarán en la lista
     * @param registroTipo Determina que módulo de registro se esta buscando
     * @param area Determina la visualización del área principal de usuario
     * logueado
     * @return Devuelve una lista de tipo string que contiene únicamente meses
     */
    public List<String> getMesesRegistros(Short ejercicio, List<Short> registroTipo, AreasUniversidad area);

    /**
     * Método que permite la búsqueda del área superior en caso de que el área
     * del usuario no sea principal o bien no contenga POA.
     *
     * @param areaUsuario Permite la búsqueda del área superior
     * @return Regresa una entity de tipo AreaUniversidad el cual será ocupado
     * para: Guardar Registro, Filtrar Ejercicio, Filtrar Meses, Filtrar
     * Registros.
     */
    public AreasUniversidad getAreaUniversidadPrincipalRegistro(Short areaUsuario);

    /**
     * Método que elimina en masivo los participantes de un registro principal.
     *
     * @param registrosParticipantes Lista que contiene los registros que se
     * eliminarán
     * @return Devuelve un valor boolean para comprobar si los registros
     * realmente se han eliminado
     */
    public Boolean eliminarRegistroParticipantes(List<Integer> registrosParticipantes);

    /**
     * Obtiene la lista de evidencias_detalle del registro correspondiente
     *
     * @param registro Permite realizar la búsqueda de todas las evidencias que
     * corresponde a ese registro
     * @return Devuelve una lista de entidades de evidencias_detalle
     * @throws java.lang.Throwable
     */
    public List<EvidenciasDetalle> getListaEvidenciasPorRegistro(Integer registro) throws Throwable;
    
    /**
     * Obtiene la lista de evidencias_detalle de los registros correspondientes
     * 
     * @param registros Permite realizar la búsqueda de todas las evidencias que corresponden a la lista de registros recibida
     * @return  Devuelve una lista de todas las entidades de evidencias_detalle
     * @throws Throwable 
     */
    public List<EvidenciasDetalle> getListaEvidenciasPorRegistrosParticipantes(List<Integer> registros) throws Throwable;

    /**
     * Registra múltiples archivos como evidencias del registro especificado.
     *
     * @param registro Registro al que se van a agreegar las evidencias.
     * @param archivos Lista de archivos de evidencias.
     * @return Regresa una entrada de mapa con la clave tipo boleana indicando
     * si todos las evidencias se almacenaron y como valor la cantidad de
     * evidencias registradas.
     * @throws java.lang.Throwable
     */
    public Map.Entry<Boolean, Integer> registrarEvidenciasARegistro(Registros registro, List<Part> archivos) throws Throwable;

    /**
     * Elimina una evidencia de un registro, si la evidencia solo está asignada
     * al registro especificado la elimina de la base de datos y del disco duro,
     * de lo contrario solo la desliga del registro.
     *
     * @param registro Registro al que está ligada la evidencia.
     * @param evidenciasDetalle Evidencia a desligar o eliminar
     * @return Regresa TRUE si la evidencia se eliminó, FALSE de lo contrario o
     * NULL si solo se desligó.
     */
    public Boolean eliminarEvidenciaEnRegistro(Registros registro, EvidenciasDetalle evidenciasDetalle);
    
     /**
     * Elimina todas las evidencias de un registro, si la evidencia solo está asignada
     * al registro especificado la elimina de la base de datos y del disco duro,
     * de lo contrario solo la desliga del registro.
     *
     * @param registro Registro al que está ligada la evidencia.
     * @param evidenciasDetalle Evidencias a desligar o eliminar
     */
    public void eliminarEvidenciasEnRegistroGeneral(Integer registro, List<EvidenciasDetalle> evidenciasDetalle);
    
    /**
     * Obtiene la referencia a la actividad que está alineada con el registro
     * @param registro  Clave del registro del cual se desea conocer su actividad alineada.
     * @return  Devuelve la refeencia a la actividad alineada o null si es que no ha sido alineada
     * @throws Throwable 
     */
    public ActividadesPoa getActividadAlineadaGeneral(Integer registro) throws Throwable;
    
    /**
     * Verifica que el registro contenga la alineación con la actividad
     * @param registro
     * @return
     * @throws Throwable 
     */
    public Boolean verificaActividadAlineadaGeneral(Integer registro) throws Throwable;
    
    /**
     * Elimina la alineación asignada a la actividad
     * @param registro  Registro al cual se le eliminará la actividad
     * @return 
     */
    public Boolean eliminarAlineacion(Integer registro);
    
    /**
     * Alinea el registro con la actividad para que compartan evidencias.
     * @param actividad Actividad con la que se desea alinear.
     * @param registro Registro que se desea alinear.
     * @return Devuelve TRUE si la alineación se completó o FALSE de lo contrario.
     */
    public Boolean alinearRegistroActividad(ActividadesPoa actividad, Integer registro);
    
    public Registros buscaRegistroPorClave(Integer registro);
}
