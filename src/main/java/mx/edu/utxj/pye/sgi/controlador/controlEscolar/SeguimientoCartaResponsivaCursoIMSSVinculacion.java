/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SeguimientoCartaResponsivaCursoIMSSRolVinculacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCartaResponsivaCursoIMMSEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCargaCartaResponsivaCursoIMSS;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoVinculacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoVinculacionEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class SeguimientoCartaResponsivaCursoIMSSVinculacion extends ViewScopedRol implements Desarrollable{
    @Getter @Setter SeguimientoCartaResponsivaCursoIMSSRolVinculacion rol;
    
    @EJB  EjbCargaCartaResponsivaCursoIMSS ejb;
    @EJB  EjbSeguimientoEstadia ejbSeguimientoEstadia;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por dirección de carrera<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;
@Getter private SeguimientoVinculacionEstudiante seguimientoVinculacionEstudiante;

@PostConstruct
    public void init(){
    if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_CARTARESPONSIVA_CURSOIMSS);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbSeguimientoEstadia.validarCoordinadorEstadia(logon.getPersonal().getClave());//validar si es director

            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo coordinadorEstadia = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new SeguimientoCartaResponsivaCursoIMSSRolVinculacion(filtro, coordinadorEstadia);
            tieneAcceso = rol.tieneAcceso(coordinadorEstadia);
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setCoordinadorEstadia(coordinadorEstadia);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
            
            rol.getInstrucciones().add("Seleccione generación.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("Seleccione programa educativo");
            rol.getInstrucciones().add("En la tabla podrá visualizar la información de seguimiento de los estudiantes.");
            rol.getInstrucciones().add("Los datos indicados con * son aquellos en los que interviene.");
            rol.getInstrucciones().add("Dar clic en botón que se habilita para validar o invalidar los documentos.");
            rol.getInstrucciones().add("Dar clic en el campo del comentario para capturar las observaciones, en caso contrario dejar con el texto predeterminado.");
            
            rol.setMostrarSegValVinc(false);
            rol.setOcultarColumnas(false);
            
            generacionesEventosRegistrados();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimiento carta responsiva y curso imss";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de generaciones en los que existen eventos de vinculación registrados
     */
    public void generacionesEventosRegistrados(){
        ResultadoEJB<List<Generaciones>> res = ejb.getGeneracionesSeguimientoRegistrados();
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
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejb.getNivelesSeguimientoRegistrados(rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            listaProgramasNivelGeneracion();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite obtener la lista de programas educativos en los que existen eventos de vinculación registrados de la generación y nivel educativos seleccionados
     */
    public void listaProgramasNivelGeneracion(){
        if(rol.getNivelEducativo()== null) return;
        ResultadoEJB<List<AreasUniversidad>> res = ejb.getProgramasEducativosSeguimientoRegistrados(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
            rol.setProgramasEducativos(res.getValor());
            if(!rol.getProgramasEducativos().isEmpty()){
                rol.setProgramaEducativo(rol.getProgramasEducativos().get(0));
                listaEstudiantesSeguimiento();
            }else{
                rol.setProgramaEducativo(null);
                rol.setEstudiantesSeguimiento(Collections.emptyList());
            }
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de estudiantes con seguimiento de vinculación de la generación, nivel y programa educativo seleccionado
     */
    public void listaEstudiantesSeguimiento(){
        ResultadoEJB<List<DtoCartaResponsivaCursoIMMSEstudiante>> res = ejb.getListaSeguimientosVinculacion(rol.getGeneracion(), rol.getNivelEducativo(), rol.getProgramaEducativo());
        if(res.getCorrecto()){
            if(!rol.getMostrarSegValVinc()){
                rol.setEstudiantesSeguimiento(res.getValor());
            }else{
                rol.setEstudiantesSeguimiento(res.getValor().stream().filter(p->!p.getSeguimientoVinculacionEstudiante().getValidado()).collect(Collectors.toList()));
            }
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite que al cambiar o seleccionar una generación se pueda actualizar la lista de niveles, programas educativos y seguimientos de vinculación 
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
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la lista de programas educativos y seguimientos de vinculación 
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            listaProgramasNivelGeneracion();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la lista de eguimientos de vinculación 
     * @param e Evento del cambio de valor
     */
    public void cambiarProgramaEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  AreasUniversidad){
            AreasUniversidad programa = (AreasUniversidad)e.getNewValue();
            rol.setProgramaEducativo(programa);
            listaEstudiantesSeguimiento();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Método que permite consultar si el documento ha sido registrado en la base de datos
     * @param claveDocumento 
     * @param dtoCartaResponsivaCursoIMMSEstudiante 
     * @return  Verdadero o Falso según sea el caso
     */
    public Boolean consultarExisteDocumento(Integer claveDocumento, DtoCartaResponsivaCursoIMMSEstudiante dtoCartaResponsivaCursoIMMSEstudiante){
        ResultadoEJB<DocumentoSeguimientoVinculacion> resBuscarDoc = ejb.buscarDocumentoEstudiante(dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante(), claveDocumento);
        Boolean valor = resBuscarDoc.getValor().getDocumentoVinculacion() != null;
        return valor;
    }
    
    /**
     * Método que busca la ruta del documento seleccionado para su descarga
     * @param claveDocumento
     * @param dtoCartaResponsivaCursoIMMSEstudiante
     * @throws java.io.IOException
     */
    public void descargarDocumento(Integer claveDocumento, DtoCartaResponsivaCursoIMMSEstudiante dtoCartaResponsivaCursoIMMSEstudiante) throws IOException{
        ResultadoEJB<DocumentoSeguimientoVinculacion> resBuscarDoc = ejb.buscarDocumentoEstudiante(dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante(), claveDocumento);
        File f = new File(resBuscarDoc.getValor().getRuta());
        Faces.sendFile(f, false);
    }
    
    /**
     * Método que busca si el documento ha sido validado
     * @param claveDocumento
     * @param dtoCartaResponsivaCursoIMMSEstudiante
     * @return 
     */
    public Boolean buscarValidacionDocumento(Integer claveDocumento,  DtoCartaResponsivaCursoIMMSEstudiante  dtoCartaResponsivaCursoIMMSEstudiante){
        ResultadoEJB<DocumentoSeguimientoVinculacion> resBuscarDoc = ejb.buscarDocumentoEstudiante(dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante(), claveDocumento);
        Boolean valor = resBuscarDoc.getValor().getValidado();
        return valor;
    }
    
    /**
     * Método que permite buscar los comentarios realizados al documento seleccionado
     * @param claveDocumento 
     * @param dtoCartaResponsivaCursoIMMSEstudiante 
     * @return Comentario del documento
     */
    public DocumentoSeguimientoVinculacion comentariosDocumento(Integer claveDocumento, DtoCartaResponsivaCursoIMMSEstudiante dtoCartaResponsivaCursoIMMSEstudiante){
        ResultadoEJB<DocumentoSeguimientoVinculacion> resBuscarDoc = ejb.buscarDocumentoEstudiante(dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante(), claveDocumento);
        return resBuscarDoc.getValor();
    }
    
     /**
     * Método que permite validar o invalidar el documento del estudiante seleccionado
     * @param claveDocumento
     * @param dtoCartaResponsivaCursoIMMSEstudiante
     */
    public void validarDocumento(Integer claveDocumento, DtoCartaResponsivaCursoIMMSEstudiante dtoCartaResponsivaCursoIMMSEstudiante){
        ResultadoEJB<DocumentoSeguimientoVinculacion> resValidar = ejb.validarDocumento(claveDocumento, dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante());
        mostrarMensajeResultadoEJB(resValidar);
        actualizarValidacionSeguimiento(dtoCartaResponsivaCursoIMMSEstudiante);
        listaEstudiantesSeguimiento();
        Ajax.update("frm");
    }
    
     /**
     * Método que permite validar o invalidar el documento del estudiante seleccionado
     * @param dtoCartaResponsivaCursoIMMSEstudiante
     */
    public void actualizarValidacionSeguimiento(DtoCartaResponsivaCursoIMMSEstudiante dtoCartaResponsivaCursoIMMSEstudiante){
        ResultadoEJB<SeguimientoVinculacionEstudiante> resValidar = ejb.validarSeguimiento(dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante());
        mostrarMensajeResultadoEJB(resValidar);
    }
    
     /**
     * Método que permite guardar el comentario del documento del estudiante seleccionado
     * @param event 
     */
    public void guardarComentarioDocumento(ValueChangeEvent event){
        String comentario = event.getNewValue().toString();
        String claveDocumento = (String) event.getComponent().getAttributes().get("documento");
        DtoCartaResponsivaCursoIMMSEstudiante dtoCartaResponsivaCursoIMMSEstudiante = (DtoCartaResponsivaCursoIMMSEstudiante) event.getComponent().getAttributes().get("seguimiento");
        Integer documento = Integer.parseInt(claveDocumento);
        ResultadoEJB<DocumentoSeguimientoVinculacion> res = ejb.guardarComentarioDocumento(comentario, documento, dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante());
        Messages.addGlobalInfo("Se ha registrado correctamente el comentario");
        Ajax.update("frm");
        
    }
  
      /**
     * Método que cambia el tipo de visualización de la información (todos los seguimientos o solo los que están validados)
     * @param event Evento al tipo de visualización
     */
    public void cambiarMostrarSegValVinc(ValueChangeEvent event){
        if(rol.getMostrarSegValVinc()){
            rol.setMostrarSegValVinc(false);
            listaEstudiantesSeguimiento();
        }else{
            rol.setMostrarSegValVinc(true);
            listaEstudiantesSeguimiento();
        }
        Ajax.update("frm");
    }
    
      /**
     * Método que cambia el tipo de visualización de la información (Mostrar u ocultar columnas)
     * @param event Evento al tipo de visualización
     */
    public void cambiarOcultarColumnas(ValueChangeEvent event){
        if(rol.getOcultarColumnas()){
            rol.setOcultarColumnas(false);
        }else{
            rol.setOcultarColumnas(true);
        }
        Ajax.update("frm");
    }
   
}
