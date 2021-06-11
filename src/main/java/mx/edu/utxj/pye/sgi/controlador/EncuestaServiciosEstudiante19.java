/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.ejb.EjbEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;


/**
 *
 * @author Planeación
 */
@Named
@ViewScoped
public class EncuestaServiciosEstudiante19 extends ViewScopedRol implements Desarrollable {
    @Getter @Setter private DtoEvaluaciones rol = new DtoEvaluaciones();
    @Getter @Setter private Boolean finalizado = false;
    @EJB private EjbEncuestaServicios ejb;
    @EJB private EjbConsultaCalificacion ejbC;
    @EJB
    EjbPropiedades ep;
    @Inject
    LogonMB logonMB;
    @Getter private Boolean cargado = false;
    @Getter private Boolean tieneAcceso = false;



    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)) return;
                if(Objects.equals(ejb.denegarAcceso(), Boolean.FALSE)) return; 
                    cargado = true;
                    setVistaControlador(ControlEscolarVistaControlador.ENCUESTA_SERVICIOS);
                    Evaluaciones evaluacion = ejb.getEvaluacionActiva();
                    if(evaluacion == null) return;
                        ResultadoEJB<Estudiante> resAcceso = ejb.validarEstudiante(Integer.parseInt(logonMB.getCurrentUser()),evaluacion.getPeriodo());
                        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
                            ResultadoEJB<Estudiante> resValidacion = ejb.validarEstudiante(Integer.parseInt(logonMB.getCurrentUser()), evaluacion.getPeriodo());
                            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
                                rol.setEstudianteCE(resValidacion.getValor());
                                tieneAcceso = rol.tieneAcceso(rol.getEstudianteCE(), UsuarioTipo.ESTUDIANTE19);
                                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
                                    ResultadoEJB<Boolean> resultadoEJB = ejb.verificarEvaluacionCOmpleta(evaluacion, rol.getEstudianteCE().getMatricula());
                                    rol.setCompleto(resultadoEJB.getValor());
                                    
                                    if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
                                    if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
                                    rol.setNivelRol(NivelRol.OPERATIVO);
                                    rol.setRespuestas(new HashMap<>());
                                    rol.respuestasPosibles = ejb.getRespuestasPosibles();
                                    EncuestaServiciosResultados resTest = ejb.getResultado(evaluacion, rol.getEstudianteCE().getMatricula(), rol.getRespuestas());
                                    rol.resultado = resTest;
                                    //dto.fechaAplicacion = dto.sdf.format(dto.resultadoTest.getFechaAplicacion());
                                    if(rol.resultado == null) return;
                                        //ResultadoEJB<List<Apartado>> resPartados = ejbTA.obtenerApartados();
                                        rol.apartados = ejb.getApartados();
                                        //System.out.println("Apartados:"+dto.apartados);
                                        finalizado = ejb.actualizarResultado(rol.resultado);
            
        } catch (Exception e) {
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
        //System.out.println("Informacion guardada:"+ rol.getResultado());
        ejb.actualizarRespuestaPorPregunta(rol.getResultado(), origen.getId(), rol.getValor(), rol.getRespuestas());
        finalizado = ejb.actualizarResultado(rol.getResultado());
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "encuesta servicios";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
////        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
}
