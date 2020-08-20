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
import javax.persistence.JoinColumns;
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
@Table(name = "becas_periodos_escolares", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BecasPeriodosEscolares.findAll", query = "SELECT b FROM BecasPeriodosEscolares b")
    , @NamedQuery(name = "BecasPeriodosEscolares.findByRegistro", query = "SELECT b FROM BecasPeriodosEscolares b WHERE b.registro = :registro")
    , @NamedQuery(name = "BecasPeriodosEscolares.findByBeca", query = "SELECT b FROM BecasPeriodosEscolares b WHERE b.beca = :beca")
    , @NamedQuery(name = "BecasPeriodosEscolares.findBySolicitud", query = "SELECT b FROM BecasPeriodosEscolares b WHERE b.solicitud = :solicitud")})
public class BecasPeriodosEscolares implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "beca")
    private short beca;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "solicitud")
    private String solicitud;
    @JoinColumns({
        @JoinColumn(name = "matricula", referencedColumnName = "matricula")
        , @JoinColumn(name = "periodo_asignacion", referencedColumnName = "periodo")})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MatriculaPeriodosEscolares matriculaPeriodosEscolares;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;

    public BecasPeriodosEscolares() {
    }

    public BecasPeriodosEscolares(Integer registro) {
        this.registro = registro;
    }

    public BecasPeriodosEscolares(Integer registro, short beca, String solicitud) {
        this.registro = registro;
        this.beca = beca;
        this.solicitud = solicitud;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public short getBeca() {
        return beca;
    }

    public void setBeca(short beca) {
        this.beca = beca;
    }

    public String getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(String solicitud) {
        this.solicitud = solicitud;
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
        if (!(object instanceof BecasPeriodosEscolares)) {
            return false;
        }
        BecasPeriodosEscolares other = (BecasPeriodosEscolares) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.BecasPeriodosEscolares[ registro=" + registro + " ]";
    }
    
}
