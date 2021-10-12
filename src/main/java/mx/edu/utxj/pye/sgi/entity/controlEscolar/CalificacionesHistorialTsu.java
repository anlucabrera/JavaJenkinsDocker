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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author Desarrollo
 */
@Entity
@Table(name = "calificaciones_historial_tsu", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalificacionesHistorialTsu.findAll", query = "SELECT c FROM CalificacionesHistorialTsu c")
    , @NamedQuery(name = "CalificacionesHistorialTsu.findByIdcalificacionHistorica", query = "SELECT c FROM CalificacionesHistorialTsu c WHERE c.idcalificacionHistorica = :idcalificacionHistorica")
    , @NamedQuery(name = "CalificacionesHistorialTsu.findByMateria", query = "SELECT c FROM CalificacionesHistorialTsu c WHERE c.materia = :materia")
    , @NamedQuery(name = "CalificacionesHistorialTsu.findByValor", query = "SELECT c FROM CalificacionesHistorialTsu c WHERE c.valor = :valor")
    , @NamedQuery(name = "CalificacionesHistorialTsu.findByHoras", query = "SELECT c FROM CalificacionesHistorialTsu c WHERE c.horas = :horas")
    , @NamedQuery(name = "CalificacionesHistorialTsu.findByGrado", query = "SELECT c FROM CalificacionesHistorialTsu c WHERE c.grado = :grado")})
public class CalificacionesHistorialTsu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_calificacion_Historica")
    private Integer idcalificacionHistorica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "materia")
    private String materia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private double valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "horas")
    private int horas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grado")
    private int grado;
    @JoinColumn(name = "estudianteHistoricoTSU", referencedColumnName = "estudianteHistoricoTSU")
    @ManyToOne(optional = false)
    private EstudianteHistorialTsu estudianteHistoricoTSU;
    @JoinColumn(name = "id_planEstudio", referencedColumnName = "id_planEstudio")
    @ManyToOne(optional = false)
    private PlanesEstudioExternos idplanEstudio;

    public CalificacionesHistorialTsu() {
    }

    public CalificacionesHistorialTsu(Integer idcalificacionHistorica) {
        this.idcalificacionHistorica = idcalificacionHistorica;
    }

    public CalificacionesHistorialTsu(Integer idcalificacionHistorica, String materia, double valor, int horas, int grado) {
        this.idcalificacionHistorica = idcalificacionHistorica;
        this.materia = materia;
        this.valor = valor;
        this.horas = horas;
        this.grado = grado;
    }

    public Integer getIdcalificacionHistorica() {
        return idcalificacionHistorica;
    }

    public void setIdcalificacionHistorica(Integer idcalificacionHistorica) {
        this.idcalificacionHistorica = idcalificacionHistorica;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getGrado() {
        return grado;
    }

    public void setGrado(int grado) {
        this.grado = grado;
    }

    public EstudianteHistorialTsu getEstudianteHistoricoTSU() {
        return estudianteHistoricoTSU;
    }

    public void setEstudianteHistoricoTSU(EstudianteHistorialTsu estudianteHistoricoTSU) {
        this.estudianteHistoricoTSU = estudianteHistoricoTSU;
    }

    public PlanesEstudioExternos getIdplanEstudio() {
        return idplanEstudio;
    }

    public void setIdplanEstudio(PlanesEstudioExternos idplanEstudio) {
        this.idplanEstudio = idplanEstudio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcalificacionHistorica != null ? idcalificacionHistorica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalificacionesHistorialTsu)) {
            return false;
        }
        CalificacionesHistorialTsu other = (CalificacionesHistorialTsu) object;
        if ((this.idcalificacionHistorica == null && other.idcalificacionHistorica != null) || (this.idcalificacionHistorica != null && !this.idcalificacionHistorica.equals(other.idcalificacionHistorica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionesHistorialTsu[ idcalificacionHistorica=" + idcalificacionHistorica + " ]";
    }
    
}
