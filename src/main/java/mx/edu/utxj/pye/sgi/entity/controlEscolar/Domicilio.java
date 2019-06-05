/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

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
@Table(name = "domicilio", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Domicilio.findAll", query = "SELECT d FROM Domicilio d")
    , @NamedQuery(name = "Domicilio.findByAspirante", query = "SELECT d FROM Domicilio d WHERE d.aspirante = :aspirante")
    , @NamedQuery(name = "Domicilio.findByCalle", query = "SELECT d FROM Domicilio d WHERE d.calle = :calle")
    , @NamedQuery(name = "Domicilio.findByNumero", query = "SELECT d FROM Domicilio d WHERE d.numero = :numero")
    , @NamedQuery(name = "Domicilio.findByIdEstado", query = "SELECT d FROM Domicilio d WHERE d.idEstado = :idEstado")
    , @NamedQuery(name = "Domicilio.findByIdMunicipio", query = "SELECT d FROM Domicilio d WHERE d.idMunicipio = :idMunicipio")
    , @NamedQuery(name = "Domicilio.findByIdAsentamiento", query = "SELECT d FROM Domicilio d WHERE d.idAsentamiento = :idAsentamiento")
    , @NamedQuery(name = "Domicilio.findByTiempoResidencia", query = "SELECT d FROM Domicilio d WHERE d.tiempoResidencia = :tiempoResidencia")
    , @NamedQuery(name = "Domicilio.findByCalleProcedencia", query = "SELECT d FROM Domicilio d WHERE d.calleProcedencia = :calleProcedencia")
    , @NamedQuery(name = "Domicilio.findByNumeroProcedencia", query = "SELECT d FROM Domicilio d WHERE d.numeroProcedencia = :numeroProcedencia")
    , @NamedQuery(name = "Domicilio.findByEstadoProcedencia", query = "SELECT d FROM Domicilio d WHERE d.estadoProcedencia = :estadoProcedencia")
    , @NamedQuery(name = "Domicilio.findByMunicipioProcedencia", query = "SELECT d FROM Domicilio d WHERE d.municipioProcedencia = :municipioProcedencia")
    , @NamedQuery(name = "Domicilio.findByAsentamientoProcedencia", query = "SELECT d FROM Domicilio d WHERE d.asentamientoProcedencia = :asentamientoProcedencia")
    , @NamedQuery(name = "Domicilio.findByVivesCon", query = "SELECT d FROM Domicilio d WHERE d.vivesCon = :vivesCon")})
public class Domicilio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "aspirante")
    private Integer aspirante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "calle")
    private String calle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "numero")
    private String numero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_estado")
    private int idEstado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_municipio")
    private int idMunicipio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_asentamiento")
    private int idAsentamiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "tiempoResidencia")
    private String tiempoResidencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "calle_procedencia")
    private String calleProcedencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "numero_procedencia")
    private String numeroProcedencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado_procedencia")
    private int estadoProcedencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "municipio_procedencia")
    private int municipioProcedencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "asentamiento_procedencia")
    private int asentamientoProcedencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "vives_con")
    private String vivesCon;
    @JoinColumn(name = "aspirante", referencedColumnName = "id_aspirante", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Aspirante aspirante1;

    public Domicilio() {
    }

    public Domicilio(Integer aspirante) {
        this.aspirante = aspirante;
    }

    public Domicilio(Integer aspirante, String calle, String numero, int idEstado, int idMunicipio, int idAsentamiento, String tiempoResidencia, String calleProcedencia, String numeroProcedencia, int estadoProcedencia, int municipioProcedencia, int asentamientoProcedencia, String vivesCon) {
        this.aspirante = aspirante;
        this.calle = calle;
        this.numero = numero;
        this.idEstado = idEstado;
        this.idMunicipio = idMunicipio;
        this.idAsentamiento = idAsentamiento;
        this.tiempoResidencia = tiempoResidencia;
        this.calleProcedencia = calleProcedencia;
        this.numeroProcedencia = numeroProcedencia;
        this.estadoProcedencia = estadoProcedencia;
        this.municipioProcedencia = municipioProcedencia;
        this.asentamientoProcedencia = asentamientoProcedencia;
        this.vivesCon = vivesCon;
    }

    public Integer getAspirante() {
        return aspirante;
    }

    public void setAspirante(Integer aspirante) {
        this.aspirante = aspirante;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public int getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public int getIdAsentamiento() {
        return idAsentamiento;
    }

    public void setIdAsentamiento(int idAsentamiento) {
        this.idAsentamiento = idAsentamiento;
    }

    public String getTiempoResidencia() {
        return tiempoResidencia;
    }

    public void setTiempoResidencia(String tiempoResidencia) {
        this.tiempoResidencia = tiempoResidencia;
    }

    public String getCalleProcedencia() {
        return calleProcedencia;
    }

    public void setCalleProcedencia(String calleProcedencia) {
        this.calleProcedencia = calleProcedencia;
    }

    public String getNumeroProcedencia() {
        return numeroProcedencia;
    }

    public void setNumeroProcedencia(String numeroProcedencia) {
        this.numeroProcedencia = numeroProcedencia;
    }

    public int getEstadoProcedencia() {
        return estadoProcedencia;
    }

    public void setEstadoProcedencia(int estadoProcedencia) {
        this.estadoProcedencia = estadoProcedencia;
    }

    public int getMunicipioProcedencia() {
        return municipioProcedencia;
    }

    public void setMunicipioProcedencia(int municipioProcedencia) {
        this.municipioProcedencia = municipioProcedencia;
    }

    public int getAsentamientoProcedencia() {
        return asentamientoProcedencia;
    }

    public void setAsentamientoProcedencia(int asentamientoProcedencia) {
        this.asentamientoProcedencia = asentamientoProcedencia;
    }

    public String getVivesCon() {
        return vivesCon;
    }

    public void setVivesCon(String vivesCon) {
        this.vivesCon = vivesCon;
    }

    public Aspirante getAspirante1() {
        return aspirante1;
    }

    public void setAspirante1(Aspirante aspirante1) {
        this.aspirante1 = aspirante1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aspirante != null ? aspirante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Domicilio)) {
            return false;
        }
        Domicilio other = (Domicilio) object;
        if ((this.aspirante == null && other.aspirante != null) || (this.aspirante != null && !this.aspirante.equals(other.aspirante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Domicilio[ aspirante=" + aspirante + " ]";
    }
    
}
