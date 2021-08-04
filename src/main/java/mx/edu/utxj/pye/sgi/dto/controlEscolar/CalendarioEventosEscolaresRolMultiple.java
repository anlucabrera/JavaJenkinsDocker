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
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class CalendarioEventosEscolaresRolMultiple extends AbstractRol{
    /**
     * Representa la referencia hacia al usuario
     */
    @Getter @NonNull private PersonalActivo usuario;
    
     /**
     * Representa el periodo escolar activo
     */
    @Getter @NonNull private Integer periodoActivo;
  
    /**
     * Lista periodos escolares
     */
    @Getter @NonNull private List<PeriodosEscolares> periodosEscolares;
    
    /**
     * Periodo escolar seleccionado
     */
    @Getter @NonNull private PeriodosEscolares periodoEscolar;
    
    /**
     * Lista de eventos escolares registrados
     */
    @Getter @NonNull private List<DtoCalendarioEventosEscolares> listaEventosRegistrados;
    
    public CalendarioEventosEscolaresRolMultiple(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
        super(filtro);
        this.usuario = usuario;
    }

    public void setUsuario(PersonalActivo usuario) {
        this.usuario = usuario;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPeriodosEscolares(List<PeriodosEscolares> periodosEscolares) {
        this.periodosEscolares = periodosEscolares;
    }

    public void setPeriodoEscolar(PeriodosEscolares periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    public void setListaEventosRegistrados(List<DtoCalendarioEventosEscolares> listaEventosRegistrados) {
        this.listaEventosRegistrados = listaEventosRegistrados;
    }
    
}
