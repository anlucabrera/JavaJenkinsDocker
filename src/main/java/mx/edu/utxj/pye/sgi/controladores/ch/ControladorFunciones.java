package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
    @Getter    @Setter    private Short claveCategoria = 0, claveCategoria2 = 1;
    @Getter    @Setter    private List<Funciones> listaFunciones = new ArrayList<>();
    @Getter    @Setter    private Funciones nuevoOBJFunciones = new Funciones(), nuevoOBJFuncionesSelect;
    @Getter    @Setter    private Integer usuario, contiene = 0,claveArea=0,totalC=0,claveEmpleado=0;

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
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbCreate ejbCreate;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUpdate ejbUpdate;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbDelate ejbDelate;
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

            listaListaPersonal = ejbSelectec.mostrarListaDeEmpleadosXClave(usuario);
            nuevoOBJListaPersonalLogeado = listaListaPersonal.get(0);
            nuveOBJPersonalParaFunciones = ejbSelectec.mostrarEmpleadosPorClave(usuario);

            listaListaPersonalSubordinados = ejbSelectec.mostrarListaDeEmpleadosParaJefes(nuevoOBJListaPersonalLogeado.getAreaOperativa());
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
            listalistaAreas.add(new listaAre(0, "Sin áreas subordinadas"));
            listaCategoriasEspecificas.add(new listaCat(Short.parseShort("1"), "Sin categoría especifica"));
            listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaOperativo(nuevoOBJListaPersonalLogeado.getAreaOperativa(), nuevoOBJListaPersonalLogeado.getCategoriaOperativa());
            listaCategoriasespecificasfuncioneses = ejbSelectec.mostrarCategoriasespecificasfuncionesArea(nuevoOBJListaPersonalLogeado.getAreaOperativa());
            Categoriasespecificasfuncionesnullo = new Categoriasespecificasfunciones(Short.parseShort("1"), "Sin categoría especifica", 83);
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
                    if (nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 1 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 26) {
                        listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaOperativo(83, nuevoOBJListaPersonalLogeado.getCategoriaOperativa());
                    } else {
                        listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaOperativo(nuevoOBJListaPersonalLogeado.getAreaOperativa(), nuevoOBJListaPersonalLogeado.getCategoriaOperativa());
                    }
                    break;
                case 30:
                    if ((nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 1 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 26) || nuevoOBJListaPersonalLogeado.getAreaOperativa() == 28 || nuevoOBJListaPersonalLogeado.getAreaOperativa() == 47) {
                        listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(83, claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    } else {
                        listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(nuevoOBJListaPersonalLogeado.getAreaOperativa(), claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    }
                    break;
                case 41:
                    if ((nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 1 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 26) || nuevoOBJListaPersonalLogeado.getAreaOperativa() == 28 || nuevoOBJListaPersonalLogeado.getAreaOperativa() == 47) {
                        listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(83, claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    } else {
                        listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(nuevoOBJListaPersonalLogeado.getAreaOperativa(), claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    }
                    break;
                case 32:
                    if ((nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 1 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 26) || nuevoOBJListaPersonalLogeado.getAreaOperativa() == 28 || nuevoOBJListaPersonalLogeado.getAreaOperativa() == 47) {
                        listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(83, claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    } else {
                        listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(nuevoOBJListaPersonalLogeado.getAreaOperativa(), claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    }
                    break;
                default:
                    if (nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 1 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 26) {
                        listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(83, claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    } else {
                        listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(nuevoOBJListaPersonalLogeado.getAreaOperativa(), claveCategoria, nuveOBJPersonalParaFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
                    }
                    break;
            }
            claveArea = 0;
            claveCategoria2 = 1;
//            System.out.println("mx.edu.utxj.pye.sgi.ch.controladores.ControladorFunciones.consultarFuncnPorAyC() claveArea "+claveArea);
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
//            System.out.println("mx.edu.utxj.pye.sgi.ch.controladores.ControladorFunciones.consultarFuncnPorAC() claveArea " + claveArea);
//            System.out.println("mx.edu.utxj.pye.sgi.ch.controladores.ControladorFunciones.consultarFuncnPorAC() claveCategoria " + claveCategoria);
//            System.out.println("mx.edu.utxj.pye.sgi.ch.controladores.ControladorFunciones.consultarFuncnPorAC() claveCategoria2 " + claveCategoria2);
            listaFunciones.clear();
            if (claveCategoria == 0) {
                if (nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 1 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 26) {
                    listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaOperativo(83, nuevoOBJListaPersonalLogeado.getCategoriaOperativa());
                } else {
                    listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaOperativo(nuevoOBJListaPersonalLogeado.getAreaOperativa(), nuevoOBJListaPersonalLogeado.getCategoriaOperativa());
                }
            } else {
                if ((nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 1 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 26) || (claveArea >= 1 && claveArea <= 26)) {
                    listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(83, claveCategoria, claveCategoria2);
                } else {
                    listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(claveArea, claveCategoria, claveCategoria2);
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
            listaFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoEspecifico(nuevoOBJListaPersonalLogeado.getAreaOperativa(), claveCategoria2);
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

            nuevoOBJComentariosfunciones = ejbCreate.agregarComentariosfunciones(nuevoOBJComentariosfunciones);

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
            ListaComentariosfuncionesConsulta = ejbSelectec.mostrarComentariosfunciones();
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
            nuveOBJCategoriasespecificasfunciones = ejbCreate.agregarCategoriasespecificasfunciones(nuveOBJCategoriasespecificasfunciones);

            listaCategoriasespecificasConsulta = ejbSelectec.mostrarCategoriasespecificasfunciones(nombreCategoriaEspecifica, nuevoOBJListaPersonalLogeado.getAreaOperativa());
            nuveOBJCategoriasespecificasfuncionesValorAg = listaCategoriasespecificasConsulta.get(0);
            listaClavesEmpleados.stream().forEach(l -> {
                try {
                    Integer clave = Integer.parseInt(l);
                    nuveOBJPersonalActualizacion = ejbSelectec.mostrarEmpleadosPorClave(clave);
                    nuveOBJPersonalActualizacion.setCategoriaEspecifica(nuveOBJCategoriasespecificasfuncionesValorAg);
                    nuveOBJPersonalActualizacion = ejbUpdate.actualizarPersonal(nuveOBJPersonalActualizacion);
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
//            System.out.println("claveArea "+claveArea);
//            System.out.println("claveCategoria "+claveCategoria);
//            System.out.println("claveCategoria2 "+claveCategoria2);
            if (claveArea != 0) {
                if (claveArea >= 1 && claveArea <= 26) {
                    nuevoOBJFunciones.setAreaOperativa(83);
                } else {
                    nuevoOBJFunciones.setAreaOperativa(claveArea);
                }
            } else {
                if (nuevoOBJListaPersonalLogeado.getAreaOperativa() >= 1 && nuevoOBJListaPersonalLogeado.getAreaOperativa() <= 26) {
                    nuevoOBJFunciones.setAreaOperativa(83);
                } else {
                    if (nuevoOBJListaPersonalLogeado.getAreaOperativa() != 28) {
                        nuevoOBJFunciones.setAreaOperativa(nuevoOBJListaPersonalLogeado.getAreaOperativa());
                    } else {
                        switch (claveCategoria) {
                            case 30:
                                nuevoOBJFunciones.setAreaOperativa(83);
                                break;
                            case 32:
                                nuevoOBJFunciones.setAreaOperativa(83);
                                break;
                            case 41:
                                nuevoOBJFunciones.setAreaOperativa(83);
                                break;
                        }
                    }
                }
            }

            nuevoOBJFunciones.setCategoriaEspesifica(new Categoriasespecificasfunciones());
            nuevoOBJFunciones.setCategoriaOperativa(new PersonalCategorias());
            nuevoOBJFunciones.getCategoriaOperativa().setCategoria(claveCategoria);
            nuevoOBJFunciones.getCategoriaEspesifica().setCategoriaEspecifica(claveCategoria2);
            nuevoOBJFunciones = ejbCreate.agregarFuncion(nuevoOBJFunciones);
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
            nuevoOBJFuncionesSelect = ejbDelate.eliminaFunciones(nuevoOBJFuncionesSelect);
            consultarFuncnPorAyC();
            Messages.addGlobalInfo("¡¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditPersonal(RowEditEvent event) {
        try {
            ejbUpdate.actualizarPersonal((Personal) event.getObject());
            usuarioLogeado();
            Messages.addGlobalInfo("¡¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFunciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            ejbUpdate.actualizarFunciones((Funciones) event.getObject());
            Messages.addGlobalInfo("¡¡Operación exitosa!!");
//            System.out.println("claveCategoria2 " + claveCategoria2);
//            System.out.println("claveArea " + claveArea);
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
            ejbUpdate.actualizarComentariosfunciones((Comentariosfunciones) event.getObject());
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

        @Getter        @Setter        private Integer clave;
        @Getter        @Setter        private String nombre;

        private listaAre(Integer _clave, String _nombre) {
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
