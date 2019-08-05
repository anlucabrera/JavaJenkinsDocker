/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzascarlos;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "vistapagosprimercuatrimestre", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vistapagosprimercuatrimestre.findAll", query = "SELECT v FROM Vistapagosprimercuatrimestre v")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByCveRegistro", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.cveRegistro = :cveRegistro")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByPeriodo", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.periodo = :periodo")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByMatricula", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.matricula = :matricula")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findBySiglas", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.siglas = :siglas")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByCurp", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.curp = :curp")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByFechaPago", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.fechaPago = :fechaPago")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByTipo", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.tipo = :tipo")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByConcepto", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.concepto = :concepto")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByPago", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.pago = :pago")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByRegistroEvidencia", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.registroEvidencia = :registroEvidencia")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByNotas", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.notas = :notas")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByFolio", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.folio = :folio")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByNoReferemcia", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.noReferemcia = :noReferemcia")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByMontoBiblioteca", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.montoBiblioteca = :montoBiblioteca")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByIdentificadorPagoBiblioteca", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.identificadorPagoBiblioteca = :identificadorPagoBiblioteca")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByCancelado", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.cancelado = :cancelado")
    , @NamedQuery(name = "Vistapagosprimercuatrimestre.findByHRegistro", query = "SELECT v FROM Vistapagosprimercuatrimestre v WHERE v.hRegistro = :hRegistro")})
public class Vistapagosprimercuatrimestre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cve_registro")
    private int cveRegistro;
    @Column(name = "periodo")
    private Integer periodo;
    @Column(name = "matricula")
    private Integer matricula;
    @Column(name = "siglas")
    private String siglas;
    @Column(name = "curp")
    private String curp;
    @Column(name = "fecha_pago")
    @Temporal(TemporalType.DATE)
    private Date fechaPago;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "concepto")
    private Integer concepto;
    @Column(name = "pago")
    private Integer pago;
    @Column(name = "registro_evidencia")
    private Integer registroEvidencia;
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
    @Column(name = "h_registro")
    private Integer hRegistro;

    public Vistapagosprimercuatrimestre() {
    }

    public int getCveRegistro() {
        return cveRegistro;
    }

    public void setCveRegistro(int cveRegistro) {
        this.cveRegistro = cveRegistro;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
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

    public Integer getConcepto() {
        return concepto;
    }

    public void setConcepto(Integer concepto) {
        this.concepto = concepto;
    }

    public Integer getPago() {
        return pago;
    }

    public void setPago(Integer pago) {
        this.pago = pago;
    }

    public Integer getRegistroEvidencia() {
        return registroEvidencia;
    }

    public void setRegistroEvidencia(Integer registroEvidencia) {
        this.registroEvidencia = registroEvidencia;
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

    public Integer getHRegistro() {
        return hRegistro;
    }

    public void setHRegistro(Integer hRegistro) {
        this.hRegistro = hRegistro;
    }
    
}
