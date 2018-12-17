package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.CursosModalidad;
import mx.edu.utxj.pye.sgi.entity.ch.CursosTipo;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreas;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.ch.Grados;
import mx.edu.utxj.pye.sgi.entity.ch.Historicoplantillapersonal;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;

@Local
public interface EjbDatosUsuarioLogeado {

////////////////////////////////////////////////////////////////////////////////Personal
    public Personal mostrarPersonalLogeado(Integer claveTrabajador) throws Throwable;

    public Personal crearNuevoPersonal(Personal nuevoPersonal) throws Throwable;

    public Personal actualizarPersonal(Personal nuevoPersonal) throws Throwable;

    public ListaPersonal mostrarVistaListaPersonalLogeado(Integer claveTrabajador) throws Throwable;

    public List<ListaPersonal> mostrarListaSubordinados(ListaPersonal perosona);

    public InformacionAdicionalPersonal mostrarInformacionAdicionalPersonalLogeado(Integer claveTrabajador) throws Throwable;

    public InformacionAdicionalPersonal crearNuevoInformacionAdicionalPersonal(InformacionAdicionalPersonal nuevoInformacionAdicionalPersonal) throws Throwable;

    public InformacionAdicionalPersonal actualizarInformacionAdicionalPersonal(InformacionAdicionalPersonal nuevoInformacionAdicionalPersonal) throws Throwable;

////////////////////////////////////////////////////////////////////////////////Elementos Generales Personal
    public List<Grados> mostrarListaGrados() throws Throwable;

    public List<Generos> mostrarListaGeneros() throws Throwable;

    public List<CursosTipo> mostrarListaCursosTipo() throws Throwable;

    public List<CursosModalidad> mostrarListaCursosModalidad() throws Throwable;

    public List<Actividades> mostrarListaActividades() throws Throwable;

    public List<PersonalCategorias> mostrarListaPersonalCategorias() throws Throwable;

    public Integer mostrarListaPersonalCategoriasAreas(Short categoria, Short area) throws Throwable;

    public PersonalCategorias crearNuevoPersonalCategorias(PersonalCategorias nuevoPersonalCategorias) throws Throwable;

    public PersonalCategorias mostrarPersonalCategorias(Short cat) throws Throwable;

    public List<Docencias> mostrarListaDocencias(Integer claveTrabajador) throws Throwable;

    public Bitacoraacceso crearBitacoraacceso(Bitacoraacceso nuevoBitacoraacceso) throws Throwable;

    public List<Modulosregistro> mostrarModulosregistrosGeneral() throws Throwable;

    public Modulosregistro actualizarModulosregistro(Modulosregistro m) throws Throwable;

    public List<Modulosregistro> mostrarModulosregistro(String actividadUsuario) throws Throwable;

    public Modulosregistro mostrarModuloregistro(String nombre) throws Throwable;

    public List<Eventos> mostrarEventoses() throws Throwable;

    public List<EventosAreas> mostrarEventosesAreases() throws Throwable;

    public EventosAreas agregarEventosesAreases(EventosAreas ea) throws Throwable;

    public EventosAreas actualizarEventosesAreases(EventosAreas ea) throws Throwable;

    public Eventos actualizarEventoses(Eventos e) throws Throwable;

    public EventosAreas eliminarEventosesEventosAreas(EventosAreas ea) throws Throwable;

    public Eventos eliminarEventosesEventos(Eventos e) throws Throwable;

    public EventosAreas mostrarEventoAreas(EventosAreasPK areasPK);

    public List<Eventos> mostrarEventosRegistro(String tipo, String nombre) throws Throwable;

    public Historicoplantillapersonal agregarHistoricoplantillapersonal(Historicoplantillapersonal nuevoHistoricoplantillapersonal) throws Throwable;

    public List<Historicoplantillapersonal> mostrarHistoricoplantillapersonal() throws Throwable;
}
