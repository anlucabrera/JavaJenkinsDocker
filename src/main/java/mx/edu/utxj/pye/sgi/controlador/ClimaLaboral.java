package mx.edu.utxj.pye.sgi.controlador;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.EjbClimaLaboral;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesClimaLaboralResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



@Named(value = "climaLaboral")
@SessionScoped
public class ClimaLaboral implements Serializable {
    private static final long serialVersionUID = 5437081347075700613L;
    @Getter private Boolean finalizado = false;
    @Getter private Boolean cargada = false;
    @Getter private Boolean enviable = false;
    
    @Getter private Evaluaciones evaluacion;
    @Getter private Personal evaluador;
    @Getter private PeriodosEscolares periodoEscolar;
    private EvaluacionesClimaLaboralResultados resultado;
    @Getter @Setter private Map<String, String> respuestas;
    
    @EJB EjbClimaLaboral ejb;
    @EJB private Facade facade;
    @Inject LogonMB logonMB;
    

@Getter private Boolean cargado = false;



    @PostConstruct
    public void init() {
        try {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            if (logonMB.getUsuarioTipo() != UsuarioTipo.ESTUDIANTE) {
                respuestas = new HashMap<>();

                try {
                    finalizado = false;

                    evaluacion = ejb.getEvaluacionActiva();
                    if (evaluacion == null) {
                        return;
                    }
                    evaluador = ejb.getEvaluador(Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina()));
                    if (evaluador == null) {
                        return;
                    }
                    resultado = ejb.getResultado(evaluacion, evaluador);
                    if (resultado == null) {
                        return;
                    }
                    facade.setEntityClass(PeriodosEscolares.class);

                    if (resultado != null) {
                        periodoEscolar = (PeriodosEscolares) facade.find(evaluacion.getPeriodo());
                        ejb.vaciarRespuestas(resultado, respuestas);
                        cargada = true;
                        verificarFinalizado();
                    }
                } catch (EJBException e) {
                    cargada = false;
                    System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionControlInterno.init() e: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            cargada = false;
            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionControlInterno.init() e: " + e.getMessage());
        }
    }
        
    public void guardarRespuesta(ValueChangeEvent e){
        UIComponent origen = (UIComponent)e.getSource();
        String valor = e.getNewValue().toString();
        System.out.println("mx.edu.utxj.pye.sgi.controlador.ClimaLaboral.guardarRespuesta(" + origen.getId() + "): " + valor);
        respuestas.put(origen.getId(), valor);
        comprobarEnvio();
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.ClimaLaboral.guardarRespuesta() enviable: " + enviable);
        
        ejb.actualizarRespuestaPorPregunta(resultado, origen.getId(), valor);
        verificarFinalizado();
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.ClimaLaboral.guardarRespuesta() finalizado: " + finalizado);
    }
    
    public void comprobarEnvio(){
        //41 no aplica pra el envío
        //42 no apli
        //44 se envía el NO y se agrega no aplica
        //45 no aplica para el envío
        enviable=false;
        for (int i = 55; i <= 60; i++) {
            if(i != 58 && i != 59){
                if (respuestas.get("p" + i) != null) {
                    if (respuestas.get("p" + i).equals("0")) {
                        enviable = true;
                        break;
                    }
                }
            }
        }

        for (int i = 61; i <= 61; i++) {
            if (respuestas.get("p" + i) != null) {
                if (respuestas.get("p" + i).equals("0")) {
                    enviable = true;
                    break;
                }
            }
        }
    }
    
    public void verificarFinalizado(){
        comprobarEnvio();
        finalizado = ejb.actualizarResultado(resultado);
        comprobarEnvio();
        if (enviable && resultado.getR63() == null) {
            finalizado = false;
        }
    }
}
