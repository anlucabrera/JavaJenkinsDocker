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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "datos_familiares", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosFamiliares.findAll", query = "SELECT d FROM DatosFamiliares d")
    , @NamedQuery(name = "DatosFamiliares.findByAspirante", query = "SELECT d FROM DatosFamiliares d WHERE d.aspirante = :aspirante")
    , @NamedQuery(name = "DatosFamiliares.findByNombrePadre", query = "SELECT d FROM DatosFamiliares d WHERE d.nombrePadre = :nombrePadre")
    , @NamedQuery(name = "DatosFamiliares.findByTelefonoPadre", query = "SELECT d FROM DatosFamiliares d WHERE d.telefonoPadre = :telefonoPadre")
    , @NamedQuery(name = "DatosFamiliares.findByNombreMadre", query = "SELECT d FROM DatosFamiliares d WHERE d.nombreMadre = :nombreMadre")
    , @NamedQuery(name = "DatosFamiliares.findByTelefonoMadre", query = "SELECT d FROM DatosFamiliares d WHERE d.telefonoMadre = :telefonoMadre")
    , @NamedQuery(name = "DatosFamiliares.findByTutor", query = "SELECT d FROM DatosFamiliares d WHERE d.tutor = :tutor")
    , @NamedQuery(name = "DatosFamiliares.findByCallePadre", query = "SELECT d FROM DatosFamiliares d WHERE d.callePadre = :callePadre")
    , @NamedQuery(name = "DatosFamiliares.findByNumeroPadre", query = "SELECT d FROM DatosFamiliares d WHERE d.numeroPadre = :numeroPadre")
    , @NamedQuery(name = "DatosFamiliares.findByEstadoPadre", query = "SELECT d FROM DatosFamiliares d WHERE d.estadoPadre = :estadoPadre")
    , @NamedQuery(name = "DatosFamiliares.findByMunicipioPadre", query = "SELECT d FROM DatosFamiliares d WHERE d.municipioPadre = :municipioPadre")
    , @NamedQuery(name = "DatosFamiliares.findByAsentamientoPadre", query = "SELECT d FROM DatosFamiliares d WHERE d.asentamientoPadre = :asentamientoPadre")
    , @NamedQuery(name = "DatosFamiliares.findByCalleMadre", query = "SELECT d FROM DatosFamiliares d WHERE d.calleMadre = :calleMadre")
    , @NamedQuery(name = "DatosFamiliares.findByNumeroMadre", query = "SELECT d FROM DatosFamiliares d WHERE d.numeroMadre = :numeroMadre")
    , @NamedQuery(name = "DatosFamiliares.findByEstadoMadre", query = "SELECT d FROM DatosFamiliares d WHERE d.estadoMadre = :estadoMadre")
    , @NamedQuery(name = "DatosFamiliares.findByMunicipioMadre", query = "SELECT d FROM DatosFamiliares d WHERE d.municipioMadre = :municipioMadre")
    , @NamedQuery(name = "DatosFamiliares.findByAsentamientoMadre", query = "SELECT d FROM DatosFamiliares d WHERE d.asentamientoMadre = :asentamientoMadre")})
