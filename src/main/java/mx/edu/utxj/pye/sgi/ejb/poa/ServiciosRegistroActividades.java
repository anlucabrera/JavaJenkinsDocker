package mx.edu.utxj.pye.sgi.ejb.poa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.Proyectos;

@Stateful
public class ServiciosRegistroActividades implements EjbRegistroActividades {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb-pye2_ejb_1.0PU")
    private EntityManager em;

    @EJB
    FacadePoa facadePoa;

    @Override
    public ActividadesPoa mostrarActividadPoaPrincipal(Integer clave) {
        facadePoa.setEntityClass(ActividadesPoa.class);
        ActividadesPoa pr = facadePoa.getEntityManager().find(ActividadesPoa.class, clave);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }

    @Override
    public ActividadesPoa agregarActividadesPoa(ActividadesPoa nuevaActividadesPoa) {
        facadePoa.setEntityClass(ActividadesPoa.class);
        facadePoa.create(nuevaActividadesPoa);
        facadePoa.flush();
        return nuevaActividadesPoa;
    }

    @Override
    public ActividadesPoa actualizaActividadesPoa(ActividadesPoa nuevaActividadesPoa) {
        facadePoa.setEntityClass(ActividadesPoa.class);
        facadePoa.edit(nuevaActividadesPoa);
        facadePoa.flush();
        return nuevaActividadesPoa;
    }

    @Override
    public ActividadesPoa eliminarActividadesPoa(ActividadesPoa nuevaActividadesPoa) {
        facadePoa.setEntityClass(ActividadesPoa.class);
        facadePoa.remove(nuevaActividadesPoa);
        facadePoa.flush();
        return nuevaActividadesPoa;
    }

