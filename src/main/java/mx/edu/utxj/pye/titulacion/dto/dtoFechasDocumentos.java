/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.titulacion.FechasDocumentos;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "dtoFechasDocumentos")
@EqualsAndHashCode
public class dtoFechasDocumentos implements Serializable{

    private static final long serialVersionUID = 2721760825600654002L;
    @Getter @Setter @NonNull private FechasDocumentos fechasDocumentos;
    @Getter @Setter private Generaciones generaciones;
    @Getter @Setter private AreasUniversidad areasUniversidad;
}
