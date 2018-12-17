/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author PLANEACION
 */
@Entity
@Table(name = "cuidados", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuidados.findAll", query = "SELECT c FROM Cuidados c")
    , @NamedQuery(name = "Cuidados.findByCuidados", query = "SELECT c FROM Cuidados c WHERE c.cuidados = :cuidados")
    , @NamedQuery(name = "Cuidados.findByNumero", query = "SELECT c FROM Cuidados c WHERE c.numero = :numero")
    , @NamedQuery(name = "Cuidados.findByFechaInicio", query = "SELECT c FROM Cuidados c WHERE c.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Cuidados.findByFechaFin", query = "SELECT c FROM Cuidados c WHERE c.fechaFin = :fechaFin")
    , @NamedQuery(name = "Cuidados.findByNumeroDias", query = "SELECT c FROM Cuidados c WHERE c.numeroDias = :numeroDias")
    , @NamedQuery(name = "Cuidados.findByEvidencia", query = "SELECT c FROM Cuidados c WHERE c.evidencia = :evidencia")
    , @NamedQuery(name = "Cuidados.findByTipo", query = "SELECT c FROM Cuidados c WHERE c.tipo = :tipo")})
public class Cuidados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cuidados")
    private Integer cuidados;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private int numero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero_dias")
    private int numeroDias;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "evidencia")
    private String evidencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "tipo")
    private String tipo;
    @JoinColumn(name = "personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal personal;

    public Cuidados() {
    }

    public Cuidados(Integer cuidados) {
        this.cuidados = cuidados;
    }

    public Cuidados(Integer cuidados, int numero, Date fechaInicio, Date fechaFin, int numeroDias, String evidencia, String tipo) {
        this.cuidados = cuidados;
        this.numero = numero;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numeroDias = numeroDias;
        this.evidencia = evidencia;
        this.tipo = tipo;
    }

    public Integer getCuidados() {
        return cuidados;
    }

    public void setCuidados(Integer cuidados) {
        this.cuidados = cuidados;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getNumeroDias() {
        return numeroDias;
    }

    public void setNumeroDias(int numeroDias) {
        this.numeroDias = numeroDias;
    }

    public String getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(String evidencia) {
        this.evidencia = evidencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cuidados != null ? cuidados.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuidados)) {
            return false;
        }
        Cuidados other = (Cuidados) object;
        if ((this.cuidados == null && other.cuidados != null) || (this.cuidados != null && !this.cuidados.equals(other.cuidados))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Cuidados[ cuidados=" + cuidados + " ]";
    }
    
}
