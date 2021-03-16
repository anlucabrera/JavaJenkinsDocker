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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "ocupacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ocupacion.findAll", query = "SELECT o FROM Ocupacion o")
    , @NamedQuery(name = "Ocupacion.findByIdOcupacion", query = "SELECT o FROM Ocupacion o WHERE o.idOcupacion = :idOcupacion")
    , @NamedQuery(name = "Ocupacion.findByDescripcion", query = "SELECT o FROM Ocupacion o WHERE o.descripcion = :descripcion")})
public class Ocupacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_ocupacion")
    private Short idOcupacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ocupacionMadre", fetch = FetchType.LAZY)
    private List<DatosFamiliares> datosFamiliaresList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ocupacionPadre", fetch = FetchType.LAZY)
    private List<DatosFamiliares> datosFamiliaresList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ocupacion", fetch = FetchType.LAZY)
    private List<IntegrantesFamilia> integrantesFamiliaList;
    @OneToMany(mappedBy = "ocupacion", fetch = FetchType.LAZY)
    private List<TutorFamiliar> tutorFamiliarList;

    public Ocupacion() {
    }

    public Ocupacion(Short idOcupacion) {
        this.idOcupacion = idOcupacion;
    }

    public Ocupacion(Short idOcupacion, String descripcion) {
        this.idOcupacion = idOcupacion;
        this.descripcion = descripcion;
    }

    public Short getIdOcupacion() {
        return idOcupacion;
    }

    public void setIdOcupacion(Short idOcupacion) {
        this.idOcupacion = idOcupacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<DatosFamiliares> getDatosFamiliaresList() {
        return datosFamiliaresList;
    }

    public void setDatosFamiliaresList(List<DatosFamiliares> datosFamiliaresList) {
        this.datosFamiliaresList = datosFamiliaresList;
    }

    @XmlTransient
    public List<DatosFamiliares> getDatosFamiliaresList1() {
        return datosFamiliaresList1;
    }

    public void setDatosFamiliaresList1(List<DatosFamiliares> datosFamiliaresList1) {
        this.datosFamiliaresList1 = datosFamiliaresList1;
    }

    @XmlTransient
    public List<IntegrantesFamilia> getIntegrantesFamiliaList() {
        return integrantesFamiliaList;
    }

    public void setIntegrantesFamiliaList(List<IntegrantesFamilia> integrantesFamiliaList) {
        this.integrantesFamiliaList = integrantesFamiliaList;
    }

    @XmlTransient
    public List<TutorFamiliar> getTutorFamiliarList() {
        return tutorFamiliarList;
    }

    public void setTutorFamiliarList(List<TutorFamiliar> tutorFamiliarList) {
        this.tutorFamiliarList = tutorFamiliarList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOcupacion != null ? idOcupacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ocupacion)) {
            return false;
        }
        Ocupacion other = (Ocupacion) object;
        if ((this.idOcupacion == null && other.idOcupacion != null) || (this.idOcupacion != null && !this.idOcupacion.equals(other.idOcupacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Ocupacion[ idOcupacion=" + idOcupacion + " ]";
    }
    
}
