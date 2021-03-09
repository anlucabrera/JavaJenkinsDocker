package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import javax.inject.*;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import lombok.*;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.ejb.EjbEncuestaServicios;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;

/**
 *
 * @author UTXJ
 */
@Named(value = "encuestaServicios")
@SessionScoped
public class EncuestaServicios implements Serializable {
    private static final long serialVersionUID = -615669920932201958L;

    @Getter @Setter private DtoEvaluaciones dto = new DtoEvaluaciones();
    
    @EJB EjbEncuestaServicios ejbES;
    @Inject LogonMB logonMB;
    
    @PostConstruct
    public void init(){
        try {
            dto.finalizado = false;
            dto.respuestas = new HashMap<>();
            dto.evaluacion = new Evaluaciones();
            dto.evaluacionAnterior = new Evaluaciones();
            dto.respuestasPosibles = ejbES.getRespuestasPosibles();
            dto.evaluacion = ejbES.getEvaluacionActiva();
            if(ejbES.aperturaEncuestaSimultaneas() == Boolean.TRUE){
                //System.out.println("Si aplica para encuesta simultanea");
                dto.evaluacionAnterior = ejbES.getEvaluacionActivaAnterior();
            }
            
            if(dto.evaluacion == null) return;
                dto.evaluador = logonMB.getCurrentUser();
                dto.alumno = ejbES.obtenerAlumnos(dto.evaluador);
                if(dto.alumno == null) return;
                    dto.evaluadorr = Integer.parseInt(dto.evaluador);
                    Integer periodo = dto.alumno.getGrupos().getGruposPK().getCvePeriodo();
                    if(periodo.equals(dto.evaluacionAnterior.getPeriodo())){dto.resultado = ejbES.getResultado(dto.evaluacionAnterior, dto.evaluadorr, dto.respuestas);}
                    if(periodo.equals(dto.evaluacion.getPeriodo())){dto.resultado = ejbES.getResultado(dto.evaluacion, dto.evaluadorr, dto.respuestas);}
                    if(dto.resultado == null) return;
                        dto.apartados = ejbES.getApartados();
                        dto.finalizado = ejbES.actualizarResultado(dto.resultado);
                        dto.cargada = true;
                        dto.mostrarES = ejbES.mostrarApartados();
        } catch (Exception e) {
            dto.cargada = false;
            System.out.println("mx.edu.utxj.pye.sgi.controlador.EncuestaServicios.init() e: " + e.getMessage());
        }
    }  
    public void guardarRespuesta(ValueChangeEvent e) throws ELException{
        UIComponent origen = (UIComponent)e.getSource();
        if(e.getNewValue() != null){
            dto.valor = e.getNewValue().toString();
        }else{
            dto.valor = e.getOldValue().toString();
        }
        ejbES.actualizarRespuestaPorPregunta(dto.resultado, origen.getId(), dto.valor, dto.respuestas);
            dto.finalizado = ejbES.actualizarResultado(dto.resultado);
        
    }
    
}
