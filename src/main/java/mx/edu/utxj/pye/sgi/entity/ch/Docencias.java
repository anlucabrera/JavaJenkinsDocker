/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

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
 * @author UTXJ
 */
@Entity
@Table(name = "docencias", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Docencias.findAll", query = "SELECT d FROM Docencias d")
    , @NamedQuery(name = "Docencias.findByDocencia", query = "SELECT d FROM Docencias d WHERE d.docencia = :docencia")
    , @NamedQuery(name = "Docencias.findByPrograma", query = "SELECT d FROM Docencias d WHERE d.programa = :programa")
    , @NamedQuery(name = "Docencias.findByMateria", query = "SELECT d FROM Docencias d WHERE d.materia = :materia")
    , @NamedQuery(name = "Docencias.findByAnio", query = "SELECT d FROM Docencias d WHERE d.anio = :anio")
    , @NamedQuery(name = "Docencias.findByMesInicio", query = "SELECT d FROM Docencias d WHERE d.mesInicio = :mesInicio")
    , @NamedQuery(name = "Docencias.findByMsFin", query = "SELECT d FROM Docencias d WHERE d.msFin = :msFin")})
public class Docencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "docencia")
    private Integer docencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "programa")
    private String programa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "materia")
    private String materia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio")
    private int anio;
    @Size(max = 255)
    @Column(name = "mesInicio")
    private String mesInicio;
    @Size(max = 255)
    @Column(name = "msFin")
    private String msFin;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;

    public Docencias() {
    }

    public Docencias(Integer docencia) {
        this.docencia = docencia;
    }

    public Docencias(Integer docencia, String programa, String materia, int anio) {
        this.docencia = docencia;
        this.programa = programa;
        this.materia = materia;
        this.anio = anio;
    }

    public Integer getDocencia() {
        return docencia;
    }

    public void setDocencia(Integer docencia) {
        this.docencia = docencia;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(String mesInicio) {
        this.mesInicio = mesInicio;
    }

    public String getMsFin() {
        return msFin;
    }

    public void setMsFin(String msFin) {
        this.msFin = msFin;
    }

    public Personal getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(Personal clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (docencia != null ? docencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Docencias)) {
            return false;
        }
        Docencias other = (Docencias) object;
        if ((this.docencia == null && other.docencia != null) || (this.docencia != null && !this.docencia.equals(other.docencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Docencias[ docencia=" + docencia + " ]";
    }
    
}
