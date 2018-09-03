/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.util;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacion360Promedios;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacionDesempenioPromedios;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacionDocenteMateria;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacionTutorPromedios;
import mx.edu.utxj.pye.sgi.dto.ListadoChartEvaluacionDocente;
import mx.edu.utxj.pye.sgi.dto.ListadoGeneralEvaluacionesPersonal;
import mx.edu.utxj.pye.sgi.ejb.EJBEvaluacionDocenteMateria;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacion3601;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacion360Combinaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMaterias;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.ch.ListaEvaluacionDocentesResultados;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
//import mx.edu.utxj.pye.sgi.entity.logueo.Pe;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Calculable;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionDesempenio;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionTutor;
import mx.edu.utxj.pye.sgi.funcional.PromediarDesempenio;
import mx.edu.utxj.pye.sgi.funcional.PromediarTutor;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionDocenteMateriaPye;
import org.highfaces.component.api.impl.DefaultChartModel;
import org.highfaces.component.api.impl.DefaultChartSeries;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

/**
 *
 * @author UTXJ
 */
@Named
@SessionScoped
public class UtilidadesAdministracionEvaluaciones implements Serializable{

    private static final long serialVersionUID = -2555896648653537850L;
    @Getter PeriodosEscolares periodoEscolar;
    
    //grafica
//    @Getter @Setter protected List<ListadoChartEvaluacionDocente> chartCompletas, chartIncompletas, chartTotal;
//    @Getter @Setter protected String selectedPoint;
//    @Getter @Setter protected String selectedSeries;
    @Getter @Setter protected DefaultChartModel model;
    //
        
    @Getter @Setter private Boolean tutor = false, directorDeCarrera = false, directivo = false, evaluacionActiva = false , secretariaAc = false, personal = false, botoneraActiva = false, botoneraActivaPersonal=false;
    @Getter @Setter private Boolean e360 = false, desempenio = false, docente = false, tutores = false, servicios = false, infrestructura = false, egresados = false, controlInterno = false;
    @Getter @Setter private Boolean renderReporte = false, renderReportePersonal = false, renderReporteGenrealPersonal = false;
    @Getter @Setter private Double promedioGeneral, promedioDesempenio;
    @Getter @Setter private Integer evaluacionSeleccionada;
    @Getter @Setter private Integer periodoSeleccionado;
    @Getter @Setter private Short areaEducativa;
    @Getter @Setter private String tipoEvaluacion;
    // select items
    @Getter @Setter private List<SelectItem> selectItemEvaluacionesDirectivo, selectItemEvaluacionesDirectorCarrera,selectItemEvaluacionesPersonal ,
            selectItemEvaluacionesPersonalSA, selectItemsAreasEducativas,
            selectItemPeriodo360, selectItemPeriodoDesempenio, selectItemsEvaluacionesPeriodo, slectItemsEvaluacionDocentePeriodos;
    // dto
    @Getter @Setter private List<ListaEvaluacion360Promedios> lista360Promedios, lista360PromediosPersonal;  
    @Getter @Setter private List<ListaEvaluacionDesempenioPromedios> listaDesempenioPromedios,  listaDesempenioPromediosPersonal;    
    @Getter @Setter private List<ListaEvaluacionTutorPromedios> listaTutoresPromedios;       
    @Getter @Setter private List<ListaEvaluacionDocenteMateria> listaDocentesPromedioGeneral, ListaDocentesPromedioMateria, listadoDocentesPromedioSa, listadoDocentesPromedioSaGeneral;
    @Getter @Setter private List<ListadoChartEvaluacionDocente> listaChartEvaluacionDocentes;
    @Getter @Setter private ListaEvaluacionDocenteMateria SubordinadoSeleccionado ;
    @Getter @Setter private List<ListadoGeneralEvaluacionesPersonal> listaEvaluacionGeneral;
    // entitites
    @Getter @Setter private List<ListaPersonal> ListaSubordinadosAdmin, listaPersonalGeneral;
    @Getter @Setter private List<AreasUniversidad> listaCarreras;
    @Getter @Setter private List<ListaEvaluacionDocentesResultados> listaDeResultadosEvaluacionDocente;
//    @Getter @Setter private List<EvaluacionDocentesMateriaResultados> listaDocentesEvaluadosGeneral;
    
