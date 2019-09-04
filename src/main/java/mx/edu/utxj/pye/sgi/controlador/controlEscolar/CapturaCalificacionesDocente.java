package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.*;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaCalificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
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
public class CapturaCalificacionesDocente extends ViewScopedRol implements Desarrollable {
    @Getter @Setter private CapturaCalificacionesRolDocente rol;
    @EJB EjbCapturaCalificaciones ejb;
    @EJB EjbPacker packer;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Inject Caster caster;
    @Getter Boolean tieneAcceso = false;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "captura de calificaciones";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    @PostConstruct
    public void init(){
//        System.out.println("CapturaCalificacionesDocente.init");
        setVistaControlador(ControlEscolarVistaControlador.CAPTURA_CALIFICACIONES);
        ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDocente(logon.getPersonal().getClave());
//        System.out.println("resAcceso = " + resAcceso);
        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

        ResultadoEJB<Boolean> resVerificarCargasExistentes = ejb.verificarCargasExistentes(resAcceso.getValor().getEntity());
        System.out.println("resVerificarCargasExistentes = " + resVerificarCargasExistentes);
        if(!resAcceso.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}

        rol = new CapturaCalificacionesRolDocente(resAcceso.getValor());

        tieneAcceso = rol.tieneAcceso(rol.getDocenteLogueado()) && resVerificarCargasExistentes.getValor();
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
//        System.out.println("CapturaCalificacionesDocente.actualizar");
        /*if(rol.getPeriodoSeleccionado() == null) {
            mostrarMensaje("No hay periodo escolar seleccionado.");
            rol.setCargasDocente(Collections.EMPTY_LIST);
            rol.setDtoUnidadConfiguraciones(Collections.EMPTY_LIST);
            rol.setEstudiantesPorGrupo(null);
            return;
        }//detener flujo si no hay periodo seleccionado

        //actualizar cargas

//        System.out.println("rol.getCargaAcademicaSeleccionada() = " + rol.getCargaAcademicaSeleccionada());

        //actualizar configuracion de unidades
        if(rol.getCargaAcademicaSeleccionada() == null){ mostrarMensaje("No hay carga académica seleccionada");return;}//verificar si se seleccionó una carga académica
        ResultadoEJB<List<DtoUnidadConfiguracion>> resConfiguraciones = ejb.getConfiguraciones(rol.getCargaAcademicaSeleccionada());//obtener las configuraciones de la carga académica seleccionada
        if(!resConfiguraciones.getCorrecto()){
            mostrarMensajeResultadoEJB(resConfiguraciones);
            rol.setDtoUnidadConfiguraciones(Collections.EMPTY_LIST);
            rol.setEstudiantesPorGrupo(null);
            return;
        } //verificar si se obtuvo resultado
//        if(!resConfiguraciones.getValor().isEmpty()) rol.setDtoUnidadConfiguracionSeleccionada(null);
        rol.setDtoUnidadConfiguraciones(resConfiguraciones.getValor());//almacenar el valor en la capa SET
        rol.getDtoUnidadConfiguracionSeleccionada().getUnidadMateriaConfiguracionDetalles().forEach((criterio, detalles) -> {
            System.out.println("criterio = " + criterio);
            System.out.println("detalles.size() = " + detalles.size());
            System.out.println("detalles = " + detalles);
        });
//        System.out.println("rol.getDtoUnidadConfiguracionSeleccionada() = " + rol.getDtoUnidadConfiguracionSeleccionada().getUnidadMateria().getNombre());

        //actualizar estudiantes del grupo correspondientes a la carga académica seleccionada
        if(rol.getDtoUnidadConfiguracionSeleccionada() == null) {
            mostrarMensaje("No hay unidad de evaluación seleccionada.");
            rol.setEstudiantesPorGrupo(null);
            return;
        }
        ResultadoEJB<DtoGrupoEstudiante> resGrupo = packer.packGrupoEstudiante(rol.getCargaAcademicaSeleccionada(), rol.getDtoUnidadConfiguracionSeleccionada());
//        System.out.println("resGrupo = " + resGrupo.getValor().getEstudiantes().size());
        rol.setEstudiantesPorGrupo(resGrupo.getValor());
//        System.out.println("rol.getDtoUnidadConfiguracionSeleccionada() = " + rol.getDtoUnidadConfiguracionSeleccionada());
        //TODO: agregar al grupo la alineacion con la materia de la unidad seleccionada*/
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
        if(rol.getCargaAcademicaSeleccionada() == null){
            mostrarMensaje("No hay carga académica seleccionada");
            rol.setDtoUnidadConfiguraciones(Collections.EMPTY_LIST);
            return;
        }//verificar si se seleccionó una carga académica

        ResultadoEJB<List<DtoUnidadConfiguracion>> resConfiguraciones = ejb.getConfiguraciones(rol.getCargaAcademicaSeleccionada());//obtener las configuraciones de la carga académica seleccionada
        if(!resConfiguraciones.getCorrecto()) mostrarMensajeResultadoEJB(resConfiguraciones);
        else {
            rol.setDtoUnidadConfiguraciones(resConfiguraciones.getValor());//almacenar el valor en la capa SET
//            System.out.println("rol.getDtoUnidadConfiguraciones().size() = " + rol.getDtoUnidadConfiguraciones().size());
//            rol.getDtoUnidadConfiguraciones().forEach(System.out::println);
        }
//        System.out.println("rol.getDtoUnidadConfiguracionSeleccionada() = " + rol.getDtoUnidadConfiguracionSeleccionada());
        cambiarUnidad();
    }

