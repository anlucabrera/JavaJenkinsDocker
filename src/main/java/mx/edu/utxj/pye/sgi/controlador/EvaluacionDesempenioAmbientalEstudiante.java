/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.EvaluacionRolMultiple;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacionDesempenioAmbiental;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDesempenioAmbientalUtxj;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Planeación
 */
@Named
@ViewScoped
public class EvaluacionDesempenioAmbientalEstudiante extends ViewScopedRol implements Desarrollable {
    @Getter @Setter private EvaluacionRolMultiple.EvaluacionRolEstudiante rol = new EvaluacionRolMultiple.EvaluacionRolEstudiante();
    @Getter @Setter private Boolean finalizado = false;
    @EJB private EjbEvaluacionDesempenioAmbiental ejb;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    @Getter private Boolean tieneAcceso = false;



    @PostConstruct
    public void init() {
        try{
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.EVALUACION_DESEMPENIO_AMBIENTAL);
            ResultadoEJB<Alumnos> resAcceso = ejb.verificarEstudiante(logonMB.getCurrentUser());
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
            ResultadoEJB<Alumnos> resValidacion = ejb.verificarEstudiante(logonMB.getCurrentUser());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
            rol.setEstudiante(resValidacion.getValor());

            tieneAcceso = rol.tieneAcceso(rol.getEstudiante(), UsuarioTipo.ESTUDIANTE);

            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            ResultadoEJB<Evaluaciones> resEvento = ejb.verificarEvaluacion();
            if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
            rol.setNivelRol(NivelRol.OPERATIVO);

            rol.setClavePersona(Integer.parseInt(rol.getEstudiante().getMatricula()));
            rol.setPersonas(ejb.obtenerEstudiante(rol.getEstudiante().getAlumnosPK().getCveAlumno()).getValor());
            ResultadoEJB<EvaluacionDesempenioAmbientalUtxj> resultados = ejb.getResultado(resEvento.getValor(), rol.getClavePersona(), rol.getRespuestas());
            rol.setResultados(resultados.getValor());
            rol.setFechaElaboracion(rol.getSdf().format(rol.getResultados().getFechaElaboracion()));
            if(rol.getResultados() != null){
                ResultadoEJB<List<Apartado>> resApartado = ejb.apartados();
                rol.setApartados(resApartado.getValor());
                finalizado = ejb.actualizarResultado(resultados.getValor());
            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }


    public void guardarRespuesta(ValueChangeEvent cve) throws ELException{
        UIComponent origen = (UIComponent) cve.getSource();
        if(cve.getNewValue() != null){
            rol.setValor(cve.getNewValue().toString());
        }else{
            rol.setValor(cve.getOldValue().toString());
        }
        ejb.actualizarRespuestaPorPregunta(rol.getResultados(), origen.getId(), rol.getValor(), rol.getRespuestas());
        finalizado = ejb.actualizarResultado(rol.getResultados());
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "evaluación desempenio ambiental";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
////        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
}
