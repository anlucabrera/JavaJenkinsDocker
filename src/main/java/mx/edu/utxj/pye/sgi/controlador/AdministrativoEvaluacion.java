/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ListaEstudiantesDtoTutor;
import mx.edu.utxj.pye.sgi.ejb.EJBEvaluacionDocenteMateria;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.ListaEvaluacionDocentesResultados;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaAlumnosPye;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionDocenteMateriaPye;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacionTutor2;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.sescolares.Alumno;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionTutor;
import mx.edu.utxj.pye.sgi.util.UtilidadesAdministracionEvaluaciones;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionesTutores;
import org.omnifaces.util.Messages;

/**
 *
 * @author Carlos Alfredo Vargas Galindo
 */
@Named
@SessionScoped
public class AdministrativoEvaluacion implements Serializable {

    @Getter @Setter Integer periodo, clavetrabajador, tipoEvaluacion;
    @Getter @Setter private Grupos grupoSeleccionado;
    @Getter @Setter private ListaPersonal directivoSeleccionado;
    @Getter @Setter private List<Grupos> grupo;
    @Getter @Setter private List<Alumnos> listaEstudiantes;
    @Getter @Setter private List<VistaAlumnosPye> listadoEstudiantesCompletado, listadoEstudiantesNC, listadoEstudiantesNA, listadoEstudiantesPeriodoActual;
    @Getter @Setter private List<Alumno> listaEstudiantesSES;
    @Getter @Setter private List<ListaEstudiantesDtoTutor>  listadoEstudiantesCompletadoSES, listadoEstudiantesNCSES, listadoEstudiantesNASES;
    @Getter @Setter private List<String> listaEstudiantesE, listaEstudiantesNE;
    @Getter @Setter private List<AreasUniversidad> listaCarreras;
    @Getter @Setter private List<ListaEvaluacionDocentesResultados> listaDeResultadosEvaluaciones;
    @Inject LogonMB logonMB;
    @Inject EvaluacionDesempenioAdmin desempenioAdmin;

    @Inject @Getter @Setter UtilidadesAdministracionEvaluaciones utilAdmin;

    @EJB EjbAdministracionEncuestas eJBAdministracionEncuestas;
    @EJB EJBEvaluacionDocenteMateria eJBEvaluacionDocenteMateria;
    @EJB EjbEvaluacionTutor2 ejbTutor;
    @EJB EJBSelectItems eJBSelectItems;

    @EJB private Facade facade;

    @PostConstruct
    public void init() {
        utilAdmin.init();
        if (41 == logonMB.getPersonal().getAreaOperativa()) {
            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministrativoEvaluacion.init() Psicopedagogia : ");

        }
    }

    public void obtenerAlumnosPorGrupo() {
        listadoEstudiantesCompletado = new ArrayList<>();
        listadoEstudiantesNC = new ArrayList<>();
        listadoEstudiantesNA = new ArrayList<>();
        List<Grupos> l = eJBAdministracionEncuestas.esTutor(logonMB.getUsuarioAutenticado().getUsuariosPK().getCvePersona(), eJBAdministracionEncuestas.getPeriodoActual().getPeriodo());
        grupoSeleccionado = l.get(0);
        listaEstudiantes = eJBAdministracionEncuestas.getListaDeAlumnosPorDocente(grupoSeleccionado.getGruposPK().getCveGrupo());
        System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministrativoEvaluacion.obtenerAlumnosPorGrupo() lista estudiantes tamaño : " + listaEstudiantes.size());
        if (null == tipoEvaluacion) {
            Messages.addGlobalWarn("Seleccione una opcion para poder obtener los resultados");
        } else {
            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministrativoEvaluacion.obtenerAlumnosPorGrupo() la evaluacion seleccionada es : " + tipoEvaluacion);
            switch (tipoEvaluacion) {
                case 1:
                    resultadoDocente();
                    break;
                case 2:
                    resultadoTutor();
                    break;
                default:
                    Messages.addGlobalWarn("Seleccione una opcion para poder obtener los resultados");
                    break;
            }
        }
    }

