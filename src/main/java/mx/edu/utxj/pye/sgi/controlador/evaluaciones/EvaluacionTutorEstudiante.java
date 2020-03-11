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
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionTutor2;
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
    @Getter private List<Apartado> apartados;
    @Getter private List<SelectItem> respuestasPosibles;
    @Inject private LogonMB logonMB;
    @EJB EjbNuevaEvaluacionTutor ejbEvaluacionTutor2;
    @EJB EJBAdimEstudianteBase ejbEstudianteBase;

    @PostConstruct
    public void init() {
        setVistaControlador(ControlEscolarVistaControlador.EVALAUCION_TUTOR_NUEVA);
        //Busca la evaluacion activa
        ResultadoEJB<Evaluaciones> resEvaluacion = ejbEvaluacionTutor2.getEvaluacionTutorActiva();
        if (resEvaluacion.getCorrecto() == true) {
            evaluacion = resEvaluacion.getValor();
            //Busca estudiantes en bases de SAUIIT Y Control Escolar
            ResultadoEJB<dtoEstudiantesEvalauciones> resEstudiante = ejbEstudianteBase.getClaveEstudiante(logonMB.getCurrentUser(), evaluacion.getPeriodo());
            if(resEstudiante.getCorrecto()==true){
                //Los estudiantes de 7mo y 11vo ya no responden la evaluacion así que se les niega el acceso
                if(resEstudiante.getValor().getGrado()!= 6 || resEstudiante.getValor().getGrado() !=11){
                    estudiante = resEstudiante.getValor();
                    //El estudiante se encuentra en alguna de las dos bases (Sauiit o Control escolar)
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

                }else {cargada= false;}

            }else {mostrarMensajeResultadoEJB(resEstudiante);}
        }else {cargada= false;}

    }

    /**
     * Guarda
     * @param e
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

    public void comprobar(){
        Comparador<EvaluacionTutoresResultados2> comparador = new ComparadorEvaluacionTutor2();
        finalizado = comparador.isCompleto(resultados);
        if(finalizado ==true){
            ResultadoEJB<EvaluacionTutoresResultados2> resUpdate = ejbEvaluacionTutor2.updateCompleto(resultados,finalizado);
            resultados = resUpdate.getValor();
            if(!resUpdate.getCorrecto()){mostrarMensajeResultadoEJB(resUpdate);}
        }
    }

}
