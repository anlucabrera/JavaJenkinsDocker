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
public class ValidadorPlaneacionCuatrimestralLAB 
        implements Validador<PlaneacionesCuatrimestrales>, Callable<Boolean>{
    @Getter private final short asignacionLABHorasClaseMaximo;
    @Getter private final short asignacionLABHorasClaseMinimo;
    @Getter private final short asignacionLABReunionAcademiaMaximo;
    @Getter private final PlaneacionesCuatrimestrales t;

    @Override
    public boolean esCorrecta(PlaneacionesCuatrimestrales t) {
        t.setTotal(Caster.planeacionToTotal(t));
        if(t.getActividadesVarias() > 0){
            t.setComentariosSistema("Se asignaron " + t.getActividadesVarias() + " horas en actividades varias, pero para Laboratorista no aplica éste criterio.");
            return false;
        }
        
        if(t.getAsesoriaClase() > 0){
            t.setComentariosSistema("Se asignaron " + t.getAsesoriaClase() + " horas en asesorias clase, pero para Laboratorista no aplica éste criterio.");
            return false;
        }
        
        if(this.asignacionLABHorasClaseMaximo < (t.getHorasClaseIng() + t.getHorasClaseTsu() + t.getEstadias())){
            t.setComentariosSistema("Se asignaron " + (t.getHorasClaseIng() + t.getHorasClaseTsu()) + " horas en clase, pero el máximo es: " + this.asignacionLABHorasClaseMaximo);
            return false;
        }
        
        if(this.asignacionLABHorasClaseMinimo > (t.getHorasClaseIng() + t.getHorasClaseTsu() + t.getEstadias())){
            t.setComentariosSistema("Se asignaron " + (t.getHorasClaseIng() + t.getHorasClaseTsu() + t.getEstadias()) + " horas en asesorias clase, pero el mínimo es: " + this.asignacionLABHorasClaseMinimo);
            return false;
        }
        
        if(t.getProyectoInvestigacion() > 0){
            t.setComentariosSistema("Se asignaron " + t.getProyectoInvestigacion() + " horas en proyecto de investigación, pero para Laboratorista no aplica éste criterio.");
            return false;
        }
        
        if(this.asignacionLABReunionAcademiaMaximo < t.getReunionAcademia()){
            t.setComentariosSistema("Se asignaron " + t.getReunionAcademia() + " horas en reunión de academia, pero el máximo es: " + this.asignacionLABReunionAcademiaMaximo);
            return false;
        }
        
        if(t.getTutoriaIndividual() > 0){
            t.setComentariosSistema("Se asignaron " + t.getTutoriaIndividual() + " horas en tutoría individual o grupal, pero para Laboratorista no aplica éste criterio.");
            return false;
        }
        
        if(t.getTotal() != 40){
            t.setComentariosSistema("Se asignaron " + t.getTotal() + " horas en total, pero para un laboratorista deben ser 40 horas en total.");
            return false;
        }
        
        t.setValidacionSistema(true);
        t.setComentariosSistema("Correcta.");
        return true;
    }

    public ValidadorPlaneacionCuatrimestralLAB(short asignacionLABHorasClaseMaximo, short asignacionLABHorasClaseMinimo, short asignacionLABReunionAcademiaMaximo) {
        this(null, asignacionLABHorasClaseMaximo, asignacionLABHorasClaseMinimo, asignacionLABReunionAcademiaMaximo);
    }

    public ValidadorPlaneacionCuatrimestralLAB(PlaneacionesCuatrimestrales t, short asignacionLABHorasClaseMaximo, short asignacionLABHorasClaseMinimo, short asignacionLABReunionAcademiaMaximo) {
        this.asignacionLABHorasClaseMaximo = asignacionLABHorasClaseMaximo;
        this.asignacionLABHorasClaseMinimo = asignacionLABHorasClaseMinimo;
        this.asignacionLABReunionAcademiaMaximo = asignacionLABReunionAcademiaMaximo;
        this.t = t;
    }

    @Override
    public Boolean call() throws Exception {
//        System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorPlaneacionCuatrimestralLAB.call()");
        this.t.setValidacionSistema(esCorrecta(t));
        return this.t.isValidacionSistema();
    }
    
    
}
