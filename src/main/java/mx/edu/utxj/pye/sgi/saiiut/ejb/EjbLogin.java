/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.ejb;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.ch.Modulos;
import mx.edu.utxj.pye.sgi.entity.ch.Permisos;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
//import mx.edu.utxj.pye.sgi.entity.logueo.Areas;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.saiiut.entity.ListaUsuarioClaveNomina;
import mx.edu.utxj.pye.sgi.saiiut.entity.Usuarios;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionesTutores;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbLogin {
    public Usuarios autenticar(String loginUsuario, String password);
    
    public Usuarios getUsuarioPorLogin(String loginUsuario);
    
    public String encriptarContrasena(String contrasena);
    
    public ListaUsuarioClaveNomina getListaUsuarioClaveNomina(String loginUsuario);
    
    public UsuarioTipo getTipoUsuario(Usuarios usuario);
    
    /*
     *Creacion del menu  posible migracion de metodos Ejb's a otro archivo
     */
    public List<Modulos>  getCategoriaModulos();
     
    public List<Permisos>  getPermisosModulos();
    
    public AreasUniversidad getAreaByClave(Short area);
    
    public PersonalCategorias getCategoriaPersonalByarea(Short categoria);
    
    public Modulos getModuloByClave(Integer modulo);
    
    public List<VistaEvaluacionesTutores> getTutoresPeriodoActual();
}
