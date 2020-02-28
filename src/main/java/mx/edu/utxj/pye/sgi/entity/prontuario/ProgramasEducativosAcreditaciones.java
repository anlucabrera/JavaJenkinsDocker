/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "programas_educativos_acreditaciones", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramasEducativosAcreditaciones.findAll", query = "SELECT p FROM ProgramasEducativosAcreditaciones p")
    , @NamedQuery(name = "ProgramasEducativosAcreditaciones.findByAcreditacion", query = "SELECT p FROM ProgramasEducativosAcreditaciones p WHERE p.acreditacion = :acreditacion")
    , @NamedQuery(name = "ProgramasEducativosAcreditaciones.findByInicio", query = "SELECT p FROM ProgramasEducativosAcreditaciones p WHERE p.inicio = :inicio")
    , @NamedQuery(name = "ProgramasEducativosAcreditaciones.findByFin", query = "SELECT p FROM ProgramasEducativosAcreditaciones p WHERE p.fin = :fin")})
public class ProgramasEducativosAcreditaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "acreditacion")
    private Integer acreditacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inicio")
    @Temporal(TemporalType.DATE)
    private Date inicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fin")
    @Temporal(TemporalType.DATE)
    private Date fin;
    @JoinColumn(name = "relacionpeoe", referencedColumnName = "relacionpeoe")
    @ManyToOne(optional = false)
    private ProgramasEducativosOrganismosEvaluadores relacionpeoe;

    public ProgramasEducativosAcreditaciones() {
    }

    public ProgramasEducativosAcreditaciones(Integer acreditacion) {
        this.acreditacion = acreditacion;
    }

    public ProgramasEducativosAcreditaciones(Integer acreditacion, Date inicio, Date fin) {
        this.acreditacion = acreditacion;
        this.inicio = inicio;
        this.fin = fin;
    }

    public Integer getAcreditacion() {
        return acreditacion;
    }

    public void setAcreditacion(Integer acreditacion) {
        this.acreditacion = acreditacion;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public ProgramasEducativosOrganismosEvaluadores getRelacionpeoe() {
        return relacionpeoe;
    }

    public void setRelacionpeoe(ProgramasEducativosOrganismosEvaluadores relacionpeoe) {
        this.relacionpeoe = relacionpeoe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (acreditacion != null ? acreditacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramasEducativosAcreditaciones)) {
            return false;
        }
        ProgramasEducativosAcreditaciones other = (ProgramasEducativosAcreditaciones) object;
        if ((this.acreditacion == null && other.acreditacion != null) || (this.acreditacion != null && !this.acreditacion.equals(other.acreditacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosAcreditaciones[ acreditacion=" + acreditacion + " ]";
    }
    
}
