/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.vin;

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
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
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
public class ControladorArchivoConvenios implements Serializable{

    private static final long serialVersionUID = -3547317217188825578L;
    
//    Variables de Lectura
    @Getter private Short ejercicio;
    @Getter private String eje;
    @Getter private AreasUniversidad area;
    @Getter private final String[] ejes = ServicioArchivos.EJES;
    @Getter private RegistroSiipEtapa etapa;
    
//    Variables de Lectura y Escritura
    @Getter @Setter private String rutaArchivo;
    @Getter @Setter private Part file; 

//    Recursos inyectados  
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorConvenios controladorConvenios;
    @Inject ControladorModulosRegistro  controladorModulosRegistro;

//    Interfaces conectadas
    @EJB    EjbCarga    ejbCarga;
    @EJB    EjbModulos  ejbModulos;
    
    @PostConstruct
    public void init(){
        eje = ejes[2];
        ejercicio = ejbModulos.getEventoRegistro().getEjercicioFiscal().getAnio();
        consultaAreaRegistro();
        if(area == null){
            return;
        }
        setEtapa(RegistroSiipEtapa.MOSTRAR);    
    }
    
    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 24);
            if (areaRegistro == null) {
                areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 32);
                if (areaRegistro == null) {
                    ResultadoEJB<AreasUniversidad> resultadoEJB = ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
                    if(resultadoEJB.getCorrecto()){
                        area = resultadoEJB.getValor();
                    }else{
                        area = null;
                    }
                } else {
                    area = areaRegistro;
                }
            } else {
                area = areaRegistro;
            }
        } catch (Exception ex) {
            area = null;
        }
    }
    
    public void recibirArchivo(ValueChangeEvent e){
        file = (Part)e.getNewValue();
    }
    
    public void setEtapa(RegistroSiipEtapa etapa) {
        this.etapa = etapa;
    }
    
    public void subirExcelConvenios() throws IOException {
        if (file != null) {
            rutaArchivo = ejbCarga.subirExcelRegistroMensual(String.valueOf(ejercicio), String.valueOf(area.getSiglas()), eje,ejbModulos.getEventoRegistro().getMes(), "convenios", file);
            if (!"Error: No se pudo leer el archivo".equals(rutaArchivo)) {
                setEtapa(RegistroSiipEtapa.CARGAR);
                controladorConvenios.listaConveniosPrevia(rutaArchivo);
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
    
}
