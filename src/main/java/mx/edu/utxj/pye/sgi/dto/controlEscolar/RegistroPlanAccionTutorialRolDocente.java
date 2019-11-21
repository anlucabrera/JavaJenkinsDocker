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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FuncionesTutor;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanAccionTutorial;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class RegistroPlanAccionTutorialRolDocente extends AbstractRol{

    private static final long serialVersionUID = -4811781384469503837L;
    @Getter     private     PersonalActivo                      docenteLogueado;
    
    @Getter     private     Integer                             periodoActivo;
    @Getter     private     PeriodosEscolares                   periodoSeleccionado;
    @Getter     private     List<PeriodosEscolares>             periodosConCargaGrupo;
    
    @Getter     private     DtoListadoTutores                   grupoTutorSeleccionado;
    @Getter     private     List<DtoListadoTutores>             listadoGruposTutor;
    
    @Getter     private     PlanAccionTutorial                  planAccionTutorial;
    @Getter     private     Boolean                             planRegistrado;
    
    @Getter     private     FuncionesTutor                      funcionesTutor;
    @Getter     private     List<FuncionesTutor>                listaFuncionesTutor;
    
    @Getter     private     SesionesGrupalesTutorias            sesionesGrupalesTutorias;
    @Getter     private     List<SesionesGrupalesTutorias>      listaSesionesGrupalesTutorias;
    
    @Getter     @Setter     private     Boolean                 tab1,tab2,tab3;
    @Getter     @Setter     private     Integer                 tabIndex;
    
    public RegistroPlanAccionTutorialRolDocente(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        this.docenteLogueado = filtro.getEntity();
    }

    public void setDocenteLogueado(PersonalActivo docenteLogueado) {
        this.docenteLogueado = docenteLogueado;
    }

    public void setPeriodoSeleccionado(PeriodosEscolares periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
        this.setListadoGruposTutor(Collections.EMPTY_LIST);
    }
    
    public void setPeriodosConCargaGrupo(List<PeriodosEscolares> periodosConCargaGrupo) {
        this.periodosConCargaGrupo = periodosConCargaGrupo;
        if(!periodosConCargaGrupo.isEmpty()){
            periodoSeleccionado = periodosConCargaGrupo.get(0);
        }
    }
    
    public void setGrupoTutorSeleccionado(DtoListadoTutores grupoTutorSeleccionado) {
        this.grupoTutorSeleccionado = grupoTutorSeleccionado;
    }

    public void setListadoGruposTutor(List<DtoListadoTutores> listadoGruposTutor) {
        this.listadoGruposTutor = listadoGruposTutor;
        if(!listadoGruposTutor.isEmpty()) grupoTutorSeleccionado = listadoGruposTutor.get(0);
        else grupoTutorSeleccionado = null;
    }
    
    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }
    
    public void setPlanAccionTutorial(PlanAccionTutorial planAccionTutorial) {
        this.planAccionTutorial = planAccionTutorial;
        if(planAccionTutorial.getObjetivo() == null || "" == this.planAccionTutorial.getObjetivo()){
            setPlanRegistrado(Boolean.TRUE);
        }else{
            setPlanRegistrado(Boolean.FALSE);
        }
    }

    public void setPlanRegistrado(Boolean planRegistrado) {
        this.planRegistrado = planRegistrado;
    }

    public void setFuncionesTutor(FuncionesTutor funcionesTutor) {
        this.funcionesTutor = funcionesTutor;
    }

    public void setListaFuncionesTutor(List<FuncionesTutor> listaFuncionesTutor) {
        this.listaFuncionesTutor = listaFuncionesTutor;
    }

    public void setSesionesGrupalesTutorias(SesionesGrupalesTutorias sesionesGrupalesTutorias) {
        this.sesionesGrupalesTutorias = sesionesGrupalesTutorias;
    }

    public void setListaSesionesGrupalesTutorias(List<SesionesGrupalesTutorias> listaSesionesGrupalesTutorias) {
        this.listaSesionesGrupalesTutorias = listaSesionesGrupalesTutorias;
    }
    
}
