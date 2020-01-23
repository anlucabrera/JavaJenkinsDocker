/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

/**
 *
 * @author UTXJ
 */
public class CasosCriticosRolEstudiante {
    @Getter     @Setter     DtoEstudiante                       dtoEstudiante;
    @Getter     @Setter     @NonNull                            protected                                       NivelRol nivelRol = NivelRol.CONSULTA;
    
    @Getter     @NonNull    private                             PeriodosEscolares                               periodoActivo;
    @Getter     @NonNull    private                             PeriodosEscolares                               periodoSeleccionado;
    @Getter     @NonNull    private                             List<PeriodosEscolares>                         periodosEscolares;
    
    @Getter     private     DtoCasoCritico                      dtoCasoCriticoSeleccionado;
    @Getter     private     DtoCasoCritico                      dtoCasoCritico;
    @Getter     private     List<DtoCasoCritico>                listaCasosCriticos;
    
    @Getter     @Setter     String                              fechaCasoCritico;
    @Getter     @Setter     String                              fechaCierreCasoCritico;
    public CasosCriticosRolEstudiante(@NonNull DtoEstudiante dtoEstudiante){
        this.dtoEstudiante = dtoEstudiante;
    }
    
    public Boolean tieneAcceso(DtoEstudiante dtoEstudiante, UsuarioTipo usuarioTipo){
        if(dtoEstudiante == null || !usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        else return true; 
    }

    public void setPeriodoSeleccionado(PeriodosEscolares periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
        if(this.periodoSeleccionado == null){
            setListaCasosCriticos(Collections.EMPTY_LIST);
        }
    }

    public void setPeriodosEscolares(List<PeriodosEscolares> periodosEscolares) {
        this.periodosEscolares = periodosEscolares;
        if(!periodosEscolares.isEmpty()){
            periodoSeleccionado = periodosEscolares.get(0);
        }
    }

    public void setPeriodoActivo(PeriodosEscolares periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setDtoCasoCritico(DtoCasoCritico dtoCasoCritico) {
        this.dtoCasoCritico = dtoCasoCritico;
    }

    public void setListaCasosCriticos(List<DtoCasoCritico> listaCasosCriticos) {
        this.listaCasosCriticos = listaCasosCriticos;
    }

    public void setDtoCasoCriticoSeleccionado(DtoCasoCritico dtoCasoCriticoSeleccionado) {
        this.dtoCasoCriticoSeleccionado = dtoCasoCriticoSeleccionado;
    }
    
}
