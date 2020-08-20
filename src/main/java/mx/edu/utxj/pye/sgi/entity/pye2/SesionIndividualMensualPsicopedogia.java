/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

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
 * @author UTXJ
 */
@Entity
@Table(name = "sesion_individual_mensual_psicopedogia", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SesionIndividualMensualPsicopedogia.findAll", query = "SELECT s FROM SesionIndividualMensualPsicopedogia s")
    , @NamedQuery(name = "SesionIndividualMensualPsicopedogia.findByRegistro", query = "SELECT s FROM SesionIndividualMensualPsicopedogia s WHERE s.registro = :registro")
    , @NamedQuery(name = "SesionIndividualMensualPsicopedogia.findByProgramaEducativo", query = "SELECT s FROM SesionIndividualMensualPsicopedogia s WHERE s.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "SesionIndividualMensualPsicopedogia.findByHombres", query = "SELECT s FROM SesionIndividualMensualPsicopedogia s WHERE s.hombres = :hombres")
    , @NamedQuery(name = "SesionIndividualMensualPsicopedogia.findByMujeres", query = "SELECT s FROM SesionIndividualMensualPsicopedogia s WHERE s.mujeres = :mujeres")
    , @NamedQuery(name = "SesionIndividualMensualPsicopedogia.findByMes", query = "SELECT s FROM SesionIndividualMensualPsicopedogia s WHERE s.mes = :mes")})
public class SesionIndividualMensualPsicopedogia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Column(name = "programa_educativo")
    private Short programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hombres")
    private short hombres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mujeres")
    private short mujeres;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "mes")
    private String mes;
    @JoinColumn(name = "area_conflicto", referencedColumnName = "area_conflicto")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AreasConflicto areaConflicto;
    @JoinColumn(name = "otro_tipo_sesion", referencedColumnName = "otro_tipo_sesion_psicopedagogia")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private OtrosTiposSesionesPsicopedagogia otroTipoSesion;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;

    public SesionIndividualMensualPsicopedogia() {
    }

    public SesionIndividualMensualPsicopedogia(Integer registro) {
        this.registro = registro;
    }

    public SesionIndividualMensualPsicopedogia(Integer registro, short hombres, short mujeres, String mes) {
        this.registro = registro;
        this.hombres = hombres;
        this.mujeres = mujeres;
        this.mes = mes;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public Short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(Short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public short getHombres() {
        return hombres;
    }

    public void setHombres(short hombres) {
        this.hombres = hombres;
    }

    public short getMujeres() {
        return mujeres;
    }

    public void setMujeres(short mujeres) {
        this.mujeres = mujeres;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public AreasConflicto getAreaConflicto() {
        return areaConflicto;
    }

    public void setAreaConflicto(AreasConflicto areaConflicto) {
        this.areaConflicto = areaConflicto;
    }

    public OtrosTiposSesionesPsicopedagogia getOtroTipoSesion() {
        return otroTipoSesion;
    }

    public void setOtroTipoSesion(OtrosTiposSesionesPsicopedagogia otroTipoSesion) {
        this.otroTipoSesion = otroTipoSesion;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registro != null ? registro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SesionIndividualMensualPsicopedogia)) {
            return false;
        }
        SesionIndividualMensualPsicopedogia other = (SesionIndividualMensualPsicopedogia) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.SesionIndividualMensualPsicopedogia[ registro=" + registro + " ]";
    }
    
}
