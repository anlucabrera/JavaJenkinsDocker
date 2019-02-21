package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Categoriasespecificasfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Comentariosfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;

@Local
public interface EjbFunciones {

////////////////////////////////////////////////////////////////////////////////Funciones
    public List<Funciones> mostrarFuncionesPorAreayPuesto(Short area, Short categoria, Integer tipo) throws Throwable;

    public List<Funciones> mostrarListaFuncionesPersonalLogeado(Short area, Short categoriaOperativa, Short categoriaEspecifica) throws Throwable;

    public Funciones agregarFuncion(Funciones nuevaFunciones) throws Throwable;

    public Funciones actualizarFunciones(Funciones nuevaActualizacionFunciones) throws Throwable;

    public Funciones eliminaFunciones(Funciones nuevoFunciones) throws Throwable;

////////////////////////////////////////////////////////////////////////////////Comentarios funciones
    public Comentariosfunciones agregarComentariosfunciones(Comentariosfunciones nuevoComentariosfunciones) throws Throwable;

    public Comentariosfunciones actualizarComentariosfunciones(Comentariosfunciones nuevoComentariosfunciones) throws Throwable;

    public List<Comentariosfunciones> mostrarComentariosfunciones() throws Throwable;

////////////////////////////////////////////////////////////////////////////////Categorias especificas funciones
    public Categoriasespecificasfunciones agregarCategoriasespecificasfunciones(Categoriasespecificasfunciones nuevaCategoriasespecificasfunciones) throws Throwable;

    public List<Categoriasespecificasfunciones> mostrarCategoriasespecificasfunciones(String nombre, Short area) throws Throwable;

    public List<Categoriasespecificasfunciones> mostrarCategoriasespecificasfuncionesArea(Short area) throws Throwable;
}
