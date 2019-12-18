package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ReincorporacionRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSelectItemCE;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Objects;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



@Named
@ViewScoped
public class ReincorporacionServiciosEscolares extends ViewScopedRol implements Desarrollable {
    @Getter @Setter ReincorporacionRolServiciosEscolares rol;
    @EJB EjbReincorporacion ejb;
    @EJB EjbPropiedades ep;
    @EJB EJBSelectItems ejbSI;
    @EJB EjbSelectItemCE ejbSICE;
    @EJB EjbFichaAdmision ejbFA;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;

    


@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
        try {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.REINCORPORACION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarServiciosEscolares(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarServiciosEscolares(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo serviciosEscolares = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ReincorporacionRolServiciosEscolares(filtro, serviciosEscolares, serviciosEscolares.getAreaOperativa());
            tieneAcceso = rol.tieneAcceso(serviciosEscolares);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setServiciosEscolares(serviciosEscolares);
            ResultadoEJB<EventoEscolar> resEvento = ejb.verificarEvento();
            if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
            rol.setNivelRol(NivelRol.OPERATIVO);

            rol.setPeriodoActivo(resEvento.getValor().getPeriodo());

            rol.setEventoActivo(resEvento.getValor());

            rol.setPersona(new Persona());
            rol.setDatosMedicos(new DatosMedicos());
            rol.setMedioComunicacion(new MedioComunicacion());
            rol.setAspirante(new Aspirante());
            rol.setDomicilio(new Domicilio());
            rol.setDatosFamiliares(new DatosFamiliares());
            rol.setTutorFamiliar(new TutorFamiliar());
            rol.setDatosAcademicos(new DatosAcademicos());
            //////////////////////////////////////////Carga de la lista\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            rol.setSelectItemGeneros(ejbFA.listaGeneros());
            rol.setSelectItemTipoSangre(ejbSICE.itemTipoSangre());
            rol.setSelectItemDiscapacidades(ejbSICE.itemDiscapcidad());
            rol.setSelectItemEstadosDomicilioRadica(ejbSI.itemEstados());
            rol.setSelectItemEstadosProcedencia(ejbSI.itemEstados());
            rol.setSelectItemEstadosTutor(ejbSI.itemEstados());
            rol.setSelectItemEstadosIEMS(ejbSI.itemEstados());
            rol.setEscolaridad(ejbSICE.itemEscolaridad());
            rol.setOcupacion(ejbSICE.itemOcupacion());
            rol.setEspecialidades(ejbSICE.itemEspecialidadCentro());
            /////////////////////////////////////////Inicialización de la variable opcion para saber el tipo de accion a realizar\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            rol.setOpcion("Buscar");
            rol.setBuscar(Boolean.TRUE);
            /////////////////////////////////////////Alta de instrucciones para realizar una reincorporación
            rol.getInstrucciones().add("\u0097 DATOS PERSONALES");
            rol.getInstrucciones().add("Seleccionar el tipo de accion a realizar, es decir, 1.- Reincorporación con registro nuevo. 2.- Reincorporación con registro existente");
            rol.getInstrucciones().add("1.- Reincorporación con registro nuevo: Dar clic en el botón 'Realizar registro nuevo', se activará el apartado para subir un archivo," +
                    " correspondiente a la CURP del estudiante, el cual al instante se obtendrá datos personales como: nombre completo, curp, género, fecha de nacimiento, pais, estado.");
            rol.getInstrucciones().add("Para poder realizar el registro completo deberá llenar los campos obligatorios los cuales son: estado civil, municipio, localidad");
            rol.getInstrucciones().add("2.- Reincorporación con registro existente: Dar clic en el botón 'Buscar por CURP', se activará el apartado para realizar la búsqueda " +
                    "correspondiente, ingresando la CURP del estudiante, obteniendo la información personal, información médica y de comunicación, domicilio de residencia, datos" +
                    " familiares, datos académicos. El cual estaran disponibles para realizar actualización en caso de cambios de acuerdo a lo indicado por el estudiante");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("\u0097 DATOS MÉDICOS Y MEDIOS DE COMUNICACIÓN");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("");
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

// Validaciones Acceso
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "reincorporaciones";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }

//Busquedea datos    
    public void obtenerDatosQR() throws IOException{
        if(rol.getFile() != null){
            //System.out.println("Nombre del archivo:"+ rol.getFile().getSubmittedFileName());
            ResultadoEJB<Persona> res = ejb.leerCurp(rol.getFile());
            //System.out.println(res.getValor());
            if(res.getCorrecto()){
                mostrarMensajeResultadoEJB(res);
                rol.setPersona(res.getValor());
                if(rol.getPersona().getNombre() != null){
                    //System.out.println("Es distinto de nulo");
                    if(rol.getPersona().getIdpersona() == null){
                        //System.out.println("Es igual a nulo");
                        if(rol.getPersona().getEstado() == null){
                            rol.setSelectItemPaises(ejbSI.itemPaises());
                            clearInformacion();
                            rol.setEstado(true);
                            rol.setEstadoExt(false);
                        }else
                            if(rol.getPersona().getEstado() <= 32){
                            rol.setPaisItem(ejbSI.itemCvePais(rol.getPersona().getEstado()));
                            rol.setSelectItemMunicipios(ejbSI.itemMunicipiosByClave(rol.getPersona().getEstado()));
                            rol.setSelectItemEstados(ejbSI.itemEstados());
                            rol.setSelectItemPaises(ejbSI.itemPaisMexico());
                            clearInformacion();
                            rol.setEstado(true);
                            rol.setEstadoExt(false);
                        }
                    }else{
                        if(rol.getPersona().getEstado() > 32){
                            rol.setPaisItem(ejbSI.itemCvePais(rol.getPersona().getEstado()));
                            rol.setSelectItemPaises(ejbSI.itemPaises());
                            rol.setSelectItemEstados(ejbSI.itemEstadoByClave(rol.getPaisItem()));
                            rol.setEstado(false);
                            rol.setEstadoExt(true);
                        }else if(rol.getPersona().getEstado() <= 32){
                            rol.setPaisItem(ejbSI.itemCvePais(rol.getPersona().getEstado()));
                            rol.setSelectItemMunicipios(ejbSI.itemMunicipiosByClave(rol.getPersona().getEstado()));
                            rol.setSelectItemEstados(ejbSI.itemEstados());
                            rol.setSelectItemPaises(ejbSI.itemPaisMexico());
                            rol.setSelectItemLocalidades(ejbSI.itemLocalidadesByClave(rol.getPersona().getEstado(), rol.getPersona().getMunicipio()));
                            rol.setEstado(true);
                            rol.setEstadoExt(false);
                        }
                    }
                }else{
                    Messages.addGlobalError("El documento es incorrecto !");
                }
            }
        }else{
            Messages.addGlobalError("Es necesario seleccionar un archivo !");
        }
    }     
    
     public  void clearInformacion(){
        rol.setDatosMedicos(new DatosMedicos());
        rol.setSelectDM(new ArrayList<>());
        rol.setMedioComunicacion(new MedioComunicacion());
        rol.setDomicilio(new Domicilio());
        rol.setDatosFamiliares(new DatosFamiliares());
        rol.setTutorFamiliar(new TutorFamiliar());
        rol.setDatosAcademicos(new DatosAcademicos());
        rol.setDm(Boolean.TRUE);
        rol.setCom(Boolean.TRUE);
        rol.setDf(Boolean.TRUE);
        rol.setDa(Boolean.TRUE);
        rol.setEvif(Boolean.TRUE);
    }
    
     public void verificarRegistros(Persona persona){
        if (persona.getDatosMedicos() != null) {
            rol.setSelectDM(new ArrayList<>());
            rol.setDatosMedicos(persona.getDatosMedicos());
            rol.setMedioComunicacion(persona.getMedioComunicacion());
            if(rol.getDatosMedicos().getFDiabetes() == true){
                rol.getSelectDM().add("Dia");
            }
            if(rol.getDatosMedicos().getFHipertenso() == true){
                rol.getSelectDM().add("Hip");
            }
            if(rol.getDatosMedicos().getFCardiaco() == true){
                rol.getSelectDM().add("Car");
            }
            if(rol.getDatosMedicos().getFCancer() == true){
                rol.getSelectDM().add("Can");
            }
            rol.setDm(Boolean.FALSE);
            rol.setCom(Boolean.FALSE);

            ResultadoEJB<Aspirante> res = ejb.buscarAspirantePorPersona(persona.getIdpersona());
            if (res.getCorrecto()) {
                rol.setAspirante(res.getValor());
            } else {
                rol.setAspirante(new Aspirante());
            }
        }
        if(rol.getAspirante() != null){
            if(rol.getAspirante() != null && rol.getAspirante().getDomicilio() != null){
                rol.setDomicilio(rol.getAspirante().getDomicilio());
                selectMunicipio();
                selectAsentamiento();
                selectMunicipioProcedencia();
                selectAsentamientoProcedencia();
                rol.setCom(Boolean.FALSE);
                rol.setDf(Boolean.FALSE);
            }
        }
        if(rol.getAspirante().getDatosFamiliares() != null) {
            rol.setDatosFamiliares(rol.getAspirante().getDatosFamiliares());
            rol.setTutorFamiliar(rol.getDatosFamiliares().getTutor());
            selectMunicipioTutor();
            selectAsentamientoTutor();
            rol.setDf(Boolean.FALSE);
            rol.setDa(Boolean.FALSE);

        }
        if(rol.getAspirante().getDatosAcademicos() != null){
            rol.setDatosAcademicos(rol.getAspirante().getDatosAcademicos());
            Iems iems = new Iems();
            AreasUniversidad p1 = new AreasUniversidad();
            AreasUniversidad p2 = new AreasUniversidad();
            iems = ejb.buscaIemsByClave(rol.getDatosAcademicos().getInstitucionAcademica());
            rol.setEstadoItem(iems.getLocalidad().getLocalidadPK().getClaveEstado());
            rol.setMunicipioItem(iems.getLocalidad().getLocalidadPK().getClaveMunicipio());
            rol.setLocalidadItem(iems.getLocalidad().getLocalidadPK().getClaveLocalidad());
            p1 = ejb.buscaPEByClave(rol.getDatosAcademicos().getPrimeraOpcion());
            p2 = ejb.buscaPEByClave(rol.getDatosAcademicos().getSegundaOpcion());
            rol.setAreaAcademicaPO(p1.getAreaSuperior());
            rol.setAreaAcademicaSO(p2.getAreaSuperior());
            selectMunicipioIems();
            selectLocalidadIems();
            selectIems();
            selectPEPrincipal();
            selectPEOpcional();
            rol.setResultado(ejb.getResultado(rol.getAspirante().getIdAspirante()));
            if(rol.getResultado() == null){
                rol.setResultado(new EncuestaAspirante());
                rol.setFinalizado(Boolean.FALSE);
            }else{
                comprobar();
            }
            rol.setDa(Boolean.FALSE);
            rol.setEvif(Boolean.FALSE);
        }
    }               
     
    public void verificarExistenciaRegistro(){
        ResultadoEJB<Persona> res = ejb.buscarDatosPersonalesEstudiante(rol.getBuscquedaCurp().toUpperCase());
        if(res.getCorrecto()){
            rol.setPersona(res.getValor());
            System.out.println("Persona:"+rol.getPersona());
            if(rol.getPersona() != null){
                rol.setPaisItem(ejbSI.itemCvePais(rol.getPersona().getEstado()));
                rol.setSelectItemMunicipios(ejbSI.itemMunicipiosByClave(rol.getPersona().getEstado()));
                rol.setSelectItemEstados(ejbSI.itemEstados());
                rol.setSelectItemPaises(ejbSI.itemPaisMexico());
                rol.setSelectItemLocalidades(ejbSI.itemLocalidadesByClave(rol.getPersona().getEstado(), rol.getPersona().getMunicipio()));
                verificarExistenciaRegistro(rol.getPersona());
                mostrarMensajeResultadoEJB(res);
                rol.setCurp("");
            }
        }else{
            rol.setCurp("");
            mostrarMensajeResultadoEJB(res);
        }

    }

    public void verificarExistenciaRegistro(Persona persona){
        rol.setPersona(persona);
        if(rol.getPersona().getDatosMedicos() != null){
            rol.setSelectDM(new ArrayList<>());
            rol.setDatosMedicos(rol.getPersona().getDatosMedicos());
            rol.setMedioComunicacion(rol.getPersona().getMedioComunicacion());
            if(rol.getDatosMedicos().getFDiabetes()){
                rol.getSelectDM().add("Dia");
            }
            if(rol.getDatosMedicos().getFCancer()){
                rol.getSelectDM().add("Can");
            }
            if(rol.getDatosMedicos().getFCardiaco()){
                rol.getSelectDM().add("Car");
            }
            if(rol.getDatosMedicos().getFHipertenso()){
                rol.getSelectDM().add("Hip");
            }
            ResultadoEJB<Aspirante> res = ejb.buscarAspirantePorPersona(rol.getPersona().getIdpersona());
            if(res.getCorrecto()){
                mostrarMensajeResultadoEJB(res);
                rol.setAspirante(res.getValor());
            }
        }
        if(rol.getAspirante() != null && rol.getAspirante().getDomicilio() != null){
            rol.setDomicilio(rol.getAspirante().getDomicilio());
            selectMunicipio();
            selectAsentamiento();
            selectMunicipioProcedencia();
            selectAsentamientoProcedencia();
        }
        assert rol.getAspirante() != null;
        if(rol.getAspirante().getDatosFamiliares() != null){
            rol.setDatosFamiliares(rol.getAspirante().getDatosFamiliares());
            rol.setTutorFamiliar(rol.getDatosFamiliares().getTutor());
            selectMunicipioTutor();
            selectAsentamientoTutor();
        }
    }    
    
    public void comprobar(){
        if(Objects.equals(rol.getResultado().getR1Lenguaindigena(), "Sí") && (rol.getResultado().getR2tipoLenguaIndigena() == null && rol.getResultado().getR3comunidadIndigena()== null)) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR4programaBienestar() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR5ingresoMensual() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR6dependesEconomicamnete() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR7ingresoFamiliar() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR8primerEstudiar() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR9nivelMaximoEstudios() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR10numeroDependientes() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR11situacionEconomica() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR12hijoPemex() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR13utxjPrimeraOpcion() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR14examenAdmisionOU() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR15medioImpacto() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR16segundaCarrera() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR17Alergia()  == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR18padecesEnfermedad() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        if(rol.getResultado().getR19tratamientoMedico() == null) {rol.setFinalizado(Boolean.FALSE); return;}
        
        rol.setMostrar(!rol.getResultado().getR1Lenguaindigena().equals("Sí"));
        rol.setFinalizado(Boolean.TRUE);
    }    
    
    public void selectIems(){
//        rol.setlistaIems = ejbSI.itemIems(rol.getEstadoItem(), rol.getMunicipioItem(), rol.getLocalidadItem());
    }
    
    public void selectPEPrincipal(){
//        rol.setlistaPEP = ejbSI.itemProgramEducativoPorArea(rol.getAreaAcademicaPO());
    }
    
    public void selectPEOpcional(){
//        listaPES = ejbSI.itemProgramEducativoPorArea(rol.getAreaAcademicaSO());       
    }
    
    public void seleccionarEstado(){
        rol.setSelectItemEstados(ejbSI.itemEstadoByClave(rol.getPaisItem()));
    }

    public void seleccionarLocalidad(){
        rol.setSelectItemLocalidades(ejbSI.itemLocalidadesByClave(rol.getPersona().getEstado(), rol.getPersona().getMunicipio()));
    }

    public void selectMunicipio(){
        rol.setSelectItemMunicipios(ejbSI.itemMunicipiosByClave(rol.getDomicilio().getIdEstado()));
    }

    public void selectAsentamiento(){
        rol.setSelectItemAsentamientos(ejbSI.itemAsentamientoByClave(rol.getDomicilio().getIdEstado(), rol.getDomicilio().getIdMunicipio()));
    }

    public void selectMunicipioProcedencia(){
        rol.setSelectItemMunicipiosProcedencia(ejbSI.itemMunicipiosByClave(rol.getDomicilio().getEstadoProcedencia()));
    }

    public void selectAsentamientoProcedencia(){
        rol.setSelectItemAsentamientosProcedencia(ejbSI.itemAsentamientoByClave(rol.getDomicilio().getEstadoProcedencia(), rol.getDomicilio().getMunicipioProcedencia()));
    }

    public void selectMunicipioTutor(){
        rol.setSelectItemMunicipios(ejbSI.itemMunicipiosByClave(rol.getTutorFamiliar().getEstado()));
    }

    public void selectMunicipioIems(){
        rol.setSelectItemMunicipiosIEMS(ejbSI.itemMunicipiosByClave(rol.getEstadoItem()));
    }

    public void selectLocalidad(){
        rol.setSelectItemLocalidades(ejbSI.itemLocalidadesByClave(rol.getPersona().getEstado(), rol.getPersona().getMunicipio()));
    }

    public void selectLocalidadIems(){
        rol.setSelectItemLocalidadesIEMS(ejbSI.itemLocalidadesByClave(rol.getEstadoItem(), rol.getPaisItem()));
    }

    public void selectAsentamientoTutor(){
        rol.setSelectItemAsentamientoTutor(ejbSI.itemAsentamientoByClave(rol.getTutorFamiliar().getEstado(), rol.getTutorFamiliar().getMunicipio()));
    }
    
    public void guardaDatosPesonales() {
        if (ejb.buscarDatosPersonalesEstudiante(rol.getPersona().getCurp()).getCorrecto()) {
            ResultadoEJB<Persona> res;
            res = ejb.guardarDatosPersonales(rol.getPersona(), Operacion.ACTUALIZAR);
            mostrarMensajeResultadoEJB(res);
            rol.setPersona(res.getValor());
            rol.setDm(Boolean.FALSE);
            rol.setIndex(1);
        } else {
            ResultadoEJB<Persona> res;
            res = ejb.guardarDatosPersonales(rol.getPersona(), Operacion.PERSISTIR);
            mostrarMensajeResultadoEJB(res);
            rol.setIndex(0);
            rol.setDm(Boolean.FALSE);
            rol.setIndex(1);
        }
    }
    
    public void guardaDatosMedicos(){
        if(rol.getDatosMedicos().getCvePersona() == null){
            rol.getSelectDM().stream()
                    .forEach(s -> {
                        switch (s) {
                            case "Dia":
                                rol.getDatosMedicos().setFDiabetes(Boolean.TRUE);
                                break;
                            case "Hip":
                                rol.getDatosMedicos().setFHipertenso(Boolean.TRUE);
                                break;
                            case "Car":
                                rol.getDatosMedicos().setFCardiaco(Boolean.TRUE);
                                break;
                            case "Can":
                                rol.getDatosMedicos().setFCancer(Boolean.TRUE);
                                break;
                        }
                    });
            rol.getDatosMedicos().setCvePersona(rol.getPersona().getIdpersona());
            ejb.guardarDatosMedicos(rol.getDatosMedicos(),rol.getPersona(),Operacion.PERSISTIR);
//            medioComunicacion.setPersona(persona.getIdpersona());
//            ejbFichaAdmision.guardaComunicacion(medioComunicacion);
//            com = false;
//            index =2;
        }else{
//            selectAM.stream()
//                    .forEach(s -> {
//                        switch (s) {
//                            case "Dia":
//                                datosMedicos.setFDiabetes(true);
//                                break;
//                            case "Hip":
//                                datosMedicos.setFHipertenso(true);
//                                break;
//                            case "Car":
//                                datosMedicos.setFCardiaco(true);
//                                break;
//                            case "Can":
//                                datosMedicos.setFCancer(true);
//                                break;
//                        }
//                    });
//            ejbFichaAdmision.actualizaDatosMedicos(datosMedicos);
//            ejbFichaAdmision.actualizaComunicacion(medioComunicacion);
//            com = false;
//            index =2;
        }
    }
    
    
    
    
    
    
    

// Metodos de Eventos
//Agregar
    public void guardarPersona() {
        ResultadoEJB<Persona> res;
        res = ejb.guardarDatosPersonales(rol.getPersona(), Operacion.PERSISTIR);
//        guardarAspirante(rol.getPersona());
        mostrarMensajeResultadoEJB(res);
    }
    public void guardarAspirante(Persona p) {
        ResultadoEJB<Aspirante> resA;
        rol.setAspirante(new Aspirante());
        rol.getAspirante().setIdPersona(new Persona());
        rol.getAspirante().setIdPersona(p);
        rol.getAspirante().setFolioAspirante(rol.getFolioAspirante());
        resA = ejb.guardarAspirantePorReincorporacion(rol.getAspirante(), rol.getPersona(), Operacion.PERSISTIR);
        mostrarMensajeResultadoEJB(resA);
    }
    
    public void ejecutarMetodos(){
        guardarDatosMedicosC();
        guardarMediosC();
        guardarDatosFamiliares();
        guardarDomicilios();
        guardarDatosAcademicos();
    }
    
    public void guardarDatosMedicosC() {
        ResultadoEJB<DatosMedicos> res;
        if (rol.getDatosMedicos().getCvePersona() == null) {
            rol.getDatosMedicos().setCvePersona(rol.getPersona().getIdpersona());
            res = ejb.guardarDatosMedicos(rol.getDatosMedicos(), rol.getPersona(), Operacion.PERSISTIR);
            mostrarMensajeResultadoEJB(res);
        } else {
            res = ejb.guardarDatosMedicos(rol.getDatosMedicos(), rol.getPersona(), Operacion.ACTUALIZAR);
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void guardarMediosC() {
//        ResultadoEJB<MedioComunicacion> res;
//        if (rol.getMedioComunicacion().getPersona() == null) {
//            rol.getMedioComunicacion().setPersona(rol.getPersona().getIdpersona());
//            res = ejb.guardarMedioComunicacion(rol.getMedioComunicacion(), rol.getPersona(), Operacion.PERSISTIR);
//            mostrarMensajeResultadoEJB(res);
//        } else {
////            res = ejb.guardarDatosMedicos(rol.getDatosMedicos(), rol.getPersona(), Operacion.ACTUALIZAR);
//            mostrarMensajeResultadoEJB(res);
//        }
    }
    
    public void guardarDatosFamiliares() {
//        ResultadoEJB<DatosFamiliares> res;
//        if (rol.getDatosFamiliares().getAspirante()== null) {
//            rol.getDatosFamiliares().setAspirante(rol.getAspirante().getFolioAspirante());
////            res = ejb.guar(rol.getDatosFamiliares(), rol.getPersona(), Operacion.PERSISTIR);
//            mostrarMensajeResultadoEJB(res);
//        } else {
////            res = ejb.guardarDatosMedicos(rol.getDatosMedicos(), rol.getPersona(), Operacion.ACTUALIZAR);
//            mostrarMensajeResultadoEJB(res);
//        }
    }
    
    public void guardarDomicilios() {
//        ResultadoEJB<DatosMedicos> res;
//        if (rol.getDatosMedicos().getCvePersona() == null) {
//            rol.getDatosMedicos().setCvePersona(rol.getPersona().getIdpersona());
//            res = ejb.guardarDatosMedicos(rol.getDatosMedicos(), rol.getPersona(), Operacion.PERSISTIR);
//            mostrarMensajeResultadoEJB(res);
//        } else {
//            res = ejb.guardarDatosMedicos(rol.getDatosMedicos(), rol.getPersona(), Operacion.ACTUALIZAR);
//            mostrarMensajeResultadoEJB(res);
//        }
    }
        
    public void guardarDatosAcademicos() {
        ResultadoEJB<DatosMedicos> res;
        if (rol.getDatosMedicos().getCvePersona() == null) {
            rol.getDatosMedicos().setCvePersona(rol.getPersona().getIdpersona());
            res = ejb.guardarDatosMedicos(rol.getDatosMedicos(), rol.getPersona(), Operacion.PERSISTIR);
            mostrarMensajeResultadoEJB(res);
        } else {
            res = ejb.guardarDatosMedicos(rol.getDatosMedicos(), rol.getPersona(), Operacion.ACTUALIZAR);
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    
    
    
//Actualizar    

    public void guardarDatosPersona(){
        ResultadoEJB<Persona> res;
        if(rol.getPersona().getIdpersona() == null){
            res = ejb.guardarDatosPersonales(rol.getPersona(), Operacion.PERSISTIR);
            guardarAspirante(rol.getPersona());
            mostrarMensajeResultadoEJB(res);
        }else {
            res = ejb.guardarDatosPersonales(rol.getPersona(), Operacion.ACTUALIZAR);
            ejecutarMetodos();
            mostrarMensajeResultadoEJB(res);
        }
    }

    public void guardarDatosMedicosPersona(){
        ResultadoEJB<DatosMedicos> res;
        if(rol.getDatosMedicos().getCvePersona() == null){
            rol.getDatosMedicos().setCvePersona(rol.getPersona().getIdpersona());
            res = ejb.guardarDatosMedicos(rol.getDatosMedicos(), rol.getPersona(), Operacion.PERSISTIR);
            mostrarMensajeResultadoEJB(res);
        }else{
            res = ejb.guardarDatosMedicos(rol.getDatosMedicos(),rol.getPersona(), Operacion.ACTUALIZAR);
            mostrarMensajeResultadoEJB(res);
        }
    }

    public void guardarMediosComunicacionPersona(){
        ResultadoEJB<MedioComunicacion> res;
        if(rol.getMedioComunicacion().getPersona() == null){
            rol.getMedioComunicacion().setPersona(rol.getPersona().getIdpersona());
            res = ejb.guardarMedioComunicacion(rol.getMedioComunicacion(),rol.getPersona(), Operacion.PERSISTIR);
            mostrarMensajeResultadoEJB(res);
        }else{
            res = ejb.guardarMedioComunicacion(rol.getMedioComunicacion(), rol.getPersona(), Operacion.ACTUALIZAR);
            mostrarMensajeResultadoEJB(res);
        }
    }

    

    public void guardaDatosResidenciayProcedencia() {
        ResultadoEJB<MedioComunicacion> res;
        if (rol.getDomicilio().getAspirante() == null) {

            rol.getDomicilio().setAspirante1(new Aspirante());
            rol.getDomicilio().setAspirante1(rol.getAspirante());

            
            
            rol.getMedioComunicacion().setPersona(rol.getPersona().getIdpersona());
            res = ejb.guardarMedioComunicacion(rol.getMedioComunicacion(), rol.getPersona(), Operacion.PERSISTIR);
            mostrarMensajeResultadoEJB(res);
        } else {
            res = ejb.guardarMedioComunicacion(rol.getMedioComunicacion(), rol.getPersona(), Operacion.ACTUALIZAR);
            mostrarMensajeResultadoEJB(res);
        }
    }

    

    public void actualizarOpciones(){
        if(rol.getOpcion().equals("Buscar")){
            rol.setBuscar(Boolean.TRUE);
            rol.setRegistrar(Boolean.FALSE);
        }
        if(rol.getOpcion().equals("Registrar")){
            rol.setRegistrar(Boolean.TRUE);
            rol.setBuscar(Boolean.FALSE);
        }
    }

    public void usarDireccionResidencia(){
        rol.getDomicilio().setCalleProcedencia(rol.getDomicilio().getCalle());
        rol.getDomicilio().setNumeroProcedencia(rol.getDomicilio().getNumero());
        rol.getDomicilio().setEstadoProcedencia(rol.getDomicilio().getIdEstado());
        rol.getDomicilio().setMunicipioProcedencia(rol.getDomicilio().getIdMunicipio());
        rol.getDomicilio().setAsentamientoProcedencia(rol.getDomicilio().getIdAsentamiento());
        selectMunicipioProcedencia();
        selectAsentamientoProcedencia();
    }


}
