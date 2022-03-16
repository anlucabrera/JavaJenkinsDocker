/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.evaluaciones;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluacion360;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacion360Combinaciones;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacionDesempenio;
import mx.edu.utxj.pye.sgi.dto.listadoEvaluacionesDTO;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacion3601;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacion360Combinaciones;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.view.Listaperiodosescolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.controladores.ch.AdministracionControl;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonalconfiguracion;
import mx.edu.utxj.pye.sgi.entity.ch.CategoriasHabilidades;
import mx.edu.utxj.pye.sgi.entity.ch.CategoriasHabilidadesPK;
import mx.edu.utxj.pye.sgi.entity.ch.Habilidades;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Ajax;
import org.primefaces.event.RowEditEvent;




/**
 *
 * @author UTXJ
 */
@Named //(value = "adminEvaluacion360")
//@SessionScoped
@ViewScoped
public class AdminEvaluacion360 implements Serializable {
    private static final long serialVersionUID = -3603823180153167652L;
    /**
     * el nivel de operacion hace referencia a el nivel de los filtros para generer Evaluaciones, o las combinaciones de las mismas
     */
    @Getter @Setter private Integer nivelOperacion, nivelOperacion2, periodoSeleccionado, periodoEvaluacion, evEd;
    @Getter @Setter private String tipoEvaluacion;
    @Getter private Boolean e360b = true, edesempenio = false ;
    @Getter @Setter private Date fechaA, fechaB;
    @Getter @Setter private Personal evaluadorAu, evaluadoAu;
    @Getter @Setter private PersonalCategorias categoria;
    
    @Getter @Setter private Evaluaciones360 nueva360;
    @Getter @Setter private DesempenioEvaluaciones nuevaDesempenio;
    @Getter @Setter private Evaluaciones360Resultados resultado360;
    @Getter @Setter private DesempenioEvaluacionResultados resultadosDesempeño;
    
    @Getter private Boolean configurado;
    @Getter private Evaluaciones360 evaluacionActiva, nuevaEvaluacion;
    @Getter private List<Personal> personalActivo, listaPersonal;
    @Getter private Map<Personal, List<Personal>> directivosOperativos;
    @Getter private SortedMap<PeriodosEscolares, Personal> evaluadoresAnteriores;
    @Getter private List<Evaluaciones360Resultados> resultados;
    @Getter @Setter List<DtoEvaluacion360> combinacionesDto;
    
    @Getter @Setter private List<PersonalCategorias> listaCategorias;
    @Getter @Setter private List<Evaluaciones360> listaEvaluaciones360;
    @Getter @Setter private List<DesempenioEvaluaciones> listaEvaluacionesDesempenio;
    @Getter @Setter private List<listadoEvaluacionesDTO> listaEvaluaciones;
    @Getter @Setter private List<ListaEvaluacion360Combinaciones> listaCombinacion360, listaCombinaciones360Filtro;
    @Getter @Setter private List<ListaEvaluacionDesempenio> listadoEvaluacionDesempenio, listaEvaluacionDesempeñoFiltro;
    
    @Getter @Setter private List<SelectItem> sIPeriodo360 , sIPeriodoDesempemio, sIPeriodos ;
    
    //Inicio Admin Ev360
    @Getter    @Setter    private List<Evaluaciones360> evaluaciones360s = new ArrayList<>();
    @Getter    @Setter    private List<PeriodosEscolares> periodosEscolareses = new ArrayList<>();
    @Getter    @Setter    private List<PersonalCategorias> categorias360 = new ArrayList<>();
    @Getter    @Setter    private List<Habilidades> habilidadeses = new ArrayList<>();
    @Getter    @Setter    private List<CategoriasHabilidades> categoriasHabilidadeses = new ArrayList<>();
    
    @Getter    @Setter    private List<Boolean> estatus = new ArrayList<>();
    
