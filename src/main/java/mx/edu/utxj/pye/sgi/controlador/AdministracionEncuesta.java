/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.ListadoEncuestaServicios;
import mx.edu.utxj.pye.sgi.dto.ListadoEvaluacionEgresados;
import mx.edu.utxj.pye.sgi.dto.ListadoGraficaEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuesta;
import mx.edu.utxj.pye.sgi.entity.ch.DatosGraficaEncuestaEgresados;
import mx.edu.utxj.pye.sgi.entity.ch.DatosGraficaEncuestaServicio;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaServicios;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import org.omnifaces.util.Messages;

/**
 *
 * @author Planeación
 */
@Named
@SessionScoped
public class AdministracionEncuesta implements Serializable{
    
    private static final long serialVersionUID = 9051830636523223017L;

    @Getter @Setter private DtoEvaluaciones dto = new DtoEvaluaciones();
    @Inject private LogonMB logonMB;
    @EJB private EjbAdministracionEncuesta ejbAdmEncuesta;
    
    
    @PostConstruct
    public void init() {
        try {
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)){
                dto.usuarioNomina=Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina());
                dto.cveTrabajador= logonMB.getListaUsuarioClaveNomina().getCvePersona();
                dto.cveDirector = dto.cveTrabajador.toString();
                    if (!ejbAdmEncuesta.esDirectorDeCarrera(2, 2, 18, 48, Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina())).isEmpty()) {
                        dto.director = true;
                        aperturarEncuestas();
                    }
                    if (logonMB.getPersonal().getAreaOperativa() == 9 || dto.usuarioNomina.equals(579)) {
                        dto.esDeIyE = true;
                        aperturarEncuestas();
                    }
                    if(!ejbAdmEncuesta.estTutordeGrupo(dto.cveTrabajador).isEmpty()){
                        dto.tutor = true;
                        aperturarEncuestas();
                    }
                    if (!ejbAdmEncuesta.esSecretarioAcademico(1, Short.parseShort("2"), Short.parseShort("38"), Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina())).isEmpty()) {
                        dto.esSecretario = true;
                        aperturarEncuestas();
                    }

                    if(!ejbAdmEncuesta.esPlaneacion(1, Short.parseShort("2"), Short.parseShort("18"), Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina())).isEmpty()){
                        dto.planeacion = true;
                        aperturarEncuestas();
                    }
            }
        } catch (Throwable ex) {
            dto.director = false; dto.esDeIyE = false; dto.tutor = false; dto.esSecretario = false; dto.planeacion = false; dto.ESTsuActiva = false; dto.ESActiva = false; dto.ESIngActiva = true;
            dto.ESEActiva = false; dto.EEActiva = true;
            Logger.getLogger(AdministracionEncuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void aperturarEncuestas(){
        if(ejbAdmEncuesta.aperturaVisualizacionEncuesta("Satisfacción de egresados de TSU")){dto.ESTsuActiva = true;}
        if(ejbAdmEncuesta.aperturaVisualizacionEncuesta("Servicios")){dto.ESActiva = true;}
        if(ejbAdmEncuesta.aperturaVisualizacionEncuesta("Satisfacción de egresados de ingenieria")){dto.ESIngActiva = true;}
        if(ejbAdmEncuesta.aperturaVisualizacionEncuesta("Estudio socioeconómico")){dto.ESEActiva = true;}
        if(ejbAdmEncuesta.aperturaVisualizacionEncuesta("Evaluación Estadía")){dto.EEActiva = true;}
    }
    
}
