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
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FuncionesTutor;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

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
    
    @Getter     @NonNull    private                                 AreasUniversidad                        programa;
    @Getter     @NonNull    private                                 AreasUniversidad                        programaEducativo;
    @Getter     @NonNull    private                                 List<AreasUniversidad>                  programasEducativos;
    
    @Getter     @NonNull    private                                 DtoPlanAccionTutorial                   dtoPlanAccionTutorial;
    @Getter     @NonNull    private                                 List<DtoPlanAccionTutorial>             listaDtoPlanAccionTutorial;

    @Getter     private     List<FuncionesTutor>                    listaFuncionesTutor;
    @Getter     private     List<SesionesGrupalesTutorias>          listaSesionesGrupalesTutorias;
    
    public ValidacionPlanAccionTutorialRolPsicopedagogia(Filter<PersonalActivo> filtro,PersonalActivo psicopedagogia) {
        super(filtro);
        this.psicopedagogia = psicopedagogia;
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
}
