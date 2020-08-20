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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "bolsa_trabajo_entrevistas", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BolsaTrabajoEntrevistas.findAll", query = "SELECT b FROM BolsaTrabajoEntrevistas b")
    , @NamedQuery(name = "BolsaTrabajoEntrevistas.findByRegistro", query = "SELECT b FROM BolsaTrabajoEntrevistas b WHERE b.registro = :registro")
    , @NamedQuery(name = "BolsaTrabajoEntrevistas.findByGeneracion", query = "SELECT b FROM BolsaTrabajoEntrevistas b WHERE b.generacion = :generacion")
    , @NamedQuery(name = "BolsaTrabajoEntrevistas.findByMatricula", query = "SELECT b FROM BolsaTrabajoEntrevistas b WHERE b.matricula = :matricula")
    , @NamedQuery(name = "BolsaTrabajoEntrevistas.findByProgramaEducativo", query = "SELECT b FROM BolsaTrabajoEntrevistas b WHERE b.programaEducativo = :programaEducativo")
    , @NamedQuery(name = "BolsaTrabajoEntrevistas.findByContratado", query = "SELECT b FROM BolsaTrabajoEntrevistas b WHERE b.contratado = :contratado")
    , @NamedQuery(name = "BolsaTrabajoEntrevistas.findByObservaciones", query = "SELECT b FROM BolsaTrabajoEntrevistas b WHERE b.observaciones = :observaciones")})
public class BolsaTrabajoEntrevistas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "matricula")
    private String matricula;
    @Basic(optional = false)
    @NotNull
    @Column(name = "programa_educativo")
    private short programaEducativo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "contratado")
    private String contratado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "observaciones")
    private String observaciones;
    @JoinColumn(name = "bolsatrabent", referencedColumnName = "bolsatrab")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BolsaTrabajo bolsatrabent;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;

    public BolsaTrabajoEntrevistas() {
    }

    public BolsaTrabajoEntrevistas(Integer registro) {
        this.registro = registro;
    }

    public BolsaTrabajoEntrevistas(Integer registro, short generacion, String matricula, short programaEducativo, String contratado, String observaciones) {
        this.registro = registro;
        this.generacion = generacion;
        this.matricula = matricula;
        this.programaEducativo = programaEducativo;
        this.contratado = contratado;
        this.observaciones = observaciones;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public short getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(short programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public String getContratado() {
        return contratado;
    }

    public void setContratado(String contratado) {
        this.contratado = contratado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BolsaTrabajo getBolsatrabent() {
        return bolsatrabent;
    }

    public void setBolsatrabent(BolsaTrabajo bolsatrabent) {
        this.bolsatrabent = bolsatrabent;
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
        if (!(object instanceof BolsaTrabajoEntrevistas)) {
            return false;
        }
        BolsaTrabajoEntrevistas other = (BolsaTrabajoEntrevistas) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajoEntrevistas[ registro=" + registro + " ]";
    }
    
}
