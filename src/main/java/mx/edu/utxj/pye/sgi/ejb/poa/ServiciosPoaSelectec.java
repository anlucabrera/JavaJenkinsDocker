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
import mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.pye2.RecursosActividad;
import mx.edu.utxj.pye.sgi.entity.pye2.UnidadMedidas;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Partidas;
import mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero;
import mx.edu.utxj.pye.sgi.entity.pye2.Productos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Proyectos;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;

@Stateful
public class ServiciosPoaSelectec implements EjbPoaSelectec {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb-pye2_ejb_1.0PU")
    private EntityManager em;

    @EJB
    FacadePoa facadePoa;

// ---- Registro de actividades
    //  -------------------------------------------- Actividades Poa -------------------------------------------------
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
    public List<ActividadesPoa> mostrarActividadesPoasAreaEjeyEjercicioFiscal(Short area, Short ejerciciosFiscales, EjesRegistro ejesRegistro) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef WHERE a.area = :area AND ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal", ActividadesPoa.class);
        q.setParameter("area", area);
        q.setParameter("eje", ejesRegistro.getEje());
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
    public List<ActividadesPoa> mostrarActividadesPoasUniversidadaEjercicioFiscal(Short ejerciciosFiscales) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.ejercicioFiscal ef WHERE ef.ejercicioFiscal=:ejercicioFiscal", ActividadesPoa.class);
        q.setParameter("ejercicioFiscal", ejerciciosFiscales);
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ActividadesPoa> mostrarActividadesPoaCuadroDeMando(Short area, Short ejercicioFiscal, CuadroMandoIntegral cuadroMando) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.ejercicioFiscal ef WHERE a.area = :area AND ef.ejercicioFiscal=:ejercicioFiscal AND a.numeroS=:numeroS AND cm.cuadroMandoInt=:cuadroMandoInt", ActividadesPoa.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        q.setParameter("numeroS", Short.parseShort("0"));
        q.setParameter("cuadroMandoInt", cuadroMando.getCuadroMandoInt());
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
//  -------------------------------------------- EjesRegistro --------------------------------------------------

    @Override
    public EjesRegistro mostrarEjeRegistro(Integer clave) {
        facadePoa.setEntityClass(EjesRegistro.class);
        EjesRegistro pr = facadePoa.getEntityManager().find(EjesRegistro.class, clave);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }

    @Override
    public List<EjesRegistro> mostrarEjesRegistros() {
        facadePoa.setEntityClass(EjesRegistro.class);
        return facadePoa.findAll();
    }

