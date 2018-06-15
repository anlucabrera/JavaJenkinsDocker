package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorPersonalconfiguracion implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<areas> listareasOperativas = new ArrayList<areas>(),listareasOficiales = new ArrayList<areas>(),listareasSuperiores = new ArrayList<areas>();
    @Getter    @Setter    private List<ListaPersonal> nuevaVistaListaPersonalAreas = new ArrayList<>();
    @Getter    @Setter    private List<PersonalCategorias> nuevaListaPersonalCategorias = new ArrayList<>();
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonalFiltroAreas;
     @Getter    @Setter    private PersonalCategorias nuevoOBJPersonalCategorias;
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;
    
    @PostConstruct
    public void init() {
        generarListasAreas();
//        System.out.println("estatus.size() " + estatus.size());
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void generarListasAreas() {
        try {
            listareasOperativas.clear();
            listareasOficiales.clear();
            listareasSuperiores.clear();
            nuevaVistaListaPersonalAreas.clear();
            nuevaListaPersonalCategorias.clear();

            for (int i = 1; i <= 3; i++) {
                nuevaVistaListaPersonalAreas.clear();
                switch (i) {
                    case 1:
                        nuevaVistaListaPersonalAreas = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeadoAreaOfi();
                        break;
                    case 2:
                        nuevaVistaListaPersonalAreas = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeadoAreaOpe();
                        break;
                    case 3:
                        nuevaVistaListaPersonalAreas = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeadoAreaSup();
                        break;
                }
                for (int j = 0; j <= nuevaVistaListaPersonalAreas.size() - 1; j++) {
                    nuevoOBJListaPersonalFiltroAreas = nuevaVistaListaPersonalAreas.get(j);
                    switch (i) {
                        case 1:
                            listareasOficiales.add(new areas(nuevoOBJListaPersonalFiltroAreas.getAreaOficial(), nuevoOBJListaPersonalFiltroAreas.getAreaOficialNombre()));
                            break;
                        case 2:
                            listareasOperativas.add(new areas(nuevoOBJListaPersonalFiltroAreas.getAreaOperativa(), nuevoOBJListaPersonalFiltroAreas.getAreaOperativaNombre()));
                            break;
                        case 3:
                            listareasSuperiores.add(new areas(nuevoOBJListaPersonalFiltroAreas.getAreaSuperior(), nuevoOBJListaPersonalFiltroAreas.getAreaSuperiorNombre()));
                            break;
                    }
                }
                nuevaVistaListaPersonalAreas.clear();
            }
            nuevaVistaListaPersonalAreas = ejbSelectec.mostrarListaDeEmpleados();
            Collections.sort(nuevaVistaListaPersonalAreas, (x, y) -> x.getAreaOperativaNombre().compareToIgnoreCase(y.getAreaOperativaNombre()));
            nuevaListaPersonalCategorias = ejbDatosUsuarioLogeado.mostrarListaPersonalCategorias();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonalconfiguracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearNuevasCategorias() {
        try {
            nuevoOBJPersonalCategorias.setTipo("Genérica");
            nuevoOBJPersonalCategorias = ejbDatosUsuarioLogeado.crearNuevoPersonalCategorias(nuevoOBJPersonalCategorias);
            Messages.addGlobalInfo("¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonalconfiguracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class areas {

        @Getter
        @Setter
        private int clave;
        @Getter
        @Setter
        private String nombre;

        private areas(int _clave, String _nombre) {
            clave = _clave;
            nombre = _nombre;
        }
    }
}
