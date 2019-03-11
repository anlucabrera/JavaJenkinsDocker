package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Categoriasespecificasfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Comentariosfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ControladorFunciones implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;
////////////////////////////////////////////////////////////////////////////////Listas entytis
    @Getter    @Setter    private List<PersonalCategorias> personalCategoriases = new ArrayList<>();   
    @Getter    @Setter    private List<AreasUniversidad> areasUniversidads = new ArrayList<>();   
    @Getter    @Setter    private List<Categoriasespecificasfunciones> categoriasespecificasfuncioneses = new ArrayList<>();  
    @Getter    @Setter    private List<Personal> subordinadosPersonals = new ArrayList<>();
    @Getter    @Setter    private List<ListaPersonal> subordinadosListaPersonals = new ArrayList<>();
    @Getter    @Setter    private List<Personal> categoriasOyEporArea = new ArrayList<>();
    @Getter    @Setter    private List<Funciones> funcioneses = new ArrayList<>();
    @Getter    @Setter    private List<Comentariosfunciones> comentariosfuncioneses = new ArrayList<>();
////////////////////////////////////////////////////////////////////////////////Objetos entytis
    @Getter    @Setter    private ListaPersonal listaPersonalLogeado = new ListaPersonal();
    @Getter    @Setter    private Personal personalLogeado = new Personal();    
    @Getter    @Setter    private Personal perosnalCategoriasFunciones = new Personal();

////////////////////////////////////////////////////////////////////////////////Listas 
    @Getter    @Setter    private List<String> listaClavesEmpleados = new ArrayList<>();
    @Getter    @Setter    private List<Categoriasespecificasfunciones> listaCategoriasespecificasConsulta = new ArrayList<>();
////////////////////////////////////////////////////////////////////////////////Objetos 
    @Getter    @Setter    private Comentariosfunciones nuevoOBJComentariosfunciones = new Comentariosfunciones();
    @Getter    @Setter    private Categoriasespecificasfunciones nuveOBJCategoriasespecificasfunciones = new Categoriasespecificasfunciones(), nuveOBJCategoriasespecificasfuncionesValorAg = new Categoriasespecificasfunciones();
    @Getter    @Setter    private Funciones nuevoOBJFunciones = new Funciones(), nuevoOBJFuncionesSelect;
    @Getter    @Setter    private Personal nuveOBJPersonalActualizacion, nuveOBJPersonal;
////////////////////////////////////////////////////////////////////////////////Variables 
    @Getter    @Setter    private String puestoTipo = "", nombreCategoriaEspecifica = "";
    @Getter    @Setter    private Short claveCategoria = 0, claveCategoria2 = 1, claveArea = 0;
    @Getter    @Setter    private Integer totalC = 0, claveEmpleado = 0;
///////////////////////////////////////////////////////////////////////////////
    @Getter    @Setter    private Iterator<Personal> empleadoActual;
    
