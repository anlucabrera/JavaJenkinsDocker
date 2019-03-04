/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuesta;
import mx.edu.utxj.pye.sgi.ejb.EjbSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;

/**
 *
 * @author Planeación
 */
@Named(value = "encuestaSatisfaccionEgresadosIng")
@SessionScoped
public class EncuestaSatisfaccionEgresados implements Serializable {

    private static final long serialVersionUID = -8590148329530476409L;
    
    @Getter private Boolean cargada,finalizado;
    @Getter @Setter private EncuestaSatisfaccionEgresadosIng resultado;
    
    @Getter private Evaluaciones evaluacion;
    @Getter private Boolean estOnceavo;
    @Getter private String evaluador;
    @Getter private Integer evaluadorr;
    @Getter @Setter Map<String, String> respuestas;
    @Getter private List<SelectItem> respuestasPosibles;
    @Getter private List<Apartado> apartados;
    @Getter @Setter private Alumnos estudiante;
    @Getter @Setter private PeriodosEscolares periodoEsc;
    
    @EJB private EjbAdministracionEncuesta ejbAdmEncuesta;
    @EJB private EjbSatisfaccionEgresadosIng ejb;
    @Inject LogonMB logonMB;
    
    @PostConstruct
    public void init() {
//        alumnosNoAccedieronEncuestaEgre();
        try {
            finalizado = false;
            respuestas = new HashMap<>();
            respuestasPosibles = ejb.getRespuestasPosibles();
            evaluacion = ejb.getEvaluacionActiva();
            if (evaluacion != null) {
                
                evaluador = logonMB.getCurrentUser();
                //System.out.println("mx.edu.utxj.pye.sgi.controlador.EncuestaSatisfaccionEgresados.init()" + evaluador);
                estudiante=ejb.obtenerAlumnos(evaluador);
                Short grado = 11;
                if (estudiante.getGradoActual().equals(grado)) {
                    estOnceavo=true;
                    evaluadorr = Integer.parseInt(evaluador);
                    //System.out.println("mx.edu.utxj.pye.sgi.controlador.EncuestaSatisfaccionEgresados.init()" + estudiante);
                    periodoEsc = ejb.getPeriodo(evaluacion);
                    //System.out.println("mx.edu.utxj.pye.sgi.controlador.EncuestaSatisfaccionEgresados.init()" + periodoEsc);
                    if (estudiante != null) {
                        resultado = ejb.getResultado(evaluacion, evaluadorr, respuestas);
                        if (resultado != null) {
                            apartados = ejb.getApartados();
                            finalizado = ejb.actualizarResultado(resultado);
                            cargada = true;
                        }
                    }
                }else{
                    estOnceavo=false;
                }

            }
        } catch (Exception e) {
            cargada = false;
            System.out.println("mx.edu.utxj.pye.sgi.controlador.EncuestaSatisfaccionEgresados.init() e: " + e.getMessage());
        }
    }
    public void guardarRespuesta(ValueChangeEvent e) {
        UIComponent origen = (UIComponent) e.getSource();
        String valor = e.getNewValue().toString();
        ejb.actualizarRespuestaPorPregunta(resultado, origen.getId(), valor, respuestas);
        finalizado = ejb.actualizarResultado(resultado);
    }
    
    
}
