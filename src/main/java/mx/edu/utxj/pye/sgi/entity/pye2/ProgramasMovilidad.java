/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "programas_movilidad", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramasMovilidad.findAll", query = "SELECT p FROM ProgramasMovilidad p")
    , @NamedQuery(name = "ProgramasMovilidad.findByPrograma", query = "SELECT p FROM ProgramasMovilidad p WHERE p.programa = :programa")
    , @NamedQuery(name = "ProgramasMovilidad.findByNombre", query = "SELECT p FROM ProgramasMovilidad p WHERE p.nombre = :nombre")})
public class ProgramasMovilidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "programa")
    private Short programa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programaMovilidad", fetch = FetchType.LAZY)
    private List<RegistrosMovilidad> registrosMovilidadList;

    public ProgramasMovilidad() {
    }

    public ProgramasMovilidad(Short programa) {
        this.programa = programa;
    }

    public ProgramasMovilidad(Short programa, String nombre) {
        this.programa = programa;
        this.nombre = nombre;
    }

    public Short getPrograma() {
        return programa;
    }

    public void setPrograma(Short programa) {
        this.programa = programa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<RegistrosMovilidad> getRegistrosMovilidadList() {
        return registrosMovilidadList;
    }

    public void setRegistrosMovilidadList(List<RegistrosMovilidad> registrosMovilidadList) {
        this.registrosMovilidadList = registrosMovilidadList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (programa != null ? programa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramasMovilidad)) {
            return false;
        }
        ProgramasMovilidad other = (ProgramasMovilidad) object;
        if ((this.programa == null && other.programa != null) || (this.programa != null && !this.programa.equals(other.programa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ProgramasMovilidad[ programa=" + programa + " ]";
    }
    
}
