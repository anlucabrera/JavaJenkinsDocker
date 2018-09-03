/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.prontuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.OrganismosEvaluadores;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;


/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioCatalogos implements EjbCatalogos{
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public List<Generaciones> getGeneracionesAct() {
        List<Generaciones> genLst = new ArrayList<>();
        TypedQuery<Generaciones> query = em.createQuery("SELECT g FROM Generaciones g", Generaciones.class);
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

    @Override
    public List<AreasUniversidad> getProgramasEducativos() {
        List<AreasUniversidad> genLst = new ArrayList<>();
        TypedQuery<AreasUniversidad> query = em.createQuery("SELECT a FROM AreasUniversidad a JOIN a.categoria c WHERE c.categoria = :categoria AND a.vigente = :vigente ORDER BY a.nombre ASC", AreasUniversidad.class);
        query.setParameter("categoria", 9);
        query.setParameter("vigente", "1");
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

    @Override
    public List<CiclosEscolares> getCiclosEscolaresAct() {
        List<CiclosEscolares> genLst = new ArrayList<>();
        TypedQuery<CiclosEscolares> query = em.createQuery("SELECT c FROM CiclosEscolares c", CiclosEscolares.class);
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

    @Override
    public List<PeriodosEscolares> getPeriodosEscolaresAct() {
        List<PeriodosEscolares> genLst = new ArrayList<>();
        TypedQuery<PeriodosEscolares> query = em.createQuery("SELECT p FROM PeriodosEscolares p", PeriodosEscolares.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }
    
     @Override
    public List<OrganismosEvaluadores> getOrganismosEvaluadoresAct() {
        List<OrganismosEvaluadores> genLst = new ArrayList<>();
        TypedQuery<OrganismosEvaluadores> query = em.createQuery("SELECT o FROM OrganismosEvaluadores o", OrganismosEvaluadores.class);
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }
    @Override
    public List<AreasUniversidad> getAreasAcademicas() {
        List<AreasUniversidad> areasAcademicas = new ArrayList<>();
        TypedQuery<AreasUniversidad> query = em.createQuery("SELECT a FROM AreasUniversidad a JOIN a.categoria c WHERE c.categoria = :categoria ORDER BY a.nombre", AreasUniversidad.class);
        query.setParameter("categoria", 8);
        try {
            areasAcademicas = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            areasAcademicas = null;
        }
        return areasAcademicas;
    }

}
