/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class VistaEvaluacionesTutoresPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "periodo")
    private Integer periodo;
    @Size(max = 15)
    @Column(name = "matricula")
    private String matricula;
    @Size(max = 20)
    @Column(name = "numeroNomina")
    private String numeroNomina;

    public VistaEvaluacionesTutoresPK() {
    }

    public VistaEvaluacionesTutoresPK(Integer periodo, String matricula, String numeroNomina) {
        this.periodo = periodo;
        this.matricula = matricula;
        this.numeroNomina = numeroNomina;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNumeroNomina() {
        return numeroNomina;
    }

    public void setNumeroNomina(String numeroNomina) {
        this.numeroNomina = numeroNomina;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.periodo);
        hash = 97 * hash + Objects.hashCode(this.matricula);
        hash = 97 * hash + Objects.hashCode(this.numeroNomina);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VistaEvaluacionesTutoresPK other = (VistaEvaluacionesTutoresPK) obj;
        if (!Objects.equals(this.matricula, other.matricula)) {
            return false;
        }
        if (!Objects.equals(this.numeroNomina, other.numeroNomina)) {
            return false;
        }
        if (!Objects.equals(this.periodo, other.periodo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "VistaEvaluacionesTutoresPK{" + "periodo=" + periodo + ", matricula=" + matricula + ", numeroNomina=" + numeroNomina + '}';
    }
    
}
