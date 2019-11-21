/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class DtoCasosCriticosPendientes implements Serializable{
    private static final long serialVersionUID = -7345640288564193806L;
    @Getter @Setter @NonNull private Integer    idGrupo;
    @Getter @Setter @NonNull private Integer    grado;
    @Getter @Setter @NonNull private Character  literal;
    @Getter @Setter @NonNull private Integer    idPE;
    @Getter @Setter @NonNull private Integer    periodo;
    @Getter @Setter @NonNull private Integer    casosCriticosPorAtender;
    @Getter @Setter @NonNull private String     tipo;
    @Getter @Setter @NonNull private String     estado;
    @Getter @Setter @NonNull private Integer    tutor;
}
