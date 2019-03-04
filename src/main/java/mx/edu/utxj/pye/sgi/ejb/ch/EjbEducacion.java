package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.CursosModalidad;
import mx.edu.utxj.pye.sgi.entity.ch.CursosPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.CursosTipo;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Grados;

@Local
public interface EjbEducacion {

////////////////////////////////////////////////////////////////////////////////Formacion Academica
    public List<FormacionAcademica> mostrarFormacionAcademica(Integer claveTrabajador) throws Throwable;

    public FormacionAcademica crearNuevoFormacionAcademica(FormacionAcademica nuevoFormacionAcademica) throws Throwable;

    public FormacionAcademica actualizarFormacionAcademica(FormacionAcademica nuevoFormacionAcademica) throws Throwable;

    public FormacionAcademica eliminarFormacionAcademica(FormacionAcademica nuevoFormacionAcademica) throws Throwable;
////////////////////////////////////////////////////////////////////////////////Experiencia Laboral

    public List<ExperienciasLaborales> mostrarExperienciasLaborales(Integer claveTrabajador) throws Throwable;

    public ExperienciasLaborales crearNuevoExperienciasLaborales(ExperienciasLaborales nuevoExperienciasLaborales) throws Throwable;

    public ExperienciasLaborales actualizarExperienciasLaborales(ExperienciasLaborales nuevoExperienciasLaborales) throws Throwable;

    public ExperienciasLaborales eliminarExperienciasLaborales(ExperienciasLaborales nuevoExperienciasLaborales) throws Throwable;
////////////////////////////////////////////////////////////////////////////////Actualizacion Profecional

    public List<Capacitacionespersonal> mostrarCapacitacionespersonal(Integer claveTrabajador) throws Throwable;

    public List<Capacitacionespersonal> mostrarCapacitacionespersonalTipo(Integer claveTrabajador, String tipo) throws Throwable;

    public Capacitacionespersonal crearNuevoCapacitacionespersonal(Capacitacionespersonal nuevoCapacitacionespersonal) throws Throwable;

    public Capacitacionespersonal actualizarCapacitacionespersonal(Capacitacionespersonal nuevoCapacitacionespersonal) throws Throwable;

    public Capacitacionespersonal eliminarCapacitacionespersonal(Capacitacionespersonal nuevoCapacitacionespersonal) throws Throwable;

////////////////////////////////////////////////////////////////////////////////Cursos Personal
    public List<CursosPersonal> mostrarCursosPersonal(Integer claveTrabajador) throws Throwable;

    ////////////////////////////////////////////////////////////////////////////////Catalogos
    public List<Grados> mostrarListaGrados() throws Throwable;

    public List<CursosTipo> mostrarListaCursosTipo() throws Throwable;

    public List<CursosModalidad> mostrarListaCursosModalidad() throws Throwable;
}
