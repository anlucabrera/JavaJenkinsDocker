package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ConfiguracionMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

@Stateless(name = "EjbConfiguracionMateria")
public class EjbConfiguracionMateria {
    /**
     * Permite obtener el periodo escolar
     * @param docente
     * @return
     */
    public ResultadoEJB<PeriodosEscolares> getPeriodoActivo(PersonalActivo docente){
        return null;
    }

    public ResultadoEJB<List<Materia>> getMateriasAsignadas(PersonalActivo docente){
        return null;
    }

    public ResultadoEJB<List<Grupo>> getGruposAsignados(PersonalActivo docente, Materia materia){
        return null;
    }

    public ResultadoEJB<SortedMap<UnidadMateria, List<ConfiguracionMateria>>> getConfiguracionesSugeridas(Materia materia){
        return null;
    }

    public ResultadoEJB<SortedMap<UnidadMateria, List<ConfiguracionMateria>>> getConfiguraciones(PersonalActivo docente, Materia materia){
        return null;
    }
}
