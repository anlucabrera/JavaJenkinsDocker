/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "cordinadores_tutores", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CordinadoresTutores.findAll", query = "SELECT c FROM CordinadoresTutores c")
    , @NamedQuery(name = "CordinadoresTutores.findByCordinadorTutor", query = "SELECT c FROM CordinadoresTutores c WHERE c.cordinadorTutor = :cordinadorTutor")
    , @NamedQuery(name = "CordinadoresTutores.findByPersonal", query = "SELECT c FROM CordinadoresTutores c WHERE c.personal = :personal")
    , @NamedQuery(name = "CordinadoresTutores.findByAreaAcademica", query = "SELECT c FROM CordinadoresTutores c WHERE c.areaAcademica = :areaAcademica")
    , @NamedQuery(name = "CordinadoresTutores.findByPeriodo", query = "SELECT c FROM CordinadoresTutores c WHERE c.periodo = :periodo")})
public class CordinadoresTutores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cordinador_tutor")
    private Integer cordinadorTutor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "personal")
    private int personal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_academica")
    private short areaAcademica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;

    public CordinadoresTutores() {
    }

    public CordinadoresTutores(Integer cordinadorTutor) {
        this.cordinadorTutor = cordinadorTutor;
    }

    public CordinadoresTutores(Integer cordinadorTutor, int personal, short areaAcademica, int periodo) {
        this.cordinadorTutor = cordinadorTutor;
        this.personal = personal;
        this.areaAcademica = areaAcademica;
        this.periodo = periodo;
    }

    public Integer getCordinadorTutor() {
        return cordinadorTutor;
    }

    public void setCordinadorTutor(Integer cordinadorTutor) {
        this.cordinadorTutor = cordinadorTutor;
    }

    public int getPersonal() {
        return personal;
    }

    public void setPersonal(int personal) {
        this.personal = personal;
    }

    public short getAreaAcademica() {
        return areaAcademica;
    }

    public void setAreaAcademica(short areaAcademica) {
        this.areaAcademica = areaAcademica;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cordinadorTutor != null ? cordinadorTutor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CordinadoresTutores)) {
            return false;
        }
        CordinadoresTutores other = (CordinadoresTutores) object;
        if ((this.cordinadorTutor == null && other.cordinadorTutor != null) || (this.cordinadorTutor != null && !this.cordinadorTutor.equals(other.cordinadorTutor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CordinadoresTutores[ cordinadorTutor=" + cordinadorTutor + " ]";
    }
    
}
