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
 * @author Desarrollo
 */
@Entity
@Table(name = "contacto_emergencias_estudiante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContactoEmergenciasEstudiante.findAll", query = "SELECT c FROM ContactoEmergenciasEstudiante c")
    , @NamedQuery(name = "ContactoEmergenciasEstudiante.findByIdContactoEmergecia", query = "SELECT c FROM ContactoEmergenciasEstudiante c WHERE c.idContactoEmergecia = :idContactoEmergecia")
    , @NamedQuery(name = "ContactoEmergenciasEstudiante.findByNombre", query = "SELECT c FROM ContactoEmergenciasEstudiante c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "ContactoEmergenciasEstudiante.findByApellidoPaterno", query = "SELECT c FROM ContactoEmergenciasEstudiante c WHERE c.apellidoPaterno = :apellidoPaterno")
    , @NamedQuery(name = "ContactoEmergenciasEstudiante.findByApellidoMaterno", query = "SELECT c FROM ContactoEmergenciasEstudiante c WHERE c.apellidoMaterno = :apellidoMaterno")
    , @NamedQuery(name = "ContactoEmergenciasEstudiante.findByTelefono", query = "SELECT c FROM ContactoEmergenciasEstudiante c WHERE c.telefono = :telefono")
    , @NamedQuery(name = "ContactoEmergenciasEstudiante.findByCalle", query = "SELECT c FROM ContactoEmergenciasEstudiante c WHERE c.calle = :calle")
    , @NamedQuery(name = "ContactoEmergenciasEstudiante.findByNumero", query = "SELECT c FROM ContactoEmergenciasEstudiante c WHERE c.numero = :numero")
    , @NamedQuery(name = "ContactoEmergenciasEstudiante.findByEstado", query = "SELECT c FROM ContactoEmergenciasEstudiante c WHERE c.estado = :estado")
    , @NamedQuery(name = "ContactoEmergenciasEstudiante.findByMunicipio", query = "SELECT c FROM ContactoEmergenciasEstudiante c WHERE c.municipio = :municipio")
    , @NamedQuery(name = "ContactoEmergenciasEstudiante.findByAsentamiento", query = "SELECT c FROM ContactoEmergenciasEstudiante c WHERE c.asentamiento = :asentamiento")
    , @NamedQuery(name = "ContactoEmergenciasEstudiante.findByParentesco", query = "SELECT c FROM ContactoEmergenciasEstudiante c WHERE c.parentesco = :parentesco")})
public class ContactoEmergenciasEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_contacto_emergecia")
    private Integer idContactoEmergecia;
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

    public ContactoEmergenciasEstudiante() {
    }

    public ContactoEmergenciasEstudiante(Integer idContactoEmergecia) {
        this.idContactoEmergecia = idContactoEmergecia;
    }

    public ContactoEmergenciasEstudiante(Integer idContactoEmergecia, String nombre, String apellidoPaterno, String apellidoMaterno, int estado, int municipio, int asentamiento, String parentesco) {
        this.idContactoEmergecia = idContactoEmergecia;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.estado = estado;
        this.municipio = municipio;
        this.asentamiento = asentamiento;
        this.parentesco = parentesco;
    }

    public Integer getIdContactoEmergecia() {
        return idContactoEmergecia;
    }

    public void setIdContactoEmergecia(Integer idContactoEmergecia) {
        this.idContactoEmergecia = idContactoEmergecia;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContactoEmergecia != null ? idContactoEmergecia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContactoEmergenciasEstudiante)) {
            return false;
        }
        ContactoEmergenciasEstudiante other = (ContactoEmergenciasEstudiante) object;
        if ((this.idContactoEmergecia == null && other.idContactoEmergecia != null) || (this.idContactoEmergecia != null && !this.idContactoEmergecia.equals(other.idContactoEmergecia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.ContactoEmergenciasEstudiante[ idContactoEmergecia=" + idContactoEmergecia + " ]";
    }
    
}
