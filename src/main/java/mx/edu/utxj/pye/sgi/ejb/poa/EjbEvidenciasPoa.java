package mx.edu.utxj.pye.sgi.ejb.poa;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;

@Local
public interface EjbEvidenciasPoa {

    public Evidencias agregarEvidenciases(Evidencias evidencias, ActividadesPoa actividadesPoa);

    public EvidenciasDetalle agregarEvidenciasesEvidenciasDetalle(EvidenciasDetalle evidenciasDetalle);

    public Evidencias actualizarEvidenciases(Evidencias evidencias);

    public Evidencias eliminarEvidencias(Evidencias evidencias);

    public EvidenciasDetalle eliminarEvidenciasDetalle(EvidenciasDetalle evidenciasDetalle);

    public List<Evidencias> mostrarEvidenciasesRegistros(ActividadesPoa actividad, List<Registros> registroses);

    public List<EvidenciasDetalle> mostrarEvidenciases(Evidencias evidencias);

}
