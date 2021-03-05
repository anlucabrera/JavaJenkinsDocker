/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;

/**
 *
 * @author UTXJ
 */
public class AperturaExtemporaneaEstadiaRolMultiple extends AbstractRol{
    /**
     * Representa la referencia hacia al personal de servicios escolares o director(a) de carrera
     */
    @Getter @NonNull private PersonalActivo usuario;
    
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
     * Pista del estudiante
     */
    @Getter @NonNull private String pistaEstudiante;
   
    /**
     * Estudiante seleccionado
     */
    @Getter @NonNull private DtoEstudianteComplete estudianteSeleccionado;
    
    /**
     * Estudiante registrado
     */
    @Getter @NonNull private DtoDatosEstudiante estudianteRegistrado;
    
    /**
     * Actividad seleccionada
     */
    @Getter @NonNull private EventoEstadia actividad;
    
    /**
     * Lista de actividades
     */
    @Getter @NonNull private List<EventoEstadia> actividades;
    
     /**
     * Fecha de inicio del permiso
     */
    @Getter @NonNull private Date fechaInicio;
    
     /**
     * Fecha fin del permiso
     */
    @Getter @NonNull private Date fechaFin;
    
    /**
     * Habilita o deshabilita el botón para registrar entrega
     */
    @Getter @NonNull private Boolean desactivarRegistro;
    
     /**
     * Número de apertuas registradas de una actividad del estudiante
     */
    @Getter @NonNull private Integer numeroAperturasRegistradas;
    
    /**
     * Lista de aperturas extemporáneas de estadía
     */
    @Getter @NonNull private List<DtoAperturaExtemporaneaEstadia> listaAperturasExtemporaneas;
      
    public AperturaExtemporaneaEstadiaRolMultiple(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
        super(filtro);
        this.usuario = usuario;
    }

    public void setUsuario(PersonalActivo usuario) {
        this.usuario = usuario;
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

    public void setPistaEstudiante(String pistaEstudiante) {
        this.pistaEstudiante = pistaEstudiante;
    }

    public void setEstudianteSeleccionado(DtoEstudianteComplete estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }

    public void setEstudianteRegistrado(DtoDatosEstudiante estudianteRegistrado) {
        this.estudianteRegistrado = estudianteRegistrado;
    }

    public void setActividad(EventoEstadia actividad) {
        this.actividad = actividad;
    }

    public void setActividades(List<EventoEstadia> actividades) {
        this.actividades = actividades;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setDesactivarRegistro(Boolean desactivarRegistro) {
        this.desactivarRegistro = desactivarRegistro;
    }

    public void setNumeroAperturasRegistradas(Integer numeroAperturasRegistradas) {
        this.numeroAperturasRegistradas = numeroAperturasRegistradas;
    }
    
    public void setListaAperturasExtemporaneas(List<DtoAperturaExtemporaneaEstadia> listaAperturasExtemporaneas) {
        this.listaAperturasExtemporaneas = listaAperturasExtemporaneas;
    }
   
}

