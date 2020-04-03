package mx.edu.utxj.pye.sgi.controlador.evaluaciones;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoEstudianteMateria;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import mx.edu.utxj.pye.sgi.ejb.EJBAdimEstudianteBase;
import mx.edu.utxj.pye.sgi.ejb.EJBEvaluacionDocenteMateria;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacionDocente2;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados2;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados3;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author  Taatisz :P
 */
@Named
@ViewScoped
public class EvaluacionDocenteEstudiante extends ViewScopedRol {

    @Getter private Boolean cargada = false;
    @Getter private Boolean finalizado = false;
    @Getter private Evaluaciones evaluacion;
    @Getter private PeriodosEscolares periodoEscolar;
    @Getter @Setter dtoEstudiantesEvalauciones estudianteEvaluacion;
    @Getter @Setter int totalDocentes, evaluados2;
    @Getter @Setter int tipoEvaluacion;
    @Getter @Setter double porcentaje;
    @Getter @Setter dtoEstudianteMateria dtoDocenteEvaluando;
    @Getter @Setter EvaluacionDocentesMateriaResultados resultadosEvaluando;
    @Getter @Setter Boolean estudianteSauiit, estudianteCE;
    @Getter @Setter List<dtoEstudianteMateria> listaMaterias,listaResultados;
    @Getter private List<Apartado> preguntas;
    @Getter private List<SelectItem> respuestasPosibles;
    @Getter private String valor;
    @Inject LogonMB logonMB;
    @EJB EjbEvaluacionDocente2 ejbEvaluacionDocente2;
    @EJB EJBAdimEstudianteBase ejbAdminEstudiante;

    @PostConstruct
    public  void  init(){
        //System.out.println("EvaluacionDocenteEstudiante.init" +logonMB.getCurrentUser());
        setVistaControlador(ControlEscolarVistaControlador.EVALUACION_DOCENTE2);
        //Se busca evaluacion activa
        ResultadoEJB<Evaluaciones> resEvaluacion = ejbEvaluacionDocente2.getEvDocenteActiva();
        if(resEvaluacion.getCorrecto()==true){
            evaluacion = resEvaluacion.getValor();

            //Se obtiene al estudiante
            ResultadoEJB<dtoEstudiantesEvalauciones> resEstudiante = ejbAdminEstudiante.getClaveEstudiante(logonMB.getCurrentUser(), evaluacion.getPeriodo());
            if(resEstudiante.getCorrecto()==true){
                //Obtuvo al estudiante, sólo hay que obetener sus materias identificando en que base estan registrados y quitar a estudiantes de 6to y 11vo
                estudianteEvaluacion = resEstudiante.getValor();
                if(estudianteEvaluacion.getEstudianteCE() !=null){
                    estudianteCE = true;
                }
                if(estudianteEvaluacion.getEstudianteSaiiut() !=null){
                    estudianteSauiit =true;
                }
                if(estudianteEvaluacion.getGrado()==6 || estudianteEvaluacion.getGrado() ==11){cargada=false; }
                else {
                    ResultadoEJB<List<dtoEstudianteMateria>> resMaterias = ejbEvaluacionDocente2.getMateriasbyEstudiante(estudianteEvaluacion,evaluacion);
                    if(resMaterias.getCorrecto()==true){
                        listaMaterias= resMaterias.getValor();
                        //Se verifica que tipo de evaluación se va a realizar (tipo 1= evaluación normal tipo 2= evaluacion por contingencia, aulas virtuales)
                        ResultadoEJB<Integer> restipoEvaluacion = ejbEvaluacionDocente2.getTipoEvaluacion();
                        if(restipoEvaluacion.getCorrecto()==true){
                            tipoEvaluacion= restipoEvaluacion.getValor();
                            if(tipoEvaluacion ==1){
                                //Evaluacion docente tipo 1 (normal)
                                getResultados();
                                preguntas = ejbEvaluacionDocente2.getApartados();

                            }else if(tipoEvaluacion == 2){
                                //evaluacion docente por coontegencia de salud
                                getResultadosTipo2();
                                preguntas = ejbEvaluacionDocente2.getApartadosContingencia();
                            }

                        }else {mostrarMensajeResultadoEJB(restipoEvaluacion);}
                        respuestasPosibles = ejbEvaluacionDocente2.getRespuestasPosibles();
                        cargada =true;
                    }else {mostrarMensajeResultadoEJB(resMaterias);}

                }
            }


        }else {mostrarMensajeResultadoEJB(resEvaluacion);}
    }

