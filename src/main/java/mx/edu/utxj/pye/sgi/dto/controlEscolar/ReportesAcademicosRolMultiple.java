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
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
public class ReportesAcademicosRolMultiple extends AbstractRol{
    /**
     * Representa la referencia hacia al usuario
     */
    @Getter @NonNull private PersonalActivo usuario;
    
     /**
     * Periodo escolar activo
     */
    @Getter @NonNull private Integer periodoActivo;
  
    /**
     * Lista periodos escolares
     */
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    
    /**
     * Periodo escolar seleccionado
     */
    @Getter @NonNull private PeriodosEscolares periodo;
    
    /**
     * Lista de niveles educativos
     */
    @Getter @NonNull private List<ProgramasEducativosNiveles> niveles;
    
    /**
     * Nivel educativo seleccionado
     */
    @Getter @NonNull private ProgramasEducativosNiveles nivel;
    
    /**
     * Lista de programas educativos
     */
    @Getter @NonNull private List<AreasUniversidad> programas;
    
    /**
     * Prigrama educativo seleccionado
     */
    @Getter @NonNull private AreasUniversidad programa;
    
    /**
     * Lista de tipos de reportes
     */
    @Getter @NonNull private List<String> reportes;
    
     /**
     * Reporte seleccionado
     */
    @Getter @NonNull private String reporte;
    
     /**
     * Reporte de estudiantes irregulares
     */
    @Getter @NonNull private List<DtoEstudianteIrregular> estudiantesIrregulares;
    
    /**
     * Reporte de planeación docente
     */
    @Getter @NonNull private List<DtoReportePlaneacionDocente> planeacionDocente;
    
      /**
     * Reporte de aprovechamiento escolar
     */
    @Getter @NonNull private List<DtoAprovechamientoEscolar> aprovechamientoEscolar;
//    
//      /**
//     * Reporte de asignaturas reprobadas
//     */
//    @Getter @NonNull private List<DtoReporteAsignaturasReprobadas> asignaturasReprobadas;
    
      /**
     * Reporte de matricula activa
     */
    @Getter @NonNull private List<DtoDatosEstudiante> matricula;
    
     /**
     * Reporte de deserción académica 
     */
    @Getter @NonNull private List<DtoTramitarBajas> desercionAcademica;
    
      /**
     * Valor del status de la baja y área que valida
     */
    @Getter @NonNull private DtoValidacionesBaja dtoValidacionesBaja;
    
    
    public ReportesAcademicosRolMultiple(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
        super(filtro);
        this.usuario = usuario;
    }

    public void setUsuario(PersonalActivo usuario) {
        this.usuario = usuario;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    public void setNiveles(List<ProgramasEducativosNiveles> niveles) {
        this.niveles = niveles;
    }

    public void setNivel(ProgramasEducativosNiveles nivel) {
        this.nivel = nivel;
    }

    public void setProgramas(List<AreasUniversidad> programas) {
        this.programas = programas;
    }

    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
    }

    public void setReportes(List<String> reportes) {
        this.reportes = reportes;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }

    public void setEstudiantesIrregulares(List<DtoEstudianteIrregular> estudiantesIrregulares) {
        this.estudiantesIrregulares = estudiantesIrregulares;
    }

    public void setPlaneacionDocente(List<DtoReportePlaneacionDocente> planeacionDocente) {
        this.planeacionDocente = planeacionDocente;
    }

    public void setAprovechamientoEscolar(List<DtoAprovechamientoEscolar> aprovechamientoEscolar) {
        this.aprovechamientoEscolar = aprovechamientoEscolar;
    }
//
//    public void setAsignaturasReprobadas(List<DtoReporteAsignaturasReprobadas> asignaturasReprobadas) {
//        this.asignaturasReprobadas = asignaturasReprobadas;
//    }

    public void setMatricula(List<DtoDatosEstudiante> matricula) {
        this.matricula = matricula;
    }

    public void setDesercionAcademica(List<DtoTramitarBajas> desercionAcademica) {
        this.desercionAcademica = desercionAcademica;
    }

    public void setDtoValidacionesBaja(DtoValidacionesBaja dtoValidacionesBaja) {
        this.dtoValidacionesBaja = dtoValidacionesBaja;
    }
    
}
