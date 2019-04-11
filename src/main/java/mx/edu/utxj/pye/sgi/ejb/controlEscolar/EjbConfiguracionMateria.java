package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import javax.ejb.Stateless;

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
}
