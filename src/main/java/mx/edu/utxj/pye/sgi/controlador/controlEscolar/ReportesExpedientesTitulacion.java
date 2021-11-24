/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReporteFotografiasTitulacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReporteEstadisticoTitulacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ReportesExpedientesRolTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReportesExpedientesTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoExpedienteGeneracion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
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
public class ReportesExpedientesTitulacion extends ViewScopedRol implements Desarrollable{
    
    @Getter @Setter ReportesExpedientesRolTitulacion rol;
    
    @EJB EjbReportesExpedientesTitulacion ejb;
    @EJB EjbSeguimientoExpedienteGeneracion  ejbSeguimientoExpedienteGeneracion;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init(){
     if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.REPORTES_EXPEDIENTES_TITULACION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarRolesReportesTitulacion(logonMB.getPersonal().getClave());//validar si es personal con acceso a reportes de titulación
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personal = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ReportesExpedientesRolTitulacion(filtro, personal);
            tieneAcceso = rol.tieneAcceso(personal);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(personal);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(ejbSeguimientoExpedienteGeneracion.getPeriodoActual().getPeriodo());
            
            rol.getInstrucciones().add("Seleccione generación.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("Seleccione tipo de búsqueda: por programa eductivo o general.");
            rol.getInstrucciones().add("Seleccione programa educativo.");
            rol.getInstrucciones().add("Seleccione reporte que desea consultar.");
            rol.getInstrucciones().add("A continuación, visualizará una tabla con la información correspondiente a los filtros seleccionados.");
            rol.getInstrucciones().add("En la parte superior derecha de la interfaz, encontrará los iconos para descargar la siguiente información:");
            rol.getInstrucciones().add("1. El primer icono descarga el reporte estadístico por generación y nivel educativo.");
            rol.getInstrucciones().add("2. El segundo icono descarga el listado de fotografías de titulación por generación, nivel educativo, programa educativo o general.");
            rol.getInstrucciones().add("3. El tercer icono descarga las fotografías por generación y nivel educativo en un archivo zip.");
           
            rol.setDeshabilitarTipoBusqueda(false);
            rol.setTipoBusqueda("busquedaPrograma");
            listaGeneracionesExpedientesRegistrados();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "reportes expedientes titulacion";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void listaGeneracionesExpedientesRegistrados(){
        ResultadoEJB<List<Generaciones>> res = ejbSeguimientoExpedienteGeneracion.getGeneracionesExpedientes();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                listaNivelesGeneracion();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejb.obtenerListaNivelesGeneracion(rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            listaProgramasEducativosNivel();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    
    public void listaProgramasEducativosNivel(){
        if(rol.getNivelEducativo()== null) return;
        ResultadoEJB<List<AreasUniversidad>> res = ejb.obtenerListaProgramasEducativos(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
            if((rol.getPersonal().getPersonal().getCategoriaOperativa().getCategoria()==18 && rol.getPersonal().getPersonal().getAreaSuperior()==2) || (rol.getPersonal().getPersonal().getCategoriaOperativa().getCategoria()==48  && rol.getPersonal().getPersonal().getAreaSuperior()==2)){
                rol.setProgramasEducativos(res.getValor().stream().filter(p->p.getAreaSuperior().equals(rol.getPersonal().getAreaOperativa().getArea())).collect(Collectors.toList()));
            }else if(rol.getPersonal().getPersonal().getAreaOperativa()==60 || (rol.getPersonal().getPersonal().getAreaOperativa()==16 && rol.getPersonal().getAreaOficial().getArea()==16)){
                rol.setProgramasEducativos(res.getValor());
            }
            if(!rol.getProgramasEducativos().isEmpty()){
                rol.setProgramaEducativo(rol.getProgramasEducativos().get(0));
            }else{
                rol.setProgramaEducativo(new AreasUniversidad());
            }
            listaReportes();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de reportes de titulación dependiendo del personal que ingresa
     */
    public void listaReportes(){
        List<String> listaReportes = new ArrayList<>();
        
        if((rol.getPersonal().getPersonal().getCategoriaOperativa().getCategoria()==18 && rol.getPersonal().getPersonal().getAreaSuperior()==2) || (rol.getPersonal().getPersonal().getCategoriaOperativa().getCategoria()==48  && rol.getPersonal().getPersonal().getAreaSuperior()==2) || (rol.getPersonal().getPersonal().getAreaOperativa()==16 && rol.getPersonal().getAreaOficial().getArea()==16)){
            listaReportes.add("Expediente con fotografía");
        }
        else if(rol.getPersonal().getPersonal().getAreaOperativa()==60){
            listaReportes.add("Expediente con fotografía");
            listaReportes.add("Informe estadístico");
        }
        
        rol.setReportes(listaReportes);
        rol.setReporte(rol.getReportes().get(0));
        generarReportes();
    }
    
     /**
     * Permite generar el reporte seleccionado dependiendo la opción seleccionada
     */
    public void generarReportes(){
        if("Expediente con fotografía".equals(rol.getReporte())){
           generarReporteFotografiaTitulacion();
        }else if("Informe estadístico".equals(rol.getReporte())){
           generarReporteEstadisticoTitulacion();
        }
    }
    
     /**
     * Permite que al cambiar o seleccionar generación se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones generacion= (Generaciones)e.getNewValue();
            rol.setGeneracion(generacion);
            listaNivelesGeneracion();
            generarReportes();
            Ajax.update("frm");
        }
    }
    
     /**
     * Permite que al cambiar o seleccionar nivel educativo se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel= (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            listaProgramasEducativosNivel();
            generarReportes();
            Ajax.update("frm");
        }
    }
    
     /**
     * Permite que al cambiar o seleccionar programa educativo se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarProgramaEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof AreasUniversidad){
            AreasUniversidad programa= (AreasUniversidad)e.getNewValue();
            rol.setProgramaEducativo(programa);
            generarReportes();
            Ajax.update("frm");
        }
    }
    
    /**
     * Permite que al cambiar o seleccionar un reporte se pueda actualizar la información
     * @param e Evento del cambio de valor
     */
    public void cambiarReporte(ValueChangeEvent e){
        if(e.getNewValue() instanceof  String){
            String reporte = (String)e.getNewValue();
            rol.setReporte(reporte);
            if(rol.getReporte().equals("Informe estadístico")){
                rol.setDeshabilitarTipoBusqueda(true);
            }else{
                rol.setDeshabilitarTipoBusqueda(false);
            }
            generarReportes();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite seleccionar y cambiar valor del tipo de búsqueda
     * @param e Evento del cambio de valor
     */
    public void cambiarTipoBusqueda(ValueChangeEvent e){
        rol.setTipoBusqueda((String)e.getNewValue());
        generarReportes();
        Ajax.update("frm");
    }
    
     /**
     * Permite generar reporte de fotografías de titulación por generación, nivel educativo y programas educativos
     */
    public void generarReporteFotografiaTitulacion(){
        if(rol.getTipoBusqueda().equals("busquedaPrograma")){
            ResultadoEJB<List<DtoReporteFotografiasTitulacion>> res = ejb.getReporteFotografiaTitulacionPrograma(rol.getGeneracion(), rol.getNivelEducativo(), rol.getProgramaEducativo());
            if(res.getCorrecto()){
                rol.setReporteFotografiasTitulacion(res.getValor());
                Ajax.update("tbReporteFotografia");
            }else mostrarMensajeResultadoEJB(res);
        }else{
            System.err.println("generarReporteFotografiaTitulacion - reporteGeneral ");
            ResultadoEJB<List<DtoReporteFotografiasTitulacion>> res = ejb.getReporteFotografiaTitulacion(rol.getGeneracion(), rol.getNivelEducativo(), rol.getProgramasEducativos());
            if(res.getCorrecto()){
                rol.setReporteFotografiasTitulacion(res.getValor());
                Ajax.update("tbReporteFotografia");
            }else mostrarMensajeResultadoEJB(res);
        }
    }
    
     /**
     * Permite generar reporte estadístico de titulación por generación y nivel educativo
     */
    public void generarReporteEstadisticoTitulacion(){
        ResultadoEJB<List<DtoReporteEstadisticoTitulacion>> res = ejb.getReporteEstadisticoTitulacion(rol.getGeneracion(), rol.getNivelEducativo());
        if (res.getCorrecto()) {
            rol.setReporteEstadisticoTitulacion(res.getValor());
            Ajax.update("tbReporteEstadistico");
        } else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite descargar el archivo de la fotografía
     * @param ruta
     * @throws java.io.IOException
     */
    public void descargarDocumento(String ruta) throws IOException{
        File f = new File(ruta);
        Faces.sendFile(f, false);
    }
    
     /**
     * Permite descargar el reporte estadístico por generación y nivel educativo
     * @throws java.io.IOException
     */
    public void descargarReporteEstadisticoGeneracionNivel() throws IOException, Throwable{
        File f = new File(ejb.getDescargarReporteEstadístico(rol.getGeneracion(), rol.getNivelEducativo()));
        Faces.sendFile(f, true);
    }  
    
     /**
     * Permite descargar el listado de fotografías de titulación por generación y nivel educativo
     * @throws java.io.IOException
     */
    public void descargarListadoFotografiasGeneracionNivel() throws IOException, Throwable{
        File f = new File(ejb.getDescargarReporteFotografia(rol.getGeneracion(), rol.getNivelEducativo(), rol.getProgramasEducativos()));
        Faces.sendFile(f, true);
    } 
    
     /**
     * Permite descargar las fotografías por generación y nivel educativo en un archivo .zip
     * @throws java.io.IOException
     */
    public void descargarFotografiasGeneracionNivel() throws IOException, Throwable{
        File f = new File(ejb.getDescargarFotografias(rol.getGeneracion(), rol.getNivelEducativo()));
        Faces.sendFile(f, true);
    } 
    
}

