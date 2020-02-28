/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "cuerpacad_integrantes", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuerpacadIntegrantes.findAll", query = "SELECT c FROM CuerpacadIntegrantes c")
    , @NamedQuery(name = "CuerpacadIntegrantes.findByRegistro", query = "SELECT c FROM CuerpacadIntegrantes c WHERE c.registro = :registro")
    , @NamedQuery(name = "CuerpacadIntegrantes.findByPersonal", query = "SELECT c FROM CuerpacadIntegrantes c WHERE c.personal = :personal")
    , @NamedQuery(name = "CuerpacadIntegrantes.findByEstatus", query = "SELECT c FROM CuerpacadIntegrantes c WHERE c.estatus = :estatus")})
public class CuerpacadIntegrantes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "personal")
    private int personal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumn(name = "cuerpo_academico", referencedColumnName = "cuerpo_academico")
    @ManyToOne(optional = false)
    private CuerposAcademicosRegistro cuerpoAcademico;

    public CuerpacadIntegrantes() {
    }

    public CuerpacadIntegrantes(Integer registro) {
        this.registro = registro;
    }

    public CuerpacadIntegrantes(Integer registro, int personal, boolean estatus) {
        this.registro = registro;
        this.personal = personal;
        this.estatus = estatus;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getPersonal() {
        return personal;
    }

    public void setPersonal(int personal) {
        this.personal = personal;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public CuerposAcademicosRegistro getCuerpoAcademico() {
        return cuerpoAcademico;
    }

    public void setCuerpoAcademico(CuerposAcademicosRegistro cuerpoAcademico) {
        this.cuerpoAcademico = cuerpoAcademico;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registro != null ? registro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuerpacadIntegrantes)) {
            return false;
        }
        CuerpacadIntegrantes other = (CuerpacadIntegrantes) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadIntegrantes[ registro=" + registro + " ]";
    }
    
}
