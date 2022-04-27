package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import edu.mx.utxj.pye.seut.util.dto.Dto;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.*;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaCalificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaTareaIntegradora;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistraPromedioAsignatura;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.omnifaces.util.Messages;

@Named
@ViewScoped
public class CapturaTareaIntegradoraDocente  extends ViewScopedRol implements Desarrollable {
    @Getter @Setter private CapturaTareaIntegradoraRolDocente rol;
    @EJB EjbCapturaTareaIntegradora ejb;
    @EJB EjbCapturaCalificaciones ejbCapturaCalificaciones;
    @EJB EjbRegistraPromedioAsignatura ejbRegistraPromedioAsignatura;
    @EJB EjbPacker packer;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Inject Caster caster;
    @Getter @Setter Boolean tieneAcceso = false, faltaUnidad = false, existeAperIndNiv = false, existeAperGrupalTI = false, existeAperIndTI = false;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "captura de tarea integradora";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    @PostConstruct
    public void init() {
        try {
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.CapturaTareaIntegradoraDocente.init(A)");
            setVistaControlador(ControlEscolarVistaControlador.CAPTURA_TAREA_INTEGRADORA);
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.CapturaTareaIntegradoraDocente.init(B)");
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbCapturaCalificaciones.validarDocente(logon.getPersonal().getClave());
//            System.out.println("resAcceso = " + resAcceso);
//        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.CapturaTareaIntegradoraDocente.init(C)");
            ResultadoEJB<Boolean> resVerificarCargasExistentes = ejbCapturaCalificaciones.verificarCargasExistentes(resAcceso.getValor().getEntity());
//            System.out.println("resVerificarCargasExistentes = " + resVerificarCargasExistentes);
//        if(!resAcceso.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}

        rol = new CapturaTareaIntegradoraRolDocente(resAcceso.getValor());

        tieneAcceso = rol.tieneAcceso(rol.getDocenteLogueado()) || resVerificarCargasExistentes.getValor();
//        System.out.println("tieneAcceso = " + tieneAcceso);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            ResultadoEJB<EventoEscolar> resEvento = ejb.verificarEvento(rol.getDocenteLogueado());
//            System.out.println("resEvento1 = " + resEvento);
            if(!resEvento.getCorrecto()) resEvento = ejb.verificarEventoExtemporaneo(rol.getDocenteLogueado());

            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejbCapturaCalificaciones.getPeriodosConCaptura(rol.getDocenteLogueado());
            rol.setNivelRol(NivelRol.OPERATIVO);
//            System.out.println("resEvento2 = " + resEvento);
            if(resEvento.getCorrecto()) {//verificar si la apertura es por evento
                rol.setEventoActivo(resEvento.getValor());
                rol.setPeriodoActivo(resEvento.getValor().getPeriodo());
            }else {
                mostrarMensajeResultadoEJB(resAcceso);
            }

            ResultadoEJB<List<Indicador>> consultarIndicadores = ejb.consultarIndicadores();
            if(consultarIndicadores.getCorrecto()) rol.setIndicadores(consultarIndicadores.getValor());
            else mostrarMensajeResultadoEJB(consultarIndicadores);
            
            ResultadoEJB<List<EvidenciaEvaluacion>> consultarEvidencias = ejb.consultarEvidencias();
            if(consultarEvidencias.getCorrecto()) rol.setEvidencias(consultarEvidencias.getValor());
            else mostrarMensajeResultadoEJB(consultarEvidencias);

            //Faces.getContext().getExternalContext().get
//            PeriodosEscolares periodo = Faces.getSessionAttribute("periodo");
//            System.out.println("periodo = " + periodo);
//            rol.setPeriodoSeleccionado(periodo);
//            DtoCargaAcademica cargaAcademica = Faces.getSessionAttribute("carga");
//            System.out.println("cargaAcademica = " + cargaAcademica);
//            rol.setCargaAcademicaSeleccionada(cargaAcademica);

            rol.setPeriodosConCarga(resPeriodos.getValor());
            if(!rol.getPeriodosConCarga().isEmpty()){
                PeriodosEscolares periodoActivo = rol.getPeriodosConCarga().stream()
                        .filter(periodosEscolares -> Objects.equals(periodosEscolares.getPeriodo(), rol.getPeriodoActivo()))
                        .findFirst().orElse(null);
                if(periodoActivo != null) rol.setPeriodoSeleccionado(periodoActivo);
            }
            cambiarPeriodo();
        }catch (Exception e){
            System.out.println("CapturaTareaIntegradoraDocente.init: Error controlado en método inicializador");
            e.printStackTrace();
        }
        //Se tiene que comentar antes de que inicie el próximo cuatrimestre
//        try {
//            if(rol.getDocenteLogueado().getPersonal().getClave().intValue() == 169) {
//                if(rol.getPeriodoSeleccionado().getPeriodo()<=56){
//                    ResultadoEJB<Point> registrarMasivamentePromedios = ejbRegistraPromedioAsignatura.registrarMasivamentePromedios();
//                    System.out.println("registrarMasivamentePromedios = " + registrarMasivamentePromedios);
//                }else{
//                    ResultadoEJB<Point> registrarMasivamentePromedios = ejbRegistraPromedioAsignatura.registrarMasivamentePromediosAlineacion();
//                    System.out.println("registrarMasivamentePromediosAlineacion = " + registrarMasivamentePromedios);
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public void actualizar(){
        repetirUltimoMensaje();
    }

    public void cambiarPeriodo(){
        if(rol.getPeriodoSeleccionado() == null) {
            mostrarMensaje("No hay periodo escolar seleccionado.");
            rol.setCargasDocente(Collections.EMPTY_LIST);
            return;
        }

        Faces.setSessionAttribute("periodo", rol.getPeriodoSeleccionado());

        ResultadoEJB<List<DtoCargaAcademica>> resCargas = ejbCapturaCalificaciones.getCargasAcadémicasPorPeriodo(rol.getDocenteLogueado(), rol.getPeriodoSeleccionado());
        if(!resCargas.getCorrecto()) mostrarMensajeResultadoEJB(resCargas);
        else rol.setCargasDocente(resCargas.getValor());

        cambiarCarga();
    }

    public void cambiarCarga(){
        if(rol.getCargaAcademicaSeleccionada() == null){
            mostrarMensaje("No hay carga académica seleccionada.");
            return;
        }

        /*Faces.setSessionAttribute("carga", rol.getCargaAcademicaSeleccionada());

        ResultadoEJB<List<DtoUnidadConfiguracion>> resConfiguraciones = ejbCapturaCalificaciones.getConfiguraciones(rol.getCargaAcademicaSeleccionada());
        if(!resConfiguraciones.getCorrecto()){
            mostrarMensaje("No se detectaron configuraciones de unidades en la materia de la carga académica seleccionada. " + resConfiguraciones.getMensaje());
            return;
        }

        rol.setDtoUnidadConfiguraciones(resConfiguraciones.getValor());
        ResultadoEJB<DtoUnidadesCalificacion> resDtoUnidadesCalificacion = packer.packDtoUnidadesCalificacion(rol.getCargaAcademicaSeleccionada(), rol.getDtoUnidadConfiguraciones());
        if(!resDtoUnidadesCalificacion.getCorrecto()){
            mostrarMensaje("No se detectaron registros de calificaciones de la carga seleccionada. " + resDtoUnidadesCalificacion.getMensaje());
        }

        rol.setDtoUnidadesCalificacion(resDtoUnidadesCalificacion.getValor());

        ResultadoEJB<TareaIntegradora> tareaIntegradoraResultadoEJB = ejb.verificarTareaIntegradora(rol.getCargaAcademicaSeleccionada());
        if(tareaIntegradoraResultadoEJB.getCorrecto()) {
            rol.setTieneIntegradora(true);
            rol.setTareaIntegradora(tareaIntegradoraResultadoEJB.getValor());
            ResultadoEJB<Map<DtoEstudiante, TareaIntegradoraPromedio>> generarContenedorCalificaciones = ejb.generarContenedorCalificaciones(rol.getDtoUnidadesCalificacion().getDtoEstudiantes(), tareaIntegradoraResultadoEJB.getValor());
            if(!generarContenedorCalificaciones.getCorrecto()){
                mostrarMensajeResultadoEJB(generarContenedorCalificaciones);
                return;
            }
            rol.getDtoUnidadesCalificacion().setTareaIntegradoraPromedioMap(generarContenedorCalificaciones.getValor());
        }
        else if(tareaIntegradoraResultadoEJB.getResultado() == 1) mostrarMensaje(tareaIntegradoraResultadoEJB.getMensaje());*/
    }

    /*public BigDecimal getPromedioAsignaturaEstudiante(@NonNull DtoEstudiante dtoEstudiante){
        ResultadoEJB<BigDecimal> res = ejb.promediarAsignatura(rol.getDtoUnidadesCalificacion(), rol.getCargaAcademicaSeleccionada(), dtoEstudiante);
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensaje(String.format("El promedio del estudiante %s %s %s con matrícula %s, no se pudo calcular.", dtoEstudiante.getPersona().getApellidoPaterno(), dtoEstudiante.getPersona().getApellidoMaterno(), dtoEstudiante.getPersona().getNombre(), dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula()));
            return BigDecimal.ZERO;
        }
    }*/

    public BigDecimal getPromedioAsignaturaEstudiante(@NonNull DtoEstudiante dtoEstudiante, @NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getPeriodoSeleccionado().getPeriodo()<=56){
            ResultadoEJB<BigDecimal> res = ejb.promediarAsignatura(getContenedor(dtoCargaAcademica), dtoCargaAcademica, dtoEstudiante);
            if(res.getCorrecto()){
                setExisteAperIndNiv(existeAperIndNivelacion(dtoCargaAcademica, dtoEstudiante));
                setFaltaUnidad(faltaCapturaUnidad(dtoCargaAcademica, dtoEstudiante));
                return res.getValor();
            }else{
                mostrarMensaje(String.format("El promedio del estudiante %s %s %s con matrícula %s, no se pudo calcular.", dtoEstudiante.getPersona().getApellidoPaterno(), dtoEstudiante.getPersona().getApellidoMaterno(), dtoEstudiante.getPersona().getNombre(), dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula()));
                return BigDecimal.ZERO;
            }
        }else{
            ResultadoEJB<BigDecimal> res = ejb.promediarAsignaturaAlineacion(getContenedorAlineacion(dtoCargaAcademica), dtoCargaAcademica, dtoEstudiante);
            if(res.getCorrecto()){
                setExisteAperIndNiv(existeAperIndNivelacion(dtoCargaAcademica, dtoEstudiante));
                setFaltaUnidad(faltaCapturaUnidad(dtoCargaAcademica, dtoEstudiante));
                return res.getValor();
            }else{
                mostrarMensaje(String.format("El promedio del estudiante %s %s %s con matrícula %s, no se pudo calcular.", dtoEstudiante.getPersona().getApellidoPaterno(), dtoEstudiante.getPersona().getApellidoMaterno(), dtoEstudiante.getPersona().getNombre(), dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula()));
                return BigDecimal.ZERO;
            }
        }
       
    }

