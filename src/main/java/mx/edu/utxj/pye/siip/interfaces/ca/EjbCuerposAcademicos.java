/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadAreasEstudio;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadDisciplinas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadIntegrantes;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadLineas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpoAreasAcademicas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerposAcademicosRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerpAcadIntegrantes;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerpoAreasAcademicas;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerposAcademicosR;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbCuerposAcademicos {

    /**
     * Método utilizado para interpretar la información llenada por parte del usuario en la plantilla del usuario y posteriormente incluirla en una lista de tipo DTOCuerposAcademicosR esto para la previsualización y validación
     * @param rutaArchivo   Parámetro que contiene la ruta del archivo que se leerá para el llenado de las listas ocupará para el llenado de las listas
     * @return              Devuelve la información que originalmente contenía el archivo de excel, en un formato de lista DTOCuerposAcademicosR
     * @throws Throwable
     */
    public List<DTOCuerposAcademicosR> getListaCuerposAcademicos(String rutaArchivo) throws Throwable;

    /**
     * Método utilizado para interpretar la información llenada por parte del usuario en la plantilla del usuario y posteriormente incluirla en una lista de tipo DTOCuerpAcadIntegrantes esto para la previsualización y validación
     * @param rutaArchivo   Parámetro que contiene la ruta del archivo que se leerá para el llenado de las listas ocupará para el llenado de las listas
     * @return              Devuelve la información que originalmente contenía el archivo de excel, en un formato de lista DTOCuerpAcadIntegrantes
     * @throws Throwable
     */
    public List<DTOCuerpAcadIntegrantes> getListaCuerpAcadIntegrantes(String rutaArchivo) throws Throwable;

    /**
     * Método utilizado para interpretar la información llenada por parte del usuario en la plantilla del usuario y posteriormente incluirla en una lista de tipo CuerpacadLineas esto para la previsualización y validación
     * @param rutaArchivo       Parámetro que contiene la ruta del archivo que se leerá para el llenado de las listas ocupará para el llenado de las listas
     * @return                  Devuelve la información que originalmente contenía el archivo de excel, en un formato de lista CuerpacadLineas
     * @throws Throwable
     */
    public List<CuerpacadLineas> getListaCuerpAcadLineas(String rutaArchivo) throws Throwable;

    /**
     * Método que permite el almacenamiento en base de datos la información correspondiente a una Lista de (Cuerpo Académicos).
     * @param listaCuerposAcademicos    Lista que contiene toda la información que se almacenara en la base de datos
     * @param registrosTipo             Permite que los modulos de registro sean almacenados mediante una clasificación
     * @param ejesRegistro              Permite la clasificación por Ejercicio
     * @param area                      Permite guardar el área del usuario que se encuentra actualmente registrando información
     * @param eventosRegistros          Permite guardar el evento del registro y poder realizar futuras consultas y filtrados con base a las necesidades de los dueños o administradores de la información
     * @throws Throwable 
     */
    public void guardaCuerposAcademicos(List<DTOCuerposAcademicosR> listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;

    /**
     * Método que permite el almacenamiento en base de datos la información correspondiente a una Lista de (Cuerpos Académicos Integrantes).
     * @param listaCuerposAcademicos    Lista que contiene toda la información que se almacenara en la base de datos
     * @param registrosTipo             Permite que los modulos de registro sean almacenados mediante una clasificación
     * @param ejesRegistro              Permite la clasificación por Ejercicio
     * @param area                      Permite guardar el área del usuario que se encuentra actualmente registrando información
     * @param eventosRegistros          Permite guardar el evento del registro y poder realizar futuras consultas y filtrados con base a las necesidades de los dueños o administradores de la información
     * @throws Throwable 
     */
    public void guardaCuerpAcadIntegrantes(List<DTOCuerpAcadIntegrantes> listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;

    /**
     * Método que permite el almacenamiento en base de datos la información correspondiente a una Lista de (Cuerpos Académicos Líneas de Investigación).
     * @param listaCuerposAcademicos    Lista que contiene toda la información que se almacenara en la base de datos
     * @param registrosTipo             Permite que los modulos de registro sean almacenados mediante una clasificación
     * @param ejesRegistro              Permite la clasificación por Ejercicio
     * @param area                      Permite guardar el área del usuario que se encuentra actualmente registrando información
     * @param eventosRegistros          Permite guardar el evento del registro y poder realizar futuras consultas y filtrados con base a las necesidades de los dueños o administradores de la información
     * @throws Throwable 
     */
    public void guardaCuerpAcadLineas(List<CuerpacadLineas> listaCuerposAcademicos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;

    /**
     * Método que devuelve la clave de registro del cuerpo académico consultado mediante la clave única de Cuerpo Académico, este método es ocupado al guardar una lista de cuerpos académicos
     * @param cuerpoAcademico Parámetro que recibe la clave única del cuerpo académico
     * @return
     */
    public Integer getRegistroCuerpoAcademicoEspecifico(String cuerpoAcademico);

    /**
     * Método que permite la búsqueda de un (Cuerpo Académico).
     * Estos métodos son disparados al momento de realizar una operación Create.
     * @param cuerpoAcademico
     * @return 
     */
    public CuerposAcademicosRegistro getCuerpoAcademico(CuerposAcademicosRegistro cuerpoAcademico);

    /**
     * Método que permite la búsqueda de un (Cuerpo Académico Integrante).
     * Estos métodos son disparados al momento de realizar una operación Create.
     * @param cuerpacadIntegrante
     * @return 
     */
    public CuerpacadIntegrantes getCuerpacadIntegrantes(CuerpacadIntegrantes cuerpacadIntegrante);

    /**
     * Método que permite la búsqueda de un (Cuerpo Académico Línea de Investigación).
     * Estos métodos son disparados al momento de realizar una operación Create.
     * @param cuerpacadLinea
     * @return 
     */
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
     * @param area  Área superior de cada usuario logueado
     * @return  Regresa una lista de registros de Cuerpos Académicos que serán ocupados para consulta y eliminación
     * @throws Throwable
     */
    public List<DTOCuerposAcademicosR> getFiltroCuerposAcademicosEjercicioMesArea(Short ejercicio, Short area) throws Throwable;
    
    /**
     *  Método que filtra el registro de DTOCuerpAcadIntegrantes por medio de Ejercicio, Mes y Área el cual es mostrado para la consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return  Regresa una lista de registros de DTOCuerpAcadIntegrantes que serán ocupados para consulta y eliminación
     * @throws Throwable
     */
    public List<DTOCuerpAcadIntegrantes> getFiltroCuerpAcadIntegrantesEjercicioMesArea(Short ejercicio, Short area) throws Throwable;
    
    /**
     *  Método que filtra el registro de los CuerpacadLineas por medio de Ejercicio, Mes y Área el cual es mostrado para la consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
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
    
    /**
     * Devuelve una lista completa de DTOCuerpoAreasAcademicas para asignar si aplica o no el cuerpo académico en el área académica
     * @return  Lista de DTO de CuerpoAreasAcademicas
     * @throws Throwable 
     */
    public List<DTOCuerpoAreasAcademicas> getCuerpoAreasAcademicas() throws Throwable;
    
    /**
     * Verifica que el Área Académica este ligada con el cuerpo académico o si existente dicha relación
     * @param cuerpoAcademico Cuerpo Académico seleccionado
     * @param areaUniversidad   Programa educativo consultado
     * @return  Devuelve True si existe o False si no hay dicha vinculación
     */
    public Boolean verificaCuerpoAreaAcademica(String cuerpoAcademico, AreasUniversidad areaUniversidad);
    
    /**
     * Guarda la asignación del área académica con el cuerpo académico
     * @param cuerpoAreasAcademica  Entidad para guardar en base de datos
     * @return 
     */
    public Boolean guardarCuerpoAreaAcademica(CuerpoAreasAcademicas cuerpoAreasAcademica);
    
    /**
     * Guarda la asignación del área académica con el cuerpo académico
     * @param cuerpoAreasAcademica  Entidad para guardar en base de datos  
     * @return 
     */
    public Boolean eliminarCuerpoAreaAcademica(CuerpoAreasAcademicas cuerpoAreasAcademica);
    
    /**
     * Método que da de baja el cuerpo académico seleccionado, así como todo lo relacionado con el: Participantes y Líneas de investigación
     * @param cuerposAcademicosRegistro
     * @return 
     */
    public Boolean bajaCuerpoAcademico(CuerposAcademicosRegistro cuerposAcademicosRegistro);
    
    /**
     * Método que da de alta el cuerpo académico seleccionado, así como todo lo relacionado con el: Participantes y Líneas de investigación
     * @param cuerposAcademicosRegistro
     * @return 
     */
    public Boolean altaCuerpoAcademico(CuerposAcademicosRegistro cuerposAcademicosRegistro);
    
    /**
     * Método que da de baja el integrante del cuerpo académico seleccionado
     * @param cuerpacadIntegrante
     * @return 
     */
    public Boolean bajaCuerpacadIntegrantes(CuerpacadIntegrantes cuerpacadIntegrante);
    
    /**
     * Método que da de alta el integrante del cuerpo académico seleccionada
     * @param cuerpacadIntegrante
     * @return 
     */
    public Boolean altaCuerpacadIntegrantes(CuerpacadIntegrantes cuerpacadIntegrante);
    
    /**
     * Método que da de baja la linea de investigación del cuerpo académico
     * @param cuerpacadLineas
     * @return 
     */
    public Boolean bajaCuerpacadLineas(CuerpacadLineas cuerpacadLineas);
    
    /**
     * Método que da de alta la línea de investigación del cuerpo académico
     * @param cuerpacadLineas
     * @return 
     */
    public Boolean altaCuerpacadLineas(CuerpacadLineas cuerpacadLineas);
    
    /**
     * Método que permite la edición de datos de: (Cuerpo Académico) que el usuario haya seleccionado desde la interfaz gráfica de usuario o administrador
     * @param cuerpoAcademicoRegistro   Es utilizado para identificar que (Cuerpo Académico) será editado, contiene la información que el usuario haya modificado desde la interfaz gráfica
     * @return Devuelve una entidad del tipo que fue actualizada para asegurar la actualización visual en la interfaz gráfica
     */
    public CuerposAcademicosRegistro editaCuerpoAcademicoRegistro(CuerposAcademicosRegistro cuerpoAcademicoRegistro);
    
    /**
     * Método que permite la edición de datos de: (Cuerpo Académico Integrante) que el usuario haya seleccionado desde la interfaz gráfica de usuario o administrador
     * @param cuerpoAcademicoIntegrante   Es utilizado para identificar que (Cuerpo Académico Integrante) será editado, contiene la información que el usuario haya modificado desde la interfaz gráfica
     * @return Devuelve una entidad del tipo que fue actualizada para asegurar la actualización visual en la interfaz gráfica
     */
    public CuerpacadIntegrantes editaCuerpoAcademicoIntegrante(CuerpacadIntegrantes cuerpoAcademicoIntegrante);
    
    /**
     * Método que permite la edición de datos de: (Cuerpo Académico Linea de Investigación) que el usuario haya seleccionado desde la interfaz gráfica de usuario o administrador
     * @param cuerpoAcademicoLinea   Es utilizado para identificar que (Cuerpo Académico Linea de Investigación) será editado, contiene la información que el usuario haya modificado desde la interfaz gráfica
     * @return Devuelve una entidad del tipo que fue actualizada para asegurar la actualización visual en la interfaz gráfica
     */
    public CuerpacadLineas editaCuerpoAcademicoLineaInvestigacion(CuerpacadLineas cuerpoAcademicoLinea);
    
    /**
     * Método que permite la búsqueda del (Cuerpo Académico Integrante, Cuerpo Académico Linea de Investigación) el cual es utilizado para la validación previa a la edición.
     * @param cuerpacadIntegrante  Es utilizado para la búsqueda de (Cuerpo Académico Integrante, Cuerpo Académico Linea de Investigación)
     * @return Devuelve un valor booleano que permite proceder o no a la actualización de la información
     */
    public Boolean buscaCuerpoAcademicoIntegranteExistente(CuerpacadIntegrantes cuerpacadIntegrante);
    
    /**
     * Documentación Previa
     * @param cuerpacadLinea
     * @return 
     */
    public Boolean buscaCuerpoAcademicoLineaInvestigacionExistente(CuerpacadLineas cuerpacadLinea);
    
    /**
     * Método que permite descargar el reporte de los (Cuerpos académicos) registrados en el ejercicio consultado en la interfaz gráfica del usuario. 
     * @param ejercicio Parámetro que permite la consulta y el filtrado de información por ejercicio
     * @return  Devuelve una lista de entidades de (Cuerpos Académicos), los cuales se agregarán a la correspondiente hoja de excel
     */
    public List<CuerposAcademicosRegistro> getReporteGeneralCuerposAcademicosPorEjercicio(Short ejercicio);
    
    /**
     * Método que permite descargar el reporte de los (CuerpacadIntegrantes) registrados en el ejercicio consultado en la interfaz gráfica del usuario. 
     * @param ejercicio Parámetro que permite la consulta y el filtrado de información por ejercicio
     * @return  Devuelve una lista de entidades de (CuerpacadIntegrantes), los cuales se agregarán a la correspondiente hoja de excel
     */
    public List<CuerpacadIntegrantes> getReporteGeneralCuerposAcademicosIntegrantesPorEjercicio(Short ejercicio);
    
    /**
     * Método que permite descargar el reporte de los (CuerpacadLineas) registrados en el ejercicio consultado en la interfaz gráfica del usuario. 
     * @param ejercicio Parámetro que permite la consulta y el filtrado de información por ejercicio
     * @return  Devuelve una lista de entidades de (CuerpacadLineas), los cuales se agregarán a la correspondiente hoja de excel
     */
    public List<CuerpacadLineas> getReporteGeneralCuerposAcademicosLineasInvestigacionPorEjercicio(Short ejercicio);
    
    /**
     * Método que permite el registro nuevo de un Cuerpo Académico
     * @param cuerpoAcademico           Entidad que contiene todos los datos del nuevo Cuerpo Académico
     * @param registroTipo              Parámetro para la tabla de registros, contiene cual es el tipo de registro que se esta dando de alta
     * @param ejesRegistro              Parámetro para la tabla de registros, contiene cual es el eje del registro que se esta dando de alta
     * @param area                      Parámetro para la tabla de registros, contiene cual es el área del registro que se esta dando de alta
     * @param eventosRegistros          Parámetro para la tabla de registros, contiene cual es el evento de registro que se esta dando de alta
     */
    public void guardaCuerpoAcademico(CuerposAcademicosRegistro cuerpoAcademico, RegistrosTipo registroTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    
    /**
     * Método que permite el registro nuevo de un Integrante en Cuerpo Académico
     * @param cuerpoAcademicoIntegrante     Entidad que contiene todos los datos del nuevo Cuerpo Académico
     * @param registroTipo                  Parámetro para la tabla de registros, contiene cual es el tipo de registro que se esta dando de alta
     * @param ejesRegistro                  Parámetro para la tabla de registros, contiene cual es el eje del registro que se esta dando de alta
     * @param area                          Parámetro para la tabla de registros, contiene cual es el área del registro que se esta dando de alta
     * @param eventosRegistros              Parámetro para la tabla de registros, contiene cual es el evento de registro que se esta dando de alta
     */
    public void guardaCuerpoAcademicoIntegrante(CuerpacadIntegrantes cuerpoAcademicoIntegrante, RegistrosTipo registroTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    
    /**
     * Método que permite el registro nuevo de Línea de Investigación en Cuerpo Académico
     * @param cuerpoAcademicoLinea          Entidad que contiene todos los datos del nuevo Cuerpo Académico
     * @param registroTipo                  Parámetro para la tabla de registros, contiene cual es el tipo de registro que se esta dando de alta
     * @param ejesRegistro                  Parámetro para la tabla de registros, contiene cual es el eje del registro que se esta dando de alta
     * @param area                          Parámetro para la tabla de registros, contiene cual es el área del registro que se esta dando de alta
     * @param eventosRegistros              Parámetro para la tabla de registros, contiene cual es el evento de registro que se esta dando de alta
     */
    public void guardaCuerpoAcademicoLineaInvestigacion(CuerpacadLineas cuerpoAcademicoLinea, RegistrosTipo registroTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
}
