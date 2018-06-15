package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.logueo.Areas;

@Local
public interface EjbAreasLogeo {

////////////////////////////////////////////////////////////////////////////////Prontuario /////////////////////////////////////////////////////////////////////////////  
    public List<Areas> mostrarListaAreas(String tipo) throws Throwable;
}
