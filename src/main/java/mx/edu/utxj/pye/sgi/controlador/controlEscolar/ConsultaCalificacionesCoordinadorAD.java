package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ConsultaCalificacionesRolCoordinadorAD;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalificacionEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacionesCoordinadorAD;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



@Named
@ViewScoped
public class ConsultaCalificacionesCoordinadorAD extends ViewScopedRol implements Desarrollable {

    @Getter @Setter private ConsultaCalificacionesRolCoordinadorAD rol;
    @Getter @Setter Boolean tieneAcceso = false;
    @EJB EjbPropiedades ep;
    @EJB EjbConsultaCalificacionesCoordinadorAD ejb;
    @EJB EjbConsultaCalificacion ejbC;
    @Inject LogonMB logonMB;



@Getter private Boolean cargado = false;


    @PostConstruct
    public void init(){
        try {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.CONSULTA_CALIFICACION_COORDINADOR);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarCoordinador(logonMB.getPersonal().getClave());
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarCoordinador(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo coordinador = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ConsultaCalificacionesRolCoordinadorAD(filtro, coordinador, coordinador.getAreaOficial());
            tieneAcceso = rol.tieneAcceso(coordinador);
//            System.out.println("tieneAcceso1 = " + tieneAcceso);
            if(!tieneAcceso){
                rol.setFiltro(resValidacion.getValor());
                tieneAcceso = rol.tieneAcceso(coordinador);
            }
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setCoordinador(coordinador);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            rol.setNivelRol(NivelRol.OPERATIVO);

            rol.setPeriodosEscolar(ejbC.getPeriodoActual());
            rol.setPeriodoSelect(rol.getPeriodosEscolar().getPeriodo());

            rol.setEstudiantes(ejb.obtenerEstudiantesActivos(rol.getPeriodosEscolar().getPeriodo()).getValor());

            ResultadoEJB<List<PeriodosEscolares>> resPeriodosEscolares = ejb.obtenerListaPeriodosEscolares();
            rol.setPeriodosEscolares(resPeriodosEscolares.getValor());

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    public void obtenerCalificacionesPorEstudiante(){
        rol.setEstudiante(ejb.obtenerEstudiante(rol.getMatricula()).getValor());
        obtenerMateriasPorEstudiante(rol.getEstudiante());
        obtenerUnidadesPorMateria(rol.getEstudiante());
        obtenerCalificaciones(rol.getEstudiante());
        obtenerPromedioMateria(rol.getEstudiante());
        obtenerPromediosFinales(rol.getEstudiante());
        obtenerPromedioCuatrimestral(rol.getEstudiante());
//        obtenerPromedioAcumulado(rol.getEstudiante());
        obtenerTareaIntegradoraPorMateria(rol.getEstudiante());
        obtenerNivelacionesPorMateria(rol.getEstudiante());
    }

    public void obtenerMateriasPorEstudiante(Estudiante estudiante) {
        ResultadoEJB<List<DtoCalificacionEstudiante.MateriasPorEstudiante>> resMaterias = ejbC.packMaterias(estudiante);
        rol.setMateriasPorEstudiante(resMaterias.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoSelect()).collect(Collectors.toList()));
        Map<Integer, Long> group = rol.getMateriasPorEstudiante().stream().collect(Collectors.groupingBy(DtoCalificacionEstudiante.MateriasPorEstudiante::getPeriodo, Collectors.counting()));
        group.forEach((k, v) -> {
            rol.setPeriodo(k);
        });
    }

    public void obtenerUnidadesPorMateria(Estudiante estudiante) {
        List<DtoCalificacionEstudiante.MapUnidadesTematicas> resMap = new ArrayList<>();
        ResultadoEJB<List<DtoCalificacionEstudiante.UnidadesPorMateria>> resUnidadesPorMateria = ejbC.packUnidadesmateria(estudiante);
        rol.setUnidadesPorMateria(resUnidadesPorMateria.getValor());
        rol.getUnidadesPorMateria().forEach(x -> {
            x.getUnidadMateriaConfiguracion().forEach(y -> {
                resMap.add(new DtoCalificacionEstudiante.MapUnidadesTematicas(y.getIdUnidadMateria().getNoUnidad(), y.getIdUnidadMateria().getNoUnidad()));
            });
        });
        rol.setMapUnidadesTematicas(new ArrayList<>(new HashSet<>(resMap)));
        rol.getMapUnidadesTematicas().sort(Comparator.comparingInt(DtoCalificacionEstudiante.MapUnidadesTematicas::getNoUnidad));
    }

    public void obtenerCalificaciones(Estudiante estudiante) {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorUnidad>> resCalificaciones = ejbC.packCalificacionesPorUnidadyMateria1(estudiante);
        rol.setCalificacionePorUnidad(resCalificaciones.getValor().stream().filter(a -> a.getEstudiante().getGrupo().getPeriodo() == rol.getPeriodoSelect()).collect(Collectors.toList()));
    }

    public void obtenerPromedioMateria(Estudiante estudiante) {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ejbC.packPromedioMateria(estudiante);
        rol.setCalificacionePorMateria(resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoSelect()).collect(Collectors.toList()));
    }

    public void obtenerPromediosFinales(Estudiante estudiante) {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ejbC.packCalificacionesFinales(estudiante);
        rol.setCalificacionesFinalesPorMateria(resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoSelect()).collect(Collectors.toList()));
    }

    public void obtenerPromedioCuatrimestral(Estudiante estudiante) {
        ResultadoEJB<BigDecimal> promedio = ejbC.obtenerPromedioCuatrimestral(estudiante, rol.getPeriodo());
        BigDecimal valor = promedio.getValor();
        rol.setMateriasPorEstudiante(ejbC.packMaterias(rol.getEstudiante()).getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoSelect()).collect(Collectors.toList()));
        BigDecimal numeroMaterias = new BigDecimal(rol.getMateriasPorEstudiante().size());
        BigDecimal promedioCuatrimestral = valor.divide(numeroMaterias, RoundingMode.HALF_UP);
        rol.setPromedio(promedioCuatrimestral.setScale(1, RoundingMode.HALF_UP));
    }

    public void obtenerPromedioAcumulado(Estudiante estudiante) {
        ResultadoEJB<List<BigDecimal>> promedios = ejbC.obtenerPromedioAcumulado(estudiante);
        BigDecimal numeroPromedios = new BigDecimal(promedios.getValor ().size());
        BigDecimal suma = promedios.getValor().stream().map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal promedio = suma.divide(numeroPromedios, RoundingMode.HALF_UP);
        rol.setPromedioAcumluado(promedio.setScale(1, RoundingMode.HALF_UP));
    }

    public void obtenerTareaIntegradoraPorMateria(Estudiante estudiante){
        ResultadoEJB<List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion>> resultadoEJB = ejbC.tareaIntegradoraPresentacion(estudiante);
        rol.setTareaIntegradoraPresentacion(resultadoEJB.getValor());
    }

    public void obtenerNivelacionesPorMateria(Estudiante estudiante){
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria>> resultadoEJB = ejbC.packPromedioNivelacionPorMateria(estudiante);
        rol.setCalificacionesNivelacionPorMateria(resultadoEJB.getValor());
    }


    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "consulta de calificaciones por coordinador";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

}
