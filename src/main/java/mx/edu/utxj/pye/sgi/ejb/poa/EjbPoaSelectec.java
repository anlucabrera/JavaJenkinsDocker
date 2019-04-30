package mx.edu.utxj.pye.sgi.ejb.poa;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.pye2.RecursosActividad;
import mx.edu.utxj.pye.sgi.entity.pye2.UnidadMedidas;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Partidas;
import mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero;
import mx.edu.utxj.pye.sgi.entity.pye2.Productos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Proyectos;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;

@Local
public interface EjbPoaSelectec {

// ---- Registro de actividades
    public ActividadesPoa mostrarActividadPoaPrincipal(Integer clave);

    public List<ActividadesPoa> mostrarActividadesPoasAreaEjeyEjercicioFiscal(Short area, Short ejerciciosFiscales, EjesRegistro ejesRegistro);

    public List<ActividadesPoa> mostrarActividadesPoasUniversidadaEjeyEjercicioFiscal(Short ejerciciosFiscales, EjesRegistro ejesRegistro);

    public List<ActividadesPoa> mostrarActividadesPoasUniversidadaEjercicioFiscal(Short ejerciciosFiscales);

    public List<ActividadesPoa> mostrarActividadesPoaCuadroDeMando(Short area, Short ejercicioFiscal, CuadroMandoIntegral cuadroMando);

    public List<ActividadesPoa> mostrarActividadesPoaCuadroDeMandoRecurso(Short area, Short ejercicioFiscal, CuadroMandoIntegral cuadroMando);
//  -------------------------------------------- EjesRegistro --------------------------------------------------

    public EjesRegistro mostrarEjeRegistro(Integer clave);

    public List<EjesRegistro> mostrarEjesRegistros();

    public List<EjesRegistro> mostrarEjesRegistrosAreas(Short area, Short ejerciciosFiscales);

    public List<EjesRegistro> mostrarEjesRegistrosAreasCMI(Short area, Short ejerciciosFiscales);
//  -------------------------------------------- Estrategias -------------------------------------------------

    public Estrategias mostrarEstrategia(Short clave);

    public List<Estrategias> mostrarEstrategiasRegistro(Short ejerciciosFiscales, EjesRegistro ejesRegistro);

    public List<Estrategias> getEstarategiasPorEje(EjesRegistro eje, Short ejercicio, Short area);

    //  -------------------------------------------- Lineas Accion -------------------------------------------------
    public LineasAccion mostrarLineaAccion(Short clave);

    public List<LineasAccion> mostrarLineasAccionRegistroParametros(Short area, Short ejerciciosFiscales, EjesRegistro ejesRegistro, Estrategias estrategias);

    public List<LineasAccion> mostrarLineasAccionRegistro(Short ejerciciosFiscales, EjesRegistro ejesRegistro, Estrategias estrategias);
    //  -------------------------------------------- UnidadMedidas --------------------------------------------------

    public List<UnidadMedidas> mostrarUnidadMedidases();

    public UnidadMedidas agregarUnidadMedidas(UnidadMedidas nuevaUnidadMedidas);

//  ------------------------------------------CuadroMandoIntegral -----------------------------------------------
    public List<CuadroMandoIntegral> mostrarCuadroMandoIntegralRegistrpo(Short ejercicioFiscal, EjesRegistro ejesRegistro, Estrategias estrategias, LineasAccion lineasAccion);

    public List<CuadroMandoIntegral> mostrarCuadroMandoIntegrals(Short ejercicioFiscal);

// ---- Fin Registro de actividades
// ---- Recurso    
    public List<Partidas> mostrarPartidases(Short ejercicio, Short area);

    public List<Productos> mostrarProductoses(Short ejercicio, Short area, Partidas partida);

    public Partidas mostrarPartidas(Short partida);

    public Productos mostrarProductos(ProductosPK ppk);

    public ProductosAreas mostrarProductosAreas(Productos productos, Partidas partidas, Short area);
// ---- Fin Registro de actividades    
//  -------------------------------------------- ActividadesPoa -------------------------------------------------

    public List<ActividadesPoa> mostrarAreasQueRegistraronActividades();

    public List<ActividadesPoa> mostrarActividadesPoasArea(Short area);

    public List<ActividadesPoa> mostrarActividadesPoasReporteArea(Short area, Short ejercicioFiscal);

    public List<ActividadesPoa> mostrarSubActividadesPoa(Short area, Short ejercicioFiscal, Short numeroP, CuadroMandoIntegral cuadroMando);

    public ActividadesPoa agregarActividadesPoa(ActividadesPoa nuevaActividadesPoa);

    public ActividadesPoa actualizaActividadesPoa(ActividadesPoa nuevaActividadesPoa);

    public ActividadesPoa eliminarActividadesPoa(ActividadesPoa nuevaActividadesPoa);

//  -------------------------------------------- EjerciciosFiscales --------------------------------------------------
    public List<EjerciciosFiscales> mostrarEjerciciosFiscaleses();
//  ------------------------------------------- ProductosAreas --------------------------------------------------

    public List<ProductosAreas> mostrarProductosAreases(Short clavearea, Short ejercicioFiscal);
//  ------------------------------------------- PretechoFinanciero --------------------------------------------------

    public List<PretechoFinanciero> mostrarPretechoFinancieros(Short clavearea, Short ejercicioFiscal);
//  ------------------------------------------- Evidencias --------------------------------------------------

    public Evidencias eliminarEvidencias(Evidencias evidencias);

    public EvidenciasDetalle eliminarEvidenciasDetalle(EvidenciasDetalle evidenciasDetalle);

    public Evidencias agregarEvidenciases(Evidencias evidencias, ActividadesPoa actividadesPoa);

    public Evidencias actualizarEvidenciases(Evidencias evidencias);

    public EvidenciasDetalle agregarEvidenciasesEvidenciasDetalle(EvidenciasDetalle evidenciasDetalle);

    public List<Evidencias> mostrarEvidenciases(ActividadesPoa actividad, List<Registros> registroses);

    public List<EvidenciasDetalle> mostrarEvidenciases(Evidencias evidencias);
//  -------------------------------------------------------------------------------------------------------------

    public List<ActividadesPoa> getActividadesPoasporEstarategias(Estrategias estrategia, EjesRegistro eje, Short ejercicio, Short area);

    public List<ActividadesPoa> getActividadesEvaluacionMadre(CuadroMandoIntegral cuadroDmando, Short numeroP, Short area);

    public List<Proyectos> getProyectosPorEje(EjesRegistro eje, Short ejercicio, Short area);

    public List<Proyectos> getProyectos(EjesRegistro eje, Short ejercicio);

    public List<Estrategias> getEstrategiaPorProyectos(Proyectos proyectos, Short ejercicio, Short area);

    public List<Estrategias> getEstrategiaProyectos(Proyectos proyectos, Short ejercicio);

    public List<ActividadesPoa> getActividadesPoasporProyecto(Estrategias estrategia, EjesRegistro eje, Proyectos proyectos, Short ejercicio, Short area);

    public List<ActividadesPoa> getActividadesPoasProyecto(Estrategias estrategia, EjesRegistro eje, Proyectos proyectos, Short ejercicio);

    public List<ActividadesPoa> getActividadesPoasProyectoGrfica(EjesRegistro eje, Proyectos proyectos, Short ejercicio);

    public List<Registros> mostrarRegistrosActividad(ActividadesPoa actividadesPoa);
}
