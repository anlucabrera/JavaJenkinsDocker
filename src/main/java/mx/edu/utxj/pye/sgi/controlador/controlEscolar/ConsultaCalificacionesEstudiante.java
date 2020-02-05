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
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
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

                rol.setNivelRol(NivelRol.CONSULTA);

                ResultadoEJB<List<Estudiante>> resEstudiantes = ejb.obtenerRegistrosEstudiante(rol.getEstudiante());
                rol.setEstudiantes(resEstudiantes.getValor());


            }else{
                return;
            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    public List<DtoCalificacionEstudiante.MapUnidadesTematicas> obtenerUnidadesPorMateria(@NonNull Estudiante estudiante) {
        List<DtoCalificacionEstudiante.MapUnidadesTematicas> resMap = new ArrayList<>();
        ResultadoEJB<List<DtoCalificacionEstudiante.UnidadesPorMateria>> resUnidadesPorMateria = ejb.packUnidadesmateria(estudiante);
        rol.setUnidadesPorMateria(resUnidadesPorMateria.getValor());
        rol.getUnidadesPorMateria().forEach(x -> {
            x.getUnidadMateriaConfiguracion().forEach(y -> {
                resMap.add(new DtoCalificacionEstudiante.MapUnidadesTematicas(y.getIdUnidadMateria().getNoUnidad(), y.getIdUnidadMateria().getNoUnidad()));
            });
        });
        rol.setMapUnidadesTematicas(new ArrayList<>(new HashSet<>(resMap)));
        rol.getMapUnidadesTematicas().sort(Comparator.comparingInt(DtoCalificacionEstudiante.MapUnidadesTematicas::getNoUnidad));
        return rol.getMapUnidadesTematicas();
    }

    public List<DtoCargaAcademica> getCargasAcademicas(@NonNull Estudiante estudiante){
        ResultadoEJB<List<DtoCargaAcademica>> resCargas = ejb.obtenerCargasAcademicas(estudiante);
        if(!resCargas.getCorrecto()) mostrarMensajeResultadoEJB(resCargas);
        else rol.setCargasEstudiante(resCargas.getValor());
        return rol.getCargasEstudiante();
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

    public DtoUnidadesCalificacionEstudiante getContenedor(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull Estudiante estudiante){
        if(rol.getDtoUnidadesCalificacionMap().containsKey(dtoCargaAcademica)) return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
        ResultadoEJB<DtoUnidadesCalificacionEstudiante> resDtoUnidadesCalificacion = ejb.packDtoUnidadesCalificacion(estudiante, getUnidades(dtoCargaAcademica));
        if(!resDtoUnidadesCalificacion.getCorrecto()){
            mostrarMensaje("No se detectaron registros de calificaciones de la carga seleccionada. " + resDtoUnidadesCalificacion.getMensaje());
            return null;
        }

        rol.getDtoUnidadesCalificacionMap().put(dtoCargaAcademica, resDtoUnidadesCalificacion.getValor());
        return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
    }

    public  Boolean tieneIntegradora(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull Estudiante estudiante){
        if(rol.getTieneIntegradoraMap().containsKey(dtoCargaAcademica)) return rol.getTieneIntegradoraMap().get(dtoCargaAcademica);

        ResultadoEJB<TareaIntegradora> tareaIntegradoraResultadoEJB = ejbCapturaTareaIntegradora.verificarTareaIntegradora(dtoCargaAcademica);
        if(tareaIntegradoraResultadoEJB.getCorrecto()) {
            rol.getTieneIntegradoraMap().put(dtoCargaAcademica, true);
            rol.getTareaIntegradoraMap().put(dtoCargaAcademica, tareaIntegradoraResultadoEJB.getValor());
            rol.setTieneIntegradora(Boolean.TRUE);

            ResultadoEJB<Map<DtoCargaAcademica, TareaIntegradoraPromedio>> generarContenedorCalificaciones = ejb.generarContenedorCalificaciones(getContenedor(dtoCargaAcademica, estudiante).getDtoCargaAcademicas(), estudiante, tareaIntegradoraResultadoEJB.getValor());
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

    public DtoCalificacionNivelacion getNivelacion(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull Estudiante estudiante){
        DtoUnidadesCalificacionEstudiante.DtoNivelacionPK pk = new DtoUnidadesCalificacionEstudiante.DtoNivelacionPK(dtoCargaAcademica, estudiante);
        if(getContenedor(dtoCargaAcademica, estudiante).getNivelacionMap().containsKey(pk)) return getContenedor(dtoCargaAcademica, estudiante).getNivelacionMap().get(pk);
        ResultadoEJB<DtoCalificacionNivelacion> packDtoCalificacionNivelacion = ejb.packDtoCalificacionNivelacion(dtoCargaAcademica, estudiante);
        if(packDtoCalificacionNivelacion.getCorrecto()){
            @NonNull DtoCalificacionNivelacion dtoCalificacionNivelacion = packDtoCalificacionNivelacion.getValor();
            getContenedor(dtoCargaAcademica, estudiante).getNivelacionMap().put(pk, dtoCalificacionNivelacion);
            return getContenedor(dtoCargaAcademica, estudiante).getNivelacionMap().get(pk);
        }else {
            mostrarMensajeResultadoEJB(packDtoCalificacionNivelacion);
            return null;
        }
    }

    public BigDecimal getPromedioAsignaturaEstudiante(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull Estudiante estudiante){
        ResultadoEJB<BigDecimal> res = ejb.promediarAsignatura(getContenedor(dtoCargaAcademica, estudiante), dtoCargaAcademica, estudiante);
        if(res.getCorrecto()){
            return res.getValor().setScale(2, RoundingMode.HALF_UP);
        }else{
            mostrarMensaje(
                    String.format("El promedio del estudiante %s %s %s con matrícula %s, no se pudo calcular.", rol.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno(),
                            rol.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno(),
                            rol.getEstudiante().getAspirante().getIdPersona().getNombre(), rol.getEstudiante().getMatricula()));
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getPromedioFinal(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull Estudiante estudiante){
        BigDecimal promedioOrdinario = getPromedioAsignaturaEstudiante(dtoCargaAcademica, estudiante).setScale(2, RoundingMode.HALF_UP);
        BigDecimal nivelacion = new BigDecimal(getNivelacion(dtoCargaAcademica, estudiante).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal promedioFinal = BigDecimal.ZERO;
        if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
            promedioFinal = promedioFinal.add(promedioOrdinario);
        }else{
            promedioFinal = promedioFinal.add(nivelacion);
        }
        return promedioFinal;
    }

    public BigDecimal getPromedioCuatrimestral(@NonNull Estudiante estudiante){
        BigDecimal promedioCuatrimestral = BigDecimal.ZERO;
        ResultadoEJB<List<DtoCargaAcademica>> resCargas = ejb.obtenerCargasAcademicas(estudiante);
        List<BigDecimal> lista = new ArrayList<>();
        if(!resCargas.getCorrecto()) mostrarMensajeResultadoEJB(resCargas);
        else rol.setCargasEstudiante(resCargas.getValor());
        rol.getCargasEstudiante().forEach(dtoCargaAcademica -> {
            lista.add(getPromedioFinal(dtoCargaAcademica, estudiante));
        });
        BigDecimal totalMaterias = new BigDecimal(rol.getCargasEstudiante().size());
        BigDecimal suma = lista.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        promedioCuatrimestral = suma.divide(totalMaterias, RoundingMode.HALF_UP);
        return promedioCuatrimestral.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal obtenerPromedioAcumulado(){
        BigDecimal promedio;
        BigDecimal totalRegistro = new BigDecimal(rol.getEstudiantes().size());
        BigDecimal suma;
        List<BigDecimal> promedios = new ArrayList<>();
        rol.getEstudiantes().forEach(estudiante -> {
            promedios.add(getPromedioCuatrimestral(estudiante));
        });
        suma = promedios.stream().map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add);
        promedio = suma.divide(totalRegistro).setScale(2, RoundingMode.HALF_UP);
        return promedio;
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "consulta de calificaciones por estudiante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

}
