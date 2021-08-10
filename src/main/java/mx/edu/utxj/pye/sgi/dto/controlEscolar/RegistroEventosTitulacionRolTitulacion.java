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
/**
 *
 * @author UTXJ
 */
public class RegistroEventosTitulacionRolTitulacion extends AbstractRol{
     /**
     * Representa la referencia hacia al usuario
     */
    @Getter @NonNull private PersonalActivo usuario;
    
     /**
     * Representa el periodo escolar activo
     */
    @Getter @NonNull private Integer periodoActivo;
    
     /**
     * Lista de eventos de titulación registrados de generaciones de control escolar
     */
    @Getter @NonNull private List<DtoEventosTitulacion.GeneracionesControlEscolar> listEventosTitulacionCE;
    
     /**
     * Lista de eventos de titulación registrados de generaciones de control escolar
     */
    @Getter @NonNull private List<DtoEventosTitulacion.GeneracionesSAIIUT> listEventosTitulacionSAIIUT;
    
    public RegistroEventosTitulacionRolTitulacion(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
        super(filtro);
        this.usuario = usuario;
    }

    public void setUsuario(PersonalActivo usuario) {
        this.usuario = usuario;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setListEventosTitulacionCE(List<DtoEventosTitulacion.GeneracionesControlEscolar> listEventosTitulacionCE) {
        this.listEventosTitulacionCE = listEventosTitulacionCE;
    }

    public void setListEventosTitulacionSAIIUT(List<DtoEventosTitulacion.GeneracionesSAIIUT> listEventosTitulacionSAIIUT) {
        this.listEventosTitulacionSAIIUT = listEventosTitulacionSAIIUT;
    }
}
