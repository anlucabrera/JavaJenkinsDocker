/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import lombok.Getter;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEvaluacionTutor;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Periodos;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Planeación
 */
@Stateful
public class EjbAdministracionEncuesta {

    @EJB
    Facade f;
    @EJB
    Facade2 f2;
    @Getter
    private Integer periodo;
    private EntityManager em;
    private EntityManager em2;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager(); em2 = f2.getEntityManager();
    }

    public List<Personal> esDirectorDeCarrera(Integer areaSup, Integer actividad, Integer catOp, Integer catOp1, Integer clave){
        TypedQuery<Personal> q = f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE  p.areaSuperior = :areaSuperior and p.actividad.actividad = :actividad and " +
                " (p.categoriaOperativa.categoria= :categoria or p.categoriaOperativa.categoria = :categoria1) AND p.clave = :clave", Personal.class);
        q.setParameter("areaSuperior", areaSup);
        q.setParameter("actividad", actividad);
        q.setParameter("categoria", catOp);
        q.setParameter("categoria1", catOp1);
        q.setParameter("clave", clave);
        return q.getResultList();
    }

    public List<Personal> esSecretarioAcademico(Integer areaSup, Short actividad, Short catOp, Integer clave){
        TypedQuery<Personal> q = f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE  p.areaSuperior = :areaSuperior and p.actividad.actividad = :actividad and  p.categoriaOperativa.categoria= :categoria AND p.clave = :clave", Personal.class);
        q.setParameter("areaSuperior", areaSup);
        q.setParameter("actividad", actividad);
        q.setParameter("categoria", catOp);
        q.setParameter("clave", clave);
        return q.getResultList();
    }

    public List<Personal> esPlaneacion(Integer areaSup, Short actividad, Short catOp, Short catOp1, Integer clave){
        TypedQuery<Personal> q = f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE  p.areaSuperior = :areaSuperior and p.actividad.actividad = :actividad and  (p.categoriaOperativa.categoria= :categoria or p.categoriaOperativa.categoria= :categoria1) AND p.clave = :clave", Personal.class);
        q.setParameter("areaSuperior", areaSup);
        q.setParameter("actividad", actividad);
        q.setParameter("categoria", catOp);
        q.setParameter("categoria1", catOp1);
        q.setParameter("clave", clave);
        return q.getResultList();
    }

    public List<Personal> esPsicopedagogia(Short areaOp, Integer clave){
        TypedQuery<Personal> q = f.getEntityManager().createQuery("SELECT p FROM Personal p WHERE  p.areaOperativa= :areaOp AND p.clave = :clave", Personal.class);
        q.setParameter("areaOp", areaOp);
        q.setParameter("clave", clave);
        return q.getResultList();
    }

    public List<Grupos> estTutordeGrupo(Integer cvePersona){
        String periodoEncuesta = Objects.requireNonNull(em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "periodoEncuestaServicios")
                .getResultStream().findFirst().orElse(null)).getValor();
        return f2.getEntityManager().createQuery("SELECT g FROM Grupos as g WHERE (g.gruposPK.cvePeriodo = :periodo) "
                + "AND g.cveMaestro = :cvePersona", Grupos.class)
                .setParameter("periodo",Integer.parseInt(periodoEncuesta))
                .setParameter("cvePersona", cvePersona)
                .getResultStream().collect(Collectors.toList());
    }
    public List<AlumnosEvaluacionTutor> estTutor(Integer cvePersona){

        return f2.getEntityManager().createQuery("SELECT a FROM AlumnosEvaluacionTutor as a WHERE a.cveMaestro = :clave")
                .setParameter("clave", cvePersona)
                .getResultList();
    }

    public Grupo esTutorCE (Integer clave){
        List<Periodos> periodos = f2.getEntityManager().createQuery("select p from Periodos as p", Periodos.class).getResultStream().collect(Collectors.toList());
        periodos.forEach(x -> {
            Boolean activo = true;
            Boolean acv = x.getActivo().equals(true);
            if (activo.equals(acv)) {
                periodo = x.getPeriodosPK().getCvePeriodo();
            }
        });
        Grupo tutorCE = f.getEntityManager().createQuery("select g from Grupo g where g.periodo=:periodo and g.tutor=:tutor", Grupo.class)
                .setParameter("periodo",periodo)
                .setParameter("tutor",clave)
                .getResultStream()
                .findFirst()
                .orElse(null)
                ;
        return tutorCE;
    }

    public boolean aperturaVisualizacionEncuesta(String tipo){
        List<AperturaVisualizacionEncuestas> ave = f.getEntityManager()
                .createQuery("select a from AperturaVisualizacionEncuestas as a where a.encuesta = :tipo and :fecha BETWEEN a.fechaInicial AND a.fechaFinal", AperturaVisualizacionEncuestas.class)
                .setParameter("tipo", tipo)
                .setParameter("fecha", new Date())
                .getResultStream().collect(Collectors.toList());
        if(ave.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    public List<AlumnosEncuestas> obtenerListaAlumnosNoAccedieron(){
        TypedQuery<AlumnosEncuestas> q=f2.getEntityManager().createQuery("SELECT a FROM AlumnosEncuestas a", AlumnosEncuestas.class);
        List<AlumnosEncuestas> pr=q.getResultList();
        if(!pr.isEmpty()){
            return pr;
        }else{
            return null;
        }
    }

    public Grupos obtenerCuatriPorTutor(Integer cveMaestro){
        TypedQuery<Grupos> q=f2.getEntityManager().createQuery("SELECT g FROM Grupos g where g.gruposPK.cvePeriodo=47 AND g.cveMaestro=:cveMaestro", Grupos.class);
        q.setParameter("cveMaestro", cveMaestro);
        List<Grupos> l=q.getResultList();
        if(l.isEmpty()){
            return null;
        }else{
            return l.get(0);
        }
    }
    // ejb´s nuevos

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

    public Boolean activarOrDesactivarVisualizacion(String nombre){
        VariablesProntuario apertura = em.createQuery("select v from VariablesProntuario v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", nombre).getResultStream().findFirst().orElse(null);
        assert apertura != null;
        if(apertura.getValor().equals("1")){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }


}
