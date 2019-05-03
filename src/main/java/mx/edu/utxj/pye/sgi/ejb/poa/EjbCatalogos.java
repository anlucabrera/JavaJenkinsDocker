package mx.edu.utxj.pye.sgi.ejb.poa;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.UnidadMedidas;

@Local
public interface EjbCatalogos {

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

}
