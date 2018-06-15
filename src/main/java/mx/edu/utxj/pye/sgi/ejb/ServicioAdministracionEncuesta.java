/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.DatosGraficaEncuestaEgresados;
import mx.edu.utxj.pye.sgi.entity.ch.DatosGraficaEncuestaServicio;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.ListaEncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.ListaEncuestaServicios;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.ViewAlumnos;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author Planeación
 */
@Stateful
public class ServicioAdministracionEncuesta implements EjbAdministracionEncuesta {

//    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
//    private EntityManager em;
    @EJB
    Facade f;
    @EJB Facade2 f2;



    @Override
    public List<Personal> esDirectorDeCarrera(Integer areaSup, Integer actividad, Integer catOp, Integer clave){
        TypedQuery<Personal> q = f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE  p.areaSuperior = :areaSuperior and p.actividad.actividad = :actividad and  p.categoriaOperativa.categoria= :categoria AND p.clave = :clave", Personal.class);
        q.setParameter("areaSuperior", areaSup);
        q.setParameter("actividad", actividad);
        q.setParameter("categoria", catOp);
        q.setParameter("clave", clave);
        return q.getResultList();
    }

   @Override
    public List<AlumnosEncuestas> obtenerListaAlumnosNoAccedieron(){
        TypedQuery<AlumnosEncuestas> q=f2.getEntityManager().createQuery("SELECT a FROM AlumnosEncuestas a",AlumnosEncuestas.class);
        List<AlumnosEncuestas> pr=q.getResultList();
        if(!pr.isEmpty()){
            return pr;
        }else{
            return null;
        }
    }

    @Override
    public PeriodosEscolares getPeriodo(Evaluaciones evaluacion){
        f.setEntityClass(PeriodosEscolares.class);
        return (PeriodosEscolares)f.find(evaluacion.getPeriodo());
    }

    @Override
    public List<AlumnosEncuestas> obtenerAlumno(String cveMaestro) {
//        TypedQuery<ViewAlumnos> q = f.getEntityManager().createQuery("SELECT a from Alumno a WHERE a.alumnoPK.periodo= 47 AND a.cuatrimestre=11 AND a.alumnoPK.matricula=:matricula", Alumno.class);
        TypedQuery<AlumnosEncuestas> q = f2.getEntityManager().createQuery("SELECT a from AlumnosEncuestas a WHERE a.cveDirector=:cveMaestro", AlumnosEncuestas.class);
        q.setParameter("cveMaestro", cveMaestro);
        List<AlumnosEncuestas> l = q.getResultList();
        if(!l.isEmpty()){
            return l;
        }else{
            return null;
        }
    }
    
    @Override
    public List<AlumnosEncuestas> obtenerTodoAlumno(Integer cveTutor){
        TypedQuery<AlumnosEncuestas> q=f2.getEntityManager().createQuery("SELECT a FROM AlumnosEncuestas a WHERE a.cveMaestro= :cveTutor",AlumnosEncuestas.class);
        q.setParameter("cveTutor", cveTutor);
        List<AlumnosEncuestas> l=q.getResultList();
        if(!l.isEmpty()){
            return l;
        }else{
            return null;
        }
    }
    
