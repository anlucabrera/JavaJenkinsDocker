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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionConocimientoCodigoEticaResultados2;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author Planeacion
 */
@Stateless
public class EjbSeguimientoEvaluacionEticaDirectivos {

    @EJB Facade f;
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;

    private EntityManager em;

    @PostConstruct
    public void init(){em = f.getEntityManager();}
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public ResultadoEJB<Filter<PersonalActivo>> validarDirectivo(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.ACTIIVIDAD.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalActividad").orElse(2)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido directivo.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbSeguimientoEvaluacionEticaDirectivos.validarDirectivo)", e, null);
        }
    
    }
    
    public ResultadoEJB<List<EvaluacionConocimientoCodigoEticaResultados2>> obtenerCompletos(Short area){
        List<EvaluacionConocimientoCodigoEticaResultados2> listCompletos = em.createQuery("select e from EvaluacionConocimientoCodigoEticaResultados2 as e where e.personal.areaOperativa = :area and e.completo = :completo", EvaluacionConocimientoCodigoEticaResultados2.class)
                .setParameter("area", area)
                .setParameter("completo", Boolean.TRUE)
                .getResultStream().collect(Collectors.toList());
        if(listCompletos.isEmpty()){
            return ResultadoEJB.crearCorrecto(new ArrayList<>(), "Lista vacia");
        }else{
            return ResultadoEJB.crearCorrecto(listCompletos, "Lista completa");
        }
    }
    
    public ResultadoEJB<Boolean> verificarEvCompleto(Integer clave){
        EvaluacionConocimientoCodigoEticaResultados2 e = em.createQuery("select e from EvaluacionConocimientoCodigoEticaResultados2 as e where e.evaluacionConocimientoCodigoEticaResultados2PK.evaluador = :evaluador", EvaluacionConocimientoCodigoEticaResultados2.class)
                .setParameter("evaluador", clave)
                .getResultStream().findFirst().orElse(new EvaluacionConocimientoCodigoEticaResultados2());
        if(e == new EvaluacionConocimientoCodigoEticaResultados2()){
            return ResultadoEJB.crearCorrecto(Boolean.FALSE, "Evaluacion incompleta");
        }
        if(e.getCompleto() == Boolean.FALSE){
            return ResultadoEJB.crearCorrecto(Boolean.FALSE, "Evaluacion incompleta");
        }else{
            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Evaluacion completa");
        }
        
    }
    
    public ResultadoEJB<List<Personal>> obtenerIncompletos(Short area){
        List<Personal> listaPersonal = em.createQuery("select p from Personal as p where p.areaOperativa = :area and (p.status = :estatus1 or p.status = :estatus2)", Personal.class)
                .setParameter("area", area)
                .setParameter("estatus1", "A".charAt(0))
                .setParameter("estatus2", "R".charAt(0))
                .getResultStream()
                .map(persona -> pack(persona))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(listaPersonal.isEmpty()){
            return ResultadoEJB.crearCorrecto(new ArrayList<>(), "Lista vacia");
        }else{
            return ResultadoEJB.crearCorrecto(listaPersonal, "Lista completa");
        }
    }
    
    public ResultadoEJB<Personal> pack(Personal clave){
        Boolean completo = verificarEvCompleto(clave.getClave()).getValor();
        if(completo.equals(Boolean.TRUE)) return ResultadoEJB.crearErroneo(1, "Tiene evaluacion completa", Personal.class);
        Personal p = em.find(Personal.class, clave.getClave());
        return ResultadoEJB.crearCorrecto(p, "Personal");
    }
    
    public ResultadoEJB<AreasUniversidad> obtenerAreaOp(Short area){
        AreasUniversidad au = em.createQuery("SELECT a from AreasUniversidad as a where a.area = :area", AreasUniversidad.class)
                .setParameter("area", area)
                .getResultStream().findFirst().orElse(new AreasUniversidad());
        return ResultadoEJB.crearCorrecto(au, "Area");
    }
    
    public ResultadoEJB<List<EvaluacionConocimientoCodigoEticaResultados2>> obtenerResultados(){
        List<EvaluacionConocimientoCodigoEticaResultados2> lista = em.createQuery("select e from EvaluacionConocimientoCodigoEticaResultados2 as e where e.completo = :completo", EvaluacionConocimientoCodigoEticaResultados2.class)
                .setParameter("completo", Boolean.TRUE)
                .getResultStream()
                .collect(Collectors.toList());
        if(lista.isEmpty()) return ResultadoEJB.crearCorrecto(new ArrayList<>(), "Lista vacia");
        return ResultadoEJB.crearCorrecto(lista, "Lista completa");
    }
}
