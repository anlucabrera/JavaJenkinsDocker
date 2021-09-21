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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "evaluacion_satisfaccion_resultados", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionSatisfaccionResultados.findAll", query = "SELECT e FROM EvaluacionSatisfaccionResultados e")
    , @NamedQuery(name = "EvaluacionSatisfaccionResultados.findByRegistro", query = "SELECT e FROM EvaluacionSatisfaccionResultados e WHERE e.registro = :registro")
    , @NamedQuery(name = "EvaluacionSatisfaccionResultados.findByNumeroPregunta", query = "SELECT e FROM EvaluacionSatisfaccionResultados e WHERE e.numeroPregunta = :numeroPregunta")
    , @NamedQuery(name = "EvaluacionSatisfaccionResultados.findByRespuestasA", query = "SELECT e FROM EvaluacionSatisfaccionResultados e WHERE e.respuestasA = :respuestasA")
    , @NamedQuery(name = "EvaluacionSatisfaccionResultados.findByRespuestasB", query = "SELECT e FROM EvaluacionSatisfaccionResultados e WHERE e.respuestasB = :respuestasB")
    , @NamedQuery(name = "EvaluacionSatisfaccionResultados.findByRespuestasC", query = "SELECT e FROM EvaluacionSatisfaccionResultados e WHERE e.respuestasC = :respuestasC")
    , @NamedQuery(name = "EvaluacionSatisfaccionResultados.findByRespuestasD", query = "SELECT e FROM EvaluacionSatisfaccionResultados e WHERE e.respuestasD = :respuestasD")
    , @NamedQuery(name = "EvaluacionSatisfaccionResultados.findByRespuestasE", query = "SELECT e FROM EvaluacionSatisfaccionResultados e WHERE e.respuestasE = :respuestasE")
    , @NamedQuery(name = "EvaluacionSatisfaccionResultados.findByRespuestasF", query = "SELECT e FROM EvaluacionSatisfaccionResultados e WHERE e.respuestasF = :respuestasF")
    , @NamedQuery(name = "EvaluacionSatisfaccionResultados.findByRespuestasG", query = "SELECT e FROM EvaluacionSatisfaccionResultados e WHERE e.respuestasG = :respuestasG")})
public class EvaluacionSatisfaccionResultados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "numero_pregunta")
    private String numeroPregunta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "respuestas_a")
    private int respuestasA;
    @Basic(optional = false)
    @NotNull
    @Column(name = "respuestas_b")
    private int respuestasB;
    @Basic(optional = false)
    @NotNull
    @Column(name = "respuestas_c")
    private int respuestasC;
    @Basic(optional = false)
    @NotNull
    @Column(name = "respuestas_d")
    private int respuestasD;
    @Basic(optional = false)
    @NotNull
    @Column(name = "respuestas_e")
    private int respuestasE;
    @Basic(optional = false)
    @NotNull
    @Column(name = "respuestas_f")
    private int respuestasF;
    @Basic(optional = false)
    @NotNull
    @Column(name = "respuestas_g")
    private int respuestasG;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Registros registros;
    @JoinColumn(name = "evaluacion", referencedColumnName = "evaluacion")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EvaluacionesServtecEducont evaluacion;
    @JoinColumn(name = "servicio_tecnologico", referencedColumnName = "servicio")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ServiciosTecnologicosAnioMes servicioTecnologico;

    public EvaluacionSatisfaccionResultados() {
    }

    public EvaluacionSatisfaccionResultados(Integer registro) {
        this.registro = registro;
    }

    public EvaluacionSatisfaccionResultados(Integer registro, String numeroPregunta, int respuestasA, int respuestasB, int respuestasC, int respuestasD, int respuestasE, int respuestasF, int respuestasG) {
        this.registro = registro;
        this.numeroPregunta = numeroPregunta;
        this.respuestasA = respuestasA;
        this.respuestasB = respuestasB;
        this.respuestasC = respuestasC;
        this.respuestasD = respuestasD;
        this.respuestasE = respuestasE;
        this.respuestasF = respuestasF;
        this.respuestasG = respuestasG;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public String getNumeroPregunta() {
        return numeroPregunta;
    }

    public void setNumeroPregunta(String numeroPregunta) {
        this.numeroPregunta = numeroPregunta;
    }

    public int getRespuestasA() {
        return respuestasA;
    }

    public void setRespuestasA(int respuestasA) {
        this.respuestasA = respuestasA;
    }

    public int getRespuestasB() {
        return respuestasB;
    }

    public void setRespuestasB(int respuestasB) {
        this.respuestasB = respuestasB;
    }

    public int getRespuestasC() {
        return respuestasC;
    }

    public void setRespuestasC(int respuestasC) {
        this.respuestasC = respuestasC;
    }

    public int getRespuestasD() {
        return respuestasD;
    }

    public void setRespuestasD(int respuestasD) {
        this.respuestasD = respuestasD;
    }

    public int getRespuestasE() {
        return respuestasE;
    }

    public void setRespuestasE(int respuestasE) {
        this.respuestasE = respuestasE;
    }

    public int getRespuestasF() {
        return respuestasF;
    }

    public void setRespuestasF(int respuestasF) {
        this.respuestasF = respuestasF;
    }

    public int getRespuestasG() {
        return respuestasG;
    }

    public void setRespuestasG(int respuestasG) {
        this.respuestasG = respuestasG;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    public EvaluacionesServtecEducont getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(EvaluacionesServtecEducont evaluacion) {
        this.evaluacion = evaluacion;
    }

    public ServiciosTecnologicosAnioMes getServicioTecnologico() {
        return servicioTecnologico;
    }

    public void setServicioTecnologico(ServiciosTecnologicosAnioMes servicioTecnologico) {
        this.servicioTecnologico = servicioTecnologico;
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
        if (!(object instanceof EvaluacionSatisfaccionResultados)) {
            return false;
        }
        EvaluacionSatisfaccionResultados other = (EvaluacionSatisfaccionResultados) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EvaluacionSatisfaccionResultados[ registro=" + registro + " ]";
    }
    
}
