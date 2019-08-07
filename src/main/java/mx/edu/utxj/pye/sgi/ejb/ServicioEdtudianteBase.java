/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;

import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EstudiantesClaves;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;

import mx.edu.utxj.pye.sgi.saiiut.entity.ListaUsuarioClaveNomina;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author Taatisz :)
 */
@Stateless
public class ServicioEdtudianteBase implements EJBAdimEstudianteBase{
    
    @EJB  Facade f;
    @EJB FacadeCE fce;
    @EJB Facade2 fs;
    @EJB EjbPropiedades ep;
    @Inject EjbAdministracionTutores ejbAdmin;

    @Override
    public MatriculaPeriodosEscolares getEstudianteSauiiut(String matricula, Integer periodo) {
        //Obtiene el periodo actual
        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEdtudianteBase.getEstudianteSauiiut()--> ENTRO A BUSCAR EN MATRICULA PERIODOS" + matricula);
        
        MatriculaPeriodosEscolares ae = new MatriculaPeriodosEscolares();
        //Busca la matricula en la pye2, con matricula y el periodo activo
        System.out.println("Matricula a buscar --->" + matricula);
        System.err.println("Periodo get Estudiantes " + periodo);
        
        MatriculaPeriodosEscolares mpe = f.getEntityManager().createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.matricula=:matricula AND m.periodo=:periodo", MatriculaPeriodosEscolares.class)
                    .setParameter("matricula", matricula.toString())
                    .setParameter("periodo", periodo)
                    .getResultStream()
                    .findAny()
                    .orElse(null);
        System.out.println("mpe: " + mpe);
        return mpe;
    }

    @Override
    public Estudiante getEstudianteControlEscolar( String matricula) {
        List<Estudiante> le = new ArrayList<>();
        Estudiante e = new Estudiante();
        TypedQuery<Estudiante> ece= fce.getEntityManager().createQuery("SELECT e FROM Estudiante e WHERE e.matricula=:matricula", Estudiante.class);
        ece.setParameter("matricula", matricula);
        le = ece.getResultList();
        if(le.isEmpty()){
            e = le.get(0);
            System.out.println("No encontro estudiante con esa matricula en CONTROL ESCOLAR");
            return e;
        }else{
            e=le.get(0);
            System.out.println("Se encontro al estudiante en la base de Control Escolar");
            return e;
        }
    }

