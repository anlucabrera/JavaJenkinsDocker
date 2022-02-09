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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoDiscapacidad;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.LenguaIndigena;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoSangre;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioDifusion;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class AdministracionCatalogosAdmisionRolEscolares extends AbstractRol{
    /**
     * Representa la referencia hacia el personal de servicios escolares
    */
    @Getter @NonNull private PersonalActivo personal;
    
     /**
     * Periodo escolar seleccionado para registro
     */
    @Getter @NonNull private PeriodosEscolares periodoActivo;
    
     /**
     * Habilitar o deshabilitar opciones para agregar tipo de discapacidad
     */
    @Getter @NonNull private Boolean agregarTipoDiscapacidad;
    
     /**
     * Habilitar o deshabilitar opciones para agregar lengua indigena
     */
    @Getter @NonNull private Boolean agregarLenguaIndigena;
    
     /**
     * Habilitar o deshabilitar opciones para agregar tipo de sangre
     */
    @Getter @NonNull private Boolean agregarTipoSangre;
    
     /**
     * Habilitar o deshabilitar opciones para agregar medio de difusión
     */
    @Getter @NonNull private Boolean agregarMedioDifusion;
    
    /**
     * Lista de tipo de discapacidad
     */
    @Getter @NonNull private List<TipoDiscapacidad> tiposDiscapacidad;
    
    /**
     * Lista de lengua indígenas
     */
    @Getter @NonNull private List<LenguaIndigena> lenguasIndigenas; 
    
     /**
     * Lista de tipo de sangre
     */
    @Getter @NonNull private List<TipoSangre> tiposSangre;
    
    /**
     * Lista de medios de difusión
     */
    @Getter @NonNull private List<MedioDifusion> mediosDifusion; 
    
    /**
     * Nombre de la nueva discapacidad
     */
    @Getter private String nuevaDiscapacidadNombre; 
    
    /**
     * Descripción de la nueva discapacidad
     */
    @Getter private String nuevaDiscapacidadDescripcion; 
    
    /**
     * Nueva lenga indígena
     */
    @Getter private String nuevaLengua; 
    
     /**
     * Nuevo tipo de sangre
     */
    @Getter private String nuevoTipoSangre; 
    
    
    /**
     * Nuevo medio de difusión
     */
    @Getter private String nuevoMedio; 
    
     /**
     * Número de pestaña activa del tab
     */
    @Getter @NonNull private Integer pestaniaActiva; 
    
    public AdministracionCatalogosAdmisionRolEscolares(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPeriodoActivo(PeriodosEscolares periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setAgregarTipoDiscapacidad(Boolean agregarTipoDiscapacidad) {
        this.agregarTipoDiscapacidad = agregarTipoDiscapacidad;
    }

    public void setAgregarLenguaIndigena(Boolean agregarLenguaIndigena) {
        this.agregarLenguaIndigena = agregarLenguaIndigena;
    }

    public void setAgregarTipoSangre(Boolean agregarTipoSangre) {
        this.agregarTipoSangre = agregarTipoSangre;
    }

    public void setAgregarMedioDifusion(Boolean agregarMedioDifusion) {
        this.agregarMedioDifusion = agregarMedioDifusion;
    }

    public void setTiposDiscapacidad(List<TipoDiscapacidad> tiposDiscapacidad) {
        this.tiposDiscapacidad = tiposDiscapacidad;
    }

    public void setLenguasIndigenas(List<LenguaIndigena> lenguasIndigenas) {
        this.lenguasIndigenas = lenguasIndigenas;
    }

    public void setTiposSangre(List<TipoSangre> tiposSangre) {
        this.tiposSangre = tiposSangre;
    }

    public void setMediosDifusion(List<MedioDifusion> mediosDifusion) {
        this.mediosDifusion = mediosDifusion;
    }

    public void setNuevaDiscapacidadNombre(String nuevaDiscapacidadNombre) {
        this.nuevaDiscapacidadNombre = nuevaDiscapacidadNombre;
        if(nuevaDiscapacidadNombre == null){
            this.setNuevaDiscapacidadNombre("Ingresar nombre");
        }
    }

    public void setNuevaDiscapacidadDescripcion(String nuevaDiscapacidadDescripcion) {
        this.nuevaDiscapacidadDescripcion = nuevaDiscapacidadDescripcion;
        if(nuevaDiscapacidadDescripcion == null){
            this.setNuevaDiscapacidadDescripcion("Ingresar descripcion");
        }
    }

    public void setNuevaLengua(String nuevaLengua) {
        this.nuevaLengua = nuevaLengua;
        if(nuevaLengua == null){
            this.setNuevaLengua("Ingresar nombre");
        }
    }

    public void setNuevoTipoSangre(String nuevoTipoSangre) {
        this.nuevoTipoSangre = nuevoTipoSangre;
        if(nuevoTipoSangre == null){
            this.setNuevoTipoSangre("Ingresar nombre");
        }
    }

    public void setNuevoMedio(String nuevoMedio) {
        this.nuevoMedio = nuevoMedio;
        if(nuevoMedio == null){
            this.setNuevoMedio("Ingresar nombre");
        }
    }

    public void setPestaniaActiva(Integer pestaniaActiva) {
        this.pestaniaActiva = pestaniaActiva;
    }
    
}
