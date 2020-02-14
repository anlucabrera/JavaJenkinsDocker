/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ca;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DTOAsesoriasTutoriasCuatrimestrales;
import mx.edu.utxj.pye.siip.dto.ca.DtoAsesoriaTutoriaCuatrimestral;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbAsesoriasTutoriasCuatrimestrales;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorAsesoriasTutoriasCuatrimestrales implements Serializable{
    private static final long serialVersionUID = 1280210182688329086L;
    @Getter @Setter DtoAsesoriaTutoriaCuatrimestral dto;
    
    @EJB    EjbAsesoriasTutoriasCuatrimestrales     ejb;
    @EJB    EjbFiscalizacion                        ejbFiscalizacion;
    @EJB    EjbModulos                              ejbModulos;
    @EJB    EjbMatriculaPeriodosEscolares           ejbMatriculaPeriodosEscolares;
    
    @Inject     ControladorEmpleado                 controladorEmpleado;
    @Inject     ControladorModulosRegistro          controladorModulosRegistro;
    
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init(){
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        try {
        dto = new DtoAsesoriaTutoriaCuatrimestral();
        consultaAreaRegistro();
        
        if(dto.getAreaPOA()== null){
            return;
        }
        
        dto.setPeriodoEscolarActivo(ejbModulos.getPeriodoEscolarActivo());
        try {
            dto.setMensaje("");
            Ajax.update("mensaje");
            dto.setEventoActual(ejbModulos.getEventoRegistro());
            dto.setListaDatosAsesoriasTutorias(ejb.getDatosAsesoriasTutorias());
            Faces.setSessionAttribute("datosAsesoriasTutorias", dto.getListaDatosAsesoriasTutorias());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorAsesoriasTutoriasCuatrimestrales.class.getName()).log(Level.SEVERE, null, ex);
        }
        initFiltros();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorAsesoriasTutoriasCuatrimestrales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 89);
            if (areaRegistro == null) {
                ResultadoEJB<AreasUniversidad> area = ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
                if(area.getCorrecto()){
                    dto.setAreaPOA(area.getValor());
                }else{
                    dto.setAreaPOA(null);
                }
            } else {
                dto.setAreaPOA(areaRegistro);
            }
        } catch (Exception ex) {
            dto.setAreaPOA(null);
        }
    }
    
    public void cargarListaPorEvento(){
        dto.setMensaje("");
        Ajax.update("mensaje");
        dto.setMatriculaInicial(ejbMatriculaPeriodosEscolares.getConteoMatriculaInicialPorPeriodo(dto.getPeriodo()));
        dto.setListaDtoAsesoriasTutoriasCuatrimestrales(ejb.getListaAsesoriaTutoriaCuatrimestralPorEventoAreaPeriodo(dto.getEventoSeleccionado(), dto.getAreaPOA().getArea(), dto.getPeriodo(), dto.getRegistroTipo()));
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void initFiltros(){
        dto.setPeriodos(ejb.getPeriodosConregistro(dto.getRegistroTipo(),dto.getEventoActual(),dto.getAreaPOA()));
        dto.setEventosPorPeriodo(ejbModulos.getEventosPorPeriodo(dto.getPeriodo()));
        try {
            Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> entrada = ejb.comprobarEventoActual(dto.getPeriodos(), dto.getEventosPorPeriodo(), dto.getEventoActual(), dto.getRegistroTipo(),dto.getAreaPOA());
            if(entrada != null){
                dto.setPeriodos(entrada.getKey());
                dto.setEventosPorPeriodo(entrada.getValue());
            }
        } catch (PeriodoEscolarNecesarioNoRegistradoException ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorAsesoriasTutoriasCuatrimestrales.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargarListaPorEvento();
    }
    
    /************************************************ Modales de Edición y Alta de Asesorias y Tutorias Cuatrimestrales ******************************************************/
    
    public void forzarAperturaEdicionAsesoriaTutoriaMensual(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicionAsesoriaTutoria').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazEdicionAsesoriaTutoriaMensual(){
        Ajax.update("frmEdicionAsesoriaTutoria");
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionAsesoriaTutoriaMensual();
    }
    
    /************************************************** Alta de Asesorías o Tutorías Cuatrimestrales Mediante Formulario **************************************************/
    public void onCellEditAsesoriaTutoria(CellEditEvent event) {
        if (dto.getAreaPOA() != null) {
            DataTable dataTable = (DataTable) event.getSource();
            DTOAsesoriasTutoriasCuatrimestrales dtoEditado = (DTOAsesoriasTutoriasCuatrimestrales) dataTable.getRowData();
            if (dtoEditado.getAsesoriaTutoriaCuatrimestral().getRegistro() != null) {
                if (!ejb.buscaAsesoriaTutoriaCuatrimestralParaEdicion(dtoEditado.getAsesoriaTutoriaCuatrimestral()).isEmpty()) {
                    dto.setMensaje("No se ha podido actualizar debido a que el sistema ha detectado un registro con las mismas caracteristicas, favor de intentar nuevamente");
                    Ajax.update("mensaje");
                } else {
                    ejb.editaAsesoriaTutoriaCuatrimestralPeriodoEscolar(dtoEditado);
                    dto.setListaDtoAsesoriasTutoriasCuatrimestrales(ejb.getListaAsesoriaTutoriaCuatrimestralPorEventoAreaPeriodo(dto.getEventoSeleccionado(), dto.getAreaPOA().getArea(), dto.getPeriodo(), dto.getRegistroTipo()));
                    dto.setMensaje("El registro ha sido editado con exito en la base de datos");
                    Ajax.update("mensaje");
                }
            } else {
                if (!ejb.buscaAsesoriaTutoriaCuatrimestralParaGuardado(dtoEditado.getAsesoriaTutoriaCuatrimestral()).isEmpty()) {
                    dto.setMensaje("Los datos que ha ingresado corresponde a una asesoría o tutoría ya existente, favor de verificar su información");
                    Ajax.update("mensaje");
                } else {
                    ejb.guardaAsesoriaTutoriaCuatrimestral(dtoEditado, dto.getRegistroTipo(), dto.getEje(), dto.getAreaPOA().getArea(), controladorModulosRegistro.getEventosRegistros());
                    dto.setListaDtoAsesoriasTutoriasCuatrimestrales(ejb.getListaAsesoriaTutoriaCuatrimestralPorEventoAreaPeriodo(dto.getEventoSeleccionado(), dto.getAreaPOA().getArea(), dto.getPeriodo(), dto.getRegistroTipo()));
                    dto.setMensaje("El registro ha sido guardado con exito en la base de datos");
                    Ajax.update("mensaje");
                }
            }
        }
    }

    public void actualizarMeses(ValueChangeEvent e){
        dto.setPeriodo((PeriodosEscolares)e.getNewValue());
        dto.setEventosPorPeriodo(ejbModulos.getEventosPorPeriodo(dto.getPeriodo()));
        cargarListaPorEvento();
    }
    
}