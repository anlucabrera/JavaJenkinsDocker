/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.*;

import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 * Ejb  para detectar si es estudiante esta registrado en la base de Saiiut o en control escolar.
 * 1.- Busca el numero de registro del estudiante por matricula y por periodo (Tabla - Matricula Periodos Escolar)
 * 2.- Del registro se busca la matricula en Saiiut y en Control Escolar y se llena un DTO con todos los datos necesarios
 * 3.- Se busca la clave del estudiante (Estudiantes-claves)---
 * Nota: Buscar para las proximas evaluaciones quitar esa tabla
 * @author Taatisz :)
 */
@Stateless
public class EJBAdimEstudianteBase {
    
    @EJB  Facade f;
    @EJB Facade2 fs;
    @EJB EjbPropiedades ep;
    @Inject EjbAdministracionTutores ejbAdmin;
    private EntityManager em;
    private EntityManager em2;

    @PostConstruct
    public void init(){em = f.getEntityManager();em2 = fs.getEntityManager();}


    public ResultadoEJB<MatriculaPeriodosEscolares> getMatriculaPeriodo(String matricula, int periodo){
        try{
            MatriculaPeriodosEscolares registro = new MatriculaPeriodosEscolares();
            if (matricula==null){return  ResultadoEJB.crearErroneo(2,registro,"La matricula no debe ser nula");}
            if (periodo==0) { return  ResultadoEJB.crearErroneo(3,registro,"El periodo no debe ser nulo");}
            //Se hace la consulta en pye2 por matricula y periodo
            registro = em.createQuery("select m from MatriculaPeriodosEscolares m where  m.matricula=:matricula and m.periodo=:periodo",MatriculaPeriodosEscolares.class)
            .setParameter("matricula",matricula)
            .setParameter("periodo",periodo)
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(registro!=null){
                //El estudiante esta registrado
                return ResultadoEJB.crearCorrecto(registro,"Estudiante encontrado");
            }else {
                //El estudiante no esta registrado
                return ResultadoEJB.crearErroneo(4,registro,"El estudiante no se encuentra registrado en este periodo");
            }

        }catch ( Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el registro del estudiante por periodo(EjbAdminEstudianteBase.getMatriculaPeriodo)", e, null);
        }
    }

