/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacion360Promedios;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacionDesempenioPromedios;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author Planeacion
 */
@Stateless
public class EjbAdministracionDirectivos {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB Facade f;
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbAdministracionEncuestas ejbAdmin;
    EntityManager em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    public ResultadoEJB<Filter<PersonalActivo>> validarDirectivo(Integer clave){
        try{
            Personal persona = em.createQuery("select p from Personal as p where p.clave = :clave and (p.actividad.actividad = :actividad1 or p.actividad.actividad = :actividad2)", Personal.class)
                    .setParameter("clave", clave)
                    .setParameter("actividad1", 2)
                    .setParameter("actividad2", 4)
                    .getResultStream().findFirst().orElse(new Personal());
            if(persona.equals(new Personal())) return ResultadoEJB.crearErroneo(1, new Filter<>(), "No se encontro la persona con esas especificaciones");
            PersonalActivo p = ejbPersonalBean.pack(persona.getClave());
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido directivo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbSeguimientoEvaluacionEticaDirectivos.validarDirectivo)", e, null);
        }
    }
    
    public ResultadoEJB<List<ListaEvaluacion360Promedios>> obtenerResultadosEv360(Evaluaciones360 evaluacion, Short areaOp) {
        List<ListaEvaluacion360Promedios> lista = em.createQuery("select e from Evaluaciones360Resultados as e where e.evaluaciones360ResultadosPK.evaluacion = :evaluacion and e.personal.areaOperativa = :areaOp GROUP BY e.evaluaciones360ResultadosPK.evaluado", Evaluaciones360Resultados.class)
                .setParameter("evaluacion", evaluacion.getEvaluacion())
                .setParameter("areaOp", areaOp)
                .getResultStream()
                .map(e -> ejbAdmin.packEv360(e))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacia");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<List<ListaEvaluacionDesempenioPromedios>> obtenerResultadosEvDesempenio(DesempenioEvaluaciones evaluacion, Integer clave) {
        List<ListaEvaluacionDesempenioPromedios> lista = em.createQuery("select d from DesempenioEvaluacionResultados as d where d.desempenioEvaluacionResultadosPK.evaluacion = :evaluacion and d.desempenioEvaluacionResultadosPK.evaluador = :evaluador", DesempenioEvaluacionResultados.class)
                .setParameter("evaluacion", evaluacion.getEvaluacion())
                .setParameter("evaluador", clave)
                .getResultStream()
                .map(e -> ejbAdmin.packEvDesempenio(e))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacia");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
    public ResultadoEJB<List<ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio>> obtenerReultadosCedulas(Integer evaluacion, Integer clave){
        List<ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio> lista = em.createQuery("select d from DesempenioEvaluacionResultados as d where d.desempenioEvaluacionResultadosPK.evaluacion = :evaluacion and d.desempenioEvaluacionResultadosPK.evaluador = :clave", DesempenioEvaluacionResultados.class)
                .setParameter("evaluacion", evaluacion)
                .setParameter("clave", clave)
                .getResultStream()
                .map(resultado -> ejbAdmin.packResultado(resultado))
                .distinct()
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(lista.isEmpty()) return ResultadoEJB.crearErroneo(1, Collections.EMPTY_LIST, "Lista vacia");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
    
}