    @Inject LogonMB logonMB;
    @Inject EvaluacionDesempenioAdmin desempenioAdmin;
    @EJB EjbAdministracionEncuestas eJBAdministracionEncuestas;
    @EJB EjbEvaluacion360Combinaciones  eJB360Combinacion;
    @EJB EJBEvaluacionDocenteMateria eJBEvaluacionDocenteMateria;
    @EJB EjbEvaluacion3601 eJBEvaluacion3601;
    @EJB EJBSelectItems eJBSelectItems;
    @PostConstruct
    public void init() {

        if (logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
//            if (!eJBAdministracionEncuestas.esDirectorDeCarrera(Short.parseShort("28"), 2, 18, Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina())).isEmpty()) {
            if(logonMB.getPersonal().getCategoriaOperativa().getCategoria() == 18 && logonMB.getPersonal().getAreaSuperior() == 2){
                directorDeCarrera = true;
                listaCarreras = eJBAdministracionEncuestas.obtenerAreasDirector(desempenioAdmin.getDirectivoSeleccionado().getAreaOperativa(), "Vigente");
               //System.out.println("Lista de carreras del directivo" + listaCarreras);
               //System.out.println("lista de carreras");
                listaCarreras.forEach(System.err::println);
                selectItemEvaluacionesDirectorCarrera = eJBSelectItems.itemsEvaluacionDirectores();
                ListaSubordinadosAdmin = desempenioAdmin.getListaSubordinados();
            } else if (/*!directorDeCarrera &&*/logonMB.getPersonal().getActividad().getActividad() == 4 || logonMB.getPersonal().getActividad().getActividad() == 2) {
               //System.out.println("directivo o coordinador");
                selectItemEvaluacionesDirectivo = eJBSelectItems.itemsEvaluacionesDirectivos();
                ListaSubordinadosAdmin = desempenioAdmin.getListaSubordinados();
                if (logonMB.getPersonal().getCategoriaOperativa().getCategoria() == 38) {
                    directivo = true;
                    secretariaAc = true;
                    selectItemEvaluacionesPersonalSA = eJBSelectItems.itemsEvaluacionSecretariaAcademica();
                    selectItemsAreasEducativas = eJBSelectItems.itemAreasAcademicas();
                   //System.out.println("Secretaria Academica");
                    renderReporte = false;
                    renderReportePersonal = false;
                } else if (logonMB.getPersonal().getAreaOperativa() == 13 && logonMB.getPersonal().getCategoriaOperativa().getCategoria() == 24) {
                    directivo = true;
                    personal = true;
                    selectItemEvaluacionesPersonal = eJBSelectItems.itemsEvaluacionPersonal();
                   //System.out.println("Jefe de personal");
                    botoneraActivaPersonal = true;
                    renderReportePersonal = false;
//                    listaPersonalGeneral = eJBAdministracionEncuestas.personalGeneral();
                } else {
                    directivo = true;
                }
            } else if (eJBAdministracionEncuestas.esTutor(logonMB.getUsuarioAutenticado().getUsuariosPK().getCvePersona(), eJBAdministracionEncuestas.getPeriodoActual().getPeriodo()) == null) {
                tutor = false;
            } else if (!eJBAdministracionEncuestas.esTutor(logonMB.getUsuarioAutenticado().getUsuariosPK().getCvePersona(), eJBAdministracionEncuestas.getPeriodoActual().getPeriodo()).isEmpty()) {
                tutor = true;
            }
        }
        if (eJBEvaluacionDocenteMateria.evaluacionActiva() != null) {
            evaluacionActiva = true;
        }
//        e360 = true;
//        selectItemPeriodoDesempenio = eJBSelectItems.itemPeriodosDesempenio();
        selectItemPeriodo360 = eJBSelectItems.itemsPeriodos360();
        periodoEscolar= eJBAdministracionEncuestas.getPeriodoActual();
    }

    public void obtenerPeriodosPorEvaluacion() {
        periodoSeleccionado = 0;
        switch (evaluacionSeleccionada) {
            case 1:
                tipoEvaluacion = "360°";
                e360 = true;
                desempenio = false;
                tutores = false;
                docente = false;
                renderReporte = false;
                renderReportePersonal= false;
                selectItemPeriodo360 = eJBSelectItems.itemsPeriodos360();
                break;
            case 2:
                tipoEvaluacion = "Desempenio";
                e360 = false;
                desempenio = true;
                tutores = false;
                docente = false;
                renderReporte = false;
                renderReportePersonal = false;
                selectItemPeriodoDesempenio = eJBSelectItems.itemPeriodosDesempenio();
                break;
            case 3:
                tipoEvaluacion = "Tutor";
                e360 = false;
                desempenio = false;
                tutores = true;
                renderReporte = false;
                docente = false;
                renderReportePersonal= false;
                selectItemsEvaluacionesPeriodo = eJBSelectItems.itemPeriodosEvaluaciones(tipoEvaluacion);
                break;
            case 4:
                tipoEvaluacion = "Docente";
                e360 = false;
                desempenio = false;
                tutores = false;
                docente = true;
                renderReportePersonal = false;
                slectItemsEvaluacionDocentePeriodos = eJBSelectItems.itemPeriodosDocenteMateria();
                break;
        }
    }

    public void renderizarEncuestas() {
        if (directivo) {
            switch (evaluacionSeleccionada) {
                case 1:
                    tipoEvaluacion = "360°";
                    obtenerResultados360();
                    break;
                case 2:
                    tipoEvaluacion = "Desempenio";
                    obtenerResultadosdesempenio();
                    break;
            }
        } else if (directorDeCarrera) {
            switch (evaluacionSeleccionada) {
                case 1:
                    tipoEvaluacion = "360°";
                    obtenerResultados360();
                    break;
                case 2:
                    tipoEvaluacion = "Desempenio";
                    obtenerResultadosdesempenio();
                    break;
                case 3:
                    tipoEvaluacion = "Tutor";
                    obtenerresultadosTutores();
                    break;
                case 4:
                    tipoEvaluacion = "Docente";
                    obtenerResultadosEvaluacionDocente();
                    break;
            }
        } else if (personal) {
            switch (evaluacionSeleccionada) {
                case 1:
                    tipoEvaluacion = "360°";
                    obtenerResultados360();
                    break;
                case 2:
                    tipoEvaluacion = "Desempenio";
                    obtenerResultadosdesempenio();
                    break;
                case 4:
                    tipoEvaluacion = "Docente";
                    obtenerResultadosEvaluacionDocente();
                   //System.out.println("docente");
                    break;
            }
        } else if (secretariaAc) {
            switch (evaluacionSeleccionada) {
                case 4:
                    tipoEvaluacion = "Docente";
                    obtenerResultadosEvaluacionDocente();
                   //System.out.println("docente");
                    break;
            }
        }
    }

