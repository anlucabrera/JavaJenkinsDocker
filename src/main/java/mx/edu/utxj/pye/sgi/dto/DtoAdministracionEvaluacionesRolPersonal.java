/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import com.github.adminfaces.starter.infra.model.Filter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author Planeacion
 */
public class DtoAdministracionEvaluacionesRolPersonal extends AbstractRol{
    
    @Getter @Setter @NonNull private PersonalActivo personalActivo;
    
    @Getter @Setter @NonNull private String tipoEvalaucion;
    
    @Getter @Setter @NonNull private Integer evSelect;
    @Getter @Setter @NonNull private Integer peSelect;
    @Getter @Setter @NonNull private Integer idEvTutor;
    
    @Getter @Setter @NonNull private Boolean renderBtnResultados, renderBtnDownloadCedulas360, renderBtnDownloadCedulasDes, renderBtnDownloadRepGral;
    @Getter @Setter @NonNull private Boolean renderDTB360, renderDTBDesempenio, renderDTBDocente, renderDTBTutor, renderDTBResGral, renderSelectPeGlobal;
    
    @Getter @Setter @NonNull private List<SelectItem> itemsPeriodos;
    @Getter @Setter @NonNull private List<DtoEvaluacion> listaEvaluaciones;
    @Getter @Setter @NonNull private List<ListaPersonal> listaPersonal;
    @Getter @Setter @NonNull private List<ListaEvaluacion360Promedios> dtoListaEv360;
    @Getter @Setter @NonNull private List<ListaEvaluacionDesempenioPromedios> dtoListaEvDes;
    @Getter @Setter @NonNull private List<ListaEvaluacionDocenteMateria> dtoListaEvDoc;
    @Getter @Setter @NonNull private List<ListaEvaluacionTutorPromedios> dtoListaEvTutor;
    @Getter @Setter @NonNull private List<ListadoGeneralEvaluacionesPersonal> dtoListaGeneralResultados;
    @Getter @Setter @NonNull private List<Personal> listaPersonas;
    @Getter @Setter @NonNull private List<PeriodosEscolares> listaPeriodos;
    
    @Getter @Setter private Evaluaciones test;
    
    @Getter @Setter @NonNull private Boolean esTutor;
    @Getter @Setter @NonNull private Integer periodoActivo;
    @Getter @Setter @NonNull private Integer idGrupo;
    
    @Getter @Setter @NonNull private Short cveCarrera;
    @Getter @Setter @NonNull private String nombre;
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
    @Getter @Setter private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    
    public DtoAdministracionEvaluacionesRolPersonal(Filter<PersonalActivo> filtro,PersonalActivo personal) {
        super(filtro);
        this.personalActivo = personal;
    }
    
}
