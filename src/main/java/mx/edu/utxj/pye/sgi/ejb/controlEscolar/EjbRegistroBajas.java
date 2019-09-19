/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
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
import javax.persistence.StoredProcedureQuery;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaReprobada;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoFormatoBaja;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRangoFechasPermiso;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRegistroBajaEstudiante;
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
           
            System.err.println("buscarDatosEstudiante - status " + dtoDatosEstudiante.getEstudiante().getTipoEstudiante().getDescripcion());
            
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
            registroBaja.setEmpleado(personal.getPersonal().getClave());
            registroBaja.setFechaBaja(fechaBaja);
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
                Personal personal = em.find(Personal.class, registroBaja.getEmpleado());
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
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias reprobdas. (EjbRegistroBajas.buscarMateriasReprobadas)", e, null);
        }
    }
}