    /**
     * 1.- Obtiene la evaluacion de la cual se obtendran los resultados, esta
     * evaluacion es previamente seleccionada en la interfaz 2.- se obtiene la
     * lista de subordinados del directivo logueado 3.- se recorre la lista de
     * subordinados, por cada uno de los subordinados se obtienen los resultados
     * de la evaluacion y se obtiene el promedio mediante una expresion lambda
     * verificando si los resultados de la evaluacion existen 4.- se inicializa
     * un DTO y se agregan los datos a la una lista del tipo del DTO para
     * posteriormente usarse en la vista
     */
    public void obtenerResultados360() {
        lista360Promedios = new ArrayList<>();
        Evaluaciones360 evaluacionObtenida = eJBAdministracionEncuestas.getEvaluacion360Administracion(periodoSeleccionado);

        ListaSubordinadosAdmin.forEach(subordinado -> {
            List<Evaluaciones360Resultados> l = eJBAdministracionEncuestas.getEvaluaciones360ResultadosSubordinados(evaluacionObtenida, subordinado.getClave());
            if (l != null) {
                Double suma = l.stream().collect(Collectors.summingDouble(Evaluaciones360Resultados::getPromedio));
                Double promedio = suma / l.size();
                ListaEvaluacion360Promedios le = new ListaEvaluacion360Promedios(subordinado.getClave(), subordinado.getNombre(), subordinado.getAreaOperativaNombre(), subordinado.getCategoriaOperativaNombre(), promedio);
                lista360Promedios.add(le);
            } else {
               //System.out.println("el subordinado : " + subordinado.getNombre() + ", no fue evaluado en ese periodo");
            }
        });

    }

    /**
     * 1.- Obtiene la evaluacion de la cual se obtendran los resultados, esta
     * evaluacion es previamente seleccionada en la interfaz 2.- se obtiene la
     * lista de subordinados del directivo logueado 3.- se recorre la lista de
     * subordinados, por cada uno de los subordinados se obtienen los resultados
     * de la evaluacion , estos se comparan mediante la interfaz funcional
     * comparador, y se obtiene el promedio mediante la interfaz funcional
     * Calculable y una expresion lambda verificando antes si los resultados de
     * la evaluacion existen 4.- se inicializa un DTO y se agregan los datos a
     * la una lista del tipo del DTO para posteriormente usarse en la vista
     */
    public void obtenerResultadosdesempenio() {
        listaDesempenioPromedios = new ArrayList<>();
        DesempenioEvaluaciones evaluacionDesempenioObtenida = eJBAdministracionEncuestas.getEvaluacionDesempenioAdministracion(periodoSeleccionado);
        Comparador<DesempenioEvaluacionResultados> comparador = new ComparadorEvaluacionDesempenio();
        Calculable<DesempenioEvaluacionResultados> obtener = new PromediarDesempenio();
        ListaSubordinadosAdmin.forEach(subordinado -> {
            List<DesempenioEvaluacionResultados> l = eJBAdministracionEncuestas.getEvaluacionesDesempenioSubordinados(evaluacionDesempenioObtenida.getEvaluacion(), subordinado.getClave());
            if (l != null) {
                l.stream().filter(encuesta -> comparador.isCompleto(encuesta)).collect(Collectors.toList()).forEach(x -> {
                    Double promedio = obtener.promediar(x);
                    ListaEvaluacionDesempenioPromedios ldp = new ListaEvaluacionDesempenioPromedios(subordinado.getClave(), subordinado.getNombre(), subordinado.getAreaOperativaNombre(), subordinado.getCategoriaOperativaNombre(), promedio);
                    listaDesempenioPromedios.add(ldp);
                });
            } else {
               //System.out.println("El subordinado : " + subordinado.getNombre() + " no fue evaluado en este periodo");
            }
        });
    }

