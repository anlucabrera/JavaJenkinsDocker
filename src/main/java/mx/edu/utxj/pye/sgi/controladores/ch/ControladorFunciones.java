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
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.Categoriasespecificasfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Comentariosfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ControladorFunciones implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private String menajeNot, tipo, nombreO,puestoTipo="",nombreCategoriaEspecifica="";
    @Getter    @Setter    private List<String> listaO = new ArrayList<>();
    @Getter    @Setter    private List<Notificaciones> listaNotificaciones = new ArrayList<>();
    @Getter    @Setter    private Notificaciones nuevOBJNotificaciones;
    @Getter    @Setter    private PersonalCategorias nuevoOBJPersonalCategorias;
    @Getter    @Setter    private Actividades nuevoOBJActividades;
    @Getter    @Setter    private Date fechaActual = new Date();
/////////////////////////////////////////////////////////////////////////////////////////////////
    @Getter    @Setter    private List<ListaPersonal> listaListaPersonal = new ArrayList<>(), listaListaPersonalSubordinados = new ArrayList<>();
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonalLogeado = new ListaPersonal(), nuevoOBJListaPersonalSub = new ListaPersonal();
    @Getter    @Setter    private List<listaCat> listaCategorias = new ArrayList<>();
    @Getter    @Setter    private listaCat nuevOBJlistaCat, nuevOBJlistaCat2;
    @Getter    @Setter    private List<listaAre> listalistaAreas = new ArrayList<>();
    @Getter    @Setter    private listaAre nuevOBJlistaAre, nuevOBJlistaAre2;
    @Getter    @Setter    private Short claveCategoria = 0, claveCategoria2 = 1,claveArea=0;
    @Getter    @Setter    private List<Funciones> listaFunciones = new ArrayList<>();
    @Getter    @Setter    private Funciones nuevoOBJFunciones = new Funciones(), nuevoOBJFuncionesSelect;
    @Getter    @Setter    private Integer usuario, contiene = 0,totalC=0,claveEmpleado=0;
    @Getter    @Setter    private Iterator<Personal> empleadoActual;

    @Getter    @Setter    private List<String> listaClavesEmpleados = new ArrayList<>();
    @Getter    @Setter    private List<Categoriasespecificasfunciones> listaCategoriasespecificasfuncioneses = new ArrayList<>();
    @Getter    @Setter    private Categoriasespecificasfunciones Categoriasespecificasfuncionesnullo=new Categoriasespecificasfunciones();
///////////////////////////////////////////////////////////////////////////////
    @Getter    @Setter    private Comentariosfunciones nuevoOBJComentariosfunciones=new Comentariosfunciones(),nuevoOBJComentariosfuncionesComparacion=new Comentariosfunciones();
    @Getter    @Setter    private List<Comentariosfunciones> ListaComentariosfuncionesConsulta=new ArrayList<>();
    @Getter    @Setter    private List<listaEstatus> listalistaEstatus = new ArrayList<>();
    @Getter    @Setter    private List<Personal> listaPersonalJefes= new ArrayList<>();
    @Getter    @Setter    private Personal nuveOBJPersonalActualizacion,nuveOBJPersonal,nuveOBJPersonalParaFunciones;
    @Getter    @Setter    private Categoriasespecificasfunciones nuveOBJCategoriasespecificasfunciones=new Categoriasespecificasfunciones(), nuveOBJCategoriasespecificasfuncionesValorAg=new Categoriasespecificasfunciones();
    @Getter    @Setter    private List<Categoriasespecificasfunciones> listaCategoriasespecificasConsulta=new ArrayList<>();
    @Getter    @Setter    private List<listaCat> listaCategoriasEspecificas = new ArrayList<>();
    @Getter    @Setter    private listaCat nuevOBJlistaCategoriasEspecificas, nuevOBJlistaCategoriasEspecificas2;
