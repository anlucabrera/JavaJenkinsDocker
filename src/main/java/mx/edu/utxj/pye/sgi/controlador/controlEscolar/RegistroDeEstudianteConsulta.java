package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsistencias;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import org.omnifaces.util.Messages;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.timeline.TimelineEvent;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReincorporacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroDelEstudianteRoConsultaMultiple;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbResultadosConfiguraciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ContactoEmergenciasEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicosComplementarios;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosFamiliares;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosLaborales;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosMedicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosSocioeconomicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documentosentregadosestudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Domicilio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Escolaridad;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EspecialidadCentro;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.IntegrantesFamilia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.LenguaIndigena;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioDifusion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Ocupacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Sistema;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoDiscapacidad;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoSangre;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutorFamiliar;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



/**
 * La selección del grupo, docente y del periodo deben ser directos de un control de entrada
 */
@Named
@ViewScoped
public class RegistroDeEstudianteConsulta extends ViewScopedRol implements Desarrollable {

    @Getter    @Setter    RegistroDelEstudianteRoConsultaMultiple rol;

    @EJB EjbAsignacionIndicadoresCriterios ejb;
    @EJB EjbReincorporacion ejbReincorporacion;
    @EJB EjbResultadosConfiguraciones configuraciones;
    @EJB EjbRegistroPlanEstudio erpe;
    @EJB EjbValidacionRol evr;
    @EJB EjbPropiedades ep;
    @EJB EjbAsistencias ea;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = Boolean.FALSE;
    Integer bt=0,bd=0,ri=0;
    BigDecimal promedio = BigDecimal.ZERO;
    Double suma = 0D;
    String texto="";

    /**
     * Inicializa:<br/>
     *      El filtro de rol por area superior y categiría operativa<br/>
     *      La referencia al director si es que el usuario logueado es efectivamente un director por medio del filtro de rol<br/>
     *      El programa educativo al que pertenece el director por medio de operación segura antierror<br/>
     *      El DTO del rol<br/>
     *      La lista de periodos escolares en forma descendente por medio de operación segura antierror<br/>
     *      EL mapa de programas con grupos por medio de operación segura antierror ordenando programas por areas, niveles y nombre del programa y los grupos por grado y letra
     */
    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init() {
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_ESTUDIANTE_CONSULTA);
            
