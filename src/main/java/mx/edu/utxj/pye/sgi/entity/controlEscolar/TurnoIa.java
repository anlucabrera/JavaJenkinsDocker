/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "turno_ia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TurnoIa.findAll", query = "SELECT t FROM TurnoIa t")
    , @NamedQuery(name = "TurnoIa.findByIdTurnoIa", query = "SELECT t FROM TurnoIa t WHERE t.idTurnoIa = :idTurnoIa")
    , @NamedQuery(name = "TurnoIa.findByNombre", query = "SELECT t FROM TurnoIa t WHERE t.nombre = :nombre")})
public class TurnoIa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_turno_ia")
    private Short idTurnoIa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTurnoIa")
    private List<InstitucionAcademica> institucionAcademicaList;

    public TurnoIa() {
    }

    public TurnoIa(Short idTurnoIa) {
        this.idTurnoIa = idTurnoIa;
    }

    public TurnoIa(Short idTurnoIa, String nombre) {
        this.idTurnoIa = idTurnoIa;
        this.nombre = nombre;
    }

    public Short getIdTurnoIa() {
        return idTurnoIa;
    }

    public void setIdTurnoIa(Short idTurnoIa) {
        this.idTurnoIa = idTurnoIa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<InstitucionAcademica> getInstitucionAcademicaList() {
        return institucionAcademicaList;
    }

    public void setInstitucionAcademicaList(List<InstitucionAcademica> institucionAcademicaList) {
        this.institucionAcademicaList = institucionAcademicaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTurnoIa != null ? idTurnoIa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TurnoIa)) {
            return false;
        }
        TurnoIa other = (TurnoIa) object;
        if ((this.idTurnoIa == null && other.idTurnoIa != null) || (this.idTurnoIa != null && !this.idTurnoIa.equals(other.idTurnoIa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TurnoIa[ idTurnoIa=" + idTurnoIa + " ]";
    }
    
}
