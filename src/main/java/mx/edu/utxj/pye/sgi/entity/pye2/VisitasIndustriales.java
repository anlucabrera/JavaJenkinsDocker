/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "visitas_industriales", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VisitasIndustriales.findAll", query = "SELECT v FROM VisitasIndustriales v")
    , @NamedQuery(name = "VisitasIndustriales.findByRegistro", query = "SELECT v FROM VisitasIndustriales v WHERE v.registro = :registro")
    , @NamedQuery(name = "VisitasIndustriales.findByCicloEscolar", query = "SELECT v FROM VisitasIndustriales v WHERE v.cicloEscolar = :cicloEscolar")
    , @NamedQuery(name = "VisitasIndustriales.findByPeriodoEscolar", query = "SELECT v FROM VisitasIndustriales v WHERE v.periodoEscolar = :periodoEscolar")
    , @NamedQuery(name = "VisitasIndustriales.findByFecha", query = "SELECT v FROM VisitasIndustriales v WHERE v.fecha = :fecha")
    , @NamedQuery(name = "VisitasIndustriales.findByProgramaEducativo", query = "SELECT v FROM VisitasIndustriales v WHERE v.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "VisitasIndustriales.findByCuatrimestre", query = "SELECT v FROM VisitasIndustriales v WHERE v.cuatrimestre = :cuatrimestre")
    , @NamedQuery(name = "VisitasIndustriales.findByGrupo", query = "SELECT v FROM VisitasIndustriales v WHERE v.grupo = :grupo")
    , @NamedQuery(name = "VisitasIndustriales.findBySistema", query = "SELECT v FROM VisitasIndustriales v WHERE v.sistema = :sistema")
    , @NamedQuery(name = "VisitasIndustriales.findByObjetivo", query = "SELECT v FROM VisitasIndustriales v WHERE v.objetivo = :objetivo")
    , @NamedQuery(name = "VisitasIndustriales.findByHombres", query = "SELECT v FROM VisitasIndustriales v WHERE v.hombres = :hombres")
    , @NamedQuery(name = "VisitasIndustriales.findByMujeres", query = "SELECT v FROM VisitasIndustriales v WHERE v.mujeres = :mujeres")})
public class VisitasIndustriales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo_escolar")
    private int cicloEscolar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo_escolar")
    private int periodoEscolar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuatrimestre")
    private int cuatrimestre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grupo")
    private Character grupo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "sistema")
    private String sistema;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "objetivo")
    private String objetivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hombres")
    private int hombres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mujeres")
    private int mujeres;
    @JoinColumn(name = "empresa", referencedColumnName = "empresa")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private OrganismosVinculados empresa;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;

    public VisitasIndustriales() {
    }

    public VisitasIndustriales(Integer registro) {
        this.registro = registro;
    }

    public VisitasIndustriales(Integer registro, int cicloEscolar, int periodoEscolar, Date fecha, short programaEducativo, int cuatrimestre, Character grupo, String sistema, String objetivo, int hombres, int mujeres) {
        this.registro = registro;
        this.cicloEscolar = cicloEscolar;
        this.periodoEscolar = periodoEscolar;
        this.fecha = fecha;
        this.programaEducativo = programaEducativo;
        this.cuatrimestre = cuatrimestre;
        this.grupo = grupo;
        this.sistema = sistema;
        this.objetivo = objetivo;
        this.hombres = hombres;
        this.mujeres = mujeres;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(int cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }

    public int getPeriodoEscolar() {
        return periodoEscolar;
    }

    public void setPeriodoEscolar(int periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public Character getGrupo() {
        return grupo;
    }

    public void setGrupo(Character grupo) {
        this.grupo = grupo;
    }

    public String getSistema() {
        return sistema;
    }

    public void setSistema(String sistema) {
        this.sistema = sistema;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public int getHombres() {
        return hombres;
    }

    public void setHombres(int hombres) {
        this.hombres = hombres;
    }

    public int getMujeres() {
        return mujeres;
    }

    public void setMujeres(int mujeres) {
        this.mujeres = mujeres;
    }

    public OrganismosVinculados getEmpresa() {
        return empresa;
    }

    public void setEmpresa(OrganismosVinculados empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof VisitasIndustriales)) {
            return false;
        }
        VisitasIndustriales other = (VisitasIndustriales) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.VisitasIndustriales[ registro=" + registro + " ]";
    }
    
}
