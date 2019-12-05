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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ParticipantesTutoriaGrupal;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

/**
 *
 * @author UTXJ
 */
public class ValidacionTutoriaGrupalRolEstudiante {
    @Getter     @Setter     DtoEstudiante                       dtoEstudiante;
    @Getter     @Setter     @NonNull                            protected                                       NivelRol nivelRol = NivelRol.CONSULTA;
    
    @Getter     private     EventosRegistros                    eventoRegistroActivo, eventoSeleccionado;
    @Getter     private     List<EventosRegistros>              eventosPorPeriodo;
    
    @Getter     private     Integer                             periodoActivo;
    @Getter     private     PeriodosEscolares                   periodoSeleccionado;
    @Getter     private     List<PeriodosEscolares>             periodosConTutoriasGrupales;
    
    @Getter     private     ParticipantesTutoriaGrupal          participantesTutoriaGrupal;
    @Getter     private     List<ParticipantesTutoriaGrupal>    listaParticipantesTutoriaGrupal;
    @Getter     @Setter     private                             List<ParticipantesTutoriaGrupal>                filtroListaParticipantesTutoriaGrupal;

    public ValidacionTutoriaGrupalRolEstudiante(@NonNull DtoEstudiante dtoEstudiante) {
        this.dtoEstudiante = dtoEstudiante;
    }
    
    public Boolean tieneAcceso(DtoEstudiante dtoEstudiante, UsuarioTipo usuarioTipo){
        if(dtoEstudiante == null || !usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        else return true;
    }

    public void setPeriodoSeleccionado(PeriodosEscolares periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
        this.setListaParticipantesTutoriaGrupal(Collections.EMPTY_LIST);
        if(this.periodoSeleccionado == null){
            setEventosPorPeriodo(Collections.EMPTY_LIST);
        }
    }

    public void setPeriodosConTutoriasGrupales(List<PeriodosEscolares> periodosConTutoriasGrupales) {
        this.periodosConTutoriasGrupales = periodosConTutoriasGrupales;
        if(!periodosConTutoriasGrupales.isEmpty()){
            periodoSeleccionado = periodosConTutoriasGrupales.get(0);
        }
    }
    
    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }
    
    public void setEventoRegistroActivo(EventosRegistros eventoRegistroActivo) {
        this.eventoRegistroActivo = eventoRegistroActivo;
    }
    
    public void setEventoSeleccionado(EventosRegistros eventoSeleccionado) {
        this.eventoSeleccionado = eventoSeleccionado;
    }
    
    public void setEventosPorPeriodo(List<EventosRegistros> eventosPorPeriodo) {
        this.eventosPorPeriodo = eventosPorPeriodo;
        if(eventosPorPeriodo != null && !eventosPorPeriodo.isEmpty()){//si la lista de eventos existe
            setEventoSeleccionado(eventosPorPeriodo.get(0));//se selecciona el primer evento de la lista
            if(getEventosPorPeriodo().contains(getEventoRegistroActivo())){//si la lista de eventos contiene al evento actual se declara a este como seleccionado
                setEventoSeleccionado(getEventoRegistroActivo());
            }
        }
    }
    
    public void setParticipantesTutoriaGrupal(ParticipantesTutoriaGrupal participantesTutoriaGrupal) {
        this.participantesTutoriaGrupal = participantesTutoriaGrupal;
    }

    public void setListaParticipantesTutoriaGrupal(List<ParticipantesTutoriaGrupal> listaParticipantesTutoriaGrupal) {
        this.listaParticipantesTutoriaGrupal = listaParticipantesTutoriaGrupal;
    }
    
}
