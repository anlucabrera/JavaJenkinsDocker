/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.ejb.*;
import org.omnifaces.util.Messages;


import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;


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



@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;



    @PostConstruct
    public void init(){
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;



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
