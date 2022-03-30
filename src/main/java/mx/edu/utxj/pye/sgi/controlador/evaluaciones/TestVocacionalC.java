/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.evaluaciones;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.TestVocacionalRolEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbTestVocacional;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestVocacional;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorTestVocacional;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class TestVocacionalC extends ViewScopedRol implements Desarrollable{
    private static final long serialVersionUID = 7411723377330728427L;
    @Getter             @Setter                     private                             TestVocacionalRolEstudiante                         rol = new TestVocacionalRolEstudiante();
    @Getter             @Setter                     private                             Boolean                                             tieneAcceso = false;
    @Getter             @Setter                     private                             Boolean                                             finalizado = false;
    @Getter             @Setter                     String                              valor;
    
    @EJB                EjbPropiedades              ep;
    @EJB                EjbTestVocacional           ejbTestVocacional;
    @EJB                EjbPacker                   packer;
    
    @Inject             LogonMB                     logonMB;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "test vocacional";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        rol.setMostrarIndexProduccion(mostrar(request, map.containsValue(valor)));
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
        try {
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)){
                setVistaControlador(ControlEscolarVistaControlador.TEST_VOCACIONAL);
                ResultadoEJB<Estudiante> resAcceso = ejbTestVocacional.validarEstudiante(logonMB.getCurrentUser());
                if(!resAcceso.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}
                rol = new TestVocacionalRolEstudiante(packer.packEstudianteGeneral(resAcceso.getValor()).getValor());
                tieneAcceso = rol.tieneAcceso(rol.getDtoEstudiante(), UsuarioTipo.ESTUDIANTE19);
                rol.setNivelRol(NivelRol.SUPERIOR);
                if(verificarInvocacionMenu()) return;
                if(!validarIdentificacion()) return;
                if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
                finalizado = Boolean.FALSE;
                rol.setCarreraInteresSeleccion(Boolean.FALSE);
                iniciarComprobaciones();
            }
        } catch (Exception e) {mostrarExcepcion(e);}
    }
    
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    /**
     * Obtiene y verifica los resultados del test vocacional, crea un nuevo test si no existe el registro aún.
     */
    public void iniciarComprobaciones(){
        cargarApartados();
        ResultadoEJB<TestVocacional> resResultados = ejbTestVocacional.getResultados(rol.getDtoEstudiante(), rol.getRespuestas());
        if(resResultados.getCorrecto()){rol.setResultado(resResultados.getValor());}
        else {mostrarMensajeResultadoEJB(resResultados);}
        finalizado = ejbTestVocacional.actualizarResultado(rol.getResultado());
        comprobar();
    }
    
    /**
     * Realiza el vaciado de los apartados y respuestas correspondientes para mostrar en la vista
     */
    public void cargarApartados(){
        try {
            rol.setRespuestas(new HashMap<>());
            rol.setApartados(ejbTestVocacional.apartados().getValor());
            rol.setRespuestasPosibles(ejbTestVocacional.respuestas().getValor());
        } catch (Exception e) {mostrarExcepcion(e);}
    }
    
    /**
     * Comprueba si el test vocacional ya está terminado
    */
    public void comprobar() {
        Comparador<TestVocacional> comparador = new ComparadorTestVocacional();
        finalizado = comparador.isCompleto(rol.getResultado());
        if (finalizado == true) {
            ResultadoEJB<TestVocacional> actualizarCompleto = ejbTestVocacional.actualizarCompleto(rol.getResultado());
            rol.setResultado(actualizarCompleto.getValor());
            if (!actualizarCompleto.getCorrecto()) {
                mostrarMensajeResultadoEJB(actualizarCompleto);
            }
            rol.setCarreraInteresSeleccion(rol.getResultado().getCarreraInteresOu()); 
            if(rol.getResultado().getRespuestaCarreraInteresOu() != null){
                rol.setCarreraInteres(rol.getResultado().getRespuestaCarreraInteresOu());
            }
            rol.setResultadosCarreras(ejbTestVocacional.obtenerResultadosCompletosTestVocacional(rol.getResultado()).getValor());
            Ajax.update("frmPrincipal");
        }
    }
    
    public void guardar(ValueChangeEvent e) throws ELException{
        UIComponent id = (UIComponent)e.getSource();
        if(e.getNewValue() != null){
            setValor(e.getNewValue().toString());
        }else{
            setValor(e.getOldValue().toString());
        }
        ejbTestVocacional.actualizarRespuestaPregunta(rol.getResultado(), id.getId(), Short.valueOf(getValor()), rol.getRespuestas());
        finalizado = ejbTestVocacional.actualizarResultado(rol.getResultado());
        comprobar();
    }
    
    public void actualizarValorBooleanCarreraInteresOu(ValueChangeEvent event) throws ELException{
        if(event == null){mostrarMensaje("No se ha enviado ningún dato para actualizar");return;}
        if(event.getNewValue() == null){mostrarMensaje("Favor de seleccionar un valor, verdadero o falso");return;}
        rol.getResultado().setCarreraInteresOu((Boolean)event.getNewValue());
        rol.setCarreraInteresSeleccion((Boolean)event.getNewValue());
        finalizado = ejbTestVocacional.actualizarResultado(rol.getResultado());
    }
    
    public void actualizarCarreraInteres(ValueChangeEvent event) throws ELException{
        if(event == null){mostrarMensaje("No se ha enviado ningún dato para actualizar");return;}
        if(event.getNewValue() == null){mostrarMensaje("Favor de ingresar una carrera de interés nueva");return;}
        if("".equals((String) event.getNewValue())) {mostrarMensaje("Ha enviado un campo vacío");return;}
        rol.getResultado().setRespuestaCarreraInteresOu((String) event.getNewValue());
        rol.setCarreraInteres((String) event.getNewValue());
        finalizado = ejbTestVocacional.actualizarResultado(rol.getResultado());
    }
    
//    public Boolean consultarFinalizado(){
//        
//    }
    
}
