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
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaCalificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaTareaIntegradora;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionNivelacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Indicador;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradora;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradoraPromedio;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;


@Named
@ViewScoped
public class ConsultaCalificaciones extends ViewScopedRol implements Desarrollable {

    @Getter @Setter private ConsultaCalificacionesRolMultiple rol;
    @Getter @Setter Boolean tieneAcceso = false;
    @EJB EjbPropiedades ep;
    @EJB EjbConsultaCalificacion ejb;
    @EJB EjbCapturaCalificaciones ejbCapturaCalificaciones;
    @EJB EjbCapturaTareaIntegradora ejbCapturaTareaIntegradora;
    @Inject LogonMB logonMB;
    @Inject ConsultaCalificacionesEstudiante c;
    @Getter private Boolean cargado = false;
    @Getter private Boolean bloquear = false;
    @Getter private Boolean desbloquear = true;


    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.CONSULTA_CALIFICACION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarCoordinador(logonMB.getPersonal().getClave());
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarCoordinador(logonMB.getPersonal().getClave());
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
            
            ResultadoEJB<List<DtoEstudiante>> resultadoEJB = ejb.obtenerEstudiantes();
            if(resultadoEJB.getCorrecto()) {
                rol.setEstudianteRegistro(resultadoEJB.getValor());
            }
            
            if(rol.getCoordinador().getPersonal().getAreaOperativa() != Short.parseShort("10")) return;
            rol.setPeriodosEscolares(ejb.obtenerListaPeriodosEscolares().getValor());
            rol.setPeriodo(ejb.getPeriodoActual().getPeriodo());
            rol.setAreasUniversidad(ejb.obtenerProgramasEducativos().getValor());
            //if(!rol.getEstudianteRegistro().isEmpty())return;
            
            //obtenerTop30MejoresEstudianteCuatrimestral();
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    public void buscarEstudiante(ValueChangeEvent event){
        rol.setMatricula((Integer) event.getNewValue());
        //System.out.println("Matricula:"+ rol.getMatricula());
        consultarCalificacionesEstudiante(rol.getMatricula());
    }
    
