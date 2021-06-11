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
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.*;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author Planeación
 */
@Named(value = "encuestaSatisfaccionEgresadosTsu")
@ViewScoped
public class EncuestaSatisfaccionEgresadosTsu extends ViewScopedRol implements Desarrollable{

    @Getter @Setter private DtoEvaluaciones dto = new DtoEvaluaciones();
    @Getter @Setter private Boolean finalizado = false;
    
    @EJB EjbPropiedades ep;
    @EJB EjbSatisfaccionEgresadosTsu ejbTA;
    @Inject LogonMB logonMB;
    
    @Getter private Boolean cargado = false;
    @Getter private Boolean tieneAcceso = false;
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)) return;
                if(Objects.equals(ejbTA.tieneAccesoTest().getValor(), Boolean.FALSE)) return; 
                    cargado = true;
                    setVistaControlador(ControlEscolarVistaControlador.ENCUESTA_SATISFACCION_TSU);
                    Evaluaciones evaluacion = ejbTA.getEvaluacionActiva();
                    if(evaluacion == null) return;
                        ResultadoEJB<Estudiante> resAcceso = ejbTA.validarEstudiante(Integer.parseInt(logonMB.getCurrentUser()),evaluacion.getPeriodo());
                        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
                            ResultadoEJB<Estudiante> resValidacion = ejbTA.validarEstudiante(Integer.parseInt(logonMB.getCurrentUser()), evaluacion.getPeriodo());
                            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
                                dto.setEstudianteCE(resValidacion.getValor());
                                tieneAcceso = dto.tieneAcceso(dto.getEstudianteCE(), UsuarioTipo.ESTUDIANTE19);
                                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
                                    ResultadoEJB<Boolean> resultadoEJB = ejbTA.verificarEncuestaCompleta(evaluacion, dto.getEstudianteCE().getMatricula());
                                    dto.setEncuestaTSUCompleto(resultadoEJB.getValor());
                                    
                                    if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
                                    if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
                                    dto.setNivelRol(NivelRol.OPERATIVO);
                                    dto.setRespuestas(new HashMap<>());
                                    dto.respuestasPosibles = ejbTA.getRespuestasPosibles();
                                    ResultadosEncuestaSatisfaccionTsu resTest = ejbTA.getResultado(evaluacion, dto.getEstudianteCE().getMatricula(), dto.getRespuestas());
                                    dto.resultadoREST = resTest;
                                    //dto.fechaAplicacion = dto.sdf.format(dto.resultadoTest.getFechaAplicacion());
                                    if(dto.resultadoREST == null) return;
                                        //ResultadoEJB<List<Apartado>> resPartados = ejbTA.obtenerApartados();
                                        dto.apartados = ejbTA.getApartados();
                                        //System.out.println("Apartados:"+dto.apartados);
                                        finalizado = ejbTA.actualizarResultado(dto.resultadoREST);
            
        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }
    
    public void guardarRespuesta(ValueChangeEvent cve) {
        UIComponent origen = (UIComponent) cve.getSource();
        if(cve.getNewValue().toString() != null){
            dto.valor = cve.getNewValue().toString();
        }else{
            dto.valor = cve.getOldValue().toString();
        }
        ejbTA.actualizarRespuestaPorPregunta(dto.resultadoREST, origen.getId(), dto.valor, dto.respuestas);
        finalizado = ejbTA.actualizarResultado(dto.resultadoREST);
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "encuesta de satisfaccion de egresados tsu";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
////        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
    
}
