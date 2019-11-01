/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.pye;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;
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
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosEnfermeriaCicloPeriodos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DTOServiciosEnfemeriaCicloPeriodos;
import mx.edu.utxj.pye.siip.dto.ca.DtoServicioEnfermeria;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbServiciosEnfermeriaCicloPeriodos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named 
@ManagedBean
@ViewScoped
public class ControladorServiciosEnfermeriaPYE implements Serializable{

    private static final long serialVersionUID = 8477182284194275657L;
    
    @Getter @Setter DtoServicioEnfermeria dto;
    
    @EJB    EjbServiciosEnfermeriaCicloPeriodos ejbServiciosEnfermeriaCicloPeriodos;
    @EJB    EjbModulos              ejbModulos;
    @EJB    EjbFiscalizacion        ejbFiscalizacion;
    @EJB    EjbCatalogos            ejbCatalogos;
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        try {
            dto = new DtoServicioEnfermeria();
            dto.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
            dto.setListaServiciosEnfermeriaTipos(ejbServiciosEnfermeriaCicloPeriodos.getServiciosEnfermeriaTipos());
            Faces.setSessionAttribute("serviciosEnfermeriaTipos", dto.getListaServiciosEnfermeriaTipos());
            filtros();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosEnfermeriaPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void filtros(){
        llenaCategorias();
        dto.nulificarCategoria();
        Faces.setSessionAttribute("categorias", dto.getListaCategoriasPOA());
    }
    
    public void actualizarAreas(ValueChangeEvent e) {
        dto.setCategoria((Categorias) e.getNewValue());
        llenaAreas();
        dto.nulificarAreaPOA();
        Faces.setSessionAttribute("areas", dto.getListaAreasPOA());
    }
    
    public void actualizarAnios(ValueChangeEvent e){
        dto.setAreaUniversidadPOA((AreasUniversidad)e.getNewValue());
        System.out.println("mx.edu.utxj.pye.siip.controller.pye.ControladorServiciosEnfermeriaPYE.actualizarAnios()Area Universidad: " + dto.getAreaUniversidadPOA().getArea());
        llenaAnios();
        dto.nulificarAnioConsulta();
    }
    
    public void actualizarMeses(ValueChangeEvent e){
        dto.setAnioConsulta((short) e.getNewValue());
        llenaMeses();
        buscaServiciosEnfermeria();
    }
    
    public void llenaCategorias() {
        dto.setListaCategoriasPOA(ejbCatalogos.getCategoriaAreasConPoa()
                .stream()
                .filter(categoria -> (short) 6 == categoria.getCategoria())
                .collect(Collectors.toList()));
        if (!dto.getListaCategoriasPOA().isEmpty() && dto.getCategoria() == null) {
            dto.setCategoria(null);
        }
    }
    
    public void llenaAreas() {
        dto.setListaAreasPOA(ejbCatalogos.getAreasUniversidadPorCategoriaConPoa(dto.getCategoria())
                .stream()
                .filter(area -> (short) 11 == area.getArea())
                .collect(Collectors.toList()));
        if (!dto.getListaAreasPOA().isEmpty() && dto.getAreaUniversidadPOA() == null) {
            dto.setAreaUniversidadPOA(null);
        }
    }
    
    public void llenaAnios() {
        dto.setAniosConsulta(ejbModulos.getEjercicioRegistros(dto.getRegistros(), dto.getAreaUniversidadPOA()));
        if (!dto.getAniosConsulta().isEmpty()) {
            dto.setAnioConsulta(null);
            System.out.println("mx.edu.utxj.pye.siip.controller.pye.ControladorServiciosEnfermeriaPYE.llenaAnios()Anio: " + dto.getAnioConsulta());
        }
    }
    
    public void llenaMeses() {
        dto.setMesesConsulta(ejbModulos.getMesesRegistros(dto.getAnioConsulta(), dto.getRegistros(), dto.getAreaUniversidadPOA()));
        if (!dto.getMesesConsulta().isEmpty()) {
            dto.setMesConsulta(null);
            System.out.println("mx.edu.utxj.pye.siip.controller.pye.ControladorServiciosEnfermeriaPYE.llenaMeses()Mes: " + dto.getMesConsulta());
        }
    }

    public void buscaServiciosEnfermeria() {
        if (dto.getMesConsulta() != null && !dto.getMesesConsulta().isEmpty()) {
            dto.setLista(ejbServiciosEnfermeriaCicloPeriodos.getFiltroServiciosEnfermeriaEjercicioMesArea(dto.getAnioConsulta(), dto.getMesConsulta(), dto.getAreaUniversidadPOA().getArea()));
            dto.getLista().stream().forEach((senf) -> {
                senf.getServiciosEnfermeriaCicloPeriodos().setRegistros(ejbModulos.buscaRegistroPorClave(senf.getServiciosEnfermeriaCicloPeriodos().getRegistro()));
            });
        }else{
            dto.setLista(Collections.EMPTY_LIST);
        }
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void eliminarRegistro(Integer registro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
            ejbModulos.eliminarRegistro(registro);
            buscaServiciosEnfermeria();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosEnfermeriaPYE.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(Integer registro) throws Throwable{
        return ejbModulos.getListaEvidenciasPorRegistro(registro);
    }
    
    public void cargarEvidenciasPorRegistro(){
        try {
            dto.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dto.getRegistro().getServiciosEnfermeriaCicloPeriodos().getRegistro()));
            Ajax.update("frmEvidencias");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosEnfermeriaPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(DTOServiciosEnfemeriaCicloPeriodos dtoServEnf){
        dto.setRegistro(dtoServEnf);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dto.getRegistro().getServiciosEnfermeriaCicloPeriodos().getRegistros(), dto.getArchivos());
            if(res.getKey()){
                buscaServiciosEnfermeria();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosEnfermeriaPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dto.getRegistro().getServiciosEnfermeriaCicloPeriodos().getRegistros(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
    /****************************** Alineación de actividades con registros ***********************************************/
    
    public Boolean verificaAlineacion(Integer registro) throws Throwable{
        return ejbModulos.verificaActividadAlineadaGeneral(registro);
    }
    
    public void actualizarEjes(Short ejercicio){
        dto.setEjes(ejbFiscalizacion.getEjes(ejercicio, dto.getAreaUniversidadPOA()));
        if(!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaUniversidadPOA()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dto.getAlineacionActividad() != null){
            dto.setAlineacionEje(dto.getAlineacionActividad().getCuadroMandoInt().getEje());

            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaUniversidadPOA()));
            dto.setAlineacionEstrategia(dto.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dto.getEstrategias());

            dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaUniversidadPOA()));
            dto.setAlineacionLinea(dto.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaUniversidadPOA(),dto.getRegistro().getServiciosEnfermeriaCicloPeriodos().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaUniversidadPOA(),dto.getRegistro().getServiciosEnfermeriaCicloPeriodos().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
        Faces.setSessionAttribute("actividades", dto.getActividades());
    }

    public void actualizarEstrategias(ValueChangeEvent event){
        dto.setAlineacionEje((EjesRegistro)event.getNewValue());
        dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaUniversidadPOA()));
        dto.nulificarEstrategia();
        Faces.setSessionAttribute("estrategias", dto.getEstrategias());
    }

    public void actualizarLineasAccion(ValueChangeEvent event){
        dto.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaUniversidadPOA()));
        dto.nulificarLinea();
        Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
    }
    
    public void abrirAlineacionPOA(DTOServiciosEnfemeriaCicloPeriodos dtoServEnf){
        try {
            dto.setRegistro(dtoServEnf);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistro().getServiciosEnfermeriaCicloPeriodos().getRegistro()));
            actualizarEjes(dto.getRegistro().getServiciosEnfermeriaCicloPeriodos().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosEnfermeriaPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getServiciosEnfermeriaCicloPeriodos().getRegistro());
        if(alineado){
            buscaServiciosEnfermeria();
            abrirAlineacionPOA(dto.getRegistro());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getRegistro().getServiciosEnfermeriaCicloPeriodos().getRegistro());
        if(eliminado){ 
            try {
                Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
                dto.getRegistro().setActividadAlineada(null);
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistro().getServiciosEnfermeriaCicloPeriodos().getRegistro()));
                actualizarEjes(dto.getRegistro().getServiciosEnfermeriaCicloPeriodos().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                cargarAlineacionXActividad();
                Ajax.update("frmAlineacion");
            } catch (Throwable ex) {
                Logger.getLogger(ControladorServiciosEnfermeriaPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void forzarAperturaEdicionServicioEnfermeria(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicionServicioEnfermeria').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazEdicionServicioEnfermeria(){
        Ajax.update("frmEdicionServicioEnfermeria");
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionServicioEnfermeria();
    }
    
    public void abrirEdicionServicioEnfermeria(ServiciosEnfermeriaCicloPeriodos servEnf) {
        DTOServiciosEnfemeriaCicloPeriodos dtoSerEnf = new DTOServiciosEnfemeriaCicloPeriodos();
        dtoSerEnf.setServiciosEnfermeriaCicloPeriodos(servEnf);
        dto.setRegistro(dtoSerEnf);
        dto.setCicloEscolar(ejbModulos.buscaCicloEscolarEspecifico(dto.getRegistro().getServiciosEnfermeriaCicloPeriodos().getCicloEscolar()));
        dto.setPeriodoEscolar(ejbModulos.buscaPeriodoEscolarEspecifico(dto.getRegistro().getServiciosEnfermeriaCicloPeriodos().getPeriodoEscolar()));
        
        dto.setCicloInicio(dateToCalendar(dto.getCicloEscolar().getInicio()).get(Calendar.YEAR));
        dto.setCicloFin(dateToCalendar(dto.getCicloEscolar().getFin()).get(Calendar.YEAR));
        dto.setMensaje("");
        actualizaInterfazEdicionServicioEnfermeria();
    }
    
    public void editaServicioEnfermeria(){
        if(ejbServiciosEnfermeriaCicloPeriodos.buscaServicioEnfermeriaExistente(dto.getRegistro().getServiciosEnfermeriaCicloPeriodos())){
            dto.setMensaje("El servicio de enfermería que ha ingresado ya ha sido agregado anteriormente, intente agregar otro servicio");
        }else{
            dto.getRegistro().setServiciosEnfermeriaCicloPeriodos(ejbServiciosEnfermeriaCicloPeriodos.editaServicioEnfermeria(dto.getRegistro().getServiciosEnfermeriaCicloPeriodos()));
            dto.setMensaje("El servicio de enfemería ha sido actualizado correctamente");
            actualizaInterfazEdicionServicioEnfermeria();
        }
        Ajax.update("mensaje");
    }
    
    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}
