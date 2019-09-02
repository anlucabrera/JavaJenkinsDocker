/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

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
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacion3601;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360ResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonalEvaluacion360;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonalEvaluacion360Promedios;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonalEvaluacion360Reporte;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ExcelWritter;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.primefaces.component.selectonemenu.SelectOneMenu;

/**
 *
 * @author UTXJ
 */
@Named
@SessionScoped
public class Evaluacion360Admin1 implements Serializable {
    
    private static final long serialVersionUID = -2104785351397760456L;

    @Getter private final Integer maxEvaluando = 4;
    @Getter private Integer evaluados = 0;
    @Getter private Integer evaluando = 0;
    @Getter private Boolean cargada = false;

    @Getter private Evaluaciones360 evaluacion;
    @Getter private PeriodosEscolares periodoEscolar;
    @Getter @Setter private ListaPersonal directivoSeleccionado;

    @Getter private List<Apartado> apartados;
    @Getter private List<ListaPersonal> listaDirectivos;
    @Getter private List<ListaPersonal> listaSubordinados;
    @Getter private List<ListaPersonalEvaluacion360> listaPersonalEvaluando;
    @Getter private List<SelectItem> respuestasPosibles;
    @Getter private List<ListaPersonalEvaluacion360> listaPersonalEvaluado;
    @Getter private final Map<Integer, List<ListaPersonal>> opciones = new HashMap<>();
    @Getter private final Map<Integer, Apartado> apartadosHabilidades = new HashMap<>();

    @Getter @Setter Map<Integer, Map<Float, Object>> respuestas = new HashMap<>();
    @Getter @Setter List<Integer> clavesOpciones = new ArrayList<>();

    @EJB private EjbEvaluacion3601 ejbEvaluacion360;
    @EJB private Facade f;
    @Inject private LogonMB logonMB;
    private EntityManager em;

