/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbTestAprendizaje;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestDiagnosticoAprendizaje;
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
public class TestDiagnosticoAprendizajeEstudiante extends ViewScopedRol implements Desarrollable{

    @Getter @Setter private DtoEvaluaciones dto = new DtoEvaluaciones();
    @Getter @Setter private Boolean finalizado = false;
    
    @EJB EjbPropiedades ep;
    @EJB EjbTestAprendizaje ejbTA;
    @Inject LogonMB logonMB;
    
    @Getter private Boolean cargado = false;
    @Getter private Boolean tieneAcceso = false;
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)) return;
                if(Objects.equals(ejbTA.tieneAccesoTest().getValor(), Boolean.FALSE)) return; 
                    cargado = true;
                    setVistaControlador(ControlEscolarVistaControlador.TEST_DIAGNOSTICO_APRENDIZAJE);
                    ResultadoEJB<Evaluaciones> resEvento = ejbTA.obtenerTestActivo();
                    Evaluaciones evaluacion = resEvento.getValor();
                    if(!resEvento.getCorrecto()) return;
                        ResultadoEJB<Estudiante> resAcceso = ejbTA.validarEstudiante(Integer.parseInt(logonMB.getCurrentUser()), resEvento.getValor().getPeriodo());
                        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
                            ResultadoEJB<Estudiante> resValidacion = ejbTA.validarEstudiante(Integer.parseInt(logonMB.getCurrentUser()), resEvento.getValor().getPeriodo());
                            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
                                dto.setEstudianteCE(resValidacion.getValor());
                                tieneAcceso = dto.tieneAcceso(dto.getEstudianteCE(), UsuarioTipo.ESTUDIANTE19);
                                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
                                    ResultadoEJB<Boolean> resultadoEJB = ejbTA.verificarTestCompleto(resEvento.getValor(), dto.getEstudianteCE().getMatricula());
                                    dto.setTestCompleto(resultadoEJB.getValor());
                                    
                                    if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
                                    if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
                                    if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
                                    dto.setNivelRol(NivelRol.OPERATIVO);
                                    dto.setRespuestas(new HashMap<>());
                                    ResultadoEJB<List<SelectItem>> resSelect = ejbTA.obtenerRespuestasPosibles();
                                    if(!resSelect.getCorrecto())return;
                                    dto.respuestasPosibles = resSelect.getValor();
                                    ResultadoEJB<TestDiagnosticoAprendizaje> resTest = ejbTA.obtenerResultado(evaluacion, dto.getEstudianteCE().getMatricula(), dto.getRespuestas());
                                    dto.resultadoTest = resTest.getValor();
                                    dto.fechaAplicacion = dto.sdf.format(dto.resultadoTest.getFechaAplicacion());
                                    //if(dto.resultadoREST == null) return;
                                        ResultadoEJB<List<Apartado>> resPartados = ejbTA.obtenerApartados();
                                        dto.apartados = resPartados.getValor();
                                        //System.out.println("Apartados:"+dto.apartados);
                                        finalizado = ejbTA.actualizarResultado(dto.getResultadoTest()).getValor();
            
        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }
    
    public void guardarRespuesta(ValueChangeEvent cve) throws ELException{
        UIComponent origen = (UIComponent) cve.getSource();
        if(cve.getNewValue() != null){
            dto.setValor(cve.getNewValue().toString());
        }else{
            dto.setValor(cve.getOldValue().toString());
        }
        ejbTA.actualizarRespuestaPregunta(dto.getResultadoTest(), origen.getId(), dto.getValor(), dto.getRespuestas());
        finalizado = ejbTA.actualizarResultado(dto.getResultadoTest()).getValor();
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "test de diagnositco de aprendizaje";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
////        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
    
}
