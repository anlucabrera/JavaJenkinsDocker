/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "tutoria", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tutoria.findAll", query = "SELECT t FROM Tutoria t")
    , @NamedQuery(name = "Tutoria.findByIdTutoria", query = "SELECT t FROM Tutoria t WHERE t.idTutoria = :idTutoria")
    , @NamedQuery(name = "Tutoria.findByAsunto", query = "SELECT t FROM Tutoria t WHERE t.asunto = :asunto")
    , @NamedQuery(name = "Tutoria.findByFecha", query = "SELECT t FROM Tutoria t WHERE t.fecha = :fecha")
    , @NamedQuery(name = "Tutoria.findByTipo", query = "SELECT t FROM Tutoria t WHERE t.tipo = :tipo")})
public class Tutoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tutoria")
    private Integer idTutoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 350)
    @Column(name = "asunto")
    private String asunto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tipo")
    private String tipo;
    @JoinColumn(name = "grupo", referencedColumnName = "id_grupo")
    @ManyToOne(optional = false)
    private Grupo grupo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "tutoria1")
    private ParticipantesTutoria participantesTutoria;

    public Tutoria() {
    }

    public Tutoria(Integer idTutoria) {
        this.idTutoria = idTutoria;
    }

    public Tutoria(Integer idTutoria, String asunto, Date fecha, String tipo) {
        this.idTutoria = idTutoria;
        this.asunto = asunto;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public Integer getIdTutoria() {
        return idTutoria;
    }

    public void setIdTutoria(Integer idTutoria) {
        this.idTutoria = idTutoria;
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

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public ParticipantesTutoria getParticipantesTutoria() {
        return participantesTutoria;
    }

    public void setParticipantesTutoria(ParticipantesTutoria participantesTutoria) {
        this.participantesTutoria = participantesTutoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTutoria != null ? idTutoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tutoria)) {
            return false;
        }
        Tutoria other = (Tutoria) object;
        if ((this.idTutoria == null && other.idTutoria != null) || (this.idTutoria != null && !this.idTutoria.equals(other.idTutoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Tutoria[ idTutoria=" + idTutoria + " ]";
    }
    
}
