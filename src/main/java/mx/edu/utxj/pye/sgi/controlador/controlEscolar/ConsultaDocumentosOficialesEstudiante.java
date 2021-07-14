/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ConsultaDocumentosOficialesRolEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbIntegracionExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroDocumentosOficiales;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class ConsultaDocumentosOficialesEstudiante extends ViewScopedRol implements Desarrollable{
    @Getter @Setter ConsultaDocumentosOficialesRolEstudiante rol= new ConsultaDocumentosOficialesRolEstudiante();
    @Getter Boolean tieneAcceso = false;
    @Getter private Boolean cargado = false;
    
    @Inject ConsultaDocumentosAspiranteEscolares consultaDocumentosAspiranteEscolares;
    
    @EJB EjbRegistroDocumentosOficiales ejbRegistroDocumentosOficiales;
    @EJB EjbPropiedades ep;
    @EJB EjbIntegracionExpedienteTitulacion ejbIntegracionExpedienteTitulacion;
    @Inject LogonMB logonMB;
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.CONSULTA_DOCUMENTOS_OFICIALES);
            ResultadoEJB<Estudiante> resAcceso = ejbRegistroDocumentosOficiales.validaEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
            ResultadoEJB<Estudiante> resValidacion = ejbRegistroDocumentosOficiales.validaEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
            Estudiante estudiante = resValidacion.getValor();
            tieneAcceso = rol.tieneAccesoEs(estudiante,UsuarioTipo.ESTUDIANTE19);
            tieneAcceso = Boolean.TRUE;
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            //----------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setEstudiante(estudiante);
            
            obtenerInformacionEstudiante();
            
            rol.setPeriodoActivo(ejbIntegracionExpedienteTitulacion.getPeriodoActual().getPeriodo());
            
            rol.getInstrucciones().add("Es importante que sigas con las instrucciones que te irá mostrando el sistema.");
            rol.getInstrucciones().add("Verifica que tu información sea correcta.");
            rol.getInstrucciones().add("Ten a la tu certificado de bachillerato (copia, imagen, etc) para consultar la fecha de emisión.");
            
            
           
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "consulta documentos oficiales";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    /**
     * Permite obtener información del estudiante
     */
    public void obtenerInformacionEstudiante(){
        ResultadoEJB<DtoEstudianteComplete> res = ejbRegistroDocumentosOficiales.getInformacionEstudiante(rol.getEstudiante());
        if(res.getCorrecto()){
            rol.setInformacionEstudiante(res.getValor());
            consultaDocumentosAspiranteEscolares.mostrarDocumentos(rol.getEstudiante().getAspirante());
            Ajax.update("frmDatosEst");
        }else mostrarMensajeResultadoEJB(res);
    }
    
}
