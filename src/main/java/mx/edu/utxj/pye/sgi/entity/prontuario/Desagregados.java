/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "desagregados", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Desagregados.findAll", query = "SELECT d FROM Desagregados d")
    , @NamedQuery(name = "Desagregados.findByDesagregado", query = "SELECT d FROM Desagregados d WHERE d.desagregado = :desagregado")
    , @NamedQuery(name = "Desagregados.findByNivel", query = "SELECT d FROM Desagregados d WHERE d.nivel = :nivel")
    , @NamedQuery(name = "Desagregados.findByIndice", query = "SELECT d FROM Desagregados d WHERE d.indice = :indice")})
public class Desagregados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "desagregado")
    private Integer desagregado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nivel")
    private short nivel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "indice")
    private short indice;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "desagregados")
    private DesagregadosProgramas desagregadosProgramas;
    @JoinColumns({@JoinColumn(name = "indicador", referencedColumnName = "indicador"),@JoinColumn(name = "clave", referencedColumnName = "clave")})
    @ManyToOne(optional = false)
    private Indicadores indicador;
    @JoinColumn(name = "tipo", referencedColumnName = "tipo")
    @ManyToOne(optional = false)
    private DesagregadoTipos tipo;

    public Desagregados() {
    }

    public Desagregados(Integer desagregado) {
        this.desagregado = desagregado;
    }

    public Desagregados(Integer desagregado, short nivel, short indice) {
        this.desagregado = desagregado;
        this.nivel = nivel;
        this.indice = indice;
    }

    public Integer getDesagregado() {
        return desagregado;
    }

    public void setDesagregado(Integer desagregado) {
        this.desagregado = desagregado;
    }

    public short getNivel() {
        return nivel;
    }

    public void setNivel(short nivel) {
        this.nivel = nivel;
    }

    public short getIndice() {
        return indice;
    }

    public void setIndice(short indice) {
        this.indice = indice;
    }

    public DesagregadosProgramas getDesagregadosProgramas() {
        return desagregadosProgramas;
    }

    public void setDesagregadosProgramas(DesagregadosProgramas desagregadosProgramas) {
        this.desagregadosProgramas = desagregadosProgramas;
    }

    public Indicadores getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicadores indicador) {
        this.indicador = indicador;
    }

    public DesagregadoTipos getTipo() {
        return tipo;
    }

    public void setTipo(DesagregadoTipos tipo) {
        this.tipo = tipo;
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
        if (!(object instanceof Desagregados)) {
            return false;
        }
        Desagregados other = (Desagregados) object;
        if ((this.desagregado == null && other.desagregado != null) || (this.desagregado != null && !this.desagregado.equals(other.desagregado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Desagregados[ desagregado=" + desagregado + " ]";
    }
    
}
