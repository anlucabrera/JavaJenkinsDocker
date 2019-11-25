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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
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
    , @NamedQuery(name = "DatosFamiliares.findByTelefonoMadre", query = "SELECT d FROM DatosFamiliares d WHERE d.telefonoMadre = :telefonoMadre")})
public class DatosFamiliares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "aspirante")
    private Integer aspirante;
    @Basic(optional = false)
    @Column(name = "nombre_padre")
    private String nombrePadre;
    @Column(name = "telefono_padre")
    private String telefonoPadre;
    @Basic(optional = false)
    @Column(name = "nombre_madre")
    private String nombreMadre;
    @Column(name = "telefono_madre")
    private String telefonoMadre;
    @JoinColumn(name = "aspirante", referencedColumnName = "id_aspirante", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Aspirante aspirante1;
    @JoinColumn(name = "escolaridad_madre", referencedColumnName = "id_escolaridad")
    @ManyToOne(optional = false)
    private Escolaridad escolaridadMadre;
    @JoinColumn(name = "escolaridad_padre", referencedColumnName = "id_escolaridad")
    @ManyToOne(optional = false)
    private Escolaridad escolaridadPadre;
    @JoinColumn(name = "ocupacion_madre", referencedColumnName = "id_ocupacion")
    @ManyToOne(optional = false)
    private Ocupacion ocupacionMadre;
    @JoinColumn(name = "ocupacion_padre", referencedColumnName = "id_ocupacion")
    @ManyToOne(optional = false)
    private Ocupacion ocupacionPadre;
    @JoinColumn(name = "tutor", referencedColumnName = "id_tutor_familiar")
    @ManyToOne(optional = false)
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
