package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.inject.Inject;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.shiro.User;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.saiiut.ejb.EjbLogin;
import mx.edu.utxj.pye.sgi.util.Encrypted;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class DatosAcceso implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;
////////////////////////////////////////////////////////////////////////////////Datos Personales

    
    @Getter    @Setter    private User user = new User();
    @Getter    @Setter    private Integer contactoDestino=0;
    @Getter    @Setter    private String newPwd="";
    
    @Inject    ControladorEmpleado controladorEmpleado;

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ep;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo areasLogeo;
    @EJB private EjbLogin el;
    
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        contactoDestino=controladorEmpleado.getNuevoOBJListaPersonal().getClave();          
        mostrarCuentaAcceso();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String buscaArea(Short area) {
        try {
            if (area != null) {
                AreasUniversidad areaU = new AreasUniversidad();
                AreasUniversidad areaS = new AreasUniversidad();
                areaU = areasLogeo.mostrarAreasUniversidad(area);
                areaS = areasLogeo.mostrarAreasUniversidad(areaU.getAreaSuperior());
                return areaS.getNombre();
            } else {
                return "Nombre Área";
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public void mostrarCuentaAcceso() {
        try {      
            user=ep.getDatosUsuario(contactoDestino.toString());
            user.setPassword(desPwd(user.getPassword()));     
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(DatosAcceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String desPwd(String pwd) {
        String str1 = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz1234567890!$%&/()=?¿¡,.-;:_ ";

        String str2 = "Uqyh.-aJ,g4TPVDE/2WZ15uBC78b3X:_ 6AOHIYrstNFGvcjñzÑdef)=?¿¡omKL90!(i$%&QRklwxnMSp;";

        String str3 = "";
        for (int i = 0; i < pwd.length(); i++)
        {
          int j = 0;
          while (j < str2.length())
          {
            int k = pwd.charAt(i);
            int m = str2.charAt(j);
            if (k == m)
            {
              if ((i + 1) % 2 == 0)
              {
                str3 = String.valueOf(String.valueOf(String.valueOf(str3))) + String.valueOf(String.valueOf(String.valueOf(str1.charAt(j))));break;
              }
              if (j == str1.length())
              {
                str3 = String.valueOf(String.valueOf(String.valueOf(str3))) + String.valueOf(String.valueOf(String.valueOf(str1.charAt(1))));break;
              }
              str3 = String.valueOf(String.valueOf(String.valueOf(str3))) + String.valueOf(String.valueOf(String.valueOf(str1.charAt(j - 1))));

              break;
            }
            j++;
          }
        }
        String str4 = str3;
        return str4;
    }
    
    public void cambiarPwd(){
        user.setPassword(el.encriptarContrasena(newPwd));
        user.setUpdateC(Boolean.TRUE);
        user=ep.actualizarUser(user);
        mostrarCuentaAcceso();
    }

    public  String encriptaPassword(String password) throws Exception{
        String contraseñaEncriptada = "";
        String key = "92AE31A79FEEB2A3";
        String iv = "0123456789ABCDEF";
        contraseñaEncriptada = Encrypted.encrypt(key, iv, password);

        return contraseñaEncriptada;
    }

    public  String desencriptaPassword(String password) throws Exception{
        String contraseñaDesencriptada = "";
        String key = "92AE31A79FEEB2A3";
        String iv = "0123456789ABCDEF";
        contraseñaDesencriptada = Encrypted.decrypt(key, iv, password);

        return contraseñaDesencriptada;
    }
 
    public void imprimirValores() {}
     
}
