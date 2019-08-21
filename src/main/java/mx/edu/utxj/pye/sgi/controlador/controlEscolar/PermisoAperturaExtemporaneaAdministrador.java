/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Collections;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.PermisoAperturaExtemporaneaRolAdministrador;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.vista.DtoAlerta;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPermisoAperturaExtemporanea;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRangoFechasPermiso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.JustificacionPermisosExtemporaneos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PermisosCapturaExtemporaneaGrupal;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class PermisoAperturaExtemporaneaAdministrador extends ViewScopedRol implements Desarrollable{
    @Getter @Setter PermisoAperturaExtemporaneaRolAdministrador rol;

    @EJB EjbPermisoAperturaExtemporanea ejb;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por area superior y categiría operativa<br/>
     *      La referencia al director si es que el usuario logueado es efectivamente un director por medio del filtro de rol<br/>
     *      El programa educativo al que pertenece el director por medio de operación segura antierror<br/>
     *      El DTO del rol<br/>
     *      La lista de periodos escolares en forma descendente por medio de operación segura antierror<br/>
     *      EL mapa de programas con grupos por medio de operación segura antierror ordenando programas por areas, niveles y nombre del programa y los grupos por grado y letra
     */
    @PostConstruct
    public void init(){
        try{
            setVistaControlador(ControlEscolarVistaControlador.PERMISO_APERTURA_EXTEMPORANEA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarAdministrador(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarAdministrador(logon.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo administrador = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new PermisoAperturaExtemporaneaRolAdministrador(filtro, administrador);
            tieneAcceso = rol.tieneAcceso(administrador);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setAdministrador(administrador);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
          
            rol.getInstrucciones().add("Seleccionar periodo escolar activo, de lo contrario solo podrá consultar configuraciones anteriores.");
            rol.getInstrucciones().add("Seleccionar Materia - Grupo - Programa Educativo que va a configurar.");
            rol.getInstrucciones().add("Seleccionar si o no aplicará Tarea Integradora en la configuración.");
            rol.getInstrucciones().add("Actualizar Porcentaje, Fecha de Inicio y Fecha Fin por cada unidad de la materia si no desea utilizar las fechas sugeridas por el sistema.");
            rol.getInstrucciones().add("En caso de que aplicará Tarea Integradora deberá ingresar Nombre, Porcentaje y Fecha de entrega.");
            rol.getInstrucciones().add("Los porcentajes que ingrese en total deben sumar 100%, en caso contrario el sistema no le permitirá guardar.");
            rol.getInstrucciones().add("Una vez que capture toda la información solicitada puede GUARDAR la configuración.");
            rol.getInstrucciones().add("Usted podrá visualizar la Configuración Guardada en sistema.");
            rol.getInstrucciones().add("Si desea ELIMINAR la configuración deberá seleccionar que desea realizar esta accción para que se active el botón de eliminar ubicado en la parte inferior.");
            rol.getInstrucciones().add("Al eliminar la configuración de la materia se eliminarán también los criterios de evaluación que se encuentren registrados.");
           
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "permiso apertura extemporanea";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Método para proporcionar lista de docentes sugeridos en un autocomplete donde se puede ingresar el número de nómina, nombre o área del docente
     * @param pista
     * @return Lista de sugerencias
     */
    public List<PersonalActivo> completeDocentes(String pista){
        ResultadoEJB<List<PersonalActivo>> res = ejb.buscarDocente(pista);
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return Collections.emptyList();
        }
    }
    
    /**
     * Permite que al cambiar o seleccionar un docente se puedan actualizar las materias asignadas a este docente
     * @param e Evento del cambio de calor
     */
    public void cambiarDocente(ValueChangeEvent e){
        if(e.getNewValue() instanceof PersonalActivo){
            PersonalActivo docente = (PersonalActivo)e.getNewValue();
            rol.setDocente(docente);
            final ResultadoEJB<List<DtoCargaAcademica>> res = ejb.getCargaAcademicaPorDocente(rol.getDocente());
            if(!res.getCorrecto()) {
                mostrarMensajeResultadoEJB(res);
                return;
            }
            rol.setCargasDocente(res.getValor());
            existenPermisosCapturasVigentes(docente);
            periodosCargas();
        }else mostrarMensaje("El valor seleccionado como docente no es del tipo necesario.");
    }
    
   
    public void periodosCargas(){
        ResultadoEJB<List<PeriodosEscolares>> res = ejb.getPeriodosCargas(rol.getCargasDocente());
        if(res.getCorrecto()){
            rol.setPeriodos(res.getValor());
            rol.setPeriodo(rol.getPeriodos().get(0));
            cargasAcademicas();
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void cargasAcademicas(){
        if(rol.getPeriodo() == null) return;
        if(rol.getDocente()== null) return;
        ResultadoEJB<List<DtoCargaAcademica>> res = ejb.getCargaAcademicaPorPeriodo(rol.getDocente(), rol.getPeriodo());
        if(res.getCorrecto()){
            rol.setCargasPeriodo(res.getValor());
            rol.setCarga(rol.getCargasPeriodo().get(0));
            tiposEvaluaciones();
            actualizarUnidadesMateria();
            justificacionesPermiso();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    public void tiposEvaluaciones(){
        if(rol.getCarga()== null) return;
        ResultadoEJB<List<String>> res = ejb.getTiposEvaluaciones();
        if(res.getCorrecto()){
            rol.setTiposEvaluacion(res.getValor());
            rol.setTipoEvaluacion(rol.getTiposEvaluacion().get(0));
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    public void actualizarUnidadesMateria(){
        ResultadoEJB<List<UnidadMateria>> res = ejb.getUnidadesMateria(rol.getCarga());
        if(res.getCorrecto()){
            rol.setUnidadesMateria(res.getValor());
            rol.setUnidadMateria(rol.getUnidadesMateria().get(0));
            rangoFechasPermiso();
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite que al cambiar el tipo de evaluación se pueda habilitar o deshabilitar la opción de unidad de la materia
     * @param e Evento del cambio de calor
     */
    public void cambiarTipoEvaluacion(ValueChangeEvent e){
       rol.setTipoEvaluacion((String)e.getNewValue());
    }
    
    public void cambiarUnidad(ValueChangeEvent e){
       rol.setUnidadMateria((UnidadMateria)e.getNewValue());
       rangoFechasPermiso();
    }
    
    public void rangoFechasPermiso(){
        ResultadoEJB<DtoRangoFechasPermiso> res = ejb.getRangoFechasPermiso(rol.getCarga(), rol.getUnidadMateria());
        if(res.getCorrecto()){
            rol.setDtoRangoFechasPermiso(res.getValor());
            rol.setRangoFechaInicial(rol.getDtoRangoFechasPermiso().getRangoFechaInicial());
            rol.setRangoFechaFinal(rol.getDtoRangoFechasPermiso().getRangoFechaFinal());
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite invocar el guardado de la configuración de la unidad materia, para que se lleve acabo, se debió haber seleccionado una carga académica, e ingresado fecha de inicio y fin de cada unidad.
     */
    public void guardarPermisoCaptura(){
        ResultadoEJB<PermisosCapturaExtemporaneaGrupal> res = ejb.guardarPermisoCaptura(rol.getCarga(), rol.getUnidadMateria(), rol.getTipoEvaluacion(), rol.getFechaInicio(), rol.getFechaFin(), rol.getJustificacionPermisosExtemporaneos(), rol.getAdministrador());
        if(res.getCorrecto()){
            rol.setPermisosCapturaExtemporaneaGrupal(res.getValor());
        }else mostrarMensajeResultadoEJB(res);
        
    }
    
    public void justificacionesPermiso(){
        if(rol.getCarga()== null) return;
        ResultadoEJB<List<JustificacionPermisosExtemporaneos>> res = ejb.getJustificacionesPermisoCaptura();
        if(res.getCorrecto()){
            rol.setListaJustificaciones(res.getValor());
            rol.setJustificacionPermisosExtemporaneos(rol.getListaJustificaciones().get(0));
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void cambiarJustificacion(ValueChangeEvent e){
       rol.setJustificacionPermisosExtemporaneos((JustificacionPermisosExtemporaneos)e.getNewValue());
    }
    
    public void existenPermisosCapturasVigentes(PersonalActivo docente){
        Date fechaActual = new Date();
        ResultadoEJB<List<PermisosCapturaExtemporaneaGrupal>> res = ejb.buscarPermisosCapturaVigentes(docente, fechaActual);
        if(res.getCorrecto()){
            rol.setListaPermisosCapturasExtemporaneas(res.getValor());
            System.err.println("existenPermisosCapturasVigentes - lista " + rol.getListaPermisosCapturasExtemporaneas().size());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
}
