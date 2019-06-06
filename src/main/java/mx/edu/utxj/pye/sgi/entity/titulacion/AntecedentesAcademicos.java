/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "antecedentes_academicos", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AntecedentesAcademicos.findAll", query = "SELECT a FROM AntecedentesAcademicos a")
    , @NamedQuery(name = "AntecedentesAcademicos.findByAntAcad", query = "SELECT a FROM AntecedentesAcademicos a WHERE a.antAcad = :antAcad")
    , @NamedQuery(name = "AntecedentesAcademicos.findByGradoAcademico", query = "SELECT a FROM AntecedentesAcademicos a WHERE a.gradoAcademico = :gradoAcademico")
    , @NamedQuery(name = "AntecedentesAcademicos.findByIems", query = "SELECT a FROM AntecedentesAcademicos a WHERE a.iems = :iems")
    , @NamedQuery(name = "AntecedentesAcademicos.findByFechaTerminacion", query = "SELECT a FROM AntecedentesAcademicos a WHERE a.fechaTerminacion = :fechaTerminacion")})
public class AntecedentesAcademicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ant_acad")
    private Integer antAcad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "grado_academico")
    private String gradoAcademico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iems")
    private int iems;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_terminacion")
    @Temporal(TemporalType.DATE)
    private Date fechaTerminacion;
    @JoinColumn(name = "matricula", referencedColumnName = "matricula")
    @ManyToOne(optional = false)
    private Egresados matricula;

    public AntecedentesAcademicos() {
    }

    public AntecedentesAcademicos(Integer antAcad) {
        this.antAcad = antAcad;
    }

    public AntecedentesAcademicos(Integer antAcad, String gradoAcademico, int iems, Date fechaTerminacion) {
        this.antAcad = antAcad;
        this.gradoAcademico = gradoAcademico;
        this.iems = iems;
        this.fechaTerminacion = fechaTerminacion;
    }

    public Integer getAntAcad() {
        return antAcad;
    }

    public void setAntAcad(Integer antAcad) {
        this.antAcad = antAcad;
    }

    public String getGradoAcademico() {
        return gradoAcademico;
    }

    public void setGradoAcademico(String gradoAcademico) {
        this.gradoAcademico = gradoAcademico;
    }

    public int getIems() {
        return iems;
    }

    public void setIems(int iems) {
        this.iems = iems;
    }

    public Date getFechaTerminacion() {
        return fechaTerminacion;
    }

    public void setFechaTerminacion(Date fechaTerminacion) {
        this.fechaTerminacion = fechaTerminacion;
    }

    public Egresados getMatricula() {
        return matricula;
    }

    public void setMatricula(Egresados matricula) {
        this.matricula = matricula;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (antAcad != null ? antAcad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AntecedentesAcademicos)) {
            return false;
        }
        AntecedentesAcademicos other = (AntecedentesAcademicos) object;
        if ((this.antAcad == null && other.antAcad != null) || (this.antAcad != null && !this.antAcad.equals(other.antAcad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.AntecedentesAcademicos[ antAcad=" + antAcad + " ]";
    }
    
}