    /**
     * 1.- Obtiene la evaluacion de la cual se obtendran los resultados, esta
     * evaluacion es previamente seleccionada en la interfaz 2.- se obtiene la
     * lista de subordinados del directivo logueado 3.- se recorre la lista de
     * subordinados, por cada uno de los subordinados se obtienen los resultados
     * de la evaluacion , estos se comparan mediante la interfaz funcional
     * comparador, y se obtiene el promedio mediante la interfaz funcional
     * Calculable , ademas se suma el promedio de cada una de las evaluaciones a
     * las que el subordinado fue asignado y se divide entre el numero de
     * evaluaciones que este tuvo todo dentro una expresion lambda, verificando
     * antes si los resultados de la evaluacion existen 4.- se inicializa un DTO
     * y se agregan los datos a la una lista del tipo del DTO para
     * posteriormente usarse en la vista
     */
    public void obtenerresultadosTutores() {
        listaTutoresPromedios = new ArrayList<>();
        Evaluaciones evaluacionObtenida = eJBAdministracionEncuestas.getEvaluaciones(periodoSeleccionado, tipoEvaluacion);
        Comparador<EvaluacionesTutoresResultados> comparador = new ComparadorEvaluacionTutor();
        Calculable<EvaluacionesTutoresResultados> obtener = new PromediarTutor();
        ListaSubordinadosAdmin.forEach(subordinado -> {
            List<EvaluacionesTutoresResultados> l = eJBAdministracionEncuestas.getEvaluacionesTutoresResultados(evaluacionObtenida, subordinado.getClave());
            if (l != null) {
                promedioGeneral = 0d;
                l.stream().filter(evaluacion -> comparador.isCompleto(evaluacion)).collect(Collectors.toList()).forEach(x -> {
                    Double promedios = obtener.promediar(x);
                    promedioGeneral += promedios / l.size();
                });
                ListaEvaluacionTutorPromedios ldp = new ListaEvaluacionTutorPromedios(subordinado.getClave(), subordinado.getNombre(), subordinado.getAreaOperativaNombre(), subordinado.getCategoriaOperativaNombre(), promedioGeneral);
                listaTutoresPromedios.add(ldp);
            } else {
               //System.out.println("la persona : " + subordinado.getNombre() + " no es tutor o no fue evaluada en este periodo");
            }
        });
    }

    /**
     *     //////////Obtiene promedio general del docente//////////// 1.- Obtiene
     * la evaluacion de la cual se obtendran los resultados, esta evaluacion es
     * previamente seleccionada en la interfaz 2.- se obtiene la lista de
     * subordinados del directivo logueado 3.- se recorre la lista de
     * subordinados, por cada uno de los subordinados se obtienen los resultados
     * de la evaluacion , estos se comparan mediante la interfaz funcional
     * comparador y una expresion lambda verificando antes si los resultados de
     * la evaluacion existen 4.- se inicializa un DTO y se agregan los datos a
     * la una lista del tipo del DTO para posteriormente usarse en la vista
     */
    public void obtenerResultadosEvaluacionDocente() {
        listaDocentesPromedioGeneral = new ArrayList<>();
        EvaluacionDocentesMaterias evaluacionObtenida = eJBAdministracionEncuestas.getEvaluacionDoncete(periodoSeleccionado);
        ListaSubordinadosAdmin.forEach(subordinado -> {
            List<EvaluacionDocentesMateriaResultados> l = eJBAdministracionEncuestas.getEvaluacionDocentesResultadosPromedioGeneral(evaluacionObtenida, subordinado.getClave());
            if (l != null) {
                List<EvaluacionDocentesMateriaResultados> listaCompletos = l.stream().filter(x -> x.getCompleto()).collect(Collectors.toList());
                Double suma = listaCompletos.stream().collect(Collectors.summingDouble(EvaluacionDocentesMateriaResultados::getPromedio));
                Double promedio = suma / listaCompletos.size();
                ListaEvaluacionDocenteMateria ld = new ListaEvaluacionDocenteMateria(subordinado.getClave(), subordinado.getNombre(), subordinado.getAreaOperativaNombre(), subordinado.getCategoriaOperativaNombre(), tipoEvaluacion, "", promedio);
                listaDocentesPromedioGeneral.add(ld);
            } else {
               //System.out.println("el subordinado : " + subordinado.getNombre() + ", no fue evaluado en ese periodo");
            }
        });
        obtenerDatosEvaluacionDocente();
    }

