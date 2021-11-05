package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.Calendarioevaluacionpoa;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreas;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK;
import mx.edu.utxj.pye.sgi.entity.ch.Historicoplantillapersonal;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.MenuDinamico;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.Permisosadminstracion;
import mx.edu.utxj.pye.sgi.entity.ch.Permisosevaluacionpoaex;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.Reporteerrores;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.omnifaces.util.Messages;

@Stateful
public class ServiciosUtilidadesCH implements EjbUtilidadesCH {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    List<Calendarioevaluacionpoa> calendarioevaluacionpoas = new ArrayList<>();

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

    @Override
    public List<Procesopoa> mostrarProcesopoa() throws Throwable {
        TypedQuery<Procesopoa> q = em.createQuery("SELECT p FROM Procesopoa p", Procesopoa.class);
        List<Procesopoa> pr = q.getResultList();
        List<Procesopoa> pr2=new ArrayList<>();
        pr.forEach((t) -> {
            if(t.getResponsable()!=null){
                pr2.add(t);
            }
        });        
        return pr2;
    }

    @Override
    public Procesopoa mostrarEtapaPOAArea(Short calveArea) throws Throwable {
        TypedQuery<Procesopoa> q = em.createQuery("SELECT p FROM Procesopoa p WHERE p.area=:area", Procesopoa.class);
        q.setParameter("area", calveArea);
        List<Procesopoa> pr = q.getResultList();
        if (pr.isEmpty()) {
            return null;
        } else {
            return pr.get(0);
        }
    }

    @Override
    public List<Procesopoa> mostrarEtapaPOAPersona(Integer responsable) throws Throwable {
        TypedQuery<Procesopoa> q = em.createQuery("SELECT p FROM Procesopoa p WHERE p.responsable=:responsable", Procesopoa.class);
        q.setParameter("responsable", responsable);
        List<Procesopoa> pr = q.getResultList();
        if (pr.isEmpty()) {
            return new ArrayList<>();
        } else {
            return pr;
        }
    }

    @Override
    public Procesopoa actualizarEtapaPOA(Procesopoa procesopoa) throws Throwable {
        facade.setEntityClass(Procesopoa.class);
        facade.edit(procesopoa);
        return procesopoa;
    }

    @Override
    public List<Calendarioevaluacionpoa> mostrarCalendarioevaluacionpoas() throws Throwable {
        facade.setEntityClass(Calendarioevaluacionpoa.class);
        List<Calendarioevaluacionpoa> es = facade.findAll();
        if (es.isEmpty()) {
            return null;
        } else {
            return es;
        }
    }
    
