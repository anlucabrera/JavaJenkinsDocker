/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;

/**
 *
 * @author UTXJ
 */
public class RegistroEventosEstadiaRolEscolares extends AbstractRol{
    /**
     * Representa la referencia hacia al usuario
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
     * Nivel educativo seleccionado
     */
    @Getter @NonNull private Boolean existeRegistro;
    
    /**
     * Lista de eventos de estadía
     */
    @Getter @NonNull private List<DtoCalendarioEventosEstadia> listaEventosRegistrados;
    
     /**
     * Lista de eventos de estadía
     */
    @Getter @NonNull private List<DtoEventosEstadia> listaEventos;
    
    public RegistroEventosEstadiaRolEscolares(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
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

    public void setExisteRegistro(Boolean existeRegistro) {
        this.existeRegistro = existeRegistro;
    }

    public void setListaEventosRegistrados(List<DtoCalendarioEventosEstadia> listaEventosRegistrados) {
        this.listaEventosRegistrados = listaEventosRegistrados;
    }

    public void setListaEventos(List<DtoEventosEstadia> listaEventos) {
        this.listaEventos = listaEventos;
    }
}
