/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar.view;

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
 * @author Desarrollo
 */
@Entity
@Table(name = "listaalumnosca", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Listaalumnosca.findAll", query = "SELECT l FROM Listaalumnosca l")
    , @NamedQuery(name = "Listaalumnosca.findByCarga", query = "SELECT l FROM Listaalumnosca l WHERE l.carga = :carga")
    , @NamedQuery(name = "Listaalumnosca.findByGrado", query = "SELECT l FROM Listaalumnosca l WHERE l.grado = :grado")
    , @NamedQuery(name = "Listaalumnosca.findByLiteral", query = "SELECT l FROM Listaalumnosca l WHERE l.literal = :literal")
    , @NamedQuery(name = "Listaalumnosca.findByMatricula", query = "SELECT l FROM Listaalumnosca l WHERE l.matricula = :matricula")
    , @NamedQuery(name = "Listaalumnosca.findByEsNombre", query = "SELECT l FROM Listaalumnosca l WHERE l.esNombre = :esNombre")
    , @NamedQuery(name = "Listaalumnosca.findByEsApePat", query = "SELECT l FROM Listaalumnosca l WHERE l.esApePat = :esApePat")
    , @NamedQuery(name = "Listaalumnosca.findByEsApeMat", query = "SELECT l FROM Listaalumnosca l WHERE l.esApeMat = :esApeMat")
    , @NamedQuery(name = "Listaalumnosca.findByAsistenciaT", query = "SELECT l FROM Listaalumnosca l WHERE l.asistenciaT = :asistenciaT")
    , @NamedQuery(name = "Listaalumnosca.findByTipoEstudiante", query = "SELECT l FROM Listaalumnosca l WHERE l.tipoEstudiante = :tipoEstudiante")})
public class Listaalumnosca implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "carga")
    private int carga;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grado")
    private int grado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "literal")
    private Character literal;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Matricula")
    private int matricula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "Es_Nombre")
    private String esNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "Es_Ape_Pat")
    private String esApePat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "Es_Ape_Mat")
    private String esApeMat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "AsistenciaT")
    private String asistenciaT;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_estudiante")
    private short tipoEstudiante;

    public Listaalumnosca() {
    }

    public int getCarga() {
        return carga;
    }

    public void setCarga(int carga) {
        this.carga = carga;
    }

    public int getGrado() {
        return grado;
    }

    public void setGrado(int grado) {
        this.grado = grado;
    }

    public Character getLiteral() {
        return literal;
    }

    public void setLiteral(Character literal) {
        this.literal = literal;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getEsNombre() {
        return esNombre;
    }

    public void setEsNombre(String esNombre) {
        this.esNombre = esNombre;
    }

    public String getEsApePat() {
        return esApePat;
    }

    public void setEsApePat(String esApePat) {
        this.esApePat = esApePat;
    }

    public String getEsApeMat() {
        return esApeMat;
    }

    public void setEsApeMat(String esApeMat) {
        this.esApeMat = esApeMat;
    }

    public String getAsistenciaT() {
        return asistenciaT;
    }

    public void setAsistenciaT(String asistenciaT) {
        this.asistenciaT = asistenciaT;
    }

    public short getTipoEstudiante() {
        return tipoEstudiante;
    }

    public void setTipoEstudiante(short tipoEstudiante) {
        this.tipoEstudiante = tipoEstudiante;
    }
    
}
