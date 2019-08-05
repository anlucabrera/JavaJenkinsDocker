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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "registro", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Registro.findAll", query = "SELECT r FROM Registro r")
    , @NamedQuery(name = "Registro.findByCveRegistro", query = "SELECT r FROM Registro r WHERE r.cveRegistro = :cveRegistro")
    , @NamedQuery(name = "Registro.findBySiglas", query = "SELECT r FROM Registro r WHERE r.siglas = :siglas")
    , @NamedQuery(name = "Registro.findByCurp", query = "SELECT r FROM Registro r WHERE r.curp = :curp")
    , @NamedQuery(name = "Registro.findByFechaPago", query = "SELECT r FROM Registro r WHERE r.fechaPago = :fechaPago")
    , @NamedQuery(name = "Registro.findByTipo", query = "SELECT r FROM Registro r WHERE r.tipo = :tipo")
    , @NamedQuery(name = "Registro.findByNotas", query = "SELECT r FROM Registro r WHERE r.notas = :notas")
    , @NamedQuery(name = "Registro.findByFolio", query = "SELECT r FROM Registro r WHERE r.folio = :folio")
    , @NamedQuery(name = "Registro.findByNoReferemcia", query = "SELECT r FROM Registro r WHERE r.noReferemcia = :noReferemcia")
    , @NamedQuery(name = "Registro.findByMontoBiblioteca", query = "SELECT r FROM Registro r WHERE r.montoBiblioteca = :montoBiblioteca")
    , @NamedQuery(name = "Registro.findByIdentificadorPagoBiblioteca", query = "SELECT r FROM Registro r WHERE r.identificadorPagoBiblioteca = :identificadorPagoBiblioteca")
    , @NamedQuery(name = "Registro.findByCancelado", query = "SELECT r FROM Registro r WHERE r.cancelado = :cancelado")})
public class Registro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cve_registro")
    private Integer cveRegistro;
    @Column(name = "siglas")
    private String siglas;
    @Column(name = "curp")
    private String curp;
    @Column(name = "fecha_pago")
    @Temporal(TemporalType.DATE)
    private Date fechaPago;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "notas")
    private String notas;
    @Basic(optional = false)
    @Column(name = "Folio")
    private String folio;
    @Column(name = "no_referemcia")
    private String noReferemcia;
    @Column(name = "monto_biblioteca")
    private Integer montoBiblioteca;
    @Column(name = "identificador_pago_biblioteca")
    private String identificadorPagoBiblioteca;
    @Column(name = "cancelado")
    private Boolean cancelado;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "registro")
    private PagosColegiatura pagosColegiatura;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cveRegistro")
    private List<Pago> pagoList;
    @JoinColumn(name = "pago", referencedColumnName = "concepto")
    @ManyToOne
    private ConceptoPago pago;
    @OneToMany(mappedBy = "hRegistro")
    private List<Registro> registroList;
    @JoinColumn(name = "h_registro", referencedColumnName = "cve_registro")
    @ManyToOne
    private Registro hRegistro;
    @JoinColumn(name = "concepto", referencedColumnName = "concepto")
    @ManyToOne
    private Concepto concepto;
    @JoinColumn(name = "registro_evidencia", referencedColumnName = "evidencia")
    @ManyToOne
    private EvidenciasFinanzas registroEvidencia;
    @JoinColumns({
        @JoinColumn(name = "matricula", referencedColumnName = "matricula")
        , @JoinColumn(name = "periodo", referencedColumnName = "periodo")})
    @ManyToOne
    private AlumnoFinanzas alumnoFinanzas;

    public Registro() {
    }

    public Registro(Integer cveRegistro) {
        this.cveRegistro = cveRegistro;
    }

    public Registro(Integer cveRegistro, String folio) {
        this.cveRegistro = cveRegistro;
        this.folio = folio;
    }

    public Integer getCveRegistro() {
        return cveRegistro;
    }

    public void setCveRegistro(Integer cveRegistro) {
        this.cveRegistro = cveRegistro;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getNoReferemcia() {
        return noReferemcia;
    }

    public void setNoReferemcia(String noReferemcia) {
        this.noReferemcia = noReferemcia;
    }

    public Integer getMontoBiblioteca() {
        return montoBiblioteca;
    }

    public void setMontoBiblioteca(Integer montoBiblioteca) {
        this.montoBiblioteca = montoBiblioteca;
    }

    public String getIdentificadorPagoBiblioteca() {
        return identificadorPagoBiblioteca;
    }

    public void setIdentificadorPagoBiblioteca(String identificadorPagoBiblioteca) {
        this.identificadorPagoBiblioteca = identificadorPagoBiblioteca;
    }

    public Boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public PagosColegiatura getPagosColegiatura() {
        return pagosColegiatura;
    }

    public void setPagosColegiatura(PagosColegiatura pagosColegiatura) {
        this.pagosColegiatura = pagosColegiatura;
    }

    @XmlTransient
    public List<Pago> getPagoList() {
        return pagoList;
    }

    public void setPagoList(List<Pago> pagoList) {
        this.pagoList = pagoList;
    }

    public ConceptoPago getPago() {
        return pago;
    }

    public void setPago(ConceptoPago pago) {
        this.pago = pago;
    }

    @XmlTransient
    public List<Registro> getRegistroList() {
        return registroList;
    }

    public void setRegistroList(List<Registro> registroList) {
        this.registroList = registroList;
    }

    public Registro getHRegistro() {
        return hRegistro;
    }

    public void setHRegistro(Registro hRegistro) {
        this.hRegistro = hRegistro;
    }

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }

    public EvidenciasFinanzas getRegistroEvidencia() {
        return registroEvidencia;
    }

    public void setRegistroEvidencia(EvidenciasFinanzas registroEvidencia) {
        this.registroEvidencia = registroEvidencia;
    }

    public AlumnoFinanzas getAlumnoFinanzas() {
        return alumnoFinanzas;
    }

    public void setAlumnoFinanzas(AlumnoFinanzas alumnoFinanzas) {
        this.alumnoFinanzas = alumnoFinanzas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveRegistro != null ? cveRegistro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Registro)) {
            return false;
        }
        Registro other = (Registro) object;
        if ((this.cveRegistro == null && other.cveRegistro != null) || (this.cveRegistro != null && !this.cveRegistro.equals(other.cveRegistro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.Registro[ cveRegistro=" + cveRegistro + " ]";
    }
    
}
