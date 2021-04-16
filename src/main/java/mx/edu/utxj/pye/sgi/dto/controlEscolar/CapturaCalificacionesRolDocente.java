package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Criterio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.util.*;
import java.util.stream.Collectors;

public class CapturaCalificacionesRolDocente extends AbstractRol {
    @Getter private PersonalActivo docenteLogueado;

    @Getter private EventoEscolar eventoActivo;
    @Getter private Integer periodoActivo;
    @Getter private PeriodosEscolares periodoSeleccionado;
    @Getter private List<PeriodosEscolares> periodosConCarga;

    @Getter private DtoCargaAcademica cargaAcademicaSeleccionada;
    @Getter private List<DtoCargaAcademica> cargasDocente;

    @Getter private DtoUnidadConfiguracion dtoUnidadConfiguracionSeleccionada;
    @Getter private List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones;
    
    @Getter private DtoUnidadConfiguracionAlineacion dtoUnidadConfiguracionAlineacionSeleccionada;
    @Getter private List<DtoUnidadConfiguracionAlineacion> dtoUnidadConfiguracionesAlineacion;

    @Getter private DtoGrupoEstudiante estudiantesPorGrupo;
    @Getter private DtoGrupoEstudianteAlineacion estudiantesPorGrupoAlineacion;
    @Getter private DtoCapturaCalificacion dtoCapturaCalificacionSeleccionada;
    @Getter private DtoCapturaCalificacionAlineacion dtoCapturaCalificacionAlineacionSeleccionada;

    @Getter private DtoCapturaCalificacion capturaEstudianteSeleccionado;
    @Getter private DtoCapturaCalificacionAlineacion capturaEstudianteAlineacionSeleccionado;
    @Getter @Setter private Map<Long, Double> calificacionMap = new HashMap<>();
    @Getter @Setter private Map<Long, Boolean> casoCriticoMap = new HashMap<>();
    private Random random = new Random(1000000);

    public CapturaCalificacionesRolDocente(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        this.docenteLogueado = filtro.getEntity();
    }
    /* Sin alineación de materias */
    public List<Criterio> getCriteriosEnUnidadSeleccionada(){
        if(dtoUnidadConfiguracionSeleccionada == null) return Collections.EMPTY_LIST;
        return dtoUnidadConfiguracionSeleccionada.getUnidadMateriaConfiguracionDetalles().keySet().stream().sorted(Comparator.comparingInt(Criterio::getCriterio)).collect(Collectors.toList());
    }

    public List<Criterio> getCriteriosEnUnidad(DtoUnidadConfiguracion dtoUnidadConfiguracion){
        if(dtoUnidadConfiguracion == null) return Collections.EMPTY_LIST;
        return dtoUnidadConfiguracion.getUnidadMateriaConfiguracionDetalles().keySet().stream().sorted(Comparator.comparingInt(Criterio::getCriterio)).collect(Collectors.toList());
    }

    public List<DtoUnidadConfiguracion.Detalle> getDetallesPorCriterioEnUnidadSeleccionada(Criterio criterio){
        if(dtoUnidadConfiguracionSeleccionada == null && criterio == null) return Collections.EMPTY_LIST;

        if(!dtoUnidadConfiguracionSeleccionada.getUnidadMateriaConfiguracionDetalles().containsKey(criterio)) return Collections.emptyList();

        return dtoUnidadConfiguracionSeleccionada.getUnidadMateriaConfiguracionDetalles().get(criterio).stream().sorted(Comparator.comparingInt(detalle -> detalle.getIndicador().getIndicador())).collect(Collectors.toList());
    }

    public DtoCapturaCalificacion.Captura getCapturaPorDetalle(DtoUnidadConfiguracion.Detalle detalle, List<DtoCapturaCalificacion.Captura> capturas){
        if(detalle == null || capturas == null) return null;

        DtoCapturaCalificacion.Captura captura = capturas.stream()
                .filter(captura1 -> captura1.getDetalle().equals(detalle))
                .findFirst()
                .orElse(null);

        return captura;
    }

    public String getIdCalificacion(DtoUnidadConfiguracion.Detalle detalle, List<DtoCapturaCalificacion.Captura> capturas){
        System.out.println("detalle = [" + detalle + "], capturas = [" + capturas + "]");
        if(capturas == null) return "cal".concat(String.valueOf(random.nextInt()));
        DtoCapturaCalificacion.Captura captura = getCapturaPorDetalle(detalle, capturas);
        return "cal_".concat(String.valueOf(captura.getCalificacion().getCalificacion()));
    }
    
