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
import javax.persistence.JoinColumns;
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
@Table(name = "ferias_profesiograficas", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FeriasProfesiograficas.findAll", query = "SELECT f FROM FeriasProfesiograficas f")
    , @NamedQuery(name = "FeriasProfesiograficas.findByRegistro", query = "SELECT f FROM FeriasProfesiograficas f WHERE f.registro = :registro")
    , @NamedQuery(name = "FeriasProfesiograficas.findByFeria", query = "SELECT f FROM FeriasProfesiograficas f WHERE f.feria = :feria")
    , @NamedQuery(name = "FeriasProfesiograficas.findByFecha", query = "SELECT f FROM FeriasProfesiograficas f WHERE f.fecha = :fecha")
    , @NamedQuery(name = "FeriasProfesiograficas.findByEvento", query = "SELECT f FROM FeriasProfesiograficas f WHERE f.evento = :evento")})
public class FeriasProfesiograficas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "feria")
    private String feria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "evento")
    private String evento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "feria")
    private List<FeriasParticipantes> feriasParticipantesList;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumns({
        @JoinColumn(name = "estado", referencedColumnName = "claveEstado")
        , @JoinColumn(name = "municipio", referencedColumnName = "claveMunicipio")
        , @JoinColumn(name = "localidad", referencedColumnName = "claveLocalidad")})
    @ManyToOne(optional = false)
    private Localidad localidad;

    public FeriasProfesiograficas() {
    }

    public FeriasProfesiograficas(Integer registro) {
        this.registro = registro;
    }

    public FeriasProfesiograficas(Integer registro, String feria, Date fecha, String evento) {
        this.registro = registro;
        this.feria = feria;
        this.fecha = fecha;
        this.evento = evento;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getFeria() {
        return feria;
    }

    public void setFeria(String feria) {
        this.feria = feria;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    @XmlTransient
    public List<FeriasParticipantes> getFeriasParticipantesList() {
        return feriasParticipantesList;
    }

    public void setFeriasParticipantesList(List<FeriasParticipantes> feriasParticipantesList) {
        this.feriasParticipantesList = feriasParticipantesList;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
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
        if (!(object instanceof FeriasProfesiograficas)) {
            return false;
        }
        FeriasProfesiograficas other = (FeriasProfesiograficas) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.FeriasProfesiograficas[ registro=" + registro + " ]";
    }
    
}
