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
 * @author UTXJ
 */
@Entity
@Table(name = "lista_estudiantes_general", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ListaEstudiantesGeneral.findAll", query = "SELECT l FROM ListaEstudiantesGeneral l")
    , @NamedQuery(name = "ListaEstudiantesGeneral.findByMatricula", query = "SELECT l FROM ListaEstudiantesGeneral l WHERE l.matricula = :matricula")
    , @NamedQuery(name = "ListaEstudiantesGeneral.findByApellidoPaterno", query = "SELECT l FROM ListaEstudiantesGeneral l WHERE l.apellidoPaterno = :apellidoPaterno")
    , @NamedQuery(name = "ListaEstudiantesGeneral.findByApellidoMaterno", query = "SELECT l FROM ListaEstudiantesGeneral l WHERE l.apellidoMaterno = :apellidoMaterno")
    , @NamedQuery(name = "ListaEstudiantesGeneral.findByNombre", query = "SELECT l FROM ListaEstudiantesGeneral l WHERE l.nombre = :nombre")
    , @NamedQuery(name = "ListaEstudiantesGeneral.findByCurp", query = "SELECT l FROM ListaEstudiantesGeneral l WHERE l.curp = :curp")})
public class ListaEstudiantesGeneral implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "matricula")
    private int matricula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "apellido_paterno")
    private String apellidoPaterno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "apellido_materno")
    private String apellidoMaterno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "curp")
    private String curp;

    public ListaEstudiantesGeneral() {
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }
    
}
