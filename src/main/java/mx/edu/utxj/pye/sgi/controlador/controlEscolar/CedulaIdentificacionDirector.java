package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CedulaIdetificacionRolDirector;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalificacionEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.*;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
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
public class CedulaIdentificacionDirector extends ViewScopedRol implements Desarrollable {

    @Getter @Setter CedulaIdetificacionRolDirector rol;
    @EJB EjbPropiedades ep;
    @EJB EjbRegistroPlanEstudio ejb;
    @EJB EjbCedulaIdentificacion ejbCedulaIdentificacion;
    @EJB EjbConsultaCalificacion ejbCalificaiones;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;


   
@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

 @PostConstruct
    public void init(){
        try {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.CEDULA_IDENTIFICACION_DIRECTOR);
            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbCedulaIdentificacion.validarDirector(logon.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resValidaEnc = ejbCedulaIdentificacion.validarEncargadoDireccion(logon.getPersonal().getClave());//validar si es director

            if (!resValidaEnc.getCorrecto() && !resValidacion.getCorrecto()) {
                mostrarMensajeResultadoEJB(resValidacion);
                return;
            }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new CedulaIdetificacionRolDirector(filtro, director, director.getAreaOficial());
            tieneAcceso = rol.tieneAcceso(director);
            if (!tieneAcceso) {
                rol.setFiltro(resValidaEnc.getValor());
                tieneAcceso = rol.tieneAcceso(director);
            }
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            rol.setNivelRol(NivelRol.CONSULTA);

            ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> resProgramaPlan = ejb.getProgramasEducativos(director);
            if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
            rol.setAreaPlanEstudioMap(resProgramaPlan.getValor());
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());
            //Periodo Actual
            ResultadoEJB<PeriodosEscolares>  periodoActual = ejbCedulaIdentificacion.getPeriodoActual();
            if(periodoActual.getCorrecto()==true){rol.setPeriodo(periodoActual.getValor());}
            rol.setPlanEstudio(rol.getPlanesEstudios().get(0));
            ResultadoEJB<List<Grupo>> resgrupos = ejb.getListaGrupoPlanEstudio(rol.getPlanEstudio(),rol.getPeriodo());
            if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
            rol.setGrupos(resgrupos.getValor());

            rol.setGrupoSelec(rol.getGrupos().get(0));

            ResultadoEJB<List<Listaalumnosca>> rejb = ejb.getListaAlumnosPorGrupo(rol.getGrupoSelec());
            if(!rejb.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
            rol.setListaEstudiantes(rejb.getValor());
            //TODO:Carga los apartados de la cedula de Idetificacion
            rol.setApartados(ejbCedulaIdentificacion.getApartados());
            //TODO:INSTRUCCIONES
            rol.getInstrucciones().add("Seleccione el programa educativo en el que esta inscrito el estudiante que desea buscar");
            rol.getInstrucciones().add("Seleccione el grupo al que pertenece el estudiante.");
            rol.getInstrucciones().add("Ingrese la matricula o el nombre del estudiante");
            rol.getInstrucciones().add("Los datos del estudiante se cargarán automaticamente");
            rol.getInstrucciones().add("NOTA IMPORTANTE: La coordinación de desarollo de software sigue trabajando en algunos datos que son necesarios en la cédula de identificación, los cuales se estarán liberando en las próximos días.");
            rol.getInstrucciones().add("NOTA IMPORNTATE: La mayoría de los datos recabados forman parte del proceso de inscripción del estudiante y se tienen almacenados en nuestras bases de datos, si existen campos vacios, es porque la información no ha sido recabada.");


        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }
    public void cambiarPeriodo(ValueChangeEvent event){
        rol.setPeriodo((PeriodosEscolares)event.getNewValue());
    }

    public void cambiarPlanestudio(ValueChangeEvent event) {
        rol.setGrupos(new ArrayList<>());
        rol.setListaEstudiantes(new ArrayList<>());
        rol.setGrupoSelec(new Grupo());
        rol.setPlanEstudio((PlanEstudio) event.getNewValue());
        ResultadoEJB<List<Grupo>> resgrupos = ejb.getListaGrupoPlanEstudio(rol.getPlanEstudio(), rol.getPeriodo());
        if (!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
        rol.setGrupos(resgrupos.getValor());
        rol.setGrupoSelec(rol.getGrupos().get(0));
        rol.setListaEstudiantes(new ArrayList<>());
        ResultadoEJB<List<Listaalumnosca>> res=  ejb.getListaAlumnosPorGrupo(rol.getGrupoSelec());
        if(!res.getCorrecto()) mostrarMensajeResultadoEJB(res);
        rol.setListaEstudiantes(res.getValor());
//
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
            rol.setEstudiante(resEstudiante.getValor());rol.setEstudiantePeriodo(resEstudiante.getValor());
            //TODO: Se ejecuta el metodo de busqueda
            ResultadoEJB<DtoCedulaIdentificacion> resCedula = ejbCedulaIdentificacion.getCedulaIdentificacion(Integer.parseInt(rol.getMatricula()));
            if(resCedula.getCorrecto()==true){
                getPeriodosEstudiante();
                //System.out.println("Entro a genera cedula");
                rol.setCedulaIdentificacion(resCedula.getValor());
                getPeriodoEstudiante();
                getCalificaciones();
            }else{
                mostrarMensajeResultadoEJB(resCedula);
            }
        }

    }
    public void getPeriodosEstudiante(){
        List<PeriodosEscolares> periodosEscolares = new ArrayList<>();
        ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejbCedulaIdentificacion.getPeriodosEscolaresbyEstudiante(rol.getEstudiantePeriodo());
        if(resPeriodos.getCorrecto()==true){
            rol.setPeriodosEstudiante(resPeriodos.getValor());
        }else {mostrarMensajeResultadoEJB(resPeriodos);}
    }

    public void getCalificaciones(){
        obtenerMateriasPorEstudiante();
        obtenerUnidadesPorMateria();
        obtenerCalificaciones();
        obtenerPromedioMateria();
        obtenerTareaIntegradoraPorMateria();
        obtenerNivelacionesPorMateria();
        obtenerPromediosFinales();
        obtenerPromedioCuatrimestral();
        // obtenerPromedioAcumulado();
    }
    public void changePeriodo(){
        ResultadoEJB<Estudiante> resEstudiantePeriodo = ejbCedulaIdentificacion.getEstudiantebyPeriodo(rol.getPeriodoEstudiante(),rol.getEstudiantePeriodo());
        if(resEstudiantePeriodo.getCorrecto()==true){
            rol.setEstudiantePeriodo(resEstudiantePeriodo.getValor());
            getPeriodosEstudiante();
            rol.setPeriodoE(resEstudiantePeriodo.getValor().getPeriodo());
            rol.setEstudiantePeriodo(resEstudiantePeriodo.getValor());
            getCalificaciones();
        }else {mostrarMensajeResultadoEJB(resEstudiantePeriodo);}
    }


    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "Cedula Identificacion Director";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    public void getPeriodoEstudiante(){
        PeriodosEscolares periodo = new PeriodosEscolares();
        ResultadoEJB<PeriodosEscolares> resPeriodo= ejbCedulaIdentificacion.getPeriodoEstudiante(rol.getEstudiante());
        if(resPeriodo.getCorrecto()==true){rol.setPeriodoEstudiante(resPeriodo.getValor());rol.setPeriodoE(rol.getPeriodoEstudiante().getPeriodo());}
        else {mostrarMensajeResultadoEJB(resPeriodo);}
    }
    //----------------------- Calificaciones del estudiante(Marce)---------------------
    public void obtenerMateriasPorEstudiante() {
        ResultadoEJB<List<DtoCalificacionEstudiante.MateriasPorEstudiante>> resMaterias = ejbCalificaiones.packMaterias(rol.getEstudiantePeriodo());
        rol.setMateriasPorEstudiante(resMaterias.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
        Map<Integer, Long> group = rol.getMateriasPorEstudiante().stream().collect(Collectors.groupingBy(DtoCalificacionEstudiante.MateriasPorEstudiante::getPeriodo, Collectors.counting()));
        group.forEach((k, v) -> {
            rol.setPeriodoX(k);
        });
    }
    public void obtenerUnidadesPorMateria() {
        List<DtoCalificacionEstudiante.MapUnidadesTematicas> resMap = new ArrayList<>();
        ResultadoEJB<List<DtoCalificacionEstudiante.UnidadesPorMateria>> resUnidadesPorMateria = ejbCalificaiones.packUnidadesmateria(rol.getEstudiantePeriodo());
        rol.setUnidadesPorMateria(resUnidadesPorMateria.getValor());
        rol.getUnidadesPorMateria().forEach(x -> {
            x.getUnidadMateriaConfiguracion().forEach(y -> {
                resMap.add(new DtoCalificacionEstudiante.MapUnidadesTematicas(y.getIdUnidadMateria().getNoUnidad(), y.getIdUnidadMateria().getNoUnidad()));
            });
        });
        rol.setMapUnidadesTematicas(new ArrayList<>(new HashSet<>(resMap)));
        rol.getMapUnidadesTematicas().sort(Comparator.comparingInt(DtoCalificacionEstudiante.MapUnidadesTematicas::getNoUnidad));
    }
    public void obtenerCalificaciones() {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorUnidad>> resCalificaciones = ejbCalificaiones.packCalificacionesPorUnidadyMateria1(rol.getEstudiantePeriodo());
        rol.setCalificacionePorUnidad(resCalificaciones.getValor().stream().filter(a -> a.getEstudiante().getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
    }
    public void obtenerPromedioMateria() {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ejbCalificaiones.packPromedioMateria(rol.getEstudiantePeriodo());
        rol.setCalificacionePorMateria(resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
    }
    public void obtenerPromediosFinales() {
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionePorMateria>> resultadoEJB = ejbCalificaiones.packCalificacionesFinales(rol.getEstudiantePeriodo());
        rol.setCalificacionesFinalesPorMateria(resultadoEJB.getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
    }
    public void obtenerPromedioCuatrimestral() {
        ResultadoEJB<BigDecimal> promedio = ejbCalificaiones.obtenerPromedioCuatrimestral( rol.getEstudiantePeriodo(), rol.getPeriodoEstudiante().getPeriodo());
        BigDecimal valor = promedio.getValor();
        rol.setMateriasPorEstudiante(ejbCalificaiones.packMaterias(rol.getEstudiantePeriodo()).getValor().stream().filter(a -> a.getGrupo().getPeriodo() == rol.getPeriodoE()).collect(Collectors.toList()));
        BigDecimal numeroMaterias = new BigDecimal(rol.getMateriasPorEstudiante().size());
        BigDecimal promedioCuatrimestral = valor.divide(numeroMaterias, RoundingMode.HALF_UP);
        rol.setPromedio(promedioCuatrimestral.setScale(1, RoundingMode.HALF_UP));
    }
    public void obtenerPromedioAcumulado() {
        ResultadoEJB<List<BigDecimal>> promedios = ejbCalificaiones.obtenerPromedioAcumulado(rol.getEstudiantePeriodo());
        BigDecimal numeroPromedios = new BigDecimal(promedios.getValor ().size());
        BigDecimal suma = promedios.getValor().stream().map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal promedio = suma.divide(numeroPromedios, RoundingMode.HALF_UP);
        rol.setPromedioAcumluado(promedio.setScale(1, RoundingMode.HALF_UP));
    }

    public void obtenerTareaIntegradoraPorMateria(){
        ResultadoEJB<List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion>> resultadoEJB = ejbCalificaiones.tareaIntegradoraPresentacion(rol.getEstudiantePeriodo());
        rol.setTareaIntegradoraPresentacion(resultadoEJB.getValor());
    }

    public void obtenerNivelacionesPorMateria(){
        ResultadoEJB<List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria>> resultadoEJB = ejbCalificaiones.packPromedioNivelacionPorMateria(rol.getEstudiantePeriodo());
        rol.setCalificacionesNivelacionPorMateria(resultadoEJB.getValor());
    }

}