    @Override
    public List<EjesRegistro> mostrarEjesRegistrosAreas(Short area, Short ejerciciosFiscales) {
        TypedQuery<EjesRegistro> q = em.createQuery("SELECT ej FROM ActividadesPoa ap INNER JOIN ap.cuadroMandoInt cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef WHERE ap.area=:area AND ef.ejercicioFiscal=:ejercicioFiscal GROUP BY ej.eje", EjesRegistro.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejerciciosFiscales);
        List<EjesRegistro> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<EjesRegistro> mostrarEjesRegistrosAreasCMI(Short area, Short ejerciciosFiscales) {
        TypedQuery<EjesRegistro> q = em.createQuery("SELECT ej FROM ActividadesPoa ap INNER JOIN ap.cuadroMandoInt cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef WHERE ap.area=:area AND ef.ejercicioFiscal=:ejercicioFiscal AND cm.proyecto!=null GROUP BY ej.eje", EjesRegistro.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejerciciosFiscales);
        List<EjesRegistro> pr = q.getResultList();
        return pr;
    }
//  -------------------------------------------- Estrategias -------------------------------------------------

    @Override
    public Estrategias mostrarEstrategia(Short clave) {
        facadePoa.setEntityClass(Estrategias.class);
        Estrategias pr = facadePoa.getEntityManager().find(Estrategias.class, clave);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }

    @Override
    public List<Estrategias> mostrarEstrategiasRegistro(Short ejerciciosFiscales, EjesRegistro ejesRegistro) {
        TypedQuery<Estrategias> q = em.createQuery("SELECT e FROM CuadroMandoIntegral cm INNER JOIN cm.estrategia e INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.eje ej WHERE ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal GROUP BY e.estrategia", Estrategias.class);
        q.setParameter("eje", ejesRegistro.getEje());
        q.setParameter("ejercicioFiscal", ejerciciosFiscales);
        List<Estrategias> pr = q.getResultList();
        return pr;
    }

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

    //  -------------------------------------------- Lineas Accion -------------------------------------------------
    @Override
    public LineasAccion mostrarLineaAccion(Short clave) {
        facadePoa.setEntityClass(LineasAccion.class);
        LineasAccion pr = facadePoa.getEntityManager().find(LineasAccion.class, clave);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }

    @Override
    public List<LineasAccion> mostrarLineasAccionRegistroParametros(Short area, Short ejerciciosFiscales, EjesRegistro ejesRegistro, Estrategias estrategias) {
        TypedQuery<LineasAccion> q = em.createQuery("SELECT l FROM ActividadesPoa ap INNER JOIN  ap.cuadroMandoInt cm  INNER JOIN cm.lineaAccion l INNER JOIN cm.estrategia e INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.eje ej WHERE ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND e.estrategia=:estrategia AND ap.area=:area GROUP BY l.lineaAccion", LineasAccion.class);
        q.setParameter("area", area);
        q.setParameter("eje", ejesRegistro.getEje());
        q.setParameter("ejercicioFiscal", ejerciciosFiscales);
        q.setParameter("estrategia", estrategias.getEstrategia());
        List<LineasAccion> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<LineasAccion> mostrarLineasAccionRegistro(Short ejerciciosFiscales, EjesRegistro ejesRegistro, Estrategias estrategias) {
        TypedQuery<LineasAccion> q = em.createQuery("SELECT l FROM CuadroMandoIntegral cm INNER JOIN cm.lineaAccion l INNER JOIN cm.estrategia e INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.eje ej WHERE ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND e.estrategia=:estrategia GROUP BY l.lineaAccion", LineasAccion.class);
        q.setParameter("eje", ejesRegistro.getEje());
        q.setParameter("ejercicioFiscal", ejerciciosFiscales);
        q.setParameter("estrategia", estrategias.getEstrategia());
        List<LineasAccion> pr = q.getResultList();
        return pr;
    }
//  -------------------------------------------- UnidadMedidas --------------------------------------------------

    @Override
    public List<UnidadMedidas> mostrarUnidadMedidases() {
        facadePoa.setEntityClass(UnidadMedidas.class);
        return facadePoa.findAll();
    }

    @Override
    public UnidadMedidas agregarUnidadMedidas(UnidadMedidas nuevaUnidadMedidas) {
        facadePoa.setEntityClass(UnidadMedidas.class);
        facadePoa.create(nuevaUnidadMedidas);
        facadePoa.flush();
        return nuevaUnidadMedidas;
    }

    //  ------------------------------------------CuadroMandoIntegral -----------------------------------------------
    @Override
    public List<CuadroMandoIntegral> mostrarCuadroMandoIntegralRegistrpo(Short ejercicioFiscal, EjesRegistro ejesRegistro, Estrategias estrategias, LineasAccion lineasAccion) {
        TypedQuery<CuadroMandoIntegral> q = em.createQuery("SELECT c FROM CuadroMandoIntegral c INNER JOIN c.ejercicioFiscal ef INNER JOIN c.eje ej INNER JOIN c.estrategia es INNER JOIN c.lineaAccion la WHERE ef.ejercicioFiscal = :ejercicioFiscal AND ej.eje=:eje AND es.estrategia=:estrategia AND la.lineaAccion=:lineaAccion", CuadroMandoIntegral.class);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        q.setParameter("eje", ejesRegistro.getEje());
        q.setParameter("estrategia", estrategias.getEstrategia());
        q.setParameter("lineaAccion", lineasAccion.getLineaAccion());
        List<CuadroMandoIntegral> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<CuadroMandoIntegral> mostrarCuadroMandoIntegrals(Short ejercicioFiscal) {
        TypedQuery<CuadroMandoIntegral> q = em.createQuery("SELECT c FROM CuadroMandoIntegral c INNER JOIN c.ejercicioFiscal e WHERE e.ejercicioFiscal = :ejercicioFiscal", CuadroMandoIntegral.class);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        List<CuadroMandoIntegral> pr = q.getResultList();
        return pr;
    }
// ---- Fin Registro de actividades
// ---- Recurso    

    @Override
    public List<Partidas> mostrarPartidases(Short ejercicio, Short area) {
        TypedQuery<Partidas> q = em.createQuery("SELECT p FROM ProductosAreas pa INNER JOIN pa.partida p WHERE pa.area = :area AND pa.productos.productosPK.ejercicioFiscal=:ejercicioFiscal GROUP BY p.partida", Partidas.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejercicio);
        List<Partidas> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Productos> mostrarProductoses(Short ejercicio, Short area, Partidas partida) {
        TypedQuery<Productos> q = em.createQuery("SELECT p FROM ProductosAreas pa INNER JOIN pa.productos p INNER JOIN pa.partida pr WHERE pa.area = :area AND pa.productos.productosPK.ejercicioFiscal=:ejercicioFiscal AND pr.partida=:partida GROUP BY p.productosPK", Productos.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejercicio);
        q.setParameter("partida", partida.getPartida());
        List<Productos> pr = q.getResultList();
        return pr;
    }

    @Override
    public Partidas mostrarPartidas(Short partida) {
        facadePoa.setEntityClass(Partidas.class);
        Partidas nuevoProductos = (Partidas) facadePoa.find(partida);
        return nuevoProductos;
    }

    @Override
    public Productos mostrarProductos(ProductosPK ppk) {
        facadePoa.setEntityClass(Productos.class);
        Productos nuevoProductos = (Productos) facadePoa.find(ppk);
        return nuevoProductos;
    }

    @Override
    public ProductosAreas mostrarProductosAreas(Productos productos, Partidas partidas, Short area) {
        TypedQuery<ProductosAreas> q = em.createQuery("SELECT p FROM ProductosAreas p WHERE p.productos=:productos AND p.partida=:partida AND p.area=:area", ProductosAreas.class);
        q.setParameter("area", area);
        q.setParameter("productos", productos);
        q.setParameter("partida", partidas);
        List<ProductosAreas> pr = q.getResultList();
        if (pr.isEmpty()) {
            return null;
        } else {
            return pr.get(0);
        }
    }
// ---- Fin Registro de actividades  
//  -------------------------------------------- ActividadesPoa -------------------------------------------------

    @Override
    public List<ActividadesPoa> mostrarAreasQueRegistraronActividades() {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a GROUP BY a.area", ActividadesPoa.class);
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ActividadesPoa> mostrarActividadesPoasArea(Short area) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a WHERE a.area = :area", ActividadesPoa.class);
        q.setParameter("area", area);
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ActividadesPoa> mostrarActividadesPoasReporteArea(Short area, Short ejercicioFiscal) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.ejercicioFiscal ef WHERE a.area = :area AND ef.ejercicioFiscal=:ejercicioFiscal", ActividadesPoa.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ActividadesPoa> mostrarSubActividadesPoa(Short area, Short ejercicioFiscal, Short numeroP, CuadroMandoIntegral cuadroMando) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.ejercicioFiscal ef WHERE a.area = :area AND ef.ejercicioFiscal=:ejercicioFiscal AND a.numeroP=:numeroP AND cm.cuadroMandoInt=:cuadroMandoInt", ActividadesPoa.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        q.setParameter("numeroP", numeroP);
        q.setParameter("cuadroMandoInt", cuadroMando.getCuadroMandoInt());
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
    public List<RecursosActividad> mostrarRecursosActividad(Short area, Short ejercicioFical, Short capitulo) {
        TypedQuery<RecursosActividad> q = em.createQuery("SELECT r FROM RecursosActividad r INNER JOIN r.productoArea p WHERE p.area = :area AND p.productos.productosPK.ejercicioFiscal=:ejercicioFiscal AND p.capitulo.capituloTipo=:capituloTipo", RecursosActividad.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejercicioFical);
        q.setParameter("capituloTipo", capitulo);
        List<RecursosActividad> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<RecursosActividad> mostrarRecursosActividadReporte(ActividadesPoa actividadesPoa) {
        TypedQuery<RecursosActividad> q = em.createQuery("SELECT r FROM RecursosActividad r INNER JOIN r.actividadPoa ap WHERE ap.actividadPoa = :actividadPoa", RecursosActividad.class);
        q.setParameter("actividadPoa", actividadesPoa.getActividadPoa());
        List<RecursosActividad> pr = q.getResultList();
        return pr;
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
    public Evidencias eliminarEvidencias(Evidencias evidencias) {
        facadePoa.setEntityClass(Evidencias.class);
        facadePoa.remove(evidencias);
        facadePoa.flush();
        return evidencias;
    }

    @Override
    public EvidenciasDetalle eliminarEvidenciasDetalle(EvidenciasDetalle evidenciasDetalle) {
        facadePoa.setEntityClass(EvidenciasDetalle.class);
        facadePoa.remove(evidenciasDetalle);
        facadePoa.flush();
        return evidenciasDetalle;
    }

    @Override
    public List<Evidencias> mostrarEvidenciases(ActividadesPoa actividad, List<Registros> registroses) {
        List<Evidencias> lev = new ArrayList<>();
        lev.clear();

        TypedQuery<Evidencias> q = em.createQuery("SELECT e FROM Evidencias e INNER JOIN e.actividadesPoaList a WHERE a.actividadPoa=:actividadPoa", Evidencias.class);
        q.setParameter("actividadPoa", actividad.getActividadPoa());
        List<Evidencias> pr = q.getResultList();
        if (!pr.isEmpty()) {
            lev.addAll(pr);
        }
        if (!registroses.isEmpty()) {
            registroses.forEach((t) -> {
                TypedQuery<Evidencias> p = em.createQuery("SELECT e FROM Evidencias e INNER JOIN e.registrosList r WHERE r.registro=:registro", Evidencias.class);
                p.setParameter("registro", t.getRegistro());
                List<Evidencias> prr = p.getResultList();
                if(!prr.isEmpty()){                    
                lev.addAll(prr);
                }
            });
        }
        return lev;
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
    public List<Proyectos> getProyectosPorEje(EjesRegistro eje, Short ejercicio, Short area) {
        return facadePoa.getEntityManager().createQuery("SELECT p FROM Proyectos p INNER JOIN p.cuadroMandoIntegralList cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.actividadesPoaList ap WHERE ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND ap.area=:area ORDER BY p.proyecto", Proyectos.class)
                .setParameter("eje", eje.getEje())
                .setParameter("ejercicioFiscal", ejercicio)
                .setParameter("area", area)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Proyectos> getProyectos(EjesRegistro eje, Short ejercicio) {
        return facadePoa.getEntityManager().createQuery("SELECT p FROM Proyectos p INNER JOIN p.cuadroMandoIntegralList cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef WHERE ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal ORDER BY p.proyecto", Proyectos.class)
                .setParameter("eje", eje.getEje())
                .setParameter("ejercicioFiscal", ejercicio)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Estrategias> getEstrategiaPorProyectos(Proyectos proyectos, Short ejercicio, Short area) {
        return facadePoa.getEntityManager().createQuery("SELECT e FROM Estrategias e INNER JOIN e.cuadroMandoIntegralList cm INNER JOIN cm.proyecto p INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.actividadesPoaList ap WHERE p.proyecto=:proyecto AND ef.ejercicioFiscal=:ejercicioFiscal AND ap.area=:area ORDER BY e.estrategia", Estrategias.class)
                .setParameter("proyecto", proyectos.getProyecto())
                .setParameter("ejercicioFiscal", ejercicio)
                .setParameter("area", area)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Estrategias> getEstrategiaProyectos(Proyectos proyectos, Short ejercicio) {
        return facadePoa.getEntityManager().createQuery("SELECT e FROM Estrategias e INNER JOIN e.cuadroMandoIntegralList cm INNER JOIN cm.proyecto p INNER JOIN cm.ejercicioFiscal ef WHERE p.proyecto=:proyecto AND ef.ejercicioFiscal=:ejercicioFiscal ORDER BY e.estrategia", Estrategias.class)
                .setParameter("proyecto", proyectos.getProyecto())
                .setParameter("ejercicioFiscal", ejercicio)
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
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
    public List<ActividadesPoa> getActividadesPoasProyecto(Estrategias estrategia, EjesRegistro eje, Proyectos proyectos, Short ejercicio) {
        List<ActividadesPoa> actividades = new ArrayList<>();
        facadePoa.flush();
        actividades.clear();
        actividades = facadePoa.getEntityManager().createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.eje ej INNER JOIN cm.ejercicioFiscal ef INNER JOIN cm.proyecto p WHERE cm.estrategia.estrategia=:estrategia AND ej.eje=:eje AND ef.ejercicioFiscal=:ejercicioFiscal AND p.proyecto=:proyecto GROUP BY a.actividadPoa ORDER BY a.numeroP, a.numeroS, a.area ", ActividadesPoa.class)
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
    public List<Registros> mostrarRegistrosActividad(ActividadesPoa actividadesPoa) {
        TypedQuery<Registros> q = em.createQuery("SELECT r FROM Registros r INNER JOIN r.actividadesPoaList a WHERE a.actividadPoa=:actividadPoa", Registros.class);
        q.setParameter("actividadPoa", actividadesPoa.getActividadPoa());
        List<Registros> pr = q.getResultList();
        return pr;
    }
}