    @Override
    public List<ActividadesPoa> mostrarSubActividadesPoa(Integer claveActividadPadre) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a WHERE a.actividadPadre=:actividadPadre ORDER BY a.numeroS", ActividadesPoa.class);
        q.setParameter("actividadPadre", claveActividadPadre);
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ActividadesPoa> mostrarActividadesPoaPrincipalesCuadroMando(Integer CuadroDeMando, Short claveArea) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt c WHERE c.cuadroMandoInt=:cuadroMandoInt AND a.numeroS=:numeroS AND a.area=:area ORDER BY a.numeroP", ActividadesPoa.class);
        q.setParameter("cuadroMandoInt", CuadroDeMando);
        q.setParameter("numeroS", Short.parseShort("0"));
        q.setParameter("area", claveArea);
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ActividadesPoa> mostrarActividadesPoasTotalArea(Short area, Short ejercicioFiscal) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.ejercicioFiscal ef WHERE a.area = :area AND ef.ejercicioFiscal=:ejercicioFiscal", ActividadesPoa.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ActividadesPoa> mostrarActividadesPoasEje(Short area, Short ejerciciosFiscales, EjesRegistro ejesRegistro) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef WHERE a.area = :area AND ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal", ActividadesPoa.class);
        q.setParameter("area", area);
        q.setParameter("eje", ejesRegistro.getEje());
        q.setParameter("ejercicioFiscal", ejerciciosFiscales);
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ActividadesPoa> getActividadesPoasEstarategias(Estrategias estrategia, EjesRegistro eje, Short ejercicio, Short area) {
        List<ActividadesPoa> actividades = new ArrayList<>();
        facadePoa.flush();
        actividades.clear();
        actividades = facadePoa.getEntityManager().createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef WHERE cm.estrategia.estrategia=:estrategia AND ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND a.area=:area GROUP BY a.actividadPoa ORDER BY cm.cuadroMandoInt, a.numeroP, a.numeroS", ActividadesPoa.class)
                .setParameter("estrategia", estrategia.getEstrategia())
                .setParameter("eje", eje.getEje())
                .setParameter("ejercicioFiscal", ejercicio)
                .setParameter("area", area)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
        if (!actividades.isEmpty()) {
            actividades.forEach((t) -> {
                facadePoa.getEntityManager().detach(t);
                facadePoa.flush();
            });
        }
        return actividades;
    }

    @Override
    public List<ActividadesPoa> getActividadesPoasporProyecto(Estrategias estrategia, EjesRegistro eje, Proyectos proyectos, Short ejercicio, Short area) {
        List<ActividadesPoa> actividades = new ArrayList<>();
        facadePoa.flush();
        actividades.clear();
        actividades = facadePoa.getEntityManager().createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.proyecto p WHERE cm.estrategia.estrategia=:estrategia AND ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND a.area=:area AND p.proyecto=:proyecto GROUP BY a.actividadPoa ORDER BY a.numeroP, a.numeroS", ActividadesPoa.class)
                .setParameter("estrategia", estrategia.getEstrategia())
                .setParameter("eje", eje.getEje())
                .setParameter("ejercicioFiscal", ejercicio)
                .setParameter("area", area)
                .setParameter("proyecto", proyectos.getProyecto())
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
        if (!actividades.isEmpty()) {
            actividades.forEach((t) -> {
                facadePoa.getEntityManager().detach(t);
                facadePoa.flush();
            });
        }
        return actividades;
    }

    @Override
    public List<ActividadesPoa> mostrarActividadesPoasUniversidadaEjercicioFiscal(Short ejerciciosFiscales) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.ejercicioFiscal ef WHERE ef.ejercicioFiscal=:ejercicioFiscal", ActividadesPoa.class);
        q.setParameter("ejercicioFiscal", ejerciciosFiscales);
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ActividadesPoa> mostrarActividadesPoasUniversidadaEjeyEjercicioFiscal(Short ejerciciosFiscales, EjesRegistro ejesRegistro) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef WHERE ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal", ActividadesPoa.class);
        q.setParameter("eje", ejesRegistro.getEje());
        q.setParameter("ejercicioFiscal", ejerciciosFiscales);
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ActividadesPoa> getActividadesPoasProyectoGrfica(EjesRegistro eje, Proyectos proyectos, Short ejercicio) {
        List<ActividadesPoa> actividades = new ArrayList<>();
        facadePoa.flush();
        actividades.clear();
        actividades = facadePoa.getEntityManager().createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.proyecto p WHERE ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND p.proyecto=:proyecto GROUP BY a.actividadPoa ORDER BY a.numeroP, a.numeroS, a.area ", ActividadesPoa.class)
                .setParameter("eje", eje.getEje())
                .setParameter("ejercicioFiscal", ejercicio)
                .setParameter("proyecto", proyectos.getProyecto())
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
        if (!actividades.isEmpty()) {
            actividades.forEach((t) -> {
                facadePoa.getEntityManager().detach(t);
                facadePoa.flush();
            });
        }
        return actividades;
    }

    @Override
    public List<ActividadesPoa> getActividadesPoasProyecto(Estrategias estrategia, EjesRegistro eje, Proyectos proyectos, Short ejercicio) {
        List<ActividadesPoa> actividades = new ArrayList<>();
        facadePoa.flush();
        actividades.clear();
        actividades = facadePoa.getEntityManager().createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.proyecto p WHERE cm.estrategia.estrategia=:estrategia AND ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND p.proyecto=:proyecto GROUP BY a.actividadPoa ORDER BY cm.cuadroMandoInt, a.numeroP, a.numeroS, a.area ", ActividadesPoa.class)
                .setParameter("estrategia", estrategia.getEstrategia())
                .setParameter("eje", eje.getEje())
                .setParameter("ejercicioFiscal", ejercicio)
                .setParameter("proyecto", proyectos.getProyecto())
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
        if (!actividades.isEmpty()) {
            actividades.forEach((t) -> {
                facadePoa.getEntityManager().detach(t);
                facadePoa.flush();
            });
        }
        return actividades;
    }

    @Override
    public List<ActividadesPoa> getActividadesEvaluacionMadre(CuadroMandoIntegral cuadroDmando, Short numeroP, Short area) {
        return facadePoa.getEntityManager().createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm WHERE cm.cuadroMandoInt=:cuadroMandoInt AND a.numeroP=:numeroP  AND a.area=:area ORDER BY a.actividadPoa", ActividadesPoa.class)
                .setParameter("cuadroMandoInt", cuadroDmando.getCuadroMandoInt())
                .setParameter("numeroP", numeroP)
                .setParameter("area", area)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<ActividadesPoa> mostrarActividadesPoasArea(Short area) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a WHERE a.area = :area", ActividadesPoa.class);
        q.setParameter("area", area);
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
    }

    @Override//cambios de 
    public List<ActividadesPoa> mostrarActividadesPoaCuadroDeMandoRecurso(Short area, Short ejercicioFiscal, CuadroMandoIntegral cuadroMando) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.ejercicioFiscal ef WHERE a.area = :area AND ef.ejercicioFiscal=:ejercicioFiscal AND cm.cuadroMandoInt=:cuadroMandoInt ORDER BY cm.cuadroMandoInt, a.numeroP, a.numeroS", ActividadesPoa.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        q.setParameter("cuadroMandoInt", cuadroMando.getCuadroMandoInt());
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
    }
    
    @Override
    public List<ActividadesPoa> getActividadesPoasporEstarategias(Estrategias estrategia, EjesRegistro eje, Short ejercicio, Short area) {
        List<ActividadesPoa> actividades = new ArrayList<>();
        facadePoa.flush();
        actividades.clear();
        actividades = facadePoa.getEntityManager().createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef WHERE cm.estrategia.estrategia=:estrategia AND ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND a.area=:area GROUP BY a.actividadPoa ORDER BY cm.cuadroMandoInt, a.numeroP, a.numeroS", ActividadesPoa.class)
                .setParameter("estrategia", estrategia.getEstrategia())
                .setParameter("eje", eje.getEje())
                .setParameter("ejercicioFiscal", ejercicio)
                .setParameter("area", area)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
        if (!actividades.isEmpty()) {
            actividades.forEach((t) -> {
                facadePoa.getEntityManager().detach(t);
                facadePoa.flush();
            });
        }
        return actividades;
    }
     @Override
    public List<ActividadesPoa> mostrarAreasQueRegistraronActividades() {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a GROUP BY a.area", ActividadesPoa.class);
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
    }
}