    public void cambiarUnidad(){
//        System.out.println("rol.getDtoUnidadConfiguracionSeleccionada() = " + caster.dtoUnidadConfiguracionToString(rol.getDtoUnidadConfiguracionSeleccionada()));
        if(rol.getDtoUnidadConfiguracionSeleccionada() == null) {
            mostrarMensaje("No hay unidad de evaluación seleccionada.");
            rol.setEstudiantesPorGrupo(null);
            return;
        }
        ResultadoEJB<DtoGrupoEstudiante> resGrupo = packer.packGrupoEstudiante(rol.getCargaAcademicaSeleccionada(), rol.getDtoUnidadConfiguracionSeleccionada());
        if(!resGrupo.getCorrecto()) mostrarMensajeResultadoEJB(resGrupo);
        else {
            rol.setEstudiantesPorGrupo(resGrupo.getValor());
//            System.out.println("resGrupo = " + resGrupo.getValor().getEstudiantes().size());
        }
    }

    public void guardarCalificacion(ValueChangeEvent event){
//        System.out.println("event.getNewValue() = " + event.getNewValue());
//        System.out.println("event.getComponent().getAttributes() = " + event.getComponent().getAttributes());
        DtoCapturaCalificacion.Captura captura = (DtoCapturaCalificacion.Captura) event.getComponent().getAttributes().get("captura");
        DtoCapturaCalificacion dtoCapturaCalificacion = (DtoCapturaCalificacion) event.getComponent().getAttributes().get("dtoCapturaCalificacion");
        Double valor = Double.parseDouble(event.getNewValue().toString());
        captura.getCalificacion().setValor(valor);
        rol.getCalificacionMap().put(captura.getCalificacion().getCalificacion(), valor);
        ResultadoEJB<Calificacion> res = ejb.guardarCapturaCalificacion(captura);
//        System.out.println("res = " + res);
        ResultadoEJB<BigDecimal> resPromedio = ejb.promediarUnidad(dtoCapturaCalificacion);
//        System.out.println("resPromedio = " + resPromedio);
        if(res.getCorrecto() && resPromedio.getCorrecto()) {
            rol.getEstudiantesPorGrupo().actualizarCalificacion(res.getValor(), resPromedio.getValor());
            dtoCapturaCalificacion.setPromedio(resPromedio.getValor());
        }
        else mostrarMensajeResultadoEJB(res);
//        System.out.println("dtoCapturaCalificacion = " + dtoCapturaCalificacion.getPromedio());
//        System.out.println("captura = " + captura);
//        System.out.println("valor = " + valor);
//        System.out.println("rol.getCalificacionMap() = " + rol.getCalificacionMap());
    }
}
