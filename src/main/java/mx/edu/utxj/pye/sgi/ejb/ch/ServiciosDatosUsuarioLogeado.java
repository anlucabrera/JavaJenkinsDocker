package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.ContactoEmergencias;
import mx.edu.utxj.pye.sgi.entity.ch.CursosModalidad;
import mx.edu.utxj.pye.sgi.entity.ch.CursosTipo;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreas;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.ch.Grados;
import mx.edu.utxj.pye.sgi.entity.ch.Historicoplantillapersonal;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServiciosDatosUsuarioLogeado implements EjbDatosUsuarioLogeado {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;

////////////////////////////////////////////////////////////////////////////////Personal
    @Override
    public Personal mostrarPersonalLogeado(Integer claveTrabajador) throws Throwable {
        facade.setEntityClass(Personal.class);
        Personal pr = facade.getEntityManager().find(Personal.class, claveTrabajador);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }

    @Override
    public Personal crearNuevoPersonal(Personal nuevoPersonal) throws Throwable {
        facade.setEntityClass(Personal.class);
        facade.create(nuevoPersonal);
        facade.flush();
        return nuevoPersonal;
    }

    @Override
    public Personal actualizarPersonal(Personal nuevoPersonal) throws Throwable {
        System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosDatosUsuarioLogeado.actualizarPersonal(1)");
        facade.setEntityClass(Personal.class);
        System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosDatosUsuarioLogeado.actualizarPersonal(2)");
        facade.edit(nuevoPersonal);
        System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosDatosUsuarioLogeado.actualizarPersonal(3)");
        return nuevoPersonal;
    }

    @Override
    public List<ListaPersonal> mostrarListaSubordinados(ListaPersonal perosona) {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE (l.areaOperativa = :areaOperativa OR l.areaSuperior=:areaSuperior) AND l.clave!=:clave ORDER BY l.clave", ListaPersonal.class);
        q.setParameter("areaOperativa", perosona.getAreaOperativa());
        q.setParameter("areaSuperior", perosona.getAreaOperativa());
        q.setParameter("clave", perosona.getClave());
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }
////////////////////////////////////////////////////////////////////////////////Informacion Personal

    @Override
    public InformacionAdicionalPersonal mostrarInformacionAdicionalPersonalLogeado(Integer claveTrabajador) throws Throwable {
        facade.setEntityClass(InformacionAdicionalPersonal.class);
        InformacionAdicionalPersonal pr = facade.getEntityManager().find(InformacionAdicionalPersonal.class, claveTrabajador);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }

    @Override
    public InformacionAdicionalPersonal crearNuevoInformacionAdicionalPersonal(InformacionAdicionalPersonal nuevoInformacionAdicionalPersonal) throws Throwable {
        facade.setEntityClass(Personal.class);
        facade.create(nuevoInformacionAdicionalPersonal);
        facade.flush();
        return nuevoInformacionAdicionalPersonal;
    }

    @Override
    public InformacionAdicionalPersonal actualizarInformacionAdicionalPersonal(InformacionAdicionalPersonal nuevoInformacionAdicionalPersonal) throws Throwable {
        facade.setEntityClass(InformacionAdicionalPersonal.class);
        facade.edit(nuevoInformacionAdicionalPersonal);
        facade.flush();
        return nuevoInformacionAdicionalPersonal;
    }

    @Override
    public List<ContactoEmergencias> mostrarContactosEmergencias(Integer claveTrabajador) throws Throwable {
        TypedQuery<ContactoEmergencias> q = em.createQuery("SELECT c FROM ContactoEmergencias c INNER JOIN c.clavePersonal p WHERE p.clave=:clave ", ContactoEmergencias.class);
        q.setParameter("clave", claveTrabajador);
        List<ContactoEmergencias> pr = q.getResultList();
        return pr;
    }

    @Override
    public ContactoEmergencias crearContactosEmergencias(ContactoEmergencias ce) throws Throwable {
        facade.setEntityClass(ContactoEmergencias.class);
        facade.create(ce);
        facade.flush();
        return ce;

    }

    @Override
    public ContactoEmergencias actualizarContactosEmergencias(ContactoEmergencias ce) throws Throwable {
        facade.setEntityClass(ContactoEmergencias.class);
        facade.edit(ce);
        facade.flush();
        return ce;

    }

    @Override
    public ContactoEmergencias eliminarContactosEmergencias(ContactoEmergencias ce) throws Throwable {
        facade.setEntityClass(ContactoEmergencias.class);
        facade.remove(ce);
        facade.flush();
        return ce;

    }

    
////////////////////////////////////////////////////////////////////////////////Vista Lista Personal

    @Override
    public ListaPersonal mostrarVistaListaPersonalLogeado(Integer claveTrabajador) throws Throwable {
        facade.setEntityClass(ListaPersonal.class);
        ListaPersonal pr = facade.getEntityManager().find(ListaPersonal.class, claveTrabajador);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }
////////////////////////////////////////////////////////////////////////////////Elementos Generales Personal

    @Override
    public List<Generos> mostrarListaGeneros() throws Throwable {
        TypedQuery<Generos> q = em.createQuery("SELECT g FROM Generos g", Generos.class);
        List<Generos> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Actividades> mostrarListaActividades() throws Throwable {
        TypedQuery<Actividades> q = em.createQuery("SELECT a FROM Actividades a", Actividades.class);
        List<Actividades> pr = q.getResultList();
        return pr;
    }
////////////////////////////////////////////////////////////////////////////////Elementos Específicos Personal Logeado

    @Override
    public List<Docencias> mostrarListaDocencias(Integer claveTrabajador) throws Throwable {
        TypedQuery<Docencias> q = em.createQuery("SELECT d FROM Docencias d JOIN d.clavePersonal c where c.clave=:clave", Docencias.class);
        q.setParameter("clave", claveTrabajador);
        List<Docencias> pr = q.getResultList();
        return pr;
    }

    @Override
    public Bitacoraacceso crearBitacoraacceso(Bitacoraacceso nuevoBitacoraacceso) throws Throwable {
        facade.setEntityClass(Bitacoraacceso.class);
        facade.create(nuevoBitacoraacceso);
        facade.flush();
        return nuevoBitacoraacceso;
    }

    @Override
    public List<Grados> mostrarListaGrados() throws Throwable {
        TypedQuery<Grados> q = em.createQuery("SELECT g FROM Grados g", Grados.class);
        List<Grados> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<CursosTipo> mostrarListaCursosTipo() throws Throwable {
        TypedQuery<CursosTipo> q = em.createQuery("SELECT c FROM CursosTipo c", CursosTipo.class);
        List<CursosTipo> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<CursosModalidad> mostrarListaCursosModalidad() throws Throwable {
        TypedQuery<CursosModalidad> q = em.createQuery("SELECT c FROM CursosModalidad c", CursosModalidad.class);
        List<CursosModalidad> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<PersonalCategorias> mostrarListaPersonalCategorias() throws Throwable {
        TypedQuery<PersonalCategorias> q = em.createQuery("SELECT p FROM PersonalCategorias p", PersonalCategorias.class);
        List<PersonalCategorias> pr = q.getResultList();
        return pr;
    }

    @Override
    public Integer mostrarListaPersonalCategoriasAreas(Short categoria, Short area) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE  l.areaSuperior=:area AND l.categoriaOperativa=:categoria", ListaPersonal.class);
        q.setParameter("categoria", categoria);
        q.setParameter("area", area);
        List<ListaPersonal> pr = q.getResultList();
        return pr.size();
    }

    @Override
    public PersonalCategorias crearNuevoPersonalCategorias(PersonalCategorias nuevoPersonalCategorias) throws Throwable {
        facade.setEntityClass(PersonalCategorias.class);
        facade.create(nuevoPersonalCategorias);
        facade.flush();
        return nuevoPersonalCategorias;
    }

    @Override
    public PersonalCategorias mostrarPersonalCategorias(Short cat) throws Throwable {
        facade.setEntityClass(PersonalCategorias.class);
        PersonalCategorias pr = facade.getEntityManager().find(PersonalCategorias.class, cat);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }

    @Override
    public Historicoplantillapersonal agregarHistoricoplantillapersonal(Historicoplantillapersonal nuevoHistoricoplantillapersonal) throws Throwable {
        facade.setEntityClass(Historicoplantillapersonal.class);
        facade.create(nuevoHistoricoplantillapersonal);
        facade.flush();
        return nuevoHistoricoplantillapersonal;
    }

    @Override
    public List<Modulosregistro> mostrarModulosregistro(String actividadUsuario) throws Throwable {
        TypedQuery<Modulosregistro> q = em.createQuery("SELECT m FROM Modulosregistro m WHERE m.actividadUsuario = :actividadUsuario", Modulosregistro.class);
        q.setParameter("actividadUsuario", actividadUsuario);
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
    public List<Modulosregistro> mostrarModulosregistrosGeneral() throws Throwable {
        TypedQuery<Modulosregistro> q = em.createQuery("SELECT m FROM Modulosregistro m", Modulosregistro.class);
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
    public Eventos actualizarEventoses(Eventos e) throws Throwable {
        facade.setEntityClass(Eventos.class);
        facade.edit(e);
        return e;
    }

    @Override
    public EventosAreas eliminarEventosesEventosAreas(EventosAreas ea) throws Throwable {
        facade.setEntityClass(EventosAreas.class);
        facade.remove(ea);
        return ea;
    }

    @Override
    public Eventos eliminarEventosesEventos(Eventos e) throws Throwable {
        facade.setEntityClass(Eventos.class);
        facade.remove(e);
        return e;
    }

    @Override
    public EventosAreas mostrarEventoAreas(EventosAreasPK areasPK) {
        facade.setEntityClass(EventosAreas.class);
        EventosAreas es = (EventosAreas) facade.find(areasPK);
        return es;
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
    public List<Historicoplantillapersonal> mostrarHistoricoplantillapersonal() throws Throwable {
        TypedQuery<Historicoplantillapersonal> q = em.createQuery("SELECT h FROM Historicoplantillapersonal h", Historicoplantillapersonal.class);
        List<Historicoplantillapersonal> pr = q.getResultList();
        return pr;
    }

}
