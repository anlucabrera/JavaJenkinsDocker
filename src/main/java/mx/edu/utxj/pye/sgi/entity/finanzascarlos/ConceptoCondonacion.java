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
@Table(name = "concepto_condonacion", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConceptoCondonacion.findAll", query = "SELECT c FROM ConceptoCondonacion c")
    , @NamedQuery(name = "ConceptoCondonacion.findByConcepto", query = "SELECT c FROM ConceptoCondonacion c WHERE c.concepto = :concepto")
    , @NamedQuery(name = "ConceptoCondonacion.findByDescripcion", query = "SELECT c FROM ConceptoCondonacion c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "ConceptoCondonacion.findByTag", query = "SELECT c FROM ConceptoCondonacion c WHERE c.tag = :tag")
    , @NamedQuery(name = "ConceptoCondonacion.findByMontoCondonado", query = "SELECT c FROM ConceptoCondonacion c WHERE c.montoCondonado = :montoCondonado")
    , @NamedQuery(name = "ConceptoCondonacion.findByInicioVigencia", query = "SELECT c FROM ConceptoCondonacion c WHERE c.inicioVigencia = :inicioVigencia")
    , @NamedQuery(name = "ConceptoCondonacion.findByFinVigencia", query = "SELECT c FROM ConceptoCondonacion c WHERE c.finVigencia = :finVigencia")})
public class ConceptoCondonacion implements Serializable {

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
    @Column(name = "monto_condonado")
    private Integer montoCondonado;
    @Column(name = "inicio_vigencia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicioVigencia;
    @Column(name = "fin_vigencia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finVigencia;
    @JoinColumn(name = "concepto", referencedColumnName = "concepto", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Concepto concepto1;

    public ConceptoCondonacion() {
    }

    public ConceptoCondonacion(Integer concepto) {
        this.concepto = concepto;
    }

    public ConceptoCondonacion(Integer concepto, String descripcion) {
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

    public Integer getMontoCondonado() {
        return montoCondonado;
    }

    public void setMontoCondonado(Integer montoCondonado) {
        this.montoCondonado = montoCondonado;
    }

    public Date getInicioVigencia() {
        return inicioVigencia;
    }

    public void setInicioVigencia(Date inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public Date getFinVigencia() {
        return finVigencia;
    }

    public void setFinVigencia(Date finVigencia) {
        this.finVigencia = finVigencia;
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
        if (!(object instanceof ConceptoCondonacion)) {
            return false;
        }
        ConceptoCondonacion other = (ConceptoCondonacion) object;
        if ((this.concepto == null && other.concepto != null) || (this.concepto != null && !this.concepto.equals(other.concepto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.ConceptoCondonacion[ concepto=" + concepto + " ]";
    }
    
}
