/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "tutor_familiar", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TutorFamiliar.findAll", query = "SELECT t FROM TutorFamiliar t")
    , @NamedQuery(name = "TutorFamiliar.findByIdTutorFamiliar", query = "SELECT t FROM TutorFamiliar t WHERE t.idTutorFamiliar = :idTutorFamiliar")
    , @NamedQuery(name = "TutorFamiliar.findByNombre", query = "SELECT t FROM TutorFamiliar t WHERE t.nombre = :nombre")
    , @NamedQuery(name = "TutorFamiliar.findByApellidoPaterno", query = "SELECT t FROM TutorFamiliar t WHERE t.apellidoPaterno = :apellidoPaterno")
    , @NamedQuery(name = "TutorFamiliar.findByApellidoMaterno", query = "SELECT t FROM TutorFamiliar t WHERE t.apellidoMaterno = :apellidoMaterno")
    , @NamedQuery(name = "TutorFamiliar.findByCalle", query = "SELECT t FROM TutorFamiliar t WHERE t.calle = :calle")
    , @NamedQuery(name = "TutorFamiliar.findByNumero", query = "SELECT t FROM TutorFamiliar t WHERE t.numero = :numero")
    , @NamedQuery(name = "TutorFamiliar.findByEstado", query = "SELECT t FROM TutorFamiliar t WHERE t.estado = :estado")
    , @NamedQuery(name = "TutorFamiliar.findByMunicipio", query = "SELECT t FROM TutorFamiliar t WHERE t.municipio = :municipio")
    , @NamedQuery(name = "TutorFamiliar.findByAsentamiento", query = "SELECT t FROM TutorFamiliar t WHERE t.asentamiento = :asentamiento")
    , @NamedQuery(name = "TutorFamiliar.findByNoTelefono", query = "SELECT t FROM TutorFamiliar t WHERE t.noTelefono = :noTelefono")
    , @NamedQuery(name = "TutorFamiliar.findByParentesco", query = "SELECT t FROM TutorFamiliar t WHERE t.parentesco = :parentesco")})
public class TutorFamiliar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tutor_familiar")
    private Integer idTutorFamiliar;
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
    @Size(max = 20)
    @Column(name = "no_telefono")
    private String noTelefono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "parentesco")
    private String parentesco;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tutor")
    private List<DatosFamiliares> datosFamiliaresList;

    public TutorFamiliar() {
    }

    public TutorFamiliar(Integer idTutorFamiliar) {
        this.idTutorFamiliar = idTutorFamiliar;
    }

    public TutorFamiliar(Integer idTutorFamiliar, String nombre, String apellidoPaterno, String apellidoMaterno, String calle, String numero, int estado, int municipio, int asentamiento, String parentesco) {
        this.idTutorFamiliar = idTutorFamiliar;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.calle = calle;
        this.numero = numero;
        this.estado = estado;
        this.municipio = municipio;
        this.asentamiento = asentamiento;
        this.parentesco = parentesco;
    }

    public Integer getIdTutorFamiliar() {
        return idTutorFamiliar;
    }

    public void setIdTutorFamiliar(Integer idTutorFamiliar) {
        this.idTutorFamiliar = idTutorFamiliar;
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

    public String getNoTelefono() {
        return noTelefono;
    }

    public void setNoTelefono(String noTelefono) {
        this.noTelefono = noTelefono;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    @XmlTransient
    public List<DatosFamiliares> getDatosFamiliaresList() {
        return datosFamiliaresList;
    }

    public void setDatosFamiliaresList(List<DatosFamiliares> datosFamiliaresList) {
        this.datosFamiliaresList = datosFamiliaresList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTutorFamiliar != null ? idTutorFamiliar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TutorFamiliar)) {
            return false;
        }
        TutorFamiliar other = (TutorFamiliar) object;
        if ((this.idTutorFamiliar == null && other.idTutorFamiliar != null) || (this.idTutorFamiliar != null && !this.idTutorFamiliar.equals(other.idTutorFamiliar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TutorFamiliar[ idTutorFamiliar=" + idTutorFamiliar + " ]";
    }
    
}
