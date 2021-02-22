package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReincorporacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroDelEstudianteRolEstudiante;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPerfilEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;


@Named
@ViewScoped
public class RegistrolDelEstudiante extends ViewScopedRol implements Desarrollable {
    
    @Getter @Setter RegistroDelEstudianteRolEstudiante rol= new RegistroDelEstudianteRolEstudiante();
    @Getter Boolean tieneAcceso = false;
    @Getter private Boolean cargado = false;
    
    @EJB EjbReincorporacion ejb;
    @EJB EjbValidacionRol evr;
    @EJB EjbFichaAdmision efa;
    @EJB EjbPropiedades ep;
    @EJB    EjbCarga carga;
    @EJB EjbPerfilEstudiante ejbPerfilEstudiante;
    @Inject LogonMB logonMB;

@PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_DEL_ESTUDIANTE);
            ResultadoEJB<Estudiante> resAcceso = ejbPerfilEstudiante.validaEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
            ResultadoEJB<Estudiante> resValidacion = ejbPerfilEstudiante.validaEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
            Estudiante estudiante = resValidacion.getValor();
            tieneAcceso = rol.tieneAccesoEs(estudiante,UsuarioTipo.ESTUDIANTE19);
            tieneAcceso = Boolean.TRUE;
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            //----------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.OPERATIVO);
            
            rol.setComunicacion(new MedioComunicacion());
            rol.setPersonaD(new DtoReincorporacion.PersonaR(new Persona(), new MedioComunicacion(), new Pais(), Operacion.PERSISTIR, Operacion.PERSISTIR, Boolean.FALSE));
            rol.setAspirante(new DtoReincorporacion.AspiranteR(new Aspirante(), new TipoAspirante(), new ProcesosInscripcion(), Operacion.PERSISTIR, Boolean.FALSE));
            rol.setDmedico(new DtoReincorporacion.MedicosR(new DatosMedicos(), new TipoSangre(), new TipoDiscapacidad(), Operacion.PERSISTIR, Boolean.FALSE));
            rol.setTutor(new DtoReincorporacion.TutorR(new TutorFamiliar(), new Ocupacion(), new Escolaridad(),Operacion.PERSISTIR, Boolean.FALSE));
            rol.setDfamiliares(new DtoReincorporacion.FamiliaresR(new DatosFamiliares(), rol.getTutor(), new Ocupacion(), new Ocupacion(), new Escolaridad(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE));
            rol.setDdomicilios(new DtoReincorporacion.DomicilioR(new Domicilio(), Boolean.FALSE, Operacion.PERSISTIR, Boolean.FALSE));
            rol.setDacademicos(new DtoReincorporacion.AcademicosR(new DatosAcademicos(), new AreasUniversidad(), new AreasUniversidad(), new Sistema(), new Sistema(), new Estado(), new Municipio(), new Localidad(), new Iems(), new EspecialidadCentro(), Operacion.PERSISTIR, Boolean.FALSE));
            rol.setEncuesta(new DtoReincorporacion.EncuestaR(new EncuestaAspirante(), new LenguaIndigena(), new MedioDifusion(), Operacion.PERSISTIR, Boolean.FALSE));
            rol.setEncuestaVocacional(new DtoReincorporacion.VocacionalR(new EncuestaVocacional(), new AreasUniversidad(), Operacion.PERSISTIR, Boolean.FALSE));
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
            inicializarValoresEscolares();
            buscarRegistro();
            buscarRegistroEstudiante();
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
        rol.setEstadosPa(ejb.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setEstadosMa(ejb.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setEstadosFa(ejb.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setEstadosCe(ejb.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setEstadosDl(ejb.getEstado(new Pais(42, "Mexico")).getValor());
        rol.setAreasAcademicasPo(ejb.getAreasUniversidad().getValor());
        rol.setAreasAcademicasSo(ejb.getAreasUniversidad().getValor());
        rol.setTipoEstudiantes(ejb.getTiposEstudiante().getValor());
        rol.setComunicacion(new MedioComunicacion());
        rol.setPersonaD(new DtoReincorporacion.PersonaR(new Persona(), new MedioComunicacion(), new Pais(), Operacion.PERSISTIR, Operacion.PERSISTIR, Boolean.FALSE));
        rol.setAspirante(new DtoReincorporacion.AspiranteR(new Aspirante(), new TipoAspirante(), new ProcesosInscripcion(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDmedico(new DtoReincorporacion.MedicosR(new DatosMedicos(), new TipoSangre(), new TipoDiscapacidad(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setTutor(new DtoReincorporacion.TutorR(new TutorFamiliar(), new Ocupacion(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDfamiliares(new DtoReincorporacion.FamiliaresR(new DatosFamiliares(), rol.getTutor(), new Ocupacion(), new Ocupacion(), new Escolaridad(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDdomicilios(new DtoReincorporacion.DomicilioR(new Domicilio(), Boolean.FALSE, Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDacademicos(new DtoReincorporacion.AcademicosR(new DatosAcademicos(), new AreasUniversidad(), new AreasUniversidad(), new Sistema(), new Sistema(), new Estado(), new Municipio(), new Localidad(), new Iems(), new EspecialidadCentro(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setEncuesta(new DtoReincorporacion.EncuestaR(new EncuestaAspirante(), new LenguaIndigena(), new MedioDifusion(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setEncuestaVocacional(new DtoReincorporacion.VocacionalR(new EncuestaVocacional(), new AreasUniversidad(), Operacion.PERSISTIR, Boolean.FALSE));
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
        llenarCatalogos();
    } 
    
    public void llenarCatalogos() {
        rol.setLateralidades(new ArrayList<>());
        rol.getLateralidades().add("Zurdo");        
        rol.getLateralidades().add("Diestro");
        rol.setParentescos(new ArrayList<>());
        rol.getParentescos().add("CONYUGE");
        rol.getParentescos().add("HERMANO/A");        
        rol.getParentescos().add("HIJO/A"); 
        rol.setPrioridades(new ArrayList<>());
        rol.getPrioridades().add("Muy Alta");  
        rol.getPrioridades().add("Alta");   
        rol.getPrioridades().add("Media");   
        rol.getPrioridades().add("Baja");   
        rol.getPrioridades().add("Muy Baja");   
        rol.setRecursos(new ArrayList<>());
        rol.getRecursos().add("Óptimos");  
        rol.getRecursos().add("Suficientes");   
        rol.getRecursos().add("Insuficientes");          
        rol.setViviendas(new ArrayList<>());
        rol.getViviendas().add("Propia");  
        rol.getViviendas().add("Rentada");   
        rol.getViviendas().add("Prestada");          
        rol.setTransportes(new ArrayList<>());
        rol.getTransportes().add("Público");  
        rol.getTransportes().add("Propio");        
    }

// Validaciones Acceso
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registroEstudiante";
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

    // Padre

    public void municipiosPorEstadoPadre() {
        rol.setMunicipiosPa(ejb.getMunicipio(rol.getDfamiliares().getDatosFamiliares().getEstadoPadre()).getValor());
    }

    public void asentamientosPorMunicipioPadre() {
        rol.setAsentamientosPa(ejb.getAsentamiento(rol.getDfamiliares().getDatosFamiliares().getEstadoPadre(), rol.getDfamiliares().getDatosFamiliares().getMunicipioPadre()).getValor());
    }
    // Madre

    public void municipiosPorEstadoMadre() {
        rol.setMunicipiosMa(ejb.getMunicipio(rol.getDfamiliares().getDatosFamiliares().getEstadoMadre()).getValor());
    }

    public void asentamientosPorMunicipioMadre() {
        rol.setAsentamientosMa(ejb.getAsentamiento(rol.getDfamiliares().getDatosFamiliares().getEstadoMadre(), rol.getDfamiliares().getDatosFamiliares().getMunicipioMadre()).getValor());
    }
    // Familiar

    public void municipiosPorEstadoFamiliar() {
        rol.setMunicipiosFa(ejb.getMunicipio(rol.getFamiliaR().getFamilia().getEstado()).getValor());
    }

    public void asentamientosPorMunicipioFamiliar() {
        rol.setAsentamientosFa(ejb.getAsentamiento(rol.getFamiliaR().getFamilia().getEstado(), rol.getFamiliaR().getFamilia().getMunicipio()).getValor());
    }
    // Familiar

    public void municipiosPorEstadoContactoEmergencias() {
        rol.setMunicipiosCe(ejb.getMunicipio(rol.getRcontactoEmergencia().getEmergenciasEstudiante().getEstado()).getValor());
    }

    public void asentamientosPorMunicipioContactoEmergencias() {
        rol.setAsentamientosCe(ejb.getAsentamiento(rol.getRcontactoEmergencia().getEmergenciasEstudiante().getEstado(), rol.getRcontactoEmergencia().getEmergenciasEstudiante().getMunicipio()).getValor());
    }
    // Laborales

    public void municipiosPorEstadoDatosLaborales() {
        rol.setMunicipiosDl(ejb.getMunicipio(rol.getRegDatosLaborales().getDatosLaborales().getEstado()).getValor());
    }

    public void asentamientosPorMunicipioDatosLaborales() {
        rol.setAsentamientosDl(ejb.getAsentamiento(rol.getRegDatosLaborales().getDatosLaborales().getEstado(), rol.getRegDatosLaborales().getDatosLaborales().getMunicipio()).getValor());
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
    
    public void viviendadT() {
        if (rol.getSosioeconomicosR().getSocioeconomicos().getVivienda().equals("Rentada")) {
            rol.getSosioeconomicosR().getSocioeconomicos().setRentaViviendaActual(Boolean.TRUE);
        } else {
            rol.getSosioeconomicosR().getSocioeconomicos().setRentaViviendaActual(Boolean.FALSE);
        }

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
        rol.setGrupos(ejb.getGruposCarrera(carrera).getValor());
    }
    

// Busquedas registros
    public void buscarRegistro(){
        inicializarValoresEscolares();
        ResultadoEJB<DtoReincorporacion.General> resAcceso = ejb.getDtoReincorporacion(rol.getCurpBusqueda(),Boolean.TRUE); 
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
            
            if(rol.getDfamiliares().getEcontrado()){
                municipiosPorEstadoPadre();
                asentamientosPorMunicipioPadre();
                municipiosPorEstadoMadre();
                asentamientosPorMunicipioMadre();
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
                rol.setGrupos(ejb.getGruposCarrera(er.getEstudiante().getCarrera()).getValor());
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
    
    public void buscarRegistroEstudiante(){
        ResultadoEJB<DtoReincorporacion.RegistroEstudiante> resAcceso = ejb.getDtoRegistroEstudiante(rol.getAspirante().getAspirante(),rol.getPersonaD().getPersona()); 
        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}
        rol.setRegistroEstudiante(resAcceso.getValor());        
        if(rol.getRegistroEstudiante().getEcontrado()){
            rol.setEmergencias(rol.getRegistroEstudiante().getRes());
            rol.setFamilias(rol.getRegistroEstudiante().getFs());  
            rol.setRegistrDelEstudianteR(rol.getRegistroEstudiante().getEsact());
            rol.setAcademicosCR(rol.getRegistroEstudiante().getDac());
            rol.setRegDatosLaborales(rol.getRegistroEstudiante().getLabo());
            rol.setSosioeconomicosR(rol.getRegistroEstudiante().getSr());
            rol.setEncuestaVocacional(rol.getRegistroEstudiante().getVr());
        } 
        
        if (rol.getRegDatosLaborales().getEcontrado()) {
            municipiosPorEstadoDatosLaborales();
            asentamientosPorMunicipioDatosLaborales();
            rol.setRespuestaTrabaja(rol.getRegDatosLaborales().getDatosLaborales().getTrabajoActual());
        }
    }
     
    public void buscarReincorporacion(ValueChangeEvent event) {
        Estudiante li = (Estudiante) event.getNewValue();
        rol.setCalificacionesR(new ArrayList<>());
        if (li.getIdEstudiante()!= null) {
            rol.setCurpBusqueda(li.getAspirante().getIdPersona().getCurp());
            buscarRegistro();
            buscarRegistroEstudiante();
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
            buscarRegistroEstudiante();
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
    
    public void guardaLaborales() {
        rol.getRegDatosLaborales().getDatosLaborales().setTrabajoActual(rol.getRespuestaTrabaja());
        ResultadoEJB<DtoReincorporacion.RegDatosLaborales> rejb =ejb.operacionesLaborales(rol.getRegDatosLaborales(), rol.getAspirante().getAspirante());
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        rol.setRegDatosLaborales(rejb.getValor());
        rol.setPaso(5);
    }
    
    public void guardaSocioeconomicosAcademicos() {
        ResultadoEJB<DtoReincorporacion.SosioeconomicosR> rejbS =ejb.operacionesSocioeconomicos(rol.getSosioeconomicosR(), rol.getAspirante().getAspirante());
        if(!rejbS.getCorrecto()){ mostrarMensajeResultadoEJB(rejbS);return;}
        rol.setSosioeconomicosR(rejbS.getValor());        
        ResultadoEJB<DtoReincorporacion.AcademicosCR> rejbA =ejb.operacionesAcademicosConplementarios(rol.getAcademicosCR(), rol.getAspirante().getAspirante());
        if(!rejbA.getCorrecto()){ mostrarMensajeResultadoEJB(rejbA);return;}
        rol.setAcademicosCR(rejbA.getValor());
        rol.setPaso(5);
    }

    public void guardaDatosFamiliares() {
         ResultadoEJB<DtoReincorporacion.TutorR> rejb =ejb.operacionesTutorR(rol.getTutor());
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        rol.setTutor(rejb.getValor());
        rol.getDfamiliares().setTutorR(rol.getTutor());
        ResultadoEJB<DtoReincorporacion.FamiliaresR> resjb =ejb.operacionesFamiliaresR(rol.getDfamiliares(), rol.getAspirante().getAspirante());
        if(!resjb.getCorrecto()){ mostrarMensajeResultadoEJB(resjb);return;}
        rol.setDfamiliares(resjb.getValor());
        rol.setPaso(3);        
        rol.setSubpaso(1);
    }
    
    public void guardaDatosIntegrantesDeFamilia() {
        ResultadoEJB<DtoReincorporacion.Familia> resjb =ejb.operacionesFamiliaR(rol.getFamiliaR(), rol.getAspirante().getAspirante());
        if(!resjb.getCorrecto()){ mostrarMensajeResultadoEJB(resjb);return;}
        rol.setFamiliaR(new DtoReincorporacion.Familia(new IntegrantesFamilia(), new Ocupacion(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setPaso(3);
        rol.setSubpaso(3);
        buscarRegistroEstudiante();
    }
    
    public void guardaDatosContactoemergencias() {
        ResultadoEJB<DtoReincorporacion.RcontactoEmergencia> resjb =ejb.operacionesContactoEmR(rol.getRcontactoEmergencia(), rol.getAspirante().getAspirante());
        if(!resjb.getCorrecto()){ mostrarMensajeResultadoEJB(resjb);return;}
        rol.setRcontactoEmergencia(new DtoReincorporacion.RcontactoEmergencia(new ContactoEmergenciasEstudiante(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setPaso(3);
        rol.setSubpaso(4);
        buscarRegistroEstudiante();
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
    
    public void guardaEncuestaVocacional(ValueChangeEvent event) {
        Integer numeroP=0;
        String valor="";    
        switch (event.getComponent().getId()) {
            case "pv1": numeroP=1; valor=event.getNewValue().toString();  break;  
            case "pv2": numeroP=2; valor=event.getNewValue().toString(); break;
            case "pv5": numeroP=5; valor=event.getNewValue().toString(); break;
        }              
        ResultadoEJB<DtoReincorporacion.VocacionalR> rejb =ejb.operacionesEncuestaVocacional(rol.getPersonaD().getPersona(),valor,numeroP);
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        rol.setEncuestaVocacional(rejb.getValor());
        rol.setPaso(6);
    }
    
    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("¡Operación cancelada!!");
    }
 
    public void imprimirValores() {

    }
    
    public void agregarCroquis() {
        if (rol.getCroquis() != null) {
            if (!rol.getEvidencia().equals("")) {
                String ruta = carga.subirEvidenciaPOA(rol.getCroquis(), new File(String.valueOf("control_escolar").concat(File.separator).concat(rol.getDdomicilios().getDomicilio().getAspirante().toString()).concat(File.separator).concat("Domicilio").concat(File.separator).concat(rol.getEvidencia()).concat(File.separator)));
                switch (rol.getEvidencia()) {
                    case "Procedencia":
                        rol.getDdomicilios().getDomicilio().setCroquisParticular(ruta);
                        break;
                    case "Recidencia":
                        rol.getDdomicilios().getDomicilio().setCroquisRenta(ruta);
                        break;
                }
                guardaDomicilio();
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
        }
        

    }
}
