/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
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
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.ejb.EJBEvaluacionDocenteMateria;
import mx.edu.utxj.pye.sgi.ejb.EjbEstudioEgresados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMaterias;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionDocenteMateriaPye;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.component.selectonemenu.SelectOneMenu;

/**
 *
 * @author Carlos Alfredo Vargas Galindo
 */
@Named
@SessionScoped
public class EvaluacionDoncenteMateriaControler implements Serializable {

    @Getter private Boolean finalizado = false;
    @Getter private Integer numeroNomina;
    @Getter private final Integer maxEvaluando = 4;
    @Getter private Integer evaluados = 0;
    @Getter private Integer evaluando = 0;
    @Getter private Boolean cargada = false;

    @Getter private EvaluacionDocentesMaterias evaluacion;
    @Getter private PeriodosEscolares periodoEscolar;
    @Getter private Alumnos estudiante;
    @Getter @Setter private VistaEvaluacionDocenteMateriaPye evaluador, docenteEvaluado, datosEvaluacion, opcionesDocentesEvaluados;

    @Getter private List<Apartado> preguntas;
    @Getter private List<VistaEvaluacionDocenteMateriaPye> listaDatosEvaluacion, ListaDatosEvaluados;
    @Getter private List<EvaluacionDocentesMateriaResultados> listaDocentesEvaluando, listaDocentesEvaluados;
//    @Getter List<EvaluacionDocentesMaterias>  listaDocentesEvaluando1, listaDocentesEvaluados1;
    @Getter private List<SelectItem> respuestasPosibles;

    @Getter private List<VistaEvaluacionDocenteMateriaPye> listaDocenteMateriaPyes;

    @Getter private final Map<Integer, List<VistaEvaluacionDocenteMateriaPye>> opciones = new HashMap<>();
//    @Getter private final Map<Integer, List<ListaEvaluacionDocentes>> opcionesEvaluacion = new HashMap<>();

    @Getter @Setter Map<String, Map<Float, Object>> respuestas = new HashMap<>();
    @Getter @Setter List<Integer> clavesOpciones = new SerializableArrayList<>();
    @Getter @Setter List<String> clavesOpcionesMateria = new SerializableArrayList<>();

    @Inject LogonMB logonMB;
    @EJB private Facade facade;
    @EJB private Facade2 facadeSaiiut;
    @EJB private EjbEstudioEgresados egresados;
    @EJB EJBEvaluacionDocenteMateria eJBEvaluacionDocenteMateria;

