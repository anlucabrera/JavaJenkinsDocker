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
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



@Named
@ViewScoped
public class CapturaComentariosDocente extends ViewScopedRol implements Desarrollable {
    @Getter @Setter CapturaComentariosRolDocente rol;
    @EJB EjbCapturaCalificaciones ejbCapturaCalificaciones;
    @EJB EjbCasoCritico ejbCasoCritico;
    @EJB EjbPacker packer;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Inject Caster caster;
    @Getter Boolean tieneAcceso = false;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "captura de comentarios";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        setVistaControlador(ControlEscolarVistaControlador.CAPTURA_COMENTARIOS_DOCENTE);
        ResultadoEJB<Filter<PersonalActivo>> validarDocente = ejbCapturaCalificaciones.validarDocente(logon.getPersonal().getClave());
        if(!validarDocente.getCorrecto()){ mostrarMensajeResultadoEJB(validarDocente);return;}

        ResultadoEJB<Boolean> resVerificarCargasExistentes = ejbCapturaCalificaciones.verificarCargasExistentes(validarDocente.getValor().getEntity());
        if(!resVerificarCargasExistentes.getCorrecto()){mostrarMensajeResultadoEJB(resVerificarCargasExistentes);return;}

        rol = new CapturaComentariosRolDocente(validarDocente.getValor());
        tieneAcceso = rol.tieneAcceso(rol.getDocenteLogueado()) && resVerificarCargasExistentes.getValor();

        /////////////////////////////////////////////////////////
        if(cortarFlujo()) return;
        if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

        ResultadoEJB<EventoEscolar> resEvento = ejbCapturaCalificaciones.verificarEvento(rol.getDocenteLogueado());
        if(!resEvento.getCorrecto()) resEvento = ejbCapturaCalificaciones.verificarEventoExtemporaneo(rol.getDocenteLogueado());
        ResultadoEJB<List<DtoUnidadConfiguracion>> resUnidades = null;
        if(!resEvento.getCorrecto()) {
            resUnidades = ejbCapturaCalificaciones.getUnidadesEnEvaluacion(rol.getDocenteLogueado());
        }//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
        ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejbCapturaCalificaciones.getPeriodosConCaptura(rol.getDocenteLogueado());

        if(!resEvento.getCorrecto() && (resUnidades!= null && !resUnidades.getCorrecto())) mostrarMensajeResultadoEJB(resEvento);
        rol.setNivelRol(NivelRol.OPERATIVO);

        if(resEvento.getCorrecto()) {//verificar si la apertura es por evento
            rol.setEventoActivo(resEvento.getValor());
            rol.setPeriodoActivo(resEvento.getValor().getPeriodo());
        }else if(resUnidades!=null && resUnidades.getCorrecto())//verificar si la apertura es por planeación del docente
            rol.setPeriodoActivo(resUnidades.getValor().get(0).getDtoCargaAcademica().getPeriodo().getPeriodo());

        rol.setPeriodosConCarga(resPeriodos.getValor());

        cambiarPeriodo();
    }

    public void cambiarPeriodo(){
//        System.out.println("rol.getPeriodoSeleccionado() = " + caster.periodoToString(rol.getPeriodoSeleccionado()));
        if(rol.getPeriodoSeleccionado() == null) {
            mostrarMensaje("No hay periodo escolar seleccionado.");
            rol.setCargasDocente(Collections.EMPTY_LIST);
            return;
        }

        ResultadoEJB<List<DtoCargaAcademica>> resCargas = ejbCapturaCalificaciones.getCargasAcadémicasPorPeriodo(rol.getDocenteLogueado(), rol.getPeriodoSeleccionado());
        if(!resCargas.getCorrecto()) mostrarMensajeResultadoEJB(resCargas);
        else rol.setCargasDocente(resCargas.getValor());

        //cambiarCarga();
    }

    public void cambiarCarga(){
        if(rol.getCargaAcademicaSeleccionada() == null){
            mostrarMensaje("No hay carga académica seleccionada");
            rol.setDtoUnidadConfiguraciones(Collections.EMPTY_LIST);
            return;
        }//verificar si se seleccionó una carga académica

        ResultadoEJB<List<DtoUnidadConfiguracion>> resConfiguraciones = ejbCapturaCalificaciones.getConfiguraciones(rol.getCargaAcademicaSeleccionada());//obtener las configuraciones de la carga académica seleccionada
        if(!resConfiguraciones.getCorrecto()) mostrarMensajeResultadoEJB(resConfiguraciones);
        else {
            rol.setDtoUnidadConfiguraciones(resConfiguraciones.getValor());//almacenar el valor en la capa SET
        }
        cambiarUnidad();
    }

    public void cambiarUnidad(){
        if(rol.getDtoUnidadConfiguracionSeleccionada() == null) {
            mostrarMensaje("No hay unidad de evaluación seleccionada.");
            rol.setEstudiantesPorGrupo(null);
            return;
        }
        ResultadoEJB<DtoGrupoEstudiante> resGrupo = packer.packGrupoEstudiante(rol.getCargaAcademicaSeleccionada(), rol.getDtoUnidadConfiguracionSeleccionada());
        if(!resGrupo.getCorrecto()) mostrarMensajeResultadoEJB(resGrupo);
        else {
            rol.setEstudiantesPorGrupo(resGrupo.getValor());
        }
    }
}