    /**
     * Obtiene la clave del estudiante que realiza la evaluación ya sea en periodos anteriores o en periodos del nuevo control escolar
     * @param matricula MAtrícula del estudiante evaluador
     * @param periodoEvaluacion Periodo en el que se realiza la evaluacion
     * @return Regresa la referencia del estudiante evaluador segun su periodo, si no lo encuentra en el repositorio correspondiente regresa null.
     */
    @Override
    public ResultadoEJB<EstudiantesClaves> getClaveEstudiante(String matricula, Integer periodoEvaluacion) {
        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEdtudianteBase.getClaveEstudiante()");
        Estudiante estudianteControlEscolar = new Estudiante();
        MatriculaPeriodosEscolares estudianteSauiit = new MatriculaPeriodosEscolares();
        EstudiantesClaves estudianteClave = new EstudiantesClaves();
       
        //TODO:busca al estudiante en la base de Contorl Escolar
        
        Integer ultimoPeriodoSauiit = ep.leerPropiedadEntera("ultimoPeriodoSauiit").orElse(51);
        System.out.println("PERIODO EN EJB "+ periodoEvaluacion);
        if(periodoEvaluacion <= ultimoPeriodoSauiit){
            //TODO: buscar referencia correspondiente al empate con SAIIUT
            MatriculaPeriodosEscolares estudiantePeriodo = getEstudianteSauiiut(matricula, periodoEvaluacion);
            if(estudiantePeriodo==null){return ResultadoEJB.crearErroneo(4,estudianteClave,"Es egresado");}
            else{
                System.err.println("Estudiante en MatriculaPeriodos" + estudiantePeriodo);
                estudianteClave= f.getEntityManager().createQuery("SELECT e FROM EstudiantesClaves e WHERE e.registro=:registro", EstudiantesClaves.class)
                        .setParameter("registro", estudiantePeriodo.getRegistro())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                System.err.println("Estudiante Clave encontrado" + estudiantePeriodo);

                if(estudianteClave!=null){
                    //todo: existe en la base de sauiit, con el periodo activo
                    return ResultadoEJB.crearCorrecto(estudianteClave,"El estudiante esta registrado en sauiit");

                }else {//TODO: No esta registrado en la base de sauiit o  es egresado
                    return ResultadoEJB.crearErroneo(2,estudianteClave,"El estudiante no esta registrado en sauiit o es egresado"); }

            }

        }else {
           //TODO: buscar referencia en el nuevo control escolar
           Estudiante estudianteCE = getEstudianteControlEscolar(matricula);
            System.out.println("Estudiante en base de CE=" + estudianteCE);
            estudianteClave =f.getEntityManager().createQuery("SELECT ec FROM EstudiantesClaves ec WHERE ec.idEstudiante=:idEstudiante",EstudiantesClaves.class)
                    .setParameter("idEstudiante", estudianteCE.getIdEstudiante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(estudianteClave!=null){
             //TODO: Estudiante registrado en Control Escolar
                return ResultadoEJB.crearCorrecto(estudianteClave,"Registrado en control escolar");

            }else{
                //TODO: No esta registrado en Control escolar
                return ResultadoEJB.crearErroneo(3,estudianteClave,"No existe registro en la base de control escolar del estudiante");

            }

        }

        
    }

    @Override
    public ResultadoEJB<MatriculaPeriodosEscolares> getEstudianteSauittClave(EstudiantesClaves estudiante, PeriodosEscolares periodo) {
        
        try{
            if(estudiante==null){ResultadoEJB.crearErroneo(3, "El estudiante no debe ser nulo",  MatriculaPeriodosEscolares.class);}
            if(periodo==null){ResultadoEJB.crearErroneo(4, "El periodo no debe ser nulo", MatriculaPeriodosEscolares.class);}
             //TODO: Busca al estudiante en la tabla MatriculaPeriodosEscolares por el numero de registro 
          MatriculaPeriodosEscolares estudianteSauitt = f.getEntityManager().createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.registro=:registro AND m.periodo=:periodo", MatriculaPeriodosEscolares.class)
                    .setParameter("registro", estudiante.getRegistro() )
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .findAny()
                    .orElse(null);
            
            //TODO:  Compara si fue localizado el estudante
            if(estudianteSauitt !=null){return ResultadoEJB.crearCorrecto(estudianteSauitt, "Estudiante encontrado");}
            else{return ResultadoEJB.crearErroneo(2, estudianteSauitt, "No se econtró al estudiante.");}
            
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo hacer la busqueda", e, null);
        }
     
    }

    @Override
    public ResultadoEJB<Estudiante> getEstudianteCEClave(EstudiantesClaves estudiante) {
        try {
            if(estudiante==null){return ResultadoEJB.crearErroneo(3, "El estudiante no debe ser nulo.", Estudiante.class);}
            //TODO: Busca al estudiante en la base de CE por el idEstudiante
             Estudiante estudianteCE = fce.getEntityManager().createQuery("SELECT e FROM Estudiante e WHERE e.idEstudiante=:idEstudiante", Estudiante.class)
                    .setParameter("idEstudiante",estudiante.getIdEstudiante() )
                    .getResultStream()
                    .findAny()
                    .orElse(null);
             if(estudianteCE !=null){return ResultadoEJB.crearCorrecto(estudianteCE,"Se encontro al estudiante en la base de CE");}
             else{return ResultadoEJB.crearErroneo(2, estudianteCE, "No se localizó al estudiante en la base de CE");}
                  
             
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la busqueda (EjbAdmminEstudianteBase)", e, null);
            
        }
        
    }

    @Override
    public ResultadoEJB<dtoEstudiantesEvalauciones> getEstudianteActivobyMatriculaSauiit(String matricula) {
        try{
            dtoEstudiantesEvalauciones dto = new dtoEstudiantesEvalauciones();
            //TODO: Busca en la vista al estudiante por la matricula
            AlumnosEncuestas estudiante= fs.getEntityManager().createQuery("SELECT a from AlumnosEncuestas a where a.matricula=:matricula",AlumnosEncuestas.class)
                    .setParameter("matricula", matricula)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            //TODO:Si el resultado de la busqueda es nula, quiere decir que el estudiante no etsá activo
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,dto,"El estudiante no esta activo");}
            //TODO:Encontro al estudiante
            else{
                //TODO:Obtengo la clave de nomina tanto de su tutor como de su director de carreras
                ResultadoEJB<Personal > Restutor= getClaveNominabyClavePersona(estudiante.getCveMaestro());
                ResultadoEJB<Personal> Resdirector = getClaveNominabyClavePersona(Integer.parseInt(estudiante.getCveDirector()));
                if(Resdirector.getCorrecto()==true){ dto.setClaveDirector(Resdirector.getValor().getClave());}
                if(Restutor.getCorrecto()==true){dto.setClaveTutor(Restutor.getValor().getClave());}
                dto.setMatricula(estudiante.getMatricula());
                dto.setNombreCEstudiante(estudiante.getNombre() + " " + estudiante.getApellidoPat() + " " + estudiante.getApellidoPat());
                dto.setNombrePE(estudiante.getAbreviatura());
                dto.setGrado(estudiante.getGrado());
                dto.setGrupo(estudiante.getIdGrupo());
                dto.setNombreTutor(estudiante.getNombreTutor() + " " + estudiante.getApPatTutor()+ " "+ estudiante.getApMatTutor());
                return  ResultadoEJB.crearCorrecto(dto,"Estudiante activo");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la busqueda (EjbAdmminEstudianteBase,getEstudiantebyMatriculaSauiit)", e, null);
        }
    }

