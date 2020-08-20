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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "comisiones_academicas_participantes", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComisionesAcademicasParticipantes.findAll", query = "SELECT c FROM ComisionesAcademicasParticipantes c")
    , @NamedQuery(name = "ComisionesAcademicasParticipantes.findByRegistro", query = "SELECT c FROM ComisionesAcademicasParticipantes c WHERE c.registro = :registro")
    , @NamedQuery(name = "ComisionesAcademicasParticipantes.findByParticipante", query = "SELECT c FROM ComisionesAcademicasParticipantes c WHERE c.participante = :participante")
    , @NamedQuery(name = "ComisionesAcademicasParticipantes.findByArea", query = "SELECT c FROM ComisionesAcademicasParticipantes c WHERE c.area = :area")})
public class ComisionesAcademicasParticipantes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "participante")
    private int participante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;
    @JoinColumn(name = "comision_academica", referencedColumnName = "comision_academica")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ComisionesAcademicas comisionAcademica;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;

    public ComisionesAcademicasParticipantes() {
    }

    public ComisionesAcademicasParticipantes(Integer registro) {
        this.registro = registro;
    }

    public ComisionesAcademicasParticipantes(Integer registro, int participante, short area) {
        this.registro = registro;
        this.participante = participante;
        this.area = area;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getParticipante() {
        return participante;
    }

    public void setParticipante(int participante) {
        this.participante = participante;
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
    }

    public ComisionesAcademicas getComisionAcademica() {
        return comisionAcademica;
    }

    public void setComisionAcademica(ComisionesAcademicas comisionAcademica) {
        this.comisionAcademica = comisionAcademica;
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
        if (!(object instanceof ComisionesAcademicasParticipantes)) {
            return false;
        }
        ComisionesAcademicasParticipantes other = (ComisionesAcademicasParticipantes) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicasParticipantes[ registro=" + registro + " ]";
    }
    
}
