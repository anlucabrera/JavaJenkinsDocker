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
@Table(name = "ferias_participantes", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FeriasParticipantes.findAll", query = "SELECT f FROM FeriasParticipantes f")
    , @NamedQuery(name = "FeriasParticipantes.findByRegistro", query = "SELECT f FROM FeriasParticipantes f WHERE f.registro = :registro")
    , @NamedQuery(name = "FeriasParticipantes.findByHombres", query = "SELECT f FROM FeriasParticipantes f WHERE f.hombres = :hombres")
    , @NamedQuery(name = "FeriasParticipantes.findByMujeres", query = "SELECT f FROM FeriasParticipantes f WHERE f.mujeres = :mujeres")
    , @NamedQuery(name = "FeriasParticipantes.findByParticipantes", query = "SELECT f FROM FeriasParticipantes f WHERE f.participantes = :participantes")})
public class FeriasParticipantes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hombres")
    private int hombres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mujeres")
    private int mujeres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "participantes")
    private int participantes;
    @JoinColumn(name = "feria", referencedColumnName = "feria")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private FeriasProfesiograficas feria;
    @JoinColumn(name = "iems", referencedColumnName = "iems")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Iems iems;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;

    public FeriasParticipantes() {
    }

    public FeriasParticipantes(Integer registro) {
        this.registro = registro;
    }

    public FeriasParticipantes(Integer registro, int hombres, int mujeres, int participantes) {
        this.registro = registro;
        this.hombres = hombres;
        this.mujeres = mujeres;
        this.participantes = participantes;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getHombres() {
        return hombres;
    }

    public void setHombres(int hombres) {
        this.hombres = hombres;
    }

    public int getMujeres() {
        return mujeres;
    }

    public void setMujeres(int mujeres) {
        this.mujeres = mujeres;
    }
    
    public int getParticipantes() {
        return participantes;
    }

    public void setParticipantes(int participantes) {
        this.participantes = participantes;
    }

    public FeriasProfesiograficas getFeria() {
        return feria;
    }

    public void setFeria(FeriasProfesiograficas feria) {
        this.feria = feria;
    }

    public Iems getIems() {
        return iems;
    }

    public void setIems(Iems iems) {
        this.iems = iems;
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
        if (!(object instanceof FeriasParticipantes)) {
            return false;
        }
        FeriasParticipantes other = (FeriasParticipantes) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.FeriasParticipantes[ registro=" + registro + " ]";
    }
    
}
