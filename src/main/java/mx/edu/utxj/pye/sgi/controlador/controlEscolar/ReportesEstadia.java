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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAsigAsesorAcadEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCumplimientoEstDocEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEficienciaEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReporteActividadesEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoZonaInfluenciaEstIns;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoZonaInfluenciaEstPrograma;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ReportesEstadiaRolMultiple;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionRolesEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReportesEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoEstadia;
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
public class ReportesEstadia extends ViewScopedRol implements Desarrollable{
    @Getter @Setter ReportesEstadiaRolMultiple rol;
    
    @EJB EjbReportesEstadia ejb;
    @EJB EjbSeguimientoEstadia ejbSeguimientoEstadia;
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
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
            setVistaControlador(ControlEscolarVistaControlador.REPORTES_ESTADIA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarRolesReportesEstadia(logon.getPersonal().getClave());//validar si es director
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ReportesEstadiaRolMultiple(filtro, usuario);
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
        String valor = "reportes estadia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de generaciones en los que existen eventos de estadía registrados
     */
    public void generacionesEventosRegistrados(){
        ResultadoEJB<List<Generaciones>> res =  ejbAsignacionRolesEstadia.getGeneracionesEventosRegistrados();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                listaNivelesGeneracion();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de nivele educativos registradas en la generación seleccionada
     */
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejbAsignacionRolesEstadia.getNivelesGeneracionEventosRegistrados(rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            listaReportes();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de reportes de estadía
     */
    public void listaReportes(){
        List<String> listaReportes = new ArrayList<>();
        
        if(rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==18 || rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==48){
            listaReportes.add("Eficiencia estadía técnica");
            listaReportes.add("Listado estudiantes con promedios");
            rol.setReportes(listaReportes);
            rol.setReporte(rol.getReportes().get(0));
            generarReportesDireccionAcademica();
        }else if(rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==15){
            listaReportes.add("Eficiencia estadía técnica");
            listaReportes.add("Listado estudiantes con promedios");
            listaReportes.add("Zona influencia institucional");
            listaReportes.add("Zona influencia programa educativo");
            rol.setReportes(listaReportes);
            rol.setReporte(rol.getReportes().get(0));
            generarReportes();
        }
        else if(rol.getUsuario().getPersonal().getAreaOperativa()==10 || rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()== 38 || rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==43){
            listaReportes.add("Seguimiento actividades de estadía");
            listaReportes.add("Asignación de asesor académico por programa educativo");
            listaReportes.add("Cumplimiento estudiante documentos");
            listaReportes.add("Eficiencia estadía técnica");
            listaReportes.add("Listado estudiantes con promedios");
            listaReportes.add("Consulta memorias de estadía");
            rol.setReportes(listaReportes);
            rol.setReporte(rol.getReportes().get(0));
            generarReportes();
        }
    }
    
    /**
     * Permite generar el reporte seleccionado dependiendo la opción seleccionada
     */
    public void generarReportes(){
        if("Seguimiento actividades de estadía".equals(rol.getReporte())){
           generarSeguimientoActEstadia();
        }else if("Asignación de asesor académico por programa educativo".equals(rol.getReporte())){
           generarAsigProgramaAsesor();
        }else if("Cumplimiento estudiante documentos".equals(rol.getReporte())){
           generarCumpDocumentoPrograma();
        }else if("Eficiencia estadía técnica".equals(rol.getReporte())){
           generarEficienciaEstadia();
        }else if("Listado estudiantes con promedios".equals(rol.getReporte())){
           generarListadoEstudiantesPromedios();
        }else if("Consulta memorias de estadía".equals(rol.getReporte())){
           generarListadoEstudiantesPromedios();
        }else if("Zona influencia institucional".equals(rol.getReporte())){
           generarZonaInfluenciaIns();
        }else if("Zona influencia programa educativo".equals(rol.getReporte())){
           generarZonaInfluenciaPrograma();
        }
    }
    
     /**
     * Permite generar el reporte seleccionado dependiendo la opción seleccionada
     */
    public void generarReportesDireccionAcademica(){
        if("Eficiencia estadía técnica".equals(rol.getReporte())){
           generarEficienciaEstadiaAreaAcademica();
        }else if("Listado estudiantes con promedios".equals(rol.getReporte())){
           generarListadoEstudiantesPromediosAreaAcademica();
        }
    }
    
    /**
     * Permite generar el reporte de seguimiento de actividades de estadía
     */
    public void generarSeguimientoActEstadia(){
        ResultadoEJB<List<DtoReporteActividadesEstadia>> res = ejb.getSeguimientoActividadesEstadia(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
             rol.setListaSegActEstadia(res.getValor());
             rol.setTotalIniciaron(rol.getListaSegActEstadia().stream().mapToInt(p->p.getEstudiantesIniciaron()).sum());
             rol.setTotalActivos(rol.getListaSegActEstadia().stream().mapToInt(p->p.getEstudiantesActivos()).sum());
             rol.setTotalAsignados(rol.getListaSegActEstadia().stream().mapToInt(p->p.getEstudiantesAsignados()).sum());
             rol.setTotalNoAsignados(rol.getListaSegActEstadia().stream().mapToInt(p->p.getEstudiantesSinAsignar()).sum());
             rol.setPorcentajeAsignacion(String.format("%.2f", rol.getListaSegActEstadia().stream().mapToDouble(p->p.getPorcentajeAsignacion()).average().getAsDouble()));
             rol.setTotalInfoRegistrada(rol.getListaSegActEstadia().stream().mapToInt(p->p.getEstudiantesInformacion()).sum());
             rol.setTotalSinInfoRegistrada(rol.getListaSegActEstadia().stream().mapToInt(p->p.getEstudiantesSinInformacion()).sum());
             rol.setPorcentajeRegistro(String.format("%.2f", rol.getListaSegActEstadia().stream().mapToDouble(p->p.getPorcentajeRegistro()).average().getAsDouble()));
             rol.setTotalInfoValidada(rol.getListaSegActEstadia().stream().mapToInt(p->p.getEstudiantesValidados()).sum());
             rol.setTotalSinInfoValidada(rol.getListaSegActEstadia().stream().mapToInt(p->p.getEstudiantesSinValidar()).sum());
             rol.setPorcentajeValidacion(String.format("%.2f", rol.getListaSegActEstadia().stream().mapToDouble(p->p.getPorcentajeValidacion()).average().getAsDouble()));
             Ajax.update("tbSegActEstadia");
         }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite generar el listado de estudiantes asignados por programa educativo y asesor académico
     */
    public void generarAsigProgramaAsesor(){
        ResultadoEJB<List<DtoAsigAsesorAcadEstadia>> res = ejb.getAsignacionAsesorAcademicoPE(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
             rol.setListaAsigAsesorAcad(res.getValor());
             Ajax.update("tbAsigAsesorAcad");
         }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite generar el listado de cumplimiento de carga por documento y programa educativo
     */
    public void generarCumpDocumentoPrograma(){
        ResultadoEJB<List<DtoCumplimientoEstDocEstadia>> res = ejb.getCumplimientoDocumentoPE(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
             rol.setListaCumplimientoEstudiante(res.getValor());
             Ajax.update("tbCumpDocEstPE");
         }else mostrarMensajeResultadoEJB(res);
    }
    
    
    /**
     * Permite generar el reporte de eficiencia de estadía
     */
    public void generarEficienciaEstadia(){
        ResultadoEJB<List<DtoEficienciaEstadia>> res = ejb.getEficienciaEstadia(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
             rol.setEficienciaEstadia(res.getValor());
             rol.setTotalAcreditados(rol.getEficienciaEstadia().stream().mapToInt(p->p.getAcreditaron()).sum());
             rol.setTotalNoAcreditados(rol.getEficienciaEstadia().stream().mapToInt(p->p.getNoAcreditaron()).sum());
             rol.setTotal(rol.getEficienciaEstadia().stream().mapToInt(p->p.getSeguimiento()).sum());
             Ajax.update("tbEficienciaEstadia");
         }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite generar el reporte de eficiencia de estadía
     */
    public void generarEficienciaEstadiaAreaAcademica(){
        ResultadoEJB<List<DtoEficienciaEstadia>> res = ejb.getEficienciaEstadia(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
             rol.setEficienciaEstadia(res.getValor().stream().filter(p->p.getProgramaEducativo().getAreaSuperior().equals(rol.getUsuario().getAreaOperativa().getArea())).collect(Collectors.toList()));
             rol.setTotalAcreditados(rol.getEficienciaEstadia().stream().mapToInt(p->p.getAcreditaron()).sum());
             rol.setTotalNoAcreditados(rol.getEficienciaEstadia().stream().mapToInt(p->p.getNoAcreditaron()).sum());
             rol.setTotal(rol.getEficienciaEstadia().stream().mapToInt(p->p.getSeguimiento()).sum());
             Ajax.update("tbEficienciaEstadia");
         }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite generar el listado de estudiantes con resultados de evaluación de estadía registrados
     */
    public void generarListadoEstudiantesPromedios(){
        ResultadoEJB<List<DtoSeguimientoEstadia>> res = ejb.getListaEstudiantesPromedio(rol.getGeneracion(), rol.getNivelEducativo());
          if(res.getCorrecto()){
              rol.setListadoEstudiantesPromedio(res.getValor());
              Ajax.update("tbEstudiantesPromedios");
          }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite generar el listado de estudiantes con resultados de evaluación de estadía registrados
     */
    public void generarListadoEstudiantesPromediosAreaAcademica(){
        ResultadoEJB<List<DtoSeguimientoEstadia>> res = ejb.getListaEstudiantesPromedio(rol.getGeneracion(), rol.getNivelEducativo());
          if(res.getCorrecto()){
              rol.setListadoEstudiantesPromedio(res.getValor().stream().filter(p->p.getAreaSuperior().getArea().equals(rol.getUsuario().getAreaOperativa().getArea())).collect(Collectors.toList()));
              Ajax.update("tbEstudiantesPromedios");
          }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite generar el reporte por zona de influencia institucional 
     */
    public void generarZonaInfluenciaIns(){
        ResultadoEJB<List<DtoSeguimientoEstadia>> res = ejb.getListaEstudiantesPromedio(rol.getGeneracion(), rol.getNivelEducativo());
          if(res.getCorrecto()){
              rol.setListadoEstudiantesPromedio(res.getValor());
              ResultadoEJB<List<DtoZonaInfluenciaEstIns>> resZI = ejb.getListaZonaInfluenciaInstitucional(rol.getListadoEstudiantesPromedio());
              rol.setListaZonaInfluenciaIns(resZI.getValor());
              rol.setTotalEstColocados(rol.getListaZonaInfluenciaIns().stream().mapToInt(p->p.getEstudiantesColocados()).sum());
              rol.setTotalEstSeguimientoEstadia(rol.getListadoEstudiantesPromedio().stream().count());
              rol.setTotalPorcentajeEstColocados(rol.getListaZonaInfluenciaIns().stream().mapToDouble(p->p.getPorcentajeEstudiantes()).sum());
              Ajax.update("tbZonaInfluenciaInstitucional");
          }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite generar el reporte por zona de influencia por program educativo
     */
    public void generarZonaInfluenciaPrograma(){
        ResultadoEJB<List<DtoSeguimientoEstadia>> res = ejb.getListaEstudiantesPromedio(rol.getGeneracion(), rol.getNivelEducativo());
          if(res.getCorrecto()){
              rol.setListadoEstudiantesPromedio(res.getValor());
              ResultadoEJB<List<DtoZonaInfluenciaEstPrograma>> resZI = ejb.getListaZonaInfluenciaProgramaEducativo(rol.getListadoEstudiantesPromedio());
              rol.setListaZonaInfluenciaPrograma(resZI.getValor());
              Ajax.update("tbZonaInfluenciaPrograma");
          }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite que al cambiar o seleccionar una generación se pueda actualizar la lista de ´niveles educativos
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
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la información
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            if(rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==18 || rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==48){
                generarReportesDireccionAcademica();
            }else if(rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==15 || rol.getUsuario().getPersonal().getAreaOperativa()==10 || rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()== 38 || rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==43){
                generarReportes();
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un reporte se pueda actualizar la información
     * @param e Evento del cambio de valor
     */
    public void cambiarReporte(ValueChangeEvent e){
        if(e.getNewValue() instanceof  String){
            String reporte = (String)e.getNewValue();
            rol.setReporte(reporte);
            if(rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==18 || rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==48){
                generarReportesDireccionAcademica();
            }else if(rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==15 || rol.getUsuario().getPersonal().getAreaOperativa()==10 || rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()== 38 || rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==43){
                generarReportes();
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Método para verificar si el estudiante seleccionado aprobó sus evaluaciones de estadía (asesor externo e interno)
     * @param dtoSeguimientoEstadia
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean aproboEstadia(DtoSeguimientoEstadia dtoSeguimientoEstadia){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejbSeguimientoEstadia.verificarAproboEstadia(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante());
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
    /**
     * Método que permite descargar en excel los reportes de la generación y nivel educativo seleccionado
     * @throws java.io.IOException
     */
     public void descargarReportesEstadia() throws IOException, Throwable{
        if(rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==18 || rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==48){
            generarEficienciaEstadiaAreaAcademica();
            generarListadoEstudiantesPromediosAreaAcademica();
            File f = new File(ejb.getReportesEstadiaAreaAcademica(rol.getEficienciaEstadia(), rol.getListadoEstudiantesPromedio(), rol.getGeneracion(), rol.getNivelEducativo()));
            Faces.sendFile(f, true);
        }else if(rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==15 || rol.getUsuario().getPersonal().getAreaOperativa()==10 || rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()== 38 || rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==43){     
            generarSeguimientoActEstadia();
            generarAsigProgramaAsesor();
            generarCumpDocumentoPrograma();
            generarEficienciaEstadia();
            generarListadoEstudiantesPromedios();
            generarZonaInfluenciaIns();
            generarZonaInfluenciaPrograma();
            File f = new File(ejb.getReportesEstadia(rol.getListaSegActEstadia(), rol.getListaAsigAsesorAcad(), rol.getListaCumplimientoEstudiante(), rol.getEficienciaEstadia(), rol.getListadoEstudiantesPromedio(), rol.getListaZonaInfluenciaIns(), rol.getListaZonaInfluenciaPrograma(), rol.getGeneracion(), rol.getNivelEducativo()));
            Faces.sendFile(f, true);
        }
    }
     
    /**
     * Método que permite consultar si el documento ha sido registrado en la base de datos
     * @param claveDocumento 
     * @param dtoSeguimientoEstadia 
     * @return  Verdadero o Falso según sea el caso
     */
    public Boolean consultarExisteDocumento(Integer claveDocumento, DtoSeguimientoEstadia dtoSeguimientoEstadia){
        return ejbSeguimientoEstadia.consultarClaveDocumento(claveDocumento, dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante()).getValor();
    }
    
    /**
     * Método que busca la ruta del documento seleccionado para su descarga
     * @param claveDocumento
     * @param dtoSeguimientoEstadia
     * @throws java.io.IOException
     */
    public void descargarDocumento(Integer claveDocumento, DtoSeguimientoEstadia dtoSeguimientoEstadia) throws IOException{
        ResultadoEJB<DocumentoSeguimientoEstadia> resBuscarDoc = ejbSeguimientoEstadia.buscarDocumentoEstudiante(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante(), claveDocumento);
        File f = new File(resBuscarDoc.getValor().getRuta());
        Faces.sendFile(f, false);
    }
    
}