    @Override 
    public Alumnos getAlumnoEvaluador(String matricula){
        TypedQuery<Alumnos> q = f2.getEntityManager().createQuery("SELECT a FROM Alumnos a WHERE a.gradoActual=11 AND a.matricula= :matricula AND a.cveStatus= 1 AND a.grupos.gruposPK.cvePeriodo=47",Alumnos.class);
        q.setParameter("matricula", matricula);
        List<Alumnos> l=q.getResultList();
        if(!l.isEmpty()){
            return l.get(0);
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

    @Override
    public List<Alumnos> obtenerAlumnosPorTutor(Integer grupo){
        TypedQuery<Alumnos> q=f2.getEntityManager().createQuery("SELECT a FROM Alumnos a where a.grupos.gruposPK.cveGrupo=:grupo",Alumnos.class);
        q.setParameter("grupo", grupo);
        List<Alumnos> l=q.getResultList();
        return l;
    }
    @Override
    public List<DatosGraficaEncuestaEgresados> listaDatosAvanceEncEgresados(){
        TypedQuery<DatosGraficaEncuestaEgresados> q=f.getEntityManager().createQuery("SELECT d FROM DatosGraficaEncuestaEgresados d",DatosGraficaEncuestaEgresados.class);
        List<DatosGraficaEncuestaEgresados> l=q.getResultList();
        return l;
    }
    
    @Override
    public List<DatosGraficaEncuestaServicio> listaAvanceEncServ(){
        TypedQuery<DatosGraficaEncuestaServicio> q=f.getEntityManager().createQuery("SELECT d FROM DatosGraficaEncuestaServicio d ",DatosGraficaEncuestaServicio.class);
        List<DatosGraficaEncuestaServicio> l=q.getResultList();
        return l;
    }
    
    @Override
    public Grupos obtenerCuatriPorTutor(Integer cveMaestro){
        TypedQuery<Grupos> q=f2.getEntityManager().createQuery("SELECT g FROM Grupos g where g.gruposPK.cvePeriodo=47 AND g.cveMaestro=:cveMaestro",Grupos.class);
        q.setParameter("cveMaestro", cveMaestro);
        List<Grupos> l=q.getResultList();
        if(l.isEmpty()){
            return null;
        }else{
            return l.get(0);
        }
    }
    // ejb´s nuevos
    @Override
    public List<EncuestaServiciosResultados> getEncuestaServicios(){
        TypedQuery<EncuestaServiciosResultados> q = f.getEntityManager().createQuery("SELECT e FROM EncuestaServiciosResultados e ", EncuestaServiciosResultados.class);
        List<EncuestaServiciosResultados> l = q.getResultList();
        if (l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }
    
    @Override
    public List<EncuestaSatisfaccionEgresadosIng> getEncuestaSatisfEgre() {
        TypedQuery<EncuestaSatisfaccionEgresadosIng> q = f.getEntityManager().createQuery("SELECT e FROM EncuestaSatisfaccionEgresadosIng e ", EncuestaSatisfaccionEgresadosIng.class);
        List<EncuestaSatisfaccionEgresadosIng> l = q.getResultList();
        if (l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    @Override
    public EncuestaServiciosResultados getEncuestaporevaluador(Integer matricula) {
        TypedQuery<EncuestaServiciosResultados> q = f.getEntityManager().createQuery("SELECT e FROM EncuestaServiciosResultados e WHERE e.encuestaServiciosResultadosPK.evaluador = :matricula", EncuestaServiciosResultados.class);
        q.setParameter("matricula", matricula);
        List<EncuestaServiciosResultados> l = q.getResultList();
        if (l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }
    
    @Override
    public EncuestaSatisfaccionEgresadosIng getEncuestaEgreporEvaluador(Integer matricula){
        TypedQuery<EncuestaSatisfaccionEgresadosIng> q = f.getEntityManager().createQuery("SELECT e FROM EncuestaSatisfaccionEgresadosIng e WHERE e.encuestaSatisfaccionEgresadosIngPK.evaluador=:matricula ", EncuestaSatisfaccionEgresadosIng.class);
        q.setParameter("matricula", matricula);
        List<EncuestaSatisfaccionEgresadosIng> l = q.getResultList();
        if (l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }
    
    @Override
    public List<Personal> esSecretarioAcademico(Integer areaSup, Short actividad, Short catOp, Integer clave){
        TypedQuery<Personal> q = f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE  p.areaSuperior = :areaSuperior and p.actividad.actividad = :actividad and  p.categoriaOperativa.categoria= :categoria AND p.clave = :clave", Personal.class);
        q.setParameter("areaSuperior", areaSup);
        q.setParameter("actividad", actividad);
        q.setParameter("categoria", catOp);
        q.setParameter("clave", clave);
        return q.getResultList();
    }
}
