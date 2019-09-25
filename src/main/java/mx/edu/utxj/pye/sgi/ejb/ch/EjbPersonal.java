package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.ContactoEmergencias;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.shiro.User;

@Local
public interface EjbPersonal {

////////////////////////////////////////////////////////////////////////////////Lista Personal
    public ListaPersonal mostrarListaPersonal(Integer claveTrabajador) throws Throwable;

    public List<ListaPersonal> mostrarListaDeEmpleados() throws Throwable;

    public List<ListaPersonal> mostrarListaPersonalsPorParametros(String nombre, Integer tipo) throws Throwable;

    public List<ListaPersonal> mostrarListaPersonalListSubordinados(ListaPersonal perosona);

    /**
     * Método que permite la búsqueda de personal mediante coincidencias de
     * Clave,Nombre ó Área Operativa
     *
     * @param parametro Búsqueda
     * @return Devuelve una lista con las coincidencias encontradas
     */
    public List<ListaPersonal> buscaCoincidenciasListaPersonal(String parametro);

    ////////////////////////////////////////////////////////////////////////////////Personal
    public List<Personal> mostrarListaPersonalSubordinados(Short area, Integer claveTrabajador) throws Throwable;

    public List<Personal> mostrarListaPersonalsPorEstatus(Integer estatus) throws Throwable;

    public Personal mostrarPersonalLogeado(Integer claveTrabajador) throws Throwable;

    public Personal crearNuevoPersonal(Personal nuevoPersonal) throws Throwable;

    public Personal actualizarPersonal(Personal nuevoPersonal) throws Throwable;

////////////////////////////////////////////////////////////////////////////////Informacion Adicional Personal
    public InformacionAdicionalPersonal mostrarInformacionAdicionalPersonalLogeado(Integer claveTrabajador) throws Throwable;

    public InformacionAdicionalPersonal crearNuevoInformacionAdicionalPersonal(InformacionAdicionalPersonal nuevoInformacionAdicionalPersonal) throws Throwable;

    public InformacionAdicionalPersonal actualizarInformacionAdicionalPersonal(InformacionAdicionalPersonal nuevoInformacionAdicionalPersonal) throws Throwable;

////////////////////////////////////////////////////////////////////////////////Contacto Emergencias    
    public List<ContactoEmergencias> mostrarContactosEmergencias(Integer claveTrabajador) throws Throwable;

    public List<ContactoEmergencias> mostrarAllContactosEmergencias() throws Throwable;

    public ContactoEmergencias crearContactosEmergencias(ContactoEmergencias ce) throws Throwable;

    public ContactoEmergencias actualizarContactosEmergencias(ContactoEmergencias ce) throws Throwable;

    public ContactoEmergencias eliminarContactosEmergencias(ContactoEmergencias ce) throws Throwable;

////////////////////////////////////////////////////////////////////////////////Datos de Acceso
    public User getDatosUsuario(String clave);

    public User crearUser(User user);

    public User actualizarUser(User user);
    
////////////////////////////////////////////////////////////////////////////////Catalogos
    public List<Docencias> mostrarListaDocencias(Integer claveTrabajador) throws Throwable;

    public List<Generos> mostrarListaGeneros() throws Throwable;

    public List<Actividades> mostrarListaActividades() throws Throwable;
}
