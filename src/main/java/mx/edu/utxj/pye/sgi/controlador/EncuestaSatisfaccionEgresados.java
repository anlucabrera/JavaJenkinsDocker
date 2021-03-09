/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.ejb.EjbSatisfaccionEgresadosIng;

/**
 *
 * @author Planeación
 */
@Named(value = "encuestaSatisfaccionEgresadosIng")
@SessionScoped
public class EncuestaSatisfaccionEgresados implements Serializable {

    private static final long serialVersionUID = -8590148329530476409L;
    @Getter @Setter private DtoEvaluaciones dto = new DtoEvaluaciones();
    @EJB private EjbSatisfaccionEgresadosIng ejb;
    @Inject LogonMB logonMB;
    
    @PostConstruct
    public void init() {
        try {
            dto.finalizado = false;
            dto.respuestas = new HashMap<>();
            dto.respuestasPosibles = ejb.getRespuestasPosibles();
            dto.evaluacion = ejb.getEvaluacionActiva();
            if(dto.evaluacion == null) return;
                dto.evaluador = logonMB.getCurrentUser();
                dto.alumno=ejb.obtenerAlumnos(dto.evaluador);
                if(dto.alumno == null) return;
                    dto.evaluadorr= Integer.parseInt(dto.evaluador);
                    dto.resultadoESEI = ejb.getResultado(dto.evaluacion, dto.evaluadorr, dto.respuestas);
                    if(dto.resultadoESEI == null) return;
                        dto.apartados = ejb.getApartados();
                        dto.finalizado = ejb.actualizarResultado(dto.resultadoESEI);
                        dto.cargada = Boolean.TRUE;
                        dto.estOnceavo = ejb.mostrarApartados();
        } catch (Exception e) {
            dto.cargada = false;
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EncuestaSatisfaccionEgresados.init() e: " + e.getMessage());
        }
    }
    public void guardarRespuesta(ValueChangeEvent e) {
        UIComponent origen = (UIComponent) e.getSource();
        if(e.getNewValue() != null){
           dto.valor = e.getNewValue().toString();
        }else{
           dto.valor = e.getOldValue().toString();
        }
        
        ejb.actualizarRespuestaPorPregunta(dto.resultadoESEI, origen.getId(), dto.valor, dto.respuestas);
        dto.finalizado = ejb.actualizarResultado(dto.resultadoESEI);
    }
    
    
}