    /* Con alineación de materias */
    public List<Criterio> getCriteriosEnUnidadAlineacionSeleccionada(){
        if(dtoUnidadConfiguracionAlineacionSeleccionada == null) return Collections.EMPTY_LIST;
        return dtoUnidadConfiguracionAlineacionSeleccionada.getUnidadMateriaConfiguracionDetalles().keySet().stream().sorted(Comparator.comparingInt(Criterio::getCriterio)).collect(Collectors.toList());
    }

    public List<Criterio> getCriteriosEnUnidadAlineacion(DtoUnidadConfiguracionAlineacion dtoUnidadConfiguracion){
        if(dtoUnidadConfiguracion == null) return Collections.EMPTY_LIST;
        return dtoUnidadConfiguracion.getUnidadMateriaConfiguracionDetalles().keySet().stream().sorted(Comparator.comparingInt(Criterio::getCriterio)).collect(Collectors.toList());
    }

    public List<DtoUnidadConfiguracionAlineacion.Detalle> getDetallesPorCriterioEnUnidadAlineacionSeleccionada(Criterio criterio){
        if(dtoUnidadConfiguracionAlineacionSeleccionada == null && criterio == null) return Collections.EMPTY_LIST;

        if(!dtoUnidadConfiguracionAlineacionSeleccionada.getUnidadMateriaConfiguracionDetalles().containsKey(criterio)) return Collections.emptyList();

        return dtoUnidadConfiguracionAlineacionSeleccionada.getUnidadMateriaConfiguracionDetalles().get(criterio).stream().sorted(Comparator.comparingInt(detalle -> detalle.getEvidencia().getEvidencia())).collect(Collectors.toList());
    }

    public DtoCapturaCalificacionAlineacion.Captura getCapturaPorDetalleAlineacion(DtoUnidadConfiguracionAlineacion.Detalle detalle, List<DtoCapturaCalificacionAlineacion.Captura> capturas){
        if(detalle == null || capturas == null) return null;

        DtoCapturaCalificacionAlineacion.Captura captura = capturas.stream()
                .filter(captura1 -> captura1.getDetalle().equals(detalle))
                .findFirst()
                .orElse(null);

        return captura;
    }

    public String getIdCalificacionAlineacion(DtoUnidadConfiguracionAlineacion.Detalle detalle, List<DtoCapturaCalificacionAlineacion.Captura> capturas){
        System.out.println("detalle = [" + detalle + "], capturas = [" + capturas + "]");
        if(capturas == null) return "cal".concat(String.valueOf(random.nextInt()));
        DtoCapturaCalificacionAlineacion.Captura captura = getCapturaPorDetalleAlineacion(detalle, capturas);
        return "cal_".concat(String.valueOf(captura.getCalificacion().getCalificacionEvidenciaInstrumento()));
    }
    /////////////////////////////////////////////////////////

    public void setDocenteLogueado(PersonalActivo docenteLogueado) {
        this.docenteLogueado = docenteLogueado;
    }

    public void setPeriodoSeleccionado(PeriodosEscolares periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
        this.setCargasDocente(Collections.EMPTY_LIST);
//        this.setCargaAcademicaSeleccionada(null);
//        this.setDtoUnidadConfiguracionSeleccionada(null);
    }

    public void setPeriodosConCarga(List<PeriodosEscolares> periodosConCarga) {
        this.periodosConCarga = periodosConCarga;
        if(!periodosConCarga.isEmpty()){
            periodoSeleccionado = periodosConCarga.get(0);
        }
    }

    public void setCargaAcademicaSeleccionada(DtoCargaAcademica cargaAcademicaSeleccionada) {
//        System.out.println("cargaAcademicaSeleccionada = [" + cargaAcademicaSeleccionada + "]");
        this.cargaAcademicaSeleccionada = cargaAcademicaSeleccionada;
        this.setDtoUnidadConfiguraciones(Collections.emptyList());
//        this.setDtoUnidadConfiguracionSeleccionada(null);
    }

    public void setCargasDocente(List<DtoCargaAcademica> cargasDocente) {
        this.cargasDocente = cargasDocente;
        if(!cargasDocente.isEmpty()) cargaAcademicaSeleccionada = cargasDocente.get(0);
        else cargaAcademicaSeleccionada = null;

    }

