/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author Planeacion
 */
@Stateless
public class EjbAdministracionEncuestaTsu {


    @EJB
    private Facade f;
    @EJB private Facade2 f2;
    @EJB private EjbSatisfaccionEgresadosTsu ejbES;


    public Evaluaciones evaluacionEstadiaPeridoActual() {
        List<Evaluaciones> e = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones as e where e.periodo = :periodo and e.tipo = :tipo", Evaluaciones.class)
                .setParameter("periodo", 51)
                .setParameter("tipo", "Satisfacción de egresados de TSU")
                .getResultStream().collect(Collectors.toList());
        if (e.isEmpty()) {
            return new Evaluaciones();
        } else {
            return e.get(0);
        }
    }


    public ResultadosEncuestaSatisfaccionTsu getResultadoEncPorEvaluador(Integer matricula){
        Integer evaluacionActiva = evaluacionEstadiaPeridoActual().getEvaluacion();
        TypedQuery<ResultadosEncuestaSatisfaccionTsu> q = f.getEntityManager()
                .createQuery("SELECT r FROM ResultadosEncuestaSatisfaccionTsu r " +
                        "WHERE r.resultadosEncuestaSatisfaccionTsuPK.evaluador= :matricula and " +
                        "r.resultadosEncuestaSatisfaccionTsuPK.evaluacion = :evaluacion",ResultadosEncuestaSatisfaccionTsu.class);
        q.setParameter("evaluacion", evaluacionActiva);
        q.setParameter("matricula", matricula);
        List<ResultadosEncuestaSatisfaccionTsu> pr=q.getResultList();
        if(pr.isEmpty()){
            return new ResultadosEncuestaSatisfaccionTsu();
        }else{
            return pr.get(0);
        }
    }

}
