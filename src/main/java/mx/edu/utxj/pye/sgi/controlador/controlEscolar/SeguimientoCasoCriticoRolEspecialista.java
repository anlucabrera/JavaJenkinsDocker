/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCasoCritico;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class SeguimientoCasoCriticoRolEspecialista extends AbstractRol{
    private static final long serialVersionUID = 1935054096246658559L;
    @Getter     @NonNull    private                     PersonalActivo                          personalEspecializadoLogueado;
    
    @Getter     @NonNull    private                     Integer                                 periodoActivo;
    @Getter     private     PeriodosEscolares           periodoSeleccionado;
    @Getter     @NonNull    private                     List<PeriodosEscolares>                 periodosConCargaCasosCriticos;
    
    @Getter     @NonNull    private                     AreasUniversidad                        programaEducativo;
    @Getter     @NonNull    private                     List<AreasUniversidad>                  programasEducativos;

    @Getter     private     DtoCasoCritico              dtoCasoCritico;
    @Getter     private     List<DtoCasoCritico>        listaCasosCriticos;
    
    @Getter     private     Part                        archivo;
    
    public SeguimientoCasoCriticoRolEspecialista(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        this.personalEspecializadoLogueado = filtro.getEntity();
    }

    public void setPersonalEspecializadoLogueado(PersonalActivo personalEspecializadoLogueado) {
        this.personalEspecializadoLogueado = personalEspecializadoLogueado;
    }

    public void setPeriodoSeleccionado(PeriodosEscolares periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
        this.setListaCasosCriticos(Collections.EMPTY_LIST);
        if (this.periodoSeleccionado == null) {
            setProgramasEducativos(Collections.EMPTY_LIST);
        }
    }
    
    public void setPeriodosConCargaCasosCriticos(List<PeriodosEscolares> periodosConCargaCasosCriticos) {
        this.periodosConCargaCasosCriticos = periodosConCargaCasosCriticos;
        if(!periodosConCargaCasosCriticos.isEmpty()){
            periodoSeleccionado = periodosConCargaCasosCriticos.get(0);
        }
    }

    public void setProgramasEducativos(List<AreasUniversidad> programasEducativos) {
        this.programasEducativos = programasEducativos;
        if(programasEducativos != null && !programasEducativos.isEmpty()){
            setProgramaEducativo(programasEducativos.get(0));
        }
    }
    
    public void setProgramaEducativo(AreasUniversidad programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public void setListaCasosCriticos(List<DtoCasoCritico> listaCasosCriticos) {
        this.listaCasosCriticos = listaCasosCriticos;
    }
    
    public void setDtoCasoCritico(DtoCasoCritico dtoCasoCritico) {
        this.dtoCasoCritico = dtoCasoCritico;
    }

    public void setArchivo(Part archivo) {
        this.archivo = archivo;
    }
    
    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }
    
    
    
}
