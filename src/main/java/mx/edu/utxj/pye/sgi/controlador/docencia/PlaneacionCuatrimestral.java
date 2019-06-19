package mx.edu.utxj.pye.sgi.controlador.docencia;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.EjbPlaneacionCuatrimestral;
import mx.edu.utxj.pye.sgi.entity.ch.Actividadesvarias;
import mx.edu.utxj.pye.sgi.entity.ch.Atividadesvariasplaneacionescuatrimestrales;
import mx.edu.utxj.pye.sgi.entity.ch.AtividadesvariasplaneacionescuatrimestralesPK;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesCuatrimestrales;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesDetalles;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesDetallesPK;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesLiberaciones;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesLiberacionesPK;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaTotalAlumnosCarreraPye;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author UTXJ
 */
@Named(value = "planeacionCuatrimestral")
@ViewScoped
public class PlaneacionCuatrimestral implements Serializable{    
    private static final long serialVersionUID = -4536433267185063683L;
    @Getter private Eventos evento;
    @Getter private PeriodosEscolares periodo;
    @Getter private Personal secretarioAcademico, jefePersonal, director;
    @Getter @Setter private Personal nuevo;
    @Getter @Setter private PlaneacionesCuatrimestrales editando;
    @Getter @Setter private List<PlaneacionesLiberaciones> listPlaneacionesLiberadas=new ArrayList<>();      
    @Getter private List<PlaneacionesLiberaciones> listPlaneacionesLiberadasSelecMenu =new ArrayList<>();  
    @Getter @Setter private PlaneacionesLiberaciones planeacionesLiberadas;
    @Getter @Setter private PlaneacionesLiberacionesPK planeacionesLiberadasPK,planeacionesLiberadaDPK;
    @Getter @Setter private List<Actividadesvarias> listaActividadesVariad =new ArrayList<>();
    
    @Getter @Setter private Actividadesvarias nuevaActividadesVariad= new Actividadesvarias();
    
    @Getter @Setter private List<Atividadesvariasplaneacionescuatrimestrales> listaActividadesvariasplaneacionescuatrimestraleses =new ArrayList<>();
    @Getter @Setter private Atividadesvariasplaneacionescuatrimestrales nuevaAtividadesvariasplaneacionescuatrimestrales;
    @Getter @Setter private AtividadesvariasplaneacionescuatrimestralesPK atividadesvariasplaneacionescuatrimestralesPK;
    
    @Getter private Boolean activa = false, esSecretarioAcademico=false, esJefePersonal=false, esDirector=false,planeacionesFinalizadas=false,planeacionesliberadasD=false,planeacionesliberadasS=false,planeacionesliberadasP=false;
    @Getter private Integer nivel = 0;
    @Getter private String outcome="index";
    
    @Getter @Setter private Integer actividadVarias=0, horasAsisgnadas=0,claveActividadVaria=0,totalHorasAsignadas=0,horasMaximas=0,totalValidados=0,planeacionesTotalesDirectores=0;
    @Getter @Setter private String nombreActividad="",tipo="",mensajeStattusPlaneacion="",actividadesVarias="Sin especificar",direccionDescarga="";
    @Getter @Setter private Double promedioAvance=0D;
    @Getter private Boolean nuevaActividad=false,superaAsigacion=false,proximosAEstadia=false;
    
    @Getter private List<Personal> docentesActivos, colaboradores;
    @Getter @Setter private List<PlaneacionesCuatrimestrales> planeaciones;
    @Getter @Setter private List<Atividadesvariasplaneacionescuatrimestrales> actividadesVariasCuatrimestrales;
    
    @Getter    @Setter    DecimalFormat df = new DecimalFormat("#.00");
    
    @Getter @Setter private List<PlaneacionesDetalles> listaDetalles;
    @Getter @Setter private PlaneacionesDetalles detalles;
    @Getter @Setter private PlaneacionesDetallesPK detallesPK;
    @Getter @Setter private Integer estudiantesEstadia=0,proyectosEstadia=0,estudiantesAsigandos=0,proyectosAsignados=0;
    
    @Getter @Setter private List<VistaTotalAlumnosCarreraPye> listaTotalAlumnosCarreraPyes;
    
    
    private static final Logger LOGGER = Logger.getLogger("newexcel.ExcelOOXML");
    
    @Inject LogonMB logonMB;
    @EJB EjbPlaneacionCuatrimestral ejb;
    