    @Getter    @Setter    private PersonalCategorias categorias;  
    @Getter    @Setter    private Habilidades habilidades;  
    @Getter    @Setter    private Evaluaciones360 evaluaciones360CategoriasHablidades;  
    
    
    @Getter    @Setter    private Integer periodoEv = 0,pestaniaActiva=0,periodoActivo=0,cvlHab=0;
    @Getter    @Setter    private Date fechaI, fechaF;  
    @Getter    @Setter    private Boolean otroPeriodo;  
    @Getter    @Setter    private Short cvlCat=0;      
    @Getter    @Setter    private String datosP="",nombCat="",nombHab="";
    
    //Fin Admin Ev360
    
    @EJB EJBSelectItems eJBSelectItems;
    @EJB EjbEvaluacion360Combinaciones ejbEvaluacion360Combinaciones;
    @EJB EjbEvaluacion3601 ejbEvaluacion3601;
    @EJB EjbAdministracionEncuestas ejbAdministracionEncuestas;
    @EJB EjbEvaluacionDesempenio ejbDes;
    
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbUtilidadesCH;

@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;



    @PostConstruct
    public void init() {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        listaPersonal = new ArrayList<>();
        listaPersonal = ejbEvaluacion360Combinaciones.getPersonalActivo();
        listaCategorias = new ArrayList<>();
        listaCategorias = ejbEvaluacion360Combinaciones.getCategorias();
        Faces.setSessionAttribute("personalCategoria", listaCategorias);
        Faces.setSessionAttribute("docentesActivos", listaPersonal);//se utiliza para el conversor de personal
        sIPeriodo360 = eJBSelectItems.itemsPeriodos360();
        sIPeriodoDesempemio = eJBSelectItems.itemsPeriodos360();
        sIPeriodos = eJBSelectItems.itemsPeriodos();
        listadosAdministracion();
    }

    public void generaEvaluacion360() {
        evaluacionActiva = ejbEvaluacion3601.getUltimaEvaluacion();
        resultados = ejbEvaluacion360Combinaciones.generar(evaluacionActiva);
        combinacionesDto = new ArrayList<>();
        resultados.forEach(res -> {
            combinacionesDto.add(ejbEvaluacion360Combinaciones.packCombinaciones(res)) ;
            //System.out.println("res = (periodo=" + res.getEvaluaciones360().getPeriodo() + ", evaluación=" + res.getEvaluaciones360().getEvaluacion() + ", evaluado=" + res.getEvaluaciones360ResultadosPK().getEvaluado() + ", tipo=" + res.getTipo() + ", evaluador=" + res.getEvaluaciones360ResultadosPK().getEvaluador() + ".");
        });
//        configurado = ejbEvaluacion360Combinaciones.detectarConfiguracion(nuevaEvaluacion);

//        if (!configurado) {
        //ejbEvaluacion360Combinaciones.guardarCombinaciones(resultados);
//        }
    }

    public void genera360() {
        if (periodoEvaluacion == null || fechaA == null || fechaB == null) {
            Messages.addGlobalWarn("Asegurese de completar los campos antes de crear la encuesta");
        } else {
            nueva360 = new Evaluaciones360();
            nueva360.setEvaluacion(0);
            nueva360.setPeriodo(periodoEvaluacion);
            nueva360.setFechaInicio(fechaA);
            nueva360.setFechaFin(fechaB);
            nuevaEvaluacion = ejbAdministracionEncuestas.nuevaEvaluacion360(nueva360);
            generaEvaluacion360();
        }
    }

    public void generaEvlaucionDesempenio() {
        nuevaDesempenio = new DesempenioEvaluaciones();
        nuevaDesempenio.setEvaluacion(0);
        nuevaDesempenio.setPeriodo(periodoEvaluacion);
        nuevaDesempenio.setFechaInicio(fechaA);
        nuevaDesempenio.setFechaFin(fechaB);
        ejbAdministracionEncuestas.nuevaEvaluacionDesempenio(nuevaDesempenio);
    }

    public void validarNuevaEvaluacion() {
        if (e360b && edesempenio) {
            Messages.addGlobalWarn("Sólo debe seleccionar una evaluación.");
        } else if (!e360b && !edesempenio) {
            Messages.addGlobalWarn("Debe seleccionar una evaluación.");
        }
    }

