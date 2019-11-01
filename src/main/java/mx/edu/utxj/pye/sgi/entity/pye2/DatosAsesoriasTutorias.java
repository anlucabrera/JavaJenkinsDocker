/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "datos_asesorias_tutorias", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosAsesoriasTutorias.findAll", query = "SELECT d FROM DatosAsesoriasTutorias d")
    , @NamedQuery(name = "DatosAsesoriasTutorias.findByDatoAsesoriaTutoria", query = "SELECT d FROM DatosAsesoriasTutorias d WHERE d.datoAsesoriaTutoria = :datoAsesoriaTutoria")
    , @NamedQuery(name = "DatosAsesoriasTutorias.findByDescripcion", query = "SELECT d FROM DatosAsesoriasTutorias d WHERE d.descripcion = :descripcion")
    , @NamedQuery(name = "DatosAsesoriasTutorias.findByTipo", query = "SELECT d FROM DatosAsesoriasTutorias d WHERE d.tipo = :tipo")})
public class DatosAsesoriasTutorias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dato_asesoria_tutoria")
    private Short datoAsesoriaTutoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "tipo")
    private String tipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datoAsesoriaTutoria")
    private List<AsesoriasTutoriasCuatrimestrales> asesoriasTutoriasCuatrimestralesList;

    public DatosAsesoriasTutorias() {
    }

    public DatosAsesoriasTutorias(Short datoAsesoriaTutoria) {
        this.datoAsesoriaTutoria = datoAsesoriaTutoria;
    }

    public DatosAsesoriasTutorias(Short datoAsesoriaTutoria, String descripcion, String tipo) {
        this.datoAsesoriaTutoria = datoAsesoriaTutoria;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public Short getDatoAsesoriaTutoria() {
        return datoAsesoriaTutoria;
    }

    public void setDatoAsesoriaTutoria(Short datoAsesoriaTutoria) {
        this.datoAsesoriaTutoria = datoAsesoriaTutoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @XmlTransient
    public List<AsesoriasTutoriasCuatrimestrales> getAsesoriasTutoriasCuatrimestralesList() {
        return asesoriasTutoriasCuatrimestralesList;
    }

    public void setAsesoriasTutoriasCuatrimestralesList(List<AsesoriasTutoriasCuatrimestrales> asesoriasTutoriasCuatrimestralesList) {
        this.asesoriasTutoriasCuatrimestralesList = asesoriasTutoriasCuatrimestralesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datoAsesoriaTutoria != null ? datoAsesoriaTutoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatosAsesoriasTutorias)) {
            return false;
        }
        DatosAsesoriasTutorias other = (DatosAsesoriasTutorias) object;
        if ((this.datoAsesoriaTutoria == null && other.datoAsesoriaTutoria != null) || (this.datoAsesoriaTutoria != null && !this.datoAsesoriaTutoria.equals(other.datoAsesoriaTutoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.DatosAsesoriasTutorias[ datoAsesoriaTutoria=" + datoAsesoriaTutoria + " ]";
    }
    
}
