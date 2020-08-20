/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "modulos_registro_especifico", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModulosRegistroEspecifico.findAll", query = "SELECT m FROM ModulosRegistroEspecifico m")
    , @NamedQuery(name = "ModulosRegistroEspecifico.findByClave", query = "SELECT m FROM ModulosRegistroEspecifico m WHERE m.modulosRegistroEspecificoPK.clave = :clave")
    , @NamedQuery(name = "ModulosRegistroEspecifico.findByPersonal", query = "SELECT m FROM ModulosRegistroEspecifico m WHERE m.modulosRegistroEspecificoPK.personal = :personal")
    , @NamedQuery(name = "ModulosRegistroEspecifico.findByAreaRegistro", query = "SELECT m FROM ModulosRegistroEspecifico m WHERE m.areaRegistro = :areaRegistro")})
public class ModulosRegistroEspecifico implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ModulosRegistroEspecificoPK modulosRegistroEspecificoPK;
    @Column(name = "areaRegistro")
    private Short areaRegistro;
    @JoinColumn(name = "clave", referencedColumnName = "clave", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ModulosRegistro modulosRegistro;

    public ModulosRegistroEspecifico() {
    }

    public ModulosRegistroEspecifico(ModulosRegistroEspecificoPK modulosRegistroEspecificoPK) {
        this.modulosRegistroEspecificoPK = modulosRegistroEspecificoPK;
    }

    public ModulosRegistroEspecifico(short clave, int personal) {
        this.modulosRegistroEspecificoPK = new ModulosRegistroEspecificoPK(clave, personal);
    }

    public ModulosRegistroEspecificoPK getModulosRegistroEspecificoPK() {
        return modulosRegistroEspecificoPK;
    }

    public void setModulosRegistroEspecificoPK(ModulosRegistroEspecificoPK modulosRegistroEspecificoPK) {
        this.modulosRegistroEspecificoPK = modulosRegistroEspecificoPK;
    }

    public Short getAreaRegistro() {
        return areaRegistro;
    }

    public void setAreaRegistro(Short areaRegistro) {
        this.areaRegistro = areaRegistro;
    }

    public ModulosRegistro getModulosRegistro() {
        return modulosRegistro;
    }

    public void setModulosRegistro(ModulosRegistro modulosRegistro) {
        this.modulosRegistro = modulosRegistro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modulosRegistroEspecificoPK != null ? modulosRegistroEspecificoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModulosRegistroEspecifico)) {
            return false;
        }
        ModulosRegistroEspecifico other = (ModulosRegistroEspecifico) object;
        if ((this.modulosRegistroEspecificoPK == null && other.modulosRegistroEspecificoPK != null) || (this.modulosRegistroEspecificoPK != null && !this.modulosRegistroEspecificoPK.equals(other.modulosRegistroEspecificoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistroEspecifico[ modulosRegistroEspecificoPK=" + modulosRegistroEspecificoPK + " ]";
    }
    
}
