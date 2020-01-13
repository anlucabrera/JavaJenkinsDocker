/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import edu.mx.utxj.pye.seut.util.preguntas.Pregunta;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Jonny Valderrabano Virgen
 */
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString
public class Apartado implements Serializable{
    
    private static final long serialVersionUID = 7572996293087959326L;
    @Getter @Setter @NonNull private Float id;
    @Getter @Setter private String contenido;
    @Getter @Setter private List<Pregunta> preguntas = new ArrayList<Pregunta>();
    
//    public static void main(String[] args) {
//        Apartado a = new Apartado
//    }
}
