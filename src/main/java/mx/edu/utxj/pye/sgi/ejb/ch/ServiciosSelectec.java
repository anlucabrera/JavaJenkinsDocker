package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Categoriasespecificasfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Comentariosfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.CursosModalidad;
import mx.edu.utxj.pye.sgi.entity.ch.CursosPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.CursosTipo;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.ch.Grados;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Historicoplantillapersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServiciosSelectec implements EjbSelectec {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;
/////////////////////////////////////////////////////////////////////////Perfil Empleado\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public List<ListaPersonal> mostrarListaDeEmpleadosXClave(Integer clave) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.clave = :clave", ListaPersonal.class);
        q.setParameter("clave", clave);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarListaDeEmpleados() throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l ORDER BY l.clave", ListaPersonal.class);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
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

    @Override
    public List<ListaPersonal> mostrarListaDeEmpleadosParaJefes(Short area) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.areaOperativa = :areaOperativa OR l.areaSuperior=:areaSuperior ORDER BY l.clave", ListaPersonal.class);
        q.setParameter("areaOperativa", area);
        q.setParameter("areaSuperior", area);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Personal> mostrarListaDePersonalParaJefes(Short area) throws Throwable {
        TypedQuery<Personal> q = em.createQuery("SELECT p FROM Personal p WHERE p.areaOperativa = :areaOperativa OR p.areaSuperior=:areaSuperior AND  p.status <> :status ORDER BY p.clave", Personal.class);
        q.setParameter("status", 'B');
        q.setParameter("areaOperativa", area);
        q.setParameter("areaSuperior", area);
        List<Personal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<InformacionAdicionalPersonal> mostrarListaDeInformacionAdicionalPersonal(Integer clave) throws Throwable {
        TypedQuery<InformacionAdicionalPersonal> q = em.createQuery("SELECT i FROM InformacionAdicionalPersonal i WHERE i.clave = :clave", InformacionAdicionalPersonal.class);
        q.setParameter("clave", clave);
        List<InformacionAdicionalPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Personal> mostrarListaDeEmpleadosPorClave(Integer clave) throws Throwable {
        TypedQuery<Personal> q = em.createQuery("SELECT p FROM Personal p WHERE p.clave = :clave", Personal.class);
        q.setParameter("clave", clave);
        List<Personal> pr = q.getResultList();
        return pr;
    }

    @Override
    public Personal mostrarEmpleadosPorClave(Integer clave) throws Throwable {
        facade.setEntityClass(Personal.class);
        facade.find(clave);
        Personal pr = (Personal) facade.find(clave);
        return pr;
    }

    @Override
    public List<Funciones> mostrarListaDeFuncionesXAreaOperativo(Short area, Short categoria) throws Throwable {
        TypedQuery<Funciones> q = em.createQuery("SELECT f FROM Funciones f JOIN f.categoriaOperativa co WHERE f.areaOperativa = :areaOperativa AND co.categoria  <> :categoriaOperativa", Funciones.class);
        q.setParameter("areaOperativa", area);
        q.setParameter("categoriaOperativa", categoria);
        List<Funciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Funciones> mostrarListaDeFuncionesXAreaYPuestoOperativo(Short area, Short puesto, Short catEspecifica) throws Throwable {
        TypedQuery<Funciones> q = em.createQuery("SELECT f FROM Funciones f JOIN f.categoriaOperativa co JOIN f.categoriaEspesifica ce WHERE f.areaOperativa = :areaOperativa AND co.categoria = :categoria AND ce.categoriaEspecifica=:categoriaEspecifica", Funciones.class);
        q.setParameter("areaOperativa", area);
        q.setParameter("categoria", puesto);
        q.setParameter("categoriaEspecifica", catEspecifica);
        List<Funciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Funciones> mostrarListaDeFuncionesXAreaYPuestoEspecifico(Short area, Short puesto) throws Throwable {
        TypedQuery<Funciones> q = em.createQuery("SELECT f FROM Funciones f JOIN f.categoriaEspesifica co WHERE f.areaOperativa = :areaOperativa AND co.categoriaEspecifica = :categoriaEspecifica", Funciones.class);
        q.setParameter("areaOperativa", area);
        q.setParameter("categoriaEspecifica", puesto);
        List<Funciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Personal> mostrarListaDeEmpleadosTotalActivos() throws Throwable {
        TypedQuery<Personal> q = em.createQuery("SELECT p FROM Personal p WHERE p.status <> :status", Personal.class);
        q.setParameter("status", 'B');
        return q.getResultList();
    }

    @Override
    public List<Personal> mostrarListaDeEmpleadosBajas() throws Throwable {
        TypedQuery<Personal> q = em.createQuery("SELECT p FROM Personal p WHERE p.status=:status", Personal.class);
        q.setParameter("status", 'B');
        return q.getResultList();
    }

    @Override
    public List<Personal> mostrarListaDeEmpleadosTotal() throws Throwable {
        TypedQuery<Personal> q = em.createQuery("SELECT p FROM Personal p", Personal.class);
        return q.getResultList();
    }

    @Override
    public List<Notificaciones> mostrarListaDenotificacionesPorUsuario(Integer clave) throws Throwable {
        TypedQuery<Notificaciones> q = em.createQuery("SELECT n FROM Notificaciones n JOIN n.claveTDestino cd JOIN n.claveTRemitente cr WHERE cd.clave=:claveD OR cr.clave=:claveR ", Notificaciones.class);
        q.setParameter("claveD", clave);
        q.setParameter("claveR", clave);
        List<Notificaciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Notificaciones> mostrarListaDenotificacionesPorUsuariosyEstatus(Integer claveD, Integer claveR, Integer estaus) throws Throwable {
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
    public List<Generos> mostrarGeneros() throws Throwable {
        TypedQuery<Generos> q = em.createQuery("SELECT g FROM Generos g", Generos.class);
        List<Generos> pr = q.getResultList();
        return pr;
    }
/////////////////////////////////////////////////////////////////////////Perfil Admin\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    @Override
    public List<PersonalCategorias> mostrarListaDePersonalCategoriases() throws Throwable {
        TypedQuery<PersonalCategorias> q = em.createQuery("SELECT p FROM PersonalCategorias p WHERE p.tipo = :tipo ORDER BY p.nombre", PersonalCategorias.class);
        q.setParameter("tipo", "Genérica");
        List<PersonalCategorias> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Actividades> mostrarListaDeActividadeses() throws Throwable {
        TypedQuery<Actividades> q = em.createQuery("SELECT a FROM Actividades a", Actividades.class);
        List<Actividades> pr = q.getResultList();
        return pr;
    }
/////////////////////////////////////////////////////////////////////////Habilidades\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public List<HabilidadesInformaticas> mostrarHabilidadesInformaticasPorClaveTrabajador(Integer claveTrabajador) throws Throwable {
        TypedQuery<HabilidadesInformaticas> q = em.createQuery("SELECT h FROM HabilidadesInformaticas h JOIN h.clavePersonal cp WHERE cp.clave = :clave ORDER BY h.estatus DESC", HabilidadesInformaticas.class);
        q.setParameter("clave", claveTrabajador);
        List<HabilidadesInformaticas> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Lenguas> mostrarHabilidadesLengiasPorClaveTrabajador(Integer claveTrabajador) throws Throwable {
        TypedQuery<Lenguas> q = em.createQuery("SELECT l FROM Lenguas l JOIN l.clavePersonal cp WHERE cp.clave = :clave ORDER BY l.estatus DESC", Lenguas.class);
        q.setParameter("clave", claveTrabajador);
        List<Lenguas> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Idiomas> mostrarHabilidadesIdiomasPorClaveTrabajador(Integer claveTrabajador) throws Throwable {
        TypedQuery<Idiomas> q = em.createQuery("SELECT i FROM Idiomas i JOIN i.clavePersonal cp WHERE cp.clave = :clave ORDER BY i.estatus DESC", Idiomas.class);
        q.setParameter("clave", claveTrabajador);
        List<Idiomas> pr = q.getResultList();
        return pr;
    }
/////////////////////////////////////////////////////////////////////////Produccion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public List<Investigaciones> mostrarInvestigacionPorClaveTrabajador(Integer claveTrabajador) throws Throwable{
        TypedQuery<Investigaciones> q = em.createQuery("SELECT i FROM Investigaciones i WHERE i.clavePerosnal = :clavePerosnal", Investigaciones.class);
        q.setParameter("clavePerosnal", claveTrabajador);
        List<Investigaciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<LibrosPub> mostrarLibrosPublicados(Integer claveTrabajador) throws Throwable {
        TypedQuery<LibrosPub> q = em.createQuery("SELECT l FROM LibrosPub l JOIN l.clavePersonal cp WHERE cp.clave = :clave ORDER BY l.estatus DESC", LibrosPub.class);
        q.setParameter("clave", claveTrabajador);
        List<LibrosPub> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Memoriaspub> mostrarMemoriaspubicados(Integer claveTrabajador) throws Throwable {
        TypedQuery<Memoriaspub> q = em.createQuery("SELECT m FROM Memoriaspub m JOIN m.clavePersonal cp WHERE cp.clave = :clave ORDER BY m.estatus DESC", Memoriaspub.class);
        q.setParameter("clave", claveTrabajador);
        List<Memoriaspub> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Articulosp> mostrarArticulospublicados(Integer claveTrabajador) throws Throwable {
        TypedQuery<Articulosp> q = em.createQuery("SELECT a FROM Articulosp a JOIN a.clavePersonal cp WHERE cp.clave = :clave ORDER BY a.estatus DESC", Articulosp.class);
        q.setParameter("clave", claveTrabajador);
        List<Articulosp> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Congresos> mostrarCongresos(Integer claveTrabajador) throws Throwable {
        TypedQuery<Congresos> q = em.createQuery("SELECT c FROM Congresos c JOIN c.clavePersonal cp WHERE cp.clave = :clave ORDER BY c.estatus DESC", Congresos.class);
        q.setParameter("clave", claveTrabajador);
        List<Congresos> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarListaDeEmpleadosN(String nombre) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.nombre = :nombre", ListaPersonal.class);
        q.setParameter("nombre", nombre);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarListaDeEmpleadosAr(Short area) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.areaSuperior = :areaSuperior", ListaPersonal.class);
        q.setParameter("areaSuperior", area);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

///////////////////////////////////////////////////////////////////////////Educacion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public List<FormacionAcademica> mostrarFormacionAcademica(Integer claveTrabajador) throws Throwable {
        TypedQuery<FormacionAcademica> q = em.createQuery("SELECT f FROM FormacionAcademica f JOIN f.clavePersonal cp WHERE cp.clave = :clave ORDER BY f.estatus DESC", FormacionAcademica.class);
        q.setParameter("clave", claveTrabajador);
        List<FormacionAcademica> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ExperienciasLaborales> mostrarExperienciasLaborales(Integer claveTrabajador) throws Throwable {
        TypedQuery<ExperienciasLaborales> q = em.createQuery("SELECT e FROM ExperienciasLaborales e JOIN e.clavePersonal cp WHERE cp.clave = :clave ORDER BY e.estatus DESC", ExperienciasLaborales.class);
        q.setParameter("clave", claveTrabajador);
        List<ExperienciasLaborales> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Capacitacionespersonal> mostrarCapacitacionespersonal(Integer claveTrabajador) throws Throwable {
        TypedQuery<Capacitacionespersonal> q = em.createQuery("SELECT c FROM Capacitacionespersonal c JOIN c.clavePersonal cp WHERE cp.clave = :clave ORDER BY c.estatus DESC", Capacitacionespersonal.class);
        q.setParameter("clave", claveTrabajador);
        List<Capacitacionespersonal> pr = q.getResultList();
        return pr;
    }
/////////////////////////////////////////////////////////////////////////Distinciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public List<Distinciones> mostrarDistinciones(Integer claveTrabajador) throws Throwable {
        TypedQuery<Distinciones> q = em.createQuery("SELECT d FROM Distinciones d JOIN d.claveEmpleado cp WHERE cp.clave = :clave ORDER BY d.estatus DESC", Distinciones.class);
        q.setParameter("clave", claveTrabajador);
        List<Distinciones> pr = q.getResultList();
        return pr;
    }
///////////////////////////////////////////////////////////////ListaPersonalResultadosEvaluaciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    @Override
    public List<Incidencias> mostrarIncidencias(Integer clave) throws Throwable {
        TypedQuery<Incidencias> q = em.createQuery("SELECT i FROM Incidencias i JOIN i.clavePersonal cp WHERE cp.clave = :clave", Incidencias.class);
        q.setParameter("clave", clave);
        List<Incidencias> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Incidencias> mostrarIncidenciasPorEstatus(Integer clave, String estatus) throws Throwable {
        TypedQuery<Incidencias> q = em.createQuery("SELECT i FROM Incidencias i JOIN i.clavePersonal cp WHERE cp.clave = :clave AND i.estatus = :estatus", Incidencias.class);
        q.setParameter("clave", clave);
        q.setParameter("estatus", estatus);
        List<Incidencias> pr = q.getResultList();
        return pr;
    }

    ///////////////////////////////////////////////////////////////Notificaciones Multiples\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    
    @Override
    public List<ListaPersonal> mostrarListaPersonalPorCategoria(String categoria) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.categoriaOperativaNombre = :categoriaOperativaNombre", ListaPersonal.class);
        q.setParameter("categoriaOperativaNombre", categoria);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarListaPersonalPorActividad(String actividad) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.actividadNombre = :actividadNombre", ListaPersonal.class);
        q.setParameter("actividadNombre", actividad);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarListaPersonalPorAreaOpySu(String area) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.areaOperativaNombre = :areaOperativaNombre OR l.areaSuperiorNombre = :areaSuperiorNombre", ListaPersonal.class);
        q.setParameter("areaOperativaNombre", area);
        q.setParameter("areaSuperiorNombre", area);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }
// <editor-fold defaultstate="collapsed" desc="Estos son lo metodos que yo agregue">  
//////////////////////////////////////////////////////////////////////Distinciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public List<Distinciones> mostrarDistincionesPorClaveEmpleado(Integer claveTrabajador) throws Throwable {
        TypedQuery<Distinciones> q = em.createQuery("select d from Distinciones d JOIN d.claveEmpleado ce WHERE ce.clave = :clave", Distinciones.class);
        q.setParameter("clave", claveTrabajador);
        List<Distinciones> pr = q.getResultList();
        return pr;
    }

    //////////////////////////////////////////////////////////////CursosTipo\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\''
    @Override
    public List<CursosTipo> mostrarTipoCursos() throws Throwable {
        TypedQuery<CursosTipo> q = em.createQuery("select c from CursosTipo c ", CursosTipo.class);
        List<CursosTipo> pr = q.getResultList();
        return pr;
    }

///////////////////////////////////////////////////////////////////////TipoModalidades\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\''''
    @Override
    public List<CursosModalidad> mostrarModalidades() throws Throwable {
        TypedQuery<CursosModalidad> q = em.createQuery("select c from CursosModalidad c ", CursosModalidad.class);
        List<CursosModalidad> pr = q.getResultList();
        return pr;
    }

///////////////////////////////////////////////////////////////////////////GRADOS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public List<Grados> mostrarGrados() throws Throwable {
        TypedQuery<Grados> q = em.createQuery("SELECT g FROM Grados g", Grados.class);
        List<Grados> pr = q.getResultList();
        return pr;
    }

    ////////////////////////////////////////////////////////////////////////////////////Educacion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public List<FormacionAcademica> mostrarFormacionPorClaveTrabajador(Integer claveTrabajador) throws Throwable {
        TypedQuery<FormacionAcademica> q = em.createQuery("select f from FormacionAcademica f JOIN f.clavePersonal cp where cp.clave= :clave", FormacionAcademica.class);
        q.setParameter("clave", claveTrabajador);
        List<FormacionAcademica> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ExperienciasLaborales> mostrarExperienciasaboralesPorClavePersonal(Integer claveTrabajador) throws Throwable {
        TypedQuery<ExperienciasLaborales> q = em.createQuery("select e from ExperienciasLaborales e JOIN e.clavePersonal cp where cp.clave= :clave", ExperienciasLaborales.class);
        q.setParameter("clave", claveTrabajador);
        List<ExperienciasLaborales> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Capacitacionespersonal> mostrarCapacitacionesInternasPorClaveEmpleado(Integer claveTrabajador) throws Throwable {
        TypedQuery<Capacitacionespersonal> q = em.createQuery("select c from Capacitacionespersonal c JOIN c.clavePersonal cp where cp.clave= :clave AND c.tipoCapacitacion=:tipoCapacitacion", Capacitacionespersonal.class);
        q.setParameter("clave", claveTrabajador);
        q.setParameter("tipoCapacitacion", "Interna");
        List<Capacitacionespersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Capacitacionespersonal> mostrarCapacitacionesExternasPorClaveEmpleado(Integer claveTrabajador) throws Throwable {
        TypedQuery<Capacitacionespersonal> q = em.createQuery("select c from Capacitacionespersonal c JOIN c.clavePersonal cp where cp.clave= :clave AND c.tipoCapacitacion=:tipoCapacitacion", Capacitacionespersonal.class);
        q.setParameter("clave", claveTrabajador);
        q.setParameter("tipoCapacitacion", "Externa");
        List<Capacitacionespersonal> pr = q.getResultList();
        return pr;
    }

    /////////////////////////////////////////////////////////////////////////////////Tecnologia\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public List<DesarrollosTecnologicos> mostrarDesarrollosTecnologicos(Integer claveTrabajador) throws Throwable {
        TypedQuery<DesarrollosTecnologicos> q = em.createQuery("select d from DesarrollosTecnologicos d JOIN d.clavePersonal c where c.clave= :clave ORDER BY d.estatus DESC", DesarrollosTecnologicos.class);
        q.setParameter("clave", claveTrabajador);
        List<DesarrollosTecnologicos> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Innovaciones> mostrarInnovaciones(Integer claveTrabajador) throws Throwable {
        TypedQuery<Innovaciones> q = em.createQuery("select i from Innovaciones i JOIN i.clavePersonal c where c.clave= :clave ORDER BY i.estatus DESC", Innovaciones.class);
        q.setParameter("clave", claveTrabajador);
        List<Innovaciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<DesarrolloSoftware> mostrarDesarrollosSoftware(Integer claveTrabajador) throws Throwable {
        TypedQuery<DesarrolloSoftware> q = em.createQuery("select d from DesarrolloSoftware d JOIN d.clavePersonal c where c.clave=:clave ORDER BY d.estatus DESC", DesarrolloSoftware.class);
        q.setParameter("clave", claveTrabajador);
        List<DesarrolloSoftware> pr = q.getResultList();
        return pr;
    }

    // </editor-fold>
    ///////////////////////////////////////////////////////////////////////////////////////////////docencias\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public List<Docencias> mostrarDocencias(Integer claveTrabajador) throws Throwable {
        TypedQuery<Docencias> q = em.createQuery("SELECT d FROM Docencias d JOIN d.clavePersonal c where c.clave=:clave", Docencias.class);
        q.setParameter("clave", claveTrabajador);
        List<Docencias> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Comentariosfunciones> mostrarComentariosfunciones() throws Throwable {
        TypedQuery<Comentariosfunciones> q = em.createQuery("SELECT c FROM Comentariosfunciones c", Comentariosfunciones.class);
        List<Comentariosfunciones> pr = q.getResultList();
        return pr;
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

    @Override
    public List<CursosPersonal> mostrarCursosPersonal(Integer claveTrabajador) throws Throwable {
        TypedQuery<CursosPersonal> q = em.createQuery("SELECT c FROM CursosPersonal c JOIN c.clave cc WHERE cc.clave=:clave", CursosPersonal.class);
        q.setParameter("clave", claveTrabajador);
        List<CursosPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Modulosregistro> mostrarModulosregistro(String actividadUsuario) throws Throwable {
        TypedQuery<Modulosregistro> q = em.createQuery("SELECT m FROM Modulosregistro m WHERE m.actividadUsuario = :actividadUsuario", Modulosregistro.class);
        q.setParameter("actividadUsuario", actividadUsuario);
        List<Modulosregistro> pr = q.getResultList();
        return pr;
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
