/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEliminarRegistrosEstIns;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.EliminarInscripcionRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbEliminarInscripcion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class EliminarInscripcionServiciosEscolares extends ViewScopedRol implements Desarrollable{
    @Getter @Setter EliminarInscripcionRolServiciosEscolares rol;

    @EJB EjbEliminarInscripcion ejb;
    @EJB EjbRegistroBajas ejbRegistroBajas;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por personal de servicios escolares<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
        try{
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.ELIMINAR_INSCRIPCION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbRegistroBajas.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es personal de servicios escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbRegistroBajas.validarServiciosEscolares(logon.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo serviciosEscolares = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new EliminarInscripcionRolServiciosEscolares(filtro, serviciosEscolares);
            tieneAcceso = rol.tieneAcceso(serviciosEscolares);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(serviciosEscolares);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(ejbRegistroBajas.getPeriodoActual().getPeriodo());
          
            rol.getInstrucciones().add("Ingrese nombre o matricula del o de la estudiante del que se eliminará inscripción, únicamente le aparecerán estudiantes del periodo actual.");
            rol.getInstrucciones().add("Una vez que seleccione el registro que corresponda al estudiante, podrá visualizar en la parte inferior una tabla que contiene la información registrada relacionada con el estudiante.");
            rol.getInstrucciones().add("Dar clic en el botón ELIMINAR, para se elimine la inscripción del estudiante, así como los registros que se encuentren relacionados.");
           
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "eliminar inscripción";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Método para proporcionar lista de estudiantes del periodo actual sugeridos en un autocomplete donde se puede ingresar el número de matricula o nombre
     * @param pista
     * @return Lista de sugerencias
     */
    public List<DtoEstudianteComplete> completeEstudiantes(String pista){
        inicializarValores();
        ResultadoEJB<List<DtoEstudianteComplete>> res = ejb.buscarEstudiantePeriodoActual(pista);
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return Collections.emptyList();
        }
    }
    
    /**
     * Permite que al cambiar o seleccionar un estudiante se pueda actualizar la información
     * @param e Evento del cambio de valor
     */
    public void cambiarEstudiante(ValueChangeEvent e){
        if(e.getNewValue() instanceof DtoEstudianteComplete){
            DtoEstudianteComplete estudiante = (DtoEstudianteComplete)e.getNewValue();
            rol.setEstudianteSeleccionado(estudiante);
            rol.setExistenRegistros(false);
            buscarDatosEstudiante(rol.getEstudianteSeleccionado().getEstudiantes().getIdEstudiante());
        }else mostrarMensaje("El valor seleccionado como estudiante no es del tipo necesario.");
    }
    
     /**
     * Permite buscar los datos del estudiante seleccionado
     * @param claveEstudiante Clave del estudiante
     */
    public void buscarDatosEstudiante(Integer claveEstudiante){
        ResultadoEJB<DtoDatosEstudiante> res = ejbRegistroBajas.buscarDatosEstudiante(claveEstudiante);
        if(res.getCorrecto()){
        rol.setDatosEstudiante(res.getValor());
        Ajax.update("frm");
        buscarRegistrosEstudiante();
        }else mostrarMensajeResultadoEJB(res);
    }
   
     /**
     * Permite buscar tipos de registros del estudiante seleccionado
     */
    public void buscarRegistrosEstudiante(){
        ResultadoEJB<List<DtoEliminarRegistrosEstIns>> res = ejb.buscarRegistrosEstudiante(rol.getDatosEstudiante());
        if (res.getCorrecto()) {
            rol.setRegistrosEstudiante(res.getValor());
            mostrarMensajeResultadoEJB(res);
            if(!rol.getRegistrosEstudiante().isEmpty()){
                rol.setExistenRegistros(true);
            }
            Ajax.update("tbRegistros");
        } else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite eliminar la inscripción del estudiante seleccionado, así como sus registros relacionados: baja, asistencias, calificaciones y tutorías
     * en tipo de evaluacion "Ordinaria"
     */
    public void eliminarInscripcion(){
        ResultadoEJB<Boolean> res = ejb.eliminarInscripcion(rol.getDatosEstudiante().getEstudiante(), rol.getRegistrosEstudiante());
        if (res.getCorrecto()) {
            mostrarMensajeResultadoEJB(res);
            if(res.getValor()){
                inicializarValores();
            }
            Ajax.update("frm");
            Ajax.update("tbRegistros");
        } else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite inicializar los valores una vez que se elimine correctamente la información o se seleccione otro estudiante.
     * en tipo de evaluacion "Ordinaria"
     */
    public void inicializarValores(){
        rol.setEstudianteSeleccionado(null);
        rol.setDatosEstudiante(null);
        rol.setExistenRegistros(false);
        rol.setRegistrosEstudiante(null);
    }
    
}
