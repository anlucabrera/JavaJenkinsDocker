/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

import java.util.concurrent.Callable;
import lombok.Getter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesCuatrimestrales;

/**
 *
 * @author UTXJ
 */
@ToString
public class ValidadorPlaneacionCuatrimestralPTC implements Validador<PlaneacionesCuatrimestrales>, Callable<Boolean> {
    @Getter private final short asignacionPTCActividadesVariasMaximo;
    @Getter private final short asignacionPTCAsesoriaClaseMaximo;
    @Getter private final short asignacionPTCHorasClaseMaximo;
    @Getter private final short asignacionPTCHorasClaseMinimo;
    @Getter private final short asignacionPTCProyectoInvestigacionMaximo;
    @Getter private final short asignacionPTCProyectoInvestigacionMinimo;
    @Getter private final short asignacionPTCReunionAcademiaMaximo;
    @Getter private final short asignacionPTCTutoriaIndividualMaximo;
    @Getter private final short asignacionPTCTutoriaGrupalMaximo;
    @Getter private final short asignacionActividadesNoFrenteAGrupoMaximo;
    
    @Getter private final PlaneacionesCuatrimestrales planeacionesCuatrimestrales;

    /**
     * Consturctor para ejecución en interfaz funcional
     * @param asignacionPTCActividadesVariasMaximo
     * @param asignacionPTCAsesoriaClaseMaximo
     * @param asignacionPTCHorasClaseMaximo
     * @param asignacionPTCHorasClaseMinimo
     * @param asignacionPTCProyectoInvestigacionMaximo
     * @param asignacionPTCProyectoInvestigacionMinimo
     * @param asignacionPTCReunionAcademiaMaximo
     * @param asignacionPTCTutoriaIndividualMaximo
     * @param asignacionPTCTutoriaGrupalMaximo
     */
    public ValidadorPlaneacionCuatrimestralPTC(short asignacionPTCActividadesVariasMaximo, short asignacionPTCAsesoriaClaseMaximo, short asignacionPTCHorasClaseMaximo, short asignacionPTCHorasClaseMinimo, short asignacionPTCProyectoInvestigacionMaximo, short asignacionPTCProyectoInvestigacionMinimo, short asignacionPTCReunionAcademiaMaximo, short asignacionPTCTutoriaIndividualMaximo, short asignacionPTCTutoriaGrupalMaximo, short asignacionActividadesNoFrenteAGrupoMaximo) {
        this(null, asignacionPTCActividadesVariasMaximo, asignacionPTCAsesoriaClaseMaximo, asignacionPTCHorasClaseMaximo, asignacionPTCHorasClaseMinimo, asignacionPTCProyectoInvestigacionMaximo, asignacionPTCProyectoInvestigacionMinimo, asignacionPTCReunionAcademiaMaximo, asignacionPTCTutoriaIndividualMaximo, asignacionPTCTutoriaGrupalMaximo, asignacionActividadesNoFrenteAGrupoMaximo);
    } 

