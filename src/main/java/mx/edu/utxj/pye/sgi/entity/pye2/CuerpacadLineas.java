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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "cuerpacad_lineas", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuerpacadLineas.findAll", query = "SELECT c FROM CuerpacadLineas c")
    , @NamedQuery(name = "CuerpacadLineas.findByRegistro", query = "SELECT c FROM CuerpacadLineas c WHERE c.registro = :registro")
    , @NamedQuery(name = "CuerpacadLineas.findByNombre", query = "SELECT c FROM CuerpacadLineas c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "CuerpacadLineas.findByEstatus", query = "SELECT c FROM CuerpacadLineas c WHERE c.estatus = :estatus")})
public class CuerpacadLineas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;
    @JoinColumn(name = "cuerpo_academico", referencedColumnName = "cuerpo_academico")
    @ManyToOne(optional = false)
    private CuerposAcademicosRegistro cuerpoAcademico;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public CuerpacadLineas() {
    }

    public CuerpacadLineas(Integer registro) {
        this.registro = registro;
    }

    public CuerpacadLineas(Integer registro, String nombre, boolean estatus) {
        this.registro = registro;
        this.nombre = nombre;
        this.estatus = estatus;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public CuerposAcademicosRegistro getCuerpoAcademico() {
        return cuerpoAcademico;
    }

    public void setCuerpoAcademico(CuerposAcademicosRegistro cuerpoAcademico) {
        this.cuerpoAcademico = cuerpoAcademico;
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
        if (!(object instanceof CuerpacadLineas)) {
            return false;
        }
        CuerpacadLineas other = (CuerpacadLineas) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadLineas[ registro=" + registro + " ]";
    }
    
}
