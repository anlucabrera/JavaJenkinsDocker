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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "caso_critico", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CasoCritico.findAll", query = "SELECT c FROM CasoCritico c")
    , @NamedQuery(name = "CasoCritico.findByCaso", query = "SELECT c FROM CasoCritico c WHERE c.caso = :caso")
    , @NamedQuery(name = "CasoCritico.findByTipo", query = "SELECT c FROM CasoCritico c WHERE c.tipo = :tipo")
    , @NamedQuery(name = "CasoCritico.findByDescripcion", query = "SELECT c FROM CasoCritico c WHERE c.descripcion = :descripcion")})
public class CasoCritico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "caso")
    private Integer caso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 72)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1500)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false)
    private Estudiante idEstudiante;

    public CasoCritico() {
    }

    public CasoCritico(Integer caso) {
        this.caso = caso;
    }

    public CasoCritico(Integer caso, String tipo, String descripcion) {
        this.caso = caso;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public Integer getCaso() {
        return caso;
    }

    public void setCaso(Integer caso) {
        this.caso = caso;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estudiante getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Estudiante idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (caso != null ? caso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CasoCritico)) {
            return false;
        }
        CasoCritico other = (CasoCritico) object;
        if ((this.caso == null && other.caso != null) || (this.caso != null && !this.caso.equals(other.caso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CasoCritico[ caso=" + caso + " ]";
    }
    
}
