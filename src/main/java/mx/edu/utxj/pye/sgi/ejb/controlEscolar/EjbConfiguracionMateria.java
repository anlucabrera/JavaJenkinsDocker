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
import java.util.SortedMap;

@Stateless(name = "EjbConfiguracionMateria")
public class EjbConfiguracionMateria {
    /**
     * Permite obtener el periodo escolar activo del cual se van a configurar las materias y que el docente logueado tenga carga académica
     * @param docente Referencia al docente logueado
     * @return Resultado del proceso
     */
    public ResultadoEJB<PeriodosEscolares> getPeriodoActivo(PersonalActivo docente){
        return null;
    }

    /**
     * Permite obtener la lista de materias asignadas del docente logueado
     * @param docente Referencia al docente logueado
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Materia>> getMateriasAsignadas(PersonalActivo docente){
        return null;
    }

    /**
     * Permite obtener la lista de grupos a quienes el docente imparte una determinada materia
     * @param docente Docente loqueado
     * @param materia Materia que el docente imparte
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Grupo>> getGruposAsignados(PersonalActivo docente, Materia materia){
        return null;
    }

    /**
     * Permite obtener un mapa ordenado de unidades y sconfiguración sugerida
     * @param materia Materia de la que se desea generar la configuración sugerida
     * @return
     */
    public ResultadoEJB<SortedMap<UnidadMateria, ConfiguracionMateria>> getConfiguracionesSugeridas(Materia materia){
        return null;
    }

    public ResultadoEJB<SortedMap<UnidadMateria, ConfiguracionMateria>> getConfiguraciones(PeriodosEscolares periodo, PersonalActivo docente, Materia materia){
        return null;
    }
}
