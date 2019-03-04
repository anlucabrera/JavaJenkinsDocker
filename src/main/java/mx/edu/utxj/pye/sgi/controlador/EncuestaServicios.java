package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuesta;
import mx.edu.utxj.pye.sgi.ejb.EjbEncuestaServicios;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;

/**
 *
 * @author UTXJ
 */
@Named(value = "encuestaServicios")
@SessionScoped
public class EncuestaServicios implements Serializable {
    private static final long serialVersionUID = -615669920932201958L;

    @Getter private Boolean cargada,finalizado;
    @Getter @Setter Short grado = 2;
    @Getter private Evaluaciones evaluacion;
    @Getter private String evaluador;
    @Getter private Integer evaluadorr;
    @Getter @Setter private EncuestaServiciosResultados resultado;
    
    @Getter @Setter Map<String,String> respuestas;
    @Getter private List<SelectItem> respuestasPosibles, respuestaSioNo, rangoDesision;
    @Getter private List<Apartado> apartados;
    @Getter @Setter private Alumnos alumno;
    @Getter @Setter private PeriodosEscolares periodoEsc;
    
    @EJB EjbAdministracionEncuesta ejbAdmEncuesta;
    @EJB EjbEncuestaServicios ejb;
    @Inject LogonMB logonMB;
    
    @PostConstruct
    public void init(){
        try {
            finalizado = false;
            respuestas = new HashMap<>();
            respuestasPosibles = ejb.getRespuestasPosibles();
            respuestaSioNo = ejb.getSioNO();
            rangoDesision = ejb.getRangoDesision();
            evaluacion = ejb.getEvaluacionActiva();
            if (evaluacion != null) {
                evaluador = logonMB.getCurrentUser();
                alumno = ejb.obtenerAlumnos(evaluador);
                evaluadorr=Integer.parseInt(evaluador);
                periodoEsc=ejb.getPeriodo(evaluacion);
                if (alumno != null) {
                    resultado = ejb.getResultado(evaluacion, evaluadorr, respuestas);
                    if (resultado != null) {
                        apartados = ejb.getApartados();
                        finalizado = ejb.actualizarResultado(resultado);
                        cargada = true;
                    }
                }
            }
        } catch (Exception e) {
            cargada = false;
            System.out.println("mx.edu.utxj.pye.sgi.controlador.EncuestaServicios.init() e: " + e.getMessage());
        }
    }  
    public void guardarRespuesta(ValueChangeEvent e){
        UIComponent origen = (UIComponent)e.getSource();
        String valor = e.getNewValue().toString();
        ejb.actualizarRespuestaPorPregunta(resultado, origen.getId(), valor, respuestas);
        finalizado = ejb.actualizarResultado(resultado);
    }
    
}
