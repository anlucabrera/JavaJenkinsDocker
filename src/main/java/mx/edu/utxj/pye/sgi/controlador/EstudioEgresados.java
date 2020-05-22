package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ListaEstudiantesDtoTutor;
import mx.edu.utxj.pye.sgi.ejb.EjbEstudioEgresados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstudioEgresadosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionEstudioEgresados;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named(value = "estudioEgresados")
@SessionScoped
public class EstudioEgresados implements Serializable {
    private static final long serialVersionUID = -615669920932201958L;

    @Getter private Boolean finalizado;
    @Getter private Boolean cargada = false, sinRespuestas = false;
    @Getter private Boolean TSU, ING;
    
    @Getter private Alumnos alumnos;
    @Getter private Evaluaciones evaluacion;
    @Getter private Integer evaluador;
    
    @Getter @Setter private String  valor;
    @Getter @Setter private EvaluacionEstudioEgresadosResultados resultados;   
    @Getter @Setter private Generaciones generacion = new Generaciones();
    
    @Getter @Setter Map<String,String> respuestas;
    @Getter private List<SelectItem> selectItemCarreras;
    @Getter private List<SelectItem> respuestasPosibles;
    @Getter private List<Apartado> apartados;
    @Getter private List<Generaciones> generaciones, listaGeneraciones;
    
    @Getter @Setter private List<EvaluacionEstudioEgresadosResultados> listaResultados, listaResultadosReporte, listaResultadosGeneralesReporte;
    
    // administracion del modulo
    @Getter @Setter private String matricula = "";
    @Getter @Setter private List<ListaEstudiantesDtoTutor> listaEstudiante;
    @Getter @Setter private ListaEstudiantesDtoTutor Estudiante;
    @Getter @Setter private Integer nivel =0;
    @Getter @Setter private Boolean completo, noAccedio, noCompleto;
    
    //usuario invitado
    @Getter @Setter private Boolean iniciada = false;
    @Getter @Setter private Integer matriculaEvaluador;
    
    @EJB EjbEstudioEgresados ejb;
    @Inject LogonMB logonMB;
    
    @PostConstruct
    public void init() {
        validaReporte();
       // System.out.println("Evaluacion ->" + evaluacion);
        cargada = false;
        selectItemCarreras = ejb.selectItemsProgramasEducativos();
        listaGeneraciones = ejb.getGeneraciones();
        if (logonMB.getUsuarioTipo() == UsuarioTipo.ESTUDIANTE) {
            alumnos = ejb.procedimiento(logonMB.getCurrentUser());
            if(alumnos != null){
                ING = true;
            }

            try {
                // modificar para aperturar a onceavos o sextos
//                if(alumnos.getGradoActual() == 11){ //estudiantes egresadod de 11 vo
                if(ING){   //Estudiantes egresados de 6to grado
//                if ( alumnos.getGradoActual() == 11 || alumnos.getGradoActual() >=6 &&alumnos.getGradoActual() <=7) { //Estudiantes egresados de 6to y 11vo grado
                    finalizado = false;
                    respuestas = new HashMap<>();
                    generaciones = new ArrayList<>();
                    generaciones = ejb.getGeneraciones();
                    evaluacion = ejb.geteEvaluacionActiva();
                    if(evaluacion==null){return;}
                    evaluador = Integer.parseInt(logonMB.getCurrentUser());
                    resultados = ejb.getResultados(evaluacion, evaluador);
                    finalizado = ejb.actualizarResultado(resultados);
                    if (resultados != null) {
                        ejb.vaciarRespuestas(resultados, respuestas);
                        cargada = true;
                    }
                }
            } catch (Exception e) {
                cargada = false;
                //System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionControlInterno.init() e: " + e.getMessage());
            }
        } else if (logonMB.getUsuarioTipo() == UsuarioTipo.INVITADO) {
            Messages.addGlobalInfo("Bienvenido usuario invitado, esta es la evaluacion de estudio a egresados");

            finalizado = false;
            respuestas = new HashMap<>();
            generaciones = new ArrayList<>();
            generaciones = ejb.getGeneraciones();
            evaluacion = ejb.geteEvaluacionActiva();
////                    System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.init() evaluacion ---:::> " + evaluacion);
//            evaluador = Integer.parseInt(logonMB.getCurrentUser());
////                    System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.init() usuario ---:::>> " + evaluador);
//            resultados = ejb.getResultados(evaluacion, evaluador);
//            finalizado = ejb.actualizarResultado(resultados);
////                    System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.init() se ah finalizado la evaluacion? : " + finalizado);
//            if (resultados != null) {
//                ejb.vaciarRespuestas(resultados, respuestas);
////                        System.out.println("Se crearon los resultados " + resultados);
//                cargada = true;
//            }

        } else if (logonMB.getUsuarioTipo() == UsuarioTipo.TRABAJADOR) {
            if (logonMB.getPersonal().getCategoriaOperativa().getCategoria() == 19 || logonMB.getPersonal().getClave()==394) {
                validaReporte();
                if(evaluacion!=null){cargada=true;
                Messages.addGlobalInfo("Bienvenido a la administración de egresados...");}
                else{
                    cargada=false;
                }
            }
        } else {
            Messages.addGlobalWarn("Usted no deberia estar en este apartado, es solo para estudiantes");
        }
    }
    
