/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzascarlos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "evidencias_finanzas", catalog = "finanzascarlos", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvidenciasFinanzas.findAll", query = "SELECT e FROM EvidenciasFinanzas e")
    , @NamedQuery(name = "EvidenciasFinanzas.findByEvidencia", query = "SELECT e FROM EvidenciasFinanzas e WHERE e.evidencia = :evidencia")
    , @NamedQuery(name = "EvidenciasFinanzas.findByMatricula", query = "SELECT e FROM EvidenciasFinanzas e WHERE e.matricula = :matricula")
    , @NamedQuery(name = "EvidenciasFinanzas.findByPoa", query = "SELECT e FROM EvidenciasFinanzas e WHERE e.poa = :poa")
    , @NamedQuery(name = "EvidenciasFinanzas.findByMes", query = "SELECT e FROM EvidenciasFinanzas e WHERE e.mes = :mes")
    , @NamedQuery(name = "EvidenciasFinanzas.findByConcepto", query = "SELECT e FROM EvidenciasFinanzas e WHERE e.concepto = :concepto")
    , @NamedQuery(name = "EvidenciasFinanzas.findByRegistro", query = "SELECT e FROM EvidenciasFinanzas e WHERE e.registro = :registro")
    , @NamedQuery(name = "EvidenciasFinanzas.findByNombreArchivo", query = "SELECT e FROM EvidenciasFinanzas e WHERE e.nombreArchivo = :nombreArchivo")
    , @NamedQuery(name = "EvidenciasFinanzas.findByRuta", query = "SELECT e FROM EvidenciasFinanzas e WHERE e.ruta = :ruta")
    , @NamedQuery(name = "EvidenciasFinanzas.findByMime", query = "SELECT e FROM EvidenciasFinanzas e WHERE e.mime = :mime")
    , @NamedQuery(name = "EvidenciasFinanzas.findByRutaRelativa", query = "SELECT e FROM EvidenciasFinanzas e WHERE e.rutaRelativa = :rutaRelativa")})
public class EvidenciasFinanzas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evidencia")
    private Integer evidencia;
    @Column(name = "matricula")
    private Integer matricula;
    @Basic(optional = false)
    @Column(name = "poa")
    @Temporal(TemporalType.DATE)
    private Date poa;
    @Basic(optional = false)
    @Column(name = "mes")
    private String mes;
    @Basic(optional = false)
    @Column(name = "concepto")
    private int concepto;
    @Basic(optional = false)
    @Column(name = "registro")
    private int registro;
    @Basic(optional = false)
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Basic(optional = false)
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @Column(name = "mime")
    private String mime;
    @Column(name = "rutaRelativa")
    private String rutaRelativa;
    @OneToMany(mappedBy = "registroEvidencia")
    private List<Registro> registroList;

    public EvidenciasFinanzas() {
    }

    public EvidenciasFinanzas(Integer evidencia) {
        this.evidencia = evidencia;
    }

    public EvidenciasFinanzas(Integer evidencia, Date poa, String mes, int concepto, int registro, String nombreArchivo, String ruta, String mime) {
        this.evidencia = evidencia;
        this.poa = poa;
        this.mes = mes;
        this.concepto = concepto;
        this.registro = registro;
        this.nombreArchivo = nombreArchivo;
        this.ruta = ruta;
        this.mime = mime;
    }

    public Integer getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(Integer evidencia) {
        this.evidencia = evidencia;
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public Date getPoa() {
        return poa;
    }

    public void setPoa(Date poa) {
        this.poa = poa;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getConcepto() {
        return concepto;
    }

    public void setConcepto(int concepto) {
        this.concepto = concepto;
    }

    public int getRegistro() {
        return registro;
    }

    public void setRegistro(int registro) {
        this.registro = registro;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getRutaRelativa() {
        return rutaRelativa;
    }

    public void setRutaRelativa(String rutaRelativa) {
        this.rutaRelativa = rutaRelativa;
    }

    @XmlTransient
    public List<Registro> getRegistroList() {
        return registroList;
    }

    public void setRegistroList(List<Registro> registroList) {
        this.registroList = registroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evidencia != null ? evidencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvidenciasFinanzas)) {
            return false;
        }
        EvidenciasFinanzas other = (EvidenciasFinanzas) object;
        if ((this.evidencia == null && other.evidencia != null) || (this.evidencia != null && !this.evidencia.equals(other.evidencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidadesFinanzas.EvidenciasFinanzas[ evidencia=" + evidencia + " ]";
    }
    
}
