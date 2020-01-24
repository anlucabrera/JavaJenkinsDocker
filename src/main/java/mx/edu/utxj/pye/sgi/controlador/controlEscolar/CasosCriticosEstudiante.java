/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CasosCriticosRolEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCasoCritico;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCasoCritico;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbEventoEscolar;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class CasosCriticosEstudiante extends ViewScopedRol implements Desarrollable{
    @Getter             @Setter                 private                     CasosCriticosRolEstudiante              rol;
    @Getter             @Setter                 private                     Boolean                                 tieneAcceso = false;
    
    @EJB                EjbPropiedades          ep;
    @EJB                EjbCasoCritico          ejbCasosCriticos;
    @EJB                EjbPacker               ejbPacker;
    @EJB                EjbValidacionRol        ejbValidacionRol;
    @EJB                EjbEventoEscolar        ejbEventoEscolarEJB;
    
    @Inject             LogonMB             logonMB;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "Casos Críticos Estudiante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
        try {
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)){
                setVistaControlador(ControlEscolarVistaControlador.CONSULTA_CASOS_CRITICOS);
                ResultadoEJB<Estudiante> resAcceso = ejbValidacionRol.validarEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resAcceso.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}
                rol = new CasosCriticosRolEstudiante(ejbPacker.packEstudiante(resAcceso.getValor()).getValor());
                tieneAcceso = rol.tieneAcceso(rol.getDtoEstudiante(), UsuarioTipo.ESTUDIANTE19);
                ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejbCasosCriticos.consultaPeriodosEscolaresCasoCriticoEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
                
                if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
                if(verificarInvocacionMenu()) return;
                if(!validarIdentificacion()) return;
                if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
                
                rol.setPeriodoActivo(ejbEventoEscolarEJB.getPeriodoActual());
                rol.setPeriodosEscolares(resPeriodos.getValor());
                cambiarPeriodo();
            }
        } catch (Exception e) {mostrarExcepcion(e);}
    }
    
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public void cambiarPeriodo(){
        if(rol.getPeriodoSeleccionado() == null){
            mostrarMensaje("No hay periodo escolar seleccionado");
            rol.setListaCasosCriticos(Collections.EMPTY_LIST);
            return;
        }
//        TODO:
        actualizarListadoCasosCriticos();
    }
    
    public void actualizarListadoCasosCriticos(){
        rol.getDtoEstudiante().getInscripcionActiva().getInscripcion().getMatricula();
        rol.getDtoEstudiante().getInscripciones().stream().forEach((t) -> {
            if(Objects.equals(t.getPeriodo().getPeriodo(), rol.getPeriodoSeleccionado().getPeriodo())){
                ResultadoEJB<List<DtoCasoCritico>> res = ejbCasosCriticos.identificarPorEsdudiante(t.getInscripcion());
                rol.setListaCasosCriticos(res.getValor());
            }
        });
    }
    
    public void detalleCasoCritico(DtoCasoCritico dtoCasoCritico){
        rol.setDtoCasoCriticoSeleccionado(dtoCasoCritico);
        rol.setFechaCasoCritico(parsearFechaCasoCritico(rol.getDtoCasoCriticoSeleccionado().getCasoCritico().getFechaRegistro()));
        if(rol.getDtoCasoCriticoSeleccionado().getCasoCritico().getFechaCierre() == null){
            rol.setFechaCierreCasoCritico("El caso crítico sigue abierto");
        }else{
            rol.setFechaCierreCasoCritico(parsearFechaCasoCritico(rol.getDtoCasoCriticoSeleccionado().getCasoCritico().getFechaCierre()));
        }
        Ajax.update("frmDetalles");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalDetalleCasosCriticos').show();");
    }
    
    public String parsearFechaCasoCritico(Date fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        String horaCadena = formatter.format(fecha);
        return horaCadena;
    }
    
    public void descargarEvidenciaTutor() throws IOException {
        File f = new File(rol.getDtoCasoCriticoSeleccionado().getCasoCritico().getEvidenciaTutor());
        Faces.sendFile(f, false);
    }
    
    public void descargarEvidenciaEspecialista() throws IOException {
        File f = new File(rol.getDtoCasoCriticoSeleccionado().getCasoCritico().getEvidenciaEspecialista());
        Faces.sendFile(f, false);
    }
    
}
