/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "asesoria", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asesoria.findAll", query = "SELECT a FROM Asesoria a")
    , @NamedQuery(name = "Asesoria.findByIdAsesoria", query = "SELECT a FROM Asesoria a WHERE a.idAsesoria = :idAsesoria")
    , @NamedQuery(name = "Asesoria.findByAsunto", query = "SELECT a FROM Asesoria a WHERE a.asunto = :asunto")
    , @NamedQuery(name = "Asesoria.findByFecha", query = "SELECT a FROM Asesoria a WHERE a.fecha = :fecha")
    , @NamedQuery(name = "Asesoria.findByTipo", query = "SELECT a FROM Asesoria a WHERE a.tipo = :tipo")})
public class Asesoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_asesoria")
    private Integer idAsesoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 450)
    @Column(name = "asunto")
    private String asunto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "tipo")
    private String tipo;
    @JoinTable(name = "participantes_asesoria", joinColumns = {
        @JoinColumn(name = "asesoria", referencedColumnName = "id_asesoria")}, inverseJoinColumns = {
        @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")})
    @ManyToMany
    private List<Inscripcion> inscripcionList;
    @JoinColumn(name = "carga", referencedColumnName = "carga")
    @ManyToOne(optional = false)
    private CargaAcademica carga;

    public Asesoria() {
    }

    public Asesoria(Integer idAsesoria) {
        this.idAsesoria = idAsesoria;
    }

    public Asesoria(Integer idAsesoria, String asunto, Date fecha, String tipo) {
        this.idAsesoria = idAsesoria;
        this.asunto = asunto;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public Integer getIdAsesoria() {
        return idAsesoria;
    }

    public void setIdAsesoria(Integer idAsesoria) {
        this.idAsesoria = idAsesoria;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @XmlTransient
    public List<Inscripcion> getInscripcionList() {
        return inscripcionList;
    }

    public void setInscripcionList(List<Inscripcion> inscripcionList) {
        this.inscripcionList = inscripcionList;
    }

    public CargaAcademica getCarga() {
        return carga;
    }

    public void setCarga(CargaAcademica carga) {
        this.carga = carga;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAsesoria != null ? idAsesoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asesoria)) {
            return false;
        }
        Asesoria other = (Asesoria) object;
        if ((this.idAsesoria == null && other.idAsesoria != null) || (this.idAsesoria != null && !this.idAsesoria.equals(other.idAsesoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Asesoria[ idAsesoria=" + idAsesoria + " ]";
    }
    
}
