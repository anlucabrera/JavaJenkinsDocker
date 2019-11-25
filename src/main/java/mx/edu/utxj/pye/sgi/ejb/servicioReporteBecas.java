package mx.edu.utxj.pye.sgi.ejb;

import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoReporteBecas;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.EstudiantesPye;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.BecaTipos;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.BecasPeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaAlumnosPye;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Stateless
public class servicioReporteBecas implements EjbReporteBecas {
    @EJB
    Facade f;
    @EJB
    Facade2 fs;
    private  EntityManager em;
    private EntityManager em2;
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
        em2 = fs.getEntityManager();
    }
    @Override
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosBecas() {
        try{
            List<PeriodosEscolares> periodos= new ArrayList<>();
            //TODO: Se obtiene la lista de periodos
            periodos = em.createQuery("SELECT p from PeriodosEscolares p ORDER BY p.periodo DESC",PeriodosEscolares.class)
                    .getResultList();
            if(periodos.isEmpty() || periodos==null){return ResultadoEJB.crearErroneo(2,periodos, "No se encontraron periodos");}
            else{return ResultadoEJB.crearCorrecto(periodos, "Se encontraron los periodos");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo crear la lista de periodos de registro de becas(EjbReporteBecas-getPeriodosBecas)", e, null);
        }
    }

    @Override
    public ResultadoEJB<List<BecasPeriodosEscolares>> getBecasbyPeriodo(PeriodosEscolares periodo) {
        try{
            //System.err.println("Periodo que recibe en ejb"+ periodo.getPeriodo());
            List<BecasPeriodosEscolares> registroBecas = new ArrayList<>();
            //TODO: Comprueba que el periodo no venga nulo
            if(periodo==null){return ResultadoEJB.crearErroneo(2,registroBecas,"El periodo no debe ser nulo");}
            else {
                //TODO: Hace la busqueda en la tabla
                registroBecas = em.createQuery("select b from BecasPeriodosEscolares b where b.matriculaPeriodosEscolares.periodo=:periodo", BecasPeriodosEscolares.class)
                        .setParameter("periodo",periodo.getPeriodo())
                        .getResultList();
                System.err.println("Becas" +registroBecas);
                if(registroBecas.isEmpty()){return ResultadoEJB.crearErroneo(3,registroBecas,"No se encontraron registros del periodo seleccionado");}
                else {
                    //TODO: Se agregan los resultados a la lista
                  
                    return ResultadoEJB.crearCorrecto(registroBecas,"Registros encontrados");
                }
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los registros de las becas por periodo(EjbReporteBecas-getBecasbyPeriodo)", e, null);
        }
    }

    @Override
    public ResultadoEJB<BecaTipos> getTipoBecabyClave(Integer clave_tipoBeca) {
        try{
            BecaTipos tipoBeca= new BecaTipos();
            //TODO:Verifica que la clave del tipo de beca no sea nulo
            if(clave_tipoBeca==null){return ResultadoEJB.crearErroneo(2,tipoBeca,"La clave de la beca no debe ser nulo.");}
            else{
                //TODO: Se ha la consulta en la tabla de tipo de becas
                BecaTipos tipoBecaC = em.createQuery("select b from BecaTipos b where b.becaTipo=:tipobeca", BecaTipos.class)
                        .setParameter("tipobeca",clave_tipoBeca)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                if(tipoBecaC!=null){
                    //TODO: Se regresa el valor de la beca encontrada
                    tipoBeca = tipoBecaC;
                    return ResultadoEJB.crearCorrecto(tipoBecaC,"Se ha encontrado el tipo de beca");
                }else {
                    //TODO: No se ha encontraado la beca
                    return  ResultadoEJB.crearErroneo(3,tipoBeca,"No se ha encontrado el tipo de beca");
                }
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el tipo de beca(EjbReporteBecas-getTipoBecabyClave)", e, null);
        }
    }

    @Override
    public ResultadoEJB<VistaAlumnosPye> getEstudiantesSauiitbyPeriodo(String matricula) {
        try{
            VistaAlumnosPye estudianteSauiit = new VistaAlumnosPye();
            //TODO: Comprobamos que los datos no vengan nulos
            if(matricula==null){return ResultadoEJB.crearErroneo(3,estudianteSauiit,"La matricula no debe ser nula");}
            //TODO: Se hace la consulta en la vista de alumno en sauiit
            estudianteSauiit = em2.createQuery("select v from VistaAlumnosPye v where v.matricula=:matricula", VistaAlumnosPye.class)
                .setParameter("matricula",matricula)
                .getResultStream()
                .findFirst()
                .orElse(null)
            ;
            if (estudianteSauiit==null){return ResultadoEJB.crearErroneo(4,estudianteSauiit,"El estudiante no esta registrado en sauiit");}
            else {
                return ResultadoEJB.crearCorrecto(estudianteSauiit,"Estudiante encontrado");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener al estudiantes en Sauiit(EjbReporteBecas-getEstudiantesSauiitbyPeriodo)", e, null);
        }
    }

    @Override
    public ResultadoEJB<EstudiantesPye> getEstudiantesCEbyPeriodo(String matricula) {
        try{
            EstudiantesPye estudianteCE= new EstudiantesPye();
            //TODO:Comprueba que los datos no vengan nulos
            if(matricula==null){return ResultadoEJB.crearErroneo(3,estudianteCE,"La matricula no debe ser nula");}
            //TODO: Se hace la consulta en la base de Control Escolar
            estudianteCE = em.createQuery("select e from EstudiantesPye e where e.matricula=:matricula", EstudiantesPye.class)
                            .setParameter("matricula",matricula)
                            .getResultStream()
                            .findFirst()
                            .orElse(null);
            if(estudianteCE==null){return  ResultadoEJB.crearErroneo(4,estudianteCE,"Estudiante no registrado en CE");}
            else {return ResultadoEJB.crearCorrecto(estudianteCE,"Estudiante encontrado");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el estudiante de CE(EjbReporteBecas-getEstudiantesCEbyPeriodo)", e, null);
        }
    }

    @Override
    public ResultadoEJB<AreasUniversidad> getAreabySiglas(String siglas) {
        try{
            AreasUniversidad area = new AreasUniversidad();
            //TODO:Comprueba que las siglas no vengan nulos
            if(siglas==null){return  ResultadoEJB.crearErroneo(2,area, "Las siglas no deben ser nulas");}
            else {
                //TODO: Se hace la consulta en la tabla por las siglas
                AreasUniversidad areas = em.createQuery("select a from AreasUniversidad  a where a.siglas=:siglas", AreasUniversidad.class)
                        .setParameter("siglas", siglas)
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
                        ;
                if(areas==null){return ResultadoEJB.crearErroneo(3,area,"No se encontro el area.");}
                else {
                    area= areas;
                    return ResultadoEJB.crearCorrecto(area,"Se ha encontrado el area");
                }
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el area por siglas(EjbReporteBecas-getAreabySiglas)", e, null);
        }
    }
    @Override
    public ResultadoEJB<AreasUniversidad> getAreabyClave(Integer clave_area) {
        try{
            AreasUniversidad area = new AreasUniversidad();
            //TODO: Comprueba que la clave del area no sea nula
            if(clave_area==null){ return  ResultadoEJB.crearErroneo(2,area,"La clave del area, no debe ser nula");}
            else {
                //TODO: Se hace la consulta en la base
                AreasUniversidad areas=em.createQuery("select a from AreasUniversidad a where a.area=:clave", AreasUniversidad.class)
                        .setParameter("clave",clave_area)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                if(areas==null){ return ResultadoEJB.crearErroneo(3,area,"No se encontro el area");}
                else {
                    area=areas;
                    return ResultadoEJB.crearCorrecto(area,"Se ha encontrado el area");
                }
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el area por clave(EjbReporteBecas-getAreabyClave)", e, null);
        }
    }

    @Override
    public ResultadoEJB<Personal> getDirectorArea(AreasUniversidad area) {
        try {
            Personal director = new Personal();
            //TODO:Comprueba que el area no sea nula
            if(area== null){return ResultadoEJB.crearErroneo(2,director,"El area no debe ser nulo");}
            else {
                //TODO:Se busca la clave del responsable del area
                Personal personal = em.createQuery("select p from Personal p where p.clave=:clave", Personal.class)
                        .setParameter("clave",area.getResponsable())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                if (personal==null){return ResultadoEJB.crearErroneo(3,director,"No se ha encontrado al responsable del area");}
                else {
                    director= personal;
                    return ResultadoEJB.crearCorrecto(director,"Se ha encontrado al encargado del area");
                }
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener al responsable del area(EjbReporteBecas-getDirectorArea)", e, null);
        }
    }
}
