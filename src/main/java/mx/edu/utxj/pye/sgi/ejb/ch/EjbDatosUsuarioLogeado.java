package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.CursosModalidad;
import mx.edu.utxj.pye.sgi.entity.ch.CursosTipo;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.ch.Grados;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;

@Local
public interface EjbDatosUsuarioLogeado {

////////////////////////////////////////////////////////////////////////////////Prontuario /////////////////////////////////////////////////////////////////////////////  
////////////////////////////////////////////////////////////////////////////////Personal
    public Personal mostrarPersonalLogeado(Integer claveTrabajador) throws Throwable;

    public Personal crearNuevoPersonal(Personal nuevoPersonal) throws Throwable;

    public Personal actualizarPersonal(Personal nuevoPersonal) throws Throwable;

    ////////////////////////////////////////////////////////////////////////////////Elementos Generales Personal
    public List<Grados> mostrarListaGrados() throws Throwable;

    public List<CursosTipo> mostrarListaCursosTipo() throws Throwable;

    public List<CursosModalidad> mostrarListaCursosModalidad() throws Throwable;

    public List<Actividades> mostrarListaActividades() throws Throwable;

    public List<PersonalCategorias> mostrarListaPersonalCategorias() throws Throwable;

//    public List<Areas> mostrarListaAreas() throws Throwable;
////////////////////////////////////////////////////////////////////////////////capital humano//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////Informacion Personal
    public InformacionAdicionalPersonal mostrarInformacionAdicionalPersonalLogeado(Integer claveTrabajador) throws Throwable;

    public InformacionAdicionalPersonal crearNuevoInformacionAdicionalPersonal(InformacionAdicionalPersonal nuevoInformacionAdicionalPersonal) throws Throwable;

    public InformacionAdicionalPersonal actualizarInformacionAdicionalPersonal(InformacionAdicionalPersonal nuevoInformacionAdicionalPersonal) throws Throwable;

////////////////////////////////////////////////////////////////////////////////Elementos Específicos Personal Logeado
    public List<Funciones> mostrarListaFuncionesPersonalLogeado(Integer area, Short categoriaOperativa, Short categoriaEspecifica) throws Throwable;

    public List<Docencias> mostrarListaDocencias(Integer claveTrabajador) throws Throwable;

    public Bitacoraacceso crearBitacoraacceso(Bitacoraacceso nuevoBitacoraacceso) throws Throwable;
////////////////////////////////////////////////////////////////////////////////Elimandos//////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////Vista Lista Personal

    public List<Generos> mostrarListaGeneros() throws Throwable;

    public ListaPersonal mostrarVistaListaPersonalLogeado(Integer claveTrabajador) throws Throwable;

    public List<ListaPersonal> mostrarVistaListaPersonalLogeadoAreaOpe() throws Throwable;

    public List<ListaPersonal> mostrarVistaListaPersonalLogeadoAreaOfi() throws Throwable;

    public List<ListaPersonal> mostrarVistaListaPersonalLogeadoAreaSup() throws Throwable;
    
    public PersonalCategorias crearNuevoPersonalCategorias(PersonalCategorias nuevoPersonalCategorias) throws Throwable;
}
