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
public class ControladorArchivoOrganismosVinculados implements Serializable{

    private static final long serialVersionUID = -3547317217188825578L;
    
//    Variables de Lectura
    @Getter private Short ejercicio;
    @Getter private String eje;
    @Getter private Short area;
    @Getter private final String[] ejes = ServicioArchivos.EJES;
    
//    Variables de Lectura y Escritura
    @Getter @Setter private String rutaArchivo;
    @Getter @Setter private Part file; 
    
//    @Getter private StreamedContent lpcr;
//    
//    @Getter private ListaOrganismosVinculados listaOrganismosVinculados;

//    Recursos inyectados  
    @Inject
    ControladorEmpleado controladorEmpleado;
    @Inject
    ControladorOrganismosVinculados controladorOrganismosVinculados;

//    Interfaces conectadas
    @EJB
    EjbCarga ejbCarga;
//    @EJB
//    EjbOrganismosVinculados ejbOrganismosVinculados;
    
    @PostConstruct
    public void init(){
        eje = ejes[2];
        ejercicio = 2018;
        area = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
    }
    
    public void subirExcelOrganismosVinculados() throws IOException {
        if (file != null) {
            rutaArchivo = ejbCarga.subirExcelRegistro(String.valueOf(ejercicio),String.valueOf(area),eje,"organismos_vinculados",file);
            if (!"Error: No se pudo leer el archivo".equals(rutaArchivo)) {
                controladorOrganismosVinculados.listaOrganismosVinculadosPrevia(rutaArchivo);
                rutaArchivo = null;
                file.delete();
            } else {
                rutaArchivo = null;
                file.delete();
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intentelo nuevamente !!");
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
        }
    }
    
//    public StreamedContent getListaActualizadaPlantilla() throws ParsePropertyException, InvalidFormatException, FileNotFoundException, IOException{
//        try {
//            listaOrganismosVinculados = ejbOrganismosVinculados.getListaActualizadaPlantilla();
//            ejbOrganismosVinculados.actualizarPlantillaConvenio(ejbOrganismosVinculados.getListaActualizadaPlantilla());
//            InputStream stream;
//            File archivo = new File("C:\\archivos\\modulos_registro\\plantillas\\vinculacion\\convenios.xlsx");
//            stream = FileUtils.openInputStream(archivo); //FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/demo/images/optimus.jpg");
//            lpcr = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "convenios.xlsx");
//        } catch (Throwable ex) {
//            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
//            Logger.getLogger(ControladorOrganismosVinculados.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return lpcr;
//    }
    
}
