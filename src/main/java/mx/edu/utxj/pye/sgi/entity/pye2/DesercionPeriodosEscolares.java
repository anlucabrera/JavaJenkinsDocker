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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "desercion_periodos_escolares", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DesercionPeriodosEscolares.findAll", query = "SELECT d FROM DesercionPeriodosEscolares d")
    , @NamedQuery(name = "DesercionPeriodosEscolares.findByRegistro", query = "SELECT d FROM DesercionPeriodosEscolares d WHERE d.registro = :registro")
    , @NamedQuery(name = "DesercionPeriodosEscolares.findByDpe", query = "SELECT d FROM DesercionPeriodosEscolares d WHERE d.dpe = :dpe")
    , @NamedQuery(name = "DesercionPeriodosEscolares.findByGeneracion", query = "SELECT d FROM DesercionPeriodosEscolares d WHERE d.generacion = :generacion")
    , @NamedQuery(name = "DesercionPeriodosEscolares.findByCausaBaja", query = "SELECT d FROM DesercionPeriodosEscolares d WHERE d.causaBaja = :causaBaja")
    , @NamedQuery(name = "DesercionPeriodosEscolares.findByTipoBaja", query = "SELECT d FROM DesercionPeriodosEscolares d WHERE d.tipoBaja = :tipoBaja")})
public class DesercionPeriodosEscolares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "dpe")
    private String dpe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "causa_baja")
    private int causaBaja;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_baja")
    private int tipoBaja;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dpe", fetch = FetchType.LAZY)
    private List<DesercionReprobacionMaterias> desercionReprobacionMateriasList;
    @JoinColumns({
        @JoinColumn(name = "matricula", referencedColumnName = "matricula")
        , @JoinColumn(name = "periodo_escolar", referencedColumnName = "periodo")})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MatriculaPeriodosEscolares matriculaPeriodosEscolares;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;

    public DesercionPeriodosEscolares() {
    }

    public DesercionPeriodosEscolares(Integer registro) {
        this.registro = registro;
    }

    public DesercionPeriodosEscolares(Integer registro, String dpe, short generacion, int causaBaja, int tipoBaja) {
        this.registro = registro;
        this.dpe = dpe;
        this.generacion = generacion;
        this.causaBaja = causaBaja;
        this.tipoBaja = tipoBaja;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getDpe() {
        return dpe;
    }

    public void setDpe(String dpe) {
        this.dpe = dpe;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    public int getCausaBaja() {
        return causaBaja;
    }

    public void setCausaBaja(int causaBaja) {
        this.causaBaja = causaBaja;
    }

    public int getTipoBaja() {
        return tipoBaja;
    }

    public void setTipoBaja(int tipoBaja) {
        this.tipoBaja = tipoBaja;
    }

    @XmlTransient
    public List<DesercionReprobacionMaterias> getDesercionReprobacionMateriasList() {
        return desercionReprobacionMateriasList;
    }

    public void setDesercionReprobacionMateriasList(List<DesercionReprobacionMaterias> desercionReprobacionMateriasList) {
        this.desercionReprobacionMateriasList = desercionReprobacionMateriasList;
    }

    public MatriculaPeriodosEscolares getMatriculaPeriodosEscolares() {
        return matriculaPeriodosEscolares;
    }

    public void setMatriculaPeriodosEscolares(MatriculaPeriodosEscolares matriculaPeriodosEscolares) {
        this.matriculaPeriodosEscolares = matriculaPeriodosEscolares;
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
        if (!(object instanceof DesercionPeriodosEscolares)) {
            return false;
        }
        DesercionPeriodosEscolares other = (DesercionPeriodosEscolares) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.DesercionPeriodosEscolares[ registro=" + registro + " ]";
    }
    
}
