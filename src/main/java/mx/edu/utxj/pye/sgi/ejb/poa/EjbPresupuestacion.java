package mx.edu.utxj.pye.sgi.ejb.poa;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.dto.poa.DTOreportePoa;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CapitulosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.Partidas;
import mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero;
import mx.edu.utxj.pye.sgi.entity.pye2.Productos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosPK;
import mx.edu.utxj.pye.sgi.entity.pye2.RecursosActividad;

@Local
public interface EjbPresupuestacion {

//---------------------------------------------------------------------- Actividades Poa --------------------------------------------------
    public List<ActividadesPoa> mostrarActividadesPoaCuadroDeMandoRecurso(Short area, Short ejercicioFiscal, CuadroMandoIntegral cuadroMando);

//---------------------------------------------------------------------- Recursos Actividad --------------------------------------------------
    public List<RecursosActividad> mostrarRecursosActividad(Short area, Short ejercicioFical, Short capitulo);

    public List<RecursosActividad> mostrarRecursosActividadReporte(ActividadesPoa actividadesPoa);

    public RecursosActividad agregarRecursosActividad(RecursosActividad nuevoRecursosActividad);

    public RecursosActividad actualizaRecursosActividad(RecursosActividad nuevoRecursosActividad);

    public RecursosActividad eliminarRecursosActividad(RecursosActividad nuevoRecursosActividad);

//  ---------------------------------------------------------------------- Productos Areas --------------------------------------------------
    public List<ProductosAreas> mostrarProductosAreases(Short clavearea, Short ejercicioFiscal);

    public ProductosAreas mostrarProductosAreas(Productos productos, Partidas partidas, Short area);
    
    public List<ProductosAreas> mostrarProductosAreasPartidas(Short ejercicioFiscal, Partidas partidas, Short area);
    
    public ProductosAreas agregarProductosAreas(ProductosAreas productos);
    
    public ProductosAreas actualizarProductosAreas(ProductosAreas productos);
    
    public ProductosAreas eliminarProductosAreas(ProductosAreas productos);

//  ---------------------------------------------------------------------- Pretecho Financiero --------------------------------------------------
    public PretechoFinanciero agregarPretechoFinanciero(PretechoFinanciero financiero);

    public PretechoFinanciero actualizaPretechoFinanciero(PretechoFinanciero financiero);

    public PretechoFinanciero eliminarPretechoFinanciero(PretechoFinanciero financiero);

    public List<PretechoFinanciero> mostrarPretechoFinancieros(Short clavearea, Short ejercicioFiscal);

    public List<PretechoFinanciero> mostrarPretechoFinancierosGeneral(Short ejercicioFiscal);

//  ------------------------------------------------------------------------ Partidas --------------------------------------------------
    public List<Partidas> mostrarPartidases(Short ejercicio, Short area);
    
    public List<Partidas> mostrarPartidasesTotales();

    public Partidas mostrarPartidas(Short partida);
    
    public Partidas agregarPartidas(Partidas partida);
    
    public Partidas actualizarPartidas(Partidas partida);

//  ------------------------------------------------------------------------ Productos --------------------------------------------------
    public List<Productos> mostrarProductoses(Short ejercicio, Short area, Partidas partida);
    
    public List<Productos> mostrarProductoseTotales(Short ejercicio, Short area);
    
     public Productos mostrarProductos(ProductosPK ppk);
    
    public Productos agregarProductos(Productos productos);
    
    public Productos actualizarProductos(Productos productos);
    
    public Productos eliminarProductos(Productos productos);
//  ------------------------------------------------------------------------ Capitulo --------------------------------------------------
    public List<CapitulosTipos> mostrarCapitulosTiposes();
    
    public List<DTOreportePoa.General> getReportePOA(Short ejercicio, Short area);
    
    public AreasUniversidad mostrArarea(Short clave);
    
    public Personal mostrPersonal(Integer clave);
    
    public String getReportePresupuestoPOA(Short ejeFiscal) throws Throwable;
    
}
