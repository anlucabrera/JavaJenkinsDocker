package mx.edu.utxj.pye.sgi.ejb.prontuario;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosContinuidad;

@Local
public interface EjbAreasLogeo {

////////////////////////////////////////////////////////////////////////////////Prontuario /////////////////////////////////////////////////////////////////////////////  
    public List<Categorias> mostrarCategorias() throws Throwable;

    public List<AreasUniversidad> mostrarAreasUniversidadActivas() throws Throwable;

    public List<AreasUniversidad> mostrarAreasUniversidadSubordinadas(Short area) throws Throwable;

    public List<AreasUniversidad> mostrarAllAreasUniversidad() throws Throwable;

    public List<AreasUniversidad> getAreasUniversidadConPoa();

    public AreasUniversidad mostrarAreasUniversidad(Short areaId) throws Throwable;

    public AreasUniversidad agregarAreasUniversidad(AreasUniversidad au) throws Throwable;

    public AreasUniversidad actualizarAreasUniversidad(AreasUniversidad au) throws Throwable;

    public List<AreasUniversidad> mostrarAreasUniversidad() throws Throwable;

    public List<AreasUniversidad> listaProgramasEducativos();
    
    public List<ProgramasEducativosContinuidad> listaProgramasEducativosContinuidad(Short clave);
    
}
