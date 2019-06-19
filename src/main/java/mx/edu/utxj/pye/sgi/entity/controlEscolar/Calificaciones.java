/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "calificaciones", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calificaciones.findAll", query = "SELECT c FROM Calificaciones c")
    , @NamedQuery(name = "Calificaciones.findByEstudiante", query = "SELECT c FROM Calificaciones c WHERE c.calificacionesPK.estudiante = :estudiante")
    , @NamedQuery(name = "Calificaciones.findByGrupo", query = "SELECT c FROM Calificaciones c WHERE c.calificacionesPK.grupo = :grupo")
    , @NamedQuery(name = "Calificaciones.findByMateria", query = "SELECT c FROM Calificaciones c WHERE c.calificacionesPK.materia = :materia")
    , @NamedQuery(name = "Calificaciones.findByCf", query = "SELECT c FROM Calificaciones c WHERE c.cf = :cf")
    , @NamedQuery(name = "Calificaciones.findByCU1", query = "SELECT c FROM Calificaciones c WHERE c.cU1 = :cU1")
    , @NamedQuery(name = "Calificaciones.findByCU2", query = "SELECT c FROM Calificaciones c WHERE c.cU2 = :cU2")
    , @NamedQuery(name = "Calificaciones.findByCU3", query = "SELECT c FROM Calificaciones c WHERE c.cU3 = :cU3")
    , @NamedQuery(name = "Calificaciones.findByCU4", query = "SELECT c FROM Calificaciones c WHERE c.cU4 = :cU4")
    , @NamedQuery(name = "Calificaciones.findByCU5", query = "SELECT c FROM Calificaciones c WHERE c.cU5 = :cU5")
    , @NamedQuery(name = "Calificaciones.findByCU6", query = "SELECT c FROM Calificaciones c WHERE c.cU6 = :cU6")
    , @NamedQuery(name = "Calificaciones.findByCU7", query = "SELECT c FROM Calificaciones c WHERE c.cU7 = :cU7")
    , @NamedQuery(name = "Calificaciones.findByCU8", query = "SELECT c FROM Calificaciones c WHERE c.cU8 = :cU8")
    , @NamedQuery(name = "Calificaciones.findByCU9", query = "SELECT c FROM Calificaciones c WHERE c.cU9 = :cU9")
    , @NamedQuery(name = "Calificaciones.findByCU10", query = "SELECT c FROM Calificaciones c WHERE c.cU10 = :cU10")
    , @NamedQuery(name = "Calificaciones.findByCU11", query = "SELECT c FROM Calificaciones c WHERE c.cU11 = :cU11")
    , @NamedQuery(name = "Calificaciones.findByCU12", query = "SELECT c FROM Calificaciones c WHERE c.cU12 = :cU12")
    , @NamedQuery(name = "Calificaciones.findByCR", query = "SELECT c FROM Calificaciones c WHERE c.cR = :cR")})
public class Calificaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CalificacionesPK calificacionesPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cf")
    private Double cf;
    @Column(name = "c_u1")
    private Double cU1;
    @Column(name = "c_u2")
    private Double cU2;
    @Column(name = "c_u3")
    private Double cU3;
    @Column(name = "c_u4")
    private Double cU4;
    @Column(name = "c_u5")
    private Double cU5;
    @Column(name = "c_u6")
    private Double cU6;
    @Column(name = "c_u7")
    private Double cU7;
    @Column(name = "c_u8")
    private Double cU8;
    @Column(name = "c_u9")
    private Double cU9;
    @Column(name = "c_u10")
    private Double cU10;
    @Column(name = "c_u11")
    private Double cU11;
    @Column(name = "c_u12")
    private Double cU12;
    @Column(name = "c_r")
    private Double cR;
    @JoinColumns({
        @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante", insertable = false, updatable = false)
        , @JoinColumn(name = "grupo", referencedColumnName = "grupo", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Estudiante estudiante1;
    @JoinColumn(name = "materia", referencedColumnName = "id_materia", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Materia materia1;

    public Calificaciones() {
    }

    public Calificaciones(CalificacionesPK calificacionesPK) {
        this.calificacionesPK = calificacionesPK;
    }

    public Calificaciones(int estudiante, int grupo, int materia) {
        this.calificacionesPK = new CalificacionesPK(estudiante, grupo, materia);
    }

    public CalificacionesPK getCalificacionesPK() {
        return calificacionesPK;
    }

    public void setCalificacionesPK(CalificacionesPK calificacionesPK) {
        this.calificacionesPK = calificacionesPK;
    }

    public Double getCf() {
        return cf;
    }

    public void setCf(Double cf) {
        this.cf = cf;
    }

    public Double getCU1() {
        return cU1;
    }

    public void setCU1(Double cU1) {
        this.cU1 = cU1;
    }

    public Double getCU2() {
        return cU2;
    }

    public void setCU2(Double cU2) {
        this.cU2 = cU2;
    }

    public Double getCU3() {
        return cU3;
    }

    public void setCU3(Double cU3) {
        this.cU3 = cU3;
    }

    public Double getCU4() {
        return cU4;
    }

    public void setCU4(Double cU4) {
        this.cU4 = cU4;
    }

    public Double getCU5() {
        return cU5;
    }

    public void setCU5(Double cU5) {
        this.cU5 = cU5;
    }

    public Double getCU6() {
        return cU6;
    }

    public void setCU6(Double cU6) {
        this.cU6 = cU6;
    }

    public Double getCU7() {
        return cU7;
    }

    public void setCU7(Double cU7) {
        this.cU7 = cU7;
    }

    public Double getCU8() {
        return cU8;
    }

    public void setCU8(Double cU8) {
        this.cU8 = cU8;
    }

    public Double getCU9() {
        return cU9;
    }

    public void setCU9(Double cU9) {
        this.cU9 = cU9;
    }

    public Double getCU10() {
        return cU10;
    }

    public void setCU10(Double cU10) {
        this.cU10 = cU10;
    }

    public Double getCU11() {
        return cU11;
    }

    public void setCU11(Double cU11) {
        this.cU11 = cU11;
    }

    public Double getCU12() {
        return cU12;
    }

    public void setCU12(Double cU12) {
        this.cU12 = cU12;
    }

    public Double getCR() {
        return cR;
    }

    public void setCR(Double cR) {
        this.cR = cR;
    }

    public Estudiante getEstudiante1() {
        return estudiante1;
    }

    public void setEstudiante1(Estudiante estudiante1) {
        this.estudiante1 = estudiante1;
    }

    public Materia getMateria1() {
        return materia1;
    }

    public void setMateria1(Materia materia1) {
        this.materia1 = materia1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calificacionesPK != null ? calificacionesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Calificaciones)) {
            return false;
        }
        Calificaciones other = (Calificaciones) object;
        if ((this.calificacionesPK == null && other.calificacionesPK != null) || (this.calificacionesPK != null && !this.calificacionesPK.equals(other.calificacionesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificaciones[ calificacionesPK=" + calificacionesPK + " ]";
    }
    
}
