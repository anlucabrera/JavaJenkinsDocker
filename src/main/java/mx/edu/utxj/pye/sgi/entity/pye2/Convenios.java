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
    , @NamedQuery(name = "Convenios.findByRecursosObtenidos", query = "SELECT c FROM Convenios c WHERE c.recursosObtenidos = :recursosObtenidos")
    , @NamedQuery(name = "Convenios.findByAach", query = "SELECT c FROM Convenios c WHERE c.aach = :aach")
    , @NamedQuery(name = "Convenios.findByAarh", query = "SELECT c FROM Convenios c WHERE c.aarh = :aarh")
    , @NamedQuery(name = "Convenios.findByIdie", query = "SELECT c FROM Convenios c WHERE c.idie = :idie")
    , @NamedQuery(name = "Convenios.findByPa", query = "SELECT c FROM Convenios c WHERE c.pa = :pa")
    , @NamedQuery(name = "Convenios.findByQab", query = "SELECT c FROM Convenios c WHERE c.qab = :qab")
    , @NamedQuery(name = "Convenios.findByGas", query = "SELECT c FROM Convenios c WHERE c.gas = :gas")
    , @NamedQuery(name = "Convenios.findByAsp", query = "SELECT c FROM Convenios c WHERE c.asp = :asp")
    , @NamedQuery(name = "Convenios.findByIpa", query = "SELECT c FROM Convenios c WHERE c.ipa = :ipa")
    , @NamedQuery(name = "Convenios.findByIbio", query = "SELECT c FROM Convenios c WHERE c.ibio = :ibio")
    , @NamedQuery(name = "Convenios.findByMai", query = "SELECT c FROM Convenios c WHERE c.mai = :mai")
    , @NamedQuery(name = "Convenios.findByMiap", query = "SELECT c FROM Convenios c WHERE c.miap = :miap")
    , @NamedQuery(name = "Convenios.findByImi", query = "SELECT c FROM Convenios c WHERE c.imi = :imi")
    , @NamedQuery(name = "Convenios.findByMecaa", query = "SELECT c FROM Convenios c WHERE c.mecaa = :mecaa")
    , @NamedQuery(name = "Convenios.findByImeca", query = "SELECT c FROM Convenios c WHERE c.imeca = :imeca")
    , @NamedQuery(name = "Convenios.findByTicasi", query = "SELECT c FROM Convenios c WHERE c.ticasi = :ticasi")
    , @NamedQuery(name = "Convenios.findByTicamc", query = "SELECT c FROM Convenios c WHERE c.ticamc = :ticamc")
    , @NamedQuery(name = "Convenios.findByItic", query = "SELECT c FROM Convenios c WHERE c.itic = :itic")
    , @NamedQuery(name = "Convenios.findByTfar", query = "SELECT c FROM Convenios c WHERE c.tfar = :tfar")
    , @NamedQuery(name = "Convenios.findByLtefi", query = "SELECT c FROM Convenios c WHERE c.ltefi = :ltefi")})
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "AACH")
    private Character aach;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AARH")
    private Character aarh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDIE")
    private Character idie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PA")
    private Character pa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QAB")
    private Character qab;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GAS")
    private Character gas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ASP")
    private Character asp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IPA")
    private Character ipa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IBIO")
    private Character ibio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MAI")
    private Character mai;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MIAP")
    private Character miap;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IMI")
    private Character imi;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MECAA")
    private Character mecaa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IMECA")
    private Character imeca;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TICASI")
    private Character ticasi;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TICAMC")
    private Character ticamc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ITIC")
    private Character itic;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TFAR")
    private Character tfar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LTEFI")
    private Character ltefi;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumn(name = "empresa", referencedColumnName = "empresa")
    @ManyToOne(optional = false)
    private OrganismosVinculados empresa;

    public Convenios() {
    }

    public Convenios(Integer registro) {
        this.registro = registro;
    }

    public Convenios(Integer registro, Date fechaFirma, Date vigencia, String objetivo, String descripcion, String impacto, short ebh, short ebm, short dbh, short dbm, double recursosObtenidos, Character aach, Character aarh, Character idie, Character pa, Character qab, Character gas, Character asp, Character ipa, Character ibio, Character mai, Character miap, Character imi, Character mecaa, Character imeca, Character ticasi, Character ticamc, Character itic, Character tfar, Character ltefi) {
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
        this.aach = aach;
        this.aarh = aarh;
        this.idie = idie;
        this.pa = pa;
        this.qab = qab;
        this.gas = gas;
        this.asp = asp;
        this.ipa = ipa;
        this.ibio = ibio;
        this.mai = mai;
        this.miap = miap;
        this.imi = imi;
        this.mecaa = mecaa;
        this.imeca = imeca;
        this.ticasi = ticasi;
        this.ticamc = ticamc;
        this.itic = itic;
        this.tfar = tfar;
        this.ltefi = ltefi;
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

    public Character getAach() {
        return aach;
    }

    public void setAach(Character aach) {
        this.aach = aach;
    }

    public Character getAarh() {
        return aarh;
    }

    public void setAarh(Character aarh) {
        this.aarh = aarh;
    }

    public Character getIdie() {
        return idie;
    }

    public void setIdie(Character idie) {
        this.idie = idie;
    }

    public Character getPa() {
        return pa;
    }

    public void setPa(Character pa) {
        this.pa = pa;
    }

    public Character getQab() {
        return qab;
    }

    public void setQab(Character qab) {
        this.qab = qab;
    }

    public Character getGas() {
        return gas;
    }

    public void setGas(Character gas) {
        this.gas = gas;
    }

    public Character getAsp() {
        return asp;
    }

    public void setAsp(Character asp) {
        this.asp = asp;
    }

    public Character getIpa() {
        return ipa;
    }

    public void setIpa(Character ipa) {
        this.ipa = ipa;
    }

    public Character getIbio() {
        return ibio;
    }

    public void setIbio(Character ibio) {
        this.ibio = ibio;
    }

    public Character getMai() {
        return mai;
    }

    public void setMai(Character mai) {
        this.mai = mai;
    }

    public Character getMiap() {
        return miap;
    }

    public void setMiap(Character miap) {
        this.miap = miap;
    }

    public Character getImi() {
        return imi;
    }

    public void setImi(Character imi) {
        this.imi = imi;
    }

    public Character getMecaa() {
        return mecaa;
    }

    public void setMecaa(Character mecaa) {
        this.mecaa = mecaa;
    }

    public Character getImeca() {
        return imeca;
    }

    public void setImeca(Character imeca) {
        this.imeca = imeca;
    }

    public Character getTicasi() {
        return ticasi;
    }

    public void setTicasi(Character ticasi) {
        this.ticasi = ticasi;
    }

    public Character getTicamc() {
        return ticamc;
    }

    public void setTicamc(Character ticamc) {
        this.ticamc = ticamc;
    }

    public Character getItic() {
        return itic;
    }

    public void setItic(Character itic) {
        this.itic = itic;
    }

    public Character getTfar() {
        return tfar;
    }

    public void setTfar(Character tfar) {
        this.tfar = tfar;
    }

    public Character getLtefi() {
        return ltefi;
    }

    public void setLtefi(Character ltefi) {
        this.ltefi = ltefi;
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
