/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

import java.util.concurrent.Callable;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesCuatrimestrales;

/**
 *
 * @author UTXJ
 */
public class ValidadorPlaneacionCuatrimestralPA implements Validador<PlaneacionesCuatrimestrales>, Callable<Boolean>{
    @Getter private final short asignacionPAActividadesVariasMaximo;
    @Getter private final short asignacionPAActividadesVariasMaximoIdiomas=8;
    @Getter private final short asignacionPAAsesoriaClaseMaximo;
    @Getter private final short asignacionPAHorasClaseMaximo;
    @Getter private final short asignacionPAHorasClaseMinimo;
    @Getter private final short asignacionPAReunionAcademiaMaximo;
    @Getter private final short asignacionPATutoriaIndividualMaximo;
    @Getter private final short asignacionPATutoriaGrupalMaximo;
    @Getter private final short asignacionActividadesNoFrenteAGrupoMaximo;
    @Getter private final PlaneacionesCuatrimestrales t;

    @Override
    public boolean esCorrecta(PlaneacionesCuatrimestrales t) {
        t.setTotal(Caster.planeacionToTotal(t));
        
        if(t.getDirector().getAreaOperativa()==47){
            if (this.asignacionPAActividadesVariasMaximoIdiomas < t.getActividadesVarias()) {
                t.setComentariosSistema("Se asignaron " + t.getActividadesVarias() + " horas en actividades varias, pero el máximo es: " + this.asignacionPAActividadesVariasMaximo);
                return false;
            }
        } else {
            if (this.asignacionPAActividadesVariasMaximo < t.getActividadesVarias()) {
                t.setComentariosSistema("Se asignaron " + t.getActividadesVarias() + " horas en actividades varias, pero el máximo es: " + this.asignacionPAActividadesVariasMaximo);
                return false;
            }
        }
        if(this.asignacionPAAsesoriaClaseMaximo < t.getAsesoriaClase()){
            t.setComentariosSistema("Se asignaron " + t.getAsesoriaClase() + " horas en asesorias clase, pero el máximo es: " + this.asignacionPAAsesoriaClaseMaximo);
            return false;
        }
        
        if(this.asignacionPAHorasClaseMaximo < (t.getHorasClaseIng() + t.getHorasClaseTsu() + t.getEstadias())){
            t.setComentariosSistema("Se asignaron " + (t.getHorasClaseIng() + t.getHorasClaseTsu() + t.getEstadias()) + " horas en clase y estadías, pero el máximo es: " + this.asignacionPAHorasClaseMaximo);
            return false;
        }
        
        if(this.asignacionPAHorasClaseMinimo > (t.getHorasClaseIng() + t.getHorasClaseTsu() + t.getEstadias())){
            t.setComentariosSistema("Se asignaron " + (t.getHorasClaseIng() + t.getHorasClaseTsu() + t.getEstadias()) + " horas en clase y estadías, pero el mínimo es: " + this.asignacionPAHorasClaseMinimo);
            return false;
        }
        
        if(t.getProyectoInvestigacion() > 0){
            t.setComentariosSistema("Se asignaron " + t.getProyectoInvestigacion() + " horas en proyecto de investigación, pero para Profesor de Asignatura no aplica éste criterio.");
            return false;
        }
        
        if(this.asignacionPAReunionAcademiaMaximo < t.getReunionAcademia()){
            t.setComentariosSistema("Se asignaron " + t.getReunionAcademia() + " horas en reunión de academia, pero el máximo es: " + this.asignacionPAReunionAcademiaMaximo);
            return false;
        }
        
        if(this.asignacionPATutoriaIndividualMaximo < t.getTutoriaIndividual()){
            t.setComentariosSistema("Se asignaron " + t.getTutoriaIndividual() + " horas en tutoría individual, pero el máximo es: " + this.asignacionPATutoriaIndividualMaximo);
            return false;
        }
        
        if(this.asignacionPATutoriaGrupalMaximo < t.getTutoriaGrupal()){
            t.setComentariosSistema("Se asignaron " + t.getTutoriaGrupal() + " horas en tutoría grupal, pero el máximo es: " + this.asignacionPATutoriaGrupalMaximo);
            return false;
        }
        
        int actividades = t.getAsesoriaClase() + t.getTutoriaGrupal() + t.getTutoriaIndividual() + t.getReunionAcademia();
        if(actividades > asignacionActividadesNoFrenteAGrupoMaximo){
            t.setComentariosSistema("Entre asesorías, tutorías y reunión de academia, se asignaron " + actividades + ", pero el máximo es " + asignacionActividadesNoFrenteAGrupoMaximo + ".");
            return false;
        }
        
        if(t.getTotal() < asignacionPAHorasClaseMinimo || t.getTotal() > asignacionPAHorasClaseMaximo){
            t.setComentariosSistema("Se asignaron " + t.getTotal() + " horas en total, pero para un P.A. deben ser entre " + asignacionPAHorasClaseMinimo + " y " + asignacionPAHorasClaseMaximo + " horas en total.");
            return false;
        }
        
        t.setValidacionSistema(true);
        t.setComentariosSistema("Correcta.");
        
        return true;
    }

    public ValidadorPlaneacionCuatrimestralPA(short asignacionPAActividadesVariasMaximo, short asignacionPAAsesoriaClaseMaximo, short asignacionPAHorasClaseMaximo, short asignacionPAHorasClaseMinimo, short asignacionPAReunionAcademiaMaximo, short asignacionPATutoriaIndividualMaximo, short asignacionPATutoriaGrupalMaximo, short asignacionActividadesNoFrenteAGrupoMaximo) {
        this(null, asignacionPAActividadesVariasMaximo, asignacionPAAsesoriaClaseMaximo, asignacionPAHorasClaseMaximo, asignacionPAHorasClaseMinimo, asignacionPAReunionAcademiaMaximo, asignacionPATutoriaIndividualMaximo, asignacionPATutoriaGrupalMaximo, asignacionActividadesNoFrenteAGrupoMaximo);
    }
    
    public ValidadorPlaneacionCuatrimestralPA(PlaneacionesCuatrimestrales t, short asignacionPAActividadesVariasMaximo, short asignacionPAAsesoriaClaseMaximo, short asignacionPAHorasClaseMaximo, short asignacionPAHorasClaseMinimo, short asignacionPAReunionAcademiaMaximo, short asignacionPATutoriaIndividualMaximo, short asignacionPATutoriaGrupalMaximo, short asignacionActividadesNoFrenteAGrupoMaximo) {
        this.asignacionPAActividadesVariasMaximo = asignacionPAActividadesVariasMaximo;
        this.asignacionPAAsesoriaClaseMaximo = asignacionPAAsesoriaClaseMaximo;
        this.asignacionPAHorasClaseMaximo = asignacionPAHorasClaseMaximo;
        this.asignacionPAHorasClaseMinimo = asignacionPAHorasClaseMinimo;
        this.asignacionPAReunionAcademiaMaximo = asignacionPAReunionAcademiaMaximo;
        this.asignacionPATutoriaIndividualMaximo = asignacionPATutoriaIndividualMaximo;
        this.asignacionPATutoriaGrupalMaximo = asignacionPATutoriaGrupalMaximo;
        this.asignacionActividadesNoFrenteAGrupoMaximo = asignacionActividadesNoFrenteAGrupoMaximo;
        this.t = t;
    }

    @Override
    public Boolean call() throws Exception {
        this.t.setValidacionSistema(esCorrecta(t));
        return this.t.isValidacionSistema();
    }
    
}
