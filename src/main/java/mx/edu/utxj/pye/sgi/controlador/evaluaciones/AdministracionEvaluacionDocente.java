package mx.edu.utxj.pye.sgi.controlador.evaluaciones;


import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoAvanceEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.dtoEstudianteMateria;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import mx.edu.utxj.pye.sgi.ejb.EJBAdimEstudianteBase;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EJBEvaluacionDocenteMateria;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDocente2;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;

/**
 * @author  Taatiz
 * Modificacion 10/03/2020
 * Cambio de evaluaciacion
 */
@Named
@SessionScoped
public class AdministracionEvaluacionDocente extends ViewScopedRol implements Serializable {

    @Getter @Setter Evaluaciones evaluacion;
    @Getter @Setter List<dtoEstudiantesEvalauciones> listEstudiantesGeneral, listEstudiantesFiltrados, listCompletos,listIncompletos,listNoAcceso,listfilter;
    @Getter @Setter List<dtoAvanceEvaluaciones> listAvance,listAvancePE,listAvanceFilter;
    @Getter @Setter List<ProgramasEducativos> listPE;
    @Getter @Setter PeriodosEscolares periodoEvalucaion;
    @Getter @Setter Personal persona;
    @Getter @Setter int totalEstudiantes,totalCompletos,totalIncompletos,totalNoAcceso,totalEstudiantesPE, totalCompletosPE,totalIncompletosPE,totalNoAccesoPE;
    @Getter @Setter int tipoEvaluacion;
    @Getter @Setter double porcentaje, porcentajePE;
    @Getter @Setter Boolean cargada;

    @EJB EJBAdimEstudianteBase ejbAdimEstudianteBase;
    @EJB  EJBEvaluacionDocenteMateria ejbEvaluacionDocenteMateria;
    @EJB EjbEvaluacionDocente2 ejbEvaluacionDocente2;
    @EJB EjbPersonal ejbPersonal;

    @Inject
    private LogonMB logonMB;


@Getter private Boolean cargado = false;



