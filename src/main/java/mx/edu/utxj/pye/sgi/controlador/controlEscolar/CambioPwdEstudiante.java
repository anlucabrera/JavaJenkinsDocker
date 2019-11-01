package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.controladores.ch.PersonalAdmin;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CambioPwdRolEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPerfilEstudiante;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Login;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Named(value = "cambioPwdEstudiante")
@ViewScoped
public class CambioPwdEstudiante extends ViewScopedRol implements Desarrollable {

    @EJB
    private EjbPropiedades ep;
    @Inject LogonMB logonMB;
    @Inject PersonalAdmin admin;
    @EJB EjbPerfilEstudiante ejbPerfilEstudiante;
    @Getter Boolean tieneAcceso = false;
    @Getter @Setter CambioPwdRolEstudiante rol = new CambioPwdRolEstudiante();


    @PostConstruct
    public void init(){
        try{
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)){
                setVistaControlador(ControlEscolarVistaControlador.CAMBIO_PWD);

                ResultadoEJB<Estudiante> resAcceso = ejbPerfilEstudiante.validaEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
                ResultadoEJB<Estudiante> resValidacion = ejbPerfilEstudiante.validaEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
                Estudiante estudiante = resValidacion.getValor();
                tieneAcceso = rol.tieneAcceso(estudiante,UsuarioTipo.ESTUDIANTE19);
                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
                rol.setEstudiante(estudiante);
                //----------------------------------------------------------------------------------------------------------------------------
                if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
                if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
                rol.setNivelRol(NivelRol.OPERATIVO);
                    //TODO: Instrucciones
                    rol.getInstrucciones().add("Ingrese la contraseña actual");
                    rol.getInstrucciones().add("Ingrese la nueva contraseña");
                    rol.getInstrucciones().add("Presione cambiar contraseña.");
                    rol.getInstrucciones().add("NOTA : Asegurese de ingresar bien los datos solicitados.");

            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    /**
     * Comprueba que la contraseña actual , sea la misma que ingreso
     * tambien encripta la nueva contraseña
     */
    public void comprobarPwd(){
        ResultadoEJB<CambioPwdRolEstudiante> resComprobar = ejbPerfilEstudiante.comprobarPwd(rol);
        if(resComprobar.getCorrecto()==true){
            //TODO: Se modifica la contraseña
            modPwd();
        }else {mostrarMensajeResultadoEJB(resComprobar);}
    }

    /**
     * Modifica la contraseña del estudiante logueado
     */
    public void modPwd(){
        ResultadoEJB<Login> resMod = ejbPerfilEstudiante.cambiaPwd(rol);
        if(resMod.getCorrecto()==true){mostrarMensajeResultadoEJB(resMod);}
        else {mostrarMensajeResultadoEJB(resMod);}
    }


    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "cambioPwdEstudiante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
}
