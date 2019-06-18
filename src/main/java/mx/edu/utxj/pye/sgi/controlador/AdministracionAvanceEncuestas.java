/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.ListaAlumnosEncuestaServicios;
import mx.edu.utxj.pye.sgi.dto.ListaDatosAvanceEncuestaServicio;
import mx.edu.utxj.pye.sgi.dto.ListadoGraficaEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.*;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionEstudioSocioEconomico;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.ViewEstudianteAsesorAcademico;
import org.omnifaces.util.Messages;

/**
 *
 * @author Planeación
 */
@Named
@SessionScoped
public class AdministracionAvanceEncuestas implements Serializable{
    
    private static final long serialVersionUID = -7745875703360648941L;
    @Getter @Setter private DtoEvaluaciones dto = new DtoEvaluaciones();
    @EJB private EjbAdministracionEncuestaServicios ejbAES;
    @EJB private EjbAdministracionEncuestaTsu ejbET;
    @EJB private EjbAdministracionEvaluacionEstadia ejbEE;
    @EJB private EjbAdministracionEncuestaIng ejbAEI;
    @EJB private EjbAdministracionEstudioSocioeconomico ejbAESE;


    @PostConstruct
    public void init(){



    }
    
    public void mostrarAvanceEncServicios() {
        try {
            dto.dtoLDAES = ejbAES.obtenerListaDatosAvanceEncuestaServicio();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarAvanceEncSatTsu() {
        try {
            dto.dtoLDAES1 = ejbET.obtenerListaDatosAvanceEncuestaSatisfaccion();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarAvanceEvaluacionEstadia() {
        try {
            dto.dtoLDAES2 = ejbEE.obtenerListaDatosAvanceEvaluacionEstadia();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarAvanceEncSatIng() {
        try {


        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarAvanceEvaluacionEstudioSocioEconomico() {
        try {

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarAvanceEncuestaServiciosPorGrupoTutor(){
        dto.dtoAESPG = new ArrayList<>();
        dto.dtoAESPG = ejbAES.avanceEncuestaServiciosPorGrupo();
    }

    public void mostrarAvanceEncuestaSatEgresadosPorGrupoTutor(){
        dto.dtoAESPG1 = new ArrayList<>();
        dto.dtoAESPG1 = ejbET.avanceEncuestaServiciosPorGrupoTutor();
    }

}
