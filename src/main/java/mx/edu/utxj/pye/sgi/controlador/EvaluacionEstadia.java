package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.ejb.EjbEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacionEstadia;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstadiaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.ViewEstudianteAsesorAcademico;

/**
 *
 * @author UTXJ
 */
@Named(value = "evaluacionEstadia")
@SessionScoped
public class EvaluacionEstadia implements Serializable{
    

    @Getter private Boolean cargada,finalizado;
    @Getter @Setter Short grado = 2;
    @Getter private Evaluaciones evaluacion;
    @Getter private String evaluador, valor;
    @Getter private Integer evaluadorr, evaluado;
    @Getter @Setter private EvaluacionEstadiaResultados resultado;
    
    @Getter @Setter Map<String,String> respuestas;
    @Getter private List<SelectItem> respuestasPosibles1, respuestasPosibles2, respuestasPosibles3, respuestasPosibles4, respuestasPosibles5;
    @Getter private List<Apartado> apartados, apartados1,apartados2, apartados3,apartados4,apartados5,apartados6;
    @Getter @Setter private Alumnos alumno;
    @Getter @Setter private ViewEstudianteAsesorAcademico asesor;
    @Getter @Setter private PeriodosEscolares periodoEsc;
    @EJB EjbEvaluacionEstadia ejb;
    @EJB EjbEncuestaServicios ejbS;
    @Inject LogonMB logonMB;
    
    @PostConstruct
    public void init(){
        try {
            evaluador = logonMB.getCurrentUser();
            finalizado = false;
            respuestas = new HashMap<>();
            respuestasPosibles1 = ejb.getRespuestasPosibles1();
            respuestasPosibles2 = ejb.getRespuestasPosibles2();
            respuestasPosibles3 = ejb.getRespuestasPosibles3();
            respuestasPosibles4 = ejb.getRespuestasPosibles4();
            respuestasPosibles5 = ejb.getRespuestasPosibles5();
            evaluacion = ejb.getEvaluacionActiva();
            //System.out.println("Evaluacion estadia"+ evaluacion);
            asesor = ejb.viewEstudianteAsesorAcademico(evaluador);
            if(asesor==null){
                return;
            }
            evaluado = Integer.parseInt(asesor.getNumeroNomina());
            if (evaluacion != null) {
                
                alumno = ejb.obtenerAlumnos(evaluador);
                //System.out.println("Alumno"+alumno.getMatricula());
                evaluadorr=Integer.parseInt(evaluador);
                periodoEsc=ejbS.getPeriodo(evaluacion);
                if (alumno != null) {
                    resultado = ejb.getResultado(evaluacion, alumno.getMatricula(), evaluado, respuestas);
                    //System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionEstadia.init() Resultado:"+ resultado);
                    if (resultado != null) {
                        apartados = ejb.getApartados();
                        apartados1 = ejb.getApartados1();
                        apartados2 = ejb.getApartados2();
                        apartados3 = ejb.getApartados3();
                        apartados4 = ejb.getApartados4();
                        apartados5 = ejb.getApartados5();
                        apartados6 = ejb.getApartados6();
                        finalizado = ejb.actualizarResultado(resultado);
                        cargada = true;
                    }
                }
            }
        } catch (Exception e) {
            cargada = false;
            e.printStackTrace();
            System.out.println("mx.edu.utxj.pye.sgi.controlador.EncuestaServicios.init() e: " + e.getMessage());
        }
    }  
    public void guardarRespuesta(ValueChangeEvent e) throws ELException{
        UIComponent origen = (UIComponent)e.getSource();
        
        if(e.getNewValue() != null){
            valor = e.getNewValue().toString();
        }else{
            valor = e.getOldValue().toString();
        }
        ejb.actualizarRespuestaPorPregunta(resultado, origen.getId(), valor, respuestas);
            finalizado = ejb.actualizarResultado(resultado);
        
    }
    
}
