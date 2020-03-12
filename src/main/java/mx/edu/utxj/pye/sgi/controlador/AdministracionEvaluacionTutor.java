package mx.edu.utxj.pye.sgi.controlador;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.EJBAdimEstudianteBase;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEvTutor;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionTutor;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.dto.dtoAvanceEvaluaciones;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionTutor2;
import org.omnifaces.util.Messages;



import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;

/**
 * @author Taatisz :)
 * Modificacion por nueva evaluacion 10/03/2020
 */
@Named
@SessionScoped
public class AdministracionEvaluacionTutor extends ViewScopedRol implements Serializable {
    @Getter @Setter List<dtoEstudiantesEvalauciones> listGeneral,listEstudiantesFiltrados,listCompleto, listIncompletos, listNoIngreso, listFilter;
    @Getter @Setter List<EvaluacionTutoresResultados> listResultados;
    @Getter @Setter dtoEstudiantesEvalauciones dtoEstudiante;
    @Getter @Setter EvaluacionTutoresResultados resultadoEstudiante = new EvaluacionTutoresResultados();
    @Getter @Setter Personal persona= new Personal();
    @Getter @Setter Evaluaciones evaluacion = new Evaluaciones();
    @Getter @Setter EstudiantesClaves estudiante= new EstudiantesClaves();
    @Getter @Setter boolean completo;
    @Getter @Setter ComparadorEvaluacionTutor comparadorET;
    @Getter @Setter PeriodosEscolares periodoEvaluacion;
    @Getter @Setter int totalEstudiantes,totalCompletos,totalIncompletos,totalNoAcceso,faltates, totalFaltantes,totalEstudiantesPE,totalCompletosPE,totalIncompletosPE,
                        totalNoAccesoPE,faltantesPE,totalFaltantesPE;
    @Getter @Setter double porcentaje, porcentajePE;
    @Getter @Setter List<dtoAvanceEvaluaciones> dtoEvance;
    @Getter @Setter List<dtoAvanceEvaluaciones> listAvance, listAvancePE, listAvanceFIlter;
    @Getter @Setter List<ProgramasEducativos> listPE;


    @EJB private EjbAdministracionEvTutor ejbAdmminEvTutor;
    @EJB private EjbPersonal ejbPersonal;
    @EJB private EJBAdimEstudianteBase ejbAdimEstudianteBase;

    @Inject private LogonMB logonMB;


@Getter private Boolean cargado = false;



    @PostConstruct
    public void init(){
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_EV_TUTOR);
        getPersona();
        getEvaluacionTutorActiva();
        getPeriodoEvaluacion();
    }

    public void getPersona(){
        try {
            //Obtiene la paersona logueada
            Integer claveNomina = logonMB.getPersonal().getClave();
            persona = ejbPersonal.mostrarPersonalLogeado(claveNomina);
        }catch (Throwable e){
        }
    }
    //Obtiene la ultima evaluacion  a tutor activa
    public void  getEvaluacionTutorActiva(){
        try{
            // Obtiene la ultima evaluacion a  tutor activa
            ResultadoEJB<Evaluaciones> resEvaluacion = ejbAdmminEvTutor.getUltimaEvTutorActiva();

            if (resEvaluacion.getCorrecto()==true){
                evaluacion = resEvaluacion.getValor();
            }else {mostrarMensajeResultadoEJB(resEvaluacion);}

        }catch (Exception e){

        }
    }
    //Obtiene el periodo de la evaluacion
    public void getPeriodoEvaluacion() {
        ResultadoEJB<PeriodosEscolares> resperiodoEvaluacion = ejbAdmminEvTutor.getPeriodoEvaluacion(evaluacion);
        if(resperiodoEvaluacion.getCorrecto()==true){periodoEvaluacion = resperiodoEvaluacion.getValor();}
        else{mostrarMensajeResultadoEJB(resperiodoEvaluacion);}

    }
    //Obtiene la lista de los estudiantes activos tanto de Sauiit como en Control Escolar por el periodo de la evalacion
    public void getEstudiantesActivosbyPeriodo(){
        listGeneral = new ArrayList<>();
        ResultadoEJB<List<dtoEstudiantesEvalauciones>> resListaGeneral = ejbAdimEstudianteBase.getEstudiantesSauiityCE(periodoEvaluacion);
        if(resListaGeneral.getCorrecto()==true){
            listGeneral = resListaGeneral.getValor();
        }else{mostrarMensajeResultadoEJB(resListaGeneral);}
    }
    // Genera las listas de estudiantes con la evaluacion Completa, Incompleta y los que no han ingresado y da el avance de la evalaucion
