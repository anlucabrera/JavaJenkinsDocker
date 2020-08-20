/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
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
@Table(name = "bajas_causa_categoria", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BajasCausaCategoria.findAll", query = "SELECT b FROM BajasCausaCategoria b")
    , @NamedQuery(name = "BajasCausaCategoria.findByCausa", query = "SELECT b FROM BajasCausaCategoria b WHERE b.bajasCausaCategoriaPK.causa = :causa")
    , @NamedQuery(name = "BajasCausaCategoria.findByCategoria", query = "SELECT b FROM BajasCausaCategoria b WHERE b.bajasCausaCategoriaPK.categoria = :categoria")})
public class BajasCausaCategoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BajasCausaCategoriaPK bajasCausaCategoriaPK;
    @JoinColumn(name = "causa", referencedColumnName = "cve_causa", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BajasCausa bajasCausa;

    public BajasCausaCategoria() {
    }

    public BajasCausaCategoria(BajasCausaCategoriaPK bajasCausaCategoriaPK) {
        this.bajasCausaCategoriaPK = bajasCausaCategoriaPK;
    }

    public BajasCausaCategoria(int causa, String categoria) {
        this.bajasCausaCategoriaPK = new BajasCausaCategoriaPK(causa, categoria);
    }

    public BajasCausaCategoriaPK getBajasCausaCategoriaPK() {
        return bajasCausaCategoriaPK;
    }

    public void setBajasCausaCategoriaPK(BajasCausaCategoriaPK bajasCausaCategoriaPK) {
        this.bajasCausaCategoriaPK = bajasCausaCategoriaPK;
    }

    public BajasCausa getBajasCausa() {
        return bajasCausa;
    }

    public void setBajasCausa(BajasCausa bajasCausa) {
        this.bajasCausa = bajasCausa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bajasCausaCategoriaPK != null ? bajasCausaCategoriaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BajasCausaCategoria)) {
            return false;
        }
        BajasCausaCategoria other = (BajasCausaCategoria) object;
        if ((this.bajasCausaCategoriaPK == null && other.bajasCausaCategoriaPK != null) || (this.bajasCausaCategoriaPK != null && !this.bajasCausaCategoriaPK.equals(other.bajasCausaCategoriaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausaCategoria[ bajasCausaCategoriaPK=" + bajasCausaCategoriaPK + " ]";
    }
    
}
