package mx.edu.utxj.pye.sgi.ejb.poa;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;

@Local
public interface EjbRegistroActividades {

    public ActividadesPoa mostrarActividadPoaPrincipal(Integer clave);

    public ActividadesPoa agregarActividadesPoa(ActividadesPoa nuevaActividadesPoa);

    public ActividadesPoa actualizaActividadesPoa(ActividadesPoa nuevaActividadesPoa);

    public ActividadesPoa eliminarActividadesPoa(ActividadesPoa nuevaActividadesPoa);

    public List<ActividadesPoa> mostrarSubActividadesPoa(Integer claveActividadPadre);

    public List<ActividadesPoa> mostrarActividadesPoaPrincipalesCuadroMando(Integer CuadroDeMando, Short claveArea);

    public List<ActividadesPoa> mostrarActividadesPoasTotalArea(Short area, Short ejercicioFiscal);

    public List<ActividadesPoa> mostrarActividadesPoasEje(Short area, Short ejerciciosFiscales, EjesRegistro ejesRegistro);

    public List<ActividadesPoa> getActividadesPoasEstarategias(Estrategias estrategia, EjesRegistro eje, Short ejercicio, Short area);

}
