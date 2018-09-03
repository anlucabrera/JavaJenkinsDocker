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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "participantes_personal_capacitado", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParticipantesPersonalCapacitado.findAll", query = "SELECT p FROM ParticipantesPersonalCapacitado p")
    , @NamedQuery(name = "ParticipantesPersonalCapacitado.findByRegistro", query = "SELECT p FROM ParticipantesPersonalCapacitado p WHERE p.registro = :registro")
    , @NamedQuery(name = "ParticipantesPersonalCapacitado.findByPersonal", query = "SELECT p FROM ParticipantesPersonalCapacitado p WHERE p.personal = :personal")})
public class ParticipantesPersonalCapacitado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "personal")
    private int personal;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;
    @JoinColumn(name = "percap", referencedColumnName = "curso")
    @ManyToOne(optional = false)
    private PersonalCapacitado percap;

    public ParticipantesPersonalCapacitado() {
    }

    public ParticipantesPersonalCapacitado(Integer registro) {
        this.registro = registro;
    }

    public ParticipantesPersonalCapacitado(Integer registro, int personal) {
        this.registro = registro;
        this.personal = personal;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getPersonal() {
        return personal;
    }

    public void setPersonal(int personal) {
        this.personal = personal;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public PersonalCapacitado getPercap() {
        return percap;
    }

    public void setPercap(PersonalCapacitado percap) {
        this.percap = percap;
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
        if (!(object instanceof ParticipantesPersonalCapacitado)) {
            return false;
        }
        ParticipantesPersonalCapacitado other = (ParticipantesPersonalCapacitado) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ParticipantesPersonalCapacitado[ registro=" + registro + " ]";
    }
    
}