    public BigDecimal getPromedioUnidad(@NonNull DtoEstudiante dtoEstudiante, @NonNull DtoUnidadConfiguracion dtoUnidadConfiguracion){
        ResultadoEJB<DtoCapturaCalificacion> packCapturaCalificacion = packer.packCapturaCalificacion(dtoEstudiante, rol.getCargaAcademicaSeleccionada(), dtoUnidadConfiguracion);
        if(packCapturaCalificacion.getCorrecto()){
            ResultadoEJB<BigDecimal> promediarUnidad = ejbCapturaCalificaciones.promediarUnidad(packCapturaCalificacion.getValor());
            if(promediarUnidad.getCorrecto()) return promediarUnidad.getValor();
            else{
                mostrarMensajeResultadoEJB(promediarUnidad);
                return BigDecimal.ZERO;
            }
        }else {
            mostrarMensajeResultadoEJB(packCapturaCalificacion);
            return BigDecimal.ZERO;
        }
    }
    
    public BigDecimal getPromedioUnidadAlineacion(@NonNull DtoEstudiante dtoEstudiante, @NonNull DtoUnidadConfiguracionAlineacion dtoUnidadConfiguracion){
        ResultadoEJB<DtoCapturaCalificacionAlineacion> packCapturaCalificacion = packer.packCapturaCalificacionAlineacion(dtoEstudiante, rol.getCargaAcademicaSeleccionada(), dtoUnidadConfiguracion);
        if(packCapturaCalificacion.getCorrecto()){
            ResultadoEJB<BigDecimal> promediarUnidad = ejbCapturaCalificaciones.promediarUnidadAlineacion(packCapturaCalificacion.getValor());
            if(promediarUnidad.getCorrecto()) return promediarUnidad.getValor();
            else{
                mostrarMensajeResultadoEJB(promediarUnidad);
                return BigDecimal.ZERO;
            }
        }else {
            mostrarMensajeResultadoEJB(packCapturaCalificacion);
            return BigDecimal.ZERO;
        }
    }

