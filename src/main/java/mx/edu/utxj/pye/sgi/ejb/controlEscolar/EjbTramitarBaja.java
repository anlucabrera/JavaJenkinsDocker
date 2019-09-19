/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoListadoTutores;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRegistroBajaEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoTramitarBajas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Baja;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.BajaReprobacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbTramitarBajas")
public class EjbTramitarBaja {
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbEstudianteBean ejbEstudianteBean;
    @EJB EjbPropiedades ep;
    @EJB EjbRegistroBajas ejb;
    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
    
      /**
     * Permite validar si el usuario autenticado es personal docente
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDocente(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.ACTIIVIDAD.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalDocenteActividad").orElse(3)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal docente no se pudo validar. (EjbTramitarBaja.validarDocente)", e, null);
        }
    }
    
     
    /**
     * Permite obtener el periodo actual
     * @return Resultado del proceso
     */
    public PeriodosEscolares getPeriodoActual() {

        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return new PeriodosEscolares();
        } else {
            return l.get(0);
        }
    }
    
    
     /**
     * Permite obtener la lista de periodos en los que el docente seleccionado tiene grupos tutorados
     * @param tutor Clave del docente
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosGruposTutorados(PersonalActivo tutor){
        System.err.println("getPeriodosGruposTutorados - tutor " + tutor.getPersonal().getClave());
        try{
            List<PeriodosEscolares> listaPeriodos = new ArrayList<>();
            
            List<Grupo> listaGrupos = em.createQuery("SELECT g FROM Grupo g WHERE g.tutor =:clave ORDER BY g.periodo DESC",  Grupo.class)
                    .setParameter("clave", tutor.getPersonal().getClave())
                    .getResultList();
          
            listaGrupos.forEach(grupo -> {
                PeriodosEscolares periodo = em.find(PeriodosEscolares.class, grupo.getPeriodo());
                listaPeriodos.add(periodo);
            });
            
             List<PeriodosEscolares> listaPeriodosDistintos = listaPeriodos.stream()
                    .distinct()
                    .collect(Collectors.toList());
             
            return ResultadoEJB.crearCorrecto(listaPeriodosDistintos, "Lista de periodo escolares en los que el docente tiene grupos tutorados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos en los que el docente tiene grupos tutorados académicas. (EjbTramitarBaja.getPeriodosGruposTutorados)", e, null);
        }
    }
   
    
    /**
     * Permite obtener una lista de grupos tutorados del periodo escolar seleccionado
     * @param tutor Clave del docente
     * @param periodo Clave del periodo seleccionado
     * @return Resultado del proceso ordenado 
     */
    public ResultadoEJB<List<DtoListadoTutores>> getGruposTutorados(PersonalActivo tutor, PeriodosEscolares periodo){
        try{
             //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            List<Grupo> grupos = em.createQuery("SELECT g FROM Grupo g WHERE g.tutor =:clave AND g.periodo =:periodo", Grupo.class)
                    .setParameter("clave", tutor.getPersonal().getClave())
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultList();
            
            List<DtoListadoTutores> listaDtoTutores = new ArrayList<>();
            
            grupos.forEach(grupo -> {
                Personal personal = em.find(Personal.class, grupo.getTutor());
                PersonalActivo personalActivo = new PersonalActivo(personal);
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, grupo.getIdPe());
                DtoListadoTutores dtoListadoTutores = new DtoListadoTutores(grupo, personalActivo, programaEducativo);
                listaDtoTutores.add(dtoListadoTutores);
            });
            
            List<DtoListadoTutores> listaDtoTutoresOrdenada = listaDtoTutores.stream()
                    .sorted(DtoListadoTutores::compareTo)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaDtoTutoresOrdenada, "Lista de grupo tutorados por el personal docente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de grupos tutorados por el personal docente. (EjbTramitarBaja.getGruposTutorados)", e, null);
        }
    }
    
     /**
     * Permite obtener lista de estudiante del grupo seleccionado
     * @param grupo Clave del grupo
     * @return Resultado del proceso con 
     */
    public ResultadoEJB<List<DtoTramitarBajas>> obtenerListaEstudiantes(Grupo grupo){
        try{
             //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            List<Estudiante> estudiantes = em.createQuery("select e from Estudiante e where e.grupo.idGrupo =:grupo", Estudiante.class)
                    .setParameter("grupo", grupo.getIdGrupo())
                    .getResultList();
           
            List<DtoTramitarBajas> listaDtoTramitarBajas = new ArrayList<>();
            
            estudiantes.forEach(estudiante -> {
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());
                PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
                DtoDatosEstudiante dtoDatosEstudiante = new DtoDatosEstudiante(estudiante, programaEducativo, periodoEscolar);
                DtoRegistroBajaEstudiante registroBajaEstudiante = ejb.buscarRegistroBajaEstudiante(estudiante.getIdEstudiante()).getValor();
                
                DtoRegistroBajaEstudiante dtoRegistroBajaEstudiante = new DtoRegistroBajaEstudiante();
                
                if(registroBajaEstudiante != null){
                    dtoRegistroBajaEstudiante = ejb.buscarRegistroBajaEstudiante(estudiante.getIdEstudiante()).getValor();
                }
                
                DtoTramitarBajas dtoTramitarBajas = new DtoTramitarBajas();
                dtoTramitarBajas.setDtoEstudiante(dtoDatosEstudiante);
                dtoTramitarBajas.setDtoRegistroBaja(dtoRegistroBajaEstudiante);
                listaDtoTramitarBajas.add(dtoTramitarBajas);
            });
            
             List<DtoTramitarBajas> listaDtoTramitarBajasOrdenada = listaDtoTramitarBajas.stream()
                    .sorted(DtoTramitarBajas::compareTo)
                    .collect(Collectors.toList());
           
            return ResultadoEJB.crearCorrecto(listaDtoTramitarBajasOrdenada, "Lista de estudiantes del grupo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de los estudiantes seleccionados. (EjbTramitarBaja.obtenerListaEstudiantes)", e, null);
        }
    }
    /**
     * Permite guardar el registro de la baja
     * @param periodoEscolar Clave del periodo escolar activo
     * @param estudiante Estudiante que se dará de baja
     * @param tipoBaja Tipo de baja que se registrará
     * @param causaBaja Causa de la baja que se registrará
     * @param personal Personal que registrará la baja
     * @param fechaBaja Fecha de la baja
     * @return Resultado del proceso
     */
    public ResultadoEJB<Baja> guardarTramitarBaja(Integer periodoEscolar, DtoDatosEstudiante estudiante, BajasTipo tipoBaja, BajasCausa causaBaja, PersonalActivo personal, Date fechaBaja){
        try{   
            Baja registroBaja = new Baja();
            registroBaja.setPeriodoEscolar(periodoEscolar);
            Estudiante estudianteBaja = em.find(Estudiante.class, estudiante.getEstudiante().getIdEstudiante());
            registroBaja.setEstudiante(estudianteBaja);
            registroBaja.setTipoBaja(tipoBaja.getTipoBaja());
            registroBaja.setCausaBaja(causaBaja.getCveCausa());
            registroBaja.setEmpleado(personal.getPersonal().getClave());
            registroBaja.setFechaBaja(fechaBaja);
            em.persist(registroBaja);
            em.flush();
            
            if(registroBaja.getCausaBaja() == 3)
            {
                   List<CargaAcademica> listaCargasAcademicas = em.createQuery("SELECT ca FROM CargaAcademica ca WHERE ca.cveGrupo.idGrupo =:grupo", CargaAcademica.class)
                           .setParameter("grupo", estudiante.getEstudiante().getGrupo().getIdGrupo())
                           .getResultList();
                   
                    listaCargasAcademicas.forEach(cargaAcademica -> {
                            BajaReprobacion bajaReprobacion = new BajaReprobacion();
                            bajaReprobacion.setRegistroBaja(registroBaja);
                            bajaReprobacion.setCargaAcademica(cargaAcademica);
                            em.persist(bajaReprobacion);
                    });
            
            }
           
            Baja baja = em.createQuery("SELECT b FROM Baja b WHERE b.estudiante.idEstudiante =:estudiante", Baja.class)
                    .setParameter("estudiante", registroBaja.getEstudiante().getIdEstudiante())
                    .getSingleResult();
            
            return ResultadoEJB.crearCorrecto(baja, "La baja se ha registrado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la baja. (EjbTramitarBaja.guardarRegistroBaja)", e, null);
        }
    }
    
     /**
     * Permite obtener la información de la baja registrada
     * @param claveEstudiante Clave del estudiante para buscar registro
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> buscarRegistroBajaEstudiante(Integer claveEstudiante) {
        try{
            Boolean valor = true;
            
            Baja registroBaja = em.createQuery("SELECT b FROM Baja b WHERE b.estudiante.idEstudiante =:estudiante", Baja.class)
                    .setParameter("estudiante", claveEstudiante)
                    .getSingleResult();
            if(registroBaja.getIdBajas() == 0)
            {
                valor= false;
            }
            return ResultadoEJB.crearCorrecto(valor, "Existe o no registro de baja del estudiante");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existe o no registro de baja del estudiante. (EjbTramitarBaja.buscarRegistroBajaEstudiante)", e, null);
        }
    }
    
}