//@EJB   
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbFunciones ejbFunciones;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorFunciones Inicio: " + System.currentTimeMillis());
        claveArea = 0;
        listaClavesEmpleados.clear();
        usuario = controladorEmpleado.getEmpleadoLogeado();
        listalistaEstatus.clear();
        listalistaEstatus.add(new listaEstatus(0, "Pendiente"));
        listalistaEstatus.add(new listaEstatus(1, "Resuelta"));
        claveCategoria2 = 1;
        usuarioLogeado();
        System.out.println(" ControladorFunciones Fin: " + System.currentTimeMillis());
    }

    public void mostrarListaCategorias() {
        try {

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public void usuarioLogeado() {
        try {
            Categoriasespecificasfuncionesnullo = new Categoriasespecificasfunciones();
            nuveOBJPersonalParaFunciones = new Personal();
            listaCategoriasespecificasfuncioneses.clear();
            listaFunciones.clear();
            listaListaPersonal.clear();
            listaListaPersonalSubordinados.clear();
            listaCategorias.clear();
            listaPersonalJefes.clear();

            nuevoOBJListaPersonalLogeado = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeado(usuario);
            nuveOBJPersonalParaFunciones = ejbDatosUsuarioLogeado.mostrarPersonalLogeado(usuario);

            listaListaPersonalSubordinados = ejbDatosUsuarioLogeado.mostrarListaSubordinados(nuevoOBJListaPersonalLogeado);
            listaPersonalJefes = ejbSelectec.mostrarListaDePersonalParaJefes(nuevoOBJListaPersonalLogeado.getAreaOperativa());

            for (int i = 0; i <= listaListaPersonalSubordinados.size() - 1; i++) {
                nuevoOBJListaPersonalSub = listaListaPersonalSubordinados.get(i);
                nuveOBJPersonal = listaPersonalJefes.get(i);
                if (!Objects.equals(nuveOBJPersonal.getClave(), usuario) || nuveOBJPersonal.getClave() == 563) {
                    nuevOBJlistaAre = new listaAre(nuevoOBJListaPersonalSub.getAreaOperativa(), nuevoOBJListaPersonalSub.getAreaOperativaNombre());
                    nuevOBJlistaCat = new listaCat(nuevoOBJListaPersonalSub.getCategoriaOperativa(), nuevoOBJListaPersonalSub.getCategoriaOperativaNombre());
                    if (nuveOBJPersonal.getCategoriaEspecifica() != null) {
                        if (nuveOBJPersonal.getCategoriaEspecifica().getCategoriaEspecifica() != 1) {
                            nuevOBJlistaCategoriasEspecificas = new listaCat(nuveOBJPersonal.getCategoriaEspecifica().getCategoriaEspecifica(), nuveOBJPersonal.getCategoriaEspecifica().getNombreCategoria());

                            for (int j = 0; j <= listaCategoriasEspecificas.size() - 1; j++) {
                                nuevOBJlistaCategoriasEspecificas2 = listaCategoriasEspecificas.get(j);
                                if (nuevOBJlistaCategoriasEspecificas2.getNombre().equals(nuevOBJlistaCategoriasEspecificas.getNombre())) {
                                    contiene = 1;
                                }
                            }
                            if (contiene == 0) {
                                listaCategoriasEspecificas.add(nuevOBJlistaCategoriasEspecificas);
                            }
                            contiene = 0;
                        }
                    }
                    for (int j = 0; j <= listaCategorias.size() - 1; j++) {
                        nuevOBJlistaCat2 = listaCategorias.get(j);
                        if (nuevOBJlistaCat2.getNombre().equals(nuevOBJlistaCat.getNombre())) {
                            contiene = 1;
                        }
                    }
                    if (contiene == 0) {
                        listaCategorias.add(nuevOBJlistaCat);
                    }
                    contiene = 0;

                    if (nuevoOBJListaPersonalSub.getAreaOperativa() != nuevoOBJListaPersonalLogeado.getAreaOperativa()) {
                        for (int j = 0; j <= listalistaAreas.size() - 1; j++) {
                            nuevOBJlistaAre2 = listalistaAreas.get(j);
                            if (nuevOBJlistaAre2.getNombre().equals(nuevOBJlistaAre.getNombre())) {
                                contiene = 1;
                            }
                        }
                        if (contiene == 0) {
                            listalistaAreas.add(nuevOBJlistaAre);
                        }
                    }
                    contiene = 0;
                }
            }
            empleadoActual = listaPersonalJefes.iterator();
            while (empleadoActual.hasNext()) {
                Personal next = empleadoActual.next();
                if (Objects.equals(next.getClave(), usuario)) {
                    empleadoActual.remove();
                }
            }
            listalistaAreas.add(new listaAre(Short.parseShort("0"), "Sin áreas subordinadas"));
            listaCategoriasEspecificas.add(new listaCat(Short.parseShort("1"), "Sin categoría especifica"));
            listaFunciones = ejbFunciones.mostrarListaDeFuncionesXAreaOperativo(nuevoOBJListaPersonalLogeado.getAreaOperativa(), nuevoOBJListaPersonalLogeado.getCategoriaOperativa());
            listaCategoriasespecificasfuncioneses = ejbFunciones.mostrarCategoriasespecificasfuncionesArea(nuevoOBJListaPersonalLogeado.getAreaOperativa());
            Categoriasespecificasfuncionesnullo = new Categoriasespecificasfunciones(Short.parseShort("1"), "Sin categoría especifica", Short.parseShort("61"));
            listaCategoriasespecificasfuncioneses.add(Categoriasespecificasfuncionesnullo);
            mostrarComentariosfunciones();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarFuncnPorAyC() {
        try {
            listaFunciones.clear();
            
            switch (claveCategoria) {
                case 0:
                    if (nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 24 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 56) {
                        listaFunciones = ejbFunciones.mostrarListaDeFuncionesXAreaOperativo(Short.parseShort("61"), nuevoOBJListaPersonalLogeado.getCategoriaOperativa());
                    } else {
                        listaFunciones = ejbFunciones.mostrarListaDeFuncionesXAreaOperativo(nuevoOBJListaPersonalLogeado.getAreaOperativa(), nuevoOBJListaPersonalLogeado.getCategoriaOperativa());
                    }
                    break;
                case 30:
                    if ((nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 24 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 56) || nuevoOBJListaPersonalLogeado.getAreaOperativa() == 28 || nuevoOBJListaPersonalLogeado.getAreaOperativa() == 47) {
                        listaFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    } else {
                        listaFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoOBJListaPersonalLogeado.getAreaOperativa(), claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    }
                    break;
                case 41:
                    if ((nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 24 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 56) || nuevoOBJListaPersonalLogeado.getAreaOperativa() == 28 || nuevoOBJListaPersonalLogeado.getAreaOperativa() == 47) {
                        listaFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    } else {
                        listaFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoOBJListaPersonalLogeado.getAreaOperativa(), claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    }
                    break;
                case 32:
                    if ((nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 24 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 56) || nuevoOBJListaPersonalLogeado.getAreaOperativa() == 28 || nuevoOBJListaPersonalLogeado.getAreaOperativa() == 47) {
                        listaFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    } else {
                        listaFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoOBJListaPersonalLogeado.getAreaOperativa(), claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    }
                    break;
                default:
                    if (nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 24 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 56) {
                        listaFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    } else {
                        listaFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoOBJListaPersonalLogeado.getAreaOperativa(), claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    }
                    break;
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
            case 1:
                puestoTipo = "Directivo";
                break;
            case 13:
                puestoTipo = "Directivo";
                break;
            case 14:
                puestoTipo = "Directivo";
                break;
            case 18:
                puestoTipo = "Directivo";
                break;
            case 24:
                puestoTipo = "Directivo";
                break;
            case 30:
                puestoTipo = "Docente";
                break;
            case 31:
                puestoTipo = "Directivo";
                break;
            case 32:
                puestoTipo = "Docente";
                break;
            case 33:
                puestoTipo = "Directivo";
                break;
            case 38:
                puestoTipo = "Directivo";
                break;
            case 39:
                puestoTipo = "Directivo";
                break;
            case 40:
                puestoTipo = "Directivo";
                break;
            case 41:
                puestoTipo = "Docente";
                break;
            case 111:
                puestoTipo = "Directivo";
                break;
            case 112:
                puestoTipo = "Directivo";
                break;
            default:
                puestoTipo = "Administrativo";
                break;
        }
    }

    public void consultarFuncnPorAC() {
        try {
            listaFunciones.clear();
            if (claveCategoria == 0) {
                if (nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 24 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 56) {
                    listaFunciones = ejbFunciones.mostrarListaDeFuncionesXAreaOperativo(Short.parseShort("61"), nuevoOBJListaPersonalLogeado.getCategoriaOperativa());
                } else {
                    listaFunciones = ejbFunciones.mostrarListaDeFuncionesXAreaOperativo(nuevoOBJListaPersonalLogeado.getAreaOperativa(), nuevoOBJListaPersonalLogeado.getCategoriaOperativa());
                }
            } else {
                if ((nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 24 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 56) || (claveArea >= 24 && claveArea <= 56)) {
                    listaFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), claveCategoria, claveCategoria2);
                } else {
                    listaFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(claveArea, claveCategoria, claveCategoria2);
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultarFuncnesAC() {
        try {
            listaFunciones.clear();
            listaFunciones = ejbFunciones.mostrarListaDeFuncionesXAreaYPuestoEspecifico(nuevoOBJListaPersonalLogeado.getAreaOperativa(), claveCategoria2);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearComentariosfunciones() {
        try {
            nuevoOBJComentariosfunciones.setIdPersonal(new Personal());
            nuevoOBJComentariosfunciones.setIdFuncion(new Funciones());
            nuevoOBJComentariosfunciones.getIdPersonal().setClave(usuario);
            nuevoOBJComentariosfunciones.getIdFuncion().setFuncion(nuevoOBJFuncionesSelect.getFuncion());
            nuevoOBJComentariosfunciones.setEsatus(0);
            nuevoOBJComentariosfunciones.setFechaHoraC(fechaActual);

            nuevoOBJComentariosfunciones = ejbFunciones.agregarComentariosfunciones(nuevoOBJComentariosfunciones);

            nuevoOBJComentariosfunciones = new Comentariosfunciones();
            nuevoOBJFuncionesSelect = new Funciones();
            Messages.addGlobalInfo("¡¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarComentariosfunciones() {
        try {
            totalC = 0;
            ListaComentariosfuncionesConsulta.clear();
            ListaComentariosfuncionesConsulta = ejbFunciones.mostrarComentariosfunciones();
            for (int i = 0; i <= ListaComentariosfuncionesConsulta.size() - 1; i++) {
                nuevoOBJComentariosfuncionesComparacion = new Comentariosfunciones();
                nuevoOBJComentariosfuncionesComparacion = ListaComentariosfuncionesConsulta.get(i);
                if (nuevoOBJComentariosfuncionesComparacion.getEsatus() == 0) {
                    totalC = totalC + 1;
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarYactualizarCategoriasEspecificas() {
        try {
            listaCategoriasespecificasConsulta.clear();
            nuveOBJCategoriasespecificasfunciones.setArea(nuevoOBJListaPersonalLogeado.getAreaOperativa());
            nuveOBJCategoriasespecificasfunciones.setNombreCategoria(nombreCategoriaEspecifica);
            nuveOBJCategoriasespecificasfunciones = ejbFunciones.agregarCategoriasespecificasfunciones(nuveOBJCategoriasespecificasfunciones);

            listaCategoriasespecificasConsulta = ejbFunciones.mostrarCategoriasespecificasfunciones(nombreCategoriaEspecifica, nuevoOBJListaPersonalLogeado.getAreaOperativa());
            nuveOBJCategoriasespecificasfuncionesValorAg = listaCategoriasespecificasConsulta.get(0);
            listaClavesEmpleados.stream().forEach(l -> {
                try {
                    Integer clave = Integer.parseInt(l);
                    nuveOBJPersonalActualizacion = ejbDatosUsuarioLogeado.mostrarPersonalLogeado(clave);
                    nuveOBJPersonalActualizacion.setCategoriaEspecifica(nuveOBJCategoriasespecificasfuncionesValorAg);
                    nuveOBJPersonalActualizacion = ejbDatosUsuarioLogeado.actualizarPersonal(nuveOBJPersonalActualizacion);
                } catch (Throwable ex) {
                    Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            usuarioLogeado();
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
                if (nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 24 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 56) {
                    nuevoOBJFunciones.setAreaOperativa(Short.parseShort("61"));
                } else {
                    if (nuevoOBJListaPersonalLogeado.getAreaOperativa() != 28) {
                        nuevoOBJFunciones.setAreaOperativa(nuevoOBJListaPersonalLogeado.getAreaOperativa());
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

    public void eliminarFuncion() {
        try {
            nuevoOBJFuncionesSelect = ejbFunciones.eliminaFunciones(nuevoOBJFuncionesSelect);
            consultarFuncnPorAyC();
            Messages.addGlobalInfo("¡¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditPersonal(RowEditEvent event) {
        try {
            ejbDatosUsuarioLogeado.actualizarPersonal((Personal) event.getObject());
            usuarioLogeado();
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
            mostrarComentariosfunciones();
            Messages.addGlobalInfo("¡¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("¡¡Operación cancelada!!");
    }

    public static class listaCat {

        @Getter        @Setter        private Short clave;
        @Getter        @Setter        private String nombre;

        private listaCat(Short _clave, String _nombre) {
            clave = _clave;
            nombre = _nombre;
        }
    }

    public static class listaAre {

        @Getter        @Setter        private Short clave;
        @Getter        @Setter        private String nombre;

        private listaAre(Short _clave, String _nombre) {
            clave = _clave;
            nombre = _nombre;
        }
    }

    public static class listaEstatus {

        @Getter        @Setter        private Integer clave;
        @Getter        @Setter        private String nombre;

        private listaEstatus(Integer _clave, String _nombre) {
            clave = _clave;
            nombre = _nombre;
        }
    }
}
