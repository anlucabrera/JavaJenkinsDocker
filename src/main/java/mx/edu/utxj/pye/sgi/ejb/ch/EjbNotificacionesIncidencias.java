package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Cuidados;
import mx.edu.utxj.pye.sgi.entity.ch.Incapacidad;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;

@Local
public interface EjbNotificacionesIncidencias {
/////////////////////////////////////////////////////////////////////////Personal\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

// ------------------------------------------------------------- Notificaciones -------------------------------------------------------------
    public List<Notificaciones> mostrarListaDenotificacionesPorUsuario(Integer clave) throws Throwable;

    public List<Notificaciones> mostrarListaDenotificacionesPorUsuariosyEstatus(Integer claveD, Integer claveR, Integer estaus);

    public List<Notificaciones> mostrarListaDenotificacionesPorUsuarios(Integer claveD, Integer estaus) throws Throwable;

    public Notificaciones agregarNotificacion(Notificaciones nuevoNotificaciones) throws Throwable;

    public Notificaciones actualizarNotificaciones(Notificaciones nuevaNotificacion);

// ------------------------------------------------------------- Incidencias -------------------------------------------------------------
    public List<Incidencias> mostrarIncidenciasTotales() throws Throwable;

    public List<Incidencias> mostrarIncidenciasArea(Short area) throws Throwable;

    public List<Incidencias> mostrarIncidenciasReporte(Date fechaI, Date fechaF) throws Throwable;

    public List<Incidencias> mostrarIncidenciasReportePendientes(Date fechaI, Date fechaF, Short area, Integer claveP) throws Throwable;

    public List<Incidencias> mostrarIncidencias(Integer clave) throws Throwable;

    public Incidencias buscarIncidencias(Integer clave) throws Throwable;

    public Incidencias agregarIncidencias(Incidencias nuevaIncidencias) throws Throwable;

    public Incidencias actualizarIncidencias(Incidencias nuevaIncidencias) throws Throwable;

    public Incidencias eliminarIncidencias(Incidencias nuevaIncidencias) throws Throwable;
// ------------------------------------------------------------- Incapacidad -------------------------------------------------------------

    public List<Incapacidad> mostrarIncapacidadTotales() throws Throwable;

    public List<Incapacidad> mostrarIncapacidadArea(Short area) throws Throwable;

    public List<Incapacidad> mostrarIncapacidadReporte(Date fechaI, Date fechaF) throws Throwable;

    public List<Incapacidad> mostrarIncapacidad(Integer clave) throws Throwable;

    public Incapacidad agregarIncapacidad(Incapacidad nuevaIncapacidad) throws Throwable;

    public Incapacidad actualizarIncapacidad(Incapacidad nuevaIncapacidad) throws Throwable;

    public Incapacidad eliminarIncapacidad(Incapacidad nuevaIncapacidad) throws Throwable;
// ------------------------------------------------------------- Cuidados -------------------------------------------------------------

    public List<Cuidados> mostrarCuidadosTotales() throws Throwable;

    public List<Cuidados> mostrarCuidadosArea(Short area) throws Throwable;

    public List<Cuidados> mostrarCuidadosReporte(Date fechaI, Date fechaF) throws Throwable;

    public List<Cuidados> mostrarCuidados(Integer clave) throws Throwable;

    public Cuidados agregarCuidados(Cuidados nuevaCuidados) throws Throwable;

    public Cuidados actualizarCuidados(Cuidados nuevaCuidados) throws Throwable;

    public Cuidados eliminarCuidados(Cuidados nuevaCuidados) throws Throwable;
}
