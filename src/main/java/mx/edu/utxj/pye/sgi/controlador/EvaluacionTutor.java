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
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionTutor;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacionTutor;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesTutoresResultadosPK;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionesTutores;
import org.omnifaces.util.Faces;
import org.primefaces.component.selectonemenu.SelectOneMenu;

/**
 *
 * @author UTXJ
 */
@Named
@SessionScoped
public class EvaluacionTutor implements Serializable {

    private static final long serialVersionUID = -2104785351397760456L;

    @Getter private final Integer maxEvaluando = 1;
    @Getter private Integer evaluados = 0;
    @Getter private Integer evaluando = 0;
    @Getter private Boolean cargada = false;
    @Getter private Boolean finalizado = false;

//    @Getter private final Cuestionario cuestionario = new CuestionarioEvaluacionDesempenio();
    @Getter private Evaluaciones evaluacion;
    @Getter private PeriodosEscolares periodoEscolar;
    @Getter @Setter private VistaEvaluacionesTutores evaluador;

    @Getter private List<Apartado> apartados;
//    @Getter private List<Alumno> listaEvaluadores;
    @Getter private List<EvaluacionesTutoresResultados> listaEvaluados;
    @Getter private List<EvaluacionesTutoresResultados> listaEvaluando;
    @Getter private List<SelectItem> respuestasPosibles;
    @Getter private List<EvaluacionesTutoresResultados> listaEvaluado;
    @Getter private final Map<Integer, List<EvaluacionesTutoresResultados>> opciones = new HashMap<>();

    @Getter @Setter Map<Integer, Map<Float, Object>> respuestas = new HashMap<>();
    @Getter @Setter List<Integer> clavesOpciones = new ArrayList<>();

    @EJB private EjbEvaluacionTutor ejbEvaluacionTutor;
    @EJB private Facade facade;
    @Inject private LogonMB logonMB;

    @PostConstruct
    public void init() {
        try {
            respuestasPosibles = ejbEvaluacionTutor.getRespuestasPosibles();
            apartados = ejbEvaluacionTutor.getApartados();
            evaluacion = ejbEvaluacionTutor.evaluacionActiva();
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.init() evaluacion: " + evaluacion);

            List<VistaEvaluacionesTutores> listaEvaluacionesTutores = ejbEvaluacionTutor.getListaEstudiantes(evaluacion.getPeriodo(), logonMB.getCurrentUser().trim());
            if (!listaEvaluacionesTutores.isEmpty()) {
                evaluador = listaEvaluacionesTutores.get(0);
//                System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.init() evaluador: " + evaluador);

                listaEvaluados = ejbEvaluacionTutor.getListaTutores(evaluador);
//                System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.init() listaEvaluados: " + listaEvaluados);

                facade.setEntityClass(PeriodosEscolares.class);
                periodoEscolar = (PeriodosEscolares) facade.find(evaluacion.getPeriodo());
//                System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.init() periodo: " + periodoEscolar);

                ejbEvaluacionTutor.cargarResultadosAlmacenados(evaluacion, evaluador, listaEvaluados);

                //paso8 inicializar claves de opciones
                clavesOpciones.clear();
                for (int i = 0; (i < maxEvaluando && i < listaEvaluados.size()); i++) {
                    clavesOpciones.add(listaEvaluados.get(i).getEvaluacionesTutoresResultadosPK().getEvaluado());
                }

                //paso 9 inicializar opciones
                initOpciones();

                //paso 10 inicializar mapeo de respuestas por clave subordinado y numero de pregunta
                initRespuestas();

                //paso 11 definir la lista del personal que se esta evaluando
                initPersonalEvaluando();

                initPersonalEvaluado();
                cargada = true;
                
                boolean res = true;
                for(EvaluacionesTutoresResultados etr: listaEvaluados){
//                    System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.init() comprobando: " + etr);
                    res = res && completo(etr);
                }
                finalizado = res;
            }
        } catch (Exception e) {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.init() e: " + e.getMessage() + " : " + (e.getCause()!=null?e.getCause().getMessage():""));
            cargada = false;
        }
    }

    private void initOpciones() {
        opciones.clear();
        Integer index = 0;
        for (Integer clave : clavesOpciones) {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.initOpciones() clave: " + clave);
            opciones.put(index, new ArrayList<>());
            facade.setEntityClass(EvaluacionesTutoresResultados.class);
            EvaluacionesTutoresResultados lpPrimero = (EvaluacionesTutoresResultados) facade.find(new EvaluacionesTutoresResultadosPK(evaluacion.getEvaluacion(), Integer.parseInt(evaluador.getPk().getMatricula()), clave));
            opciones.get(index).add(lpPrimero);
            //opcionesSelect.get(index).add(e)
            for (EvaluacionesTutoresResultados lp : listaEvaluados) {
//                System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.initOpciones() res: " + (!clavesOpciones.contains(lp.getPk().getEvaluado()) || clave.equals(lp.getPk().getEvaluado())) + ", lp: " + lp);
                if ((!clavesOpciones.contains(lp.getEvaluacionesTutoresResultadosPK().getEvaluado()) || clave.equals(lp.getEvaluacionesTutoresResultadosPK().getEvaluado())) && !opciones.get(index).contains(lp)) {
                    opciones.get(index).add(lp);
                }
            }
            index++;
        }
//        opciones.forEach((k,v) -> System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.initOpciones(" + k + "): " +  v ));
    }

