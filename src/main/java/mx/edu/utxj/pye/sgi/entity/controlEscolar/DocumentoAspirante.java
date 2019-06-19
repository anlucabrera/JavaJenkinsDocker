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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "documento_aspirante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoAspirante.findAll", query = "SELECT d FROM DocumentoAspirante d"),
    @NamedQuery(name = "DocumentoAspirante.findByAspirante", query = "SELECT d FROM DocumentoAspirante d WHERE d.aspirante = :aspirante"),
    @NamedQuery(name = "DocumentoAspirante.findByEvidenciaActaNacimiento", query = "SELECT d FROM DocumentoAspirante d WHERE d.evidenciaActaNacimiento = :evidenciaActaNacimiento"),
    @NamedQuery(name = "DocumentoAspirante.findByEvidenciaHistorialAcademico", query = "SELECT d FROM DocumentoAspirante d WHERE d.evidenciaHistorialAcademico = :evidenciaHistorialAcademico"),
    @NamedQuery(name = "DocumentoAspirante.findByEvidenciaCurp", query = "SELECT d FROM DocumentoAspirante d WHERE d.evidenciaCurp = :evidenciaCurp")})
public class DocumentoAspirante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "aspirante")
    private Integer aspirante;
    @Size(max = 200)
    @Column(name = "evidencia_acta_nacimiento")
    private String evidenciaActaNacimiento;
    @Size(max = 200)
    @Column(name = "evidencia_historial_academico")
    private String evidenciaHistorialAcademico;
    @Size(max = 200)
    @Column(name = "evidencia_curp")
    private String evidenciaCurp;
    @Lob
    @Size(max = 65535)
    @Column(name = "comentarios")
    private String comentarios;
    @JoinColumn(name = "aspirante", referencedColumnName = "id_aspirante", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Aspirante aspirante1;

    public DocumentoAspirante() {
    }

    public DocumentoAspirante(Integer aspirante) {
        this.aspirante = aspirante;
    }

    public Integer getAspirante() {
        return aspirante;
    }

    public void setAspirante(Integer aspirante) {
        this.aspirante = aspirante;
    }

    public String getEvidenciaActaNacimiento() {
        return evidenciaActaNacimiento;
    }

    public void setEvidenciaActaNacimiento(String evidenciaActaNacimiento) {
        this.evidenciaActaNacimiento = evidenciaActaNacimiento;
    }

    public String getEvidenciaHistorialAcademico() {
        return evidenciaHistorialAcademico;
    }

    public void setEvidenciaHistorialAcademico(String evidenciaHistorialAcademico) {
        this.evidenciaHistorialAcademico = evidenciaHistorialAcademico;
    }

    public String getEvidenciaCurp() {
        return evidenciaCurp;
    }

    public void setEvidenciaCurp(String evidenciaCurp) {
        this.evidenciaCurp = evidenciaCurp;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Aspirante getAspirante1() {
        return aspirante1;
    }

    public void setAspirante1(Aspirante aspirante1) {
        this.aspirante1 = aspirante1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aspirante != null ? aspirante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentoAspirante)) {
            return false;
        }
        DocumentoAspirante other = (DocumentoAspirante) object;
        if ((this.aspirante == null && other.aspirante != null) || (this.aspirante != null && !this.aspirante.equals(other.aspirante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoAspirante[ aspirante=" + aspirante + " ]";
    }
    
}
