/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.vin;

import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
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
public class ControladorArchivoFerias implements Serializable{

    private static final long serialVersionUID = 0L;
    //    Variables de Lectura
    @Getter private Short ejercicio;
    @Getter private String eje;
    @Getter private AreasUniversidad area;
    @Getter private final String[] ejes = ServicioArchivos.EJES;
    @Getter private RegistroSiipEtapa etapa;
    
    //    Variables de Lectura y Escritura
    @Getter @Setter private String rutaArchivo;
    @Getter @Setter private Part file; 
    
    @Inject
    ControladorEmpleado controladorEmpleado;
    
    @Inject
    ControladorFeriasProfesiograficas controladorFeriasProfesiograficas;
    
    @Inject
    ControladorFeriasParticipantes controladorFeriasParticipantes;
    
    @Inject ControladorModulosRegistro  controladorModulosRegistro;
    
    @EJB
    EjbCarga ejbCarga;
    @EJB EjbModulos ejbModulos;
    
    @PostConstruct
    public void init(){
        eje = ejes[2];
        ejercicio = ejbModulos.getEventoRegistro().getEjercicioFiscal().getAnio();
        consultaAreaRegistro(); 
        setEtapa(RegistroSiipEtapa.MOSTRAR);
    }
    
    public void consultaAreaRegistro() {
        AreasUniversidad areaRegistro = new AreasUniversidad();
        areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 27);
        if (areaRegistro == null) {
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 64);
            if (areaRegistro == null) {
                area = (ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
            } else {
                area = areaRegistro;
            }
        } else {
            area = areaRegistro;
        }
    }

    public void setEtapa(RegistroSiipEtapa etapa) {
        this.etapa = etapa;
    }
    
    //ActionListener
    public void subirExcelFerias() {
       if (file != null) {
            rutaArchivo = ejbCarga.subirExcelRegistroMensual(String.valueOf(ejercicio),String.valueOf(area.getSiglas()),eje,ejbModulos.getEventoRegistro().getMes(),"ferias_profesiográficas",file);
            if (!"Error: No se pudo leer el archivo".equals(rutaArchivo)) {
                setEtapa(RegistroSiipEtapa.CARGAR);
                controladorFeriasProfesiograficas.listaFeriasProfesiograficasPrevia(rutaArchivo);
                controladorFeriasParticipantes.listaFeriasprofParticipantesPrevia(rutaArchivo);
                rutaArchivo = null;
            } else {
                rutaArchivo = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Inténtelo nuevamente");
            }
        } else {
             Messages.addGlobalWarn("Es necesario seleccionar un archivo");
        }
    }
    
}
