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
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EJBAdimEstudianteBase;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacionTutor2;
import mx.edu.utxj.pye.sgi.entity.ch.EstudiantesClaves;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionTutor;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionesTutores;

/**
 *
 * @author UTXJ
 */
@Named(value = "evaluacionTutor2")
@SessionScoped
public class EvaluacionTutor2 extends ViewScopedRol{

    private static final long serialVersionUID = -5394114656610194476L;
    @Getter private Boolean cargada = false;
    @Getter private Boolean finalizado = false;
    @Getter @Setter String valor;

    @Getter private Evaluaciones evaluacion;
    @Getter private PeriodosEscolares periodoEscolar;
    @Getter private Personal tutorEstudiante;
    @Getter @Setter AlumnosEncuestas estudianteTutor2;
    @Getter @Setter EstudiantesClaves estudianteClave;
    @Getter private EvaluacionTutoresResultados resultados;
    @Getter private List<Apartado> apartados;
    @Getter private List<SelectItem> respuestasPosibles;
    @Inject private LogonMB logonMB;
    @EJB EjbEvaluacionTutor2 ejbEvaluacionTutor2;
    @EJB EJBAdimEstudianteBase ejbEstudianteBase;


    @PostConstruct
    public void init(){
        //Busca la Evaluación activa
        evaluacion = ejbEvaluacionTutor2.evaluacionActiva();
        System.out.println("evaluacion = " + evaluacion);
        getTutor();
        System.out.println("EJECUTO TUTOR");

        if(evaluacion != null){
            //buscar estudiantes en bases de SAUIIT Y Control Escolar
            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor2.init()-->Entro al initi evaluacion no nula -- Periodo de la evaluacion" + evaluacion.getPeriodo());
            estudianteClave = ejbEstudianteBase.getClaveEstudiante(Integer.parseInt(logonMB.getCurrentUser()), evaluacion.getPeriodo());
            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor2.init()--> Ejecuto bien al estudianyte" + estudianteClave);
            if(estudianteClave !=null){

                ResultadoEJB<EvaluacionTutoresResultados>  resResultados = ejbEvaluacionTutor2.getResultadosEvaluacionTutorEstudiante(evaluacion, estudianteClave);
                if(resResultados.getCorrecto()){
                    mostrarMensajeResultadoEJB(resResultados);
                    resultados= resResultados.getValor();
                    System.out.println("resultados = " + resultados);
                    apartados = ejbEvaluacionTutor2.getApartados();

                    System.out.println("apartados = " + apartados);
                    respuestasPosibles = ejbEvaluacionTutor2.getRespuestasPosibles();
                    System.out.println("respuestasPosibles = " + respuestasPosibles);
                    cargada = true;
                    comprobar();
                }else {mostrarMensajeResultadoEJB(resResultados);}



            }else{
                System.out.println("El alumno no esta registrado en ninguna base, pye, sauiit o control escolar");
            }


        }
    }
    public void getTutor(){
        estudianteTutor2 = new AlumnosEncuestas();
        ResultadoEJB<AlumnosEncuestas> estudiante = ejbEvaluacionTutor2.getEstudianteTutorSauiit(logonMB.getCurrentUser());
        estudianteTutor2 = estudiante.getValor();
        ResultadoEJB<Personal> restutor = ejbEvaluacionTutor2.getTutor(estudianteTutor2);
        tutorEstudiante= restutor.getValor();


    }

    public void guardar(ValueChangeEvent e) throws ELException{

        UIComponent id = (UIComponent)e.getSource();

        if(e.getNewValue() != null){
            valor = e.getNewValue().toString();
        }else{
            valor = e.getOldValue().toString();
        }
        ResultadoEJB<EvaluacionTutoresResultados> guardar=ejbEvaluacionTutor2.cargarResultadosEstudianteClave(id.getId(), valor, resultados, Operacion.REFRESCAR);
        ResultadoEJB<EvaluacionTutoresResultados> update=ejbEvaluacionTutor2.cargarResultadosEstudianteClave(id.getId(), valor, resultados, Operacion.PERSISTIR);
        comprobar();

    }
    public void comprobar(){
        Comparador<EvaluacionTutoresResultados> comparador = new ComparadorEvaluacionTutor();
        finalizado = comparador.isCompleto(resultados);
    }
}
