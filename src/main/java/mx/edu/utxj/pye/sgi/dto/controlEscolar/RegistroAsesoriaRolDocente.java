/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asesoria;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

/**
 *
 * @author UTXJ
 */
public class RegistroAsesoriaRolDocente extends AbstractRol{

    private static final long serialVersionUID = -3832131070067730829L;
    @Getter     private     PersonalActivo                      docenteLogueado;
    
    @Getter     private     EventosRegistros                    eventoRegistroActivo, eventoSeleccionado;
    @Getter     private     List<EventosRegistros>              eventosPorPeriodo;
    
    @Getter     private     Integer                             periodoActivo;
    @Getter     private     PeriodosEscolares                   periodoSeleccionado;
    @Getter     private     List<PeriodosEscolares>             periodosConCarga;
    
    @Getter     private     DtoCargaAcademica                   cargaAcademicaSeleccionada;
    @Getter     private     List<DtoCargaAcademica>             cargasDocente;
    
    @Getter     private     DtoUnidadConfiguracion              unidadConfiguracionSeleccionada;
    @Getter     private     List<DtoUnidadConfiguracion>        unidadConfiguraciones;
    
    @Getter     private     DtoAsesoriaCE                       dtoAsesoriaCE;
    @Getter     private     Asesoria                            asesoriaSeleccionada;
    @Getter     private     List<Asesoria>                      listaAsesorias;
    
    @Getter     private     List<DtoParticipantesAsesoriaCE>    listaEstudiantes;
    @Getter     private     List<DtoParticipantesAsesoriaCE>    listaFiltroEstudiantes;
    
    @Getter     @Setter     private                             Date                              fecha;
    @Getter     @Setter     private                             Date                              hora;
    
    public RegistroAsesoriaRolDocente(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        this.docenteLogueado = filtro.getEntity();
    }
    
    public void setDocenteLogueado(PersonalActivo docenteLogueado) {
        this.docenteLogueado = docenteLogueado;
    }

    public void setPeriodoSeleccionado(PeriodosEscolares periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
        this.setCargasDocente(Collections.EMPTY_LIST);
        if(this.periodoSeleccionado == null){
            setEventosPorPeriodo(Collections.EMPTY_LIST);
        }
    }

    public void setPeriodosConCarga(List<PeriodosEscolares> periodosConCarga) {
        this.periodosConCarga = periodosConCarga;
        if(!periodosConCarga.isEmpty()){
            periodoSeleccionado = periodosConCarga.get(0);
        }
    }

    public void setCargaAcademicaSeleccionada(DtoCargaAcademica cargaAcademicaSeleccionada) {
//        System.out.println("cargaAcademicaSeleccionada = [" + cargaAcademicaSeleccionada + "]");
        this.cargaAcademicaSeleccionada = cargaAcademicaSeleccionada;
        this.setUnidadConfiguraciones(Collections.emptyList());
//        this.setDtoUnidadConfiguracionSeleccionada(null);
    }

    public void setCargasDocente(List<DtoCargaAcademica> cargasDocente) {
        this.cargasDocente = cargasDocente;
        if(!cargasDocente.isEmpty()) cargaAcademicaSeleccionada = cargasDocente.get(0);
        else cargaAcademicaSeleccionada = null;

    }

    public void setUnidadConfiguracionSeleccionada(DtoUnidadConfiguracion unidadConfiguracionSeleccionada) {
        this.unidadConfiguracionSeleccionada = unidadConfiguracionSeleccionada;
    }

    public void setUnidadConfiguraciones(List<DtoUnidadConfiguracion> unidadConfiguraciones) {
        this.unidadConfiguraciones = unidadConfiguraciones;
        if(!unidadConfiguraciones.isEmpty()) unidadConfiguracionSeleccionada = unidadConfiguraciones.get(0);
        else unidadConfiguracionSeleccionada = null;
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

    public void setDtoAsesoriaCE(DtoAsesoriaCE dtoAsesoriaCE) {
        this.dtoAsesoriaCE = dtoAsesoriaCE;
    }

    public void setListaAsesorias(List<Asesoria> listaAsesorias) {
        this.listaAsesorias = listaAsesorias;
    }

    public void setListaEstudiantes(List<DtoParticipantesAsesoriaCE> listaEstudiantes) {
        this.listaEstudiantes = listaEstudiantes;
    }

    public void setListaFiltroEstudiantes(List<DtoParticipantesAsesoriaCE> listaFiltroEstudiantes) {
        this.listaFiltroEstudiantes = listaFiltroEstudiantes;
    }

    public void setAsesoriaSeleccionada(Asesoria asesoriaSeleccionada) {
        this.asesoriaSeleccionada = asesoriaSeleccionada;
    }
        
}
