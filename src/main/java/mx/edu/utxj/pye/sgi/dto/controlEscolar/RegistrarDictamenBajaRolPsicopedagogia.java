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

/**
 *
 * @author UTXJ
 */
public class RegistrarDictamenBajaRolPsicopedagogia extends AbstractRol{
    
    /**
     * Representa la referencia hacia el personal de psicopedagogía
     */
    @Getter @NonNull private PersonalActivo personalPsicopedagogia;
    
     /**
     * Pista del estudiante
     */
    @Getter @NonNull private String pistaEstudiante;
   
    /**
     * Estudiante seleccionado
     */
    @Getter @NonNull private DtoEstudianteComplete estudianteSeleccionado;
    
    /**
     * Estudiante seleccionado
     */
    @Getter @NonNull private DtoDatosEstudiante datosEstudiante;
    
    /**
     * Representa la clave
     */
    @Getter @NonNull private Integer periodoActivo;
    
    /**
     * Información completa de la Baja registrada
     */
    @Getter @NonNull private DtoRegistroBajaEstudiante registroBajaEstudiante;
    
      /**
     * Lista de materias reprobadas registradas en caso de ser baja por reprobación
     */
    @Getter @NonNull private List<DtoMateriaReprobada> listaMateriasReprobadas;
    
     /**
     * Valor modal con materias reprobadas
     */
    @Getter @NonNull private Boolean forzarAperturaDialogo;
    
    public RegistrarDictamenBajaRolPsicopedagogia(Filter<PersonalActivo> filtro, PersonalActivo personalPsicopedagogia) {
        super(filtro);
        this.personalPsicopedagogia = personalPsicopedagogia;
    }

    public void setPersonalPsicopedagogia(PersonalActivo personalPsicopedagogia) {
        this.personalPsicopedagogia = personalPsicopedagogia;
    }

    public void setPistaEstudiante(String pistaEstudiante) {
        this.pistaEstudiante = pistaEstudiante;
    }

    public void setEstudianteSeleccionado(DtoEstudianteComplete estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }

    public void setDatosEstudiante(DtoDatosEstudiante datosEstudiante) {
        this.datosEstudiante = datosEstudiante;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setRegistroBajaEstudiante(DtoRegistroBajaEstudiante registroBajaEstudiante) {
        this.registroBajaEstudiante = registroBajaEstudiante;
    }

    public void setListaMateriasReprobadas(List<DtoMateriaReprobada> listaMateriasReprobadas) {
        this.listaMateriasReprobadas = listaMateriasReprobadas;
    }

    public void setForzarAperturaDialogo(Boolean forzarAperturaDialogo) {
        this.forzarAperturaDialogo = forzarAperturaDialogo;
    }
}
