package mx.edu.utxj.pye.sgi.ejb.poa;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.pye2.RecursosActividad;
import mx.edu.utxj.pye.sgi.entity.pye2.UnidadMedidas;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero;

@Stateful
public class ServiciosPoaSelectec implements EjbPoaSelectec {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb-pye2_ejb_1.0PU")
    private EntityManager em;

    @EJB
    FacadePoa facadePoa;

//  -------------------------------------------- ActividadesPoa -------------------------------------------------
    @Override
    public List<ActividadesPoa> mostrarActividadesPoasArea(Short area) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a WHERE a.area = :area", ActividadesPoa.class);
        q.setParameter("area", area);
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
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
//  ------------------------------------------ RecursosActividad ------------------------------------------------

    @Override
    public List<RecursosActividad> mostrarRecursosActividad() {
        facadePoa.setEntityClass(RecursosActividad.class);
        return facadePoa.findAll();
    }

    @Override
    public RecursosActividad agregarRecursosActividad(RecursosActividad nuevoRecursosActividad) {
        facadePoa.setEntityClass(RecursosActividad.class);
        facadePoa.create(nuevoRecursosActividad);
        facadePoa.flush();
        return nuevoRecursosActividad;
    }

    @Override
    public RecursosActividad actualizaRecursosActividad(RecursosActividad nuevoRecursosActividad) {
        facadePoa.setEntityClass(RecursosActividad.class);
        facadePoa.edit(nuevoRecursosActividad);
        facadePoa.flush();
        return nuevoRecursosActividad;
    }

    @Override
    public RecursosActividad eliminarRecursosActividad(RecursosActividad nuevoRecursosActividad) {
        facadePoa.setEntityClass(RecursosActividad.class);
        facadePoa.remove(nuevoRecursosActividad);
        facadePoa.flush();
        return nuevoRecursosActividad;
    }
//  ------------------------------------------CuadroMandoIntegral -----------------------------------------------

    @Override
    public List<CuadroMandoIntegral> mostrarCuadroMandoIntegrals(Short ejercicioFiscal) {
        TypedQuery<CuadroMandoIntegral> q = em.createQuery("SELECT c FROM CuadroMandoIntegral c INNER JOIN c.ejercicioFiscal e WHERE e.ejercicioFiscal = :ejercicioFiscal", CuadroMandoIntegral.class);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        List<CuadroMandoIntegral> pr = q.getResultList();
        return pr;
    }
//  -------------------------------------------- UnidadMedidas --------------------------------------------------

    @Override
    public List<UnidadMedidas> mostrarUnidadMedidases() {
        facadePoa.setEntityClass(UnidadMedidas.class);
        return facadePoa.findAll();
    }
//  -------------------------------------------- EjesRegistro --------------------------------------------------

    @Override
    public List<EjesRegistro> mostrarEjesRegistros() {
        facadePoa.setEntityClass(EjesRegistro.class);
        return facadePoa.findAll();
    }
//  -------------------------------------------- EjerciciosFiscales --------------------------------------------------

    @Override
    public List<EjerciciosFiscales> mostrarEjerciciosFiscaleses() {
        facadePoa.setEntityClass(EjesRegistro.class);
        return facadePoa.findAll();
    }

//  ------------------------------------------- PretechoFinanciero --------------------------------------------------
    @Override
    public List<PretechoFinanciero> mostrarPretechoFinancieros(Short clavearea, Short ejercicioFiscal) {
        TypedQuery<PretechoFinanciero> q = em.createQuery("SELECT p FROM PretechoFinanciero p INNER JOIN p.ejercicioFiscal e WHERE p.area = :area AND e.ejercicioFiscal=:ejercicioFiscal", PretechoFinanciero.class);
        q.setParameter("area", clavearea);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        List<PretechoFinanciero> pr = q.getResultList();
        return pr;
    }
//  ------------------------------------------- Evidencias --------------------------------------------------

