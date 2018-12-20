/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
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
@Embeddable @NoArgsConstructor @RequiredArgsConstructor @EqualsAndHashCode(of = {"evaluacion","evaluador"}) @ToString(of = {"evaluacion","evaluador"})
public class EvaluacionEvaluadorPK implements Serializable {    
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluacion")
    @Getter @Setter @NonNull private Integer evaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluador")
    @Getter @Setter @NonNull private Integer evaluador;
}
