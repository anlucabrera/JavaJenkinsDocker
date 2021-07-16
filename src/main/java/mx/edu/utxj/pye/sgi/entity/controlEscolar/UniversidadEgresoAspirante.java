/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "universidad_egreso_aspirante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UniversidadEgresoAspirante.findAll", query = "SELECT u FROM UniversidadEgresoAspirante u")
    , @NamedQuery(name = "UniversidadEgresoAspirante.findByAspirante", query = "SELECT u FROM UniversidadEgresoAspirante u WHERE u.universidadEgresoAspirantePK.aspirante = :aspirante")
    , @NamedQuery(name = "UniversidadEgresoAspirante.findByUniversidadEgreso", query = "SELECT u FROM UniversidadEgresoAspirante u WHERE u.universidadEgresoAspirantePK.universidadEgreso = :universidadEgreso")})
public class UniversidadEgresoAspirante implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UniversidadEgresoAspirantePK universidadEgresoAspirantePK;
    @JoinColumn(name = "aspirante", referencedColumnName = "id_aspirante", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Aspirante aspirante1;

    public UniversidadEgresoAspirante() {
    }

    public UniversidadEgresoAspirante(UniversidadEgresoAspirantePK universidadEgresoAspirantePK) {
        this.universidadEgresoAspirantePK = universidadEgresoAspirantePK;
    }

    public UniversidadEgresoAspirante(int aspirante, int universidadEgreso) {
        this.universidadEgresoAspirantePK = new UniversidadEgresoAspirantePK(aspirante, universidadEgreso);
    }

    public UniversidadEgresoAspirantePK getUniversidadEgresoAspirantePK() {
        return universidadEgresoAspirantePK;
    }

    public void setUniversidadEgresoAspirantePK(UniversidadEgresoAspirantePK universidadEgresoAspirantePK) {
        this.universidadEgresoAspirantePK = universidadEgresoAspirantePK;
    }

    public Aspirante getAspirante1() {
        return aspirante1;
    }

    public void setAspirante1(Aspirante aspirante1) {
        this.aspirante1 = aspirante1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (universidadEgresoAspirantePK != null ? universidadEgresoAspirantePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UniversidadEgresoAspirante)) {
            return false;
        }
        UniversidadEgresoAspirante other = (UniversidadEgresoAspirante) object;
        if ((this.universidadEgresoAspirantePK == null && other.universidadEgresoAspirantePK != null) || (this.universidadEgresoAspirantePK != null && !this.universidadEgresoAspirantePK.equals(other.universidadEgresoAspirantePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.UniversidadEgresoAspirante[ universidadEgresoAspirantePK=" + universidadEgresoAspirantePK + " ]";
    }
    
}
