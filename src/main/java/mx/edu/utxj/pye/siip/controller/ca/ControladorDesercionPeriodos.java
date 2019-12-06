/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ca;

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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
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
@Named(value = "desercionPer")
@ManagedBean
@ViewScoped
public class ControladorDesercionPeriodos implements Serializable {

    private static final long serialVersionUID = -7906921688779844933L;
    /*Se implementa el patron set y el selecitem par ale filtro*/
    @Getter @Setter private DtoDesercionPeriodo dto;
    @Getter @Setter private DtoDesercionReprobacion dtoRep;
    @EJB
    EJBSelectItems ejbItems;
    /*sE INYECTA EL CONTROLADOR DE DESERCION POR REPORBACION PARA CONTROLAR LOS FILTROS MEDIANTE UNA SOLA VISTA*/
    @Inject ControladorDesercionReprobacion cReporbacion;

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
       
        consultaAreaRegistro();
        
       /*INIT FILTRADO*/
        dto.setSelectItemEjercicioFiscal(ejbItems.itemEjercicioFiscalPorRegistro((short) 25));
        dto.setAreaPOA(ejbFiscalizacion.getAreaConPOA(dto.getArea().getArea()));
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
            Logger.getLogger(ControladorDesercionPeriodos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        clavePersonal = controladorEmpleado.getNuevoOBJListaPersonal().getClave();
        claveRegistroCA = 13;
        claveRegistroEB = 41;
        consultarPermiso();
    }
    
    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 13);
            if (areaRegistro == null) {
                areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 41);
                if (areaRegistro == null) {
                    dto.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
                } else {
                    dto.setArea(areaRegistro);
                }
            } else {
                dto.setArea(areaRegistro);
            }
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorDesercionPeriodos.consultaAreaRegistro(): " + e.getMessage());
        }
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
    
    public void eliminarRegistro(Integer registro, String clave){
        
           List<Integer> registroReprobacion = new ArrayList<>();
           List<Integer> registroEvidencias = new ArrayList<>();
           try {
               registroReprobacion = ejbDesercionPeriodos.buscaRegistroReprobacionDesercionAcademica(clave);
               registroEvidencias = ejbEvidenciasAlineacion.buscaRegistroEvidenciasRegistro(registro);

               if (registroReprobacion.size() > 0) {
                   List<Integer> registroEvidenciasRep = new ArrayList<>();
                   registroEvidenciasRep = ejbDesercionPeriodos.buscaRegistroEvidenciasDesercionRep(clave);

                   if (registroEvidenciasRep.size() > 0) {

                       ejbModulos.eliminarRegistroEvidencias(registroEvidenciasRep);

                   }
                   ejbModulos.eliminarRegistroParticipantes(registroReprobacion);
                   init();
                   Ajax.update("formtab");
               }

               if (registroEvidencias.size() > 0) {

                   ejbModulos.eliminarRegistroEvidencias(registroEvidencias);
                   init();
                   Ajax.update("formtab");
               }

               ejbModulos.eliminarRegistro(registro);
               init();
               Ajax.update("formtab");

           } catch (Throwable ex) {
               Logger.getLogger(ControladorDesercionPeriodos.class.getName()).log(Level.SEVERE, null, ex);
               Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
           }
    }
      
     public void eliminarRegistroReprobacion(Integer registro) {
     
      
       List<Integer> registroEvidencias = new ArrayList<>();
        try {
            registroEvidencias = ejbEvidenciasAlineacion.buscaRegistroEvidenciasRegistro(registro);
            
           
            if(registroEvidencias.size()>0){
            
                ejbModulos.eliminarRegistroEvidencias(registroEvidencias);
                ejbModulos.eliminarRegistro(registro);
                init();
                Ajax.update("formtab");
            }
            if(registroEvidencias.isEmpty()){
            
                ejbModulos.eliminarRegistro(registro);
                init();
                Ajax.update("formtab");
            }
            
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDesercionReprobacion.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
          
     }
     
     
    /*FIN DEL FILTRADO Y ELIMINACION*/
    
    public void abrirAlineacionPOA(ListaDtoDesercion registro){
        dto.setRegistro(registro);        
        dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(registro.getDesercionPeriodosEscolares().getRegistro()));
        actualizarEjes();
        cargarAlineacionXActividad();
        Ajax.update("frmAlineacion");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAlineacion').show();");
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA(), dto.getEventoActual().getEjercicioFiscal().getAnio()));
        Faces.setSessionAttribute("actividades", dto.getActividades());
    }
    
    public void actualizarEjes(){
        dto.setEjes(ejbFiscalizacion.getEjes(dto.getEventoActual().getEjercicioFiscal().getAnio(), dto.getAreaPOA()));
        if(!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }

    public void actualizarEstrategias(ValueChangeEvent event){
        dto.setAlineacionEje((EjesRegistro)event.getNewValue());
        dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
        dto.nulificarEstrategia();
        Faces.setSessionAttribute("estrategias", dto.getEstrategias());
    }

    public void actualizarLineasAccion(ValueChangeEvent event){
        dto.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA()));
        dto.nulificarLinea();
        Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
    }
    
     public void alinearRegistro(){
        Boolean alineado = ejbEvidenciasAlineacion.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getDesercionPeriodosEscolares().getRegistro());
        if(alineado){
            filtroDesercion(dto.getMes(), dto.getEjercicioFiscal());
            abrirAlineacionPOA(dto.getRegistro());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
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

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA(), dto.getEventoActual().getEjercicioFiscal().getAnio()));
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
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarAlineacion(dto.getRegistro().getDesercionPeriodosEscolares().getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
            dto.getRegistro().setActividadAlineada(null);
            dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(dto.getRegistro().getDesercionPeriodosEscolares().getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarEvidenciaEnRegistro(dto.getRegistro().getDesercionPeriodosEscolares().getRegistro(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
     public void seleccionarRegistro(ListaDtoDesercion registro){
        dto.setRegistro(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
       Map.Entry<Boolean, Integer> res = ejbEvidenciasAlineacion.registrarEvidenciasARegistro(dto.getRegistro().getDesercionPeriodosEscolares().getRegistro(), dto.getArchivos(), dto.getEventoActual(), dto.getRegistroTipo());
        if(res.getKey()){ 
            filtroDesercion(dto.getMes(), dto.getEjercicioFiscal());
            Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
        }else{ 
            Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
        }
    }
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void listaDesercionPeriodosPrevia(String rutaArchivo) {
        try {
            dto.setListaDesercionPeriodos(ejbDesercionPeriodos.getListaDesercionPeriodos(rutaArchivo));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorDesercionPeriodos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void guardaDesercionPeriodos() {
        try {
            ejbDesercionPeriodos.guardaDesercionPeriodos(dto.getListaDesercionPeriodos(), dto.getRegistroTipo(), dto.getEjesRegistro(), dto.getArea().getArea(), controladorModulosRegistro.getEventosRegistros());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorDesercionPeriodos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cancelarArchivo() {
        dto.getListaDesercionPeriodos().getDesercion().clear();

    }
    
     public void consultarPermiso(){
        listaReg = ejbModulos.getListaPermisoPorRegistroEjesDistintos(clavePersonal, claveRegistroCA, claveRegistroEB);
        if(listaReg == null || listaReg.isEmpty()){
            Messages.addGlobalWarn("Usted no cuenta con permiso para visualizar este apartado");
        }
    }
 
}
