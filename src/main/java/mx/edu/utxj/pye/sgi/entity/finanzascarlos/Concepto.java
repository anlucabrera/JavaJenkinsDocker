/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzascarlos;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "concepto", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Concepto.findAll", query = "SELECT c FROM Concepto c")
    , @NamedQuery(name = "Concepto.findByConcepto", query = "SELECT c FROM Concepto c WHERE c.concepto = :concepto")
    , @NamedQuery(name = "Concepto.findByDescripcion", query = "SELECT c FROM Concepto c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "Concepto.findByTag", query = "SELECT c FROM Concepto c WHERE c.tag = :tag")
    , @NamedQuery(name = "Concepto.findByFolioCategoria", query = "SELECT c FROM Concepto c WHERE c.folioCategoria = :folioCategoria")
    , @NamedQuery(name = "Concepto.findByActivo", query = "SELECT c FROM Concepto c WHERE c.activo = :activo")})
public class Concepto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "concepto")
    private Integer concepto;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "tag")
    private String tag;
    @Basic(optional = false)
    @Column(name = "folio_categoria")
    private String folioCategoria;
    @Column(name = "activo")
    private Boolean activo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "concepto1")
    private Credito credito;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "concepto1")
    private ConceptoCondonacion conceptoCondonacion;
    @OneToMany(mappedBy = "concepto")
    private List<Registro> registroList;

    public Concepto() {
    }

    public Concepto(Integer concepto) {
        this.concepto = concepto;
    }

    public Concepto(Integer concepto, String folioCategoria) {
        this.concepto = concepto;
        this.folioCategoria = folioCategoria;
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

    public String getFolioCategoria() {
        return folioCategoria;
    }

    public void setFolioCategoria(String folioCategoria) {
        this.folioCategoria = folioCategoria;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Credito getCredito() {
        return credito;
    }

    public void setCredito(Credito credito) {
        this.credito = credito;
    }

    public ConceptoCondonacion getConceptoCondonacion() {
        return conceptoCondonacion;
    }

    public void setConceptoCondonacion(ConceptoCondonacion conceptoCondonacion) {
        this.conceptoCondonacion = conceptoCondonacion;
    }

    @XmlTransient
    public List<Registro> getRegistroList() {
        return registroList;
    }

    public void setRegistroList(List<Registro> registroList) {
        this.registroList = registroList;
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
        if (!(object instanceof Concepto)) {
            return false;
        }
        Concepto other = (Concepto) object;
        if ((this.concepto == null && other.concepto != null) || (this.concepto != null && !this.concepto.equals(other.concepto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.Concepto[ concepto=" + concepto + " ]";
    }
    
}
