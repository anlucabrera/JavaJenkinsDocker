package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.*;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaCalificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCasoCritico;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistraPromedioAsignatura;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionEvidenciaInstrumento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoTipo;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.stream.Collectors;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Messages;



@Named
@ViewScoped
public class CapturaCalificacionesDocente extends ViewScopedRol implements Desarrollable {
    @Getter @Setter private CapturaCalificacionesRolDocente rol;
    @EJB EjbCapturaCalificaciones ejb;
    @EJB EjbCasoCritico ejbCasoCritico;
    @EJB EjbRegistraPromedioAsignatura ejbRegistraPromedioAsignatura;
    @EJB EjbPacker packer;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Inject Caster caster;
    @Getter @Setter Boolean tieneAcceso = false, existeAperturaInd = false;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "captura de calificaciones";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
//        System.out.println("CapturaCalificacionesDocente.init");
        setVistaControlador(ControlEscolarVistaControlador.CAPTURA_CALIFICACIONES);
        ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDocente(logon.getPersonal().getClave());
//        System.out.println("resAcceso = " + resAcceso);
//        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

        ResultadoEJB<Boolean> resVerificarCargasExistentes = ejb.verificarCargasExistentes(resAcceso.getValor().getEntity());
//        System.out.println("resVerificarCargasExistentes = " + resVerificarCargasExistentes);
//        if(!resAcceso.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}

        rol = new CapturaCalificacionesRolDocente(resAcceso.getValor());

        tieneAcceso = rol.tieneAcceso(rol.getDocenteLogueado()) || resVerificarCargasExistentes.getValor();
//        System.out.println("tieneAcceso = " + tieneAcceso);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
        if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
        if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

        ResultadoEJB<EventoEscolar> resEvento = ejb.verificarEvento(rol.getDocenteLogueado());
//        System.out.println("resEvento = " + resEvento);
        if(!resEvento.getCorrecto()) resEvento = ejb.verificarEventoExtemporaneo(rol.getDocenteLogueado());
        ResultadoEJB<List<DtoUnidadConfiguracion>> resUnidades = null;
        if(!resEvento.getCorrecto()) {
            resUnidades = ejb.getUnidadesEnEvaluacion(rol.getDocenteLogueado());
//            System.out.println("resUnidades = " + resUnidades);
//            if(!resUnidades.getCorrecto()){ tieneAcceso = false;return;}
        }//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
        ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosConCaptura(rol.getDocenteLogueado());
//        System.out.println("resPeriodos = " + resPeriodos);
//        if(!resPeriodos.getCorrecto()) {mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}

        if(!resEvento.getCorrecto() && (resUnidades!= null && !resUnidades.getCorrecto())) mostrarMensajeResultadoEJB(resEvento);
        rol.setNivelRol(NivelRol.OPERATIVO);

        if(resEvento.getCorrecto()) {//verificar si la apertura es por evento
            rol.setEventoActivo(resEvento.getValor());
            rol.setPeriodoActivo(resEvento.getValor().getPeriodo());
        }else if(resUnidades!=null && resUnidades.getCorrecto())//verificar si la apertura es por planeación del docente
            rol.setPeriodoActivo(resUnidades.getValor().get(0).getDtoCargaAcademica().getPeriodo().getPeriodo());

