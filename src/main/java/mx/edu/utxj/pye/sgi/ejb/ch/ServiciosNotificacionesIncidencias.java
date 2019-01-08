package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.Cuidados;
import mx.edu.utxj.pye.sgi.entity.ch.Incapacidad;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServiciosNotificacionesIncidencias implements EjbNotificacionesIncidencias {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;

    // ------------------------------------------------------------- Notificaciones -------------------------------------------------------------
    @Override
    public List<Notificaciones> mostrarListaDenotificacionesPorUsuario(Integer clave) throws Throwable {
        TypedQuery<Notificaciones> q = em.createQuery("SELECT n FROM Notificaciones n JOIN n.claveTDestino cd JOIN n.claveTRemitente cr WHERE cd.clave=:claveD OR cr.clave=:claveR ORDER BY n.fecha DESC ", Notificaciones.class);
        q.setParameter("claveD", clave);
        q.setParameter("claveR", clave);
        List<Notificaciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Notificaciones> mostrarListaDenotificacionesPorUsuariosyEstatus(Integer claveD, Integer claveR, Integer estaus) {
        TypedQuery<Notificaciones> q = em.createQuery("SELECT n FROM Notificaciones n JOIN n.claveTDestino cd JOIN n.claveTRemitente cr WHERE cd.clave=:claveD AND cr.clave=:claveR AND n.status=:status", Notificaciones.class);
        q.setParameter("claveD", claveD);
        q.setParameter("claveR", claveR);
        q.setParameter("status", estaus);
        List<Notificaciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Notificaciones> mostrarListaDenotificacionesPorUsuarios(Integer claveD, Integer estaus) throws Throwable {
        TypedQuery<Notificaciones> q = em.createQuery("SELECT n FROM Notificaciones n JOIN n.claveTDestino cd WHERE cd.clave=:claveD AND n.status=:status", Notificaciones.class);
        q.setParameter("claveD", claveD);
        q.setParameter("status", estaus);
        List<Notificaciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public Notificaciones agregarNotificacion(Notificaciones nuevoNotificaciones) throws Throwable {
        facade.setEntityClass(Notificaciones.class);
        facade.create(nuevoNotificaciones);
        facade.flush();
        return nuevoNotificaciones;
    }

    @Override
    public Notificaciones actualizarNotificaciones(Notificaciones nuevaNotificacion) {
        facade.setEntityClass(Notificaciones.class);
        facade.edit(nuevaNotificacion);
        facade.flush();
        return nuevaNotificacion;
    }

// ------------------------------------------------------------- Incidencias -------------------------------------------------------------
    @Override
    public List<Incidencias> mostrarIncidenciasTotales() throws Throwable {
        facade.setEntityClass(Incidencias.class);
        return facade.findAll();
    }

    @Override
    public List<Incidencias> mostrarIncidenciasArea(Short area) throws Throwable {
        TypedQuery<Incidencias> q = em.createQuery("SELECT i FROM Incidencias i JOIN i.clavePersonal cp WHERE cp.areaOperativa=:areaOperativa OR cp.areaSuperior=:areaSuperior", Incidencias.class);
        q.setParameter("areaOperativa", area);
        q.setParameter("areaSuperior", area);
        List<Incidencias> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Incidencias> mostrarIncidenciasReporte(Date fechaI, Date fechaF) throws Throwable {
        TypedQuery<Incidencias> q = em.createQuery("SELECT i FROM Incidencias i JOIN i.clavePersonal cp WHERE i.fecha BETWEEN :fechaI AND :frchaF ORDER BY cp.areaOperativa , cp.clave", Incidencias.class);
        q.setParameter("fechaI", fechaI);
        q.setParameter("frchaF", fechaF);
        List<Incidencias> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Incidencias> mostrarIncidencias(Integer clave) throws Throwable {
        TypedQuery<Incidencias> q = em.createQuery("SELECT i FROM Incidencias i JOIN i.clavePersonal cp WHERE cp.clave = :clave", Incidencias.class);
        q.setParameter("clave", clave);
        List<Incidencias> pr = q.getResultList();
        return pr;
    }

    @Override
    public Incidencias agregarIncidencias(Incidencias nuevaIncidencias) throws Throwable {
        facade.setEntityClass(Incidencias.class);
        facade.create(nuevaIncidencias);
        facade.flush();
        return nuevaIncidencias;
    }

    @Override
    public Incidencias actualizarIncidencias(Incidencias nuevaIncidencias) throws Throwable {
        facade.setEntityClass(Incidencias.class);
        facade.edit(nuevaIncidencias);
        facade.flush();
        return nuevaIncidencias;
    }

    @Override
    public Incidencias eliminarIncidencias(Incidencias nuevaIncidencias) throws Throwable {
        facade.setEntityClass(Incidencias.class);
        facade.remove(nuevaIncidencias);
        facade.flush();
        return nuevaIncidencias;
    }
// ------------------------------------------------------------- Incapacidad -------------------------------------------------------------

    @Override
    public List<Incapacidad> mostrarIncapacidadTotales() throws Throwable {
        facade.setEntityClass(Incapacidad.class);
        return facade.findAll();
    }

    @Override
    public List<Incapacidad> mostrarIncapacidadArea(Short area) throws Throwable {
        TypedQuery<Incapacidad> q = em.createQuery("SELECT i FROM Incapacidad i JOIN i.clavePersonal cp WHERE cp.areaOperativa=:areaOperativa OR cp.areaSuperior=:areaSuperior", Incapacidad.class);
        q.setParameter("areaOperativa", area);
        q.setParameter("areaSuperior", area);
        List<Incapacidad> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Incapacidad> mostrarIncapacidadReporte(Date fechaI, Date fechaF) throws Throwable {
        TypedQuery<Incapacidad> q = em.createQuery("SELECT i FROM Incapacidad i JOIN i.clavePersonal cp WHERE i.fechaInicio BETWEEN :fechaI AND :frchaF OR i.fechaFin BETWEEN :fechaI AND :frchaF ORDER BY cp.areaOperativa , cp.clave", Incapacidad.class);
        q.setParameter("fechaI", fechaI);
        q.setParameter("frchaF", fechaF);
        List<Incapacidad> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Incapacidad> mostrarIncapacidad(Integer clave) throws Throwable {
        TypedQuery<Incapacidad> q = em.createQuery("SELECT i FROM Incapacidad i JOIN i.clavePersonal cp WHERE cp.clave = :clave", Incapacidad.class);
        q.setParameter("clave", clave);
        List<Incapacidad> pr = q.getResultList();
        return pr;
    }

    @Override
    public Incapacidad agregarIncapacidad(Incapacidad nuevaIncapacidad) throws Throwable {
        facade.setEntityClass(Incapacidad.class);
        facade.create(nuevaIncapacidad);
        facade.flush();
        return nuevaIncapacidad;
    }

    @Override
    public Incapacidad actualizarIncapacidad(Incapacidad nuevaIncapacidad) throws Throwable {
        facade.setEntityClass(Incapacidad.class);
        facade.edit(nuevaIncapacidad);
        facade.flush();
        return nuevaIncapacidad;
    }

    @Override
    public Incapacidad eliminarIncapacidad(Incapacidad nuevaIncapacidad) throws Throwable {
        facade.setEntityClass(Incapacidad.class);
        facade.remove(nuevaIncapacidad);
        facade.flush();
        return nuevaIncapacidad;
    }
// ------------------------------------------------------------- Cuidados -------------------------------------------------------------

    @Override
    public List<Cuidados> mostrarCuidadosTotales() throws Throwable {
        facade.setEntityClass(Cuidados.class);
        return facade.findAll();
    }

    @Override
    public List<Cuidados> mostrarCuidadosArea(Short area) throws Throwable {
        TypedQuery<Cuidados> q = em.createQuery("SELECT c FROM Cuidados c JOIN c.personal cp WHERE cp.areaOperativa=:areaOperativa OR cp.areaSuperior=:areaSuperior", Cuidados.class);
        q.setParameter("areaOperativa", area);
        q.setParameter("areaSuperior", area);
        List<Cuidados> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Cuidados> mostrarCuidadosReporte(Date fechaI, Date fechaF) throws Throwable {
        TypedQuery<Cuidados> q = em.createQuery("SELECT c FROM Cuidados c JOIN c.personal cp WHERE c.fechaInicio BETWEEN :fechaI AND :frchaF OR c.fechaFin BETWEEN :fechaI AND :frchaF ORDER BY cp.areaOperativa , cp.clave", Cuidados.class);
        q.setParameter("fechaI", fechaI);
        q.setParameter("frchaF", fechaF);
        List<Cuidados> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Cuidados> mostrarCuidados(Integer clave) throws Throwable {
        TypedQuery<Cuidados> q = em.createQuery("SELECT c FROM Cuidados c JOIN c.personal cp WHERE cp.clave = :clave", Cuidados.class);
        q.setParameter("clave", clave);
        List<Cuidados> pr = q.getResultList();
        return pr;
    }

    @Override
    public Cuidados agregarCuidados(Cuidados nuevaCuidados) throws Throwable {
        facade.setEntityClass(Cuidados.class);
        facade.create(nuevaCuidados);
        facade.flush();
        return nuevaCuidados;
    }

    @Override
    public Cuidados actualizarCuidados(Cuidados nuevaCuidados) throws Throwable {
        facade.setEntityClass(Cuidados.class);
        facade.edit(nuevaCuidados);
        facade.flush();
        return nuevaCuidados;
    }

    @Override
    public Cuidados eliminarCuidados(Cuidados nuevaCuidados) throws Throwable {
        facade.setEntityClass(Cuidados.class);
        facade.remove(nuevaCuidados);
        facade.flush();
        return nuevaCuidados;
    }
}
