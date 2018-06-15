/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "informacion_adicional_personal", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InformacionAdicionalPersonal.findAll", query = "SELECT i FROM InformacionAdicionalPersonal i")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByClave", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.clave = :clave")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByLugarProcedencia", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.lugarProcedencia = :lugarProcedencia")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByDireccion", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.direccion = :direccion")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByEvidenciaDomicilio", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.evidenciaDomicilio = :evidenciaDomicilio")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByEdad", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.edad = :edad")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByEvidenciaIne", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.evidenciaIne = :evidenciaIne")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByEvidenciaActa", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.evidenciaActa = :evidenciaActa")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByCurp", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.curp = :curp")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByEvidenciaCurp", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.evidenciaCurp = :evidenciaCurp")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByRfc", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.rfc = :rfc")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByEvidenciaRfc", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.evidenciaRfc = :evidenciaRfc")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByNumTelMovil", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.numTelMovil = :numTelMovil")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByNumTelFijo", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.numTelFijo = :numTelFijo")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByEstadoCivil", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.estadoCivil = :estadoCivil")
    , @NamedQuery(name = "InformacionAdicionalPersonal.findByEstatus", query = "SELECT i FROM InformacionAdicionalPersonal i WHERE i.estatus = :estatus")})
public class InformacionAdicionalPersonal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave")
    private Integer clave;
    @Size(max = 250)
    @Column(name = "lugar_procedencia")
    private String lugarProcedencia;
    @Size(max = 300)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 255)
    @Column(name = "evidencia_domicilio")
    private String evidenciaDomicilio;
    @Column(name = "edad")
    private Integer edad;
    @Size(max = 255)
    @Column(name = "evidencia_ine")
    private String evidenciaIne;
    @Size(max = 255)
    @Column(name = "evidencia_acta")
    private String evidenciaActa;
    @Size(max = 18)
    @Column(name = "curp")
    private String curp;
    @Size(max = 255)
    @Column(name = "evidencia_curp")
    private String evidenciaCurp;
    @Size(max = 13)
    @Column(name = "rfc")
    private String rfc;
    @Size(max = 255)
    @Column(name = "evidencia_rfc")
    private String evidenciaRfc;
    @Size(max = 20)
    @Column(name = "num_tel_movil")
    private String numTelMovil;
    @Size(max = 20)
    @Column(name = "num_tel_fijo")
    private String numTelFijo;
    @Size(max = 14)
    @Column(name = "estado_civil")
    private String estadoCivil;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @JoinColumn(name = "clave", referencedColumnName = "clave", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Personal personal;

    public InformacionAdicionalPersonal() {
    }

    public InformacionAdicionalPersonal(Integer clave) {
        this.clave = clave;
    }

    public InformacionAdicionalPersonal(Integer clave, String estatus) {
        this.clave = clave;
        this.estatus = estatus;
    }

    public Integer getClave() {
        return clave;
    }

    public void setClave(Integer clave) {
        this.clave = clave;
    }

    public String getLugarProcedencia() {
        return lugarProcedencia;
    }

    public void setLugarProcedencia(String lugarProcedencia) {
        this.lugarProcedencia = lugarProcedencia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEvidenciaDomicilio() {
        return evidenciaDomicilio;
    }

    public void setEvidenciaDomicilio(String evidenciaDomicilio) {
        this.evidenciaDomicilio = evidenciaDomicilio;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getEvidenciaIne() {
        return evidenciaIne;
    }

    public void setEvidenciaIne(String evidenciaIne) {
        this.evidenciaIne = evidenciaIne;
    }

    public String getEvidenciaActa() {
        return evidenciaActa;
    }

    public void setEvidenciaActa(String evidenciaActa) {
        this.evidenciaActa = evidenciaActa;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getEvidenciaCurp() {
        return evidenciaCurp;
    }

    public void setEvidenciaCurp(String evidenciaCurp) {
        this.evidenciaCurp = evidenciaCurp;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getEvidenciaRfc() {
        return evidenciaRfc;
    }

    public void setEvidenciaRfc(String evidenciaRfc) {
        this.evidenciaRfc = evidenciaRfc;
    }

    public String getNumTelMovil() {
        return numTelMovil;
    }

    public void setNumTelMovil(String numTelMovil) {
        this.numTelMovil = numTelMovil;
    }

    public String getNumTelFijo() {
        return numTelFijo;
    }

    public void setNumTelFijo(String numTelFijo) {
        this.numTelFijo = numTelFijo;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clave != null ? clave.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InformacionAdicionalPersonal)) {
            return false;
        }
        InformacionAdicionalPersonal other = (InformacionAdicionalPersonal) object;
        if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal[ clave=" + clave + " ]";
    }
    
}