    /*public void guardarCalificacion(ValueChangeEvent event){
        DtoEstudiante dtoEstudiante = (DtoEstudiante) event.getComponent().getAttributes().get("estudiante");
        Double valor = Double.parseDouble(event.getNewValue().toString());
        rol.getDtoUnidadesCalificacion().getTareaIntegradoraPromedioMap().get(dtoEstudiante).setValor(valor);
        ResultadoEJB<TareaIntegradoraPromedio> guardarCalificacion = ejb.guardarCalificacion(rol.getTareaIntegradora(), rol.getDtoUnidadesCalificacion(), dtoEstudiante);
        ResultadoEJB<BigDecimal> promediarAsignatura = ejb.promediarAsignatura(rol.getDtoUnidadesCalificacion(), rol.getCargaAcademicaSeleccionada(), dtoEstudiante);
        if(guardarCalificacion.getCorrecto() && promediarAsignatura.getCorrecto()) {
            rol.getDtoUnidadesCalificacion().getTareaIntegradoraPromedioMap().put(dtoEstudiante, guardarCalificacion.getValor());
        }
        else mostrarMensajeResultadoEJB(guardarCalificacion);
    }*/

    public void guardarCalificacion(ValueChangeEvent event){
        @NonNull DtoCargaAcademica dtoCargaAcademica = (DtoCargaAcademica)event.getComponent().getAttributes().get("carga");
        @NonNull DtoEstudiante dtoEstudiante = (DtoEstudiante) event.getComponent().getAttributes().get("estudiante");
        @NonNull Double valor = Double.parseDouble(event.getNewValue().toString());
        Boolean validarReins = existeReinscripcion(dtoCargaAcademica, dtoEstudiante);
        Boolean validarNivelacion = existeNivelacion(dtoCargaAcademica, dtoEstudiante);
        if (!validarReins) {
            if (!validarNivelacion) {
                getContenedor(dtoCargaAcademica).getTareaIntegradoraPromedioMap().get(dtoEstudiante).setValor(valor);
                ResultadoEJB<TareaIntegradoraPromedio> guardarCalificacion = ejb.guardarCalificacion(rol.getTareaIntegradoraMap().get(dtoCargaAcademica), getContenedor(dtoCargaAcademica), dtoEstudiante);
                ResultadoEJB<BigDecimal> promediarAsignatura = ejb.promediarAsignatura(getContenedor(dtoCargaAcademica), rol.getCargaAcademicaSeleccionada(), dtoEstudiante);
                if (guardarCalificacion.getCorrecto() && promediarAsignatura.getCorrecto()) {
                    getContenedor(dtoCargaAcademica).getTareaIntegradoraPromedioMap().put(dtoEstudiante, guardarCalificacion.getValor());
                } else {
                    mostrarMensajeResultadoEJB(guardarCalificacion);
                }
           } else {
            Messages.addGlobalFatal("El estudiante ya tiene nivelación final registrada, no se puede actualizar calificación tarea integradora");
            }
        } else {
            Messages.addGlobalFatal("El estudiante ya se reinscribió al siguiente cuatrimestre, no se puede actualizar el resultado de tarea integradora");
        }
    }
    
