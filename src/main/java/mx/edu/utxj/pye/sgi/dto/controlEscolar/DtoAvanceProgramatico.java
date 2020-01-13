/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@ToString
@EqualsAndHashCode(of = {"avanceProgra"})
public class DtoAvanceProgramatico implements Serializable{
    
    @Getter    @NonNull    private String docente;
    @Getter    @NonNull    private String materia;
    @Getter    @NonNull    private List<String> unidadd;
    @Getter    @NonNull    private List<Double> avanceU;
    @Getter    @NonNull    private List<String> comentario;
    @Getter    @NonNull    private List<String> clase;
    @Getter    @NonNull    private List<String> notas;

    public DtoAvanceProgramatico(String docente, String materia, List<String> unidadd, List<Double> avanceU, List<String> comentario, List<String> clase, List<String> notas) {
        this.docente = docente;
        this.materia = materia;
        this.unidadd = unidadd;
        this.avanceU = avanceU;
        this.comentario = comentario;
        this.clase = clase;
        this.notas = notas;
    }
}
