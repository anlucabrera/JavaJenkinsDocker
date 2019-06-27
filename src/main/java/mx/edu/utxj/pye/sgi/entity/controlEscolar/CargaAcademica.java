/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "carga_academica", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CargaAcademica.findAll", query = "SELECT c FROM CargaAcademica c")
    , @NamedQuery(name = "CargaAcademica.findByCarga", query = "SELECT c FROM CargaAcademica c WHERE c.carga = :carga")
    , @NamedQuery(name = "CargaAcademica.findByDocente", query = "SELECT c FROM CargaAcademica c WHERE c.docente = :docente")
    , @NamedQuery(name = "CargaAcademica.findByHorasSemana", query = "SELECT c FROM CargaAcademica c WHERE c.horasSemana = :horasSemana")})
public class CargaAcademica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "carga")
    private Integer carga;
    @Basic(optional = false)
    @NotNull
    @Column(name = "docente")
    private int docente;
    @Column(name = "horas_semana")
    private Integer horasSemana;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carga")
    private List<TareaIntegradora> tareaIntegradoraList;
    @JoinColumn(name = "id_plan_materia", referencedColumnName = "id_plan_materia")
    @ManyToOne(optional = false)
    private PlanEstudioMateria idPlanMateria;
    @JoinColumn(name = "cve_grupo", referencedColumnName = "id_grupo")
    @ManyToOne(optional = false)
    private Grupo cveGrupo;
    @JoinColumn(name = "evento", referencedColumnName = "evento")
    @ManyToOne(optional = false)
    private EventoEscolar evento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carga")
    private List<Asesoria> asesoriaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carga")
    private List<UnidadMateriaConfiguracion> unidadMateriaConfiguracionList;

    public CargaAcademica() {
    }

    public CargaAcademica(Integer carga) {
        this.carga = carga;
    }

    public CargaAcademica(Integer carga, int docente) {
        this.carga = carga;
        this.docente = docente;
    }

    public Integer getCarga() {
        return carga;
    }

    public void setCarga(Integer carga) {
        this.carga = carga;
    }

    public int getDocente() {
        return docente;
    }

    public void setDocente(int docente) {
        this.docente = docente;
    }

    public Integer getHorasSemana() {
        return horasSemana;
    }

    public void setHorasSemana(Integer horasSemana) {
        this.horasSemana = horasSemana;
    }

    @XmlTransient
    public List<TareaIntegradora> getTareaIntegradoraList() {
        return tareaIntegradoraList;
    }

    public void setTareaIntegradoraList(List<TareaIntegradora> tareaIntegradoraList) {
        this.tareaIntegradoraList = tareaIntegradoraList;
    }

    public PlanEstudioMateria getIdPlanMateria() {
        return idPlanMateria;
    }

    public void setIdPlanMateria(PlanEstudioMateria idPlanMateria) {
        this.idPlanMateria = idPlanMateria;
    }

    public Grupo getCveGrupo() {
        return cveGrupo;
    }

    public void setCveGrupo(Grupo cveGrupo) {
        this.cveGrupo = cveGrupo;
    }

    public EventoEscolar getEvento() {
        return evento;
    }

    public void setEvento(EventoEscolar evento) {
        this.evento = evento;
    }

    @XmlTransient
    public List<Asesoria> getAsesoriaList() {
        return asesoriaList;
    }

    public void setAsesoriaList(List<Asesoria> asesoriaList) {
        this.asesoriaList = asesoriaList;
    }

    @XmlTransient
    public List<UnidadMateriaConfiguracion> getUnidadMateriaConfiguracionList() {
        return unidadMateriaConfiguracionList;
    }

    public void setUnidadMateriaConfiguracionList(List<UnidadMateriaConfiguracion> unidadMateriaConfiguracionList) {
        this.unidadMateriaConfiguracionList = unidadMateriaConfiguracionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carga != null ? carga.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CargaAcademica)) {
            return false;
        }
        CargaAcademica other = (CargaAcademica) object;
        if ((this.carga == null && other.carga != null) || (this.carga != null && !this.carga.equals(other.carga))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica[ carga=" + carga + " ]";
    }
    
}