            ResultadoEJB<Filter<PersonalActivo>> resValidacionDi = evr.validarDirector(logon.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resValidacionEn = evr.validarEncargadoDireccion(logon.getPersonal().getClave());//validar si es director                        
            ResultadoEJB<Filter<PersonalActivo>> resValidacionSa = evr.validarSecretariaAcademica(logon.getPersonal().getClave());//validar si es director                        
            ResultadoEJB<Filter<PersonalActivo>> resValidacionDo = evr.validarTutor(logon.getPersonal().getClave());//validar si es director                        
            ResultadoEJB<Filter<PersonalActivo>> resValidacionSe = evr.validarJefeDepartamento(logon.getPersonal().getClave(),11);//validar si es director                        
            ResultadoEJB<Filter<PersonalActivo>> resValidacionPs = evr.validarPsicopedagogia(logon.getPersonal().getClave());//validar si es director                        
            
            if (!resValidacionDi.getCorrecto() && !resValidacionEn.getCorrecto() && !resValidacionSa.getCorrecto() && !resValidacionDo.getCorrecto() && !resValidacionSe.getCorrecto() && !resValidacionPs.getCorrecto()) {
                rol.setMensajeV("El acceso solo está autorizado a Personla Académico.");
                mostrarMensajeResultadoEJB(resValidacionDi);
                mostrarMensajeResultadoEJB(resValidacionDo);
                mostrarMensajeResultadoEJB(resValidacionEn);
                mostrarMensajeResultadoEJB(resValidacionSa);
                mostrarMensajeResultadoEJB(resValidacionSe);
                mostrarMensajeResultadoEJB(resValidacionPs);
                return;
            }            
            Filter<PersonalActivo> filtroEn = resValidacionEn.getValor();//se obtiene el filtro resultado de la validación         
            Filter<PersonalActivo> filtroDi = resValidacionDi.getValor();//se obtiene el filtro resultado de la validación         
            Filter<PersonalActivo> filtroDo = resValidacionDo.getValor();//se obtiene el filtro resultado de la validación         
            Filter<PersonalActivo> filtroSa = resValidacionSa.getValor();//se obtiene el filtro resultado de la validación   
            Filter<PersonalActivo> filtroSe = resValidacionSe.getValor();//se obtiene el filtro resultado de la validación   
            Filter<PersonalActivo> filtroPs = resValidacionPs.getValor();//se obtiene el filtro resultado de la validación   
            
            PersonalActivo activoEn = filtroEn.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            PersonalActivo activoDo = filtroDo.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            PersonalActivo activoDi = filtroDi.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            PersonalActivo activoSa = filtroSa.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            PersonalActivo activoSe = filtroSe.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            PersonalActivo activoPs = filtroPs.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
                        
            rol = new RegistroDelEstudianteRoConsultaMultiple(filtroEn, activoEn, activoEn.getAreaOperativa());
            rol.setEsEn(rol.tieneAcceso(activoEn));            
            if(rol.getEsEn()==null)rol.setEsEn(Boolean.FALSE);
            if (rol.getEsEn()) {
                rol = new RegistroDelEstudianteRoConsultaMultiple(filtroEn, activoEn, activoEn.getAreaOperativa());
                tieneAcceso=Boolean.TRUE;
                rol.setDirector(activoEn);
                rol.setTipoUser(1);
            }else{
                rol = new RegistroDelEstudianteRoConsultaMultiple(filtroDo, activoDo, activoDo.getAreaOperativa());
                rol.setEsDo(rol.tieneAcceso(activoDo));
                if(rol.getEsDo()==null)rol.setEsDo(Boolean.FALSE);
                if (rol.getEsDo()) {
                    rol = new RegistroDelEstudianteRoConsultaMultiple(filtroDo, activoDo, activoDo.getAreaOperativa());
                    tieneAcceso=Boolean.TRUE;
                    rol.setTipoUser(3);
                    rol.setDocente(activoDo);
                } else {
                    rol = new RegistroDelEstudianteRoConsultaMultiple(filtroDi, activoDi, activoDi.getAreaOperativa());
                    rol.setEsDi(rol.tieneAcceso(activoDi));
                    if(rol.getEsDi()==null)rol.setEsDi(Boolean.FALSE);
                    if (rol.getEsDi()) {
                        rol = new RegistroDelEstudianteRoConsultaMultiple(filtroDi, activoDi, activoDi.getAreaOperativa());
                        tieneAcceso=Boolean.TRUE;
                        rol.setDirector(activoEn);
                        rol.setTipoUser(1);
                    }else{
                        rol = new RegistroDelEstudianteRoConsultaMultiple(filtroSa, activoSa, activoSa.getAreaOperativa());
                        rol.setEsSa(rol.tieneAcceso(activoSa));
                        if(rol.getEsSa()==null)rol.setEsSa(Boolean.FALSE);
                        if (rol.getEsSa()) {
                            rol = new RegistroDelEstudianteRoConsultaMultiple(filtroSa, activoSa, activoSa.getAreaOperativa());
                            tieneAcceso=Boolean.TRUE;
                            rol.setAcademica(activoSa);
                            rol.setTipoUser(2);
                        } else  {
                            rol = new RegistroDelEstudianteRoConsultaMultiple(filtroSe, activoSe, activoSe.getAreaOperativa());
                            rol.setEsSe(rol.tieneAcceso(activoSe));
                            if(rol.getEsSe()==null)rol.setEsSe(Boolean.FALSE);
                            if (rol.getEsSe()) {
                                rol = new RegistroDelEstudianteRoConsultaMultiple(filtroSe, activoSe, activoSe.getAreaOperativa());
                                tieneAcceso=Boolean.TRUE;
                                rol.setEstudiantiles(activoSe);
                                rol.setTipoUser(2);
                            } else  {
                                rol = new RegistroDelEstudianteRoConsultaMultiple(filtroPs, activoPs, activoPs.getAreaOperativa());
                                rol.setEsPs(rol.tieneAcceso(activoPs));
                                if(rol.getEsPs()==null)rol.setEsPs(Boolean.FALSE);
                                if (rol.getEsPs()) {
                                    rol = new RegistroDelEstudianteRoConsultaMultiple(filtroPs, activoPs, activoPs.getAreaOperativa());
                                    tieneAcceso=Boolean.TRUE;
                                    rol.setPsicopedadogico(activoPs);
                                    rol.setTipoUser(2);
                                } else  {
                                    tieneAcceso=Boolean.FALSE;
                                    mostrarMensajeNoAcceso();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
           
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso           
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu

            consultaDatos();
            buscarRegistroEstudianteP1();
            buscarRegistroEstudianteP2();
            rol.setFechaInpresion(new Date());
            inicializarValoresEscolares();
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registroEstudiante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void consultaDatos() {
        ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
        if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
   
        rol.setPeriodos(resPeriodos.getValor());
        rol.setPeriodo(ea.getPeriodoActual());
        rol.setPeriodoActivo(rol.getPeriodo().getPeriodo());
        if(!rol.getTipoUser().equals(3)){
               
            ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> resProgramaPlan;
            if (rol.getTipoUser().equals(2)) {
                resProgramaPlan = erpe.getProgramasEducativostotal();
            } else {
                resProgramaPlan = erpe.getProgramasEducativos(rol.getDirector());
            }
            if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
                
            rol.setAreaPlanEstudioMap(resProgramaPlan.getValor());           
            rol.setPlanEstudio(rol.getPlanesEstudios().get(0));
        }             
        ResultadoEJB<List<Grupo>> resgrupos;
        if(!rol.getTipoUser().equals(3)){
            resgrupos = erpe.getListaGrupoPlanEstudio(rol.getPlanEstudio(),rol.getPeriodo());
        }else{                
            resgrupos = erpe.getListaGrupoPorTutor(rol.getDocente(),rol.getPeriodo());
        }  
        if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);   
        rol.setGruposA(resgrupos.getValor());
        rol.setGrupoSelec(rol.getGruposA().get(0));
        ResultadoEJB<List<Estudiante>> resEstu = ejbReincorporacion.getEstudiantesGrupo(rol.getGrupoSelec());            
        if(!resEstu.getCorrecto()) mostrarMensajeResultadoEJB(resEstu);                
        rol.setEstudiantes(resEstu.getValor());
        rol.getEstudiantes().add(new Estudiante(0));
        rol.setEstudiante(new Estudiante(0));
        buscarRegistroEstudianteP1();
        buscarRegistroEstudianteP2();
    }
   
    public void cambiarPeriodo() {
        if (rol.getPeriodo() == null) {
            mostrarMensaje("No hay periodo escolar seleccionado.");
            rol.setGruposA(Collections.EMPTY_LIST);
            rol.setEstudiantes(Collections.EMPTY_LIST);  
            rol.setGrupoSelec(new Grupo());            
            rol.setEstudiante(null);
            return;
        }
        ResultadoEJB<List<Grupo>> resgrupos;
        if(rol.getTipoUser()!=3){
            rol.setPlanEstudio(rol.getPlanesEstudios().get(0));
            resgrupos = erpe.getListaGrupoPlanEstudio(rol.getPlanEstudio(),rol.getPeriodo());
        }else{
            resgrupos = erpe.getListaGrupoPorTutor(rol.getDocente(),rol.getPeriodo());
        }
        if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);        
        if(!resgrupos.getValor().isEmpty()){
            rol.setGruposA(resgrupos.getValor()); 
            rol.setGrupoSelec(rol.getGruposA().get(0));    
            ResultadoEJB<List<Estudiante>> estB;
            if(rol.getTipoUser()!=3){ 
                estB = ejbReincorporacion.getEstudiantesGrupo(rol.getGrupoSelec());   
            }else{
                estB = ejbReincorporacion.getEstudiantesGrupo(rol.getGrupoSelec());
            }
            if(!estB.getCorrecto()) mostrarMensajeResultadoEJB(estB);
            if (!estB.getValor().isEmpty()) {
                rol.setEstudiantes(estB.getValor());
                rol.getEstudiantes().add(new Estudiante(0));
                rol.setEstudiante(new Estudiante(0));
            } else {
                inicializarValoresEscolares();
                consultaDatos();
            }
        }else{
            inicializarValoresEscolares();
            consultaDatos();
        }   
        buscarRegistroEstudianteP1();
        buscarRegistroEstudianteP2();
    }
     
    public void cambiarPlanestudio(ValueChangeEvent event) {
        rol.setGruposA(new ArrayList<>());
        rol.setEstudiantes(new ArrayList<>());
        rol.setGrupoSelec(new Grupo());
        rol.setPlanEstudio((PlanEstudio) event.getNewValue());
        
        ResultadoEJB<List<Grupo>> resgrupos = erpe.getListaGrupoPlanEstudio(rol.getPlanEstudio(),rol.getPeriodo());
        if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
        rol.setGruposA(resgrupos.getValor()); 
        rol.setGrupoSelec(rol.getGruposA().get(0));
        ResultadoEJB<List<Estudiante>> resEstu = ejbReincorporacion.getEstudiantesGrupo(rol.getGrupoSelec());            
        if(!resEstu.getCorrecto()) mostrarMensajeResultadoEJB(resEstu);
        if (resEstu.getValor().isEmpty()) {
            inicializarValoresEscolares();
            rol.setEstudiantes(Collections.EMPTY_LIST);
            return;
        }
        rol.setEstudiantes(resEstu.getValor());
        rol.getEstudiantes().add(new Estudiante(0));
        rol.setEstudiante(new Estudiante(0));
        buscarRegistroEstudianteP1();
        buscarRegistroEstudianteP2();
    }    
    
   public void cambiarGrupo(ValueChangeEvent event) {
        rol.setEstudiantes(new ArrayList<>());
        rol.setGrupoSelec((Grupo) event.getNewValue());
        ResultadoEJB<List<Estudiante>> resEstu = ejbReincorporacion.getEstudiantesGrupo(rol.getGrupoSelec());            
        if(!resEstu.getCorrecto()) mostrarMensajeResultadoEJB(resEstu);
        if (resEstu.getValor().isEmpty()) {
            inicializarValoresEscolares();
            rol.setEstudiantes(Collections.EMPTY_LIST);
            return;
        }
        rol.setEstudiantes(resEstu.getValor());
        rol.getEstudiantes().add(new Estudiante(0));
        rol.setEstudiante(new Estudiante(0));
        buscarRegistroEstudianteP1();
        buscarRegistroEstudianteP2();
    }
    
    public void cambiarEstudiante(ValueChangeEvent event) {
        inicializarValoresEscolares();
        rol.setEstudiante((Estudiante) event.getNewValue());
        buscarRegistroEstudianteP1();
        buscarRegistroEstudianteP2();
    }

    public void inicializarValoresEscolares() {
        rol.setComunicacion(new MedioComunicacion());
        rol.setPersonaD(new DtoReincorporacion.PersonaR(new Persona(), new MedioComunicacion(), new Pais(), Operacion.PERSISTIR, Operacion.PERSISTIR, Boolean.FALSE));
        rol.setAspirante(new DtoReincorporacion.AspiranteR(new Aspirante(), new TipoAspirante(), new ProcesosInscripcion(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDmedico(new DtoReincorporacion.MedicosR(new DatosMedicos(), new TipoSangre(), new TipoDiscapacidad(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setTutor(new DtoReincorporacion.TutorR(new TutorFamiliar(), new Ocupacion(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDfamiliares(new DtoReincorporacion.FamiliaresR(new DatosFamiliares(), rol.getTutor(), new Ocupacion(), new Ocupacion(), new Escolaridad(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDdomicilios(new DtoReincorporacion.DomicilioR(new Domicilio(), Boolean.FALSE, Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDacademicos(new DtoReincorporacion.AcademicosR(new DatosAcademicos(), new AreasUniversidad(), new AreasUniversidad(), new Sistema(), new Sistema(), new Estado(), new Municipio(), new Localidad(), new Iems(), new EspecialidadCentro(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setEncuesta(new DtoReincorporacion.EncuestaR(new EncuestaAspirante(), new LenguaIndigena(), new MedioDifusion(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setRein(new DtoReincorporacion.ProcesoInscripcionRein("", "", 0, Boolean.TRUE, 0, "", new ArrayList<>(), new Documentosentregadosestudiante()));
        rol.setEstudianteR(new ArrayList<>());
        rol.setCalificacionesR(new ArrayList<>());
        rol.setEstudiantesReincorporaciones(new ArrayList<>());
        rol.setPaso(0);
        rol.setTipo(0);
        rol.setExtran(Boolean.FALSE);
        rol.setFinalizado(Boolean.FALSE);
        rol.setEditarCalificaciones(Boolean.FALSE);
        rol.setNombreR("Seleccione Un Tipo");
        rol.setTipoCal("Regulatoria");
        rol.setPuedeValidar(Boolean.FALSE);
        rol.setTipoSangres(ejbReincorporacion.getTiposSangre().getValor());
        rol.setTipoDiscapacidads(ejbReincorporacion.getTipoDiscapacidad().getValor());
        rol.setTipoAspirantes(ejbReincorporacion.getTipoAspirante().getValor());
        rol.setOcupacions(ejbReincorporacion.getOcupaciones().getValor());
        rol.setEscolaridads(ejbReincorporacion.getEscolaridades().getValor());
        rol.setLenguaIndigenas(ejbReincorporacion.getLenguaIndigena().getValor());
        rol.setMedioDifusions(ejbReincorporacion.getMedioDifusion().getValor());
        rol.setEspecialidadCentros(ejbReincorporacion.getEspecialidadCentro().getValor());
        rol.setSistemas(ejbReincorporacion.getSistema().getValor());
        rol.setPaisesN(ejbReincorporacion.getPais().getValor());
        rol.setEstadosDo(ejbReincorporacion.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setEstadosPo(ejbReincorporacion.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setEstadosTt(ejbReincorporacion.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setEstadosIe(ejbReincorporacion.getEstado(new Pais(42, "Mexico")).getValor());        
        rol.setEstadosPa(ejbReincorporacion.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setEstadosMa(ejbReincorporacion.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setEstadosFa(ejbReincorporacion.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setEstadosCe(ejbReincorporacion.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setEstadosDl(ejbReincorporacion.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setAreasAcademicasPo(ejbReincorporacion.getAreasUniversidad().getValor());
        rol.setAreasAcademicasSo(ejbReincorporacion.getAreasUniversidad().getValor());
        rol.setTipoEstudiantes(ejbReincorporacion.getTiposEstudiante().getValor());
        rol.setComunicacion(new MedioComunicacion());
        rol.setPersonaD(new DtoReincorporacion.PersonaR(new Persona(), new MedioComunicacion(), new Pais(), Operacion.PERSISTIR, Operacion.PERSISTIR, Boolean.FALSE));
        rol.setAspirante(new DtoReincorporacion.AspiranteR(new Aspirante(), new TipoAspirante(), new ProcesosInscripcion(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDmedico(new DtoReincorporacion.MedicosR(new DatosMedicos(), new TipoSangre(), new TipoDiscapacidad(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setTutor(new DtoReincorporacion.TutorR(new TutorFamiliar(), new Ocupacion(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDfamiliares(new DtoReincorporacion.FamiliaresR(new DatosFamiliares(), rol.getTutor(), new Ocupacion(), new Ocupacion(), new Escolaridad(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDdomicilios(new DtoReincorporacion.DomicilioR(new Domicilio(), Boolean.FALSE, Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDacademicos(new DtoReincorporacion.AcademicosR(new DatosAcademicos(), new AreasUniversidad(), new AreasUniversidad(), new Sistema(), new Sistema(), new Estado(), new Municipio(), new Localidad(), new Iems(), new EspecialidadCentro(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setEncuesta(new DtoReincorporacion.EncuestaR(new EncuestaAspirante(), new LenguaIndigena(), new MedioDifusion(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setRein(new DtoReincorporacion.ProcesoInscripcionRein("", "", 0, Boolean.TRUE, 0, "", new ArrayList<>(), new Documentosentregadosestudiante()));
        rol.setFamiliaR(new DtoReincorporacion.Familia(new IntegrantesFamilia(),new Ocupacion(), new Escolaridad(),Operacion.PERSISTIR, Boolean.TRUE));
        rol.setRcontactoEmergencia(new DtoReincorporacion.RcontactoEmergencia(new ContactoEmergenciasEstudiante(),Operacion.PERSISTIR, Boolean.TRUE));
        rol.setRegDatosLaborales(new DtoReincorporacion.RegDatosLaborales(new DatosLaborales(), Operacion.PERSISTIR, cargado));
        rol.setAcademicosCR(new DtoReincorporacion.AcademicosCR(new DatosAcademicosComplementarios(), Operacion.PERSISTIR, cargado));
        rol.setSosioeconomicosR(new DtoReincorporacion.SosioeconomicosR(new DatosSocioeconomicos(), Operacion.PERSISTIR, cargado));
        rol.setEstudianteR(new ArrayList<>());
        rol.setCalificacionesR(new ArrayList<>());
        rol.setEstudiantesReincorporaciones(new ArrayList<>());
        rol.setPaso(0);
        rol.setSubpaso(0);
        rol.setTipo(0);
        rol.setExtran(Boolean.FALSE);
        rol.setFinalizado(Boolean.FALSE);
        rol.setRespuestaTrabaja(Boolean.FALSE);
        rol.setEditarCalificaciones(Boolean.FALSE);        
        rol.setNombreR("Seleccione Un Tipo");
        rol.setTipoCal("Regulatoria");
        rol.setPuedeValidar(Boolean.FALSE);
        rol.setEvidencia("");
//        llenarCatalogos();
    } 
    
     public void buscarRegistroEstudianteP1(){
        inicializarValoresEscolares();
         if (rol.getEstudiante().getIdEstudiante() == 0) {
             return;
         }
        rol.setCurpBusqueda(rol.getEstudiante().getAspirante().getIdPersona().getCurp());
        ResultadoEJB<DtoReincorporacion.General> resAcceso = ejbReincorporacion.getDtoReincorporacion(rol.getCurpBusqueda(),Boolean.TRUE); 
        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}
        rol.setGeneral(resAcceso.getValor());        
        if(rol.getGeneral().getEcontrado()){                 
            rol.setComunicacion(rol.getGeneral().getPr().getMedioComunicacion());
            rol.setPersonaD(rol.getGeneral().getPr());
            rol.setAspirante(rol.getGeneral().getAr());
            rol.setDmedico(rol.getGeneral().getMr());
            rol.setTutor(rol.getGeneral().getTr());
            rol.setDfamiliares(rol.getGeneral().getFr());
            rol.setDdomicilios(rol.getGeneral().getDr());
            rol.setDacademicos(rol.getGeneral().getAc());
            rol.setEncuesta(rol.getGeneral().getEr());
            rol.setEstudianteR(rol.getGeneral().getErs());
            rol.setCalificacionesR(rol.getGeneral().getAcs());
        }
    }

    public void buscarRegistroEstudianteP2(){
        ResultadoEJB<DtoReincorporacion.RegistroEstudiante> resAcceso = ejbReincorporacion.getDtoRegistroEstudiante(rol.getAspirante().getAspirante(),rol.getPersonaD().getPersona()); 
        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}
        rol.setRegistroEstudiante(resAcceso.getValor());        
        if(rol.getRegistroEstudiante().getEcontrado()){                 
            rol.setRegistrDelEstudianteR(rol.getRegistroEstudiante().getEsact());
            rol.setEmergencias(rol.getRegistroEstudiante().getRes());
            rol.setAcademicosCR(rol.getRegistroEstudiante().getDac());
            rol.setRegDatosLaborales(rol.getRegistroEstudiante().getLabo());
            rol.setSosioeconomicosR(rol.getRegistroEstudiante().getSr());
            rol.setFamilias(rol.getRegistroEstudiante().getFs()); 
            rol.setRVocacional(rol.getRegistroEstudiante().getVr());
        }        
    }
     
    
    public String buscarPersonal(Integer clave) {
        try {            
            Personal p = new Personal();
            if (clave != null) {
                p = ejbPersonal.mostrarPersonalLogeado(clave);
                return p.getNombre();
            } else {
                return "Nombre del tutor";
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PaseListaDoc.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
   
    public String buscarDirector(Short clave){
        try {
            Personal p = new Personal();
            AreasUniversidad au=new AreasUniversidad();
            au=ejbAreasLogeo.mostrarAreasUniversidad(clave);
            p = ejbPersonal.mostrarPersonalLogeado(au.getResponsable());
            return p.getNombre();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(RegistroDeEstudianteConsulta.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public void onSelect(TimelineSelectEvent e) {  
        TimelineEvent timelineEvent = e.getTimelineEvent();  
   
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected event:", timelineEvent.getData().toString());  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }
    
    public String convertirRutaVistaEvidencia(String ruta) {
        if (!"".equals(ruta)) {
            File file = new File(ruta);
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }
    
    public String direccion(Integer busqueda,Integer tipo,String t) {
        String nombre = "";
        Asentamiento a = new Asentamiento();
        Localidad l = new Localidad();
        
        Pais p = new Pais();
        Estado e = new Estado();
        Municipio m = new Municipio();
        
        if ("a".equals(t)) {
            ResultadoEJB<Asentamiento> asen = ejbReincorporacion.getAsentamiento(busqueda);
            if (asen.getCorrecto()) {
                a = asen.getValor();
                p = a.getMunicipio1().getEstado().getIdpais();
                e = a.getMunicipio1().getEstado();
                m = a.getMunicipio1();
                nombre =a.getCodigoPostal()+" "+a.getNombreAsentamiento();
            }
        } else {
            ResultadoEJB<Localidad> loc = ejbReincorporacion.getLocalidadUn(busqueda);
            if (loc.getCorrecto()) {
                l = loc.getValor();
                p = l.getMunicipio().getEstado().getIdpais();
                e = l.getMunicipio().getEstado();
                m = l.getMunicipio();
                nombre=l.getNombre();
            }
        }
        switch (tipo) {
            case 1:                nombre = p.getNombre();                break;// Pais
            case 2:                nombre = e.getNombre();                break;// Estado
            case 3:                nombre = m.getNombre();                break;// Municipio
            case 4:                nombre = l.getNombre();                break;// Localidad
            case 5:                nombre = a.getNombreAsentamiento();    break;// Asentamiento
            case 6:                nombre =a.getCodigoPostal();           break;// CP
        }
        return nombre;
    }
    
    public String direccionLoc(Integer esta,Integer municipio,Integer loca, Integer tipo,String t) {
        String nombre = "";
        Localidad l = new Localidad();        
        Pais p = new Pais();
        Estado e = new Estado();
        Municipio m = new Municipio();
        
            ResultadoEJB<Localidad> loc = ejbReincorporacion.getLocalidadDireccion(esta, municipio, loca);
            if (loc.getCorrecto()) {
                l = loc.getValor();
                p = l.getMunicipio().getEstado().getIdpais();
                e = l.getMunicipio().getEstado();
                m = l.getMunicipio();
                nombre=l.getNombre();
            }
        switch (tipo) {
            case 1:                nombre = p.getNombre();                break;// Pais
            case 2:                nombre = e.getNombre();                break;// Estado
            case 3:                nombre = m.getNombre();                break;// Municipio
            case 4:                nombre = l.getNombre();                break;// Localidad
        }
        return nombre;
    }
    
    public String obtenerPromedios(Integer grado) {        
        List<DtoReincorporacion.AlineacionCalificaciones> acs = new ArrayList<>();
        acs = rol.getCalificacionesR().stream().filter(t -> t.getGrupo().getGrado() == grado).collect(Collectors.toList());
        promedio = BigDecimal.ZERO;
        suma = 0D;
        if (!acs.isEmpty()) {
            acs.forEach((t) -> {
                if (!Objects.equals(t.getGrupo().getIdGrupo(), rol.getRegistrDelEstudianteR().getGrupo().getIdGrupo())) {
                    if (!t.getCalificacionesReincorporacions().isEmpty()) {
                        t.getCalificacionesReincorporacions().forEach((c) -> {
                            suma = suma + c.getCalificacionPromedio().getValor();
                        });
                        BigDecimal sb = BigDecimal.valueOf(suma);
                        Integer tc = t.getCalificacionesReincorporacions().size() * 10;
                        BigDecimal tm = BigDecimal.valueOf(Double.parseDouble(tc.toString()));
                        BigDecimal calm = BigDecimal.valueOf(10D);
                        promedio = sb.multiply(calm).divide(tm);
                    }
                    texto = promedio.toString();
                } else {
                    texto = "Cuatrimestre en curso";
                }
            });
            return texto;
        } else {
            return "Sin calificaciones";
        }
    }
}
