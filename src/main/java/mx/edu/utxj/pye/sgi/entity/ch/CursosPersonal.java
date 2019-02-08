/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jonny
 */
@Entity
@Table(name = "cursos_personal", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CursosPersonal.findAll", query = "SELECT c FROM CursosPersonal c")
    , @NamedQuery(name = "CursosPersonal.findByRegistro", query = "SELECT c FROM CursosPersonal c WHERE c.registro = :registro")})
public class CursosPersonal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "registro")
    private Integer registro;
    @JoinColumn(name = "curso", referencedColumnName = "curso")
    @ManyToOne(optional = false)
    private Cursos curso;
    @JoinColumn(name = "clave", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clave;

    public CursosPersonal() {
    }

    public CursosPersonal(Integer registro) {
        this.registro = registro;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public Cursos getCurso() {
        return curso;
    }

    public void setCurso(Cursos curso) {
        this.curso = curso;
    }

    public Personal getClave() {
        return clave;
    }

    public void setClave(Personal clave) {
        this.clave = clave;
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
        if (!(object instanceof CursosPersonal)) {
            return false;
        }
        CursosPersonal other = (CursosPersonal) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.CursosPersonal[ registro=" + registro + " ]";
    }
    
}