    private void initRespuestas() {
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.initRespuestas() listaevaluados: " + listaEvaluados);
        listaEvaluados.stream().map((subordinado) -> {
            respuestas.put(subordinado.getEvaluacionesTutoresResultadosPK().getEvaluado(), new HashMap<>());
            return subordinado;
        }).forEachOrdered((subordinado) -> {
            Integer index = evaluacion.getEvaluacionesTutoresResultadosList().indexOf(new EvaluacionesTutoresResultados(evaluacion.getEvaluacion(), Integer.parseInt(evaluador.getPk().getMatricula()), subordinado.getEvaluacionesTutoresResultadosPK().getEvaluado()));
            EvaluacionesTutoresResultados resultado = evaluacion.getEvaluacionesTutoresResultadosList().get(index);
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.initRespuestas(10) resultado: " + resultado);
            for (float fi = 1.0f; fi <= 21.0f; fi++) {
                respuestas.get(subordinado.getEvaluacionesTutoresResultadosPK().getEvaluado()).put(fi, ejbEvaluacionTutor.obtenerRespuestaPorPregunta(resultado, fi));
            }
        });
    }

    private void initPersonalEvaluando() {
        listaEvaluando = null;
        initPersonalEvaluado();
        listaEvaluando = new ArrayList<>();
        clavesOpciones.stream().map((clave) -> {
            return clave;
        }).forEachOrdered((clave) -> {
            EvaluacionesTutoresResultados lpde = new EvaluacionesTutoresResultados(new EvaluacionesTutoresResultadosPK(evaluacion.getEvaluacion(), Integer.parseInt(evaluador.getPk().getMatricula()), clave));
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.initPersonalEvaluando() lpde: " + lpde);
            Integer index = listaEvaluado.indexOf(lpde);
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.initPersonalEvaluando() index: " + index);
            listaEvaluando.add(listaEvaluado.get(index));
//            listaEvaluando.add((ListaPersonalDesempenioEvaluacion)facade.find(new DesempenioEvaluacionResultadosPK(evaluacion.getEvaluacion(), evaluador.getClave(), clave)));
        });
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.initPersonalEvaluando(11) evaluando: " + listaEvaluando.toString());
    }

    private void initPersonalEvaluado() {
        listaEvaluado = null;
        Comparador comparador = new ComparadorEvaluacionTutor();
        listaEvaluado = ejbEvaluacionTutor.obtenerListaResultadosPorEvaluacionEvaluador(evaluacion, evaluador);
//        listaEvaluado.forEach(lp -> System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.initPersonalEvaluado() lp: " + lp));
        evaluados = listaEvaluado.stream().filter(evaluado -> comparador.isCompleto(evaluado)).collect(Collectors.toList()).size();
        evaluando = listaEvaluado.stream().filter(evaluado -> !comparador.isCompleto(evaluado)).collect(Collectors.toList()).size();
    }

    public void guardarRespuesta(ValueChangeEvent e) {
        UIComponent origen = (UIComponent) e.getSource();
        String[] datos = origen.getId().split("_");
        Float pregunta_id = Float.parseFloat(datos[0].replaceAll("-", "\\.").replaceAll("r", ""));
        Integer clave = Integer.parseInt(datos[1]);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.guardarRespuesta(" + pregunta_id + ") evaluado: " + clave + ", respuesta: " + e.getNewValue() );

        //1 detectar si ya se tiene la respuesta, de lo contrario generarla
        EvaluacionesTutoresResultados resultado = new EvaluacionesTutoresResultados(evaluacion.getEvaluacion(), Integer.parseInt(evaluador.getPk().getMatricula()), clave);
        if (evaluacion.getEvaluacionesTutoresResultadosList().contains(resultado)) {
            int index = evaluacion.getEvaluacionesTutoresResultadosList().indexOf(resultado);
            resultado = evaluacion.getEvaluacionesTutoresResultadosList().get(index);
            ejbEvaluacionTutor.actualizarRespuestaPorPregunta(resultado, pregunta_id, e.getNewValue().toString());
        } else {
            ejbEvaluacionTutor.actualizarRespuestaPorPregunta(resultado, pregunta_id, e.getNewValue().toString());
            evaluacion.getEvaluacionesTutoresResultadosList().add(resultado);
        }
        facade.setEntityClass(resultado.getClass());
        facade.edit(resultado);

        initPersonalEvaluando();

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
        Comparador<EvaluacionesTutoresResultados> comparador = new ComparadorEvaluacionTutor();
        for (EvaluacionesTutoresResultados lpde : listaEvaluado.stream().filter(personal -> comparador.isCompleto(personal)).collect(Collectors.toList())) {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.continuarEvaluando() incompleto: " + lpde);
            if (total < 4) {
                clavesOpciones.add(lpde.getEvaluacionesTutoresResultadosPK().getEvaluado());
                total++;
            } else {
                break;
            }
        }

        if (clavesOpciones.size() < 4) {
            for (EvaluacionesTutoresResultados lpde : listaEvaluado.stream().filter(personal -> comparador.isCompleto(personal)).collect(Collectors.toList())) {
//                System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.continuarEvaluando() completo: " + lpde);
                if (total < 4) {
                    clavesOpciones.add(lpde.getEvaluacionesTutoresResultadosPK().getEvaluado());
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
    
    public Boolean completo(EvaluacionesTutoresResultados resultado){
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionTutor.completo() resultado: " + resultado);
        Comparador<EvaluacionesTutoresResultados> cet = new ComparadorEvaluacionTutor();
        return cet.isCompleto(resultado);
    }
}
