package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Incapacidad;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;

@Local
public interface EjbNotificacionesIncidencias {
/////////////////////////////////////////////////////////////////////////Personal\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public List<Notificaciones> mostrarListaDenotificacionesPorUsuario(Integer clave) throws Throwable;

    public List<Notificaciones> mostrarListaDenotificacionesPorUsuariosyEstatus(Integer claveD, Integer claveR, Integer estaus) throws Throwable;

    public List<Notificaciones> mostrarListaDenotificacionesPorUsuarios(Integer claveD, Integer estaus) throws Throwable;

    public Notificaciones agregarNotificacion(Notificaciones nuevoNotificaciones) throws Throwable;

    public Notificaciones actualizarNotificaciones(Notificaciones nuevaNotificacion) throws Throwable;

    public List<Incidencias> mostrarIncidenciasTotales() throws Throwable;

    public List<Incidencias> mostrarIncidenciasReporte(Date fechaI, Date fechaF) throws Throwable;

    public List<Incidencias> mostrarIncidencias(Integer clave) throws Throwable;

    public Incidencias agregarIncidencias(Incidencias nuevaIncidencias) throws Throwable;

    public Incidencias actualizarIncidencias(Incidencias nuevaIncidencias) throws Throwable;

    public Incidencias eliminarIncidencias(Incidencias nuevaIncidencias) throws Throwable;

    public List<Incapacidad> mostrarIncapacidadTotales() throws Throwable;

    public List<Incapacidad> mostrarIncapacidadReporte(Date fechaI, Date fechaF) throws Throwable;

    public List<Incapacidad> mostrarIncapacidad(Integer clave) throws Throwable;

    public Incapacidad agregarIncapacidad(Incapacidad nuevaIncapacidad) throws Throwable;

    public Incapacidad actualizarIncapacidad(Incapacidad nuevaIncapacidad) throws Throwable;
}