    public void obtieneResultados() {
        if(resultados == null){
            Messages.addGlobalWarn("Este usuario no cuenta con respuestas para mostrar");
        }else if (resultados != null) {
            ejb.vaciarRespuestas(resultados, respuestas);
        }else{
            Messages.addGlobalWarn("Este usuario no pertenece a la ultima evaluación");
        }
    }

    public void guardarRespuesta(ValueChangeEvent e) {
        UIComponent origen = (UIComponent) e.getSource();
        if(e.getNewValue()==null){
        valor = "";            
        }else{
        valor = e.getNewValue().toString();
        }
        //System.out.println("id->" + origen + " Valor" + valor);
        ejb.actualizarRespuestaPorPregunta(resultados, origen.getId(), valor);
        finalizado = ejb.actualizarResultado(resultados);
        
    }  
    
    public void guardarRespuestaRemota(){
        String id = Faces. getRequestParameter("id");
        String valor = Faces.getRequestParameter("valor");
        respuestas.put(id, valor);
        ejb.actualizarRespuestaPorPregunta(resultados, id, valor);
        finalizado = ejb.actualizarResultado(resultados);
    }
    
    public void buscaEstudianteEstudioEgresado() {
        if (!matricula.equals("")) {
            Alumnos a = ejb.getAlumnoPorMatricula(matricula);
            if (a.getGradoActual() <= 10 && a.getGradoActual() >= 6) {
                nivel = 1;
            } else if (a.getGradoActual() == 11) {
                nivel = 2;
            } else {
                nivel = 3;
            }
            Personas p = ejb.getDatosPersonalesAlumnos(a.getAlumnosPK().getCveAlumno());
            Estudiante = new ListaEstudiantesDtoTutor(Integer.parseInt(matricula), p.getNombre() + " " + p.getApellidoPat() + " " + p.getApellidoMat(), Integer.parseInt(a.getGradoActual().toString()), a.getGrupos().getIdGrupo(), "");
            resultados = ejb.getResultadoIndividual(matricula);
            if (resultados != null) {
                respuestas = new HashMap<>();
                generaciones = ejb.getGeneraciones();
                resultados = ejb.getResultadoIndividual(matricula);
                finalizado = ejb.actualizarResultado(resultados);
                Comparador<EvaluacionEstudioEgresadosResultados> comparador = new ComparadorEvaluacionEstudioEgresados();
                completo = comparador.isCompleto(resultados);
                
                if (resultados == null) {
                    sinRespuestas = true;
                    Messages.addGlobalWarn("Este usuario no cuenta con respuestas para mostrar");
                } else if (respuestas.containsKey("p0001") && respuestas.containsValue("")) {

                } else {
                    sinRespuestas = false;
                }
                if (!completo) {
                    noCompleto = true;
                    completo = false;
                    noAccedio = false;
                } else {
                    noAccedio = false;
                    noCompleto = false;
                }
            } else {
                noAccedio = true;
                completo = false;
                noCompleto = false;
            }
        }
    }

