/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "categorias_habilidades", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CategoriasHabilidades.findAll", query = "SELECT c FROM CategoriasHabilidades c")
    , @NamedQuery(name = "CategoriasHabilidades.findByCategoria", query = "SELECT c FROM CategoriasHabilidades c WHERE c.categoriasHabilidadesPK.categoria = :categoria")
    , @NamedQuery(name = "CategoriasHabilidades.findByHabilidad", query = "SELECT c FROM CategoriasHabilidades c WHERE c.categoriasHabilidadesPK.habilidad = :habilidad")
    , @NamedQuery(name = "CategoriasHabilidades.findByEvaluacion", query = "SELECT c FROM CategoriasHabilidades c WHERE c.categoriasHabilidadesPK.evaluacion = :evaluacion")})
public class CategoriasHabilidades implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CategoriasHabilidadesPK categoriasHabilidadesPK;
    @JoinColumn(name = "categoria", referencedColumnName = "categoria", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PersonalCategorias personalCategorias;
    @JoinColumn(name = "habilidad", referencedColumnName = "habilidad", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Habilidades habilidades;
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Evaluaciones360 evaluaciones360;

    public CategoriasHabilidades() {
    }

    public CategoriasHabilidades(CategoriasHabilidadesPK categoriasHabilidadesPK) {
        this.categoriasHabilidadesPK = categoriasHabilidadesPK;
    }

    public CategoriasHabilidades(short categoria, int habilidad, int evaluacion) {
        this.categoriasHabilidadesPK = new CategoriasHabilidadesPK(categoria, habilidad, evaluacion);
    }

    public CategoriasHabilidadesPK getCategoriasHabilidadesPK() {
        return categoriasHabilidadesPK;
    }

    public void setCategoriasHabilidadesPK(CategoriasHabilidadesPK categoriasHabilidadesPK) {
        this.categoriasHabilidadesPK = categoriasHabilidadesPK;
    }

    public PersonalCategorias getPersonalCategorias() {
        return personalCategorias;
    }

    public void setPersonalCategorias(PersonalCategorias personalCategorias) {
        this.personalCategorias = personalCategorias;
    }

    public Habilidades getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(Habilidades habilidades) {
        this.habilidades = habilidades;
    }

    public Evaluaciones360 getEvaluaciones360() {
        return evaluaciones360;
    }

    public void setEvaluaciones360(Evaluaciones360 evaluaciones360) {
        this.evaluaciones360 = evaluaciones360;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoriasHabilidadesPK != null ? categoriasHabilidadesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoriasHabilidades)) {
            return false;
        }
        CategoriasHabilidades other = (CategoriasHabilidades) object;
        if ((this.categoriasHabilidadesPK == null && other.categoriasHabilidadesPK != null) || (this.categoriasHabilidadesPK != null && !this.categoriasHabilidadesPK.equals(other.categoriasHabilidadesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.CategoriasHabilidades[ categoriasHabilidadesPK=" + categoriasHabilidadesPK + " ]";
    }
    
}
