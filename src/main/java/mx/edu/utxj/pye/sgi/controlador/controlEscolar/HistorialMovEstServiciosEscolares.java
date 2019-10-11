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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
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
    @PostConstruct
    public void init(){
        try{
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
          
            rol.getInstrucciones().add("Ingrese nombre o matricula del o de la estudiante que dará de baja.");
            rol.getInstrucciones().add("Seleccionar causa de baja.");
            rol.getInstrucciones().add("Seleccionar tipo de baja.");
            rol.getInstrucciones().add("Seleccionar fecha de la baja.");
            rol.getInstrucciones().add("Dar clic en el botón de Guardar para registrar la baja del estudiante.");
            rol.getInstrucciones().add("Una vez que se haya registrado la baja en la parte inferior podrá visualizar una tabla con la información registrada.");
            rol.getInstrucciones().add("En la columna OPCIONES, usted puede: Consultar materias reprobadas, Generar formato y Eliminar baja.");
            rol.getInstrucciones().add("El botón de Consultar materias reprobadas se habilita únicamente en el caso de que la baja haya sido por reprobación.");
            rol.getInstrucciones().add("Para generar el formato de baja de clic en el botón Generar formato.");
            rol.getInstrucciones().add("En caso de que se haya equivocado podrá eliminar el registro dando clic en el botón Eliminar baja y cambiar la situación actual del estudiante.");
            rol.getInstrucciones().add("Puede modificar las acciones tomadas por el tutor y el dictamen de psicopedagogía.");
           
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "historial de movimiento estudiante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Método para proporcionar lista de docentes sugeridos en un autocomplete donde se puede ingresar el número de nómina, nombre o área del docente
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
            Ajax.update("tbRegBaja");
        }else mostrarMensaje("El valor seleccionado como estudiante no es del tipo necesario.");
    }

    public void buscarHistorialMovimientos(Persona persona){
        ResultadoEJB<List<DtoHistorialMovEstudiante>> res = ejb.buscarHistorialMovEstudiante(persona);
        if(res.getCorrecto()){
        rol.setListaHistorialMovEst(res.getValor());
        Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
         Ajax.update("frm");
    }
    
}