    @PostConstruct
    public void init() {
        evento = ejb.getEventoActivo();
        activa = evento != null;
        if (!activa) {
            return;
        }
        nivel++;//1 evento existente
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.init(1) nivel: " + nivel);
        
        periodo = ejb.verificarPeriodo(evento);
        if (periodo == null) {
            return;
        }
        nivel++;//2 periodo existente
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.init(2) nivel: " + nivel);
        
        secretarioAcademico = ejb.getSecretarioAcademico();
        jefePersonal = ejb.getJefePersonal();
        director = ejb.getDirector(logonMB.getPersonal().getClave());
        esSecretarioAcademico = secretarioAcademico.equals(logonMB.getPersonal());
        esJefePersonal = jefePersonal.equals(logonMB.getPersonal());
        esDirector = director != null;
        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.init() director:" + director);
        if (!esDirector && !esJefePersonal && !esSecretarioAcademico) {
            activa = false;
            return;
        }
        nivel++;// 3 usuario válido
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.init(3) nivel: " + nivel);
       
        if (esJefePersonal) {;
            outcome = "docencia/planeacionDirPersonal";
            nivel++;// 4 jefePersonal existente
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.init(4) nivel: " + nivel);
            buscarPlaneacionesLiberadasSecretarioAcademico();
            planeaciones = ejb.buscarPlaneacionesCuatrimestrales();
            tipo = "Jefe";
            calculaPorcntajeAvances();

            return;
        }
        if (esSecretarioAcademico) {;
            outcome = "docencia/planeacionSecAcademico";
            nivel++;// 5 secretarioAcademico existente
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.init(5) nivel: " + nivel);
            buscarPlaneacionesLiberadasDirectores();
            planeaciones = ejb.buscarPlaneacionesCuatrimestrales();
            tipo = "Secretario";
            calculaPorcntajeAvances();

            return;
        }
        if (esDirector) {
            outcome = "docencia/planeacionDirector";
            docentesActivos = ejb.getPersonalDocenteActivo();
//            docentesActivos.stream().map(d -> d.getCategoriaOficial().getCategoria()).forEach(System.out::println);
            Faces.setSessionAttribute("docentesActivos", docentesActivos);//se utiliza para el conversor de personal
            if (docentesActivos.isEmpty()) {
                return;
            }
            nivel++; //6 hay docentes activos
            System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.init(6) nivel: " + nivel);
            colaboradores = ejb.getPersonalDocenteColaborador(docentesActivos, director,periodo.getPeriodo());
//            colaboradores.stream().map(d -> d.getCategoriaOficial().getCategoria()).forEach(System.out::println);
            planeaciones = ejb.inicializarPlaneaciones(colaboradores, periodo, director);
            ejb.ordenarPlaneaciones(planeaciones);
            buscarPlaneacionesLiberadas();
            listaActividadesVariad = ejb.buscarActividadesVarias();
            tipo = "Director";
            calculaPorcntajeAvances();
            planeacionesCuatrimestralesDetalles();

            return;
        }
    }

