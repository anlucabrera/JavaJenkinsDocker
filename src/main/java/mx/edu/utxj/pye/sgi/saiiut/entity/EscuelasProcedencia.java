/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "escuelas_procedencia", catalog = "saiiut", schema = "SAIIUT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EscuelasProcedencia.findAll", query = "SELECT e FROM EscuelasProcedencia e")
    , @NamedQuery(name = "EscuelasProcedencia.findByCveEscuelaProcedencia", query = "SELECT e FROM EscuelasProcedencia e WHERE e.cveEscuelaProcedencia = :cveEscuelaProcedencia")
    , @NamedQuery(name = "EscuelasProcedencia.findByCveTipoEscuela", query = "SELECT e FROM EscuelasProcedencia e WHERE e.cveTipoEscuela = :cveTipoEscuela")
    , @NamedQuery(name = "EscuelasProcedencia.findByCveEstado", query = "SELECT e FROM EscuelasProcedencia e WHERE e.cveEstado = :cveEstado")
    , @NamedQuery(name = "EscuelasProcedencia.findByCveMunicipio", query = "SELECT e FROM EscuelasProcedencia e WHERE e.cveMunicipio = :cveMunicipio")
    , @NamedQuery(name = "EscuelasProcedencia.findByCveLocalidad", query = "SELECT e FROM EscuelasProcedencia e WHERE e.cveLocalidad = :cveLocalidad")
    , @NamedQuery(name = "EscuelasProcedencia.findByNombre", query = "SELECT e FROM EscuelasProcedencia e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "EscuelasProcedencia.findByComentarios", query = "SELECT e FROM EscuelasProcedencia e WHERE e.comentarios = :comentarios")})
public class EscuelasProcedencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_escuela_procedencia")
    private Integer cveEscuelaProcedencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_tipo_escuela")
    private int cveTipoEscuela;
    @Column(name = "cve_estado")
    private Integer cveEstado;
    @Column(name = "cve_municipio")
    private Integer cveMunicipio;
    @Column(name = "cve_localidad")
    private Integer cveLocalidad;
    @Size(max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 200)
    @Column(name = "comentarios")
    private String comentarios;

    public EscuelasProcedencia() {
    }

    public EscuelasProcedencia(Integer cveEscuelaProcedencia) {
        this.cveEscuelaProcedencia = cveEscuelaProcedencia;
    }

    public EscuelasProcedencia(Integer cveEscuelaProcedencia, int cveTipoEscuela) {
        this.cveEscuelaProcedencia = cveEscuelaProcedencia;
        this.cveTipoEscuela = cveTipoEscuela;
    }

    public Integer getCveEscuelaProcedencia() {
        return cveEscuelaProcedencia;
    }

    public void setCveEscuelaProcedencia(Integer cveEscuelaProcedencia) {
        this.cveEscuelaProcedencia = cveEscuelaProcedencia;
    }

    public int getCveTipoEscuela() {
        return cveTipoEscuela;
    }

    public void setCveTipoEscuela(int cveTipoEscuela) {
        this.cveTipoEscuela = cveTipoEscuela;
    }

    public Integer getCveEstado() {
        return cveEstado;
    }

    public void setCveEstado(Integer cveEstado) {
        this.cveEstado = cveEstado;
    }

    public Integer getCveMunicipio() {
        return cveMunicipio;
    }

    public void setCveMunicipio(Integer cveMunicipio) {
        this.cveMunicipio = cveMunicipio;
    }

    public Integer getCveLocalidad() {
        return cveLocalidad;
    }

    public void setCveLocalidad(Integer cveLocalidad) {
        this.cveLocalidad = cveLocalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveEscuelaProcedencia != null ? cveEscuelaProcedencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EscuelasProcedencia)) {
            return false;
        }
        EscuelasProcedencia other = (EscuelasProcedencia) object;
        if ((this.cveEscuelaProcedencia == null && other.cveEscuelaProcedencia != null) || (this.cveEscuelaProcedencia != null && !this.cveEscuelaProcedencia.equals(other.cveEscuelaProcedencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.EscuelasProcedencia[ cveEscuelaProcedencia=" + cveEscuelaProcedencia + " ]";
    }
    
}
