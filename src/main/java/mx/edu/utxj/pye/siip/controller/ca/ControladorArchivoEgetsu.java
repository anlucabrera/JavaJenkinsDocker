/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ca;

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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
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
public class ControladorArchivoEgetsu implements Serializable{

    private static final long serialVersionUID = 6985164317064814566L;
    
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
    @Inject ControladorEgetsuResultados controladorEgetsuResultados;
    @Inject Caster caster;
    @Inject ControladorModulosRegistro  controladorModulosRegistro;
    
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
        areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 14);
        if (areaRegistro == null) {
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 45);
            if (areaRegistro == null) {
                area = (ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
            } else {
                area = areaRegistro;
            }
        } else {
            area = areaRegistro;
        }
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorArchivoEgetsu.consultaAreaRegistro(): " + e.getMessage());
        }
    }
    
    public void recibirArchivo(ValueChangeEvent e){
        file = (Part)e.getNewValue();
    }

    public void setEtapa(RegistroSiipEtapa etapa) {
        this.etapa = etapa;
    }
    //ActionListener
    public void subirExcelEgetsu() throws IOException {
        
      if (file != null) {
            rutaArchivo = ejbCarga.subirExcelRegistroMensual(String.valueOf(ejercicio),String.valueOf(area.getSiglas()),eje,ejbModulos.getEventoRegistro().getMes(),"egetsu",file);
            if (!"Error: No se pudo leer el archivo".equals(rutaArchivo)) {
                setEtapa(RegistroSiipEtapa.CARGAR);
                 controladorEgetsuResultados.listaEgetsuResultadosPrevia(rutaArchivo);
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
