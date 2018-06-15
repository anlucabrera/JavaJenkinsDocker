package mx.edu.utxj.pye.sgi.ejb.ch;

import javax.ejb.Local;
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

@Local
public interface EjbUpdate {
/////////////////////////////////////////////////////////////////////////Habilidades\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    public HabilidadesInformaticas actualizarHabilidadesInformaticas(HabilidadesInformaticas nuevoHabilidadesInformaticas) throws Throwable;

    public Lenguas actualizarHabilidadesLengias(Lenguas nuevoLenguas) throws Throwable;

    public Idiomas actualizarHabilidadesIdiomas(Idiomas nuevoIdiomas) throws Throwable;

/////////////////////////////////////////////////////////////////////////Produccion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public LibrosPub actualizarLibrosPublicados(LibrosPub nuevoLibrosPub) throws Throwable;

    public Memoriaspub actualizarMemoriaspubicados(Memoriaspub nuevoMemoriaspub) throws Throwable;

    public Articulosp actualizarArticulospublicados(Articulosp nuevoArticulosp) throws Throwable;

    public Investigaciones actualizarInvestigacion(Investigaciones nuevoInvestigacion) throws Throwable;

    public Congresos actualizarCongresos(Congresos nuevoCongresos) throws Throwable;

///////////////////////////////////////////////////////////////////////////Educacion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public FormacionAcademica actualizarFormacionAcademica(FormacionAcademica nuevoFormacionAcademica) throws Throwable;

    public ExperienciasLaborales actualizarExperienciasLaborales(ExperienciasLaborales nuevoExperienciasLaborales) throws Throwable;

    public Capacitacionespersonal actualizarCapacitacionespersonal(Capacitacionespersonal nuevoCapacitacionespersonal) throws Throwable;

/////////////////////////////////////////////////////////////////////////Distinciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public Distinciones actualizarDistinciones(Distinciones nuevoDistinciones) throws Throwable;

///////////////////////////////////////////////////////////////////////////Tecnologia\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public DesarrollosTecnologicos actualizarDesarrollosTecnologicos(DesarrollosTecnologicos nuevoDesarrollosTecnologicos) throws Throwable;

    public DesarrolloSoftware actualizarDesarrolloSoftware(DesarrolloSoftware nuevoDesarrolloSoftware) throws Throwable;

    public Innovaciones actualizarInnovaciones(Innovaciones nuevoInnovaciones) throws Throwable;
///////////////////////////////////////////////////////////////////////////funciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public Funciones actualizarFunciones(Funciones nuevaActualizacionFunciones) throws Throwable;
///////////////////////////////////////////////////////////////////////////perfiladmin\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public Personal actualizarPersonal(Personal nuevaActualizacionPersonal) throws Throwable;

///////////////////////////////////////////////////////////////////////////perfil empleado\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public InformacionAdicionalPersonal actualizarInformacionAdicionalPersonal(InformacionAdicionalPersonal nuevaInformacionAdicionalPersonal) throws Throwable;

    public Incidencias actualizarIncidencias(Incidencias nuevaIncidencias) throws Throwable;

    public Notificaciones actualizarNotificaciones(Notificaciones nuevaNotificacion) throws Throwable;

    public Comentariosfunciones actualizarComentariosfunciones(Comentariosfunciones nuevoComentariosfunciones) throws Throwable;

}
