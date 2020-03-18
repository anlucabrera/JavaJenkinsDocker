package com.github.adminfaces.starter.infra.security;

import com.github.adminfaces.template.session.AdminSession;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Login;
import mx.edu.utxj.pye.sgi.util.Encrypted;
import org.omnifaces.util.Faces;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import com.github.adminfaces.template.config.AdminConfig;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.MenuDinamico;
import mx.edu.utxj.pye.sgi.entity.ch.Permisos;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.shiro.User;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.saiiut.ejb.EjbLogin;
import mx.edu.utxj.pye.sgi.saiiut.entity.ListaUsuarioClaveNomina;
import mx.edu.utxj.pye.sgi.saiiut.entity.Usuarios;
import mx.edu.utxj.pye.sgi.util.permisosUsuarios;
import org.omnifaces.util.Messages;

/**
 * Created by rmpestano on 12/20/14.
 *
 * This is just a login example.
 *
 * AdminSession uses isLoggedIn to determine if user must be redirect to login page or not.
 * By default AdminSession isLoggedIn always resolves to true so it will not try to redirect user.
 *
 * If you already have your authorization mechanism which controls when user must be redirect to initial page or logon
 * you can skip this class.
 */
@Named
@SessionScoped
@Specializes
public class LogonMB extends AdminSession implements Serializable {
    @Inject
    private AdminConfig adminConfig;
    
    @Getter @Setter private String currentUser;
    @Getter @Setter private String email;
    @Getter @Setter private String password;
    @Getter @Setter private Boolean acceso;
    @Getter @Setter private Boolean accesoSaiiut;
    @Getter @Setter private boolean remember;
      
    @Getter    @Setter    private Bitacoraacceso nuevaBitacoraacceso;
    @Getter    @Setter    private String nombreTabla,numeroRegistro,accion;
    
    ////////////////Administración de permisos///////////////
    @Getter @Setter private List<MenuDinamico> listaModulos;
    @Getter @Setter private List<Permisos> listaPermisos;
    
    
    @Getter private UsuarioTipo usuarioTipo;
    @Getter @Setter private Usuarios usuarioAutenticado;
    @Getter @Setter private Login estuduianteAutenticado;
    @Getter @Setter private User usuarioAutenticadoShiro;
    @Getter private Personal personal;
    @Getter @Setter private ListaUsuarioClaveNomina listaUsuarioClaveNomina;
    @Getter @Setter private User listaUsuarioClaveNominaShiro;

    @EJB private EjbLogin ejbLogin;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbUtilidadesCH;
 //accede al controlador que da permisos a los usuarios
    @Inject permisosUsuarios pUsuarios;
    /*Cuando saiiut falle hacer
    Des comentar las variables :
        usuarioAutenticadoShiro
        listaUsuarioClaveNominaShiro
        resShiro
        usuarioShiro
    y comentar las variables :
        res
        usuario
    */   
    public void login() throws IOException {
//        System.out.println("com.github.adminfaces.starter.infra.security.LogonMB.login()");
        Login estudiante19 = ejbLogin.autenticar19(email, password);
        if ("estudioEgresad@s".equals(email) && password.equals("248163264")) {
            currentUser = email;
            usuarioTipo = UsuarioTipo.INVITADO;
            addDetailMessage("Bienvenido <b>" + "Usuario invitado" + "</b>");
            Faces.getExternalContext().getFlash().setKeepMessages(true);
            Faces.redirect("estudioEgresadosDestiempo.xhtml");
        }else if("aspirante".equals(email) && password.equals("aspirante")){
            currentUser = email;
            usuarioTipo = UsuarioTipo.ASPIRANTE;
            addDetailMessage("Bienvenido y bienvenida <b>" + "Aspirante" + "</b>");
            Faces.getExternalContext().getFlash().setKeepMessages(true);
            Faces.redirect("controlEscolar/fichaAdmision.xhtml");
        }else if(estudiante19 != null){
            currentUser = email;
            usuarioTipo = UsuarioTipo.ESTUDIANTE19;// ejbLogin.getTipoUsuario(email);
            estuduianteAutenticado = estudiante19;//ejbUtilToolAcademicas.autenticarUser(email, password);
            addDetailMessage("Bienvenido estimado estudiante.");
            Faces.getExternalContext().getFlash().setKeepMessages(true);
            Faces.redirect("index.xhtml");
        }else {
            usuarioTipo = ejbLogin.getTipoUsuario(email);
           // System.out.println("Usario tipo --->" +usuarioTipo);
            if(estudiante19 == null && usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) {
                addDetailMessage("La contraseña ingresada es incorrecta.");
                try{
                    Login usuario19PorLogin = ejbLogin.getUsuario19PorLogin(email);
                    if(usuario19PorLogin != null) System.out.println("Encrypted.decrypt(res.getPassword()) = " + Encrypted.decrypt(Encrypted.KEY, Encrypted.IV, usuario19PorLogin.getPassword()));
                }catch (Exception e){e.printStackTrace();}
                Faces.getExternalContext().getFlash().setKeepMessages(true);
                Faces.redirect("login.xhtml");
                return;
            }

            usuarioTipo = ejbLogin.getTipoUsuario(email);
            Boolean accesoConcedido = Boolean.FALSE;
            if (autenticarPorSaiiut()) {
                accesoSaiiut = Boolean.TRUE;
                accesoConcedido = Boolean.TRUE;
            } else if (autenticarPorShiro()) {
                accesoSaiiut = Boolean.FALSE;
                accesoConcedido = Boolean.TRUE;
            }
            System.out.println("com.github.adminfaces.starter.infra.security.LogonMB.login()"+accesoConcedido);
            if (accesoConcedido) {
                addDetailMessage("Bienvenido");
                Faces.getExternalContext().getFlash().setKeepMessages(true);
                Faces.redirect("index.xhtml");
            } else {
                addDetailMessage("No fue posible conectar con su cuenta, en caso de a ver actualizado recientemente su contraseña intente ingresar con su contraseña anterior, si el problema persiste favor de comunicarse con el departamento de Desarrollo de Software al correo: sistemas@utxicotepec.edu.mx");
                Faces.getExternalContext().getFlash().setKeepMessages(true);
                Faces.redirect("login.xhtml");
            }

        }
//        System.out.println("usuarioTipo2 = " + usuarioTipo);
    }

