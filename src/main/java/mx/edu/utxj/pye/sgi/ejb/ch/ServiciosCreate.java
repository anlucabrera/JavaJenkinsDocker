package mx.edu.utxj.pye.sgi.ejb.ch;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Categoriasespecificasfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Comentariosfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Historicoplantillapersonal;
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
public class ServiciosCreate implements EjbCreate {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;
/////////////////////////////////////////////////////////////////////////Personal\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public Personal agregarPersonal(Personal nuevoPersonal) throws Throwable {
        facade.setEntityClass(Personal.class);
        facade.create(nuevoPersonal);
        facade.flush();
        return nuevoPersonal;
    }
/////////////////////////////////////////////////////////////////////////Habilidades\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public HabilidadesInformaticas agregarHabilidadesInformaticas(HabilidadesInformaticas nuevoHabilidadesInformaticas) throws Throwable {
        facade.setEntityClass(HabilidadesInformaticas.class);
        facade.create(nuevoHabilidadesInformaticas);
        facade.flush();
        return nuevoHabilidadesInformaticas;
    }

    @Override
    public Lenguas agregarHabilidadesLenguas(Lenguas nuevoLenguas) throws Throwable {
        facade.setEntityClass(Lenguas.class);
        facade.create(nuevoLenguas);
        facade.flush();
        return nuevoLenguas;
    }

    @Override
    public Idiomas agregarHabilidadesIdiomas(Idiomas nuevoIdiomas) throws Throwable {
        facade.setEntityClass(Idiomas.class);
        facade.create(nuevoIdiomas);
        facade.flush();
        return nuevoIdiomas;
    }
/////////////////////////////////////////////////////////////////////////Publicaciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public LibrosPub agregarLibrosPublicados(LibrosPub nuevoLibrosPub) throws Throwable {
        facade.setEntityClass(LibrosPub.class);
        facade.create(nuevoLibrosPub);
        facade.flush();
        return nuevoLibrosPub;
    }

    @Override
    public Memoriaspub agregarMemoriaspubicados(Memoriaspub nuevoMemoriaspub) throws Throwable {
        facade.setEntityClass(Memoriaspub.class);
        facade.create(nuevoMemoriaspub);
        facade.flush();
        return nuevoMemoriaspub;
    }

    @Override
    public Articulosp agregarArticulospublicados(Articulosp nuevoArticulosp) throws Throwable {
        facade.setEntityClass(Articulosp.class);
        facade.create(nuevoArticulosp);
        facade.flush();
        return nuevoArticulosp;
    }

    @Override
    public Investigaciones agregarInvestigacion(Investigaciones nuevoInvestigacion) throws Throwable {
        facade.setEntityClass(Investigaciones.class);
        facade.create(nuevoInvestigacion);
        facade.flush();
        return nuevoInvestigacion;
    }

    @Override
    public Congresos agregarCongresos(Congresos nuevoCongresos) throws Throwable {
        facade.setEntityClass(Congresos.class);
        facade.create(nuevoCongresos);
        facade.flush();
        return nuevoCongresos;
    }

    ///////////////////////////////////////////////////////////////////////////Educacion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public FormacionAcademica agregarFormacionAcademica(FormacionAcademica nuevoFormacionAcademica) throws Throwable {
        facade.setEntityClass(FormacionAcademica.class);
        facade.create(nuevoFormacionAcademica);
        facade.flush();
        return nuevoFormacionAcademica;
    }

    @Override
    public ExperienciasLaborales agregarExperienciasLaborales(ExperienciasLaborales nuevoExperienciasLaborales) throws Throwable {
        facade.setEntityClass(ExperienciasLaborales.class);
        facade.create(nuevoExperienciasLaborales);
        facade.flush();
        return nuevoExperienciasLaborales;
    }

    @Override
    public Capacitacionespersonal agregarCapacitacionespersonal(Capacitacionespersonal nuevoCapacitacionespersonal) throws Throwable {
        facade.setEntityClass(Capacitacionespersonal.class);
        facade.create(nuevoCapacitacionespersonal);
        facade.flush();
        return nuevoCapacitacionespersonal;
    }

