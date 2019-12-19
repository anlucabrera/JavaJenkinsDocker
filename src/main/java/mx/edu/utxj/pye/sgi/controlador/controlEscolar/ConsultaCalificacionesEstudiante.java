package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.*;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaCalificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaTareaIntegradora;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Indicador;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradora;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradoraPromedio;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ConsultaCalificacionesEstudiante extends ViewScopedRol implements Desarrollable {

    @Getter @Setter private ConsultaCalificacionesRolEstudiante rol = new ConsultaCalificacionesRolEstudiante();
    @Getter @Setter Boolean tieneAcceso = false;
    @EJB EjbPropiedades ep;
    @EJB EjbConsultaCalificacion ejb;
    @EJB EjbCapturaCalificaciones ejbCapturaCalificaciones;
    @EJB EjbCapturaTareaIntegradora ejbCapturaTareaIntegradora;
    @Inject LogonMB logonMB;

    @PostConstruct
    public void init(){
        try {
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)){
                setVistaControlador(ControlEscolarVistaControlador.CONSULTA_CALIFICACION_ESTUDIANTE);
                ResultadoEJB<Estudiante> resAcceso = ejb.validadEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
                ResultadoEJB<Estudiante> resValidacion = ejb.validadEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
                Estudiante estudiante = resValidacion.getValor();
                tieneAcceso = rol.tieneAcceso(estudiante, UsuarioTipo.ESTUDIANTE19);
                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

                rol.setEstudiante(estudiante);

                //////////////////////////////////////////////////////////////////////////////////////////////////////////
                if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
                if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

                rol.setNivelRol(NivelRol.OPERATIVO);

                PeriodosEscolares resPeriodoAcitvo = ejb.getPeriodoActual();
                rol.setPeriodoActivo(resPeriodoAcitvo);
                rol.setPeriodoSeleccionado(rol.getPeriodoActivo().getPeriodo());

                ResultadoEJB<List<PeriodosEscolares>> resPeriodosEscolares = ejb.obtenerListaPeriodosEscolares();
                rol.setPeriodosEscolares(resPeriodosEscolares.getValor());

                ResultadoEJB<List<DtoCargaAcademica>> resCargas = ejb.obtenerCargasAcadémicas(rol.getEstudiante());
                if(!resCargas.getCorrecto()) mostrarMensajeResultadoEJB(resCargas);
                else rol.setCargasEstudiante(resCargas.getValor().stream().filter(x -> x.getPeriodo().getPeriodo().equals(rol.getPeriodoSeleccionado())).collect(Collectors.toList()));
                obtenerUnidadesPorMateria();

            }else{
                return;
            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    public void obtenerUnidadesPorMateria() {
        List<DtoCalificacionEstudiante.MapUnidadesTematicas> resMap = new ArrayList<>();
        ResultadoEJB<List<DtoCalificacionEstudiante.UnidadesPorMateria>> resUnidadesPorMateria = ejb.packUnidadesmateria(rol.getEstudiante());
        rol.setUnidadesPorMateria(resUnidadesPorMateria.getValor());
        rol.getUnidadesPorMateria().forEach(x -> {
            x.getUnidadMateriaConfiguracion().forEach(y -> {
                resMap.add(new DtoCalificacionEstudiante.MapUnidadesTematicas(y.getIdUnidadMateria().getNoUnidad(), y.getIdUnidadMateria().getNoUnidad()));
            });
        });
        rol.setMapUnidadesTematicas(new ArrayList<>(new HashSet<>(resMap)));
        rol.getMapUnidadesTematicas().sort(Comparator.comparingInt(DtoCalificacionEstudiante.MapUnidadesTematicas::getNoUnidad));
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

    public DtoUnidadesCalificacionEstudiante getContenedor(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getDtoUnidadesCalificacionMap().containsKey(dtoCargaAcademica)) return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
        ResultadoEJB<DtoUnidadesCalificacionEstudiante> resDtoUnidadesCalificacion = ejb.packDtoUnidadesCalificacion(rol.getEstudiante(), getUnidades(dtoCargaAcademica));
        if(!resDtoUnidadesCalificacion.getCorrecto()){
            mostrarMensaje("No se detectaron registros de calificaciones de la carga seleccionada. " + resDtoUnidadesCalificacion.getMensaje());
            return null;
        }

        rol.getDtoUnidadesCalificacionMap().put(dtoCargaAcademica, resDtoUnidadesCalificacion.getValor());
        return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
    }

    public  Boolean tieneIntegradora(@NonNull DtoCargaAcademica dtoCargaAcademica){
        if(rol.getTieneIntegradoraMap().containsKey(dtoCargaAcademica)) return rol.getTieneIntegradoraMap().get(dtoCargaAcademica);

        ResultadoEJB<TareaIntegradora> tareaIntegradoraResultadoEJB = ejbCapturaTareaIntegradora.verificarTareaIntegradora(dtoCargaAcademica);
        if(tareaIntegradoraResultadoEJB.getCorrecto()) {
            rol.getTieneIntegradoraMap().put(dtoCargaAcademica, true);
            rol.getTareaIntegradoraMap().put(dtoCargaAcademica, tareaIntegradoraResultadoEJB.getValor());
            rol.setTieneIntegradora(Boolean.TRUE);

            ResultadoEJB<Map<DtoCargaAcademica, TareaIntegradoraPromedio>> generarContenedorCalificaciones = ejb.generarContenedorCalificaciones(getContenedor(dtoCargaAcademica).getDtoCargaAcademicas(), rol.getEstudiante(), tareaIntegradoraResultadoEJB.getValor());
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

    public DtoCalificacionNivelacion getNivelacion(@NonNull DtoCargaAcademica dtoCargaAcademica){
        DtoUnidadesCalificacionEstudiante.DtoNivelacionPK pk = new DtoUnidadesCalificacionEstudiante.DtoNivelacionPK(dtoCargaAcademica, rol.getEstudiante());
        if(getContenedor(dtoCargaAcademica).getNivelacionMap().containsKey(pk)) return getContenedor(dtoCargaAcademica).getNivelacionMap().get(pk);
        ResultadoEJB<DtoCalificacionNivelacion> packDtoCalificacionNivelacion = ejb.packDtoCalificacionNivelacion(dtoCargaAcademica, rol.getEstudiante());
        if(packDtoCalificacionNivelacion.getCorrecto()){
            @NonNull DtoCalificacionNivelacion dtoCalificacionNivelacion = packDtoCalificacionNivelacion.getValor();
            getContenedor(dtoCargaAcademica).getNivelacionMap().put(pk, dtoCalificacionNivelacion);
            return getContenedor(dtoCargaAcademica).getNivelacionMap().get(pk);
        }else {
            mostrarMensajeResultadoEJB(packDtoCalificacionNivelacion);
            return null;
        }
    }

    public BigDecimal getPromedioAsignaturaEstudiante(@NonNull DtoCargaAcademica dtoCargaAcademica){
        ResultadoEJB<BigDecimal> res = ejb.promediarAsignatura(getContenedor(dtoCargaAcademica), dtoCargaAcademica, rol.getEstudiante());
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensaje(
                    String.format("El promedio del estudiante %s %s %s con matrícula %s, no se pudo calcular.", rol.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno(),
                            rol.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno(),
                            rol.getEstudiante().getAspirante().getIdPersona().getNombre(), rol.getEstudiante().getMatricula()));
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getPromedioFinal(@NonNull DtoCargaAcademica dtoCargaAcademica){
        BigDecimal promedioOrdinario = getPromedioAsignaturaEstudiante(dtoCargaAcademica);
        BigDecimal nivelacion = new BigDecimal(getNivelacion(dtoCargaAcademica).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal promedioFinal = BigDecimal.ZERO;
        if(promedioOrdinario.compareTo(BigDecimal.valueOf(8)) >= 0){
            promedioFinal = promedioFinal.add(promedioOrdinario);
        }else {
            promedioFinal = promedioFinal.add(nivelacion);
        }
        return promedioFinal;
    }

    public BigDecimal getPromedioCuatrimestral(){
        BigDecimal promedioCuatrimestral = BigDecimal.ZERO;
        ResultadoEJB<List<DtoCargaAcademica>> resCargas = ejb.obtenerCargasAcadémicas(rol.getEstudiante());
        List<BigDecimal> lista = new ArrayList<>();
        if(!resCargas.getCorrecto()) mostrarMensajeResultadoEJB(resCargas);
        else rol.setCargasEstudiante(resCargas.getValor().stream().filter(x -> x.getPeriodo().getPeriodo().equals(rol.getPeriodoSeleccionado())).collect(Collectors.toList()));
        rol.getCargasEstudiante().forEach(dtoCargaAcademica -> {
            lista.add(getPromedioFinal(dtoCargaAcademica));
        });
        BigDecimal totalMaterias = new BigDecimal(rol.getCargasEstudiante().size());
        BigDecimal suma = lista.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        promedioCuatrimestral = suma.divide(totalMaterias, RoundingMode.HALF_UP);
        return promedioCuatrimestral.setScale(1, RoundingMode.HALF_UP);
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "consulta de calificaciones por estudiante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

}
