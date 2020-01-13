package mx.edu.utxj.pye.siip.controller.pye;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasMensualPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DtoAsesoriasTutorias;
import mx.edu.utxj.pye.siip.dto.ca.DTOAsesoriasTutoriasCicloPeriodos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbAsesoriasTutoriasCiclosPeriodos;
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
public class ControladorAsesoriasTutoriasCicloEscolarPYE implements Serializable{

    private static final long serialVersionUID = -7714155978335195969L;
    
    @Getter @Setter DtoAsesoriasTutorias        dto;
    
    @EJB EjbAsesoriasTutoriasCiclosPeriodos     ejb;
    @EJB EjbFiscalizacion                       ejbFiscalizacion;
    @EJB EjbModulos                             ejbModulos;
    @EJB EjbCatalogos                           ejbCatalogos;
    
    @Inject Caster                              caster;
    
    @Inject ControladorEmpleado                 controladorEmpleado;
    @Inject ControladorModulosRegistro          controladorModulosRegistro;
    
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        try {
        dto = new DtoAsesoriasTutorias();
        dto.setAreaPOA(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        dto.setPeriodoEscolarActivo(ejbModulos.getPeriodoEscolarActivo());
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorAsesoriasTutoriasCicloEscolarPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
        initFiltros();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorAsesoriasTutoriasCicloEscolarPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void actualizaPeriodosEscolares(ValueChangeEvent e){
        dto.setAreaUniversidadPOA((AreasUniversidad)e.getNewValue());

        if (dto.getAreaUniversidadPOA().getArea().equals(23)) {
            dto.setProgramasEducativos(ejbCatalogos.getProgramasEducativos());
        } else {
            dto.setProgramasEducativos(ejbCatalogos.getProgramasEducativos()
                    .stream()
                    .filter((area) -> (short) dto.getAreaUniversidadPOA().getArea() == area.getAreaSuperior())
                    .collect(Collectors.toList())
            );
        }

        Faces.setSessionAttribute("programasEducativos", dto.getProgramasEducativos());
        
        dto.setPeriodos(ejb.getPeriodosConregistro(dto.getRegistroTipo(),dto.getEventoActual()));
        dto.setEventosPorPeriodo(ejbModulos.getEventosPorPeriodo(dto.getPeriodo()));
        try {
            Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> entrada = ejb.comprobarEventoActual(dto.getPeriodos(), dto.getEventosPorPeriodo(), dto.getEventoActual(), dto.getRegistroTipo());
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.initFiltros() entrada: " + entrada);
            if(entrada != null){
                dto.setPeriodos(entrada.getKey());
                dto.setEventosPorPeriodo(entrada.getValue());
            }
        } catch (PeriodoEscolarNecesarioNoRegistradoException ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorAsesoriasTutoriasCicloEscolarPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargarListaPorEvento();
    }
    
    public void cargarListaPorEvento(){
        dto.setLista(ejb.getListaRegistrosPorEventoAreaPeriodo(dto.getEventoSeleccionado(), dto.getAreaUniversidadPOA().getArea(), dto.getPeriodo(), dto.getRegistroTipo(), controladorEmpleado.getNuevoOBJListaPersonal().getActividad(),controladorEmpleado.getNuevoOBJListaPersonal().getClave(),controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        dto.getLista().stream().forEach((atce) -> {
            atce.getAsesoriasTutoriasCicloPeriodos().setRegistros(ejbModulos.buscaRegistroPorClave(atce.getAsesoriasTutoriasCicloPeriodos().getRegistro()));
        });
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void initFiltros(){
        llenaAreasAcademicas();
        Faces.setSessionAttribute("areas", dto.getListaAreasPOA());
    }
    
    public void llenaAreasAcademicas(){
        dto.setListaAreasPOA(ejbCatalogos.getAreasAcademicasAsesoriasTutorias());
        if(!dto.getListaAreasPOA().isEmpty() && dto.getAreaUniversidadPOA() == null){
            dto.setAreaUniversidadPOA(null);
        }
    }
     
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    ///////////////////////////////////// Guardado, Edición y Eliminación \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    public void eliminarRegistro(DTOAsesoriasTutoriasCicloPeriodos registro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro.getAsesoriasTutoriasCicloPeriodos().getRegistro(), ejbModulos.getListaEvidenciasPorRegistro(registro.getAsesoriasTutoriasCicloPeriodos().getRegistro()));
            Boolean eliminado = ejb.eliminarRegistro(registro);
            Messages.addGlobalInfo("El registro se eliminó de forma correcta.");
            cargarListaPorEvento();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorAsesoriasTutoriasCicloEscolarPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    ///////////////////////////////////// Evidencias \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    public void cargarEvidenciasPorRegistro(){
        dto.setListaEvidencias(ejb.getListaEvidenciasPorRegistro(dto.getRegistro()));
        Ajax.update("frmEvidencias");
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(DTOAsesoriasTutoriasCicloPeriodos registro){
        return ejb.getListaEvidenciasPorRegistro(registro);
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejb.eliminarEvidenciaEnRegistro(dto.getRegistro(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void seleccionarRegistro(DTOAsesoriasTutoriasCicloPeriodos registro){
//        System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.seleccionarRegistro(): " + registro);
        dto.setRegistro(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        Map.Entry<Boolean, Integer> res = ejb.registrarEvidenciasARegistro(dto.getRegistro(), dto.getArchivos());
        if(res.getKey()){ 
            cargarListaPorEvento();
            Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
        }else{ 
            Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
        }
    }
    
    ///////////////////////////////////// Alineación \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaUniversidadPOA(), dto.getEventoActual().getEjercicioFiscal().getAnio()));
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
    
    public void actualizarMeses(ValueChangeEvent e){
        dto.setPeriodo((PeriodosEscolares)e.getNewValue());
        dto.setEventosPorPeriodo(ejbModulos.getEventosPorPeriodo(dto.getPeriodo()));
        cargarListaPorEvento();
    }
    
    public void actualizarEjes(){
        dto.setEjes(ejbFiscalizacion.getEjes(dto.getRegistro().getAsesoriasTutoriasCicloPeriodos().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio(), dto.getAreaUniversidadPOA()));
//        dto.getEjes().forEach(e -> System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.actualizarEjes() eje: " + e));
        if(!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaUniversidadPOA()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dto.getAlineacionActividad() != null){
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.cargarAlineacionXActividad() actividad: " + dto.getAlineacionActividad());
            dto.setAlineacionEje(dto.getAlineacionActividad().getCuadroMandoInt().getEje());

            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaUniversidadPOA()));
            dto.setAlineacionEstrategia(dto.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dto.getEstrategias());
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.cargarAlineacionXActividad() estrategias: " + dto.getEstrategias());

            dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaUniversidadPOA()));
            dto.setAlineacionLinea(dto.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.cargarAlineacionXActividad() lineas: " + dto.getLineasAccion());

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaUniversidadPOA(),dto.getEventoActual().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.cargarAlineacionXActividad() actividades: " + dto.getActividades());
//            dto.setAlineacionActividad(dto.getAlineacionActividad());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
    public void abrirAlineacionPOA(DTOAsesoriasTutoriasCicloPeriodos registro){
        dto.setRegistro(registro);        
        dto.setAlineacionActividad(ejb.getActividadAlineada(registro));
        actualizarEjes();
        cargarAlineacionXActividad();
        Ajax.update("frmAlineacion");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAlineacion').show();");
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejb.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro());
        if(alineado){
            cargarListaPorEvento();
            abrirAlineacionPOA(dto.getRegistro());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejb.eliminarAlineacion(dto.getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
            dto.getRegistro().setActividadAlineada(null);
            dto.setAlineacionActividad(ejb.getActividadAlineada(dto.getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    /********************************************* Edición ***********************************************/
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
    
    public void abrirEdicionAsesoriasTutoriasMensual(AsesoriasTutoriasMensualPeriodosEscolares asesoriaTutoriaMensual) {
        dto.setProgramasEducativos(ejbCatalogos.getProgramasEducativos());
            if (dto.getAreaUniversidadPOA().getArea() == ((short)23)) {
                dto.setProgramasEducativos(ejbCatalogos.getProgramasEducativos());
            } else {
                dto.setProgramasEducativos(ejbCatalogos.getProgramasEducativos()
                        .stream()
                        .filter((area) -> (short) dto.getAreaUniversidadPOA().getArea() == area.getAreaSuperior())
                        .collect(Collectors.toList())
                );
            }
            Faces.setSessionAttribute("programasEducativos", dto.getProgramasEducativos());
            Ajax.update("somProgramasEducativos");
        
        dto.setNuevoRegistro(Boolean.FALSE);
        DTOAsesoriasTutoriasCicloPeriodos dtoAsTutMen = new DTOAsesoriasTutoriasCicloPeriodos();
        dtoAsTutMen.setAsesoriasTutoriasCicloPeriodos(asesoriaTutoriaMensual);
        dto.setRegistro(dtoAsTutMen);
        dto.setPeriodoEscolarAsesoriaTutoria(ejbModulos.buscaPeriodoEscolarEspecifico(dto.getRegistro().getAsesoriasTutoriasCicloPeriodos().getPeriodoEscolar()));
        dto.setProgramaEducativoAsesTut(ejbModulos.buscaProgramaEducativoEspecifico(dto.getRegistro().getAsesoriasTutoriasCicloPeriodos().getProgramaEducativo()));
        dto.setMensaje("");
        actualizaInterfazEdicionAsesoriaTutoriaMensual();
    }
    
    public void editaAsesoriaTutoriaMensual(AsesoriasTutoriasMensualPeriodosEscolares asesoriaTutoria){
        if(ejb.buscaAsesoriaTutoriaExistente(asesoriaTutoria)){
            dto.setMensaje("No se ha podido actualizar debido a que el sistema ha detectado un registro con las mismas caracteristicas, favor de intentar nuevamente");
            Ajax.update("mensaje");
        }else{
            dto.getRegistro().setAsesoriasTutoriasCicloPeriodos(ejb.editaAsesoriaTutoriaMensualPeriodoEscolar(asesoriaTutoria));
            dto.setMensaje("El registro se ha actualizado correctamente");
            Ajax.update("mensaje");
            actualizaInterfazEdicionAsesoriaTutoriaMensual();
        }
    }
    
    public void actualizarProgramaEducativo(ValueChangeEvent event){
        dto.setProgramaEducativoAsesTut(((AreasUniversidad)event.getNewValue()));
        dto.getRegistro().getAsesoriasTutoriasCicloPeriodos().setProgramaEducativo(dto.getProgramaEducativoAsesTut().getArea());
    }
    
    /************************************************** Alta de Asesorías o Tutorías Mediante Formulario **************************************************/
    
    public void nuevoRegistroAsesoriaTutoria(){
        if(dto.getAreaUniversidadPOA()!= null){
            dto.setNuevoRegistro(Boolean.TRUE);
            DTOAsesoriasTutoriasCicloPeriodos dtoAT = new DTOAsesoriasTutoriasCicloPeriodos();
            dtoAT.setAsesoriasTutoriasCicloPeriodos(new AsesoriasTutoriasMensualPeriodosEscolares());
            dtoAT.setPeriodoEscolar(caster.periodoToString(ejbModulos.getPeriodoEscolarActivo()));
            dtoAT.getAsesoriasTutoriasCicloPeriodos().setMes(controladorModulosRegistro.getEventosRegistros().getMes());

            dto.setPeriodoEscolarAsesoriaTutoria(ejbModulos.getPeriodoEscolarActivo());
            
            dto.setProgramasEducativos(ejbCatalogos.getProgramasEducativos());
            if (dto.getAreaUniversidadPOA().getArea() == ((short)23)) {
                dto.setProgramasEducativos(ejbCatalogos.getProgramasEducativos());
            } else {
                dto.setProgramasEducativos(ejbCatalogos.getProgramasEducativos()
                        .stream()
                        .filter((area) -> (short) dto.getAreaUniversidadPOA().getArea() == area.getAreaSuperior())
                        .collect(Collectors.toList())
                );
            }
            Faces.setSessionAttribute("programasEducativos", dto.getProgramasEducativos());
            Ajax.update("somProgramasEducativos");

            
            dto.setRegistro(dtoAT);
            dto.setMensaje("");
            actualizaInterfazEdicionAsesoriaTutoriaMensual();
        }else{
            Messages.addGlobalWarn("Debes seleccionar un área para poder dar de alta un asesoría o tutoría");
        }
    }
    
    public void guardarAsesoriaTutoria(AsesoriasTutoriasMensualPeriodosEscolares asesoriaTutoria){
        asesoriaTutoria.setTutor(controladorEmpleado.getNuevoOBJListaPersonal().getClave());
        asesoriaTutoria.setPeriodoEscolar(ejbModulos.getPeriodoEscolarActivo().getPeriodo());
        if(ejb.getRegistroAsesoriaTutoriaCicloPeriodo(asesoriaTutoria) != null){
            dto.setMensaje("Los datos que ha ingresado corresponde a una asesoría o tutoría ya existente, favor de verificar su información");
        }else{
            asesoriaTutoria.setProgramaEducativo(dto.getProgramaEducativoAsesTut().getArea());
            ejb.guardaAsesoriaTutoria(asesoriaTutoria, dto.getRegistroTipo(), dto.getEje(), dto.getAreaUniversidadPOA().getArea(), controladorModulosRegistro.getEventosRegistros());
            dto.setMensaje("El registro ha sido guardado con exito en la base de datos");
            cargarListaPorEvento();
            DTOAsesoriasTutoriasCicloPeriodos dtoAT = new DTOAsesoriasTutoriasCicloPeriodos();
            dtoAT.setAsesoriasTutoriasCicloPeriodos(new AsesoriasTutoriasMensualPeriodosEscolares());
            
            
            dtoAT.setPeriodoEscolar(caster.periodoToString(ejbModulos.getPeriodoEscolarActivo()));
            dtoAT.getAsesoriasTutoriasCicloPeriodos().setMes(controladorModulosRegistro.getEventosRegistros().getMes());
            dto.setPeriodoEscolarAsesoriaTutoria(ejbModulos.getPeriodoEscolarActivo());
            
            dto.setProgramasEducativos(ejbCatalogos.getProgramasEducativos());
            if (dto.getAreaUniversidadPOA().getArea() == ((short)23)) {
                dto.setProgramasEducativos(ejbCatalogos.getProgramasEducativos());
            } else {
                dto.setProgramasEducativos(ejbCatalogos.getProgramasEducativos()
                        .stream()
                        .filter((area) -> (short) dto.getAreaUniversidadPOA().getArea() == area.getAreaSuperior())
                        .collect(Collectors.toList())
                );
            }
            Faces.setSessionAttribute("programasEducativos", dto.getProgramasEducativos());
            Ajax.update("somProgramasEducativos");

            dto.setRegistro(dtoAT);
            cargarListaPorEvento();
        }
        Ajax.update("mensaje");
    }
    
    public void accionAsesoriaTutoria(AsesoriasTutoriasMensualPeriodosEscolares asesoriaTutoria){
        if(dto.getNuevoRegistro()){
            guardarAsesoriaTutoria(asesoriaTutoria);
        }else{
            editaAsesoriaTutoriaMensual(asesoriaTutoria);
        }
    }
}
