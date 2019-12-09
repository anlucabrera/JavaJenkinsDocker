/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.controller;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.titulacion.dto.dtoEstadisticaEgresados;
import mx.edu.utxj.pye.titulacion.interfaces.EjbEstadisticaEgresados;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
/**
 *
 * @author UTXJ
 */
@Named(value = "controladorEstadisticaEgresados")
@ManagedBean
@SessionScoped
public class ControladorEstadisticaEgresados implements Serializable{

    private static final long serialVersionUID = -8650911824702760712L;

    @EJB private EjbEstadisticaEgresados ejbEstadisticaEgresados;
    
    // Para consulta por Generación y Nivel Educativo 
    @Getter @Setter private Generaciones generacion;
    @Getter @Setter private String nivel;
    @Getter @Setter private List<Generaciones> generaciones;
    @Getter @Setter private List<String> niveles;
    @Getter @Setter private List<dtoEstadisticaEgresados> listaDatos;
    
    // Para totales
    @Getter @Setter private Integer totalInscritos, totalEgresados, totalAcredEstadia, totalExpInt, totalExpVal;
   
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        try {
        generacionesExpedientesRegistrados();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstadisticaEgresados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    public void descargarReporteGeneracionNivel() throws IOException, Throwable{
        File f = new File(ejbEstadisticaEgresados.getReporteGeneracionNivel(generacion, nivel));
        Faces.sendFile(f, true);
    }  
    
    public void descargarListadoGeneracionNivel() throws IOException, Throwable{
        File f = new File(ejbEstadisticaEgresados.getListadoGeneracionNivel(generacion));
        Faces.sendFile(f, true);
    }  

     /**
     * Permite obtener la lista de generaciones en lo que hay expedientes registrados
     */
    public void generacionesExpedientesRegistrados(){
        List<Generaciones> res = ejbEstadisticaEgresados.getGeneracionesConRegistro();
            if (res.size() != 0) {
                setGeneraciones(res);
                setGeneracion(getGeneraciones().get(0));
                nivelesExpedientesRegistrados();
            }
    }
   
     /**
     * Permite obtener la lista de niveles educativos que tienen expedientes registrados
     */
    public void nivelesExpedientesRegistrados(){
        List<String> res = ejbEstadisticaEgresados.getNivelesConRegistro(getGeneracion());
            if (res.size() != 0) {
                setNiveles(res);
                setNivel(getNiveles().get(0));
                listaExpedientesNivel();
            }
    }
    
    /**
     * Permite obtener la lista de expedientes registrados en la generación y nivel educativo seleccionado previamente
     */
    public void listaExpedientesNivel(){
        List<dtoEstadisticaEgresados> res = ejbEstadisticaEgresados.obtenerReporteIntegracionExp(getGeneracion(), getNivel());
            setListaDatos(res);
            Ajax.update("dtbReporte");
            setTotalInscritos(listaDatos.stream().mapToInt(dtoEstadisticaEgresados::getInscritos).sum());
            setTotalEgresados(listaDatos.stream().mapToInt(dtoEstadisticaEgresados::getEgresados).sum());
            setTotalAcredEstadia(listaDatos.stream().mapToInt(dtoEstadisticaEgresados::getAcredEstadia).sum());
            setTotalExpInt(listaDatos.stream().mapToInt(dtoEstadisticaEgresados::getIntExp).sum());
            setTotalExpVal(listaDatos.stream().mapToInt(dtoEstadisticaEgresados::getExpVal).sum());
    
    }
   
    /**
     * Permite que al cambiar o seleccionar una generación se pueda actualizar la lista de niveles educativos por generación
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones generacion = (Generaciones)e.getNewValue();
            setGeneracion(generacion);
            nivelesExpedientesRegistrados();
            Ajax.update("formMuestraDatosActivos");
        }
    }
    
    /**
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la lista de expedientes por programa educativo
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  String){
            String nivel = (String)e.getNewValue();
            setNivel(nivel);
            listaExpedientesNivel();
            Ajax.update("formMuestraDatosActivos");
        }
    }
}
