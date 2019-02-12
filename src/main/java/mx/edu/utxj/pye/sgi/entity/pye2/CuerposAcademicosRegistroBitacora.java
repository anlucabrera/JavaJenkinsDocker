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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "cuerpos_academicos_registro_bitacora", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuerposAcademicosRegistroBitacora.findAll", query = "SELECT c FROM CuerposAcademicosRegistroBitacora c")
    , @NamedQuery(name = "CuerposAcademicosRegistroBitacora.findByBitacora", query = "SELECT c FROM CuerposAcademicosRegistroBitacora c WHERE c.bitacora = :bitacora")
    , @NamedQuery(name = "CuerposAcademicosRegistroBitacora.findByFechaCambio", query = "SELECT c FROM CuerposAcademicosRegistroBitacora c WHERE c.fechaCambio = :fechaCambio")
    , @NamedQuery(name = "CuerposAcademicosRegistroBitacora.findByRegistro", query = "SELECT c FROM CuerposAcademicosRegistroBitacora c WHERE c.registro = :registro")
    , @NamedQuery(name = "CuerposAcademicosRegistroBitacora.findByCuerpoAcademico", query = "SELECT c FROM CuerposAcademicosRegistroBitacora c WHERE c.cuerpoAcademico = :cuerpoAcademico")
    , @NamedQuery(name = "CuerposAcademicosRegistroBitacora.findByFechaInicio", query = "SELECT c FROM CuerposAcademicosRegistroBitacora c WHERE c.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "CuerposAcademicosRegistroBitacora.findByFechaTermino", query = "SELECT c FROM CuerposAcademicosRegistroBitacora c WHERE c.fechaTermino = :fechaTermino")
    , @NamedQuery(name = "CuerposAcademicosRegistroBitacora.findByNombre", query = "SELECT c FROM CuerposAcademicosRegistroBitacora c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "CuerposAcademicosRegistroBitacora.findByNivelProdep", query = "SELECT c FROM CuerposAcademicosRegistroBitacora c WHERE c.nivelProdep = :nivelProdep")
    , @NamedQuery(name = "CuerposAcademicosRegistroBitacora.findByEstatus", query = "SELECT c FROM CuerposAcademicosRegistroBitacora c WHERE c.estatus = :estatus")})
public class CuerposAcademicosRegistroBitacora implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bitacora")
    private Integer bitacora;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_cambio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCambio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private int registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "cuerpo_academico")
    private String cuerpoAcademico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_termino")
    @Temporal(TemporalType.DATE)
    private Date fechaTermino;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "nivel_prodep")
    private String nivelProdep;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;
    @JoinColumn(name = "area_estudio", referencedColumnName = "area_estudio")
    @ManyToOne(optional = false)
    private CuerpacadAreasEstudio areaEstudio;
    @JoinColumn(name = "disciplina", referencedColumnName = "disciplina")
    @ManyToOne(optional = false)
    private CuerpacadDisciplinas disciplina;

    public CuerposAcademicosRegistroBitacora() {
    }

    public CuerposAcademicosRegistroBitacora(Integer bitacora) {
        this.bitacora = bitacora;
    }

    public CuerposAcademicosRegistroBitacora(Integer bitacora, Date fechaCambio, int registro, String cuerpoAcademico, Date fechaInicio, Date fechaTermino, String nombre, String nivelProdep, boolean estatus) {
        this.bitacora = bitacora;
        this.fechaCambio = fechaCambio;
        this.registro = registro;
        this.cuerpoAcademico = cuerpoAcademico;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
        this.nombre = nombre;
        this.nivelProdep = nivelProdep;
        this.estatus = estatus;
    }

    public Integer getBitacora() {
        return bitacora;
    }

    public void setBitacora(Integer bitacora) {
        this.bitacora = bitacora;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public int getRegistro() {
        return registro;
    }

    public void setRegistro(int registro) {
        this.registro = registro;
    }

    public String getCuerpoAcademico() {
        return cuerpoAcademico;
    }

    public void setCuerpoAcademico(String cuerpoAcademico) {
        this.cuerpoAcademico = cuerpoAcademico;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(Date fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNivelProdep() {
        return nivelProdep;
    }

    public void setNivelProdep(String nivelProdep) {
        this.nivelProdep = nivelProdep;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public CuerpacadAreasEstudio getAreaEstudio() {
        return areaEstudio;
    }

    public void setAreaEstudio(CuerpacadAreasEstudio areaEstudio) {
        this.areaEstudio = areaEstudio;
    }

    public CuerpacadDisciplinas getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(CuerpacadDisciplinas disciplina) {
        this.disciplina = disciplina;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bitacora != null ? bitacora.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuerposAcademicosRegistroBitacora)) {
            return false;
        }
        CuerposAcademicosRegistroBitacora other = (CuerposAcademicosRegistroBitacora) object;
        if ((this.bitacora == null && other.bitacora != null) || (this.bitacora != null && !this.bitacora.equals(other.bitacora))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CuerposAcademicosRegistroBitacora[ bitacora=" + bitacora + " ]";
    }
    
}
