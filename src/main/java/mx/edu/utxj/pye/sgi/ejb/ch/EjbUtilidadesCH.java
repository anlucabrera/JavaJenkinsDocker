package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.Calendarioevaluacionpoa;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreas;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK;
import mx.edu.utxj.pye.sgi.entity.ch.Historicoplantillapersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.MenuDinamico;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.Reporteerrores;

@Local
public interface EjbUtilidadesCH {

////////////////////////////////////////////////////////////////////////////////Eventos
    public List<Eventos> mostrarEventoses() throws Throwable;

    public List<Eventos> mostrarEventosRegistro(String tipo, String nombre) throws Throwable;

    public Eventos actualizarEventoses(Eventos e) throws Throwable;

    public Eventos eliminarEventosesEventos(Eventos e) throws Throwable;

    public List<Procesopoa> mostrarProcesopoa() throws Throwable;

    public Procesopoa mostrarEtapaPOAArea(Short calveArea) throws Throwable;

    public Procesopoa mostrarEtapaPOAPersona(Integer responsable) throws Throwable;

    public Procesopoa actualizarEtapaPOA(Procesopoa procesopoa) throws Throwable;

    public Calendarioevaluacionpoa mostrarCalendarioEvaluacion(Date fecha) throws Throwable;
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
    public List<Bitacoraacceso> mostrarBitacoraacceso(String tabla, Date fechaI, Date fechaF) throws Throwable;

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

////////////////////////////////////////////////////////////////////////////////Menu
    public List<MenuDinamico> mostrarListaMenu(ListaPersonal personal, Integer nivel, String titulo, String tipoUsuario);

////////////////////////////////////////////////////////////////////////////////Errores
    public List<Reporteerrores> mostrarListaReporteerroreses();

    public Reporteerrores agregarReporteerroreses(Reporteerrores r);

    public Reporteerrores actualizarReporteerroreses(Reporteerrores r);

    public Reporteerrores eliminarReporteerroreses(Reporteerrores r);

}
