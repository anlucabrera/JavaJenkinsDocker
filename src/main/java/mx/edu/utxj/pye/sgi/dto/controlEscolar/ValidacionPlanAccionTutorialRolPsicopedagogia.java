/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asesoria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesoriasEstudiantes;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FuncionesTutor;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutorias;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutoriasGrupales;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;

/**
 *
 * @author UTXJ
 */
public class ValidacionPlanAccionTutorialRolPsicopedagogia extends AbstractRol{
    private static final long serialVersionUID = 5895980353592571097L;
    @Getter     @NonNull    private                                 PersonalActivo                          psicopedagogia;
    
    @Getter     private     Integer                                 periodoActivo;
    @Getter     private     PeriodosEscolares                       periodoSeleccionado;
    @Getter     private     List<PeriodosEscolares>                 periodosConPlanAccionTutorial;
    
    @Getter     private     EventosRegistros                        eventoRegistroActivo, eventoSeleccionado;
    @Getter     private     List<EventosRegistros>                  eventosPorPeriodo;    
    
    @Getter     @NonNull    private                                 AreasUniversidad                        programa;
    @Getter     @NonNull    private                                 AreasUniversidad                        programaEducativo;
    @Getter     @NonNull    private                                 List<AreasUniversidad>                  programasEducativos;
    
    @Getter     @NonNull    private                                 DtoPlanAccionTutorial                   dtoPlanAccionTutorial;
    @Getter     @NonNull    private                                 List<DtoPlanAccionTutorial>             listaDtoPlanAccionTutorial;
    @Getter     @Setter     private                                 List<DtoPlanAccionTutorial>             listaDtoPlanAccionTutorialFiltro;
    
    @Getter     private     TutoriasGrupales                        tutoriaGrupal;
    @Getter     private     List<TutoriasGrupales>                  listaTutoriasGrupales;
    
    @Getter     private     DtoTutoriaIndividualCE                  dtoTutoriaIndividualCE;
    @Getter     @Setter     private                                 DtoTutoriaIndividualCE                  tutoriaIndividualSeleccionada;
    @Getter     private     List<DtoTutoriaIndividualCE>            listaDtoTutoriasIndividuales;

    @Getter     private     List<Asesoria>                          listaAsesorias;
    
    @Getter     private     List<AsesoriasEstudiantes>              listaAsesoriasGenerales;
    
    @Getter     private     List<FuncionesTutor>                    listaFuncionesTutor;
    @Getter     private     List<SesionesGrupalesTutorias>          listaSesionesGrupalesTutorias;
    
    @Getter     @Setter     private                                 List<String>                             listaSecundariaProgramasEducativos;        
    
    @Getter     @NonNull    private                                 String                                      pistaDocente;
    @Getter     @NonNull    private                                 PersonalActivo                              docente;
    @Getter     @NonNull    private                                 List<PersonalActivo>                        docentes;
    
    @Getter     @Setter     private                                 Boolean                                     validaCT;
    @Getter     @Setter     private                                 Boolean                                     moduloAsignado;
    
    public ValidacionPlanAccionTutorialRolPsicopedagogia(Filter<PersonalActivo> filtro,PersonalActivo psicopedagogia) {
        super(filtro);
        this.psicopedagogia = psicopedagogia;
        this.moduloAsignado = false;
    }

    public void setPsicopedagogia(PersonalActivo psicopedagogia) {
        this.psicopedagogia = psicopedagogia;
    }
    
    public void setPeriodoSeleccionado(PeriodosEscolares periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
        this.setListaDtoPlanAccionTutorial(Collections.EMPTY_LIST);
        if(this.periodoSeleccionado == null){
            setProgramasEducativos(Collections.EMPTY_LIST);
        }
    }

