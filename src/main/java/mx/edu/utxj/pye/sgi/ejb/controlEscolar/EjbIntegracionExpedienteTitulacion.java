/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPagosEstudianteFinanzas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Domicilio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FechaTerminacionTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Viewregalumnosnoadeudo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.AsentamientoPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbIntegracionExpedienteTitulacion")
public class EjbIntegracionExpedienteTitulacion {
    @EJB EjbEstudianteBean ejbEstudianteBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    private EntityManager em;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
     /**
     * Permite obtener el periodo actual
     * @return Resultado del proceso
     */
    public PeriodosEscolares getPeriodoActual() {

        PeriodoEscolarFechas periodoFechas = em.createQuery("SELECT p FROM PeriodoEscolarFechas p WHERE current_timestamp between p.inicio and p.fin", PeriodoEscolarFechas.class)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        
        PeriodosEscolares periodo = new PeriodosEscolares();
        
        if(periodoFechas != null)
        {
            periodo = em.find(PeriodosEscolares.class, periodoFechas.getPeriodo());
        }
        return periodo;
    }
    
    /**
     * Permite validar usuario de estudiante con situación regular o egresado
     * @param matricula
     * @return Resultado del proceso
     */
    public ResultadoEJB<Estudiante> validaEstudiante(Integer matricula){
        try{
            List<Short> listaTipos = new ArrayList<>();
            listaTipos.add((short)1);
            listaTipos.add((short)4);
            
            
            Estudiante e = em.createQuery("SELECT e FROM Estudiante as e WHERE e.matricula = :matricula ORDER BY e.periodo DESC", Estudiante.class)
                    .setParameter("matricula", matricula)
                    .getResultStream().findFirst().orElse(null);
            
                Estudiante estudianteTSU = new Estudiante();
             
                if(e.getGrupo().getGrado()>6){
            
                    estudianteTSU = em.createQuery("SELECT e FROM Estudiante as e WHERE e.matricula = :matricula AND e.tipoEstudiante.idTipoEstudiante IN :listaTipos AND e.grupo.grado=:grado", Estudiante.class)
                        .setParameter("matricula", e.getMatricula())
                        .setParameter("listaTipos", listaTipos)
                        .setParameter("grado", 6)
                        .getResultStream().findFirst().orElse(null);
                    
                    if(estudianteTSU != null){
                        ExpedienteTitulacion expedienteTitulacionTSU = buscarExpedienteRegistrado(estudianteTSU).getValor();
                        if(expedienteTitulacionTSU != null){
                            if(!expedienteTitulacionTSU.getPasoRegistro().equals("Fin Integración")){
                                e = estudianteTSU;
                            }
                        }else{
                            e = estudianteTSU;
                        }
                    }
                }
                
            return ResultadoEJB.crearCorrecto(e, "El usuario ha sido comprobado como estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El estudiante no se pudo validar. (EjbIntegracionExpedienteTitulacion.validadEstudiante)", e, null);
        }
    }
    
