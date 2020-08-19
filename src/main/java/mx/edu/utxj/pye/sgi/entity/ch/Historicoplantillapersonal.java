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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "historicoplantillapersonal", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Historicoplantillapersonal.findAll", query = "SELECT h FROM Historicoplantillapersonal h")
    , @NamedQuery(name = "Historicoplantillapersonal.findByHistorico", query = "SELECT h FROM Historicoplantillapersonal h WHERE h.historico = :historico")
    , @NamedQuery(name = "Historicoplantillapersonal.findByCuatrimestre", query = "SELECT h FROM Historicoplantillapersonal h WHERE h.cuatrimestre = :cuatrimestre")
    , @NamedQuery(name = "Historicoplantillapersonal.findByMescaptura", query = "SELECT h FROM Historicoplantillapersonal h WHERE h.mescaptura = :mescaptura")
    , @NamedQuery(name = "Historicoplantillapersonal.findByAnio", query = "SELECT h FROM Historicoplantillapersonal h WHERE h.anio = :anio")
    , @NamedQuery(name = "Historicoplantillapersonal.findByRutaArchivo", query = "SELECT h FROM Historicoplantillapersonal h WHERE h.rutaArchivo = :rutaArchivo")})
public class Historicoplantillapersonal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "historico")
    private Integer historico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "cuatrimestre")
    private String cuatrimestre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "mescaptura")
    private String mescaptura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "anio")
    private String anio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "rutaArchivo")
    private String rutaArchivo;

    public Historicoplantillapersonal() {
    }

    public Historicoplantillapersonal(Integer historico) {
        this.historico = historico;
    }

    public Historicoplantillapersonal(Integer historico, String cuatrimestre, String mescaptura, String anio, String rutaArchivo) {
        this.historico = historico;
        this.cuatrimestre = cuatrimestre;
        this.mescaptura = mescaptura;
        this.anio = anio;
        this.rutaArchivo = rutaArchivo;
    }

    public Integer getHistorico() {
        return historico;
    }

    public void setHistorico(Integer historico) {
        this.historico = historico;
    }

    public String getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(String cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public String getMescaptura() {
        return mescaptura;
    }

    public void setMescaptura(String mescaptura) {
        this.mescaptura = mescaptura;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (historico != null ? historico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Historicoplantillapersonal)) {
            return false;
        }
        Historicoplantillapersonal other = (Historicoplantillapersonal) object;
        if ((this.historico == null && other.historico != null) || (this.historico != null && !this.historico.equals(other.historico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Historicoplantillapersonal[ historico=" + historico + " ]";
    }
    
}
