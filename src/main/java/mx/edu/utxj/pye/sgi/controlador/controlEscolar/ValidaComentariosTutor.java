package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ValidaComentariosRolTutor;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaCalificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



@Named
@ViewScoped
public class ValidaComentariosTutor extends ViewScopedRol implements Desarrollable {
    @Getter @Setter private ValidaComentariosRolTutor rol;

    @EJB EjbPacker packer;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    @EJB EjbCapturaCalificaciones ejbCapturaCalificaciones;
    @EJB EjbValidacionRol ejbValidacionRol;

    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        setVistaControlador(ControlEscolarVistaControlador.VALIDACION_COMENTARIOS_TUTOR);
        ResultadoEJB<Filter<PersonalActivo>> validarTutor = ejbValidacionRol.validarTutor(logon.getPersonal().getClave());
        if(!validarTutor.getCorrecto()) {mostrarMensajeResultadoEJB(validarTutor);return;}

        rol = new ValidaComentariosRolTutor(validarTutor.getValor());
        tieneAcceso = rol.tieneAcceso(rol.getTutorLogueado());

        //////////////////////////////////////////////////////////////////////
        if(!cortarFlujo()) return;
        if(!tieneAcceso){mostrarMensajeNoAcceso(); return;}

        //obtener periodos en los que ha sido tutor

        //determinar si tiene comentarios pendientes por validar y seleccionar la primer captura que debe validar
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "validación de comentarios";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