    @PostConstruct
    public void init(){
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_EV_DOCENTE);
        getUltimaEvaluacionActiva();
        getTipoEvaluacion();
        getPeriodoEvaluacion();
        getPersonal();

    }
    //Obtiene la ultima evaluacion a Docente activa
    public void getUltimaEvaluacionActiva(){
        ResultadoEJB<Evaluaciones> resEvaluacion = ejbEvaluacionDocenteMateria.getUltimaEvDocenteActiva();
        if(resEvaluacion.getCorrecto()==true){
            evaluacion = resEvaluacion.getValor();
            ResultadoEJB<AperturaVisualizacionEncuestas> resAper= ejbEvaluacionDocente2.getApertura(evaluacion);
            if(resAper.getCorrecto()){
                cargada=true;
            }else {cargada=false;}
        }
        else {mostrarMensajeResultadoEJB(resEvaluacion);}
    }
    //
    public void  getTipoEvaluacion(){
        try{
            if(evaluacion.getTipo().equals(EvaluacionesTipo.DOCENTE.getLabel())){tipoEvaluacion=1;}
            else if(evaluacion.getTipo().equals(EvaluacionesTipo.DOCENTE_2.getLabel())){tipoEvaluacion=2;}
            else if(evaluacion.getTipo().equals(EvaluacionesTipo.DOCENTE_3.getLabel())){tipoEvaluacion=3;}
            else if(evaluacion.getTipo().equals(EvaluacionesTipo.DOCENTE_4.getLabel())){tipoEvaluacion=4;}
        }
        catch (Exception e){mostrarExcepcion(e);}
    }
    //Obtiene el periodo de la evaluacion
    public void getPeriodoEvaluacion(){
        ResultadoEJB<PeriodosEscolares> resPeriodo= ejbEvaluacionDocenteMateria.getPeriodoEvaluacion(evaluacion);
        if(resPeriodo.getCorrecto()==true){periodoEvalucaion=resPeriodo.getValor();}
        else{mostrarMensajeResultadoEJB(resPeriodo);}

    }
    //Obtiene Personal de quien esta logueada
    public void getPersonal(){
        try {
            //Obtiene el obejeto de la paersona logueada
            Integer claveNomina = logonMB.getPersonal().getClave();
            persona = ejbPersonal.mostrarPersonalLogeado(claveNomina);
        }catch (Throwable e){
        }

    }
    //Obtiene la lista general de estudiantes activos tanto de Sauiit como del CE
    public void getEstudiantesActivosbyPeriodo(){
        listEstudiantesGeneral = new ArrayList<>();
        ResultadoEJB<List<dtoEstudiantesEvalauciones>> resEstudiantes = ejbAdimEstudianteBase.getEstudiantesSauiityCE(periodoEvalucaion);
        if(resEstudiantes.getCorrecto()==true){listEstudiantesGeneral=resEstudiantes.getValor();
           // System.out.println("Lista total controlador -> "+ listEstudiantesGeneral.size());
        }
        else{mostrarMensajeResultadoEJB(resEstudiantes);}
    }
    public void generarListasSeguimiento(List<dtoEstudiantesEvalauciones> estudiantes){
        listCompletos = new ArrayList<>();
        listIncompletos = new ArrayList<>();
        listNoAcceso = new ArrayList<>();
        //Recorre la lista de sus estudiantes
        estudiantes.forEach(e->{
            //Obtiene la lista de materias, no importa en que base este registrada
            List<dtoEstudianteMateria> listaMaterias = new ArrayList<>();
            //System.out.println(e);
            ResultadoEJB<List<dtoEstudianteMateria>> resMaterias = ejbEvaluacionDocenteMateria.getMateriasbyEstudiante(e,evaluacion);
            if(resMaterias.getCorrecto()==true){
                listaMaterias = resMaterias.getValor();
                //Se verifica el tipo de evaluacion para cargar los resultados correcpondientes
                if(tipoEvaluacion==1){
                    getResultadosTipo1(e,listaMaterias);
                }
                else if(tipoEvaluacion==2){
                    getResultadosTipo2(e,listaMaterias);
                }
                else if(tipoEvaluacion==3){
                    getResultadosTipo3(e,listaMaterias);
                }
                else if(tipoEvaluacion==4){
                    getResultadosTipo4(e,listaMaterias);
                }

            }
            else {mostrarMensajeResultadoEJB(resMaterias);}
        });
    }
    public void getResultadosTipo1(dtoEstudiantesEvalauciones estudiante,List<dtoEstudianteMateria> materias){
        try{
            //Obtiene la lista de resultados por matricula y evaluacion(tipo 1 = evaluacion docente noraml)
            //ResultadoEJB<List<EvaluacionDocentesMateriaResultados>> resResultadosEvaluacion = ejbEvaluacionDocenteMateria.getListResultadosDocenteMateriabyMatricula(evaluacion,Integer.parseInt(e.getMatricula()));
            ResultadoEJB<List<EvaluacionDocentesMateriaResultados2>> resResultadosEvaluacion = ejbEvaluacionDocenteMateria.getListaResultadosMateria2byMatricula(evaluacion,Integer.parseInt(estudiante.getMatricula()));
            //List<EvaluacionDocentesMateriaResultados> listTotalResuldos = resResultadosEvaluacion.getValor();
            List<EvaluacionDocentesMateriaResultados2> listTotalResuldos = resResultadosEvaluacion.getValor();
            if(resResultadosEvaluacion.getCorrecto()==true){
                //Se filtran los resultados encontrados, para obtener solo los resultados completos
                List<EvaluacionDocentesMateriaResultados2> listResultadosCompletos= resResultadosEvaluacion.getValor().stream().filter(x-> x.getCompleto()==true).collect(Collectors.toList());
                //List<EvaluacionDocentesMateriaResultados> listResultadosCompletos= resResultadosEvaluacion.getValor().stream().filter(x-> x.getCompleto()==true).collect(Collectors.toList());
                // System.out.println("TOTAL DE RESULTADOS COMPLETOS "+ listResultadosCompletos.size());
                int totalaEvaluar = materias.size();
                int totalResultadosCompletos = listResultadosCompletos.size();
                // Comprueba si ha terminado la evaluacion a docente
                if(totalResultadosCompletos < totalaEvaluar){
                    //Si la los registros de resultados completos es menor a la total de la que debe evaluar, la evaluacion esta incompleta y se agrega a la lista de incompletos
                    listIncompletos.add(estudiante);
                    //System.out.println("Ev incompleta");
                }if(totalResultadosCompletos == totalaEvaluar){
                    //Si los registros de resultados completos del estudiante es igual al los que debe evaluar, entonces la evaluacion esta finalizada, y se agrega a la lista de completos
                    listCompletos.add(estudiante);
                    // System.out.println("Ev completa");
                }
            }
            //Si no existen registros se agrega a la lista de estudiantes que no han ingresado al sistema
            else {listNoAcceso.add(estudiante); totalNoAcceso++;
                //System.out.println("Ev no accedio");
            }
        }catch (Exception e){ mostrarExcepcion(e); }
    }
    public void getResultadosTipo2(dtoEstudiantesEvalauciones estudiante,List<dtoEstudianteMateria> materias){
        try{
            //Obtien la lista de resultados (evaluacion tipo 3 = evaluacion al desempeño docente por contingencia de salud)
            ResultadoEJB<List<EvaluacionDocentesMateriaResultados3>> resResultados = ejbEvaluacionDocente2.getListaResultadosMateriabyMatricula(evaluacion,Integer.parseInt(estudiante.getMatricula()));
            List<EvaluacionDocentesMateriaResultados3> listTotalResuldos = resResultados.getValor();
            if(resResultados.getCorrecto()==true){
                //Se filtran los resultados encontrados, para obtener solo los resultados completos
                List<EvaluacionDocentesMateriaResultados3> listResultadosCompletos= resResultados.getValor().stream().filter(x-> x.getCompleto()==true).collect(Collectors.toList());
                //List<EvaluacionDocentesMateriaResultados> listResultadosCompletos= resResultadosEvaluacion.getValor().stream().filter(x-> x.getCompleto()==true).collect(Collectors.toList());
                // System.out.println("TOTAL DE RESULTADOS COMPLETOS "+ listResultadosCompletos.size());
                int totalaEvaluar = materias.size();
                int totalResultadosCompletos = listResultadosCompletos.size();
                // Comprueba si ha terminado la evaluacion a docente
                if(totalResultadosCompletos < totalaEvaluar){
                    //Si la los registros de resultados completos es menor a la total de la que debe evaluar, la evaluacion esta incompleta y se agrega a la lista de incompletos
                    listIncompletos.add(estudiante);
                    //System.out.println("Ev incompleta");
                }if(totalResultadosCompletos == totalaEvaluar){
                    //Si los registros de resultados completos del estudiante es igual al los que debe evaluar, entonces la evaluacion esta finalizada, y se agrega a la lista de completos
                    listCompletos.add(estudiante);
                    // System.out.println("Ev completa");
                }
            }
            //Si no existen registros se agrega a la lista de estudiantes que no han ingresado al sistema
            else {listNoAcceso.add(estudiante); totalNoAcceso++;
                //System.out.println("Ev no accedio");
            }
        }catch (Exception e){mostrarExcepcion(e);}
    }
    public void getResultadosTipo3(dtoEstudiantesEvalauciones estudiante,List<dtoEstudianteMateria> materias){
        try{
            //Obtien la lista de resultados (evaluacion tipo 3 = evaluacion al desempeño docente por contingencia de salud)
            ResultadoEJB<List<EvaluacionDocentesMateriaResultados4>> resResultados = ejbEvaluacionDocente2.getListaResultados4MateriabyMatricula(evaluacion,Integer.parseInt(estudiante.getMatricula()));
            List<EvaluacionDocentesMateriaResultados4> listTotalResuldos = resResultados.getValor();
            if(resResultados.getCorrecto()==true){
                //Se filtran los resultados encontrados, para obtener solo los resultados completos
                List<EvaluacionDocentesMateriaResultados4> listResultadosCompletos= resResultados.getValor().stream().filter(x-> x.getCompleto()==true).collect(Collectors.toList());
                //List<EvaluacionDocentesMateriaResultados> listResultadosCompletos= resResultadosEvaluacion.getValor().stream().filter(x-> x.getCompleto()==true).collect(Collectors.toList());
                // System.out.println("TOTAL DE RESULTADOS COMPLETOS "+ listResultadosCompletos.size());
                int totalaEvaluar = materias.size();
                int totalResultadosCompletos = listResultadosCompletos.size();
                // Comprueba si ha terminado la evaluacion a docente
                if(totalResultadosCompletos < totalaEvaluar){
                    //Si la los registros de resultados completos es menor a la total de la que debe evaluar, la evaluacion esta incompleta y se agrega a la lista de incompletos
                    listIncompletos.add(estudiante);
                    //System.out.println("Ev incompleta");
                }if(totalResultadosCompletos == totalaEvaluar){
                    //Si los registros de resultados completos del estudiante es igual al los que debe evaluar, entonces la evaluacion esta finalizada, y se agrega a la lista de completos
                    listCompletos.add(estudiante);
                    // System.out.println("Ev completa");
                }
            }
            //Si no existen registros se agrega a la lista de estudiantes que no han ingresado al sistema
            else {listNoAcceso.add(estudiante); totalNoAcceso++;
                //System.out.println("Ev no accedio");
            }
        }catch (Exception e){mostrarExcepcion(e);}
    }
    public void getResultadosTipo4(dtoEstudiantesEvalauciones estudiante,List<dtoEstudianteMateria> materias){
        try{
            //Obtien la lista de resultados (evaluacion tipo 3 = evaluacion al desempeño docente por contingencia de salud)
            ResultadoEJB<List<EvaluacionDocentesMateriaResultados5>> resResultados = ejbEvaluacionDocente2.getListaResultados5MateriabyMatricula(evaluacion,Integer.parseInt(estudiante.getMatricula()));
            List<EvaluacionDocentesMateriaResultados5> listTotalResuldos = resResultados.getValor();
            if(resResultados.getCorrecto()==true){
                //Se filtran los resultados encontrados, para obtener solo los resultados completos
                List<EvaluacionDocentesMateriaResultados5> listResultadosCompletos= resResultados.getValor().stream().filter(x-> x.getCompleto()==true).collect(Collectors.toList());
                //List<EvaluacionDocentesMateriaResultados> listResultadosCompletos= resResultadosEvaluacion.getValor().stream().filter(x-> x.getCompleto()==true).collect(Collectors.toList());
                // System.out.println("TOTAL DE RESULTADOS COMPLETOS "+ listResultadosCompletos.size());
                int totalaEvaluar = materias.size();
                int totalResultadosCompletos = listResultadosCompletos.size();
                // Comprueba si ha terminado la evaluacion a docente
                if(totalResultadosCompletos < totalaEvaluar){
                    //Si la los registros de resultados completos es menor a la total de la que debe evaluar, la evaluacion esta incompleta y se agrega a la lista de incompletos
                    listIncompletos.add(estudiante);
                    //System.out.println("Ev incompleta");
                }if(totalResultadosCompletos == totalaEvaluar){
                    //Si los registros de resultados completos del estudiante es igual al los que debe evaluar, entonces la evaluacion esta finalizada, y se agrega a la lista de completos
                    listCompletos.add(estudiante);
                    // System.out.println("Ev completa");
                }
            }
            //Si no existen registros se agrega a la lista de estudiantes que no han ingresado al sistema
            else {listNoAcceso.add(estudiante); totalNoAcceso++;
                //System.out.println("Ev no accedio");
            }
        }catch (Exception e){mostrarExcepcion(e);}
    }
    //Genera el avance de la evaluacion
    public void generarAvance(){
        listAvance = new ArrayList<>();
        totalCompletos = listCompletos.size();
        totalIncompletos= listIncompletos.size();
        Double dte = new Double(totalEstudiantes);
        Double dc= new Double(totalCompletos);
        //General el porcentaje de avance
        porcentaje = (dc * 100) / dte;
        // LO AGREGA AL DTO Y DESPUES A LA LISTA
        dtoAvanceEvaluaciones dto = new dtoAvanceEvaluaciones();
        dto.setTotalEstudiantes(totalEstudiantes);
        dto.setTotalCompletos(totalCompletos);
        dto.setTotalIncompletos(totalIncompletos);
        dto.setTotalNoIngreso(totalNoAcceso);
        dto.setPorcentaje(porcentaje);
        //System.out.println("DTO AVANCE " + dto);
        listAvance.add(dto);
        //System.out.println("Seagrego a la listaaa dtooo--<>" + listAvance.size());
    }
    //Genera el avanace por programa educativo
    public void generarAvancePE(List<dtoEstudiantesEvalauciones> estudiantes){
        //Obtiene la lista de programas edictaivos activos
        ResultadoEJB<List<ProgramasEducativos>> resPE= ejbAdimEstudianteBase.getPEActivos();
        if(resPE.getCorrecto() == true){
            listPE = new ArrayList<>();
            listPE = resPE.getValor();
            listAvancePE = new ArrayList<>();
            listPE.forEach(pe->{
                totalEstudiantesPE= (int) estudiantes.stream().filter(f-> pe.getSiglas().equals(f.getNombrePE())).count();
                totalCompletosPE = (int) listCompletos.stream().filter(f->pe.getSiglas().equals(f.getNombrePE())).count();
                totalIncompletosPE =(int) listIncompletos.stream().filter(f->pe.getSiglas().equals(f.getNombrePE())).count();
                totalNoAccesoPE = (int) listNoAcceso.stream().filter(f-> pe.getSiglas().equals(f.getNombrePE())).count();
                Double dte = new Double(totalEstudiantesPE);
                Double dc= new Double(totalCompletosPE);
                porcentajePE = (dc * 100) / dte;
                dtoAvanceEvaluaciones dto= new dtoAvanceEvaluaciones();
                dto.setTotalEstudiantes(totalEstudiantesPE);
                dto.setPe(pe.getSiglas());
                dto.setTotalCompletos(totalCompletosPE);
                dto.setTotalIncompletos(totalIncompletosPE);
                dto.setTotalNoIngreso(totalNoAccesoPE);
                dto.setPorcentaje(porcentajePE);
                listAvancePE.add(dto);
                //System.out.println("dtoo----->" + dto);
            });
            //System.out.println("Lista total por PE"+listAvancePE.size());
        }else{mostrarMensajeResultadoEJB(resPE);}
    }
    //Seguimiento para el tutor
    public void seguimientoTutor(){
        System.out.println("Persona clave" + persona.getClave());
        //:Obtiene la lista general de estudiantes(Sauiit y CE)
        getEstudiantesActivosbyPeriodo();
        //Filtra la lista general de estudiantes a sólo sus estudiantes tutorados
        listEstudiantesGeneral.forEach(e->{
            if(e.getClaveTutor()==persona.getClave()){
                System.out.println("Es su tutorado");
            }
        });
        listEstudiantesFiltrados = listEstudiantesGeneral.stream()
                .filter(e-> persona.getClave().equals(e.getClaveTutor()))
                .filter(e->e.getGrado()!=6 & e.getGrado()!=11 )
                .collect(Collectors.toList());
        System.out.println("Estudiantes "+ listEstudiantesFiltrados.size() + " --->general" + listEstudiantesGeneral.size());
        //Cuenta el total de estudiantes a su cargo
        totalEstudiantes = listEstudiantesFiltrados.size();
        generarListasSeguimiento(listEstudiantesFiltrados);
        generarAvance();
    }
    //Seguiento para el Director
    public void seguimientoDirector(){
        //Obtiene la lista general de estudiantes(Sauiit y CE)
        getEstudiantesActivosbyPeriodo();
        //Filtra la lista general de estudiantes a sólo sus estudiantes a cargo
        listEstudiantesFiltrados = listEstudiantesGeneral.stream()
                .filter(e-> persona.getClave().equals(e.getClaveDirector()))
                .filter(e->e.getGrado()!=6 & e.getGrado()!=11 )
                .collect(Collectors.toList());
        //Cuenta el total de estudiantes a su cargo
        totalEstudiantes = listEstudiantesFiltrados.size();
        generarListasSeguimiento(listEstudiantesFiltrados);
        generarAvance();

    }
    //Seguimiento para Secretaría Académica
    public void seguimientoSE(){
        //Obtiene la lista general de los estudiantes sauiit y CE sacando de la lista a estudiantes de 6to y 11vo
        getEstudiantesActivosbyPeriodo();
        listEstudiantesFiltrados = listEstudiantesGeneral;
        listEstudiantesFiltrados = listEstudiantesGeneral.stream()
                .filter(e->e.getGrado()!=6 & e.getGrado()!=11 )
                .collect(Collectors.toList());
        totalEstudiantes = listEstudiantesFiltrados.size();
        generarListasSeguimiento(listEstudiantesFiltrados);
        generarAvance();
        generarAvancePE(listEstudiantesFiltrados);
    }





}
