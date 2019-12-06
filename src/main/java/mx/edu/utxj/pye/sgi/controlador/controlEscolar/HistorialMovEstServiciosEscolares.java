/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoHistorialMovEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.HistorialMovEstRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbHistorialMovEstudiante;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;


import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;


/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class HistorialMovEstServiciosEscolares extends ViewScopedRol implements Desarrollable{
    @Getter @Setter HistorialMovEstRolServiciosEscolares rol;

    @EJB EjbHistorialMovEstudiante ejb;
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
            setVistaControlador(ControlEscolarVistaControlador.HISTORIAL_MOVEST);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es personal de servicios escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarServiciosEscolares(logon.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo serviciosEscolares = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new HistorialMovEstRolServiciosEscolares(filtro, serviciosEscolares);
            tieneAcceso = rol.tieneAcceso(serviciosEscolares);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(serviciosEscolares);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
          
            rol.getInstrucciones().add("Ingrese nombre o curp del o de la estudiante del que desea consultar el historial de movimientos.");
            rol.getInstrucciones().add("Seleccione el nombre o curp que corresponda.");
            rol.getInstrucciones().add("A continuación podrá visualizar el historial de movimientos separado de la siguiente forma:");
            rol.getInstrucciones().add("Datos personales.");
            rol.getInstrucciones().add("Historial de movimientos agrupados por Proceso de Admisión.");
            rol.getInstrucciones().add("Cada historial contiene información de admisión, inscripción, reinscripciones y baja.");
           
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "historial de movimiento estudiante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Método para proporcionar lista de estudiantes sugeridos en un autocomplete donde se puede ingresar la curp o nombre del estudiante
     * @param pista
     * @return Lista de sugerencias
     */
    public List<Persona> completePersonas(String pista){
        ResultadoEJB<List<Persona>> res = ejb.buscarEstudiante(pista);
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
    public void cambiarPersona(ValueChangeEvent e){
        if(e.getNewValue() instanceof Persona){
            Persona persona = (Persona)e.getNewValue();
            rol.setPersona(persona);
            buscarHistorialMovimientos(rol.getPersona());
            Ajax.update("frm");
        }else mostrarMensaje("El valor seleccionado como estudiante no es del tipo necesario.");
    }
    
     /**
     * Permite que buscar el historial de movimientos del estudiante seleccionado
     * @param persona Registro de persona del estudiante
     */
    public void buscarHistorialMovimientos(Persona persona){
        ResultadoEJB<List<DtoHistorialMovEstudiante>> res = ejb.buscarHistorialMovEstudiante(persona);
        if(res.getCorrecto()){
        rol.setListaHistorialMovEst(res.getValor());
        Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
         Ajax.update("frm");
    }
    
}
