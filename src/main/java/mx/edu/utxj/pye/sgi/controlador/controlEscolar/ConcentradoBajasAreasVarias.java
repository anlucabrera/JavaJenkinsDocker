/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ConcentradoBajasRolVarios;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoConcentradoBajas;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
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
public class ConcentradoBajasAreasVarias extends ViewScopedRol implements Desarrollable{
    @Getter @Setter ConcentradoBajasRolVarios rol;
    
    @EJB EjbRegistroBajas ejb;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    @Getter Boolean tieneAcceso = false;
    @Inject GeneracionFormatoBaja generacionFormatoBaja;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por personal de servicios escolares<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    
    @PostConstruct
    public void init(){
    if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
    cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.CONCENTRADO_BAJAS);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarServiciosEscolares(logonMB.getPersonal().getClave());//validar si es personal de servicios escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personal = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ConcentradoBajasRolVarios(filtro, personal);
            tieneAcceso = rol.tieneAcceso(personal);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(personal);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
            
            rol.getInstrucciones().add("Seleccione periodo escolar para consultar bajas registradas durante ese periodo.");
            rol.getInstrucciones().add("Seleccione programa educativo.");
            rol.getInstrucciones().add("En la columna OPCIONES, usted puede: Consultar materias reprobadas, Generar formato y Eliminar el registro de baja.");
            rol.getInstrucciones().add("El botón de Consultar materias reprobadas se habilita únicamente en el caso de que la baja haya sido por reprobación.");
            rol.getInstrucciones().add("Para generar el formato de baja de clic en el botón Generar formato.");
            rol.getInstrucciones().add("Dar clic en botón Eliminar registro, para eliminar el registro de la baja y cambiar la situación académica del estudiante en sistema.");
           
            ciclosBajasRegistradas();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "cocentrado bajas";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de periodo escolares en lo que se hay bajas registradas
     */
    public void ciclosBajasRegistradas(){
        ResultadoEJB<List<CiclosEscolares>> res = ejb.getCiclosBajas();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setCiclos(res.getValor());
                rol.setCiclo(rol.getCiclos().get(0));
                periodosBajasRegistradas();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite obtener la lista de periodo escolares en lo que se hay bajas registradas
     */
    public void periodosBajasRegistradas(){
        ResultadoEJB<List<PeriodosEscolares>> res = ejb.obtenerPeriodosCicloBajas(rol.getCiclo());
        if(res.getCorrecto()){
            rol.setPeriodos(res.getValor());
            rol.setPeriodo(rol.getPeriodos().get(0));
            listaBajasPeriodoCategoria();
            listaBajasPeriodoTipo();
            listaBajasPeriodoCausa();
            calcularTotales();
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de bajas registradas en el periodo seleccionado
     */
    public void listaBajasPeriodoCategoria(){
        if(rol.getPeriodo() == null) return;
        ResultadoEJB<List<DtoConcentradoBajas>> res = ejb.generarConcentradoBajasPorCategoria(rol.getPeriodo());
        if(res.getCorrecto()){
            rol.setListaConcentradoPorCategoria(res.getValor());
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite obtener la lista de bajas registradas en el periodo seleccionado
     */
    public void listaBajasPeriodoTipo(){
        if(rol.getPeriodo() == null) return;
        ResultadoEJB<List<DtoConcentradoBajas>> res = ejb.generarConcentradoBajasPorTipo(rol.getPeriodo());
        if(res.getCorrecto()){
            rol.setListaConcentradoPorTipo(res.getValor());
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite obtener la lista de bajas registradas en el periodo seleccionado
     */
    public void listaBajasPeriodoCausa(){
        if(rol.getPeriodo() == null) return;
        ResultadoEJB<List<DtoConcentradoBajas>> res = ejb.generarConcentradoBajasPorCausa(rol.getPeriodo());
        if(res.getCorrecto()){
            rol.setListaConcentradoPorCausa(res.getValor());
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite obtener la lista de expedientes registrados en la generación y nivel educativo seleccionado previamente
     */
    public void calcularTotales(){
        rol.setBajasCatAcademicos(rol.getListaConcentradoPorCategoria().stream().filter(f-> f.getCategoriaConcentrado().equals("Académicos")).mapToInt(DtoConcentradoBajas::getNumeroBajas).sum());
        rol.setBajasCatEconomicos(rol.getListaConcentradoPorCategoria().stream().filter(f-> f.getCategoriaConcentrado().equals("Económicos")).mapToInt(DtoConcentradoBajas::getNumeroBajas).sum());
        rol.setBajasCatPersonales(rol.getListaConcentradoPorCategoria().stream().filter(f-> f.getCategoriaConcentrado().equals("Personales o Individuales")).mapToInt(DtoConcentradoBajas::getNumeroBajas).sum());
        
        rol.setBajasTipDefinitiva(rol.getListaConcentradoPorTipo().stream().filter(f-> f.getCategoriaConcentrado().equals("Definitiva")).mapToInt(DtoConcentradoBajas::getNumeroBajas).sum());
        rol.setBajasTipTemporal(rol.getListaConcentradoPorTipo().stream().filter(f-> f.getCategoriaConcentrado().equals("Temporal")).mapToInt(DtoConcentradoBajas::getNumeroBajas).sum());
    
    }
}
