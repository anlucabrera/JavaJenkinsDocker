/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "catalogo_no_adeudo_area", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatalogoNoAdeudoArea.findAll", query = "SELECT c FROM CatalogoNoAdeudoArea c")
    , @NamedQuery(name = "CatalogoNoAdeudoArea.findByCatalogoNoAdeudo", query = "SELECT c FROM CatalogoNoAdeudoArea c WHERE c.catalogoNoAdeudo = :catalogoNoAdeudo")
    , @NamedQuery(name = "CatalogoNoAdeudoArea.findByArea", query = "SELECT c FROM CatalogoNoAdeudoArea c WHERE c.area = :area")
    , @NamedQuery(name = "CatalogoNoAdeudoArea.findByAdeudo", query = "SELECT c FROM CatalogoNoAdeudoArea c WHERE c.adeudo = :adeudo")})
public class CatalogoNoAdeudoArea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "catalogo_no_adeudo")
    private Integer catalogoNoAdeudo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 57)
    @Column(name = "area")
    private String area;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "adeudo")
    private String adeudo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "adeudo")
    private List<NoAdeudoEstudiante> noAdeudoEstudianteList;

    public CatalogoNoAdeudoArea() {
    }

    public CatalogoNoAdeudoArea(Integer catalogoNoAdeudo) {
        this.catalogoNoAdeudo = catalogoNoAdeudo;
    }

    public CatalogoNoAdeudoArea(Integer catalogoNoAdeudo, String area, String adeudo) {
        this.catalogoNoAdeudo = catalogoNoAdeudo;
        this.area = area;
        this.adeudo = adeudo;
    }

    public Integer getCatalogoNoAdeudo() {
        return catalogoNoAdeudo;
    }

    public void setCatalogoNoAdeudo(Integer catalogoNoAdeudo) {
        this.catalogoNoAdeudo = catalogoNoAdeudo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAdeudo() {
        return adeudo;
    }

    public void setAdeudo(String adeudo) {
        this.adeudo = adeudo;
    }

    @XmlTransient
    public List<NoAdeudoEstudiante> getNoAdeudoEstudianteList() {
        return noAdeudoEstudianteList;
    }

    public void setNoAdeudoEstudianteList(List<NoAdeudoEstudiante> noAdeudoEstudianteList) {
        this.noAdeudoEstudianteList = noAdeudoEstudianteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (catalogoNoAdeudo != null ? catalogoNoAdeudo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatalogoNoAdeudoArea)) {
            return false;
        }
        CatalogoNoAdeudoArea other = (CatalogoNoAdeudoArea) object;
        if ((this.catalogoNoAdeudo == null && other.catalogoNoAdeudo != null) || (this.catalogoNoAdeudo != null && !this.catalogoNoAdeudo.equals(other.catalogoNoAdeudo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CatalogoNoAdeudoArea[ catalogoNoAdeudo=" + catalogoNoAdeudo + " ]";
    }
    
}
