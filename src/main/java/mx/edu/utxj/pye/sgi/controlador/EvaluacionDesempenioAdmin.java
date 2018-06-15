/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import edu.mx.utxj.pye.seut.util.util.Cuestionario;
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
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.cuestionario.CuestionarioEvaluacionDesempenio;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacionDesempenio;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonalDesempenioEvaluacion;
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
public class EvaluacionDesempenioAdmin implements Serializable {

    private static final long serialVersionUID = -2104785351397760456L;

    @Getter private final Integer maxEvaluando = 4;
    @Getter private Integer evaluados = 0;
    @Getter private Integer evaluando = 0;
    @Getter private Boolean cargada = false;

    @Getter private final Cuestionario cuestionario = new CuestionarioEvaluacionDesempenio();
    @Getter private DesempenioEvaluaciones desempenioEvaluacion;
    @Getter private PeriodosEscolares periodoEscolar;
    @Getter @Setter private ListaPersonal directivoSeleccionado;

    @Getter private List<Apartado> apartados;
    @Getter private List<ListaPersonal> listaDirectivos;
    @Getter private List<ListaPersonal> listaSubordinados;
    @Getter private List<ListaPersonalDesempenioEvaluacion> listaPersonalEvaluando;
    @Getter private List<SelectItem> respuestasPosibles;
    @Getter private List<ListaPersonalDesempenioEvaluacion> listaPersonalEvaluado;
    @Getter private final Map<Integer, List<ListaPersonal>> opciones = new HashMap<>();

    @Getter @Setter Map<Integer, Map<Float, Object>> respuestas = new HashMap<>();
    @Getter @Setter List<Integer> clavesOpciones = new ArrayList<>();

    @EJB private EjbEvaluacionDesempenio evaluacionDesempenioEJB;
    @EJB private Facade facade;
    @Inject private LogonMB logonMB;

