/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacion360;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360ResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonalEvaluacion360;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.omnifaces.util.Faces;
import org.primefaces.component.selectonemenu.SelectOneMenu;

/**
 *
 * @author UTXJ
 */
@Named(value = "evaluacion360Admin")
@SessionScoped @NoArgsConstructor
public class Evaluacion360Admin implements Serializable {

    private static final long serialVersionUID = 2577046852499228390L;

    @Getter private final Integer maxEvaluando = 4;
    @Getter private Integer evaluados = 0;
    @Getter private Integer evaluando = 0;

    @Getter private Evaluaciones360 evaluaciones360;
    @Getter private PeriodosEscolares periodoEscolar;
    @Getter @Setter private ListaPersonal evaluador;


    @Getter private List<Apartado> apartados;
    @Getter private List<SelectItem> respuestasPosibles;
    @Getter private List<ListaPersonalEvaluacion360> listaPersonalEvaluando;
    @Getter private List<ListaPersonalEvaluacion360> listaPersonalEvaluado;
    @Getter private final Map<Integer,List<ListaPersonalEvaluacion360>> opciones = new HashMap<>();
    @Getter private final Map<Integer,Apartado> apartadosHabilidades = new HashMap<>();

    @Getter @Setter Map<Integer,Map<Float,Object>> respuestas = new HashMap<>();
    @Getter @Setter List<Integer> clavesOpciones = new ArrayList<>();

    @EJB private Facade facade;
    @EJB private EjbEvaluacion360 ejbEvaluacion360;
    @Inject private LogonMB logonMB;

    @PostConstruct
    public void init(){
        evaluaciones360 = ejbEvaluacion360.getEvaluacionActiva();
        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.init() evaluacion: " + evaluaciones360);
        respuestasPosibles = ejbEvaluacion360.getRespuestasPosibles();
        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.init() respuestas posibles: " + respuestasPosibles);

        apartados = ejbEvaluacion360.getApartados();

        facade.setEntityClass(ListaPersonal.class);
        evaluador = (ListaPersonal)facade.find(Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina()));
        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.init() evaluador: " + evaluador);

        facade.setEntityClass(PeriodosEscolares.class);
        periodoEscolar = (PeriodosEscolares)facade.find(evaluaciones360.getPeriodo());

