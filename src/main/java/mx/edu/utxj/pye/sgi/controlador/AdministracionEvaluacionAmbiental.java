package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.EvaluacionRolMultiple;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.*;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacionDesempenioAmbiental;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaCalificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCasoCritico;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoTipo;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Named
@ViewScoped
public class AdministracionEvaluacionAmbiental extends ViewScopedRol implements Desarrollable {
    @Getter @Setter private EvaluacionRolMultiple.EvaluacionRolPersonal rol;
    @EJB EjbEvaluacionDesempenioAmbiental ejb;
    @EJB EjbPropiedades ep;
    @Getter Boolean tieneAcceso = false;

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init(){
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR))return;
        cargado = true;
        setVistaControlador(ControlEscolarVistaControlador.ADMINISTRACION_DESEMPENIO_AMBIENTAL);

        ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarPersonal(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares

        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

        ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarPersonal(logonMB.getPersonal().getClave());

        if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

        Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
        PersonalActivo personalActivo = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
        rol = new EvaluacionRolMultiple.EvaluacionRolPersonal(filtro, personalActivo);

        tieneAcceso = rol.tieneAcceso(personalActivo);

        if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------
        if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución

        if(!tieneAcceso){mostrarMensajeNoAcceso();return;}

        rol.setNivelRol(NivelRol.OPERATIVO);

    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administración evaluación ambiental";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
