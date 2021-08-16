/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;

/**
 *
 * @author Planeacion
 */
public class DtoSeguimientoEvaluacionEstadiaRolMultiple extends AbstractRol{
    
    @Getter @Setter @NonNull private PersonalActivo personalActivo;
    @Getter @Setter @NonNull private Personas tutor;
    @Getter @Setter @NonNull private Grupo grupo;
    @Getter @Setter @NonNull private Grupos grupos;
    @Getter @Setter private Evaluaciones test;
    @Getter @Setter private AreasUniversidad area;
    
    @Getter @Setter @NonNull private Boolean esTutor;
    @Getter @Setter @NonNull private Integer periodoActivo;
    @Getter @Setter @NonNull private Integer idGrupo;
    @Getter @Setter @NonNull private Integer periodo;
    @Getter @Setter @NonNull private Short cveCarrera;
    @Getter @Setter private Short areaAcademica;
    @Getter @Setter @NonNull private String nombre2;
    @Getter @Setter @NonNull private String carpeta;
    @Getter @Setter @NonNull private String subcarpeta;
    @Getter @Setter @NonNull private String libro;
    @Getter @Setter @NonNull private String excel;
    @Getter @Setter @NonNull private String nombreWord;
    @Getter @Setter @NonNull private String word;
    @Getter @Setter @NonNull private String nameWord;
    @Getter @Setter @NonNull private String bd;
    @Getter @Setter @NonNull private Integer evaluacion;
    @Getter @Setter @NonNull private Integer periodoEvaluacion;
    @Getter @Setter @NonNull private Date fechaInicio;
    @Getter @Setter @NonNull private Date fechaFin;
    @Getter @Setter @NonNull private String tipo;
    @Getter @Setter @NonNull private String var1;
    @Getter @Setter @NonNull private String var2;
    @Getter @Setter @NonNull private String var3;
    @Getter @Setter @NonNull private String var4;
    @Getter @Setter private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoGruposTutor> dtoGrupos = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoGruposTutor> dtoGruposS = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> listaDtoEstudiante = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> listaTestCompleto = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> listaTestIncompleto = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> listaDtoEstudianteS = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> listaTestCompletoS = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> listaTestIncompletoS = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoProgramaEducativo> listaProgramasEducativos = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoPeriodoEscolar> listaPeriodosEscolares = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoAlumnosEvaluacion> listaAlumnosTestCompleto = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoAlumnosEvaluacion> listaAlumnosTestIncompleto = new ArrayList<>();
    @Getter @Setter private List<DtoAlumnosEncuesta.DtoAlumnosEvaluacion> listaAlumnosTestCompletoF;
    @Getter @Setter private List<DtoAlumnosEncuesta.DtoAlumnosEvaluacion> listaAlumnosTestIncompletoF;
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoAvaceTestProgramaEducativo> listaAvanceTest = new ArrayList<>();
    @Getter @Setter @NonNull private List<Evaluaciones> listaEvaluacines = new ArrayList<>();
    @Getter @Setter @NonNull private List<AreasUniversidad> listaAreas = new ArrayList<>();
    @Getter @Setter @NonNull private List<AreasUniversidad> listaProgramas = new ArrayList<>();
    
    public DtoSeguimientoEvaluacionEstadiaRolMultiple(Filter<PersonalActivo> filtro,PersonalActivo psicopedagogia) {
        super(filtro);
        this.personalActivo = psicopedagogia;
    }
    
}
