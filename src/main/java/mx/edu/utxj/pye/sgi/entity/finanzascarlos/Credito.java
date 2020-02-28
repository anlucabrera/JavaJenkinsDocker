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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "credito", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Credito.findAll", query = "SELECT c FROM Credito c")
    , @NamedQuery(name = "Credito.findByConcepto", query = "SELECT c FROM Credito c WHERE c.concepto = :concepto")
    , @NamedQuery(name = "Credito.findByDescripcion", query = "SELECT c FROM Credito c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "Credito.findByTag", query = "SELECT c FROM Credito c WHERE c.tag = :tag")
    , @NamedQuery(name = "Credito.findByClausula", query = "SELECT c FROM Credito c WHERE c.clausula = :clausula")
    , @NamedQuery(name = "Credito.findByFechaInicioAcuerdo", query = "SELECT c FROM Credito c WHERE c.fechaInicioAcuerdo = :fechaInicioAcuerdo")
    , @NamedQuery(name = "Credito.findByFechaFinAcuerdo", query = "SELECT c FROM Credito c WHERE c.fechaFinAcuerdo = :fechaFinAcuerdo")})
public class Credito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "concepto")
    private Integer concepto;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "tag")
    private String tag;
    @Column(name = "clausula")
    private String clausula;
    @Column(name = "fecha_inicio_acuerdo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicioAcuerdo;
    @Column(name = "fecha_fin_acuerdo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinAcuerdo;
    @JoinColumn(name = "concepto", referencedColumnName = "concepto", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Concepto concepto1;

    public Credito() {
    }

    public Credito(Integer concepto) {
        this.concepto = concepto;
    }

    public Credito(Integer concepto, String descripcion) {
        this.concepto = concepto;
        this.descripcion = descripcion;
    }

    public Integer getConcepto() {
        return concepto;
    }

    public void setConcepto(Integer concepto) {
        this.concepto = concepto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getClausula() {
        return clausula;
    }

    public void setClausula(String clausula) {
        this.clausula = clausula;
    }

    public Date getFechaInicioAcuerdo() {
        return fechaInicioAcuerdo;
    }

    public void setFechaInicioAcuerdo(Date fechaInicioAcuerdo) {
        this.fechaInicioAcuerdo = fechaInicioAcuerdo;
    }

    public Date getFechaFinAcuerdo() {
        return fechaFinAcuerdo;
    }

    public void setFechaFinAcuerdo(Date fechaFinAcuerdo) {
        this.fechaFinAcuerdo = fechaFinAcuerdo;
    }

    public Concepto getConcepto1() {
        return concepto1;
    }

    public void setConcepto1(Concepto concepto1) {
        this.concepto1 = concepto1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (concepto != null ? concepto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Credito)) {
            return false;
        }
        Credito other = (Credito) object;
        if ((this.concepto == null && other.concepto != null) || (this.concepto != null && !this.concepto.equals(other.concepto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.Credito[ concepto=" + concepto + " ]";
    }
    
}
