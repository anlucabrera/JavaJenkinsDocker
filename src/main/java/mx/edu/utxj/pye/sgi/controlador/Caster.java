/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import java.text.SimpleDateFormat;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesCuatrimestrales;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@Named(value = "caster")
@ApplicationScoped
public class Caster {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    public Caster() {
    }
    
    public int toInt(long l) {
        return (int) l;
    }
    
    public float toFloat(long l) {
        return (float) l;
    }
    
    public int indexOf(List l, Object e){
        return l.indexOf(e);
    }
    
    public String periodoToString(PeriodosEscolares periodo){
        return (new StringBuilder())
                .append(periodo.getMesInicio().getMes())
                .append(" - ")
                .append(periodo.getMesFin().getMes())
                .append(" ")
                .append(periodo.getMesInicio().getMes().equals("Septiembre")?sdf.format(periodo.getCiclo().getInicio()):sdf.format(periodo.getCiclo().getFin()))
                .toString();
        
    }
    
    public static short planeacionToTotal(PlaneacionesCuatrimestrales pc){
        return (short)(pc.getHorasClaseTsu() + pc.getHorasClaseIng() + pc.getEstadias() + pc.getProyectoInvestigacion() + pc.getAsesoriaClase() + pc.getTutoriaIndividual() + pc.getTutoriaGrupal() + pc.getReunionAcademia() + pc.getActividadesVarias());
    }
}
