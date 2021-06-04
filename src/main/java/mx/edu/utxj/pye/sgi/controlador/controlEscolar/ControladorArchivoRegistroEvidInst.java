/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.RegistroSiipEtapa;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.ca.ControladorActFormacionIntegral;
import mx.edu.utxj.pye.siip.controller.ca.ControladorPartActFormInt;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorArchivoRegistroEvidInst implements Serializable{
    
    //    Variables de Lectura
    @Getter private RegistroSiipEtapa etapa;
    
    //    Variables de Lectura y Escritura
    @Getter @Setter private String rutaArchivo;
    @Getter @Setter private Part file; 
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject RegistroEvidInstEvalMateriasDireccion registroEvidInstEvalMateriasDireccion;
    @Inject Caster caster;
    
    @EJB EjbCarga ejbCarga;
    @EJB EjbModulos ejbModulos;
//    
//    @PostConstruct
//    public void init(){
//        try{
//            
//        }catch (Throwable ex) {
//            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
//            Logger.getLogger(ControladorArchivoRegistroEvidInst.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
     public void recibirArchivo(ValueChangeEvent e){
        file = (Part)e.getNewValue();
    }

    public void setEtapa(RegistroSiipEtapa etapa) {
        this.etapa = etapa;
    }

    //ActionListener
    public void subirExcelEvidInstMateria() throws IOException {
         
        if (file != null) {
            rutaArchivo = ejbCarga.subirPlantillaAlineacionMaterias(String.valueOf(registroEvidInstEvalMateriasDireccion.rol.getPlanEstudioRegistrado().getAnio()), registroEvidInstEvalMateriasDireccion.rol.getProgramaEducativo().getSiglas(),file);
            if (!"Error: No se pudo leer el archivo".equals(rutaArchivo)) {
                setEtapa(RegistroSiipEtapa.CARGAR);
//                registroEvidInstEvalMateriasDireccion.rol.listaEvidInsMateriaPrevia(rutaArchivo);
                rutaArchivo = null;
                file.delete();
            } else {
                rutaArchivo = null;
                file.delete();
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intentelo nuevamente");
            }
        } else {
             Messages.addGlobalWarn("Es necesario seleccionar un archivo");
        }
    }
    
}
