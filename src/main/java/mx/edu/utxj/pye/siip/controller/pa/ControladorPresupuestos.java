/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.pa;

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
import mx.edu.utxj.pye.sgi.entity.pye2.Presupuestos;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.siip.dto.finanzas.DtoPresupuesto;
import mx.edu.utxj.pye.siip.dto.finanzas.DTOPresupuestos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.interfaces.pa.EjbPlantillasPAExcel;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEvidenciasAlineacion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.pa.EjbPresupuestos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;


/**
 *
 * @author UTXJ
 */
@Named(value = "presupuestosEjercicio")
@ManagedBean
@ViewScoped
public class ControladorPresupuestos implements Serializable{

    private static final long serialVersionUID = 4303365491897481485L;
    @Getter @Setter DtoPresupuesto dto;
    
    @EJB EJBSelectItems ejbItems;
    @EJB EjbPresupuestos  ejbPresupuestos;
    @EJB EjbFiscalizacion ejbFiscalizacion;  
    @EJB EjbModulos ejbModulos;
    @EJB EjbEvidenciasAlineacion ejbEvidenciasAlineacion;
    @EJB EjbPlantillasPAExcel ejbPlantillasPAExcel;
     
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    //Variables para verificar permiso del usuario para visualizar apartado
    @Getter @Setter private List<ModulosRegistrosUsuarios> listaReg;
    @Getter @Setter private Integer clavePersonal;
    @Getter @Setter private Short claveRegistroPA;
    @Getter @Setter private Short claveRegistroEB;
    
    @Getter @Setter private Presupuestos nuevoPresupuesto;
    
