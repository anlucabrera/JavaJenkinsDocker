/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "comentariosprocesopoa", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comentariosprocesopoa.findAll", query = "SELECT c FROM Comentariosprocesopoa c")
    , @NamedQuery(name = "Comentariosprocesopoa.findByComentariosProcesoPoa", query = "SELECT c FROM Comentariosprocesopoa c WHERE c.comentariosProcesoPoa = :comentariosProcesoPoa")
    , @NamedQuery(name = "Comentariosprocesopoa.findByArea", query = "SELECT c FROM Comentariosprocesopoa c WHERE c.area = :area")
    , @NamedQuery(name = "Comentariosprocesopoa.findByStatus", query = "SELECT c FROM Comentariosprocesopoa c WHERE c.status = :status")
    , @NamedQuery(name = "Comentariosprocesopoa.findByProceso", query = "SELECT c FROM Comentariosprocesopoa c WHERE c.proceso = :proceso")
    , @NamedQuery(name = "Comentariosprocesopoa.findByFecha", query = "SELECT c FROM Comentariosprocesopoa c WHERE c.fecha = :fecha")})
public class Comentariosprocesopoa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "comentariosProcesoPoa")
    private Integer comentariosProcesoPoa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;
    @Lob
    @Size(max = 65535)
    @Column(name = "comentarios")
    private String comentarios;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "proceso")
    private String proceso;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "ejercicio_fiscal", referencedColumnName = "ejercicio_fiscal")
    @ManyToOne(optional = false)
    private EjerciciosFiscales ejercicioFiscal;

    public Comentariosprocesopoa() {
    }

    public Comentariosprocesopoa(Integer comentariosProcesoPoa) {
        this.comentariosProcesoPoa = comentariosProcesoPoa;
    }

    public Comentariosprocesopoa(Integer comentariosProcesoPoa, short area, boolean status, String proceso) {
        this.comentariosProcesoPoa = comentariosProcesoPoa;
        this.area = area;
        this.status = status;
        this.proceso = proceso;
    }

    public Integer getComentariosProcesoPoa() {
        return comentariosProcesoPoa;
    }

    public void setComentariosProcesoPoa(Integer comentariosProcesoPoa) {
        this.comentariosProcesoPoa = comentariosProcesoPoa;
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public EjerciciosFiscales getEjercicioFiscal() {
        return ejercicioFiscal;
    }

    public void setEjercicioFiscal(EjerciciosFiscales ejercicioFiscal) {
        this.ejercicioFiscal = ejercicioFiscal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (comentariosProcesoPoa != null ? comentariosProcesoPoa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comentariosprocesopoa)) {
            return false;
        }
        Comentariosprocesopoa other = (Comentariosprocesopoa) object;
        if ((this.comentariosProcesoPoa == null && other.comentariosProcesoPoa != null) || (this.comentariosProcesoPoa != null && !this.comentariosProcesoPoa.equals(other.comentariosProcesoPoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Comentariosprocesopoa[ comentariosProcesoPoa=" + comentariosProcesoPoa + " ]";
    }
    
}
