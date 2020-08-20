/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "desercion_historico", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DesercionHistorico.findAll", query = "SELECT d FROM DesercionHistorico d")
    , @NamedQuery(name = "DesercionHistorico.findByIdDesercion", query = "SELECT d FROM DesercionHistorico d WHERE d.idDesercion = :idDesercion")
    , @NamedQuery(name = "DesercionHistorico.findByCuatrimestre", query = "SELECT d FROM DesercionHistorico d WHERE d.cuatrimestre = :cuatrimestre")
    , @NamedQuery(name = "DesercionHistorico.findByTemporales", query = "SELECT d FROM DesercionHistorico d WHERE d.temporales = :temporales")
    , @NamedQuery(name = "DesercionHistorico.findByDefinitivas", query = "SELECT d FROM DesercionHistorico d WHERE d.definitivas = :definitivas")
    , @NamedQuery(name = "DesercionHistorico.findBySinCausa", query = "SELECT d FROM DesercionHistorico d WHERE d.sinCausa = :sinCausa")
    , @NamedQuery(name = "DesercionHistorico.findByIncExp", query = "SELECT d FROM DesercionHistorico d WHERE d.incExp = :incExp")
    , @NamedQuery(name = "DesercionHistorico.findByReprobacion", query = "SELECT d FROM DesercionHistorico d WHERE d.reprobacion = :reprobacion")
    , @NamedQuery(name = "DesercionHistorico.findByProbEco", query = "SELECT d FROM DesercionHistorico d WHERE d.probEco = :probEco")
    , @NamedQuery(name = "DesercionHistorico.findByMotPer", query = "SELECT d FROM DesercionHistorico d WHERE d.motPer = :motPer")
    , @NamedQuery(name = "DesercionHistorico.findByDisUt", query = "SELECT d FROM DesercionHistorico d WHERE d.disUt = :disUt")
    , @NamedQuery(name = "DesercionHistorico.findByProbTrab", query = "SELECT d FROM DesercionHistorico d WHERE d.probTrab = :probTrab")
    , @NamedQuery(name = "DesercionHistorico.findByCamUt", query = "SELECT d FROM DesercionHistorico d WHERE d.camUt = :camUt")
    , @NamedQuery(name = "DesercionHistorico.findByCamCar", query = "SELECT d FROM DesercionHistorico d WHERE d.camCar = :camCar")
    , @NamedQuery(name = "DesercionHistorico.findByRegEsc", query = "SELECT d FROM DesercionHistorico d WHERE d.regEsc = :regEsc")
    , @NamedQuery(name = "DesercionHistorico.findByIncHor", query = "SELECT d FROM DesercionHistorico d WHERE d.incHor = :incHor")
    , @NamedQuery(name = "DesercionHistorico.findByCamDom", query = "SELECT d FROM DesercionHistorico d WHERE d.camDom = :camDom")
    , @NamedQuery(name = "DesercionHistorico.findByDefuncion", query = "SELECT d FROM DesercionHistorico d WHERE d.defuncion = :defuncion")
    , @NamedQuery(name = "DesercionHistorico.findByProbSal", query = "SELECT d FROM DesercionHistorico d WHERE d.probSal = :probSal")
    , @NamedQuery(name = "DesercionHistorico.findByProbFam", query = "SELECT d FROM DesercionHistorico d WHERE d.probFam = :probFam")
    , @NamedQuery(name = "DesercionHistorico.findByIntCar", query = "SELECT d FROM DesercionHistorico d WHERE d.intCar = :intCar")
    , @NamedQuery(name = "DesercionHistorico.findByCamUni", query = "SELECT d FROM DesercionHistorico d WHERE d.camUni = :camUni")
    , @NamedQuery(name = "DesercionHistorico.findByInasistencia", query = "SELECT d FROM DesercionHistorico d WHERE d.inasistencia = :inasistencia")
    , @NamedQuery(name = "DesercionHistorico.findByOriVoc", query = "SELECT d FROM DesercionHistorico d WHERE d.oriVoc = :oriVoc")
    , @NamedQuery(name = "DesercionHistorico.findByEmbarazo", query = "SELECT d FROM DesercionHistorico d WHERE d.embarazo = :embarazo")
    , @NamedQuery(name = "DesercionHistorico.findByCamCd", query = "SELECT d FROM DesercionHistorico d WHERE d.camCd = :camCd")
    , @NamedQuery(name = "DesercionHistorico.findByAbanEst", query = "SELECT d FROM DesercionHistorico d WHERE d.abanEst = :abanEst")
    , @NamedQuery(name = "DesercionHistorico.findByIntDist", query = "SELECT d FROM DesercionHistorico d WHERE d.intDist = :intDist")
    , @NamedQuery(name = "DesercionHistorico.findByProbAcad", query = "SELECT d FROM DesercionHistorico d WHERE d.probAcad = :probAcad")})
