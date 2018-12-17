/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.ejb;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.ch.Modulos;
import mx.edu.utxj.pye.sgi.entity.ch.Permisos;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.ch.shiro.User;
import mx.edu.utxj.pye.sgi.entity.prontuario.Listaperiodosescolares;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Patron;
import mx.edu.utxj.pye.sgi.funcional.PatronMatricula;
import mx.edu.utxj.pye.sgi.saiiut.entity.ListaUsuarioClaveNomina;
import mx.edu.utxj.pye.sgi.saiiut.entity.Usuarios;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionesTutores;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioLogin implements EjbLogin {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb-shiro.0PU")
    private EntityManager em;

    @EJB    Facade f2;// mysql
    // Comentar las siguiente Importaciones cuando falle saiiut //
    @EJB    Facade2 f;
    // Fin de Importaciones 
    
    private final Patron patronMatricula = new PatronMatricula();   
    
// Comentar los siguiente métodos cuando falle saiiut //
//    @Override
//    public Usuarios autenticar(String loginUsuario, String password) {
////        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioLogin.autenticar() loginUsuario: " + loginUsuario + ", password: " + password);
//        Usuarios usuarioBd = getUsuarioPorLogin(loginUsuario);
//        if (usuarioBd != null) {
////            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioLogin.autenticar() usuarioBd no nulo");
//            if (usuarioBd.getContrasena().equals(encriptarContrasena(password))) {
////                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioLogin.autenticar() las contraseñas coinciden");
//                return usuarioBd;
//            } else {
////                System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioLogin.autenticar() las contraseñas no coinciden");
//                return null;
//            }
//        } else {
////            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioLogin.autenticar() usuarioBd es nulo");
//            return null;
//        }
//    }
//
//    @Override
//    public Usuarios getUsuarioPorLogin(String loginUsuario) {
////        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioLogin.getUsuarioPorLogin() loginUsuario: " + loginUsuario);
//        TypedQuery<Usuarios> q = f.getEntityManager().createNamedQuery("Usuarios.findByLoginUsuario", Usuarios.class);
//        q.setParameter("loginUsuario", loginUsuario);
//        List<Usuarios> l = q.getResultList();
//
////        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioLogin.getUsuarioPorLogin() l: " + l);
//        if (l.isEmpty()) {
//            return null;
//        } else {
//            return l.get(0);
//        }
//    }
//
//    @Override
//    public ListaUsuarioClaveNomina getListaUsuarioClaveNomina(String loginUsuario) {
//        f.setEntityClass(ListaUsuarioClaveNomina.class);
//        return (ListaUsuarioClaveNomina) f.find(loginUsuario);
//    }
//
//    @Override
//    public List<VistaEvaluacionesTutores> getTutoresPeriodoActual() {
//        TypedQuery<Listaperiodosescolares> periodo = f2.getEntityManager().createQuery("SELECT p from Listaperiodosescolares p ORDER BY p.periodo DESC", Listaperiodosescolares.class);
//        if (periodo.getResultList().isEmpty() || periodo.getResultList() == null) {
//            System.out.println("mx.edu.utxj.pye.sgi.saiiut.ejb.ServicioLogin.getTutoresPeriodoActual() no se encontro periodo::: ");
//        } else {
////            System.out.println("mx.edu.utxj.pye.sgi.saiiut.ejb.ServicioLogin.getTutoresPeriodoActual() el periodo es ::: " + periodo.getSingleResult());
//        }
//        TypedQuery<VistaEvaluacionesTutores> q = f.getEntityManager().createQuery("SELECT v FROM VistaEvaluacionesTutores v WHERE v.pk.periodo = :periodo", VistaEvaluacionesTutores.class);
//        q.setParameter("periodo", periodo.getResultList().get(0).getPeriodo());
//        if (q.getResultList().isEmpty() || q.getResultList() == null) {
//            return null;
//        } else {
//            return q.getResultList();
//        }
//    }
// Fin de métodos 

    @Override
    public String encriptarContrasena(String contrasena) {
        String original = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz1234567890!$%&/()=?¿¡,.-;:_ ";
        String encriptada = "Uqyh.-aJ,g4TPVDE/2WZ15uBC78b3X:_ 6AOHIYrstNFGvcjñzÑdef)=?¿¡omKL90!(i$%&QRklwxnMSp;";
        String temporal = "";
        for (int x = 0; x < contrasena.length(); x++) {
            for (int y = 0; y < original.length(); y++) {
                char xContrasena = contrasena.charAt(x);
                char xOriginal = original.charAt(y);
                if (xContrasena == xOriginal) {
                    if ((x + 1) % 2 == 0) {
                        temporal = String.valueOf(String.valueOf(temporal)).concat(String.valueOf(String.valueOf(encriptada.charAt(y))));
                        break;
                    }
                    if (y == encriptada.length()) {
                        temporal = String.valueOf(String.valueOf(temporal)).concat(String.valueOf(String.valueOf(encriptada.charAt(1))));
                        break;
                    }
                    temporal = String.valueOf(String.valueOf(temporal)).concat(String.valueOf(String.valueOf(encriptada.charAt(y + 1))));

                    break;
                }
            }
        }
        String cadenaEncriptada = temporal;

        return cadenaEncriptada;
    }

    @Override
    public UsuarioTipo getTipoUsuario(Usuarios usuario) {
        if (usuario != null) {
            if (patronMatricula.coincide(usuario.getLoginUsuario().trim())) {
                return UsuarioTipo.ESTUDIANTE;
            } else {
                return UsuarioTipo.TRABAJADOR;
            }
        } else {
            return null;
        }
    }

    @Override
    public User getUsuarioPorLoginShiro(String loginUsuario) {
        TypedQuery<User> q = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        q.setParameter("username", loginUsuario);
        List<User> l = q.getResultList();

//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioLogin.getUsuarioPorLogin() l: " + l);
        if (l.isEmpty()) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public User autenticarShiro(String loginUsuario, String password) {
        User usuarioBd = getUsuarioPorLoginShiro(loginUsuario);
        if (usuarioBd != null) {
            if (usuarioBd.getPassword().equals(password)) {
                return usuarioBd;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<Modulos> getCategoriaModulos() {
        StoredProcedureQuery q = f2.getEntityManager().createStoredProcedureQuery("obtener_lista_modulos_usuarios", Modulos.class);
        List<Modulos> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    @Override
    public List<Permisos> getPermisosModulos() {
        StoredProcedureQuery q = f2.getEntityManager().createStoredProcedureQuery("obtener_lista_permisos_usuario", Permisos.class);
        List<Permisos> l = q.getResultList();
        if (l == null || l.isEmpty()) {
            return null;
        } else {
            return l;
        }
    }

    @Override
    public AreasUniversidad getAreaByClave(Short area) {
        TypedQuery<AreasUniversidad> q = f2.getEntityManager().createQuery("SELECT a from AreasUniversidad a WHERE a.area = :area", AreasUniversidad.class);
        q.setParameter("area", area);
        if (q.getResultList().isEmpty() || q.getResultList() == null) {
            System.out.println("mx.edu.utxj.pye.sgi.saiiut.ejb.ServicioLogin.getAreaByClave() es nulo");
            return null;
        } else {
//            System.out.println("mx.edu.utxj.pye.sgi.saiiut.ejb.ServicioLogin.getAreaByClave() area es : " + q.getResultList().get(0));
            return q.getResultList().get(0);
        }
    }

    @Override
    public PersonalCategorias getCategoriaPersonalByarea(Short categoria) {
        TypedQuery<PersonalCategorias> q = f2.getEntityManager().createQuery("SELECT p from PersonalCategorias p WHERE p.categoria = :categoria", PersonalCategorias.class);
        q.setParameter("categoria", categoria);
        if (q.getResultList().isEmpty() || q.getResultList() == null) {
            return null;
        } else {
            return q.getResultList().get(0);
        }
    }

    @Override
    public Modulos getModuloByClave(Integer modulo) {
        TypedQuery<Modulos> q = f2.getEntityManager().createQuery("SELECT m from Modulos m WHERE m.modulo = :modulo ", Modulos.class);
        q.setParameter("modulo", modulo);
        if (q.getResultList().isEmpty() || q.getResultList() == null) {
            return null;
        } else {
            return q.getResultList().get(0);
        }
    }

}
