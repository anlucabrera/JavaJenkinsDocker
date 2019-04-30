package mx.edu.utxj.pye.sgi.ejb.poa;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.Partidas;
import mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero;
import mx.edu.utxj.pye.sgi.entity.pye2.Productos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosPK;
import mx.edu.utxj.pye.sgi.entity.pye2.RecursosActividad;

@Stateful
public class ServiciosPresupuestacion implements EjbPresupuestacion {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb-pye2_ejb_1.0PU")
    private EntityManager em;

    @EJB
    FacadePoa facadePoa;

//---------------------------------------------------------------------- Actividades Poa --------------------------------------------------
    @Override
    public List<ActividadesPoa> mostrarActividadesPoaCuadroDeMandoRecurso(Short area, Short ejercicioFiscal, CuadroMandoIntegral cuadroMando) {
        TypedQuery<ActividadesPoa> q = em.createQuery("SELECT a FROM ActividadesPoa a INNER JOIN a.cuadroMandoInt cm INNER JOIN cm.ejercicioFiscal ef WHERE a.area = :area AND ef.ejercicioFiscal=:ejercicioFiscal AND cm.cuadroMandoInt=:cuadroMandoInt ORDER BY cm.cuadroMandoInt, a.numeroP, a.numeroS", ActividadesPoa.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        q.setParameter("cuadroMandoInt", cuadroMando.getCuadroMandoInt());
        List<ActividadesPoa> pr = q.getResultList();
        return pr;
    }

//---------------------------------------------------------------------- Recursos Actividad --------------------------------------------------
    @Override
    public List<RecursosActividad> mostrarRecursosActividad(Short area, Short ejercicioFical, Short capitulo) {
        TypedQuery<RecursosActividad> q = em.createQuery("SELECT r FROM RecursosActividad r INNER JOIN r.productoArea p WHERE p.area = :area AND p.productos.productosPK.ejercicioFiscal=:ejercicioFiscal AND p.capitulo.capituloTipo=:capituloTipo", RecursosActividad.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejercicioFical);
        q.setParameter("capituloTipo", capitulo);
        List<RecursosActividad> pr = q.getResultList();
        if (pr.isEmpty()) {
            return new ArrayList<>();
        } else {
            return pr;
        }
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

//  ---------------------------------------------------------------------- Productos Areas --------------------------------------------------
    @Override
    public List<ProductosAreas> mostrarProductosAreases(Short clavearea, Short ejercicioFiscal) {
        TypedQuery<ProductosAreas> q = em.createQuery("SELECT p FROM ProductosAreas p WHERE p.area = :area AND p.productos.productosPK.ejercicioFiscal=:ejercicioFiscal", ProductosAreas.class);
        q.setParameter("area", clavearea);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        List<ProductosAreas> pr = q.getResultList();
        return pr;
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

//  ---------------------------------------------------------------------- Pretecho Financiero --------------------------------------------------
    @Override
    public List<PretechoFinanciero> mostrarPretechoFinancieros(Short clavearea, Short ejercicioFiscal) {
        TypedQuery<PretechoFinanciero> q = em.createQuery("SELECT p FROM PretechoFinanciero p INNER JOIN p.ejercicioFiscal e WHERE p.area = :area AND e.ejercicioFiscal=:ejercicioFiscal", PretechoFinanciero.class);
        q.setParameter("area", clavearea);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        List<PretechoFinanciero> pr = q.getResultList();
        return pr;
    }

//  ------------------------------------------------------------------------ Partidas --------------------------------------------------
    @Override
    public List<Partidas> mostrarPartidases(Short ejercicio, Short area) {
        TypedQuery<Partidas> q = em.createQuery("SELECT p FROM ProductosAreas pa INNER JOIN pa.partida p WHERE pa.area = :area AND pa.productos.productosPK.ejercicioFiscal=:ejercicioFiscal GROUP BY p.partida", Partidas.class);
        q.setParameter("area", area);
        q.setParameter("ejercicioFiscal", ejercicio);
        List<Partidas> pr = q.getResultList();
        return pr;
    }

    @Override
    public Partidas mostrarPartidas(Short partida) {
        facadePoa.setEntityClass(Partidas.class);
        Partidas nuevoProductos = (Partidas) facadePoa.find(partida);
        return nuevoProductos;
    }

//  ------------------------------------------------------------------------ Productos --------------------------------------------------
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
    public Productos mostrarProductos(ProductosPK ppk) {
        facadePoa.setEntityClass(Productos.class);
        Productos nuevoProductos = (Productos) facadePoa.find(ppk);
        return nuevoProductos;
    }

}
