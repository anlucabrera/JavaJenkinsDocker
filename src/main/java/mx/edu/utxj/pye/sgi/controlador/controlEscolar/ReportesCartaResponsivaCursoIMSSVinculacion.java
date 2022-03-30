/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCartaResponsivaCursoIMMSEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCumplimientoCartaResponsivaCursoIMSS;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteSituacionCartaResponsivaCursoIMSS;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReporteDocumentosVinculacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ReportesCartaResponsivaCursoIMSSRolVinculacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroEventosVinculacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCargaCartaResponsivaCursoIMSS;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReportesCartaResponsivaCursoIMSS;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class ReportesCartaResponsivaCursoIMSSVinculacion extends ViewScopedRol implements Desarrollable{
    @Getter @Setter ReportesCartaResponsivaCursoIMSSRolVinculacion rol;
    
    @EJB EjbReportesCartaResponsivaCursoIMSS ejb;
    @EJB EjbRegistroEventosVinculacion ejbRegistroEventosVinculacion;
    @EJB EjbCargaCartaResponsivaCursoIMSS ejbCargaCartaResponsivaCursoIMSS;
    
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por dirección de carrera<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    @PostConstruct
    public void init(){
    if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.REPORTES_CARTARESPONSIVA_CURSOIMSS);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbRegistroEventosVinculacion.validarCoordinacionEstadia(logon.getPersonal().getClave());//validar si es director
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ReportesCartaResponsivaCursoIMSSRolVinculacion(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setUsuario(usuario);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            
            rol.getInstrucciones().add("Seleccione generación y nivel educativo.");
            rol.getInstrucciones().add("Seleccione el reporte que desea consultar.");
            rol.getInstrucciones().add("Se visualizará el reporte correspondiente.");
            rol.getInstrucciones().add("Dar clic en el botón de Descargar Reporte en Excel, para generar un archivo que contiene la información de los reportes disponibles.");
           
            generacionesEventosRegistrados();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "reportes carta responsiva curso imss";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de generaciones en los que existen eventos de vinculación registrados
     */
    public void generacionesEventosRegistrados(){
        ResultadoEJB<List<Generaciones>> res =  ejbCargaCartaResponsivaCursoIMSS.getGeneracionesSeguimientoRegistrados();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                listaNivelesGeneracion();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de niveles educativos en los que existen eventos de vinculación registrados de la generación seleccionada
     */
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejbCargaCartaResponsivaCursoIMSS.getNivelesSeguimientoRegistrados(rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            listaReportes();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de reportes disponibles
     */
    public void listaReportes(){
        List<String> listaReportes = new ArrayList<>();
        listaReportes.add("Cumplimiento estudiante documentos");
        listaReportes.add("Porcentaje de cumplimiento y validación");
        listaReportes.add("Listado de estudiantes");
        rol.setReportes(listaReportes);
        rol.setReporte(rol.getReportes().get(0));
        generarReportes();
        
    }
    
    /**
     * Permite generar el reporte seleccionado dependiendo la opción seleccionada
     */
    public void generarReportes(){
        if("Cumplimiento estudiante documentos".equals(rol.getReporte())){
           generarCumpDocumentoPrograma();
        }else if("Porcentaje de cumplimiento y validación".equals(rol.getReporte())){
           generarReporteDocumentosVinculacion();
        }else if("Listado de estudiantes".equals(rol.getReporte())){
           generarListaEstudiantes();
        }
    }
    
    /**
     * Permite generar el listado de cumplimiento de carga por documento y programa educativo
     */
    public void generarCumpDocumentoPrograma(){
        ResultadoEJB<List<DtoCumplimientoCartaResponsivaCursoIMSS>> res = ejb.getCumplimientoDocumentoPE(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
            rol.setCumplimientoEstDocumento(res.getValor());
            Ajax.update("tbCumpDocEstPE");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite generar el reporte de cumplimiento y validación de los documentos
     */
    public void generarReporteDocumentosVinculacion(){
        ResultadoEJB<List<DtoReporteDocumentosVinculacion>> res = ejb.getReporteDocumentosVinculacion(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
             rol.setListaReporteVinculacion(res.getValor());
             rol.setTotalEstudiantesActivos(rol.getListaReporteVinculacion().stream().mapToInt(p->p.getEstudiantesActivos()).sum());
             rol.setTotalCargados(rol.getListaReporteVinculacion().stream().mapToInt(p->p.getEstudiantesEvidencia()).sum());
             rol.setTotalSinCargar(rol.getListaReporteVinculacion().stream().mapToInt(p->p.getEstudiantesSinEvidencia()).sum());
             rol.setTotalValidados(rol.getListaReporteVinculacion().stream().mapToInt(p->p.getEstudiantesEvidenciaValidada()).sum());
             rol.setTotalNoValidados(rol.getListaReporteVinculacion().stream().mapToInt(p->p.getEstudiantesEvidenciaNoValidada()).sum());
             rol.setPorcentajeCumplimiento(String.format("%.2f", rol.getListaReporteVinculacion().stream().mapToDouble(p->p.getPorcentajeCumplimiento()).average().getAsDouble()));
             rol.setPorcentajeValidacion(String.format("%.2f", rol.getListaReporteVinculacion().stream().mapToDouble(p->p.getPorcentajeValidacion()).average().getAsDouble()));
             Ajax.update("tbPorCumplVal");
         }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite generar el listado de estudiantes con la situación de validación de los documentos
     */
    public void generarListaEstudiantes(){
        ResultadoEJB<List<DtoEstudianteSituacionCartaResponsivaCursoIMSS>> res = ejb.getListaEstudiantes(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
            rol.setListaEstudiantes(res.getValor());
            Ajax.update("tbListaEstudiantes");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite que al cambiar o seleccionar una generación se pueda actualizar la lista de niveles educativos y la información del reporte seleccionado
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones generacion = (Generaciones)e.getNewValue();
            rol.setGeneracion(generacion);
            listaNivelesGeneracion();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la información del reporte seleccionado
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            generarReportes();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un reporte se pueda actualizar el valor del reporte
     * @param e Evento del cambio de valor
     */
    public void cambiarReporte(ValueChangeEvent e){
        if(e.getNewValue() instanceof  String){
            String reporte = (String)e.getNewValue();
            rol.setReporte(reporte);
            generarReportes();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Método que permite descargar en excel los reportes de la generación y nivel educativo seleccionado
     * @throws java.io.IOException
     */
     public void descargarReportes() throws IOException, Throwable{
         generarCumpDocumentoPrograma();
         generarReporteDocumentosVinculacion();
         generarListaEstudiantes();
         File f = new File(ejb.getReportes(rol.getCumplimientoEstDocumento(), rol.getListaReporteVinculacion(), rol.getListaEstudiantes(), rol.getGeneracion(), rol.getNivelEducativo()));
         Faces.sendFile(f, true);
        
    }
     
}