    @Override
    public boolean esCorrecta(PlaneacionesCuatrimestrales t) {        
        t.setTotal(Caster.planeacionToTotal(t));
        
        if(this.asignacionPTCActividadesVariasMaximo < t.getActividadesVarias()){
//            System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorPlaneacionCuatrimestralPTC.esCorrecta(" + this.asignacionPTCActividadesVariasMaximo + ") getActividadesVarias: " + t.getActividadesVarias());
            t.setComentariosSistema("Se asignaron " + t.getActividadesVarias() + " horas en actividades varias, pero el máximo es: " + this.asignacionPTCActividadesVariasMaximo);
            return false;
        }
        
        if(this.asignacionPTCAsesoriaClaseMaximo < t.getAsesoriaClase()){
//            System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorPlaneacionCuatrimestralPTC.esCorrecta(" + this.asignacionPTCAsesoriaClaseMaximo + ") getAsesoriaClase: " + t.getAsesoriaClase());
            t.setComentariosSistema("Se asignaron " + t.getAsesoriaClase() + " horas en asesorias clase, pero el máximo es: " + this.asignacionPTCAsesoriaClaseMaximo);
            return false;
        }
        
        if(this.asignacionPTCHorasClaseMaximo < (t.getHorasClaseIng() + t.getHorasClaseTsu() + t.getEstadias())){
//            System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorPlaneacionCuatrimestralPTC.esCorrecta(" + this.asignacionPTCHorasClaseMaximo + ") (t.getHorasClaseIng() + t.getHorasClaseTsu()): " + (t.getHorasClaseIng() + t.getHorasClaseTsu()));
            t.setComentariosSistema("Se asignaron " + (t.getHorasClaseIng() + t.getHorasClaseTsu()) + " horas en clase y estadías, pero el máximo es: " + this.asignacionPTCHorasClaseMaximo);
            return false;
        }
        
        if(this.asignacionPTCHorasClaseMinimo > (t.getHorasClaseIng() + t.getHorasClaseTsu() + t.getEstadias())){
//            System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorPlaneacionCuatrimestralPTC.esCorrecta(" + this.asignacionPTCHorasClaseMinimo + ") (t.getHorasClaseIng() + t.getHorasClaseTsu()): " + (t.getHorasClaseIng() + t.getHorasClaseTsu()));
            t.setComentariosSistema("Se asignaron " + (t.getHorasClaseIng() + t.getHorasClaseTsu() + t.getEstadias()) + " horas en clase y estadías, pero el mínimo es: " + this.asignacionPTCHorasClaseMinimo);
            return false;
        }
        
        if(this.asignacionPTCProyectoInvestigacionMaximo < t.getProyectoInvestigacion()){
//            System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorPlaneacionCuatrimestralPTC.esCorrecta(" + this.asignacionPTCProyectoInvestigacionMaximo + ") getProyectoInvestigacion: " + t.getProyectoInvestigacion());
            t.setComentariosSistema("Se asignaron " + t.getProyectoInvestigacion() + " horas en proyecto de investigación, pero el máximo es: " + this.asignacionPTCProyectoInvestigacionMaximo);
            return false;
        }
        
        if(this.asignacionPTCProyectoInvestigacionMinimo > t.getProyectoInvestigacion()){
//            System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorPlaneacionCuatrimestralPTC.esCorrecta(" + this.asignacionPTCProyectoInvestigacionMinimo + ") getProyectoInvestigacion: " + t.getProyectoInvestigacion());
            t.setComentariosSistema("Se asignaron " + t.getProyectoInvestigacion() + " horas en proyecto de investigación, pero el mínimo es: " + this.asignacionPTCProyectoInvestigacionMinimo);
            return false;
        }
        
        if(this.asignacionPTCReunionAcademiaMaximo < t.getReunionAcademia()){
//            System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorPlaneacionCuatrimestralPTC.esCorrecta(" + this.asignacionPTCReunionAcademiaMaximo + ") getReunionAcademia: " + t.getReunionAcademia());
            t.setComentariosSistema("Se asignaron " + t.getReunionAcademia() + " horas en reunión de academia, pero el máximo es: " + this.asignacionPTCReunionAcademiaMaximo);
            return false;
        }
        
        if(this.asignacionPTCTutoriaIndividualMaximo < t.getTutoriaIndividual()){
//            System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorPlaneacionCuatrimestralPTC.esCorrecta(" + this.asignacionPTCTutoriaIndividualMaximo + ") getTutoria: " + t.getTutoria());
            t.setComentariosSistema("Se asignaron " + t.getTutoriaIndividual() + " horas en tutoría individual, pero el máximo es: " + this.asignacionPTCTutoriaIndividualMaximo);
            return false;
        }
        
        if(this.asignacionPTCTutoriaGrupalMaximo < t.getTutoriaGrupal()){
            t.setComentariosSistema("Se asignaron " + t.getTutoriaGrupal() + " horas en tutoría grupal, pero el máximo es: " + this.asignacionPTCTutoriaGrupalMaximo);
            return false;
        }
        
        int actividades = t.getAsesoriaClase() + t.getTutoriaGrupal() + t.getTutoriaIndividual() + t.getReunionAcademia();
        if(actividades > asignacionActividadesNoFrenteAGrupoMaximo){
            t.setComentariosSistema("Entre asesorías, tutorías y reunión de academia, se asignaron " + actividades + " horas, pero el máximo es " + asignacionActividadesNoFrenteAGrupoMaximo + ".");
            return false;
        }
        
        if(t.getTotal() != 40){
            t.setComentariosSistema("Se asignaron " + t.getTotal() + " horas en total, pero para un P.T.C. deben ser 40 horas en total.");
            return false;
        }
        
        t.setValidacionSistema(true);
        t.setComentariosSistema("Correcta.");
        return true;
    }
    
    public ValidadorPlaneacionCuatrimestralPTC(PlaneacionesCuatrimestrales t, short asignacionPTCActividadesVariasMaximo, short asignacionPTCAsesoriaClaseMaximo, short asignacionPTCHorasClaseMaximo, short asignacionPTCHorasClaseMinimo, short asignacionPTCProyectoInvestigacionMaximo, short asignacionPTCProyectoInvestigacionMinimo, short asignacionPTCReunionAcademiaMaximo, short asignacionPTCTutoriaIndividualMaximo, short asignacionPTCTutoriaGrupalMaximo, short asignacionActividadesNoFrenteAGrupoMaximo) {
        this.asignacionPTCActividadesVariasMaximo = asignacionPTCActividadesVariasMaximo;
        this.asignacionPTCAsesoriaClaseMaximo = asignacionPTCAsesoriaClaseMaximo;
        this.asignacionPTCHorasClaseMaximo = asignacionPTCHorasClaseMaximo;
        this.asignacionPTCHorasClaseMinimo = asignacionPTCHorasClaseMinimo;
        this.asignacionPTCProyectoInvestigacionMaximo = asignacionPTCProyectoInvestigacionMaximo;
        this.asignacionPTCProyectoInvestigacionMinimo = asignacionPTCProyectoInvestigacionMinimo;
        this.asignacionPTCReunionAcademiaMaximo = asignacionPTCReunionAcademiaMaximo;
        this.asignacionPTCTutoriaIndividualMaximo = asignacionPTCTutoriaIndividualMaximo;
        this.asignacionPTCTutoriaGrupalMaximo = asignacionPTCTutoriaGrupalMaximo;
        this.planeacionesCuatrimestrales=t;
        this.asignacionActividadesNoFrenteAGrupoMaximo = asignacionActividadesNoFrenteAGrupoMaximo;
//        System.out.println("mx.edu.utxj.pye.sgi.funcional.ValidadorPlaneacionCuatrimestralPTC.<init>(): " + this.toString());
    } 

    @Override
    public Boolean call() throws Exception {
        this.planeacionesCuatrimestrales.setValidacionSistema(esCorrecta(planeacionesCuatrimestrales));
        return this.planeacionesCuatrimestrales.isValidacionSistema();
    }

}
