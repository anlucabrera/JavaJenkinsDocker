package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class Organigrama implements Serializable {

    private static final long serialVersionUID = 2288964212463101066L;

    @Getter    @Setter    private ListaPersonal nuevoEmpleado;
    @Getter    @Setter    private Personal nuevoEmpleadoFunciones;
    @Getter    @Setter    private List<Funciones> listaDFunciones = new ArrayList<>();
    @Getter    @Setter    private Integer sub = 0, sec = 0, ptc = 0, pda = 0, lab = 0;

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbDatosUsuarioLogeado;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo areasLogeo;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbFunciones ejbFunciones;

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try {
            sub = 0;            sec = 0;            ptc = 0;            pda = 0;            lab = 0;
            nuevoEmpleadoFunciones = new Personal();
            nuevoEmpleado = new ListaPersonal();
            listaDFunciones.clear();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(Organigrama.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void nodeSelectListener() {
        try {
            sub = 0;            sec = 0;            ptc = 0;            pda = 0;            lab = 0;

            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            int clave = Integer.valueOf(params.get("clave"));

            if (clave <= 0) {
                Messages.addGlobalWarn("No hay Datos");
                Ajax.oncomplete("PF('datos').hide();");
            } else {
                nuevoEmpleado = new ListaPersonal();
                nuevoEmpleado = ejbPersonal.mostrarListaPersonal(clave);
                nuevoEmpleadoFunciones = ejbPersonal.mostrarPersonalLogeado(nuevoEmpleado.getClave());
                AreasUniversidad areasUniversidad = new AreasUniversidad();
                areasUniversidad = areasLogeo.mostrarAreasUniversidad(nuevoEmpleado.getAreaOperativa());
                if (areasUniversidad.getCategoria().getDescripcion().equals("Área Académica")) {
                    pda = 0;                    ptc = 0;                    sec = 0;                    lab = 0;
                    pda = ejbDatosUsuarioLogeado.mostrarListaPersonalCategoriasAreas(Short.parseShort("30"), nuevoEmpleado.getAreaOperativa());
                    ptc = ejbDatosUsuarioLogeado.mostrarListaPersonalCategoriasAreas(Short.parseShort("32"), nuevoEmpleado.getAreaOperativa());
                    sec = ejbDatosUsuarioLogeado.mostrarListaPersonalCategoriasAreas(Short.parseShort("34"), nuevoEmpleado.getAreaOperativa());
                    lab = ejbDatosUsuarioLogeado.mostrarListaPersonalCategoriasAreas(Short.parseShort("41"), nuevoEmpleado.getAreaOperativa());
                    sub = lab + pda + ptc + sec;
                }
                buscaFunciones();
                Ajax.oncomplete("PF('datos').show();");
                Ajax.oncomplete("PF('datos').show();");
            }

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(Organigrama.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscaFunciones() {
        try {
            listaDFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoEmpleadoFunciones.getAreaOperativa(), nuevoEmpleadoFunciones.getCategoriaOperativa().getCategoria(), nuevoEmpleadoFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
            if (nuevoEmpleado.getCategoriaOperativa() == 111) {
                listaDFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoEmpleadoFunciones.getAreaOperativa(), Short.parseShort("33"), nuevoEmpleadoFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
            }
            if ((nuevoEmpleado.getAreaSuperior() >= 23 && nuevoEmpleado.getAreaSuperior() <= 50) || (nuevoEmpleado.getAreaOperativa() >= 23 && nuevoEmpleado.getAreaOperativa() <= 50)) {
                listaDFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoEmpleadoFunciones.getCategoriaOperativa().getCategoria(), nuevoEmpleadoFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
            }
            if (listaDFunciones.isEmpty()) {
                Messages.addGlobalWarn("Sin información de funciones");
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(Organigrama.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
