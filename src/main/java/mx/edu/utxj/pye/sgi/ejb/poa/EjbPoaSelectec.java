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
import mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero;

@Local
public interface EjbPoaSelectec {

//  -------------------------------------------- ActividadesPoa -------------------------------------------------
    public List<ActividadesPoa> mostrarAreasQueRegistraronActividades();
    
    public List<ActividadesPoa> mostrarActividadesPoasArea(Short area);
    
    public List<ActividadesPoa> mostrarActividadesPoasReporteArea(Short area,Short ejercicioFiscal);
    
    public List<ActividadesPoa> mostrarSubActividadesPoa(Short area,Short ejercicioFiscal,Short numeroP,CuadroMandoIntegral cuadroMando);

    public ActividadesPoa agregarActividadesPoa(ActividadesPoa nuevaActividadesPoa);

    public ActividadesPoa actualizaActividadesPoa(ActividadesPoa nuevaActividadesPoa);

    public ActividadesPoa eliminarActividadesPoa(ActividadesPoa nuevaActividadesPoa);
//  ------------------------------------------ RecursosActividad ------------------------------------------------

    public List<RecursosActividad> mostrarRecursosActividad();

    public RecursosActividad agregarRecursosActividad(RecursosActividad nuevoRecursosActividad);

    public RecursosActividad actualizaRecursosActividad(RecursosActividad nuevoRecursosActividad);

    public RecursosActividad eliminarRecursosActividad(RecursosActividad nuevoRecursosActividad);
//  ------------------------------------------CuadroMandoIntegral -----------------------------------------------

    public List<CuadroMandoIntegral> mostrarCuadroMandoIntegrals(Short ejercicioFiscal);
//  -------------------------------------------- UnidadMedidas --------------------------------------------------

    public List<UnidadMedidas> mostrarUnidadMedidases();
//  -------------------------------------------- EjesRegistro --------------------------------------------------

    public List<EjesRegistro> mostrarEjesRegistros();
//  -------------------------------------------- EjerciciosFiscales --------------------------------------------------

    public List<EjerciciosFiscales> mostrarEjerciciosFiscaleses();
//  ------------------------------------------- ProductosAreas --------------------------------------------------

    public List<ProductosAreas> mostrarProductosAreases(Short clavearea, Short ejercicioFiscal);
//  ------------------------------------------- PretechoFinanciero --------------------------------------------------

    public List<PretechoFinanciero> mostrarPretechoFinancieros(Short clavearea, Short ejercicioFiscal);
//  ------------------------------------------- Evidencias --------------------------------------------------

    public Evidencias agregarEvidenciases(Evidencias evidencias, ActividadesPoa actividadesPoa);

    public Evidencias actualizarEvidenciases(Evidencias evidencias);

    public EvidenciasDetalle agregarEvidenciasesEvidenciasDetalle(EvidenciasDetalle evidenciasDetalle);

    public List<Evidencias> mostrarEvidenciases(ActividadesPoa actividad);

    public List<EvidenciasDetalle> mostrarEvidenciases(Evidencias evidencias);
//  -------------------------------------------------------------------------------------------------------------

    public List<Estrategias> getEstarategiasPorEje(EjesRegistro eje, Short ejercicio, Short area);

    public List<ActividadesPoa> getActividadesPoasporEstarategias(Estrategias estrategia, EjesRegistro eje, Short ejercicio, Short area);

    public List<ActividadesPoa> getActividadesEvaluacionMadre(CuadroMandoIntegral cuadroDmando, Short numeroP);
}
