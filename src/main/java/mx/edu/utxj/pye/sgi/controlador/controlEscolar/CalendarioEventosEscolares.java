/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CalendarioEventosEscolaresRolMultiple;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalendarioEventosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroEventosEscolares;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPermisoAperturaExtemporanea;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class CalendarioEventosEscolares extends ViewScopedRol implements Desarrollable{
    @Getter @Setter CalendarioEventosEscolaresRolMultiple rol;
    
    @EJB EjbRegistroEventosEscolares ejb;
    @EJB EjbPermisoAperturaExtemporanea ejbPermisoAperturaExtemporanea;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol personal del departamento de servicios escolares<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init(){
    if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.CALENDARIO_EVENTOS_ESCOLARES);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarUsuariosCalendarioEscolar(logon.getPersonal().getClave());//validar si es director
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new CalendarioEventosEscolaresRolMultiple(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setUsuario(usuario);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbPermisoAperturaExtemporanea.getPeriodoActual().getPeriodo());
            
            rol.getInstrucciones().add("Seleccione periodo escolar.");
            rol.getInstrucciones().add("A continuación visualizará la lista de eventos escolares registrados o por registrar.");
            rol.getInstrucciones().add("Para modificar las fechas de los eventos registrados deberá dar clic en el campo de fecha de inicio o fin, seleccionar la fecha del calendario y dar enter para guardar cambios.");
            rol.getInstrucciones().add("Para registrar un evento escolar nuevo deberá seleccionar en el calendario la fecha de inicio y fin de cada actividad, al finalizar dar clic en el botón Guardar ubicado en la parte inferior de la tabla.");
            rol.getInstrucciones().add("Si registró por error eventos de estadía, dará clic en el botón eliminar.");
           
            periodosEscolaresRegistrados();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "calendario eventos escolares";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de periodos escolares en los que existen eventos escolares registrados
     */
    public void periodosEscolaresRegistrados(){
        ResultadoEJB<List<PeriodosEscolares>> res =  ejb.getPeriodosEscolares();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setPeriodosEscolares(res.getValor());
                rol.setPeriodoEscolar(ejb.getUltimoPeriodoRegistrado().getValor());
                listaEventosEscolaresRegistrados();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de eventos escolares registrados en el periodo escolar seleccionado
     */
    public void listaEventosEscolaresRegistrados(){
        ResultadoEJB<List<DtoCalendarioEventosEscolares>> res = ejb.getCalendarioEventosEscolares(rol.getPeriodoEscolar());
        if(res.getCorrecto()){
            rol.setListaEventosRegistrados(res.getValor());
            Ajax.update("tbListaEventosEscolaresRegistrados");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite que al cambiar o seleccionar un periodo escolar se actualice la información en la tabla que corresponda
     * @param e Evento del cambio de valor
     */
    public void cambiarPeriodo(ValueChangeEvent e){
        if(e.getNewValue() instanceof PeriodosEscolares){
            PeriodosEscolares periodo = (PeriodosEscolares)e.getNewValue();
            rol.setPeriodoEscolar(periodo);
            listaEventosEscolaresRegistrados();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
}
