/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacionTutor2;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionTutor;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionesTutores;

/**
 *
 * @author UTXJ
 */
@Named(value = "evaluacionTutor2")
@SessionScoped
public class EvaluacionTutor2 implements Serializable {

    private static final long serialVersionUID = -5394114656610194476L;
    @Getter private Boolean cargada = false;
    @Getter private Boolean finalizado = false;

    @Getter private Evaluaciones evaluacion;
    @Getter private PeriodosEscolares periodoEscolar;
    @Getter private VistaEvaluacionesTutores estudianteTutor;
    @Getter private EvaluacionesTutoresResultados resultados;
    @Getter private List<Apartado> apartados;
    @Getter private List<SelectItem> respuestasPosibles;
    @Inject private LogonMB logonMB;
    @EJB EjbEvaluacionTutor2 ejbEvaluacionTutor2;
    

    @PostConstruct
    public void init(){
        evaluacion = ejbEvaluacionTutor2.evaluacionActiva();        
        
        if(evaluacion != null){
            estudianteTutor = ejbEvaluacionTutor2.getEstudianteTutor(evaluacion.getPeriodo(), logonMB.getCurrentUser());
            periodoEscolar = ejbEvaluacionTutor2.getPeriodo(evaluacion);
            if(estudianteTutor != null){
                resultados = ejbEvaluacionTutor2.getResultados(evaluacion, estudianteTutor);
                apartados = ejbEvaluacionTutor2.getApartados();
                respuestasPosibles = ejbEvaluacionTutor2.getRespuestasPosibles();
                cargada = true;
                comprobar();
            }
        }
    }
    
    public void guardar(ValueChangeEvent event){
        String id = event.getComponent().getId().trim();
        String valor = event.getNewValue().toString();
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor2.guardar(" + id + ") valor: " + valor);
        ejbEvaluacionTutor2.actualizar(id, valor, resultados);
        ejbEvaluacionTutor2.guardar(resultados);
        comprobar();
    }
    
    public void comprobar(){
        Comparador<EvaluacionesTutoresResultados> comparador = new ComparadorEvaluacionTutor();
        finalizado = comparador.isCompleto(resultados);
    }
}
