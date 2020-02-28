/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

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
@Table(name = "comentariosfunciones", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comentariosfunciones.findAll", query = "SELECT c FROM Comentariosfunciones c")
    , @NamedQuery(name = "Comentariosfunciones.findByIdComentario", query = "SELECT c FROM Comentariosfunciones c WHERE c.idComentario = :idComentario")
    , @NamedQuery(name = "Comentariosfunciones.findByComentario", query = "SELECT c FROM Comentariosfunciones c WHERE c.comentario = :comentario")
    , @NamedQuery(name = "Comentariosfunciones.findByEsatus", query = "SELECT c FROM Comentariosfunciones c WHERE c.esatus = :esatus")
    , @NamedQuery(name = "Comentariosfunciones.findByFechaHoraC", query = "SELECT c FROM Comentariosfunciones c WHERE c.fechaHoraC = :fechaHoraC")})
public class Comentariosfunciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_comentario")
    private Integer idComentario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "comentario")
    private String comentario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "esatus")
    private int esatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaHoraC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraC;
    @JoinColumn(name = "id_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal idPersonal;
    @JoinColumn(name = "id_Funcion", referencedColumnName = "funcion")
    @ManyToOne(optional = false)
    private Funciones idFuncion;

    public Comentariosfunciones() {
    }

    public Comentariosfunciones(Integer idComentario) {
        this.idComentario = idComentario;
    }

    public Comentariosfunciones(Integer idComentario, String comentario, int esatus, Date fechaHoraC) {
        this.idComentario = idComentario;
        this.comentario = comentario;
        this.esatus = esatus;
        this.fechaHoraC = fechaHoraC;
    }

    public Integer getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Integer idComentario) {
        this.idComentario = idComentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getEsatus() {
        return esatus;
    }

    public void setEsatus(int esatus) {
        this.esatus = esatus;
    }

    public Date getFechaHoraC() {
        return fechaHoraC;
    }

    public void setFechaHoraC(Date fechaHoraC) {
        this.fechaHoraC = fechaHoraC;
    }

    public Personal getIdPersonal() {
        return idPersonal;
    }

    public void setIdPersonal(Personal idPersonal) {
        this.idPersonal = idPersonal;
    }

    public Funciones getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(Funciones idFuncion) {
        this.idFuncion = idFuncion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idComentario != null ? idComentario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comentariosfunciones)) {
            return false;
        }
        Comentariosfunciones other = (Comentariosfunciones) object;
        if ((this.idComentario == null && other.idComentario != null) || (this.idComentario != null && !this.idComentario.equals(other.idComentario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Comentariosfunciones[ idComentario=" + idComentario + " ]";
    }
    
}
