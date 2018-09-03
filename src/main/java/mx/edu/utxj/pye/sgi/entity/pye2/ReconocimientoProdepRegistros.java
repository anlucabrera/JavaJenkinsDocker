/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "reconocimiento_prodep_registros", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReconocimientoProdepRegistros.findAll", query = "SELECT r FROM ReconocimientoProdepRegistros r")
    , @NamedQuery(name = "ReconocimientoProdepRegistros.findByRegistro", query = "SELECT r FROM ReconocimientoProdepRegistros r WHERE r.registro = :registro")
    , @NamedQuery(name = "ReconocimientoProdepRegistros.findByDocente", query = "SELECT r FROM ReconocimientoProdepRegistros r WHERE r.docente = :docente")
    , @NamedQuery(name = "ReconocimientoProdepRegistros.findByMonto", query = "SELECT r FROM ReconocimientoProdepRegistros r WHERE r.monto = :monto")
    , @NamedQuery(name = "ReconocimientoProdepRegistros.findByVigenciaInicial", query = "SELECT r FROM ReconocimientoProdepRegistros r WHERE r.vigenciaInicial = :vigenciaInicial")
    , @NamedQuery(name = "ReconocimientoProdepRegistros.findByVigenciaFinal", query = "SELECT r FROM ReconocimientoProdepRegistros r WHERE r.vigenciaFinal = :vigenciaFinal")})
public class ReconocimientoProdepRegistros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "docente")
    private int docente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto")
    private double monto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vigencia_inicial")
    @Temporal(TemporalType.DATE)
    private Date vigenciaInicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vigencia_final")
    @Temporal(TemporalType.DATE)
    private Date vigenciaFinal;
    @JoinColumn(name = "cuerp_acad", referencedColumnName = "cuerpo_academico")
    @ManyToOne(optional = false)
    private CuerposAcademicosRegistro cuerpAcad;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumn(name = "tipo_apoyo", referencedColumnName = "tipo")
    @ManyToOne(optional = false)
    private ReconocimientoProdepTiposApoyo tipoApoyo;

    public ReconocimientoProdepRegistros() {
    }

    public ReconocimientoProdepRegistros(Integer registro) {
        this.registro = registro;
    }

    public ReconocimientoProdepRegistros(Integer registro, int docente, double monto, Date vigenciaInicial, Date vigenciaFinal) {
        this.registro = registro;
        this.docente = docente;
        this.monto = monto;
        this.vigenciaInicial = vigenciaInicial;
        this.vigenciaFinal = vigenciaFinal;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getDocente() {
        return docente;
    }

    public void setDocente(int docente) {
        this.docente = docente;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getVigenciaInicial() {
        return vigenciaInicial;
    }

    public void setVigenciaInicial(Date vigenciaInicial) {
        this.vigenciaInicial = vigenciaInicial;
    }

    public Date getVigenciaFinal() {
        return vigenciaFinal;
    }

    public void setVigenciaFinal(Date vigenciaFinal) {
        this.vigenciaFinal = vigenciaFinal;
    }

    public CuerposAcademicosRegistro getCuerpAcad() {
        return cuerpAcad;
    }

    public void setCuerpAcad(CuerposAcademicosRegistro cuerpAcad) {
        this.cuerpAcad = cuerpAcad;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public ReconocimientoProdepTiposApoyo getTipoApoyo() {
        return tipoApoyo;
    }

    public void setTipoApoyo(ReconocimientoProdepTiposApoyo tipoApoyo) {
        this.tipoApoyo = tipoApoyo;
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
        if (!(object instanceof ReconocimientoProdepRegistros)) {
            return false;
        }
        ReconocimientoProdepRegistros other = (ReconocimientoProdepRegistros) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ReconocimientoProdepRegistros[ registro=" + registro + " ]";
    }
    
}
