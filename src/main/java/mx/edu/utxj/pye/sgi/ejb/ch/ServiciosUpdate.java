package mx.edu.utxj.pye.sgi.ejb.ch;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Comentariosfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServiciosUpdate implements EjbUpdate {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;
/////////////////////////////////////////////////////////////////////////Habilidades\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public HabilidadesInformaticas actualizarHabilidadesInformaticas(HabilidadesInformaticas nuevoHabilidadesInformaticas) throws Throwable {
        facade.setEntityClass(HabilidadesInformaticas.class);
        facade.edit(nuevoHabilidadesInformaticas);
        return nuevoHabilidadesInformaticas;
    }

    @Override
    public Lenguas actualizarHabilidadesLengias(Lenguas nuevoLenguas) throws Throwable {
        facade.setEntityClass(Lenguas.class);
        facade.edit(nuevoLenguas);
        return nuevoLenguas;
    }

    @Override
    public Idiomas actualizarHabilidadesIdiomas(Idiomas nuevoIdiomas) throws Throwable {
        facade.setEntityClass(Idiomas.class);
        facade.edit(nuevoIdiomas);
        return nuevoIdiomas;
    }
/////////////////////////////////////////////////////////////////////////Publicaciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public LibrosPub actualizarLibrosPublicados(LibrosPub nuevoLibrosPub) throws Throwable {
        facade.setEntityClass(LibrosPub.class);
        facade.edit(nuevoLibrosPub);
        facade.flush();
        return nuevoLibrosPub;
    }

    @Override
    public Memoriaspub actualizarMemoriaspubicados(Memoriaspub nuevoMemoriaspub) throws Throwable {
        facade.setEntityClass(Memoriaspub.class);
        facade.edit(nuevoMemoriaspub);
        facade.flush();
        return nuevoMemoriaspub;
    }

    @Override
    public Articulosp actualizarArticulospublicados(Articulosp nuevoArticulosp) throws Throwable {
        facade.setEntityClass(Articulosp.class);
        facade.edit(nuevoArticulosp);
        facade.flush();
        return nuevoArticulosp;
    }

    @Override
    public Investigaciones actualizarInvestigacion(Investigaciones nuevoInvestigacion) throws Throwable {
        facade.setEntityClass(Investigaciones.class);
        facade.edit(nuevoInvestigacion);
        return nuevoInvestigacion;
    }

    @Override
    public Congresos actualizarCongresos(Congresos nuevoCongresos) throws Throwable {
        facade.setEntityClass(Congresos.class);
        facade.edit(nuevoCongresos);
        return nuevoCongresos;
    }

    ///////////////////////////////////////////////////////////////////////////Educacion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public FormacionAcademica actualizarFormacionAcademica(FormacionAcademica nuevoFormacionAcademica) throws Throwable {
        facade.setEntityClass(FormacionAcademica.class);
        facade.edit(nuevoFormacionAcademica);
        return nuevoFormacionAcademica;
    }

    @Override
    public ExperienciasLaborales actualizarExperienciasLaborales(ExperienciasLaborales nuevoExperienciasLaborales) throws Throwable {
        facade.setEntityClass(ExperienciasLaborales.class);
        facade.edit(nuevoExperienciasLaborales);
        return nuevoExperienciasLaborales;
    }

    @Override
    public Capacitacionespersonal actualizarCapacitacionespersonal(Capacitacionespersonal nuevoCapacitacionespersonal) throws Throwable {
        facade.setEntityClass(Capacitacionespersonal.class);
        facade.edit(nuevoCapacitacionespersonal);
        return nuevoCapacitacionespersonal;
    }

/////////////////////////////////////////////////////////////////////////Distinciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public Distinciones actualizarDistinciones(Distinciones nuevoDistinciones) throws Throwable {
        facade.setEntityClass(Distinciones.class);
        facade.edit(nuevoDistinciones);
        return nuevoDistinciones;
    }

///////////////////////////////////////////////////////////////////////////Tecnologia\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public DesarrollosTecnologicos actualizarDesarrollosTecnologicos(DesarrollosTecnologicos nuevoDesarrollosTecnologicos) throws Throwable {
        facade.setEntityClass(DesarrollosTecnologicos.class);
        facade.edit(nuevoDesarrollosTecnologicos);
        return nuevoDesarrollosTecnologicos;
    }

    @Override
    public DesarrolloSoftware actualizarDesarrolloSoftware(DesarrolloSoftware nuevoDesarrolloSoftware) throws Throwable {
        facade.setEntityClass(DesarrolloSoftware.class);
        facade.edit(nuevoDesarrolloSoftware);
        return nuevoDesarrolloSoftware;
    }

    @Override
    public Innovaciones actualizarInnovaciones(Innovaciones nuevoInnovaciones) throws Throwable {
        facade.setEntityClass(Innovaciones.class);
        facade.edit(nuevoInnovaciones);
        return nuevoInnovaciones;
    }
///////////////////////////////////////////////////////////////////////////funciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    @Override
    public Funciones actualizarFunciones(Funciones nuevaActualizacionFunciones) throws Throwable {
        facade.setEntityClass(Funciones.class);
        facade.edit(nuevaActualizacionFunciones);
        return nuevaActualizacionFunciones;
    }
///////////////////////////////////////////////////////////////////////////perfiladmin\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    @Override
    public Personal actualizarPersonal(Personal nuevaActualizacionPersonal) throws Throwable {
        facade.setEntityClass(Personal.class);
        facade.edit(nuevaActualizacionPersonal);
        return nuevaActualizacionPersonal;
    }
///////////////////////////////////////////////////////////////////////////perfil empleado\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public InformacionAdicionalPersonal actualizarInformacionAdicionalPersonal(InformacionAdicionalPersonal nuevaInformacionAdicionalPersonal) throws Throwable {
        facade.setEntityClass(InformacionAdicionalPersonal.class);
        facade.edit(nuevaInformacionAdicionalPersonal);
        return nuevaInformacionAdicionalPersonal;
    }

    @Override
    public Incidencias actualizarIncidencias(Incidencias nuevaIncidencias) throws Throwable {
        facade.setEntityClass(Incidencias.class);
        facade.edit(nuevaIncidencias);
        return nuevaIncidencias;
    }

    @Override
    public Notificaciones actualizarNotificaciones(Notificaciones nuevaNotificacion) throws Throwable {
        facade.setEntityClass(Notificaciones.class);
        facade.edit(nuevaNotificacion);
        return nuevaNotificacion;
    }

    @Override
    public Comentariosfunciones actualizarComentariosfunciones(Comentariosfunciones nuevoComentariosfunciones) throws Throwable {
        facade.setEntityClass(Comentariosfunciones.class);
        facade.edit(nuevoComentariosfunciones);
        return nuevoComentariosfunciones;
    }
}