    public void resultadoDocente() {
        System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministrativoEvaluacion.resultadoDocente() entra al metodo");
        if (eJBEvaluacionDocenteMateria.evaluacionActiva() != null) {
            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministrativoEvaluacion.resultadoDocente() la evaluacion no es nula");
            listaEstudiantes.stream().forEach(e -> {

                List<VistaEvaluacionDocenteMateriaPye> listaDatosEvaluacion = eJBEvaluacionDocenteMateria.getDocenteMAteria(e.getMatricula(), eJBEvaluacionDocenteMateria.evaluacionActiva().getPeriodo());
                List<EvaluacionDocentesMateriaResultados> listaResultados = new ArrayList<>();

                listaDatosEvaluacion.stream().forEach(vista -> {
                    EvaluacionDocentesMateriaResultadosPK pk = new EvaluacionDocentesMateriaResultadosPK(2, Integer.parseInt(e.getMatricula()), vista.getCveMateria(), vista.getCveMaestro());
                    facade.setEntityClass(EvaluacionDocentesMateriaResultados.class);
                    if (eJBEvaluacionDocenteMateria.obtenerListaResultadosPorEvaluacionEvaluador(eJBEvaluacionDocenteMateria.ultimaEvaluacionDocenteMaterias(), Integer.parseInt(e.getMatricula())) != null) {
                        listaResultados.addAll(eJBEvaluacionDocenteMateria.obtenerListaResultadosPorEvaluacionEvaluador(eJBEvaluacionDocenteMateria.ultimaEvaluacionDocenteMaterias(), Integer.parseInt(e.getMatricula())));
                    }
                });
            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministrativoEvaluacion.obtenerAlumnosPorGrupo() lista de resultados : " + listaResultados + " tamaño : " + listaResultados.size());
                if (!listaResultados.isEmpty()) {
                    Integer total = listaDatosEvaluacion.size();
                    Integer completos = (int) listaResultados.stream().filter(r -> r.getCompleto()).count() / total;
                    Boolean termino = total >= completos;
            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministrativoEvaluacion.obtenerAlumnosPorGrupo()" + e.getMatricula() + ""
                        + " completo : " + termino + " completo a : " + completos + " de un total de : " + total);
                    if (completos < total) {
//                    List<VistaAlumnosPye> lnc = eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula());
                        listadoEstudiantesNC.add(eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula()).get(0));
                        //addAll(lnc.stream().distinct().collect(Collectors.toList()));
                    } else if (termino) {
//                    List<VistaAlumnosPye> lc = eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula());
                        listadoEstudiantesCompletado.add(eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula()).get(0));
                    }
                } else {
//                List<VistaAlumnosPye> lna = eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula());
                    listadoEstudiantesNA.add(eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula()).get(0));
                }
            });
