/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

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
@Table(name = "lista_expedientes", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ListaExpedientes.findAll", query = "SELECT l FROM ListaExpedientes l")
    , @NamedQuery(name = "ListaExpedientes.findByMatricula", query = "SELECT l FROM ListaExpedientes l WHERE l.matricula = :matricula")
    , @NamedQuery(name = "ListaExpedientes.findByNombre", query = "SELECT l FROM ListaExpedientes l WHERE l.nombre = :nombre")
    , @NamedQuery(name = "ListaExpedientes.findByPrograma", query = "SELECT l FROM ListaExpedientes l WHERE l.programa = :programa")
    , @NamedQuery(name = "ListaExpedientes.findByExpediente", query = "SELECT l FROM ListaExpedientes l WHERE l.expediente = :expediente")})
public class ListaExpedientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "matricula")
    private String matricula;
    @Size(max = 452)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "programa")
    private String programa;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "expediente")
    private int expediente;

    public ListaExpedientes() {
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public int getExpediente() {
        return expediente;
    }

    public void setExpediente(int expediente) {
        this.expediente = expediente;
    }
    
}
