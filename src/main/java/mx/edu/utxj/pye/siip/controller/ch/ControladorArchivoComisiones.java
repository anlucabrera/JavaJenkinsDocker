/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ch;

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
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorArchivoComisiones implements Serializable{

    private static final long serialVersionUID = 6783998947836984809L;
    //    Variables de Lectura
    @Getter private Short ejercicio;
    @Getter private String eje;
    @Getter private Short area;
    @Getter private final String[] ejes = ServicioArchivos.EJES;
    
    //    Variables de Lectura y Escritura
    @Getter @Setter private String rutaArchivo;
    @Getter @Setter private Part file; 
    
    @Inject
    ControladorEmpleado controladorEmpleado;
    
    @Inject
    ControladorComisionesAcademicas controladorComisionesAcademicas;
    
    @Inject
    ControladorComAcadParticipantes controladorComAcadParticipantes;
    
    @EJB
    EjbCarga ejbCarga;
    
    @PostConstruct
    public void init(){
        eje = ejes[4];
        ejercicio = 2018;
        area = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
    }

    //ActionListener
    public void subirExcelComAcad() {
        if (file != null) {
            rutaArchivo = ejbCarga.subirExcelRegistro(String.valueOf(ejercicio),String.valueOf(area),eje,"comisiones_academicas",file);
            if (!"Error: No se pudo leer el archivo".equals(rutaArchivo)) {
                controladorComisionesAcademicas.listaComisionesAcademicasPrevia(rutaArchivo);
                controladorComAcadParticipantes.listaComAcadParticipantesPrevia(rutaArchivo);
                rutaArchivo = null;
            } else {
                rutaArchivo = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intentelo nuevamente !!");
            }
        } else {
             Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
        }
    }
    
}