        listaPersonalEvaluado = ejbEvaluacion360.getPersonalEvaluado(evaluador.getClave());
        listaPersonalEvaluado.forEach(lpe -> System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.init() lpe: " + lpe));
        evaluados = (int)listaPersonalEvaluado.stream().filter(lpe -> lpe.getCompleto()).count();
        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.init() evaluados: " + evaluados);

        listaPersonalEvaluando = listaPersonalEvaluado.subList(0, maxEvaluando > listaPersonalEvaluado.size()?listaPersonalEvaluado.size():maxEvaluando);
        listaPersonalEvaluando.forEach(lpe ->System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.init() evaluando a: " + lpe));

        clavesOpciones.clear();
        for(int i = 0; (i < maxEvaluando && i < listaPersonalEvaluando.size()); i++){
            clavesOpciones.add(listaPersonalEvaluado.get(i).getPk().getEvaluado());
        }
        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.init() claves opciones: " + clavesOpciones);

        initOpciones();

        initRespuestas();

        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.init() fin");
    }

    private void initOpciones(){
        opciones.clear();
        Integer index = 0;
        for (Integer clave : clavesOpciones) {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.init(9) clave: " + clave);
            opciones.put(index, new ArrayList<>());
            facade.setEntityClass(ListaPersonalEvaluacion360.class);
            ListaPersonalEvaluacion360 lpPrimero = (ListaPersonalEvaluacion360)facade.find(new Evaluaciones360ResultadosPK(evaluaciones360.getEvaluacion(), evaluador.getClave(), clave));
            opciones.get(index).add(lpPrimero);
            //opcionesSelect.get(index).add(e)
            for (ListaPersonalEvaluacion360 lp : listaPersonalEvaluado) {
//                System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.init(9) res: " + (!clavesOpciones.contains(lp.getPk().getEvaluado())) + ", lp: " + lp);
                if (!clavesOpciones.contains(lp.getPk().getEvaluado())) {
                    opciones.get(index).add(lp);
                }
            }

//            facade.setEntityClass(ListaPersonalEvaluacion360.class);
//            ListaPersonalEvaluacion360 lpe = (ListaPersonalEvaluacion360)facade.find(new Evaluaciones360ResultadosPK(evaluaciones360.getEvaluacion(), evaluador.getClave(), lpPrimero.getClave()));
            apartadosHabilidades.put(clave, ejbEvaluacion360.getApartadoHabilidades(lpPrimero.getCategoria()));
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.initOpciones() ah: " + apartadosHabilidades);
            index++;
        }
//        opciones.forEach((k,v) -> System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.initOpciones(" + k + "): " +  v ));
    }

    private void initRespuestas(){
        listaPersonalEvaluado.stream().map((subordinado) -> {
            respuestas.put(subordinado.getPk().getEvaluado(), new HashMap<>());
            return subordinado;
        }).forEachOrdered((subordinado) -> {
            Integer index = evaluaciones360.getEvaluaciones360ResultadosList().indexOf(new Evaluaciones360Resultados(subordinado.getPk()));
            Evaluaciones360Resultados resultado = evaluaciones360.getEvaluaciones360ResultadosList().get(index);
            System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.initRespuestas(10) resultado: " + resultado);
            for (float fi = 1.0f; fi <= 33.0f; fi++) {
                respuestas.get(subordinado.getPk().getEvaluado()).put(fi, ejbEvaluacion360.obtenerRespuestaPorPregunta(resultado, fi));
            }
        });
    }
    
    private void initPersonalEvaluando(){
        initPersonalEvaluado();
        listaPersonalEvaluando = new ArrayList<>();
        for(Integer clave:clavesOpciones){
            facade.setEntityClass(ListaPersonalEvaluacion360.class);
            ListaPersonalEvaluacion360 lpde = (ListaPersonalEvaluacion360)facade.find(new Evaluaciones360ResultadosPK(evaluaciones360.getEvaluacion(), evaluador.getClave(), clave));
            System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.initPersonalEvaluando() lpde: " + lpde);
            listaPersonalEvaluando.add(lpde);
        }
    }
    
    private void initPersonalEvaluado(){
        listaPersonalEvaluado = null;
        listaPersonalEvaluado = ejbEvaluacion360.getPersonalEvaluado(evaluador.getClave());
        listaPersonalEvaluado.forEach(lp -> System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionDesempenioAdmin.initPersonalEvaluado() lp: " + lp));
        evaluados = listaPersonalEvaluado.stream().filter(personal -> personal.getCompleto()).collect(Collectors.toList()).size();
        evaluando = listaPersonalEvaluado.stream().filter(personal -> personal.getIncompleto()).collect(Collectors.toList()).size();
    }

    public void guardarRespuesta(ValueChangeEvent e){
        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.guardarRespuesta() : " + e.getNewValue());
        UIComponent origen = (UIComponent)e.getSource();
        String[] datos = origen.getId().split("_");
        Float pregunta_id = Float.parseFloat(datos[0].replaceAll("-", "\\.").replaceAll("r", ""));
        Integer clave = Integer.parseInt(datos[1]);
        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.guardarRespuesta(" + pregunta_id + ") evaluado: " + clave + ", respuesta: " + e.getNewValue().toString() );

        //1 detectar si ya se tiene la respuesta, de lo contrario generarla
        Evaluaciones360Resultados resultado = new Evaluaciones360Resultados(evaluaciones360.getEvaluacion(), evaluador.getClave(), clave);
        ListaPersonalEvaluacion360 resultadoVista = new ListaPersonalEvaluacion360(resultado.getEvaluaciones360ResultadosPK());
        if(evaluaciones360.getEvaluaciones360ResultadosList().contains(resultado)){
            int index = evaluaciones360.getEvaluaciones360ResultadosList().indexOf(resultado);
            resultado = evaluaciones360.getEvaluaciones360ResultadosList().get(index);
            
            index = listaPersonalEvaluando.indexOf(resultadoVista);
            resultadoVista= listaPersonalEvaluando.get(index);
            ejbEvaluacion360.actualizarRespuestaPorPregunta(resultado, resultadoVista, pregunta_id, e.getNewValue().toString());
        }else{
            ejbEvaluacion360.actualizarRespuestaPorPregunta(resultado, resultadoVista, pregunta_id, e.getNewValue().toString());
            evaluaciones360.getEvaluaciones360ResultadosList().add(resultado);
        }
        ejbEvaluacion360.comprobarResultado(resultado, resultadoVista);
        facade.setEntityClass(resultado.getClass());
        facade.edit(resultado);
        facade.flush();
        
//        facade.setEntityClass(ListaPersonalEvaluacion360.class);
//        ListaPersonalEvaluacion360 lpe = (ListaPersonalEvaluacion360)facade.find(resultado.getEvaluaciones360ResultadosPK());
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.guardarRespuesta() lpe: " + lpe);
//
//        initPersonalEvaluando();
    }
    
    public void cambiarOpciones(ValueChangeEvent e) throws IOException {
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360dmin.cambiarOpciones():" + e.getNewValue());
//        listaPersonalEvaluando.forEach(lpe -> System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.cambiarOpciones() lpe: " + lpe));
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.cambiarOpciones() evaluando: " + listaPersonalEvaluando);
        SelectOneMenu origen = (SelectOneMenu) e.getSource();
        String id = origen.getId();
        Integer claveSeleccionada = Integer.parseInt(e.getNewValue().toString().split("evaluado=")[1].split("},")[0]); // lp.getClave();  //Integer.parseInt(e.getNewValue().toString());
        Integer posicion = Integer.parseInt(id.replace("opcion", ""));
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360dmin.cambiarOpciones() id: " + id);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360dmin.cambiarOpciones() claveSeleccionada: " + claveSeleccionada);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360dmin.cambiarOpciones() posición: " + posicion);

        clavesOpciones.set(posicion, claveSeleccionada);
        initOpciones();
        initRespuestas();
        Evaluaciones360ResultadosPK pk = new Evaluaciones360ResultadosPK(evaluaciones360.getEvaluacion(), evaluador.getClave(), claveSeleccionada);
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.cambiarOpciones(" + listaPersonalEvaluado.contains(new ListaPersonalEvaluacion360(pk)) + ") pk: " + pk);
        ListaPersonalEvaluacion360 lpe = listaPersonalEvaluado.get(listaPersonalEvaluado.indexOf(new ListaPersonalEvaluacion360(pk)));
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.cambiarOpciones() lpe: " + lpe);
        listaPersonalEvaluando.set(posicion, lpe);
        listaPersonalEvaluando.forEach(lpe1 -> System.out.println("mx.edu.utxj.pye.sgi.controlador.Evaluacion360Admin.cambiarOpciones(" + listaPersonalEvaluado.size() + "," + clavesOpciones.size() + ") lpe1: " + lpe1));
        Faces.refresh();
//        initPersonalEvaluando();
    }
    
    public void continuarEvaluando() throws IOException{
        init();
//        initPersonalEvaluado();
        Faces.refresh();
    }
}
