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
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
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
public class ControladorArchivoCalificacionesCuatrimestre implements Serializable{
    
    private static final long serialVersionUID = -3376146491828216238L;
//    Variables de solo lectura
    @Getter private EventosRegistros eventoRegistro;
    @Getter private String eje;
    @Getter private AreasUniversidad area;
    @Getter private final String[] ejes = ServicioArchivos.EJES;
    @Getter private RegistroSiipEtapa etapa;
    
//    Variables de Lectura y escritura
    @Getter @Setter private String rutaArchivo;
    @Getter @Setter private Part file; 
    
//    Inyección de dependencias
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro  controladorModulosRegistro;
//    TODO: Realizar el controlador de CalificacionesCuatrimestre
    
//    EJB - Servicios
    @EJB    EjbCarga    ejbCarga;
    @EJB    EjbModulos  ejbModulos;
    
    @PostConstruct
    public void init(){
        eje = ejes[1];
        eventoRegistro = ejbModulos.getEventoRegistro();
        consultaAreaRegistro(); 
        setEtapa(RegistroSiipEtapa.MOSTRAR);
    }

    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 11);
            if (areaRegistro == null) {
                area = (ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
            } else {
                area = areaRegistro;
            }
        } catch (Exception ex) {
            System.out.println("ControladorArchivoCalificacionesCuatrimestre.consultaAreaRegistro: " + ex.getMessage());
        }
    }
    
    public void recibirArchivo(ValueChangeEvent e){
        file = (Part)e.getNewValue();
    }
    
    public void setEtapa(RegistroSiipEtapa etapa){
        this.etapa = etapa;
    }
    
    public void subirExcelServiciosTecnologicos() throws IOException{
        if (file != null) {
            rutaArchivo = ejbCarga.subirExcelRegistroMensual(String.valueOf(eventoRegistro.getEjercicioFiscal().getAnio()),String.valueOf(area.getSiglas()),eje,eventoRegistro.getMes(),"servicios_tecnologicos",file);
            if (!"Error: No se pudo leer el archivo".equals(rutaArchivo)) {
                setEtapa(RegistroSiipEtapa.CARGAR);
//                TODO: hacer controlador
                rutaArchivo = null;
                file.delete();
            } else {
                rutaArchivo = null;
                file.delete();
                Messages.addGlobalWarn("¡No fue posible cargar el archivo, Inténtelo nuevamente!");
            }
        } else {
             Messages.addGlobalWarn("¡Es necesario seleccionar un archivo!");
        }
    }
    
//    TODO: Creación de EJB (Interfaces y servicios), Controlador general y Archivo .xhtml, e incluir mecanismo de edición, eliminación, evidencias y alineación a poa.
    
}
