/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import java.io.IOException;
import java.io.Serializable;
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
import mx.edu.utxj.pye.sgi.enums.RegistroSiipEtapa;
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

    private static final long serialVersionUID = 2050407053726343860L;
    
    //    Variables de Lectura
    @Getter private RegistroSiipEtapa etapa;
    
    //    Variables de Lectura y Escritura
    @Getter @Setter private String rutaArchivo;
    @Getter @Setter private String plan;
    @Getter @Setter private String programa;
    @Getter @Setter private Part file; 
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject RegistroEvidInstEvalMateriasDireccion registroEvidInstEvalMateriasDireccion;
    @Inject Caster caster;
    
    @EJB EjbCarga ejbCarga;
    @EJB EjbModulos ejbModulos;

    @PostConstruct
    public void init(){
        setEtapa(RegistroSiipEtapa.MOSTRAR);
        plan = String.valueOf(registroEvidInstEvalMateriasDireccion.getRol().getPlanEstudio().getAnio());
        programa = registroEvidInstEvalMateriasDireccion.getRol().getProgramaEducativo().getSiglas();
    }
    
     public void recibirArchivo(ValueChangeEvent e){
        file = (Part)e.getNewValue();
    }

    public void setEtapa(RegistroSiipEtapa etapa) {
        this.etapa = etapa;
    }

    //ActionListener
    public void subirExcelEvidInstMateria() throws IOException {
        if (file != null) {
            rutaArchivo = ejbCarga.subirPlantillaAlineacionMaterias(plan, programa, file);
            if (!"Error: No se pudo leer el archivo".equals(rutaArchivo)) {
                setEtapa(RegistroSiipEtapa.CARGAR);
                registroEvidInstEvalMateriasDireccion.listaPreviaEvidenciasInstrumentos(rutaArchivo);
                rutaArchivo = null;
                file.delete();
            } else {
                rutaArchivo = null;
                file.delete();
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intentelo nuevamente");
            }
        } else {
            System.err.println("subirExcelEvidInstMateria - file es null ");
             Messages.addGlobalWarn("Es necesario seleccionar un archivo");
        }
    }
    
}
