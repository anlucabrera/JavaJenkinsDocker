/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FuncionesTutorPlantilla;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanAccionTutorialPlantilla;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutoriasPlantilla;

/**
 *
 * @author UTXJ
 */
public class PlantillaPlanAccionTutorialRolPsicopedagogia extends AbstractRol{
    private static final long serialVersionUID = 4646547670938203382L;
    @Getter             private             PersonalActivo                                      personalPsicopedagogico;
    @Getter             private             String                                              gradoCuatrimestre;
    @Getter             private             List<String>                                        gradosCuatrimestres;
    
    @Getter             private             PlanAccionTutorialPlantilla                         planAccionTutorialPlantilla;
    @Getter             private             Boolean                                             planRegistrado;
    
    @Getter             private             FuncionesTutorPlantilla                             funcionTutorPlantilla;
    @Getter             private             SesionesGrupalesTutoriasPlantilla                   sesionGrupalTutoriaPlantilla;
    
    @Getter             private             List<FuncionesTutorPlantilla>                       listaFuncionesTutorPlantilla;
    @Getter             private             List<SesionesGrupalesTutoriasPlantilla>             listaSesionesGrupalesTutoriaPlantilla;
    
    @Getter             @Setter             private                                             Boolean                                          tab1,tab2,tab3;
    @Getter             @Setter             private                                             Integer                                          tabIndex;
    
    public PlantillaPlanAccionTutorialRolPsicopedagogia(@NonNull Filter<PersonalActivo> filtro){
        super(filtro);
        this.personalPsicopedagogico = filtro.getEntity();
        setGradosCuatrimestres(Arrays.asList("1", "2", "3", "4", "5", "7", "8", "9", "10"));
    }

    public void setPersonalPsicopedagogico(PersonalActivo personalPsicopedagogico) {
        this.personalPsicopedagogico = personalPsicopedagogico;
    }

    public void setGradoCuatrimestre(String gradoCuatrimestre) {
        this.gradoCuatrimestre = gradoCuatrimestre;
    }

    public void setGradosCuatrimestres(List<String> gradosCuatrimestres) {
        this.gradosCuatrimestres = gradosCuatrimestres;
        if(!gradosCuatrimestres.isEmpty()){
            this.gradoCuatrimestre = gradosCuatrimestres.get(0);
        }
    }

    public void setPlanAccionTutorialPlantilla(PlanAccionTutorialPlantilla planAccionTutorialPlantilla) {
        this.planAccionTutorialPlantilla = planAccionTutorialPlantilla;
        if (this.planAccionTutorialPlantilla.getObjetivo() == null || "".equals(this.planAccionTutorialPlantilla.getObjetivo())) {
            setPlanRegistrado(Boolean.TRUE);
        } else {
            setPlanRegistrado(Boolean.FALSE);
        }
    }

    public void setPlanRegistrado(Boolean planRegistrado) {
        this.planRegistrado = planRegistrado;
    }
    
    public void setFuncionTutorPlantilla(FuncionesTutorPlantilla funcionTutorPlantilla) {
        this.funcionTutorPlantilla = funcionTutorPlantilla;
    }

    public void setListaFuncionesTutorPlantilla(List<FuncionesTutorPlantilla> listaFuncionesTutorPlantilla) {
        this.listaFuncionesTutorPlantilla = listaFuncionesTutorPlantilla;
    }
    
    public void setSesionGrupalTutoriaPlantilla(SesionesGrupalesTutoriasPlantilla sesionGrupalTutoriaPlantilla) {
        this.sesionGrupalTutoriaPlantilla = sesionGrupalTutoriaPlantilla;
    }

    public void setListaSesionesGrupalesTutoriaPlantilla(List<SesionesGrupalesTutoriasPlantilla> listaSesionesGrupalesTutoriaPlantilla) {
        this.listaSesionesGrupalesTutoriaPlantilla = listaSesionesGrupalesTutoriaPlantilla;
    }
    
}
