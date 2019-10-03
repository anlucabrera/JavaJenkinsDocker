/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.StoredProcedureQuery;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaReprobada;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoFormatoBaja;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoListadoTutores;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRangoFechasPermiso;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRegistroBajaEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoTramitarBajas;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoValidacionesBaja;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroBajas")
public class EjbRegistroBajas {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbEstudianteBean ejbEstudianteBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
     /* MÓDULO DE REGISTRO DE BAJA SERVICIOS ESCOLARES */
    
     /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios escolares
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarServiciosEscolares(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(10)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbRegistroBajas.validarServiciosEscolares)", e, null);
        }
    }
    /**
     * Permite identificar a una lista de posibles estudiante para registrar la baja
     * @param pista Contenido que la vista que puede incluir parte del nombre, apellidos o matricula del estudiante
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarEstudiante(String pista){
        try{
             //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            List<EstudiantesPye> estudiantes = em.createQuery("select e from EstudiantesPye e where concat(e.aPaterno, e.aMaterno, e.nombre, e.matricula) like concat('%',:pista,'%')  ", EstudiantesPye.class)
                    .setParameter("pista", pista)
                    .getResultList();
            
            List<DtoEstudianteComplete> listaDtoEstudiantes = new ArrayList<>();
            
            estudiantes.forEach(estudiante -> {
                String datosComplete = estudiante.getAPaterno() +" "+ estudiante.getAMaterno() +" "+ estudiante.getNombre() + " - " + estudiante.getMatricula();
                DtoEstudianteComplete dtoEstudianteComplete = new DtoEstudianteComplete(estudiante, datosComplete);
                listaDtoEstudiantes.add(dtoEstudianteComplete);
            });
            
            return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Lista para mostrar en autocomplete");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de estudiantes activos. (EjbRegistroBajas.buscarEstudiante)", e, null);
        }
    }
    
     /**
     * Permite identificar a una lista de posibles estudiante para registrar la baja
     * @param claveEstudiante Clave del estudiante
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<DtoDatosEstudiante> buscarDatosEstudiante(Integer claveEstudiante){
        try{
             //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            Estudiante estudiante = em.createQuery("select e from Estudiante e where e.idEstudiante =:estudiante", Estudiante.class)
                    .setParameter("estudiante", claveEstudiante)
                    .getSingleResult();
            
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());
            PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
            DtoDatosEstudiante dtoDatosEstudiante = new DtoDatosEstudiante(estudiante, programaEducativo,periodoEscolar);
           
            return ResultadoEJB.crearCorrecto(dtoDatosEstudiante, "Datos del estudiante seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los datos del estudiante seleccionado. (EjbRegistroBajas.buscarDatosEstudiante)", e, null);
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
     * Permite obtener la lista de tipos de baja activas para registrar la baja
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<BajasTipo>> getTiposBaja(){
        try{
            List<BajasTipo> bajasTipos = em.createQuery("SELECT bt FROM BajasTipo bt ORDER BY bt.descripcion ASC", BajasTipo.class)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(bajasTipos, "Lista de tipos de baja para realizar el registro de baja.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tipos de bajas para el registro de baja. (EjbRegistroBajas.getTiposBaja)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de tipos de baja activas para registrar la baja
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<BajasTipo>> getTiposBajaTutor(){
        try{
            List<BajasTipo> bajasTipos = em.createQuery("SELECT bt FROM BajasTipo bt WHERE bt.tipoBaja =:tipo  ORDER BY bt.descripcion ASC", BajasTipo.class)
                    .setParameter("tipo", (int) 1)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(bajasTipos, "Lista de tipos de baja para realizar el registro de baja.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tipos de bajas para el registro de baja. (EjbRegistroBajas.getTiposBaja)", e, null);
        }
    }
    
    
      /**
     * Permite obtener la lista de causas de baja para registrar la baja
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<BajasCausa>> getCausasBaja(){
        try{
            List<BajasCausa> bajasCausas = em.createQuery("SELECT bc FROM BajasCausa bc ORDER BY bc.causa ASC", BajasCausa.class)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(bajasCausas, "Lista de causas de baja para realizar el registro de baja.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de causas de baja para el registro de baja. (EjbRegistroBajas.getCausasBaja)", e, null);
        }
    }
    
     /**
     * Permite obtener el rango de fechas dependiendo del periodo escolar activo
     * @param periodoEscolar Periodo Escolar activ
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoRangoFechasPermiso> getRangoFechas(Integer periodoEscolar){
        try{
           
            PeriodoEscolarFechas fechasPeriodos = em.createQuery("SELECT pef FROM PeriodoEscolarFechas pef WHERE pef.periodosEscolares.periodo =:periodo", PeriodoEscolarFechas.class)
                    .setParameter("periodo", periodoEscolar)
                    .getSingleResult();
           
            Date fechaInicio=fechasPeriodos.getInicio();
            Date fechaFin = fechasPeriodos.getFin();
            
            DtoRangoFechasPermiso dtoRangoFechasPermiso = new DtoRangoFechasPermiso(fechaInicio, fechaFin);
            
            return ResultadoEJB.crearCorrecto(dtoRangoFechasPermiso, "Rango de fechas obtenidas para el periodo escolar activo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el rango de fechas del periodo escolar activo. (EjbRegistroBajas.getRangoFechas)", e, null);
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
    public ResultadoEJB<Baja> guardarRegistroBaja(Integer periodoEscolar, DtoDatosEstudiante estudiante, BajasTipo tipoBaja, BajasCausa causaBaja, PersonalActivo personal, Date fechaBaja){
        try{   
            Baja registroBaja = new Baja();
            registroBaja.setPeriodoEscolar(periodoEscolar);
            Estudiante estudianteBaja = em.find(Estudiante.class, estudiante.getEstudiante().getIdEstudiante());
            registroBaja.setEstudiante(estudianteBaja);
            registroBaja.setTipoBaja(tipoBaja.getTipoBaja());
            registroBaja.setCausaBaja(causaBaja.getCveCausa());
            registroBaja.setEmpleadoRegistro(personal.getPersonal().getClave());
            registroBaja.setFechaBaja(fechaBaja);
            registroBaja.setAccionesTutor("Sin información");
            registroBaja.setDictamenPsicopedagogia("Sin información");
            registroBaja.setFechaValidacion(new Date());
            registroBaja.setValidada((int)1);
            em.persist(registroBaja);
            em.flush();
            
            if (registroBaja.getTipoBaja() == 1) {

                TipoEstudiante tipoEstudiante = em.find(TipoEstudiante.class, (short)2);

                Integer t = em.createQuery("update Estudiante e set e.tipoEstudiante =:tipoEstudiante where e.idEstudiante =:estudiante")
                        .setParameter("tipoEstudiante", tipoEstudiante)
                        .setParameter("estudiante", registroBaja.getEstudiante().getIdEstudiante())
                        .executeUpdate();
            } else {

               TipoEstudiante tipoEstudiante = em.find(TipoEstudiante.class, (short)3);

                Integer t = em.createQuery("update Estudiante e set e.tipoEstudiante =:tipoEstudiante where e.idEstudiante =:estudiante")
                        .setParameter("tipoEstudiante", tipoEstudiante)
                        .setParameter("estudiante", registroBaja.getEstudiante().getIdEstudiante())
                        .executeUpdate();
            }
            
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
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la baja. (EjbRegistroBajas.guardarRegistroBaja)", e, null);
        }
    }
    
     /**
     * Permite obtener la información de la baja registrada
     * @param claveEstudiante Clave del estudiante para buscar registro
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoRegistroBajaEstudiante> buscarRegistroBajaEstudiante(Integer claveEstudiante) {
        try{
           
            Estudiante estudiante = em.find(Estudiante.class, claveEstudiante);
            
            Baja registroBaja = em.createQuery("SELECT b FROM Baja b WHERE b.estudiante.idEstudiante =:estudiante", Baja.class)
                    .setParameter("estudiante", claveEstudiante)
                    .getSingleResult();
            
                BajasTipo tipoBaja = em.find(BajasTipo.class, registroBaja.getTipoBaja());
                BajasCausa causaBaja = em.find(BajasCausa.class, registroBaja.getCausaBaja());
                Personal personal = em.find(Personal.class, registroBaja.getEmpleadoRegistro());
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());
                PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
            
                DtoRegistroBajaEstudiante dtoRegistroBajaEstudiante = new DtoRegistroBajaEstudiante(registroBaja, tipoBaja, causaBaja, personal, programaEducativo, periodoEscolar);
                
            return ResultadoEJB.crearCorrecto(dtoRegistroBajaEstudiante, "Información de la baja registrada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la información de la baja registrada. (EjbRegistroBajas.buscarRegistroBajaEstudiante)", e, null);
        }
    }
    
    public void actualizarRegistroBaja(DtoRegistroBajaEstudiante dtoRegistroBajaEstudiante) {
            f.setEntityClass(Baja.class);
            f.edit(dtoRegistroBajaEstudiante.getRegistroBaja());
            f.flush();
    }
     
      /**
     * Permite eliminar el registro de baja
     * @param registro Registro de baja
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarRegistroBaja(Baja registro){
        try{
            if(registro == null) return ResultadoEJB.crearErroneo(2, "La clave de la baja no puede ser nula.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM Baja b WHERE b.idBajas =:baja", Baja.class)
                .setParameter("baja", registro.getIdBajas())
                .executeUpdate();
           
           if(delete == 1)
           {
               TipoEstudiante tipoEstudiante = em.find(TipoEstudiante.class, (short)1);
               
               Integer t = em.createQuery("update Estudiante e set e.tipoEstudiante =:tipoEstudiante where e.idEstudiante =:estudiante")
                        .setParameter("tipoEstudiante", tipoEstudiante)
                        .setParameter("estudiante", registro.getEstudiante().getIdEstudiante())
                        .executeUpdate();
           }

            return ResultadoEJB.crearCorrecto(delete, "El registro de la baja se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el registro de la baja. (EjbRegistroBajas.eliminarRegistroBaja)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de materias reprobadas en caso de ser baja por reprobación
     * @param registro Clave de registro de baja
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoMateriaReprobada>> buscarMateriasReprobadas(Baja registro) {
        try{
            List<DtoMateriaReprobada> listaMateriasReprobadas = new ArrayList<>();
            
            List<BajaReprobacion> listaBajaReprobacion = em.createQuery("SELECT br FROM BajaReprobacion br WHERE br.registroBaja.idBajas =:baja ORDER BY br.cargaAcademica.idPlanMateria.claveMateria ASC", BajaReprobacion.class)
                    .setParameter("baja", registro.getIdBajas())
                    .getResultList();
            
            listaBajaReprobacion.forEach(bajaReprobacion -> {
                Personal personal = em.find(Personal.class, bajaReprobacion.getCargaAcademica().getDocente());
                DtoMateriaReprobada dtoMateriaReprobada = new DtoMateriaReprobada(bajaReprobacion, personal);
                listaMateriasReprobadas.add(dtoMateriaReprobada);
                
            });
            
            return ResultadoEJB.crearCorrecto(listaMateriasReprobadas, "Lista de materias reprobadas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias reprobdas. (EjbRegistroBajas.buscarMateriasReprobadas)", e, null);
        }
    }
    
      /**
     * Permite eliminar el registro de baja
     * @param registro Registro de baja
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarRegistroMateriaReprobada(BajaReprobacion registro){
        try{
            if(registro == null) return ResultadoEJB.crearErroneo(2, "La clave de la materia reprobada no puede ser nula.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM BajaReprobacion b WHERE b.bajaReprobacion=:baja", BajaReprobacion.class)
                .setParameter("baja", registro.getBajaReprobacion())
                .executeUpdate();
          
            return ResultadoEJB.crearCorrecto(delete, "El registro de la materia reprobada se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el registro de la materia reprobada. (EjbRegistroBajas.eliminarRegistroMateriaReprobada)", e, null);
        }
    }
    
    /**
     * Permite obtener la información para generar formato de baja
     * @param registro Registro de la baja
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoFormatoBaja> generarFormatoBaja(Baja registro) {
        try{
                DtoRegistroBajaEstudiante dtoRegistroBajaEstudiante = buscarRegistroBajaEstudiante(registro.getEstudiante().getIdEstudiante()).getValor();
               
                List<DtoMateriaReprobada> listaDtoMateriaReprobada = buscarMateriasReprobadas(registro).getValor();
                
                List<DtoDocumentosEstudiante> listaDocumentosEstudiante = buscarDocumentosEstudiante(registro.getEstudiante().getIdEstudiante()).getValor();
                
                AreasUniversidad areaSuperior = em.find(AreasUniversidad.class, dtoRegistroBajaEstudiante.getProgramaEducativo().getAreaSuperior());
                
                Personal director = em.find(Personal.class, areaSuperior.getResponsable());
                
                Personal tutor = em.find(Personal.class, registro.getEstudiante().getGrupo().getTutor());
            
                DtoFormatoBaja dtoFormatoBaja = new DtoFormatoBaja(dtoRegistroBajaEstudiante, listaDtoMateriaReprobada, listaDocumentosEstudiante, director, tutor);
                
            return ResultadoEJB.crearCorrecto(dtoFormatoBaja, "Información de para generar formato de baja.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la información para generar formato de baja. (EjbRegistroBajas.generarFormatoBaja)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de documentos entregados por el estudiante
     * @param estudiante Clave del estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoDocumentosEstudiante>> buscarDocumentosEstudiante(Integer estudiante) {
        try{
            List<DtoDocumentosEstudiante> listaDocumentosEstudiante = new ArrayList<>();
            
            Documentosentregadosestudiante documentosEstudiante = em.createQuery("SELECT de FROM Documentosentregadosestudiante de WHERE de.estudiante =:estudiante", Documentosentregadosestudiante.class)
                    .setParameter("estudiante", estudiante)
                    .getSingleResult();
           
            if (documentosEstudiante.getActaNacimiento()) {
                DtoDocumentosEstudiante actaNacimiento = new DtoDocumentosEstudiante(estudiante, "Acta de Nacimiento");
                listaDocumentosEstudiante.add(actaNacimiento);
            }
            if (documentosEstudiante.getCertificadoIems()) {
                DtoDocumentosEstudiante certificadoBachillerato = new DtoDocumentosEstudiante(estudiante, "Certificado de Bachillerato");
                listaDocumentosEstudiante.add(certificadoBachillerato);
            }
            if (documentosEstudiante.getCurp()) {
                DtoDocumentosEstudiante curp = new DtoDocumentosEstudiante(estudiante, "CURP");
                listaDocumentosEstudiante.add(curp);
            }
            DtoDocumentosEstudiante copias = new DtoDocumentosEstudiante(estudiante, "Copias Varias");
            listaDocumentosEstudiante.add(copias);
            
            return ResultadoEJB.crearCorrecto(listaDocumentosEstudiante, "Lista de materias reprobadas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias reprobadas. (EjbRegistroBajas.buscarMateriasReprobadas)", e, null);
        }
    }
    
    /* MÓDULO DE TRAMITAR BAJA TUTOR */
    
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
            return ResultadoEJB.crearErroneo(1, "El personal docente no se pudo validar. (EjbRegistroBajas.validarDocente)", e, null);
        }
    }
   
     /**
     * Permite obtener la lista de periodos en los que el docente seleccionado tiene grupos tutorados
     * @param tutor Clave del docente
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosGruposTutorados(PersonalActivo tutor){
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
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos en los que el docente tiene grupos tutorados académicas. (EjbRegistroBajas.getPeriodosGruposTutorados)", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de grupos tutorados por el personal docente. (EjbRegistroBajas.getGruposTutorados)", e, null);
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
                DtoRegistroBajaEstudiante registroBajaEstudiante = buscarRegistroBajaEstudiante(estudiante.getIdEstudiante()).getValor();
                
                DtoRegistroBajaEstudiante dtoRegistroBajaEstudiante = new DtoRegistroBajaEstudiante();
                
                if(registroBajaEstudiante != null){
                    dtoRegistroBajaEstudiante = buscarRegistroBajaEstudiante(estudiante.getIdEstudiante()).getValor();
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
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de los estudiantes seleccionados. (EjbRegistroBajas.obtenerListaEstudiantes)", e, null);
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
    public ResultadoEJB<Baja> guardarTramitarBaja(Integer periodoEscolar, DtoDatosEstudiante estudiante, BajasTipo tipoBaja, BajasCausa causaBaja, String acciones, PersonalActivo personal, Date fechaBaja){
        try{   
            Baja registroBaja = new Baja();
            registroBaja.setPeriodoEscolar(periodoEscolar);
            Estudiante estudianteBaja = em.find(Estudiante.class, estudiante.getEstudiante().getIdEstudiante());
            registroBaja.setEstudiante(estudianteBaja);
            registroBaja.setTipoBaja(tipoBaja.getTipoBaja());
            registroBaja.setCausaBaja(causaBaja.getCveCausa());
            registroBaja.setEmpleadoRegistro(personal.getPersonal().getClave());
            registroBaja.setFechaBaja(fechaBaja);
            registroBaja.setAccionesTutor(acciones);
            registroBaja.setDictamenPsicopedagogia("Sin información");
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
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la baja. (EjbRegistroBajas.guardarRegistroBaja)", e, null);
        }
    }
    
    /**
     * Permite actualizar el registro de la baja
     * @param periodoEscolar Clave del periodo escolar activo
     * @param dtoRegistroBaja Registro de baja del estudiante que se va a actualizar
     * @param tipoBaja Tipo de baja que se registrará
     * @param causaBaja Causa de la baja que se registrará
     * @param acciones Acciones tomadas por el tutor
     * @param personal Personal que registrará la baja
     * @param fechaBaja Fecha de la baja
     * @return Resultado del proceso
     */
    public ResultadoEJB<Baja> actualizarTramitarBaja(Integer periodoEscolar, DtoRegistroBajaEstudiante dtoRegistroBaja, BajasTipo tipoBaja, BajasCausa causaBaja, String acciones, PersonalActivo personal, Date fechaBaja){
        try{
            
            Baja registroBaja = em.find(Baja.class, dtoRegistroBaja.getRegistroBaja().getIdBajas());
            registroBaja.setTipoBaja(tipoBaja.getTipoBaja());
            registroBaja.setCausaBaja(causaBaja.getCveCausa());
            registroBaja.setFechaBaja(fechaBaja);
            registroBaja.setAccionesTutor(acciones);
            f.edit(registroBaja);
            em.flush();
           
                Integer delete = em.createQuery("DELETE FROM BajaReprobacion b WHERE b.registroBaja.idBajas =:baja", Baja.class)
                        .setParameter("baja", registroBaja.getIdBajas())
                        .executeUpdate();
            
            if(registroBaja.getCausaBaja() == 3)
            {
                   List<CargaAcademica> listaCargasAcademicas = em.createQuery("SELECT ca FROM CargaAcademica ca WHERE ca.cveGrupo.idGrupo =:grupo", CargaAcademica.class)
                           .setParameter("grupo", registroBaja.getEstudiante().getGrupo().getIdGrupo())
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
            
            return ResultadoEJB.crearCorrecto(baja, "La baja se ha actualizado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la baja. (EjbRegistroBajas.actualizarTramitarBaja)", e, null);
        }
    }
    
     /**
     * Permite obtener la información de la baja registrada
     * @param claveEstudiante Clave del estudiante para buscar registro
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> existeRegistroBajaEstudiante(Integer claveEstudiante) {
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
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existe o no registro de baja del estudiante. (EjbRegistroBajas.buscarRegistroBajaEstudiante)", e, null);
        }
    }
    
    /* MÓDULO DE DICTAMEN DE BAJA ÁREA DE PSICOPEDAGOGIA*/
    
      /**
     * Permite validar si el usuario autenticado es personal docente
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarPsicopedagogia(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalPsicopedagogia").orElse(18)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal del área de psicopedagogía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal del área no se pudo validar. (EjbRegistroBajas.validarPsicopedagogia)", e, null);
        }
    }
   
     /**
     * Permite obtener la lista de periodos en los que se han registrado bajas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosBajas(){
        try{
            List<PeriodosEscolares> listaPeriodos = new ArrayList<>();
            
            List<Baja> periodosBaja = em.createQuery("SELECT b FROM Baja b ORDER BY b.periodoEscolar DESC",  Baja.class)
                    .getResultList();
          
            periodosBaja.forEach(periodoBaja -> {
                PeriodosEscolares periodo = em.find(PeriodosEscolares.class, periodoBaja.getPeriodoEscolar());
                listaPeriodos.add(periodo);
            });
            
             List<PeriodosEscolares> listaPeriodosDistintos = listaPeriodos.stream()
                    .distinct()
                    .collect(Collectors.toList());
             
            return ResultadoEJB.crearCorrecto(listaPeriodosDistintos, "Lista de periodo escolares en los que se han registrado bajas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos en los que se han registrado bajas. (EjbRegistroBajas.getPeriodosBajas)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de programas educativos que tienen registradas bajas dependiendo del área superior seleccionada
     * @param bajas Lista de bajas registradas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> getProgramasEducativos(List<DtoTramitarBajas> bajas){
         try{
            List<AreasUniversidad> listaProgramasEducativos = new ArrayList<>();
            
            bajas.forEach(baja -> {
                AreasUniversidad area = em.find(AreasUniversidad.class, baja.getDtoEstudiante().getProgramaEducativo().getArea());
                listaProgramasEducativos.add(area);
            });
            
             List<AreasUniversidad> listaProgramasDistintos = listaProgramasEducativos.stream()
                    .distinct()
                    .collect(Collectors.toList());
             
            return ResultadoEJB.crearCorrecto(listaProgramasDistintos, "Lista de programas educativos que tienen registro de baja.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos que tienen registro de baja. (EjbRegistroBajas.getProgramasEducativos)", e, null);
        }
    }
   
    
    /**
     * Permite obtener lista de bajas registradas del periodo escolar seleccionado ordenadas por fecha
     * @param periodo Clave del periodo seleccionado
     * @return Resultado del proceso ordenado 
     */
    public ResultadoEJB<List<DtoTramitarBajas>> obtenerListaBajasPeriodo(PeriodosEscolares periodo){
        try{
            List<Baja> bajas = em.createQuery("SELECT b FROM Baja b WHERE b.periodoEscolar =:periodo ORDER BY b.fechaBaja DESC", Baja.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultList();
            
            List<DtoTramitarBajas> listaDtoTramitarBajas = new ArrayList<>();
            
            bajas.forEach(baja -> {
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, baja.getEstudiante().getCarrera());
                PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, baja.getPeriodoEscolar());
                DtoDatosEstudiante dtoDatosEstudiante = new DtoDatosEstudiante(baja.getEstudiante(), programaEducativo, periodoEscolar);
                DtoRegistroBajaEstudiante dtoRegistroBajaEstudiante = buscarRegistroBajaEstudiante(baja.getEstudiante().getIdEstudiante()).getValor();
               
                DtoTramitarBajas dtoTramitarBajas = new DtoTramitarBajas();
                dtoTramitarBajas.setDtoEstudiante(dtoDatosEstudiante);
                dtoTramitarBajas.setDtoRegistroBaja(dtoRegistroBajaEstudiante);
                listaDtoTramitarBajas.add(dtoTramitarBajas);
            });
            
            return ResultadoEJB.crearCorrecto(listaDtoTramitarBajas, "Lista de bajas registradas en el periodo escolar seleccionado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de bajas registradas. (EjbRegistroBajas.getBajasPeriodo)", e, null);
        }
    }
    
    /**
     * Permite obtener el dictamen registrado
     * @param baja Clave de la baja
     * @return Resultado del proceso ordenado 
     */
    public ResultadoEJB<String> buscarDictamenBajaPsicopedagogia(Baja baja){
        try{
            Baja registrobaja = em.createQuery("SELECT b FROM Baja b WHERE b.idBajas =:baja ", Baja.class)
                    .setParameter("baja", baja.getIdBajas())
                    .getSingleResult();
            
            String dictamen = registrobaja.getDictamenPsicopedagogia();
           
            return ResultadoEJB.crearCorrecto(dictamen, "Dictamen realizado por psicopedagogía");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el dictamen realizado por psicpedagogía. (EjbRegistroBajas.buscarDictamenBajaPsicopedagogia)", e, null);
        }
    }
    
    /**
     * Permite guardar o actualizar el dictamen 
     * @param baja Registro de baja del estudiante que se va a actualizar
     * @param dictamen Dictamen realizado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Baja> actualizarDictamenBaja(Baja baja, String dictamen){
        try{
            Integer valido =1;
            Baja registroBaja = em.find(Baja.class, baja.getIdBajas());
            registroBaja.setDictamenPsicopedagogia(dictamen);
            registroBaja.setFechaValpsicopedagogia(new Date());
            registroBaja.setValidoPsicopedagogia(valido);
            f.edit(registroBaja);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(registroBaja, "El dictamen se ha registrado o actualizado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo guardado o actualizar la baja. (EjbRegistroBajas.actualizarDictamenBaja)", e, null);
        }
    }
    
    /* MÓDULO DE VALIDACIÓN DE BAJAS DIRECTOR DE CARRERA */
    
     /**
     * Permite crear el filtro para validar si el usuario autenticado es un director de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDirector(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorCategoriaOperativa").orElse(18)));
            return ResultadoEJB.crearCorrecto(filtro, "El filtro del usuario ha sido preparado como un director.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El director no se pudo validar. (EjbAsignacionAcademica.validarDirector)", e, null);
        }
    }
    
    /**
     * Permite crear el filtro para validar si el usuario autenticado es un encarcado de dirección de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarEncargadoDireccion(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorEncargadoCategoriaOperativa").orElse(48)));
            return ResultadoEJB.crearCorrecto(filtro, "El filtro del usuario ha sido preparado como un encargado de dirección.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El encargado de dirección de área académica no se pudo validar. (EjbAsignacionAcademica.validarDirector)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de áreas académicas de las que se han registrado bajas
     * @param clave Clave del responsable del área
     * @return Resultado del proceso
     */
    public ResultadoEJB<AreasUniversidad> getAreaSuperior(Integer clave){
         try{
             
            AreasUniversidad areaSuperior = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.responsable =:clave",  AreasUniversidad.class)
                    .setParameter("clave", clave)
                    .getSingleResult();
          
            
            return ResultadoEJB.crearCorrecto(areaSuperior, "Área Superior de la que es responsable el director.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el área superior de la que es responsable el director. (EjbRegistroBajas.getAreaSuperior)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de programas educativos que tienen registradas bajas dependiendo del área superior seleccionada
     * @param bajas Lista de bajas registradas
     * @param areaSuperior Clave del área superior
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> getProgramasEducativosDirector(List<DtoTramitarBajas> bajas, AreasUniversidad areaSuperior){
         try{
            List<AreasUniversidad> listaProgramasEducativos = new ArrayList<>();
            
            bajas.forEach(baja -> {
                AreasUniversidad area = em.find(AreasUniversidad.class, baja.getDtoEstudiante().getProgramaEducativo().getArea());
                if(area.getAreaSuperior().equals(areaSuperior.getArea())){
                    listaProgramasEducativos.add(area);
                }
            });
            
             List<AreasUniversidad> listaProgramasDistintos = listaProgramasEducativos.stream()
                    .distinct()
                    .collect(Collectors.toList());
             
            return ResultadoEJB.crearCorrecto(listaProgramasDistintos, "Lista de programas educativos del área superior correspondiente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos del área superior correspondiente. (EjbRegistroBajas.getProgramasEducativosDirector)", e, null);
        }
    }
    
    /**
     * Permite obtener lista de bajas registradas del periodo escolar y programa educativo seleccionado
     * @param bajasPeriodo Lista de bajas registradas
     * @param programaEducativo Programa Educativo del que se hará al consulta
     * @return Resultado del proceso ordenado 
     */
    public ResultadoEJB<List<DtoTramitarBajas>> obtenerListaBajasProgramaEducativo(List<DtoTramitarBajas> bajasPeriodo, AreasUniversidad programaEducativo){
        try{
            
            List<DtoTramitarBajas> listaDtoTramitarBajas = new ArrayList<>();
            
            bajasPeriodo.forEach(baja -> {
                if (baja.getDtoEstudiante().getProgramaEducativo().getArea().equals(programaEducativo.getArea())) {
                    listaDtoTramitarBajas.add(baja);
                }
            });
            
            return ResultadoEJB.crearCorrecto(listaDtoTramitarBajas, "Lista de bajas registradas en el periodo escolar y programa educativo seleccionado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de bajas registradas en el periodo escolar y programa educativo seleccionado. (EjbRegistroBajas.obtenerListaBajasProgramaEducativo)", e, null);
        }
    }
    
    public LocalDate convertirDateALocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    
    /**
     * Permite obtener la información de validaciones de la baja por el área correspondiente (Servicios Escolares o Dirección de Carrera) y Psicopedagogía
     * @param registro Clave de registro de baja
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoValidacionesBaja> buscarValidacionesBaja(Baja registro) {
        try{
            Baja baja = em.createQuery("SELECT b FROM Baja b WHERE b.idBajas =:baja", Baja.class)
                    .setParameter("baja", registro.getIdBajas())
                    .getSingleResult();
            
            String areaValidacion ="", validacionBaja = "", validacionPsic ="", fechaVal ="", fechaValPsic ="";
            
            Personal personal = em.find(Personal.class, baja.getEmpleadoRegistro());
            
              
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            
            if(personal.getAreaOperativa()== 10)
            {
                areaValidacion ="Servicios Escolares";
            }else{
                areaValidacion ="Dirección de Carrera";
            }
            
             if(baja.getValidada() == 0)
            {
                validacionBaja ="Pendiente";
                fechaVal = "Pendiente";
            }else{
                validacionBaja ="Hecha";
                LocalDate fecVal = convertirDateALocalDate(baja.getFechaValidacion());
                fechaVal = fecVal.format(formatter);
            }
             
              if(baja.getValidoPsicopedagogia()== 0)
            {
                validacionPsic ="Pendiente";
                fechaValPsic = "Pendiente";
            }else{
                validacionPsic ="Hecha";
                LocalDate fecValPsic = convertirDateALocalDate(baja.getFechaValpsicopedagogia());
                fechaValPsic = fecValPsic.format(formatter);
            }
          
            DtoValidacionesBaja dtoValidacionesBaja = new  DtoValidacionesBaja(areaValidacion, fechaVal, validacionBaja, fechaValPsic, validacionPsic);
            
            return ResultadoEJB.crearCorrecto(dtoValidacionesBaja, "Status de la baja.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el status de la baja. (EjbRegistroBajas.buscarValidacionBaja)", e, null);
        }
    }
   
      /**
     * Permite eliminar el registro de baja
     * @param registro Registro de baja
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> validarBaja(Baja registro){
        try{
            String mensaje;
            Integer validar;

            if (registro.getValidada()== 0) {
                validar = em.createQuery("update Baja b set b.validada =:valor, b.fechaValidacion =:fecha where b.idBajas =:baja").setParameter("valor", (int)1).setParameter("baja", registro.getIdBajas()).setParameter("fecha", new Date())
                        .executeUpdate();
                
                Integer actualizarStatus = cambiarStatusEstudiante(registro).getValor();
                mensaje ="La baja se ha validado correctamente";
            } else {
                validar = em.createQuery("update Baja b set b.validada =:valor, b.fechaValidacion =:fecha where b.idBajas =:baja").setParameter("valor", (int)0).setParameter("baja", registro.getIdBajas()).setParameter("fecha", null)
                        .executeUpdate();
                mensaje ="La baja se ha invalidado correctamente";
                Integer actualizarStatus = resetearStatusEstudiante(registro).getValor();
            }
            
                       
            return ResultadoEJB.crearCorrecto(validar, mensaje);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el registro de la baja. (EjbRegistroBajas.eliminarRegistroBaja)", e, null);
        }
    }
    
    public ResultadoEJB<Integer> cambiarStatusEstudiante(Baja registro){
       try{
           Integer delete;
                   
           if (registro.getTipoBaja() == 1) {

               TipoEstudiante tipoEstudiante = em.find(TipoEstudiante.class, (short) 2);

               delete = em.createQuery("update Estudiante e set e.tipoEstudiante =:tipoEstudiante where e.idEstudiante =:estudiante")
                       .setParameter("tipoEstudiante", tipoEstudiante)
                       .setParameter("estudiante", registro.getEstudiante().getIdEstudiante())
                       .executeUpdate();
           } else {

               TipoEstudiante tipoEstudiante = em.find(TipoEstudiante.class, (short) 3);

               delete = em.createQuery("update Estudiante e set e.tipoEstudiante =:tipoEstudiante where e.idEstudiante =:estudiante")
                       .setParameter("tipoEstudiante", tipoEstudiante)
                       .setParameter("estudiante", registro.getEstudiante().getIdEstudiante())
                       .executeUpdate();
           }
                       
            return ResultadoEJB.crearCorrecto(delete, "Se ha cambiado la situación académica del estudiante (Baja)");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar la situación académica del estudiante. (EjbRegistroBajas.cambiarStatusEstudiante)", e, null);
        }
    
    }
    
    public ResultadoEJB<Integer> resetearStatusEstudiante(Baja registro){
       try{
          
            TipoEstudiante tipoEstudiante = em.find(TipoEstudiante.class, (short) 1);

            Integer update = em.createQuery("update Estudiante e set e.tipoEstudiante =:tipoEstudiante where e.idEstudiante =:estudiante")
                       .setParameter("tipoEstudiante", tipoEstudiante)
                       .setParameter("estudiante", registro.getEstudiante().getIdEstudiante())
                       .executeUpdate();
          
            return ResultadoEJB.crearCorrecto(update, "Se ha cambiado la situación académica del estudiante (Regular)");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar la situación académica del estudiante. (EjbRegistroBajas.resetearStatusEstudiante)", e, null);
        }
    
    }
}