    public void setDtoUnidadConfiguracionSeleccionada(DtoUnidadConfiguracion dtoUnidadConfiguracionSeleccionada) {
        this.dtoUnidadConfiguracionSeleccionada = dtoUnidadConfiguracionSeleccionada;
    }

    public void setDtoUnidadConfiguraciones(List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones) {
        this.dtoUnidadConfiguraciones = dtoUnidadConfiguraciones;
        if(!dtoUnidadConfiguraciones.isEmpty()) dtoUnidadConfiguracionSeleccionada = dtoUnidadConfiguraciones.get(0);
        else dtoUnidadConfiguracionSeleccionada = null;
    }

    public void setDtoUnidadConfiguracionAlineacionSeleccionada(DtoUnidadConfiguracionAlineacion dtoUnidadConfiguracionAlineacionSeleccionada) {
        this.dtoUnidadConfiguracionAlineacionSeleccionada = dtoUnidadConfiguracionAlineacionSeleccionada;
    }

    public void setDtoUnidadConfiguracionesAlineacion(List<DtoUnidadConfiguracionAlineacion> dtoUnidadConfiguracionesAlineacion) {
        this.dtoUnidadConfiguracionesAlineacion = dtoUnidadConfiguracionesAlineacion;
        if(!dtoUnidadConfiguracionesAlineacion.isEmpty()) dtoUnidadConfiguracionAlineacionSeleccionada = this.dtoUnidadConfiguracionesAlineacion.get(0);
        else dtoUnidadConfiguracionAlineacionSeleccionada = null;
    }
    
    public void setEstudiantesPorGrupo(DtoGrupoEstudiante estudiantesPorGrupo) {
        this.estudiantesPorGrupo = estudiantesPorGrupo;
        if(estudiantesPorGrupo == null) return;
        calificacionMap.clear();
        estudiantesPorGrupo.getEstudiantes()
                .stream()
                .map(dtoCapturaCalificacion -> dtoCapturaCalificacion.getCapturas())
                .flatMap(capturas -> capturas.stream())
                .map(captura -> captura.getCalificacion())
                .forEach(calificacion -> {
                    Long clave = calificacion.getCalificacion();
                    Double valor = calificacion.getValor();
                    calificacionMap.put(clave, valor);
                });
//        System.out.println("calificacionMap = " + calificacionMap);
    }
    
    public void setEstudiantesPorGrupoAlineacion(DtoGrupoEstudianteAlineacion estudiantesPorGrupoAlineacion) {
        this.estudiantesPorGrupoAlineacion = estudiantesPorGrupoAlineacion;
        if(estudiantesPorGrupoAlineacion == null) return;
        calificacionMap.clear();
        estudiantesPorGrupoAlineacion.getEstudiantes()
                .stream()
                .map(dtoCapturaCalificacion -> dtoCapturaCalificacion.getCapturas())
                .flatMap(capturas -> capturas.stream())
                .map(captura -> captura.getCalificacion())
                .forEach(calificacion -> {
                    Long clave = calificacion.getCalificacionEvidenciaInstrumento();
                    Double valor = calificacion.getValor();
                    calificacionMap.put(clave, valor);
                });
//        System.out.println("calificacionMap = " + calificacionMap);
    }

    public void setCapturaEstudianteSeleccionado(DtoCapturaCalificacion capturaEstudianteSeleccionado) {
        this.capturaEstudianteSeleccionado = capturaEstudianteSeleccionado;
    }

    public void setCapturaEstudianteAlineacionSeleccionado(DtoCapturaCalificacionAlineacion capturaEstudianteAlineacionSeleccionado) {
        this.capturaEstudianteAlineacionSeleccionado = capturaEstudianteAlineacionSeleccionado;
    }
    
    public void setEventoActivo(EventoEscolar eventoActivo) {
        this.eventoActivo = eventoActivo;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setDtoCapturaCalificacionSeleccionada(DtoCapturaCalificacion dtoCapturaCalificacionSeleccionada) {
        this.dtoCapturaCalificacionSeleccionada = dtoCapturaCalificacionSeleccionada;
//        System.out.println("dtoCapturaCalificacionSeleccionada = " + this.dtoCapturaCalificacionSeleccionada);
    }

    public void setDtoCapturaCalificacionAlineacionSeleccionada(DtoCapturaCalificacionAlineacion dtoCapturaCalificacionAlineacionSeleccionada) {
        this.dtoCapturaCalificacionAlineacionSeleccionada = dtoCapturaCalificacionAlineacionSeleccionada;
    }
    
}