public class DesercionHistorico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_desercion")
    private Integer idDesercion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuatrimestre")
    private int cuatrimestre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "temporales")
    private int temporales;
    @Basic(optional = false)
    @NotNull
    @Column(name = "definitivas")
    private int definitivas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sin_causa")
    private int sinCausa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inc_exp")
    private int incExp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reprobacion")
    private int reprobacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prob_eco")
    private int probEco;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mot_per")
    private int motPer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dis_ut")
    private int disUt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prob_trab")
    private int probTrab;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cam_ut")
    private int camUt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cam_car")
    private int camCar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reg_esc")
    private int regEsc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inc_hor")
    private int incHor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cam_dom")
    private int camDom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "defuncion")
    private int defuncion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prob_sal")
    private int probSal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prob_fam")
    private int probFam;
    @Basic(optional = false)
    @NotNull
    @Column(name = "int_car")
    private int intCar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cam_uni")
    private int camUni;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inasistencia")
    private int inasistencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ori_voc")
    private int oriVoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "embarazo")
    private int embarazo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cam_cd")
    private int camCd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aban_est")
    private int abanEst;
    @Basic(optional = false)
    @NotNull
    @Column(name = "int_dist")
    private int intDist;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prob_acad")
    private int probAcad;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CiclosEscolares ciclo;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PeriodosEscolares periodo;
    @JoinColumn(name = "siglas", referencedColumnName = "siglas")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProgramasEducativos siglas;

    public DesercionHistorico() {
    }

    public DesercionHistorico(Integer idDesercion) {
        this.idDesercion = idDesercion;
    }

    public DesercionHistorico(Integer idDesercion, int cuatrimestre, int temporales, int definitivas, int sinCausa, int incExp, int reprobacion, int probEco, int motPer, int disUt, int probTrab, int camUt, int camCar, int regEsc, int incHor, int camDom, int defuncion, int probSal, int probFam, int intCar, int camUni, int inasistencia, int oriVoc, int embarazo, int camCd, int abanEst, int intDist, int probAcad) {
        this.idDesercion = idDesercion;
        this.cuatrimestre = cuatrimestre;
        this.temporales = temporales;
        this.definitivas = definitivas;
        this.sinCausa = sinCausa;
        this.incExp = incExp;
        this.reprobacion = reprobacion;
        this.probEco = probEco;
        this.motPer = motPer;
        this.disUt = disUt;
        this.probTrab = probTrab;
        this.camUt = camUt;
        this.camCar = camCar;
        this.regEsc = regEsc;
        this.incHor = incHor;
        this.camDom = camDom;
        this.defuncion = defuncion;
        this.probSal = probSal;
        this.probFam = probFam;
        this.intCar = intCar;
        this.camUni = camUni;
        this.inasistencia = inasistencia;
        this.oriVoc = oriVoc;
        this.embarazo = embarazo;
        this.camCd = camCd;
        this.abanEst = abanEst;
        this.intDist = intDist;
        this.probAcad = probAcad;
    }

    public Integer getIdDesercion() {
        return idDesercion;
    }

    public void setIdDesercion(Integer idDesercion) {
        this.idDesercion = idDesercion;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public int getTemporales() {
        return temporales;
    }

    public void setTemporales(int temporales) {
        this.temporales = temporales;
    }

    public int getDefinitivas() {
        return definitivas;
    }

    public void setDefinitivas(int definitivas) {
        this.definitivas = definitivas;
    }

    public int getSinCausa() {
        return sinCausa;
    }

    public void setSinCausa(int sinCausa) {
        this.sinCausa = sinCausa;
    }

    public int getIncExp() {
        return incExp;
    }

    public void setIncExp(int incExp) {
        this.incExp = incExp;
    }

    public int getReprobacion() {
        return reprobacion;
    }

    public void setReprobacion(int reprobacion) {
        this.reprobacion = reprobacion;
    }

    public int getProbEco() {
        return probEco;
    }

    public void setProbEco(int probEco) {
        this.probEco = probEco;
    }

    public int getMotPer() {
        return motPer;
    }

    public void setMotPer(int motPer) {
        this.motPer = motPer;
    }

    public int getDisUt() {
        return disUt;
    }

    public void setDisUt(int disUt) {
        this.disUt = disUt;
    }

    public int getProbTrab() {
        return probTrab;
    }

    public void setProbTrab(int probTrab) {
        this.probTrab = probTrab;
    }

    public int getCamUt() {
        return camUt;
    }

    public void setCamUt(int camUt) {
        this.camUt = camUt;
    }

    public int getCamCar() {
        return camCar;
    }

    public void setCamCar(int camCar) {
        this.camCar = camCar;
    }

    public int getRegEsc() {
        return regEsc;
    }

    public void setRegEsc(int regEsc) {
        this.regEsc = regEsc;
    }

    public int getIncHor() {
        return incHor;
    }

    public void setIncHor(int incHor) {
        this.incHor = incHor;
    }

    public int getCamDom() {
        return camDom;
    }

    public void setCamDom(int camDom) {
        this.camDom = camDom;
    }

    public int getDefuncion() {
        return defuncion;
    }

    public void setDefuncion(int defuncion) {
        this.defuncion = defuncion;
    }

    public int getProbSal() {
        return probSal;
    }

    public void setProbSal(int probSal) {
        this.probSal = probSal;
    }

    public int getProbFam() {
        return probFam;
    }

    public void setProbFam(int probFam) {
        this.probFam = probFam;
    }

    public int getIntCar() {
        return intCar;
    }

    public void setIntCar(int intCar) {
        this.intCar = intCar;
    }

    public int getCamUni() {
        return camUni;
    }

    public void setCamUni(int camUni) {
        this.camUni = camUni;
    }

    public int getInasistencia() {
        return inasistencia;
    }

    public void setInasistencia(int inasistencia) {
        this.inasistencia = inasistencia;
    }

    public int getOriVoc() {
        return oriVoc;
    }

    public void setOriVoc(int oriVoc) {
        this.oriVoc = oriVoc;
    }

    public int getEmbarazo() {
        return embarazo;
    }

    public void setEmbarazo(int embarazo) {
        this.embarazo = embarazo;
    }

    public int getCamCd() {
        return camCd;
    }

    public void setCamCd(int camCd) {
        this.camCd = camCd;
    }

    public int getAbanEst() {
        return abanEst;
    }

    public void setAbanEst(int abanEst) {
        this.abanEst = abanEst;
    }

    public int getIntDist() {
        return intDist;
    }

    public void setIntDist(int intDist) {
        this.intDist = intDist;
    }

    public int getProbAcad() {
        return probAcad;
    }

    public void setProbAcad(int probAcad) {
        this.probAcad = probAcad;
    }

    public CiclosEscolares getCiclo() {
        return ciclo;
    }

    public void setCiclo(CiclosEscolares ciclo) {
        this.ciclo = ciclo;
    }

    public PeriodosEscolares getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    public ProgramasEducativos getSiglas() {
        return siglas;
    }

    public void setSiglas(ProgramasEducativos siglas) {
        this.siglas = siglas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDesercion != null ? idDesercion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DesercionHistorico)) {
            return false;
        }
        DesercionHistorico other = (DesercionHistorico) object;
        if ((this.idDesercion == null && other.idDesercion != null) || (this.idDesercion != null && !this.idDesercion.equals(other.idDesercion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.DesercionHistorico[ idDesercion=" + idDesercion + " ]";
    }
    
}
