/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Domicilio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;

/**
 *
 * @author UTXJ
 */
public class RegistroExpedienteRolTitulacion extends AbstractRol{
    
    /**
     * Representa la referencia hacia el personal de titulación
     */
    @Getter @NonNull private PersonalActivo personal;
    
    /**
     * Lista generaciones
     */
    @Getter @NonNull private List<Generaciones> generaciones;
    
    /**
     * Lista niveles educativos
     */
    @Getter @NonNull private List<ProgramasEducativosNiveles> nivelesEducativos;
    
    /**
     * Lista proceos de integración de expediente
     */
    @Getter @NonNull private List<EventoTitulacion> procesosIntegracion;
   
    /**
     * Generación seleccionada
     */
    @Getter @NonNull private Generaciones generacion;
    
    /**
     * Nivel educativo seleccionado
     */
    @Getter @NonNull private ProgramasEducativosNiveles nivelEducativo;
    
    /**
     * Proceso de integración seleccionado
     */
    @Getter @NonNull private EventoTitulacion procesoIntegracion;
    
    /**
     * Clave del periodo escolar activo
     */
    @Getter @NonNull private Integer periodoActivo;
  
      /**
     * Pista del estudiante
     */
    @Getter @NonNull private String pistaEstudiante;
   
    /**
     * Estudiante seleccionado
     */
    @Getter @NonNull private DtoEstudianteComplete estudianteSeleccionado;
    
    /**
     * Datos del estudiante para registrar expediente
     */
    @Getter @NonNull private DtoNuevoExpedienteTitulacion datosNuevoExpediente;
    
     /**
     * Fecha de integración de expediente
     */
    @Getter @NonNull private Date fechaIntExp;
    
     /**
     * Valor de existencia del expediente
     */
    @Getter private Boolean existeExpediente;
    
     /**
     * Valor para habilitar o deshabilitar botón
     */
    @Getter private Boolean habilitarBoton;
    
     /**
     * Expediente de titulación registrado
     */
    @Getter private ExpedienteTitulacion expedienteTitulacion;
    
     /**
     * Paso en el que se encuentra el registro del expediente
     */
    @Getter private String pasoRegistroInd;
    
     /**
     * Medios de comunicaicón registrados
     */
    @Getter private MedioComunicacion medioComunicacion;
    
     /**
     * Domicilio registrado
     */
    @Getter private Domicilio domicilio;
    
     /**
     * Datos académicos registrados
     */
    @Getter private DatosAcademicos datosAcademicos;

     /**
     * Representa SelectItem Ubicación de domicilio en que radica
     */
    @Getter private List<SelectItem> listaEstadosDomicilioRadica;
    
    @Getter private List<SelectItem> listaMunicipiosRadica;
    
    @Getter private List<SelectItem> listaAsentamientos;
    
    
     /**
     * Representa SelectItem Ubicación de iems
     */
    @Getter private List<SelectItem> listaEstadosIEMS;
    
    @Getter private List<SelectItem> listaMunicipiosIEMS;
    
    @Getter private List<SelectItem> listaLocalidadesIEMS;
    
    @Getter private List<SelectItem> listaIEMS;
    
     
    public RegistroExpedienteRolTitulacion(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setGeneraciones(List<Generaciones> generaciones) {
        this.generaciones = generaciones;
    }

    public void setNivelesEducativos(List<ProgramasEducativosNiveles> nivelesEducativos) {
        this.nivelesEducativos = nivelesEducativos;
    }

    public void setProcesosIntegracion(List<EventoTitulacion> procesosIntegracion) {
        this.procesosIntegracion = procesosIntegracion;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public void setNivelEducativo(ProgramasEducativosNiveles nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public void setProcesoIntegracion(EventoTitulacion procesoIntegracion) {
        this.procesoIntegracion = procesoIntegracion;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPistaEstudiante(String pistaEstudiante) {
        this.pistaEstudiante = pistaEstudiante;
    }

    public void setEstudianteSeleccionado(DtoEstudianteComplete estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }

    public void setDatosNuevoExpediente(DtoNuevoExpedienteTitulacion datosNuevoExpediente) {
        this.datosNuevoExpediente = datosNuevoExpediente;
    }

    public void setFechaIntExp(Date fechaIntExp) {
        this.fechaIntExp = fechaIntExp;
    }

    public void setExisteExpediente(Boolean existeExpediente) {
        this.existeExpediente = existeExpediente;
    }

    public void setHabilitarBoton(Boolean habilitarBoton) {
        this.habilitarBoton = habilitarBoton;
    }
    
    public void setExpedienteTitulacion(ExpedienteTitulacion expedienteTitulacion) {
        this.expedienteTitulacion = expedienteTitulacion;
    }

    public void setPasoRegistroInd(String pasoRegistroInd) {
        this.pasoRegistroInd = pasoRegistroInd;
    }

    public void setMedioComunicacion(MedioComunicacion medioComunicacion) {
        this.medioComunicacion = medioComunicacion;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public void setDatosAcademicos(DatosAcademicos datosAcademicos) {
        this.datosAcademicos = datosAcademicos;
    }
    
//    public void setPromedio(Float promedio) {
//        this.promedio = promedio;
//    }
//
//    public void setServicioSocial(Boolean servicioSocial) {
//        this.servicioSocial = servicioSocial;
//    }
    
    public void setListaEstadosDomicilioRadica(List<SelectItem> listaEstadosDomicilioRadica) {
        this.listaEstadosDomicilioRadica = listaEstadosDomicilioRadica;
    }

    public void setListaMunicipiosRadica(List<SelectItem> listaMunicipiosRadica) {
        this.listaMunicipiosRadica = listaMunicipiosRadica;
    }

    public void setListaAsentamientos(List<SelectItem> listaAsentamientos) {
        this.listaAsentamientos = listaAsentamientos;
    }

    public void setListaEstadosIEMS(List<SelectItem> listaEstadosIEMS) {
        this.listaEstadosIEMS = listaEstadosIEMS;
    }

    public void setListaMunicipiosIEMS(List<SelectItem> listaMunicipiosIEMS) {
        this.listaMunicipiosIEMS = listaMunicipiosIEMS;
    }

    public void setListaLocalidadesIEMS(List<SelectItem> listaLocalidadesIEMS) {
        this.listaLocalidadesIEMS = listaLocalidadesIEMS;
    }

    public void setListaIEMS(List<SelectItem> listaIEMS) {
        this.listaIEMS = listaIEMS;
    }
    
}
