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
@Table(name = "materias", catalog = "saiiut", schema = "SAIIUT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Materias1.findAll", query = "SELECT m FROM Materias m")
    , @NamedQuery(name = "Materias1.findByCveMateria", query = "SELECT m FROM Materias m WHERE m.cveMateria = :cveMateria")
    , @NamedQuery(name = "Materias1.findByNombre", query = "SELECT m FROM Materias m WHERE m.nombre = :nombre")})
public class Materias1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "cve_materia")
    private String cveMateria;
    @Column(name = "cve_area_conocimiento")
    private Integer cveAreaConocimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 125)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "horas_practica")
    private Short horasPractica;
    @Column(name = "horas_teoria")
    private Short horasTeoria;
    @Column(name = "horas_cuatrimestre")
    private Short horasCuatrimestre;
    @Column(name = "creditos")
    private Short creditos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "curricular")
    private boolean curricular;
    @Basic(optional = false)
    @NotNull
    @Column(name = "opcional")
    private boolean opcional;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;

    public Materias1() {
    }

    public Materias1(String cveMateria) {
        this.cveMateria = cveMateria;
    }

    public Materias1(String cveMateria, String nombre) {
        this.cveMateria = cveMateria;
        this.nombre = nombre;
    }

    public String getCveMateria() {
        return cveMateria;
    }

    public void setCveMateria(String cveMateria) {
        this.cveMateria = cveMateria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveMateria != null ? cveMateria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Materias1)) {
            return false;
        }
        Materias1 other = (Materias1) object;
        if ((this.cveMateria == null && other.cveMateria != null) || (this.cveMateria != null && !this.cveMateria.equals(other.cveMateria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Materias1[ cveMateria=" + cveMateria + " ]";
    }

    public Integer getCveAreaConocimiento() {
        return cveAreaConocimiento;
    }

    public void setCveAreaConocimiento(Integer cveAreaConocimiento) {
        this.cveAreaConocimiento = cveAreaConocimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Short getHorasPractica() {
        return horasPractica;
    }

    public void setHorasPractica(Short horasPractica) {
        this.horasPractica = horasPractica;
    }

    public Short getHorasTeoria() {
        return horasTeoria;
    }

    public void setHorasTeoria(Short horasTeoria) {
        this.horasTeoria = horasTeoria;
    }

    public Short getHorasCuatrimestre() {
        return horasCuatrimestre;
    }

    public void setHorasCuatrimestre(Short horasCuatrimestre) {
        this.horasCuatrimestre = horasCuatrimestre;
    }

    public Short getCreditos() {
        return creditos;
    }

    public void setCreditos(Short creditos) {
        this.creditos = creditos;
    }

    public boolean getCurricular() {
        return curricular;
    }

    public void setCurricular(boolean curricular) {
        this.curricular = curricular;
    }

    public boolean getOpcional() {
        return opcional;
    }

    public void setOpcional(boolean opcional) {
        this.opcional = opcional;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
}