public void generalListas(List<dtoEstudiantesEvalauciones> estudiantes){
        //Recorre la lista de los estudiantes ya filtrados ya sea del tutor, director, SE.
        
        listCompleto = new ArrayList<>();
        listIncompletos = new ArrayList<>();
        listNoIngreso = new ArrayList<>();
        estudiantes.forEach(e->{
           // ResultadoEJB<EvaluacionTutoresResultados> res = ejbAdmminEvTutor.getResultadoEvaluacionByEstudiante(e); --> Esta es la version anterior de la evaluación
            ResultadoEJB<EvaluacionTutoresResultados2> res = ejbAdmminEvTutor.getResultadosEvByEstudiante(e,evaluacion);

            if(res.getCorrecto()==true){
                //EvaluacionTutoresResultados resultado = new EvaluacionTutoresResultados();
                EvaluacionTutoresResultados2 resultado = new EvaluacionTutoresResultados2();
                resultado = res.getValor();
               //System.out.println("Resultados encontrados" + resultado);
                //Comparador<EvaluacionTutoresResultados> comparador = new ComparadorEvaluacionTutor();
                Comparador<EvaluacionTutoresResultados2> comparador = new ComparadorEvaluacionTutor2();
                boolean finalizado = comparador.isCompleto(resultado);
                if(resultado!=null){
                    if(finalizado){
                        listCompleto.add(e);
                        totalCompletos = totalCompletos +1;
                    }
                    if(!finalizado){
                        listIncompletos.add(e);
                        totalIncompletos = totalIncompletos +1;
                    }
                }
            }else{
                listNoIngreso.add(e);
                totalNoAcceso = totalNoAcceso +1;
            }

        });

        }

    //Hace calculos de avance de la evalaucion
        public void generarAvaance(){
            listAvance = new ArrayList<>();
            totalFaltantes = totalEstudiantes - totalCompletos;
            Double dte = new Double(totalEstudiantes);
            Double dc= new Double(totalCompletos);
            porcentaje = (dc * 100) / dte;
            // LO AGREGA AL DTO Y DESPUES A LA LISTA
            dtoAvanceEvaluaciones dto = new dtoAvanceEvaluaciones();
            dto.setTotalEstudiantes(totalEstudiantes);
            dto.setTotalCompletos(totalCompletos);
            dto.setTotalIncompletos(totalIncompletos);
            dto.setTotalNoIngreso(totalNoAcceso);
            dto.setPorcentaje(porcentaje);
            listAvance.add(dto);
        }
        //Generar avance por PE
    public void generarAvancePE(List<dtoEstudiantesEvalauciones> estudiantes){
        //Obtiene la lista de programas edictaivos activos
        ResultadoEJB<List<ProgramasEducativos>> resPE= ejbAdimEstudianteBase.getPEActivos();
        if(resPE.getCorrecto() == true){
            listPE = new ArrayList<>();
            listPE = resPE.getValor();
            listAvancePE = new ArrayList<>();
            listPE.forEach(pe->{
               totalEstudiantesPE= (int) estudiantes.stream().filter(f-> pe.getSiglas().equals(f.getNombrePE())).count();
               totalCompletosPE = (int) listCompleto.stream().filter(f->pe.getSiglas().equals(f.getNombrePE())).count();
               totalIncompletosPE =(int) listIncompletos.stream().filter(f->pe.getSiglas().equals(f.getNombrePE())).count();
               totalNoAccesoPE = (int) listNoIngreso.stream().filter(f-> pe.getSiglas().equals(f.getNombrePE())).count();
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
            });
        }else{mostrarMensajeResultadoEJB(resPE);}
    }


//Obtiene lista de resultados de evaluaciones completas o inclompletas de sus estudiantes tutorados, ademas del total de los estudiantes que tiene a su cargo
public void seguimientoTutor(){
    try {
            listGeneral = new ArrayList<>();
            getEstudiantesActivosbyPeriodo();
            //Obtiene la lista de estudiantes tutorados por el personal logeado!
            listEstudiantesFiltrados =new ArrayList<>();
            listEstudiantesFiltrados= listGeneral.stream().filter(e-> persona.getClave().equals(e.getClaveTutor())).collect(Collectors.toList());
            totalEstudiantes = listEstudiantesFiltrados.size();
            //System.out.println( "Total estudiantes " + totalEstudiantes);
            //Genera las listas completas e incompletas
            generalListas(listEstudiantesFiltrados);
            generarAvaance();

    }catch (Exception e){
        Logger.getLogger(AdministracionEncuestaTsu.class.getName()).log(Level.SEVERE, null, e);
    }
    }
//Obtiene lista de resultados de evaluaciones completas o inclompletas de sus estudiantes bajo su direccion, ademas del total de los estudiantes que tiene a su cargo
    public void seguimientoDirector(){
        try{
            listGeneral = new ArrayList<>();
            getEstudiantesActivosbyPeriodo();
            //Obtiene la lista general de estudiantes bajo su dirección
            listEstudiantesFiltrados = new ArrayList<>();
            listEstudiantesFiltrados = listGeneral.stream().filter(e->persona.getClave().equals(e.getClaveDirector())).collect(Collectors.toList());
            //Genera las listas
            totalEstudiantes = listEstudiantesFiltrados.size();
            generalListas(listEstudiantesFiltrados);
            generarAvaance();
        }catch (Exception e){
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuestaTsu.class.getName()).log(Level.SEVERE, null, e);
        }
    }
//Obtiene lista de resultados de evaluaciones completas o inclompletas de sus estudiantes activos
    public void seguimientoSE(){
        try{
            listGeneral = new ArrayList<>();
            getEstudiantesActivosbyPeriodo();
            //Obtiene la lista general de estudiantes activos
            listEstudiantesFiltrados = new ArrayList<>();
            listEstudiantesFiltrados = listGeneral;
            totalEstudiantes = listEstudiantesFiltrados.size();
            generalListas(listEstudiantesFiltrados);
            generarAvaance();
            generarAvancePE(listEstudiantesFiltrados);
        }catch (Exception e){
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEncuestaTsu.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}