    public void guardarCalificacionAlineacion(ValueChangeEvent event){
        @NonNull DtoCargaAcademica dtoCargaAcademica = (DtoCargaAcademica)event.getComponent().getAttributes().get("carga");
        @NonNull DtoEstudiante dtoEstudiante = (DtoEstudiante) event.getComponent().getAttributes().get("estudiante");
        @NonNull Double valor = Double.parseDouble(event.getNewValue().toString());
        Boolean validarCapturaUnidades = faltaCapturaUnidad(dtoCargaAcademica, dtoEstudiante);
        Boolean validarReins = existeReinscripcion(dtoCargaAcademica, dtoEstudiante);
        Boolean validarNivelacion = existeNivelacion(dtoCargaAcademica, dtoEstudiante);
        if (!validarCapturaUnidades) {
            if (!validarReins) {
                if (!validarNivelacion) {
                    getContenedorAlineacion(dtoCargaAcademica).getTareaIntegradoraPromedioMap().get(dtoEstudiante).setValor(valor);
                    ResultadoEJB<TareaIntegradoraPromedio> guardarCalificacion = ejb.guardarCalificacionAlineacion(rol.getTareaIntegradoraMap().get(dtoCargaAcademica), getContenedorAlineacion(dtoCargaAcademica), dtoEstudiante);
                    ResultadoEJB<BigDecimal> promediarAsignatura = ejb.promediarAsignaturaAlineacion(getContenedorAlineacion(dtoCargaAcademica), rol.getCargaAcademicaSeleccionada(), dtoEstudiante);
                    if (guardarCalificacion.getCorrecto() && promediarAsignatura.getCorrecto()) {
                        getContenedor(dtoCargaAcademica).getTareaIntegradoraPromedioMap().put(dtoEstudiante, guardarCalificacion.getValor());
                    } else {
                        mostrarMensajeResultadoEJB(guardarCalificacion);
                    }
                } else {
                    Messages.addGlobalFatal("El estudiante ya tiene nivelación final registrada, no se puede actualizar calificación tarea integradora");
                }
            } else {
                Messages.addGlobalFatal("El estudiante ya se reinscribió al siguiente cuatrimestre, no se puede actualizar el resultado de tarea integradora");
            }
        } else {
            Messages.addGlobalFatal("No se puede guardar debe de registrarle calificaciones al estudiante en todas las unidades");
        }
    }