    @PostConstruct
    public void init() {
        if (eJBEvaluacionDocenteMateria.evaluacionActiva() != null) {
            respuestasPosibles = eJBEvaluacionDocenteMateria.getRespuestasPosibles();
            preguntas = eJBEvaluacionDocenteMateria.getApartados();
            //paso 1 obtemer lista de los alumnos que evaluan
            listaDatosEvaluacion = eJBEvaluacionDocenteMateria.getDocenteMAteria(logonMB.getCurrentUser(), eJBEvaluacionDocenteMateria.evaluacionActiva().getPeriodo());
//         System.out.println(" datos de la evaluacion : " + listaDatosEvaluacion);
            estudiante = egresados.getAlumnoPorMatricula(logonMB.getCurrentUser());
            if (!estudiante.getActivo()) {
                Messages.addGlobalWarn("Usted es un alumno que no se encuentra activo o es egresado, por lo tanto no tiene acceso a la evaluacion docente");
//                return;
                cargada = false;
            } else {
                if (estudiante.getGradoActual() == 11 || estudiante.getGradoActual() == 6) {
                    Messages.addGlobalWarn("Usted esta en proceso de estadia, o es egresado por lo tanto no tiene acceso a la evaluacion docente");
                    cargada = false;
                } else {
                    if (listaDatosEvaluacion.isEmpty() || listaDatosEvaluacion == null) {
//                        return;
                        cargada = false;
                    } else {
                        //paso 2 eligir a un estudiante como evaluador
                        evaluador = listaDatosEvaluacion.get(0);
//         System.out.println("estudiante que evalua : "+evaluador.getNombre() +" "+ evaluador.getApellidoPat()+ " "+ evaluador.getApellidoMat());

                        if (listaDatosEvaluacion != null || listaDatosEvaluacion.isEmpty()) {
                            //paso 3 obtener la lista de docentes a evaluar
                            ListaDatosEvaluados = eJBEvaluacionDocenteMateria.getDocenteMAteria(logonMB.getCurrentUser(), eJBEvaluacionDocenteMateria.evaluacionActiva().getPeriodo());
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDoncenteMateriaControler.init() lista de docentes evaluados : " + ListaDatosEvaluados);

                            //paso 4 obtener las fotos de los subordinados
                            //se convierte en paso 11 debido a que la lista de docente debe decidirse segun las opciones en pantalla
                            //paso 5 obtener la evaluacion activa
                            evaluacion = eJBEvaluacionDocenteMateria.evaluacionActiva();
//            System.out.println("obtener la evaluacion activa : " + evaluacion);
                            if (evaluacion != null) {
                                //paso 6 obtener periodo activo
                                facade.setEntityClass(PeriodosEscolares.class);
                                periodoEscolar = (PeriodosEscolares) facade.find(evaluacion.getPeriodo());// Cambiar periodo de la evaluacion por evaluacion.getPeriodo()
                                //paso 7 obtener respuestas
                                eJBEvaluacionDocenteMateria.cargarResultadosAlmacenados(evaluacion, evaluador, ListaDatosEvaluados);

                                //paso8 inicializar claves de opciones
                                clavesOpciones.clear();
                                for (int i = 0; (i < maxEvaluando && i < ListaDatosEvaluados.size()); i++) {
                                    clavesOpciones.add(Integer.parseInt(ListaDatosEvaluados.get(i).getNumeroNomina()));
//                System.out.println("Numero de nomina de las primeras cuatro personas evaluadas: " + clavesOpciones);
                                }
                                clavesOpcionesMateria.clear();
                                for (int i = 0; (i < maxEvaluando && i < ListaDatosEvaluados.size()); i++) {
                                    clavesOpcionesMateria.add(ListaDatosEvaluados.get(i).getCveMateria());
//                System.out.println("Numero de nomina de las primeras cuatro materias evaluadas: " + clavesOpcionesMateria);
                                }
                                //paso 9 inicializar las siguientes opciociones
                                initOpciones();
                                // paso 10 inicializar el mapeo de respuestas por clave de docente y el numero de pregunta
                                initRespuestas();
                                //paso 11 definir la lista del personal que se esta evaluando
                                initDocenteEvaluando();
//            initDocenteEvaluado();
                                cargada = true;
                            }
                        }
                    }
                }
            }

        }
    }

    private void initOpciones() {
        opciones.clear();
        Integer index = 0;
        for (String materia : clavesOpcionesMateria) {
            opciones.put(index, new SerializableArrayList<>());
            opcionesDocentesEvaluados = eJBEvaluacionDocenteMateria.getDatosDocente(logonMB.getCurrentUser(), materia, evaluacion.getPeriodo());// CAMBIAR POR PERIODO ESCOLAR periodo.getPeriodo()
//            System.out.println("docente seleccionado : " + opcionesDocentesEvaluados);
            opciones.get(index).add(opcionesDocentesEvaluados);
//            System.out.println("materia que imparte el docente : " + materia);
//            System.out.println("datos del docente : " + opciones.get(index));
            for (VistaEvaluacionDocenteMateriaPye lp : ListaDatosEvaluados) {
//                System.out.println("evaluacion res: " + (!clavesOpcionesMateria.contains(lp.getCveMateria()) || materia.equals(lp.getCveMateria())) + ", lp: " + lp);
                if ((!clavesOpcionesMateria.contains(lp.getCveMateria()) || materia.equals(lp.getCveMateria())) && !opciones.get(index).contains(lp)) {
                    opciones.get(index).add(lp);
                }
//                System.out.println("posicion del mapa : " + index);
//                System.out.println("las opciones de evaluacion  : " + opciones);
            }
            index++;
        }

    }

