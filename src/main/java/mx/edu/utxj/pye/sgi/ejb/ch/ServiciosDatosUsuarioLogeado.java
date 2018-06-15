package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.CursosModalidad;
import mx.edu.utxj.pye.sgi.entity.ch.CursosTipo;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.ch.Grados;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
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
        facade.setEntityClass(Personal.class);
        facade.edit(nuevoPersonal);
        facade.flush();
        return nuevoPersonal;
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
    public List<Funciones> mostrarListaFuncionesPersonalLogeado(Integer area, Short categoriaOperativa, Short categoriaEspecifica) throws Throwable {
        TypedQuery<Funciones> q = em.createQuery("SELECT f FROM Funciones f JOIN f.categoriaOperativa co JOIN f.categoriaEspesifica ce WHERE f.areaOperativa = :areaOperativa AND co.categoria = :categoria AND ce.categoriaEspecifica=:categoriaEspecifica", Funciones.class);
        q.setParameter("areaOperativa", area);
        q.setParameter("categoria", categoriaOperativa);
        q.setParameter("categoriaEspecifica", categoriaEspecifica);
        List<Funciones> pr = q.getResultList();
        return pr;
    }

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
        TypedQuery<PersonalCategorias> q = em.createQuery("SELECT p FROM PersonalCategorias p WHERE p.tipo = :tipo", PersonalCategorias.class);
        q.setParameter("tipo", "Genérica");
        List<PersonalCategorias> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarVistaListaPersonalLogeadoAreaOpe() throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l GROUP BY l.areaOperativa ORDER BY l.areaOperativaNombre", ListaPersonal.class);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarVistaListaPersonalLogeadoAreaOfi() throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l GROUP BY l.areaOficial ORDER BY l.areaOficialNombre", ListaPersonal.class);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarVistaListaPersonalLogeadoAreaSup() throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l GROUP BY l.areaSuperior ORDER BY l.areaSuperiorNombre", ListaPersonal.class);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }
    
    @Override
    public PersonalCategorias crearNuevoPersonalCategorias(PersonalCategorias nuevoPersonalCategorias) throws Throwable {
        facade.setEntityClass(PersonalCategorias.class);
        facade.create(nuevoPersonalCategorias);
        facade.flush();
        return nuevoPersonalCategorias;
    }
}