    /**
     * Obtendrá el registro de materias por docente y el promedio segun sea el
     * area y el docente
     *
     * @param clave la clave del docente obtenida en la interfaz grafica
     * (Datatable)
     */
    public void obtenerResultadoEvaluacionDocentePorMateria(ListaEvaluacionDocenteMateria clave) {
        SubordinadoSeleccionado = clave;
        ListaDocentesPromedioMateria = new ArrayList<>();
        List<VistaEvaluacionDocenteMateriaPye> listaMaterias = eJBAdministracionEncuestas.getMateriasPorDocente(clave.getClave(), periodoSeleccionado);
       //System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones.obtenerResultadoEvaluacionDocentePorMateria() la lista de materias es "+ listaMaterias);
       //System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones.obtenerResultadoEvaluacionDocentePorMateria() el periodo seleccionado es : " + periodoSeleccionado);
        EvaluacionDocentesMaterias evaluacion = eJBAdministracionEncuestas.getEvaluacionDoncete(periodoSeleccionado);
       //System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones.obtenerResultadoEvaluacionDocentePorMateria() la evaluacion es :" + evaluacion);
        Map<String, Long> lm = listaMaterias.stream().collect(
                Collectors.groupingBy(VistaEvaluacionDocenteMateriaPye::getCveMateria, Collectors.counting()));
        List<String> rLM = lm.keySet().stream().collect(Collectors.toList());
       //System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones.obtenerResultadoEvaluacionDocentePorMateria() ,aterias");
        rLM.forEach(System.err::println);
        rLM.forEach(mat -> {
           //System.out.println("la materia en cuestion");
            List<EvaluacionDocentesMateriaResultados> lpm = eJBAdministracionEncuestas.getEvaluacionDocentesResultadosPromedioMateria(evaluacion, clave.getClave(), mat);
            List<EvaluacionDocentesMateriaResultados> listaCompletos = lpm.stream().filter(x -> x.getCompleto()).collect(Collectors.toList());
            Double promedioMateria = listaCompletos.stream().collect(Collectors.summingDouble(EvaluacionDocentesMateriaResultados::getPromedio)) / listaCompletos.size();
            List<VistaEvaluacionDocenteMateriaPye> listaMateriaSeleccionada = listaMaterias.stream().filter(m -> m.getCveMateria().equals(mat)).collect(Collectors.toList());
            VistaEvaluacionDocenteMateriaPye materiaSeleccionada = listaMateriaSeleccionada.get(0);
           //System.out.println("La lista de materias es : # ");            
            listaMateriaSeleccionada.forEach(System.err::println);
            listaCarreras.forEach(c -> {
               //System.out.println("las siglas de c son: " +c.getSiglas());
               //System.out.println("las siglas de materia seleccionada son : "+ materiaSeleccionada.getSiglas());
                if (materiaSeleccionada.getSiglas().equals(c.getSiglas())) {
                    
                    ListaEvaluacionDocenteMateria evaluacionMateria = new ListaEvaluacionDocenteMateria(clave.getClave(), clave.getNombre(), clave.getAreaOperativa(), clave.getCategoriaOperativa(), mat, materiaSeleccionada.getNombreMateria(), promedioMateria);
                   //System.out.println("la evaluacion de la materia : " + evaluacionMateria);
                    ListaDocentesPromedioMateria.add(evaluacionMateria);
                } else {
                   //System.out.println("la materia : " + materiaSeleccionada.getNombreMateria() + " pertenece a otra area");
                }
            });
        });
       //System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones.obtenerResultadoEvaluacionDocentePorMateria() las evaluaciones son ");
       //System.out.println("---");
        ListaDocentesPromedioMateria.forEach(System.err::println);

    }

    /**
     * Obtiene los resultados de la evaluacion docente para el tutor Descripcion
     * : este metodo obtiene un listado de alumnos para un docente mientras este
     * sea tutor de grupo, obtiene tres resultados diferentes, los estudiantes
     * que completaron la envaluacion docente, los estudiantes que no
     * completaron su evaluacion docente y los etudiantes que no accesaron al
     * sistema de encuestas
     */
    public void obtenerDatosEvaluacionDocente() {
        listaCarreras = new ArrayList<>();
        EvaluacionDocentesMaterias evaluacionDocente = eJBAdministracionEncuestas.getEvaluacionDoncete(periodoSeleccionado);
        listaDeResultadosEvaluacionDocente = new ArrayList<>();
        desempenioAdmin.getDirectivoSeleccionado();
        Short area = desempenioAdmin.getDirectivoSeleccionado().getAreaOperativa();
        listaCarreras = eJBAdministracionEncuestas.obtenerAreasDirector(area, "Vigente");
        listaCarreras.stream().forEach(c -> {
            listaDeResultadosEvaluacionDocente.addAll(eJBAdministracionEncuestas.resultadosEvaluacionGlobalDirector(c.getSiglas(), evaluacionDocente.getEvaluacion()));
        });
    }

    /**
     * este metodo obtiene los resultados de las evaluaciones docente generales
     * Descripcion: obtiene los resultados de la evaluacion docente dependiendo
     * de los filtors de la interfaz grafica, esto quiere decir que conocera el
     * resultado de la evaluacion docente por cada uno de los docentes adscritos
     * a las areas correspondientes
     */
    public void resultadosSA() {
        listadoDocentesPromedioSa = new ArrayList<>();
        
        EvaluacionDocentesMaterias evaluacionDocenteSeleccionada = eJBAdministracionEncuestas.getEvaluacionDoncete(periodoSeleccionado);
       //System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones.resultadosSA() el periodo : " + periodoSeleccionado);
       //System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones.resultadosSA() la evaluacuion : " + evaluacionDocenteSeleccionada);
        List<ListaPersonal> lp = eJBAdministracionEncuestas.getListadoDocentesPorArea(areaEducativa);
       //System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones.resultadosSA() el tamaño de la lista : " + lp.size());
        lp.forEach(docente -> {
           //System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones.resultadosSA() entra al for each");
            List<EvaluacionDocentesMateriaResultados> l = eJBAdministracionEncuestas.getEvaluacionDocentesResultadosPromedioGeneral(evaluacionDocenteSeleccionada, docente.getClave());
            if (l != null) {
               //System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones.resultadosSA() no es null");
                List<EvaluacionDocentesMateriaResultados> listaCompletos = l.stream().filter(x -> x.getCompleto()).collect(Collectors.toList());
                Double suma = listaCompletos.stream().collect(Collectors.summingDouble(EvaluacionDocentesMateriaResultados::getPromedio));
                Double promedio = suma / listaCompletos.size();
                ListaEvaluacionDocenteMateria ld = new ListaEvaluacionDocenteMateria(docente.getClave(), docente.getNombre(), docente.getAreaOperativaNombre(), docente.getCategoriaOperativaNombre(), tipoEvaluacion, "", promedio);
                listadoDocentesPromedioSa.add(ld);
            } else {
               //System.out.println("el docente : " + docente.getNombre() + ", no fue evaluado en ese periodo");
            }
        });
        botoneraActiva = true;
//        resultadosSAGeneral();
        obtenerDatosGrafica();
    }

