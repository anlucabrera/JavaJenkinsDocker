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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "bolsa_trabajo_contratados", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BolsaTrabajoContratados.findAll", query = "SELECT b FROM BolsaTrabajoContratados b")
    , @NamedQuery(name = "BolsaTrabajoContratados.findByCveContratado", query = "SELECT b FROM BolsaTrabajoContratados b WHERE b.cveContratado = :cveContratado")
    , @NamedQuery(name = "BolsaTrabajoContratados.findByNombreAlumno", query = "SELECT b FROM BolsaTrabajoContratados b WHERE b.nombreAlumno = :nombreAlumno")
    , @NamedQuery(name = "BolsaTrabajoContratados.findByContratados", query = "SELECT b FROM BolsaTrabajoContratados b WHERE b.contratados = :contratados")
    , @NamedQuery(name = "BolsaTrabajoContratados.findByObservaciones", query = "SELECT b FROM BolsaTrabajoContratados b WHERE b.observaciones = :observaciones")})
public class BolsaTrabajoContratados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_contratado")
    private Integer cveContratado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre_alumno")
    private String nombreAlumno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "contratados")
    private int contratados;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "observaciones")
    private String observaciones;
    @JoinColumn(name = "generacion", referencedColumnName = "generacion")
    @ManyToOne(optional = false)
    private Generaciones generacion;
    @JoinColumn(name = "id_plaza", referencedColumnName = "id_plaza")
    @ManyToOne(optional = false)
    private BolsaTrabajoPlazas idPlaza;
    @JoinColumn(name = "programa_educativo", referencedColumnName = "siglas")
    @ManyToOne(optional = false)
    private ProgramasEducativos programaEducativo;

    public BolsaTrabajoContratados() {
    }

    public BolsaTrabajoContratados(Integer cveContratado) {
        this.cveContratado = cveContratado;
    }

    public BolsaTrabajoContratados(Integer cveContratado, String nombreAlumno, int contratados, String observaciones) {
        this.cveContratado = cveContratado;
        this.nombreAlumno = nombreAlumno;
        this.contratados = contratados;
        this.observaciones = observaciones;
    }

    public Integer getCveContratado() {
        return cveContratado;
    }

    public void setCveContratado(Integer cveContratado) {
        this.cveContratado = cveContratado;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public int getContratados() {
        return contratados;
    }

    public void setContratados(int contratados) {
        this.contratados = contratados;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Generaciones getGeneracion() {
        return generacion;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public BolsaTrabajoPlazas getIdPlaza() {
        return idPlaza;
    }

    public void setIdPlaza(BolsaTrabajoPlazas idPlaza) {
        this.idPlaza = idPlaza;
    }

    public ProgramasEducativos getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(ProgramasEducativos programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveContratado != null ? cveContratado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BolsaTrabajoContratados)) {
            return false;
        }
        BolsaTrabajoContratados other = (BolsaTrabajoContratados) object;
        if ((this.cveContratado == null && other.cveContratado != null) || (this.cveContratado != null && !this.cveContratado.equals(other.cveContratado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.BolsaTrabajoContratados[ cveContratado=" + cveContratado + " ]";
    }
    
}
