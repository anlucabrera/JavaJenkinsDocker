/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "procesos_generaciones", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProcesosGeneraciones.findAll", query = "SELECT p FROM ProcesosGeneraciones p")
    , @NamedQuery(name = "ProcesosGeneraciones.findByProceso", query = "SELECT p FROM ProcesosGeneraciones p WHERE p.procesosGeneracionesPK.proceso = :proceso")
    , @NamedQuery(name = "ProcesosGeneraciones.findByGeneracion", query = "SELECT p FROM ProcesosGeneraciones p WHERE p.procesosGeneracionesPK.generacion = :generacion")
    , @NamedQuery(name = "ProcesosGeneraciones.findByPeriodo", query = "SELECT p FROM ProcesosGeneraciones p WHERE p.procesosGeneracionesPK.periodo = :periodo")
    , @NamedQuery(name = "ProcesosGeneraciones.findByGrado", query = "SELECT p FROM ProcesosGeneraciones p WHERE p.procesosGeneracionesPK.grado = :grado")})
public class ProcesosGeneraciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProcesosGeneracionesPK procesosGeneracionesPK;

    public ProcesosGeneraciones() {
    }

    public ProcesosGeneraciones(ProcesosGeneracionesPK procesosGeneracionesPK) {
        this.procesosGeneracionesPK = procesosGeneracionesPK;
    }

    public ProcesosGeneraciones(int proceso, short generacion, int periodo, short grado) {
        this.procesosGeneracionesPK = new ProcesosGeneracionesPK(proceso, generacion, periodo, grado);
    }

    public ProcesosGeneracionesPK getProcesosGeneracionesPK() {
        return procesosGeneracionesPK;
    }

    public void setProcesosGeneracionesPK(ProcesosGeneracionesPK procesosGeneracionesPK) {
        this.procesosGeneracionesPK = procesosGeneracionesPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (procesosGeneracionesPK != null ? procesosGeneracionesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProcesosGeneraciones)) {
            return false;
        }
        ProcesosGeneraciones other = (ProcesosGeneraciones) object;
        if ((this.procesosGeneracionesPK == null && other.procesosGeneracionesPK != null) || (this.procesosGeneracionesPK != null && !this.procesosGeneracionesPK.equals(other.procesosGeneracionesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosGeneraciones[ procesosGeneracionesPK=" + procesosGeneracionesPK + " ]";
    }
    
}
