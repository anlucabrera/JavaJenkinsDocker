package mx.edu.utxj.pye.sgi.ejb.ch;

import javax.ejb.Local;
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

@Local
public interface EjbCreate {
/////////////////////////////////////////////////////////////////////////Personal\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public Personal agregarPersonal(Personal nuevoPersonal) throws Throwable;
/////////////////////////////////////////////////////////////////////////Habilidades\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public HabilidadesInformaticas agregarHabilidadesInformaticas(HabilidadesInformaticas nuevoHabilidadesInformaticas) throws Throwable;

    public Lenguas agregarHabilidadesLenguas(Lenguas nuevoLenguas) throws Throwable;

    public Idiomas agregarHabilidadesIdiomas(Idiomas nuevoIdiomas) throws Throwable;
/////////////////////////////////////////////////////////////////////////Produccion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public LibrosPub agregarLibrosPublicados(LibrosPub nuevoLibrosPub) throws Throwable;

    public Memoriaspub agregarMemoriaspubicados(Memoriaspub nuevoMemoriaspub) throws Throwable;

    public Articulosp agregarArticulospublicados(Articulosp nuevoArticulosp) throws Throwable;

    public Investigaciones agregarInvestigacion(Investigaciones nuevoInvestigaciones) throws Throwable;

    public Congresos agregarCongresos(Congresos nuevoCongresos) throws Throwable;

///////////////////////////////////////////////////////////////////////////Educacion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public FormacionAcademica agregarFormacionAcademica(FormacionAcademica nuevoFormacionAcademica) throws Throwable;

    public ExperienciasLaborales agregarExperienciasLaborales(ExperienciasLaborales nuevoExperienciasLaborales) throws Throwable;

    public Capacitacionespersonal agregarCapacitacionespersonal(Capacitacionespersonal nuevoCapacitacionespersonal) throws Throwable;

/////////////////////////////////////////////////////////////////////////Distinciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public Distinciones agregarDistinciones(Distinciones nuevoDistinciones) throws Throwable;

///////////////////////////////////////////////////////////////////////////Tecnologia\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public DesarrollosTecnologicos agregarDesarrollosTecnologicos(DesarrollosTecnologicos nuevoDesarrollosTecnologicos) throws Throwable;

    public DesarrolloSoftware agregarDesarrolloSoftware(DesarrolloSoftware nuevoDesarrolloSoftware) throws Throwable;

    public Innovaciones agregarInnovaciones(Innovaciones nuevoInnovaciones) throws Throwable;
///////////////////////////////////////////////////////////////////////////notificaciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    public Notificaciones agregarNotificacion(Notificaciones nuevoNotificaciones) throws Throwable;
///////////////////////////////////////////////////////////////////////////funciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    public Funciones agregarFuncion(Funciones nuevaFunciones) throws Throwable;
///////////////////////////////////////////////////////////////////////////Incidencias\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    public Incidencias agregarIncidencias(Incidencias nuevaIncidencias) throws Throwable;

    public Comentariosfunciones agregarComentariosfunciones(Comentariosfunciones nuevoComentariosfunciones) throws Throwable;

    public Categoriasespecificasfunciones agregarCategoriasespecificasfunciones(Categoriasespecificasfunciones nuevaCategoriasespecificasfunciones) throws Throwable;

    public InformacionAdicionalPersonal agregarInformacionAdicionalPersonal(InformacionAdicionalPersonal nuevaInformacionAdicionalPersonal) throws Throwable;
    
    public Bitacoraacceso agregarBitacoraacceso(Bitacoraacceso nuevaBitacoraacceso) throws Throwable;
    
    public Historicoplantillapersonal agregarHistoricoplantillapersonal(Historicoplantillapersonal nuevoHistoricoplantillapersonal) throws Throwable;
}