public class DatosFamiliares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "aspirante")
    private Integer aspirante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 450)
    @Column(name = "nombre_padre")
    private String nombrePadre;
    @Size(max = 20)
    @Column(name = "telefono_padre")
    private String telefonoPadre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 450)
    @Column(name = "nombre_madre")
    private String nombreMadre;
    @Size(max = 20)
    @Column(name = "telefono_madre")
    private String telefonoMadre;
    @Size(max = 150)
    @Column(name = "calle_padre")
    private String callePadre;
    @Size(max = 10)
    @Column(name = "numero_padre")
    private String numeroPadre;
    @Column(name = "estado_padre")
    private Integer estadoPadre;
    @Column(name = "municipio_padre")
    private Integer municipioPadre;
    @Column(name = "asentamiento_padre")
    private Integer asentamientoPadre;
    @Size(max = 150)
    @Column(name = "calle_madre")
    private String calleMadre;
    @Size(max = 10)
    @Column(name = "numero_madre")
    private String numeroMadre;
    @Column(name = "estado_madre")
    private Integer estadoMadre;
    @Column(name = "municipio_madre")
    private Integer municipioMadre;
    @Column(name = "asentamiento_madre")
    private Integer asentamientoMadre;
    @JoinColumn(name = "aspirante", referencedColumnName = "id_aspirante", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Aspirante aspirante1;
    @JoinColumn(name = "escolaridad_madre", referencedColumnName = "id_escolaridad")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Escolaridad escolaridadMadre;
    @JoinColumn(name = "escolaridad_padre", referencedColumnName = "id_escolaridad")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Escolaridad escolaridadPadre;
    @JoinColumn(name = "ocupacion_madre", referencedColumnName = "id_ocupacion")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Ocupacion ocupacionMadre;
    @JoinColumn(name = "ocupacion_padre", referencedColumnName = "id_ocupacion")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Ocupacion ocupacionPadre;
    @JoinColumn(name = "tutor", referencedColumnName = "id_tutor_familiar")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TutorFamiliar tutor;

    public DatosFamiliares() {
    }

    public DatosFamiliares(Integer aspirante) {
        this.aspirante = aspirante;
    }

    public DatosFamiliares(Integer aspirante, String nombrePadre, String nombreMadre) {
        this.aspirante = aspirante;
        this.nombrePadre = nombrePadre;
        this.nombreMadre = nombreMadre;
    }

    public Integer getAspirante() {
        return aspirante;
    }

    public void setAspirante(Integer aspirante) {
        this.aspirante = aspirante;
    }

    public String getNombrePadre() {
        return nombrePadre;
    }

    public void setNombrePadre(String nombrePadre) {
        this.nombrePadre = nombrePadre;
    }

    public String getTelefonoPadre() {
        return telefonoPadre;
    }

    public void setTelefonoPadre(String telefonoPadre) {
        this.telefonoPadre = telefonoPadre;
    }

    public String getNombreMadre() {
        return nombreMadre;
    }

    public void setNombreMadre(String nombreMadre) {
        this.nombreMadre = nombreMadre;
    }

    public String getTelefonoMadre() {
        return telefonoMadre;
    }

    public void setTelefonoMadre(String telefonoMadre) {
        this.telefonoMadre = telefonoMadre;
    }

    public String getCallePadre() {
        return callePadre;
    }

    public void setCallePadre(String callePadre) {
        this.callePadre = callePadre;
    }

    public String getNumeroPadre() {
        return numeroPadre;
    }

    public void setNumeroPadre(String numeroPadre) {
        this.numeroPadre = numeroPadre;
    }

    public Integer getEstadoPadre() {
        return estadoPadre;
    }

    public void setEstadoPadre(Integer estadoPadre) {
        this.estadoPadre = estadoPadre;
    }

    public Integer getMunicipioPadre() {
        return municipioPadre;
    }

    public void setMunicipioPadre(Integer municipioPadre) {
        this.municipioPadre = municipioPadre;
    }

    public Integer getAsentamientoPadre() {
        return asentamientoPadre;
    }

    public void setAsentamientoPadre(Integer asentamientoPadre) {
        this.asentamientoPadre = asentamientoPadre;
    }

    public String getCalleMadre() {
        return calleMadre;
    }

    public void setCalleMadre(String calleMadre) {
        this.calleMadre = calleMadre;
    }

    public String getNumeroMadre() {
        return numeroMadre;
    }

    public void setNumeroMadre(String numeroMadre) {
        this.numeroMadre = numeroMadre;
    }

    public Integer getEstadoMadre() {
        return estadoMadre;
    }

    public void setEstadoMadre(Integer estadoMadre) {
        this.estadoMadre = estadoMadre;
    }

    public Integer getMunicipioMadre() {
        return municipioMadre;
    }

    public void setMunicipioMadre(Integer municipioMadre) {
        this.municipioMadre = municipioMadre;
    }

    public Integer getAsentamientoMadre() {
        return asentamientoMadre;
    }

    public void setAsentamientoMadre(Integer asentamientoMadre) {
        this.asentamientoMadre = asentamientoMadre;
    }

    public Aspirante getAspirante1() {
        return aspirante1;
    }

    public void setAspirante1(Aspirante aspirante1) {
        this.aspirante1 = aspirante1;
    }

    public Escolaridad getEscolaridadMadre() {
        return escolaridadMadre;
    }

    public void setEscolaridadMadre(Escolaridad escolaridadMadre) {
        this.escolaridadMadre = escolaridadMadre;
    }

    public Escolaridad getEscolaridadPadre() {
        return escolaridadPadre;
    }

    public void setEscolaridadPadre(Escolaridad escolaridadPadre) {
        this.escolaridadPadre = escolaridadPadre;
    }

    public Ocupacion getOcupacionMadre() {
        return ocupacionMadre;
    }

    public void setOcupacionMadre(Ocupacion ocupacionMadre) {
        this.ocupacionMadre = ocupacionMadre;
    }

    public Ocupacion getOcupacionPadre() {
        return ocupacionPadre;
    }

    public void setOcupacionPadre(Ocupacion ocupacionPadre) {
        this.ocupacionPadre = ocupacionPadre;
    }

    public TutorFamiliar getTutor() {
        return tutor;
    }

    public void setTutor(TutorFamiliar tutor) {
        this.tutor = tutor;
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
        if (!(object instanceof DatosFamiliares)) {
            return false;
        }
        DatosFamiliares other = (DatosFamiliares) object;
        if ((this.aspirante == null && other.aspirante != null) || (this.aspirante != null && !this.aspirante.equals(other.aspirante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosFamiliares[ aspirante=" + aspirante + " ]";
    }
    
}
