/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionConocimientoCodigoEticaResultados2;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author Planeacion
 */
public class SeguimientoEncuestaRolSeguimientoEgresados extends AbstractRol {
    
    @Getter @Setter private String nombreExcel1;
    
    @Getter @Setter private String nombreExcel2;
    
    @Getter @Setter private String excelSalida;
    
    @Getter @Setter private String subCarpeta;
    
    @Getter @Setter private String carpeta;
    
    @Getter @Setter private String libro;
    
    @Getter @Setter private String tipoEvaluacion;
    
    @Getter @Setter private String tipoEvaluacion2;
    
    @Getter @Setter private Date fechaIn;
    
    @Getter @Setter private Date fechaFin;
    
    @Getter @Setter private Integer evaluacion;
    
    @Getter @Setter private String matricula;
    
    @Getter @Setter private Integer matriculaR;
    
    @Getter @Setter private Integer evaluacionR;
    
    @Getter @Setter private Integer periodo;
    
    @Getter @Setter private Integer periodoB;
    
    @Getter @Setter private Integer periodoConsulta;
    
    @Getter @Setter private Integer periodoConsulta2;
    
    @Getter @Setter private Double porcentaje;
    
    @Getter @Setter private Boolean activarBtn;
    
    @Getter @Setter private Boolean activarBtn2;
    
    @Getter @Setter private Boolean activarBtn3;
   
    @Getter @Setter private Boolean activarBtn4;
    
    @Getter @Setter private Boolean activarBtn5;
    
    @Getter @Setter private Boolean estaActiva;
    
    @Getter @Setter private Boolean activarInput;
    
    @Getter @Setter private Boolean activarTabViewRegistro;
    
    @Getter @Setter private PersonalActivo personalActivo;
    
    @Getter @Setter private AreasUniversidad programa;
    
    @Getter @Setter private Evaluaciones evaluacionA; 
    
    @Getter @Setter private List<EvaluacionConocimientoCodigoEticaResultados2> lista;
    @Getter @Setter private List<EvaluacionConocimientoCodigoEticaResultados2> listaResultados;
    
    @Getter @Setter private DtoEstudianteEncuesta.DtoEstudianteControlEscolar dtoEstudianteCE;
    @Getter @Setter private DtoEstudianteEncuesta.DtoEstudianteSaiiut dtoEstudianteSA;
    @Getter @Setter private DtoEstudianteEncuesta.DtoEstudianteInformacion dtoEstudianteInf;
    @Getter @Setter private DtoEstudianteEncuesta.DtoInformacionResultados dtoSeleccionado;
    @Getter @Setter private DtoEstudianteEncuesta.DtoInformacionResultados2 dtoSeleccionado2;
    
    @Getter @Setter private List<DtoEstudianteEncuesta.DtoGeneraciones> listaDto;
    @Getter @Setter private List<DtoEstudianteEncuesta.DtoEstudiante> listaDtoE;
    @Getter @Setter private List<DtoEstudianteEncuesta.DtoEstudiante> listaRegistro;
    @Getter @Setter private List<DtoEstudianteEncuesta.DtoInformacionResultados> listaDtoResultados;
    @Getter @Setter private List<DtoEstudianteEncuesta.DtoInformacionResultados> listaDtoFiltro;
    @Getter @Setter private List<DtoEstudianteEncuesta.DtoInformacionResultados2> listaDtoResultados2;
    
    @Getter @Setter private List<Personal> listaIn;
    
    public SeguimientoEncuestaRolSeguimientoEgresados(Filter<PersonalActivo> filtro, PersonalActivo responsable) {
        super(filtro);
        this.personalActivo = responsable;
    }
    
}