    public void verListaEvaluaciones() {
        listaEvaluaciones = new ArrayList<>();
        if (e360b && edesempenio) {
            Messages.addGlobalWarn("solo debe seleccionar una evlaución al mismo tiempo.");
        } else if (!e360b && !edesempenio) {
            Messages.addGlobalWarn("solo debe seleccionar una evaluación.");
        }
        if (e360b) {
            List<Evaluaciones360> le = ejbEvaluacion360Combinaciones.getEvaluaciones360();
            if (le.isEmpty()) {
                Messages.addGlobalWarn("No existen evaluaciones para mostrar");
            } else {
                le.forEach(e -> {
                    Integer pe = e.getPeriodo();
                    Listaperiodosescolares pEva = ejbEvaluacion360Combinaciones.getPeriodoEscolar(pe);
                    String status = "";
                    Date hoy = (new Date());
                    if (e.getFechaInicio().before(hoy) && e.getFechaFin().after(hoy)) {
                        status = "Activo";
                    } else {
                        status = "Inactivo";
                    }
                    listaEvaluaciones.add(new listadoEvaluacionesDTO(e.getEvaluacion(), pEva.getMesInicio() + "-" + pEva.getMesFin() + " " + pEva.getAnio() + " (" + pEva.getPeriodo() + ")", e.getFechaInicio(), e.getFechaFin(), status));
                });
            }
        } else if (edesempenio) {
            List<DesempenioEvaluaciones> le = ejbEvaluacion360Combinaciones.getEvaluacionesDesempenio();
            if (le.isEmpty()) {
                Messages.addGlobalWarn("No existen evaluaciones para mostrar");
            } else {
                le.forEach(e -> {
                    Integer pe = e.getPeriodo();
                    Listaperiodosescolares pEva = ejbEvaluacion360Combinaciones.getPeriodoEscolar(pe);
                    String status = "";
                    Date hoy = (new Date());
                    if (e.getFechaInicio().before(hoy) && e.getFechaFin().after(hoy)) {
                        status = "Activo";
                    } else {
                        status = "Inactivo";
                    }
                    listaEvaluaciones.add(new listadoEvaluacionesDTO(e.getEvaluacion(), pEva.getMesInicio() + "-" + pEva.getMesFin() + " " + pEva.getAnio() + " (" + pEva.getPeriodo() + ")", e.getFechaInicio(), e.getFechaFin(), status));
                });
            }
        } else {
            Messages.addGlobalWarn("solo debe seleccionar una evaluación.");
        }
    }

