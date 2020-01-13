package mx.edu.utxj.pye.siip.interfaces.vin;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVinculacion;
import mx.edu.utxj.pye.sgi.entity.pye2.ContactosEmpresa;
import mx.edu.utxj.pye.sgi.entity.pye2.CorreosEmpresa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EmpresasTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.GirosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasBeneficiadosVinculacion;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.SectoresTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.TelefonosEmpresa;
import mx.edu.utxj.pye.siip.dto.vin.DTOActividadesVinculacion;
import mx.edu.utxj.pye.siip.dto.vin.DTOProgramasBeneficiadosVinculacion;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbOrganismosVinculados {
    
//    public ListaOrganismosVinculados getListaActualizadaPlantilla()throws Throwable;
//    
//    public void actualizarPlantillaConvenio(ListaOrganismosVinculados listaOrganismosVinculados) throws FileNotFoundException, IOException, ParsePropertyException, InvalidFormatException,Throwable;
//    
    public List<OrganismosVinculados> getListaOrganismosVinculados(String rutaArchivo) throws Throwable;
    
    public void guardaOrganismosVinculados(List<OrganismosVinculados> listaOrganismosVinculados, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    
    public Integer getRegistroOrganismoEspecifico(Integer empresa);
    
    public OrganismosVinculados getOrganismoVinculadoPorEmpresa(Integer empresa);
    
    public OrganismosVinculados getOrganismosVinculado(OrganismosVinculados organismoVinculado);
    
    /***************************** Catalogos para la actualización de plantillas *******************************************/
    /**
     * Devuelve una lista de tipo Organismos Vinculados Vigentes, para la actualización de las plantillas correspondientes
     * @return Lista de entidades de OrganismosVinculados 
     */
    public List<OrganismosVinculados> getOrganismosVinculadoVigentes();
    
    /**
     * Devuelve una lista de tipo Organismos Tipo para el llenado de las plantillas correspondientes
     * @return  Lista de entidades de OrganismosVinculados
     * @throws Throwable 
     */
    public List<OrganismosTipo> getOrganismosTipo() throws Throwable;
    
    /**
     * Devuelve una lista de tipo Empresas Tipo para el llenado de las plantillas correspondientes
     * @return  Lista de entidades de Empresas Tipo
     * @throws Throwable 
     */
    public List<EmpresasTipo> getEmpresasTipos() throws Throwable;
    
    /**
     * Devuelve una lista completa de Giros Tipo para el llenado de las plantillas correspondientes
     * @return  Lista de entidades de GirosTipo
     * @throws Throwable 
     */
    public List<GirosTipo> getGirosTipo() throws Throwable;
    
    /**
     * Devuelve una lista completa de Sectores Tipo para el llenado de las plantillas correspondientes
     * @return  Lista de entidades de SectoresTipo
     * @throws Throwable 
     */
    public List<SectoresTipo> getSectoresTipo() throws Throwable;

    /**
     * Método que se ocupa para el filtrado de OrganismosVinculados por Ejercicio, Mes y área el cual es mostrado para consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return Regresa una lista de registros de OrganismosVinculados que serán ocupados para consulta o eliminación
     */
    public List<OrganismosVinculados> getFiltroOrganismoVinculadoEjercicioMesArea(Short ejercicio, String mes, Short area);
    
    /**
     * Devuelve una lista completa de ActividadesVinculacion para la asignación de Actividades de Vinculación con Organismos Vinculados
     * @return  Lista de Entididades de ActividadesVinculacion
     * @throws Throwable 
     */
    public List<DTOActividadesVinculacion> getActividadesVinculacion() throws Throwable;
    
    /**
     * Verifica que el organismo vinculado este ligado con la actividad de vinculación o si es existente dicha vinculación
     * @param empresa   Empresa que hace referencia
     * @param actividadVinculacion  Actividad que se esta consultando
     * @return  Devuelve True si existe o False si no hay dicha vinculación
     */
    public Boolean verificaActividadVinculacion(OrganismosVinculados empresa, ActividadesVinculacion actividadVinculacion);
    
    /**
     * Elimina la ActividadVinculacionEmpresa asignada a la empresa
     * 
     * @param empresa   Empresa a la cual se le eliminara la actividad de vinculación
     * @param actividadVinculacion  ActividadDeVinculacio a la cual se le eliminara la actividad de vinculación
     * @return 
     */
    public Boolean eliminarActividadVinculacionEmpresa(OrganismosVinculados empresa, DTOActividadesVinculacion actividadVinculacion);
    
    /**
     * Asigna a la empresa la actividad de vinculación
     * @param empresa   Empresa a la cual se desea asignar la actividad de vinculación
     * @param actividadVinculacion  Actividad que se asignará
     * @return  Devuelve TRUE si la Asignación se completó o FALSE de lo contrario.
     */
    public Boolean guardarActividadVinculacionEmpresa(OrganismosVinculados empresa, DTOActividadesVinculacion actividadVinculacion);
    
    public void bajaOrganismoVinculado(OrganismosVinculados registro);
    
    /**
     * Realiza la búsqueda de los contactos extras asignados a cada organismo vinculado
     * @param organismoVinculado
     * @return 
     */
    public List<ContactosEmpresa> consultaContactosEmpresa(OrganismosVinculados organismoVinculado);
    
    /**
     * Método que almacena los representantes o contactos secundarios de los organismos vinculados
     * @param contactoEmpresa
     * @return 
     */
    public Boolean guardarContactoEmpresa(ContactosEmpresa contactoEmpresa);
    
    /**
     * Método que elimina el contacto secundario del organismo seleccionado seleccionado
     * @param contactosEmpresa
     * @return 
     */
    public Boolean eliminarContactoEmpresa(ContactosEmpresa contactosEmpresa);
    
    /**
     * Método que edita los contactos de la empresa previamente ya registrados
     * @param contactoEmpresa
     * @return 
     */
    public Boolean editaContactoEmpresa(ContactosEmpresa contactoEmpresa);
    
    /**
     * Realiza la búsqueda de los correos extras asignados a cada organismo vinculado
     * @param organismoVinculado
     * @return 
     */
    public List<CorreosEmpresa> consultaCorreosEmpresa(OrganismosVinculados organismoVinculado);
    
    /**
     * Método que almacena los correos secundarios de los organismos vinculados
     * @param correoEmpresa
     * @return 
     */
    public Boolean guardarCorreoEmpresa(CorreosEmpresa correoEmpresa);
    
    /**
     * Método que elimina el correo secundario del organismo seleccionado seleccionado
     * @param correoEmpresa
     * @return 
     */
    public Boolean eliminarCorreoEmpresa(CorreosEmpresa correoEmpresa);
    
    /**
     * Método que edita los correos de la empresa previamente ya registrados
     * @param correoEmpresa
     * @return 
     */
    public Boolean editaCorreoEmpresa(CorreosEmpresa correoEmpresa);
    
    /**
     * Realiza la búsqueda de los teléfonos extras asignados a cada organismo vinculado
     * @param organismoVinculado
     * @return 
     */
    public List<TelefonosEmpresa> consultaTelefonosEmpresa(OrganismosVinculados organismoVinculado);
    
    /**
     * Método que almacena los teléfonos secundarios de los organismos vinculados
     * @param telefonoEmpresa
     * @return 
     */
    public Boolean guardaTelefonoEmpresa(TelefonosEmpresa telefonoEmpresa);
    
    /**
     * Método que elimina el teléfono secundario del organismo seleccionado seleccionado
     * @param telefonoEmpresa
     * @return 
     */
    public Boolean eliminarTelefonoEmpresa(TelefonosEmpresa telefonoEmpresa);
    
    /**
     * Método que edita los teléfonos de la empresa previamente ya registrados
     * @param telefonosEmpresa
     * @return 
     */
    public Boolean editaTelefonoEmpresa(TelefonosEmpresa telefonosEmpresa);
    
    public Pais getPaisOrganismoVinculado(OrganismosVinculados organismoVinculado);
    
    public Localidad getLocalidadOrganismoVinculado(OrganismosVinculados organismoVinculado);
    
    public Boolean guardaUbicacion(OrganismosVinculados organismoVinculado, Pais pais, Estado estado, Municipio municipio, Localidad localidad);
    
    public Boolean eliminaUbicacion(OrganismosVinculados organismosVinculados);
    
    /**
     * Devuelve una lista completa de DTOProgramasBeneficiadosVinculacion para asignar si aplica o no el beneficio en el programaEducativo
     * @return  Lista de DTO de ProgramasBeneficiadosVinculacion
     * @throws Throwable 
     */
    public List<DTOProgramasBeneficiadosVinculacion> getProgramasBeneficiadosVinculacion() throws Throwable;
    
    /**
     * Verifica que el ProgramasBeneficiadosVinculacion este ligado con la empresa o si existente dicha relación
     * @param empresa   Empresa o convenio seleccionado
     * @param areaUniversidad   Programa educativo consultado
     * @return  Devuelve True si existe o False si no hay dicha vinculación
     */
    public Boolean verificaProgramaBeneficiadoVinculacion(Integer empresa, AreasUniversidad areaUniversidad);
    
    /**
     * Guarda la asignación del programa educativo con el convenio
     * @param programaBeneficiadosVinculacion   Entidad para guardar en base de datos
     * @return 
     */
    public Boolean guardarProgramaBeneficiadoVinculacion(ProgramasBeneficiadosVinculacion programaBeneficiadosVinculacion);
    
    /**
     * Guarda la asignación del programa educativo con el convenio
     * @param programaBeneficiadosVinculacion   Entidad para guardar en base de datos
     * @return 
     */
    public Boolean eliminarProgramaBeneficiadoVinculacion(ProgramasBeneficiadosVinculacion programaBeneficiadosVinculacion);
    
    public OrganismosVinculados editaOrganismoVinculado(OrganismosVinculados organismoVinculado);
    
    public List<OrganismosVinculados> buscaCoincidenciasOrganismosVinculados(String parametro);
    
    public Boolean buscaOrganismoVinculadoExistente(OrganismosVinculados organismoVinculado);
    
    public List<OrganismosVinculados> getReporteOrganismosVinculados();
    
    public List<OrganismosVinculados> getReporteActividadesVinculacion();
    
    public List<ProgramasBeneficiadosVinculacion> getReporteProgramasBeneficiadosVinculacion();
    
    public List<TelefonosEmpresa> getReporteTelefonosEmpresa();
    
    public List<CorreosEmpresa> getCorreosEmpresas();
    
    public List<ContactosEmpresa> getReporteContactosEmpresa();
    
}
