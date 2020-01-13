/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author UTXJ
 */
@Stateless
public class EjbConsultaCalificacionesCoordinadorAD {
    
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;

    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public void init() {
        em = f.getEntityManager();
    }
    
    /**
     * Permite validar el usuario autenticado si es como director de área académica 
     * @param clave Número de nomina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarCoordinador(Integer clave){
        PersonalActivo p = ejbPersonalBean.pack(clave);
        Filter<PersonalActivo> filtro = new Filter<>();
        filtro.setEntity(p);
        filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("coordinadorAreaOperativa").orElse(22)));
        filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("coordinadorAreaSuperior").orElse(14)));
        return ResultadoEJB.crearCorrecto(filtro, "El filtro del usuario ha sido preparado como Coordinador.");
    }

    public ResultadoEJB<Estudiante> obtenerEstudiante(Integer matricula){
        try{
            Estudiante estudiante = em.createQuery("select e from Estudiante as e where e.matricula = :matricula", Estudiante.class)
                    .setParameter("matricula", matricula)
                    .getResultStream()
                    .findFirst().orElse(null);
            return ResultadoEJB.crearCorrecto(estudiante, "Los estudiantes fueron obtenidos correctamente");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo cargar la lista de estudiantes", e, null);
        }
    }

    public ResultadoEJB<List<Estudiante>> obtenerEstudiantesActivos(Integer periodo){
        try{
            List<Estudiante> estudiantes = em.createQuery("select e from Estudiante as e where e.periodo = :periodo and e.tipoEstudiante.idTipoEstudiante = :tipo", Estudiante.class)
                    .setParameter("tipo", Short.parseShort("1"))
                    .setParameter("periodo", periodo)
                    .getResultStream()
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(estudiantes, "Los estudiantes fueron obtenidos correctamente");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo cargar la lista de estudiantes", e, null);
        }
    }

    public ResultadoEJB<List<PeriodosEscolares>> obtenerListaPeriodosEscolares(){
        try {
            List<PeriodosEscolares> pe = em.createQuery("select p from PeriodosEscolares as p where p.periodo >= :periodo", PeriodosEscolares.class)
                    .setParameter("periodo", 52).getResultStream().collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(pe, "Lista completa de periodos");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el periodo. (EjbConsultaCalificacion.obtenerListaPeriodosEscolares)", e, null);
        }
    }



}
