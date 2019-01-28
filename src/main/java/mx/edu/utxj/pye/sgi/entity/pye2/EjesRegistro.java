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
@Table(name = "ejes_registro", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EjesRegistro.findAll", query = "SELECT e FROM EjesRegistro e")
    , @NamedQuery(name = "EjesRegistro.findByEje", query = "SELECT e FROM EjesRegistro e WHERE e.eje = :eje")
    , @NamedQuery(name = "EjesRegistro.findByNombre", query = "SELECT e FROM EjesRegistro e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "EjesRegistro.findByObjetivo", query = "SELECT e FROM EjesRegistro e WHERE e.objetivo = :objetivo")
    , @NamedQuery(name = "EjesRegistro.findByIcono", query = "SELECT e FROM EjesRegistro e WHERE e.icono = :icono")
    , @NamedQuery(name = "EjesRegistro.findByLink", query = "SELECT e FROM EjesRegistro e WHERE e.link = :link")})
public class EjesRegistro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "eje")
    private Integer eje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "objetivo")
    private String objetivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "icono")
    private String icono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "link")
    private String link;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eje")
    private List<Registros> registrosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eje")
    private List<ModulosRegistro> modulosRegistroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eje")
    private List<InformacionCorrespondienteArea> informacionCorrespondienteAreaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eje")
    private List<CuadroMandoIntegral> cuadroMandoIntegralList;

    public EjesRegistro() {
    }

    public EjesRegistro(Integer eje) {
        this.eje = eje;
    }

    public EjesRegistro(Integer eje, String nombre, String objetivo, String icono, String link) {
        this.eje = eje;
        this.nombre = nombre;
        this.objetivo = objetivo;
        this.icono = icono;
        this.link = link;
    }

    public Integer getEje() {
        return eje;
    }

    public void setEje(Integer eje) {
        this.eje = eje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @XmlTransient
    public List<Registros> getRegistrosList() {
        return registrosList;
    }

    public void setRegistrosList(List<Registros> registrosList) {
        this.registrosList = registrosList;
    }

    @XmlTransient
    public List<ModulosRegistro> getModulosRegistroList() {
        return modulosRegistroList;
    }

    public void setModulosRegistroList(List<ModulosRegistro> modulosRegistroList) {
        this.modulosRegistroList = modulosRegistroList;
    }

    @XmlTransient
    public List<InformacionCorrespondienteArea> getInformacionCorrespondienteAreaList() {
        return informacionCorrespondienteAreaList;
    }

    public void setInformacionCorrespondienteAreaList(List<InformacionCorrespondienteArea> informacionCorrespondienteAreaList) {
        this.informacionCorrespondienteAreaList = informacionCorrespondienteAreaList;
    }

    @XmlTransient
    public List<CuadroMandoIntegral> getCuadroMandoIntegralList() {
        return cuadroMandoIntegralList;
    }

    public void setCuadroMandoIntegralList(List<CuadroMandoIntegral> cuadroMandoIntegralList) {
        this.cuadroMandoIntegralList = cuadroMandoIntegralList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eje != null ? eje.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EjesRegistro)) {
            return false;
        }
        EjesRegistro other = (EjesRegistro) object;
        if ((this.eje == null && other.eje != null) || (this.eje != null && !this.eje.equals(other.eje))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro[ eje=" + eje + " ]";
    }
    
}