    /**
     * Permite verificar si hay un periodo abierto para integración de expediente de titulación
     * @param estudiante Estudiante que va a integrar expediente
     * @return Evento escolar detectado o null de lo contrario
     */
    public ResultadoEJB<EventoTitulacion> verificarEvento(Estudiante estudiante){
         try{
            EventoTitulacion evento = null;
            
            if(estudiante.getTipoEstudiante().getIdTipoEstudiante()==1 || estudiante.getTipoEstudiante().getIdTipoEstudiante()==4){
                AreasUniversidad programa = em.find(AreasUniversidad.class, estudiante.getCarrera());
                //verificar apertura del evento
                evento = em.createQuery("select e from EventoTitulacion e where e.generacion =:generacion and e.nivel =:nivel and current_timestamp between e.fechaInicio and e.fechaFin", EventoTitulacion.class)
                        .setParameter("generacion", estudiante.getGrupo().getGeneracion())
                        .setParameter("nivel", programa.getNivelEducativo().getNivel())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
            }
           
            if(evento == null){
                return ResultadoEJB.crearErroneo(2,evento, "No existe evento aperturado actualmente.");// .crearCorrecto(map.entrySet().iterator().next(), "Evento aperturado.");
            }else{
                return ResultadoEJB.crearCorrecto(evento, "Evento aperturado.");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar la apertura de evento (EjbIntegracionExpedienteTitulacion.verificarEvento).", e, EventoTitulacion.class);
        }
         
         
    }
    
    /**
     * Permite consultar si existe expediente registrado por el estudiante
     * @param estudiante 
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> buscarExpedienteRegistrado (Estudiante estudiante){
         try{
            AreasUniversidad programa = em.find(AreasUniversidad.class, estudiante.getGrupo().getIdPe());
            //verificar apertura del evento
           ExpedienteTitulacion expediente = em.createQuery("SELECT e FROM ExpedienteTitulacion e WHERE e.estudiante.matricula =:matricula AND e.evento.generacion =:generacion AND e.evento.nivel =:nivel AND e.activo =:valor", ExpedienteTitulacion.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("generacion", estudiante.getGrupo().getGeneracion())
                    .setParameter("nivel", programa.getNivelEducativo().getNivel())
                    .setParameter("valor", Boolean.TRUE)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            return ResultadoEJB.crearCorrecto(expediente, "Existe expediente registrado.");
            
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar la existencia del expediente de titulación (EjbIntegracionExpedienteTitulacion.buscarExpedienteRegistrado).", e, ExpedienteTitulacion.class);
        }
    }
    
    
     /**
     * Permite obtener la generación de la integración del expediente de titulación
     * @param generacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Generaciones> getGeneracionEventoActivo(Short generacion) {
       try{
            Generaciones generacionBD = em.find(Generaciones.class, generacion);
            return ResultadoEJB.crearCorrecto(generacionBD, "Generación del evento activo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar la generación del evento activo (EjbIntegracionExpedienteTitulacion.getGeneracionExpediente).", e, Generaciones.class);
        }
    }
    
    /**
     * Permite obtener el nivel educativo de la integración del expediente de titulación
     * @param nivel
     * @return Resultado del proceso
     */
    public ResultadoEJB<ProgramasEducativosNiveles> getNivelEventoActivo(String nivel) {
       try{
            ProgramasEducativosNiveles nivelEducativoBD = em.find(ProgramasEducativosNiveles.class, nivel);
            return ResultadoEJB.crearCorrecto(nivelEducativoBD, "Nivel educativo del evento activo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el nivel educativo del evento activo (EjbIntegracionExpedienteTitulacion.getNivelEventoActivo).", e, ProgramasEducativosNiveles.class);
        }
    }
    
    
    /**
     * Permite buscar expediente por su clave de registro
     * @param expediente 
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> buscarExpedienteRegistradoClave (Integer expediente){
         try{
            ExpedienteTitulacion expedienteTitulacion = em.find(ExpedienteTitulacion.class, expediente);
            
            if(expedienteTitulacion == null){
                return ResultadoEJB.crearErroneo(2,expedienteTitulacion, "No se ha registrado expediente de titulación.");// .crearCorrecto(map.entrySet().iterator().next(), "Evento aperturado.");
            }else{
                return ResultadoEJB.crearCorrecto(expedienteTitulacion, "Existe expediente registrado.");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar la existencia del expediente de titulación (EjbIntegracionExpedienteTitulacion.buscarExpedienteRegistradoClave).", e, ExpedienteTitulacion.class);
        }
    }
    
     /**
     * Permite guardar expediente de titulación
     * @param evento
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> guardarExpedienteTitulacion(EventoTitulacion evento, Estudiante estudiante){
        try{
            ExpedienteTitulacion expedienteTitulacion = new ExpedienteTitulacion();
            expedienteTitulacion.setEvento(evento);
            expedienteTitulacion.setFechaRegistro(new Date());
            expedienteTitulacion.setEstudiante(estudiante);
            expedienteTitulacion.setValidado(false);
            expedienteTitulacion.setFechaValidacion(null);
            expedienteTitulacion.setPersonalValido(null);
            expedienteTitulacion.setPasoRegistro("Inicio Integración");
            expedienteTitulacion.setFechaPaso(new Date());
            expedienteTitulacion.setPromedio(0);
            expedienteTitulacion.setServicioSocial(false);
            expedienteTitulacion.setActivo(true);
            em.persist(expedienteTitulacion);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(expedienteTitulacion, "El expediente de titulación se ha registrado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el expediente de titulación. (EjbIntegracionExpedienteTitulacion.guardarExpedienteTitulacion)", e, null);
        }
    }
    
     /**
     * Empaqueta DTO del expediente
     * @param expediente
     * @return DTO 
     */
    public ResultadoEJB<DtoExpedienteTitulacion> getDtoExpedienteTitulacion(ExpedienteTitulacion expediente){
        try{
            if(expediente == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un expediente de titulación nulo.", DtoExpedienteTitulacion.class);
            if(expediente.getExpediente()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un expediente de titulación con clave nula.", DtoExpedienteTitulacion.class);

            ExpedienteTitulacion expedienteBD = em.find(ExpedienteTitulacion.class, expediente.getExpediente());
            if(expedienteBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar un expediente de titulación no registrado previamente en base de datos.", DtoExpedienteTitulacion.class);
            
            
            Estudiante ultRegEstudiante = em.createQuery("SELECT e FROM Estudiante e where e.matricula=:matricula AND e.grupo.generacion=:generacion ORDER BY e.periodo DESC", Estudiante.class)
                    .setParameter("matricula", expedienteBD.getEstudiante().getMatricula())
                    .setParameter("generacion", expedienteBD.getEvento().getGeneracion())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            String personal = "Sin información";
            
            if(expedienteBD.getPersonalValido()!= null){   
                Personal personalBD = em.find(Personal.class, expedienteBD.getPersonalValido());
                personal = personalBD.getNombre();
            }
           
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            String fechaIntExp = sm.format(expedienteBD.getFechaRegistro());
            String fechaNacimiento = sm.format(expedienteBD.getEstudiante().getAspirante().getIdPersona().getFechaNacimiento());
            
            AreasUniversidad progEdu = em.find(AreasUniversidad.class, ultRegEstudiante.getGrupo().getIdPe());
            
            List<DtoPagosEstudianteFinanzas> listaPagos = getListaDtoPagosEstudianteFinanzas(expedienteBD.getEstudiante().getMatricula()).getValor();
            
            DtoPagosEstudianteFinanzas dtoPagoTit = new DtoPagosEstudianteFinanzas();
            
            String nivelEdu = "";
            switch (progEdu.getNivelEducativo().getNivel()) {
                case "TSU":
                    nivelEdu = "Técnico Superior Universitario";
                    dtoPagoTit = getDtoPagoTitulacionTSU(listaPagos).getValor();
                    break;
                case "5A":
                    nivelEdu = "Ingeniería";
                    dtoPagoTit = getDtoPagoTitulacionING(listaPagos).getValor();
                    break;
                case "5B":
                    nivelEdu = "Licenciatura";
                    dtoPagoTit = getDtoPagoTitulacionING(listaPagos).getValor();
                    break;
                default:
                    break;
            }
            Generaciones generacion = em.find(Generaciones.class, expedienteBD.getEvento().getGeneracion());
            Generos genero = em.find(Generos.class, expedienteBD.getEstudiante().getAspirante().getIdPersona().getGenero());
            Domicilio domicilio = em.find(Domicilio.class, expedienteBD.getEstudiante().getAspirante().getIdAspirante());
            Estado estadoDom = em.find(Estado.class, domicilio.getIdEstado());
            MunicipioPK mpk = new MunicipioPK(estadoDom.getIdestado(), domicilio.getIdMunicipio());
            Municipio munDom = em.find(Municipio.class, mpk);
            AsentamientoPK apk = new AsentamientoPK(estadoDom.getIdestado(), munDom.getMunicipioPK().getClaveMunicipio(), domicilio.getIdAsentamiento());
            Asentamiento asentDom = em.find(Asentamiento.class, apk);
            MedioComunicacion comunicaciones = em.find(MedioComunicacion.class, expedienteBD.getEstudiante().getAspirante().getIdPersona().getIdpersona());
            DatosAcademicos datosAcademicos = em.find(DatosAcademicos.class, expedienteBD.getEstudiante().getAspirante().getIdAspirante());
            Iems iems = em.find(Iems.class, datosAcademicos.getInstitucionAcademica());
            Localidad locIems = em.find(Localidad.class, iems.getLocalidad().getLocalidadPK());
            
            List<DocumentoProceso> numTotal = new ArrayList<>();
            
            if(expedienteBD.getEvento().getNivel().equals("TSU")){
                numTotal = em.createQuery("SELECT d FROM DocumentoProceso d WHERE d.proceso =:proceso", DocumentoProceso.class)
                    .setParameter("proceso", "TitulacionTSU")
                    .getResultList();
            }else{
                if(ultRegEstudiante.getGrupo().getIdPe()==50){
                    numTotal = em.createQuery("SELECT d FROM DocumentoProceso d WHERE d.proceso =:proceso", DocumentoProceso.class)
                     .setParameter("proceso", "TitulacionIngLic")
                     .getResultList();
                }else{
                    numTotal = em.createQuery("SELECT d FROM DocumentoProceso d WHERE d.proceso =:proceso AND d.documento.documento<>:documento", DocumentoProceso.class)
                     .setParameter("proceso", "TitulacionIngLic")
                     .setParameter("documento",32)
                     .getResultList();
                }
            }
            
            FechaTerminacionTitulacion fechaTerminacionTitulacion = em.createQuery("SELECT f FROM FechaTerminacionTitulacion f WHERE f.generacion =:generacion AND f.nivel =:nivel", FechaTerminacionTitulacion.class)
                    .setParameter("generacion", expedienteBD.getEvento().getGeneracion())
                    .setParameter("nivel", expedienteBD.getEvento().getNivel())
                    .getResultStream().findFirst().orElse(null);
            
            DtoExpedienteTitulacion dto = new DtoExpedienteTitulacion(expedienteBD, ultRegEstudiante.getTipoEstudiante().getDescripcion(), personal, fechaIntExp, fechaNacimiento, progEdu, nivelEdu, generacion, genero, domicilio, estadoDom, munDom, asentDom, comunicaciones, datosAcademicos, iems, locIems, dtoPagoTit, expedienteBD.getDocumentoExpedienteTitulacionList().size(), numTotal.size(), fechaTerminacionTitulacion);
            return ResultadoEJB.crearCorrecto(dto, "Expediente de titulación empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el expediente de titulación (EjbIntegracionExpedienteTitulacion.getDtoExpedienteTitulacion).", e, DtoExpedienteTitulacion.class);
        }
    }
    
     /**
     * Permite consultar documentos que tiene registrado el expediente de titulación
     * @param expediente
     * @return Lista de documentos registrados
     */
    public ResultadoEJB<List<DocumentoExpedienteTitulacion>> buscarDocumentosExpediente(ExpedienteTitulacion expediente) {
        try{
            
            List<DocumentoExpedienteTitulacion> documentosExpediente = em.createQuery("SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.expediente.expediente =:expediente", DocumentoExpedienteTitulacion.class)
                .setParameter("expediente", expediente.getExpediente())
                .getResultList();
            
            documentosExpediente = buscarDocumentosExpedienteFísico(documentosExpediente).getValor();
            
            return ResultadoEJB.crearCorrecto(documentosExpediente, "La búsqueda de documentos se ha realizado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de documentos. (EjbIntegracionExpedienteTitulacion.buscarDocumentosExpediente)", e, null);
        }
    }
    
     /**
     * Permite validar datos personales
     * @param expedienteTitulacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> validarDatosPersonales(ExpedienteTitulacion expedienteTitulacion){
        try{
            
            expedienteTitulacion.setPasoRegistro("Datos Personales");
            expedienteTitulacion.setFechaPaso(new Date());
            em.merge(expedienteTitulacion);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(expedienteTitulacion, "Tus datos personales se han validado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo validar tus datos personales. (EjbIntegracionExpedienteTitulacion.validarDatosPersonales)", e, null);
        }
    }
    
     /**
     * Permite validar datos de domicilio y de contacto
     * @param expedienteTitulacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> validarDomCom(ExpedienteTitulacion expedienteTitulacion){
        try{
            
            expedienteTitulacion.setPasoRegistro("Domicilio y Comunicaciones");
            expedienteTitulacion.setFechaPaso(new Date());
            em.merge(expedienteTitulacion);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(expedienteTitulacion, "Tu domicilio y comunicaciones se han validado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo validar tu domicilio y comunicaciones. (EjbIntegracionExpedienteTitulacion.validarDomCom)", e, null);
        }
    }
    
    /**
     * Permite validar antecedentes académicos
     * @param expedienteTitulacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> validarAntAcademicos(ExpedienteTitulacion expedienteTitulacion){
        try{
            
            expedienteTitulacion.setPasoRegistro("Antecedentes Académicos");
            expedienteTitulacion.setFechaPaso(new Date());
            em.merge(expedienteTitulacion);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(expedienteTitulacion, "Tus antecedentes académicos se han validado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo validar tus antecedentes académicos. (EjbIntegracionExpedienteTitulacion.validarAntAcademicos)", e, null);
        }
    }
    
    /**
     * Permite finalizar la integración de expediente
     * @param expedienteTitulacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> finalizarIntegracion(ExpedienteTitulacion expedienteTitulacion){
        try{
            
            expedienteTitulacion.setPasoRegistro("Fin Integración");
            expedienteTitulacion.setFechaPaso(new Date());
            em.merge(expedienteTitulacion);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(expedienteTitulacion, "Se ha finalizado la integración de expediente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo finalizar la integración del expediente. (EjbIntegracionExpedienteTitulacion.finalizarIntegracion)", e, null);
        }
    }
    
    /**
     * Permite guardar paso de fotografía
     * @param expedienteTitulacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> guardarPasoFotografiaTitulacion(ExpedienteTitulacion expedienteTitulacion){
        try{
            
            expedienteTitulacion.setPasoRegistro("Fotografía para Título");
            expedienteTitulacion.setFechaPaso(new Date());
            em.merge(expedienteTitulacion);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(expedienteTitulacion, "Se guardó paso de carga de fotografía para titulación correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar paso de carga de fotografía. (EjbIntegracionExpedienteTitulacion.guardarPasoFotografiaTitulacion)", e, null);
        }
    }
    
     /**
     * Permite obtener pestaña de registro
     * @param expedienteTitulacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> getPestaniaRegistro(ExpedienteTitulacion expedienteTitulacion){
        try{
            Integer pestaniaActiva = 0;
            switch (expedienteTitulacion.getPasoRegistro()) {
                case "Inicio Integración":
                    pestaniaActiva = 0;
                    break;
                case "Datos Personales":
                    pestaniaActiva = 1;
                    break;
                case "Domicilio y Comunicaciones":
                    pestaniaActiva = 2;
                    break;
                case "Antecedentes Académicos":
                    pestaniaActiva = 3;
                    break;
                case "Fotografía para Título":
                    pestaniaActiva = 4;
                    break;
                case "Fin Integración":
                    pestaniaActiva = 4;
                    break;
                default:
                    break;
            }
            
            return ResultadoEJB.crearCorrecto(pestaniaActiva, "Se ha obtenido la pestania activa del registro.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la pestania activa del registro. (EjbIntegracionExpedienteTitulacion.getPestaniaRegistro)", e, null);
        }
    }
    
     /**
     * Permite obtener el progreso del registro
     * @param expedienteTitulacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> getProgresoRegistro(ExpedienteTitulacion expedienteTitulacion){
        try{
            Integer progresoExpediente = 0;
            switch (expedienteTitulacion.getPasoRegistro()) {
                case "Inicio Integración":
                    progresoExpediente = 10;
                    break;
                case "Datos Personales":
                    progresoExpediente = 20;
                    break;
                case "Domicilio y Comunicaciones":
                    progresoExpediente = 30;
                    break;
                case "Antecedentes Académicos":
                    progresoExpediente = 40;
                    break;
                case "Fotografía para Título":
                    progresoExpediente = 90;
                    break;
                case "Fin Integración":
                    progresoExpediente = 100;
                    break;
                default:
                    break;
            }
            
            return ResultadoEJB.crearCorrecto(progresoExpediente, "Se ha obtenido el progreso del registro.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el progreso del registro. (EjbIntegracionExpedienteTitulacion.getProgresoRegistro)", e, null);
        }
    }
     /**
     * Permite actualizar los datos de contacto
     * @param medioComunicacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<MedioComunicacion> actualizarMediosComunicacion(MedioComunicacion medioComunicacion){
        try{
            
            em.merge(medioComunicacion);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(medioComunicacion, "Tus medios de comunicación se han actualizado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar tus medios de comunicación. (EjbIntegracionExpedienteTitulacion.actualizarMediosComunicacion)", e, null);
        }
    }
    
     /**
     * Permite actualizar domicilio
     * @param domicilio
     * @return Resultado del proceso
     */
    public ResultadoEJB<Domicilio> actualizarDomicilio(Domicilio domicilio){
        try{
            
            em.merge(domicilio);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(domicilio, "Tu domicilio se han actualizado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar tu domicilio. (EjbIntegracionExpedienteTitulacion.actualizarDomicilio)", e, null);
        }
    }
    
     /**
     * Permite actualizar datos académicos
     * @param datosAcademicos
     * @return Resultado del proceso
     */
    public ResultadoEJB<DatosAcademicos> actualizarDatosAcademicos(DatosAcademicos datosAcademicos){
        try{ 
            
            em.merge(datosAcademicos);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(datosAcademicos, "Tus antecedentes académicos se han actualizado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar tus antecedentes académicos. (EjbIntegracionExpedienteTitulacion.actualizarDatosAcademicos)", e, null);
        }
    }
    
     /**
     * Permite obtener información del documento seleccionado
     * @param claveDoc
     * @return Resultado del proceso
     */
    public Documento obtenerInformacionDocumento(Integer claveDoc){
        
        Documento documentoTitulacion = em.createQuery("SELECT d FROM Documento d WHERE d.documento =:documento", Documento.class)
                    .setParameter("documento", claveDoc)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        
        return documentoTitulacion;
       
    }
    
     /**
     * Permite guardar documento en el expediente
     * @param documentoExpedienteTitulacion
     * @return Resultado del proceso
     */
    public DocumentoExpedienteTitulacion guardarDocumentoExpediente(DocumentoExpedienteTitulacion documentoExpedienteTitulacion){
       
        em.persist(documentoExpedienteTitulacion);
        em.flush();
        Messages.addGlobalInfo("<b>Se registró el documento al expediente correctamente </b>");
        
        if(documentoExpedienteTitulacion.getDocumento().getDocumento().equals(18) || documentoExpedienteTitulacion.getDocumento().getDocumento().equals(25))
        {
            guardarPasoFotografiaTitulacion(documentoExpedienteTitulacion.getExpediente());
        }
            
        return documentoExpedienteTitulacion;
    }
    
     /**
     * Permite consultar status de validación del documento
     * @param claveDoc
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> consultarStatusDocumento(Integer claveDoc){
        try{ 
            
            DocumentoExpedienteTitulacion documentoExpedienteTitulacion = em.createQuery("SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.documentoExpediente =:documento", DocumentoExpedienteTitulacion.class)
                    .setParameter("documento", claveDoc)
                    .getSingleResult();
        
            return ResultadoEJB.crearCorrecto(documentoExpedienteTitulacion.getValidado(), "Se ha obtenido el status del documento correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el status del documento. (EjbIntegracionExpedienteTitulacion.consultarStatusDocumento)", e, null);
        }
    }
    
     /**
     * Permite eliminar documento del expediente (física y en la base de datos)
     * @param docsExp
     * @return Resultado del proceso
     */
    public Boolean eliminarDocumentosEnExpediente(DocumentoExpedienteTitulacion docsExp) {
       
         if (docsExp == null) {
            return false;
        }

        Integer id = docsExp.getDocumentoExpediente();
        try {
            ServicioArchivos.eliminarArchivo(docsExp.getRuta());
            DocumentoExpedienteTitulacion doc = em.find(DocumentoExpedienteTitulacion.class, docsExp.getDocumentoExpediente());
            em.remove(doc);
            em.flush();
            Messages.addGlobalInfo("<b>Se eliminó el documento del expediente correctamente </b>");

        } catch (Exception e) {
            return false;
        }
        
        if(docsExp.getDocumento().getDocumento().equals(18) || docsExp.getDocumento().getDocumento().equals(25))
        {
             validarAntAcademicos(docsExp.getExpediente());
        }

        return em.find(DocumentoExpedienteTitulacion.class, id) == null;
    }
    
     /**
     * Permite consultar lista de pagos realiados por el estudiante
     * @param matricula
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoPagosEstudianteFinanzas>> getListaDtoPagosEstudianteFinanzas(Integer matricula) {
        try{ 
            
         String mat = Integer.toString(matricula); 
            
         List<Viewregalumnosnoadeudo> listaPagosNoAdeudo = em.createQuery("select v from Viewregalumnosnoadeudo v where v.matricula = :matricula", Viewregalumnosnoadeudo.class)
                .setParameter("matricula", mat)
                .getResultList();

        List<DtoPagosEstudianteFinanzas> listaDtoPagosEstudianteFinanzas = new ArrayList<>();

        listaPagosNoAdeudo.forEach(pago -> {
            DtoPagosEstudianteFinanzas dto = new DtoPagosEstudianteFinanzas(pago.getConcepto(), pago.getCveRegistro(), pago.getDescripcion(), pago.getFechaPago(), pago.getIdCatalogoConceptoPago(), pago.getMatricula(), pago.getMonto(), pago.getSiglas(), pago.getValcartanoadueudoTSU(), pago.getValcartanoadedudoIng());
            listaDtoPagosEstudianteFinanzas.add(dto);
        });
        
            return ResultadoEJB.crearCorrecto(listaDtoPagosEstudianteFinanzas, "Se ha obtenido la lista de pagos en finanzas del estudiante correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de pagos en finanzas del estudiante. (EjbIntegracionExpedienteTitulacion.getListaDtoPagosFinanzas)", e, null);
        }
    }
    
     /**
     * Permite consultar pagos por concepto de titulación nivel tsu
     * @param listaDtoPagosEstudianteFinanzas
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoPagosEstudianteFinanzas> getDtoPagoTitulacionTSU(List<DtoPagosEstudianteFinanzas> listaDtoPagosEstudianteFinanzas) {
        try {
            Short validacion;
            Long existeValidacion = listaDtoPagosEstudianteFinanzas.stream().filter(p -> p.getValcartanoadueudoTSU() == 1).count();
            if (existeValidacion == 0) {
                validacion = 0;
            } else {
                validacion = 1;
            }
            DtoPagosEstudianteFinanzas dtoPagosEstudianteFinanzas = new DtoPagosEstudianteFinanzas();
            listaDtoPagosEstudianteFinanzas.forEach((pagoFinanzas) -> {

                if (pagoFinanzas.getConcepto() == 50 || pagoFinanzas.getConcepto() == 128) {

                    dtoPagosEstudianteFinanzas.setConcepto(pagoFinanzas.getConcepto());
                    dtoPagosEstudianteFinanzas.setCveRegistro(pagoFinanzas.getConcepto());
                    dtoPagosEstudianteFinanzas.setDescripcion(pagoFinanzas.getDescripcion());
                    dtoPagosEstudianteFinanzas.setFechaPago(pagoFinanzas.getFechaPago());
                    dtoPagosEstudianteFinanzas.setIdCataloogoConceptoPago(pagoFinanzas.getIdCataloogoConceptoPago());
                    dtoPagosEstudianteFinanzas.setMatricula(pagoFinanzas.getMatricula());
                    dtoPagosEstudianteFinanzas.setMonto(pagoFinanzas.getMonto());
                    dtoPagosEstudianteFinanzas.setSiglas(pagoFinanzas.getSiglas());
                    dtoPagosEstudianteFinanzas.setValcartanoadueudoTSU(validacion);

                }

            });
            return ResultadoEJB.crearCorrecto(dtoPagosEstudianteFinanzas, "Se ha obtenido la información de pago de título de tsu del estudiante correctamente.");
        } catch (Throwable e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la información de pago de título del estudiante. (EjbIntegracionExpedienteTitulacion.getDtoPagoTitulacionTSU)", e, null);
        }
    }
    
     /**
     * Permite consultar pagos por concepto de titulación nivel ingenieria-licenciatura
     * @param listaDtoPagosEstudianteFinanzas
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoPagosEstudianteFinanzas> getDtoPagoTitulacionING(List<DtoPagosEstudianteFinanzas> listaDtoPagosEstudianteFinanzas) {
        try {
            Short validacion;
            Long existeValidacion = listaDtoPagosEstudianteFinanzas.stream().filter(p -> p.getValcartanoadedudoIng() == 1).count();
            if (existeValidacion == 0) {
                validacion = 0;
            } else {
                validacion = 1;
            }
            DtoPagosEstudianteFinanzas dtoPagosEstudianteFinanzas = new DtoPagosEstudianteFinanzas();
            listaDtoPagosEstudianteFinanzas.forEach((pagoFinanzas) -> {

                if (pagoFinanzas.getConcepto() == 40 || pagoFinanzas.getConcepto() == 127) {

                    dtoPagosEstudianteFinanzas.setConcepto(pagoFinanzas.getConcepto());
                    dtoPagosEstudianteFinanzas.setCveRegistro(pagoFinanzas.getConcepto());
                    dtoPagosEstudianteFinanzas.setDescripcion(pagoFinanzas.getDescripcion());
                    dtoPagosEstudianteFinanzas.setFechaPago(pagoFinanzas.getFechaPago());
                    dtoPagosEstudianteFinanzas.setIdCataloogoConceptoPago(pagoFinanzas.getIdCataloogoConceptoPago());
                    dtoPagosEstudianteFinanzas.setMatricula(pagoFinanzas.getMatricula());
                    dtoPagosEstudianteFinanzas.setMonto(pagoFinanzas.getMonto());
                    dtoPagosEstudianteFinanzas.setSiglas(pagoFinanzas.getSiglas());
                    dtoPagosEstudianteFinanzas.setValcartanoadedudoIng(validacion);

                }

            });
            return ResultadoEJB.crearCorrecto(dtoPagosEstudianteFinanzas, "Se ha obtenido la información de pago de título de ingeniería/licenciatura del estudiante correctamente.");
        } catch (Throwable e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la información de pago de título del estudiante. (EjbIntegracionExpedienteTitulacion.getDtoPagoTitulacionING)", e, null);
        }
    }
    
      /**
     * Permite actualizar datos de titulación
     * @param expedienteTitulacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<ExpedienteTitulacion> actualizarDatosTitulacion(ExpedienteTitulacion expedienteTitulacion){
        try{ 
            
            em.merge(expedienteTitulacion);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(expedienteTitulacion, "Los datos de promedio y servicio social se han actualizado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar los datos de promedio y servicio social. (EjbIntegracionExpedienteTitulacion.actualizarDatosTitulacion)", e, null);
        }
    }
    
     /**
     * Permite consultar documentos que tiene registrado el expediente de titulación
     * @param documentosExpedienteTitulacion
     * @return Lista de documentos registrados
     */
    public ResultadoEJB<List<DocumentoExpedienteTitulacion>> buscarDocumentosExpedienteFísico(List<DocumentoExpedienteTitulacion> documentosExpedienteTitulacion) {
        try{
            
            List<DocumentoExpedienteTitulacion> documentosExpedienteExisten = new ArrayList<>();
            documentosExpedienteTitulacion.forEach((documento) -> {
                File f = new File(documento.getRuta());
                if (!f.exists()) {
                      System.err.println("No existe el siguiente documento " + documento.getRuta());
//                    em.remove(documento);
//                    em.flush();
                }else{
                    documentosExpedienteExisten.add(documento);
                }
            });
            
            
            return ResultadoEJB.crearCorrecto(documentosExpedienteExisten, "La búsqueda de documentos se ha realizado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de documentos. (EjbIntegracionExpedienteTitulacion.buscarDocumentosExpedienteFísico)", e, null);
        }
    }
    
     /**
     * Permite actualizar validación y observaciones de un documento en el expediente
     * @param documentoExpedienteTitulacion
     * @return Resultado del proceso
     */
    public DocumentoExpedienteTitulacion actualizarDocumentoExpediente(DocumentoExpedienteTitulacion documentoExpedienteTitulacion){
        em.merge(documentoExpedienteTitulacion);
        em.flush();
        Messages.addGlobalInfo("<b>Se actualizó la validación y observaciones del documento correctamente </b>");
            
        return documentoExpedienteTitulacion;
    }
}
