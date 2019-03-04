/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "comisiones_academicas", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComisionesAcademicas.findAll", query = "SELECT c FROM ComisionesAcademicas c")
    , @NamedQuery(name = "ComisionesAcademicas.findByRegistro", query = "SELECT c FROM ComisionesAcademicas c WHERE c.registro = :registro")
    , @NamedQuery(name = "ComisionesAcademicas.findByComisionAcademica", query = "SELECT c FROM ComisionesAcademicas c WHERE c.comisionAcademica = :comisionAcademica")
    , @NamedQuery(name = "ComisionesAcademicas.findByFechaInicio", query = "SELECT c FROM ComisionesAcademicas c WHERE c.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ComisionesAcademicas.findByFechaFin", query = "SELECT c FROM ComisionesAcademicas c WHERE c.fechaFin = :fechaFin")
    , @NamedQuery(name = "ComisionesAcademicas.findByLugar", query = "SELECT c FROM ComisionesAcademicas c WHERE c.lugar = :lugar")
    , @NamedQuery(name = "ComisionesAcademicas.findByObjetivo", query = "SELECT c FROM ComisionesAcademicas c WHERE c.objetivo = :objetivo")
    , @NamedQuery(name = "ComisionesAcademicas.findByAcuerdos", query = "SELECT c FROM ComisionesAcademicas c WHERE c.acuerdos = :acuerdos")})
public class ComisionesAcademicas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "comision_academica")
    private String comisionAcademica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "lugar")
    private String lugar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "objetivo")
    private String objetivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "acuerdos")
    private String acuerdos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comisionAcademica")
    private List<ComisionesAcademicasParticipantes> comisionesAcademicasParticipantesList;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumn(name = "tipo_comision", referencedColumnName = "tipo_comision")
    @ManyToOne(optional = false)
    private ComisionesAcademicasTipos tipoComision;

    public ComisionesAcademicas() {
    }

    public ComisionesAcademicas(Integer registro) {
        this.registro = registro;
    }

    public ComisionesAcademicas(Integer registro, String comisionAcademica, Date fechaInicio, Date fechaFin, String lugar, String objetivo, String acuerdos) {
        this.registro = registro;
        this.comisionAcademica = comisionAcademica;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.lugar = lugar;
        this.objetivo = objetivo;
        this.acuerdos = acuerdos;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getComisionAcademica() {
        return comisionAcademica;
    }

    public void setComisionAcademica(String comisionAcademica) {
        this.comisionAcademica = comisionAcademica;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getAcuerdos() {
        return acuerdos;
    }

    public void setAcuerdos(String acuerdos) {
        this.acuerdos = acuerdos;
    }

    @XmlTransient
    public List<ComisionesAcademicasParticipantes> getComisionesAcademicasParticipantesList() {
        return comisionesAcademicasParticipantesList;
    }

    public void setComisionesAcademicasParticipantesList(List<ComisionesAcademicasParticipantes> comisionesAcademicasParticipantesList) {
        this.comisionesAcademicasParticipantesList = comisionesAcademicasParticipantesList;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public ComisionesAcademicasTipos getTipoComision() {
        return tipoComision;
    }

    public void setTipoComision(ComisionesAcademicasTipos tipoComision) {
        this.tipoComision = tipoComision;
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
        if (!(object instanceof ComisionesAcademicas)) {
            return false;
        }
        ComisionesAcademicas other = (ComisionesAcademicas) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicas[ registro=" + registro + " ]";
    }
    
}
