package mx.edu.utxj.pye.sgi.ejb.poa;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.poa.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.poa.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.poa.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.poa.RecursosActividad;
import mx.edu.utxj.pye.sgi.entity.poa.UnidadMedidas;

@Local
public interface EjbPoaSelectec {

    public List<ActividadesPoa> mostrarActividadesPoas();

    public ActividadesPoa agregarActividadesPoa(ActividadesPoa nuevaActividadesPoa);

    public ActividadesPoa actualizaActividadesPoa(ActividadesPoa nuevaActividadesPoa);

    public RecursosActividad agregarRecursosActividad(RecursosActividad nuevoRecursosActividad);

    public RecursosActividad actualizaRecursosActividad(RecursosActividad nuevoRecursosActividad);

    public ActividadesPoa eliminarActividadesPoa(ActividadesPoa nuevaActividadesPoa);

    public RecursosActividad eliminarRecursosActividad(RecursosActividad nuevoRecursosActividad);

    public List<CuadroMandoIntegral> mostrarCuadroMandoIntegrals();

    public List<UnidadMedidas> mostrarUnidadMedidases();

    public List<ProductosAreas> mostrarProductosAreases(Short clavearea, Short ejercicioFiscal);

    public List<RecursosActividad> mostrarRecursosActividads();
}