    /**
     * Busca todos los datos necesarios del estudiante registrado en Saiiut por matricula
     * @param matricula estudiante (registro de Matricula periodos escolares)
     * @param periodo estudiante (registro de Matricula periodos escolares)
     * @return Resultado del proceso
     */
    public ResultadoEJB<dtoEstudiantesEvalauciones> getEstudianteSaiiut(String matricula, Integer periodo){
        try {
            dtoEstudiantesEvalauciones estudianteSaiiut = new dtoEstudiantesEvalauciones();
            if(matricula== null){return ResultadoEJB.crearErroneo(2,estudianteSaiiut,"El estudiante no debe ser nulo");}
            if(periodo== null){return ResultadoEJB.crearErroneo(2,estudianteSaiiut,"El periodo no debe ser nulo");}
            //Hace la consulta en la vista de sauiit
            AlumnosEvaluacionTutor alumnosEvaluacionTutor;
            alumnosEvaluacionTutor = em2.createQuery("select a from AlumnosEvaluacionTutor a where a.matricula=:matricula and a.cvePeriodo = :periodo", AlumnosEvaluacionTutor.class)
            .setParameter("matricula", matricula)
            .setParameter("periodo", periodo)
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(alumnosEvaluacionTutor==null){
                //El estudiante no esta registrado en saiiut
                return ResultadoEJB.crearErroneo(3,estudianteSaiiut,"El estudiante no esta en Saiiut");
            }else {
                //El estudiante esta registrado en saiiut
                estudianteSaiiut.setEstudianteSaiiut(alumnosEvaluacionTutor);
                //Buscar tutor
                ResultadoEJB<Personal > Restutor= getClaveNominabyClavePersona(alumnosEvaluacionTutor.getCveMaestro());
                if(Restutor.getCorrecto()==true){estudianteSaiiut.setTutor(Restutor.getValor());}
                else {return ResultadoEJB.crearErroneo(3,estudianteSaiiut,"Error al biscar al tutor del estudiante de Saiiut");}
                //BUSCO director
                ResultadoEJB<Personal> Resdirector = getClaveNominabyClavePersona(Integer.parseInt(alumnosEvaluacionTutor.getCveDirector()));
                if(Resdirector.getCorrecto()==true){estudianteSaiiut.setDirector(Resdirector.getValor());}
                else {return ResultadoEJB.crearErroneo(4,estudianteSaiiut,"Error al buscar al director del estudiante de Saiiut");}
                //Busca carrera por abreviatura
                ResultadoEJB<AreasUniversidad> resArea= getAreabySiglas(estudianteSaiiut.getEstudianteSaiiut().getAbreviatura());
                if(resArea.getCorrecto()==true){estudianteSaiiut.setCarrera(resArea.getValor());}
                else {return  ResultadoEJB.crearErroneo(5,estudianteSaiiut, "Error a buscar la carrera del estudiante de Saiiut");}
                //Se llenan los datos aparte del dto (Aunque ya no los necesita, sólo para no afecta el funcionamiento de la creacion de los Reportes)
                estudianteSaiiut.setMatricula(alumnosEvaluacionTutor.getMatricula());
                estudianteSaiiut.setRegistro(0);
                estudianteSaiiut.setNombreCEstudiante(alumnosEvaluacionTutor.getNombre().concat(" ").concat(alumnosEvaluacionTutor.getApellidoPat().concat(" ").concat(alumnosEvaluacionTutor.getApellidoMat())));
                estudianteSaiiut.setGrado(((int) alumnosEvaluacionTutor.getGrado()));
                estudianteSaiiut.setGrupo(alumnosEvaluacionTutor.getIdGrupo());
                estudianteSaiiut.setClavePE((int)resArea.getValor().getArea());
                estudianteSaiiut.setNombrePE(resArea.getValor().getSiglas());
                estudianteSaiiut.setClaveTutor(Restutor.getValor().getClave());
                estudianteSaiiut.setClaveDirector(Resdirector.getValor().getClave());
                return ResultadoEJB.crearCorrecto(estudianteSaiiut,"Estudiante Saiiut");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener al estudiante de Saiiut(EjbAdminEstudianteBase.getEstudianteSaiiut)", e, null);
        }
    }
    public ResultadoEJB<dtoEstudiantesEvalauciones> getEstudianteControlEscolar(String matricula, Integer periodo){
        try{
            //System.out.println("Entro a Ce " + estudiante);
            dtoEstudiantesEvalauciones estudianteControlEscolar = new dtoEstudiantesEvalauciones();
            if(matricula ==null){return ResultadoEJB.crearErroneo(2,estudianteControlEscolar,"El estudiante no debe ser nulo");}
            if(periodo ==null){return ResultadoEJB.crearErroneo(2,estudianteControlEscolar,"El estudiante no debe ser nulo");}
            //Busca al estudiante en la base de Control Escolar
            Estudiante estudianteCE;
            estudianteCE = em.createQuery("select e from Estudiante e where e.matricula=:matricula and e.periodo=:periodo and e.tipoEstudiante.descripcion=:tipoE",Estudiante.class)
            .setParameter("matricula",Integer.parseInt(matricula))
                    .setParameter("periodo",periodo)
                    .setParameter("tipoE", "Regular")
                    .getResultStream()
            .findFirst()
            .orElse(null)
            ;
           // System.out.println("ServicioEdtudianteBase.getEstudianteControlEscolar ->" + estudianteCE);
            //System.out.println("Estudiante CE---> " +estudianteCE);
            if(estudianteCE == null){return ResultadoEJB.crearErroneo(3,estudianteControlEscolar,"El estudiante no esta registrado en la base de control escolar");}
            else {
                //Se envia los datos necesarios al dto
                estudianteControlEscolar.setMatriculaPeriodosEscolares(new MatriculaPeriodosEscolares());
                estudianteControlEscolar.setEstudianteCE(estudianteCE);
                //Se busca la carrera del estudiante
                ResultadoEJB<AreasUniversidad> resArea= getAreabyClave(estudianteCE.getCarrera());
                //System.out.println("Carrera CE---> " +resArea.getValor());
                if(resArea.getCorrecto()==true){estudianteControlEscolar.setCarrera(resArea.getValor());}
                else {return ResultadoEJB.crearErroneo(4,estudianteControlEscolar,"No se encontro el área del estudiante");}
                //Busca al tutor
                ResultadoEJB<Personal> resTutor = getPersonalbyClave(estudianteCE.getGrupo().getTutor());
                //System.out.println("Tutor---> " +resTutor);
                if(resTutor.getCorrecto()==true){estudianteControlEscolar.setTutor(resTutor.getValor());}
                else {return ResultadoEJB.crearErroneo(5,estudianteControlEscolar,"No se pudo obeneter al tutor del estudiante de CE");}
                //Director
                ResultadoEJB<Personal> resDirector = getDirectorCE(resArea.getValor());
               // System.out.println("Director" +resArea.getValor());
                if(resDirector.getCorrecto() ==true){estudianteControlEscolar.setDirector(resDirector.getValor());}
                else {return ResultadoEJB.crearErroneo(6,estudianteControlEscolar,"No se pudo obeneter al director");}
                estudianteControlEscolar.setMatricula(Integer.toString(estudianteCE.getMatricula()));
                estudianteControlEscolar.setRegistro(0);
                estudianteControlEscolar.setNombreCEstudiante(estudianteCE.getAspirante().getIdPersona().getNombre().concat(" ").concat(estudianteCE.getAspirante().getIdPersona().getApellidoPaterno()).concat(" ").concat(estudianteCE.getAspirante().getIdPersona().getApellidoMaterno()));
                estudianteControlEscolar.setGrado(estudianteCE.getGrupo().getGrado());
                estudianteControlEscolar.setGrupo(estudianteCE.getGrupo().getLiteral().toString());
                estudianteControlEscolar.setClavePE(estudianteCE.getCarrera());
                estudianteControlEscolar.setNombrePE(resArea.getValor().getSiglas());
                estudianteControlEscolar.setClaveTutor(resTutor.getValor().getClave());
                estudianteControlEscolar.setNombreTutor(resTutor.getValor().getNombre());
                estudianteControlEscolar.setClaveDirector(resDirector.getValor().getClave());
                //System.out.println("Completo CE---> " +estudianteControlEscolar);
                return ResultadoEJB.crearCorrecto(estudianteControlEscolar,"Estudiante en Control Escolar");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener al estudiante de Control Escolar(EjbAdminEstudianteBase.getEstudianteControlEscolar)", e, null);
        }
    }

    /**
     * Busca el registro del estudiante por matricula y periodo
     * @param matricula
     * @param periodo periodo de la evaluacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<MatriculaPeriodosEscolares> getRegistroEstudiantebyPeriodo(String matricula, int periodo){
        try {
            MatriculaPeriodosEscolares registro= new MatriculaPeriodosEscolares();
            if (matricula==null){return ResultadoEJB.crearErroneo(2,registro,"La matricula no debe ser nula");}
            if(periodo==0){return ResultadoEJB.crearErroneo(3,registro,"El periodo no debe ser nulo");}
            //Se hace la consulta
            registro = em.createQuery("select m from MatriculaPeriodosEscolares  m where  m.matricula=:matricula and m.periodo=:periodo",MatriculaPeriodosEscolares.class)
            .setParameter("matricula", matricula)
            .setParameter("periodo",periodo)
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(registro==null){return ResultadoEJB.crearErroneo(4,registro,"No se encontro registro del estudiante con matricula "+ matricula + " en ese periodo");}
            else {return ResultadoEJB.crearCorrecto(registro,"Se encontro al estudiante");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el registro del estudiante(EjbAdminEstudianteBase.getRegistroEstudiantebyPeriodo)", e, null);
        }
    }


    /**
     * Obtiene al estudiante, identifica donde esta registrado ya sea en saiiut o en control escolar y obtiene todos los datos necesarios
     * @param matricula MAtrícula del estudiante evaluador
     * @param periodoEvaluacion Periodo en el que se realiza la evaluacion
     * @return Regresa la referencia del estudiante evaluador segun su periodo, si no lo encuentra en el repositorio correspondiente regresa null.
     */
    public ResultadoEJB<dtoEstudiantesEvalauciones> getClaveEstudiante(String matricula, Integer periodoEvaluacion) {
        try{
//            System.out.println("Matricula que recibe " + matricula);
//            System.out.println("Periodo " + periodoEvaluacion);
            if (matricula == null) { return ResultadoEJB.crearErroneo(2, "La matricula no debe ser nula", dtoEstudiantesEvalauciones.class); }
            if (periodoEvaluacion == 0) { return ResultadoEJB.crearErroneo(3, "El periodo no debe ser nulo", dtoEstudiantesEvalauciones.class); }
            if(matricula.length()<6){matricula ="0".concat(matricula);}
            //Se busca el registro del estudiante segun su matricula y periodo de la evaluacion
            dtoEstudiantesEvalauciones estudiante = new dtoEstudiantesEvalauciones();
            
            
            
            ResultadoEJB<dtoEstudiantesEvalauciones> resSaiiut = getEstudianteSaiiut(matricula, periodoEvaluacion);
            if (resSaiiut.getCorrecto() == true) {
                //System.out.println("Esta en Saiiut" + resSaiiut.getValor());
                //El estudiante esta regristrado en Saiiut
                estudiante.setEstudianteSaiiut(resSaiiut.getValor().getEstudianteSaiiut());
                estudiante.setTutor(resSaiiut.getValor().getTutor());
                estudiante.setDirector(resSaiiut.getValor().getDirector());
                estudiante.setCarrera(resSaiiut.getValor().getCarrera());
                estudiante.setMatricula(resSaiiut.getValor().getMatricula());
                estudiante.setRegistro(resSaiiut.getValor().getRegistro());
                estudiante.setClaveEstudiante(resSaiiut.getValor().getClaveEstudiante());
                estudiante.setNombreCEstudiante(resSaiiut.getValor().getNombreCEstudiante());
                estudiante.setGrado(resSaiiut.getValor().getGrado());
                estudiante.setGrupo(resSaiiut.getValor().getGrupo());
                estudiante.setClavePE(resSaiiut.getValor().getClavePE());
                estudiante.setNombrePE(resSaiiut.getValor().getNombrePE());
                estudiante.setClaveTutor(resSaiiut.getValor().getClaveTutor());
                estudiante.setNombreTutor(resSaiiut.getValor().getNombreTutor());
                estudiante.setClaveDirector(resSaiiut.getValor().getClaveDirector());
                
            } 
            ResultadoEJB<dtoEstudiantesEvalauciones> resCE = getEstudianteControlEscolar(matricula, periodoEvaluacion);
                if (resCE.getCorrecto() == true) {
                    //System.out.println("Esta en Control Escolar" + resCE.getValor());
                    //Esta en control Escolar
                    estudiante.setEstudianteCE(resCE.getValor().getEstudianteCE());
                    estudiante.setTutor(resCE.getValor().getTutor());
                    estudiante.setDirector(resCE.getValor().getDirector());
                    estudiante.setCarrera(resCE.getValor().getCarrera());
                    estudiante.setMatricula(resCE.getValor().getMatricula());
                    estudiante.setRegistro(resCE.getValor().getRegistro());
                    estudiante.setClaveEstudiante(resCE.getValor().getClaveEstudiante());
                    estudiante.setNombreCEstudiante(resCE.getValor().getNombreCEstudiante());
                    estudiante.setGrado(resCE.getValor().getGrado());
                    estudiante.setGrupo(resCE.getValor().getGrupo());
                    estudiante.setClavePE(resCE.getValor().getClavePE());
                    estudiante.setNombrePE(resCE.getValor().getNombrePE());
                    estudiante.setClaveTutor(resCE.getValor().getClaveTutor());
                    estudiante.setNombreTutor(resCE.getValor().getNombreTutor());
                    estudiante.setClaveDirector(resCE.getValor().getClaveDirector());
                    
                }
                return ResultadoEJB.crearCorrecto(estudiante, "Se encontro el estudiante");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener al estudiante(EjbAdminEstudianteBase.getClaveEstudiante)", e, null);
        }
    }


    public ResultadoEJB<List<dtoEstudiantesEvalauciones>> getEstudiantesSauiit(PeriodosEscolares periodo) {
        try {
            List<AlumnosEvaluacionTutor> listAlumnosSauiit= new ArrayList<>();
            List<dtoEstudiantesEvalauciones> listDtoEstudiantesSauiit = new ArrayList<>();
            //Busca a los estudiantes activos
            listAlumnosSauiit = em2.createQuery("SELECT a FROM AlumnosEvaluacionTutor a",AlumnosEvaluacionTutor.class)
                    .getResultList()
                    ;
            //System.out.println("Lista Saiiut" + listAlumnosSauiit.size());
             //System.out.println("Lista de vista --->" + listAlumnosSauiit.size());
            if(listAlumnosSauiit.isEmpty() || listAlumnosSauiit== null){return ResultadoEJB.crearErroneo(2,listDtoEstudiantesSauiit , "La lista de Estudiantes en Sauiit es nula");}
            else{
                 // System.out.println("La lista no esta vacía!");
                // recorre la lista general de estudiantes activos en sauiit para poder llenar el dto
                listAlumnosSauiit.forEach(x ->{
                    dtoEstudiantesEvalauciones dtoEstudiante = new dtoEstudiantesEvalauciones();
                    //System.out.println("1");
                    dtoEstudiante.setEstudianteSaiiut(x);
                    // como la clave de Tutor y del Director es clave persona, obtengo la clave de Nomina de ambos.
                   if(x.getCveMaestro()==null){
                       dtoEstudiante.setNombreTutor("Sin asignar");
                       //System.out.println("No tiene tutot");
                   }else {  Personal tutor = getClaveNominabyClavePersona(x.getCveMaestro()).getValor();
                       dtoEstudiante.setTutor(tutor);
                       dtoEstudiante.setNombreTutor(tutor.getNombre());
                       dtoEstudiante.setClaveTutor(tutor.getClave());
                       //.out.println("Tutor->" + tutor);
                   }
                   Personal director= getClaveNominabyClavePersona(Integer.parseInt(x.getCveDirector())).getValor();
                   // System.out.println("Director -> " +director.getClave());
                    //Busca el numero de registro del estudiante por matricula
                   MatriculaPeriodosEscolares estudianteR = f.getEntityManager().createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.matricula=:matricula AND m.periodo=:periodo", MatriculaPeriodosEscolares.class)
                                    .setParameter("matricula", x.getMatricula())
                                    .setParameter("periodo", periodo.getPeriodo() )
                                    .getResultStream()
                                    .findFirst()
                                    .orElse(null);
                    //System.out.println("Registro " + estudianteR);
                    //System.out.println("ServicioEdtudianteBase.getEstudiantesSauiit " + estudianteR);
                   //Busca la clave del Estudiante por numero de registro
                    /*EstudiantesClaves estudianteC = f.getEntityManager().createQuery("SELECT e FROM EstudiantesClaves e WHERE e.registro=:registro", EstudiantesClaves.class)
                            .setParameter("registro", estudianteR.getRegistro())
                            .getResultStream()
                            .findFirst()
                            .orElse(null)
                            ;
                            */
                   // System.out.println("ServicioEdtudianteBase.getEstudiantesSauiit 2" + estudianteC);
                    //Llena el dto con los datos obtenidos
                    dtoEstudiante.setMatricula(x.getMatricula());
                    //dtoEstudiante.setRegistro(estudianteR.getRegistro());
                    //dtoEstudiante.setClaveEstudiante(estudianteC.getClave());
                    dtoEstudiante.setNombreCEstudiante(x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat());
                    dtoEstudiante.setGrado(x.getGrado());
                    dtoEstudiante.setGrupo(x.getIdGrupo());
                    dtoEstudiante.setClavePE(x.getCveCarrera());
                    dtoEstudiante.setNombrePE(x.getAbreviatura());
                    dtoEstudiante.setClaveDirector(director.getClave());
                    //TODO: Agrega el dto a la lista
                    listDtoEstudiantesSauiit.add(dtoEstudiante);
                    //System.out.println("DTo s->"+ dtoEstudiante);
                });
                //System.out.println("Lista total--> " + listDtoEstudiantesSauiit.size());
            }
            if(listDtoEstudiantesSauiit.isEmpty()||listDtoEstudiantesSauiit ==null){return ResultadoEJB.crearErroneo(3, listDtoEstudiantesSauiit, "No se pudo hacer el llenado de la lista de estudiantes en sauiit.");}
            else{return ResultadoEJB.crearCorrecto(listDtoEstudiantesSauiit,"Lista de Estudiantes activos en sauiit creada correctamente");}
                
            
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la busqueda yllenado  (EjbAdmminEstudianteBase-->getEstudiantesSauiit)", e, null);
        }
        
      
    }

    public ResultadoEJB<List<dtoEstudiantesEvalauciones>> getEstudiantesCE(PeriodosEscolares periodo) {
        List<dtoEstudiantesEvalauciones> listEstudiantesCE = new ArrayList<>();
        if(periodo ==null){return ResultadoEJB.crearErroneo(2,listEstudiantesCE,"El periodo no debe ser nulo");}
        //TODO: Busco la lista de estudiantes en control escolar por periodo
        List<Estudiante> estudiantesCE = new ArrayList<>();
        estudiantesCE = em.createQuery("select e from Estudiante e where e.periodo=:periodo and e.tipoEstudiante.idTipoEstudiante=:tipo",Estudiante.class)
             .setParameter("tipo",1)
            .setParameter("periodo",periodo.getPeriodo() )
            .getResultList()
        ;
        if(estudiantesCE ==null || estudiantesCE.isEmpty()){return ResultadoEJB.crearErroneo(3,listEstudiantesCE,"No hay registro de estudiantes en el periodo");}
        else {
            //Se recorre la lista para obetener sus datos
            estudiantesCE.forEach(e->{
                //System.out.println("Estudiante--------->" + e);
                dtoEstudiantesEvalauciones dtoEstudiante = new dtoEstudiantesEvalauciones();
                dtoEstudiante.setEstudianteCE(e);
                dtoEstudiante.setMatricula(Integer.toString(e.getMatricula()));
                //Datos generales del estudiante
                dtoEstudiante.setNombreCEstudiante(e.getAspirante().getIdPersona().getNombre().concat(" ").concat(e.getAspirante().getIdPersona().getApellidoPaterno().concat(" ").concat(e.getAspirante().getIdPersona().getApellidoMaterno())));
                //Grado y grupo
                dtoEstudiante.setGrado(e.getGrupo().getGrado());
                dtoEstudiante.setGrupo(e.getGrupo().getLiteral().toString());
                //Busco al tutor
                if(e.getGrupo().getTutor()!=null){
                    ResultadoEJB<Personal> tutor= getPersonalbyClave(e.getGrupo().getTutor());
                    if(tutor.getCorrecto()==true){
                        //Se envia al tutor  y sus datos
                        dtoEstudiante.setTutor(tutor.getValor());
                        dtoEstudiante.setNombreTutor(tutor.getValor().getNombre());
                        dtoEstudiante.setClaveTutor(tutor.getValor().getClave());
                    }
                }else {
                    dtoEstudiante.setNombreTutor("Sin asignar");}
                //Busco el area a la que está inscrito
                ResultadoEJB<AreasUniversidad> areas = getAreabyClave(e.getCarrera());
                if(areas.getCorrecto()==true){
                    //Se mandan todos los datos de las areas
                    dtoEstudiante.setCarrera(areas.getValor());
                    dtoEstudiante.setNombrePE(areas.getValor().getSiglas());
                    dtoEstudiante.setClavePE(areas.getValor().getArea());
                }
                //Busco al director
                ResultadoEJB<Personal> director = getDirectorCE(areas.getValor());
                if(director.getCorrecto()==true){
                    //Se envian los datos
                    dtoEstudiante.setDirector(director.getValor());
                    dtoEstudiante.setClaveDirector(director.getValor().getClave());
                }
                //TODO: Busca el numero de registro del estudiante por matricula
                MatriculaPeriodosEscolares estudianteR = f.getEntityManager().createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.matricula=:matricula AND m.periodo=:periodo", MatriculaPeriodosEscolares.class)
                        .setParameter("matricula", Integer.toString(e.getMatricula()))
                        .setParameter("periodo", periodo.getPeriodo() )
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                if(estudianteR!=null){
                    dtoEstudiante.setMatriculaPeriodosEscolares(estudianteR);
                    dtoEstudiante.setRegistro(estudianteR.getRegistro());
                }
               // System.out.println("Registro CE --->" +estudianteR);
                //TODO:Busca la clave del Estudiante por numero de registro
               /* EstudiantesClaves estudianteC = f.getEntityManager().createQuery("SELECT e FROM EstudiantesClaves e WHERE e.registro=:registro", EstudiantesClaves.class)
                        .setParameter("registro", estudianteR.getRegistro())
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
                        ;
                        */
                //System.out.println("Clave --->" +estudianteC);
                /*if(estudianteC !=null){
                    dtoEstudiante.setEstudiantesClaves(estudianteC);
                    dtoEstudiante.setClaveEstudiante(estudianteC.getClave());
                }*/
//               System.out.println("Estudiante Control Escolar --->" + dtoEstudiante);
                listEstudiantesCE.add(dtoEstudiante);
            });
            return ResultadoEJB.crearCorrecto(listEstudiantesCE ,"Lista de estudiantes CE");

        }

    }


    public ResultadoEJB<List<dtoEstudiantesEvalauciones>> getEstudiantesSauiityCE(PeriodosEscolares periodoEvaluacion) {
        try {
            List<dtoEstudiantesEvalauciones> listTotalEstudiantes = new ArrayList<>();
            if(periodoEvaluacion==null) {
                return ResultadoEJB.crearErroneo(3, listTotalEstudiantes, "El periodo de la evaluación no debe ser nulo");
            }else{
                //System.out.println("Periodo que recibe en ejb del administracion de alumnos" + periodoEvaluacion.getPeriodo());
                List<dtoEstudiantesEvalauciones> listEstudiantesSauiit = new ArrayList<>();
                List<dtoEstudiantesEvalauciones> listEstudiantesCE = new ArrayList<>();

                //Obtiene la lista de Estudiantes en sauiit
                listEstudiantesSauiit = getEstudiantesSauiit(periodoEvaluacion).getValor();
                //System.out.println("Genero la lista de estudiantes en Sauiit"+ listEstudiantesSauiit.size());
                //Obtiene la lista de Estudiantes en Control Escolar
                listEstudiantesCE = getEstudiantesCE(periodoEvaluacion).getValor();
                //System.out.println("Genero la lista de estudiantes en ce"+ listEstudiantesCE.size());
                //Agrega ambas listas a la lista total de estudiantes
                listTotalEstudiantes.addAll(listEstudiantesCE);
                listTotalEstudiantes.addAll(listEstudiantesSauiit);
               // System.out.println("Lista total " + listTotalEstudiantes.size());
                //System.out.println("Se añadio a la lista general!" + listTotalEstudiantes.size());
                if (listTotalEstudiantes.isEmpty()) {
                    return ResultadoEJB.crearErroneo(3, listTotalEstudiantes, "La lista total esta vacia!");
                } else {
                    //System.out.println("Se creo ResultadoEjbCorrecto");
                    return ResultadoEJB.crearCorrecto(listTotalEstudiantes, "Lista total");
                }
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la busqueda y llenado  (EjbAdmminEstudianteBase-->getTotalEstudiantes)", e, null);
        }

    }

    public ResultadoEJB<Personal> getClaveNominabyClavePersona(int clavePersona) {
        try {
          //  System.out.println("Clave persona " + clavePersona);
            Personal personal = new Personal();
            ListaUsuarioClaveNomina personalS =  em2.createQuery("SELECT l FROM ListaUsuarioClaveNomina l WHERE l.cvePersona=:clave", ListaUsuarioClaveNomina.class)
                    .setParameter("clave", clavePersona)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(personalS == null){return ResultadoEJB.crearErroneo(3, personal, "No se encontro la clave de persona");}
            else{
                personal = f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE p.clave=:clave", Personal.class)
                        .setParameter("clave", Integer.parseInt(personalS.getNumeroNomina()))
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
                        ;
                if (personal == null){return ResultadoEJB.crearErroneo(4, personal, "No se encontro al personal en la base de Personal");}
                else{return ResultadoEJB.crearCorrecto(personal, "Se encontro al personal por su clave de persona");}
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la busqueda  (EjbAdmminEstudianteBase-->getClaveNomnaByClavePersona)", e, null);
        }
    }

    public ResultadoEJB<List<ProgramasEducativos>> getPEActivos() {
        try {
            List<ProgramasEducativos> pe= f.getEntityManager().createQuery("select p from ProgramasEducativos p where p.activo=true",ProgramasEducativos.class)
                    .getResultList()
                    ;
            if(pe.isEmpty() || pe ==null){ return ResultadoEJB.crearErroneo(2,pe,"La lista esta vacía");}
            else{return ResultadoEJB.crearCorrecto(pe,"Lista de pe, correcto");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la busqueda  (EjbAdmminEstudianteBase-->getPEActivo)", e, null);
        }


    }

    /**
     * Busca el Area por las siglas
     * @param siglas
     * @return
     */
    public ResultadoEJB<AreasUniversidad> getAreabySiglas(String siglas){
        try{
            AreasUniversidad area = new AreasUniversidad();
            if(siglas==null){return ResultadoEJB.crearErroneo(2,area,"Las siglas no deben ser nulas");}
            //Se hace la consulta
            area = em.createQuery("select a from AreasUniversidad  a where a.siglas=:siglas", AreasUniversidad.class)
                    .setParameter("siglas",siglas)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(area==null){return ResultadoEJB.crearErroneo(3,area,"No se encontro el área");}
            else {return ResultadoEJB.crearCorrecto(area,"Se encontro el área");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el área(EjbAdminEstudianteBase.getAreabySiglas)", e, null);
        }
    }

    /**
     * Busca área por clave
     * @param clave
     * @return
     */
    public ResultadoEJB<AreasUniversidad> getAreabyClave(short clave){
        try{
            AreasUniversidad area = new AreasUniversidad();
            if(clave==0){return ResultadoEJB.crearErroneo(2,area,"La clave del área no debe ser nulo");}
            //Se hace la consulta
            area = em.createQuery("select a from AreasUniversidad  a where a.area=:area",AreasUniversidad.class)
                    .setParameter("area",clave)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(area ==null){return ResultadoEJB.crearErroneo(3,area,"No se encontro el área");}
            else {return ResultadoEJB.crearCorrecto(area,"Área encontrada");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el área(EjbAdminEstudianteBase.getAreabyClave)", e, null);
        }
    }

    /**
     * Busca a personal por clave
     * @param clave
     * @return
     */
    
    public ResultadoEJB<Personal> getPersonalbyClave(int clave){
        try{
            Personal personal = new Personal();
            if(clave ==0){return ResultadoEJB.crearErroneo(2,personal,"La clave del personal no debe ser nulo");}
            //Se hace la consulta
            personal = em.createQuery("select p from Personal p where p.clave=:clave",Personal.class)
            .setParameter("clave",clave)
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(personal==null){return ResultadoEJB.crearErroneo(3,personal,"No se encontro al personal");}
            else {return ResultadoEJB.crearCorrecto(personal,"Personal encontrado");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener al personal(EjbAdminEstudianteBase.getPersonalbyClave)", e, null);
        }
    }

    /**
     * Obtiene al director segun el plan eductivo(carrera)del estudiante de CE
     * @param carrera Plan educativo (carrera) del estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Personal> getDirectorCE (AreasUniversidad carrera){
        try{
            Personal director = new Personal();
            if(carrera==null){return ResultadoEJB.crearErroneo(2,director,"La carrera no debe ser nulo");}
            //Busca el superior de la carrera
            ResultadoEJB<AreasUniversidad> resAreaSuperior = getAreabyClave(carrera.getAreaSuperior());
            if(resAreaSuperior.getCorrecto()==true){
                //Se busca al personal encargado del area
                ResultadoEJB<Personal> resPersonal = getPersonalbyClave(resAreaSuperior.getValor().getResponsable());
                if(resPersonal.getCorrecto()==true){
                    //Se encontro al director de la carrera
                    director =resPersonal.getValor();
                    return ResultadoEJB.crearCorrecto(director,"Director encontrado");
                }
                //No se encontro al director
                else {return ResultadoEJB.crearErroneo(4,director,"No se pudo encontrar al director");}

            }else {return ResultadoEJB.crearErroneo(3,director,"No se contro el area superior de la carrera");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener al director del estudiante(EjbAdminEstudianteBase.getDirectorCE)", e, null);
        }
    }

}
