/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoListadoTutores;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAsignacionTutores")
public class EjbAsignacionTutores {
    
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbAsignacionAcademica ejbAsignacionAcademica;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    
    /**
     * Permite validar si el usuario autenticado es director de área académica
     * @param clave Número de nomína del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDirector(Integer clave){
        try {
            return ejbAsignacionAcademica.validarDirector(clave);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El director no se pudo validar. (EjbAsignacionTutores.validarDirector)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de periodos escolares a elejir para realizar la asignación de tutores de grupo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosDesendentes(){
        try {
            return ejbAsignacionAcademica.getPeriodosDescendentes();
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares. (EjbAsignacionAcademica)", e, null);
        }
    }
    /**
     * Permite obtener un listado de programas educativos vigentes, los programas deben de ordenarse por 
     * área, nivel y nombre
     * @param director
     * @param periodo
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<AreasUniversidad,List<Grupo>>> getProgramasEducativos(PersonalActivo director, PeriodosEscolares periodo){
        try {
            return  ejbAsignacionAcademica.getProgramasActivos(director, periodo);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo mapear los programas y sus grupos. (EjbAsignacionTutores)", e, null);
        }
    }
    
    /**
     * Permite mapear los programas educativos y sincroniza a su vez los grupos que tiene asignados, ademas de asignar el tutor en caso de que ya haya sido asignado
     * @param director Número de nómina del director autenticado
     * @param periodo Periodo seleccionado por el usuario
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<AreasUniversidad,List<DtoListadoTutores>>> getProgramasEducativosGrupoTutor(PersonalActivo director, PeriodosEscolares periodo){
        try {
            Integer programaEducativoCategoria = ep.leerPropiedadEntera("programaEducativoCategoria").orElse(9);
            
            List<AreasUniversidad> programas = f.getEntityManager().createQuery("select a from AreasUniversidad  a where a.areaSuperior=:areaPoa and a.categoria.categoria=:categoria and a.vigente = '1' order by a.nombre", AreasUniversidad.class)
                    .setParameter("areaPoa", director.getAreaPOA().getArea())
                    .setParameter("categoria", programaEducativoCategoria)
                    .getResultList();

            Map<AreasUniversidad, List<DtoListadoTutores>> programasMap = programas.stream()
                    .collect(Collectors.toMap(programa -> programa, programa -> generarGruposTutor(programa, periodo)));
            return ResultadoEJB.crearCorrecto(programasMap, "Mapa de programas y grupos");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo mapear los programas y sus grupos. (EjbAsignacionTutores)", e, null);
        }
    }

    private List<DtoListadoTutores> generarGruposTutor(AreasUniversidad programa, PeriodosEscolares periodo){
        List<DtoListadoTutores> listadoTutorese = new ArrayList<>();
        f.getEntityManager().createQuery("select g from Grupo g where g.idPe=:programa and g.periodo=:periodo", Grupo.class)
                .setParameter("programa", programa.getArea())
                .setParameter("periodo", periodo.getPeriodo())
                .getResultStream().forEach(g ->{
                    DtoListadoTutores dlt = new DtoListadoTutores();
                    if(g.getTutor() != null){
                        PersonalActivo personalActivo = ejbPersonalBean.pack(g.getTutor());
                        dlt.setGrupo(g);
                        dlt.setTutor(personalActivo);
                    }else{
                        dlt.setGrupo(g);
                    }
                    listadoTutorese.add(dlt);
                        
                });
        return  listadoTutorese;
    }
    
    /**
     * Permite identificar a una lista de posibles docentes para asignar la materia
     * @param pista Contenido que la vista que puede incluir parte del nombre, nùmero de nómina o área operativa del docente que se busca
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<PersonalActivo>> buscarDocente(String pista){
        try {
            return ejbAsignacionAcademica.buscarDocente(pista);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de docentes activos. (EjbAsignacionTutores)", e, null);
        }
    }
    
    public ResultadoEJB<Grupo>  asignarTutorGrupo(DtoListadoTutores tutor,Operacion operacion){
        try {
            switch (operacion){
                case PERSISTIR:
                    Grupo grupo = tutor.getGrupo();
                    grupo.setTutor(tutor.getTutor().getPersonal().getClave());
                    f.edit(grupo);
                    return ResultadoEJB.crearCorrecto(grupo, "Asignación Tutor Correctamente");
                default:
                    return ResultadoEJB.crearErroneo(2, "Operación no autorizada.", Grupo.class);
            }
        } catch (Exception e) {
             return ResultadoEJB.crearErroneo(1, "No se pudo asignar tutor al grupo (EjbAsignacionTutores)", e, null);
        }
    }
    /**
     * Permite obtener la lista de grupos asignados por docente
     * @param docente Docente de quien se requiere obtener el listado de grupos
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Grupo>> getGruposPorTutor(PersonalActivo docente){
        try {
            List<Grupo> grupos = Collections.EMPTY_LIST;
            return ResultadoEJB.crearCorrecto(grupos, "Lista de grupos por docente.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista grupos por docente (EjbAsignacionTutores)", e, null);
        }
    }
}