    public void resultadosSAGeneral() {
        listadoDocentesPromedioSaGeneral = new ArrayList<>();
        listadoDocentesPromedioSaGeneral.clear();
       //System.out.println("Evaluaciones generales");
        if (!docente) {
            Messages.addFlashGlobalWarn("Seleccione una evaluacion");
        } else if (periodoSeleccionado < 0) {
            Messages.addFlashGlobalWarn("Seleccione un periodo");
        } else {
            EvaluacionDocentesMaterias evaluacionObtenida = eJBAdministracionEncuestas.getEvaluacionDoncete(periodoSeleccionado);
            List<ListaPersonal> lpG = eJBAdministracionEncuestas.personalGeneral();
            lpG.forEach(persona -> {
                List<EvaluacionDocentesMateriaResultados> l = eJBAdministracionEncuestas.getEvaluacionDocentesResultadosPromedioGeneral(evaluacionObtenida, persona.getClave());
                if (l != null) {
                    List<EvaluacionDocentesMateriaResultados> listaCompletos = l.stream().filter(x -> x.getCompleto()).collect(Collectors.toList());

                    Double suma = listaCompletos.stream().collect(Collectors.summingDouble(EvaluacionDocentesMateriaResultados::getPromedio));
                    Double promedio = suma / listaCompletos.size();
                   //System.out.println("promedio general --> " + promedio + " perteneciente a " + persona.getNombre());
                    ListaEvaluacionDocenteMateria ld = new ListaEvaluacionDocenteMateria(persona.getClave(), persona.getNombre(), persona.getAreaOperativaNombre(), persona.getCategoriaOperativaNombre(), tipoEvaluacion, "", promedio);
                    listadoDocentesPromedioSaGeneral.add(ld);
                    renderReporte = true;
                } else {
                   //System.out.println("el subordinado : " + persona.getNombre() + ", no fue evaluado en ese periodo");
                }
            });
        }

    }

    public void obtenerDatosGrafica() {
       //System.out.println("imprimir el area _ " + areaEducativa);
        if (areaEducativa == 47 || areaEducativa == 999) {
           //System.out.println("Esta area no pertenece a un area educativa en especifico ....");
        } else {
            listaCarreras = new ArrayList<>();
            listaChartEvaluacionDocentes = new ArrayList<>();
            EvaluacionDocentesMaterias evaluacionDocente = eJBAdministracionEncuestas.getEvaluacionDoncete(periodoSeleccionado);
           //System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones.obtenerDatosGrafica() el periodo seleccionado es : " + periodoSeleccionado);
            listaCarreras = eJBAdministracionEncuestas.obtenerAreasDirector(areaEducativa, "Vigente");
           //System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones.obtenerDatosGrafica() la lista de carreras es : " + listaCarreras);
            listaCarreras.stream().forEach(c -> {
               //System.out.println("Controlador la carrera en cuestion es :  " + c.getNombre());
                listaDeResultadosEvaluacionDocente = new ArrayList<>();
               //System.out.println("siglas : " + c.getSiglas() + " evaluacion : " + evaluacionDocente.getEvaluacion());
                List<ListaEvaluacionDocentesResultados> l = eJBAdministracionEncuestas.resultadosEvaluacionGlobalDirector(c.getSiglas(), evaluacionDocente.getEvaluacion());
                if (l.isEmpty() || l == null) {
                   //System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones.obtenerDatosGrafica() null");
                } else {
                    listaDeResultadosEvaluacionDocente.addAll(l);
                }
                if (listaDeResultadosEvaluacionDocente == null || listaDeResultadosEvaluacionDocente.isEmpty()) {
                   //System.out.println("mx.edu.utxj.pye.sgi.funcional.UtilidadesAdministracionEvaluaciones.obtenerDatosGrafica() lista vacia" + listaDeResultadosEvaluacionDocente);
                } else if (!listaDeResultadosEvaluacionDocente.isEmpty()) {
                   //System.out.println("pasa hasta el llenado del dto ");
                    Long completos = (listaDeResultadosEvaluacionDocente.stream().filter(x -> x.getEstatus().equalsIgnoreCase("Finalizado")).count());
                   //System.out.println("loc ompletos son : " + completos);
                    Long incompletos = listaDeResultadosEvaluacionDocente.stream().filter(x -> x.getEstatus().equalsIgnoreCase("Incompleto")).count();
                   //System.out.println("los incompletos son : " + incompletos);
                    Long total = completos + incompletos;
                    ListadoChartEvaluacionDocente chartDato = new ListadoChartEvaluacionDocente(
                            c.getSiglas(), Integer.parseInt(completos.toString()), Integer.parseInt(incompletos.toString()), Integer.parseInt(total.toString()));
                   //System.out.println("el dto llenado es :");
                   //System.out.println(chartDato);
                    listaChartEvaluacionDocentes.add(chartDato);
                   //System.out.println("el tamaño de la lista es : " + listaChartEvaluacionDocentes.size());
                }
            });
           //System.out.println("lista de dato por carrera : " + listaChartEvaluacionDocentes);
            if (listaChartEvaluacionDocentes.isEmpty() || listaChartEvaluacionDocentes == null) {
               //System.out.println("NO HAY DATOS EN LA LISTA :::::::::::::");
            } else {
               //System.out.println("el tamaño de la lista es : " + listaChartEvaluacionDocentes.size());
               //System.out.println("GRAFICA");
                graficar();
            }

        }
    }

