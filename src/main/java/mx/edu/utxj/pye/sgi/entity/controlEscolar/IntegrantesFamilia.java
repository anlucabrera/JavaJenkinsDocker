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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "integrantes_familia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IntegrantesFamilia.findAll", query = "SELECT i FROM IntegrantesFamilia i")
    , @NamedQuery(name = "IntegrantesFamilia.findByIdIntegrantesFamilia", query = "SELECT i FROM IntegrantesFamilia i WHERE i.idIntegrantesFamilia = :idIntegrantesFamilia")
    , @NamedQuery(name = "IntegrantesFamilia.findByNombre", query = "SELECT i FROM IntegrantesFamilia i WHERE i.nombre = :nombre")
    , @NamedQuery(name = "IntegrantesFamilia.findByApellidoPaterno", query = "SELECT i FROM IntegrantesFamilia i WHERE i.apellidoPaterno = :apellidoPaterno")
    , @NamedQuery(name = "IntegrantesFamilia.findByApellidoMaterno", query = "SELECT i FROM IntegrantesFamilia i WHERE i.apellidoMaterno = :apellidoMaterno")
    , @NamedQuery(name = "IntegrantesFamilia.findByTelefono", query = "SELECT i FROM IntegrantesFamilia i WHERE i.telefono = :telefono")
    , @NamedQuery(name = "IntegrantesFamilia.findByCalle", query = "SELECT i FROM IntegrantesFamilia i WHERE i.calle = :calle")
    , @NamedQuery(name = "IntegrantesFamilia.findByNumero", query = "SELECT i FROM IntegrantesFamilia i WHERE i.numero = :numero")
    , @NamedQuery(name = "IntegrantesFamilia.findByEstado", query = "SELECT i FROM IntegrantesFamilia i WHERE i.estado = :estado")
    , @NamedQuery(name = "IntegrantesFamilia.findByMunicipio", query = "SELECT i FROM IntegrantesFamilia i WHERE i.municipio = :municipio")
    , @NamedQuery(name = "IntegrantesFamilia.findByAsentamiento", query = "SELECT i FROM IntegrantesFamilia i WHERE i.asentamiento = :asentamiento")
    , @NamedQuery(name = "IntegrantesFamilia.findByParentesco", query = "SELECT i FROM IntegrantesFamilia i WHERE i.parentesco = :parentesco")})
public class IntegrantesFamilia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_integrantes_familia")
    private Integer idIntegrantesFamilia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "apellido_paterno")
    private String apellidoPaterno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "apellido_materno")
    private String apellidoMaterno;
    @Size(max = 20)
    @Column(name = "telefono")
    private String telefono;
    @Size(max = 150)
    @Column(name = "calle")
    private String calle;
    @Size(max = 10)
    @Column(name = "numero")
    private String numero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private int estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "municipio")
    private int municipio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "asentamiento")
    private int asentamiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "parentesco")
    private String parentesco;
    @JoinColumn(name = "aspirante", referencedColumnName = "id_aspirante")
    @ManyToOne(fetch = FetchType.LAZY)
    private Aspirante aspirante;
    @JoinColumn(name = "escolaridad", referencedColumnName = "id_escolaridad")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Escolaridad escolaridad;
    @JoinColumn(name = "ocupacion", referencedColumnName = "id_ocupacion")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Ocupacion ocupacion;

    public IntegrantesFamilia() {
    }

    public IntegrantesFamilia(Integer idIntegrantesFamilia) {
        this.idIntegrantesFamilia = idIntegrantesFamilia;
    }

    public IntegrantesFamilia(Integer idIntegrantesFamilia, String nombre, String apellidoPaterno, String apellidoMaterno, int estado, int municipio, int asentamiento, String parentesco) {
        this.idIntegrantesFamilia = idIntegrantesFamilia;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.estado = estado;
        this.municipio = municipio;
        this.asentamiento = asentamiento;
        this.parentesco = parentesco;
    }

    public Integer getIdIntegrantesFamilia() {
        return idIntegrantesFamilia;
    }

    public void setIdIntegrantesFamilia(Integer idIntegrantesFamilia) {
        this.idIntegrantesFamilia = idIntegrantesFamilia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getMunicipio() {
        return municipio;
    }

    public void setMunicipio(int municipio) {
        this.municipio = municipio;
    }

    public int getAsentamiento() {
        return asentamiento;
    }

    public void setAsentamiento(int asentamiento) {
        this.asentamiento = asentamiento;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public Aspirante getAspirante() {
        return aspirante;
    }

    public void setAspirante(Aspirante aspirante) {
        this.aspirante = aspirante;
    }

    public Escolaridad getEscolaridad() {
        return escolaridad;
    }

    public void setEscolaridad(Escolaridad escolaridad) {
        this.escolaridad = escolaridad;
    }

    public Ocupacion getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(Ocupacion ocupacion) {
        this.ocupacion = ocupacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idIntegrantesFamilia != null ? idIntegrantesFamilia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IntegrantesFamilia)) {
            return false;
        }
        IntegrantesFamilia other = (IntegrantesFamilia) object;
        if ((this.idIntegrantesFamilia == null && other.idIntegrantesFamilia != null) || (this.idIntegrantesFamilia != null && !this.idIntegrantesFamilia.equals(other.idIntegrantesFamilia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.IntegrantesFamilia[ idIntegrantesFamilia=" + idIntegrantesFamilia + " ]";
    }
    
}