    public void guardarNivelacion(ValueChangeEvent event){
        @NonNull DtoCargaAcademica dtoCargaAcademica = (DtoCargaAcademica)event.getComponent().getAttributes().get("carga");
//        System.out.println("dtoCargaAcademica = " + dtoCargaAcademica);
        @NonNull DtoEstudiante dtoEstudiante = (DtoEstudiante) event.getComponent().getAttributes().get("estudiante");
//        System.out.println("dtoEstudiante = " + dtoEstudiante);
        Boolean validarReins = existeReinscripcion(dtoCargaAcademica, dtoEstudiante);
        if(!validarReins){
            if(event.getNewValue() instanceof Double){
                Double valor = (Double)event.getNewValue();
                if(getNivelacion(dtoCargaAcademica, dtoEstudiante) != null) {
                    getNivelacion(dtoCargaAcademica, dtoEstudiante).getCalificacionNivelacion().setValor(valor);
                    ResultadoEJB<CalificacionNivelacion> guardarNivelacion = ejb.guardarNivelacion(getContenedor(dtoCargaAcademica), dtoEstudiante);
                    if(!guardarNivelacion.getCorrecto()) mostrarMensajeResultadoEJB(guardarNivelacion);
                }
            }else if(event.getNewValue() instanceof  Indicador){
                Indicador indicador = (Indicador)event.getNewValue();
//            System.out.println("indicador controller = " + indicador);
                if(getNivelacion(dtoCargaAcademica, dtoEstudiante) != null) {
                    getNivelacion(dtoCargaAcademica, dtoEstudiante).setIndicador(indicador);
                    ResultadoEJB<CalificacionNivelacion> guardarNivelacion = ejb.guardarNivelacion(getContenedor(dtoCargaAcademica), dtoEstudiante);
                    if(!guardarNivelacion.getCorrecto()) mostrarMensajeResultadoEJB(guardarNivelacion);
                }
            }
        }else{
            Messages.addGlobalFatal("El estudiante ya se reinscribió al siguiente cuatrimestre, no se puede actualizar el resultado de nivelación");
        }

    }
    
    public void guardarNivelacionAlineacion(ValueChangeEvent event){
        @NonNull DtoCargaAcademica dtoCargaAcademica = (DtoCargaAcademica)event.getComponent().getAttributes().get("carga");
//        System.out.println("dtoCargaAcademica = " + dtoCargaAcademica);
        @NonNull DtoEstudiante dtoEstudiante = (DtoEstudiante) event.getComponent().getAttributes().get("estudiante");
//        System.out.println("dtoEstudiante = " + dtoEstudiante);
        Boolean validarReins = existeReinscripcion(dtoCargaAcademica, dtoEstudiante);
        if(!validarReins){
            if(event.getNewValue() instanceof Double){
                Double valor = (Double)event.getNewValue();
                if(getNivelacionAlineacion(dtoCargaAcademica, dtoEstudiante) != null) {
                    getNivelacionAlineacion(dtoCargaAcademica, dtoEstudiante).getCalificacionNivelacion().setValor(valor);
                    ResultadoEJB<CalificacionNivelacion> guardarNivelacion = ejb.guardarNivelacionAlineacion(getContenedorAlineacion(dtoCargaAcademica), dtoEstudiante);
                    if(!guardarNivelacion.getCorrecto()) mostrarMensajeResultadoEJB(guardarNivelacion);
                }
            }else if(event.getNewValue() instanceof  Indicador){
                Indicador indicador = (Indicador)event.getNewValue();
//            System.out.println("indicador controller = " + indicador);
                if(getNivelacionAlineacion(dtoCargaAcademica, dtoEstudiante) != null) {
                    getNivelacionAlineacion(dtoCargaAcademica, dtoEstudiante).setIndicador(indicador);
                    ResultadoEJB<CalificacionNivelacion> guardarNivelacion = ejb.guardarNivelacionAlineacion(getContenedorAlineacion(dtoCargaAcademica), dtoEstudiante);
                    if(!guardarNivelacion.getCorrecto()) mostrarMensajeResultadoEJB(guardarNivelacion);
                }
            }
        }else{
            Messages.addGlobalFatal("El estudiante ya se reinscribió al siguiente cuatrimestre, no se puede actualizar el resultado de nivelación");
        }

    }

