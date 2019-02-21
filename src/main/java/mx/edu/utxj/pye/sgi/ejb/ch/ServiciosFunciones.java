package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.Categoriasespecificasfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Comentariosfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServiciosFunciones implements EjbFunciones {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;

////////////////////////////////////////////////////////////////////////////////Funciones
    @Override
    public List<Funciones> mostrarFuncionesPorAreayPuesto(Short area, Short categoria, Integer tipo) throws Throwable {
        TypedQuery<Funciones> q = em.createQuery("SELECT f FROM Funciones f", Funciones.class);
        switch (tipo) {
            case 1:
                q = em.createQuery("SELECT f FROM Funciones f JOIN f.categoriaOperativa co WHERE f.areaOperativa = :areaOperativa AND co.categoria  <> :categoriaOperativa", Funciones.class);
                q.setParameter("categoriaOperativa", categoria);
                break;
            case 2:
                q = em.createQuery("SELECT f FROM Funciones f JOIN f.categoriaEspesifica co WHERE f.areaOperativa = :areaOperativa AND co.categoriaEspecifica = :categoriaEspecifica", Funciones.class);
                q.setParameter("categoriaEspecifica", categoria);
                break;
        }
        q.setParameter("areaOperativa", area);
        List<Funciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Funciones> mostrarListaFuncionesPersonalLogeado(Short area, Short categoriaOperativa, Short categoriaEspecifica) throws Throwable {
        TypedQuery<Funciones> q = em.createQuery("SELECT f FROM Funciones f JOIN f.categoriaOperativa co JOIN f.categoriaEspesifica ce WHERE f.areaOperativa = :areaOperativa AND co.categoria = :categoria AND ce.categoriaEspecifica=:categoriaEspecifica ORDER BY f.tipo DESC", Funciones.class);
        q.setParameter("areaOperativa", area);
        q.setParameter("categoria", categoriaOperativa);
        q.setParameter("categoriaEspecifica", categoriaEspecifica);
        List<Funciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public Funciones agregarFuncion(Funciones nuevaFunciones) throws Throwable {
        facade.setEntityClass(Funciones.class);
        facade.create(nuevaFunciones);
        facade.flush();
        return nuevaFunciones;
    }

    @Override
    public Funciones actualizarFunciones(Funciones nuevaActualizacionFunciones) throws Throwable {
        facade.setEntityClass(Funciones.class);
        facade.edit(nuevaActualizacionFunciones);
        return nuevaActualizacionFunciones;
    }

    @Override
    public Funciones eliminaFunciones(Funciones nuevoFunciones) throws Throwable {
        facade.setEntityClass(Funciones.class);
        facade.remove(nuevoFunciones);
        facade.flush();
        return nuevoFunciones;
    }

////////////////////////////////////////////////////////////////////////////////Comentarios funciones
    @Override
    public Comentariosfunciones agregarComentariosfunciones(Comentariosfunciones nuevoComentariosfunciones) throws Throwable {
        facade.setEntityClass(Comentariosfunciones.class);
        facade.create(nuevoComentariosfunciones);
        facade.flush();
        return nuevoComentariosfunciones;
    }

    @Override
    public Comentariosfunciones actualizarComentariosfunciones(Comentariosfunciones nuevoComentariosfunciones) throws Throwable {
        facade.setEntityClass(Comentariosfunciones.class);
        facade.edit(nuevoComentariosfunciones);
        return nuevoComentariosfunciones;
    }

    @Override
    public List<Comentariosfunciones> mostrarComentariosfunciones() throws Throwable {
        TypedQuery<Comentariosfunciones> q = em.createQuery("SELECT c FROM Comentariosfunciones c", Comentariosfunciones.class);
        List<Comentariosfunciones> pr = q.getResultList();
        return pr;
    }

////////////////////////////////////////////////////////////////////////////////Categorias especificas funciones
    @Override
    public Categoriasespecificasfunciones agregarCategoriasespecificasfunciones(Categoriasespecificasfunciones nuevaCategoriasespecificasfunciones) throws Throwable {
        facade.setEntityClass(Categoriasespecificasfunciones.class);
        facade.create(nuevaCategoriasespecificasfunciones);
        facade.flush();
        return nuevaCategoriasespecificasfunciones;
    }

    @Override
    public List<Categoriasespecificasfunciones> mostrarCategoriasespecificasfunciones(String nombre, Short area) throws Throwable {
        TypedQuery<Categoriasespecificasfunciones> q = em.createQuery("SELECT c FROM Categoriasespecificasfunciones c WHERE c.nombreCategoria=:nombreCategoria AND c.area=:area", Categoriasespecificasfunciones.class);
        q.setParameter("nombreCategoria", nombre);
        q.setParameter("area", area);
        List<Categoriasespecificasfunciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Categoriasespecificasfunciones> mostrarCategoriasespecificasfuncionesArea(Short area) throws Throwable {
        TypedQuery<Categoriasespecificasfunciones> q = em.createQuery("SELECT c FROM Categoriasespecificasfunciones c WHERE c.area=:area", Categoriasespecificasfunciones.class);
        q.setParameter("area", area);
        List<Categoriasespecificasfunciones> pr = q.getResultList();
        return pr;
    }

}