    @PostConstruct
    public void init(){
         //        Variables que se obtendrán mediante un método
        dto = new DtoPresupuesto();
        dto.setRegistroTipo(new RegistrosTipo());
        dto.getRegistroTipo().setRegistroTipo((short)15);
        dto.setEje(new EjesRegistro());
        dto.getEje().setEje(2);
        
        consultaAreaRegistro();
        
        dto.setSelectItemEjercicioFiscal(ejbItems.itemEjercicioFiscalPorRegistro((short) 15));
        
        dto.setAreaPOA(ejbFiscalizacion.getAreaConPOA(dto.getArea().getArea()));
        dto.setClavesAreasSubordinadas(ejbFiscalizacion.getAreasSubordinadasSinPOA(dto.getAreaPOA()).stream().map(a -> a.getArea()).collect(Collectors.toList()));
        if (dto.getSelectItemEjercicioFiscal() == null) {
            Messages.addGlobalInfo("No existen registros");
        } else {
            dto.setEjercicioFiscal((short) ejbItems.itemEjercicioFiscalPorRegistro((short) 15).get(0).getValue());
            dto.setSelectItemMes(ejbItems.itemMesesPorRegistro((short) 15, dto.getEjercicioFiscal()));
            filtroPresupuestos(dto.getSelectItemMes().get(0).getLabel(), dto.getEjercicioFiscal());
        }     
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorPresupuestos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        clavePersonal = controladorEmpleado.getNuevoOBJListaPersonal().getClave();
        claveRegistroPA = 5;
        claveRegistroEB = 54;
        consultarPermiso();

    }
    
    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 5);
            if (areaRegistro == null) {
                areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 54);
                if (areaRegistro == null) {
                    dto.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
                } else {
                    dto.setArea(areaRegistro);
                }
            } else {
                dto.setArea(areaRegistro);
            }
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.controller.pa.ControladorPresupuestos.consultaAreaRegistro(): " + e.getMessage());
        }
    }
    
    public void descargarPlantilla() throws IOException, Throwable{
        File f = new File(ejbPlantillasPAExcel.getPlantillaPresupuestos());
        Faces.sendFile(f, true);
    }
    
    /*
     * se inicializan los filtrados
     */
    public void seleccionarMes(Short ejercicioFiscal) {
        dto.setSelectItemMes(ejbItems.itemMesesPorRegistro((short) 15, ejercicioFiscal));
        filtroPresupuestos(dto.getSelectItemMes().get(0).getLabel(),ejercicioFiscal);
    }

    public void filtroPresupuestos(String mes, Short ejercicio) {
        dto.setMes(mes);
        dto.setEjercicioFiscal(ejercicio);
        dto.setLista(ejbPresupuestos.getRegistroDTOPresupuestos(mes, ejercicio));

        if (dto.getLista().isEmpty() || dto.getLista()== null) {
            Messages.addGlobalWarn("No hay información registrada en el mes " + mes + " y el ejercicio fiscal " + ejercicio);
        }
    }
    public void listaPresupuestosPrevia(String rutaArchivo) {
       try {
            dto.setLista(ejbPresupuestos.getListaPresupuestos(rutaArchivo));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPresupuestos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void guardaPresupuestos() {
       try {
            ejbPresupuestos.guardaPresupuestos(dto.getLista(), dto.getRegistroTipo(), dto.getEje(), dto.getArea().getArea(), controladorModulosRegistro.getEventosRegistros());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPresupuestos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cancelarArchivo(){
        dto.getLista().clear();

    }
    
    public void consultarPermiso(){
        listaReg = ejbModulos.getListaPermisoPorRegistroEjesDistintos(clavePersonal, claveRegistroPA, claveRegistroEB);
        if(listaReg == null || listaReg.isEmpty()){
            Messages.addGlobalWarn("Usted no cuenta con permiso para visualizar este apartado");
        }
    }
     
    public void eliminarRegistro(Integer registro) {

        List<Integer> registroEvidencias = new ArrayList<>();
        try {
            registroEvidencias = ejbEvidenciasAlineacion.buscaRegistroEvidenciasRegistro(registro);

            if (registroEvidencias.size() > 0) {

                ejbModulos.eliminarRegistroEvidencias(registroEvidencias);
                ejbModulos.eliminarRegistro(registro);
                init();
                Ajax.update("formMuestraDatosActivos");
            }
            if (registroEvidencias.isEmpty()) {

                ejbModulos.eliminarRegistro(registro);
                init();
                Ajax.update("formMuestraDatosActivos");
            }

        } catch (Throwable ex) {
            Logger.getLogger(ControladorPresupuestos.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
    }

    public void abrirAlineacionPOA(DTOPresupuestos registro) {
        dto.setRegistro(registro);
        dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(registro.getPresupuestos().getRegistro()));
        actualizarEjes();
        cargarAlineacionXActividad();
        Ajax.update("frmAlineacion");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAlineacion').show();");
    }

    public void actualizarActividades(ValueChangeEvent event) {
        dto.setAlineacionLinea((LineasAccion) event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA(),dto.getEventoActual().getEjercicioFiscal().getAnio()));
        Faces.setSessionAttribute("actividades", dto.getActividades());
    }

    public void actualizarEjes() {
        dto.setEjes(ejbFiscalizacion.getEjes(dto.getEventoActual().getEjercicioFiscal().getAnio(), dto.getAreaPOA()));
        if (!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null) {
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }

    public void actualizarEstrategias(ValueChangeEvent event) {
        dto.setAlineacionEje((EjesRegistro) event.getNewValue());
        dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
        dto.nulificarEstrategia();
        Faces.setSessionAttribute("estrategias", dto.getEstrategias());
    }

    public void actualizarLineasAccion(ValueChangeEvent event) {
        dto.setAlineacionEstrategia((Estrategias) event.getNewValue());
        dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA()));
        dto.nulificarLinea();
        Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
    }

    public void alinearRegistro() {
        Boolean alineado = ejbEvidenciasAlineacion.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getPresupuestos().getRegistro());
        if (alineado) {
            filtroPresupuestos(dto.getMes(), dto.getEjercicioFiscal());
            abrirAlineacionPOA(dto.getRegistro());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        } else {
            Messages.addGlobalError("El registro no pudo alinearse.");
        }
    }

    public void cargarAlineacionXActividad() {
        if (dto.getAlineacionActividad() != null) {
            dto.setAlineacionEje(dto.getAlineacionActividad().getCuadroMandoInt().getEje());

            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
            dto.setAlineacionEstrategia(dto.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dto.getEstrategias());

            dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA()));
            dto.setAlineacionLinea(dto.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA(), dto.getEventoActual().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dto.getActividades());

        } else {
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }

    public void cargarEvidenciasPorRegistro() {
        dto.setListaEvidencias(ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(dto.getRegistro().getPresupuestos().getRegistro()));
        Ajax.update("frmEvidencias");
    }

    public List<EvidenciasDetalle> consultarEvidencias(DTOPresupuestos registro) {
        return ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(registro.getPresupuestos().getRegistro());
    }

    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException {
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }

    public void eliminarAlineacion() {
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarAlineacion(dto.getRegistro().getPresupuestos().getRegistro());
        if (eliminado) {
            Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
            dto.getRegistro().setActividadAlineada(null);
            dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(dto.getRegistro().getPresupuestos().getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
        } else {
            Messages.addGlobalError("La alineación no pudo eliminarse.");
        }
    }

    public void eliminarEvidencia(EvidenciasDetalle evidencia) {
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarEvidenciaEnRegistro(dto.getRegistro().getPresupuestos().getRegistro(), evidencia);
        if (eliminado) {
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
        } else {
            Messages.addGlobalError("El archivo no pudo eliminarse.");
        }
    }

    public void seleccionarRegistro(DTOPresupuestos registro) {
        dto.setRegistro(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }

    public void subirEvidencias() {
        Map.Entry<Boolean, Integer> res = ejbEvidenciasAlineacion.registrarEvidenciasARegistro(dto.getRegistro().getPresupuestos().getRegistro(), dto.getArchivos(), dto.getEventoActual(), dto.getRegistroTipo());
        if (res.getKey()) {
            filtroPresupuestos(dto.getMes(), dto.getEjercicioFiscal());
            Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
        } else {
            Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(), String.valueOf(dto.getArchivos().size())));
        }
    }

    public void forzarAperturaEvidenciasDialogo() {
        if (dto.getForzarAperturaDialogo()) {
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void editarRegistro(DTOPresupuestos registro){
        dto.setRegistro(registro);
        nuevoPresupuesto = dto.getRegistro().getPresupuestos();
        Ajax.update("frmModalEdicion");
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionDialogo();
    }
    
    public void forzarAperturaEdicionDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicion').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void guardarEdicion() {
         try {
            nuevoPresupuesto = ejbPresupuestos.actualizarPresupuesto(nuevoPresupuesto);
            Ajax.update("formMuestraDatosActivos");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorPresupuestos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