    public void validaReporte() {
        Evaluaciones evaluacion1 = new Evaluaciones();
        evaluacion1 = ejb.geteEvaluacionActiva();
       // System.out.println("Evaluacion en reporte " + evaluacion1);
        if(evaluacion1==null){
        evaluacion1 = ejb.getLastEvaluacion();
        }
        evaluacion = evaluacion1;

        listaResultadosReporte = new ArrayList<>();
        listaResultadosReporte.clear();
        listaResultadosGeneralesReporte = ejb.getResultadosEvActiva(evaluacion1.getEvaluacion());
        listaResultadosGeneralesReporte.forEach(x -> {
            Comparador<EvaluacionEstudioEgresadosResultados> comparador = new ComparadorEvaluacionEstudioEgresados();
            if (comparador.isCompleto(x)) {
                listaResultadosReporte.add(x);
            }
        });

        //System.err.println("la lista final contiene : " + listaResultadosReporte.size());
//                if (listaResultadosReporte == null || listaResultadosReporte.isEmpty()) {
//                    listaResultadosReporte.clear();
//                    Messages.addGlobalInfo("No se encontraron registros de este estudio, si considera esto un error contacte con el administador ");
//                } else {
//                    Messages.addGlobalInfo("Se encontraron los registros");
//                }

    }

    public void generaNuevaEvaluación() {        
        if (evaluacion == null || matricula == null) {
            Messages.addGlobalWarn("Ocurrio un error inesperado, contacte con un administrador");
        } else {
            resultados = ejb.getResultados(evaluacion, Integer.parseInt(matricula));            
            if (resultados == null) {
                iniciada = false;
                Messages.addGlobalWarn("Ocurrio un error inesperado, contacte con un administrador");
            } else {
                //System.err.println("Los resultados son : " + resultados);
                iniciada = true;
            }
        }
    }
    
    public void buscaEstudianteEstudioEgresadoTramiteTitulacion() {
        if (!matricula.equals("")) {
            resultados = ejb.getResultadoIndividual(matricula);
            if (resultados != null) {
                
                if (resultados.getR2() != null && resultados.getR3()==null) {
                    nivel = 1;
                } else if (resultados.getR3() !=null) {
                    nivel = 2;
                } else {
                    nivel = 3;
                }
                
                
                respuestas = new HashMap<>();
                generaciones = ejb.getGeneraciones();
                resultados = ejb.getResultadoIndividual(matricula);
                
                Comparador<EvaluacionEstudioEgresadosResultados> comparador = new ComparadorEvaluacionEstudioEgresados();
                completo = comparador.isCompleto(resultados);
                //System.err.println("el resultado esta completo ? : " + completo);
                finalizado = ejb.actualizarResultado(resultados);
                if (resultados == null) {
                    sinRespuestas = true;
                    Messages.addGlobalWarn("Este usuario no cuenta con respuestas para mostrar");
                } else if (respuestas.containsKey("p0001") && respuestas.containsValue("")) {

                } else {
                    sinRespuestas = false;
                }
                 if (!completo) {
                    noCompleto = true;
                    completo = false;
                    noAccedio = false;
                } else {
                    noAccedio = false;
                    noCompleto = false;
                }
            } else {
                noAccedio = true;
                completo = false;
                noCompleto = false;
            }
        }
    }
    
    
}
