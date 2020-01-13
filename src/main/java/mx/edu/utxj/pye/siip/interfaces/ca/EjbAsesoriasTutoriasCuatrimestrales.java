/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasCuatrimestrales;
import mx.edu.utxj.pye.sgi.entity.pye2.DatosAsesoriasTutorias;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.siip.dto.ca.DTOAsesoriasTutoriasCuatrimestrales;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbAsesoriasTutoriasCuatrimestrales {
    
    /**
     * Método que devuelve una lista completa de DatosAsesoriasTutorias que se ocupará en el formulario de registro de AsesoriasTutoriasCuatrimestrales 
     * @return Lista de entidades de tipo DatosAsesoriasTutorias
     */
    public List<DatosAsesoriasTutorias> getDatosAsesoriasTutorias();
    
    /**
     * Obtiene la lista de periodos con registros de tutorías y asesorías cuatrimestrales.
     * @param registrosTipo Permite filtrar los registros por el tipo necesario.
     * @param eventoRegistro Permite filtrar los registros por el evento del registro
     * @param areasUniversidad  Permite filtrar los registros por el área consultante
     * @return Lista de periodos.
     */
    public List<PeriodosEscolares> getPeriodosConregistro(RegistrosTipo registrosTipo, EventosRegistros eventoRegistro, AreasUniversidad area);
    
    /**
     * Comprueba si el periodo del evento actual se encuentra en el periodo mas reciente, en caso de no encontrarlo obtiene el periodo correspondiente.
     * @param periodos Lista de periodos obtenidos con registros.
     * @param eventos Eventos del periodo mas reciente.
     * @param eventoActual Evento de registro actual.
     * @param registrosTipo
     * @param area
     * @return Lista de periodos actualizados y eventos que incluyen evento actual del periodo correspondiente.
     * @throws mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException Se lanza en casi que se requiera un periodo que no existe aún en la base de datos.
     */
    public Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoActual, RegistrosTipo registrosTipo, AreasUniversidad area) throws PeriodoEscolarNecesarioNoRegistradoException;    
    
    /**
     * Método que permite la búsqueda de registros de tipo AsesoriasTutoriasCuatrimestral (Principal Uso: getListaAsesoriaTutoriaCuatrimestralPorEventoAreaPeriodo) Apoyo en el llenado de información del DTO
     * @param registroTipo  Tipo de registro
     * @param areaUniversidad   Área de registro
     * @param periodoEscolar    Periodo Escolar de registro
     * @param datoAsesoriaTutoria   Tipo de servicio, el cual contiene la información y descripción
     * @return  Lista de tipo AsesoriasTutoriasCuatrimestrales el cual solo deberia devolver un solo registro o la lista debería estar vacía
     */
    public List<AsesoriasTutoriasCuatrimestrales> buscaAsesoriaTutoriaCuatrimestralEspecifico(RegistrosTipo registroTipo, AreasUniversidad areaUniversidad, PeriodosEscolares periodoEscolar, DatosAsesoriasTutorias datoAsesoriaTutoria);
    
    /**
     * Obtiene la lista de registros correspondientes al evento seleccionado.
     * @param evento Evento seleccionado.
     * @param claveArea Área a la que pertenece el usuario y que servirá como filtro.
     * @param periodo Periodo del evento y que servirá como filtro
     * @param registrosTipo Permite filtrar los registros por el tipo necesario.
     * @return Lista de registros mensuales.
     */
    public List<DTOAsesoriasTutoriasCuatrimestrales> getListaAsesoriaTutoriaCuatrimestralPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo);
 
    /**
     * Método que permite el registro nuevo de una Asesoría y Tutoría Cuatrimestral
     * @param asesoriaTutoriaCuatrimestral  Entidad que contiene todos los datos de la nueva Asesoría ó Tutoría Cuatrimestral
     * @param registroTipo                  Parámetro para la tabla de registros, contiene cual es el tipo de registro que se esta dando de alta
     * @param ejesRegistro                  Parámetro para la tabla de registros, contiene cual es el eje del registro que se esta dando de alta
     * @param area                          Parámetro para la tabla de registros, contiene cual es el área del registro que se esta dando de alta
     * @param eventosRegistros              Parámetro para la tabla de registros, contiene cual es el evento de registro que se esta dando de alta
     */
    public void guardaAsesoriaTutoriaCuatrimestral(DTOAsesoriasTutoriasCuatrimestrales asesoriaTutoriaCuatrimestral, RegistrosTipo registroTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    
    /**
     * Método que permite la edición del registro de Asesoria Tutoria Cuatrimestral individual 
     * @param asesoriaTutoriaMensual    Entidad que contiene la información actualizada del registro que se actualizará
     * @return  Regresa un mensaje de tipo String que contiene la información de la operación que se esta realizando.
     */
    public String editaAsesoriaTutoriaCuatrimestralPeriodoEscolar(DTOAsesoriasTutoriasCuatrimestrales asesoriaTutoriaMensual);
    
    /**
     * Método que permite la búsqueda de un registro de Asesoría o Tutoría Cuatrimestral, previo al almacenamiento de un nuevo registro, evitando de esta manera la duplicidad de información
     * @param asesoriaTutoriaCuatrimestral  Entidad que contiene toda la información necesaria para detectar similitudes en la base de datos
     * @return  Devuelve una lista de entidades de tipo AsesoriasTutoriasCuatrimestrales, utilizada para validar si contiene o no resultados de la consulta
     */
    public List<AsesoriasTutoriasCuatrimestrales> buscaAsesoriaTutoriaCuatrimestralParaGuardado(AsesoriasTutoriasCuatrimestrales asesoriaTutoriaCuatrimestral);
    
    /**
     * Método que permite la búsqueda de un registro de Asesoría o Tutoría Cuatrimestral, previo a la edición de un registro existente, evitando de esta manera la duplicidad de información
     * @param asesoriaTutoriaCuatrimestral  Entidad que contiene toda la información necesaria para detectar similitudes en la base de datos
     * @return  Devuelve una lista de entidades de tipo AsesoriasTutoriasCuatrimestrales, utilizada para validar si contiene o no resultados de la consulta
     */
    public List<AsesoriasTutoriasCuatrimestrales> buscaAsesoriaTutoriaCuatrimestralParaEdicion(AsesoriasTutoriasCuatrimestrales asesoriaTutoriaCuatrimestral);
    
}
