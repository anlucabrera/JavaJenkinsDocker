/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.SeguroFacultativoValidacion;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

/**
 * @author UTXJ
 */
public class ValidacionSeguroFacultativoRolEstudiantiles extends AbstractRol{
    private static final long serialVersionUID = 2451521660642090917L;
    @Getter                     private                     PersonalActivo                              personalEstudiantiles;
    @Getter                     @Setter                     protected                                   NivelRol                                        nivelRol= NivelRol.OPERATIVO;
    
    @Getter                     private                     PeriodosEscolares                           periodoEscolarActivo;
    
    @Getter                     private                     PeriodosEscolares                           periodoEscolarSeleccionado;
    @Getter                     private                     List<PeriodosEscolares>                     listaPeriodosEscolares;
    
    @Getter                     private                     AreasUniversidad                            programaEducativo;
    @Getter                     private                     List<AreasUniversidad>                      listaProgramasEducativos;
    
    @Getter                     private                     Grupo                                       grupo;
    @Getter                     private                     List<Grupo>                                 listaGrupos;
    
    @Getter                     private                     DtoSeguroFacultativo                        dtoSeguroFacultativoSeleccionado;
    @Getter                     private                     List<DtoSeguroFacultativo>                  listaDtoSeguroFacultativo;
    
    @Getter                     private                     String                                      pistaEstudiante;
    @Getter                     private                     DtoEstudianteComplete                       estudianteSeguimientoSeleccionado;
    @Getter                     private                     Estudiante                                  estudianteSeleccionado;
    
    @Getter                     @Setter                     private                                     String                                          rolConsultaSeguroFacultativo;
    @Getter                     @Setter                     private                                     ResultadoEJB<List<Integer>>                     listaIdEstudiantes;
    @Getter                     @Setter                     private                                     List<Integer>                                   listaIdPeriodosEscolares;
    @Getter                     @Setter                     private                                     List<Short>                                     listaIdProgramasEducativos;
    @Getter                     @Setter                     private                                     List<Integer>                                   listaIdGrupos;
    
    public ValidacionSeguroFacultativoRolEstudiantiles(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        this.personalEstudiantiles = filtro.getEntity();
    }

    public void setPersonalEstudiantiles(PersonalActivo personalEstudiantiles) {
        this.personalEstudiantiles = personalEstudiantiles;
    }

    public void setPeriodoEscolarActivo(PeriodosEscolares periodoEscolarActivo) {
        this.periodoEscolarActivo = periodoEscolarActivo;
    }

    public void setPeriodoEscolarSeleccionado(PeriodosEscolares periodoEscolarSeleccionado) {
        this.periodoEscolarSeleccionado = periodoEscolarSeleccionado;
        if(this.periodoEscolarSeleccionado == null){
            setListaProgramasEducativos(Collections.EMPTY_LIST);
            setListaGrupos(Collections.EMPTY_LIST);
            setListaDtoSeguroFacultativo(Collections.EMPTY_LIST);
        }
    }

    public void setListaPeriodosEscolares(List<PeriodosEscolares> listaPeriodosEscolares) {
        this.listaPeriodosEscolares = listaPeriodosEscolares;
        if(!this.listaPeriodosEscolares.isEmpty()){
//            this.periodoEscolarSeleccionado = listaPeriodosEscolares.get(0);
            setPeriodoEscolarSeleccionado(this.listaPeriodosEscolares.get(0));
        }else{
            setPeriodoEscolarSeleccionado(null);
        }
    }

    public void setProgramaEducativo(AreasUniversidad programaEducativo) {
        this.programaEducativo = programaEducativo;
        if(this.programaEducativo == null){
            setListaGrupos(Collections.EMPTY_LIST);
            setListaDtoSeguroFacultativo(Collections.EMPTY_LIST);
        }
    }

    public void setListaProgramasEducativos(List<AreasUniversidad> listaProgramasEducativos) {
        this.listaProgramasEducativos = listaProgramasEducativos;
        if(!this.listaProgramasEducativos.isEmpty()){
//            this.programaEducativo = listaProgramasEducativos.get(0);
            setProgramaEducativo(this.listaProgramasEducativos.get(0));
        }else{
            setProgramaEducativo(null);
        }
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
        if(this.grupo == null){
            setListaDtoSeguroFacultativo(Collections.EMPTY_LIST);
        }
    }

    public void setListaGrupos(List<Grupo> listaGrupos) {
        this.listaGrupos = listaGrupos;
        if(!this.listaGrupos.isEmpty()){
//            this.grupo = listaGrupos.get(0);
            setGrupo(this.listaGrupos.get(0));
        }else{
            setGrupo(null);
        }
    }
    
    public void setDtoSeguroFacultativoSeleccionado(DtoSeguroFacultativo dtoSeguroFacultativoSeleccionado) {
        this.dtoSeguroFacultativoSeleccionado = dtoSeguroFacultativoSeleccionado;
    }

    public void setListaDtoSeguroFacultativo(List<DtoSeguroFacultativo> listaDtoSeguroFacultativo) {
        this.listaDtoSeguroFacultativo = listaDtoSeguroFacultativo;
        if(!this.listaDtoSeguroFacultativo.isEmpty()){
            this.listaDtoSeguroFacultativo.stream().forEach((sf) -> {
                if(sf.getSeguroFactultativo().getValidacionEnfermeria().equals(SeguroFacultativoValidacion.ALTA.getLabel())){
                    sf.setValidacionSeguro(true);
                }else{
                    sf.setValidacionSeguro(false);
                }
            });
        }
    }

    public void setPistaEstudiante(String pistaEstudiante) {
        this.pistaEstudiante = pistaEstudiante;
    }

    public void setEstudianteSeguimientoSeleccionado(DtoEstudianteComplete estudianteSeguimientoSeleccionado) {
        this.estudianteSeguimientoSeleccionado = estudianteSeguimientoSeleccionado;
    }

    public void setEstudianteSeleccionado(Estudiante estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }
    
    
}