    private void initRespuestas() {
        ListaDatosEvaluados.stream().map((docenteAEvaluar) -> {
            respuestas.put(docenteAEvaluar.getCveMateria(), new HashMap<>());
//            System.out.println("impresion de las respuestas : " + respuestas);
            return docenteAEvaluar;
        }).forEachOrdered((docenteAEvaluar) -> {
            Integer index = evaluacion.getEvaluacionDocentesMateriaResultadosList().indexOf(new EvaluacionDocentesMateriaResultados(evaluacion.getEvaluacion(), Integer.parseInt(evaluador.getMatricula()), docenteAEvaluar.getCveMateria(), Integer.parseInt(docenteAEvaluar.getNumeroNomina())));
//            System.out.println("impresion de el index del metodo respuestas : " + index);
            EvaluacionDocentesMateriaResultados resultado = evaluacion.getEvaluacionDocentesMateriaResultadosList().get(index);
            for (float fi = 1.0f; fi <= 32.0f; fi++) {
                respuestas.get(docenteAEvaluar.getCveMateria()).put(fi, eJBEvaluacionDocenteMateria.obtenerRespuestaPorPregunta(resultado, fi));
//                System.out.println("respuestas por docente : " + respuestas);
            }
        });
    }

    private void initDocenteEvaluando() {
        listaDocentesEvaluando = null;
        initDocenteEvaluado();
        listaDocentesEvaluando = new ArrayList<>();
        clavesOpcionesMateria.stream().map((clave) -> {
            facade.setEntityClass(EvaluacionDocentesMateriaResultados.class);
            return clave;
        }).forEachOrdered((clave) -> {
            datosEvaluacion = eJBEvaluacionDocenteMateria.getcveMateria(logonMB.getCurrentUser(), clave);
//            System.out.println("docente evaluado ---> : " + datosEvaluacion.getNumeroNomina());
            EvaluacionDocentesMateriaResultados led = new EvaluacionDocentesMateriaResultados(new EvaluacionDocentesMateriaResultadosPK(evaluacion.getEvaluacion(), Integer.parseInt(datosEvaluacion.getMatricula()), datosEvaluacion.getCveMateria(), Integer.parseInt(datosEvaluacion.getNumeroNomina())));
//            ListaEvaluacionDocentes led = new ListaEvaluacionDocentes(new EvaluacionDocentesMateriaResultadosPK(evaluacion.getEvaluacion(), Integer.parseInt(logonMB.getCurrentUser()), clave, Integer.parseInt(datosEvaluacion.getNumeroNomina())));
//            System.out.println("impresion antes de agregar el siguiente" + listaDocentesEvaluados.indexOf(led));
            listaDocentesEvaluando.add(listaDocentesEvaluados.get(listaDocentesEvaluados.indexOf(led)));
        });
    }

    public void initDocenteEvaluado() {
        listaDocentesEvaluados = null;
        listaDocentesEvaluados = eJBEvaluacionDocenteMateria.obtenerListaResultadosPorEvaluacionEvaluador(evaluacion, evaluador);
//        System.out.println("docentesEvaluados : " + listaDocentesEvaluados);
//        System.out.println("lista de docentes evaluando : " + listaDocentesEvaluados.size());
        evaluados = listaDocentesEvaluados.stream().filter(docente -> docente.getCompleto()).collect(Collectors.toList()).size();
//        System.out.println("evaluados : " +evaluados);
        evaluando = listaDocentesEvaluados.stream().filter(docente -> docente.getIncompleto()).collect(Collectors.toList()).size();
//         System.out.println("evaluando : " + evaluando);
    }

