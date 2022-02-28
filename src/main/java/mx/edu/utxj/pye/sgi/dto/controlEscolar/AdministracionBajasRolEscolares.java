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
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausaCategoria;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class AdministracionBajasRolEscolares extends AbstractRol{
    
    /**
     * Representa la referencia hacia el personal de servicios escolares
    */
    @Getter @NonNull private PersonalActivo personal;
    
     /**
     * Periodo escolar seleccionado para registro
     */
    @Getter @NonNull private PeriodosEscolares periodoActivo;
    
    /**
     * Lista de tipos de baja registradas
     */
    @Getter @NonNull private List<BajasTipo> tiposBaja;

    /**
     * Lista de causas de baja registradas
     */
    @Getter @NonNull private List<BajasCausa> causasBaja;
    
    /**
     * Lista de causas baja con su categoría
     */
    @Getter @NonNull private List<BajasCausaCategoria> categoriasCausaBaja;
    
     /**
     * Habilitar o deshabilitar opciones para agregar tipo de baja
     */
    @Getter @NonNull private Boolean agregarTipoBaja;
    
     /**
     * Habilitar o deshabilitar opciones para agregar causa de baja
     */
    @Getter @NonNull private Boolean agregarCausaBaja;
    
     /**
     * Habilitar o deshabilitar opciones para agregar relación baja a una categoría
     */
    @Getter @NonNull private Boolean agregarBajaCategoria;
    
    /**
     * Nuevo tipo de baja
     */
    @Getter @NonNull private String nuevoTipoBaja;

    /**
     * Nueva causa de baja
     */
    @Getter @NonNull private String nuevaCausaBaja;  
    
    /**
     * Lista de categorías de baja
     */
    @Getter @NonNull private List<String> categorias;
    
     /**
     * Categorías de baja seleccionada
     */
    @Getter @NonNull private String categoria;
    
     /**
     * Lista de causas de baja sin categoría
     */
    @Getter @NonNull private List<BajasCausa> causasBajaDisponibles;
    
     /**
     * Causa de baja seleccionada
     */
    @Getter @NonNull private BajasCausa causaBaja;
    
     /**
     * Número de pestaña activa del tab
     */
    @Getter @NonNull private Integer pestaniaActiva; 
    
    public AdministracionBajasRolEscolares(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setPeriodoActivo(PeriodosEscolares periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setTiposBaja(List<BajasTipo> tiposBaja) {
        this.tiposBaja = tiposBaja;
    }

    public void setCausasBaja(List<BajasCausa> causasBaja) {
        this.causasBaja = causasBaja;
    }

    public void setCategoriasCausaBaja(List<BajasCausaCategoria> categoriasCausaBaja) {
        this.categoriasCausaBaja = categoriasCausaBaja;
    }

    public void setAgregarTipoBaja(Boolean agregarTipoBaja) {
        this.agregarTipoBaja = agregarTipoBaja;
    }

    public void setAgregarCausaBaja(Boolean agregarCausaBaja) {
        this.agregarCausaBaja = agregarCausaBaja;
    }

    public void setAgregarBajaCategoria(Boolean agregarBajaCategoria) {
        this.agregarBajaCategoria = agregarBajaCategoria;
    }

    public void setNuevoTipoBaja(String nuevoTipoBaja) {
        this.nuevoTipoBaja = nuevoTipoBaja;
        if(nuevoTipoBaja == null){
            this.setNuevoTipoBaja("Ingresar nombre");
        }
    }

    public void setNuevaCausaBaja(String nuevaCausaBaja) {
        this.nuevaCausaBaja = nuevaCausaBaja;
        if(nuevaCausaBaja == null){
            this.setNuevaCausaBaja("Ingresar nombre");
        }
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setCausasBajaDisponibles(List<BajasCausa> causasBajaDisponibles) {
        this.causasBajaDisponibles = causasBajaDisponibles;
    }
    
    public void setCausaBaja(BajasCausa causaBaja) {
        this.causaBaja = causaBaja;
    }

    public void setPestaniaActiva(Integer pestaniaActiva) {
        this.pestaniaActiva = pestaniaActiva;
    }
        
}
