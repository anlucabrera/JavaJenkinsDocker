/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEliminarRegistrosEstIns;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbEliminarInscripcion")
public class EjbEliminarInscripcion {
    @EJB Facade f;
    private EntityManager em;
   
    @EJB EjbRegistroBajas ejbRegistroBajas;
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Permite identificar a una lista de posibles estudiante del periodo actual posibles para eliminar inscripción
     * @param pista Contenido que la vista que puede incluir parte del nombre, apellidos o matricula del estudiante
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarEstudiantePeriodoActual(String pista){
        try{
            PeriodosEscolares periodoActual = ejbRegistroBajas.getPeriodoActual();
            
             //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            List<Estudiante> estudiantes = em.createQuery("select e from Estudiante e INNER JOIN e.aspirante a INNER JOIN a.idPersona p WHERE concat(p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.matricula) like concat('%',:pista,'%') AND e.periodo=:periodo ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombre, e.periodo DESC", Estudiante.class)
                    .setParameter("pista", pista)
                    .setParameter("periodo", periodoActual.getPeriodo())
                    .getResultList();
            
            List<DtoEstudianteComplete> listaDtoEstudiantes = new ArrayList<>();
            
            estudiantes.forEach(estudiante -> {
                String datosComplete = estudiante.getAspirante().getIdPersona().getApellidoPaterno()+" "+ estudiante.getAspirante().getIdPersona().getApellidoMaterno()+" "+ estudiante.getAspirante().getIdPersona().getNombre()+ " - " + estudiante.getMatricula();
                PeriodosEscolares periodo = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
                String periodoEscolar = periodo.getMesInicio().getAbreviacion()+" - "+periodo.getMesFin().getAbreviacion()+" "+periodo.getAnio();
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getCarrera());
                DtoEstudianteComplete dtoEstudianteComplete = new DtoEstudianteComplete(estudiante, datosComplete, periodoEscolar, programaEducativo);
                listaDtoEstudiantes.add(dtoEstudianteComplete);
            });
            
            return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Lista de estudiantes del periodo actual para mostrar en autocomplete");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de estudiantes activos del periodo actual. (EjbEliminarInscripcion.buscarEstudiantePeriodoActual)", e, null);
        }
    }
    
    /**
     * Permite buscar si existen registros de documentos entregados en la inscripción a ingeniería o licenciatura del estudiante seleccionado
     * @param estudiante Estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoEliminarRegistrosEstIns>> buscarRegistrosEstudiante(DtoDatosEstudiante estudiante){
        try{
            
            List<DtoEliminarRegistrosEstIns> listaRegistrosEstudiante = new ArrayList<>();
            
            Baja bajaEstudiante = buscarBajaRegistrada(estudiante.getEstudiante()).getValor();
            if(bajaEstudiante != null){
                DtoEliminarRegistrosEstIns dtoBaja = new DtoEliminarRegistrosEstIns("Baja", (int)1);
                listaRegistrosEstudiante.add(dtoBaja);
            }
            
            List<CasoCritico> casosCriticos = buscarCasosCriticosRegistrados(estudiante.getEstudiante()).getValor();
            if(!casosCriticos.isEmpty()){
                DtoEliminarRegistrosEstIns dtoCasosCriticos = new DtoEliminarRegistrosEstIns("Casos Críticos", casosCriticos.size());
                listaRegistrosEstudiante.add(dtoCasosCriticos);
            }
            
            if(estudiante.getProgramaEducativo().getNivelEducativo().getNivel().equals("TSU") && estudiante.getEstudiante().getGrupo().getGrado()== 1){
                Documentosentregadosestudiante docsEntregados = buscarDocumentosTSURegistrados(estudiante.getEstudiante()).getValor();
                if(docsEntregados != null){
                    DtoEliminarRegistrosEstIns dtoDocsTSU = new DtoEliminarRegistrosEstIns("Documentos inscripción T.S.U.", (int)1);
                    listaRegistrosEstudiante.add(dtoDocsTSU);
                }
            }else if(!estudiante.getProgramaEducativo().getNivelEducativo().getNivel().equals("TSU") && estudiante.getEstudiante().getGrupo().getGrado() == 7){
                List<DocumentoEstudianteProceso> docsEntregados = buscarDocumentosIngLicRegistrados(estudiante.getEstudiante()).getValor();
                if(!docsEntregados.isEmpty()){
                    DtoEliminarRegistrosEstIns dtoDocsTSU = new DtoEliminarRegistrosEstIns("Documentos inscripción Ing/Lic.", docsEntregados.size());
                    listaRegistrosEstudiante.add(dtoDocsTSU);
                }
            }
            
            List<Asistenciasacademicas> asistencias = buscarAsistenciasRegistradas(estudiante.getEstudiante()).getValor();
            if(!asistencias.isEmpty()){
               DtoEliminarRegistrosEstIns dtoAsistencias = new DtoEliminarRegistrosEstIns("Asistencias", asistencias.size());
               listaRegistrosEstudiante.add(dtoAsistencias); 
            }
            
            List<CalificacionEvidenciaInstrumento> califEvidencias = buscarCalificacionesEvidenciasRegistradas(estudiante.getEstudiante()).getValor();
            if(!califEvidencias.isEmpty()){
               DtoEliminarRegistrosEstIns dtoCalifEvid = new DtoEliminarRegistrosEstIns("Calificaciones de evidencias", califEvidencias.size());
               listaRegistrosEstudiante.add(dtoCalifEvid); 
            }
            
            List<CalificacionPromedio> califPromedios = buscarCalificacionesPromedioRegistradas(estudiante.getEstudiante()).getValor();
            if(!califPromedios.isEmpty()){
               DtoEliminarRegistrosEstIns dtoCalifProm = new DtoEliminarRegistrosEstIns("Calificaciones promedio", califPromedios.size());
               listaRegistrosEstudiante.add(dtoCalifProm); 
            }
            
            List<TutoriasIndividuales> tutorias = buscarTutoriasRegistradas(estudiante.getEstudiante()).getValor();
            if(!tutorias.isEmpty()){
               DtoEliminarRegistrosEstIns dtoTutorias = new DtoEliminarRegistrosEstIns("Tutorías individuales", tutorias.size());
               listaRegistrosEstudiante.add(dtoTutorias); 
            }
            
            List<ParticipantesTutoriaGrupal> tutoriasGrupales = buscarTutoriasGrupalesRegistradas(estudiante.getEstudiante()).getValor();
            if(!tutoriasGrupales.isEmpty()){
               DtoEliminarRegistrosEstIns dtoTutoriasGrup = new DtoEliminarRegistrosEstIns("Tutorías grupales", tutoriasGrupales.size());
               listaRegistrosEstudiante.add(dtoTutoriasGrup); 
            }
            
            List<AsesoriasEstudiantes> asesorias = buscarAsesoriasRegistradas(estudiante.getEstudiante()).getValor();
            if(!asesorias.isEmpty()){
               DtoEliminarRegistrosEstIns dtoAsesorias = new DtoEliminarRegistrosEstIns("Asesorías", asesorias.size());
               listaRegistrosEstudiante.add(dtoAsesorias); 
            }
            

            return ResultadoEJB.crearCorrecto(listaRegistrosEstudiante, "Tipos de registro del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo buscar tipos de registros del estudiante seleccionado. (EjbEliminarInscripcion.buscarRegistrosEstudiante)", e, null);
        }
    }
    
      /**
     * Permite eliminar la inscripción del estudiante seleccionado
     * @param estudiante Estudiante
     * @param registrosEliminar
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> eliminarInscripcion(Estudiante estudiante, List<DtoEliminarRegistrosEstIns> registrosEliminar){
        try{
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, "El estudiante no puede ser nulo.", Boolean.TYPE);
            
            Boolean valor = false;
            
            List<String> mensajes = new ArrayList<>();
            
            if(!registrosEliminar.isEmpty()){
            
                registrosEliminar.forEach(registro -> {
                    switch (registro.getTipoRegistro()) {
                        case "Baja":
                            ResultadoEJB<Integer> eliminarBaja = eliminarRegistroBaja(estudiante);
                            mensajes.add(eliminarBaja.getMensaje());
                            break;
                        
                        case "Casos Críticos":
                            ResultadoEJB<Integer> eliminarCasosCriticos = eliminarRegistrosCasosCriticos(estudiante);
                            mensajes.add(eliminarCasosCriticos.getMensaje());
                            break;
                            
                        case "Documentos inscripción T.S.U.":
                            ResultadoEJB<Integer> eliminarDocsTSU = eliminarRegistroDocumentosTSU(estudiante);
                            mensajes.add(eliminarDocsTSU.getMensaje());
                            break;

                        case "Documentos inscripción Ing/Lic.":
                            ResultadoEJB<Integer> eliminarDocsIL = eliminarRegistroDocumentosIngLic(estudiante);
                            mensajes.add(eliminarDocsIL.getMensaje());
                            break;
                        
                        case "Asistencias":
                            ResultadoEJB<Integer> eliminarAsistencias = eliminarRegistroAsistencias(estudiante);
                            mensajes.add(eliminarAsistencias.getMensaje());
                            break;

                        case "Calificaciones de evidencias":
                            ResultadoEJB<Integer> eliminarCalifEvid = eliminarRegistroCalificacionesEvidencia(estudiante);
                            mensajes.add(eliminarCalifEvid.getMensaje());
                            break;
                            
                        case "Calificaciones promedio":
                            ResultadoEJB<Integer> eliminarCalifProm = eliminarRegistroCalificacionesPromedio(estudiante);
                            mensajes.add(eliminarCalifProm.getMensaje());
                            break;
                            
                        case "Tutorías individuales":
                            ResultadoEJB<Integer> eliminarTutInd = eliminarRegistroTutoriasIndividuales(estudiante);
                            mensajes.add(eliminarTutInd.getMensaje());
                            break;
                            
                        case "Tutorías grupales":
                            ResultadoEJB<Integer> eliminarTutGrup = eliminarRegistroTutoriasGrupales(estudiante);
                            mensajes.add(eliminarTutGrup.getMensaje());
                            break;
                         
                        default:
                            break;
                    }
                });
            }

            Integer delete = em.createQuery("DELETE FROM Estudiante e WHERE e.idEstudiante =:clave", Estudiante.class)
                .setParameter("clave", estudiante.getIdEstudiante())
                .executeUpdate();
           
           if(delete == 1)
           {
               valor = true;
           }

            return ResultadoEJB.crearCorrecto(valor, mensajes.toString());
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el registro de la baja. (EjbEliminarInscripcion.eliminarRegistroBaja)", e, null);
        }
    }
    
     /**
     * Permite buscar si existen registros de documentos entregados en la inscripción a tsu del estudiante seleccionado
     * @param estudiante Estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Documentosentregadosestudiante> buscarDocumentosTSURegistrados(Estudiante estudiante){
        try{
            Documentosentregadosestudiante documentosTSU = em.createQuery("SELECT d FROM Documentosentregadosestudiante d WHERE d.estudiante1.idEstudiante=:clave", Documentosentregadosestudiante.class)
                    .setParameter("clave", estudiante.getIdEstudiante())
                    .getResultStream().findFirst().orElse(null);

            return ResultadoEJB.crearCorrecto(documentosTSU, "Registros de documentos entregados en la inscripción a tsu del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo buscar registros de documentos entregados en la inscripción a tsu del estudiante seleccionado. (EjbEliminarInscripcion.buscarDocumentosTSURegistrados)", e, null);
        }
    }
    
    /**
     * Permite buscar si existen registros de documentos entregados en la inscripción a ingeniería o licenciatura del estudiante seleccionado
     * @param estudiante Estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DocumentoEstudianteProceso>> buscarDocumentosIngLicRegistrados(Estudiante estudiante){
        try{
            List<DocumentoEstudianteProceso> documentosIngLic = em.createQuery("SELECT d FROM DocumentoEstudianteProceso d WHERE d.estudiante.idEstudiante=:clave", DocumentoEstudianteProceso.class)
                    .setParameter("clave", estudiante.getIdEstudiante())
                    .getResultStream().collect(Collectors.toList());

            return ResultadoEJB.crearCorrecto(documentosIngLic, "Registros de documentos entregados en la inscripción a ingeniería o licenciatura del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo buscar registros de documentos entregados en la inscripción a ingeniería o licenciatura del estudiante seleccionado. (EjbEliminarInscripcion.buscarCalificacionesPromedioRegistradas)", e, null);
        }
    }
    
      /**
     * Permite buscar si existe registro de baja del estudiante seleccionado
     * @param estudiante Estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Baja> buscarBajaRegistrada(Estudiante estudiante){
        try{
           Baja baja = em.createQuery("SELECT b FROM Baja b WHERE b.estudiante.idEstudiante=:clave", Baja.class)
                    .setParameter("clave", estudiante.getIdEstudiante())
                    .getResultStream().findFirst().orElse(null);

            return ResultadoEJB.crearCorrecto(baja, "Registro de baja del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo buscar registro de baja del estudiante seleccionado. (EjbEliminarInscripcion.buscarBajaRegistrada)", e, null);
        }
    }
    
      /**
     * Permite buscar si existe registros de casos críticos del estudiante seleccionado
     * @param estudiante Estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<CasoCritico>> buscarCasosCriticosRegistrados(Estudiante estudiante){
        try{
           List<CasoCritico> casosCriticos = em.createQuery("SELECT c FROM CasoCritico c WHERE c.idEstudiante.idEstudiante=:clave", CasoCritico.class)
                    .setParameter("clave", estudiante.getIdEstudiante())
                    .getResultStream().collect(Collectors.toList());

            return ResultadoEJB.crearCorrecto(casosCriticos, "Registros de casos críticos del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo buscar registros de casos críticos del estudiante seleccionado. (EjbEliminarInscripcion.buscarCasosCriticosRegistrados)", e, null);
        }
    }
    
     /**
     * Permite buscar si existen registros de asistencias del estudiante seleccionado
     * @param estudiante Estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Asistenciasacademicas>> buscarAsistenciasRegistradas(Estudiante estudiante){
        try{
            List<Asistenciasacademicas> asistencias = em.createQuery("SELECT a FROM Asistenciasacademicas a WHERE a.estudiante.idEstudiante=:clave", Asistenciasacademicas.class)
                    .setParameter("clave", estudiante.getIdEstudiante())
                    .getResultStream().collect(Collectors.toList());

            return ResultadoEJB.crearCorrecto(asistencias, "Registros de asistencias del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo buscar registros de asistencias del estudiante seleccionado. (EjbEliminarInscripcion.buscarAsistenciasRegistradas)", e, null);
        }
    }
    
     /**
     * Permite buscar si existen registros de calificaciones por evidencias del estudiante seleccionado
     * @param estudiante Estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<CalificacionEvidenciaInstrumento>> buscarCalificacionesEvidenciasRegistradas(Estudiante estudiante){
        try{
            List<CalificacionEvidenciaInstrumento> califEvidencias = em.createQuery("SELECT c FROM CalificacionEvidenciaInstrumento c WHERE c.idEstudiante.idEstudiante=:clave", CalificacionEvidenciaInstrumento.class)
                    .setParameter("clave", estudiante.getIdEstudiante())
                    .getResultStream().collect(Collectors.toList());

            return ResultadoEJB.crearCorrecto(califEvidencias, "Registros de calificaciones por evidencias del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo buscar registros de calificaciones por evidencias del estudiante seleccionado. (EjbEliminarInscripcion.buscarCalificacionesEvidenciasRegistradas)", e, null);
        }
    }
    
     /**
     * Permite buscar si existen registros de calificaciones promedio del estudiante seleccionado
     * @param estudiante Estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<CalificacionPromedio>> buscarCalificacionesPromedioRegistradas(Estudiante estudiante){
        try{
            List<CalificacionPromedio> califPromedio = em.createQuery("SELECT c FROM CalificacionPromedio c WHERE c.estudiante.idEstudiante=:clave", CalificacionPromedio.class)
                    .setParameter("clave", estudiante.getIdEstudiante())
                    .getResultStream().collect(Collectors.toList());

            return ResultadoEJB.crearCorrecto(califPromedio, "Registros de calificaciones promedio del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo buscar registros de calificaciones promedio del estudiante seleccionado. (EjbEliminarInscripcion.buscarCalificacionesPromedioRegistradas)", e, null);
        }
    }
    
     /**
     * Permite buscar si existen registros de tutorias del estudiante seleccionado
     * @param estudiante Estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<TutoriasIndividuales>> buscarTutoriasRegistradas(Estudiante estudiante){
        try{
            List<TutoriasIndividuales> tutorias = em.createQuery("SELECT t FROM TutoriasIndividuales t WHERE t.estudiante.idEstudiante=:clave", TutoriasIndividuales.class)
                    .setParameter("clave", estudiante.getIdEstudiante())
                    .getResultStream().collect(Collectors.toList());

            return ResultadoEJB.crearCorrecto(tutorias, "Registros de tutorias del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo buscar registros de tutorias del estudiante seleccionado. (EjbEliminarInscripcion.buscarTutoriasRegistradas)", e, null);
        }
    }
    
     /**
     * Permite buscar si existen registros de tutorias del estudiante seleccionado
     * @param estudiante Estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<ParticipantesTutoriaGrupal>> buscarTutoriasGrupalesRegistradas(Estudiante estudiante){
        try{
            List<ParticipantesTutoriaGrupal> tutoriasGrupales = em.createQuery("SELECT p FROM ParticipantesTutoriaGrupal p WHERE p.estudiante1.idEstudiante=:clave", ParticipantesTutoriaGrupal.class)
                    .setParameter("clave", estudiante.getIdEstudiante())
                    .getResultStream().collect(Collectors.toList());

            return ResultadoEJB.crearCorrecto(tutoriasGrupales, "Registros de tutorias grupales del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo buscar registros de tutorias grupales del estudiante seleccionado. (EjbEliminarInscripcion.buscarTutoriasGrupalesRegistradas)", e, null);
        }
    }
    
     /**
     * Permite buscar si existen registros de asesorías del estudiante seleccionado
     * @param estudiante Estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AsesoriasEstudiantes>> buscarAsesoriasRegistradas(Estudiante estudiante){
        try{
            List<AsesoriasEstudiantes> asesorias = em.createQuery("SELECT a FROM AsesoriasEstudiantes a INNER JOIN a.estudianteList l WHERE l.idEstudiante =:clave", AsesoriasEstudiantes.class)
                    .setParameter("clave", estudiante.getIdEstudiante())
                    .getResultStream().collect(Collectors.toList());

            return ResultadoEJB.crearCorrecto(asesorias, "Registros de asesorías del estudiante seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo buscar registros de asesorías del estudiante seleccionado. (EjbEliminarInscripcion.buscarAsesoriasRegistradas)", e, null);
        }
    }
    
     /**
     * Permite eliminar el registro de baja
     * @param estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarRegistroBaja(Estudiante estudiante){
        try{
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, "El estudiante no puede ser nulo.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM Baja b WHERE b.estudiante.idEstudiante =:clave", Baja.class)
                .setParameter("clave", estudiante.getIdEstudiante())
                .executeUpdate();
           
            return ResultadoEJB.crearCorrecto(delete, "El registro de la baja se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el registro de la baja. (EjbEliminarInscripcion.eliminarRegistroBaja)", e, null);
        }
    }
    
     /**
     * Permite eliminar los registros de casos críticos
     * @param estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarRegistrosCasosCriticos(Estudiante estudiante){
        try{
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, "El estudiante no puede ser nulo.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM CasoCritico c WHERE c.idEstudiante.idEstudiante =:clave", CasoCritico.class)
                .setParameter("clave", estudiante.getIdEstudiante())
                .executeUpdate();
           
            return ResultadoEJB.crearCorrecto(delete, "Los registros de casos críticos se han eliminado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudieron eliminar los registros de casos críticos. (EjbEliminarInscripcion.eliminarRegistrosCasosCriticos)", e, null);
        }
    }
    
     /**
     * Permite eliminar el registro de documentos entregados en la inscripción a tsu del estudiante seleccionado
     * @param estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarRegistroDocumentosTSU(Estudiante estudiante){
        try{
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, "El estudiante no puede ser nulo.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM Documentosentregadosestudiante d WHERE d.estudiante1.idEstudiante =:clave", Documentosentregadosestudiante.class)
                .setParameter("clave", estudiante.getIdEstudiante())
                .executeUpdate();
           
            return ResultadoEJB.crearCorrecto(delete, "El registro de documentos entregados en la inscripción a tsu se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el registro de documentos entregados en la inscripción a tsu. (EjbEliminarInscripcion.eliminarRegistroDocumentosTSU)", e, null);
        }
    }
    
     /**
     * Permite eliminar el registro de documentos entregados en la inscripción a ing o lic del estudiante seleccionado
     * @param estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarRegistroDocumentosIngLic(Estudiante estudiante){
        try{
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, "El estudiante no puede ser nulo.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM DocumentoEstudianteProceso d WHERE d.estudiante.idEstudiante =:clave", DocumentoEstudianteProceso.class)
                .setParameter("clave", estudiante.getIdEstudiante())
                .executeUpdate();
           
            return ResultadoEJB.crearCorrecto(delete, "El registro de documentos entregados en la inscripción a ing o lic se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el registro de documentos entregados en la inscripción a ing o lic. (EjbEliminarInscripcion.eliminarRegistroDocumentosIngLic)", e, null);
        }
    }
    
    /**
     * Permite eliminar el registro de documentos entregados en la inscripción a ing o lic del estudiante seleccionado
     * @param estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarRegistroAsistencias(Estudiante estudiante){
        try{
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, "El estudiante no puede ser nulo.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM Asistenciasacademicas a WHERE a.estudiante.idEstudiante =:clave",  Asistenciasacademicas.class)
                .setParameter("clave", estudiante.getIdEstudiante())
                .executeUpdate();
           
            return ResultadoEJB.crearCorrecto(delete, "El registro de asistencias se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el registro de asistencias. (EjbEliminarInscripcion.eliminarRegistroAsistencias)", e, null);
        }
    }
    
    /**
     * Permite eliminar el registro de calificaciones por evidencia del estudiante seleccionado
     * @param estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarRegistroCalificacionesEvidencia(Estudiante estudiante){
        try{
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, "El estudiante no puede ser nulo.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM CalificacionEvidenciaInstrumento c WHERE c.idEstudiante.idEstudiante =:clave",  CalificacionEvidenciaInstrumento.class)
                .setParameter("clave", estudiante.getIdEstudiante())
                .executeUpdate();
           
            return ResultadoEJB.crearCorrecto(delete, "El registro de calificaciones por evidencia se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el registro de calificaciones por evidencia. (EjbEliminarInscripcion.eliminarRegistroCalificacionesEvidencia)", e, null);
        }
    }
    
    /**
     * Permite eliminar el registro de calificaciones promedio del estudiante seleccionado
     * @param estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarRegistroCalificacionesPromedio(Estudiante estudiante){
        try{
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, "El estudiante no puede ser nulo.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM CalificacionPromedio c WHERE c.estudiante.idEstudiante =:clave",  CalificacionPromedio.class)
                .setParameter("clave", estudiante.getIdEstudiante())
                .executeUpdate();
           
            return ResultadoEJB.crearCorrecto(delete, "El registro de calificaciones promedio se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el registro de calificaciones promedio. (EjbEliminarInscripcion.eliminarRegistroCalificacionesPromedio)", e, null);
        }
    }
    
    /**
     * Permite eliminar el registro de tutorías individuales del estudiante seleccionado
     * @param estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarRegistroTutoriasIndividuales(Estudiante estudiante){
        try{
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, "El estudiante no puede ser nulo.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM TutoriasIndividuales t WHERE t.estudiante.idEstudiante =:clave",  TutoriasIndividuales.class)
                .setParameter("clave", estudiante.getIdEstudiante())
                .executeUpdate();
           
            return ResultadoEJB.crearCorrecto(delete, "El registro de tutorías individuales se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el registro de tutorías individuales. (EjbEliminarInscripcion.eliminarRegistroTutoriasIndividuales)", e, null);
        }
    }
    
     /**
     * Permite eliminar el registro de tutorías grupales del estudiante seleccionado
     * @param estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarRegistroTutoriasGrupales(Estudiante estudiante){
        try{
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, "El estudiante no puede ser nulo.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM ParticipantesTutoriaGrupal p WHERE p.estudiante1.idEstudiante =:clave",  ParticipantesTutoriaGrupal.class)
                .setParameter("clave", estudiante.getIdEstudiante())
                .executeUpdate();
           
            return ResultadoEJB.crearCorrecto(delete, "El registro de tutorías grupales se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el registro de tutorías grupales. (EjbEliminarInscripcion.eliminarRegistroTutoriasGrupales)", e, null);
        }
    }
    
   
    
}
