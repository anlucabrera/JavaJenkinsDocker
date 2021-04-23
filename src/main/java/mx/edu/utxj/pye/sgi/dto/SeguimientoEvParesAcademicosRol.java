package mx.edu.utxj.pye.sgi.dto;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;

import java.util.List;

public class SeguimientoEvParesAcademicosRol {

    @Getter private PersonalActivo personal;
    @Getter private Evaluaciones evaluacion, evaluacionActiva;
    @Getter private AperturaVisualizacionEncuestas aperturaActiva;
    @Getter private List<Evaluaciones> evaluaciones;
    @Getter private List<DtoEvaluacionParesAcademicos> combinaciones;
    @Getter private  List<AreasUniversidad> areas ;
    @Getter private AreasUniversidad areaSeleccionada,areaSelect;
    @Getter private List<PersonalActivo> totoalDocentes, docentesCompletos, docentesIncompletos, filter;

    @Getter @Setter List<dtoAvanceEvaluaciones> listAvance;

    public void setPersonal(PersonalActivo personal) { this.personal = personal; }

    public void setEvaluacion(Evaluaciones evaluacion) { this.evaluacion = evaluacion; }

    public void setCombinaciones(List<DtoEvaluacionParesAcademicos> combinaciones) { this.combinaciones = combinaciones; }

    public void setEvaluaciones(List<Evaluaciones> evaluaciones) { this.evaluaciones = evaluaciones; }

    public void setAreas(List<AreasUniversidad> areas) { this.areas = areas; }

    public void setAreaSelect(AreasUniversidad areaSelect) { this.areaSelect = areaSelect; }

    public void setDocentesCompletos(List<PersonalActivo> docentesCompletos) { this.docentesCompletos = docentesCompletos; }

    public void setDocentesIncompletos(List<PersonalActivo> docentesIncompletos) { this.docentesIncompletos = docentesIncompletos; }

    public void setEvaluacionActiva(Evaluaciones evaluacionActiva) { this.evaluacionActiva = evaluacionActiva; }

    public void setAperturaActiva(AperturaVisualizacionEncuestas aperturaActiva) { this.aperturaActiva = aperturaActiva; }

    public void setFilter(List<PersonalActivo> filter) { this.filter = filter; }

    public void setTotoalDocentes(List<PersonalActivo> totoalDocentes) { this.totoalDocentes = totoalDocentes; }

    public Boolean tieneAcceso(PersonalActivo personalActivo, UsuarioTipo usuarioTipo){
        if(personalActivo == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.TRABAJADOR)) return false;
        return true;
    }



}