        rol.setPeriodosConCarga(resPeriodos.getValor());

//        actualizar();
        cambiarPeriodo();
    }

    public void actualizar(){
        repetirUltimoMensaje();
    }

    public void cambiarPeriodo(){
//        System.out.println("rol.getPeriodoSeleccionado() = " + caster.periodoToString(rol.getPeriodoSeleccionado()));
        if(rol.getPeriodoSeleccionado() == null) {
            mostrarMensaje("No hay periodo escolar seleccionado.");
            rol.setCargasDocente(Collections.EMPTY_LIST);
            return;
        }

        ResultadoEJB<List<DtoCargaAcademica>> resCargas = ejb.getCargasAcadémicasPorPeriodo(rol.getDocenteLogueado(), rol.getPeriodoSeleccionado());
        if(!resCargas.getCorrecto()) mostrarMensajeResultadoEJB(resCargas);
        else rol.setCargasDocente(resCargas.getValor());

        cambiarCarga();
    }

    public void cambiarCarga(){
//        System.out.println("rol.getCargaAcademicaSeleccionada(1) = " + caster.dtoCargaAcademicaToString(rol.getCargaAcademicaSeleccionada()));
        
        if(rol.getPeriodoSeleccionado().getPeriodo()<=56){
            if(rol.getCargaAcademicaSeleccionada() == null){
                mostrarMensaje("No hay carga académica seleccionada");
                rol.setDtoUnidadConfiguraciones(Collections.EMPTY_LIST);
                return;
            }//verificar si se seleccionó una carga académica

            ResultadoEJB<List<DtoUnidadConfiguracion>> resConfiguraciones = ejb.getConfiguraciones(rol.getCargaAcademicaSeleccionada());//obtener las configuraciones de la carga académica seleccionada
            if (!resConfiguraciones.getCorrecto()) {
                mostrarMensajeResultadoEJB(resConfiguraciones);
            } else {
                rol.setDtoUnidadConfiguraciones(resConfiguraciones.getValor());//almacenar el valor en la capa SET
//            System.out.println("rol.getDtoUnidadConfiguraciones().size() = " + rol.getDtoUnidadConfiguraciones().size());
//            rol.getDtoUnidadConfiguraciones().forEach(System.out::println);
            }
        }else{
            if(rol.getCargaAcademicaSeleccionada() == null){
                mostrarMensaje("No hay carga académica seleccionada");
                rol.setDtoUnidadConfiguracionesAlineacion(Collections.EMPTY_LIST);
                return;
            }//verificar si se seleccionó una carga académica
            
            ResultadoEJB<List<DtoUnidadConfiguracionAlineacion>> resConfiguraciones = ejb.getConfiguracionesAlineacion(rol.getCargaAcademicaSeleccionada());//obtener las configuraciones de la carga académica seleccionada
            if (!resConfiguraciones.getCorrecto()) {
                mostrarMensajeResultadoEJB(resConfiguraciones);
            } else {
                rol.setDtoUnidadConfiguracionesAlineacion(resConfiguraciones.getValor());//almacenar el valor en la capa SET
            }
        
        }
//        System.out.println("rol.getDtoUnidadConfiguracionSeleccionada() = " + rol.getDtoUnidadConfiguracionSeleccionada());
        cambiarUnidad();
    }

    public void cambiarUnidad(){
//        System.out.println("rol.getDtoUnidadConfiguracionSeleccionada() = " + caster.dtoUnidadConfiguracionToString(rol.getDtoUnidadConfiguracionSeleccionada()));
        if(rol.getPeriodoSeleccionado().getPeriodo()<=56){
            if(rol.getDtoUnidadConfiguracionSeleccionada() == null) {
                mostrarMensaje("No hay unidad de evaluación seleccionada.");
                rol.setEstudiantesPorGrupo(null);
                return;
            }
            ResultadoEJB<DtoGrupoEstudiante> resGrupo = packer.packGrupoEstudiante(rol.getCargaAcademicaSeleccionada(), rol.getDtoUnidadConfiguracionSeleccionada());
            if (!resGrupo.getCorrecto()) {
                mostrarMensajeResultadoEJB(resGrupo);
            } else {
                rol.setEstudiantesPorGrupo(resGrupo.getValor());
                setExisteAperturaInd(existeAperturaIndividual(rol.getEstudiantesPorGrupo().getEstudiantes()));
//            System.out.println("resGrupo = " + resGrupo.getValor().getEstudiantes().size());
            }
        }else{
            if(rol.getDtoUnidadConfiguracionAlineacionSeleccionada() == null) {
                mostrarMensaje("No hay unidad de evaluación seleccionada.");
                rol.setEstudiantesPorGrupoAlineacion(null);
                return;
            }
            ResultadoEJB<DtoGrupoEstudianteAlineacion> resGrupo = packer.packGrupoEstudianteAlineacion(rol.getCargaAcademicaSeleccionada(), rol.getDtoUnidadConfiguracionAlineacionSeleccionada());
            if (!resGrupo.getCorrecto()) {
                mostrarMensajeResultadoEJB(resGrupo);
            } else {
                rol.setEstudiantesPorGrupoAlineacion(resGrupo.getValor());
                setExisteAperturaInd(existeAperturaIndividualAlineacion(rol.getEstudiantesPorGrupoAlineacion().getEstudiantes()));
//            System.out.println("resGrupo = " + resGrupo.getValor().getEstudiantes().size());
            }
        }
    }

    public void guardarCalificacion(ValueChangeEvent event){
        if(rol.getPeriodoSeleccionado().getPeriodo()<=56){
            DtoCapturaCalificacion.Captura captura = (DtoCapturaCalificacion.Captura) event.getComponent().getAttributes().get("captura");
            DtoCapturaCalificacion dtoCapturaCalificacion = (DtoCapturaCalificacion) event.getComponent().getAttributes().get("dtoCapturaCalificacion");
            Double valor = Double.parseDouble(event.getNewValue().toString());
            captura.getCalificacion().setValor(valor);
            rol.getCalificacionMap().put(captura.getCalificacion().getCalificacion(), valor);
            Boolean validarReins = existeReinscripcion(dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoEstudiante());
            Boolean validarNivelacion = existeNivelacion(dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoEstudiante());
            if (!validarReins) {
                if (!validarNivelacion) {
                    ResultadoEJB<Calificacion> res = ejb.guardarCapturaCalificacion(captura);
                    ResultadoEJB<BigDecimal> resPromedio = ejb.promediarUnidad(dtoCapturaCalificacion);
                        if(res.getCorrecto() && resPromedio.getCorrecto()) {
                        rol.getEstudiantesPorGrupo().actualizarCalificacion(res.getValor(), resPromedio.getValor());
                        dtoCapturaCalificacion.setPromedio(resPromedio.getValor());
                    
                        registrarPromediosAsignatura(dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoEstudiante());
                    
                        ResultadoEJB<DtoCasoCritico> registrarPorReprobacion = ejbCasoCritico.registrarPorReprobacion(dtoCapturaCalificacion);
                        if(registrarPorReprobacion.getCorrecto()) mostrarMensaje("Se generó un caso crítico automáticamente por promedio reprobatorio.");
                        else if(registrarPorReprobacion.getResultado() < 4) mostrarMensajeResultadoEJB(registrarPorReprobacion);
                        }
                        else mostrarMensajeResultadoEJB(res);
                } else {
                Messages.addGlobalFatal("El estudiante ya tiene nivelación final registrada, no se puede actualizar una calificación ordinaria");
                }
            } else {
                Messages.addGlobalFatal("El estudiante ya se reinscribió al siguiente cuatrimestre, no se puede actualizar una calificación ordinaria");
            }
        }else{
            DtoCapturaCalificacionAlineacion.Captura captura = (DtoCapturaCalificacionAlineacion.Captura) event.getComponent().getAttributes().get("captura");
            DtoCapturaCalificacionAlineacion dtoCapturaCalificacion = (DtoCapturaCalificacionAlineacion) event.getComponent().getAttributes().get("dtoCapturaCalificacion");
            Double valor = Double.parseDouble(event.getNewValue().toString());
            captura.getCalificacion().setValor(valor);
            rol.getCalificacionMap().put(captura.getCalificacion().getCalificacionEvidenciaInstrumento(), valor);
            Boolean validarReins = existeReinscripcion(dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoEstudiante());
            Boolean validarNivelacion = existeNivelacion(dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoEstudiante());
            if (!validarReins) {
                if (!validarNivelacion) {
                    ResultadoEJB<CalificacionEvidenciaInstrumento> res = ejb.guardarCapturaCalificacionAlineacion(captura);
                    ResultadoEJB<BigDecimal> resPromedio = ejb.promediarUnidadAlineacion(dtoCapturaCalificacion);
                        if(res.getCorrecto() && resPromedio.getCorrecto()) {
                        rol.getEstudiantesPorGrupoAlineacion().actualizarCalificacion(res.getValor(), resPromedio.getValor());
                        dtoCapturaCalificacion.setPromedio(resPromedio.getValor());
                    
                        registrarPromediosAsignaturaAlineacion(dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoEstudiante());
                    
                        ResultadoEJB<DtoCasoCriticoAlineacion> registrarPorReprobacion = ejbCasoCritico.registrarPorReprobacionAlineacion(dtoCapturaCalificacion);
                        if(registrarPorReprobacion.getCorrecto()) mostrarMensaje("Se generó un caso crítico automáticamente por promedio reprobatorio.");
                        else if(registrarPorReprobacion.getResultado() < 4) mostrarMensajeResultadoEJB(registrarPorReprobacion);
                        }
                        else mostrarMensajeResultadoEJB(res);
                } else {
                Messages.addGlobalFatal("El estudiante ya tiene nivelación final registrada, no se puede actualizar una calificación ordinaria");
                }
            } else {
                Messages.addGlobalFatal("El estudiante ya se reinscribió al siguiente cuatrimestre, no se puede actualizar una calificación ordinaria");
            }
        
        }
    }

    public void iniciarRegistroCasoCritico(DtoCapturaCalificacion dtoCapturaCalificacion){
        rol.setDtoCapturaCalificacionSeleccionada(dtoCapturaCalificacion);
//        System.out.println("CapturaCalificacionesDocente.iniciarRegistroCasoCritico");
//        System.out.println("dtoCapturaCalificacion = " + dtoCapturaCalificacion);
//        System.out.println("dtoCapturaCalificacion.getTieneCasoCritico() = " + dtoCapturaCalificacion.getTieneCasoCritico());
        if(!dtoCapturaCalificacion.getTieneCasoCritico()){
            ResultadoEJB<DtoCasoCritico> generarNuevo = ejbCasoCritico.generarNuevo(dtoCapturaCalificacion.getDtoEstudiante(), dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoUnidadConfiguracion(), CasoCriticoTipo.ASISTENCIA_IRREGURLAR);
//            System.out.println("generarNuevo = " + generarNuevo);
            if(generarNuevo.getCorrecto()){
                rol.getDtoCapturaCalificacionSeleccionada().setDtoCasoCritico(generarNuevo.getValor());
//                System.out.println("dtoCapturaCalificacion.getDtoCasoCritico() = " + dtoCapturaCalificacion.getDtoCasoCritico());
//                System.out.println("dtoCapturaCalificacion.getDtoCasoCritico().getCasoCritico() = " + dtoCapturaCalificacion.getDtoCasoCritico().getCasoCritico());
            }else mostrarMensajeResultadoEJB(generarNuevo);

        }

    }

    public void iniciarRegistroCasoCriticoAlineacion(DtoCapturaCalificacionAlineacion dtoCapturaCalificacion){
        rol.setDtoCapturaCalificacionAlineacionSeleccionada(dtoCapturaCalificacion);
//        System.out.println("CapturaCalificacionesDocente.iniciarRegistroCasoCritico");
//        System.out.println("dtoCapturaCalificacion = " + dtoCapturaCalificacion);
//        System.out.println("dtoCapturaCalificacion.getTieneCasoCritico() = " + dtoCapturaCalificacion.getTieneCasoCritico());
        if(!dtoCapturaCalificacion.getTieneCasoCritico()){
            ResultadoEJB<DtoCasoCriticoAlineacion> generarNuevo = ejbCasoCritico.generarNuevoAlineacion(dtoCapturaCalificacion.getDtoEstudiante(), dtoCapturaCalificacion.getDtoCargaAcademica(), dtoCapturaCalificacion.getDtoUnidadConfiguracion(), CasoCriticoTipo.ASISTENCIA_IRREGURLAR);
//            System.out.println("generarNuevo = " + generarNuevo);
            if(generarNuevo.getCorrecto()){
                rol.getDtoCapturaCalificacionAlineacionSeleccionada().setDtoCasoCritico(generarNuevo.getValor());
//                System.out.println("dtoCapturaCalificacion.getDtoCasoCritico() = " + dtoCapturaCalificacion.getDtoCasoCritico());
//                System.out.println("dtoCapturaCalificacion.getDtoCasoCritico().getCasoCritico() = " + dtoCapturaCalificacion.getDtoCasoCritico().getCasoCritico());
            }else mostrarMensajeResultadoEJB(generarNuevo);

        }

    }
    
    public Boolean comprobarDtoCapturaCalificacionSeleccionada(){
        if(rol.getDtoCapturaCalificacionSeleccionada() == null) {
            mostrarMensaje("No se puede registrar el caso crítico porque la referencia de la captura de calificación es nula.");
            return false;
        }

        return true;
    }
    
    public Boolean comprobarDtoCapturaCalificacionAlineacionSeleccionada(){
        if(rol.getDtoCapturaCalificacionAlineacionSeleccionada() == null) {
            mostrarMensaje("No se puede registrar el caso crítico porque la referencia de la captura de calificación es nula.");
            return false;
        }

        return true;
    }

    public void guardarCasoCritico(){
//        System.out.println("CapturaCalificacionesDocente.guardarCasoCritico");
        if(rol.getPeriodoSeleccionado().getPeriodo()<=56){
            if(!comprobarDtoCapturaCalificacionSeleccionada()) return;

//          System.out.println("rol.getDtoCapturaCalificacionSeleccionada().getDtoCasoCritico().getCasoCritico().getDescripcion() = " + rol.getDtoCapturaCalificacionSeleccionada().getDtoCasoCritico().getCasoCritico().getDescripcion());
            if(rol.getDtoCapturaCalificacionSeleccionada().getDtoCasoCritico().getCasoCritico().getDescripcion() == null || rol.getDtoCapturaCalificacionSeleccionada().getDtoCasoCritico().getCasoCritico().getDescripcion().trim().isEmpty()){
                mostrarMensaje("Debe ingresar una descripción del caso crítico");
                Ajax.oncomplete("PF('modalCasoCritico').show();");
                return;
            }
            ResultadoEJB<DtoCasoCritico> registrar = ejbCasoCritico.registrar(rol.getDtoCapturaCalificacionSeleccionada().getDtoCasoCritico());
//          System.out.println("registrar = " + registrar);
            if(registrar.getCorrecto()){
                rol.getDtoCapturaCalificacionSeleccionada().setDtoCasoCritico(registrar.getValor());
//              System.out.println("rol.getDtoCapturaCalificacionSeleccionada().getTieneCasoCritico() = " + rol.getDtoCapturaCalificacionSeleccionada().getTieneCasoCritico());
            }else mostrarMensajeResultadoEJB(registrar);
        }else{
            if(!comprobarDtoCapturaCalificacionAlineacionSeleccionada()) return;

//          System.out.println("rol.getDtoCapturaCalificacionSeleccionada().getDtoCasoCritico().getCasoCritico().getDescripcion() = " + rol.getDtoCapturaCalificacionSeleccionada().getDtoCasoCritico().getCasoCritico().getDescripcion());
            if(rol.getDtoCapturaCalificacionAlineacionSeleccionada().getDtoCasoCritico().getCasoCritico().getDescripcion() == null || rol.getDtoCapturaCalificacionAlineacionSeleccionada().getDtoCasoCritico().getCasoCritico().getDescripcion().trim().isEmpty()){
                mostrarMensaje("Debe ingresar una descripción del caso crítico");
                Ajax.oncomplete("PF('modalCasoCritico').show();");
                return;
            }
            ResultadoEJB<DtoCasoCriticoAlineacion> registrar = ejbCasoCritico.registrarAlineacion(rol.getDtoCapturaCalificacionAlineacionSeleccionada().getDtoCasoCritico());
//          System.out.println("registrar = " + registrar);
            if(registrar.getCorrecto()){
                rol.getDtoCapturaCalificacionAlineacionSeleccionada().setDtoCasoCritico(registrar.getValor());
//              System.out.println("rol.getDtoCapturaCalificacionSeleccionada().getTieneCasoCritico() = " + rol.getDtoCapturaCalificacionSeleccionada().getTieneCasoCritico());
            }else mostrarMensajeResultadoEJB(registrar);
        }
    }

    public void eliminarCasoCritico(){
        if(rol.getPeriodoSeleccionado().getPeriodo()<=56){
            if(!comprobarDtoCapturaCalificacionSeleccionada() || !rol.getDtoCapturaCalificacionSeleccionada().getTieneCasoCritico()) return;
            ResultadoEJB<Boolean> eliminar = ejbCasoCritico.eliminarAlineacion(rol.getDtoCapturaCalificacionAlineacionSeleccionada().getDtoCasoCritico());
            mostrarMensajeResultadoEJB(eliminar);
        }else{
            if(!comprobarDtoCapturaCalificacionAlineacionSeleccionada() || !rol.getDtoCapturaCalificacionAlineacionSeleccionada().getTieneCasoCritico()) return;
            ResultadoEJB<Boolean> eliminar = ejbCasoCritico.eliminarAlineacion(rol.getDtoCapturaCalificacionAlineacionSeleccionada().getDtoCasoCritico());
            mostrarMensajeResultadoEJB(eliminar);
        }
    }

    public List<CasoCriticoTipo> getListaCasoCriticoTipos(){
        return CasoCriticoTipo.Lista();
    }

    /*public String generarIdTabla(DtoUnidadConfiguracion dtoUnidadConfiguracion){//
//        System.out.println("dtoUnidadConfiguracion = " + dtoUnidadConfiguracion);
//        System.out.println("dtoUnidadConfiguracion.getUnidadMateriaConfiguracion() = " + dtoUnidadConfiguracion.getUnidadMateriaConfiguracion());
//        System.out.println("dtoUnidadConfiguracion.getUnidadMateriaConfiguracion().getConfiguracion() = " + dtoUnidadConfiguracion.getUnidadMateriaConfiguracion().getConfiguracion());
        return "tbl".concat(dtoUnidadConfiguracion.getUnidadMateriaConfiguracion().getConfiguracion().toString());
    }*/
    
    public Boolean existeReinscripcion(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull DtoEstudiante dtoEstudiante){
        ResultadoEJB<Boolean> estudianteReinscrito = ejb.existeReinscripcion(dtoCargaAcademica.getCargaAcademica().getEvento().getPeriodo(), dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula());
        if(estudianteReinscrito.getValor()){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
            }
    }
    
    public Boolean existeNivelacion(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull DtoEstudiante dtoEstudiante){
        ResultadoEJB<Boolean> registroNivelacion = ejb.existeNivelacion(dtoCargaAcademica.getCargaAcademica(), dtoEstudiante.getInscripcionActiva().getInscripcion());
        if(registroNivelacion.getValor()){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
            }
    }
    
    public Boolean existeAperturaIndividual(@NonNull List<DtoCapturaCalificacion> estudiantes){
        
        List<DtoCapturaCalificacion> listaAperturas = estudiantes.stream().filter(a -> a.getPermisoExtInd()).collect(Collectors.toList());
        
        if(listaAperturas.isEmpty()){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }
    
    public Boolean existeAperturaIndividualAlineacion(@NonNull List<DtoCapturaCalificacionAlineacion> estudiantes){
        
        List<DtoCapturaCalificacionAlineacion> listaAperturas = estudiantes.stream().filter(a -> a.getPermisoExtInd()).collect(Collectors.toList());
        
        if(listaAperturas.isEmpty()){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }
    
    public void registrarPromediosAsignatura(DtoCargaAcademica dtoCargaAcademica, DtoEstudiante dtoEstudiante)
    {
        try {
            ejbRegistraPromedioAsignatura.registrarPromediosAsignatura(dtoCargaAcademica, dtoEstudiante);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void registrarPromediosAsignaturaAlineacion(DtoCargaAcademica dtoCargaAcademica, DtoEstudiante dtoEstudiante)
    {
        try {
            ejbRegistraPromedioAsignatura.registrarPromediosAsignaturaAlineacion(dtoCargaAcademica, dtoEstudiante);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
