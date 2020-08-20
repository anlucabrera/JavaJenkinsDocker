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
import javax.persistence.FetchType;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "convenios", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Convenios.findAll", query = "SELECT c FROM Convenios c")
    , @NamedQuery(name = "Convenios.findByRegistro", query = "SELECT c FROM Convenios c WHERE c.registro = :registro")
    , @NamedQuery(name = "Convenios.findByFechaFirma", query = "SELECT c FROM Convenios c WHERE c.fechaFirma = :fechaFirma")
    , @NamedQuery(name = "Convenios.findByVigencia", query = "SELECT c FROM Convenios c WHERE c.vigencia = :vigencia")
    , @NamedQuery(name = "Convenios.findByObjetivo", query = "SELECT c FROM Convenios c WHERE c.objetivo = :objetivo")
    , @NamedQuery(name = "Convenios.findByDescripcion", query = "SELECT c FROM Convenios c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "Convenios.findByImpacto", query = "SELECT c FROM Convenios c WHERE c.impacto = :impacto")
    , @NamedQuery(name = "Convenios.findByEbh", query = "SELECT c FROM Convenios c WHERE c.ebh = :ebh")
    , @NamedQuery(name = "Convenios.findByEbm", query = "SELECT c FROM Convenios c WHERE c.ebm = :ebm")
    , @NamedQuery(name = "Convenios.findByDbh", query = "SELECT c FROM Convenios c WHERE c.dbh = :dbh")
    , @NamedQuery(name = "Convenios.findByDbm", query = "SELECT c FROM Convenios c WHERE c.dbm = :dbm")
    , @NamedQuery(name = "Convenios.findByRecursosObtenidos", query = "SELECT c FROM Convenios c WHERE c.recursosObtenidos = :recursosObtenidos")})
public class Convenios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_firma")
    @Temporal(TemporalType.DATE)
    private Date fechaFirma;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vigencia")
    @Temporal(TemporalType.DATE)
    private Date vigencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "objetivo")
    private String objetivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "impacto")
    private String impacto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EBH")
    private short ebh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EBM")
    private short ebm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DBH")
    private short dbh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DBM")
    private short dbm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "recursos_obtenidos")
    private double recursosObtenidos;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;
    @JoinColumn(name = "empresa", referencedColumnName = "empresa")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private OrganismosVinculados empresa;

    public Convenios() {
    }

    public Convenios(Integer registro) {
        this.registro = registro;
    }

    public Convenios(Integer registro, Date fechaFirma, Date vigencia, String objetivo, String descripcion, String impacto, short ebh, short ebm, short dbh, short dbm, double recursosObtenidos) {
        this.registro = registro;
        this.fechaFirma = fechaFirma;
        this.vigencia = vigencia;
        this.objetivo = objetivo;
        this.descripcion = descripcion;
        this.impacto = impacto;
        this.ebh = ebh;
        this.ebm = ebm;
        this.dbh = dbh;
        this.dbm = dbm;
        this.recursosObtenidos = recursosObtenidos;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public Date getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(Date fechaFirma) {
        this.fechaFirma = fechaFirma;
    }

    public Date getVigencia() {
        return vigencia;
    }

    public void setVigencia(Date vigencia) {
        this.vigencia = vigencia;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImpacto() {
        return impacto;
    }

    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }

    public short getEbh() {
        return ebh;
    }

    public void setEbh(short ebh) {
        this.ebh = ebh;
    }

    public short getEbm() {
        return ebm;
    }

    public void setEbm(short ebm) {
        this.ebm = ebm;
    }

    public short getDbh() {
        return dbh;
    }

    public void setDbh(short dbh) {
        this.dbh = dbh;
    }

    public short getDbm() {
        return dbm;
    }

    public void setDbm(short dbm) {
        this.dbm = dbm;
    }

    public double getRecursosObtenidos() {
        return recursosObtenidos;
    }

    public void setRecursosObtenidos(double recursosObtenidos) {
        this.recursosObtenidos = recursosObtenidos;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public OrganismosVinculados getEmpresa() {
        return empresa;
    }

    public void setEmpresa(OrganismosVinculados empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof Convenios)) {
            return false;
        }
        Convenios other = (Convenios) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Convenios[ registro=" + registro + " ]";
    }
    
}
