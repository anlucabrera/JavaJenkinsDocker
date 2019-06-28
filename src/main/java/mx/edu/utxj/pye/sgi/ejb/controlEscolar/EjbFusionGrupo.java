/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Inscripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbFusionGrupo")
public class EjbFusionGrupo {
    
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbAsignacionAcademica ejbAsignacionAcademica;
    
    /**
     * Permite validar el usuario autenticado si es como director de área académica 
     * @param clave Número de nomina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDirector(Integer clave){
        try {
            return ejbAsignacionAcademica.validarDirector(clave);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El director no se pudo validar. (EjbRegistroPlanEstudio.validarDirector)", e, null);
        }
    }
    
    /**
     * Permite obtener el listado de periodos ordenados de forma decententes
     * @return Resultdo del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosDesendentes(){
        try {
            return ejbAsignacionAcademica.getPeriodosDescendentes();
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares. (EjbFusionGrupo)", e, null);
        }
    }
    
    /**
     * Mapea el listado de programas educativos vigentes y los grupos deben de ordenarse por grado y literal
     * @param director 
     * @param periodo
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<AreasUniversidad, List<Grupo>>> getProgramaEducativo(PersonalActivo director, PeriodosEscolares periodo){
        try {
            return ejbAsignacionAcademica.getProgramasActivos(director, periodo);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo mapear los programas y sus grupos. (EjbFusionGrupo)", e, null);
        }
    }

    /**
     * Permite realizar el cambio de grupo de los estudiantes seleccionados
     * @param estudiantesCambio Lista de los estudiantes a cambiar
     * @param grupoDestino Grupo al cual se asignaran los estudiantes
     * @return Resultado del proceso
     */
    public ResultadoEJB< List<Inscripcion> > reasignacionGrupo(List<Inscripcion> estudiantesCambio, Grupo grupoDestino){
        try {
            List<Inscripcion> grupoEstudiantes = Collections.EMPTY_LIST;
            //TODO: Actualización del grupo de los estudiantes seleccionados
            return ResultadoEJB.crearCorrecto(grupoEstudiantes, "Los estudiantes fueron cambiados satisfactoriamente de grupo");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar el cambio de grupo de los estudiantes", e, null);
        }
    }
}
