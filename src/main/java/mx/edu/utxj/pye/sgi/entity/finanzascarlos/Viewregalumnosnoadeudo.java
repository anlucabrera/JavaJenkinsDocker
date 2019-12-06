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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "viewregalumnosnoadeudo", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Viewregalumnosnoadeudo.findAll", query = "SELECT v FROM Viewregalumnosnoadeudo v")
    , @NamedQuery(name = "Viewregalumnosnoadeudo.findByCveRegistro", query = "SELECT v FROM Viewregalumnosnoadeudo v WHERE v.cveRegistro = :cveRegistro")
    , @NamedQuery(name = "Viewregalumnosnoadeudo.findByMatricula", query = "SELECT v FROM Viewregalumnosnoadeudo v WHERE v.matricula = :matricula")
    , @NamedQuery(name = "Viewregalumnosnoadeudo.findBySiglas", query = "SELECT v FROM Viewregalumnosnoadeudo v WHERE v.siglas = :siglas")
    , @NamedQuery(name = "Viewregalumnosnoadeudo.findByValcartanoadueudoTSU", query = "SELECT v FROM Viewregalumnosnoadeudo v WHERE v.valcartanoadueudoTSU = :valcartanoadueudoTSU")
    , @NamedQuery(name = "Viewregalumnosnoadeudo.findByValcartanoadedudoIng", query = "SELECT v FROM Viewregalumnosnoadeudo v WHERE v.valcartanoadedudoIng = :valcartanoadedudoIng")
    , @NamedQuery(name = "Viewregalumnosnoadeudo.findByConcepto", query = "SELECT v FROM Viewregalumnosnoadeudo v WHERE v.concepto = :concepto")
    , @NamedQuery(name = "Viewregalumnosnoadeudo.findByFechaPago", query = "SELECT v FROM Viewregalumnosnoadeudo v WHERE v.fechaPago = :fechaPago")
    , @NamedQuery(name = "Viewregalumnosnoadeudo.findByDescripcion", query = "SELECT v FROM Viewregalumnosnoadeudo v WHERE v.descripcion = :descripcion")
    , @NamedQuery(name = "Viewregalumnosnoadeudo.findByMonto", query = "SELECT v FROM Viewregalumnosnoadeudo v WHERE v.monto = :monto")
    , @NamedQuery(name = "Viewregalumnosnoadeudo.findByIdCatalogoConceptoPago", query = "SELECT v FROM Viewregalumnosnoadeudo v WHERE v.idCatalogoConceptoPago = :idCatalogoConceptoPago")})
public class Viewregalumnosnoadeudo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_registro")
    private int cveRegistro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "matricula")
    private String matricula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "siglas")
    private String siglas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "val_carta_no_adueudo_TSU")
    private short valcartanoadueudoTSU;
    @Basic(optional = false)
    @NotNull
    @Column(name = "val_carta_no_adedudo_Ing")
    private short valcartanoadedudoIng;
    @Basic(optional = false)
    @NotNull
    @Column(name = "concepto")
    private int concepto;
    @Column(name = "fecha_pago")
    @Temporal(TemporalType.DATE)
    private Date fechaPago;
    @Size(max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "monto")
    private Integer monto;
    @Size(max = 9)
    @Column(name = "id_catalogo_concepto_pago")
    private String idCatalogoConceptoPago;

    public Viewregalumnosnoadeudo() {
    }

    public int getCveRegistro() {
        return cveRegistro;
    }

    public void setCveRegistro(int cveRegistro) {
        this.cveRegistro = cveRegistro;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public short getValcartanoadueudoTSU() {
        return valcartanoadueudoTSU;
    }

    public void setValcartanoadueudoTSU(short valcartanoadueudoTSU) {
        this.valcartanoadueudoTSU = valcartanoadueudoTSU;
    }

    public short getValcartanoadedudoIng() {
        return valcartanoadedudoIng;
    }

    public void setValcartanoadedudoIng(short valcartanoadedudoIng) {
        this.valcartanoadedudoIng = valcartanoadedudoIng;
    }

    public int getConcepto() {
        return concepto;
    }

    public void setConcepto(int concepto) {
        this.concepto = concepto;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    public String getIdCatalogoConceptoPago() {
        return idCatalogoConceptoPago;
    }

    public void setIdCatalogoConceptoPago(String idCatalogoConceptoPago) {
        this.idCatalogoConceptoPago = idCatalogoConceptoPago;
    }
    
}
