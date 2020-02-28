/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "habilidades_informaticas", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HabilidadesInformaticas.findAll", query = "SELECT h FROM HabilidadesInformaticas h")
    , @NamedQuery(name = "HabilidadesInformaticas.findByConocimiento", query = "SELECT h FROM HabilidadesInformaticas h WHERE h.conocimiento = :conocimiento")
    , @NamedQuery(name = "HabilidadesInformaticas.findByTipoConocimiento", query = "SELECT h FROM HabilidadesInformaticas h WHERE h.tipoConocimiento = :tipoConocimiento")
    , @NamedQuery(name = "HabilidadesInformaticas.findByProgramaDominante", query = "SELECT h FROM HabilidadesInformaticas h WHERE h.programaDominante = :programaDominante")
    , @NamedQuery(name = "HabilidadesInformaticas.findByNivelConocimiento", query = "SELECT h FROM HabilidadesInformaticas h WHERE h.nivelConocimiento = :nivelConocimiento")
    , @NamedQuery(name = "HabilidadesInformaticas.findByEstatus", query = "SELECT h FROM HabilidadesInformaticas h WHERE h.estatus = :estatus")})
public class HabilidadesInformaticas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "conocimiento")
    private Integer conocimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "tipo_conocimiento")
    private String tipoConocimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 21)
    @Column(name = "programa_dominante")
    private String programaDominante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 22)
    @Column(name = "nivel_conocimiento")
    private String nivelConocimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;

    public HabilidadesInformaticas() {
    }

    public HabilidadesInformaticas(Integer conocimiento) {
        this.conocimiento = conocimiento;
    }

    public HabilidadesInformaticas(Integer conocimiento, String tipoConocimiento, String programaDominante, String nivelConocimiento, String estatus) {
        this.conocimiento = conocimiento;
        this.tipoConocimiento = tipoConocimiento;
        this.programaDominante = programaDominante;
        this.nivelConocimiento = nivelConocimiento;
        this.estatus = estatus;
    }

    public Integer getConocimiento() {
        return conocimiento;
    }

    public void setConocimiento(Integer conocimiento) {
        this.conocimiento = conocimiento;
    }

    public String getTipoConocimiento() {
        return tipoConocimiento;
    }

    public void setTipoConocimiento(String tipoConocimiento) {
        this.tipoConocimiento = tipoConocimiento;
    }

    public String getProgramaDominante() {
        return programaDominante;
    }

    public void setProgramaDominante(String programaDominante) {
        this.programaDominante = programaDominante;
    }

    public String getNivelConocimiento() {
        return nivelConocimiento;
    }

    public void setNivelConocimiento(String nivelConocimiento) {
        this.nivelConocimiento = nivelConocimiento;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Personal getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(Personal clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (conocimiento != null ? conocimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HabilidadesInformaticas)) {
            return false;
        }
        HabilidadesInformaticas other = (HabilidadesInformaticas) object;
        if ((this.conocimiento == null && other.conocimiento != null) || (this.conocimiento != null && !this.conocimiento.equals(other.conocimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas[ conocimiento=" + conocimiento + " ]";
    }
    
}
