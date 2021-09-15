package mx.edu.utxj.pye.sgi.ejb.poa;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.controladores.cmi.DtoCmi;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Proyectos;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.UnidadMedidas;

@Local
public interface EjbCatalogosPoa {

    //  -------------------------------------------- EjesRegistro --------------------------------------------------
    public EjesRegistro mostrarEjeRegistro(Integer clave);

    public List<EjesRegistro> mostrarEjesRegistros();

    public List<EjesRegistro> mostrarEjesRegistrosAreas(Short area, Short ejerciciosFiscales);

    public List<EjesRegistro> mostrarEjesRegistrosAreasCMI(Short area, Short ejerciciosFiscales);
//  -------------------------------------------- Estrategias -------------------------------------------------

    public Estrategias mostrarEstrategia(Short clave);

    public List<Estrategias> mostrarEstrategiasRegistro(Short ejerciciosFiscales, EjesRegistro ejesRegistro);

    public List<Estrategias> getEstarategiasPorEje(EjesRegistro eje, Short ejercicio, Short area);

    public List<Estrategias> getEstrategiaPorProyectos(Proyectos proyectos, Short ejercicio, Short area);

    public List<Estrategias> getEstrategiaProyectos(Proyectos proyectos, Short ejercicio);

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

//  ------------------------------------------Proyectos -----------------------------------------------
    public List<Proyectos> getProyectosPorEje(EjesRegistro eje, Short ejercicio, Short area);

    public List<Proyectos> getProyectos(EjesRegistro eje, Short ejercicio);

//  ------------------------------------------Registros -----------------------------------------------
    public List<Registros> mostrarRegistrosActividad(ActividadesPoa actividadesPoa);

//  ------------------------------------------EjerciciosFiscales -----------------------------------------------
    public EjerciciosFiscales mostrarEjercicioFiscaleses(Short idE);
    
    public EjerciciosFiscales mostrarEjercicioFiscalAnio(Short anio);

    public List<EjerciciosFiscales> mostrarEjercicioFiscalesesTotales();
    
//  ------------------------------------------Plantilla -----------------------------------------------    
    public String getReporteCuadroMandoPOA(Short ejeFiscal,List<DtoCmi.ReporteCuatrimestralAreas> reporte) throws Throwable;

}
