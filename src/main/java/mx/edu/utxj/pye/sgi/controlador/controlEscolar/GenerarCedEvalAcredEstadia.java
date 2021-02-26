/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionCriterioEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FolioAcreditacionEstadia;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@SessionScoped
public class GenerarCedEvalAcredEstadia implements Serializable{

    private static final long serialVersionUID = -2954478546015327422L;
    
    @Setter @Getter private String fechaEmision;
    @Setter @Getter private String fechaInicioEstadia;
    @Setter @Getter private String fechaFinEstadia;
    @Setter @Getter private FolioAcreditacionEstadia folioAcreditacionEstadia;
    @Setter @Getter private Boolean evaluacionRegistrada;
    @Setter @Getter private DtoSeguimientoEstadia dtoSeguimientoEstadia;
    @Setter @Getter private List<CalificacionCriterioEstadia> listaEvaluacionEstadiaRegistrada;
    @EJB EjbSeguimientoEstadia ejb;
    @EJB EjbRegistroBajas ejbRegistroBajas;
    
      /**
     * Permite generar el formato de cédula de evaluación y acreditación de estadía de un estudiante 
     * @param dtoSeguimientoEstadia Registro de la baja
     * @param evaluacion
     */
    public void generarFormato(DtoSeguimientoEstadia dtoSeguimientoEstadia, Boolean evaluacion){
       setDtoSeguimientoEstadia(dtoSeguimientoEstadia);
       setEvaluacionRegistrada(evaluacion);
       listaCriteriosEvaluacionEstadiaRegistrada();
       convertirFechasTexto();
       buscarFolio();
//       asignarFolio();
    }
    
     /**
     * Permite obtener la lista de criterios de evaluación de estadía del estudiante
     */
    public void listaCriteriosEvaluacionEstadiaRegistrada(){
        ResultadoEJB<List<CalificacionCriterioEstadia>> res = ejb.getListaEvaluacionEstadiaRegistrada(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante());
        if(res.getCorrecto()){
           setListaEvaluacionEstadiaRegistrada(res.getValor());
           Ajax.update("frmModalCapturaEvaluacion");
        }else Messages.addGlobalWarn("No se encontró la evaluación");
    
    }
    
      /**
     * Permite convertir las fechas de inicio y fin de la estadía en texto
     */
    public void convertirFechasTexto(){
        SimpleDateFormat completa = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("ES", "MX"));
        SimpleDateFormat sinAnio = new SimpleDateFormat("d 'de' MMMM ", new Locale("ES", "MX"));
        SimpleDateFormat conDias = new SimpleDateFormat("d 'días del mes de' MMMM 'de' yyyy", new Locale("ES", "MX"));
        
        setFechaEmision(conDias.format(new Date()));
        setFechaInicioEstadia(sinAnio.format(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getFechaInicio()));
        setFechaFinEstadia(completa.format(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getFechaFin()));
    }
    
    /**
     * Permite buscar si ya existe folio registrado para el seguimiento de estadía del estudiante
     */
    public void buscarFolio(){
        ResultadoEJB<FolioAcreditacionEstadia> res = ejb.getFolioAcreditacionEstadia(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante());
        if(res.getCorrecto()){
           setFolioAcreditacionEstadia(res.getValor());
           if(folioAcreditacionEstadia == null){
               asignarFolio();
           }
           Ajax.update("frmModalCapturaEvaluacion");
        }else Messages.addGlobalWarn("No se encontró folio registrado");
    
    }
    
    /**
     * Permite asignar folio para la cédula de evaluación y acreditación de estadía
     */
    public void asignarFolio(){
        ResultadoEJB<FolioAcreditacionEstadia> res = ejb.asignarFolioAcreditacionEstadia(dtoSeguimientoEstadia);
        if(res.getCorrecto()){
           setFolioAcreditacionEstadia(res.getValor());
           Ajax.update("frmModalCapturaEvaluacion");
        }else Messages.addGlobalWarn("No se encontró folio registrado");
    
    }
}
