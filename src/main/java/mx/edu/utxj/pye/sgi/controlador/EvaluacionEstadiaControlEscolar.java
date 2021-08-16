/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacionEstadia;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstadiaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author Planeacion
 */
@Named
@ViewScoped
public class EvaluacionEstadiaControlEscolar extends ViewScopedRol implements Desarrollable{
    @Getter @Setter DtoEvaluaciones dto = new DtoEvaluaciones();
    @Getter @Setter Boolean tieneAcceso = false;
    @Getter @Setter Boolean finalizado = false;
    @Getter @Setter Boolean cargado = false;
    @Getter @Setter Boolean cargada = false;
    @Getter @Setter Boolean mostrar = false;
    
    @EJB EjbPropiedades ep;
    @EJB EjbEvaluacionEstadia ejbEE;
    
    @Inject LogonMB logonMB;
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)) return;
                if(Objects.equals(ejbEE.tieneAccesoEvaluacionEstadia().getValor(), Boolean.FALSE)) return;
                //System.out.println("Tiene acceso");
                cargado = true;
                    setVistaControlador(ControlEscolarVistaControlador.EVALUACION_ESTADIA);
                    Evaluaciones evaluacion = ejbEE.getEvaluacionActiva();
                    if(evaluacion == null) return;
                    //System.out.println("Evaluacioon:"+ evaluacion.getPeriodo()+"-"+evaluacion.getTipo());
                        ResultadoEJB<Estudiante> resAcceso = ejbEE.validarEstudiante(Integer.parseInt(logonMB.getCurrentUser()),evaluacion.getPeriodo());
                        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
                            ResultadoEJB<Estudiante> resValidacion = ejbEE.validarEstudiante(Integer.parseInt(logonMB.getCurrentUser()), evaluacion.getPeriodo());
                            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
                                dto.setEstudianteCE(resValidacion.getValor());
                                tieneAcceso = dto.tieneAcceso(dto.getEstudianteCE(), UsuarioTipo.ESTUDIANTE19);
                                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
                                //System.out.println("El estudiante:"+ dto.estudianteCE.getMatricula() + " tiene acceso");
                                    ResultadoEJB<Boolean> resultadoEJB = ejbEE.verificarEvaluacionCompleta(evaluacion, dto.getEstudianteCE().getMatricula());
                                    dto.setEvaluacionEstadiaCompleto(resultadoEJB.getValor());
                                    //System.out.println("Evaluacion completo:"+ dto.getEvaluacionEstadiaCompleto());
                                        if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
                                        if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
                                        dto.setNivelRol(NivelRol.OPERATIVO);
                                        dto.setRespuestas(new HashMap<>());
                                        dto.respuestasPosibles1 = ejbEE.getRespuestasPosibles1();
                                        dto.respuestasPosibles2 = ejbEE.getRespuestasPosibles2();
                                        dto.respuestasPosibles3 = ejbEE.getRespuestasPosibles3();
                                        dto.respuestasPosibles4 = ejbEE.getRespuestasPosibles4();
                                        dto.respuestasPosibles5 = ejbEE.getRespuestasPosibles5();
                                        dto.areasUniversidad = ejbEE.obtenerCarrera(dto.estudianteCE.getCarrera()).getValor();
                                        //System.out.println("Carrera"+ dto.areasUniversidad.getNombre());
                                        dto.seguimiento = ejbEE.obtenerAsesor(dto.getEstudianteCE().getMatricula()).getValor();
                                        if(dto.seguimiento.equals(new SeguimientoEstadiaEstudiante())) return;
                                        dto.persona = ejbEE.obtenerDatosAsesor(dto.seguimiento.getAsesor().getPersonal()).getValor();
                                        //System.out.println("Asesor academico:" + dto.persona.getNombre());
                                        EvaluacionEstadiaResultados evaluacionRes = ejbEE.getResultado(evaluacion, String.valueOf(dto.estudianteCE.getMatricula()), dto.persona.getClave(), dto.getRespuestas());
                                        dto.resultadoEER = evaluacionRes;
                                        //System.out.println("Evaluacion"+ dto.resultadoEER);
                                            dto.apartados = ejbEE.getApartados();
                                            dto.apartados1 = ejbEE.getApartados1();
                                            dto.apartados2 = ejbEE.getApartados2();
                                            dto.apartados3 = ejbEE.getApartados3();
                                            dto.apartados4 = ejbEE.getApartados4();
                                            dto.apartados5 = ejbEE.getApartados5();
                                            dto.apartados6 = ejbEE.getApartados6();
                                            finalizado = ejbEE.actualizarResultado(dto.resultadoEER);
        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }
    
    public void guardarRespuesta(ValueChangeEvent e) throws ELException{
        UIComponent origen = (UIComponent)e.getSource();
        
        if(e.getNewValue() != null){
            dto.valor = e.getNewValue().toString();
        }else{
            dto.valor = e.getOldValue().toString();
        }
        ejbEE.actualizarRespuestaPorPregunta(dto.resultadoEER, origen.getId(), dto.valor, dto.respuestas);
            finalizado = ejbEE.actualizarResultado(dto.resultadoEER);
        
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "evaluacion de estadia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
////        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
    
}
