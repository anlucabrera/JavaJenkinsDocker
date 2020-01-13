/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzascarlos;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "avisos", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Avisos.findAll", query = "SELECT a FROM Avisos a")
    , @NamedQuery(name = "Avisos.findByCveAvisos", query = "SELECT a FROM Avisos a WHERE a.cveAvisos = :cveAvisos")
    , @NamedQuery(name = "Avisos.findByTipo", query = "SELECT a FROM Avisos a WHERE a.tipo = :tipo")
    , @NamedQuery(name = "Avisos.findByAviso", query = "SELECT a FROM Avisos a WHERE a.aviso = :aviso")
    , @NamedQuery(name = "Avisos.findByFecha", query = "SELECT a FROM Avisos a WHERE a.fecha = :fecha")})
public class Avisos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cve_avisos")
    private Integer cveAvisos;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "aviso")
    private String aviso;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    public Avisos() {
    }

    public Avisos(Integer cveAvisos) {
        this.cveAvisos = cveAvisos;
    }

    public Integer getCveAvisos() {
        return cveAvisos;
    }

    public void setCveAvisos(Integer cveAvisos) {
        this.cveAvisos = cveAvisos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAviso() {
        return aviso;
    }

    public void setAviso(String aviso) {
        this.aviso = aviso;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveAvisos != null ? cveAvisos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Avisos)) {
            return false;
        }
        Avisos other = (Avisos) object;
        if ((this.cveAvisos == null && other.cveAvisos != null) || (this.cveAvisos != null && !this.cveAvisos.equals(other.cveAvisos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.Avisos[ cveAvisos=" + cveAvisos + " ]";
    }
    
}
