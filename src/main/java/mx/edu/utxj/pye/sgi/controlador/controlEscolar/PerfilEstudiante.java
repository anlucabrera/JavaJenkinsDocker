package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.controladores.ch.PersonalAdmin;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CambioPwdRolEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.PerfilRolEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPerfilEstudiante;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.security.cert.TrustAnchor;
import java.util.Map;

@Named(value = "perfilEstudiante")
@ViewScoped
public class PerfilEstudiante extends ViewScopedRol implements Desarrollable {

    @EJB
    private EjbPropiedades ep;
    @Inject LogonMB logonMB;
    @Inject PersonalAdmin admin;
    @EJB EjbPerfilEstudiante ejbPerfilEstudiante;
    @EJB EjbCedulaIdentificacion ejbCedulaIdentificacion;
    @Getter Boolean tieneAcceso = false;
    @Getter @Setter PerfilRolEstudiante rol = new PerfilRolEstudiante();


    @PostConstruct
    public void init(){
        try{
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)){
                setVistaControlador(ControlEscolarVistaControlador.PERFIL_ESTUDIANTE);

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
                //todo: apartados para cargar todos dus datos de la cedula de identificacion
                rol.setApartados(ejbCedulaIdentificacion.getApartados());
                //TODO: Obtiene todos los datos de la cedula
                ResultadoEJB<DtoCedulaIdentificacion> resCedula = ejbCedulaIdentificacion.getCedulaIdentificacion(rol.getEstudiante().getMatricula());
                if(resCedula.getCorrecto()==true){rol.setCedulaIdentificacion(resCedula.getValor());}
                else {mostrarMensajeResultadoEJB(resCedula);}
                rol.setDatosFamiliares(rol.getEstudiante().getAspirante().getDatosFamiliares());
                rol.setMedioComunicacion(rol.getEstudiante().getAspirante().getIdPersona().getMedioComunicacion());
//                rol.setTutorFamiliar(rol.getEstudiante().getAspirante().getDatosFamiliares().getTutor());
                rol.setNivelRol(NivelRol.OPERATIVO);
                    //TODO: Instrucciones
                    rol.getInstrucciones().add("Ingrese los datos que desea actualizar.");
                    rol.getInstrucciones().add("Verifique que los datos sean correctos");
                    rol.getInstrucciones().add("Presione el botón actualizar, ubicado en la parte superior derecha.");
                    rol.getInstrucciones().add("Confirme que desea actualizar los datos, presionando el boton 'Si'");
                     rol.getInstrucciones().add("El sistema le mostrará un mensaje cuando los datos hayan sido actualizados.");

            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    /**
     * Actualiza los datos del estudiante (Medios de cumunicación, datos del tutor familiar,datos familiares)
     */
    public void updateDatosEstudiante(){
      ResultadoEJB<PerfilRolEstudiante> resUpdate = ejbPerfilEstudiante.upadateDatosEstudiante(rol);
      if(resUpdate.getCorrecto()==true){mostrarMensajeResultadoEJB(resUpdate);}
      else {mostrarMensajeResultadoEJB(resUpdate);}

    }


    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "perfilEstudiante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
}
