package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
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
import mx.edu.utxj.pye.sgi.entity.sescolares.Alumno;
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
    @Getter private Boolean cargada, sinRespuestas = false;
    @Getter private Boolean TSU, ING;
    
    @Getter private Alumnos alumnos;
    @Getter private Evaluaciones evaluacion;
    @Getter private Integer evaluador;
    @Getter @Setter private EvaluacionEstudioEgresadosResultados resultados;
    
    @Getter @Setter Map<String,String> respuestas;
    @Getter private List<SelectItem> selectItemCarreras;
    @Getter private List<SelectItem> respuestasPosibles;
    @Getter private List<Apartado> apartados;
    @Getter private List<Generaciones> generaciones;
    
    // administracion del modulo
    @Getter @Setter private String matricula = "";
    @Getter @Setter private List<ListaEstudiantesDtoTutor> listaEstudiante;
    @Getter @Setter private ListaEstudiantesDtoTutor Estudiante;
    @Getter @Setter private Integer nivel =0;
    @Getter @Setter private Boolean completo, noAccedio, noCompleto;
    
    @EJB EjbEstudioEgresados ejb;
    @Inject LogonMB logonMB;
    
    @PostConstruct
    public void init() {
        
        if (logonMB.getUsuarioTipo() == UsuarioTipo.ESTUDIANTE) {
            alumnos = ejb.getAlumnoPorMatricula(logonMB.getCurrentUser());
            selectItemCarreras = ejb.selectItemsProgramasEducativos();
            if (alumnos.getGradoActual() == 11) {
                ING = true;
                System.out.println("Egreso de ING");
            } else  if (alumnos.getGradoActual() >=6 &&alumnos.getGradoActual() < 11) {
                System.out.println("Egreso de TSU");
                TSU = false;
            }
            try {
                System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.init() entra al try del init");
                if ( alumnos.getGradoActual() == 11 /*|| alumnos.getGradoActual() >=6 &&alumnos.getGradoActual() < 1*/ ) {
                    System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.init()´pasa el if");
                    finalizado = false;
                    respuestas = new HashMap<>();
                    generaciones = ejb.getGeneraciones();
                    System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.init() generaciones : " + generaciones);
                    evaluacion = ejb.geteEvaluacionActiva();
                    System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.init() evaluacion ---:::> " + evaluacion);
                    evaluador = Integer.parseInt(logonMB.getCurrentUser());
                    System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.init() usuario ---:::>> " + evaluador);
                    resultados = ejb.getResultados(evaluacion, evaluador);
                    finalizado = ejb.actualizarResultado(resultados);
                    System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.init() se ah finalizado la evaluacion? : " + finalizado);
                    if (resultados != null) {
                        ejb.vaciarRespuestas(resultados, respuestas);
                        System.out.println("Se crearon los resultados " + resultados);
                        cargada = true;
                    }
                }
            } catch (Exception e) {
                cargada = false;
                System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionControlInterno.init() e: " + e.getMessage());
            }
        }else if (logonMB.getUsuarioTipo() == UsuarioTipo.TRABAJADOR) {
            if (logonMB.getPersonal().getCategoriaOperativa().getCategoria() == 19) {
                Messages.addGlobalInfo("Bienvenido a la administración de egresados...");
            }
        } else {
            Messages.addGlobalWarn("Usted no deberia estar en este apartado, es solo para estudiantes");
        }
    }
    
    public void getResultados() {
        System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.getResultados() entra al metodo");
        System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.getResultados() obtiene la ultima evaluacion" +ejb.getLastEvaluacion());
//        selectItemCarreras = ejb.selectItemsProgramasEducativos();
        
        if(resultados == null){
            Messages.addGlobalWarn("Este usuario no cuenta con respuestas para mostrar");
        }else if (resultados != null) {
            ejb.vaciarRespuestas(resultados, respuestas);
            System.out.println("Se crearon los resultados " + resultados);
        }else{
            Messages.addGlobalWarn("Este usuario no pertenece a la ultima evaluación");
        }
    }

    public void guardarRespuesta(ValueChangeEvent e) {
        System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.guardarRespuesta() ejecuta el metodo:::");
        UIComponent origen = (UIComponent) e.getSource();
        String valor = e.getNewValue().toString();
//        ejb.actualizarRespuestaPorPregunta(resultados, origen.getId(), valor, respuestas);
        ejb.actualizarRespuestaPorPregunta(resultados, origen.getId(), valor);
        System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.guardarRespuesta() valor : " + valor+", id : " +origen.getId());
        finalizado = ejb.actualizarResultado(resultados);
        
    }  
    
    public void guardarRespuestaRemota(){
        
        String id = Faces. getRequestParameter("id");
        System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.guardarRespuestaRemota() requestParameter : " + id);
        String valor = Faces.getRequestParameter("valor");
        System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.guardarRespuestaRemota() requestParameter : " + valor);
        respuestas.put(id, valor);
        ejb.actualizarRespuestaPorPregunta(resultados, id, valor);
        System.out.println("mx.edu.utxj.pye.sgi.controlador.EstudioEgresados.guardarRespuestaRemota() respuestas: " + respuestas);
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
}
