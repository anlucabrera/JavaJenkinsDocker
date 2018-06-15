/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "prueba", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prueba.findAll", query = "SELECT p FROM Prueba p")
    , @NamedQuery(name = "Prueba.findByCiclo", query = "SELECT p FROM Prueba p WHERE p.pruebaPK.ciclo = :ciclo")
    , @NamedQuery(name = "Prueba.findByNivel", query = "SELECT p FROM Prueba p WHERE p.pruebaPK.nivel = :nivel")
    , @NamedQuery(name = "Prueba.findByOtro", query = "SELECT p FROM Prueba p WHERE p.otro = :otro")})
public class Prueba implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PruebaPK pruebaPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "otro")
    private String otro;

    public Prueba() {
    }

    public Prueba(PruebaPK pruebaPK) {
        this.pruebaPK = pruebaPK;
    }

    public Prueba(PruebaPK pruebaPK, String otro) {
        this.pruebaPK = pruebaPK;
        this.otro = otro;
    }

    public Prueba(int ciclo, String nivel) {
        this.pruebaPK = new PruebaPK(ciclo, nivel);
    }

    public PruebaPK getPruebaPK() {
        return pruebaPK;
    }

    public void setPruebaPK(PruebaPK pruebaPK) {
        this.pruebaPK = pruebaPK;
    }

    public String getOtro() {
        return otro;
    }

    public void setOtro(String otro) {
        this.otro = otro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pruebaPK != null ? pruebaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prueba)) {
            return false;
        }
        Prueba other = (Prueba) object;
        if ((this.pruebaPK == null && other.pruebaPK != null) || (this.pruebaPK != null && !this.pruebaPK.equals(other.pruebaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Prueba[ pruebaPK=" + pruebaPK + " ]";
    }
    
}
