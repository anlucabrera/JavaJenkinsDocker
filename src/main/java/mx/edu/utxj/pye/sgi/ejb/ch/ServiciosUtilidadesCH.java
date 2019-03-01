package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreas;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK;
import mx.edu.utxj.pye.sgi.entity.ch.Historicoplantillapersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServiciosUtilidadesCH implements EjbUtilidadesCH {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;

    ////////////////////////////////////////////////////////////////////////////////Eventos
    @Override
    public List<Eventos> mostrarEventoses() throws Throwable {
        facade.setEntityClass(Eventos.class);
        List<Eventos> es = facade.findAll();
        if (es.isEmpty()) {
            return null;
        } else {
            return es;
        }
    }

    @Override
    public List<Eventos> mostrarEventosRegistro(String tipo, String nombre) throws Throwable {
        TypedQuery<Eventos> q = em.createQuery("SELECT e FROM Eventos e WHERE e.tipo = :tipo AND e.nombre = :nombre", Eventos.class);
        q.setParameter("tipo", tipo);
        q.setParameter("nombre", nombre);
        List<Eventos> pr = q.getResultList();
        return pr;
    }

    @Override
    public Eventos actualizarEventoses(Eventos e) throws Throwable {
        facade.setEntityClass(Eventos.class);
        facade.edit(e);
        return e;
    }

    @Override
    public Eventos eliminarEventosesEventos(Eventos e) throws Throwable {
        facade.setEntityClass(Eventos.class);
        facade.remove(e);
        return e;
    }

    ////////////////////////////////////////////////////////////////////////////////Eventos Áreas
    @Override
    public List<EventosAreas> mostrarEventosesAreases() throws Throwable {
        facade.setEntityClass(EventosAreas.class);
        List<EventosAreas> es = facade.findAll();
        if (es.isEmpty()) {
            return null;
        } else {
            return es;
        }
    }

    @Override
    public EventosAreas mostrarEventoAreas(EventosAreasPK areasPK) {
        facade.setEntityClass(EventosAreas.class);
        EventosAreas es = (EventosAreas) facade.find(areasPK);
        return es;
    }

    @Override
    public EventosAreas agregarEventosesAreases(EventosAreas ea) throws Throwable {
        facade.setEntityClass(EventosAreas.class);
        facade.create(ea);
        return ea;
    }

    @Override
    public EventosAreas actualizarEventosesAreases(EventosAreas ea) throws Throwable {
        facade.setEntityClass(EventosAreas.class);
        facade.edit(ea);
        return ea;
    }

    @Override
    public EventosAreas eliminarEventosesEventosAreas(EventosAreas ea) throws Throwable {
        facade.setEntityClass(EventosAreas.class);
        facade.remove(ea);
        return ea;
    }

    ////////////////////////////////////////////////////////////////////////////////Personal Categorias
    @Override
    public List<PersonalCategorias> mostrarListaPersonalCategorias() throws Throwable {
        TypedQuery<PersonalCategorias> q = em.createQuery("SELECT p FROM PersonalCategorias p", PersonalCategorias.class);
        List<PersonalCategorias> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<PersonalCategorias> mostrarListaPersonalCategoriasArea(Short area) throws Throwable {
        TypedQuery<PersonalCategorias> q = em.createQuery("SELECT c FROM Personal p INNER JOIN p.categoriaOperativa c WHERE (p.areaOperativa = :areaOperativa OR p.areaSuperior=:areaSuperior) GROUP BY c.categoria", PersonalCategorias.class);
        q.setParameter("areaOperativa", area);
        q.setParameter("areaSuperior", area);
        List<PersonalCategorias> pr = q.getResultList();
       if (pr.isEmpty() ) {
          pr=new ArrayList<>();
        } 
        return pr;
    }

    @Override
    public PersonalCategorias crearNuevoPersonalCategorias(PersonalCategorias nuevoPersonalCategorias) throws Throwable {
        facade.setEntityClass(PersonalCategorias.class);
        facade.create(nuevoPersonalCategorias);
        facade.flush();
        return nuevoPersonalCategorias;
    }

////////////////////////////////////////////////////////////////////////////////Bitacora Accesos
    @Override
    public Bitacoraacceso crearBitacoraacceso(Bitacoraacceso nuevoBitacoraacceso) throws Throwable {
        facade.setEntityClass(Bitacoraacceso.class);
        facade.create(nuevoBitacoraacceso);
        facade.flush();
        return nuevoBitacoraacceso;
    }

    ////////////////////////////////////////////////////////////////////////////////Modulos registro
    @Override
    public List<Modulosregistro> mostrarModulosregistrosGeneral() throws Throwable {
        TypedQuery<Modulosregistro> q = em.createQuery("SELECT m FROM Modulosregistro m", Modulosregistro.class);
        List<Modulosregistro> pr = q.getResultList();
        return pr;
    }

    @Override
    public Modulosregistro actualizarModulosregistro(Modulosregistro m) throws Throwable {
        facade.setEntityClass(Modulosregistro.class);
        facade.edit(m);
        return m;
    }

    @Override
    public List<Modulosregistro> mostrarModulosregistro(String actividadUsuario) throws Throwable {
        TypedQuery<Modulosregistro> q = em.createQuery("SELECT m FROM Modulosregistro m WHERE m.actividadUsuario = :actividadUsuario", Modulosregistro.class);
        q.setParameter("actividadUsuario", actividadUsuario);
        List<Modulosregistro> pr = q.getResultList();
        return pr;
    }

    @Override
    public Modulosregistro mostrarModuloregistro(String nombre) throws Throwable {
        TypedQuery<Modulosregistro> q = em.createQuery("SELECT m FROM Modulosregistro m WHERE m.nombre = :nombre", Modulosregistro.class);
        q.setParameter("nombre", nombre);
        List<Modulosregistro> pr = q.getResultList();
        if (!pr.isEmpty()) {
            return pr.get(0);
        } else {
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////Historico plantilla personal
    @Override
    public List<Historicoplantillapersonal> mostrarHistoricoplantillapersonal() throws Throwable {
        TypedQuery<Historicoplantillapersonal> q = em.createQuery("SELECT h FROM Historicoplantillapersonal h", Historicoplantillapersonal.class);
        List<Historicoplantillapersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public Historicoplantillapersonal agregarHistoricoplantillapersonal(Historicoplantillapersonal nuevoHistoricoplantillapersonal) throws Throwable {
        facade.setEntityClass(Historicoplantillapersonal.class);
        facade.create(nuevoHistoricoplantillapersonal);
        facade.flush();
        return nuevoHistoricoplantillapersonal;
    }

////////////////////////////////////////////////////////////////////////////////Elementos Generales Personal
    @Override
    public Integer mostrarListaPersonalCategoriasAreas(Short categoria, Short area) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE  l.areaSuperior=:area AND l.categoriaOperativa=:categoria", ListaPersonal.class);
        q.setParameter("categoria", categoria);
        q.setParameter("area", area);
        List<ListaPersonal> pr = q.getResultList();
        return pr.size();
    }

}
