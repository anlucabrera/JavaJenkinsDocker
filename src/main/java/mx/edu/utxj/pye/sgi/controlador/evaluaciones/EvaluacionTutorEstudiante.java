package mx.edu.utxj.pye.sgi.controlador.evaluaciones;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import mx.edu.utxj.pye.sgi.ejb.EJBAdimEstudianteBase;
import mx.edu.utxj.pye.sgi.ejb.EjbNuevaEvaluacionTutor;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados2;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados3;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionTutor2;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionTutor3;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author Taatisz :P
 * Evaluacion al tutor nueva
 * Fecha de actualizacion 4/03/2020
 *
 */
@Named
@ViewScoped
public class EvaluacionTutorEstudiante extends ViewScopedRol {

    @Getter private Boolean cargada = false;
    @Getter private Boolean finalizado = false;
    @Getter @Setter String valor;
    @Getter private Evaluaciones evaluacion;
    @Getter @Setter private dtoEstudiantesEvalauciones estudiante;
    @Getter private Personal tutorEstudiante;
    @Getter private EvaluacionTutoresResultados2 resultados;
    @Getter @Setter private EvaluacionTutoresResultados3 resultados3;
    @Getter private List<Apartado> apartados;
    @Getter private List<SelectItem> respuestasPosibles;
    @Inject private LogonMB logonMB;
    @EJB EjbNuevaEvaluacionTutor ejbEvaluacionTutor2;
    @EJB EJBAdimEstudianteBase ejbEstudianteBase;

    @PostConstruct
    public void init() {
        cargada=false;
        setVistaControlador(ControlEscolarVistaControlador.EVALAUCION_TUTOR_NUEVA);
        //Busca la evaluacion activa
        ResultadoEJB<Evaluaciones> resEvaluacion = ejbEvaluacionTutor2.getEvaluacionTutorActiva();
        if (resEvaluacion.getCorrecto() == true) {
            evaluacion = resEvaluacion.getValor();
            //Busca estudiantes en bases de SAUIIT Y Control Escolar
            ResultadoEJB<dtoEstudiantesEvalauciones> resEstudiante = ejbEstudianteBase.getClaveEstudiante(logonMB.getCurrentUser(), evaluacion.getPeriodo());
            if(resEstudiante.getCorrecto()==true){
                //Los estudiantes de 7mo y 11vo ya no responden la evaluacion así que se les niega el acceso
                if(resEstudiante.getValor().getGrado()== 6 || resEstudiante.getValor().getGrado() ==11){
                    cargada=false;
                }else {
                    estudiante = resEstudiante.getValor();
                    //System.out.println("Estudiante -> " + estudiante + "Tipo Evaluacion" + evaluacion.getTipo());
                    //El estudiante se encuentra en alguna de las dos bases (Sauiit o Control escolar)
                    //Se comprueba el tipo de la evaluacion
                    if(evaluacion.getTipo().equals(EvaluacionesTipo.TUTOR.getLabel())){
                        //System.out.println("Tipo 1");
                        //Se cargan los apartados de la evalaucion con el cuestionario del tipo 1
                        //Busca los resultados del estudiante que se encontro, si no existen, se crean
                        ResultadoEJB<EvaluacionTutoresResultados2>  resResultados = ejbEvaluacionTutor2.getResultadosEvaluacionTutorEstudiante(evaluacion, resEstudiante.getValor());
                        if(resResultados.getCorrecto()){
                            mostrarMensajeResultadoEJB(resResultados);
                            resultados= resResultados.getValor();
                            //System.out.println("resultados = " + resultados);
                            apartados = ejbEvaluacionTutor2.getApartados();
                            //System.out.println("apartados = " + apartados);
                            respuestasPosibles = ejbEvaluacionTutor2.getRespuestasPosibles();
                            //System.out.println("respuestasPosibles = " + respuestasPosibles);
                            // Se activa la evaluacion
                            cargada = true;
                            tutorEstudiante = resEstudiante.getValor().getTutor();
                            comprobar();
                        }else {mostrarMensajeResultadoEJB(resResultados);}
                    }
                    else if(evaluacion.getTipo().equals(EvaluacionesTipo.TUTOR_2.getLabel())){
                        //System.out.println("Tipo 2");
                        //Carga los apartados del cuestionario tipo 2
                        //Se cargan los apartados de la evalaucion con el cuestionario del tipo 1
                        //Busca los resultados del estudiante que se encontro, si no existen, se crean
                        ResultadoEJB<EvaluacionTutoresResultados3>  resResultados = ejbEvaluacionTutor2.getResultadosEvaluacionTutor3Estudiante(evaluacion, resEstudiante.getValor());
                        if(resResultados.getCorrecto()){
                            mostrarMensajeResultadoEJB(resResultados);
                            resultados3= resResultados.getValor();
                            //System.out.println("resultados = " + resultados);
                            apartados = ejbEvaluacionTutor2.getApartadosTutor2();
                            //System.out.println("apartados = " + apartados);
                            respuestasPosibles = ejbEvaluacionTutor2.getRespuestasPosibles();
                            //System.out.println("respuestasPosibles = " + respuestasPosibles);
                            // Se activa la evaluacion
                            cargada = true;
                            tutorEstudiante = resEstudiante.getValor().getTutor();
                            comprobar2();
                        }else {mostrarMensajeResultadoEJB(resResultados);}

                    }
                }

            }else {mostrarMensajeResultadoEJB(resEstudiante);}
        }else {cargada= false;}

    }
    public void getTipoEvaluacion(){}

