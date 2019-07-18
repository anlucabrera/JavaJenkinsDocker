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
@Table(name = "bitacoraediciones", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bitacoraediciones.findAll", query = "SELECT b FROM Bitacoraediciones b")
    , @NamedQuery(name = "Bitacoraediciones.findByRegistro", query = "SELECT b FROM Bitacoraediciones b WHERE b.registro = :registro")
    , @NamedQuery(name = "Bitacoraediciones.findByMatricula", query = "SELECT b FROM Bitacoraediciones b WHERE b.matricula = :matricula")
    , @NamedQuery(name = "Bitacoraediciones.findByConcepto", query = "SELECT b FROM Bitacoraediciones b WHERE b.concepto = :concepto")
    , @NamedQuery(name = "Bitacoraediciones.findByFechaEdicion", query = "SELECT b FROM Bitacoraediciones b WHERE b.fechaEdicion = :fechaEdicion")
    , @NamedQuery(name = "Bitacoraediciones.findByFolio", query = "SELECT b FROM Bitacoraediciones b WHERE b.folio = :folio")})
public class Bitacoraediciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @Column(name = "matricula")
    private int matricula;
    @Basic(optional = false)
    @Column(name = "concepto")
    private int concepto;
    @Basic(optional = false)
    @Column(name = "fechaEdicion")
    @Temporal(TemporalType.DATE)
    private Date fechaEdicion;
    @Basic(optional = false)
    @Column(name = "folio")
    private String folio;

    public Bitacoraediciones() {
    }

    public Bitacoraediciones(Integer registro) {
        this.registro = registro;
    }

    public Bitacoraediciones(Integer registro, int matricula, int concepto, Date fechaEdicion, String folio) {
        this.registro = registro;
        this.matricula = matricula;
        this.concepto = concepto;
        this.fechaEdicion = fechaEdicion;
        this.folio = folio;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getConcepto() {
        return concepto;
    }

    public void setConcepto(int concepto) {
        this.concepto = concepto;
    }

    public Date getFechaEdicion() {
        return fechaEdicion;
    }

    public void setFechaEdicion(Date fechaEdicion) {
        this.fechaEdicion = fechaEdicion;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registro != null ? registro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bitacoraediciones)) {
            return false;
        }
        Bitacoraediciones other = (Bitacoraediciones) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.Bitacoraediciones[ registro=" + registro + " ]";
    }
    
}
