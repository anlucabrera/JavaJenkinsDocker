package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreas;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK;
import mx.edu.utxj.pye.sgi.entity.ch.Historicoplantillapersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;

@Local
public interface EjbUtilidadesCH {

////////////////////////////////////////////////////////////////////////////////Eventos
    public List<Eventos> mostrarEventoses() throws Throwable;

    public List<Eventos> mostrarEventosRegistro(String tipo, String nombre) throws Throwable;

    public Eventos actualizarEventoses(Eventos e) throws Throwable;

    public Eventos eliminarEventosesEventos(Eventos e) throws Throwable;

////////////////////////////////////////////////////////////////////////////////Eventos Áreas
    public List<EventosAreas> mostrarEventosesAreases() throws Throwable;

    public EventosAreas mostrarEventoAreas(EventosAreasPK areasPK);

    public EventosAreas agregarEventosesAreases(EventosAreas ea) throws Throwable;

    public EventosAreas actualizarEventosesAreases(EventosAreas ea) throws Throwable;

    public EventosAreas eliminarEventosesEventosAreas(EventosAreas ea) throws Throwable;

////////////////////////////////////////////////////////////////////////////////Personal Categorias
    public List<PersonalCategorias> mostrarListaPersonalCategorias() throws Throwable;

    public List<PersonalCategorias> mostrarListaPersonalCategoriasArea(Short area) throws Throwable;

    public PersonalCategorias crearNuevoPersonalCategorias(PersonalCategorias nuevoPersonalCategorias) throws Throwable;

////////////////////////////////////////////////////////////////////////////////Bitacora Accesos
    public List<Bitacoraacceso> mostrarBitacoraacceso(String tabla) throws Throwable;

    public Bitacoraacceso crearBitacoraacceso(Bitacoraacceso nuevoBitacoraacceso) throws Throwable;

////////////////////////////////////////////////////////////////////////////////Modulos registro
    public List<Modulosregistro> mostrarModulosregistrosGeneral() throws Throwable;

    public Modulosregistro actualizarModulosregistro(Modulosregistro m) throws Throwable;

    public List<Modulosregistro> mostrarModulosregistro(String actividadUsuario) throws Throwable;

    public Modulosregistro mostrarModuloregistro(String nombre) throws Throwable;

////////////////////////////////////////////////////////////////////////////////Historico plantilla personal
    public List<Historicoplantillapersonal> mostrarHistoricoplantillapersonal() throws Throwable;

    public Historicoplantillapersonal agregarHistoricoplantillapersonal(Historicoplantillapersonal nuevoHistoricoplantillapersonal) throws Throwable;

////////////////////////////////////////////////////////////////////////////////Elementos Generales Personal
    public Integer mostrarListaPersonalCategoriasAreas(Short categoria, Short area) throws Throwable;

}