    /**
     * Obtiene los resultados del estudiante logueado (Evaluación tipo1)
     */
    public void getResultados(){
        listaResultados = new ArrayList<>();
        //Se recorre la lista de materias que cursa el estudiante (Esta lista ya tiene quien imparte la materia
        listaMaterias.stream().forEach(m->{
            EvaluacionDocentesMateriaResultados2 resultados = new EvaluacionDocentesMateriaResultados2();
            dtoEstudianteMateria materia = new dtoEstudianteMateria();
            //Obtienen los datos ---> Aqui si no existen los crea dentro del metodo que se llama en el ejb
            ResultadoEJB<EvaluacionDocentesMateriaResultados2> resResultados = ejbEvaluacionDocente2.getResultadobyEvaluadorEvaluado(estudianteEvaluacion,m,evaluacion);
            if(resResultados.getCorrecto()==true){
                //Se le asigna el valor
                resultados = resResultados.getValor();
                //Se agrega a la lista de resultados
                materia.setResultados2(resultados);
                // System.out.println("Resultados "+ materia.getResultados().getR1());
                materia.setNombreMateria(m.getNombreMateria());
                materia.setClaveMateria(m.getClaveMateria());
                materia.setDocenteImparte(m.getDocenteImparte());
                //  System.out.println("Materia " + materia);
                listaResultados.add(materia);
                //System.out.println("EvaluacionDocenteEstudiante.getResultados");
            }else {mostrarMensajeResultadoEJB(resResultados);}
        });
        totalDocentes = listaResultados.size();
        //System.out.println("EvaluacionDocenteEstudiante.getResultados "+ listaResultados);
        evaluados2 = listaResultados.stream().filter(docente -> docente.getResultados2().getCompleto()).collect(Collectors.toList()).size();
        Double dte = new Double(totalDocentes);
        Double dc= new Double(evaluados2);
        porcentaje = (dc * 100) / dte;
        if(totalDocentes==evaluados2){finalizado =true;}
        else {finalizado =false;}
        //  System.out.println("Evaluados" + evaluados2 + " Porcentaje  " + porcentaje);
        //  System.out.println("Resultados" + listaResultados);
        //Una vez obtenida la lista de resultados, filtramos por las que ya estan completas y las que no
        // System.out.println("Resultados filtrados " + listaDocentesEvaluados + "  " + listaDocentesEvaluando);
    }
    /**
     * Obtiene los resultados del estudiante logueado (Evaluación tipo 2)
     */
    public void getResultadosTipo2(){
        listaResultados = new ArrayList<>();
        //Se recorre la lista de materias que cursa el estudiante (Esta lista ya tiene quien imparte la materia
        listaMaterias.stream().forEach(m->{
            EvaluacionDocentesMateriaResultados3 resultados = new EvaluacionDocentesMateriaResultados3();
            dtoEstudianteMateria materia = new dtoEstudianteMateria();
            //Obtienen los datos ---> Aqui si no existen los crea dentro del metodo que se llama en el ejb
            ResultadoEJB<EvaluacionDocentesMateriaResultados3> resResultados = ejbEvaluacionDocente2.getResultadosTipo2byEvaluadorEvaluado(estudianteEvaluacion,m,evaluacion);
            if(resResultados.getCorrecto()==true){
                //Se le asigna el valor
                resultados = resResultados.getValor();
                //Se agrega a la lista de resultados
                materia.setResultadosTipo2(resultados);
                // System.out.println("Resultados "+ materia.getResultados().getR1());
                materia.setNombreMateria(m.getNombreMateria());
                materia.setClaveMateria(m.getClaveMateria());
                materia.setDocenteImparte(m.getDocenteImparte());
                //  System.out.println("Materia " + materia);
                listaResultados.add(materia);
                //System.out.println("EvaluacionDocenteEstudiante.getResultados");
            }else {mostrarMensajeResultadoEJB(resResultados);}
        });
        totalDocentes = listaResultados.size();
        //System.out.println("EvaluacionDocenteEstudiante.getResultados "+ listaResultados);
        evaluados2 = listaResultados.stream().filter(docente -> docente.getResultadosTipo2().getCompleto()).collect(Collectors.toList()).size();
        Double dte = new Double(totalDocentes);
        Double dc= new Double(evaluados2);
        porcentaje = (dc * 100) / dte;
        if(totalDocentes==evaluados2){finalizado =true;}
        else {finalizado =false;}
        //  System.out.println("Evaluados" + evaluados2 + " Porcentaje  " + porcentaje);
        //  System.out.println("Resultados" + listaResultados);
        //Una vez obtenida la lista de resultados, filtramos por las que ya estan completas y las que no
        // System.out.println("Resultados filtrados " + listaDocentesEvaluados + "  " + listaDocentesEvaluando);
    }
    /*
    Guarda la respuesta (Evaluacion docente tipo 1)
     */
    public void saveRespueta(ValueChangeEvent e){
        UIComponent id = (UIComponent)e.getSource();

        if(e.getNewValue() != null){
            valor = e.getNewValue().toString();
        }else{
            valor = e.getOldValue().toString();
        }
        ResultadoEJB<EvaluacionDocentesMateriaResultados2> resActualiza = ejbEvaluacionDocente2.actualizaRespuestaPorPregunta2(dtoDocenteEvaluando.getResultados2(),id.getId(),valor);
        if(resActualiza.getCorrecto()==true){
            //System.out.println("Pregunta: "+ id.getId()+"Valor ----------->" +valor);
            EvaluacionDocentesMateriaResultados2 resultados = new EvaluacionDocentesMateriaResultados2();
            resultados = resActualiza.getValor();
            // System.out.println("Valoooor"+resultados.getR1());
            ejbEvaluacionDocente2.comprobarResultado(resultados);
            getResultados();
        }
    }
    /*
    Guarda respuesta evaluacion tipo 2
     */
    public void saveRespuetaTipo2(ValueChangeEvent e){
        UIComponent id = (UIComponent)e.getSource();


        if(e.getNewValue() != null){
            valor = e.getNewValue().toString();
        }else{
            valor = e.getOldValue().toString();
        }
        ResultadoEJB<EvaluacionDocentesMateriaResultados3> resActualiza = ejbEvaluacionDocente2.actualizaRespuestaPorPregunta3(dtoDocenteEvaluando.getResultadosTipo2(),id.getId(),valor);
        if(resActualiza.getCorrecto()==true){
            //System.out.println("Pregunta: "+ id.getId()+"Valor ----------->" +valor);
            EvaluacionDocentesMateriaResultados3 resultados = new EvaluacionDocentesMateriaResultados3();
            resultados = resActualiza.getValor();
            // System.out.println("Valoooor"+resultados.getR1());
            ejbEvaluacionDocente2.comprobarResultado2(resultados);
            getResultadosTipo2();
            System.out.println("Id que recibe -> "+ id.getId() + " valor ->" + valor);
        }
    }
    public void  getdocenteEvaluando(dtoEstudianteMateria evaluando){
        dtoDocenteEvaluando = new dtoEstudianteMateria();
        dtoDocenteEvaluando = evaluando;
        Ajax.update("frmEvaluacion");
        //System.out.println("Evaluado" + dtoDocenteEvaluando);
    }
    public void actualizaDocente(){
        dtoDocenteEvaluando = new dtoEstudianteMateria();
        Ajax.update("frmEvaluacion");
        Ajax.update("frm");
    }

}
