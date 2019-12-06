package mx.edu.utxj.pye.sgi.controlador;


import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoAvanceEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.dtoEstudianteMateria;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import mx.edu.utxj.pye.sgi.ejb.EJBAdimEstudianteBase;
import mx.edu.utxj.pye.sgi.ejb.EJBEvaluacionDocenteMateria;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
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
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;


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
    @Getter @Setter double porcentaje, porcentajePE;

    @EJB EJBAdimEstudianteBase ejbAdimEstudianteBase;
    @EJB  EJBEvaluacionDocenteMateria ejbEvaluacionDocenteMateria;
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
        getPeriodoEvaluacion();
        getPersonal();

    }
    //TODO: Obtiene la ultima evaluacion a Docente activa
    public void getUltimaEvaluacionActiva(){
        ResultadoEJB<Evaluaciones> resEvaluacion = ejbEvaluacionDocenteMateria.getUltimaEvDocenteActiva();
        if(resEvaluacion.getCorrecto()==true){
            evaluacion = resEvaluacion.getValor();
        }
        else {mostrarMensajeResultadoEJB(resEvaluacion);}
    }
    //TODO:Obtiene el periodo de la evaluacion
    public void getPeriodoEvaluacion(){
        ResultadoEJB<PeriodosEscolares> resPeriodo= ejbEvaluacionDocenteMateria.getPeriodoEvaluacion(evaluacion);
        if(resPeriodo.getCorrecto()==true){periodoEvalucaion=resPeriodo.getValor();}
        else{mostrarMensajeResultadoEJB(resPeriodo);}

    }
    //TODO: Obtiene Personal de quien esta logueada
    public void getPersonal(){
        try {
            //TODO: Obtiene el obejeto de la paersona logueada
            Integer claveNomina = logonMB.getPersonal().getClave();
            persona = ejbPersonal.mostrarPersonalLogeado(claveNomina);
        }catch (Throwable e){
        }

    }
    //TODO:Obtiene la lista general de estudiantes activos tanto de Sauiit como del CE
    public void getEstudiantesActivosbyPeriodo(){
        listEstudiantesGeneral = new ArrayList<>();
        ResultadoEJB<List<dtoEstudiantesEvalauciones>> resEstudiantes = ejbAdimEstudianteBase.getEstudiantesSauiityCE(periodoEvalucaion);
        if(resEstudiantes.getCorrecto()==true){listEstudiantesGeneral=resEstudiantes.getValor();}
        else{mostrarMensajeResultadoEJB(resEstudiantes);}
    }
    public void generarListasSeguimiento(List<dtoEstudiantesEvalauciones> estudiantes){
        listCompletos = new ArrayList<>();
        listIncompletos = new ArrayList<>();
        listNoAcceso = new ArrayList<>();
        //TODO: Recorre la lista de sus estudiantes
        estudiantes.forEach(e->{
            //Obtine la lista de materias, no importa en que base este registrada
            List<dtoEstudianteMateria> listaMaterias = new ArrayList<>();
            ResultadoEJB<List<dtoEstudianteMateria>> resMaterias = ejbEvaluacionDocenteMateria.getMateriasbyEstudiante(e,evaluacion);
            if(resMaterias.getCorrecto()==true){
                listaMaterias = resMaterias.getValor();
                //Obtiene la lista de resultados por matricula y evaluacion
                ResultadoEJB<List<EvaluacionDocentesMateriaResultados>> resResultadosEvaluacion = ejbEvaluacionDocenteMateria.getListResultadosDocenteMateriabyMatricula(evaluacion,Integer.parseInt(e.getMatricula()));
                List<EvaluacionDocentesMateriaResultados> listTotalResuldos = resResultadosEvaluacion.getValor();
                if(resResultadosEvaluacion.getCorrecto()==true){
                    //TODO: Se filtran los resultados encontrados, para obtener solo los resultados completos
                    List<EvaluacionDocentesMateriaResultados> listResultadosCompletos= resResultadosEvaluacion.getValor().stream().filter(x-> x.getCompleto()==true).collect(Collectors.toList());
                    // System.out.println("TOTAL DE RESULTADOS COMPLETOS "+ listResultadosCompletos.size());
                    int totalaEvaluar = listaMaterias.size();
                    int totalResultadosCompletos = listResultadosCompletos.size();
                    //TODO: Comprueba si ha terminado la evaluacion a docente
                    if(totalResultadosCompletos < totalaEvaluar){
                        //TODO:Si la los registros de resultados completos es menor a la total de la que debe evaluar, la evaluacion esta incompleta y se agrega a la lista de incompletos
                        listIncompletos.add(e);
                        //System.out.println("Ev incompleta");
                    }if(totalResultadosCompletos == totalaEvaluar){
                        //TODO: Si los registros de resultados completos del estudiante es igual al los que debe evaluar, entonces la evaluacion esta finalizada, y se agrega a la lista de completos
                        listCompletos.add(e);
                        // System.out.println("Ev completa");
                    }
                }
                //TODO: Si no existen registros se agrega a la lista de estudiantes que no han ingresado al sistema
                else {listNoAcceso.add(e); totalNoAcceso++;
                    //System.out.println("Ev no accedio");
                }
            }
            else {mostrarMensajeResultadoEJB(resMaterias);}
        });
    }
    //TODO: Genera el avance de la evaluacion
    public void generarAvance(){
        listAvance = new ArrayList<>();
        totalCompletos = listCompletos.size();
        totalIncompletos= listIncompletos.size();
        Double dte = new Double(totalEstudiantes);
        Double dc= new Double(totalCompletos);
        //TODO: General el porcentaje de avance
        porcentaje = (dc * 100) / dte;
        // TODO: LO AGREGA AL DTO Y DESPUES A LA LISTA
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
    //TODO: Genera el avanace por programa educativo
    public void generarAvancePE(List<dtoEstudiantesEvalauciones> estudiantes){
        //TODO:Obtiene la lista de programas edictaivos activos
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
    //TODO:Seguimiento para el tutor
    public void seguimientoTutor(){
        //TODO:Obtiene la lista general de estudiantes(Sauiit y CE)
        getEstudiantesActivosbyPeriodo();
        //TODO:Filtra la lista general de estudiantes a sólo sus estudiantes tutorados y se quita a estudiantes de 6to y 11avo
        listEstudiantesFiltrados = listEstudiantesGeneral.stream()
                .filter(e-> persona.getClave().equals(e.getClaveTutor()))
                .filter(e->e.getGrado()!=6 & e.getGrado()!=11 )
                .collect(Collectors.toList());
        //TODO:Cuenta el total de estudiantes a su cargo
        totalEstudiantes = listEstudiantesFiltrados.size();
        generarListasSeguimiento(listEstudiantesFiltrados);
        generarAvance();
    }
    //TODO:Seguiento para el Director
    public void seguimientoDirector(){
        //TODO:Obtiene la lista general de estudiantes(Sauiit y CE)
        getEstudiantesActivosbyPeriodo();
        //TODO:Filtra la lista general de estudiantes a sólo sus estudiantes a cargo, sacando de la lista 6to y 11vo
        listEstudiantesFiltrados = listEstudiantesGeneral.stream()
                .filter(e-> persona.getClave().equals(e.getClaveDirector()))
                .filter(e->e.getGrado()!=6 & e.getGrado()!=11 )
                .collect(Collectors.toList());
        //TODO:Cuenta el total de estudiantes a su cargo
        totalEstudiantes = listEstudiantesFiltrados.size();
        generarListasSeguimiento(listEstudiantesFiltrados);
        generarAvance();

    }
    //TODO: Seguimiento para Secretaría Académica
    public void seguimientoSE(){
        //TODO:Obtiene la lista general de los estudiantes sauiit y CE sacando de la lista a estudiantes de 6to y 11vo
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