    @PostConstruct
    public void init() {
        try {
            respuestasPosibles = evaluacionDesempenioEJB.getRespuestasPosibles();
            apartados = evaluacionDesempenioEJB.getApartados(cuestionario);

            //paso 1 obtener lista de directivos
//        listaDirectivos = evaluacionDesempenioEJB.getListaDirectivos();
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.init() ld: " + listaDirectivos);
            //paso 2 elegir un directivo
//        directivoSeleccionado = new ListaPersonal(30);
//        if(listaDirectivos.contains(directivoSeleccionado)){
//            directivoSeleccionado = listaDirectivos.get(listaDirectivos.indexOf(directivoSeleccionado));
//        }
            facade.setEntityClass(ListaPersonal.class);
            directivoSeleccionado = (ListaPersonal) facade.find(Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina()));
        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.init() elegido: " + directivoSeleccionado);

            //paso 3 obtener la lista de subordnados del directivo elegido
            listaSubordinados = evaluacionDesempenioEJB.getListaSubordinados(directivoSeleccionado);
            listaSubordinados.forEach(s -> {
                System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.init() subordinado 1: " + s.getClave());
            });
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.init() subordinados: " + listaSubordinados.size());

            //paso 4 obtener las fotos de los subordinados
            //se convierte en paso 11 debido a que la lista de personal debe decidirse segun las opciones en pantalla
            //paso 5 obtener la evaluacion activa
            desempenioEvaluacion = evaluacionDesempenioEJB.evaluacionActiva();
            //TODO: tomar acciones en casa de no encontrar una evaluación activa
            if (desempenioEvaluacion != null) {
                //paso 6 obtener periodo activo
                facade.setEntityClass(PeriodosEscolares.class);
                periodoEscolar = (PeriodosEscolares) facade.find(desempenioEvaluacion.getPeriodo());
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.init() periodoEscolar:" + periodoEscolar);

                if (directivoSeleccionado.getActividad() == 2 && desempenioEvaluacion != null || directivoSeleccionado.getActividad() == 4 && desempenioEvaluacion != null) {

                    //paso 7 obtener respuestas
                    evaluacionDesempenioEJB.cargarResultadosAlmacenados(desempenioEvaluacion, directivoSeleccionado, listaSubordinados);
//        for(DesempenioEvaluacionResultados der : desempenioEvaluacion.getDesempenioEvaluacionResultadosList()){
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.init(7) der: " + der);
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
                    System.out.println("se carga  des");
                }
            }
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.init() e: " + e.getMessage());
            cargada = false;
        }
    }

    private void initOpciones() {
        opciones.clear();
        Integer index = 0;
        for (Integer clave : clavesOpciones) {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.init(9) clave: " + clave);
            opciones.put(index, new ArrayList<>());
            facade.setEntityClass(ListaPersonal.class);
            ListaPersonal lpPrimero = (ListaPersonal) facade.find(clave);
            opciones.get(index).add(lpPrimero);
            //opcionesSelect.get(index).add(e)
            for (ListaPersonal lp : listaSubordinados) {
//                System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.init(9) res: " + (!clavesOpciones.contains(lp.getClave()) || clave.equals(lp.getClave())) + ", lp: " + lp);
                if ((!clavesOpciones.contains(lp.getClave()) || clave.equals(lp.getClave())) && !opciones.get(index).contains(lp)) {
                    opciones.get(index).add(lp);
                }
            }
            index++;
        }
//        opciones.forEach((k,v) -> System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.initOpciones(" + k + "): " +  v ));
    }

    private void initRespuestas() {
        listaSubordinados.stream().map((subordinado) -> {
            respuestas.put(subordinado.getClave(), new HashMap<>());
            return subordinado;
        }).forEachOrdered((subordinado) -> {
            Integer index = desempenioEvaluacion.getDesempenioEvaluacionResultadosList().indexOf(new DesempenioEvaluacionResultados(desempenioEvaluacion.getEvaluacion(), directivoSeleccionado.getClave(), subordinado.getClave()));
            DesempenioEvaluacionResultados resultado = desempenioEvaluacion.getDesempenioEvaluacionResultadosList().get(index);
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.init(10) resultado: " + resultado);
            for (float fi = 1.0f; fi <= 21.0f; fi++) {
                respuestas.get(subordinado.getClave()).put(fi, evaluacionDesempenioEJB.obtenerRespuestaPorPregunta(resultado, fi));
            }
        });
    }

    private void initPersonalEvaluando() {
        listaPersonalEvaluando = null;
        initPersonalEvaluado();
        listaPersonalEvaluando = new ArrayList<>();
        clavesOpciones.stream().map((clave) -> {
            facade.setEntityClass(ListaPersonalDesempenioEvaluacion.class);
            return clave;
        }).forEachOrdered((clave) -> {
            ListaPersonalDesempenioEvaluacion lpde = new ListaPersonalDesempenioEvaluacion(new DesempenioEvaluacionResultadosPK(desempenioEvaluacion.getEvaluacion(), directivoSeleccionado.getClave(), clave));
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.initPersonalEvaluando() lpde: " + lpde);
            listaPersonalEvaluando.add(listaPersonalEvaluado.get(listaPersonalEvaluado.indexOf(lpde)));
//            listaPersonalEvaluando.add((ListaPersonalDesempenioEvaluacion)facade.find(new DesempenioEvaluacionResultadosPK(desempenioEvaluacion.getEvaluacion(), directivoSeleccionado.getClave(), clave)));
        });//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.init(11) evaluando: " + listaPersonalEvaluando.toString());
    }

    private void initPersonalEvaluado() {
        listaPersonalEvaluado = null;
        listaPersonalEvaluado = evaluacionDesempenioEJB.obtenerListaResultadosPorEvaluacionEvaluador(desempenioEvaluacion, directivoSeleccionado);
//        listaPersonalEvaluado.forEach(lp -> System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.initPersonalEvaluado() lp: " + lp));
        evaluados = listaPersonalEvaluado.stream().filter(personal -> personal.isCompleto()).collect(Collectors.toList()).size();
        evaluando = listaPersonalEvaluado.stream().filter(personal -> personal.isIncompleto()).collect(Collectors.toList()).size();
    }

    public void guardarRespuesta(ValueChangeEvent e) {
        UIComponent origen = (UIComponent) e.getSource();
        String[] datos = origen.getId().split("_");
        Float pregunta_id = Float.parseFloat(datos[0].replaceAll("-", "\\.").replaceAll("r", ""));
        Integer clave = Integer.parseInt(datos[1]);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.guardarRespuesta(" + pregunta_id + ") evaluado: " + clave + ", respuesta: " + e.getNewValue() );

        //1 detectar si ya se tiene la respuesta, de lo contrario generarla
        DesempenioEvaluacionResultados resultado = new DesempenioEvaluacionResultados(desempenioEvaluacion.getEvaluacion(), directivoSeleccionado.getClave(), clave);
        if (desempenioEvaluacion.getDesempenioEvaluacionResultadosList().contains(resultado)) {
            int index = desempenioEvaluacion.getDesempenioEvaluacionResultadosList().indexOf(resultado);
            resultado = desempenioEvaluacion.getDesempenioEvaluacionResultadosList().get(index);
            evaluacionDesempenioEJB.actualizarRespuestaPorPregunta(resultado, pregunta_id, e.getNewValue().toString());
        } else {
            evaluacionDesempenioEJB.actualizarRespuestaPorPregunta(resultado, pregunta_id, e.getNewValue().toString());
            desempenioEvaluacion.getDesempenioEvaluacionResultadosList().add(resultado);
        }
        facade.setEntityClass(resultado.getClass());
        facade.edit(resultado);

        initPersonalEvaluando();

//        facade.setEntityClass(ListaPersonalDesempenioEvaluacion.class);
//        ListaPersonalDesempenioEvaluacion lpde = (ListaPersonalDesempenioEvaluacion)facade.find(resultado.getPk());
//        facade.getEntityManager().refresh(lpde);
//        listaPersonalEvaluando.set(listaPersonalEvaluando.indexOf(lpde), lpde);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.guardarRespuesta() evaluacion: " + desempenioEvaluacion);
    }

    public void cambiarOpciones(ValueChangeEvent e) throws IOException {
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.cambiarOpciones():" + e.getNewValue());
        SelectOneMenu origen = (SelectOneMenu) e.getSource();
        String id = origen.getId();
        Integer claveSeleccionada = Integer.parseInt(e.getNewValue().toString().split("clave=")[1].split(",")[0]); // lp.getClave();  //Integer.parseInt(e.getNewValue().toString());
        Integer posicion = Integer.parseInt(id.replace("opcion", ""));
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.cambiarOpciones() id: " + id);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.cambiarOpciones() claveSeleccionada: " + claveSeleccionada);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.cambiarOpciones() posición: " + posicion);

        clavesOpciones.set(posicion, claveSeleccionada);
        initOpciones();
        initRespuestas();
        initPersonalEvaluando();
