/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "desagregados_programas", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DesagregadosProgramas.findAll", query = "SELECT d FROM DesagregadosProgramas d")
    , @NamedQuery(name = "DesagregadosProgramas.findByDesagregado", query = "SELECT d FROM DesagregadosProgramas d WHERE d.desagregado = :desagregado")})
public class DesagregadosProgramas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "desagregado")
    private Integer desagregado;
    @JoinColumn(name = "desagregado", referencedColumnName = "desagregado", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Desagregados desagregados;
    @JoinColumn(name = "siglas", referencedColumnName = "siglas")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProgramasEducativos siglas;

    public DesagregadosProgramas() {
    }

    public DesagregadosProgramas(Integer desagregado) {
        this.desagregado = desagregado;
    }

    public Integer getDesagregado() {
        return desagregado;
    }

    public void setDesagregado(Integer desagregado) {
        this.desagregado = desagregado;
    }

    public Desagregados getDesagregados() {
        return desagregados;
    }

    public void setDesagregados(Desagregados desagregados) {
        this.desagregados = desagregados;
    }

    public ProgramasEducativos getSiglas() {
        return siglas;
    }

    public void setSiglas(ProgramasEducativos siglas) {
        this.siglas = siglas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (desagregado != null ? desagregado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DesagregadosProgramas)) {
            return false;
        }
        DesagregadosProgramas other = (DesagregadosProgramas) object;
        if ((this.desagregado == null && other.desagregado != null) || (this.desagregado != null && !this.desagregado.equals(other.desagregado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.DesagregadosProgramas[ desagregado=" + desagregado + " ]";
    }
    
}
