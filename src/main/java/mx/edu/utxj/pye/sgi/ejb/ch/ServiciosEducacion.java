package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.CursosModalidad;
import mx.edu.utxj.pye.sgi.entity.ch.CursosPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.CursosTipo;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Grados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.PersonalCapacitado;

import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServiciosEducacion implements EjbEducacion {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;
    
    Integer i = 800;

////////////////////////////////////////////////////////////////////////////////Formacion Academica
    @Override
    public List<FormacionAcademica> mostrarFormacionAcademica(Integer claveTrabajador) throws Throwable {
        TypedQuery<FormacionAcademica> q = em.createQuery("SELECT f FROM FormacionAcademica f JOIN f.clavePersonal cp WHERE cp.clave = :clave ORDER BY f.estatus DESC", FormacionAcademica.class);
        q.setParameter("clave", claveTrabajador);
        List<FormacionAcademica> pr = q.getResultList();
        return pr;
    }

    @Override
    public FormacionAcademica crearNuevoFormacionAcademica(FormacionAcademica nuevoFormacionAcademica) throws Throwable {
        facade.setEntityClass(FormacionAcademica.class);
        facade.create(nuevoFormacionAcademica);
        facade.flush();
        return nuevoFormacionAcademica;
    }

    @Override
    public FormacionAcademica actualizarFormacionAcademica(FormacionAcademica nuevoFormacionAcademica) throws Throwable {
        facade.setEntityClass(FormacionAcademica.class);
        facade.edit(nuevoFormacionAcademica);
        facade.flush();
        return nuevoFormacionAcademica;
    }

    @Override
    public FormacionAcademica eliminarFormacionAcademica(FormacionAcademica nuevoFormacionAcademica) throws Throwable {
        facade.setEntityClass(FormacionAcademica.class);
        facade.remove(nuevoFormacionAcademica);
        facade.flush();
        return nuevoFormacionAcademica;
    }
////////////////////////////////////////////////////////////////////////////////Experiencia Laboral

    @Override
    public List<ExperienciasLaborales> mostrarExperienciasLaborales(Integer claveTrabajador) throws Throwable {
        TypedQuery<ExperienciasLaborales> q = em.createQuery("select e from ExperienciasLaborales e JOIN e.clavePersonal cp where cp.clave= :clave ORDER BY e.estatus DESC", ExperienciasLaborales.class);
        q.setParameter("clave", claveTrabajador);
        List<ExperienciasLaborales> pr = q.getResultList();
        return pr;
    }
    
    @Override
    public ExperienciasLaborales mostrarExperienciasLabora(Integer id) throws Throwable {
        ExperienciasLaborales laborales = em.find(ExperienciasLaborales.class, id);
        return laborales;
    }

    @Override
    public ExperienciasLaborales crearNuevoExperienciasLaborales(ExperienciasLaborales nuevoExperienciasLaborales) throws Throwable {
        facade.setEntityClass(ExperienciasLaborales.class);
        facade.create(nuevoExperienciasLaborales);
        facade.flush();
        return nuevoExperienciasLaborales;
    }

    @Override
    public ExperienciasLaborales actualizarExperienciasLaborales(ExperienciasLaborales nuevoExperienciasLaborales) throws Throwable {
        facade.setEntityClass(ExperienciasLaborales.class);
        facade.edit(nuevoExperienciasLaborales);
        facade.flush();
        return nuevoExperienciasLaborales;
    }

    @Override
    public ExperienciasLaborales eliminarExperienciasLaborales(ExperienciasLaborales nuevoExperienciasLaborales) throws Throwable {
        facade.setEntityClass(ExperienciasLaborales.class);
        facade.remove(nuevoExperienciasLaborales);
        facade.flush();
        return nuevoExperienciasLaborales;
    }
////////////////////////////////////////////////////////////////////////////////Actualizacion Profecional

    @Override
    public List<Capacitacionespersonal> mostrarCapacitacionespersonal(Integer claveTrabajador) throws Throwable {
        TypedQuery<Capacitacionespersonal> q = em.createQuery("SELECT c FROM Capacitacionespersonal c JOIN c.clavePersonal cp where cp.clave= :clave ORDER BY c.estatus DESC", Capacitacionespersonal.class);
        q.setParameter("clave", claveTrabajador);
        List<Capacitacionespersonal> pr = q.getResultList();
        
        TypedQuery<PersonalCapacitado> p = em.createQuery("SELECT c FROM PersonalCapacitado c JOIN c.participantesPersonalCapacitadoList pc where pc.personal= :clave", PersonalCapacitado.class);
        p.setParameter("clave", claveTrabajador);
        List<PersonalCapacitado> pc = p.getResultList();
        i = 800;
        pc.forEach((t) -> {
            t.getParticipantesPersonalCapacitadoList().forEach((par) -> {
                if(claveTrabajador==par.getPersonal()){
                par.getRegistros().getEvidenciasList().forEach((ev) -> {
                    ev.getEvidenciasDetalleList().forEach((evd) -> {
                        String[] parts = t.getDuracion().split(":");
                        Capacitacionespersonal c = new Capacitacionespersonal();
                        c.setCursoClave(i);
                        c.setClavePersonal(new Personal(par.getPersonal()));
                        c.setNombre(t.getNombre());
                        c.setFechaInicio(t.getFechaInicial());
                        c.setFechaFin(t.getFechaFinal());
                        c.setDuracionHoras(duracion(parts[0]));
                        c.setDuracionMinutos(duracion(parts[1]));
                        c.setTipo(new CursosTipo(t.getTipo().getPercapTipo(), t.getTipo().getTipo()));
                        c.setModalidad(new CursosModalidad(t.getModalidad().getPercapMod(), t.getModalidad().getModalidad()));
                        c.setEmpresaImpartidora(t.getEmpresaImpartidora());
                        c.setObjetivo(t.getObjetivo());
                        c.setLugar(t.getLugarImparticion());
                        c.setEvidenciaCapacitacion(evd.getRuta());
                        c.setEstatus("Aceptado");
                        c.setTipoCapacitacion("Interna");
                        c.setCategoriaCapacitacion("profesional");
                        c.setRipppa("No Aplica");
                        pr.add(c);
                        i++;
                    });
                });
                }
            });
        });        
        return pr;
    }
    
    public Short duracion(String tim){
        Short tiempo=0;
        switch (tim){
            case "00": tiempo=Short.parseShort("0"); break;
            case "01": tiempo=Short.parseShort("1"); break;
            case "02": tiempo=Short.parseShort("2"); break;
            case "03": tiempo=Short.parseShort("3"); break;
            case "04": tiempo=Short.parseShort("4"); break;
            case "05": tiempo=Short.parseShort("5"); break;
            case "06": tiempo=Short.parseShort("6"); break;
            case "07": tiempo=Short.parseShort("7"); break;
            case "08": tiempo=Short.parseShort("8"); break;
            case "09": tiempo=Short.parseShort("9"); break;
        }
        return tiempo;
    }

    @Override
    public List<Capacitacionespersonal> mostrarCapacitacionespersonalTipo(Integer claveTrabajador, String Tipo) throws Throwable {
        TypedQuery<Capacitacionespersonal> q = em.createQuery("select c from Capacitacionespersonal c JOIN c.clavePersonal cp where cp.clave= :clave AND c.tipoCapacitacion=:tipoCapacitacion ORDER BY c.estatus DESC", Capacitacionespersonal.class);
        q.setParameter("clave", claveTrabajador);
        q.setParameter("tipoCapacitacion", Tipo);
        List<Capacitacionespersonal> pr = q.getResultList();
        return pr;
    }
    
    @Override
    public Capacitacionespersonal mostrarCapacitacionpersonal(Integer id) throws Throwable {
        Capacitacionespersonal capacitacionespersonal = em.find(Capacitacionespersonal.class, id);
        return capacitacionespersonal;
    }

    @Override
    public Capacitacionespersonal crearNuevoCapacitacionespersonal(Capacitacionespersonal nuevoCapacitacionespersonal) throws Throwable {
        facade.setEntityClass(Capacitacionespersonal.class);
        facade.create(nuevoCapacitacionespersonal);
        facade.flush();
        return nuevoCapacitacionespersonal;
    }

    @Override
    public Capacitacionespersonal actualizarCapacitacionespersonal(Capacitacionespersonal nuevoCapacitacionespersonal) throws Throwable {
        facade.setEntityClass(Capacitacionespersonal.class);
        facade.edit(nuevoCapacitacionespersonal);
        facade.flush();
        return nuevoCapacitacionespersonal;
    }

    @Override
    public Capacitacionespersonal eliminarCapacitacionespersonal(Capacitacionespersonal nuevoCapacitacionespersonal) throws Throwable {
        facade.setEntityClass(Capacitacionespersonal.class);
        facade.remove(nuevoCapacitacionespersonal);
        facade.flush();
        return nuevoCapacitacionespersonal;
    }

////////////////////////////////////////////////////////////////////////////////Cursos Personal    
    @Override
    public List<CursosPersonal> mostrarCursosPersonal(Integer claveTrabajador) throws Throwable {
        TypedQuery<CursosPersonal> q = em.createQuery("SELECT c FROM CursosPersonal c JOIN c.clave cc WHERE cc.clave=:clave", CursosPersonal.class);
        q.setParameter("clave", claveTrabajador);
        List<CursosPersonal> pr = q.getResultList();
        return pr;
    }
    ////////////////////////////////////////////////////////////////////////////////Catalogos

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

}