    public List<DtoUnidadConfiguracion> getUnidades(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getDtoUnidadConfiguracionesMap().containsKey(dtoCargaAcademica)) return  rol.getDtoUnidadConfiguracionesMap().get(dtoCargaAcademica);

        ResultadoEJB<List<DtoUnidadConfiguracion>> resConfiguraciones = ejbCapturaCalificaciones.getConfiguraciones(dtoCargaAcademica);
        if(!resConfiguraciones.getCorrecto()){
            mostrarMensaje("No se detectaron configuraciones de unidades en la materia de la carga académica seleccionada. " + resConfiguraciones.getMensaje());
            return Collections.EMPTY_LIST;
        }
        rol.getDtoUnidadConfiguracionesMap().put(dtoCargaAcademica, resConfiguraciones.getValor());

        return  rol.getDtoUnidadConfiguracionesMap().get(dtoCargaAcademica);
    }
    
    public List<DtoUnidadConfiguracionAlineacion> getUnidadesAlineacion(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getDtoUnidadConfiguracionesAlineacionMap().containsKey(dtoCargaAcademica)) return  rol.getDtoUnidadConfiguracionesAlineacionMap().get(dtoCargaAcademica);

        ResultadoEJB<List<DtoUnidadConfiguracionAlineacion>> resConfiguraciones = ejbCapturaCalificaciones.getConfiguracionesAlineacion(dtoCargaAcademica);
        if(!resConfiguraciones.getCorrecto()){
            mostrarMensaje("No se detectaron configuraciones de unidades en la materia de la carga académica seleccionada. " + resConfiguraciones.getMensaje());
            return Collections.EMPTY_LIST;
        }
        rol.getDtoUnidadConfiguracionesAlineacionMap().put(dtoCargaAcademica, resConfiguraciones.getValor());

        return  rol.getDtoUnidadConfiguracionesAlineacionMap().get(dtoCargaAcademica);
    }

    public  Boolean tieneIntegradora(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getTieneIntegradoraMap().containsKey(dtoCargaAcademica)) return rol.getTieneIntegradoraMap().get(dtoCargaAcademica);

        ResultadoEJB<TareaIntegradora> tareaIntegradoraResultadoEJB = ejb.verificarTareaIntegradora(dtoCargaAcademica);
        if(tareaIntegradoraResultadoEJB.getCorrecto()) {
            rol.getTieneIntegradoraMap().put(dtoCargaAcademica, true);
            rol.getTareaIntegradoraMap().put(dtoCargaAcademica, tareaIntegradoraResultadoEJB.getValor());

            if(rol.getPeriodoSeleccionado().getPeriodo()<=56)  {  
                ResultadoEJB<Map<DtoEstudiante, TareaIntegradoraPromedio>> generarContenedorCalificaciones = ejb.generarContenedorCalificaciones(getContenedor(dtoCargaAcademica).getDtoEstudiantes(), tareaIntegradoraResultadoEJB.getValor(), dtoCargaAcademica);
                if(!generarContenedorCalificaciones.getCorrecto()){
                    mostrarMensajeResultadoEJB(generarContenedorCalificaciones);
                }
                rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica).setTareaIntegradoraPromedioMap(generarContenedorCalificaciones.getValor());
            }else{
                ResultadoEJB<Map<DtoEstudiante, TareaIntegradoraPromedio>> generarContenedorCalificaciones = ejb.generarContenedorCalificaciones(getContenedorAlineacion(dtoCargaAcademica).getDtoEstudiantes(), tareaIntegradoraResultadoEJB.getValor(), dtoCargaAcademica);
                if(!generarContenedorCalificaciones.getCorrecto()){
                    mostrarMensajeResultadoEJB(generarContenedorCalificaciones);
                }
                rol.getDtoUnidadesCalificacionAlineacionMap().get(dtoCargaAcademica).setTareaIntegradoraPromedioMap(generarContenedorCalificaciones.getValor());
            }
        }
        else{
            rol.getTieneIntegradoraMap().put(dtoCargaAcademica, false);
        }

        return rol.getTieneIntegradoraMap().get(dtoCargaAcademica);
    }

    public DtoUnidadesCalificacion getContenedor(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getDtoUnidadesCalificacionMap().containsKey(dtoCargaAcademica)) return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
        
        if(rol.getEventoActivo()!= null){
            ResultadoEJB<DtoUnidadesCalificacion> resDtoUnidadesCalificacion = packer.packDtoUnidadesCalificacion(dtoCargaAcademica, getUnidades(dtoCargaAcademica), rol.getEventoActivo());
                if(!resDtoUnidadesCalificacion.getCorrecto()){
                mostrarMensaje("No se detectaron registros de calificaciones de la carga seleccionada. " + resDtoUnidadesCalificacion.getMensaje());
                return null;
            }
            rol.getDtoUnidadesCalificacionMap().put(dtoCargaAcademica, resDtoUnidadesCalificacion.getValor());
            return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
        }else{
            mostrarMensaje("No existe evento activo para captura de tarea integradora y nivelación final. ");
            return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
        }
    }
    
    public DtoUnidadesCalificacionAlineacion getContenedorAlineacion(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getDtoUnidadesCalificacionAlineacionMap().containsKey(dtoCargaAcademica)) return rol.getDtoUnidadesCalificacionAlineacionMap().get(dtoCargaAcademica);
           
        if(rol.getEventoActivo()!= null){
            ResultadoEJB<DtoUnidadesCalificacionAlineacion> resDtoUnidadesCalificacion = packer.packDtoUnidadesCalificacionAlineacion(dtoCargaAcademica, getUnidadesAlineacion(dtoCargaAcademica), rol.getEventoActivo());
            if(!resDtoUnidadesCalificacion.getCorrecto()){
                mostrarMensaje("No se detectaron registros de calificaciones de la carga seleccionada. " + resDtoUnidadesCalificacion.getMensaje());
                return null;
            }
             rol.getDtoUnidadesCalificacionAlineacionMap().put(dtoCargaAcademica, resDtoUnidadesCalificacion.getValor());
             return rol.getDtoUnidadesCalificacionAlineacionMap().get(dtoCargaAcademica);
        }else{
            mostrarMensaje("No existe evento activo para captura de tarea integradora y nivelación final. ");
            return rol.getDtoUnidadesCalificacionAlineacionMap().get(dtoCargaAcademica);
        }
    }

    public DtoCalificacionNivelacion getNivelacion(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull DtoEstudiante dtoEstudiante){
        DtoUnidadesCalificacion.DtoNivelacionPK pk = new DtoUnidadesCalificacion.DtoNivelacionPK(dtoCargaAcademica, dtoEstudiante);
        if(getContenedor(dtoCargaAcademica).getNivelacionMap().containsKey(pk)) return getContenedor(dtoCargaAcademica).getNivelacionMap().get(pk);

        @NonNull List<Indicador> indicadores = rol.getIndicadores();
        if(indicadores.isEmpty()){
            mostrarMensaje("La lista de indicadores está vacía");
            return null;
        }
        ResultadoEJB<DtoCalificacionNivelacion> packDtoCalificacionNivelacion = packer.packDtoCalificacionNivelacion(dtoCargaAcademica, dtoEstudiante, indicadores.get(0));
        if(packDtoCalificacionNivelacion.getCorrecto()){
            @NonNull DtoCalificacionNivelacion dtoCalificacionNivelacion = packDtoCalificacionNivelacion.getValor();
//            if(dtoCalificacionNivelacion.getCalificacionNivelacion().getCargaAcademica().getCarga() == 161 && dtoCalificacionNivelacion.getCalificacionNivelacion().getEstudiante().getIdEstudiante() == 194)
//                System.out.println("dtoCalificacionNivelacion = " + dtoCalificacionNivelacion.getIndicador());
            getContenedor(dtoCargaAcademica).getNivelacionMap().put(pk, dtoCalificacionNivelacion);
            return getContenedor(dtoCargaAcademica).getNivelacionMap().get(pk);
        }else {
            mostrarMensajeResultadoEJB(packDtoCalificacionNivelacion);
            return null;
        }
    }
    
    public DtoCalificacionNivelacion getNivelacionAlineacion(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull DtoEstudiante dtoEstudiante){
        DtoUnidadesCalificacionAlineacion.DtoNivelacionPK pk = new DtoUnidadesCalificacionAlineacion.DtoNivelacionPK(dtoCargaAcademica, dtoEstudiante);
        if(getContenedorAlineacion(dtoCargaAcademica).getNivelacionMap().containsKey(pk)) return getContenedorAlineacion(dtoCargaAcademica).getNivelacionMap().get(pk);

        @NonNull List<Indicador> indicadores = rol.getIndicadores();
        if(indicadores.isEmpty()){
            mostrarMensaje("La lista de indicadores está vacía");
            return null;
        }
        ResultadoEJB<DtoCalificacionNivelacion> packDtoCalificacionNivelacion = packer.packDtoCalificacionNivelacion(dtoCargaAcademica, dtoEstudiante, indicadores.get(0));
        if(packDtoCalificacionNivelacion.getCorrecto()){
            @NonNull DtoCalificacionNivelacion dtoCalificacionNivelacion = packDtoCalificacionNivelacion.getValor();
//            if(dtoCalificacionNivelacion.getCalificacionNivelacion().getCargaAcademica().getCarga() == 161 && dtoCalificacionNivelacion.getCalificacionNivelacion().getEstudiante().getIdEstudiante() == 194)
//                System.out.println("dtoCalificacionNivelacion = " + dtoCalificacionNivelacion.getIndicador());
            getContenedorAlineacion(dtoCargaAcademica).getNivelacionMap().put(pk, dtoCalificacionNivelacion);
            return getContenedorAlineacion(dtoCargaAcademica).getNivelacionMap().get(pk);
        }else {
            mostrarMensajeResultadoEJB(packDtoCalificacionNivelacion);
            return null;
        }
    }

    public Boolean deshabilitarCaptura(@NonNull DtoCargaAcademica dtoCargaAcademica, BigDecimal promedio){
        try {
            Boolean activa;
            if(rol.getPeriodoSeleccionado().getPeriodo()<=56){
                DtoUnidadesCalificacion contenedor = getContenedor(dtoCargaAcademica);
                activa = contenedor.getActivaPorFecha() || contenedor.getActivaPorAperturaExtemporanea();
            }else{
                DtoUnidadesCalificacionAlineacion contenedor = getContenedorAlineacion(dtoCargaAcademica);
                activa = contenedor.getActivaPorFecha() || contenedor.getActivaPorAperturaExtemporanea();
            }
            return !(promedio.compareTo(new BigDecimal(8)) < 0 && activa);
        }catch (NullPointerException e){
            return Boolean.TRUE;
        }
    }
    
    public Boolean deshabilitarCapturaTI(@NonNull DtoCargaAcademica dtoCargaAcademica){
        ResultadoEJB<Boolean> fechasApertura = ejb.fechasAperturaTI(dtoCargaAcademica);
        if(fechasApertura.getCorrecto()){
            return fechasApertura.getValor();
        }else{
            return Boolean.FALSE;
        }
    }
    
    public Boolean existeReinscripcion(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull DtoEstudiante dtoEstudiante){
        ResultadoEJB<Boolean> estudianteReinscrito = ejbCapturaCalificaciones.existeReinscripcion(dtoCargaAcademica.getCargaAcademica().getEvento().getPeriodo(), dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula());
        if(estudianteReinscrito.getValor()){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
            }
    }
    
     public Boolean existeNivelacion(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull DtoEstudiante dtoEstudiante){
        ResultadoEJB<Boolean> registroNivelacion = ejbCapturaCalificaciones.existeNivelacion(dtoCargaAcademica.getCargaAcademica(), dtoEstudiante.getInscripcionActiva().getInscripcion());
        if(registroNivelacion.getValor()){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
            }
    }
     
    public Boolean faltaCapturaUnidad(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull DtoEstudiante dtoEstudiante){
        ResultadoEJB<Boolean> faltaCapturaUnidad = ejb.faltaCapturaCalificacionUnidad(dtoCargaAcademica, dtoEstudiante);
        if(faltaCapturaUnidad.getValor()){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
        }
    }
     
    public Boolean existeAperIndNivelacion(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull DtoEstudiante dtoEstudiante){
        ResultadoEJB<Boolean> aperturaIndNiv = ejb.existeAperIndNivelacion(dtoCargaAcademica, dtoEstudiante);
        if(aperturaIndNiv.getValor()){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
        }
    }
    
    public Boolean existeAperGrupalTareaInt(@NonNull DtoCargaAcademica dtoCargaAcademica){
        ResultadoEJB<Boolean> aperturaGrupTI = ejb.existeAperGrupalTI(dtoCargaAcademica);
        if(aperturaGrupTI.getValor()){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
        }
    }
    
    public Boolean existeAperIndTareaInt(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull DtoEstudiante dtoEstudiante){
        ResultadoEJB<Boolean> aperturaIndTI = ejb.existeAperIndTI(dtoCargaAcademica, dtoEstudiante);
        if(aperturaIndTI.getValor()){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
        }
    }
    
}
