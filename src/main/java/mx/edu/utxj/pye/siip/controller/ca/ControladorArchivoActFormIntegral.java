/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ca;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
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
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistrosUsuarios;
import mx.edu.utxj.pye.sgi.enums.RegistroSiipEtapa;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
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
public class ControladorArchivoActFormIntegral implements Serializable{

    private static final long serialVersionUID = -8764342405505396086L;
   //    Variables de Lectura
    @Getter private Short ejercicio;
    @Getter private String eje;
    @Getter private AreasUniversidad area;
    @Getter private final String[] ejes = ServicioArchivos.EJES;
    @Getter private RegistroSiipEtapa etapa;
    
    //    Variables de Lectura y Escritura
    @Getter @Setter private String rutaArchivo;
    @Getter @Setter private Part file; 
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorActFormacionIntegral controladorActFormacionIntegral;
    @Inject ControladorPartActFormInt controladorPartActFormInt;
    @Inject ControladorModulosRegistro  controladorModulosRegistro;
    @Inject Caster caster;
    
    @EJB EjbCarga ejbCarga;
    @EJB EjbModulos ejbModulos;
    
    @PostConstruct
    public void init(){
        eje = ejes[1];
        ejercicio = ejbModulos.getEventoRegistro().getEjercicioFiscal().getAnio();
        consultaAreaRegistro(); 
        setEtapa(RegistroSiipEtapa.MOSTRAR);
    }

    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 7);
            if (areaRegistro == null) {
                area = (ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
            } else {
                area = areaRegistro;
            }
        } catch (Exception ex) {
            System.out.println("ControladorArchivoActFormIntegral.consultaAreaRegistro: " + ex.getMessage());
        }
    }
    
    public void recibirArchivo(ValueChangeEvent e){
        file = (Part)e.getNewValue();
    }

    public void setEtapa(RegistroSiipEtapa etapa) {
        this.etapa = etapa;
    }

    //ActionListener
    public void subirExcelActFormInt() throws IOException {
         
        if (file != null) {
            rutaArchivo = ejbCarga.subirExcelRegistroMensual(String.valueOf(ejercicio),String.valueOf(area.getSiglas()),eje,ejbModulos.getEventoRegistro().getMes(),"actividades_formacion_integral",file);
            if (!"Error: No se pudo leer el archivo".equals(rutaArchivo)) {
                setEtapa(RegistroSiipEtapa.CARGAR);
                controladorActFormacionIntegral.listaActformacionIntegralPrevia(rutaArchivo);
                controladorPartActFormInt.listaParticipantesActformacionintPrevia(rutaArchivo);
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
    
     //ActionListener
    public void subirExcelPartActFormInt() throws IOException {
         
        if (file != null) {
            rutaArchivo = ejbCarga.subirExcelRegistroMensual(String.valueOf(ejercicio),String.valueOf(area.getSiglas()),eje,ejbModulos.getEventoRegistro().getMes(),"participantes_actformint",file);
            if (!"Error: No se pudo leer el archivo".equals(rutaArchivo)) {
                setEtapa(RegistroSiipEtapa.CARGAR);
                controladorPartActFormInt.listaParticipantesActformacionintPrevia(rutaArchivo);
                rutaArchivo = null;
                file.delete();
            } else {
                rutaArchivo = null;
                file.delete();
                Messages.addGlobalWarn("No fue posible cargar el archivo, Inténtelo nuevamente");
            }
        } else {
             Messages.addGlobalWarn("Es necesario seleccionar un archivo");
        }
    }
}