    /**
     * Guarda resultado seleccionado
     * @param e
     * Evaluacion Tutur
     * @throws ELException
     */
    public void guardar(ValueChangeEvent e) throws ELException {

        UIComponent id = (UIComponent)e.getSource();

        if(e.getNewValue() != null){
            valor = e.getNewValue().toString();
        }else{
            valor = e.getOldValue().toString();
        }
        ResultadoEJB<EvaluacionTutoresResultados2> guardar=ejbEvaluacionTutor2.cargarResultadosEstudianteClave(id.getId(), valor, resultados, Operacion.REFRESCAR);
        ResultadoEJB<EvaluacionTutoresResultados2> update=ejbEvaluacionTutor2.cargarResultadosEstudianteClave(id.getId(), valor, resultados, Operacion.PERSISTIR);
        comprobar();

    }
    /**
     * Guarda resultado seleccionado
     * @param e
     * Evaluacion Tutur (Cuestionario 2)
     * @throws ELException
     */
    public void guardarTipo2(ValueChangeEvent e) throws ELException {

        UIComponent id = (UIComponent)e.getSource();

        if(e.getNewValue() != null){
            valor = e.getNewValue().toString();
        }else{
            valor = e.getOldValue().toString();
        }
        ResultadoEJB<EvaluacionTutoresResultados3> guardar=ejbEvaluacionTutor2.cargarResultadosEstudianteClave3(id.getId(), valor, resultados3, Operacion.REFRESCAR);
        ResultadoEJB<EvaluacionTutoresResultados3> update=ejbEvaluacionTutor2.cargarResultadosEstudianteClave3(id.getId(), valor, resultados3, Operacion.PERSISTIR);
        comprobar2();

    }
    /*
    Comprueba si ya termino la evaluacion (Evaluacion Tutor)
     */
    public void comprobar(){
        Comparador<EvaluacionTutoresResultados2> comparador = new ComparadorEvaluacionTutor2();
        finalizado = comparador.isCompleto(resultados);
        if(finalizado ==true){
            ResultadoEJB<EvaluacionTutoresResultados2> resUpdate = ejbEvaluacionTutor2.updateCompleto(resultados,finalizado);
            resultados = resUpdate.getValor();
            if(!resUpdate.getCorrecto()){mostrarMensajeResultadoEJB(resUpdate);}
        }
    }
    /*
   Comprueba si ya termino la evaluacion (Evaluacion Tutor  (Cuestionario 2))
    */
    public void comprobar2(){
        Comparador<EvaluacionTutoresResultados3> comparador = new ComparadorEvaluacionTutor3();
        finalizado = comparador.isCompleto(resultados3);
        if(finalizado ==true){
            ResultadoEJB<EvaluacionTutoresResultados3> resUpdate = ejbEvaluacionTutor2.updateCompleto2(resultados3,finalizado);
            resultados3 = resUpdate.getValor();
            if(!resUpdate.getCorrecto()){mostrarMensajeResultadoEJB(resUpdate);}
        }
    }

}
