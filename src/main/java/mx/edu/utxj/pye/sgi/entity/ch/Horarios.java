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
@Table(name = "horarios", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Horarios.findAll", query = "SELECT h FROM Horarios h")
    , @NamedQuery(name = "Horarios.findByCargaHoraria", query = "SELECT h FROM Horarios h WHERE h.cargaHoraria = :cargaHoraria")
    , @NamedQuery(name = "Horarios.findByPeriodo", query = "SELECT h FROM Horarios h WHERE h.periodo = :periodo")
    , @NamedQuery(name = "Horarios.findByEvidenciaHorario", query = "SELECT h FROM Horarios h WHERE h.evidenciaHorario = :evidenciaHorario")})
public class Horarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "carga_horaria")
    private Integer cargaHoraria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 21)
    @Column(name = "periodo")
    private String periodo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "evidencia_horario")
    private String evidenciaHorario;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;

    public Horarios() {
    }

    public Horarios(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Horarios(Integer cargaHoraria, String periodo, String evidenciaHorario) {
        this.cargaHoraria = cargaHoraria;
        this.periodo = periodo;
        this.evidenciaHorario = evidenciaHorario;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getEvidenciaHorario() {
        return evidenciaHorario;
    }

    public void setEvidenciaHorario(String evidenciaHorario) {
        this.evidenciaHorario = evidenciaHorario;
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
        hash += (cargaHoraria != null ? cargaHoraria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Horarios)) {
            return false;
        }
        Horarios other = (Horarios) object;
        if ((this.cargaHoraria == null && other.cargaHoraria != null) || (this.cargaHoraria != null && !this.cargaHoraria.equals(other.cargaHoraria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Horarios[ cargaHoraria=" + cargaHoraria + " ]";
    }
    
}
