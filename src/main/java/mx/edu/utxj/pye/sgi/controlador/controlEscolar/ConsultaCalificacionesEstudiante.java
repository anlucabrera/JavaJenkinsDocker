package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.*;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaCalificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaTareaIntegradora;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
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
import org.omnifaces.util.Faces;

@Named
@ViewScoped
public class ConsultaCalificacionesEstudiante extends ViewScopedRol implements Desarrollable {

    @Getter @Setter private ConsultaCalificacionesRolEstudiante rol = new ConsultaCalificacionesRolEstudiante();
    @Getter @Setter Boolean tieneAcceso = false;
    @EJB
    EjbPropiedades ep;
    @EJB
    EjbConsultaCalificacion ejb;
    @EJB
    EjbCapturaCalificaciones ejbCapturaCalificaciones;
    @EJB
    EjbCapturaTareaIntegradora ejbCapturaTareaIntegradora;
    @Inject
    LogonMB logonMB;

    @PostConstruct
    public void init(){
        try {
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)){
                setVistaControlador(ControlEscolarVistaControlador.CONSULTA_CALIFICACION_ESTUDIANTE);
                ResultadoEJB<DtoEstudiante> resAcceso = ejb.validadEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
                ResultadoEJB<DtoEstudiante> resValidacion = ejb.validadEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
                ResultadoEJB<DtoEstudiante> resEstudianteK = ejb.validadEstudianteK(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
                DtoEstudiante estudiante = resValidacion.getValor();
                DtoEstudiante estudianteK = resEstudianteK.getValor();
                tieneAcceso = rol.tieneAcceso(estudiante, UsuarioTipo.ESTUDIANTE19);
                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

                rol.setEstudiante(estudiante);
                rol.setEstudianteK(estudianteK);
                //////////////////////////////////////////////////////////////////////////////////////////////////////////
                if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
                if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

                rol.setNivelRol(NivelRol.CONSULTA);
                
                
                rol.setInscripciones(
                        rol.getEstudiante()
                                .getInscripciones()
                                .stream()
                                .filter(x -> x.getInscripcion().getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("1")) || 
                                        x.getInscripcion().getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("5")) || 
                                        x.getInscripcion().getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("4")) ||
                                        x.getInscripcion().getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("6")))
                                .collect(Collectors.toList())
                                
                );
                
                        //.stream()
                        //.filter(dtoInscripcion -> dtoInscripcion.getInscripcion().getPeriodo() == ejb.getPeriodoActual().getPeriodo())
                        //.collect(Collectors.toList())
                rol.setEstudianteCE(rol.getInscripciones().get(rol.getInscripciones().size() - 1).getInscripcion());


                rol.setPeriodosEscolares(ejb.obtenerListaPeriodosEscolares().getValor());
            }else{
                return;
            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    public List<DtoCalificacionEstudiante.MapUnidadesTematicas> obtenerUnidadesPorMateria(Estudiante estudiante) {
        List<DtoCalificacionEstudiante.MapUnidadesTematicas> map = new ArrayList<>();
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
        Faces.setApplicationAttribute(clave, cargas);
        return cargas;
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
        BigDecimal totalRegistro = BigDecimal.ZERO;
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

    public void cambiarPeriodo(){
        rol.setInscripciones(rol.getEstudiante().getInscripciones()
                .stream()
                .filter(dtoInscripcion -> dtoInscripcion.getInscripcion().getPeriodo() == rol.getPeriodo())
                .collect(Collectors.toList()));
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "consulta de calificaciones por estudiante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

}