//@EJB   
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbUtilidadesCH;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbFunciones ejbFunciones;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        claveArea = 0;
        listaClavesEmpleados.clear();
        listaPersonalLogeado = controladorEmpleado.getNuevoOBJListaPersonal();
        claveCategoria2 = 1;
        mostrarListaCatelogos();
    }
    
    public void mostrarListaCatelogos() {
        try {
            personalLogeado = ejbPersonal.mostrarPersonalLogeado(listaPersonalLogeado.getClave());
            personalCategoriases = ejbUtilidadesCH.mostrarListaPersonalCategoriasArea(listaPersonalLogeado.getAreaOperativa());
            areasUniversidads = ejbAreasLogeo.mostrarAreasUniversidadSubordinadas(listaPersonalLogeado.getAreaOperativa());
            categoriasespecificasfuncioneses = ejbFunciones.mostrarCategoriasespecificasfuncionesArea(listaPersonalLogeado.getAreaOperativa());
            subordinadosListaPersonals = ejbPersonal.mostrarListaPersonalListSubordinados(listaPersonalLogeado);
            subordinadosPersonals = ejbPersonal.mostrarListaPersonalSubordinados(listaPersonalLogeado.getAreaOperativa(), listaPersonalLogeado.getClave());            
            funcioneses = ejbFunciones.mostrarListaFuncionesParaARAE(personalLogeado);
            comentariosfuncioneses = ejbFunciones.mostrarComentariosfunciones();
            categoriasOyEporArea=ejbFunciones.mostrarCategoriasOyEporArea(personalLogeado);
            if (!comentariosfuncioneses.isEmpty()) {
                comentariosfuncioneses.forEach((t) -> {
                    if (t.getEsatus() == 0) {
                        totalC = totalC + 1;
                    }
                });
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agregarFuncion() {
        try {
            System.out.println("Inicio del metodo de registro de fuciones");
            nuevoOBJFunciones.setCategoriaOperativa(new PersonalCategorias());
            nuevoOBJFunciones.setCategoriaEspesifica(new Categoriasespecificasfunciones());
            if ((perosnalCategoriasFunciones.getAreaOperativa() >= 23 && perosnalCategoriasFunciones.getAreaOperativa() <= 29) || (perosnalCategoriasFunciones.getAreaSuperior()>= 23 && perosnalCategoriasFunciones.getAreaSuperior() <= 29)) {
                nuevoOBJFunciones.setAreaOperativa(Short.parseShort("61"));
            } else {
                nuevoOBJFunciones.setAreaOperativa(perosnalCategoriasFunciones.getAreaOperativa());
            }
            nuevoOBJFunciones.setCategoriaOperativa(perosnalCategoriasFunciones.getCategoriaOperativa());
            nuevoOBJFunciones.setCategoriaEspesifica(perosnalCategoriasFunciones.getCategoriaEspecifica());
            nuevoOBJFunciones = ejbFunciones.agregarFuncion(nuevoOBJFunciones);
            nuevoOBJFunciones = new Funciones();
            consultarFunciones(perosnalCategoriasFunciones);
            System.out.println("Fin del metodo de registro de fuciones");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarFuncion(RowEditEvent event) {
        try {
            Funciones f = (Funciones) event.getObject();
            ejbFunciones.actualizarFunciones(f);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarFuncion(Funciones f) {
        try {
            ejbFunciones.eliminaFunciones(f);
            consultarFuncnPorAyC();
            Messages.addGlobalInfo("¡¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Boolean comentDisable(Short cat) {
        Boolean tipo = true;
        if (listaPersonalLogeado.getAreaOperativa() >= 23 && listaPersonalLogeado.getAreaOperativa() <= 29) {
            if (cat == 30 || cat == 32 || cat == 41) {
                tipo = false;
            }
        }
        return tipo;
    }
     
    public Boolean editVisible(Short cat) {
        Boolean tipo = true;
        if (cat == 30 || cat == 32 || cat == 41) {
            if(listaPersonalLogeado.getAreaOperativa() >= 23 && listaPersonalLogeado.getAreaOperativa() <= 29) {
                tipo = false;
            } else if (listaPersonalLogeado.getAreaOperativa() == 2) {
                tipo = true;
            }
        }

        if (cat == 1 || cat == 18 || cat == 33 || cat == 38) {
            if(listaPersonalLogeado.getAreaOperativa() == 1) {
                tipo = true;
            } else {
                tipo = false;
            }
        }

        return tipo;
    }
    
    public Boolean delateDisable(Short cat) {
        if(cat==null){
            return false;
        }
        Boolean tipo = false;
        if (cat == 30 || cat == 32 || cat == 41) {
            if(listaPersonalLogeado.getAreaOperativa() >= 23 && listaPersonalLogeado.getAreaOperativa() <= 29) {
                tipo = true;
            } else if (listaPersonalLogeado.getAreaOperativa() == 2) {
                tipo = false;
            }
        }

        if (cat == 1 || cat == 18 || cat == 33 || cat == 38) {
            if(listaPersonalLogeado.getAreaOperativa() == 1) {
                tipo = false;
            } else {
                tipo = true;
            }
        }


        return tipo;
    }

    public void asignarPerosnalFunciones(ValueChangeEvent event) {
        try {
            perosnalCategoriasFunciones = new Personal();
            if (Integer.parseInt(event.getNewValue().toString()) == 0) {
                funcioneses = ejbFunciones.mostrarListaFuncionesParaARAE(personalLogeado);
            } else {
                perosnalCategoriasFunciones = ejbPersonal.mostrarPersonalLogeado(Integer.parseInt(event.getNewValue().toString()));
                consultarFunciones(perosnalCategoriasFunciones);
            }
       } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarFunciones(Personal persona) {
        try {
            System.out.println("Fin del metodo de registro de fuciones"+persona);
            funcioneses = new ArrayList<>();
            funcioneses.clear();

                if (persona.getAreaSuperior() >= 23 && persona.getAreaSuperior() <= 29) {
                    funcioneses = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), persona.getCategoriaOperativa().getCategoria(), persona.getCategoriaEspecifica().getCategoriaEspecifica());
                } else {
                    funcioneses = ejbFunciones.mostrarListaFuncionesPersonalLogeado(persona.getAreaOperativa(), persona.getCategoriaOperativa().getCategoria(), persona.getCategoriaEspecifica().getCategoriaEspecifica());
                }

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void metodoBase() {

    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void mostrarListaCategorias() {
        try {

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarFuncnPorAyC() {
        try {
            funcioneses.clear();
            if (listaPersonalLogeado.getCategoriaOperativa() == 30
                    || listaPersonalLogeado.getCategoriaOperativa() == 32
                    || listaPersonalLogeado.getCategoriaOperativa() == 41
                    || (listaPersonalLogeado.getCategoriaOperativa() == 34 && (listaPersonalLogeado.getAreaSuperior() >= 23 && listaPersonalLogeado.getAreaSuperior() <= 29))) {
                funcioneses = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), listaPersonalLogeado.getCategoriaOperativa(), personalLogeado.getCategoriaEspecifica().getCategoriaEspecifica());
            } else {
                funcioneses = ejbFunciones.mostrarListaFuncionesPersonalLogeado(listaPersonalLogeado.getAreaOperativa(), listaPersonalLogeado.getCategoriaOperativa(), personalLogeado.getCategoriaEspecifica().getCategoriaEspecifica());
            }
            claveArea = 0;
            claveCategoria2 = 1;
            filtradoPuestosadministrativos();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void filtradoPuestosadministrativos() {
        switch (claveCategoria) {
            case 1:                puestoTipo = "Directivo";                break;
            case 13:                puestoTipo = "Directivo";                break;
            case 14:                puestoTipo = "Directivo";                break;
            case 18:                puestoTipo = "Directivo";                break;
            case 24:                puestoTipo = "Directivo";                break;
            case 30:                puestoTipo = "Docente";                break;
            case 31:                puestoTipo = "Directivo";                break;
            case 32:                puestoTipo = "Docente";                break;
            case 33:                puestoTipo = "Directivo";                break;
            case 38:                puestoTipo = "Directivo";                break;
            case 39:                puestoTipo = "Directivo";                break;
            case 40:                puestoTipo = "Directivo";                break;
            case 41:                puestoTipo = "Docente";                break;
            case 111:                puestoTipo = "Directivo";                break;
            case 112:                puestoTipo = "Directivo";                break;
            default:                puestoTipo = "Administrativo";                break;
        }
    }

    public void consultarFuncnPorAC() {
        try {
            funcioneses.clear();
            if (claveCategoria == 0) {
                if (listaPersonalLogeado.getAreaOperativa() >= 24 && listaPersonalLogeado.getAreaOperativa() <= 56) {
                    funcioneses = ejbFunciones.mostrarFuncionesPorAreayPuesto(Short.parseShort("61"), listaPersonalLogeado.getCategoriaOperativa(), 1);
                } else {
                    funcioneses = ejbFunciones.mostrarFuncionesPorAreayPuesto(listaPersonalLogeado.getAreaOperativa(), listaPersonalLogeado.getCategoriaOperativa(), 1);
                }
            } else {
                if ((listaPersonalLogeado.getAreaOperativa() >= 24 && listaPersonalLogeado.getAreaOperativa() <= 56) || (claveArea >= 24 && claveArea <= 56)) {
                    funcioneses = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), claveCategoria, claveCategoria2);
                } else {
                    funcioneses = ejbFunciones.mostrarListaFuncionesPersonalLogeado(claveArea, claveCategoria, claveCategoria2);
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarFuncnesAC() {
        try {
            funcioneses.clear();
            funcioneses = ejbFunciones.mostrarFuncionesPorAreayPuesto(listaPersonalLogeado.getAreaOperativa(), claveCategoria2, 2);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearComentariosfunciones() {
        try {
            nuevoOBJComentariosfunciones.setIdPersonal(new Personal());
            nuevoOBJComentariosfunciones.setIdFuncion(new Funciones());
            nuevoOBJComentariosfunciones.getIdPersonal().setClave(listaPersonalLogeado.getClave());
            nuevoOBJComentariosfunciones.getIdFuncion().setFuncion(nuevoOBJFuncionesSelect.getFuncion());
            nuevoOBJComentariosfunciones.setEsatus(0);
            nuevoOBJComentariosfunciones.setFechaHoraC(new Date());

            nuevoOBJComentariosfunciones = ejbFunciones.agregarComentariosfunciones(nuevoOBJComentariosfunciones);

            nuevoOBJComentariosfunciones = new Comentariosfunciones();
            nuevoOBJFuncionesSelect = new Funciones();
            Messages.addGlobalInfo("¡¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarYactualizarCategoriasEspecificas() {
        try {
            listaCategoriasespecificasConsulta.clear();
            nuveOBJCategoriasespecificasfunciones.setArea(listaPersonalLogeado.getAreaOperativa());
            nuveOBJCategoriasespecificasfunciones.setNombreCategoria(nombreCategoriaEspecifica);
            nuveOBJCategoriasespecificasfunciones = ejbFunciones.agregarCategoriasespecificasfunciones(nuveOBJCategoriasespecificasfunciones);

            listaCategoriasespecificasConsulta = ejbFunciones.mostrarCategoriasespecificasfunciones(nombreCategoriaEspecifica, listaPersonalLogeado.getAreaOperativa());
            nuveOBJCategoriasespecificasfuncionesValorAg = listaCategoriasespecificasConsulta.get(0);
            listaClavesEmpleados.stream().forEach(l -> {
                try {
                    Integer clave = Integer.parseInt(l);
                    nuveOBJPersonalActualizacion = ejbPersonal.mostrarPersonalLogeado(clave);
                    nuveOBJPersonalActualizacion.setCategoriaEspecifica(nuveOBJCategoriasespecificasfuncionesValorAg);
                    nuveOBJPersonalActualizacion = ejbPersonal.actualizarPersonal(nuveOBJPersonalActualizacion);
                } catch (Throwable ex) {
                    Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            mostrarListaCatelogos();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearFuncion() {
        try {
            if (claveArea != 0) {
                if (claveArea >= 24 && claveArea <= 56) {
                    nuevoOBJFunciones.setAreaOperativa(Short.parseShort("61"));
                } else {
                    nuevoOBJFunciones.setAreaOperativa(claveArea);
                }
            } else {
                if (listaPersonalLogeado.getAreaOperativa() >= 24 && listaPersonalLogeado.getAreaOperativa() <= 56) {
                    nuevoOBJFunciones.setAreaOperativa(Short.parseShort("61"));
                } else {
                    if (listaPersonalLogeado.getAreaOperativa() != 28) {
                        nuevoOBJFunciones.setAreaOperativa(listaPersonalLogeado.getAreaOperativa());
                    } else {
                        switch (claveCategoria) {
                            case 30:
                                nuevoOBJFunciones.setAreaOperativa(Short.parseShort("61"));
                                break;
                            case 32:
                                nuevoOBJFunciones.setAreaOperativa(Short.parseShort("61"));
                                break;
                            case 41:
                                nuevoOBJFunciones.setAreaOperativa(Short.parseShort("61"));
                                break;
                        }
                    }
                }
            }

            nuevoOBJFunciones.setCategoriaEspesifica(new Categoriasespecificasfunciones());
            nuevoOBJFunciones.setCategoriaOperativa(new PersonalCategorias());
            nuevoOBJFunciones.getCategoriaOperativa().setCategoria(claveCategoria);
            nuevoOBJFunciones.getCategoriaEspesifica().setCategoriaEspecifica(claveCategoria2);
            nuevoOBJFunciones = ejbFunciones.agregarFuncion(nuevoOBJFunciones);
            nuevoOBJFunciones = new Funciones();
            Messages.addGlobalInfo("¡¡Operación exitosa!!");

            if (claveCategoria2 != 1) {
                consultarFuncnesAC();
            } else {
                if (claveArea != 0) {
                    consultarFuncnPorAC();
                } else {
                    consultarFuncnPorAyC();
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void onRowEditPersonal(RowEditEvent event) {
        try {
            ejbPersonal.actualizarPersonal((Personal) event.getObject());
            mostrarListaCatelogos();
            Messages.addGlobalInfo("¡¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            ejbFunciones.actualizarFunciones((Funciones) event.getObject());
            Messages.addGlobalInfo("¡¡Operación exitosa!!");
            if (claveCategoria2 != 1) {
                consultarFuncnesAC();
            } else {
                if (claveArea != 0) {
                    consultarFuncnPorAC();
                } else {
                    consultarFuncnPorAyC();
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditComent(RowEditEvent event) {
        try {
            ejbFunciones.actualizarComentariosfunciones((Comentariosfunciones) event.getObject());
            mostrarListaCatelogos();
            Messages.addGlobalInfo("¡¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("¡¡Operación cancelada!!");
    }

    public static class listaCategorias {

        @Getter        @Setter        private Integer areaOperatova;
        @Getter        @Setter        private Short categoriaOperativa;
        @Getter        @Setter        private Short categoriaEspecifica;

        private listaCategorias(Integer _areaOperatova, Short _categoriaOperativa, Short _categoriaEspecifica) {
            areaOperatova = _areaOperatova;
            categoriaOperativa = _categoriaOperativa;
            categoriaEspecifica = _categoriaEspecifica;
        }
    }
}
