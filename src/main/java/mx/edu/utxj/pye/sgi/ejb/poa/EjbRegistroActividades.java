package mx.edu.utxj.pye.sgi.ejb.poa;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.Comentariosprocesopoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.Proyectos;

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

    public List<ActividadesPoa> getActividadesPoasporProyecto(Estrategias estrategia, EjesRegistro eje, Proyectos proyectos, Short ejercicio, Short area);

    public List<ActividadesPoa> mostrarActividadesPoasUniversidadaEjercicioFiscal(Short ejerciciosFiscales);

    public List<ActividadesPoa> mostrarActividadesPoasUniversidadaEjeyEjercicioFiscal(Short ejerciciosFiscales, EjesRegistro ejesRegistro);

    public List<ActividadesPoa> getActividadesPoasProyectoGrfica(EjesRegistro eje, Proyectos proyectos, Short ejercicio);

    public List<ActividadesPoa> getActividadesPoasProyecto(Estrategias estrategia, EjesRegistro eje, Proyectos proyectos, Short ejercicio);

    public List<ActividadesPoa> getActividadesEvaluacionMadre(CuadroMandoIntegral cuadroDmando, Short numeroP, Short area);

    public List<ActividadesPoa> mostrarActividadesPoasArea(Short area);

    public List<ActividadesPoa> mostrarActividadesPoaCuadroDeMandoRecurso(Short area, Short ejercicioFiscal, CuadroMandoIntegral cuadroMando);

    public List<ActividadesPoa> getActividadesPoasporEstarategias(Estrategias estrategia, EjesRegistro eje, Short ejercicio, Short area);

    public List<ActividadesPoa> mostrarAreasQueRegistraronActividades();
    
    public List<Comentariosprocesopoa> mostrarComentariosprocesopoaArea(Short area, Short ejercicioFiscal);
    
    public Comentariosprocesopoa agregarComentariosprocesopoa(Comentariosprocesopoa nuevaComentariosprocesopoa);

    public Comentariosprocesopoa actualizaComentariosprocesopoa(Comentariosprocesopoa nuevaComentariosprocesopoa);

    public Comentariosprocesopoa eliminarComentariosprocesopoa(Comentariosprocesopoa nuevaComentariosprocesopoa);

}
