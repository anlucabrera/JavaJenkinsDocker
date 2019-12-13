package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
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
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradora;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradoraPromedio;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class CapturaTareaIntegradoraDocente  extends ViewScopedRol implements Desarrollable {
    @Getter @Setter private CapturaTareaIntegradoraRolDocente rol;
    @EJB EjbCapturaTareaIntegradora ejb;
    @EJB EjbCapturaCalificaciones ejbCapturaCalificaciones;
    @EJB EjbPacker packer;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Inject Caster caster;
    @Getter Boolean tieneAcceso = false;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "captura de tarea integradora";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    @PostConstruct
    public void init(){
        try{
            setVistaControlador(ControlEscolarVistaControlador.CAPTURA_TAREA_INTEGRADORA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbCapturaCalificaciones.validarDocente(logon.getPersonal().getClave());
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Boolean> resVerificarCargasExistentes = ejbCapturaCalificaciones.verificarCargasExistentes(resAcceso.getValor().getEntity());
            rol = new CapturaTareaIntegradoraRolDocente(resAcceso.getValor());
            tieneAcceso = rol.tieneAcceso(rol.getDocenteLogueado()) && resVerificarCargasExistentes.getValor();
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            ResultadoEJB<EventoEscolar> resEvento = ejb.verificarEvento(rol.getDocenteLogueado());
            if(!resEvento.getCorrecto()) resEvento = ejb.verificarEventoExtemporaneo(rol.getDocenteLogueado());

            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejbCapturaCalificaciones.getPeriodosConCaptura(rol.getDocenteLogueado());
            rol.setNivelRol(NivelRol.OPERATIVO);
//            System.out.println("resEvento = " + resEvento);
            if(resEvento.getCorrecto()) {//verificar si la apertura es por evento
                rol.setEventoActivo(resEvento.getValor());
                rol.setPeriodoActivo(resEvento.getValor().getPeriodo());
            }else {

            }

            rol.setPeriodosConCarga(resPeriodos.getValor());

            cambiarPeriodo();
        }catch (Exception e){
            System.out.println("CapturaTareaIntegradoraDocente.init: Error controlado en método inicializador");
            e.printStackTrace();
        }
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
        else if(tareaIntegradoraResultadoEJB.getResultado() == 1) mostrarMensaje(tareaIntegradoraResultadoEJB.getMensaje());
    }

    public BigDecimal getPromedioAsignaturaEstudiante(@NonNull DtoEstudiante dtoEstudiante){
        ResultadoEJB<BigDecimal> res = ejb.promediarAsignatura(rol.getDtoUnidadesCalificacion(), rol.getCargaAcademicaSeleccionada(), dtoEstudiante);
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensaje(String.format("El promedio del estudiante %s %s %s con matrícula %s, no se pudo calcular.", dtoEstudiante.getPersona().getApellidoPaterno(), dtoEstudiante.getPersona().getApellidoMaterno(), dtoEstudiante.getPersona().getNombre(), dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula()));
            return BigDecimal.ZERO;
        }
    }

    public void guardarCalificacion(ValueChangeEvent event){
        DtoEstudiante dtoEstudiante = (DtoEstudiante) event.getComponent().getAttributes().get("estudiante");
        Double valor = Double.parseDouble(event.getNewValue().toString());
        rol.getDtoUnidadesCalificacion().getTareaIntegradoraPromedioMap().get(dtoEstudiante).setValor(valor);
        ResultadoEJB<TareaIntegradoraPromedio> guardarCalificacion = ejb.guardarCalificacion(rol.getTareaIntegradora(), rol.getDtoUnidadesCalificacion(), dtoEstudiante);
        ResultadoEJB<BigDecimal> promediarAsignatura = ejb.promediarAsignatura(rol.getDtoUnidadesCalificacion(), rol.getCargaAcademicaSeleccionada(), dtoEstudiante);
        if(guardarCalificacion.getCorrecto() && promediarAsignatura.getCorrecto()) {
            rol.getDtoUnidadesCalificacion().getTareaIntegradoraPromedioMap().put(dtoEstudiante, guardarCalificacion.getValor());
        }
        else mostrarMensajeResultadoEJB(guardarCalificacion);
    }
}
