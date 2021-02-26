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
@Table(name = "datos_academicos_complementarios", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatosAcademicosComplementarios.findAll", query = "SELECT d FROM DatosAcademicosComplementarios d")
    , @NamedQuery(name = "DatosAcademicosComplementarios.findByAspirante", query = "SELECT d FROM DatosAcademicosComplementarios d WHERE d.aspirante = :aspirante")
    , @NamedQuery(name = "DatosAcademicosComplementarios.findByPrioridadEstudio", query = "SELECT d FROM DatosAcademicosComplementarios d WHERE d.prioridadEstudio = :prioridadEstudio")
    , @NamedQuery(name = "DatosAcademicosComplementarios.findByOtraAcrrera", query = "SELECT d FROM DatosAcademicosComplementarios d WHERE d.otraAcrrera = :otraAcrrera")
    , @NamedQuery(name = "DatosAcademicosComplementarios.findByEspacioEstudio", query = "SELECT d FROM DatosAcademicosComplementarios d WHERE d.espacioEstudio = :espacioEstudio")
    , @NamedQuery(name = "DatosAcademicosComplementarios.findByEquipoComputo", query = "SELECT d FROM DatosAcademicosComplementarios d WHERE d.equipoComputo = :equipoComputo")
    , @NamedQuery(name = "DatosAcademicosComplementarios.findByInternetCasa", query = "SELECT d FROM DatosAcademicosComplementarios d WHERE d.internetCasa = :internetCasa")
    , @NamedQuery(name = "DatosAcademicosComplementarios.findByInternetRenta", query = "SELECT d FROM DatosAcademicosComplementarios d WHERE d.internetRenta = :internetRenta")})
public class DatosAcademicosComplementarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "aspirante")
    private Integer aspirante;
    @Size(max = 9)
    @Column(name = "prioridad_estudio")
    private String prioridadEstudio;
    @Size(max = 255)
    @Column(name = "otra_acrrera")
    private String otraAcrrera;
    @Column(name = "espacio_estudio")
    private Boolean espacioEstudio;
    @Column(name = "equipo_computo")
    private Boolean equipoComputo;
    @Column(name = "internet_casa")
    private Boolean internetCasa;
    @Column(name = "internet_renta")
    private Boolean internetRenta;
    @JoinColumn(name = "aspirante", referencedColumnName = "id_aspirante", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Aspirante aspirante1;

    public DatosAcademicosComplementarios() {
    }

    public DatosAcademicosComplementarios(Integer aspirante) {
        this.aspirante = aspirante;
    }

    public Integer getAspirante() {
        return aspirante;
    }

    public void setAspirante(Integer aspirante) {
        this.aspirante = aspirante;
    }

    public String getPrioridadEstudio() {
        return prioridadEstudio;
    }

    public void setPrioridadEstudio(String prioridadEstudio) {
        this.prioridadEstudio = prioridadEstudio;
    }

    public String getOtraAcrrera() {
        return otraAcrrera;
    }

    public void setOtraAcrrera(String otraAcrrera) {
        this.otraAcrrera = otraAcrrera;
    }

    public Boolean getEspacioEstudio() {
        return espacioEstudio;
    }

    public void setEspacioEstudio(Boolean espacioEstudio) {
        this.espacioEstudio = espacioEstudio;
    }

    public Boolean getEquipoComputo() {
        return equipoComputo;
    }

    public void setEquipoComputo(Boolean equipoComputo) {
        this.equipoComputo = equipoComputo;
    }

    public Boolean getInternetCasa() {
        return internetCasa;
    }

    public void setInternetCasa(Boolean internetCasa) {
        this.internetCasa = internetCasa;
    }

    public Boolean getInternetRenta() {
        return internetRenta;
    }

    public void setInternetRenta(Boolean internetRenta) {
        this.internetRenta = internetRenta;
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
        if (!(object instanceof DatosAcademicosComplementarios)) {
            return false;
        }
        DatosAcademicosComplementarios other = (DatosAcademicosComplementarios) object;
        if ((this.aspirante == null && other.aspirante != null) || (this.aspirante != null && !this.aspirante.equals(other.aspirante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicosComplementarios[ aspirante=" + aspirante + " ]";
    }
    
}