    public void getListaCombinaciones(Integer evaluacion) {
        Integer evaluaciionN = evaluacion;
        if (e360b && edesempenio) {
            Messages.addGlobalWarn("solo debe seleccionar una evlaución al mismo tiempo.");
        } else if (!e360b && !edesempenio) {
            Messages.addGlobalWarn("solo debe seleccionar una evaluación.");
        }
        if (e360b) {
            personalActivo = new ArrayList<>();
            listaCombinacion360 = new ArrayList<>();
            listaEvaluacionDesempeñoFiltro = new ArrayList<>();
            personalActivo = ejbEvaluacion360Combinaciones.getPersonalActivo();
            personalActivo.forEach(p -> {
                List<Evaluaciones360Resultados> lr = ejbEvaluacion360Combinaciones.getResultados360(evaluaciionN, p.getClave());
                if (lr == null || lr.isEmpty()) {
//                    System.out.println("Persona inactiva durante el periodo : " + p.getNombre());
                } else {
                    lr.forEach(lr360 -> {
                        Personal evaluador = ejbEvaluacion360Combinaciones.getPersonalEvaluador(lr360.getPersonal1().getClave());
                        String claveLista = evaluacion + "" + evaluador.getClave() + "" + p.getClave();
                        listaCombinacion360.add(new ListaEvaluacion360Combinaciones(Integer.parseInt(claveLista), evaluacion, lr360.getTipo(),
                                p.getClave(), p.getNombre(), ejbEvaluacion360Combinaciones.getAreaPorClave(p.getAreaOperativa()).getNombre(),
                                ejbEvaluacion360Combinaciones.getCategoriaPorClave(Integer.parseInt(p.getCategoriaOperativa().getCategoria().toString())).getNombre(),
                                ejbEvaluacion360Combinaciones.getCategoriaPorClave(Integer.parseInt(p.getCategoria360().getCategoria().toString())).getNombre(),
                                evaluador.getClave(), evaluador.getNombre(), ejbEvaluacion360Combinaciones.getAreaPorClave(evaluador.getAreaOperativa()).getNombre(),
                                ejbEvaluacion360Combinaciones.getCategoriaPorClave(Integer.parseInt(evaluador.getCategoriaOperativa().getCategoria().toString())).getNombre()));
                    });
                }
            });
        } else if (edesempenio) {
            personalActivo = new ArrayList<>();
            listadoEvaluacionDesempenio = new ArrayList<>();
            personalActivo = ejbEvaluacion360Combinaciones.getPersonalActivo();
            personalActivo.forEach(p -> {
                List<DesempenioEvaluacionResultados> lr = ejbAdministracionEncuestas.getEvaluacionesDesempenioSubordinados(evaluaciionN, p.getClave());
                if (lr == null || lr.isEmpty()) {
//                    System.out.println("Persona no evaluada en desempeño este periodo : " + p.getNombre());
                } else {
                    lr.forEach(rd -> {
                        Personal evaluador = ejbEvaluacion360Combinaciones.getPersonalEvaluador(rd.getPersonal1().getClave());
                        String clavegenerada = evaluacion + "" + evaluador.getClave() + "" + p.getClave();
                        listadoEvaluacionDesempenio.add(new ListaEvaluacionDesempenio(Integer.parseInt(clavegenerada),evaluacion,
                                evaluador.getClave(), evaluador.getNombre(),
                                rd.getPersonal().getClave(), rd.getPersonal().getNombre(), (int)rd.getPersonal().getAreaOperativa(), 
                                ejbEvaluacion360Combinaciones.getAreaPorClave(rd.getPersonal().getAreaOperativa()).getNombre()));
                    });
                }
            });
        } else {
            Messages.addGlobalWarn("solo debe seleccionar una evaluación.");
        }
    }

    public void validaEdicion(Integer evaluacion) {
        if (evaluacion > 0) {
            evEd = evaluacion;
        } else {
            Messages.addGlobalWarn("Si condsidera que esto es un error, contacte con el administrador");
        }
    }

    public void editaEvaluacion(Integer evaluacion) {
        if (e360b) {
            Evaluaciones360 e3 = ejbEvaluacion360Combinaciones.getEvaluaciones360().stream().filter(e -> e.getEvaluacion().equals(evaluacion)).collect(Collectors.toList()).get(0);
            ejbEvaluacion360Combinaciones.editaEvaluacion360(e3, fechaA, fechaB);
        } else if (edesempenio) {
            DesempenioEvaluaciones eD = ejbEvaluacion360Combinaciones.getEvaluacionesDesempenio().stream().filter(e -> e.getEvaluacion().equals(evaluacion)).collect(Collectors.toList()).get(0);
            ejbEvaluacion360Combinaciones.editaDesempenioEvaluaciones(eD, fechaA, fechaB);
        }
    }
    
    public void validaEdicionCombinaciones360(ListaEvaluacion360Combinaciones combinacion) {
        if (combinacion != null) {
            resultado360 = ejbEvaluacion360Combinaciones.getCombinacion(combinacion.getEvaluacion(), combinacion.getNominaEvaluado(), combinacion.getNominaEvaluador());
        }
    }

    public void validaEdicionCombinacionesDes(ListaEvaluacionDesempenio combinacion) {
        
        resultadosDesempeño = ejbEvaluacion360Combinaciones.getCombinacionDesempenio(combinacion.getEvaluacion(), combinacion.getEvaluado(), combinacion.getEvaluador());
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.AdminEvaluacion360.validaEdicionCombinacionesDes() la combinacion es -->> " + resultadosDesempeño);
    }

    public void agregaCombinacion360() {
        Integer evaluacion = listaCombinacion360.get(0).getEvaluacion();
        ejbEvaluacion360Combinaciones.nuevaCombinacion360(evaluacion, evaluadoAu.getClave(), evaluadorAu.getClave(), categoria.getCategoria(), tipoEvaluacion);
        
    }
    
