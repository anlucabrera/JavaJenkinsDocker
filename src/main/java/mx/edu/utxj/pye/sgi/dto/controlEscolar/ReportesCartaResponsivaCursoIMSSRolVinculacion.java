/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import java.util.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;

/**
 *
 * @author UTXJ
 */
public class ReportesCartaResponsivaCursoIMSSRolVinculacion extends AbstractRol{
    /**
     * Representa la referencia hacia al usuario
     */
    @Getter @NonNull private PersonalActivo usuario;
  
    /**
     * Lista generaciones
     */
    @Getter @NonNull private List<Generaciones> generaciones;
    
    /**
     * Generación seleccionada
     */
    @Getter @NonNull private Generaciones generacion;
    
   
    /**
     * Lista de niveles educativos
     */
    @Getter @NonNull private List<ProgramasEducativosNiveles> nivelesEducativos;
    
    /**
     * Nivel educativo seleccionado
     */
    @Getter @NonNull private ProgramasEducativosNiveles nivelEducativo;
    
    /**
     * Lista de tipos de reportes
     */
    @Getter @NonNull private List<String> reportes;
    
     /**
     * Reporte seleccionado
     */
    @Getter @NonNull private String reporte;
    
     /**
     * Reporte de cumplimiento por documento
     */
    @Getter @NonNull private List<DtoCumplimientoCartaResponsivaCursoIMSS> cumplimientoEstDocumento;
    
    /**
     * Total de estudiantes que iniciaron el cuatrimestre
     */
    @Getter @NonNull private Integer totalEstudiantesIniciaron;
    
     /**
     * Total de estudiantes activos (regulares o egresados no titulados)
     */
    @Getter @NonNull private Integer totalEstudiantesActivos;
    
     /**
     * Total de estudiantes que cargaron documento
     */
    @Getter @NonNull private Integer totalCargados;
    
     /**
     * Total de estudiantes que no cargaron documento
     */
    @Getter @NonNull private Integer totalSinCargar;
    
     /**
     * Total de estudiantes con documento validado
     */
    @Getter @NonNull private Integer totalValidados;
    
     /**
     * Total de estudiantes con documento sin validar
     */
    @Getter @NonNull private Integer totalNoValidados;
    
     /**
     * Porcentaje total de cumplimiento de carga de documento
     */
    @Getter @NonNull private String porcentajeCumplimiento;
    
     /**
     * Porcentaje total de validación de docuemnto
     */
    @Getter @NonNull private String porcentajeValidacion;
    
     /**
     * Reporte de validación y cumplimiento de los estudiantes por documento por programa educativo 
     */
    @Getter @NonNull private List<DtoReporteDocumentosVinculacion> listaReporteVinculacion;
    
    public ReportesCartaResponsivaCursoIMSSRolVinculacion(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
        super(filtro);
        this.usuario = usuario;
    }

    public void setUsuario(PersonalActivo usuario) {
        this.usuario = usuario;
    }

    public void setGeneraciones(List<Generaciones> generaciones) {
        this.generaciones = generaciones;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public void setNivelesEducativos(List<ProgramasEducativosNiveles> nivelesEducativos) {
        this.nivelesEducativos = nivelesEducativos;
    }

    public void setNivelEducativo(ProgramasEducativosNiveles nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public void setReportes(List<String> reportes) {
        this.reportes = reportes;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }

    public void setCumplimientoEstDocumento(List<DtoCumplimientoCartaResponsivaCursoIMSS> cumplimientoEstDocumento) {
        this.cumplimientoEstDocumento = cumplimientoEstDocumento;
    }

    public void setTotalEstudiantesIniciaron(Integer totalEstudiantesIniciaron) {
        this.totalEstudiantesIniciaron = totalEstudiantesIniciaron;
    }

    public void setTotalEstudiantesActivos(Integer totalEstudiantesActivos) {
        this.totalEstudiantesActivos = totalEstudiantesActivos;
    }

    public void setTotalCargados(Integer totalCargados) {
        this.totalCargados = totalCargados;
    }

    public void setTotalSinCargar(Integer totalSinCargar) {
        this.totalSinCargar = totalSinCargar;
    }

    public void setTotalValidados(Integer totalValidados) {
        this.totalValidados = totalValidados;
    }

    public void setTotalNoValidados(Integer totalNoValidados) {
        this.totalNoValidados = totalNoValidados;
    }
    
    public void setPorcentajeCumplimiento(String porcentajeCumplimiento) {
        this.porcentajeCumplimiento = porcentajeCumplimiento;
    }

    public void setPorcentajeValidacion(String porcentajeValidacion) {
        this.porcentajeValidacion = porcentajeValidacion;
    }

    public void setListaReporteVinculacion(List<DtoReporteDocumentosVinculacion> listaReporteVinculacion) {
        this.listaReporteVinculacion = listaReporteVinculacion;
    }
}