    public void onCellEdit(RowEditEvent event) {
//        Object oldValue = event.getOldValue();
//        Object newValue = event.getNewValue();
        PlaneacionesCuatrimestrales modificada = (PlaneacionesCuatrimestrales) event.getObject();
        Map.Entry<Boolean, String> res = ejb.validarPlaneacion(modificada);
        Map.Entry<Boolean, PlaneacionesCuatrimestrales> res2 = ejb.detectarReunionAcademia(modificada.getDocente(), director, periodo);
        if (res2.getKey()) {
            modificada.setComentariosSistema(String.format("La reunión de academia ya fue asignada por el director %s y usted ya no puede asignarla nuevamente.", res2.getValue().getDirector().getNombre()));
        }
        ejb.guardarPlaneacion(planeaciones.get(planeaciones.indexOf(modificada)));

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, res.getKey() ? "Planeación correcta" : "Planeación incorrecta", res.getValue());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("!!Operación cancelada!!");
    }

    public String abrir() {
        return outcome.concat("?faces-redirect=true");
    }

    public void agregarDocente() {
        nuevo = null;
    }

    public void agregarPlaneacion() {
        PlaneacionesCuatrimestrales pl = ejb.agregarDocente(colaboradores, director, nuevo, periodo);
        Boolean existe = !planeaciones.stream()
                .filter(p -> Objects.equals(p.getDirector().getClave(), pl.getDirector().getClave()) && Objects.equals(p.getDocente().getClave(), pl.getDocente().getClave()) && p.getPeriodo() == pl.getPeriodo())
                .collect(Collectors.toList())
                .isEmpty();
        
        if(!existe){
            planeaciones.add(pl);
            ejb.ordenarPlaneaciones(planeaciones);
        }
    }

    public void eliminarPlaneacion(PlaneacionesCuatrimestrales planeacion) {
        ejb.eliminarDocente(colaboradores, planeacion, planeaciones);
    }

    public void validarDirector(ValueChangeEvent e) {
        String id = e.getComponent().getClientId();
        PlaneacionesCuatrimestrales pc = planeaciones.get(Integer.parseInt(id.split("tblColaboradores:")[1].split(":validar")[0]));
        pc.setValidacionDirector((Boolean) e.getNewValue());
        ejb.guardarPlaneacion(pc);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.validarDirector(" + pc.isValidacionDirector() + ") pc: " + pc);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.validarDirector(" + id + ") value: " + e.getNewValue());
    }

    public void validarSecretarioAcademico(ValueChangeEvent e) {
        String id = e.getComponent().getClientId();
        PlaneacionesCuatrimestrales pc = planeaciones.get(Integer.parseInt(id.split("tblColaboradores:")[1].split(":validar")[0]));
        pc.setValidacionSecretarioAdemico((Boolean) e.getNewValue());
        ejb.guardarPlaneacion(pc);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.validarDirector(" + pc.isValidacionDirector() + ") pc: " + pc);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.validarDirector(" + id + ") value: " + e.getNewValue());
    }

    public void validarJefePersonal(ValueChangeEvent e) {
        String id = e.getComponent().getClientId();
        PlaneacionesCuatrimestrales pc = planeaciones.get(Integer.parseInt(id.split("tblColaboradores:")[1].split(":validar")[0]));
        pc.setValidacionJefePersonal((Boolean) e.getNewValue());
        ejb.guardarPlaneacion(pc);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.validarDirector(" + pc.isValidacionDirector() + ") pc: " + pc);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.validarDirector(" + id + ") value: " + e.getNewValue());
    }

    public void guardarComentario() {
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.guardarComentario(" + editando.getDocente().getNombre() + "): " + editando.getComentariosDirector());
        ejb.guardarPlaneacion(editando);
    }

    public List<Personal> completePersonal(String query) {
        return docentesActivos.stream()
                .filter(d -> !colaboradores.contains(d))
                .filter(d -> d.getNombre().contains(query.trim()))
                .collect(Collectors.toList());
    }

    public Long getDirectorCorrectas() {
        return planeaciones.stream().filter(pc -> pc.getValidacionSistema()).count();
    }

    public Long getDirectorValidados() {
        getDirectorPlaneacioFinalizado();
        return planeaciones.stream().filter(pc -> pc.getValidacionDirector()).count();
    }

    public Boolean getDirectorFinalizado() {
        return planeaciones.stream().anyMatch(pc -> !pc.getValidacionSistema());
    }

    public void getDirectorPlaneacioFinalizado() {
//        if (planeaciones.stream().filter(pc -> pc.getValidacionSistema()).count() == planeaciones.stream().filter(pc -> pc.getValidacionDirector()).count()) {
            if (detalles.getEstudiantesEstadia() == estudiantesAsigandos && detalles.getProyectosEstadia() == proyectosAsignados) {
                planeacionesFinalizadas = true;
            
        } else {
            planeacionesFinalizadas = false;
        }
    }

    public void buscarPlaneacionesLiberadas() {
        planeacionesLiberadas = new PlaneacionesLiberaciones();
        listPlaneacionesLiberadas.clear();
        listPlaneacionesLiberadas = ejb.buscarPlaneacionLiberada(periodo.getPeriodo(), Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina()));
        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.buscarPlaneacionesLiberadas(listPlaneacionesLiberadas)"+listPlaneacionesLiberadas);
        if (listPlaneacionesLiberadas == null) {
            planeacionesliberadasD = false;
        } else {
            planeacionesLiberadas = listPlaneacionesLiberadas.get(0);
            planeacionesliberadasD = planeacionesLiberadas.getLiberacionDirector();
        }
        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.buscarPlaneacionesLiberadas(planeacionesliberadasD)"+planeacionesliberadasD);
    }

    public void liberarPlaneacionesCuatrimestralesDirector() throws IOException {
        planeacionesLiberadas = new PlaneacionesLiberaciones();
        planeacionesLiberadasPK = new PlaneacionesLiberacionesPK();
        planeacionesLiberadasPK.setDirector(Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina()));
        planeacionesLiberadasPK.setPeriodo(periodo.getPeriodo());
        if (listPlaneacionesLiberadas != null) {
            planeacionesLiberadas = listPlaneacionesLiberadas.get(0);
            planeacionesLiberadas.setRechazoSecretarioAcademico(false);
        } else {
            planeacionesLiberadas.setValidacionSecretarioAcademico(false);
            planeacionesLiberadas.setValidacionJefePersonal(false);
            planeacionesLiberadas.setRechazoJefePersonal(false);
            planeacionesLiberadas.setRechazoSecretarioAcademico(false);
        }
        if (planeaciones.stream().filter(pc -> pc.getValidacionSistema()).count() == planeaciones.stream().filter(pc -> pc.getValidacionDirector()).count()) {
            planeacionesLiberadas.setLiberacionSistema(true);
        }else{
            planeacionesLiberadas.setLiberacionSistema(false);
        }
        planeacionesLiberadas.setPlaneacionesLiberacionesPK(planeacionesLiberadasPK);
        planeacionesLiberadas.setLiberacionDirectorFecha(new Date());
        planeacionesLiberadas.setLiberacionDirector(true);

        if (listPlaneacionesLiberadas != null) {
            ejb.actualizarPlaneacionLiberada(planeacionesLiberadas);
        } else {
            ejb.agregarPlaneacionLiberada(planeacionesLiberadas);
        }
        Messages.addGlobalInfo("Planeación cuatrimestral liberada correctamente");
        Faces.refresh();
    }

    public void asignarActividadesVarias(ValueChangeEvent event) {
        if (Integer.parseInt(event.getNewValue().toString()) == 0) {
            nuevaActividad = true;
            claveActividadVaria = 0;
        } else {
            nuevaActividad = false;
            claveActividadVaria = Integer.parseInt(event.getNewValue().toString());
        }
    }

    public void planeacionesCuatrimestralesDetalles() {
        estudiantesEstadia = 0;
        detallesPK = new PlaneacionesDetallesPK();
        detalles = new PlaneacionesDetalles();
        listaDetalles = ejb.buscraPlaneacionesDetalles(periodo.getPeriodo(), Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina()));
        listaTotalAlumnosCarreraPyes = ejb.mostraralumnosProximosAEstadia(logonMB.getListaUsuarioClaveNomina().getNumeroNomina());

        if (listaTotalAlumnosCarreraPyes != null) {
            estudiantesEstadia = listaTotalAlumnosCarreraPyes.stream().filter(y -> y.getPeriodo().equals(periodo.getPeriodo()) && (y.getGrado().equals(Short.parseShort("6")) || y.getGrado().equals(Short.parseShort("11")))).mapToInt(VistaTotalAlumnosCarreraPye::getTotalMatricula).sum();
        }
        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.planeacionesCuatrimestralesDetalles(estudiantesEstadia) " + estudiantesEstadia);
        if (estudiantesEstadia != 0) {
            proximosAEstadia = true;
            if (listaDetalles == null) {
                detallesPK.setDirector(Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina()));
                detallesPK.setPeriodo(periodo.getPeriodo());
                detalles.setPlaneacionesDetallesPK(detallesPK);
                detalles.setEstudiantesEstadia(Short.valueOf(estudiantesEstadia.toString()));
                detalles.setProyectosEstadia(Short.valueOf(proyectosEstadia.toString()));
                ejb.agregarPlaneacionesDetalles(detalles);
            } else {
                detalles = listaDetalles.get(0);
                if (detalles.getEstudiantesEstadia() != estudiantesEstadia) {
                    detalles.setEstudiantesEstadia(estudiantesEstadia);
                    ejb.actualizarPlaneacionesDetalles(detalles);
                }
                listaDetalles = ejb.buscraPlaneacionesDetalles(periodo.getPeriodo(), Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina()));
                detalles = new PlaneacionesDetalles();
                detalles = listaDetalles.get(0);
            };
        }
    }

    public void planeacionesCuatrimestralesDetallesSecretarioYJefe() {
        detallesPK = new PlaneacionesDetallesPK();
        detalles = new PlaneacionesDetalles();
        listaDetalles = ejb.buscraPlaneacionesDetalles(periodo.getPeriodo(), director.getClave());
        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.planeacionesCuatrimestralesDetallesSecretarioYJefe(listaDetalles)"+listaDetalles);
        if (listaDetalles == null) {
            detallesPK.setDirector(director.getClave());
            detallesPK.setPeriodo(periodo.getPeriodo());
            detalles.setPlaneacionesDetallesPK(detallesPK);
            detalles.setEstudiantesEstadia(Short.valueOf(estudiantesEstadia.toString()));
            detalles.setProyectosEstadia(Short.valueOf(proyectosEstadia.toString()));
            ejb.agregarPlaneacionesDetalles(detalles);
        } else {
            detalles = listaDetalles.get(0);
        }
        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.planeacionesCuatrimestralesDetallesSecretarioYJefe(detalles)"+detalles);
        List<VistaTotalAlumnosCarreraPye> alumnos = ejb.mostraralumnosProximosAEstadia(director.getClave().toString());
        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.planeacionesCuatrimestralesDetallesSecretarioYJefe(alumnos)"+alumnos);
        if (alumnos != null) {
            System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.planeacionesCuatrimestralesDetallesSecretarioYJefe(alumnos)"+alumnos);
            if(alumnos.stream().filter(y -> y.getPeriodo().equals(periodo.getPeriodo()) && (y.getGrado().equals(Short.parseShort("6")) || y.getGrado().equals(Short.parseShort("11")))).count() !=0 ){
                proximosAEstadia=true;
            }
        }
        
    }

    public void actualizaPlaneacionesDetalles() {
        detalles.setEstudiantesEstadia(estudiantesEstadia);
        detalles.setProyectosEstadia(proyectosEstadia);
        ejb.actualizarPlaneacionesDetalles(detalles);
    }

    public long getTotalEstudiantesAsigandos() {
        estudiantesAsigandos = 0;
        if (planeaciones != null) {
            planeaciones.forEach((t) -> {
                estudiantesAsigandos = estudiantesAsigandos + t.getEstudiantesEstadia();
            });
        }
        return estudiantesAsigandos;
    }

    public long getTotalProyuectosAsigandos() {
        proyectosAsignados = 0;
        if (planeaciones != null) {
            planeaciones.forEach((t) -> {
                proyectosAsignados = proyectosAsignados + t.getProyectosEstadia();
            });
        }
        return proyectosAsignados;
    }

    public void actualizarplaneacionesCuatrimestralesEstudiantesEstadia(ValueChangeEvent event) {
        estudiantesEstadia = Integer.parseInt(event.getNewValue().toString());
    }

    public void actualizarplaneacionesCuatrimestralesProyectosEstadia(ValueChangeEvent event) {
        proyectosEstadia = Integer.parseInt(event.getNewValue().toString());
    }

    //////////////////////////////////////////////////////////////////////////// Secretario Academico
    public void buscarPlaneacionesLiberadasDirectores() {
        listPlaneacionesLiberadasSelecMenu.clear();
        listPlaneacionesLiberadasSelecMenu = ejb.buscarPlaneacionesLiberadasParaValidarSecretarioAcademico(periodo.getPeriodo());
    }

    public Long getSecretarioAcademicoCorrectas() {
        if (planeaciones == null) {
            return 0L;
        } else {
            return planeaciones.stream().filter(pc -> pc.getValidacionDirector()).count();
        }
    }

    public Long getSecretarioAcademicoValidados() {
        if (planeaciones == null) {
            return 0L;
        } else {
            getSecretarioAcademicoPlaneacioFinalizado();
            return planeaciones.stream().filter(pc -> pc.getValidacionSecretarioAdemico()).count();
        }
    }

    public void getSecretarioAcademicoPlaneacioFinalizado() {
        if (planeaciones != null) {
            if (planeaciones.stream().filter(pc -> pc.getValidacionSistema()).count() == planeaciones.stream().filter(pc -> pc.getValidacionSecretarioAdemico()).count()) {
                planeacionesFinalizadas = true;
            } else {
                planeacionesFinalizadas = false;
            }
        }
    }

    public void liberarPlaneacionesCuatrimestralesSecretarioAcademico() throws IOException {
        planeacionesLiberadas = new PlaneacionesLiberaciones();
        planeacionesLiberadasPK = new PlaneacionesLiberacionesPK();
        planeacionesLiberadasPK.setDirector(director.getClave());
        planeacionesLiberadasPK.setPeriodo(periodo.getPeriodo());
        if (listPlaneacionesLiberadas != null) {
            planeacionesLiberadas = listPlaneacionesLiberadas.get(0);
            planeacionesLiberadas.setRechazoJefePersonal(false);
        }
        planeacionesLiberadas.setPlaneacionesLiberacionesPK(planeacionesLiberadasPK);
        planeacionesLiberadas.setValidacionSecretarioAcademicoFecha(new Date());
        planeacionesLiberadas.setValidacionSecretarioAcademico(true);
        planeacionesLiberadas.setRechazoSecretarioAcademico(false);

        if (listPlaneacionesLiberadas != null) {
            ejb.actualizarPlaneacionLiberada(planeacionesLiberadas);
        } else {
            ejb.agregarPlaneacionLiberada(planeacionesLiberadas);
        }
        Messages.addGlobalInfo("Planeación cuatrimestral liberada correctamente");
        Faces.refresh();
    }

    public void denegarPlaneacionesCuatrimestralesSecretarioAcademico() throws IOException {
        planeacionesLiberadas = new PlaneacionesLiberaciones();
        planeacionesLiberadasPK = new PlaneacionesLiberacionesPK();
        planeacionesLiberadasPK.setDirector(director.getClave());
        planeacionesLiberadasPK.setPeriodo(periodo.getPeriodo());
        if (listPlaneacionesLiberadas != null) {
            planeacionesLiberadas = listPlaneacionesLiberadas.get(0);
        }
        planeacionesLiberadas.setPlaneacionesLiberacionesPK(planeacionesLiberadasPK);
        planeacionesLiberadas.setRechazoSecretarioAcademicoFecha(new Date());
        planeacionesLiberadas.setValidacionSecretarioAcademico(false);
        planeacionesLiberadas.setRechazoSecretarioAcademico(true);
        planeacionesLiberadas.setLiberacionDirector(false);

        if (listPlaneacionesLiberadas != null) {
            ejb.actualizarPlaneacionLiberada(planeacionesLiberadas);
        } else {
            ejb.agregarPlaneacionLiberada(planeacionesLiberadas);
        }
        Messages.addGlobalInfo("Planeación cuatrimestral liberada correctamente");
        Faces.refresh();
    }

    public void comparacionPlaneacionesLiberadasSecretarioAcademico() {
        planeacionesLiberadas = new PlaneacionesLiberaciones();
        listPlaneacionesLiberadas.clear();
        listPlaneacionesLiberadas = ejb.buscarPlaneacionLiberada(planeacionesLiberadaDPK.getPeriodo(), planeacionesLiberadaDPK.getDirector());
        if (listPlaneacionesLiberadas == null) {
            planeacionesliberadasS = false;
        } else {
            planeacionesLiberadas = listPlaneacionesLiberadas.get(0);
            planeacionesliberadasS = planeacionesLiberadas.getValidacionSecretarioAcademico();
        }
    }

    //////////////////////////////////////////////////////////////////////////// Jefe de personal
    public void buscarPlaneacionesLiberadasSecretarioAcademico() {
        listPlaneacionesLiberadasSelecMenu.clear();
        listPlaneacionesLiberadasSelecMenu = ejb.buscarPlaneacionesLiberadasParaValidarJefePersonal(periodo.getPeriodo());
    }

    public Long getJefePersonalCorrectas() {
        if (planeaciones == null) {
            return 0L;
        } else {
            return planeaciones.stream().filter(pc -> pc.getValidacionSecretarioAdemico()).count();
        }
    }

    public Long getJefePersonalValidados() {
        if (planeaciones == null) {
            return 0L;
        } else {
            getJefePersonalPlaneacioFinalizado();
            return planeaciones.stream().filter(pc -> pc.getValidacionJefePersonal()).count();
        }
    }

    public void getJefePersonalPlaneacioFinalizado() {
        if (planeaciones != null) {
            if (planeaciones.stream().filter(pc -> pc.getValidacionSistema()).count() == planeaciones.stream().filter(pc -> pc.getValidacionJefePersonal()).count()) {
                planeacionesFinalizadas = true;
            } else {
                planeacionesFinalizadas = false;
            }
        }
    }

    public void liberarPlaneacionesCuatrimestralesJefePersonal() throws IOException {
        planeacionesLiberadas = new PlaneacionesLiberaciones();
        planeacionesLiberadasPK = new PlaneacionesLiberacionesPK();
        planeacionesLiberadasPK.setDirector(director.getClave());
        planeacionesLiberadasPK.setPeriodo(periodo.getPeriodo());
        if (listPlaneacionesLiberadas != null) {
            planeacionesLiberadas = listPlaneacionesLiberadas.get(0);
        }
        planeacionesLiberadas.setPlaneacionesLiberacionesPK(planeacionesLiberadasPK);
        planeacionesLiberadas.setValidacionJefePersonalFecha(new Date());
        planeacionesLiberadas.setValidacionJefePersonal(true);
        planeacionesLiberadas.setRechazoJefePersonal(false);

        if (listPlaneacionesLiberadas != null) {
            ejb.actualizarPlaneacionLiberada(planeacionesLiberadas);
        } else {
            ejb.agregarPlaneacionLiberada(planeacionesLiberadas);
        }
        Messages.addGlobalInfo("Planeación cuatrimestral liberada correctamente");
        Faces.refresh();
    }

    public void denegarPlaneacionesCuatrimestralesJefePersonal() throws IOException {
        planeacionesLiberadas = new PlaneacionesLiberaciones();
        planeacionesLiberadasPK = new PlaneacionesLiberacionesPK();
        planeacionesLiberadasPK.setDirector(director.getClave());
        planeacionesLiberadasPK.setPeriodo(periodo.getPeriodo());
        if (listPlaneacionesLiberadas != null) {
            planeacionesLiberadas = listPlaneacionesLiberadas.get(0);
        }
        planeacionesLiberadas.setPlaneacionesLiberacionesPK(planeacionesLiberadasPK);
        planeacionesLiberadas.setRechazoJefePersonalFecha(new Date());
        planeacionesLiberadas.setValidacionJefePersonal(false);
        planeacionesLiberadas.setRechazoJefePersonal(true);
        planeacionesLiberadas.setValidacionSecretarioAcademico(false);

        if (listPlaneacionesLiberadas != null) {
            ejb.actualizarPlaneacionLiberada(planeacionesLiberadas);
        } else {
            ejb.agregarPlaneacionLiberada(planeacionesLiberadas);
        }
        Messages.addGlobalInfo("Planeación cuatrimestral liberada correctamente");
        Faces.refresh();
    }

    public void comparacionPlaneacionesLiberadasJefePersonal() {
        planeacionesLiberadas = new PlaneacionesLiberaciones();
        listPlaneacionesLiberadas.clear();
        listPlaneacionesLiberadas = ejb.buscarPlaneacionLiberada(planeacionesLiberadaDPK.getPeriodo(), planeacionesLiberadaDPK.getDirector());
        if (listPlaneacionesLiberadas == null) {
            planeacionesliberadasP = false;
        } else {
            planeacionesLiberadas = listPlaneacionesLiberadas.get(0);
            planeacionesliberadasP = planeacionesLiberadas.getValidacionJefePersonal();
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void buscarPlaneacionesLiberadasPorDirrectores(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            planeacionesLiberadas = new PlaneacionesLiberaciones();
            director = new Personal();

            director = ejb.getDirector(Integer.parseInt(event.getNewValue().toString()));
            System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.buscarPlaneacionesLiberadasPorDirrectores(director)"+director);
            planeaciones = ejb.getPlaneacionesPorDirectorPeriodo(director, periodo);
            System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.buscarPlaneacionesLiberadasPorDirrectores(planeaciones)"+planeaciones.size());
        }
        listPlaneacionesLiberadas.clear();
        listPlaneacionesLiberadas = ejb.buscarPlaneacionLiberada(periodo.getPeriodo(), Integer.parseInt(event.getNewValue().toString()));
        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.buscarPlaneacionesLiberadasPorDirrectores(listPlaneacionesLiberadas)"+listPlaneacionesLiberadas);
        if (listPlaneacionesLiberadas == null) {
            planeacionesliberadasS = false;
            planeacionesliberadasP = false;
        } else {
            planeacionesLiberadas = listPlaneacionesLiberadas.get(0);
            planeacionesliberadasS = planeacionesLiberadas.getValidacionSecretarioAcademico();
            planeacionesliberadasD = planeacionesLiberadas.getValidacionJefePersonal();
        }

        planeacionesCuatrimestralesDetallesSecretarioYJefe();
    }

    public void imprimirPlaneaciones() {
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.imprimirPlaneaciones()" + planeacionesLiberadaDPK);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.imprimirPlaneaciones() nuevo: " + nuevo);
//         planeaciones.forEach(pc ->{
//            String cade = "PlaneacionesCuatrimestrales{" 
//                    + "planeacion=" + pc.getPlaneacion() 
//                    + ", periodo=" + pc.getPeriodo()
//                    + ", horasClaseTsu=" + pc.getHorasClaseTsu()
//                    + ", horasClaseIng=" + pc.getHorasClaseIng()
//                    + ", proyectoInvestigacion=" + pc.getProyectoInvestigacion()
//                    + ", asesoriaClase=" + pc.getAsesoriaClase()
//                    + ", tutoria=" + pc.getTutoria()
//                    + ", reunionAcademia=" + pc.getReunionAcademia()
//                    + ", actividadesVarias=" + pc.getActividadesVarias()
//                    + ", validacionSecretarioAdemico=" + pc.isValidacionSecretarioAdemico()
//                    + ", fechaValidacionSecretarioAdemico=" + pc.getFechaValidacionSecretarioAdemico()
//                    + ", validacionJefePersonal=" + pc.isValidacionJefePersonal()
//                    + ", fechaValidacionJefePersonal=" + pc.getFechaValidacionJefePersonal()
//                    + ", comentariosDirector=" + pc.getComentariosDirector()
//                    + ", comentariosSecretarioAcademico=" + pc.getComentariosSecretarioAcademico()
//                    + ", comentariosJefePersonal=" + pc.getComentariosJefePersonal()
//                    + ", comentariosSistema=" + pc.getComentariosSistema()
//                    + ", validacionSistema=" + pc.isValidacionSistema()
//                    + ", total=" + pc.getTotal()
//                    + ", director=" + pc.getDirector()
//                    + ", docente=" + pc.getDocente() + '}';
//             System.out.println("mx.edu.utxj.pye.sgi.controlador.docencia.PlaneacionCuatrimestral.imprimirPlaneaciones(): " + cade);
//         });
    }

    public void buscarActividadesVariasPlaneacionCuatrimestral(PlaneacionesCuatrimestrales planeacionEditando) {
        totalHorasAsignadas = 0;
        horasMaximas = 0;
        listaActividadesvariasplaneacionescuatrimestraleses = ejb.buscarActividadesvariasplaneacionescuatrimestraleses(planeacionEditando.getPlaneacion());
        if (listaActividadesvariasplaneacionescuatrimestraleses == null) {
            totalHorasAsignadas = 0;
            superaAsigacion = false;
            horasMaximas = Integer.parseInt(String.valueOf(planeacionEditando.getActividadesVarias()));
        } else {
            totalHorasAsignadas = 0;
            listaActividadesvariasplaneacionescuatrimestraleses.forEach((t) -> {
                totalHorasAsignadas = totalHorasAsignadas + t.getHorasAsignadas();
                superaAsigacion = false;
            });

            horasMaximas = Integer.parseInt(String.valueOf(planeacionEditando.getActividadesVarias())) - totalHorasAsignadas;
            if (Short.parseShort(totalHorasAsignadas.toString()) >= planeacionEditando.getActividadesVarias()) {
                superaAsigacion = true;
            }
        }
    }

    public void guardarActividadesVariasPlaneacionCuatrimestral() {
        atividadesvariasplaneacionescuatrimestralesPK = new AtividadesvariasplaneacionescuatrimestralesPK();
        nuevaAtividadesvariasplaneacionescuatrimestrales = new Atividadesvariasplaneacionescuatrimestrales();
        nuevaActividadesVariad = new Actividadesvarias();

        if (0 == claveActividadVaria) {
            nuevaActividadesVariad.setNombreAcctividad(nombreActividad);
            nuevaActividadesVariad = ejb.agregarActividadesvarias(nuevaActividadesVariad);
            claveActividadVaria = nuevaActividadesVariad.getActividad();
        }
        atividadesvariasplaneacionescuatrimestralesPK = new AtividadesvariasplaneacionescuatrimestralesPK(claveActividadVaria, editando.getPlaneacion());
        nuevaAtividadesvariasplaneacionescuatrimestrales.setAtividadesvariasplaneacionescuatrimestralesPK(atividadesvariasplaneacionescuatrimestralesPK);
        nuevaAtividadesvariasplaneacionescuatrimestrales.setHorasAsignadas(horasAsisgnadas);
        nuevaAtividadesvariasplaneacionescuatrimestrales = ejb.agregarAtividadesvariasplaneacionescuatrimestrales(nuevaAtividadesvariasplaneacionescuatrimestrales);
        if (nuevaAtividadesvariasplaneacionescuatrimestrales == null) {
            Messages.addGlobalWarn("La asignaion de actividad ya existe");
        } else {
            Messages.addGlobalInfo("Asignacion de actividad correcta");
        }
        buscarActividadesVariasPlaneacionCuatrimestral(editando);
    }

    public void asignarnombreActividad(ValueChangeEvent event) {
        nombreActividad = event.getNewValue().toString();
    }

    public void asignarHorasAsignadas(ValueChangeEvent event) {
        horasAsisgnadas = Integer.parseInt(event.getNewValue().toString());
    }

    public void eliminarAsignacionActividadVarias(Atividadesvariasplaneacionescuatrimestrales actividadesvarias) {
        ejb.eliminarActividadVaria(actividadesvarias);
    }

    public void onCellEditHoras(RowEditEvent event) {

        ejb.actualizarAtividadesvariasplaneacionescuatrimestrales((Atividadesvariasplaneacionescuatrimestrales) event.getObject());
    }

    public void calculaPorcntajeAvances() {
        planeacionesTotalesDirectores = 0;
        if (planeaciones != null) {
            switch (tipo) {
                case "Director":
                    planeaciones.forEach((t) -> {
                        if (t.getValidacionDirector()) {
                            totalValidados = totalValidados + 1;
                        }
                    });
                    planeacionesTotalesDirectores = planeaciones.size();
                    break;
                case "Secretario":
                    planeaciones.forEach((t) -> {
                        if (listPlaneacionesLiberadasSelecMenu != null) {
                            listPlaneacionesLiberadasSelecMenu.forEach((pl) -> {
                                if (pl.getLiberacionDirector() == true) {
                                    if (Objects.equals(pl.getPersonal().getClave(), t.getDirector().getClave())) {
                                        planeacionesTotalesDirectores = planeacionesTotalesDirectores + 1;
                                        if (t.getValidacionSecretarioAdemico()) {
                                            totalValidados = totalValidados + 1;
                                        }
                                    }
                                }
                            });
                        }
                    });
                    break;
                case "Jefe":
                    planeaciones.forEach((t) -> {
                        if (listPlaneacionesLiberadasSelecMenu != null) {
                            listPlaneacionesLiberadasSelecMenu.forEach((pl) -> {
                                if (pl.getValidacionSecretarioAcademico() == true) {
                                    if (Objects.equals(pl.getPersonal().getClave(), t.getDirector().getClave())) {
                                        planeacionesTotalesDirectores = planeacionesTotalesDirectores + 1;
                                        if (t.getValidacionJefePersonal()) {
                                            totalValidados = totalValidados + 1;
                                        }
                                    }
                                }
                            });
                        }
                    });
                    break;
            }
            promedioAvance = (Double.parseDouble(totalValidados.toString()) * 100.0) / (Double.parseDouble(String.valueOf(planeacionesTotalesDirectores)));
            switch (tipo) {
                case "Director":
                    if ("0.0".equals(promedioAvance.toString())) {
                        mensajeStattusPlaneacion = "Pendiente";
                    } else {
                        if (promedioAvance.toString().equals("100.0")) {
                            if (planeacionesliberadasD == true) {
                                mensajeStattusPlaneacion = "Terminada";
                            } else {
                                mensajeStattusPlaneacion = "Sin liberar planeación";
                            }
                        } else {
                            mensajeStattusPlaneacion = df.format(promedioAvance) + "%";
                        }
                    }
                    break;
                default:
                    if ("0.0".equals(promedioAvance.toString())) {
                        mensajeStattusPlaneacion = "Pendiente";
                    } else {
                        if (promedioAvance.toString().equals("100.0")) {
                            mensajeStattusPlaneacion = "Terminada";
                        } else {
                            mensajeStattusPlaneacion = df.format(promedioAvance) + "%";
                        }
                    }
                    planeaciones = null;
                    break;
            }
        } else {
            mensajeStattusPlaneacion = "0%";
        }
    }
    
    
}
