/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

/**
 *
 * @author UTXJ
 */
public class LetreroFotografiasRolEstudiante {
    /**
     * Representa la referencia hacia al estudiante con seguimiento de estadía
     */
    @Getter @NonNull private Estudiante  estudiante;
    
    /**
     * Lista generaciones
     */
    @Getter private List<EventoEstadia> eventosEstadia;
  
    /**
     * Lista generaciones
     */
    @Getter @NonNull private List<Generaciones> generaciones;
    
    /**
     * Generacion seleccionada
     */
    @Getter @NonNull private Generaciones generacion;
    
   
    /**
     * Lista de niveles educativos
     */
    @Getter @NonNull private List<ProgramasEducativosNiveles> nivelesEducativos;
    
    /**
     * Nivel educativo seleccionado
     */
    @Getter @NonNull private ProgramasEducativosNiveles nivelEducativo;
    
    /**
     * Representa los datos del estudiante
     */
    @Getter @NonNull private DtoDatosEstudiante  dtoDatosEstudiante;
    
    /**
     * Representa la generación del estudiante convertida en texto
     */
    @Getter @NonNull private String generacionTexto;
    
     /**
     * Representa valor del código qr, matricula del estudiante en texto
     */
    @Getter @NonNull private String codigoQr;
    
     /**
     * Representa la fecha de emisión del documento, fecha en texto
     */
    @Getter @NonNull private String fechaEmision;
    
     /**
     * Representa evento de estadía seleccionado
     */
    @Getter @NonNull private EventoEstadia eventoEstadia;
    
     /**
     * Representa la fecha de inicio del evento, fecha en texto
     */
    @Getter @NonNull private String fechaInicio;
    
     /**
     * Representa la fecha de fin del evento, fecha en texto
     */
    @Getter @NonNull private String fechaFin;
    
    /**
     * Representa la fecha de fin del evento, fecha en texto
     */
    @Getter @NonNull private Boolean eventoActivo;
    
     /**
     * Representa el nivel del rol
     */
    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;
    
    /**
     * Lista de intruccions para utilizar el módulo
     */
    @Getter private List<String> instrucciones = new ArrayList<>();
    
    public Boolean tieneAcceso(Estudiante estudiante, UsuarioTipo usuarioTipo){
        if(estudiante == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        return true;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public void setEventosEstadia(List<EventoEstadia> eventosEstadia) {
        this.eventosEstadia = eventosEstadia;
    }
    
    public void setGeneraciones(List<Generaciones> generaciones) {
        this.generaciones = generaciones;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public void setNivelesEducativos(List<ProgramasEducativosNiveles> nivelesEducativos) {
        this.nivelesEducativos = nivelesEducativos;
    }

    public void setNivelEducativo(ProgramasEducativosNiveles nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public void setDtoDatosEstudiante(DtoDatosEstudiante dtoDatosEstudiante) {
        this.dtoDatosEstudiante = dtoDatosEstudiante;
    }

    public void setGeneracionTexto(String generacionTexto) {
        this.generacionTexto = generacionTexto;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public void setEventoEstadia(EventoEstadia eventoEstadia) {
        this.eventoEstadia = eventoEstadia;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setEventoActivo(Boolean eventoActivo) {
        this.eventoActivo = eventoActivo;
    }
    
    public void setInstrucciones(List<String> instrucciones) {
        this.instrucciones = instrucciones;
    }
    
}
