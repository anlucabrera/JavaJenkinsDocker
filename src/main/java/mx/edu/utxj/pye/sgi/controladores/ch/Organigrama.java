package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
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
    @Getter    @Setter    private List<ListaPersonal> nuevaListaPersonal = new ArrayList<>();
    @Getter    @Setter    private List<String> nuevaListaFuncionesEspecificas = new ArrayList<>();
    @Getter    @Setter    private List<Funciones> listaDFunciones = new ArrayList<>();
    @Getter    @Setter    private List<String> nuevaListaFuncionesGenerales = new ArrayList<>();
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @Getter    @Setter    private Integer  sub = 0, sec = 0, ptc = 0, pda = 0, lab = 0;
    @Getter    @Setter    private Short  area=0,catO=0,catE=0;
    @Getter    @Setter    private String fechaI;
    

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo areasLogeo;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbFunciones ejbFunciones;

    @PostConstruct
    public void init() {
        try {
            sub = 0;            sec = 0;            ptc = 0;            pda = 0;
            lab = 0;            area = 0;            catO = 0;            catE = 0;
            fechaI = "";
            nuevoEmpleadoFunciones = new Personal();
            nuevaListaFuncionesEspecificas.clear();
            nuevaListaFuncionesGenerales.clear();
            nuevoEmpleado = new ListaPersonal();
            nuevaListaPersonal.clear();
            listaDFunciones.clear();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(Organigrama.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void nodeSelectListener() {
        try {
            sub = 0;            sec = 0;            ptc = 0;            pda = 0;
            lab = 0;            area = 0;            catO = 0;            catE = 0;
            fechaI = "";
            AreasUniversidad areasUniversidad = new AreasUniversidad();

            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            int clave = Integer.valueOf(params.get("clave"));

            if (clave <= 0) {
                Messages.addGlobalWarn("No hay Datos");
                Ajax.oncomplete("PF('datos').hide();");
            } else {
                nuevoEmpleado = new ListaPersonal();
                nuevoEmpleado = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeado(clave);
                nuevoEmpleadoFunciones = ejbDatosUsuarioLogeado.mostrarPersonalLogeado(nuevoEmpleado.getClave());
                areasUniversidad = new AreasUniversidad();
                areasUniversidad = areasLogeo.mostrarAreasUniversidad(nuevoEmpleado.getAreaOperativa());
                if (areasUniversidad.getCategoria().getDescripcion().equals("Área Académica")) {
                    pda = 0;
                    ptc = 0;
                    sec = 0;
                    lab = 0;
                    pda = ejbDatosUsuarioLogeado.mostrarListaPersonalCategoriasAreas(Short.parseShort("30"), nuevoEmpleado.getAreaOperativa());
                    ptc = ejbDatosUsuarioLogeado.mostrarListaPersonalCategoriasAreas(Short.parseShort("32"), nuevoEmpleado.getAreaOperativa());
                    sec = ejbDatosUsuarioLogeado.mostrarListaPersonalCategoriasAreas(Short.parseShort("34"), nuevoEmpleado.getAreaOperativa());
                    lab = ejbDatosUsuarioLogeado.mostrarListaPersonalCategoriasAreas(Short.parseShort("41"), nuevoEmpleado.getAreaOperativa());
                    sub=lab+pda+ptc+sec;
                    System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.Organigrama.nodeSelectListener(pda)"+pda);
                    System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.Organigrama.nodeSelectListener(ptc)"+ptc);
                    System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.Organigrama.nodeSelectListener(sec)"+sec);
                    System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.Organigrama.nodeSelectListener(lab)"+lab);
                    System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.Organigrama.nodeSelectListener(sub)"+sub);
                }
                fechaI = dateFormat.format(nuevoEmpleado.getFechaIngreso());
                buscaFunciones();
                Ajax.oncomplete("PF('datos').show();");
                Ajax.oncomplete("PF('datos').show();");
            }

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscaFunciones() {
        try {
            area=0;
            catO=0;
            catE=0;
            nuevaListaFuncionesGenerales.clear();
            nuevaListaFuncionesEspecificas.clear();
            if (nuevoEmpleado.getCategoriaOperativa() == 111) {
                catO=33;
            }else{
                catO=nuevoEmpleado.getCategoriaOperativa();
            }
            if ((nuevoEmpleado.getAreaSuperior() >= 23 && nuevoEmpleado.getAreaSuperior() <= 50) || (nuevoEmpleado.getAreaOperativa() >= 23 && nuevoEmpleado.getAreaOperativa() <= 50)) {
                area = 61;
            } else {
                area = nuevoEmpleado.getAreaOperativa();
            }
            catE=nuevoEmpleadoFunciones.getCategoriaEspecifica().getCategoriaEspecifica();
            listaDFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(area,catO,catE);
            if (listaDFunciones.isEmpty()) {
                Messages.addGlobalWarn("Sin información de funciones");
            } else {
                for (int i = 0; i <= listaDFunciones.size() - 1; i++) {
                    Funciones nuevaFunciones = new Funciones();
                    nuevaFunciones = listaDFunciones.get(i);
                    if ("GENERAL".equals(nuevaFunciones.getTipo())) {
                        nuevaListaFuncionesGenerales.add(nuevaFunciones.getNombre());
                    } else {
                        nuevaListaFuncionesEspecificas.add(nuevaFunciones.getNombre());
                    }
                }
            }
            listaDFunciones.clear();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