    @Override
    public List<Permisosevaluacionpoaex> mostrarPermisosEvaluacionExtemporaneaPOA(Date fecha,Procesopoa idP) throws Throwable {
        TypedQuery<Permisosevaluacionpoaex> q = em.createQuery("SELECT cp FROM Permisosevaluacionpoaex cp WHERE cp.procesoPOA.procesoPOA=:idProceso AND (:fecha BETWEEN cp.fechaApertura AND cp.fechaCierre)", Permisosevaluacionpoaex.class);
        q.setParameter("fecha", fecha);
        q.setParameter("idProceso", idP.getProcesoPOA());
        List<Permisosevaluacionpoaex> pr = q.getResultList();
        if (pr.isEmpty()) {
            return new ArrayList<>();
        } else {
            return pr;
        }
    }
    @Override
    public List<Calendarioevaluacionpoa> mostrarCalendaiosActivosAreaPOA(Date fecha, Procesopoa idP, Short area) {
        try {
            
            calendarioevaluacionpoas = new ArrayList<>();
            List<Calendarioevaluacionpoa> calendarios = new ArrayList<>();
            calendarios = mostrarCalendarioevaluacionpoas();
            calendarios.forEach((t) -> {
                if((fecha.after(t.getFechaInicio()) && fecha.before(t.getFechaFin())) || (fecha.equals(t.getFechaInicio()) || fecha.equals(t.getFechaFin()))){
                    calendarioevaluacionpoas.add(t);
                }
            });
            List<Permisosevaluacionpoaex> ps= new ArrayList<>();
            ps=mostrarPermisosEvaluacionExtemporaneaPOA(fecha, idP);
            if (!ps.isEmpty()) {
                ps.forEach((t) -> {
                    Calendarioevaluacionpoa c = new Calendarioevaluacionpoa();
                    c = t.getEvaluacionPOA();
                    c.setFechaFin(t.getFechaCierre());
                    c.setFechaInicio(t.getFechaCierre());
                    calendarioevaluacionpoas.add(c);
                });
            }     
            return calendarioevaluacionpoas;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());            
            return new ArrayList<>();
        }
    }

    @Override
    public Calendarioevaluacionpoa mostrarCalendarioEvaluacion(Date fecha) throws Throwable {
        TypedQuery<Calendarioevaluacionpoa> q = em.createQuery("SELECT cp FROM Calendarioevaluacionpoa cp WHERE :fecha BETWEEN cp.fechaInicio AND cp.fechaFin", Calendarioevaluacionpoa.class);
        q.setParameter("fecha", fecha);
        List<Calendarioevaluacionpoa> pr = q.getResultList();
        if (pr.isEmpty()) {
            return new Calendarioevaluacionpoa();
        } else {
            return pr.get(0);
        }
    }  
    
        
    @Override
    public List<Calendarioevaluacionpoa> mostrarCalendariosEvaluacionActivos(Date fecha) throws Throwable {
        TypedQuery<Calendarioevaluacionpoa> q = em.createQuery("SELECT cp FROM Calendarioevaluacionpoa cp WHERE :fecha BETWEEN cp.fechaInicio AND cp.fechaFin", Calendarioevaluacionpoa.class);
        q.setParameter("fecha", fecha);
        List<Calendarioevaluacionpoa> pr = q.getResultList();
        if (pr.isEmpty()) {
            return new ArrayList<>();
        } else {
            return pr;
        }
    }  
    
    ////////////////////////////////////////////////////////////////////////////////Eventos Áreas
    @Override
    public List<EventosAreas> mostrarEventosesAreases() throws Throwable {
        facade.setEntityClass(EventosAreas.class);
        List<EventosAreas> es = facade.findAll();
        if (es.isEmpty()) {
            return new ArrayList<>();
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
        if (pr.isEmpty()) {
            pr = new ArrayList<>();
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
    public List<Bitacoraacceso> mostrarBitacoraacceso(String tabla, Date fechaI, Date fechaF) throws Throwable {
        TypedQuery<Bitacoraacceso> q = em.createQuery("SELECT b FROM Bitacoraacceso b WHERE b.tabla=:tabla AND b.fechaHora BETWEEN :fechaI AND :frchaF", Bitacoraacceso.class);
        q.setParameter("tabla", tabla);
        q.setParameter("fechaI", fechaI);
        q.setParameter("frchaF", fechaF);
        List<Bitacoraacceso> pr = q.getResultList();
        if (pr.isEmpty()) {
            pr = new ArrayList<>();
        }
        return pr;
    }

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
////////////////////////////////////////////////////////////////////////////////Menu

    @Override
    public List<MenuDinamico> mostrarListaMenu(ListaPersonal personal, Integer nivel, String titulo, String tipoUsuario) {
        TypedQuery<MenuDinamico> q = em.createQuery("SELECT m FROM MenuDinamico m WHERE  (m.personas LIKE CONCAT('%-',:personas,'-%' ) OR m.areas LIKE CONCAT('%',:areas,'%' ) OR m.categorias LIKE CONCAT('%',:categorias,'%' ) OR m.actividades LIKE CONCAT('%',:actividades,'%' )) AND m.activo=:activo AND m.tipoUsuario=:tipoUsuario GROUP BY m.encabezado ORDER BY m.modulo", MenuDinamico.class);
        switch (nivel) {
            case 1:
                q = em.createQuery("SELECT m FROM MenuDinamico m WHERE  (m.personas LIKE CONCAT('%-',:personas,'-%' ) OR m.areas LIKE CONCAT('%',:areas,'%' ) OR m.categorias LIKE CONCAT('%',:categorias,'%' ) OR m.actividades LIKE CONCAT('%',:actividades,'%' )) AND m.activo=:activo AND m.tituloNivel1!='' AND m.tipoUsuario=:tipoUsuario AND m.encabezado=:titulo GROUP BY m.tituloNivel1 ORDER BY m.modulo", MenuDinamico.class);
                break;
            case 2:
                q = em.createQuery("SELECT m FROM MenuDinamico m WHERE  (m.personas LIKE CONCAT('%-',:personas,'-%' ) OR m.areas LIKE CONCAT('%',:areas,'%' ) OR m.categorias LIKE CONCAT('%',:categorias,'%' ) OR m.actividades LIKE CONCAT('%',:actividades,'%' )) AND m.activo=:activo AND m.tituloNivel2!='' AND m.tipoUsuario=:tipoUsuario AND m.tituloNivel1=:titulo GROUP BY m.tituloNivel2 ORDER BY m.modulo", MenuDinamico.class);
                break;
            case 3:
                q = em.createQuery("SELECT m FROM MenuDinamico m WHERE  (m.personas LIKE CONCAT('%-',:personas,'-%' ) OR m.areas LIKE CONCAT('%',:areas,'%' ) OR m.categorias LIKE CONCAT('%',:categorias,'%' ) OR m.actividades LIKE CONCAT('%',:actividades,'%' )) AND m.activo=:activo AND m.titulonivel3!='' AND m.tipoUsuario=:tipoUsuario AND m.tituloNivel2=:titulo GROUP BY m.titulonivel3 ORDER BY m.modulo", MenuDinamico.class);
                break;
            case 4:
                q = em.createQuery("SELECT m FROM MenuDinamico m WHERE  (m.personas LIKE CONCAT('%-',:personas,'-%' ) OR m.areas LIKE CONCAT('%',:areas,'%' ) OR m.categorias LIKE CONCAT('%',:categorias,'%' ) OR m.actividades LIKE CONCAT('%',:actividades,'%' )) AND m.activo=:activo AND m.titulonivel4!='' AND m.tipoUsuario=:tipoUsuario AND m.titulonivel3=:titulo GROUP BY m.titulonivel4 ORDER BY m.modulo", MenuDinamico.class);
                break;
            case 5:
                q = em.createQuery("SELECT m FROM MenuDinamico m WHERE  (m.personas LIKE CONCAT('%-',:personas,'-%' ) OR m.areas LIKE CONCAT('%',:areas,'%' ) OR m.categorias LIKE CONCAT('%',:categorias,'%' ) OR m.actividades LIKE CONCAT('%',:actividades,'%' )) AND m.activo=:activo AND m.titulonivel5!='' AND m.tipoUsuario=:tipoUsuario AND m.titulonivel4=:titulo GROUP BY m.titulonivel5 ORDER BY m.modulo", MenuDinamico.class);
                break;
        }
        q.setParameter("personas", String.valueOf(personal.getClave()));
        q.setParameter("areas", String.valueOf(personal.getAreaOperativa()));
        q.setParameter("categorias", String.valueOf(personal.getCategoriaOperativa()));
        q.setParameter("actividades", String.valueOf(personal.getActividad()));
        q.setParameter("activo", true);
        q.setParameter("tipoUsuario", tipoUsuario);
        if (nivel != 0) {
            q.setParameter("titulo", titulo);
        }
        
        List<MenuDinamico> pr = q.getResultList();
        if (pr.isEmpty()) {
            return new ArrayList<>();
        } else {
            return pr;
        }
    }
    
     @Override
    public List<MenuDinamico> mostrarListaTitulosMenu(ListaPersonal personal, Integer nivel, String titulo, String tipoUsuario) {
        TypedQuery<MenuDinamico> q = em.createQuery("SELECT m FROM MenuDinamico m WHERE  (m.personas LIKE CONCAT('%-',:personas,'-%' ) OR m.areas LIKE CONCAT('%',:areas,'%' ) OR m.categorias LIKE CONCAT('%',:categorias,'%' ) OR m.actividades LIKE CONCAT('%',:actividades,'%' )) AND m.activo=:activo AND m.tipoUsuario=:tipoUsuario AND m.tituloNivel1 !='' GROUP BY m.encabezado ORDER BY m.modulo", MenuDinamico.class);
        switch (nivel) {
            case 1:
                q = em.createQuery("SELECT m FROM MenuDinamico m WHERE  (m.personas LIKE CONCAT('%-',:personas,'-%' ) OR m.areas LIKE CONCAT('%',:areas,'%' ) OR m.categorias LIKE CONCAT('%',:categorias,'%' ) OR m.actividades LIKE CONCAT('%',:actividades,'%' )) AND m.activo=:activo AND m.tituloNivel1!='' AND m.tipoUsuario=:tipoUsuario AND m.encabezado=:titulo AND m.tituloNivel2 !='' GROUP BY m.tituloNivel1 ORDER BY m.modulo", MenuDinamico.class);
                break;
            case 2:
                q = em.createQuery("SELECT m FROM MenuDinamico m WHERE  (m.personas LIKE CONCAT('%-',:personas,'-%' ) OR m.areas LIKE CONCAT('%',:areas,'%' ) OR m.categorias LIKE CONCAT('%',:categorias,'%' ) OR m.actividades LIKE CONCAT('%',:actividades,'%' )) AND m.activo=:activo AND m.tituloNivel2!='' AND m.tipoUsuario=:tipoUsuario AND m.tituloNivel1=:titulo AND m.titulonivel3 !='' GROUP BY m.tituloNivel2 ORDER BY m.modulo", MenuDinamico.class);
                break;
            case 3:
                q = em.createQuery("SELECT m FROM MenuDinamico m WHERE  (m.personas LIKE CONCAT('%-',:personas,'-%' ) OR m.areas LIKE CONCAT('%',:areas,'%' ) OR m.categorias LIKE CONCAT('%',:categorias,'%' ) OR m.actividades LIKE CONCAT('%',:actividades,'%' )) AND m.activo=:activo AND m.titulonivel3!='' AND m.tipoUsuario=:tipoUsuario AND m.tituloNivel2=:titulo AND m.titulonivel4 !='' GROUP BY m.titulonivel3 ORDER BY m.modulo", MenuDinamico.class);
                break;
            case 4:
                q = em.createQuery("SELECT m FROM MenuDinamico m WHERE  (m.personas LIKE CONCAT('%-',:personas,'-%' ) OR m.areas LIKE CONCAT('%',:areas,'%' ) OR m.categorias LIKE CONCAT('%',:categorias,'%' ) OR m.actividades LIKE CONCAT('%',:actividades,'%' )) AND m.activo=:activo AND m.titulonivel4!='' AND m.tipoUsuario=:tipoUsuario AND m.titulonivel3=:titulo AND m.titulonivel5 !='' GROUP BY m.titulonivel4 ORDER BY m.modulo", MenuDinamico.class);
                break;
            case 5:
                q = em.createQuery("SELECT m FROM MenuDinamico m WHERE  (m.personas LIKE CONCAT('%-',:personas,'-%' ) OR m.areas LIKE CONCAT('%',:areas,'%' ) OR m.categorias LIKE CONCAT('%',:categorias,'%' ) OR m.actividades LIKE CONCAT('%',:actividades,'%' )) AND m.activo=:activo AND m.titulonivel5!='' AND m.tipoUsuario=:tipoUsuario AND m.titulonivel4=:titulo GROUP BY m.titulonivel5 ORDER BY m.modulo", MenuDinamico.class);
                break;
        }
        q.setParameter("personas", String.valueOf(personal.getClave()));
        q.setParameter("areas", String.valueOf(personal.getAreaOperativa()));
        q.setParameter("categorias", String.valueOf(personal.getCategoriaOperativa()));
        q.setParameter("actividades", String.valueOf(personal.getActividad()));
        q.setParameter("activo", true);
        q.setParameter("tipoUsuario", tipoUsuario);
        if (nivel != 0) {
            q.setParameter("titulo", titulo);
        }
        
        List<MenuDinamico> pr = q.getResultList();
        if (pr.isEmpty()) {
            return new ArrayList<>();
        } else {
            return pr;
        }
    }
    
    @Override
    public List<MenuDinamico> mostrarListaMenuDocumentacion(){
        TypedQuery<MenuDinamico> q = em.createQuery("SELECT m FROM MenuDinamico m WHERE m.activo=:activo AND m.tipoUsuario=:tipoUsuario AND m.tipoenlace=:tipoenlace ORDER BY m.modulo", MenuDinamico.class);
        q.setParameter("activo", true);
        q.setParameter("tipoUsuario", "Trabajador");
        q.setParameter("tipoenlace", "documento");
        List<MenuDinamico> pr = q.getResultList();
        if (pr.isEmpty()) {
            return new ArrayList<>();
        } else {
            return pr;
        }
    }
    
    @Override
    public MenuDinamico agregarMenuDocumentacion(MenuDinamico md) {
        facade.setEntityClass(MenuDinamico.class);
        facade.create(md);
        facade.flush();

        return md;
    }

    @Override
    public MenuDinamico actualizaMenuDocumentacion(MenuDinamico md) {
        facade.setEntityClass(MenuDinamico.class);
        facade.edit(md);
        facade.flush();

        return md;
    }
    
////////////////////////////////////////////////////////////////////////////////Errores
    @Override
    public List<Reporteerrores> mostrarListaReporteerroreses(){
        facade.setEntityClass(Reporteerrores.class);
        List<Reporteerrores> es = facade.findAll();
        if (es.isEmpty()) {
            return null;
        } else {
            return es;
        }
    }

    @Override
    public Reporteerrores agregarReporteerroreses(Reporteerrores r){
        facade.setEntityClass(Reporteerrores.class);
        facade.create(r);
        return r;
    }

    @Override
    public Reporteerrores actualizarReporteerroreses(Reporteerrores r){
        facade.setEntityClass(Reporteerrores.class);
        facade.edit(r);
        return r;
    }

    @Override
    public Reporteerrores eliminarReporteerroreses(Reporteerrores r){
        facade.setEntityClass(Reporteerrores.class);
        facade.remove(r);
        return r;
    }
}
