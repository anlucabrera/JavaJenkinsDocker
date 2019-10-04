package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Login;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.shiro.User;
import mx.edu.utxj.pye.sgi.saiiut.ejb.EjbLogin;
import mx.edu.utxj.pye.sgi.util.Encrypted;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class PersonalAdmin implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;
////////////////////////////////////////////////////////////////////////////////Datos Personales
    @Getter    @Setter    private List<Funciones> listaFuncioneSubordinado = new ArrayList<>();
    @Getter    @Setter    private List<Personal> listaPersonal = new ArrayList<>();
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal;
    @Getter    @Setter    private Personal nuevOBJPersonalSubordinado;
    @Getter    @Setter    private List<Estudiante> estudiantes = new ArrayList<>();
    @Getter    @Setter    private Aspirante aspirante;
    @Getter    @Setter    private Estudiante estudiante;
    @Getter    @Setter    private Persona persona;
    
    @Getter    @Setter    private User user = new User();
    @Getter    @Setter    private Login login = new Login();
    @Getter    @Setter    private Integer contactoDestino=0,matricula=0;
    @Getter    @Setter    private String newPwd="";

    @EJB    private mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbDatosEstudianteAdmin edea;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ep;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbFunciones ef;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo areasLogeo;

    @EJB private EjbLogin el;
    
    @PostConstruct
    public void init() {
        nuevOBJPersonalSubordinado = new Personal();
        mostrarSubordinados();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void mostrarSubordinados() {
        try {
            listaPersonal.clear();
            listaPersonal = ep.mostrarListaPersonalsPorEstatus(1);
            
            estudiantes.clear();
            estudiantes = edea.buscaEstudiantes();
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PersonalAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void asignarPersona(ValueChangeEvent event){
        contactoDestino = (Integer) event.getNewValue();    
        mostrarPerfilSubordinado();
    }
    
    public void asignarEstudiante(ValueChangeEvent event){
        matricula = (Integer) event.getNewValue();  
        mostrarPerfilEstudiante();
    }

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
    
    public void mostrarPerfilSubordinado() {
        try {                  
            nuevoOBJInformacionAdicionalPersonal = ep.mostrarInformacionAdicionalPersonalLogeado(contactoDestino);
            nuevOBJPersonalSubordinado = ep.mostrarPersonalLogeado(contactoDestino);
            nuevoOBJListaPersonal = ep.mostrarListaPersonal(contactoDestino);                   
            mostrarFuncioneSubordinado();   
            user=ep.getDatosUsuario(contactoDestino.toString());
            user.setPassword(desPwd(user.getPassword()));     
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PersonalAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarFuncioneSubordinado() {
        try {
            listaFuncioneSubordinado = new ArrayList<>();
            listaFuncioneSubordinado.clear();
            if (nuevoOBJListaPersonal.getCategoriaOperativa() == 30
                    || nuevoOBJListaPersonal.getCategoriaOperativa() == 32
                    || nuevoOBJListaPersonal.getCategoriaOperativa() == 41
                    || (nuevoOBJListaPersonal.getCategoriaOperativa() == 18 && (nuevoOBJListaPersonal.getAreaSuperior() >= 23 && nuevoOBJListaPersonal.getAreaSuperior() <= 29))
                    || (nuevoOBJListaPersonal.getCategoriaOperativa() == 34 && (nuevoOBJListaPersonal.getAreaOperativa() >= 23 && nuevoOBJListaPersonal.getAreaOperativa() <= 29))) {
                listaFuncioneSubordinado = ef.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevOBJPersonalSubordinado.getCategoriaEspecifica().getCategoriaEspecifica());
            } else {
                listaFuncioneSubordinado = ef.mostrarListaFuncionesPersonalLogeado(nuevoOBJListaPersonal.getAreaOperativa(), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevOBJPersonalSubordinado.getCategoriaEspecifica().getCategoriaEspecifica());
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PersonalAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void mostrarPerfilEstudiante() {
        try {
            estudiante = edea.buscaEstudiante(matricula);
            aspirante = estudiante.getAspirante();
            persona = aspirante.getIdPersona();
            login=persona.getLogin();
            login.setPassword(Encrypted.decrypt(Encrypted.KEY,Encrypted.IV,login.getPassword()));       
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PersonalAdmin.class.getName()).log(Level.SEVERE, null, ex);
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
        user.setUpdateC(Boolean.FALSE);
        user=ep.actualizarUser(user);
        mostrarPerfilSubordinado();
    }
    
    public void cambiarPwdestudiante(){
        try{
        login.setPassword(Encrypted.encrypt(Encrypted.KEY,Encrypted.IV,newPwd));
        login.setModificado(Boolean.FALSE);
        login=edea.actualizarUser(login);
        mostrarPerfilEstudiante();
        }catch (Exception e){
            return;
        }
    }
 
    public void imprimirValores() {}
     
}
