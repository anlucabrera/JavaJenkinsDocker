package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Categoriasespecificasfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Comentariosfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.CursosModalidad;
import mx.edu.utxj.pye.sgi.entity.ch.CursosPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.CursosTipo;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.ch.Grados;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Historicoplantillapersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;

@Local
public interface EjbSelectec {
/////////////////////////////////////////////////////////////////////////Perfil Empleado\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public List<ListaPersonal> mostrarListaDeEmpleadosXClave(Integer clave) throws Throwable;

    public List<ListaPersonal> mostrarListaDeEmpleados() throws Throwable;

    public List<ListaPersonal> mostrarListaSubordinados(ListaPersonal perosona);

    public List<ListaPersonal> mostrarListaDeEmpleadosParaJefes(Short area) throws Throwable;

    public List<Personal> mostrarListaDePersonalParaJefes(Short area) throws Throwable;

    public List<ListaPersonal> mostrarListaDeEmpleadosN(String nombre) throws Throwable;

    public List<ListaPersonal> mostrarListaDeEmpleadosAr(Short area) throws Throwable;

    public List<Personal> mostrarListaDeEmpleadosPorClave(Integer clave) throws Throwable;

    public Personal mostrarEmpleadosPorClave(Integer clave) throws Throwable;

    public List<Personal> mostrarListaDeEmpleadosTotalActivos() throws Throwable;

    public List<Personal> mostrarListaDeEmpleadosBajas() throws Throwable;

    public List<Personal> mostrarListaDeEmpleadosTotal() throws Throwable;

    public List<InformacionAdicionalPersonal> mostrarListaDeInformacionAdicionalPersonal(Integer clave) throws Throwable;

    public List<Funciones> mostrarListaDeFuncionesXAreaOperativo(Short area, Short categoria) throws Throwable;

    public List<Funciones> mostrarListaDeFuncionesXAreaYPuestoOperativo(Short area, Short puesto, Short catEspecifica) throws Throwable;

    public List<Funciones> mostrarListaDeFuncionesXAreaYPuestoEspecifico(Short area, Short puesto) throws Throwable;

    public List<Notificaciones> mostrarListaDenotificacionesPorUsuario(Integer clave) throws Throwable;

    public List<Notificaciones> mostrarListaDenotificacionesPorUsuariosyEstatus(Integer claveD, Integer claveR, Integer estaus) throws Throwable;

    public List<Notificaciones> mostrarListaDenotificacionesPorUsuarios(Integer claveD, Integer estaus) throws Throwable;

    public List<Generos> mostrarGeneros() throws Throwable;
/////////////////////////////////////////////////////////////////////////Perfil Admin\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    public List<PersonalCategorias> mostrarListaDePersonalCategoriases() throws Throwable;

    public List<Actividades> mostrarListaDeActividadeses() throws Throwable;
/////////////////////////////////////////////////////////////////////////Habilidades\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public List<HabilidadesInformaticas> mostrarHabilidadesInformaticasPorClaveTrabajador(Integer claveTrabajador) throws Throwable;

    public List<Lenguas> mostrarHabilidadesLengiasPorClaveTrabajador(Integer claveTrabajador) throws Throwable;

    public List<Idiomas> mostrarHabilidadesIdiomasPorClaveTrabajador(Integer claveTrabajador) throws Throwable;
/////////////////////////////////////////////////////////////////////////Produccion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public List<LibrosPub> mostrarLibrosPublicados(Integer claveTrabajador) throws Throwable;

    public List<Memoriaspub> mostrarMemoriaspubicados(Integer claveTrabajador) throws Throwable;

    public List<Articulosp> mostrarArticulospublicados(Integer claveTrabajador) throws Throwable;

    public List<Investigaciones> mostrarInvestigacionPorClaveTrabajador(Integer claveTrabajador) throws Throwable;

    public List<Congresos> mostrarCongresos(Integer claveTrabajador) throws Throwable;
///////////////////////////////////////////////////////////////////////////Educacion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public List<FormacionAcademica> mostrarFormacionAcademica(Integer claveTrabajador) throws Throwable;

    public List<ExperienciasLaborales> mostrarExperienciasLaborales(Integer claveTrabajador) throws Throwable;

    public List<Capacitacionespersonal> mostrarCapacitacionespersonal(Integer claveTrabajador) throws Throwable;
/////////////////////////////////////////////////////////////////////////Distinciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public List<Distinciones> mostrarDistinciones(Integer claveTrabajador) throws Throwable;
///////////////////////////////////////////////////////////////Incidencias\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    public List<Incidencias> mostrarIncidencias(Integer clave) throws Throwable;

    public List<Incidencias> mostrarIncidenciasPorEstatus(Integer clave, String estatus) throws Throwable;
///////////////////////////////////////////////////////////////Notificaciones Multiples\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    public List<ListaPersonal> mostrarListaPersonalPorCategoria(String categoria) throws Throwable;

    public List<ListaPersonal> mostrarListaPersonalPorActividad(String actividad) throws Throwable;

    public List<ListaPersonal> mostrarListaPersonalPorAreaOpySu(String area) throws Throwable;
//////////////////////////////////////////////////////////////ListaDistinciones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public List<Distinciones> mostrarDistincionesPorClaveEmpleado(Integer claveTrabajador) throws Throwable;
////////////////////////////////////////////////////////////////////////////////////Educacion\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public List<CursosTipo> mostrarTipoCursos() throws Throwable;

    public List<CursosModalidad> mostrarModalidades() throws Throwable;

    public List<Grados> mostrarGrados() throws Throwable;

    public List<FormacionAcademica> mostrarFormacionPorClaveTrabajador(Integer claveTrabajador) throws Throwable;

    public List<ExperienciasLaborales> mostrarExperienciasaboralesPorClavePersonal(Integer claveTrabajador) throws Throwable;

    public List<Capacitacionespersonal> mostrarCapacitacionesExternasPorClaveEmpleado(Integer claveTrabajador) throws Throwable;

    public List<Capacitacionespersonal> mostrarCapacitacionesInternasPorClaveEmpleado(Integer claveTrabajador) throws Throwable;

    ///////////////////////////////////////////////////////////////////////////////////////////////Tecnologia\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public List<Innovaciones> mostrarInnovaciones(Integer claveTrabajador) throws Throwable;

    public List<DesarrolloSoftware> mostrarDesarrollosSoftware(Integer claveTrabajador) throws Throwable;

    public List<DesarrollosTecnologicos> mostrarDesarrollosTecnologicos(Integer claveTrabajador) throws Throwable;
    ///////////////////////////////////////////////////////////////////////////////////////////////docencias\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public List<Docencias> mostrarDocencias(Integer claveTrabajador) throws Throwable;

    public List<Comentariosfunciones> mostrarComentariosfunciones() throws Throwable;

    public List<Categoriasespecificasfunciones> mostrarCategoriasespecificasfunciones(String nombre, Short area) throws Throwable;

    public List<Categoriasespecificasfunciones> mostrarCategoriasespecificasfuncionesArea(Short area) throws Throwable;

    public List<CursosPersonal> mostrarCursosPersonal(Integer claveTrabajador) throws Throwable;

    public List<Modulosregistro> mostrarModulosregistro(String actividadUsuario) throws Throwable;

    ///////////////////////////////////////////////////////////////////////////////////////////////proceso elecrtoral\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public List<Eventos> mostrarEventosRegistro(String tipo, String nombre) throws Throwable;

    public List<Historicoplantillapersonal> mostrarHistoricoplantillapersonal() throws Throwable;
}
