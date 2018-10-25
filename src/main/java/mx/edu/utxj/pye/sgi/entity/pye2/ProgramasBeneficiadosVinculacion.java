/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "programas_beneficiados_vinculacion", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramasBeneficiadosVinculacion.findAll", query = "SELECT p FROM ProgramasBeneficiadosVinculacion p")
    , @NamedQuery(name = "ProgramasBeneficiadosVinculacion.findByEmpresa", query = "SELECT p FROM ProgramasBeneficiadosVinculacion p WHERE p.programasBeneficiadosVinculacionPK.empresa = :empresa")
    , @NamedQuery(name = "ProgramasBeneficiadosVinculacion.findByProgramaEducativo", query = "SELECT p FROM ProgramasBeneficiadosVinculacion p WHERE p.programasBeneficiadosVinculacionPK.programaEducativo = :programaEducativo")})
public class ProgramasBeneficiadosVinculacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProgramasBeneficiadosVinculacionPK programasBeneficiadosVinculacionPK;
    @JoinColumn(name = "empresa", referencedColumnName = "empresa", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Convenios convenios;

    public ProgramasBeneficiadosVinculacion() {
    }

    public ProgramasBeneficiadosVinculacion(ProgramasBeneficiadosVinculacionPK programasBeneficiadosVinculacionPK) {
        this.programasBeneficiadosVinculacionPK = programasBeneficiadosVinculacionPK;
    }

    public ProgramasBeneficiadosVinculacion(int empresa, short programaEducativo) {
        this.programasBeneficiadosVinculacionPK = new ProgramasBeneficiadosVinculacionPK(empresa, programaEducativo);
    }

    public ProgramasBeneficiadosVinculacionPK getProgramasBeneficiadosVinculacionPK() {
        return programasBeneficiadosVinculacionPK;
    }

    public void setProgramasBeneficiadosVinculacionPK(ProgramasBeneficiadosVinculacionPK programasBeneficiadosVinculacionPK) {
        this.programasBeneficiadosVinculacionPK = programasBeneficiadosVinculacionPK;
    }

    public Convenios getConvenios() {
        return convenios;
    }

    public void setConvenios(Convenios convenios) {
        this.convenios = convenios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (programasBeneficiadosVinculacionPK != null ? programasBeneficiadosVinculacionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramasBeneficiadosVinculacion)) {
            return false;
        }
        ProgramasBeneficiadosVinculacion other = (ProgramasBeneficiadosVinculacion) object;
        if ((this.programasBeneficiadosVinculacionPK == null && other.programasBeneficiadosVinculacionPK != null) || (this.programasBeneficiadosVinculacionPK != null && !this.programasBeneficiadosVinculacionPK.equals(other.programasBeneficiadosVinculacionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ProgramasBeneficiadosVinculacion[ programasBeneficiadosVinculacionPK=" + programasBeneficiadosVinculacionPK + " ]";
    }
    
}