    public void reload() throws IOException {
       //System.out.println("Recarga pagina");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    /**
     * abre modal que muestra la gráfica de resultados de evaluaciones
     * respondidas por los estudiantes dependiendo del área que se selecciona
     */
    public void vistaModalChart() {
       //System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones.vistaModalChart() llega al modal de  chart");
        Map<String, Object> modalChart = new HashMap<>();
        modalChart.put("resize", false);
        modalChart.put("modal", true);
        modalChart.put("width", 750);
        modalChart.put("height", 450);
        modalChart.put("draggable", false);
        modalChart.put("contentWidth", "100%");
        modalChart.put("contentHeight", "100%");
        modalChart.put("headerElement", "Gráfica de respuesta");
        RequestContext.getCurrentInstance().openDialog("chartSA", modalChart, null);
    }
    /**
     * gráfica según sea el área seleccionada previamente en los filtros de la
     * interfaz grafica
     */
    public void graficar() {
        model = new DefaultChartModel();
        DefaultChartSeries serieCompletados = new DefaultChartSeries();
        DefaultChartSeries serieIncompletos = new DefaultChartSeries();
        DefaultChartSeries serieTotal = new DefaultChartSeries();
        serieCompletados.setName("Completos");
        serieIncompletos.setName("Incompletos");
        serieTotal.setName("Total");
       //System.out.println("lista de evaluaciones _ " + listaChartEvaluacionDocentes);
               
        listaChartEvaluacionDocentes.forEach(x -> {
           //System.out.println("el registro en cuestionn  : "  + x);
            serieCompletados.addPoint(x.getSiglas(), x.getTerminadas());
            serieIncompletos.addPoint(x.getSiglas(), x.getIncompleta());
            serieTotal.addPoint(x.getSiglas(), x.getTotal());
        });
        model.getSeries().add(serieCompletados);
        model.getSeries().add(serieIncompletos);
        model.getSeries().add(serieTotal);
    }

    /**
     * apartado de la administración del personal en este apartado se
     * visualizaran las diferentes tablas con el reporte de la evaluación
     * seleccionada en el periodo elegido
     */
    public void resultadosPersonal() {
        listaPersonalGeneral = new ArrayList<>();
        listaPersonalGeneral = eJBAdministracionEncuestas.personalGeneralCompleta();
        switch (evaluacionSeleccionada) {
            case 1:
                resultadoEvaluacion360Personal();
                break;
            case 2:
                resultadoEvaluacionDesempenioPersonal();
                break;
            case 4:
                resultadoEvaluacionDocentePersonal();
                break;
        }
    }

    public void resultadoEvaluacion360Personal() {
        lista360PromediosPersonal = new ArrayList<>();
        Evaluaciones360 evaluacionObtenida = eJBAdministracionEncuestas.getEvaluacion360Administracion(periodoSeleccionado);
        listaPersonalGeneral.forEach(personal -> {
            List<Evaluaciones360Resultados> l = eJBAdministracionEncuestas.getEvaluaciones360ResultadosSubordinados(evaluacionObtenida, personal.getClave());
            if (l != null) {
                Double suma = l.stream().collect(Collectors.summingDouble(Evaluaciones360Resultados::getPromedio));
                Double promedio = suma / l.size();
                ListaEvaluacion360Promedios le = new ListaEvaluacion360Promedios(personal.getClave(), personal.getNombre(), personal.getAreaOperativaNombre(), personal.getCategoriaOperativaNombre(), promedio);
                lista360PromediosPersonal.add(le);
            } else {
               //System.out.println("El empleado -> : " + personal.getNombre() + ", no fue evaluado en ese periodo");
            }
        });
        lista360PromediosPersonal.forEach(System.out::println);
    }

    public void resultadoEvaluacionDesempenioPersonal() {
        listaDesempenioPromediosPersonal = new ArrayList<>();
        DesempenioEvaluaciones evaluacionDesempenioObtenida = eJBAdministracionEncuestas.getEvaluacionDesempenioAdministracion(periodoSeleccionado);
        Comparador<DesempenioEvaluacionResultados> comparador = new ComparadorEvaluacionDesempenio();
        Calculable<DesempenioEvaluacionResultados> obtener = new PromediarDesempenio();
        listaPersonalGeneral.forEach(personal -> {
            List<DesempenioEvaluacionResultados> l = eJBAdministracionEncuestas.getEvaluacionesDesempenioSubordinados(evaluacionDesempenioObtenida.getEvaluacion(), personal.getClave());
            if (l != null) {
                l.stream().filter(encuesta -> comparador.isCompleto(encuesta)).collect(Collectors.toList()).forEach(x -> {
                    Double promedio = obtener.promediar(x);
                    ListaEvaluacionDesempenioPromedios ldp = new ListaEvaluacionDesempenioPromedios(personal.getClave(), personal.getNombre(), personal.getAreaOperativaNombre(), personal.getCategoriaOperativaNombre(), promedio);
                    listaDesempenioPromediosPersonal.add(ldp);
                });
            } else {
               //System.out.println("El empleado : " + personal.getNombre() + " no fue evaluado en este periodo");
            }
        });
    }

    public void resultadoEvaluacionDocentePersonal() {
        listadoDocentesPromedioSaGeneral = new ArrayList<>();
        listadoDocentesPromedioSaGeneral.clear();
            EvaluacionDocentesMaterias evaluacionObtenida = eJBAdministracionEncuestas.getEvaluacionDoncete(periodoSeleccionado);
            listaPersonalGeneral.forEach(persona -> {
                List<EvaluacionDocentesMateriaResultados> l = eJBAdministracionEncuestas.getEvaluacionDocentesResultadosPromedioGeneral(evaluacionObtenida, persona.getClave());
                if (l != null) {
                    List<EvaluacionDocentesMateriaResultados> listaCompletos = l.stream().filter(x -> x.getCompleto()).collect(Collectors.toList());
                    Double suma = listaCompletos.stream().collect(Collectors.summingDouble(EvaluacionDocentesMateriaResultados::getPromedio));
                    Double promedio = suma / listaCompletos.size();
                    ListaEvaluacionDocenteMateria ld = new ListaEvaluacionDocenteMateria(persona.getClave(), persona.getNombre(), persona.getAreaOperativaNombre(), persona.getCategoriaOperativaNombre(), tipoEvaluacion, "", promedio);
                    listadoDocentesPromedioSaGeneral.add(ld);
                    renderReportePersonal = true;
                } else {
                   //System.out.println("el subordinado : " + persona.getNombre() + ", no fue evaluado en ese periodo");
                }
            });
    }
    /**
     * Obtiene los resultados de la ultima evaluación por cada integrante de la 
     * plantilla de personal
     * @throws java.io.IOException
     */
    public void personalEvaluadoResultados() throws IOException{
        // obtener evaluaciones 
        listaEvaluacionGeneral = new ArrayList<>();
        listaPersonalGeneral = new ArrayList<>();
        listaPersonalGeneral = eJBAdministracionEncuestas.personalGeneralCompleta();
        Integer periodoActual =eJBAdministracionEncuestas.getPeriodoActual().getPeriodo();
        Integer periodoAnterior =  periodoActual - 1;
        Evaluaciones360 evaluacion360 = eJBAdministracionEncuestas.getEvaluacion360Administracion(periodoAnterior);
        DesempenioEvaluaciones evaluacionDesempenio = eJBAdministracionEncuestas.getEvaluacionDesempenioAdministracion(periodoAnterior);
        EvaluacionDocentesMaterias evaluacionDocente = eJBAdministracionEncuestas.getEvaluacionDoncete(periodoAnterior);
        listaPersonalGeneral.forEach(persona -> {
            Integer sumatoriaPromedios = 0;
            Double promedioDocente, promedio360, promedioGeneralPersonal, promedioB10;
            Comparador<DesempenioEvaluacionResultados> comparador = new ComparadorEvaluacionDesempenio();
            Calculable<DesempenioEvaluacionResultados> obtener = new PromediarDesempenio();
            List<EvaluacionDocentesMateriaResultados> lDoc = eJBAdministracionEncuestas.getEvaluacionDocentesResultadosPromedioGeneral(evaluacionDocente, persona.getClave());
            if (lDoc != null) {
                List<EvaluacionDocentesMateriaResultados> listaCompletos = lDoc.stream().filter(x -> x.getCompleto()).collect(Collectors.toList());
                promedioDocente = listaCompletos.stream().collect(Collectors.summingDouble(EvaluacionDocentesMateriaResultados::getPromedio)) / listaCompletos.size();
            } else {
                promedioDocente = 0.0;
            }
            List<Evaluaciones360Resultados> l360 = eJBAdministracionEncuestas.getEvaluaciones360ResultadosSubordinados(evaluacion360, persona.getClave());
            if (l360 != null) {
                Double promedioBase360 = l360.stream().collect(Collectors.summingDouble(Evaluaciones360Resultados::getPromedio)) / l360.size();
                promedio360 = (promedioBase360/4)*5;
            } else {
                promedio360 = 0.0;
            }
            List<DesempenioEvaluacionResultados> lDes = eJBAdministracionEncuestas.getEvaluacionesDesempenioSubordinados(evaluacionDesempenio.getEvaluacion(), persona.getClave());
            if (lDes != null) {
                lDes.stream().filter(eDesempenio -> comparador.isCompleto(eDesempenio)).collect(Collectors.toList()).forEach(x -> {
                    promedioDesempenio = obtener.promediar(x);
                });
            } else {
                 promedioDesempenio = 0.0;
            }
            if(promedioDocente == 0.0){
                 promedioGeneralPersonal = (promedio360 + promedioDesempenio)/2;
            }else{
                promedioGeneralPersonal = (promedio360 + promedioDesempenio + promedioDocente)/3;
            }
            ListadoGeneralEvaluacionesPersonal evaluacionGeneral = new ListadoGeneralEvaluacionesPersonal(persona.getClave(), persona.getNombre(), persona.getAreaOperativaNombre() , persona.getCategoriaOperativaNombre(), promedio360, promedioDesempenio, promedioDocente,promedioGeneralPersonal);
            listaEvaluacionGeneral.add(evaluacionGeneral);
        });
        renderReporteGenrealPersonal = true;
        reload();
    }
}