/////////////////////////////////////////////////////////////////////////Distinciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public Distinciones agregarDistinciones(Distinciones nuevoDistinciones) throws Throwable {
        facade.setEntityClass(Distinciones.class);
        facade.create(nuevoDistinciones);
        facade.flush();
        return nuevoDistinciones;
    }

///////////////////////////////////////////////////////////////////////////Tecnologia\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public DesarrollosTecnologicos agregarDesarrollosTecnologicos(DesarrollosTecnologicos nuevoDesarrollosTecnologicos) throws Throwable {
        facade.setEntityClass(DesarrollosTecnologicos.class);
        facade.create(nuevoDesarrollosTecnologicos);
        facade.flush();
        return nuevoDesarrollosTecnologicos;
    }

    @Override
    public DesarrolloSoftware agregarDesarrolloSoftware(DesarrolloSoftware nuevoDesarrolloSoftware) throws Throwable {
        facade.setEntityClass(DesarrolloSoftware.class);
        facade.create(nuevoDesarrolloSoftware);
        facade.flush();
        return nuevoDesarrolloSoftware;
    }

    @Override
    public Innovaciones agregarInnovaciones(Innovaciones nuevoInnovaciones) throws Throwable {
        facade.setEntityClass(Innovaciones.class);
        facade.create(nuevoInnovaciones);
        facade.flush();
        return nuevoInnovaciones;
    }

///////////////////////////////////////////////////////////////////////////notificaciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    
    @Override
    public Notificaciones agregarNotificacion(Notificaciones nuevoNotificaciones) throws Throwable {
        facade.setEntityClass(Notificaciones.class);
        facade.create(nuevoNotificaciones);
        facade.flush();
        return nuevoNotificaciones;
    }
///////////////////////////////////////////////////////////////////////////funciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    @Override
    public Funciones agregarFuncion(Funciones nuevaFunciones) throws Throwable {
        facade.setEntityClass(Funciones.class);
        facade.create(nuevaFunciones);
        facade.flush();
        return nuevaFunciones;
    }
///////////////////////////////////////////////////////////////////////////Incidencias\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    @Override
    public Incidencias agregarIncidencias(Incidencias nuevaIncidencias) throws Throwable {
        facade.setEntityClass(Incidencias.class);
        facade.create(nuevaIncidencias);
        facade.flush();
        return nuevaIncidencias;
    }

    @Override
    public Comentariosfunciones agregarComentariosfunciones(Comentariosfunciones nuevoComentariosfunciones) throws Throwable {
        facade.setEntityClass(Comentariosfunciones.class);
        facade.create(nuevoComentariosfunciones);
        facade.flush();
        return nuevoComentariosfunciones;
    }

    @Override
    public Categoriasespecificasfunciones agregarCategoriasespecificasfunciones(Categoriasespecificasfunciones nuevaCategoriasespecificasfunciones) throws Throwable {
        facade.setEntityClass(Categoriasespecificasfunciones.class);
        facade.create(nuevaCategoriasespecificasfunciones);
        facade.flush();
        return nuevaCategoriasespecificasfunciones;
    }

    @Override
    public InformacionAdicionalPersonal agregarInformacionAdicionalPersonal(InformacionAdicionalPersonal nuevaInformacionAdicionalPersonal) throws Throwable {
        facade.setEntityClass(InformacionAdicionalPersonal.class);
        facade.create(nuevaInformacionAdicionalPersonal);
        facade.flush();
        return nuevaInformacionAdicionalPersonal;
    }

    @Override
    public Bitacoraacceso agregarBitacoraacceso(Bitacoraacceso nuevaBitacoraacceso) throws Throwable {
        facade.setEntityClass(Bitacoraacceso.class);
        facade.create(nuevaBitacoraacceso);
        facade.flush();
        return nuevaBitacoraacceso;
    }
    
     @Override
    public Historicoplantillapersonal agregarHistoricoplantillapersonal(Historicoplantillapersonal nuevoHistoricoplantillapersonal) throws Throwable {
        facade.setEntityClass(Historicoplantillapersonal.class);
        facade.create(nuevoHistoricoplantillapersonal);
        facade.flush();
        return nuevoHistoricoplantillapersonal;
    }
}