    public void agregaCombinacionDes() {
        Integer evaluacion = listadoEvaluacionDesempenio.get(0).getEvaluacion();
        ejbEvaluacion360Combinaciones.nuevaCo0mbinacionDesempenio(evaluacion, evaluadoAu.getClave(), evaluadorAu.getClave());
    }
    
    public void editaCombinación() {
        ejbEvaluacion360Combinaciones.editaCombinacion360(resultado360, evaluadoAu.getClave(), evaluadorAu.getClave(), categoria.getCategoria(), tipoEvaluacion);
        listaCombinacion360.clear();
    }
    
    public void editaCombinaciónDesempeño() {
            ejbEvaluacion360Combinaciones.editaCombinacionDesempenio(resultadosDesempeño, evaluadoAu.getClave(), evaluadorAu.getClave());
            listadoEvaluacionDesempenio.clear();
    }
    
    public void eliminaCombinacion360(ListaEvaluacion360Combinaciones combinacion) {
        ejbEvaluacion360Combinaciones.eliminaCombinacion360(combinacion);
    }

    public void eliminaCombinacionDes(ListaEvaluacionDesempenio combinacion) {
        ejbEvaluacion360Combinaciones.eliminaCombinacionDes(combinacion);
    }

    public List<Personal> completePersonal(String query) {
        return listaPersonal.stream().filter(x -> x.getNombre().contains(query.trim())).collect(Collectors.toList());
    }

    public List<PersonalCategorias> completePersonalCategorias(String query) { 
        return listaCategorias.stream().filter(x -> x.getNombre().contains(query.trim())).collect(Collectors.toList());
    }

    public void setE360b(Boolean e360b) {
        this.e360b = e360b;
        this.edesempenio = ! e360b;
    }

    public void setEdesempenio(Boolean edesempenio) {
        this.edesempenio = edesempenio;
        this.e360b = !edesempenio;
    }
    
