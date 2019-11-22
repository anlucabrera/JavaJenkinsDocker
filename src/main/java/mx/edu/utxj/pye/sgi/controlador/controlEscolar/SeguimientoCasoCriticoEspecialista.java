/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCasoCritico;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCasoCritico;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroAsesoriaTutoria;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoEstado;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class SeguimientoCasoCriticoEspecialista extends ViewScopedRol implements Desarrollable{
    @Getter     @Setter                         SeguimientoCasoCriticoRolEspecialista     rol;
    @EJB        EjbRegistroAsesoriaTutoria      ejb;
    @EJB        EjbCasoCritico                  ejbCasoCritico;
    @EJB        EjbPropiedades                  ep;
    @Inject     LogonMB                         logon;
    @Getter     Boolean                         tieneAcceso = false;
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimiento especialista caso critico";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
        try {
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_CASO_CRITICO_ESPECIALISTA);
            
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarUsuarioEspecialista(logon.getPersonal().getClave());
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}
            
            rol = new SeguimientoCasoCriticoRolEspecialista(resAcceso.getValor());
            tieneAcceso = rol.tieneAcceso(rol.getPersonalEspecializadoLogueado());
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosConCasosCriticoRegistrados(rol.getPersonalEspecializadoLogueado());
            if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejb.getPeriodoEscolarActivo().getValor().getPeriodo());
            rol.setPeriodosConCargaCasosCriticos(resPeriodos.getValor());
            cambiarPeriodo();
        }catch (Exception e){mostrarExcepcion(e); }
    }
    
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public void inicializarListaCasosCriticos(){
        rol.setListaCasosCriticos(new ArrayList<>());
        rol.setListaCasosCriticos(Collections.EMPTY_LIST);
    }
    
    public void cambiarPeriodo(){
        if(rol.getPeriodoSeleccionado() == null){
            mostrarMensaje("No hay periodo seleccionado");
            rol.setProgramasEducativos(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<AreasUniversidad>> resProgramasEducativos = ejb.getProgramasEducativosCasosCriticosEspecialista(rol.getPeriodoSeleccionado(), rol.getPersonalEspecializadoLogueado());
        if(!resProgramasEducativos.getCorrecto()) mostrarMensajeResultadoEJB(resProgramasEducativos);
        else rol.setProgramasEducativos(resProgramasEducativos.getValor());
        cambiarProgramaEducativo();
    }
    
    public void cambiarProgramaEducativo(){
        if(rol.getProgramaEducativo() == null){
            mostrarMensaje("No hay un programa educativo seleccionado");
            rol.setListaCasosCriticos(Collections.EMPTY_LIST);
            return;
        }
        actualizarListaCasosCriticos();
//        TODO: ingresar codigo para actualizar el listado de casos críticos
    }
    
    
     public void actualizarComentariosEspecialista(ValueChangeEvent event){
        DtoCasoCritico dtoCasoCritico = (DtoCasoCritico) event.getComponent().getAttributes().get("dtoCasoCritico");
        String comentariosEspecialista = (String) event.getNewValue();
        dtoCasoCritico.getCasoCritico().setComentariosEspecialista(comentariosEspecialista);
        actualizaCasoCritico(dtoCasoCritico);
    }
     
    public void actualizaCasoCritico(DtoCasoCritico dtoCasoCritico){
        dtoCasoCritico.getCasoCritico().setEstado(CasoCriticoEstado.EN_SEGUIMIENTO_ESPECIALISTA.getLabel());
        ResultadoEJB<DtoCasoCritico> resCasoCritico = ejbCasoCritico.actualizarCasoCritico(dtoCasoCritico);
        if(resCasoCritico.getCorrecto()){
            mostrarMensajeResultadoEJB(resCasoCritico);
            actualizarListaCasosCriticos();
        }else{
            mostrarMensajeResultadoEJB(resCasoCritico);
            actualizarListaCasosCriticos();
        }
    }
    
    public void abriDialogoEvidencia(DtoCasoCritico dto){
        rol.setDtoCasoCritico(dto);
        Ajax.update("frmEvidencias");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalCargaEvidenciaCasoCriticoEspecialista').show();");
    }
    
    public void subirEvidencia(){
        try {
            Map.Entry<Boolean, Integer> res = ejb.registrarEvidenciaEspecialistaCasoCritico(rol.getDtoCasoCritico(), rol.getArchivo());
            if (res.getKey()) {
                Messages.addGlobalInfo("Las evidencia se ha registrado correctamente."); 
                actualizarListaCasosCriticos();
            } else {
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(), String.valueOf(1)));
            }
        } catch (Throwable ex) {
            Logger.getLogger(RegistroTutoriaIndividual.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarEvidencia(){
        Boolean eliminado = ejb.eliminarEvidenciaCasoCriticoEspecialista(rol.getDtoCasoCritico());
        if(eliminado){
           Messages.addGlobalInfo("El archivo se eliminó de forma correcta."); 
           actualizarListaCasosCriticos();
           Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void descargarEvidencia() throws IOException {
        File f = new File(rol.getDtoCasoCritico().getCasoCritico().getEvidenciaEspecialista());
        Faces.sendFile(f, false);
    }
    
    public void actualizarListaCasosCriticos(){
        ResultadoEJB<List<DtoCasoCritico>> res = ejb.getCasosCriticosParaEspecialista(rol.getPeriodoSeleccionado(), rol.getProgramaEducativo(), rol.getPersonalEspecializadoLogueado());
        if(res.getCorrecto()){
            inicializarListaCasosCriticos();
            rol.setListaCasosCriticos(res.getValor());
        }else{
            inicializarListaCasosCriticos();
            mostrarMensaje("No se han encontrado casos críticos canalizados");
        }
    }
    
}