    @Override
    public ResultadoEJB<List<dtoEstudiantesEvalauciones>> getEstudiantesSauiit(PeriodosEscolares periodo) {
        try {
            
            List<AlumnosEncuestas> listAlumnosSauiit= new ArrayList<>();
            List<dtoEstudiantesEvalauciones> listDtoEstudiantesSauiit = new ArrayList<>();
            //TODO: Busca a los estudiantes activos 
            listAlumnosSauiit = fs.getEntityManager().createQuery("SELECT a FROM AlumnosEncuestas a", AlumnosEncuestas.class)
                    .getResultList()
                    ;
            System.out.println("Lista de vista --->" + listAlumnosSauiit.size());
            if(listAlumnosSauiit.isEmpty() || listAlumnosSauiit== null){return ResultadoEJB.crearErroneo(2,listDtoEstudiantesSauiit , "La lista de Estudiantes en Sauiit es nula");}
            else{
                System.out.println("La lista no esta vacía!");
                // TODO:recorre la lista general de estudiantes activos en sauiit para poder llenar el dto
                listAlumnosSauiit.forEach(x ->{

                    dtoEstudiantesEvalauciones dtoEstudiante = new dtoEstudiantesEvalauciones();
                    //TODO: como la clave de Tutor y del Director es clave persona, obtengo la clave de Nomina de ambos.
                    Personal tutor = getClaveNominabyClavePersona(x.getCveMaestro()).getValor();
                    Personal director= getClaveNominabyClavePersona(Integer.parseInt(x.getCveDirector())).getValor();
                    //TODO: Busca el numero de registro del estudiante por matricula
                   MatriculaPeriodosEscolares estudianteR = f.getEntityManager().createQuery("SELECT m FROM MatriculaPeriodosEscolares m WHERE m.matricula=:matricula AND m.periodo=:periodo", MatriculaPeriodosEscolares.class)
                                    .setParameter("matricula", x.getMatricula())
                                    .setParameter("periodo", periodo.getPeriodo() )
                                    .getResultStream()
                                    .findFirst()
                                    .orElse(null);

                   //TODO:Busca la clave del Estudiante por numero de registro
                    EstudiantesClaves estudianteC = f.getEntityManager().createQuery("SELECT e FROM EstudiantesClaves e WHERE e.registro=:registro", EstudiantesClaves.class)
                            .setParameter("registro", estudianteR.getRegistro())
                            .getResultStream()
                            .findFirst()
                            .orElse(null)
                            ;
                    //TODO: Llena el dto con los datos obtenidos
                    dtoEstudiante.setMatricula(x.getMatricula());
                    dtoEstudiante.setRegistro(estudianteR.getRegistro());
                    dtoEstudiante.setClaveEstudiante(estudianteC.getClave());
                    dtoEstudiante.setNombreCEstudiante(x.getNombre() + " " + x.getApellidoPat() + " " + x.getApellidoMat());
                    dtoEstudiante.setGrado(x.getGrado());
                    dtoEstudiante.setGrupo(x.getIdGrupo());
                    dtoEstudiante.setClavePE(x.getCveCarrera());
                    dtoEstudiante.setNombrePE(x.getAbreviatura());
                    dtoEstudiante.setClaveTutor(tutor.getClave());
                    dtoEstudiante.setNombreTutor(x.getNombreTutor() + " " + x.getApPatTutor() + " " + x.getApMatTutor());
                    dtoEstudiante.setClaveDirector(director.getClave());
                    //TODO: Agrega el dto a la lista
                    listDtoEstudiantesSauiit.add(dtoEstudiante);
                });
                System.out.println("Lista total--> " + listDtoEstudiantesSauiit.size());
            }
            if(listDtoEstudiantesSauiit.isEmpty()||listDtoEstudiantesSauiit ==null){return ResultadoEJB.crearErroneo(3, listDtoEstudiantesSauiit, "No se pudo hacer el llenado de la lista de estudiantes en sauiit.");}
            else{return ResultadoEJB.crearCorrecto(listDtoEstudiantesSauiit,"Lista de Estudiantes activos en sauiit creada correctamente");}
                
            
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la busqueda yllenado  (EjbAdmminEstudianteBase-->getEstudiantesSauiit)", e, null);
        }
        
      
    }

