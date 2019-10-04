package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ConsultaCalificacionesRolEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalificacionEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ConsultaCalificacionesEstudiante extends ViewScopedRol implements Desarrollable {

    @Getter @Setter private ConsultaCalificacionesRolEstudiante rol = new ConsultaCalificacionesRolEstudiante();
    @Getter @Setter Boolean tieneAcceso = false;
    @EJB
    EjbPropiedades ep;
    @EJB
    EjbConsultaCalificacion ejb;
    @Inject
    LogonMB logonMB;

    @PostConstruct
    public void init(){
        try {
            setVistaControlador(ControlEscolarVistaControlador.CONSULTA_CALIFICACION_ESTUDIANTE);
            ResultadoEJB<Estudiante> resAcceso = ejb.validadEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
            ResultadoEJB<Estudiante> resValidacion = ejb.validadEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
            Estudiante estudiante = resValidacion.getValor();
            tieneAcceso = rol.tieneAcceso(estudiante);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setEstudiante(estudiante);

            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}

            rol.setNivelRol(NivelRol.OPERATIVO);

            ResultadoEJB<PeriodosEscolares> resPeriodoAcitvo = ejb.periodoActivo();
            rol.setPeriodoActivo(resPeriodoAcitvo.getValor());
            rol.setPeriodoSeleccionado(rol.getPeriodoActivo().getPeriodo());

            ResultadoEJB<List<PeriodosEscolares>> resPeriodosEscolares = ejb.obtenerListaPeriodosEscolares();
            rol.setPeriodosEscolares(resPeriodosEscolares.getValor());

            mostrarCalificacionesPorEstudiante();

            ResultadoEJB<List<DtoCalificacionEstudiante.UnidadesPorMateria>> resUnidadesPorMateria = ejb.packUnidadesmateria(Integer.parseInt(logonMB.getCurrentUser()));
            rol.setUnidadesPorMateria(resUnidadesPorMateria.getValor().stream().filter(x -> x.getGrupo().getPeriodo() == rol.getPeriodoSeleccionado()).collect(Collectors.toList()));

            mapearUnidadesPorMateria();
            obtenerCalificaciones();
            obtenerTareaIntegradoraPorMateria();
            obtenerNivelacionesPorMateria();
            obtenerPromedioMateria();
            obtenerPromediosFinales();
            obtenerPromedioCuatrimestral();
            obtenerPromedioAcumulado();
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    public void mostrarCalificacionesPorEstudiante(){
        ResultadoEJB<List<DtoCalificacionEstudiante.MateriasPorEstudiante>> resMaterias = ejb.packMaterias(Integer.parseInt(logonMB.getCurrentUser()));
        rol.setMateriasPorEstudiante(resMaterias.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoSeleccionado()).collect(Collectors.toList()));
        Map<Integer, Long> group = rol.getMateriasPorEstudiante().stream().collect(Collectors.groupingBy(DtoCalificacionEstudiante.MateriasPorEstudiante::getPeriodo, Collectors.counting()));
        group.forEach((k, v) -> {
            rol.setPeriodo(k);
        });
    }

    public void mapearUnidadesPorMateria(){
        List<DtoCalificacionEstudiante.MapUnidadesTematicas> resMap = new ArrayList<>();
        Map<Integer, Long> map = rol.getUnidadesPorMateria().stream().collect(Collectors.groupingBy(DtoCalificacionEstudiante.UnidadesPorMateria::getNoUnidad, Collectors.counting()));
        map.forEach((k, v) -> {
            resMap.add(new DtoCalificacionEstudiante.MapUnidadesTematicas(k, v.intValue()));
        });
        rol.setMapUnidadesTematicas(resMap);
    }

    public void obtenerCalificaciones(){
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorUnidad>> resCalificaciones = ejb.packCalificacionesPorUnidadyMateria1(Integer.parseInt(logonMB.getCurrentUser()));
        rol.setCalificacionePorUnidad(resCalificaciones.getValor().stream().filter(a -> a.getEstudiante().getGrupo().getPeriodo() == rol.getPeriodoSeleccionado()).collect(Collectors.toList()));
    }

    public void obtenerPromedioMateria(){
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ejb.packPromedioMateria(Integer.parseInt(logonMB.getCurrentUser()));
        rol.setCalificacionePorMateria(resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoSeleccionado()).collect(Collectors.toList()));
    }

    public void obtenerPromediosFinales(){
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ejb.packCalificacionesFinales(Integer.parseInt(logonMB.getCurrentUser()));
        rol.setCalificacionesFinalesPorMateria(resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoSeleccionado()).collect(Collectors.toList()));
    }

    public void obtenerPromedioCuatrimestral(){
        ResultadoEJB<BigDecimal> promedio = ejb.obtenerPromedioCuatrimestral(Integer.parseInt(logonMB.getCurrentUser()), rol.getPeriodo());
        BigDecimal valor = promedio.getValor();
        rol.setMateriasPorEstudiante(ejb.packMaterias(Integer.parseInt(logonMB.getCurrentUser())).getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoSeleccionado()).collect(Collectors.toList()));
        BigDecimal numeroMaterias = new BigDecimal(rol.getMateriasPorEstudiante().size());
        BigDecimal promedioCuatrimestral = valor.divide(numeroMaterias, RoundingMode.FLOOR);
        rol.setPromedio(promedioCuatrimestral.setScale(2, RoundingMode.FLOOR));
    }

    public void obtenerPromedioAcumulado(){
        ResultadoEJB<List<BigDecimal>> promedios = ejb.obtenerPromedioAcumulado(Integer.parseInt(logonMB.getCurrentUser()));
        BigDecimal numeroPromedios = new BigDecimal(promedios.getValor ().size());
        BigDecimal suma = promedios.getValor().stream().map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal promedio = suma.divide(numeroPromedios, RoundingMode.FLOOR);
        rol.setPromedioAcumluado(promedio.setScale(2, RoundingMode.FLOOR));
    }

    public void obtenerTareaIntegradoraPorMateria(){
        ResultadoEJB<List<DtoCalificacionEstudiante.TareaIntegradoraPorMateria>> resultadoEJB = ejb.packTareaIntegradora(Integer.parseInt(logonMB.getCurrentUser()));
        rol.setTareaIntegradoraPorMateria(resultadoEJB.getValor());
    }

    public void obtenerNivelacionesPorMateria(){
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria>> resultadoEJB = ejb.packPromedioNivelacionPorMateria(Integer.parseInt(logonMB.getCurrentUser()));
        rol.setCalificacionesNivelacionPorMateria(resultadoEJB.getValor());
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "consulta de calificaciones por estudiante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

}
