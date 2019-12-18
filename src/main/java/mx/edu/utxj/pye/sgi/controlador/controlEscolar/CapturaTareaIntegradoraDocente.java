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

            ResultadoEJB<List<Indicador>> consultarIndicadores = ejb.consultarIndicadores();
            if(consultarIndicadores.getCorrecto()) rol.setIndicadores(consultarIndicadores.getValor());
            else mostrarMensajeResultadoEJB(consultarIndicadores);

            //Faces.getContext().getExternalContext().get
//            PeriodosEscolares periodo = Faces.getSessionAttribute("periodo");
//            System.out.println("periodo = " + periodo);
//            rol.setPeriodoSeleccionado(periodo);
//            DtoCargaAcademica cargaAcademica = Faces.getSessionAttribute("carga");
//            System.out.println("cargaAcademica = " + cargaAcademica);
//            rol.setCargaAcademicaSeleccionada(cargaAcademica);

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
        ResultadoEJB<BigDecimal> res = ejb.promediarAsignatura(getContenedor(dtoCargaAcademica), dtoCargaAcademica, dtoEstudiante);
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensaje(String.format("El promedio del estudiante %s %s %s con matrícula %s, no se pudo calcular.", dtoEstudiante.getPersona().getApellidoPaterno(), dtoEstudiante.getPersona().getApellidoMaterno(), dtoEstudiante.getPersona().getNombre(), dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula()));
            return BigDecimal.ZERO;
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
        getContenedor(dtoCargaAcademica).getTareaIntegradoraPromedioMap().get(dtoEstudiante).setValor(valor);
        ResultadoEJB<TareaIntegradoraPromedio> guardarCalificacion = ejb.guardarCalificacion(rol.getTareaIntegradoraMap().get(dtoCargaAcademica), getContenedor(dtoCargaAcademica), dtoEstudiante);
        ResultadoEJB<BigDecimal> promediarAsignatura = ejb.promediarAsignatura(getContenedor(dtoCargaAcademica), rol.getCargaAcademicaSeleccionada(), dtoEstudiante);
        if(guardarCalificacion.getCorrecto() && promediarAsignatura.getCorrecto()) {
            getContenedor(dtoCargaAcademica).getTareaIntegradoraPromedioMap().put(dtoEstudiante, guardarCalificacion.getValor());
        }
        else mostrarMensajeResultadoEJB(guardarCalificacion);
    }

    public void guardarNivelacion(ValueChangeEvent event){
        @NonNull DtoCargaAcademica dtoCargaAcademica = (DtoCargaAcademica)event.getComponent().getAttributes().get("carga");
//        System.out.println("dtoCargaAcademica = " + dtoCargaAcademica);
        @NonNull DtoEstudiante dtoEstudiante = (DtoEstudiante) event.getComponent().getAttributes().get("estudiante");
//        System.out.println("dtoEstudiante = " + dtoEstudiante);
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

    public  Boolean tieneIntegradora(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getTieneIntegradoraMap().containsKey(dtoCargaAcademica)) return rol.getTieneIntegradoraMap().get(dtoCargaAcademica);

        ResultadoEJB<TareaIntegradora> tareaIntegradoraResultadoEJB = ejb.verificarTareaIntegradora(dtoCargaAcademica);
        if(tareaIntegradoraResultadoEJB.getCorrecto()) {
            rol.getTieneIntegradoraMap().put(dtoCargaAcademica, true);
            rol.getTareaIntegradoraMap().put(dtoCargaAcademica, tareaIntegradoraResultadoEJB.getValor());

            ResultadoEJB<Map<DtoEstudiante, TareaIntegradoraPromedio>> generarContenedorCalificaciones = ejb.generarContenedorCalificaciones(getContenedor(dtoCargaAcademica).getDtoEstudiantes(), tareaIntegradoraResultadoEJB.getValor());
            if(!generarContenedorCalificaciones.getCorrecto()){
                mostrarMensajeResultadoEJB(generarContenedorCalificaciones);
            }
            rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica).setTareaIntegradoraPromedioMap(generarContenedorCalificaciones.getValor());
        }
        else{
            rol.getTieneIntegradoraMap().put(dtoCargaAcademica, false);
        }

        return rol.getTieneIntegradoraMap().get(dtoCargaAcademica);
    }

    public DtoUnidadesCalificacion getContenedor(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getDtoUnidadesCalificacionMap().containsKey(dtoCargaAcademica)) return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);

        ResultadoEJB<DtoUnidadesCalificacion> resDtoUnidadesCalificacion = packer.packDtoUnidadesCalificacion(dtoCargaAcademica, getUnidades(dtoCargaAcademica));
        if(!resDtoUnidadesCalificacion.getCorrecto()){
            mostrarMensaje("No se detectaron registros de calificaciones de la carga seleccionada. " + resDtoUnidadesCalificacion.getMensaje());
            return null;
        }

        rol.getDtoUnidadesCalificacionMap().put(dtoCargaAcademica, resDtoUnidadesCalificacion.getValor());
        return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
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
}