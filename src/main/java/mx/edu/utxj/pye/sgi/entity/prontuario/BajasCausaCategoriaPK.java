/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class BajasCausaCategoriaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "causa")
    private int causa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 26)
    @Column(name = "categoria")
    private String categoria;

    public BajasCausaCategoriaPK() {
    }

    public BajasCausaCategoriaPK(int causa, String categoria) {
        this.causa = causa;
        this.categoria = categoria;
    }

    public int getCausa() {
        return causa;
    }

    public void setCausa(int causa) {
        this.causa = causa;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) causa;
        hash += (categoria != null ? categoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BajasCausaCategoriaPK)) {
            return false;
        }
        BajasCausaCategoriaPK other = (BajasCausaCategoriaPK) object;
        if (this.causa != other.causa) {
            return false;
        }
        if ((this.categoria == null && other.categoria != null) || (this.categoria != null && !this.categoria.equals(other.categoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausaCategoriaPK[ causa=" + causa + ", categoria=" + categoria + " ]";
    }
    
}
