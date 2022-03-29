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
     * Porcentaje total de eficiencia de estadía
     */
    @Getter @NonNull private String porcentajeEficiencia;
    
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

    public void setTotalAcreditados(Integer totalAcreditados) {
        this.totalAcreditados = totalAcreditados;
    }

    public void setTotalNoAcreditados(Integer totalNoAcreditados) {
        this.totalNoAcreditados = totalNoAcreditados;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setPorcentajeEficiencia(String porcentajeEficiencia) {
        this.porcentajeEficiencia = porcentajeEficiencia;
    }

    
}