//        Faces.redirect("desempenio.xhtml");
    }

    public void continuarEvaluando() throws IOException {
        init();
        initPersonalEvaluado();
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.continuarEvaluando(1) clavesOpciones: " + clavesOpciones);

        clavesOpciones.clear();
        int total = 0;
        for (ListaPersonalDesempenioEvaluacion lpde : listaPersonalEvaluado.stream().filter(personal -> personal.isIncompleto()).collect(Collectors.toList())) {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.continuarEvaluando() incompleto: " + lpde);
            if (total < 4) {
                clavesOpciones.add(lpde.getPk().getEvaluado());
                total++;
            } else {
                break;
            }
        }

        if (clavesOpciones.size() < 4) {
            for (ListaPersonalDesempenioEvaluacion lpde : listaPersonalEvaluado.stream().filter(personal -> personal.isCompleto()).collect(Collectors.toList())) {
//                System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.continuarEvaluando() completo: " + lpde);
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

//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.continuarEvaluando(2) clavesOpciones: " + clavesOpciones);
        Faces.refresh();
    }

    public String getId(@NonNull Float preguntaNumero, @NonNull Integer trabajadorClave) {
        String id = "r" + preguntaNumero.toString().replaceAll("\\.", "-") + "_" + trabajadorClave.toString();
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.getId(): " + id);
        return id;
    }

    public void descargarCedulas() {
        ExcelWritter ew = new ExcelWritter(directivoSeleccionado, desempenioEvaluacion, getListaPersonalEvaluado(), periodoEscolar);
        ew.obtenerLibro();
        ew.editarLibro();
        ew.escribirLibro();
        String ruta = ew.enviarLibro();
        Ajax.oncomplete("descargar('" + ew.enviarLibro() + "');");

//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.descargarCedulas() ruta: " + ruta);
    }
}