//        listadoEstudiantesNA.stream().distinct().collect(Collectors.toList());
//        listadoEstudiantesCompletado.stream().distinct().collect(Collectors.toList());
//        listadoEstudiantesNC.stream().distinct().collect(Collectors.toList());
        System.out.println("lista completo : " + listadoEstudiantesCompletado.size() + ", no completo : " + listadoEstudiantesNC.size() + " no accedio : " + listadoEstudiantesNA.size());
        } else {
            
            Messages.addGlobalInfo("La evaluacion ya no se encuentra activa, debido a esto usted ya no puede ver el resumen de esta");
        }
    }

    public void resultadoTutor() {
        if (ejbTutor.evaluacionActiva() != null) {
        System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministrativoEvaluacion.resultadoTutor() lista de estudiantes : " + listaEstudiantes);
            listaEstudiantes.stream().forEach(e -> {
            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministrativoEvaluacion.resultadoTutor() el alumno e es : " + e);
//            List<VistaEvaluacionDocenteMateriaPye> listaDatosEvaluacion = eJBEvaluacionDocenteMateria.getDocenteMAteria(e.getMatricula());
                EvaluacionesTutoresResultados resultado;
                Evaluaciones eva;
                eva = ejbTutor.evaluacionActiva();
//            Integer matricula = e.getMatricula();
                resultado = ejbTutor.getSoloResultados(eva, Integer.parseInt(e.getMatricula()));
            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministrativoEvaluacion.resultadoTutor() los resultados de "+e.getMatricula()+" y son " + resultado);
                if (resultado != null) {
                    Comparador<EvaluacionesTutoresResultados> comparador = new ComparadorEvaluacionTutor();
                    boolean finalizado = comparador.isCompleto(resultado);
            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministrativoEvaluacion.resultadoTutor() finalizo? : " +finalizado);
                    if (!finalizado) {
//                    List<VistaAlumnosPye> lnc = eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula());
                        listadoEstudiantesNC.add(eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula()).get(0));
                        //addAll(lnc.stream().distinct().collect(Collectors.toList()));
                    } else if (finalizado) {
//                    List<VistaAlumnosPye> lc = eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula());
                        listadoEstudiantesCompletado.add(eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula()).get(0));
                    }
                } else {
//                List<VistaAlumnosPye> lna = eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula());
                    listadoEstudiantesNA.add(eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula()).get(0));
                }
            });
        } else {
            Messages.addGlobalInfo("La evaluacion ya no se encuentra activa, debido a esto usted ya no puede ver el resumen de esta");
        }
    }

    public void obtenerDatosEvaluacion() {
        listaCarreras = new ArrayList<>();
        listaDeResultadosEvaluaciones = new ArrayList<>();
        desempenioAdmin.getDirectivoSeleccionado();
        System.out.println("Director Seleccionado : " + desempenioAdmin.getDirectivoSeleccionado());
        Short area = desempenioAdmin.getDirectivoSeleccionado().getAreaOperativa();
        System.out.println("area operativa : " + desempenioAdmin.getDirectivoSeleccionado().getAreaOperativa());
        listaCarreras = eJBAdministracionEncuestas.obtenerAreasDirector(area, "1");
        System.out.println("Lista de carreras : " + listaCarreras);
        listaCarreras.stream().forEach(c -> {
            listaDeResultadosEvaluaciones.addAll(eJBAdministracionEncuestas.resultadosEvaluacionGlobalDirector(c.getSiglas(), 1));
            System.out.println("siglas agregadas : " + c.getSiglas());
        });
        System.out.println("lista de resultados : "+ listaDeResultadosEvaluaciones);
    }

    public void resultadosTutorPsicopedagogia() {
        listadoEstudiantesCompletado = new ArrayList<>();
        listadoEstudiantesNC = new ArrayList<>();
        listadoEstudiantesNA = new ArrayList<>();
        listadoEstudiantesPeriodoActual = eJBAdministracionEncuestas.getEstudiantesGeneral();
        if (!listadoEstudiantesPeriodoActual.isEmpty() || listadoEstudiantesPeriodoActual == null) {
            listadoEstudiantesPeriodoActual.forEach(e -> {
                EvaluacionesTutoresResultados resultado;
                Evaluaciones eva;
                eva = ejbTutor.evaluacionActiva();
                VistaEvaluacionesTutores  estuduianteTutorado;
                estuduianteTutorado = ejbTutor.getEstudianteTutor(eJBAdministracionEncuestas.getPeriodoActual().getPeriodo(), e.getMatricula());
                resultado = ejbTutor.getSoloResultados(eva, Integer.parseInt(e.getMatricula()));
                if (resultado != null) {
                    Comparador<EvaluacionesTutoresResultados> comparador = new ComparadorEvaluacionTutor();
                    boolean finalizado = comparador.isCompleto(resultado);
                    if (!finalizado) {
                        listadoEstudiantesNC.add(eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula()).get(0));
                    } else if (finalizado) {
                        listadoEstudiantesCompletado.add(eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula()).get(0));
                    }
                } else {
                    listadoEstudiantesNA.add(eJBAdministracionEncuestas.findAllByMatricula(e.getMatricula()).get(0));
                }
            });
        }
    }
    
    public void resultadosTutoresPs(){
        listadoEstudiantesCompletadoSES = new ArrayList<>();
        listadoEstudiantesNCSES = new ArrayList<>();
        listadoEstudiantesNASES = new ArrayList<>();
        listaEstudiantesSES = eJBAdministracionEncuestas.getEstudiantesSEScolaes();
        listaEstudiantesSES.forEach(e ->{
            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministrativoEvaluacion.resultadosTutoresPs() matricula que entra " + e.getAlumnoPK().getMatricula());
            System.out.println("mx.edu.utxj.pye.sgi.controlador.AdministrativoEvaluacion.resultadosTutoresPs() la evaluacion es : " + ejbTutor.evaluacionActiva().getEvaluacion());
            Integer matricula = e.getAlumnoPK().getMatricula();
            EvaluacionesTutoresResultados resultado = ejbTutor.getSoloResultados(ejbTutor.evaluacionActiva(), matricula);
            Integer clave =  e.getPe();
            AreasUniversidad carrera = eJBAdministracionEncuestas.getProgramaPorClave(Short.parseShort(clave.toString()));
            if (resultado != null) {
                    Comparador<EvaluacionesTutoresResultados> comparador = new ComparadorEvaluacionTutor();
                    boolean finalizado = comparador.isCompleto(resultado);
                    if (!finalizado) {
                        listadoEstudiantesNCSES.add( new ListaEstudiantesDtoTutor(matricula, e.getNombre()+" "+e.getApellidoPaterno()+" "+e.getApellidoMaterno(), e.getCuatrimestre(), e.getGrupo(), carrera.getSiglas()));
                    } else if (finalizado) {
                        listadoEstudiantesCompletadoSES.add( new ListaEstudiantesDtoTutor(matricula, e.getNombre()+" "+e.getApellidoPaterno()+" "+e.getApellidoMaterno(), e.getCuatrimestre(), e.getGrupo(), carrera.getSiglas()));
                    }
                } else {
                    listadoEstudiantesNASES.add( new ListaEstudiantesDtoTutor(matricula, e.getNombre()+" "+e.getApellidoPaterno()+" "+e.getApellidoMaterno(), e.getCuatrimestre(), e.getGrupo(), carrera.getSiglas()));
                }
        });
    }
}