    @Override
    public ResultadoEJB<List<dtoEstudiantesEvalauciones>> getEstudiantesCE(PeriodosEscolares periodo) {
        //TODO: AQUI HABRÍA QUE HACER LA LISTA DEL DTO, UNA VEZ QUE EXITAN DATOS REALES EN CONTROL ESCOLAR!!
        try {
            
            
        } catch (Exception e) {
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultadoEJB<List<dtoEstudiantesEvalauciones>> getEstudiantesSauiityCE(PeriodosEscolares periodoEvaluacion) {
        try {
            List<dtoEstudiantesEvalauciones> listTotalEstudiantes = new ArrayList<>();
            if(periodoEvaluacion==null) {
                return ResultadoEJB.crearErroneo(3, listTotalEstudiantes, "El periodo de la evaluación no debe ser nulo");

            }else{
                System.out.println("Periodo que recibe en ejb del administracion de alumnos" + periodoEvaluacion.getPeriodo());
                List<dtoEstudiantesEvalauciones> listEstudiantesSauiit = new ArrayList<>();
                List<dtoEstudiantesEvalauciones> listEstudiantesCE = new ArrayList<>();

                //TODO:Obtiene la lista de Estudiantes en sauiit
                listEstudiantesSauiit = getEstudiantesSauiit(periodoEvaluacion).getValor();
                System.out.println("Genero la lista de estudiantes en Sauiit"+ listEstudiantesSauiit.size());
                //TODO: Obtiene la lista de Estudiantes en Control Escolar
               // listEstudiantesCE = getEstudiantesCE(periodoEvaluacion).getValor(); -->Descomentar cuando haya estudiantes en CE
                //TODO:Agrega ambas listas a la lista total de estudiantes
                //listTotalEstudiantes.addAll(listEstudiantesCE); --> Descomentar cuado haya estudiantes en CE
                listTotalEstudiantes.addAll(listEstudiantesSauiit);
                System.out.println("Se añadio a la lista general!" + listTotalEstudiantes.size());
                if (listTotalEstudiantes.isEmpty()) {
                    return ResultadoEJB.crearErroneo(3, listTotalEstudiantes, "La lista total esta vacia!");
                } else {
                    System.out.println("Se creo ResultadoEjbCorrecto");
                    return ResultadoEJB.crearCorrecto(listTotalEstudiantes, "Lista total");
                }
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la busqueda yllenado  (EjbAdmminEstudianteBase-->getTotalEstudiantes)", e, null);
        }

    }

    @Override
    public ResultadoEJB<Personal> getClaveNominabyClavePersona(int clavePersona) {
        try {
            Personal personal = new Personal();
            ListaUsuarioClaveNomina personalS =  fs.getEntityManager().createQuery("SELECT l FROM ListaUsuarioClaveNomina l WHERE l.cvePersona=:clave", ListaUsuarioClaveNomina.class)
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

    @Override
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


}
