/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "programas_educativos_organismos_evaluadores", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramasEducativosOrganismosEvaluadores.findAll", query = "SELECT p FROM ProgramasEducativosOrganismosEvaluadores p")
    , @NamedQuery(name = "ProgramasEducativosOrganismosEvaluadores.findByRelacionpeoe", query = "SELECT p FROM ProgramasEducativosOrganismosEvaluadores p WHERE p.relacionpeoe = :relacionpeoe")})
public class ProgramasEducativosOrganismosEvaluadores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "relacionpeoe")
    private Integer relacionpeoe;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "relacionpeoe")
    private List<ProgramasEducativosAcreditaciones> programasEducativosAcreditacionesList;
    @JoinColumn(name = "organismo", referencedColumnName = "siglas")
    @ManyToOne(optional = false)
    private OrganismosEvaluadores organismo;
    @JoinColumn(name = "programa", referencedColumnName = "siglas")
    @ManyToOne(optional = false)
    private ProgramasEducativos programa;

    public ProgramasEducativosOrganismosEvaluadores() {
    }

    public ProgramasEducativosOrganismosEvaluadores(Integer relacionpeoe) {
        this.relacionpeoe = relacionpeoe;
    }

    public Integer getRelacionpeoe() {
        return relacionpeoe;
    }

    public void setRelacionpeoe(Integer relacionpeoe) {
        this.relacionpeoe = relacionpeoe;
    }

    @XmlTransient
    public List<ProgramasEducativosAcreditaciones> getProgramasEducativosAcreditacionesList() {
        return programasEducativosAcreditacionesList;
    }

    public void setProgramasEducativosAcreditacionesList(List<ProgramasEducativosAcreditaciones> programasEducativosAcreditacionesList) {
        this.programasEducativosAcreditacionesList = programasEducativosAcreditacionesList;
    }

    public OrganismosEvaluadores getOrganismo() {
        return organismo;
    }

    public void setOrganismo(OrganismosEvaluadores organismo) {
        this.organismo = organismo;
    }

    public ProgramasEducativos getPrograma() {
        return programa;
    }

    public void setPrograma(ProgramasEducativos programa) {
        this.programa = programa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (relacionpeoe != null ? relacionpeoe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramasEducativosOrganismosEvaluadores)) {
            return false;
        }
        ProgramasEducativosOrganismosEvaluadores other = (ProgramasEducativosOrganismosEvaluadores) object;
        if ((this.relacionpeoe == null && other.relacionpeoe != null) || (this.relacionpeoe != null && !this.relacionpeoe.equals(other.relacionpeoe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosOrganismosEvaluadores[ relacionpeoe=" + relacionpeoe + " ]";
    }
    
}
