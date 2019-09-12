package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Actividadesvarias;
import mx.edu.utxj.pye.sgi.entity.ch.Atividadesvariasplaneacionescuatrimestrales;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesCuatrimestrales;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesDetalles;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesLiberaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaTotalAlumnosCarreraPye;

@Local
public interface EjbPlaneacionCuatrimestral{

    /**
     * Consulta el evento de planeación cuatrimestral activa
     *
     * @return Datos del evento activo, en caso de no existir regresa null
     */
    public Eventos getEventoActivo();

    /**
     * Checa si el siguiente periodo cuatrimestral ya ha sido agregado o no, se
     * debe considerar que la planeación cuatrimestral se va a realizar en el
     * cuatrimestre inmediato anterior o en el primer mes del cuatrimestre que
     * se está planeando
     *
     * @param evento Evento de planeación activo
     * @return Regresa el periodo cuatrimestral a planear
     */
    public PeriodosEscolares verificarPeriodo(Eventos evento);

    /**
     * Verifica si el usuario logueado es un director de área académica.
     *
     * @param clave Clave del usuario logueado
     * @return Regresa los datos como trabajador en caso que el usuario sea un
     * director o null de lo contrario
     */
    public Personal getDirector(Integer clave);

    /**
     * Consulta los datos del secretario académico
     *
     * @return Regresa datos como trabajador del secretario academico
     */
    public Personal getSecretarioAcademico();

    /**
     * COnsulta los datos del jefe de personal
     *
     * @return Datos como trabajador del jefe de personal
     */
    public Personal getJefePersonal();

    /**
     * Obtiene desde la base de datos la lista del personal docente marcado como
     * activo en la plantilla del personal
     *
     * @return Lista de personal activo
     */
    public List<Personal> getPersonalDocenteActivo();

    /**
     * Filtra desde la plantilla docente activa, los docentes que colaboran en
     * el periodo cuatrimestral anterior con el director.
     *
     * @param docentes Lista de docentes activos en plantilla
     * @param director Datos del director que realiza la planeación
     * @return Lista de docentes que colaboran con el director.
     */
    public List<Personal> getPersonalDocenteColaborador(List<Personal> docentes, Personal director, Integer periodo);

    /**
     * Inicializa la lista de planeaciones cuatrimestrales de un director de
     * carrera con objetos transientes para posterior edición y persistencia
     *
     * @param colaboradores Lista de los colaboradores actuales del directos
     * @param periodo Periodo de la planeación
     * @param director Director al que se le genera su planeación
     * @return Lista de planeaciones en estado transiente
     */
    public List<PlaneacionesCuatrimestrales> inicializarPlaneaciones(List<Personal> colaboradores, PeriodosEscolares periodo, Personal director);

    /**
     * Agrega a un docente como colaborador
     *
     * @param colaboradores Lista de colaboradores actuales
     * @param director Director que realiza la planeación
     * @param docente Docente a agregar como colaborador
     * @param periodo Periodo del evento
     * @return
     */
    public PlaneacionesCuatrimestrales agregarDocente(List<Personal> colaboradores, Personal director, Personal docente, PeriodosEscolares periodo);

    /**
     * Elimina a un docente como colaborador del director
     *
     * @param colaboradores Lista de colaboradores actuales
     * @param planeaciones
     */
    public void eliminarDocente(List<Personal> colaboradores, PlaneacionesCuatrimestrales planeacion, List<PlaneacionesCuatrimestrales> planeaciones);

    /**
     * Valida los datos de la planeacion cuatrimestral
     *
     * @param planeacion Planeación a validar
     * @return Entrada de mapa con clave true si es correcta o false de lo
     * contrario incluyendo como valor de la entrada el motivo.
     */
    public Map.Entry<Boolean, String> validarPlaneacion(PlaneacionesCuatrimestrales planeacion);

    /**
     * Guarda la planeación cuatrimestral previamente validada.
     *
     * @param planeacion Planeación a guardar
     */
    public void guardarPlaneacion(PlaneacionesCuatrimestrales planeacion);

    /**
     * Valida la planeación como secretario académico o como jefa de personal
     *
     * @param validador
     */
    public void validar(Personal validador);

    /**
     * Consulta la lista de planeacion por director y periodo
     *
     * @param director Filtro por director
     * @param periodo Filtro por periodo
     * @return Lista de planeaciones por director y periodo
     */
    public List<PlaneacionesCuatrimestrales> getPlaneacionesPorDirectorPeriodo(Personal director, PeriodosEscolares periodo);

    /**
     * Consulta la lista de planeaciones por periodo y las clasifica por
     * director
     *
     * @param periodo Filtro por periodo
     * @return Lista de planeacion mapeadas por director
     */
    public Map<Personal, List<PlaneacionesCuatrimestrales>> getPlaneacionesPorPeriodo(PeriodosEscolares periodo);

    /**
     * Verifica si a un docente ya le asignaron la hora de Reunión de academia,
     * con el objetivo de impedir que se asigne mas de una vez.
     *
     * @param docente Docente a comprobar
     * @param periodo Periodo en el que se va a comprobar
     * @return Regresa un entrada de mapa
     * <True,Instancia de Planeación Cuatrimestral asignada> si ya se le asignó
     * o <False,Null> de lo contrario.
     */
    public Map.Entry<Boolean, PlaneacionesCuatrimestrales> detectarReunionAcademia(Personal docente, Personal director, PeriodosEscolares periodo);

    public void ordenarPlaneaciones(List<PlaneacionesCuatrimestrales> planeaciones);

    public List<PlaneacionesLiberaciones> buscarPlaneacionLiberada(Integer periodo, Integer director);

    public List<PlaneacionesLiberaciones> buscarPlaneacionesLiberadasParaValidarSecretarioAcademico(Integer periodo);

    public List<PlaneacionesLiberaciones> buscarPlaneacionesLiberadasParaValidarJefePersonal(Integer periodo);

    public void actualizarPlaneacionLiberada(PlaneacionesLiberaciones planaeacionLiberadaActualizada);

    public void agregarPlaneacionLiberada(PlaneacionesLiberaciones nuevaPlaneacionLiberada);

    public List<Actividadesvarias> buscarActividadesVarias();

    public List<Atividadesvariasplaneacionescuatrimestrales> buscarActividadesvariasplaneacionescuatrimestraleses(Integer planeacion);

    public Atividadesvariasplaneacionescuatrimestrales agregarAtividadesvariasplaneacionescuatrimestrales(Atividadesvariasplaneacionescuatrimestrales nuevaAtividadesvariasplaneacionescuatrimestrales);

    public Actividadesvarias agregarActividadesvarias(Actividadesvarias nuevaActividadesvarias);

    public void eliminarActividadVaria(Atividadesvariasplaneacionescuatrimestrales nuevaActividadesvarias);

    public void actualizarAtividadesvariasplaneacionescuatrimestrales(Atividadesvariasplaneacionescuatrimestrales nuevaActividadesvarias);

    public List<PlaneacionesCuatrimestrales> buscarPlaneacionesCuatrimestrales();

    public List<PlaneacionesDetalles> buscraPlaneacionesDetalles(Integer periodo, Integer director);

    public void actualizarPlaneacionesDetalles(PlaneacionesDetalles nuevaPlaneacionesDetalles);

    public void agregarPlaneacionesDetalles(PlaneacionesDetalles nuevaPlaneacionesDetalles);
    
    public List<VistaTotalAlumnosCarreraPye> mostraralumnosProximosAEstadia(String numeroNomina);
}
