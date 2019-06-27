/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "baja", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Baja.findAll", query = "SELECT b FROM Baja b")
    , @NamedQuery(name = "Baja.findByIdBajas", query = "SELECT b FROM Baja b WHERE b.idBajas = :idBajas")
    , @NamedQuery(name = "Baja.findByPeriodoEscolar", query = "SELECT b FROM Baja b WHERE b.periodoEscolar = :periodoEscolar")
    , @NamedQuery(name = "Baja.findByEmpleado", query = "SELECT b FROM Baja b WHERE b.empleado = :empleado")
    , @NamedQuery(name = "Baja.findByFechaBaja", query = "SELECT b FROM Baja b WHERE b.fechaBaja = :fechaBaja")})
public class Baja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_bajas")
    private Integer idBajas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo_escolar")
    private int periodoEscolar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "empleado")
    private int empleado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_baja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaBaja;
    @JoinColumn(name = "causa_baja", referencedColumnName = "id_causa_baja")
    @ManyToOne(optional = false)
    private CausaBaja causaBaja;
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false)
    private Inscripcion estudiante;
    @JoinColumn(name = "tipo_baja", referencedColumnName = "id_tipo_baja")
    @ManyToOne(optional = false)
    private TipoBaja tipoBaja;

    public Baja() {
    }

    public Baja(Integer idBajas) {
        this.idBajas = idBajas;
    }

    public Baja(Integer idBajas, int periodoEscolar, int empleado, Date fechaBaja) {
        this.idBajas = idBajas;
        this.periodoEscolar = periodoEscolar;
        this.empleado = empleado;
        this.fechaBaja = fechaBaja;
    }

    public Integer getIdBajas() {
        return idBajas;
    }

    public void setIdBajas(Integer idBajas) {
        this.idBajas = idBajas;
    }

    public int getPeriodoEscolar() {
        return periodoEscolar;
    }

    public void setPeriodoEscolar(int periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    public int getEmpleado() {
        return empleado;
    }

    public void setEmpleado(int empleado) {
        this.empleado = empleado;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public CausaBaja getCausaBaja() {
        return causaBaja;
    }

    public void setCausaBaja(CausaBaja causaBaja) {
        this.causaBaja = causaBaja;
    }

    public Inscripcion getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Inscripcion estudiante) {
        this.estudiante = estudiante;
    }

    public TipoBaja getTipoBaja() {
        return tipoBaja;
    }

    public void setTipoBaja(TipoBaja tipoBaja) {
        this.tipoBaja = tipoBaja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBajas != null ? idBajas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Baja)) {
            return false;
        }
        Baja other = (Baja) object;
        if ((this.idBajas == null && other.idBajas != null) || (this.idBajas != null && !this.idBajas.equals(other.idBajas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Baja[ idBajas=" + idBajas + " ]";
    }
    
}
