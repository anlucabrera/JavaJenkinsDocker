/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzascarlos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "concepto_pago", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConceptoPago.findAll", query = "SELECT c FROM ConceptoPago c")
    , @NamedQuery(name = "ConceptoPago.findByConcepto", query = "SELECT c FROM ConceptoPago c WHERE c.concepto = :concepto")
    , @NamedQuery(name = "ConceptoPago.findByDescripcion", query = "SELECT c FROM ConceptoPago c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "ConceptoPago.findByTag", query = "SELECT c FROM ConceptoPago c WHERE c.tag = :tag")
    , @NamedQuery(name = "ConceptoPago.findByMonto", query = "SELECT c FROM ConceptoPago c WHERE c.monto = :monto")
    , @NamedQuery(name = "ConceptoPago.findByInicioVigencia", query = "SELECT c FROM ConceptoPago c WHERE c.inicioVigencia = :inicioVigencia")
    , @NamedQuery(name = "ConceptoPago.findByFinVigencia", query = "SELECT c FROM ConceptoPago c WHERE c.finVigencia = :finVigencia")
    , @NamedQuery(name = "ConceptoPago.findByFolioCategoria", query = "SELECT c FROM ConceptoPago c WHERE c.folioCategoria = :folioCategoria")
    , @NamedQuery(name = "ConceptoPago.findByActivo", query = "SELECT c FROM ConceptoPago c WHERE c.activo = :activo")
    , @NamedQuery(name = "ConceptoPago.findByPagoObligatorioTitulacion", query = "SELECT c FROM ConceptoPago c WHERE c.pagoObligatorioTitulacion = :pagoObligatorioTitulacion")
    , @NamedQuery(name = "ConceptoPago.findByIdCatalogoConceptoPago", query = "SELECT c FROM ConceptoPago c WHERE c.idCatalogoConceptoPago = :idCatalogoConceptoPago")})
public class ConceptoPago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "concepto")
    private Integer concepto;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "tag")
    private String tag;
    @Column(name = "monto")
    private Integer monto;
    @Column(name = "inicio_vigencia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicioVigencia;
    @Column(name = "fin_vigencia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finVigencia;
    @Basic(optional = false)
    @Column(name = "folio_categoria")
    private String folioCategoria;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "pago_obligatorio_titulacion")
    private Boolean pagoObligatorioTitulacion;
    @Column(name = "id_catalogo_concepto_pago")
    private String idCatalogoConceptoPago;
    @OneToMany(mappedBy = "pago")
    private List<Registro> registroList;

    public ConceptoPago() {
    }

    public ConceptoPago(Integer concepto) {
        this.concepto = concepto;
    }

    public ConceptoPago(Integer concepto, String folioCategoria) {
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

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
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

    public Boolean getPagoObligatorioTitulacion() {
        return pagoObligatorioTitulacion;
    }

    public void setPagoObligatorioTitulacion(Boolean pagoObligatorioTitulacion) {
        this.pagoObligatorioTitulacion = pagoObligatorioTitulacion;
    }

    public String getIdCatalogoConceptoPago() {
        return idCatalogoConceptoPago;
    }

    public void setIdCatalogoConceptoPago(String idCatalogoConceptoPago) {
        this.idCatalogoConceptoPago = idCatalogoConceptoPago;
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
        if (!(object instanceof ConceptoPago)) {
            return false;
        }
        ConceptoPago other = (ConceptoPago) object;
        if ((this.concepto == null && other.concepto != null) || (this.concepto != null && !this.concepto.equals(other.concepto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.ConceptoPago[ concepto=" + concepto + " ]";
    }
    
}
