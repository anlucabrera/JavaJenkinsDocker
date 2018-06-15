/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestasTsu;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author Planeacion
 */
@Stateful
public class ServicioAdministracionEncuestaTsu implements EjbAdministracionEncuestaTsu{
    
    @EJB private Facade f;
    @EJB private Facade2 f2;
    
    @Override
    public ResultadosEncuestaSatisfaccionTsu getResultadoEncPorEvaluador(Integer matricula){
        TypedQuery<ResultadosEncuestaSatisfaccionTsu> q = f.getEntityManager().createQuery("SELECT r FROM ResultadosEncuestaSatisfaccionTsu r WHERE r.resultadosEncuestaSatisfaccionTsuPK.evaluador= :matricula",ResultadosEncuestaSatisfaccionTsu.class);
        q.setParameter("matricula", matricula);
        List<ResultadosEncuestaSatisfaccionTsu> pr=q.getResultList();
        if(pr.isEmpty()){
            return null;
        }else{
            return pr.get(0);
        }
    }
    
    @Override
    public List<AlumnosEncuestasTsu> obtenerListaAlumnosNoAccedieron(){
        TypedQuery<AlumnosEncuestasTsu> q=f2.getEntityManager().createQuery("SELECT a FROM AlumnosEncuestasTsu a",AlumnosEncuestasTsu.class);
        List<AlumnosEncuestasTsu> pr=q.getResultList();
        return pr;
    }
    
    @Override
    public List<AlumnosEncuestasTsu> obtenerAlumnosPorDirector(String cveDirector){
        TypedQuery<AlumnosEncuestasTsu> q= f2.getEntityManager().createQuery("SELECT a FROM AlumnosEncuestasTsu a WHERE a.cveDirector= :cveDirector",AlumnosEncuestasTsu.class);
        q.setParameter("cveDirector", cveDirector);
        List<AlumnosEncuestasTsu> l = q.getResultList();
        if(!l.isEmpty()){
            return l;
        }else{
            return null;
        }
    }
    
        @Override 
    public Alumnos getAlumnoEvaluadorTsu(String matricula){
        TypedQuery<Alumnos> q = f2.getEntityManager().createQuery("SELECT a FROM Alumnos a WHERE a.gradoActual=6 AND a.matricula= :matricula AND a.cveStatus= 1 AND a.grupos.gruposPK.cvePeriodo=48",Alumnos.class);
        q.setParameter("matricula", matricula);
        List<Alumnos> l=q.getResultList();
        if(!l.isEmpty()){
            return l.get(0);
        }else{
            return null;
        }
    }

}
