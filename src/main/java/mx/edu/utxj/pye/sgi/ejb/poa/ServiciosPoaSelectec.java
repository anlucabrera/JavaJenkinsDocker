package mx.edu.utxj.pye.sgi.ejb.poa;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.poa.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.poa.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.poa.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.poa.RecursosActividad;
import mx.edu.utxj.pye.sgi.entity.poa.UnidadMedidas;

@Stateful
public class ServiciosPoaSelectec implements EjbPoaSelectec {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb-poa_ejb_1.0PU")
    private EntityManager em;

    @EJB
    FacadePoa facadePoa;

    @Override
    public List<ActividadesPoa> mostrarActividadesPoas() {
        facadePoa.setEntityClass(ActividadesPoa.class);
        return facadePoa.findAll();
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
    public RecursosActividad agregarRecursosActividad(RecursosActividad nuevoRecursosActividad) {
        facadePoa.setEntityClass(RecursosActividad.class);
        facadePoa.create(nuevoRecursosActividad);
        facadePoa.flush();
        return nuevoRecursosActividad;
    }

    @Override
    public RecursosActividad actualizaRecursosActividad(RecursosActividad nuevoRecursosActividad) {
        System.out.println("mx.edu.utxj.pye.sgi.ejb.poa.ServiciosPoaSelectec.actualizaRecursosActividad(1)");
        facadePoa.setEntityClass(RecursosActividad.class);
        System.out.println("mx.edu.utxj.pye.sgi.ejb.poa.ServiciosPoaSelectec.actualizaRecursosActividad(2)");
        facadePoa.edit(nuevoRecursosActividad);
        System.out.println("mx.edu.utxj.pye.sgi.ejb.poa.ServiciosPoaSelectec.actualizaRecursosActividad(3)");
        facadePoa.flush();
        System.out.println("mx.edu.utxj.pye.sgi.ejb.poa.ServiciosPoaSelectec.actualizaRecursosActividad(4)");
        return nuevoRecursosActividad;
    }

    @Override
    public ActividadesPoa eliminarActividadesPoa(ActividadesPoa nuevaActividadesPoa) {
        facadePoa.setEntityClass(ActividadesPoa.class);
        facadePoa.remove(nuevaActividadesPoa);
        facadePoa.flush();
        return nuevaActividadesPoa;
    }

    @Override
    public RecursosActividad eliminarRecursosActividad(RecursosActividad nuevoRecursosActividad) {
        facadePoa.setEntityClass(RecursosActividad.class);
        facadePoa.remove(nuevoRecursosActividad);
        facadePoa.flush();
        return nuevoRecursosActividad;
    }

    @Override
    public List<CuadroMandoIntegral> mostrarCuadroMandoIntegrals() {
        facadePoa.setEntityClass(CuadroMandoIntegral.class);
        return facadePoa.findAll();
    }

    @Override
    public List<UnidadMedidas> mostrarUnidadMedidases() {
        facadePoa.setEntityClass(UnidadMedidas.class);
        return facadePoa.findAll();
    }

    @Override
    public List<ProductosAreas> mostrarProductosAreases(Short clavearea, Short ejercicioFiscal) {
        TypedQuery<ProductosAreas> q = em.createQuery("SELECT p FROM ProductosAreas p WHERE p.area = :area AND p.productos.productosPK.ejercicioFiscal=:ejercicioFiscal", ProductosAreas.class);
        q.setParameter("area", clavearea);
        q.setParameter("ejercicioFiscal", ejercicioFiscal);
        List<ProductosAreas> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<RecursosActividad> mostrarRecursosActividads() {
        facadePoa.setEntityClass(RecursosActividad.class);
        return facadePoa.findAll();
    }

}
