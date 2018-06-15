package mx.edu.utxj.pye.sgi.ejb.ch;

import javax.ejb.Local;
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

@Local
public interface EjbDelate {
/////////////////////////////////////////////////////////////////////////Habilidades\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    public HabilidadesInformaticas eliminarHabilidadesInformaticas(HabilidadesInformaticas nuevoHabilidadesInformaticas) throws Throwable;

    public Lenguas eliminarHabilidadesLenguas(Lenguas nuevoLenguas) throws Throwable;

    public Idiomas eliminarHabilidadesIdiomas(Idiomas nuevoIdiomas) throws Throwable;

/////////////////////////////////////////////////////////////////////////Produccion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public LibrosPub eliminarLibrosPublicados(LibrosPub nuevoLibrosPub) throws Throwable;

    public Memoriaspub eliminarMemoriaspubicados(Memoriaspub nuevoMemoriaspub) throws Throwable;

    public Articulosp eliminarArticulospublicados(Articulosp nuevoArticulosp) throws Throwable;

    public Investigaciones eliminarInvestigacion(Investigaciones nuevoInvestigaciones) throws Throwable;

    public Congresos eliminarCongresos(Congresos nuevoCongresos) throws Throwable;

///////////////////////////////////////////////////////////////////////////Educacion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public FormacionAcademica eliminarFormacionAcademica(FormacionAcademica nuevoFormacionAcademica) throws Throwable;

    public ExperienciasLaborales eliminarExperienciasLaborales(ExperienciasLaborales nuevoExperienciasLaborales) throws Throwable;

    public Capacitacionespersonal eliminarCapacitacionespersonal(Capacitacionespersonal nuevoCapacitacionespersonal) throws Throwable;

/////////////////////////////////////////////////////////////////////////Distinciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public Distinciones eliminarDistinciones(Distinciones nuevoDistinciones) throws Throwable;

///////////////////////////////////////////////////////////////////////////Tecnologia\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public DesarrollosTecnologicos eliminarDesarrollosTecnologicos(DesarrollosTecnologicos nuevoDesarrollosTecnologicos) throws Throwable;

    public DesarrolloSoftware eliminarDesarrolloSoftware(DesarrolloSoftware nuevoDesarrolloSoftware) throws Throwable;

    public Innovaciones eliminarInnovaciones(Innovaciones nuevoInnovaciones) throws Throwable;

///////////////////////////////////////////////////////////////////////////funciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    
    public Funciones eliminaFunciones(Funciones nuevoFunciones) throws Throwable;
}
