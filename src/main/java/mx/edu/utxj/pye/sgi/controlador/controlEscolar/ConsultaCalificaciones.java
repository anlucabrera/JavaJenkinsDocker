package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.*;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
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
import java.util.*;
import java.util.stream.Collectors;


@Named
@ViewScoped
public class ConsultaCalificaciones extends ViewScopedRol implements Desarrollable {

    @Getter @Setter private ConsultaCalificacionesRolMultiple rol;
    @Getter @Setter Boolean tieneAcceso = false;
    @EJB
    EjbPropiedades ep;
    @EJB
    EjbConsultaCalificacion ejbC;
    @Inject
    LogonMB logonMB;
    @Inject
    ConsultaCalificacionesEstudiante c;
    @Getter private Boolean cargado = false;


    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.CONSULTA_CALIFICACION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbC.validarCoordinador(logonMB.getPersonal().getClave());
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbC.validarCoordinador(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo coordinador = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ConsultaCalificacionesRolMultiple(filtro, coordinador, coordinador.getAreaOficial());
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
            rol.setNivelRol(NivelRol.CONSULTA);

            if(rol.getCoordinador().getPersonal().getAreaOperativa() != Short.parseShort("10")) return;
            rol.setPeriodosEscolares(ejbC.obtenerListaPeriodosEscolares().getValor());
            rol.setPeriodo(ejbC.getPeriodoActual().getPeriodo());
            rol.setAreasUniversidad(ejbC.obtenerProgramasEducativos().getValor());
            if(!rol.getEstudianteRegistro().isEmpty())return;
            ResultadoEJB<List<DtoEstudiante>> resultadoEJB = ejbC.obtenerEstudiantes();
            if(resultadoEJB.getCorrecto()) {
                rol.setEstudianteRegistro(resultadoEJB.getValor());
            }
            //obtenerTop30MejoresEstudianteCuatrimestral();
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    public void cambiarEstudianteSeleccionado(ValueChangeEvent event){
        if(event.getNewValue() instanceof DtoEstudianteComplete){
            List<DtoCalificacionEstudiante.DtoHistorialCalificaciones> dtoHistorialCalificaciones =  new ArrayList<>();
            List<DtoCalificacionEstudiante.MapUnidadesTematicas> map = new ArrayList<>();
            DtoEstudianteComplete estudiante = (DtoEstudianteComplete) event.getNewValue();
            ResultadoEJB<DtoEstudiante> estudianteEncontrado = ejbC.validadEstudiante(estudiante.getEstudiantes().getMatricula());
            //ResultadoEJB<DtoEstudiante> estudianteK = ejbC.validadEstudianteK(estudiante.getEstudiantes().getMatricula());
            if(estudianteEncontrado.getCorrecto()){
                rol.setEstudiante(estudianteEncontrado.getValor());
                //rol.setEstudianteK(estudianteK.getValor());
                if(rol.getEstudiante().getInscripcionActiva().getGrupo().getCargaAcademicaList().isEmpty())return;
                rol.setDtoInscripciones(rol.getEstudiante().getInscripciones());
                
            }else{
                mostrarMensaje("El estudiante no cuenta con una inscripción activa.");
            }
        }else mostrarMensaje("El valor seleccionado no es valido");
    }

    public void obtenerTop30MejoresEstudiantes(){
        if(rol.getCoordinador().getPersonal().getAreaOperativa() != Short.parseShort("10")) return;
        if(!rol.getMapTopMejoresEstudiantes().isEmpty())return;
        final Map<DtoInscripcion, BigDecimal> map = new HashMap<>();
        final Map<DtoInscripcion, BigDecimal> maper = new HashMap<>();
        rol.setMapTopMejoresEstudiantes(new HashMap<>());
        rol.getEstudianteRegistro().forEach(dtoEstudiante -> {
            dtoEstudiante.getInscripciones().forEach(dtoInscripcion -> {
                List<DtoCargaAcademica> dtoCargaAcademicas = ejbC.obtenerCargasAcademicas(dtoInscripcion.getInscripcion()).getValor();
                List<BigDecimal> promediosOrdinarios = dtoCargaAcademicas
                        .stream()
                        .map(dtoCargaAcademica -> {
                            if(!dtoInscripcion.getGrupo().getIdGrupo().equals(dtoCargaAcademica.getGrupo().getIdGrupo())) return BigDecimal.ZERO;
                            BigDecimal promedioOrdinario;
                            BigDecimal promedioNivelacion;
                            if(dtoInscripcion.getInscripcion().getCalificacionPromedioList().size() == 0){
                                promedioOrdinario = BigDecimal.ZERO;
                            }else {
                                promedioOrdinario = new BigDecimal(ejbC.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), dtoInscripcion.getInscripcion()).getValor().getValor());
                            }
                            if(dtoInscripcion.getInscripcion().getCalificacionNivelacionList().size() == 0){
                                promedioNivelacion = BigDecimal.ZERO;
                            }else{
                                promedioNivelacion = new BigDecimal(c.getNivelacion(dtoCargaAcademica, dtoInscripcion.getInscripcion()).getCalificacionNivelacion().getValor());
                            }
                            if(promedioNivelacion.compareTo(BigDecimal.ZERO) == 0){
                                return promedioOrdinario;
                            }else{
                                return promedioNivelacion;
                            }
                        }
                        ).collect(Collectors.toList());
                BigDecimal totalCalificaciones = new BigDecimal(dtoCargaAcademicas.size());
                BigDecimal promedioFinal = promediosOrdinarios
                        .stream()
                        .sorted()
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .plus()
                        .divide(totalCalificaciones, RoundingMode.HALF_UP)
                        .setScale(2, RoundingMode.HALF_UP);
                //if(promedioFinal.compareTo(BigDecimal.valueOf(9)) < 0)return;
                map.put(dtoInscripcion, promedioFinal);
            });
        });