    //--------------------------------------------------------------------------Herramientas de administracion evaluacion 360//--------------------------------------------------------------------------
    public void listadosAdministracion() {
        try {
            evaluaciones360s = ejbUtilidadesCH.getEvaluaciones360();
            periodosEscolareses = ejbUtilidadesCH.mostrarPeriodosEscolaresEvaluaciones360();
            categorias360 = ejbUtilidadesCH.mostrarListaPersonalCategorias360();
            habilidadeses = ejbUtilidadesCH.mostrarListaHabilidades();
            periodoEv = periodosEscolareses.get(0).getPeriodo();
            periodoActivo = periodoEv;
            categoriasHabilidadeses = ejbUtilidadesCH.mostrarCategoriasHabilidades(periodoEv);
            estatus.add(Boolean.TRUE);
            estatus.add(Boolean.FALSE);
            categorias = new PersonalCategorias();
            habilidades = new Habilidades();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminEvaluacion360.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void numeroProcesoAsiganado(ValueChangeEvent event) {
        try {
            pestaniaActiva = 3;
            periodoEv = Integer.parseInt(event.getNewValue().toString());
            categoriasHabilidadeses = new ArrayList<>();
            categoriasHabilidadeses.clear();
            categoriasHabilidadeses = ejbUtilidadesCH.mostrarCategoriasHabilidades(periodoEv);
            Ajax.update("frmCatHab");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String datosperido(Integer periodo) {
        datosP = "";
        periodosEscolareses.forEach((t) -> {
            if (Objects.equals(periodo, t.getPeriodo())) {
                datosP = t.getPeriodo() + ": " + t.getMesInicio().getMes() + " - " + t.getMesFin().getMes() + " -- " + t.getAnio();
            }
        });
        return datosP;
    }

    public void onRowEditEvaluacion(RowEditEvent event) {
        try {
            evaluaciones360s = new ArrayList<>();
            evaluaciones360s.clear();
            Evaluaciones360 m = (Evaluaciones360) event.getObject();
            ejbUtilidadesCH.actualizarEvaluaciones360(m);
            Messages.addGlobalInfo("¡Operación exitosa!");
            evaluaciones360s = ejbUtilidadesCH.getEvaluaciones360();
            pestaniaActiva = 0;
            Ajax.update("frmev");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditCategorias(RowEditEvent event) {
        try {
            categorias360 = new ArrayList<>();
            categorias360.clear();
            PersonalCategorias m = (PersonalCategorias) event.getObject();
            ejbUtilidadesCH.actualizarNuevoPersonalCategorias(m);
            Messages.addGlobalInfo("¡Operación exitosa!");
            categorias360 = ejbUtilidadesCH.mostrarListaPersonalCategorias360();
            pestaniaActiva = 1;
            Ajax.update("frmCat");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditHabilidades(RowEditEvent event) {
        try {
            habilidadeses = new ArrayList<>();
            habilidadeses.clear();
            Habilidades m = (Habilidades) event.getObject();
            ejbUtilidadesCH.actualizarNuevoHabilidades(m);
            Messages.addGlobalInfo("¡Operación exitosa!");
            habilidadeses = ejbUtilidadesCH.mostrarListaHabilidades();
            pestaniaActiva = 2;
            Ajax.update("frmHab");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearEvluacion360() {
        try {
            List<Evaluaciones360> es = new ArrayList<>();
            es = evaluaciones360s.stream().filter(t -> t.getPeriodo() == periodoEv).collect(Collectors.toList());
            if (!es.isEmpty()) {
                Messages.addGlobalWarn("¡Ya cuenta con una evaluación para este periodo!");
                return;
            }
            evaluaciones360s = new ArrayList<>();
            evaluaciones360s.clear();
            Evaluaciones360 e360 = new Evaluaciones360();
            e360.setFechaFin(fechaF);
            e360.setFechaInicio(fechaI);
            e360.setPeriodo(periodoEv);
            ejbUtilidadesCH.crearNuevaEvaluaciones360(e360);
            evaluaciones360s = ejbUtilidadesCH.getEvaluaciones360();
            pestaniaActiva = 0;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminEvaluacion360.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearCategoria() {
        try {
            categorias360 = new ArrayList<>();
            categorias360.clear();
            categorias.setTipo("Específica");
            categorias.setActivo(Boolean.TRUE);
            ejbUtilidadesCH.crearNuevoPersonalCategorias(categorias);
            categorias360 = ejbUtilidadesCH.mostrarListaPersonalCategorias360();
            categorias = new PersonalCategorias();
            pestaniaActiva = 1;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminEvaluacion360.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearHabilidad() {
        try {
            habilidadeses = new ArrayList<>();
            habilidadeses.clear();
            habilidades.setActivo(Boolean.TRUE);
            ejbUtilidadesCH.crearNuevoHabilidades(habilidades);
            habilidadeses = ejbUtilidadesCH.mostrarListaHabilidades();
            habilidades = new Habilidades();
            pestaniaActiva = 2;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminEvaluacion360.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregaCategoriasHabilidadesEvAnterior() {
        try {
            pestaniaActiva = 3;
            categoriasHabilidadeses = new ArrayList<>();
            categoriasHabilidadeses = ejbUtilidadesCH.mostrarCategoriasHabilidades(periodoEv);
            Evaluaciones360 e = ejbUtilidadesCH.muestraEvaluaciones360();
            if(!periodoEv.equals(e.getPeriodo())){
                Messages.addGlobalError("¡Aún no cuenta con una evaluación para este periodo!");
                return;
            }
            if (categoriasHabilidadeses.isEmpty()) {
                categoriasHabilidadeses = new ArrayList<>();
                categoriasHabilidadeses = ejbUtilidadesCH.mostrarCategoriasHabilidades((periodoEv - 1));
                if (!categoriasHabilidadeses.isEmpty()) {
                    categoriasHabilidadeses.forEach((t) -> {
                        CategoriasHabilidades ch = new CategoriasHabilidades();
                        CategoriasHabilidadesPK chpk = new CategoriasHabilidadesPK();

                        chpk.setCategoria(t.getPersonalCategorias().getCategoria());
                        chpk.setHabilidad(t.getHabilidades().getHabilidad());
                        chpk.setEvaluacion(e.getEvaluacion());

                        ch.setCategoriasHabilidadesPK(new CategoriasHabilidadesPK());
                        ch.setCategoriasHabilidadesPK(chpk);
                        ch.setEvaluaciones360(new Evaluaciones360());
                        ch.setHabilidades(new Habilidades());
                        ch.setPersonalCategorias(new PersonalCategorias());

                        ch.setEvaluaciones360(e);
                        ch.setHabilidades(t.getHabilidades());
                        ch.setPersonalCategorias(t.getPersonalCategorias());

                        try {
                            ejbUtilidadesCH.crearCategoriasHabilidades(ch);
                        } catch (Throwable ex) {
                            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                            Logger.getLogger(AdminEvaluacion360.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
            categoriasHabilidadeses = new ArrayList<>();
            categoriasHabilidadeses = ejbUtilidadesCH.mostrarCategoriasHabilidades(periodoEv);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminEvaluacion360.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregaCategoriasHabilidades() {
        try {
            categorias = new PersonalCategorias();
            if (cvlCat == 0) {
                categorias.setNombre(nombCat);
                categorias.setTipo("Específica");
                categorias.setActivo(Boolean.TRUE);
                categorias = ejbUtilidadesCH.crearNuevoPersonalCategorias(categorias);
            } else {
                categorias.setCategoria(cvlCat);
            }

            habilidades = new Habilidades();
            if (cvlHab == 0) {
                habilidades.setNombre(nombHab);
                habilidades.setActivo(Boolean.TRUE);
                habilidades = ejbUtilidadesCH.crearNuevoHabilidades(habilidades);
            } else {
                habilidades.setHabilidad(cvlHab);
            }

            evaluaciones360CategoriasHablidades = new Evaluaciones360();
            evaluaciones360CategoriasHablidades = ejbUtilidadesCH.getEvaluaciones360Periodo(periodoEv);

            pestaniaActiva = 3;
            CategoriasHabilidades ch = new CategoriasHabilidades();
            CategoriasHabilidadesPK chpk = new CategoriasHabilidadesPK();

            chpk.setCategoria(categorias.getCategoria());
            chpk.setHabilidad(habilidades.getHabilidad());
            chpk.setEvaluacion(evaluaciones360CategoriasHablidades.getEvaluacion());

            ch.setCategoriasHabilidadesPK(new CategoriasHabilidadesPK());
            ch.setCategoriasHabilidadesPK(chpk);
            ch.setEvaluaciones360(new Evaluaciones360());
            ch.setHabilidades(new Habilidades());
            ch.setPersonalCategorias(new PersonalCategorias());

            ch.setEvaluaciones360(evaluaciones360CategoriasHablidades);
            ch.setHabilidades(habilidades);
            ch.setPersonalCategorias(categorias);

            ejbUtilidadesCH.crearCategoriasHabilidades(ch);

            categoriasHabilidadeses = new ArrayList<>();
            categorias360 = new ArrayList<>();
            habilidadeses = new ArrayList<>();

            categorias360 = ejbUtilidadesCH.mostrarListaPersonalCategorias360();
            habilidadeses = ejbUtilidadesCH.mostrarListaHabilidades();
            categoriasHabilidadeses = ejbUtilidadesCH.mostrarCategoriasHabilidades(periodoEv);

            evaluaciones360CategoriasHablidades = new Evaluaciones360();
            habilidades = new Habilidades();
            categorias = new PersonalCategorias();
            cvlHab = 0;
            nombCat = "";
            nombHab = "";
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminEvaluacion360.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaAineacion(CategoriasHabilidades categoriasHabilidades) {
        try {
            ejbUtilidadesCH.eliminarCategoriasHabilidades(categoriasHabilidades);
            categoriasHabilidadeses = new ArrayList<>();
            categoriasHabilidadeses = ejbUtilidadesCH.mostrarCategoriasHabilidades(periodoEv);
            Ajax.update("frmCatHab");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminEvaluacion360.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void imprimirValores() {
    }

}