    public void setPeriodosConPlanAccionTutorial(List<PeriodosEscolares> periodosConPlanAccionTutorial) {
        this.periodosConPlanAccionTutorial = periodosConPlanAccionTutorial;
        if(!periodosConPlanAccionTutorial.isEmpty()){
            periodoSeleccionado = periodosConPlanAccionTutorial.get(0);
        }
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setProgramaEducativo(AreasUniversidad programaEducativo) {
        this.programaEducativo = programaEducativo;
        this.setListaDtoPlanAccionTutorial(Collections.EMPTY_LIST);
    }

    public void setProgramasEducativos(List<AreasUniversidad> programasEducativos) {
        this.programasEducativos = programasEducativos;
        if(!programasEducativos.isEmpty()){
            programaEducativo = programasEducativos.get(0);
        }else{
            programaEducativo = null;
        }
    }

    public void setDtoPlanAccionTutorial(DtoPlanAccionTutorial dtoPlanAccionTutorial) {
        this.dtoPlanAccionTutorial = dtoPlanAccionTutorial;
    }

    public void setListaDtoPlanAccionTutorial(List<DtoPlanAccionTutorial> listaDtoPlanAccionTutorial) {
        this.listaDtoPlanAccionTutorial = listaDtoPlanAccionTutorial;
    }

    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
    }

    public void setListaFuncionesTutor(List<FuncionesTutor> listaFuncionesTutor) {
        this.listaFuncionesTutor = listaFuncionesTutor;
    }

    public void setListaSesionesGrupalesTutorias(List<SesionesGrupalesTutorias> listaSesionesGrupalesTutorias) {
        this.listaSesionesGrupalesTutorias = listaSesionesGrupalesTutorias;
    }

    public void setEventoRegistroActivo(EventosRegistros eventoRegistroActivo) {
        this.eventoRegistroActivo = eventoRegistroActivo;
    }
    
    public void setEventoSeleccionado(EventosRegistros eventoSeleccionado) {
        this.eventoSeleccionado = eventoSeleccionado;
    }

    public void setEventosPorPeriodo(List<EventosRegistros> eventosPorPeriodo) {
        this.eventosPorPeriodo = eventosPorPeriodo;
        if (eventosPorPeriodo != null && !eventosPorPeriodo.isEmpty()) {
            setEventoSeleccionado(eventosPorPeriodo.get(0));
            if (getEventosPorPeriodo().contains(getEventoRegistroActivo())) {//si la lista de eventos contiene al evento actual se declara a este como seleccionado
                setEventoSeleccionado(getEventoRegistroActivo());
            }
        }
    }

    public void setTutoriaGrupal(TutoriasGrupales tutoriaGrupal) {
        this.tutoriaGrupal = tutoriaGrupal;
    }

    public void setListaTutoriasGrupales(List<TutoriasGrupales> listaTutoriasGrupales) {
        this.listaTutoriasGrupales = listaTutoriasGrupales;
    }
    
    public void setDtoTutoriaIndividualCE(DtoTutoriaIndividualCE dtoTutoriaIndividualCE) {
        this.dtoTutoriaIndividualCE = dtoTutoriaIndividualCE;
    }

    public void setListaDtoTutoriasIndividuales(List<DtoTutoriaIndividualCE> listaDtoTutoriasIndividuales) {
        this.listaDtoTutoriasIndividuales = listaDtoTutoriasIndividuales;
    }

    public void setListaAsesorias(List<Asesoria> listaAsesorias) {
        this.listaAsesorias = listaAsesorias;
    }

    public void setListaAsesoriasGenerales(List<AsesoriasEstudiantes> listaAsesoriasGenerales) {
        this.listaAsesoriasGenerales = listaAsesoriasGenerales;
    }
    
    public void setPistaDocente(String pistaDocente) {
        this.pistaDocente = pistaDocente;
    }

    public void setDocente(PersonalActivo docente) {
        this.docente = docente;
    }

    public void setDocentes(List<PersonalActivo> docentes) {
        this.docentes = docentes;
    }
    
}
