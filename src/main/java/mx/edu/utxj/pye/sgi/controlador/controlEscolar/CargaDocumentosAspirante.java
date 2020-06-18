package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CargaDocumentosRolAspirante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCargaDocumentosAspirante;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import javax.faces.event.ValueChangeEvent;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoAspiranteProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Guardable;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Faces;



/**
 * La selección del grupo, docente y del periodo deben ser directos de un control de entrada
 */
@Named
@SessionScoped
public class CargaDocumentosAspirante extends ViewScopedRol implements Desarrollable{

    @EJB EjbCargaDocumentosAspirante ejb;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Inject UtilidadesCH utilidadesCH;
    @Getter Boolean tieneAcceso = false;
    @Getter @Setter CargaDocumentosRolAspirante rol;
    @Getter @Setter CargaArchivosControlador cargaArchivosControlador;
//    
//    @Getter @Setter private String curp;
//    @Getter @Setter private Integer folioAdmision;
//    @Getter @Setter private Boolean validacionCurpFolio;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por area superior y categiría operativa<br/>
     *      La referencia al director si es que el usuario logueado es efectivamente un director por medio del filtro de rol<br/>
     *      El programa educativo al que pertenece el director por medio de operación segura antierror<br/>
     *      El DTO del rol<br/>
     *      La lista de periodos escolares en forma descendente por medio de operación segura antierror<br/>
     *      EL mapa de programas con grupos por medio de operación segura antierror ordenando programas por areas, niveles y nombre del programa y los grupos por grado y letra
     */


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;


    @PostConstruct
    public void init(){
        try{
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ASPIRANTE)) return;
        cargado = true;
        setVistaControlador(ControlEscolarVistaControlador.CARGA_DOCUMENTOS_ASPIRANTE);
        ResultadoEJB<Boolean> resAcceso = ejb.verficaAcceso(logonMB.getUsuarioTipo());
        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
        ResultadoEJB<Boolean> resValidacion = ejb.verficaAcceso(logonMB.getUsuarioTipo());
        if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
        rol = new CargaDocumentosRolAspirante();
        tieneAcceso = resAcceso.getValor();
        rol.setTieneAcceso(tieneAcceso);
        if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
        ResultadoEJB<EventoEscolar> resEvento = ejb.verificaEvento();
        mostrarMensajeResultadoEJB(resAcceso);
        if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
        rol.setEventoEscolar(resEvento.getValor());
        // Se busca un proceso de inscripción activo
        ResultadoEJB<ProcesosInscripcion> resProcesoI = ejb.getProcesosInscripcionActivo();
        if(resProcesoI.getCorrecto()==true){rol.setProcesosInscripcion(resProcesoI.getValor());}
        else {tieneAcceso=false;}
        // ----------------------------------------------------------------------------------------------------------------------------------------------------------
        if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
        if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
        rol.setNivelRol(NivelRol.OPERATIVO);
        
        }catch (Exception e) {
            cargado = false;
        }
    }

    public void validarCurpFolio(){
       if(rol.getCurp()== null || rol.getFolioAdmision() == null) return;
        ResultadoEJB<Aspirante> res = ejb.validarCurpFolio(rol.getCurp(), rol.getFolioAdmision(), rol.getProcesosInscripcion());
        if(res.getCorrecto()){
            rol.setAspirante(res.getValor());
            if(res.getValor()==null){
                rol.setValidacionCurpFolio(false);
                addDetailMessage("Por favor verifica tus datos");
                Faces.getExternalContext().getFlash().setKeepMessages(true);
            }else{
                rol.setValidacionCurpFolio(true);
                addDetailMessage("Se han validado los datos correctamente");
                Faces.getExternalContext().getFlash().setKeepMessages(true);
                Faces.redirect("controlEscolar/aspirante/cargaDocumentos.xhtml");
                mostrarDocumentos(rol.getAspirante());
            }
        }else mostrarMensajeResultadoEJB(res);  
       
    }
    
    public void mostrarDocumentos(Aspirante aspirante){
        ResultadoEJB<List<DtoDocumentoAspirante>> res = ejb.getDocumentoAspirante(aspirante);
        if(res.getCorrecto()){
            rol.setListaDocumentoAspirante(res.getValor());
            Ajax.update("frmDocsAsp");
        }else mostrarMensajeResultadoEJB(res);  
       
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "carga documentos aspirante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
   
}
