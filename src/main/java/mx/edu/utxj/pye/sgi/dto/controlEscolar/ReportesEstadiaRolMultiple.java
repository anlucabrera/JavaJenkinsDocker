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
public class ReportesEstadiaRolMultiple extends AbstractRol{
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
     * Reporte de eficiencia de estadía
     */
    @Getter @NonNull private List<DtoEficienciaEstadia> eficienciaEstadia;
    
     /**
     * Total de estudiantes acreditados
     */
    @Getter @NonNull private Integer totalAcreditados;
    
     /**
     * Total de estudiantes no acreditados
     */
    @Getter @NonNull private Integer totalNoAcreditados;
    
     /**
     * Total de estudiantes
     */
    @Getter @NonNull private Integer total;
    
      /**
     * Reporte de listado de estudiantes con promedios
     */
    @Getter @NonNull private List<DtoSeguimientoEstadia> listadoEstudiantesPromedio;
    
      /**
     * Reporte de listado de zona influencia institucional
     */
    @Getter @NonNull private List<DtoZonaInfluenciaEstIns> listaZonaInfluenciaIns;
    
     /**
     * Total de estudiantes colocados
     */
    @Getter @NonNull private Integer totalEstColocados;
    
     /**
     * Total de estudiantes con seguimiento de estadía
     */
    @Getter @NonNull private Long totalEstSeguimientoEstadia;
    
     /**
     * Total de procentaje de colocación
     */
    @Getter @NonNull private Double totalPorcentajeEstColocados;
    
      /**
     * Reporte de listado de zona influencia por programa educativo
     */
    @Getter @NonNull private List<DtoZonaInfluenciaEstPrograma> listaZonaInfluenciaPrograma;
    
      /**
     * Reporte de listado de seguimiento de actividaes de estadía
     */
    @Getter @NonNull private List<DtoReporteActividadesEstadia> listaSegActEstadia;
    
     /**
     * Total de estudiantes iniciaron
     */
    @Getter @NonNull private Integer totalIniciaron;
    
     /**
     * Total de estudiantes activos
     */
    @Getter @NonNull private Integer totalActivos;
    
     /**
     * Total de estudiantes asignados a un asesor académico
     */
    @Getter @NonNull private Integer totalAsignados;
    
     /**
     * Total de estudiantes no asignados a un asesor académico
     */
    @Getter @NonNull private Integer totalNoAsignados;
    
     /**
     * Porcentaje de asignación de asesor académico
     */
    @Getter @NonNull private String porcentajeAsignacion;
    
     /**
     * Total de estudiantes con información de estadía registrada (Empresa, proyecto, etc)
     */
    @Getter @NonNull private Integer totalInfoRegistrada;
    
     /**
     * Total de estudiantes sin información de estadía registrada (Empresa, proyecto, etc)
     */
    @Getter @NonNull private Integer totalSinInfoRegistrada;
    
     /**
     * Porcentaje de registro de información de estadía
     */
    @Getter @NonNull private String porcentajeRegistro;
    
     /**
     * Total de estudiantes con información de estadía validada
     */
    @Getter @NonNull private Integer totalInfoValidada;
    
     /**
     * Total de estudiantes sin información de estadía sin validar
     */
    @Getter @NonNull private Integer totalSinInfoValidada;
    
     /**
     * Porcentaje de validación de información de estadía
     */
    @Getter @NonNull private String porcentajeValidacion;

      /**
     * Reporte de listado de asignación de estudiantes por programa educativo y asesor académico 
     */
    @Getter @NonNull private List<DtoAsigAsesorAcadEstadia> listaAsigAsesorAcad;
    
      /**
     * Reporte de listado de cumplimiento de los estudiantes por documento y programa educativo 
     */
    @Getter @NonNull private List<DtoCumplimientoEstDocEstadia> listaCumplimientoEstudiante;
    
    public ReportesEstadiaRolMultiple(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
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

    public void setEficienciaEstadia(List<DtoEficienciaEstadia> eficienciaEstadia) {
        this.eficienciaEstadia = eficienciaEstadia;
    }

    public void setTotalAcreditados(Integer totalAcreditados) {
        this.totalAcreditados = totalAcreditados;
    }

    public void setTotalNoAcreditados(Integer totalNoAcreditados) {
        this.totalNoAcreditados = totalNoAcreditados;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setListadoEstudiantesPromedio(List<DtoSeguimientoEstadia> listadoEstudiantesPromedio) {
        this.listadoEstudiantesPromedio = listadoEstudiantesPromedio;
    }

    public void setListaZonaInfluenciaIns(List<DtoZonaInfluenciaEstIns> listaZonaInfluenciaIns) {
        this.listaZonaInfluenciaIns = listaZonaInfluenciaIns;
    }

    public void setTotalEstColocados(Integer totalEstColocados) {
        this.totalEstColocados = totalEstColocados;
    }

    public void setTotalEstSeguimientoEstadia(Long totalEstSeguimientoEstadia) {
        this.totalEstSeguimientoEstadia = totalEstSeguimientoEstadia;
    }
    
    public void setTotalPorcentajeEstColocados(Double totalPorcentajeEstColocados) {
        this.totalPorcentajeEstColocados = totalPorcentajeEstColocados;
    }
    
    public void setListaZonaInfluenciaPrograma(List<DtoZonaInfluenciaEstPrograma> listaZonaInfluenciaPrograma) {
        this.listaZonaInfluenciaPrograma = listaZonaInfluenciaPrograma;
    }

    public void setListaSegActEstadia(List<DtoReporteActividadesEstadia> listaSegActEstadia) {
        this.listaSegActEstadia = listaSegActEstadia;
    }

    public void setTotalIniciaron(Integer totalIniciaron) {
        this.totalIniciaron = totalIniciaron;
    }

    public void setTotalActivos(Integer totalActivos) {
        this.totalActivos = totalActivos;
    }

    public void setTotalAsignados(Integer totalAsignados) {
        this.totalAsignados = totalAsignados;
    }

    public void setTotalNoAsignados(Integer totalNoAsignados) {
        this.totalNoAsignados = totalNoAsignados;
    }

    public void setPorcentajeAsignacion(String porcentajeAsignacion) {
        this.porcentajeAsignacion = porcentajeAsignacion;
    }

    public void setTotalInfoRegistrada(Integer totalInfoRegistrada) {
        this.totalInfoRegistrada = totalInfoRegistrada;
    }

    public void setTotalSinInfoRegistrada(Integer totalSinInfoRegistrada) {
        this.totalSinInfoRegistrada = totalSinInfoRegistrada;
    }

    public void setPorcentajeRegistro(String porcentajeRegistro) {
        this.porcentajeRegistro = porcentajeRegistro;
    }

    public void setTotalInfoValidada(Integer totalInfoValidada) {
        this.totalInfoValidada = totalInfoValidada;
    }

    public void setTotalSinInfoValidada(Integer totalSinInfoValidada) {
        this.totalSinInfoValidada = totalSinInfoValidada;
    }

    public void setPorcentajeValidacion(String porcentajeValidacion) {
        this.porcentajeValidacion = porcentajeValidacion;
    }

    public void setListaAsigAsesorAcad(List<DtoAsigAsesorAcadEstadia> listaAsigAsesorAcad) {
        this.listaAsigAsesorAcad = listaAsigAsesorAcad;
    }

    public void setListaCumplimientoEstudiante(List<DtoCumplimientoEstDocEstadia> listaCumplimientoEstudiante) {
        this.listaCumplimientoEstudiante = listaCumplimientoEstudiante;
    }
    
}
