/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "lenguas", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lenguas.findAll", query = "SELECT l FROM Lenguas l")
    , @NamedQuery(name = "Lenguas.findByLengua", query = "SELECT l FROM Lenguas l WHERE l.lengua = :lengua")
    , @NamedQuery(name = "Lenguas.findByTipoLengua", query = "SELECT l FROM Lenguas l WHERE l.tipoLengua = :tipoLengua")
    , @NamedQuery(name = "Lenguas.findByGradoDominio", query = "SELECT l FROM Lenguas l WHERE l.gradoDominio = :gradoDominio")
    , @NamedQuery(name = "Lenguas.findByNivelConversacion", query = "SELECT l FROM Lenguas l WHERE l.nivelConversacion = :nivelConversacion")
    , @NamedQuery(name = "Lenguas.findByNivelLectura", query = "SELECT l FROM Lenguas l WHERE l.nivelLectura = :nivelLectura")
    , @NamedQuery(name = "Lenguas.findByNivelEscritura", query = "SELECT l FROM Lenguas l WHERE l.nivelEscritura = :nivelEscritura")
    , @NamedQuery(name = "Lenguas.findByEstatus", query = "SELECT l FROM Lenguas l WHERE l.estatus = :estatus")})
public class Lenguas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "lengua")
    private Integer lengua;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 21)
    @Column(name = "tipo_lengua")
    private String tipoLengua;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 19)
    @Column(name = "grado_dominio")
    private String gradoDominio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 19)
    @Column(name = "nivel_conversacion")
    private String nivelConversacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 19)
    @Column(name = "nivel_lectura")
    private String nivelLectura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 19)
    @Column(name = "nivel_escritura")
    private String nivelEscritura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Personal clavePersonal;

    public Lenguas() {
    }

    public Lenguas(Integer lengua) {
        this.lengua = lengua;
    }

    public Lenguas(Integer lengua, String tipoLengua, String gradoDominio, String nivelConversacion, String nivelLectura, String nivelEscritura, String estatus) {
        this.lengua = lengua;
        this.tipoLengua = tipoLengua;
        this.gradoDominio = gradoDominio;
        this.nivelConversacion = nivelConversacion;
        this.nivelLectura = nivelLectura;
        this.nivelEscritura = nivelEscritura;
        this.estatus = estatus;
    }

    public Integer getLengua() {
        return lengua;
    }

    public void setLengua(Integer lengua) {
        this.lengua = lengua;
    }

    public String getTipoLengua() {
        return tipoLengua;
    }

    public void setTipoLengua(String tipoLengua) {
        this.tipoLengua = tipoLengua;
    }

    public String getGradoDominio() {
        return gradoDominio;
    }

    public void setGradoDominio(String gradoDominio) {
        this.gradoDominio = gradoDominio;
    }

    public String getNivelConversacion() {
        return nivelConversacion;
    }

    public void setNivelConversacion(String nivelConversacion) {
        this.nivelConversacion = nivelConversacion;
    }

    public String getNivelLectura() {
        return nivelLectura;
    }

    public void setNivelLectura(String nivelLectura) {
        this.nivelLectura = nivelLectura;
    }

    public String getNivelEscritura() {
        return nivelEscritura;
    }

    public void setNivelEscritura(String nivelEscritura) {
        this.nivelEscritura = nivelEscritura;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Personal getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(Personal clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lengua != null ? lengua.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lenguas)) {
            return false;
        }
        Lenguas other = (Lenguas) object;
        if ((this.lengua == null && other.lengua != null) || (this.lengua != null && !this.lengua.equals(other.lengua))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Lenguas[ lengua=" + lengua + " ]";
    }
    
}
