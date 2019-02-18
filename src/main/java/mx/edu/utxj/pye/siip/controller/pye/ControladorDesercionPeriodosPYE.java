/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.admin;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistrosUsuarios;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DtoDesercionPeriodo;
import mx.edu.utxj.pye.siip.dto.ca.DtoDesercionReprobacion;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaDtoDesercion;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbDesercionPeriodos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbPlantillasCAExcel;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEvidenciasAlineacion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named(value = "desercionPerPYE")
@ManagedBean
@ViewScoped
public class ControladorDesercionPeriodosPYE implements Serializable {

    private static final long serialVersionUID = -7906921688779844933L;
    /*Se implementa el patron set y el selecitem par ale filtro*/
    @Getter @Setter private DtoDesercionPeriodo dto;
    @Getter @Setter private DtoDesercionReprobacion dtoRep;
    @EJB
    EJBSelectItems ejbItems;
    /*sE INYECTA EL CONTROLADOR DE DESERCION POR REPORBACION PARA CONTROLAR LOS FILTROS MEDIANTE UNA SOLA VISTA*/
    @Inject ControladorDesercionReprobacionPYE cReporbacion;

    @EJB EjbDesercionPeriodos ejbDesercionPeriodos;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbEvidenciasAlineacion ejbEvidenciasAlineacion;
    @EJB EjbModulos ejbModulos;
    @EJB EjbPlantillasCAExcel ejbPlantillasCAExcel;
    

    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    //Variables para verificar permiso del usuario para visualizar apartado
    @Getter @Setter private List<ModulosRegistrosUsuarios> listaReg;
    @Getter @Setter private Integer clavePersonal;
    @Getter @Setter private Short claveRegistroCA;
    @Getter @Setter private Short claveRegistroEB;

    @PostConstruct
    public void init() {
        //        Variables que se obtendrán mediante un método 
        dto = new DtoDesercionPeriodo();

        dto.setRegistroTipo(new RegistrosTipo());
        dto.getRegistroTipo().setRegistroTipo((short) 25);
        dto.setEjesRegistro(new EjesRegistro());
        dto.getEjesRegistro().setEje(3);
        dto.setArea((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        /*INIT FILTRADO*/
        dto.setSelectItemEjercicioFiscal(ejbItems.itemEjercicioFiscalPorRegistro((short) 25));
        dto.setAreaPOA(ejbFiscalizacion.getAreaConPOA(dto.getArea()));
        dto.setClavesAreasSubordinadas(ejbFiscalizacion.getAreasSubordinadasSinPOA(dto.getAreaPOA()).stream().map(a -> a.getArea()).collect(Collectors.toList()));
        if (dto.getSelectItemEjercicioFiscal() == null) {
//            Messages.addGlobalInfo("No existen registros");
        } else {
            dto.setEjercicioFiscal((short) ejbItems.itemEjercicioFiscalPorRegistro((short) 25).get(0).getValue());
            dto.setSelectItemMes(ejbItems.itemMesesPorRegistro((short) 25, dto.getEjercicioFiscal()));

            filtroDesercion(dto.getSelectItemMes().get(0).getLabel(), dto.getEjercicioFiscal());
            /*SE INICIALIZA EL INIT DEL CONTROLADOR DE DESERCION POR REPORBACION PARA 
        QUE AMBOS FILTROS SE REFLEJEN AL MISMO TIEMPO*/
            cReporbacion.init();
        }
        /*FIN DEL INIT FILTRADO*/
         try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorDesercionPeriodosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        clavePersonal = controladorEmpleado.getNuevoOBJListaPersonal().getClave();
        claveRegistroCA = 13;
        claveRegistroEB = 41;
        consultarPermiso();
    }
    
    public void descargarPlantilla() throws IOException, Throwable{
        File f = new File(ejbPlantillasCAExcel.getPlantillaDesercion());
        Faces.sendFile(f, true);
    }

    /*INICIO DEL FILTRADO Y ELIMINACION*/
    public void seleccionarMes(Short ejercicioFiscal) {
        dto.setSelectItemMes(ejbItems.itemMesesPorRegistro((short) 25, ejercicioFiscal));
        filtroDesercion(dto.getSelectItemMes().get(0).getLabel(), ejercicioFiscal);
        cReporbacion.seleccionarMes(ejercicioFiscal);
        cReporbacion.filtroReprobacion(dto.getSelectItemMes().get(0).getLabel(), ejercicioFiscal);
    }

    public void filtroDesercion(String mes, Short ejercicio) {

        dto.setMes(mes);
        dto.setEjercicioFiscal(ejercicio);      
        dto.setListaDtoDesercion(ejbDesercionPeriodos.getListaRegistrosDesercionDto(mes, ejercicio));
        if ( dto.getListaDtoDesercion() == null) {
            Messages.addGlobalWarn("No hay registros de Deserción Académica en el mes " + mes + " y el ejercicio fiscal " + ejercicio);
        }
        /*FILTRADO DE REPROBACION*/
        cReporbacion.filtroReprobacion(mes, ejercicio);
    }
    
   
    /*FIN DEL FILTRADO*/
    
    public void abrirAlineacionPOA(ListaDtoDesercion registro){
        dto.setRegistro(registro);        
        dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(registro.getDesercionPeriodosEscolares().getRegistro()));
        cargarAlineacionXActividad();
        Ajax.update("frmAlineacion");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAlineacion').show();");
    }
    public void cargarAlineacionXActividad(){
        if(dto.getAlineacionActividad() != null){
            dto.setAlineacionEje(dto.getAlineacionActividad().getCuadroMandoInt().getEje());

            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
            dto.setAlineacionEstrategia(dto.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dto.getEstrategias());

            dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA()));
            dto.setAlineacionLinea(dto.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
    public void cargarEvidenciasPorRegistro(){
        dto.setListaEvidencias(ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(dto.getRegistro().getDesercionPeriodosEscolares().getRegistro()));
        Ajax.update("frmEvidencias");
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(ListaDtoDesercion registro){
        return ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(registro.getDesercionPeriodosEscolares().getRegistro());
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
  
     public void seleccionarRegistro(ListaDtoDesercion registro){
        dto.setRegistro(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
  
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
     public void consultarPermiso(){
        listaReg = ejbModulos.getListaPermisoPorRegistroEjesDistintos(clavePersonal, claveRegistroCA, claveRegistroEB);
        if(listaReg == null || listaReg.isEmpty()){
            Messages.addGlobalWarn("Usted no cuenta con permiso para visualizar este apartado");
        }
    }
 
}