    public void guardarRespuesta(ValueChangeEvent e) {
        UIComponent origen = (UIComponent) e.getSource();
        String[] datos = origen.getId().split("_");
        Float pregunta_id = Float.parseFloat(datos[0].replaceAll("-", "\\.").replaceAll("r", ""));

        String clave = datos[1];
//        System.out.println("la clave es igual a = : " + clave);
//        System.out.println("la pregunta es igual a = : " + pregunta_id);

        opcionesDocentesEvaluados = eJBEvaluacionDocenteMateria.getDatosDocente(logonMB.getCurrentUser(), clave, evaluacion.getPeriodo());// cambiar por periodo escolar peridoescolar.getperiodoEscolar()
        Integer claveDocente = Integer.parseInt(opcionesDocentesEvaluados.getNumeroNomina());
//        System.out.println("el numero de nomina del docente es : " + claveDocente);
        datosEvaluacion = eJBEvaluacionDocenteMateria.getcveMateria(logonMB.getCurrentUser(), opcionesDocentesEvaluados.getCveMateria());
//        System.out.println("datos evaaluacion : " + datosEvaluacion);
        EvaluacionDocentesMateriaResultados resultado = new EvaluacionDocentesMateriaResultados(evaluacion.getEvaluacion(), Integer.parseInt(evaluador.getMatricula()), datosEvaluacion.getCveMateria(), claveDocente);
        if (evaluacion.getEvaluacionDocentesMateriaResultadosList().contains(resultado)) {
            int index = evaluacion.getEvaluacionDocentesMateriaResultadosList().indexOf(resultado);
            resultado = evaluacion.getEvaluacionDocentesMateriaResultadosList().get(index);
            eJBEvaluacionDocenteMateria.actualizarRespuestaPorPregunta(resultado, pregunta_id, e.getNewValue().toString());
        } else {
            eJBEvaluacionDocenteMateria.actualizarRespuestaPorPregunta(resultado, pregunta_id, e.getNewValue().toString());
            evaluacion.getEvaluacionDocentesMateriaResultadosList().add(resultado);
        }
        facade.setEntityClass(resultado.getClass());
        eJBEvaluacionDocenteMateria.comprobarResultado(resultado);
        facade.edit(resultado);

        initDocenteEvaluando();
    }

    public void cambiarOpciones(ValueChangeEvent e) throws IOException {
        SelectOneMenu origen = (SelectOneMenu) e.getSource();
        String id = origen.getId();
//        System.out.println("impresion del valor nuevo : " + e.getNewValue().toString() 
//                + "\n impresion del id : " + id);
        String claveSeleccionadaMateria = e.getNewValue().toString();//.split("clave=")[1].split(",")[0];
//        Integer claveSeleccionada = Integer.parseInt(e.getNewValue().toString().split("clave=")[1].split(",")[0]); // lp.getClave();  //Integer.parseInt(e.getNewValue().toString());
//        System.out.println("la clave seleccionada : " + claveSeleccionadaMateria);
        Integer posicion = Integer.parseInt(id.replace("opcion", ""));
//        System.out.println("posicion de la clave : "+ posicion); 
        clavesOpcionesMateria.set(posicion, claveSeleccionadaMateria);
//        System.out.println("datos de las opciones : " + clavesOpcionesMateria);
        initOpciones();
        initRespuestas();
        initDocenteEvaluando();
        Faces.refresh();
    }

    public void continuarEvaluando() throws IOException {
        init();
        initDocenteEvaluado();
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.continuarEvaluando(1) clavesOpciones: " + clavesOpciones);
//        listaDatosEvaluacion.stream().forEach(p -> System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.continuarEvaluando() lpe: " + p));
        clavesOpcionesMateria.clear();
        int total = 0;
        for (EvaluacionDocentesMateriaResultados lpde : listaDocentesEvaluados.stream().filter(personal -> !personal.getCompleto()).collect(Collectors.toList())) {
            System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.continuarEvaluando() incompleto: " + lpde);
            if (total < 4) {
                clavesOpcionesMateria.add(lpde.getPk().getCveMateria());
                total++;
            } else {
                break;
            }
        }

        if (clavesOpcionesMateria.size() < 4) {
            for (EvaluacionDocentesMateriaResultados lpde : listaDocentesEvaluados.stream().filter(personal -> personal.getCompleto()).collect(Collectors.toList())) {
                System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin1.continuarEvaluando() completo: " + lpde);
                if (total < 4) {
                    clavesOpcionesMateria.add(lpde.getPk().getCveMateria());
                    total++;
                } else {
                    break;
                }
            }
        }

        initDocenteEvaluando();
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

}