    public Boolean autenticarPorSaiiut() {
        System.out.println("com.github.adminfaces.starter.infra.security.LogonMB.autenticarPorSaiiut(1)");
        Usuarios res = ejbLogin.getUsuarioPorLogin(email);
        System.out.println("com.github.adminfaces.starter.infra.security.LogonMB.autenticarPorSaiiut(2)"+res);
        Usuarios usuario = ejbLogin.autenticar(email, password);
        System.out.println("com.github.adminfaces.starter.infra.security.LogonMB.autenticarPorSaiiut(3)"+usuario);
        Boolean accedio = Boolean.FALSE;
        acceso = Boolean.FALSE;
        if (res != null) {
            System.out.println("com.github.adminfaces.starter.infra.security.LogonMB.autenticarPorSaiiut()");
            if (usuario != null) {
                System.out.println("com.github.adminfaces.starter.infra.security.LogonMB.autenticarPorSaiiut()");
                currentUser = usuario.getLoginUsuario();
                usuarioAutenticado = usuario;
                listaUsuarioClaveNomina = ejbLogin.getListaUsuarioClaveNomina(currentUser);
                if ((usuarioTipo.equals(UsuarioTipo.TRABAJADOR)) && (listaUsuarioClaveNomina != null)) {
                    System.out.println("com.github.adminfaces.starter.infra.security.LogonMB.autenticarPorSaiiut()");
                    personal = ejbLogin.buscaPersona(Integer.parseInt(listaUsuarioClaveNomina.getNumeroNomina()));
                    accedio = Boolean.TRUE;
                    acceso = Boolean.TRUE;;
                }
            }
        }
        return accedio;
    }

    public Boolean autenticarPorShiro() {
        User resShiro = ejbLogin.getUsuarioPorLoginShiro(email);
        User usuarioShiro = ejbLogin.autenticarShiro(email, password);
        Boolean accedio = Boolean.FALSE;
        acceso = Boolean.FALSE;
        if (resShiro != null) {
            if (usuarioShiro != null) {
                currentUser = usuarioShiro.getUsername();
                usuarioAutenticadoShiro = usuarioShiro;
                usuarioTipo = UsuarioTipo.TRABAJADOR;
                listaUsuarioClaveNominaShiro = usuarioAutenticadoShiro;
                if ((usuarioTipo.equals(UsuarioTipo.TRABAJADOR)) && (listaUsuarioClaveNominaShiro != null)) {
                    personal = ejbLogin.buscaPersona(Integer.parseInt(usuarioAutenticadoShiro.getClaveNomina()));
                    accedio = Boolean.TRUE;
                    acceso = Boolean.TRUE;;
                }
            }
        }
        return accedio;
    }

    public void getMenuCategorias() {
        listaModulos = new ArrayList<>();
        listaModulos = pUsuarios.getListaModulos();
    }

    public void getPermisosAcceso() {
        listaPermisos = new ArrayList<>();
        pUsuarios.getPermisos();
        listaPermisos = pUsuarios.getListaPermisos();
        getMenuCategorias();
    }

    @Override
    public boolean isLoggedIn() {

        return currentUser != null;
    }

    public void agregaBitacora() {
        try {
            nombreTabla = "Login";
            numeroRegistro = "";
            accion = "Acceso";
            Date fechaActual = new Date();
            nuevaBitacoraacceso = new Bitacoraacceso();
            nuevaBitacoraacceso.setClavePersonal(Integer.parseInt(listaUsuarioClaveNomina.getNumeroNomina()));
            nuevaBitacoraacceso.setNumeroRegistro(numeroRegistro);
            nuevaBitacoraacceso.setTabla(nombreTabla);
            nuevaBitacoraacceso.setAccion(accion);
            nuevaBitacoraacceso.setFechaHora(fechaActual);
            nuevaBitacoraacceso = ejbUtilidadesCH.crearBitacoraacceso(nuevaBitacoraacceso);
            nombreTabla = "";
            numeroRegistro = "";
            accion = "";
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(LogonMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
