/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.model.SelectItem;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestVocacional;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

/**
 *
 * @author UTXJ
 */
public class TestVocacionalRolEstudiante implements Serializable{
    private static final long serialVersionUID = -8777736471700926131L;
    @Getter             @NonNull                private             DtoEstudiante                                       dtoEstudiante;
    @Getter             @NonNull                private             List<Apartado>                                      apartados;
    @Getter             @NonNull                private             Map<String, String>                                 respuestas;
    @Getter             @NonNull                private             List<SelectItem>                                    respuestasPosibles;
    @Getter             @NonNull                private             TestVocacional                                      resultado;
    @Getter             @Setter                 private             String                                              carreraInteres;
    @Getter             @Setter                 private             Boolean                                             carreraInteresSeleccion,mostrarIndexProduccion;
    @Getter             @Setter                 private             Map<String, Double>                                 resultadosCarreras;
    
    @Getter             @Setter                 protected           NivelRol                                            nivelRol = NivelRol.OPERATIVO;

    public TestVocacionalRolEstudiante() {
    }
    
    public TestVocacionalRolEstudiante(@NonNull DtoEstudiante dtoEstudiante){
        this.dtoEstudiante = dtoEstudiante;
    }
    
    public Boolean tieneAcceso(DtoEstudiante dtoEstudiante, UsuarioTipo usuarioTipo){
        if(dtoEstudiante == null || !usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        else return true;
    }

    public void setDtoEstudiante(DtoEstudiante dtoEstudiante) {
        this.dtoEstudiante = dtoEstudiante;
    }

    public void setApartados(List<Apartado> apartados) {
        this.apartados = apartados;
    }

    public void setRespuestasPosibles(List<SelectItem> respuestasPosibles) {
        this.respuestasPosibles = respuestasPosibles;
    }
    
    public void setResultado(TestVocacional resultado) {
        this.resultado = resultado;
    }

    public void setRespuestas(Map<String, String> respuestas) {
        this.respuestas = respuestas;
    }
    
}
