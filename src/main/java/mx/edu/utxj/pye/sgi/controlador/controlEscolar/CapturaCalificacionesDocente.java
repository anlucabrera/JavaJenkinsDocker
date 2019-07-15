package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CapturaCalificacionesRolDocente;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoGrupoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadConfiguracion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaCalificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
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
    @Getter Boolean tieneAcceso = false;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "captura de calificaciones";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    @PostConstruct
    public void init(){
        ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDocente(logon.getPersonal().getClave());
        System.out.println("resAcceso = " + resAcceso);
        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

        rol = new CapturaCalificacionesRolDocente(resAcceso.getValor());

        tieneAcceso = rol.tieneAcceso(rol.getDocenteLogueado());
        System.out.println("tieneAcceso = " + tieneAcceso);

        ResultadoEJB<EventoEscolar> resEvento = ejb.verificarEvento(rol.getDocenteLogueado());
        System.out.println("resEvento = " + resEvento);
        if(!resEvento.getCorrecto()) resEvento = ejb.verificarEventoExtemporaneo(rol.getDocenteLogueado());
        ResultadoEJB<List<DtoUnidadConfiguracion>> resUnidades = null;
        if(!resEvento.getCorrecto()) {
            resUnidades = ejb.getUnidadesEnEvaluacion(rol.getDocenteLogueado());
            System.out.println("resUnidades = " + resUnidades);
            if(!resUnidades.getCorrecto()){ tieneAcceso = false;return;}
        }//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
        ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosConCaptura(rol.getDocenteLogueado());
        System.out.println("resPeriodos = " + resPeriodos);
        if(!resPeriodos.getCorrecto()) {mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
        if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
        if(!resEvento.getCorrecto() && (resUnidades!= null && !resUnidades.getCorrecto())) mostrarMensajeResultadoEJB(resEvento);
        rol.setNivelRol(NivelRol.OPERATIVO);

        if(resEvento.getCorrecto()) {//verificar si la apertura es por evento
            rol.setEventoActivo(resEvento.getValor());
            rol.setPeriodoActivo(resEvento.getValor().getPeriodo());
        }else if(resUnidades!=null && resUnidades.getCorrecto())//verificar si la apertura es por planeación del docente
            rol.setPeriodoActivo(resUnidades.getValor().get(0).getDtoCargaAcademica().getPeriodo().getPeriodo());

        rol.setPeriodosConCarga(resPeriodos.getValor());

        actualizar();
    }

    public void actualizar(){
//        System.out.println("CapturaCalificacionesDocente.actualizar");
        if(rol.getPeriodoSeleccionado() == null) { mostrarMensaje("No hay periodo escolar seleccionado.");return;}//detener flujo si no hay periodo seleccionado

        //actualizar cargas
        ResultadoEJB<List<DtoCargaAcademica>> resCargas = ejb.getCargasAcadémicasPorPeriodo(rol.getDocenteLogueado(), rol.getPeriodoSeleccionado());
        if(!resCargas.getCorrecto()) { mostrarMensajeResultadoEJB(resCargas);return; }
        rol.setCargasDocente(resCargas.getValor());
//        System.out.println("rol.getCargaAcademicaSeleccionada() = " + rol.getCargaAcademicaSeleccionada());

        //actualizar configuracion de unidades
        if(rol.getCargaAcademicaSeleccionada() == null){ mostrarMensaje("No hay carga académica seleccionada");return;}//verificar si se seleccionó una carga académica
        ResultadoEJB<List<DtoUnidadConfiguracion>> resConfiguraciones = ejb.getConfiguraciones(rol.getCargaAcademicaSeleccionada());//obtener las configuraciones de la carga académica seleccionada
        if(!resConfiguraciones.getCorrecto()){mostrarMensajeResultadoEJB(resConfiguraciones); return;} //verificar si se obtuvo resultado
        rol.setDtoUnidadConfiguraciones(resConfiguraciones.getValor());//almacenar el valor en la capa SET
//        System.out.println("rol.getDtoUnidadConfiguracionSeleccionada() = " + rol.getDtoUnidadConfiguracionSeleccionada().getUnidadMateria().getNombre());

        //actualizar estudiantes del grupo correspondientes a la carga académica seleccionada
        if(rol.getDtoUnidadConfiguracionSeleccionada() == null) { mostrarMensaje("No hay unidad de evaluación seleccionada."); return;}
        ResultadoEJB<DtoGrupoEstudiante> resGrupo = packer.packGrupoEstudiante(rol.getCargaAcademicaSeleccionada(), rol.getDtoUnidadConfiguracionSeleccionada());
//        System.out.println("resGrupo = " + resGrupo.getValor().getEstudiantes().size());
        rol.setEstudiantesPorGrupo(resGrupo.getValor());
        System.out.println("rol.getDtoUnidadConfiguracionSeleccionada() = " + rol.getDtoUnidadConfiguracionSeleccionada());
        //TODO: agregar al grupo la alineacion con la materia de la unidad seleccionada
    }
}