    @PostConstruct
    public void init() {
        try {
            em = f.getEntityManager();
            respuestasPosibles = ejbEvaluacion360.getRespuestasPosibles();
            apartados = ejbEvaluacion360.getApartados();

            //paso 1 obtener lista de directivos
//        listaDirectivos = evaluacionDesempenioEJB.getListaDirectivos();
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.init() ld: " + listaDirectivos);
            //paso 2 elegir un directivo
//        directivoSeleccionado = new ListaPersonal(30);
//        if(listaDirectivos.contains(directivoSeleccionado)){
//            directivoSeleccionado = listaDirectivos.get(listaDirectivos.indexOf(directivoSeleccionado));
//        }
            //facade.setEntityClass(ListaPersonal.class);
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.init() lucn: " + logonMB.getListaUsuarioClaveNomina());
            directivoSeleccionado = em.find(ListaPersonal.class, Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina()));//(ListaPersonal) facade.find(Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina()));
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.init() elegido: " + directivoSeleccionado);

            if (directivoSeleccionado != null) {
                //paso 3 obtener la lista de subordnados del directivo elegido
                listaSubordinados = ejbEvaluacion360.getListaSubordinados(directivoSeleccionado);
//                System.err.println("");
//                listaSubordinados.forEach(x -> {System.err.println("el personal : "+ x.getClave());});
//                System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.init() subordinados: " + listaSubordinados.size());
//                listaSubordinados.forEach(System.out::println);
                //paso 4 obtener las fotos de los subordinados
                //se convierte en paso 11 debido a que la lista de personal debe decidirse segun las opciones en pantalla
                //paso 5 obtener la evaluacion activa
                evaluacion = ejbEvaluacion360.evaluacionActiva();
                //TODO: tomar acciones en casa de no encontrar una evaluación activa

                if (evaluacion != null) {
                    //paso 6 obtener periodo activo
                    //facade.setEntityClass(PeriodosEscolares.class);
                    periodoEscolar = em.find(PeriodosEscolares.class, evaluacion.getPeriodo());//(PeriodosEscolares) facade.find(evaluacion.getPeriodo());
//                    System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.init() periodoEscolar:" + periodoEscolar);
                    //paso 7 obtener respuestas
                    ejbEvaluacion360.cargarResultadosAlmacenados(evaluacion, directivoSeleccionado, listaSubordinados);
//        for(DesempenioEvaluacionResultados der : evaluacion.getDesempenioEvaluacionResultadosList()){
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.init(7) der: " + der);
//        }

                    //paso8 inicializar claves de opciones
                    clavesOpciones.clear();
                    for (int i = 0; (i < maxEvaluando && i < listaSubordinados.size()); i++) {
                        clavesOpciones.add(listaSubordinados.get(i).getClave());
                    }

                    //paso 9 inicializar opciones
                    initOpciones();

                    //paso 10 inicializar mapeo de respuestas por clave subordinado y numero de pregunta
                    initRespuestas();

                    //paso 11 definir la lista del personal que se esta evaluando
                    initPersonalEvaluando();

                    initPersonalEvaluado();
                    cargada = true;
//                    System.out.println("se carga  360");
                }
            }

        } catch (Exception e) {
//            System.out.println("admini 360 e: " + e.getMessage());
            cargada = false;
        }

    }

    private void initOpciones() {
        opciones.clear();
        Integer index = 0;
        for (Integer clave : clavesOpciones) {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.init(9) clave: " + clave);
            opciones.put(index, new ArrayList<>());
            //facade.setEntityClass(ListaPersonal.class);
            ListaPersonal lpPrimero = em.find(ListaPersonal.class, clave);//(ListaPersonal) facade.find(clave);
            opciones.get(index).add(lpPrimero);
            //opcionesSelect.get(index).add(e)
            for (ListaPersonal lp : listaSubordinados) {
//                System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.init(9) res: " + (!clavesOpciones.contains(lp.getClave()) || clave.equals(lp.getClave())) + ", lp: " + lp);
                if ((!clavesOpciones.contains(lp.getClave()) || clave.equals(lp.getClave())) && !opciones.get(index).contains(lp)) {
                    opciones.get(index).add(lp);
                }
            }
            //facade.setEntityClass(Evaluaciones360Resultados.class);
            Evaluaciones360ResultadosPK pk = new Evaluaciones360ResultadosPK(evaluacion.getEvaluacion(), directivoSeleccionado.getClave(), clave);
            Evaluaciones360Resultados resultado = em.find(Evaluaciones360Resultados.class, pk);//(Evaluaciones360Resultados) facade.find();
            apartadosHabilidades.put(clave, ejbEvaluacion360.getApartadoHabilidades(resultado.getCategoria().getCategoria()));
            index++;
        }
//        opciones.forEach((k,v) -> System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.initOpciones(" + k + "): " +  v ));
    }

    private void initRespuestas() {
        listaSubordinados.stream().map((subordinado) -> {
            respuestas.put(subordinado.getClave(), new HashMap<>());
            return subordinado;
        }).forEachOrdered((subordinado) -> {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.initRespuestas() el subordinado es : " + subordinado.getNombre());
            Integer index = evaluacion.getEvaluaciones360ResultadosList().indexOf(new Evaluaciones360Resultados(evaluacion.getEvaluacion(), directivoSeleccionado.getClave(), subordinado.getClave()));
            Evaluaciones360Resultados resultado = evaluacion.getEvaluaciones360ResultadosList().get(index);
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.init(10) resultado: " + resultado);
            for (float fi = 1.0f; fi <= 33.0f; fi++) {
                respuestas.get(subordinado.getClave()).put(fi, ejbEvaluacion360.obtenerRespuestaPorPregunta(resultado, fi));
            }
        });
    }

    private void initPersonalEvaluando() {
        listaPersonalEvaluando = null;
        initPersonalEvaluado();
        listaPersonalEvaluando = new ArrayList<>();
        clavesOpciones.stream().map((clave) -> {
            //facade.setEntityClass(ListaPersonalEvaluacion360.class);
            return clave;
        }).forEachOrdered((clave) -> {
            ListaPersonalEvaluacion360 lpde = new ListaPersonalEvaluacion360(new Evaluaciones360ResultadosPK(evaluacion.getEvaluacion(), directivoSeleccionado.getClave(), clave));
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.initPersonalEvaluando() lpde: " + lpde);
            listaPersonalEvaluando.add(listaPersonalEvaluado.get(listaPersonalEvaluado.indexOf(lpde)));
//            listaPersonalEvaluando.add((ListaPersonalDesempenioEvaluacion)facade.find(new DesempenioEvaluacionResultadosPK(evaluacion.getEvaluacion(), directivoSeleccionado.getClave(), clave)));
        });//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.init(11) evaluando: " + listaPersonalEvaluando.toString());
    }

    private void initPersonalEvaluado() {
        listaPersonalEvaluado = null;
        listaPersonalEvaluado = ejbEvaluacion360.obtenerListaResultadosPorEvaluacionEvaluador(evaluacion, directivoSeleccionado);
//        listaPersonalEvaluado.forEach(lp -> System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.initPersonalEvaluado() lp: " + lp));
        evaluados = listaPersonalEvaluado.stream().filter(personal -> personal.getCompleto()).collect(Collectors.toList()).size();
        evaluando = listaPersonalEvaluado.stream().filter(personal -> personal.getIncompleto()).collect(Collectors.toList()).size();
    }

    public void guardarRespuesta(ValueChangeEvent e) {
        UIComponent origen = (UIComponent) e.getSource();
        String[] datos = origen.getId().split("_");
        Float pregunta_id = Float.parseFloat(datos[0].replaceAll("-", "\\.").replaceAll("r", ""));
        Integer clave = Integer.parseInt(datos[1]);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.guardarRespuesta(" + pregunta_id + ") evaluado: " + clave + ", respuesta: " + e.getNewValue() );

        //1 detectar si ya se tiene la respuesta, de lo contrario generarla
        Evaluaciones360Resultados resultado = new Evaluaciones360Resultados(evaluacion.getEvaluacion(), directivoSeleccionado.getClave(), clave);
        if (evaluacion.getEvaluaciones360ResultadosList().contains(resultado)) {
            int index = evaluacion.getEvaluaciones360ResultadosList().indexOf(resultado);
            resultado = evaluacion.getEvaluaciones360ResultadosList().get(index);
            ejbEvaluacion360.actualizarRespuestaPorPregunta(resultado, pregunta_id, e.getNewValue().toString());
        } else {
            ejbEvaluacion360.actualizarRespuestaPorPregunta(resultado, pregunta_id, e.getNewValue().toString());
            evaluacion.getEvaluaciones360ResultadosList().add(resultado);
        }
        //facade.setEntityClass(resultado.getClass());
        ejbEvaluacion360.comprobarResultado(resultado);
        f.edit(resultado);
        //em.merge(resultado);

        initPersonalEvaluando();

//        facade.setEntityClass(ListaPersonalDesempenioEvaluacion.class);
//        ListaPersonalDesempenioEvaluacion lpde = (ListaPersonalDesempenioEvaluacion)facade.find(resultado.getPk());
//        facade.getEntityManager().refresh(lpde);
//        listaPersonalEvaluando.set(listaPersonalEvaluando.indexOf(lpde), lpde);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.guardarRespuesta() evaluacion: " + evaluacion);
    }

    public void cambiarOpciones(ValueChangeEvent e) throws IOException {
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.cambiarOpciones():" + e.getNewValue());
        SelectOneMenu origen = (SelectOneMenu) e.getSource();
        String id = origen.getId();
        Integer claveSeleccionada = Integer.parseInt(e.getNewValue().toString().split("clave=")[1].split(",")[0]); // lp.getClave();  //Integer.parseInt(e.getNewValue().toString());
        Integer posicion = Integer.parseInt(id.replace("opcion", ""));
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.cambiarOpciones() id: " + id);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.cambiarOpciones() claveSeleccionada: " + claveSeleccionada);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.cambiarOpciones() posición: " + posicion);

        clavesOpciones.set(posicion, claveSeleccionada);
        initOpciones();
        initRespuestas();
        initPersonalEvaluando();
        Faces.refresh();
