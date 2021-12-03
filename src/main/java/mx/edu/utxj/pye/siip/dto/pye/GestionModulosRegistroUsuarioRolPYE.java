/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.pye;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistroEspecifico;

/**
 *
 * @author UTXJ
 */
public class GestionModulosRegistroUsuarioRolPYE extends AbstractRol {
     /**
     * Representa la referencia hacia el personal de planeación y evaluación
     */
    @Getter @NonNull private PersonalActivo usuario;
    
    /**
     * Representa la clave
     */
    @Getter @NonNull private Integer periodoActivo;
    
     /**
     * Lista de tipo de módulo
     */
    @Getter @NonNull private List<String> tipos;
    
     /**
     * Tipo de módulo seleccionado
     */
    @Getter @NonNull private String tipo;
    
     /**
     * Lista de ejes de registro
     */
    @Getter @NonNull private List<EjesRegistro> ejesRegistro;
    
     /**
     * Eje de registro seleccionado
     */
    @Getter @NonNull private EjesRegistro ejeRegistro;
     
    /**
     * Lista de módulos de registro activos
     */
    @Getter @NonNull private List<ModulosRegistro> modulosRegistro;
    
    /**
     * Módulo de registro seleccionado
     */
    @Getter @NonNull private ModulosRegistro moduloRegistro;
    
     /**
     * Representa valor si se habilita o no opciones para agregar usuario
     */
    @Getter @NonNull private Boolean agregarUsuario;
    
    /**
     * Personal seleccionado
     */
    @Getter @NonNull private PersonalActivo personal;
    
    /**
     * Representa valor si se habilita o no opciones para agregar área de registro
     */
    @Getter @NonNull private Boolean agregarAreaRegistro;
    
     /**
     * Lista de áreas de registro
     */
    @Getter @NonNull private List<AreasUniversidad> areasRegistro;
    
    /**
     * Área de registro seleccionada
     */
    @Getter private AreasUniversidad areaRegistro;
    
     /**
     * Lista de usuario registrados para el módulo seleccionado
     */
    @Getter @NonNull private List<DtoModulosRegistroUsuario> listaModulosRegistroAsignados;
    
    /**
     * Lista de áreas 
     */
    @Getter @NonNull private List<AreasUniversidad> areas;
    
    /**
     * Lista de módulos asignados al personal seleccionado
     */
    @Getter @NonNull private List<DtoModulosRegistroUsuario> listaModulosAsignadosPersonal;
    
    /**
     * Lista de áreas 
     */
    @Getter @NonNull private ModulosRegistroEspecifico modulosRegistroEspecifico;
    
    /**
     * Número de pestaña activa
     */
    @Getter private Integer pestaniaActiva;
   

    public GestionModulosRegistroUsuarioRolPYE(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
        super(filtro);
        this.usuario = usuario;
    }

    public void setUsuario(PersonalActivo usuario) {
        this.usuario = usuario;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setTipos(List<String> tipos) {
        this.tipos = tipos;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEjesRegistro(List<EjesRegistro> ejesRegistro) {
        this.ejesRegistro = ejesRegistro;
    }

    public void setEjeRegistro(EjesRegistro ejeRegistro) {
        this.ejeRegistro = ejeRegistro;
    }
    
    public void setModulosRegistro(List<ModulosRegistro> modulosRegistro) {
        this.modulosRegistro = modulosRegistro;
    }

    public void setModuloRegistro(ModulosRegistro moduloRegistro) {
        this.moduloRegistro = moduloRegistro;
    }

    public void setAgregarUsuario(Boolean agregarUsuario) {
        this.agregarUsuario = agregarUsuario;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setAgregarAreaRegistro(Boolean agregarAreaRegistro) {
        this.agregarAreaRegistro = agregarAreaRegistro;
    }
    
    public void setAreasRegistro(List<AreasUniversidad> areasRegistro) {
        this.areasRegistro = areasRegistro;
    }

    public void setAreaRegistro(AreasUniversidad areaRegistro) {
        this.areaRegistro = areaRegistro;
    }
    
    public void setListaModulosRegistroAsignados(List<DtoModulosRegistroUsuario> listaModulosRegistroAsignados) {
        this.listaModulosRegistroAsignados = listaModulosRegistroAsignados;
    }

    public void setAreas(List<AreasUniversidad> areas) {
        this.areas = areas;
    }

    public void setListaModulosAsignadosPersonal(List<DtoModulosRegistroUsuario> listaModulosAsignadosPersonal) {
        this.listaModulosAsignadosPersonal = listaModulosAsignadosPersonal;
    }

    public void setModulosRegistroEspecifico(ModulosRegistroEspecifico modulosRegistroEspecifico) {
        this.modulosRegistroEspecifico = modulosRegistroEspecifico;
    }

    public void setPestaniaActiva(Integer pestaniaActiva) {
        this.pestaniaActiva = pestaniaActiva;
    }
}
