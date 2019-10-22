package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.ContactoEmergencias;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.shiro.User;

import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateless
public class ServiciosPersonal implements EjbPersonal {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB    Facade facade;
    @PostConstruct
    public void init(){
        em = facade.getEntityManager();
    }

////////////////////////////////////////////////////////////////////////////////Lista Personal
    @Override
    public List<ListaPersonal> mostrarListaDeEmpleados() throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l ORDER BY l.clave ASC", ListaPersonal.class);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarListaPersonalsPorParametros(String nombre, Integer tipo) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l", ListaPersonal.class);
        switch (tipo) {
            case 1:
                q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.nombre = :nombre", ListaPersonal.class);
                q.setParameter("nombre", nombre);
                break;
            case 2:
                q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.categoriaOperativaNombre = :categoriaOperativaNombre", ListaPersonal.class);
                q.setParameter("categoriaOperativaNombre", nombre);
                break;
            case 3:
                q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.actividadNombre = :actividadNombre", ListaPersonal.class);
                q.setParameter("actividadNombre", nombre);
                break;
            case 4:
                q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.areaOperativaNombre = :areaOperativaNombre OR l.areaSuperiorNombre = :areaSuperiorNombre", ListaPersonal.class);
                q.setParameter("areaOperativaNombre", nombre);
                q.setParameter("areaSuperiorNombre", nombre);
                break;
            default:
                q = em.createQuery("SELECT l FROM ListaPersonal l ORDER BY l.clave", ListaPersonal.class);
                break;
        }

        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarListaPersonalListSubordinados(ListaPersonal perosona) {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE (l.areaOperativa = :areaOperativa OR l.areaSuperior=:areaSuperior) AND l.clave!=:clave ORDER BY l.clave", ListaPersonal.class);
        q.setParameter("areaOperativa", perosona.getAreaOperativa());
        q.setParameter("areaSuperior", perosona.getAreaOperativa());
        q.setParameter("clave", perosona.getClave());
        List<ListaPersonal> pr = q.getResultList();
        if (pr.isEmpty() ) {
          pr=new ArrayList<>();
        } 
        return pr;
    }
    
    @Override
    public List<ListaPersonal> buscaCoincidenciasListaPersonal(String parametro) {
        try {
            return em.createQuery("SELECT l FROM ListaPersonal l WHERE l.nombre LIKE CONCAT('%',:parametro,'%' ) OR l.clave LIKE CONCAT('%',:parametro,'%' ) OR l.areaOperativaNombre LIKE CONCAT('%',:parametro,'%' )", ListaPersonal.class)
                    .setParameter("parametro", parametro)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }
    
////////////////////////////////////////////////////////////////////////////////Personal

    @Override
    public ListaPersonal mostrarListaPersonal(Integer claveTrabajador) throws Throwable {
        ListaPersonal pr = em.find(ListaPersonal.class, claveTrabajador);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }

    @Override
    public List<Personal> mostrarListaPersonalSubordinados(Short area,Integer claveTrabajador) throws Throwable {
        TypedQuery<Personal> q = em.createQuery("SELECT p FROM Personal p WHERE (p.areaOperativa = :areaOperativa OR p.areaSuperior=:areaSuperior) AND  p.status<>:status AND p.clave!=:clave ORDER BY p.clave", Personal.class);
        q.setParameter("status", 'B');
        q.setParameter("areaOperativa", area);
        q.setParameter("areaSuperior", area);
        q.setParameter("clave", claveTrabajador);
        List<Personal> pr = q.getResultList();
        if (pr.isEmpty() ) {
          pr=new ArrayList<>();
        } 
        return pr;
    }

    @Override
    public List<Personal> mostrarListaPersonalsPorEstatus(Integer estatus) throws Throwable {
        TypedQuery<Personal> q = em.createQuery("SELECT p FROM Personal p WHERE p.status=:status", Personal.class);
        switch (estatus) {
            case 0:
                q = em.createQuery("SELECT p FROM Personal p WHERE p.status=:status", Personal.class);
                break;
            case 1:
                q = em.createQuery("SELECT p FROM Personal p WHERE p.status <> :status", Personal.class);
                break;
        }
        q.setParameter("status", 'B');
        return q.getResultList();
    }

    @Override
    public Personal mostrarPersonalLogeado(Integer claveTrabajador) throws Throwable {
        Personal pr = em.find(Personal.class, claveTrabajador);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }

    @Override
    public Personal crearNuevoPersonal(Personal nuevoPersonal) throws Throwable {
        em.persist(nuevoPersonal);
        em.flush();
        return nuevoPersonal;
    }

    @Override
    public Personal actualizarPersonal(Personal nuevoPersonal) throws Throwable {
        em.merge(nuevoPersonal);
        return nuevoPersonal;
    }

////////////////////////////////////////////////////////////////////////////////Informacion Adicional Personal
    @Override
    public InformacionAdicionalPersonal mostrarInformacionAdicionalPersonalLogeado(Integer claveTrabajador) throws Throwable {
        InformacionAdicionalPersonal pr = em.find(InformacionAdicionalPersonal.class, claveTrabajador);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }

    @Override
    public InformacionAdicionalPersonal crearNuevoInformacionAdicionalPersonal(InformacionAdicionalPersonal nuevoInformacionAdicionalPersonal) throws Throwable {
        em.persist(nuevoInformacionAdicionalPersonal);
        em.flush();
        return nuevoInformacionAdicionalPersonal;
    }

    @Override
    public InformacionAdicionalPersonal actualizarInformacionAdicionalPersonal(InformacionAdicionalPersonal nuevoInformacionAdicionalPersonal) throws Throwable {
        em.merge(nuevoInformacionAdicionalPersonal);
        em.flush();
        return nuevoInformacionAdicionalPersonal;
    }

////////////////////////////////////////////////////////////////////////////////Contacto Emergencias    
    @Override
    public List<ContactoEmergencias> mostrarContactosEmergencias(Integer claveTrabajador) throws Throwable {
        TypedQuery<ContactoEmergencias> q = em.createQuery("SELECT c FROM ContactoEmergencias c INNER JOIN c.clavePersonal p WHERE p.clave=:clave ", ContactoEmergencias.class);
        q.setParameter("clave", claveTrabajador);
        List<ContactoEmergencias> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ContactoEmergencias> mostrarAllContactosEmergencias() throws Throwable {
        TypedQuery<ContactoEmergencias> q = em.createQuery("SELECT c FROM ContactoEmergencias ", ContactoEmergencias.class);
        List<ContactoEmergencias> pr = q.getResultList();
        return pr;
    }

    @Override
    public ContactoEmergencias crearContactosEmergencias(ContactoEmergencias ce) throws Throwable {
        em.persist(ce);
        em.flush();
        return ce;
    }

    @Override
    public ContactoEmergencias actualizarContactosEmergencias(ContactoEmergencias ce) throws Throwable {
        em.merge(ce);
        em.flush();
        return ce;
    }

    @Override
    public ContactoEmergencias eliminarContactosEmergencias(ContactoEmergencias ce) throws Throwable {
        em.remove(ce);
        em.flush();
        return ce;
    }

    
////////////////////////////////////////////////////////////////////////////////Datos de Acceso
     @Override
    public User getDatosUsuario(String clave) {
        TypedQuery<User> q = em.createQuery("SELECT u FROM User u WHERE u.claveNomina = :claveNomina", User.class);
        q.setParameter("claveNomina", clave);
        List<User> l = q.getResultList();

//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioLogin.getUsuarioPorLogin() l: " + l);
        if (l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }
    
    @Override
    public User crearUser(User user){
        em.persist(user);
        em.flush();
        return user;
    }

    @Override
    public User actualizarUser(User user){
        em.merge(user);
        em.flush();
        return user;
    }
    
    
////////////////////////////////////////////////////////////////////////////////Catalogos
    @Override
    public List<Docencias> mostrarListaDocencias(Integer claveTrabajador) throws Throwable {
        TypedQuery<Docencias> q = em.createQuery("SELECT d FROM Docencias d JOIN d.clavePersonal c where c.clave=:clave ORDER BY d.anio DESC ", Docencias.class);
        q.setParameter("clave", claveTrabajador);
        List<Docencias> pr = q.getResultList();
        return pr;
    }

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
}
