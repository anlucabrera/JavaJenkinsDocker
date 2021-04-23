package mx.edu.utxj.pye.sgi.dto;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;

import java.util.List;

public class ResultadosEvParesAcademicosRol {

    @Getter private PersonalActivo personal;
    @Getter private Evaluaciones evaluacion, evaluacionActiva;
    @Getter private List<Evaluaciones> evaluaciones;
    @Getter private List<DtoEvaluacionParesAcademicos> combinaciones, filter;

    @Getter @Setter List<dtoAvanceEvaluaciones> listAvance;

    public void setPersonal(PersonalActivo personal) { this.personal = personal; }

    public void setEvaluacion(Evaluaciones evaluacion) { this.evaluacion = evaluacion; }

    public void setCombinaciones(List<DtoEvaluacionParesAcademicos> combinaciones) { this.combinaciones = combinaciones; }

    public void setEvaluaciones(List<Evaluaciones> evaluaciones) { this.evaluaciones = evaluaciones; }

    public void setFilter(List<DtoEvaluacionParesAcademicos> filter) { this.filter = filter; }

    public void setEvaluacionActiva(Evaluaciones evaluacionActiva) { this.evaluacionActiva = evaluacionActiva; }


    public Boolean tieneAcceso(PersonalActivo personalActivo, UsuarioTipo usuarioTipo){
        if(personalActivo == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.TRABAJADOR)) return false;
        return true;
    }



}
