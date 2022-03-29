/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.evaluaciones;

import edu.mx.utxj.pye.seut.util.util.Cuestionario;
import java.io.IOException;
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
import javax.inject.Named;
import javax.persistence.EntityManager;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.cuestionario.CuestionarioEvaluacionDesempenio;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonalDesempenioEvaluacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ExcelWritter;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.primefaces.component.selectonemenu.SelectOneMenu;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import javax.servlet.http.HttpServletRequest;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacionDesempenioPromedios;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;



/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class EvaluacionDesempenioAdmin extends ViewScopedRol implements Desarrollable{

    private static final long serialVersionUID = -2104785351397760456L;

    @Getter private final Integer maxEvaluando = 4;
    @Getter private Integer evaluados = 0;
    @Getter private Integer evaluando = 0;
    @Getter private Boolean cargada = false;
    @Getter @Setter private NivelRol nivel;

    @Getter private final Cuestionario cuestionario = new CuestionarioEvaluacionDesempenio();
    @Getter private DesempenioEvaluaciones desempenioEvaluacion;
    @Getter private PeriodosEscolares periodoEscolar;
    @Getter @Setter private Personal persona;
    @Getter @Setter private ListaPersonal directivoSeleccionado;

    @Getter private List<Apartado> apartados;
    @Getter private List<Personal> listaPersonal;
    @Getter private List<ListaPersonal> listaDirectivos;
    @Getter private List<ListaPersonal> listaSubordinados;
    @Getter private List<ListaPersonalDesempenioEvaluacion> listaPersonalEvaluando;
    @Getter private List<SelectItem> respuestasPosibles;
    
    @Getter private List<ListaPersonalDesempenioEvaluacion> listaPersonalEvaluado, listaPersonalEvaluadoAntes;
    @Getter private List<ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio> listaEvaluados, listaEvaluando;
    @Getter private final Map<Integer, List<Personal>> opciones = new HashMap<>();

    @Getter @Setter Map<Integer, Map<Float, Object>> respuestas = new HashMap<>();
    @Getter @Setter List<Integer> clavesOpciones = new ArrayList<>();

    @EJB private EjbEvaluacionDesempenio evaluacionDesempenioEJB;
    @EJB private EjbPropiedades ep;
    @EJB private Facade f;
    private EntityManager em;
    @Inject private LogonMB logonMB;


    @Getter private Boolean cargado = false, tieneAcceso;



    @PostConstruct
    public void init() {
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.EVALUACION_DESEMPENIO);
            
            ResultadoEJB<DesempenioEvaluaciones> resEvaluacion = evaluacionDesempenioEJB.obtenerEvaluacionActiva();
            if(!resEvaluacion.getCorrecto()) return;
            desempenioEvaluacion = resEvaluacion.getValor();
            
            ResultadoEJB<Personal> resValidacion = evaluacionDesempenioEJB.validarPersona(Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina()));
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
            persona = resValidacion.getValor();
            tieneAcceso = Boolean.TRUE;
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            
            periodoEscolar = evaluacionDesempenioEJB.obtenerPeriodo(desempenioEvaluacion.getPeriodo()).getValor();
            
            respuestasPosibles = evaluacionDesempenioEJB.getRespuestasPosibles();
            apartados = evaluacionDesempenioEJB.getApartados(cuestionario);
            
            listaPersonal = evaluacionDesempenioEJB.obtenerListaSubordinados(persona).getValor();
            evaluacionDesempenioEJB.cargarResultadosAlmacenados(desempenioEvaluacion, persona, listaPersonal);
            initPersonalEvaluado();
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if (!tieneAcceso) {mostrarMensajeNoAcceso();return;}
            if (!resEvaluacion.getCorrecto()) {mostrarMensajeResultadoEJB(resEvaluacion);}
            nivel = NivelRol.OPERATIVO;
            clavesOpciones.clear();
            for (int i = 0; (i < maxEvaluando && i < listaPersonal.size()); i++) {
                clavesOpciones.add(listaPersonal.get(i).getClave());
            }

            //paso 9 inicializar opciones
            initOpciones();

            //paso 10 inicializar mapeo de respuestas por clave subordinado y numero de pregunta
            initRespuestas();

            //paso 11 definir la lista del personal que se esta evaluando
            initPersonalEvaluando();

            initPersonalEvaluado();
            
            
            em = f.getEntityManager();
        } catch (Exception e) {
            //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.init() e: " + e.getMessage());
            cargada = false;
        }
    }

    private void initOpciones() {
        opciones.clear();
        Integer index = 0;
        for (Integer clave : clavesOpciones) {
//            //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.init(9) clave: " + clave);
            opciones.put(index, new ArrayList<>());
            //facade.setEntityClass(ListaPersonal.class);
            Personal lpPrimero = evaluacionDesempenioEJB.obtenerPersona(clave).getValor();//(ListaPersonal) facade.find(clave);
            opciones.get(index).add(lpPrimero);
            //opcionesSelect.get(index).add(e)
            for (Personal lp : listaPersonal) {
//                //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.init(9) res: " + (!clavesOpciones.contains(lp.getClave()) || clave.equals(lp.getClave())) + ", lp: " + lp);
                if ((!clavesOpciones.contains(lp.getClave()) || clave.equals(lp.getClave())) && !opciones.get(index).contains(lp)) {
                    opciones.get(index).add(lp);
                }
            }
            index++;
        }
//        opciones.forEach((k,v) -> //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.initOpciones(" + k + "): " +  v ));
    }

    private void initRespuestas() {
        listaPersonal.stream().map((subordinado) -> {
            respuestas.put(subordinado.getClave(), new HashMap<>());
            return subordinado;
        }).forEachOrdered((subordinado) -> {
            Integer index = desempenioEvaluacion.getDesempenioEvaluacionResultadosList().indexOf(new DesempenioEvaluacionResultados(desempenioEvaluacion.getEvaluacion(), persona.getClave(), subordinado.getClave()));
            DesempenioEvaluacionResultados resultado = desempenioEvaluacion.getDesempenioEvaluacionResultadosList().get(index);
//            //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.init(10) resultado: " + resultado);
            for (float fi = 1.0f; fi <= 21.0f; fi++) {
                respuestas.get(subordinado.getClave()).put(fi, evaluacionDesempenioEJB.obtenerRespuestaPorPregunta(resultado, fi));
            }
        });
    }

    private void initPersonalEvaluando() {
        initPersonalEvaluado();
        listaEvaluando = new ArrayList<>();
        clavesOpciones.stream().map((clave) -> {
            //facade.setEntityClass(ListaPersonalDesempenioEvaluacion.class);
            return clave;
        }).forEachOrdered((clave) -> {
            ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio dto = listaEvaluados.stream()
                    .filter(x -> x.getEvaluado().getClave().equals(clave))
                    .findFirst().orElse(new ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio());
            if(dto.equals(new ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio())) return;
            listaEvaluando.add(dto);
//            listaPersonalEvaluando.add((ListaPersonalDesempenioEvaluacion)facade.find(new DesempenioEvaluacionResultadosPK(desempenioEvaluacion.getEvaluacion(), evaluador.getClave(), clave)));
        });//        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.init(11) evaluando: " + listaPersonalEvaluando.toString());
    }

    private void initPersonalEvaluado() {
        listaEvaluados = new ArrayList();
        listaEvaluados = evaluacionDesempenioEJB.obtenerListaResultadosEvaluacionEvaluador(desempenioEvaluacion.getEvaluacion(), persona.getClave()).getValor();
//        listaPersonalEvaluado.forEach(lp -> //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.initPersonalEvaluado() lp: " + lp));
        evaluados = listaEvaluados.stream().filter(personal -> personal.getCompleto().equals("1")).collect(Collectors.toList()).size();
        evaluando = listaEvaluados.stream().filter(personal -> personal.getIncompleto().equals("0")).collect(Collectors.toList()).size();
    }

    public void guardarRespuesta(ValueChangeEvent e) {
        UIComponent origen = (UIComponent) e.getSource();
        String[] datos = origen.getId().split("_");
        Float pregunta_id = Float.parseFloat(datos[0].replaceAll("-", "\\.").replaceAll("r", ""));
        Integer clave = Integer.parseInt(datos[1]);
//        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.guardarRespuesta(" + pregunta_id + ") evaluado: " + clave + ", respuesta: " + e.getNewValue() );

        //1 detectar si ya se tiene la respuesta, de lo contrario generarla
        DesempenioEvaluacionResultados resultado = new DesempenioEvaluacionResultados(desempenioEvaluacion.getEvaluacion(), persona.getClave(), clave);
        if (desempenioEvaluacion.getDesempenioEvaluacionResultadosList().contains(resultado)) {
            int index = desempenioEvaluacion.getDesempenioEvaluacionResultadosList().indexOf(resultado);
            resultado = desempenioEvaluacion.getDesempenioEvaluacionResultadosList().get(index);
            evaluacionDesempenioEJB.actualizarRespuestaPorPregunta(resultado, pregunta_id, e.getNewValue().toString());
        } else {
            evaluacionDesempenioEJB.actualizarRespuestaPorPregunta(resultado, pregunta_id, e.getNewValue().toString());
            desempenioEvaluacion.getDesempenioEvaluacionResultadosList().add(resultado);
        }
        //facade.setEntityClass(resultado.getClass());
        f.edit(resultado);
        //em.merge(resultado);

        initPersonalEvaluando();

//        facade.setEntityClass(ListaPersonalDesempenioEvaluacion.class);
//        ListaPersonalDesempenioEvaluacion lpde = (ListaPersonalDesempenioEvaluacion)facade.find(resultado.getPk());
//        facade.getEntityManager().refresh(lpde);
//        listaPersonalEvaluando.set(listaPersonalEvaluando.indexOf(lpde), lpde);
//        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.guardarRespuesta() evaluacion: " + desempenioEvaluacion);
    }

    public void cambiarOpciones(ValueChangeEvent e) throws IOException {
//        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.cambiarOpciones():" + e.getNewValue());
        SelectOneMenu origen = (SelectOneMenu) e.getSource();
        String id = origen.getId();
        Integer claveSeleccionada = Integer.parseInt(e.getNewValue().toString().split("clave=")[1].split(" ]")[0]); // lp.getClave();  //Integer.parseInt(e.getNewValue().toString());
        Integer posicion = Integer.parseInt(id.replace("opcion", ""));
//        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.cambiarOpciones() id: " + id);
//        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.cambiarOpciones() claveSeleccionada: " + claveSeleccionada);
//        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.cambiarOpciones() posición: " + posicion);

        clavesOpciones.set(posicion, claveSeleccionada);
        initOpciones();
        initRespuestas();
        initPersonalEvaluando();
//        Faces.redirect("desempenio.xhtml");
    }

    public void continuarEvaluando() throws IOException {
        init();
        initPersonalEvaluado();
//        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.continuarEvaluando(1) clavesOpciones: " + clavesOpciones);

        clavesOpciones.clear();
        int total = 0;
        for (ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio lpde : listaEvaluados.stream().filter(personal -> personal.getCompleto().equals("1")).collect(Collectors.toList())) {
//            //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.continuarEvaluando() incompleto: " + lpde);
            if (total < 4) {
                clavesOpciones.add(lpde.getEvaluado().getClave());
                total++;
            } else {
                break;
            }
        }

        if (clavesOpciones.size() < 4) {
            for (ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio lpde : listaEvaluados.stream().filter(personal -> personal.getIncompleto().equals("0")).collect(Collectors.toList())) {
//                //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.continuarEvaluando() completo: " + lpde);
                if (total < 4) {
                    clavesOpciones.add(lpde.getEvaluado().getClave());
                    total++;
                } else {
                    break;
                }
            }
        }

        initPersonalEvaluando();
        initOpciones();

//        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.continuarEvaluando(2) clavesOpciones: " + clavesOpciones);
        Faces.refresh();
    }

    public String getId(@NonNull Float preguntaNumero, @NonNull Integer trabajadorClave) {
        String id = "r" + preguntaNumero.toString().replaceAll("\\.", "-") + "_" + trabajadorClave.toString();
//        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.getId(): " + id);
        return id;
    }

    public void descargarCedulas() {
        ExcelWritter ew = new ExcelWritter(directivoSeleccionado, desempenioEvaluacion, getListaPersonalEvaluado(), periodoEscolar);
        ew.obtenerLibro();
        ew.editarLibro();
        ew.escribirLibro();
        String ruta = ew.enviarLibro();
        Ajax.oncomplete("descargar('" + ew.enviarLibro() + "');");

//        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.descargarCedulas() ruta: " + ruta);
    }

    public void descargarCedulasDestiempo() throws IOException {
        listaPersonalEvaluadoAntes = new ArrayList<>();
        
        DesempenioEvaluaciones ultimaEvaluacion = evaluacionDesempenioEJB.getUltimaEvaluacionDesempenio();
        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.descargarCedulasDestiempo() la ultima evaluacion es : "+ ultimaEvaluacion);
        
        directivoSeleccionado = em.find(ListaPersonal.class,logonMB.getPersonal().getClave());
        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.descargarCedulasDestiempo() el directivo es : "+ evaluador);
        
        listaSubordinados = evaluacionDesempenioEJB.getListaSubordinados(directivoSeleccionado);
        
        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.descargarCedulasDestiempo() los subordinados actuales son : " + listaSubordinados);
        evaluacionDesempenioEJB.cargarResultadosAlmacenados(ultimaEvaluacion, directivoSeleccionado, listaSubordinados);
        
        
        PeriodosEscolares periodoSeleccionado = evaluacionDesempenioEJB.getPeriodoDeLaEvaluacionDesempenio(ultimaEvaluacion.getPeriodo());
        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.descargarCedulasDestiempo() el periodo seleccionado = : " + periodoSeleccionado);
        List<ListaPersonalDesempenioEvaluacion> l =
        /*listaPersonalEvaluadoAntes =*/ evaluacionDesempenioEJB.obtenerListaResultadosPorEvaluacionEvaluador(ultimaEvaluacion, directivoSeleccionado);
        //l.forEach(System.out::println);
        l.forEach(x -> {
            if(x.isCompleto()){
                listaPersonalEvaluadoAntes.add(x);
            }
        });
        ExcelWritter ew = new ExcelWritter(directivoSeleccionado, ultimaEvaluacion, getListaPersonalEvaluadoAntes(), periodoSeleccionado);
        ew.obtenerLibro();
        ew.editarLibro();
        ew.escribirLibro();
        String ruta = ew.enviarLibro();
        Ajax.oncomplete("descargar('" + ew.enviarLibro() + "');");

    }
    public void descargarCedulasDestiempoIndex() throws IOException {
        listaPersonalEvaluadoAntes = new ArrayList<>();
        
        DesempenioEvaluaciones ultimaEvaluacion = evaluacionDesempenioEJB.getUltimaEvaluacionDesempenio();
        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.descargarCedulasDestiempo() la ultima evaluacion es : "+ ultimaEvaluacion);
        
        directivoSeleccionado = em.find(ListaPersonal.class, Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina()));//(ListaPersonal) facade.find(Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina()));
        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.descargarCedulasDestiempo() el directivo es : "+ evaluador);
        
        listaSubordinados = evaluacionDesempenioEJB.getListaSubordinados(directivoSeleccionado);
        
        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.descargarCedulasDestiempo() los subordinados actuales son : " + listaSubordinados);
        evaluacionDesempenioEJB.cargarResultadosAlmacenados(ultimaEvaluacion, directivoSeleccionado, listaSubordinados);
        
        
        PeriodosEscolares periodoSeleccionado = evaluacionDesempenioEJB.getPeriodoDeLaEvaluacionDesempenio(ultimaEvaluacion.getPeriodo());
        //System.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.descargarCedulasDestiempo() el periodo seleccionado = : " + periodoSeleccionado);
        List<ListaPersonalDesempenioEvaluacion> l =
        /*listaPersonalEvaluadoAntes =*/ evaluacionDesempenioEJB.obtenerListaResultadosPorEvaluacionEvaluador(ultimaEvaluacion, directivoSeleccionado);
        //l.forEach(System.out::println);
        l.forEach(x -> {
            if(x.isCompleto()){
                listaPersonalEvaluadoAntes.add(x);
            }
        });
        ExcelWritter ew = new ExcelWritter(directivoSeleccionado, ultimaEvaluacion, getListaPersonalEvaluadoAntes(), periodoSeleccionado);
        ew.obtenerLibro();
        ew.editarLibro();
        ew.escribirLibro();
        String ruta = ew.enviarLibro();
        ew.descargaCedulasOmnifaces();

    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "evaluacion de desempenio";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
////        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
}
