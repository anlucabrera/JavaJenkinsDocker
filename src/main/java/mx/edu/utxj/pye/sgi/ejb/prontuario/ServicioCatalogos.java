/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.prontuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.dto.listaDTOCiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.BecaTipos;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.OrganismosEvaluadores;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.omnifaces.util.Messages;


/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioCatalogos implements EjbCatalogos{
    
    @EJB    Facade  facadeServGen;

    @Override
    public List<Generaciones> getGeneracionesAct() {
        List<Generaciones> genLst = new ArrayList<>();
        TypedQuery<Generaciones> query = facadeServGen.getEntityManager().createQuery("SELECT g FROM Generaciones g ORDER BY g.generacion DESC", Generaciones.class);
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
        TypedQuery<AreasUniversidad> query = facadeServGen.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a JOIN a.categoria c WHERE c.categoria = :categoria AND a.vigente = :vigente ORDER BY a.nombre ASC", AreasUniversidad.class);
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
        TypedQuery<CiclosEscolares> query = facadeServGen.getEntityManager().createQuery("SELECT c FROM CiclosEscolares c ORDER BY c.ciclo DESC", CiclosEscolares.class);
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
        TypedQuery<PeriodosEscolares> query = facadeServGen.getEntityManager().createQuery("SELECT p FROM PeriodosEscolares p", PeriodosEscolares.class);
        
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
        TypedQuery<OrganismosEvaluadores> query = facadeServGen.getEntityManager().createQuery("SELECT o FROM OrganismosEvaluadores o", OrganismosEvaluadores.class);
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
        TypedQuery<AreasUniversidad> query = facadeServGen.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a JOIN a.categoria c WHERE c.categoria = :categoria ORDER BY a.nombre", AreasUniversidad.class);
        query.setParameter("categoria", 8);
        try {
            areasAcademicas = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            areasAcademicas = null;
        }
        return areasAcademicas;
    }
    
    @Override
    public List<AreasUniversidad> getAreasAcademicasDistribucionAulas() {
        try {
            return facadeServGen.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a JOIN a.categoria c WHERE c.categoria = :categoria OR a.area = :area ORDER BY a.nombre", AreasUniversidad.class)
                .setParameter("categoria", 8)
                .setParameter("area", 8)
                .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
        
    }

    @Override
    public List<ProgramasEducativos> getProgramasEducativosProntuario() {
        try {
            return facadeServGen.getEntityManager().createQuery("SELECT p FROM ProgramasEducativos p ORDER BY p.nombre", ProgramasEducativos.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<AreasUniversidad> getProgramasEducativoPorAreaAcademica(Short area) {
        try {
            return facadeServGen.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a JOIN a.categoria c WHERE c.categoria = :categoria AND a.vigente = :vigente AND a.areaSuperior = :area ORDER BY a.nombre ASC", AreasUniversidad.class)
                    .setParameter("categoria", 9)
                    .setParameter("vigente", "1")
                    .setParameter("area", area)
                    .getResultList();
        } catch (NoResultException ex) {
            return Collections.EMPTY_LIST;
        }
    }
    
    @Override
    public List<BecaTipos> getBecaTiposAct() {
        List<BecaTipos> genLst = new ArrayList<>();
        TypedQuery<BecaTipos> query = facadeServGen.getEntityManager().createQuery("SELECT b FROM BecaTipos b ORDER BY b.nombre ASC", BecaTipos.class);
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

    @Override
    public List<BajasCausa> getCausasBajaAct() {
        List<BajasCausa> genLst = new ArrayList<>();
        TypedQuery<BajasCausa> query = facadeServGen.getEntityManager().createQuery("SELECT b FROM BajasCausa b ORDER BY b.causa ASC", BajasCausa.class);
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }

    @Override
    public List<BajasTipo> getTipoBajaAct() {
        List<BajasTipo> genLst = new ArrayList<>();
        TypedQuery<BajasTipo> query = facadeServGen.getEntityManager().createQuery("SELECT b FROM BajasTipo b ORDER BY b.descripcion ASC", BajasTipo.class);
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }
    
    @Override
    public List<listaDTOCiclosEscolares> getCiclosEscolaresDTO() {
        List<CiclosEscolares> genLst = new ArrayList<>();
        TypedQuery<CiclosEscolares> query = facadeServGen.getEntityManager().createQuery("SELECT c FROM CiclosEscolares c", CiclosEscolares.class);
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
        
        List<listaDTOCiclosEscolares> l = new ArrayList<>();
        List<CiclosEscolares> ciclosEscolares = genLst.stream().sorted(Comparator.comparing(CiclosEscolares::getInicio).reversed()).collect(Collectors.toList());
        ciclosEscolares.forEach(e -> {
           
            String strDateFormat = "yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            String cicloe = sdf.format(e.getInicio()) + " - " + sdf.format(e.getFin());
            Integer ciclo = e.getCiclo();
            
            l.add(new listaDTOCiclosEscolares(ciclo, cicloe));
        });
        
        return l;
    }

    @Override
    public List<Categorias> getCategoriaAreasConPoa() {
        try {
            return facadeServGen.getEntityManager().createQuery("SELECT DISTINCT(c) FROM Categorias c INNER JOIN c.areasUniversidadList a WHERE a.vigente = :vigente AND a.tienePoa = :tienePOA", Categorias.class)
                    .setParameter("vigente", "1")
                    .setParameter("tienePOA", true)
                    .getResultList();
        } catch (NoResultException e) {
            Messages.addGlobalWarn("<b>No se han encontrado Categorías para el filtrado de areas universidad</b>");
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<AreasUniversidad> getAreasUniversidadPorCategoriaConPoa(Categorias categoria) {
        try {
            return facadeServGen.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a INNER JOIN a.categoria c WHERE c.categoria = :categoria AND a.vigente = :vigente AND a.tienePoa = :tienePOA", AreasUniversidad.class)
                    .setParameter("categoria", categoria.getCategoria())
                    .setParameter("vigente", "1")
                    .setParameter("tienePOA", true)
                    .getResultList();
        } catch (NoResultException e) {
            Messages.addGlobalWarn("<b>No se han encontrado Áreas para el filtrado de ejercicios y mes </b>");
            return Collections.EMPTY_LIST;
        }
    }
}