        rol.getEstudianteRegistro().forEach(dtoEstudiante -> {
            Integer inscripciones = Math.toIntExact(dtoEstudiante.getInscripciones()
                    .stream()
                    .filter(dtoInscripcion -> dtoInscripcion.getInscripcion().getMatricula() == dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula()).count());
            BigDecimal promedio = map.entrySet().stream()
                    .filter(predicate -> predicate.getKey().getInscripcion().getMatricula() == dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula())
                    .map(Map.Entry::getValue).reduce(BigDecimal.ZERO, BigDecimal::add)
                    .plus()
                    .divide(new BigDecimal(inscripciones), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
            maper.put(dtoEstudiante.getInscripcionActiva(), promedio);
            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula() == dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula())return;
        });

        //System.out.println(maper);
        Map<DtoInscripcion, BigDecimal> topMejoresEstudiantes = maper.entrySet()
                .stream()
                .distinct()
                .sorted(Map.Entry.<DtoInscripcion, BigDecimal>comparingByValue().reversed())
                .limit(30)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> k, LinkedHashMap::new));
        rol.setMapTopMejoresEstudiantes(topMejoresEstudiantes);

    }

    public void obtenerTop30MejoresEstudiantesPorProgramaEducativo(){
        if(rol.getCoordinador().getPersonal().getAreaOperativa() != Short.parseShort("10")) return;
        final Map<DtoInscripcion, BigDecimal> map = new HashMap<>();
        final Map<DtoInscripcion, BigDecimal> maper = new HashMap<>();
        rol.setMapTopMejoresEstudiantePE(new HashMap<>());
        List<DtoEstudiante> dtoEstudiantes = rol.getEstudianteRegistro().parallelStream().filter(dtoEstudiante ->
                dtoEstudiante.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("1")) &&
                        dtoEstudiante.getInscripcionActiva().getInscripcion().getCarrera() == rol.getCarrera()).collect(Collectors.toList());
        //ResultadoEJB<List<DtoEstudiante>> resultadoEJB = ejbC.obtenerEstudiantes();
        //rol.setEstudianteRegistro(resultadoEJB.getValor().stream().filter(dtoEstudiante -> dtoEstudiante.getInscripcionActiva().getInscripcion().getCarrera() == rol.getCarrera()).collect(Collectors.toList()));

        dtoEstudiantes.forEach(dtoEstudiante -> {
                    dtoEstudiante.getInscripciones().forEach(dtoInscripcion -> {
                        List<DtoCargaAcademica> dtoCargaAcademicas = ejbC.obtenerCargasAcademicas(dtoInscripcion.getInscripcion()).getValor();
                        List<BigDecimal> promediosOrdinarios = dtoCargaAcademicas
                                .stream()
                                .map(dtoCargaAcademica -> {
                                            if(!dtoInscripcion.getGrupo().getIdGrupo().equals(dtoCargaAcademica.getGrupo().getIdGrupo())) return BigDecimal.ZERO;
                                            BigDecimal promedioOrdinario;
                                            BigDecimal promedioNivelacion;
                                            if(dtoInscripcion.getInscripcion().getCalificacionPromedioList().size() == 0){
                                                promedioOrdinario = BigDecimal.ZERO;
                                            }else {
                                                promedioOrdinario = new BigDecimal(ejbC.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), dtoInscripcion.getInscripcion()).getValor().getValor());
                                            }
                                            if(dtoInscripcion.getInscripcion().getCalificacionNivelacionList().size() == 0){
                                                promedioNivelacion = BigDecimal.ZERO;
                                            }else{
                                                promedioNivelacion = new BigDecimal(c.getNivelacion(dtoCargaAcademica, dtoInscripcion.getInscripcion()).getCalificacionNivelacion().getValor());
                                            }
                                            if(promedioNivelacion.compareTo(BigDecimal.ZERO) == 0){
                                                return promedioOrdinario;
                                            }else{
                                                return promedioNivelacion;
                                            }
                                        }
                                ).collect(Collectors.toList());
                        BigDecimal totalCalificaciones = new BigDecimal(dtoCargaAcademicas.size());
                        BigDecimal promedioFinal = promediosOrdinarios
                                .stream()
                                .sorted()
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                                .plus()
                                .divide(totalCalificaciones, RoundingMode.HALF_UP)
                                .setScale(2, RoundingMode.HALF_UP);
                        //if(promedioFinal.compareTo(BigDecimal.valueOf(9)) < 0)return;
                        map.put(dtoInscripcion, promedioFinal);
                    });
        });

        dtoEstudiantes.forEach(dtoEstudiante -> {
            //Map<DtoInscripcion, BigDecimal> mapp = new HashMap<>();
            Integer inscripciones = Math.toIntExact(dtoEstudiante.getInscripciones()
                    .stream()
                    .filter(dtoInscripcion -> dtoInscripcion.getInscripcion().getMatricula() == dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula()).count());
            BigDecimal promedio = map.entrySet().stream()
                    .filter(predicate -> predicate.getKey().getInscripcion().getMatricula() == dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula())
                    .map(Map.Entry::getValue).reduce(BigDecimal.ZERO, BigDecimal::add)
                    .plus()
                    .divide(new BigDecimal(inscripciones), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
            maper.put(dtoEstudiante.getInscripcionActiva(), promedio);
            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula() == dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula())return;
        });
        //System.out.println(map.size());
        Map<DtoInscripcion, BigDecimal> topMejoresEstudiantes = maper.entrySet()
                .stream()
                .sorted(Map.Entry.<DtoInscripcion, BigDecimal>comparingByValue().reversed())
                .distinct()
                .limit(30)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> k, LinkedHashMap::new));
        rol.setMapTopMejoresEstudiantePE(topMejoresEstudiantes);
    }

    public void obtenerTop30MejoresEstudianteCuatrimestral(){
        if(rol.getCoordinador().getPersonal().getAreaOperativa() != Short.parseShort("10")) return;
        if(!rol.getMapTopMejoresEstudiantes().isEmpty())return;
        final Map<DtoInscripcion, BigDecimal> map = new HashMap<>();
        rol.setMapTopMejoresEstudiantes(new HashMap<>());
        rol.getEstudianteRegistro().forEach(dtoEstudiante -> {
            dtoEstudiante.getInscripciones().stream().filter(dtoInscripcion -> dtoInscripcion.getInscripcion().getPeriodo() == rol.getPeriodo()).forEach(dtoInscripcion -> {
                List<DtoCargaAcademica> dtoCargaAcademicas = ejbC.obtenerCargasAcademicas(dtoInscripcion.getInscripcion()).getValor();
                List<BigDecimal> promediosOrdinarios = dtoCargaAcademicas
                        .stream()
                        .map(dtoCargaAcademica -> {
                                    if(!dtoInscripcion.getGrupo().getIdGrupo().equals(dtoCargaAcademica.getGrupo().getIdGrupo())) return BigDecimal.ZERO;
                                    BigDecimal promedioOrdinario;
                                    BigDecimal promedioNivelacion;
                                    if(dtoInscripcion.getInscripcion().getCalificacionPromedioList().size() == 0){
                                        promedioOrdinario = BigDecimal.ZERO;
                                    }else {
                                        promedioOrdinario = new BigDecimal(ejbC.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), dtoInscripcion.getInscripcion()).getValor().getValor());
                                    }
                                    if(dtoInscripcion.getInscripcion().getCalificacionNivelacionList().size() == 0){
                                        promedioNivelacion = BigDecimal.ZERO;
                                    }else{
                                        promedioNivelacion = new BigDecimal(c.getNivelacion(dtoCargaAcademica, dtoInscripcion.getInscripcion()).getCalificacionNivelacion().getValor());
                                    }
                                    if(promedioNivelacion.compareTo(BigDecimal.ZERO) == 0){
                                        return promedioOrdinario;
                                    }else{
                                        return promedioNivelacion;
                                    }
                                }
                        ).collect(Collectors.toList());
                BigDecimal totalCalificaciones = new BigDecimal(dtoCargaAcademicas.size());
                BigDecimal promedioFinal = promediosOrdinarios
                        .stream()
                        .sorted()
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .plus()
                        .divide(totalCalificaciones, RoundingMode.HALF_UP)
                        .setScale(2, RoundingMode.HALF_UP);
                map.put(dtoInscripcion, promedioFinal);
            });
        });

        Map<DtoInscripcion, BigDecimal> topMejoresEstudiantes = map.entrySet()
                .stream()
                .distinct()
                .sorted(Map.Entry.<DtoInscripcion, BigDecimal>comparingByValue().reversed())
                .limit(30)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> k, LinkedHashMap::new));
        rol.setMapTopMejoresEstudiantesCuatri(topMejoresEstudiantes);
    }

    public AreasUniversidad obtenerProgramaEducativo(Short carrera){
        ResultadoEJB<AreasUniversidad> area = ejbC.obtenerProgramaEducativo(carrera);
        return area.getValor();
    }


    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "consulta de calificaciones por coordinador";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

}
