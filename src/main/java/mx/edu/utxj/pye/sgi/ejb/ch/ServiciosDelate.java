package mx.edu.utxj.pye.sgi.ejb.ch;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServiciosDelate implements EjbDelate {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;
/////////////////////////////////////////////////////////////////////////Habilidades\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public HabilidadesInformaticas eliminarHabilidadesInformaticas(HabilidadesInformaticas nuevoHabilidadesInformaticas) throws Throwable {
        facade.setEntityClass(HabilidadesInformaticas.class);
        facade.remove(nuevoHabilidadesInformaticas);
        facade.flush();
        return nuevoHabilidadesInformaticas;
    }

    @Override
    public Lenguas eliminarHabilidadesLenguas(Lenguas nuevoLenguas) throws Throwable {
        facade.setEntityClass(Lenguas.class);
        facade.remove(nuevoLenguas);
        facade.flush();
        return nuevoLenguas;
    }

    @Override
    public Idiomas eliminarHabilidadesIdiomas(Idiomas nuevoIdiomas) throws Throwable {
        facade.setEntityClass(Idiomas.class);
        facade.remove(nuevoIdiomas);
        facade.flush();
        return nuevoIdiomas;
    }
/////////////////////////////////////////////////////////////////////////Publicaciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public LibrosPub eliminarLibrosPublicados(LibrosPub nuevoLibrosPub) throws Throwable {
        facade.setEntityClass(LibrosPub.class);
        facade.remove(nuevoLibrosPub);
        facade.flush();
        return nuevoLibrosPub;
    }

    @Override
    public Memoriaspub eliminarMemoriaspubicados(Memoriaspub nuevoMemoriaspub) throws Throwable {
        facade.setEntityClass(Memoriaspub.class);
        facade.remove(nuevoMemoriaspub);
        facade.flush();
        return nuevoMemoriaspub;
    }

    @Override
    public Articulosp eliminarArticulospublicados(Articulosp nuevoArticulosp) throws Throwable {
        facade.setEntityClass(Articulosp.class);
        facade.remove(nuevoArticulosp);
        facade.flush();
        return nuevoArticulosp;
    }

    @Override
    public Investigaciones eliminarInvestigacion(Investigaciones nuevoInvestigacion) throws Throwable {
        facade.setEntityClass(Investigaciones.class);
        facade.remove(nuevoInvestigacion);
        facade.flush();
        return nuevoInvestigacion;
    }

    @Override
    public Congresos eliminarCongresos(Congresos nuevoCongresos) throws Throwable {
        facade.setEntityClass(Congresos.class);
        facade.remove(nuevoCongresos);
        facade.flush();
        return nuevoCongresos;
    }

    ///////////////////////////////////////////////////////////////////////////Educacion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public FormacionAcademica eliminarFormacionAcademica(FormacionAcademica nuevoFormacionAcademica) throws Throwable {
        facade.setEntityClass(FormacionAcademica.class);
        facade.remove(nuevoFormacionAcademica);
        facade.flush();
        return nuevoFormacionAcademica;
    }

    @Override
    public ExperienciasLaborales eliminarExperienciasLaborales(ExperienciasLaborales nuevoExperienciasLaborales) throws Throwable {
        facade.setEntityClass(ExperienciasLaborales.class);
        facade.remove(nuevoExperienciasLaborales);
        facade.flush();
        return nuevoExperienciasLaborales;
    }

    @Override
    public Capacitacionespersonal eliminarCapacitacionespersonal(Capacitacionespersonal nuevoCapacitacionespersonal) throws Throwable {
        facade.setEntityClass(Capacitacionespersonal.class);
        facade.remove(nuevoCapacitacionespersonal);
        facade.flush();
        return nuevoCapacitacionespersonal;
    }

/////////////////////////////////////////////////////////////////////////Distinciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public Distinciones eliminarDistinciones(Distinciones nuevoDistinciones) throws Throwable {
        facade.setEntityClass(Distinciones.class);
        facade.remove(nuevoDistinciones);
        facade.flush();
        return nuevoDistinciones;
    }

///////////////////////////////////////////////////////////////////////////Tecnologia\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public DesarrollosTecnologicos eliminarDesarrollosTecnologicos(DesarrollosTecnologicos nuevoDesarrollosTecnologicos) throws Throwable {
        facade.setEntityClass(DesarrollosTecnologicos.class);
        facade.remove(nuevoDesarrollosTecnologicos);
        facade.flush();
        return nuevoDesarrollosTecnologicos;
    }

    @Override
    public DesarrolloSoftware eliminarDesarrolloSoftware(DesarrolloSoftware nuevoDesarrolloSoftware) throws Throwable {
        facade.setEntityClass(DesarrolloSoftware.class);
        facade.remove(nuevoDesarrolloSoftware);
        facade.flush();
        return nuevoDesarrolloSoftware;
    }

    @Override
    public Innovaciones eliminarInnovaciones(Innovaciones nuevoInnovaciones) throws Throwable {
        facade.setEntityClass(Innovaciones.class);
        facade.remove(nuevoInnovaciones);
        facade.flush();
        return nuevoInnovaciones;
    }

///////////////////////////////////////////////////////////////////////////funciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    
    @Override
    public Funciones eliminaFunciones(Funciones nuevoFunciones) throws Throwable {
        facade.setEntityClass(Funciones.class);
        facade.remove(nuevoFunciones);
        facade.flush();
        return nuevoFunciones;
    }
}
