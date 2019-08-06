package com.github.adminfaces.starter.infra.security;

import com.github.adminfaces.template.session.AdminSession;
import org.omnifaces.util.Faces;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;
import javax.faces.context.FacesContext;
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
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbUtilToolAcademicas;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.MenuDinamico;
import mx.edu.utxj.pye.sgi.entity.ch.Permisos;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.shiro.User;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
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
    @Getter @Setter private boolean remember;
      
    @Getter    @Setter    private Bitacoraacceso nuevaBitacoraacceso;
    @Getter    @Setter    private String nombreTabla,numeroRegistro,accion;
    
    ////////////////Administración de permisos///////////////
    @Getter @Setter private List<MenuDinamico> listaModulos;
    @Getter @Setter private List<Permisos> listaPermisos;
    
    
    @Getter private UsuarioTipo usuarioTipo;
    @Getter @Setter private Usuarios usuarioAutenticado;
    @Getter @Setter private User usuarioAutenticadoShiro;
    @Getter private Personal personal;
    @Getter @Setter private ListaUsuarioClaveNomina listaUsuarioClaveNomina;
    @Getter @Setter private User listaUsuarioClaveNominaShiro;

    @EJB private EjbLogin ejbLogin;
    @EJB private Facade f;   
    @EJB private EjbUtilToolAcademicas ejbUtilToolAcademicas;
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
        System.out.println("com.github.adminfaces.starter.infra.security.LogonMB.login()");

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
        }else if(ejbUtilToolAcademicas.autenticarUser(email, password) != null){
            currentUser = email;
            usuarioTipo = UsuarioTipo.ASPIRANTE;
            addDetailMessage("Bienvenido");
            Faces.getExternalContext().getFlash().setKeepMessages(true);
            Faces.redirect("index.xhtml");
        }else {
            Usuarios res = ejbLogin.getUsuarioPorLogin(email);
            Usuarios usuario = ejbLogin.autenticar(email, password);
//            User resShiro = ejbLogin.getUsuarioPorLoginShiro(email);
//            User usuarioShiro = ejbLogin.autenticarShiro(email, password);

//            if (resShiro == null) {
            if (res == null) {
                addDetailMessage("El usuario ingresado no existe.");
                Faces.getExternalContext().getFlash().setKeepMessages(true);
                Faces.redirect("login.xhtml");
            } else {
//                if (usuarioShiro != null) {
                if (usuario != null) {
                    currentUser = usuario.getLoginUsuario();
//                    currentUser = usuarioShiro.getUsername();
                    usuarioAutenticado = usuario;
//                    usuarioAutenticadoShiro = usuarioShiro;
                    usuarioTipo = ejbLogin.getTipoUsuario(usuario);
//                    usuarioTipo = UsuarioTipo.TRABAJADOR;
                    listaUsuarioClaveNomina = ejbLogin.getListaUsuarioClaveNomina(currentUser);
//                    listaUsuarioClaveNominaShiro = usuarioAutenticadoShiro;
                    if (usuarioTipo.equals(UsuarioTipo.TRABAJADOR)) {
                        f.setEntityClass(Personal.class);
//                        personal = (Personal) f.find(Integer.parseInt(usuarioAutenticadoShiro.getClaveNomina()));  
                        personal = (Personal) f.find(Integer.parseInt(listaUsuarioClaveNomina.getNumeroNomina()));
                        listaUsuarioClaveNomina.getNumeroNomina();
//                    agregaBitacora();
//                    getPermisosAcceso();
                    }
                    System.out.println("com.github.adminfaces.starter.infra.security.LogonMB.login() tipo: " + usuarioTipo);
//                    addDetailMessage("Bienvenido <b>" + usuario.getPersonas().getNombre() + "</b>");
                    addDetailMessage("Bienvenido");
                    Faces.getExternalContext().getFlash().setKeepMessages(true);
                    Faces.redirect("index.xhtml");
                } else {
                    addDetailMessage("La contraseña ingresada es incorrecta.");
                    Faces.getExternalContext().getFlash().setKeepMessages(true);
                    Faces.redirect("login.xhtml");
                }
            }
        }
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
