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
@Table(name = "desagregado_tipos", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DesagregadoTipos.findAll", query = "SELECT d FROM DesagregadoTipos d")
    , @NamedQuery(name = "DesagregadoTipos.findByTipo", query = "SELECT d FROM DesagregadoTipos d WHERE d.tipo = :tipo")
    , @NamedQuery(name = "DesagregadoTipos.findByNombre", query = "SELECT d FROM DesagregadoTipos d WHERE d.nombre = :nombre")})
public class DesagregadoTipos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tipo")
    private Short tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private List<Desagregados> desagregadosList;

    public DesagregadoTipos() {
    }

    public DesagregadoTipos(Short tipo) {
        this.tipo = tipo;
    }

    public DesagregadoTipos(Short tipo, String nombre) {
        this.tipo = tipo;
        this.nombre = nombre;
    }

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Desagregados> getDesagregadosList() {
        return desagregadosList;
    }

    public void setDesagregadosList(List<Desagregados> desagregadosList) {
        this.desagregadosList = desagregadosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipo != null ? tipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DesagregadoTipos)) {
            return false;
        }
        DesagregadoTipos other = (DesagregadoTipos) object;
        if ((this.tipo == null && other.tipo != null) || (this.tipo != null && !this.tipo.equals(other.tipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.DesagregadoTipos[ tipo=" + tipo + " ]";
    }
    
}
