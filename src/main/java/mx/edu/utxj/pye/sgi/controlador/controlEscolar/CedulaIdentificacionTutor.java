package mx.edu.utxj.pye.sgi.controlador.controlEscolar;


import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CedulaIdetificacionRolTutor;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalificacionEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class CedulaIdentificacionTutor extends ViewScopedRol implements Desarrollable {
    @Getter @Setter CedulaIdetificacionRolTutor rol;
    @EJB EjbPropiedades ep;
    @EJB EjbValidacionRol evr;
    @EJB EjbRegistroPlanEstudio ejb;
    @EJB EjbCedulaIdentificacion ejbCedulaIdentificacion;
    @EJB EjbConsultaCalificacion ejbCalificaiones;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    @PostConstruct
    public void init(){
        try {
            setVistaControlador(ControlEscolarVistaControlador.CEDULA_IDENTIFICACION_TUTOR);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = evr.validarTutor(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            rol = new CedulaIdetificacionRolTutor(resAcceso.getValor());
            tieneAcceso = rol.tieneAcceso(rol.getTutor());
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            rol.setNivelRol(NivelRol.CONSULTA);

            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());
            ResultadoEJB<PeriodosEscolares> resPeriodo = ejbCedulaIdentificacion.getPeriodoActual();
            if(resPeriodo.getCorrecto()==true){rol.setPeriodo(resPeriodo.getValor());}
            else {mostrarMensajeResultadoEJB(resPeriodo);}
            ResultadoEJB<List<Grupo>> resgrupos = ejb.getListaGrupoPorTutor(rol.getTutor(),rol.getPeriodo());
            if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
            rol.setGrupos(resgrupos.getValor());
            rol.setGrupoSelec(rol.getGrupos().get(0));

            ResultadoEJB<List<Listaalumnosca>> rejb = ejb.getListaAlumnosPorGrupo(rol.getGrupoSelec());
            if(!rejb.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
            rol.setListaEstudiantes(rejb.getValor());
            //TODO: Apartados
            rol.setApartados(ejbCedulaIdentificacion.getApartados());
            //TODO: Instrucciones
            rol.getInstrucciones().add("Seleccione el grupo al que pertenece el estudiante");
            rol.getInstrucciones().add("Ingrese la matricula o el nombre del estudiante");
            rol.getInstrucciones().add("Los datos del estudiante se cargarán automaticamente");
            rol.getInstrucciones().add("NOTA IMPORTANTE: La coordinación de desarollo de software sigue trabajando en algunos datos que son necesarios en la cédula de identificación, los cuales se estarán liberando en las próximos días.");
            rol.getInstrucciones().add("NOTA IMPORNTATE: La mayoría de los datos recabados forman parte del proceso de inscripción del estudiante y se tienen almacenados en nuestras bases de datos, si existen campos vacios, es porque la información no ha sido recabada.");

        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }
    /**
     * Obtiene a los estudiantes del tutor
     * @param event
     */
    public void consultarAlumnos(ValueChangeEvent event) {
        rol.setListaEstudiantes(new ArrayList<>());
        rol.setGrupoSelec((Grupo) event.getNewValue());
        ResultadoEJB<List<Listaalumnosca>> rejb = ejb.getListaAlumnosPorGrupo(rol.getGrupoSelec());
        if(!rejb.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
        rol.setListaEstudiantes(rejb.getValor());
    }

    //TODO: Busca los datos del estudiante
    public void getEstudiante(){
        if(rol.getMatricula()== null) return;
        //TODO:Busca al estudiante
        ResultadoEJB<Estudiante> resEstudiante = ejbCedulaIdentificacion.validaEstudiante(Integer.parseInt(rol.getMatricula()));
        if(resEstudiante.getCorrecto()==true){
            rol.setEstudiante(resEstudiante.getValor());
            //TODO: Se ejecuta el metodo de busqueda
            ResultadoEJB<DtoCedulaIdentificacion> resCedula = ejbCedulaIdentificacion.getCedulaIdentificacion(Integer.parseInt(rol.getMatricula()));
            if(resCedula.getCorrecto()==true){
                //System.out.println("Entro a genera cedula");
                rol.setCedulaIdentificacion(resCedula.getValor());
                // System.out.println(resCedula.getValor());
                getPeriodoEstudiante();
               obtenerMateriasPorEstudiante();
                obtenerUnidadesPorMateria();
                obtenerCalificaciones();
                obtenerPromedioMateria();
                obtenerTareaIntegradoraPorMateria();
                obtenerNivelacionesPorMateria();
                obtenerPromediosFinales();
                obtenerPromedioCuatrimestral();
                //obtenerPromedioAcumulado();
            }else{
                mostrarMensajeResultadoEJB(resCedula);
            }

        }

    }

    public void getPeriodoEstudiante(){
        PeriodosEscolares periodo = new PeriodosEscolares();
        ResultadoEJB<PeriodosEscolares> resPeriodo= ejbCedulaIdentificacion.getPeriodoEstudiante(rol.getEstudiante());
        if(resPeriodo.getCorrecto()==true){rol.setPeriodoEstudiante(resPeriodo.getValor());rol.setPeriodoE(rol.getPeriodoEstudiante().getPeriodo());}
        else {mostrarMensajeResultadoEJB(resPeriodo);}
    }
    //----------------------- Calificaciones del estudiante(Marce)---------------------

    public void obtenerMateriasPorEstudiante() {
        ResultadoEJB<List<DtoCalificacionEstudiante.MateriasPorEstudiante>> resMaterias = ejbCalificaiones.packMaterias(rol.getEstudiante());
        rol.setMateriasPorEstudiante(resMaterias.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
        Map<Integer, Long> group = rol.getMateriasPorEstudiante().stream().collect(Collectors.groupingBy(DtoCalificacionEstudiante.MateriasPorEstudiante::getPeriodo, Collectors.counting()));
        group.forEach((k, v) -> {
            rol.setPeriodoX(k);
        });
    }
    public void obtenerUnidadesPorMateria() {
        List<DtoCalificacionEstudiante.MapUnidadesTematicas> resMap = new ArrayList<>();
        ResultadoEJB<List<DtoCalificacionEstudiante.UnidadesPorMateria>> resUnidadesPorMateria = ejbCalificaiones.packUnidadesmateria(rol.getEstudiante());
        rol.setUnidadesPorMateria(resUnidadesPorMateria.getValor().stream().filter(x -> x.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
        Map<Integer, Long> map = rol.getUnidadesPorMateria().stream().collect(Collectors.groupingBy(DtoCalificacionEstudiante.UnidadesPorMateria::getNoUnidad, Collectors.counting()));
        map.forEach((k, v) -> {
            resMap.add(new DtoCalificacionEstudiante.MapUnidadesTematicas(k, v.intValue()));
        });
        rol.setMapUnidadesTematicas(resMap);
    }
    public void obtenerCalificaciones() {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorUnidad>> resCalificaciones = ejbCalificaiones.packCalificacionesPorUnidadyMateria1(rol.getEstudiante());
        rol.setCalificacionePorUnidad(resCalificaciones.getValor().stream().filter(a -> a.getEstudiante().getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
    }
    public void obtenerPromedioMateria() {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ejbCalificaiones.packPromedioMateria(rol.getEstudiante());
        rol.setCalificacionePorMateria(resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
    }
    public void obtenerPromediosFinales() {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ejbCalificaiones.packCalificacionesFinales(rol.getEstudiante());
        rol.setCalificacionesFinalesPorMateria(resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
    }
    public void obtenerPromedioCuatrimestral() {
        ResultadoEJB<BigDecimal> promedio = ejbCalificaiones.obtenerPromedioCuatrimestral( rol.getEstudiante(), rol.getPeriodoX());
        BigDecimal valor = promedio.getValor();
        rol.setMateriasPorEstudiante(ejbCalificaiones.packMaterias(rol.getEstudiante()).getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
        BigDecimal numeroMaterias = new BigDecimal(rol.getMateriasPorEstudiante().size());
        BigDecimal promedioCuatrimestral = valor.divide(numeroMaterias, RoundingMode.HALF_UP);
        rol.setPromedio(promedioCuatrimestral.setScale(1, RoundingMode.HALF_UP));
    }
    public void obtenerPromedioAcumulado() {
        ResultadoEJB<List<BigDecimal>> promedios = ejbCalificaiones.obtenerPromedioAcumulado(rol.getEstudiante());
        BigDecimal numeroPromedios = new BigDecimal(promedios.getValor ().size());
        BigDecimal suma = promedios.getValor().stream().map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal promedio = suma.divide(numeroPromedios, RoundingMode.HALF_UP);
        rol.setPromedioAcumluado(promedio.setScale(1, RoundingMode.HALF_UP));
    }

    public void obtenerTareaIntegradoraPorMateria(){
        ResultadoEJB<List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion>> resultadoEJB = ejbCalificaiones.tareaIntegradoraPresentacion(rol.getEstudiante());
        rol.setTareaIntegradoraPresentacion(resultadoEJB.getValor());
    }

    public void obtenerNivelacionesPorMateria(){
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria>> resultadoEJB = ejbCalificaiones.packPromedioNivelacionPorMateria(rol.getEstudiante());
        rol.setCalificacionesNivelacionPorMateria(resultadoEJB.getValor());
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "Cedula Identificacion Tutor";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
