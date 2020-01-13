/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class ProgramasBeneficiadosVinculacionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "empresa")
    private int empresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;

    public ProgramasBeneficiadosVinculacionPK() {
    }

    public ProgramasBeneficiadosVinculacionPK(int empresa, short programaEducativo) {
        this.empresa = empresa;
        this.programaEducativo = programaEducativo;
    }

    public int getEmpresa() {
        return empresa;
    }

    public void setEmpresa(int empresa) {
        this.empresa = empresa;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) empresa;
        hash += (int) programaEducativo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramasBeneficiadosVinculacionPK)) {
            return false;
        }
        ProgramasBeneficiadosVinculacionPK other = (ProgramasBeneficiadosVinculacionPK) object;
        if (this.empresa != other.empresa) {
            return false;
        }
        if (this.programaEducativo != other.programaEducativo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ProgramasBeneficiadosVinculacionPK[ empresa=" + empresa + ", programaEducativo=" + programaEducativo + " ]";
    }
    
}
