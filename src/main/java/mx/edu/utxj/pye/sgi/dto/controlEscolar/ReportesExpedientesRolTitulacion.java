/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;

/**
 *
 * @author UTXJ
 */
public class ReportesExpedientesRolTitulacion extends AbstractRol{
    /**
     * Representa la referencia hacia el personal de la coordinación de titulación
     */
    @Getter @NonNull private PersonalActivo personal;
    
    /**
     * Lista generaciones
     */
    @Getter @NonNull private List<Generaciones> generaciones;
    
    /**
     * Lista niveles educativos
     */
    @Getter @NonNull private List<ProgramasEducativosNiveles> nivelesEducativos;
    
    /**
     * Lista programas educativos
     */
    @Getter @NonNull private List<AreasUniversidad> programasEducativos;
    
     /**
     * Lista de reportes
     */
    @Getter @NonNull private List<String> reportes;
    
    /**
     * Lista expedientes registrados en la generación seleccionada
     */
    @Getter @NonNull private List<DtoReporteEstadisticoTitulacion> reporteEstadisticoTitulacion;
    
     /**
     * Lista expedientes registrados en la generación y programa educativo seleccionado
     */
    @Getter @NonNull private List<DtoReporteFotografiasTitulacion> reporteFotografiasTitulacion;
    
     /**
     * Lista de estudiantes que se encuentran activos de la generación y programa educativo seleccionado
     */
    @Getter @NonNull private List<DtoDatosEstudiante> listadoEstudiantesActivos;
    
     /**
     * Lista de estudiantes que concluyeron estadía de la generación y programa educativo seleccionado
     */
    @Getter @NonNull private List<DtoDatosEstudiante> listadoConcluyeronEstadia;
    
    /**
     * Generacion seleccionada
     */
    @Getter @NonNull private Generaciones generacion;
    
    /**
     * Nivel educativo seleccionado
     */
    @Getter @NonNull private ProgramasEducativosNiveles nivelEducativo;
    
    /**
     * Programa educativo seleccionado
     */
    @Getter @NonNull private AreasUniversidad programaEducativo;
    
    /**
     * Reporte seleccionado
     */
    @Getter @NonNull private String reporte;
    
     /**
     * Representa la clave del periodo escolar activo
     */
    @Getter @NonNull private Integer periodoActivo;
    
     /**
     * Tipo de búsqueda general o por programa educativo
     */
    @Getter @NonNull private String tipoBusqueda ;
    
     /**
     * Deshabilitar tipo de búsqueda
     */
    @Getter @NonNull private Boolean deshabilitarTipoBusqueda ;
  
    
    public ReportesExpedientesRolTitulacion(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }
    
    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setGeneraciones(List<Generaciones> generaciones) {
        this.generaciones = generaciones;
    }

    public void setNivelesEducativos(List<ProgramasEducativosNiveles> nivelesEducativos) {
        this.nivelesEducativos = nivelesEducativos;
    }

    public void setProgramasEducativos(List<AreasUniversidad> programasEducativos) {
        this.programasEducativos = programasEducativos;
    }

    public void setReportes(List<String> reportes) {
        this.reportes = reportes;
    }
    
    public void setReporteEstadisticoTitulacion(List<DtoReporteEstadisticoTitulacion> reporteEstadisticoTitulacion) {
        this.reporteEstadisticoTitulacion = reporteEstadisticoTitulacion;
    }

    public void setReporteFotografiasTitulacion(List<DtoReporteFotografiasTitulacion> reporteFotografiasTitulacion) {
        this.reporteFotografiasTitulacion = reporteFotografiasTitulacion;
    }

    public void setListadoEstudiantesActivos(List<DtoDatosEstudiante> listadoEstudiantesActivos) {
        this.listadoEstudiantesActivos = listadoEstudiantesActivos;
    }

    public void setListadoConcluyeronEstadia(List<DtoDatosEstudiante> listadoConcluyeronEstadia) {
        this.listadoConcluyeronEstadia = listadoConcluyeronEstadia;
    }
    
    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public void setNivelEducativo(ProgramasEducativosNiveles nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public void setProgramaEducativo(AreasUniversidad programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }
    
    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public void setDeshabilitarTipoBusqueda(Boolean deshabilitarTipoBusqueda) {
        this.deshabilitarTipoBusqueda = deshabilitarTipoBusqueda;
    }
    
}