    public void consultarCalificacionesEstudiante(Integer matricula){
        //if(rol.getMatricula() == null) return;
        //Ajax.oncomplete("limpiarComponentes()");
        //rol.getDtoInscripciones().clear();
        //rol.setEstudiante(new DtoEstudiante(new Persona(), new Aspirante(), new ArrayList<>(), new DtoInscripcion(new Estudiante(), new Grupo(), new PeriodosEscolares(), new Generaciones(), false)));
        //rol.getMapUnidadesTematicas().clear();
        //rol.getCargasEstudiante().clear();
        ResultadoEJB<DtoEstudiante> estudianteEncontrado = ejb.validadEstudianteK(matricula);
        if(estudianteEncontrado.getCorrecto()){
            rol.setEstudiante(estudianteEncontrado.getValor());
        
        //System.out.println("Estudiante:"+ rol.getEstudiante().getInscripcionActiva().getInscripcion().getMatricula());
        rol.setDtoInscripciones(
                rol.getEstudiante()
                .getInscripciones()
                .stream()
                .filter(x -> x.getInscripcion().getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("1")) || 
                        x.getInscripcion().getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("5")) || 
                        x.getInscripcion().getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("4")) ||
                        x.getInscripcion().getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("6")))
                .collect(Collectors.toList())
        );
           bloquear =  Boolean.TRUE;
           desbloquear =  Boolean.FALSE;
        }else{
            mostrarMensajeError("El estudiante solcitado ya no se encuentra activo");
            rol.setDtoInscripciones(new ArrayList<>());
            rol.setEstudiante(new DtoEstudiante(new Persona(), new Aspirante(), new ArrayList<>(), new DtoInscripcion(new Estudiante(), new Grupo(), new PeriodosEscolares(), new Generaciones(), false)));
        }
        
    }
    
    public void cambiarEstudianteSeleccionado(ValueChangeEvent event){
        if(event.getNewValue() instanceof DtoEstudianteComplete){
            rol.setDtoInscripciones(new ArrayList<>());
            rol.setEstudiante(new DtoEstudiante(new Persona(), new Aspirante(), new ArrayList<>(), new DtoInscripcion(new Estudiante(), new Grupo(), new PeriodosEscolares(), new Generaciones(), false)));
            DtoEstudianteComplete estudiante = (DtoEstudianteComplete) event.getNewValue();
            System.out.println("Estudiante complete"+ estudiante.getEstudiantes().getMatricula());
            ResultadoEJB<DtoEstudiante> estudianteEncontrado = ejb.validadEstudiante(estudiante.getEstudiantes().getMatricula());
            System.out.println("Estudiante encontrado:"+ estudianteEncontrado.getValor().getInscripcionActiva().getInscripcion().getMatricula());
            //ResultadoEJB<DtoEstudiante> estudianteK = ejb.validadEstudianteK(estudiante.getEstudiantes().getMatricula());
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
                List<DtoCargaAcademica> dtoCargaAcademicas = ejb.obtenerCargasAcademicas(dtoInscripcion.getInscripcion()).getValor();
                List<BigDecimal> promediosOrdinarios = dtoCargaAcademicas
                        .stream()
                        .map(dtoCargaAcademica -> {
                            if(!dtoInscripcion.getGrupo().getIdGrupo().equals(dtoCargaAcademica.getGrupo().getIdGrupo())) return BigDecimal.ZERO;
                            BigDecimal promedioOrdinario;
                            BigDecimal promedioNivelacion;
                            if(dtoInscripcion.getInscripcion().getCalificacionPromedioList().isEmpty()){
                                promedioOrdinario = BigDecimal.ZERO;
                            }else {
                                promedioOrdinario = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), dtoInscripcion.getInscripcion()).getValor().getValor());
                            }
                            if(dtoInscripcion.getInscripcion().getCalificacionNivelacionList().isEmpty()){
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
        //ResultadoEJB<List<DtoEstudiante>> resultadoEJB = ejb.obtenerEstudiantes();
        //rol.setEstudianteRegistro(resultadoEJB.getValor().stream().filter(dtoEstudiante -> dtoEstudiante.getInscripcionActiva().getInscripcion().getCarrera() == rol.getCarrera()).collect(Collectors.toList()));

        dtoEstudiantes.forEach(dtoEstudiante -> {
                    dtoEstudiante.getInscripciones().forEach(dtoInscripcion -> {
                        List<DtoCargaAcademica> dtoCargaAcademicas = ejb.obtenerCargasAcademicas(dtoInscripcion.getInscripcion()).getValor();
                        List<BigDecimal> promediosOrdinarios = dtoCargaAcademicas
                                .stream()
                                .map(dtoCargaAcademica -> {
                                            if(!dtoInscripcion.getGrupo().getIdGrupo().equals(dtoCargaAcademica.getGrupo().getIdGrupo())) return BigDecimal.ZERO;
                                            BigDecimal promedioOrdinario;
                                            BigDecimal promedioNivelacion;
                                            if(dtoInscripcion.getInscripcion().getCalificacionPromedioList().isEmpty()){
                                                promedioOrdinario = BigDecimal.ZERO;
                                            }else {
                                                promedioOrdinario = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), dtoInscripcion.getInscripcion()).getValor().getValor());
                                            }
                                            if(dtoInscripcion.getInscripcion().getCalificacionNivelacionList().isEmpty()){
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
                List<DtoCargaAcademica> dtoCargaAcademicas = ejb.obtenerCargasAcademicas(dtoInscripcion.getInscripcion()).getValor();
                List<BigDecimal> promediosOrdinarios = dtoCargaAcademicas
                        .stream()
                        .map(dtoCargaAcademica -> {
                                    if(!dtoInscripcion.getGrupo().getIdGrupo().equals(dtoCargaAcademica.getGrupo().getIdGrupo())) return BigDecimal.ZERO;
                                    BigDecimal promedioOrdinario;
                                    BigDecimal promedioNivelacion;
                                    if(dtoInscripcion.getInscripcion().getCalificacionPromedioList().isEmpty()){
                                        promedioOrdinario = BigDecimal.ZERO;
                                    }else {
                                        promedioOrdinario = new BigDecimal(ejb.obtenerPromedioEstudiante(dtoCargaAcademica.getCargaAcademica(), dtoInscripcion.getInscripcion()).getValor().getValor());
                                    }
                                    if(dtoInscripcion.getInscripcion().getCalificacionNivelacionList().isEmpty()){
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
        ResultadoEJB<AreasUniversidad> area = ejb.obtenerProgramaEducativo(carrera);
        return area.getValor();
    }

    public List<DtoCalificacionEstudiante.MapUnidadesTematicas> obtenerUnidadesPorMateria(Estudiante estudiante) {
        List<DtoCalificacionEstudiante.MapUnidadesTematicas> map = new ArrayList<>();
        rol.setMapUnidadesTematicas(new ArrayList<>());
        ejb.packUnidadesmateria(estudiante).getValor().forEach(unidadesPorMateria -> {
            unidadesPorMateria.getUnidadMaterias().forEach(unidadMateria -> {
                map.add(new DtoCalificacionEstudiante.MapUnidadesTematicas(unidadesPorMateria.getMateria().getCveGrupo().getIdGrupo(), unidadMateria.getNoUnidad()));
            });
        });
        rol.setMapUnidadesTematicas(new ArrayList<>(new HashSet<>(map)));
        rol.getMapUnidadesTematicas().sort(Comparator.comparingInt(DtoCalificacionEstudiante.MapUnidadesTematicas::getNoUnidad));
        if(rol.getMapUnidadesTematicas().isEmpty())return new ArrayList<>();
        return rol.getMapUnidadesTematicas();
    }
    
    public synchronized List<DtoCargaAcademica> getCargasAcademicas(Estudiante estudiante) {
        rol.setCargasEstudiante(new ArrayList<>());
        if (estudiante.getGrupo() == null) {
            mostrarMensaje("El estudiante no tiene grupo");
            return Collections.EMPTY_LIST;
        }

        if (estudiante.getGrupo().getIdGrupo() == null) {
            mostrarMensaje("La clave del grupo del estudiante es nula");
            return Collections.EMPTY_LIST;
        }
        
        if(estudiante.getGrupo().getGrado() == 6 || estudiante.getGrupo().getGrado() == 11) {
            return Collections.EMPTY_LIST;
        }
        if(estudiante.getCalificacionList().isEmpty() && estudiante.getCalificacionEvidenciaInstrumentoList().isEmpty()){
            mostrarMensaje("No se encontro registro de calificaciones en el cuatrimestre: "+ estudiante.getGrupo().getGrado());
            return Collections.EMPTY_LIST;
        }
        String clave = "cargasPorGrupo".concat(estudiante.getGrupo().getIdGrupo().toString());
        //System.out.println("Clave"+ clave);
        List<DtoCargaAcademica> cargas = Faces.getApplicationAttribute(clave);
        if (cargas != null) {
            return cargas;
        }

        ResultadoEJB<List<DtoCargaAcademica>> resCargas = ejb.obtenerCargasAcademicas(estudiante);
        if (!resCargas.getCorrecto()) {
            mostrarMensaje("No se pudo obtener la lista de cargas en la capa de lógica de negocios. ".concat(resCargas.getMensaje()));
            return Collections.EMPTY_LIST;
        }

        cargas = resCargas.getValor();
        rol.setCargasEstudiante(cargas);
        Faces.setApplicationAttribute(clave, rol.getCargasEstudiante());
        return rol.getCargasEstudiante();
//        rol.setCargasEstudiante(resCargas.getValor());
//        if(rol.getCargasEstudiante().isEmpty()) return new ArrayList<>();
//        return rol.getCargasEstudiante();
    }
    
    public List<DtoUnidadConfiguracion> getUnidades(DtoCargaAcademica dtoCargaAcademica){
        if(rol.getDtoUnidadConfiguracionesMap().containsKey(dtoCargaAcademica)) return  rol.getDtoUnidadConfiguracionesMap().get(dtoCargaAcademica);

        ResultadoEJB<List<DtoUnidadConfiguracion>> resConfiguraciones = ejbCapturaCalificaciones.getConfiguraciones(dtoCargaAcademica);
        if(!resConfiguraciones.getCorrecto()){
            //mostrarMensaje("No se detectaron configuraciones de unidades en la materia de la carga académica seleccionada. " + resConfiguraciones.getMensaje());
            return Collections.EMPTY_LIST;
        }
        rol.getDtoUnidadConfiguracionesMap().put(dtoCargaAcademica, resConfiguraciones.getValor());

        return  rol.getDtoUnidadConfiguracionesMap().get(dtoCargaAcademica);
    }
    
    public List<DtoUnidadConfiguracionAlineacion> getUnidadesAlineacion(DtoCargaAcademica dtoCargaAcademica){
        if(rol.getDtoUnidadConfiguracionesAlineacionMap().containsKey(dtoCargaAcademica)) return  rol.getDtoUnidadConfiguracionesAlineacionMap().get(dtoCargaAcademica);

        ResultadoEJB<List<DtoUnidadConfiguracionAlineacion>> resConfiguraciones = ejbCapturaCalificaciones.getConfiguracionesAlineacion(dtoCargaAcademica);
        if(!resConfiguraciones.getCorrecto()){
            //mostrarMensaje("No se detectaron configuraciones de unidades en la materia de la carga académica seleccionada. " + resConfiguraciones.getMensaje());
            return Collections.EMPTY_LIST;
        }
        rol.getDtoUnidadConfiguracionesAlineacionMap().put(dtoCargaAcademica, resConfiguraciones.getValor());

        return  rol.getDtoUnidadConfiguracionesAlineacionMap().get(dtoCargaAcademica);
    }

    public DtoUnidadesCalificacionEstudiante getContenedor(DtoCargaAcademica dtoCargaAcademica, Estudiante estudiante){
        if(rol.getDtoUnidadesCalificacionMap().containsKey(dtoCargaAcademica)) return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
        ResultadoEJB<DtoUnidadesCalificacionEstudiante> resDtoUnidadesCalificacion = ejb.packDtoUnidadesCalificacion(estudiante, dtoCargaAcademica, getUnidades(dtoCargaAcademica));
        if(!resDtoUnidadesCalificacion.getCorrecto()){
            mostrarMensaje("No se detectaron registros de calificaciones de la carga seleccionada. " + resDtoUnidadesCalificacion.getMensaje());
            return null;
        }

        rol.getDtoUnidadesCalificacionMap().put(dtoCargaAcademica, resDtoUnidadesCalificacion.getValor());
        return rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica);
    }
    
    public DtoUnidadesCalificacionEstudianteAlineacion getContenedor2(DtoCargaAcademica dtoCargaAcademica, Estudiante estudiante){
        if(rol.getDtoUnidadesCalificacioAlineacionnMap().containsKey(dtoCargaAcademica)) return rol.getDtoUnidadesCalificacioAlineacionnMap().get(dtoCargaAcademica);
        ResultadoEJB<DtoUnidadesCalificacionEstudianteAlineacion> resDtoUnidadesCalificacion = ejb.packDtoUnidadesCalificacionAlineacion(estudiante, dtoCargaAcademica, getUnidadesAlineacion(dtoCargaAcademica));
        if(!resDtoUnidadesCalificacion.getCorrecto()){
            mostrarMensaje("No se detectaron registros de calificaciones de la carga seleccionada. " + resDtoUnidadesCalificacion.getMensaje());
            return null;
        }

        rol.getDtoUnidadesCalificacioAlineacionnMap().put(dtoCargaAcademica, resDtoUnidadesCalificacion.getValor());
        return rol.getDtoUnidadesCalificacioAlineacionnMap().get(dtoCargaAcademica);
    }

    public  Boolean tieneIntegradora(DtoCargaAcademica dtoCargaAcademica, Estudiante estudiante){
        if(rol.getTieneIntegradoraMap().containsKey(dtoCargaAcademica)) return rol.getTieneIntegradoraMap().get(dtoCargaAcademica);

        ResultadoEJB<TareaIntegradora> tareaIntegradoraResultadoEJB = ejbCapturaTareaIntegradora.verificarTareaIntegradora(dtoCargaAcademica);
        if(tareaIntegradoraResultadoEJB.getCorrecto()) {
            rol.getTieneIntegradoraMap().put(dtoCargaAcademica, true);
            rol.getTareaIntegradoraMap().put(dtoCargaAcademica, tareaIntegradoraResultadoEJB.getValor());
            rol.setTieneIntegradora(Boolean.TRUE);

            ResultadoEJB<Map<DtoCargaAcademica, TareaIntegradoraPromedio>> generarContenedorCalificaciones = ejb.generarContenedorCalificaciones(dtoCargaAcademica, estudiante, tareaIntegradoraResultadoEJB.getValor());
            if(!generarContenedorCalificaciones.getCorrecto()){
                mostrarMensajeResultadoEJB(generarContenedorCalificaciones);
            }
            if(estudiante.getCalificacionEvidenciaInstrumentoList().isEmpty() && estudiante.getCalificacionList().isEmpty()){
                //System.out.println("Ambos estan vacios");
                rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica).setTareaIntegradoraPromedioMap(generarContenedorCalificaciones.getValor());
            }
            if(estudiante.getCalificacionEvidenciaInstrumentoList().isEmpty() && !estudiante.getCalificacionList().isEmpty()){
                //System.out.println("Vacio el primero pero el segundo no");
                rol.getDtoUnidadesCalificacionMap().get(dtoCargaAcademica).setTareaIntegradoraPromedioMap(generarContenedorCalificaciones.getValor());
            }
            if(estudiante.getCalificacionList().isEmpty() && !estudiante.getCalificacionEvidenciaInstrumentoList().isEmpty()){
                //System.out.println("Vacio el segundo pero el primero no");
                rol.getDtoUnidadesCalificacioAlineacionnMap().get(dtoCargaAcademica).setTareaIntegradoraPromedioMap(generarContenedorCalificaciones.getValor());
            }
            
            
        }
        else{
            rol.getTieneIntegradoraMap().put(dtoCargaAcademica, false);
        }

        return rol.getTieneIntegradoraMap().get(dtoCargaAcademica);
    }

    public DtoCalificacionNivelacion getNivelacion(DtoCargaAcademica dtoCargaAcademica, Estudiante estudiante){
        DtoUnidadesCalificacionEstudiante.DtoNivelacionPK pk = new DtoUnidadesCalificacionEstudiante.DtoNivelacionPK(dtoCargaAcademica, estudiante);
        if(getContenedor(dtoCargaAcademica, estudiante).getNivelacionMap().containsKey(pk)) return getContenedor(dtoCargaAcademica, estudiante).getNivelacionMap().get(pk);
        //if(getContenedor2(dtoCargaAcademica, estudiante).getNivelacionMap().containsKey(pk)) return getContenedor2(dtoCargaAcademica, estudiante).getNivelacionMap().get(pk);
        ResultadoEJB<DtoCalificacionNivelacion> packDtoCalificacionNivelacion = ejb.packDtoCalificacionNivelacion(dtoCargaAcademica, estudiante);
        if(packDtoCalificacionNivelacion.getCorrecto()){
            DtoCalificacionNivelacion dtoCalificacionNivelacion = packDtoCalificacionNivelacion.getValor();
            getContenedor(dtoCargaAcademica, estudiante).getNivelacionMap().put(pk, dtoCalificacionNivelacion);
            return getContenedor(dtoCargaAcademica, estudiante).getNivelacionMap().get(pk);
        }else {
            mostrarMensajeResultadoEJB(packDtoCalificacionNivelacion);
            return new DtoCalificacionNivelacion(new CalificacionNivelacion(), new Indicador());
        }
    }
    
    public DtoCalificacionNivelacion getNivelacion2(DtoCargaAcademica dtoCargaAcademica, Estudiante estudiante){
        DtoUnidadesCalificacionEstudianteAlineacion.DtoNivelacionPK pk = new DtoUnidadesCalificacionEstudianteAlineacion.DtoNivelacionPK(dtoCargaAcademica, estudiante);
        if(getContenedor2(dtoCargaAcademica, estudiante).getNivelacionMap().containsKey(pk)) return getContenedor2(dtoCargaAcademica, estudiante).getNivelacionMap().get(pk);
        //if(getContenedor2(dtoCargaAcademica, estudiante).getNivelacionMap().containsKey(pk)) return getContenedor2(dtoCargaAcademica, estudiante).getNivelacionMap().get(pk);
        ResultadoEJB<DtoCalificacionNivelacion> packDtoCalificacionNivelacion = ejb.packDtoCalificacionNivelacion(dtoCargaAcademica, estudiante);
        if(packDtoCalificacionNivelacion.getCorrecto()){
            DtoCalificacionNivelacion dtoCalificacionNivelacion = packDtoCalificacionNivelacion.getValor();
            getContenedor2(dtoCargaAcademica, estudiante).getNivelacionMap().put(pk, dtoCalificacionNivelacion);
            return getContenedor2(dtoCargaAcademica, estudiante).getNivelacionMap().get(pk);
        }else {
            mostrarMensajeResultadoEJB(packDtoCalificacionNivelacion);
            return new DtoCalificacionNivelacion(new CalificacionNivelacion(), new Indicador());
        }
    }

    public BigDecimal getPromedioAsignaturaEstudiante(DtoCargaAcademica dtoCargaAcademica, Estudiante estudiante){
        ResultadoEJB<BigDecimal> res = ejb.promediarAsignatura(getContenedor(dtoCargaAcademica, estudiante), dtoCargaAcademica, estudiante);
        if(res.getCorrecto()){
            return res.getValor().setScale(2, RoundingMode.HALF_UP);
        }else{
            return BigDecimal.ZERO;
        }
    }
    
    public BigDecimal getPromedioAsignaturaAlineacionEstudiante(DtoCargaAcademica dtoCargaAcademica, Estudiante estudiante){
        ResultadoEJB<BigDecimal> res = ejb.promediarAsignatura(getContenedor2(dtoCargaAcademica, estudiante), dtoCargaAcademica, estudiante);
        if(res.getCorrecto()){
            return res.getValor().setScale(2, RoundingMode.HALF_UP);
        }else{
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getPromedioFinal(DtoCargaAcademica dtoCargaAcademica, Estudiante estudiante){
        BigDecimal promedioOrdinario = getPromedioAsignaturaEstudiante(dtoCargaAcademica, estudiante);
        BigDecimal nivelacion = new BigDecimal(getNivelacion(dtoCargaAcademica, estudiante).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal promedioFinal = BigDecimal.ZERO;
        if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
            promedioFinal = promedioFinal.add(promedioOrdinario);
        }else{
            promedioFinal = promedioFinal.add(nivelacion);
        }
        return promedioFinal;
    }
    
    public BigDecimal getPromedioFinalAlineacion(DtoCargaAcademica dtoCargaAcademica, Estudiante estudiante){
        BigDecimal promedioOrdinario = getPromedioAsignaturaAlineacionEstudiante(dtoCargaAcademica, estudiante);
        BigDecimal nivelacion = new BigDecimal(getNivelacion2(dtoCargaAcademica, estudiante).getCalificacionNivelacion().getValor()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal promedioFinal = BigDecimal.ZERO;
        if(nivelacion.compareTo(BigDecimal.ZERO) == 0){
            promedioFinal = promedioFinal.add(promedioOrdinario);
        }else{
            promedioFinal = promedioFinal.add(nivelacion);
        }
        return promedioFinal;
    }

    public BigDecimal getPromedioCuatrimestral(Estudiante estudiante){
        BigDecimal promedioCuatrimestral;
        if(estudiante.getGrupo().getIdGrupo() == null) return BigDecimal.ZERO;
        if(estudiante.getGrupo().getCargaAcademicaList().isEmpty()) return BigDecimal.ZERO;
        ResultadoEJB<List<DtoCargaAcademica>> resCargas = ejb.obtenerCargasAcademicas(estudiante);
        List<BigDecimal> lista = new ArrayList<>();
        if(!resCargas.getCorrecto()) mostrarMensajeResultadoEJB(resCargas);
        else rol.setCargasEstudiante(resCargas.getValor());
        rol.getCargasEstudiante().forEach(dtoCargaAcademica -> {
            if(estudiante.getCalificacionEvidenciaInstrumentoList().isEmpty()){
                lista.add(getPromedioFinal(dtoCargaAcademica, estudiante));
            }
            if(estudiante.getCalificacionList().isEmpty()){
                lista.add(getPromedioFinalAlineacion(dtoCargaAcademica, estudiante));
            }
        });
        BigDecimal totalMaterias = new BigDecimal(rol.getCargasEstudiante().size());
        
        BigDecimal suma = lista.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        //System.out.println("Estudiante:"+estudiante.getMatricula()+"- Materias:"+ totalMaterias+" Suma calificaciones:"+ suma);
        promedioCuatrimestral = suma.divide(totalMaterias, RoundingMode.HALF_UP);
        return promedioCuatrimestral.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal obtenerPromedioAcumulado(){
        try{
        BigDecimal promedio;
        BigDecimal totalRegistro;
        if(rol.getEstudiante().getInscripcionActiva().getGrupo().getGrado() == 6){
            totalRegistro = new BigDecimal(rol.getEstudiante().getInscripciones().size() - 1);
        }else{
            totalRegistro = new BigDecimal(rol.getEstudiante().getInscripciones().size());
        }
        
        BigDecimal suma;
        List<BigDecimal> promedios = new ArrayList<>();
        rol.getEstudiante().getInscripciones().forEach(estudiante -> {
            promedios.add(getPromedioCuatrimestral(estudiante.getInscripcion()));
        });
        suma = promedios.stream().map(BigDecimal::plus).reduce(BigDecimal.ZERO, BigDecimal::add);
        promedio = suma.divide(totalRegistro,8 ,RoundingMode.HALF_UP);
        return promedio;
        }catch(Exception e){
           return BigDecimal.ZERO;
        }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "consulta de calificaciones por coordinador";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

}
