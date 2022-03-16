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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "encuesta_empleadores", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncuestaEmpleadores.findAll", query = "SELECT e FROM EncuestaEmpleadores e")
    , @NamedQuery(name = "EncuestaEmpleadores.findByEvaluacion", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.encuestaEmpleadoresPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EncuestaEmpleadores.findByEvaluado", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.encuestaEmpleadoresPK.evaluado = :evaluado")
    , @NamedQuery(name = "EncuestaEmpleadores.findByNombreEmpresa", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.nombreEmpresa = :nombreEmpresa")
    , @NamedQuery(name = "EncuestaEmpleadores.findByEncuestado", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.encuestado = :encuestado")
    , @NamedQuery(name = "EncuestaEmpleadores.findByCargo", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.cargo = :cargo")
    , @NamedQuery(name = "EncuestaEmpleadores.findByNombreEgresado", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.nombreEgresado = :nombreEgresado")
    , @NamedQuery(name = "EncuestaEmpleadores.findByCarrera", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.carrera = :carrera")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR6", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r6 = :r6")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR7", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r7 = :r7")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR8", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r8 = :r8")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR9", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r9 = :r9")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR10", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r10 = :r10")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR11", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r11 = :r11")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR12", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r12 = :r12")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR13", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r13 = :r13")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR14", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r14 = :r14")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR15", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r15 = :r15")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR16", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r16 = :r16")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR17", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r17 = :r17")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR18", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r18 = :r18")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR19", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r19 = :r19")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR20", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r20 = :r20")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR21", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r21 = :r21")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR22", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r22 = :r22")
    , @NamedQuery(name = "EncuestaEmpleadores.findByR23", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.r23 = :r23")
    , @NamedQuery(name = "EncuestaEmpleadores.findByBdIdentificador", query = "SELECT e FROM EncuestaEmpleadores e WHERE e.bdIdentificador = :bdIdentificador")})
public class EncuestaEmpleadores implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EncuestaEmpleadoresPK encuestaEmpleadoresPK;
    @Size(max = 150)
    @Column(name = "nombre_empresa")
    private String nombreEmpresa;
    @Size(max = 150)
    @Column(name = "encuestado")
    private String encuestado;
    @Size(max = 150)
    @Column(name = "cargo")
    private String cargo;
    @Size(max = 150)
    @Column(name = "nombre_egresado")
    private String nombreEgresado;
    @Size(max = 150)
    @Column(name = "carrera")
    private String carrera;
    @Column(name = "r6")
    private Short r6;
    @Column(name = "r7")
    private Short r7;
    @Column(name = "r8")
    private Short r8;
    @Column(name = "r9")
    private Short r9;
    @Column(name = "r10")
    private Short r10;
    @Column(name = "r11")
    private Short r11;
    @Column(name = "r12")
    private Short r12;
    @Column(name = "r13")
    private Short r13;
    @Size(max = 300)
    @Column(name = "r14")
    private String r14;
    @Size(max = 200)
    @Column(name = "r15")
    private String r15;
    @Column(name = "r16")
    private Short r16;
    @Column(name = "r17")
    private Short r17;
    @Column(name = "r18")
    private Short r18;
    @Column(name = "r19")
    private Short r19;
    @Column(name = "r20")
    private Short r20;
    @Column(name = "r21")
    private Short r21;
    @Column(name = "r22")
    private Short r22;
    @Size(max = 300)
    @Column(name = "r23")
    private String r23;
    @Size(max = 50)
    @Column(name = "bd_identificador")
    private String bdIdentificador;

    public EncuestaEmpleadores() {
    }

    public EncuestaEmpleadores(EncuestaEmpleadoresPK encuestaEmpleadoresPK) {
        this.encuestaEmpleadoresPK = encuestaEmpleadoresPK;
    }

    public EncuestaEmpleadores(int evaluacion, int evaluado) {
        this.encuestaEmpleadoresPK = new EncuestaEmpleadoresPK(evaluacion, evaluado);
    }

    public EncuestaEmpleadoresPK getEncuestaEmpleadoresPK() {
        return encuestaEmpleadoresPK;
    }

    public void setEncuestaEmpleadoresPK(EncuestaEmpleadoresPK encuestaEmpleadoresPK) {
        this.encuestaEmpleadoresPK = encuestaEmpleadoresPK;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getEncuestado() {
        return encuestado;
    }

    public void setEncuestado(String encuestado) {
        this.encuestado = encuestado;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getNombreEgresado() {
        return nombreEgresado;
    }

    public void setNombreEgresado(String nombreEgresado) {
        this.nombreEgresado = nombreEgresado;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public Short getR6() {
        return r6;
    }

    public void setR6(Short r6) {
        this.r6 = r6;
    }

    public Short getR7() {
        return r7;
    }

    public void setR7(Short r7) {
        this.r7 = r7;
    }

    public Short getR8() {
        return r8;
    }

    public void setR8(Short r8) {
        this.r8 = r8;
    }

    public Short getR9() {
        return r9;
    }

    public void setR9(Short r9) {
        this.r9 = r9;
    }

    public Short getR10() {
        return r10;
    }

    public void setR10(Short r10) {
        this.r10 = r10;
    }

    public Short getR11() {
        return r11;
    }

    public void setR11(Short r11) {
        this.r11 = r11;
    }

    public Short getR12() {
        return r12;
    }

    public void setR12(Short r12) {
        this.r12 = r12;
    }

    public Short getR13() {
        return r13;
    }

    public void setR13(Short r13) {
        this.r13 = r13;
    }

    public String getR14() {
        return r14;
    }

    public void setR14(String r14) {
        this.r14 = r14;
    }

    public String getR15() {
        return r15;
    }

    public void setR15(String r15) {
        this.r15 = r15;
    }

    public Short getR16() {
        return r16;
    }

    public void setR16(Short r16) {
        this.r16 = r16;
    }

    public Short getR17() {
        return r17;
    }

    public void setR17(Short r17) {
        this.r17 = r17;
    }

    public Short getR18() {
        return r18;
    }

    public void setR18(Short r18) {
        this.r18 = r18;
    }

    public Short getR19() {
        return r19;
    }

    public void setR19(Short r19) {
        this.r19 = r19;
    }

    public Short getR20() {
        return r20;
    }

    public void setR20(Short r20) {
        this.r20 = r20;
    }

    public Short getR21() {
        return r21;
    }

    public void setR21(Short r21) {
        this.r21 = r21;
    }

    public Short getR22() {
        return r22;
    }

    public void setR22(Short r22) {
        this.r22 = r22;
    }

    public String getR23() {
        return r23;
    }

    public void setR23(String r23) {
        this.r23 = r23;
    }

    public String getBdIdentificador() {
        return bdIdentificador;
    }

    public void setBdIdentificador(String bdIdentificador) {
        this.bdIdentificador = bdIdentificador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (encuestaEmpleadoresPK != null ? encuestaEmpleadoresPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaEmpleadores)) {
            return false;
        }
        EncuestaEmpleadores other = (EncuestaEmpleadores) object;
        if ((this.encuestaEmpleadoresPK == null && other.encuestaEmpleadoresPK != null) || (this.encuestaEmpleadoresPK != null && !this.encuestaEmpleadoresPK.equals(other.encuestaEmpleadoresPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaEmpleadores[ encuestaEmpleadoresPK=" + encuestaEmpleadoresPK + " ]";
    }
    
}
