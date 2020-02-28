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
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.ejb.*;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;

/**
 *
 * @author Planeación
 */
@Named(value = "encuestaSatisfaccionEgresadosTsu")
@SessionScoped
public class EncuestaSatisfaccionEgresadosTsu implements Serializable {

    private static final long serialVersionUID = -8590148329530476409L;

    @Getter @Setter private DtoEvaluaciones dto = new DtoEvaluaciones();

    @EJB private EjbSatisfaccionEgresadosTsu ejb;
    @EJB private EjbEncuestaServicios ejbES;
    @Inject LogonMB logonMB;
    
    @PostConstruct
    public void init() {
        try {
            dto.finalizado = false;
            dto.respuestas = new HashMap<>();
            dto.respuestasPosibles = ejb.getRespuestasPosibles();
            dto.evaluacion = ejb.getEvaluacionActiva();
            if (dto.evaluacion != null) {
                dto.evaluador = logonMB.getCurrentUser();
                dto.alumno=ejbES.obtenerAlumnos(dto.evaluador);
                if (dto.alumno.getGrupos().getGrado()==6) {
                    dto.estSexto=true;
                    dto.evaluadorr = Integer.parseInt(dto.evaluador);
                    if (dto.alumno != null) {
                        dto.resultadoREST = ejb.getResultado(dto.evaluacion, dto.evaluadorr, dto.respuestas);
                        if (dto.resultadoREST != null) {
                            dto.apartados = ejb.getApartados();
                            dto.finalizado = ejb.actualizarResultado(dto.resultadoREST);
                            dto.cargada = true;
                        }
                    }
                }else{
                    dto.estSexto=false;
                }

            }
        } catch (Exception e) {
            dto.cargada = false;
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EncuestaSatisfaccionEgresadosTsu.init() e: " + e.getMessage());
        }
    }
    public void guardarRespuesta(ValueChangeEvent cve) {
        UIComponent origen = (UIComponent) cve.getSource();
        if(cve.getNewValue().toString() != null){
            dto.valor = cve.getNewValue().toString();
        }else{
            dto.valor = cve.getOldValue().toString();
        }
        ejb.actualizarRespuestaPorPregunta(dto.resultadoREST, origen.getId(), dto.valor, dto.respuestas);
        dto.finalizado = ejb.actualizarResultado(dto.resultadoREST);
    }
    
    
}
