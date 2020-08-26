package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReincorporacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ReincorporacionRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;



@Named
@ViewScoped
public class ReincorporacionServiciosEscolares extends ViewScopedRol implements Desarrollable {
    
    @Getter @Setter ReincorporacionRolServiciosEscolares rol;
    @Getter Boolean tieneAcceso = false;
    @Getter private Boolean cargado = false;
    
    @EJB EjbReincorporacion ejb;
    @EJB EjbValidacionRol evr;
    @EJB EjbFichaAdmision efa;
    @EJB EjbPropiedades ep;
    
    @Inject LogonMB logonMB;

    



@PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.REINCORPORACION);
            ResultadoEJB<Filter<PersonalActivo>> resAccesoEs = ejb.validarServiciosEscolares(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
            ResultadoEJB<Filter<PersonalActivo>> resAccesoDi = evr.validarDirector(logonMB.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAccesoEd = evr.validarEncargadoDireccion(logonMB.getPersonal().getClave());//validar si es director           
                                  
            if (!resAccesoDi.getCorrecto() && !resAccesoEd.getCorrecto() && !resAccesoEs.getCorrecto()) {                
                mostrarMensajeResultadoEJB(resAccesoDi);
                mostrarMensajeResultadoEJB(resAccesoEd);
                mostrarMensajeResultadoEJB(resAccesoEs);
                return;
            }
            
            Filter<PersonalActivo> filtroEs = resAccesoEs.getValor();//se obtiene el filtro resultado de la validación         
            Filter<PersonalActivo> filtroDi = resAccesoDi.getValor();//se obtiene el filtro resultado de la validación         
            Filter<PersonalActivo> filtroEd = resAccesoEd.getValor();//se obtiene el filtro resultado de la validación         
            
            PersonalActivo activoEs = filtroEs.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            PersonalActivo activoDi = filtroDi.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            PersonalActivo activoEd = filtroEd.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            
            rol = new ReincorporacionRolServiciosEscolares(filtroEs, activoEs, activoEs.getAreaOperativa());
            tieneAcceso = rol.tieneAcceso(activoEs);
            if (tieneAcceso) {
                rol.setEsEscolares(Boolean.TRUE);
                rol.setPersonalActivoSe(activoEs);
            } else {
                rol = new ReincorporacionRolServiciosEscolares(filtroDi, activoDi, activoDi.getAreaOperativa());
                tieneAcceso = rol.tieneAcceso(activoDi);
                if (tieneAcceso) {
                    rol.setEsEscolares(Boolean.FALSE);
                    rol.setPersonalActivoSe(activoDi);
                } else {
                    rol = new ReincorporacionRolServiciosEscolares(filtroEd, activoEd, activoEd.getAreaOperativa());
                    tieneAcceso = rol.tieneAcceso(activoEd);
                    if (tieneAcceso) {
                        rol.setEsEscolares(Boolean.FALSE);
                        rol.setPersonalActivoSe(activoEd);
                    } else {
                        mostrarMensajeNoAcceso();
                        return;
                    }
                }
            }
            
            ResultadoEJB<EventoEscolar> resEvento = ejb.verificarEvento();
            if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setEventoActivo(resEvento.getValor());
            
            
            rol.setComunicacion(new MedioComunicacion());
            rol.setPersonaD(new DtoReincorporacion.PersonaR(new Persona(), new MedioComunicacion(), new Pais(), Operacion.PERSISTIR, Operacion.PERSISTIR, Boolean.FALSE));
            rol.setAspirante(new DtoReincorporacion.AspiranteR(new Aspirante(), new TipoAspirante(), new ProcesosInscripcion(), Operacion.PERSISTIR, Boolean.FALSE));
            rol.setDmedico(new DtoReincorporacion.MedicosR(new DatosMedicos(), new TipoSangre(), new TipoDiscapacidad(), Operacion.PERSISTIR, Boolean.FALSE));
            rol.setTutor(new DtoReincorporacion.TutorR(new TutorFamiliar(),Operacion.PERSISTIR, Boolean.FALSE));
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
            if(!rol.getEsEscolares()){
                buscarReincorporacionPorDirector();
            }else{
                inicializarValoresEscolares();
            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    
    public void inicializarValoresEscolares() {
        rol.setTipoSangres(ejb.getTiposSangre().getValor());
        rol.setTipoDiscapacidads(ejb.getTipoDiscapacidad().getValor());
        rol.setTipoAspirantes(ejb.getTipoAspirante().getValor());
        rol.setOcupacions(ejb.getOcupaciones().getValor());
        rol.setEscolaridads(ejb.getEscolaridades().getValor());
        rol.setLenguaIndigenas(ejb.getLenguaIndigena().getValor());
        rol.setMedioDifusions(ejb.getMedioDifusion().getValor());
        rol.setEspecialidadCentros(ejb.getEspecialidadCentro().getValor());
        rol.setSistemas(ejb.getSistema().getValor());
        rol.setPaisesN(ejb.getPais().getValor());
        rol.setEstadosDo(ejb.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setEstadosPo(ejb.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setEstadosTt(ejb.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setEstadosIe(ejb.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setAreasAcademicasPo(ejb.getAreasUniversidad().getValor());
        rol.setAreasAcademicasSo(ejb.getAreasUniversidad().getValor());
        rol.setTipoEstudiantes(ejb.getTiposEstudiante().getValor());
    }        

// Validaciones Acceso
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "reincorporaciones";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
// Filtros Catalogos
    // Origen

    public void estadosPorPaisOrigen() {
        rol.setEstadosOr(ejb.getEstado(rol.getPersonaD().getPaisOr()).getValor());
    }

    public void municipiosPorEstadoOrigen() {
        rol.setMunicipiosOr(ejb.getMunicipio(rol.getPersonaD().getPersona().getEstado()).getValor());
    }

    public void localidadPormunicipioOrigen() {
        rol.setLocalidadsOr(ejb.getLocalidad(rol.getPersonaD().getPersona().getEstado(), rol.getPersonaD().getPersona().getMunicipio()).getValor());
    }

    // Residencia
    public void municipiosPorEstadoResidencia() {
        rol.setMunicipiosDo(ejb.getMunicipio(rol.getDdomicilios().getDomicilio().getIdEstado()).getValor());
    }

    public void asentamientosPorMunicipioResidencia() {
        rol.setAsentamientosDo(ejb.getAsentamiento(rol.getDdomicilios().getDomicilio().getIdEstado(), rol.getDdomicilios().getDomicilio().getIdMunicipio()).getValor());
    }

    // Procedencia
    public void municipiosPorEstadoProcedencia() {
        rol.setMunicipiosPo(ejb.getMunicipio(rol.getDdomicilios().getDomicilio().getEstadoProcedencia()).getValor());
    }

    public void asentamientosPorMunicipioProcedencia() {
        rol.setAsentamientosPo(ejb.getAsentamiento(rol.getDdomicilios().getDomicilio().getEstadoProcedencia(), rol.getDdomicilios().getDomicilio().getMunicipioProcedencia()).getValor());
    }
    // Tutor

    public void municipiosPorEstadoTutor() {
        rol.setMunicipiosTt(ejb.getMunicipio(rol.getTutor().getTutorFamiliar().getEstado()).getValor());
    }

    public void asentamientosPorMunicipioTutor() {
        rol.setAsentamientosTt(ejb.getAsentamiento(rol.getTutor().getTutorFamiliar().getEstado(), rol.getTutor().getTutorFamiliar().getMunicipio()).getValor());
    }

    //Iems
    public void municipiosPorEstadoIems() {
        rol.setMunicipiosIe(ejb.getMunicipio(rol.getDacademicos().getEstado().getIdestado()).getValor());
    }

    public void localidadPormunicipioIems() {
        rol.setLocalidadsIe(ejb.getLocalidad(rol.getDacademicos().getEstado().getIdestado(), rol.getDacademicos().getMunicipio().getMunicipioPK().getClaveMunicipio()).getValor());
    }

    public void iemsPorlocalidadIems() {
        rol.setIemses(ejb.getIems(rol.getDacademicos().getEstado().getIdestado(), rol.getDacademicos().getMunicipio().getMunicipioPK().getClaveMunicipio(), rol.getDacademicos().getLocalidad().getLocalidadPK().getClaveLocalidad()).getValor());
    }

    //Postulacion
    public void programasEducativosAcPo() {
        rol.setProgramasEducativosPo(ejb.getProgramasEducativos().getValor().stream().filter(t -> Objects.equals(t.getAreaSuperior(), rol.getDacademicos().getUniversidad1().getArea())).collect(Collectors.toList()));
    }

    public void programasEducativosAcSo() {
        rol.setProgramasEducativosSo(ejb.getProgramasEducativos().getValor().stream().filter(t -> Objects.equals(t.getAreaSuperior(), rol.getDacademicos().getUniversidad2().getArea())).collect(Collectors.toList()));
    }
    
// Validadores
    public void usarDireccionResidencia() {
        if (rol.getDdomicilios().getIgualD().equals(Boolean.TRUE)) {
            rol.setMunicipiosPo(rol.getMunicipiosDo());
            rol.setAsentamientosPo(rol.getAsentamientosDo());
            rol.getDdomicilios().getDomicilio().setCalleProcedencia(rol.getDdomicilios().getDomicilio().getCalle());
            rol.getDdomicilios().getDomicilio().setNumeroProcedencia(rol.getDdomicilios().getDomicilio().getNumero());
            rol.getDdomicilios().getDomicilio().setEstadoProcedencia(rol.getDdomicilios().getDomicilio().getIdEstado());
            rol.getDdomicilios().getDomicilio().setMunicipioProcedencia(rol.getDdomicilios().getDomicilio().getIdMunicipio());
            rol.getDdomicilios().getDomicilio().setAsentamientoProcedencia(rol.getDdomicilios().getDomicilio().getIdAsentamiento());
        }
    }
    
    public void tipoDeRegistro() {
        switch (rol.getTipo()) {
            case 0:
                rol.setNombreR("Seleccione Un Tipo");
                rol.setEditarCalificaciones(Boolean.FALSE);
                break;
            case 1:
                rol.setNombreR("Cambio de grupo");
                rol.setEditarCalificaciones(Boolean.FALSE);
                rol.setTipoCal("Regulatoria");
                llenarProceso();
                Ajax.oncomplete("PF('bui').hide();");
                break;
            case 2:
                rol.setNombreR("Cambio de carrera");
                rol.setEditarCalificaciones(Boolean.TRUE);
                rol.setTipoCal("Regulatoria");
                llenarProceso();
                Ajax.oncomplete("PF('dlgActualizacionActividad').show();");
                break;
            case 3:
                rol.setNombreR("Reincorporación otra UT");
                rol.setEditarCalificaciones(Boolean.TRUE);
                rol.setTipoCal("Regulatoria");
                llenarProceso();
                Ajax.oncomplete("PF('dlgActualizacionActividad').show();");
                break;
            case 4:
                rol.setNombreR("Equivalencia");
                rol.setEditarCalificaciones(Boolean.FALSE);
                rol.setTipoCal("Equivalencia");
                llenarProceso();
                Ajax.oncomplete("PF('dlgActualizacionActividad').show();");
                break;
            case 5:
                rol.setNombreR("Reincorporación otra generación");
                rol.setEditarCalificaciones(Boolean.TRUE);
                rol.setTipoCal("Regulatoria");
                llenarProceso();
                Ajax.oncomplete("PF('dlgActualizacionActividad').show();");
                break;
        }
        opcionInscripcion();
    }
    
    public void llenarProceso() {
        Boolean primeraOp;
        Integer matr = 0;
        List<Grupo> gs = new ArrayList<>();
        if (!rol.getEstudianteR().isEmpty()) {
            DtoReincorporacion.EstudianteR er = rol.getEstudianteR().get(rol.getEstudianteR().size() - 1);
            rol.setGrupos(ejb.getGrupos(er.getEstudiante().getCarrera()).getValor());
            primeraOp = er.getEstudiante().getOpcionIncripcion();
            matr = er.getEstudiante().getMatricula();
            if (er.getEstudiante().getTipoRegistro().equals("Equivalencia")) {
                rol.setTipoCal("Equivalencia");
            }
            List<Grupo> gs2 = new ArrayList<>();
            rol.getEstudianteR().forEach((t) -> {
                gs2.add(t.getGrupo());
            });
            gs = gs2;
        } else {
            rol.setGrupos(ejb.getGrupos(rol.getDacademicos().getAcademicos().getPrimeraOpcion()).getValor());
            primeraOp = Boolean.TRUE;
            matr = 0;
            gs = new ArrayList<>();
        }

        List<AreasUniversidad> areP = new ArrayList<>();
        List<AreasUniversidad> areS = new ArrayList<>();
        AreasUniversidad arP = new AreasUniversidad();
        AreasUniversidad arS = new AreasUniversidad();

        String op1 = "";
        String op2 = "";
        areP = rol.getProgramasEducativosPo().stream().filter(t -> t.getArea() == rol.getDacademicos().getAcademicos().getPrimeraOpcion()).collect(Collectors.toList());
        arP = areP.get(0);

        areS = rol.getProgramasEducativosSo().stream().filter(t -> t.getArea() == rol.getDacademicos().getAcademicos().getSegundaOpcion()).collect(Collectors.toList());
        arS = areS.get(0);

        op1 = arP.getNombre() + "--" + rol.getDacademicos().getSistemaPo().getNombre();
        op2 = arS.getNombre() + "--" + rol.getDacademicos().getSistemaSo().getNombre();

        rol.setRein(new DtoReincorporacion.ProcesoInscripcionRein(op1, op2, matr, primeraOp, rol.getPersonalActivoSe().getPersonal().getClave(), rol.getNombreR(), gs, new Documentosentregadosestudiante()));
    }

    public void opcionInscripcion() {
        Short carrera = 0;
        rol.getRein().setGrupos(new ArrayList<>());
        if (rol.getRein().getOpcionIncripcion()) {
            carrera = rol.getDacademicos().getAcademicos().getPrimeraOpcion();
        } else {
            carrera = rol.getDacademicos().getAcademicos().getSegundaOpcion();
        }
        if(!rol.getEstudianteR().isEmpty()){
            rol.getEstudianteR().forEach((t) -> {
                if(Objects.equals(rol.getRein().getOpcionIncripcion(), t.getPrimeraOpcion())){
                    rol.getRein().getGrupos().add(t.getGrupo());
                }
            });
        }
        rol.setGrupos(ejb.getGrupos(carrera).getValor());
    }
    

// Busquedas registros
    public void buscarReincorporacionPorDirector() {
        ResultadoEJB<List<Estudiante>> resAcceso = ejb.getEstudiantes(rol.getAreaSe(),rol.getEventoActivo()); 
        if (!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}   
        if (!resAcceso.getValor().isEmpty()) {
            resAcceso.getValor().forEach((t) -> {
                if (!(t.getTipoRegistro().equals("Inscripción") || t.getTipoRegistro().equals("Reinscripción") || t.getTipoRegistro().equals("Reinscripcion Autónoma"))) {
                    rol.getEstudiantesReincorporaciones().add(t);
                }
            });
        }
    }

     public void buscarRegistro(){
        ResultadoEJB<DtoReincorporacion.General> resAcceso = ejb.getDtoReincorporacion(rol.getCurpBusqueda(),rol.getEsEscolares()); 
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
            
            if (rol.getPersonaD().getEcontrado() && rol.getPersonaD().getPersona().getEstado() != null) {
                rol.setExtran(Boolean.FALSE);
                estadosPorPaisOrigen();
                municipiosPorEstadoOrigen();
                localidadPormunicipioOrigen();
            }
            
            if(rol.getDdomicilios().getEcontrado()){
                municipiosPorEstadoProcedencia();
                municipiosPorEstadoResidencia();
                asentamientosPorMunicipioProcedencia();
                asentamientosPorMunicipioResidencia();
            }
            
            if(rol.getTutor().getEcontrado()){
                municipiosPorEstadoTutor();
                asentamientosPorMunicipioTutor();
            }
            
            if(rol.getDacademicos().getEcontrado()){
                municipiosPorEstadoIems();
                localidadPormunicipioIems();
                iemsPorlocalidadIems();
                programasEducativosAcPo();
                programasEducativosAcSo();
            }
            if (!rol.getEstudianteR().isEmpty()) {
                DtoReincorporacion.EstudianteR er = rol.getEstudianteR().get(rol.getEstudianteR().size() - 1);
                rol.setGrupos(ejb.getGrupos(er.getEstudiante().getCarrera()).getValor());
                llenarProceso();
            }
            if (!rol.getCalificacionesR().isEmpty()) {
                if (rol.getCalificacionesR().stream().filter(t -> Objects.equals(t.getPuedeValidar(), Boolean.FALSE)).collect(Collectors.toList()).isEmpty()) {
                    rol.setPuedeValidar(Boolean.TRUE);
                }else{
                    rol.setPuedeValidar(Boolean.FALSE);
                }
            }
            if(rol.getEncuesta().getEcontrado()){
                Integer tt=0;
                if(rol.getEncuesta().getEncuestaAspirante().getR1Lenguaindigena()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR2tipoLenguaIndigena()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR3comunidadIndigena()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR4programaBienestar()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR5ingresoMensual()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR6dependesEconomicamnete()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR7ingresoFamiliar()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR8primerEstudiar()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR9nivelMaximoEstudios()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR10numeroDependientes()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR11situacionEconomica()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR12hijoPemex()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR13utxjPrimeraOpcion()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR14examenAdmisionOU()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR15medioImpacto()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR16segundaCarrera()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR17Alergia()!= null){tt=tt+1;}
                if(rol.getEncuesta().getEncuestaAspirante().getR18padecesEnfermedad()!= null){tt=tt+1;}   
                if(rol.getEncuesta().getEncuestaAspirante().getR19tratamientoMedico()!= null){tt=tt+1;}   
                if(rol.getEncuesta().getEncuestaAspirante().getR1Lenguaindigena().equals("Si") && tt==19){
                    rol.setFinalizado(Boolean.TRUE);
                }else if(rol.getEncuesta().getEncuestaAspirante().getR1Lenguaindigena().equals("No") && tt==18){
                    rol.setFinalizado(Boolean.TRUE);
                }else{
                    rol.setFinalizado(Boolean.FALSE);
                }
            }
        }         
    }
     
    public void buscarReincorporacion(ValueChangeEvent event) {
        Estudiante li = (Estudiante) event.getNewValue();
        rol.setCalificacionesR(new ArrayList<>());
        if (li.getIdEstudiante()!= null) {
            rol.setCurpBusqueda(li.getAspirante().getIdPersona().getCurp());
            buscarRegistro();
        }
    }
        
// Registro y Actualizacion informacion 
     public void leerQR()throws IOException {
        if(rol.getFileCurp() != null){
            Persona per = efa.leerCurp(rol.getFileCurp());
            ResultadoEJB<DtoReincorporacion.PersonaR> resAcceso = ejb.getPersonaR(per.getCurp()); 
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}
            rol.setPersonaD(resAcceso.getValor());
            if (rol.getPersonaD().getEcontrado()) {
                rol.setCurpBusqueda(per.getCurp());
                buscarRegistro();
                return;
            }
            rol.getPersonaD().setPersona(per);
            if (rol.getPersonaD().getPersona().getNombre() != null) {
                if (rol.getPersonaD().getPersona().getEstado() == null) {
                    rol.setExtran(Boolean.TRUE);
                } else if (rol.getPersonaD().getPersona().getEstado() <= 32) {
                    ResultadoEJB<Pais> pais = ejb.getPaisSeleccionado(rol.getPersonaD().getPersona().getEstado());
                    rol.getPersonaD().setPaisOr(pais.getValor());
                    estadosPorPaisOrigen();
                    municipiosPorEstadoOrigen();
                    rol.setExtran(Boolean.FALSE);
                }
            } else {
                Messages.addGlobalError("El documento es incorrecto !");
            }
        } else {
            Messages.addGlobalError("Es necesario seleccionar un archivo !");
        }
    }

    public void registrarDatosCurp() {
        ResultadoEJB<DtoReincorporacion.PersonaR> rejb =ejb.operacionesPersonaR(rol.getPersonaD());
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        rol.setPersonaD(rejb.getValor());
        ResultadoEJB<DtoReincorporacion.AspiranteR> resjb =ejb.operacionesAspiranteR(rol.getAspirante(),rol.getPersonaD().getPersona());
        if(!resjb.getCorrecto()){ mostrarMensajeResultadoEJB(resjb);return;}
        rol.setAspirante(resjb.getValor());
        rol.setPaso(1);
    }

    public void guardaDatosMedicos() {
         ResultadoEJB<DtoReincorporacion.MedicosR> rejb =ejb.operacionesMedicosR(rol.getDmedico(), rol.getPersonaD().getPersona());
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        rol.setDmedico(rejb.getValor());
        
        ResultadoEJB<DtoReincorporacion.PersonaR> resjb =ejb.operacionesPersonaR(rol.getPersonaD());
        if(!resjb.getCorrecto()){ mostrarMensajeResultadoEJB(resjb);return;}
        rol.setPersonaD(resjb.getValor());
        rol.setPaso(2);
    }

    public void guardaDomicilio() {
         ResultadoEJB<DtoReincorporacion.DomicilioR> rejb =ejb.operacionesDomicilioR(rol.getDdomicilios(), rol.getAspirante().getAspirante());
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        rol.setDdomicilios(rejb.getValor());
        rol.setPaso(3);
    }

    public void guardaDatosFamiliares() {
         ResultadoEJB<DtoReincorporacion.TutorR> rejb =ejb.operacionesTutorR(rol.getTutor());
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        rol.setTutor(rejb.getValor());
        rol.getDfamiliares().setTutorR(rol.getTutor());
        ResultadoEJB<DtoReincorporacion.FamiliaresR> resjb =ejb.operacionesFamiliaresR(rol.getDfamiliares(), rol.getAspirante().getAspirante());
        if(!resjb.getCorrecto()){ mostrarMensajeResultadoEJB(resjb);return;}
        rol.setDfamiliares(resjb.getValor());
        rol.setPaso(4);
    }

    public void guardaDatosAcademicos() {
        ResultadoEJB<DtoReincorporacion.AcademicosR> rejb =ejb.operacionesAcademicosR(rol.getDacademicos(), rol.getAspirante().getAspirante());
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        rol.setDacademicos(rejb.getValor());
        rol.setPaso(5);
    }
    
    public void guardaEncuesta(ValueChangeEvent event) {
        Integer numeroP=0;
        String valor="";    
        switch (event.getComponent().getId()) {
            case "p1": numeroP=1; valor=event.getNewValue().toString();  break;
            case "li": numeroP=2; 
            LenguaIndigena li=(LenguaIndigena)event.getNewValue();
            if(li.getIdLenguaIndigena()!=null){
                 valor=li.getIdLenguaIndigena().toString();
            }          
            break;
            case "p3": numeroP=3; valor=event.getNewValue().toString(); break;
            case "p4": numeroP=4; valor=event.getNewValue().toString(); break;
            case "p5": numeroP=5; valor=event.getNewValue().toString(); break;
            case "p6": numeroP=6; valor=event.getNewValue().toString(); break;
            case "p7": numeroP=7; valor=event.getNewValue().toString(); break;
            case "p8": numeroP=8; valor=event.getNewValue().toString(); break;
            case "p9": numeroP=9; valor=event.getNewValue().toString(); break;
            case "p10": numeroP=10; valor=event.getNewValue().toString();  break;
            case "p11": numeroP=11; valor=event.getNewValue().toString();  break;
            case "p12": numeroP=12; valor=event.getNewValue().toString();  break;
            case "p13": numeroP=13; valor=event.getNewValue().toString();  break;
            case "p14": numeroP=14; valor=event.getNewValue().toString();  break;
            case "md": numeroP=15; 
            MedioDifusion md=(MedioDifusion)event.getNewValue();
            if(md.getIdMedioDifusion()!=null){
                 valor=md.getIdMedioDifusion().toString();
            }  
            break;
            case "p16": numeroP=16; valor=event.getNewValue().toString();  break;
            case "p17": numeroP=17; valor=event.getNewValue().toString();  break;
            case "p18": numeroP=18; valor=event.getNewValue().toString();  break;
            case "p19": numeroP=19; valor=event.getNewValue().toString();  break;
        }      
        ResultadoEJB<DtoReincorporacion.EncuestaR> rejb =ejb.operacionesEncuestaR(rol.getAspirante().getAspirante(),valor,numeroP);
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        rol.setEncuesta(rejb.getValor());
        
        if(rol.getEncuesta().getEcontrado()){
            Integer tt=0;
            if(rol.getEncuesta().getEncuestaAspirante().getR1Lenguaindigena()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR2tipoLenguaIndigena()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR3comunidadIndigena()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR4programaBienestar()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR5ingresoMensual()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR6dependesEconomicamnete()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR7ingresoFamiliar()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR8primerEstudiar()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR9nivelMaximoEstudios()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR10numeroDependientes()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR11situacionEconomica()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR12hijoPemex()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR13utxjPrimeraOpcion()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR14examenAdmisionOU()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR15medioImpacto()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR16segundaCarrera()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR17Alergia()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR18padecesEnfermedad()!= null){tt=tt+1;}   
            if(rol.getEncuesta().getEncuestaAspirante().getR19tratamientoMedico()!= null){tt=tt+1;}   
            if(rol.getEncuesta().getEncuestaAspirante().getR1Lenguaindigena().equals("Si") && tt==19){
                rol.setFinalizado(Boolean.TRUE);
            }else if(rol.getEncuesta().getEncuestaAspirante().getR1Lenguaindigena().equals("No") && tt==18){
                rol.setFinalizado(Boolean.TRUE);
            }else{
                rol.setFinalizado(Boolean.FALSE);
            }
        }
        rol.setPaso(6);
    }
    
    public void guardaEstudiante() {   
        rol.getRein().setTrabajadorInscribe(rol.getPersonalActivoSe().getPersonal().getClave());            
        prepararCoreo(null);
        ResultadoEJB<DtoReincorporacion.ProcesoInscripcionRein> rejb =ejb.operacionesEstudianteR(rol.getRein(),rol.getAspirante().getAspirante(),rol.getEventoActivo());
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        rol.setRein(rejb.getValor());
        
        ResultadoEJB<List<DtoReincorporacion.AlineacionCalificaciones>> eJB =ejb.getAlineacionCalificaciones(rol.getAspirante().getAspirante(),rol.getEsEscolares());
        if(!eJB.getCorrecto()){ mostrarMensajeResultadoEJB(eJB);return;}
        rol.setCalificacionesR(eJB.getValor());
        buscarRegistro();
        rol.setPaso(7);
    }

    public void actualizarUltimoGrupo(RowEditEvent event) {
        DtoReincorporacion.EstudianteR er = (DtoReincorporacion.EstudianteR) event.getObject();
        er.getEstudiante().setTipoRegistro(rol.getNombreR());
        er.setOperacion(Operacion.ACTUALIZAR);        
        prepararCoreo(er);
        ResultadoEJB<DtoReincorporacion.EstudianteR> rejb =ejb.operacionesEstudianteRegistrados(er);
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        buscarRegistro();
        rol.setPaso(7);        
    }
    
    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("¡Operación cancelada!!");
    }
    
    public void guardaCalificaciones(ValueChangeEvent event) {
        String idC=event.getComponent().getId();
        String[] parts=idC.split("-");
        ResultadoEJB<CalificacionPromedio> rejb =ejb.registrarCalificacionesPorPromedio(Integer.parseInt(parts[2]),Integer.parseInt(parts[1]),Double.parseDouble(event.getNewValue().toString()),rol.getTipoCal());
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        buscarRegistro();
        rol.setPaso(8);
    }
    
    public void guardaCalificacionesValidadas() {
        if (!rol.getCalificacionesR().isEmpty()) {
            rol.getCalificacionesR().forEach((t) -> {
                if (!t.getCalificacionesReincorporacions().isEmpty()) {
                    t.getCalificacionesReincorporacions().forEach((ca) -> {
                        ResultadoEJB<CalificacionPromedio> rejb = ejb.registrarCalificacionesPorPromedio(ca.getCalificacionPromedio().getCalificacionPromedioPK().getIdEstudiante(), ca.getCalificacionPromedio().getCalificacionPromedioPK().getCarga(), ca.getCalificacionPromedio().getValor(), "Oficial");
                    });
                }
            });
        } 
        buscarRegistro();
        rol.setPaso(8);
    }
  
    public void prepararCoreo(DtoReincorporacion.EstudianteR er) {
        String titulo = "Movimientos Escolares";        
        String correoD="",correoO="";
        rol.setMensaje("Me permito por este medio enviarle un cordial saludo, al mismo tiempo, informarle que se ha realizado una reincorporación del tipo: "
                + rol.getNombreR()
                + " del estudiante \n"
                + rol.getPersonaD().getPersona().getApellidoPaterno()
                + " "
                + rol.getPersonaD().getPersona().getApellidoMaterno()
                + " "
                + rol.getPersonaD().getPersona().getNombre()
                + " con matricula: "
                + rol.getRein().getMatricula());
        String mEscolares = rol.getMensaje();
        switch (rol.getNombreR()) {
            case "Cambio de grupo":
                rol.setMensaje(rol.getMensaje()
                        + " del grupo "
                        + er.getEstudiante().getGrupo().getGrado()
                        + "° "
                        + er.getEstudiante().getGrupo().getLiteral()
                        + " modalidad "
                        + er.getEstudiante().getGrupo().getIdSistema().getNombre()
                        + " al grupo "
                        + er.getGrupo().getGrado()
                        + "° "
                        + er.getGrupo().getLiteral()
                        + " modalidad "
                        + er.getGrupo().getIdSistema().getNombre()
                        + " del programa educativo ");

                if (rol.getRein().getOpcionIncripcion()) {
                    rol.setMensaje(rol.getMensaje()
                            + rol.getRein().getPrimeraOp());
                    correoD = rol.getDacademicos().getUniversidad1().getCorreoInstitucional();
                } else {
                    rol.setMensaje(rol.getMensaje()
                            + rol.getRein().getSegundaOP());
                    correoD = rol.getDacademicos().getUniversidad2().getCorreoInstitucional();
                }
                if (!rol.getCalificacionesR().isEmpty()) {
                    if (!rol.getCalificacionesR().stream().filter(t -> t.getCalificacionesReincorporacions().isEmpty() == false).collect(Collectors.toList()).isEmpty()) {
                        rol.setMensaje(rol.getMensaje() + " el cual ya contaba con las siguientes calificaciones registradas \n");
                    }
                    rol.getCalificacionesR().forEach((t) -> {
                        if (Objects.equals(er.getEstudiante().getGrupo().getIdGrupo(), t.getGrupo().getIdGrupo())) {
                            if (!t.getCalificacionesReincorporacions().isEmpty()) {
                                rol.setMensaje(rol.getMensaje() + " del grupo " + t.getGrupo().getGrado() + "° " + t.getGrupo().getLiteral() + "\n");
                                t.getCalificacionesReincorporacions().forEach((ca) -> {
                                    rol.setMensaje(rol.getMensaje()
                                            + ca.getEstudioMateria().getClaveMateria()
                                            + "--"
                                            + ca.getMateria().getNombre()
                                            + " -- Promedio -- "
                                            + ca.getCalificacionPromedio().getValor()
                                            + "\n");
                                });
                            }
                        }
                    });
                }
                ejb.enviarConfirmacionCorreoElectronico(correoD, titulo, rol.getNombreR(), rol.getMensaje());//coreo para el area academica correspondiente
                break;
            case "Cambio de carrera":
                rol.setMensaje(rol.getMensaje() + " del programa educativo ");
                if (!rol.getRein().getOpcionIncripcion()) {
                    rol.setMensaje(rol.getMensaje()
                            + rol.getRein().getPrimeraOp());
                    correoO = rol.getDacademicos().getUniversidad1().getCorreoInstitucional();
                } else {
                    rol.setMensaje(rol.getMensaje()
                            + rol.getRein().getSegundaOP());
                    correoO = rol.getDacademicos().getUniversidad2().getCorreoInstitucional();
                }
                rol.setMensaje(rol.getMensaje() + " al programa educativo ");
                if (rol.getRein().getOpcionIncripcion()) {
                    rol.setMensaje(rol.getMensaje()
                            + rol.getRein().getPrimeraOp());
                    correoD = rol.getDacademicos().getUniversidad1().getCorreoInstitucional();
                } else {
                    rol.setMensaje(rol.getMensaje()
                            + rol.getRein().getSegundaOP());
                    correoD = rol.getDacademicos().getUniversidad2().getCorreoInstitucional();
                }
                if (!Objects.equals(rol.getDacademicos().getUniversidad1().getArea(), rol.getDacademicos().getUniversidad2().getArea())) {
                    rol.setNombreR("Cambio de programa educativo");
                    ejb.enviarConfirmacionCorreoElectronico(correoO, titulo, rol.getNombreR(), rol.getMensaje());//coreo para el area academica orijen 
                }else{
                    rol.setNombreR("Cambio de plan de estudio");
                }
                if (!rol.getCalificacionesR().isEmpty()) {
                    if (!rol.getCalificacionesR().stream().filter(t -> t.getCalificacionesReincorporacions().isEmpty()==false).collect(Collectors.toList()).isEmpty()) {
                        rol.setMensaje(rol.getMensaje() + " el cual ya contaba con las siguientes calificaciones registradas \n");
                    }
                    rol.getCalificacionesR().forEach((t) -> {
                        if (!t.getCalificacionesReincorporacions().isEmpty()) {
                            rol.setMensaje(rol.getMensaje() + " del grupo " + t.getGrupo().getGrado() + "° " + t.getGrupo().getLiteral() + "\n");
                            t.getCalificacionesReincorporacions().forEach((ca) -> {
                                rol.setMensaje(rol.getMensaje()
                                        + ca.getEstudioMateria().getClaveMateria()
                                        + "--"
                                        + ca.getMateria().getNombre()
                                        + " -- Promedio -- "
                                        + ca.getCalificacionPromedio().getValor()
                                        + "\n");
                            });
                        }
                    });
                }
                rol.setMensaje(rol.getMensaje() + " por lo que se le solicita realizar el registro correspondiente de calificaciones, notificando al Departamento de Servicios Escolares la finalizacion del proceso.");
                ejb.enviarConfirmacionCorreoElectronico(correoD, titulo, rol.getNombreR(), rol.getMensaje());//coreo para el area academica destino

                break;
            case "Reincorporación otra UT":
                if (rol.getRein().getOpcionIncripcion()) {
                    correoD = rol.getDacademicos().getUniversidad1().getCorreoInstitucional();
                } else {
                    correoD = rol.getDacademicos().getUniversidad2().getCorreoInstitucional();
                }
                rol.setMensaje(rol.getMensaje()
                        + " por lo que se le pide estar atento al proceso hasta que se halla culminado la catura de calificaciones por parte del departamento de Servicios Escolares");
                ejb.enviarConfirmacionCorreoElectronico(correoD, titulo, rol.getNombreR(), rol.getMensaje());//coreo para el area academica destino

                break;
            case "Equivalencia":
                if (rol.getRein().getOpcionIncripcion()) {
                    correoD = rol.getDacademicos().getUniversidad1().getCorreoInstitucional();
                } else {
                    correoD = rol.getDacademicos().getUniversidad2().getCorreoInstitucional();
                }
                rol.setMensaje(rol.getMensaje()
                        + " por lo que se le pide registrar la calificacion correspondiente a cada matera ");

                ejb.enviarConfirmacionCorreoElectronico(correoD, titulo, rol.getNombreR(), rol.getMensaje());//coreo para el area academica destino
                break;
            case "Reincorporación otra generación":
                if (rol.getRein().getOpcionIncripcion()) {
                    correoD = rol.getDacademicos().getUniversidad1().getCorreoInstitucional();
                } else {
                    correoD = rol.getDacademicos().getUniversidad2().getCorreoInstitucional();
                }
                rol.setMensaje(rol.getMensaje()
                        + " por lo que se le pide estar atento al proceso hasta que se halla culminado la catura de calificaciones por parte del departamento de Servicios Escolares");
                ejb.enviarConfirmacionCorreoElectronico(correoD, titulo, rol.getNombreR(), rol.getMensaje());//coreo para el area academica destino
               break;
        }
        rol.getRein().setTipoRegistro(rol.getNombreR());
        ejb.enviarConfirmacionCorreoElectronico("servicios.escolares@utxicotepec.edu.mx", titulo, rol.getNombreR(), mEscolares);// correo para el departamento de servicios escolares
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.ReincorporacionServiciosEscolares1.prepararCoreo()" + rol.getMensaje());
//        System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.ReincorporacionServiciosEscolares.prepararCoreo()"+mEscolares);
    }

    public void imprimirValores() {

    }
}