    @Override
    public Evidencias agregarEvidenciases(Evidencias evidencias, ActividadesPoa actividadesPoa) {
        facadePoa.setEntityClass(Evidencias.class);
        if (!actividadesPoa.getEvidenciasList().contains(evidencias)) {
            actividadesPoa.getEvidenciasList().add(evidencias);
            facadePoa.create(evidencias);
            evidencias.getActividadesPoaList().add(actividadesPoa);
            facadePoa.edit(actividadesPoa);
            facadePoa.flush();
        }
        return evidencias;
    }

    @Override
    public Evidencias actualizarEvidenciases(Evidencias evidencias) {
        facadePoa.setEntityClass(Evidencias.class);
        facadePoa.edit(evidencias);
        facadePoa.flush();
        return evidencias;
    }

    @Override
    public EvidenciasDetalle agregarEvidenciasesEvidenciasDetalle(EvidenciasDetalle evidenciasDetalle) {
        facadePoa.setEntityClass(Evidencias.class);
        facadePoa.create(evidenciasDetalle);
        facadePoa.flush();
        return evidenciasDetalle;
    }

    @Override
    public List<Evidencias> mostrarEvidenciases(ActividadesPoa actividad) {
        TypedQuery<Evidencias> q = em.createQuery("SELECT e FROM Evidencias e INNER JOIN e.actividadesPoaList a WHERE a.actividadPoa=:actividadPoa", Evidencias.class);
        q.setParameter("actividadPoa", actividad.getActividadPoa());
        List<Evidencias> pr = q.getResultList();
        return pr;
    }
//  ------------------------------------------- ProductosAreas --------------------------------------------------

    @Override
    public List<ProductosAreas> mostrarProductosAreases(Short clavearea, Short ejercicioFiscal) {
        TypedQuery<ProductosAreas> q = em.createQuery("SELECT p FROM ProductosAreas p WHERE p.area = :area AND p.productos.productosPK.ejercicioFiscal=:ejercicioFiscal", ProductosAreas.class);
        q.setParameter("area", clavearea);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        List<ProductosAreas> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<EvidenciasDetalle> mostrarEvidenciases(Evidencias evidencias) {
        TypedQuery<EvidenciasDetalle> q = em.createQuery("SELECT d FROM EvidenciasDetalle d INNER JOIN d.evidencia e WHERE e.evidencia = :evidencia", EvidenciasDetalle.class);
        q.setParameter("evidencia", evidencias.getEvidencia());
        List<EvidenciasDetalle> pr = q.getResultList();
        return pr;
    }
//  -------------------------------------------------------------------------------------------------------------

    @Override
    public List<Estrategias> getEstarategiasPorEje(EjesRegistro eje, Short ejercicio, Short area) {
        return facadePoa.getEntityManager().createQuery("SELECT e FROM Estrategias e INNER JOIN e.cuadroMandoIntegralList cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.actividadesPoaList ap WHERE ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND ap.area=:area ORDER BY e.estrategia", Estrategias.class)
                .setParameter("eje", eje.getEje())
                .setParameter("ejercicioFiscal", ejercicio)
                .setParameter("area", area)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<ActividadesPoa> getActividadesPoasporEstarategias(Estrategias estrategia, EjesRegistro eje, Short ejercicio, Short area) {
        return facadePoa.getEntityManager().createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef WHERE cm.estrategia.estrategia=:estrategia AND ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND a.area=:area GROUP BY a.actividadPoa ORDER BY a.actividadPoa", ActividadesPoa.class)
                .setParameter("estrategia", estrategia.getEstrategia())
                .setParameter("eje", eje.getEje())
                .setParameter("ejercicioFiscal", ejercicio)
                .setParameter("area", area)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<ActividadesPoa> getActividadesEvaluacionMadre(CuadroMandoIntegral cuadroDmando, Short numeroP) {
        return facadePoa.getEntityManager().createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm WHERE cm.cuadroMandoInt=:cuadroMandoInt AND a.numeroP=:numeroP ORDER BY a.actividadPoa", ActividadesPoa.class)
                .setParameter("cuadroMandoInt", cuadroDmando.getCuadroMandoInt())
                .setParameter("numeroP", numeroP)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

}
