/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesoriasEstudiantes;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;

/**
 *
 * @author UTXJ
 */
public class RegistroAsesoriaEstudianteRolGeneral extends AbstractRol{

    private static final long serialVersionUID = 7923223261016288514L;
    @Getter     private     PersonalActivo                      personal;
    
    @Getter     private     Integer                             periodoActivo;
    @Getter     private     PeriodosEscolares                   periodoSeleccionado;
    @Getter     private     List<PeriodosEscolares>             periodosConGrupo;
    
    @Getter     private     EventosRegistros                    eventoRegistroActivo, eventoSeleccionado;
    @Getter     private     List<EventosRegistros>              eventosPorPeriodo;
    
    @Getter     private     DtoAsesoriaEstudianteCe             dtoAsesoriaEstudianteCe;
    @Getter     private     AsesoriasEstudiantes                asesoriaEstudiante;
    @Getter     private     List<AsesoriasEstudiantes>          listaAsesoriasEstudiantes;
    
    @Getter     private     List<DtoParticipantesAsesoriaCE>    listaEstudiantes;
    @Getter     private     List<DtoParticipantesAsesoriaCE>    listaFiltroEstudiantes;
    
    @Getter     @Setter     private                             Date                              fecha;
    @Getter     @Setter     private                             Date                              hora;
    @Getter     @Setter     private                             String                            formatoFechaAsesoria;
    
    @Getter     private     AreasUniversidad                    programaEducativoSeleccionado;
    @Getter     private     List<AreasUniversidad>              listaProgramasEducativosPorPeriodo;
    
    @Getter     private     Grupo                               grupoSeleccionado;
    @Getter     private     List<Grupo>                         listaGruposPorPeriodoProgramaEducativo;

    public RegistroAsesoriaEstudianteRolGeneral(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        this.personal = filtro.getEntity();
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setPeriodoSeleccionado(PeriodosEscolares periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
        if(this.periodoSeleccionado == null){
            setEventosPorPeriodo(Collections.EMPTY_LIST);
            nulificarPeriodoSeleccionado();
        }
    }

    public void setPeriodosConGrupo(List<PeriodosEscolares> periodosConGrupo) {
        this.periodosConGrupo = periodosConGrupo;
        if(!periodosConGrupo.isEmpty()){
            this.periodoSeleccionado = periodosConGrupo.get(0);
        }else{
            nulificarPeriodoSeleccionado();
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
        if(eventosPorPeriodo != null && !eventosPorPeriodo.isEmpty()){
            setEventoSeleccionado(eventosPorPeriodo.get(0));
            if(getEventosPorPeriodo().contains(getEventoRegistroActivo())){
                setEventoSeleccionado(getEventoRegistroActivo());
            }
        }
    }
    
    public void nulificarPeriodoSeleccionado(){
        this.setListaProgramasEducativosPorPeriodo(Collections.EMPTY_LIST);
        nulificarProgramasEducativosPorPeriodo();
    }
    
    public void nulificarProgramasEducativosPorPeriodo(){
        this.setListaGruposPorPeriodoProgramaEducativo(Collections.EMPTY_LIST);
        nulificarGruposPorPeriodoProgramaEducativo();
    }
    
    public void nulificarGruposPorPeriodoProgramaEducativo(){
        this.setListaEstudiantes(Collections.EMPTY_LIST);
    }
    
    public void setProgramaEducativoSeleccionado(AreasUniversidad programaEducativoSeleccionado) {
        this.programaEducativoSeleccionado = programaEducativoSeleccionado;
        if(this.programaEducativoSeleccionado == null){
            nulificarProgramasEducativosPorPeriodo();
        }
    }
    
    public void setListaProgramasEducativosPorPeriodo(List<AreasUniversidad> listaProgramasEducativosPorPeriodo) {
        this.listaProgramasEducativosPorPeriodo = listaProgramasEducativosPorPeriodo;
        if(this.listaProgramasEducativosPorPeriodo.isEmpty()){
            nulificarProgramasEducativosPorPeriodo();
        }else{
            this.programaEducativoSeleccionado = listaProgramasEducativosPorPeriodo.get(0);
        }
    }

    public void setGrupoSeleccionado(Grupo grupoSeleccionado) {
        this.grupoSeleccionado = grupoSeleccionado;
        if(this.grupoSeleccionado == null){
            nulificarGruposPorPeriodoProgramaEducativo();
        }
    }

    public void setListaGruposPorPeriodoProgramaEducativo(List<Grupo> listaGruposPorPeriodoProgramaEducativo) {
        this.listaGruposPorPeriodoProgramaEducativo = listaGruposPorPeriodoProgramaEducativo;
        if(!this.listaGruposPorPeriodoProgramaEducativo.isEmpty()){
            this.grupoSeleccionado = listaGruposPorPeriodoProgramaEducativo.get(0);
        }else{
            nulificarGruposPorPeriodoProgramaEducativo();
        }
    }
    
    public void setDtoAsesoriaEstudianteCe(DtoAsesoriaEstudianteCe dtoAsesoriaEstudianteCe) {
        this.dtoAsesoriaEstudianteCe = dtoAsesoriaEstudianteCe;
    }

    public void setAsesoriaEstudiante(AsesoriasEstudiantes asesoriaEstudiante) {
        this.asesoriaEstudiante = asesoriaEstudiante;
        if(this.asesoriaEstudiante != null){
            this.setFormatoFechaAsesoria(Caster.convertirFormatoFecha(this.asesoriaEstudiante.getFechaHora()));
        }else{
            this.setFormatoFechaAsesoria("");
        }
    }

    public void setListaAsesoriasEstudiantes(List<AsesoriasEstudiantes> listaAsesoriasEstudiantes) {
        this.listaAsesoriasEstudiantes = listaAsesoriasEstudiantes;
    }
    
    public void setListaFiltroEstudiantes(List<DtoParticipantesAsesoriaCE> listaFiltroEstudiantes) {
        this.listaFiltroEstudiantes = listaFiltroEstudiantes;
    }

    public void setListaEstudiantes(List<DtoParticipantesAsesoriaCE> listaEstudiantes) {
        this.listaEstudiantes = listaEstudiantes;
    }
    
}
