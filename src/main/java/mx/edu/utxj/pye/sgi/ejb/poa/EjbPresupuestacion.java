package mx.edu.utxj.pye.sgi.ejb.poa;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
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
    
//  ---------------------------------------------------------------------- Pretecho Financiero --------------------------------------------------
    public List<PretechoFinanciero> mostrarPretechoFinancieros(Short clavearea, Short ejercicioFiscal);

//  ------------------------------------------------------------------------ Partidas --------------------------------------------------
    public List<Partidas> mostrarPartidases(Short ejercicio, Short area);

    public Partidas mostrarPartidas(Short partida);

//  ------------------------------------------------------------------------ Productos --------------------------------------------------
    public List<Productos> mostrarProductoses(Short ejercicio, Short area, Partidas partida);

    public Productos mostrarProductos(ProductosPK ppk);

}