//        Faces.redirect("desempenio.xhtml");
    }

    public void continuarEvaluando() throws IOException {
        init();
        initPersonalEvaluado();
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.continuarEvaluando(1) clavesOpciones: " + clavesOpciones);
//        listaPersonalEvaluado.stream().forEach(p -> System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.continuarEvaluando() lpe: " + p));
        clavesOpciones.clear();
        int total = 0;
        for (ListaPersonalEvaluacion360 lpde : listaPersonalEvaluado.stream().filter(personal -> !personal.getCompleto()).collect(Collectors.toList())) {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.continuarEvaluando() incompleto: " + lpde);
            if (total < 4) {
                clavesOpciones.add(lpde.getPk().getEvaluado());
                total++;
            } else {
                break;
            }
        }

        if (clavesOpciones.size() < 4) {
            for (ListaPersonalEvaluacion360 lpde : listaPersonalEvaluado.stream().filter(personal -> personal.getCompleto()).collect(Collectors.toList())) {
//                System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.continuarEvaluando() completo: " + lpde);
                if (total < 4) {
                    clavesOpciones.add(lpde.getPk().getEvaluado());
                    total++;
                } else {
                    break;
                }
            }
        }

        initPersonalEvaluando();
        initOpciones();

//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.continuarEvaluando(2) clavesOpciones: " + clavesOpciones);
        Faces.refresh();
    }

    public String getId(@NonNull Float preguntaNumero, @NonNull Integer trabajadorClave) {
        String id = "r" + preguntaNumero.toString().replaceAll("\\.", "-") + "_" + trabajadorClave.toString();
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.getId(): " + id);
        return id;
    }

    public void actualizar() throws IOException {
        Faces.refresh();
    }

    public void descargarCedulas() {
        List<ListaPersonalEvaluacion360Promedios> listaPromedios = ejbEvaluacion360.getPromediosPorEvaluado();
        Map<Short, Apartado> mapaHabilidades = new HashMap<>();
        listaPromedios.forEach(lpe -> {
            if (!mapaHabilidades.containsKey(lpe.getCategoria())) {
                mapaHabilidades.put(lpe.getCategoria(), ejbEvaluacion360.getApartadoHabilidades(lpe.getCategoria()));
            }
        });

        List<ListaPersonalEvaluacion360Reporte> listaResportes = ejbEvaluacion360.getReportesPorEvaluador();

        ExcelWritter ew = new ExcelWritter(evaluacion, listaPromedios, listaResportes, periodoEscolar, mapaHabilidades);
        ew.obtenerLibro();
        ew.editarLibro();
        ew.escribirLibro();
//        String ruta = ew.enviarLibro();
        Ajax.oncomplete("descargar('" + ew.enviarLibro() + "');");

//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.descargarCedulas() ruta: " + ruta);
    }

}
