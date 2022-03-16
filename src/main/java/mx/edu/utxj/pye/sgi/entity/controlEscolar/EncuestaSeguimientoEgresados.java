/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "encuesta_seguimiento_egresados", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EncuestaSeguimientoEgresados.findAll", query = "SELECT e FROM EncuestaSeguimientoEgresados e")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByEvaluacion", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.encuestaSeguimientoEgresadosPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByEvaluador", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.encuestaSeguimientoEgresadosPK.evaluador = :evaluador")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByNumTelFijo", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.numTelFijo = :numTelFijo")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByNumTelMovil", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.numTelMovil = :numTelMovil")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByCorreoElectronico", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.correoElectronico = :correoElectronico")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByRedSocial", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.redSocial = :redSocial")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByUltNivelEscolaridad", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.ultNivelEscolaridad = :ultNivelEscolaridad")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByInstitucionEstudias", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.institucionEstudias = :institucionEstudias")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByActividadActual", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.actividadActual = :actividadActual")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByActividadEconomico", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.actividadEconomico = :actividadEconomico")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByPuestoActual", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.puestoActual = :puestoActual")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByCoincideFormacionUt", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.coincideFormacionUt = :coincideFormacionUt")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByIngresoMensual", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.ingresoMensual = :ingresoMensual")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByRegimenJuridico", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.regimenJuridico = :regimenJuridico")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByTamanioEmpresa", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.tamanioEmpresa = :tamanioEmpresa")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByTiempoEmpleo", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.tiempoEmpleo = :tiempoEmpleo")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByMedioTrabajo", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.medioTrabajo = :medioTrabajo")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByGenteCargo", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.genteCargo = :genteCargo")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findBySituacionEconomica", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.situacionEconomica = :situacionEconomica")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByFactorA", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.factorA = :factorA")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByFactorB", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.factorB = :factorB")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByFactorC", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.factorC = :factorC")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByFactorD", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.factorD = :factorD")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByFactorE", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.factorE = :factorE")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByFactorF", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.factorF = :factorF")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByNombreEmpresa", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.nombreEmpresa = :nombreEmpresa")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByCalleNoEmpresa", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.calleNoEmpresa = :calleNoEmpresa")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByColonia", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.colonia = :colonia")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByMunicipio", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.municipio = :municipio")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByEstado", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.estado = :estado")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByTelefono", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.telefono = :telefono")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByExtension", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.extension = :extension")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByCorreoElectronicoEmpresa", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.correoElectronicoEmpresa = :correoElectronicoEmpresa")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByAspectoA", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.aspectoA = :aspectoA")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByAspectoB", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.aspectoB = :aspectoB")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByAspectoC", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.aspectoC = :aspectoC")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByAspectoD", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.aspectoD = :aspectoD")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByAspectoE", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.aspectoE = :aspectoE")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByAspectoF", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.aspectoF = :aspectoF")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByAspectoG", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.aspectoG = :aspectoG")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByAspectoH", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.aspectoH = :aspectoH")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByAspectoI", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.aspectoI = :aspectoI")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByAspectoJ", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.aspectoJ = :aspectoJ")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByAspectoK", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.aspectoK = :aspectoK")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByAspectoL", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.aspectoL = :aspectoL")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByAspectoM", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.aspectoM = :aspectoM")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByFormacionExpectativa", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.formacionExpectativa = :formacionExpectativa")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByCurso", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.curso = :curso")
    , @NamedQuery(name = "EncuestaSeguimientoEgresados.findByComentarios", query = "SELECT e FROM EncuestaSeguimientoEgresados e WHERE e.comentarios = :comentarios")})
public class EncuestaSeguimientoEgresados implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EncuestaSeguimientoEgresadosPK encuestaSeguimientoEgresadosPK;
    @Size(max = 20)
    @Column(name = "num_tel_fijo")
    private String numTelFijo;
    @Size(max = 20)
    @Column(name = "num_tel_movil")
    private String numTelMovil;
    @Size(max = 50)
    @Column(name = "correo_electronico")
    private String correoElectronico;
    @Size(max = 100)
    @Column(name = "red_social")
    private String redSocial;
    @Column(name = "ult_nivel_escolaridad")
    private Short ultNivelEscolaridad;
    @Column(name = "institucion_estudias")
    private Short institucionEstudias;
    @Column(name = "actividad_actual")
    private Short actividadActual;
    @Column(name = "actividad_economico")
    private Short actividadEconomico;
    @Column(name = "puesto_actual")
    private Short puestoActual;
    @Column(name = "coincide_formacion_ut")
    private Short coincideFormacionUt;
    @Column(name = "ingreso_mensual")
    private Short ingresoMensual;
    @Column(name = "regimen_juridico")
    private Short regimenJuridico;
    @Column(name = "tamanio_empresa")
    private Short tamanioEmpresa;
    @Column(name = "tiempo_empleo")
    private Short tiempoEmpleo;
    @Column(name = "medio_trabajo")
    private Short medioTrabajo;
    @Column(name = "gente_cargo")
    private Short genteCargo;
    @Column(name = "situacion_economica")
    private Short situacionEconomica;
    @Column(name = "factor_a")
    private Short factorA;
    @Column(name = "factor_b")
    private Short factorB;
    @Column(name = "factor_c")
    private Short factorC;
    @Column(name = "factor_d")
    private Short factorD;
    @Column(name = "factor_e")
    private Short factorE;
    @Column(name = "factor_f")
    private Short factorF;
    @Size(max = 100)
    @Column(name = "nombre_empresa")
    private String nombreEmpresa;
    @Size(max = 100)
    @Column(name = "calle_no_empresa")
    private String calleNoEmpresa;
    @Size(max = 100)
    @Column(name = "colonia")
    private String colonia;
    @Size(max = 100)
    @Column(name = "municipio")
    private String municipio;
    @Size(max = 100)
    @Column(name = "estado")
    private String estado;
    @Size(max = 20)
    @Column(name = "telefono")
    private String telefono;
    @Size(max = 10)
    @Column(name = "extension")
    private String extension;
    @Size(max = 50)
    @Column(name = "correo_electronico_empresa")
    private String correoElectronicoEmpresa;
    @Column(name = "aspecto_a")
    private Short aspectoA;
    @Column(name = "aspecto_b")
    private Short aspectoB;
    @Column(name = "aspecto_c")
    private Short aspectoC;
    @Column(name = "aspecto_d")
    private Short aspectoD;
    @Column(name = "aspecto_e")
    private Short aspectoE;
    @Column(name = "aspecto_f")
    private Short aspectoF;
    @Column(name = "aspecto_g")
    private Short aspectoG;
    @Column(name = "aspecto_h")
    private Short aspectoH;
    @Column(name = "aspecto_i")
    private Short aspectoI;
    @Column(name = "aspecto_j")
    private Short aspectoJ;
    @Column(name = "aspecto_k")
    private Short aspectoK;
    @Column(name = "aspecto_l")
    private Short aspectoL;
    @Column(name = "aspecto_m")
    private Short aspectoM;
    @Column(name = "formacion_expectativa")
    private Short formacionExpectativa;
    @Size(max = 255)
    @Column(name = "curso")
    private String curso;
    @Size(max = 255)
    @Column(name = "comentarios")
    private String comentarios;

    public EncuestaSeguimientoEgresados() {
    }

    public EncuestaSeguimientoEgresados(EncuestaSeguimientoEgresadosPK encuestaSeguimientoEgresadosPK) {
        this.encuestaSeguimientoEgresadosPK = encuestaSeguimientoEgresadosPK;
    }

    public EncuestaSeguimientoEgresados(int evaluacion, int evaluador) {
        this.encuestaSeguimientoEgresadosPK = new EncuestaSeguimientoEgresadosPK(evaluacion, evaluador);
    }

    public EncuestaSeguimientoEgresadosPK getEncuestaSeguimientoEgresadosPK() {
        return encuestaSeguimientoEgresadosPK;
    }

    public void setEncuestaSeguimientoEgresadosPK(EncuestaSeguimientoEgresadosPK encuestaSeguimientoEgresadosPK) {
        this.encuestaSeguimientoEgresadosPK = encuestaSeguimientoEgresadosPK;
    }

    public String getNumTelFijo() {
        return numTelFijo;
    }

    public void setNumTelFijo(String numTelFijo) {
        this.numTelFijo = numTelFijo;
    }

    public String getNumTelMovil() {
        return numTelMovil;
    }

    public void setNumTelMovil(String numTelMovil) {
        this.numTelMovil = numTelMovil;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getRedSocial() {
        return redSocial;
    }

    public void setRedSocial(String redSocial) {
        this.redSocial = redSocial;
    }

    public Short getUltNivelEscolaridad() {
        return ultNivelEscolaridad;
    }

    public void setUltNivelEscolaridad(Short ultNivelEscolaridad) {
        this.ultNivelEscolaridad = ultNivelEscolaridad;
    }

    public Short getInstitucionEstudias() {
        return institucionEstudias;
    }

    public void setInstitucionEstudias(Short institucionEstudias) {
        this.institucionEstudias = institucionEstudias;
    }

    public Short getActividadActual() {
        return actividadActual;
    }

    public void setActividadActual(Short actividadActual) {
        this.actividadActual = actividadActual;
    }

    public Short getActividadEconomico() {
        return actividadEconomico;
    }

    public void setActividadEconomico(Short actividadEconomico) {
        this.actividadEconomico = actividadEconomico;
    }

    public Short getPuestoActual() {
        return puestoActual;
    }

    public void setPuestoActual(Short puestoActual) {
        this.puestoActual = puestoActual;
    }

    public Short getCoincideFormacionUt() {
        return coincideFormacionUt;
    }

    public void setCoincideFormacionUt(Short coincideFormacionUt) {
        this.coincideFormacionUt = coincideFormacionUt;
    }

    public Short getIngresoMensual() {
        return ingresoMensual;
    }

    public void setIngresoMensual(Short ingresoMensual) {
        this.ingresoMensual = ingresoMensual;
    }

    public Short getRegimenJuridico() {
        return regimenJuridico;
    }

    public void setRegimenJuridico(Short regimenJuridico) {
        this.regimenJuridico = regimenJuridico;
    }

    public Short getTamanioEmpresa() {
        return tamanioEmpresa;
    }

    public void setTamanioEmpresa(Short tamanioEmpresa) {
        this.tamanioEmpresa = tamanioEmpresa;
    }

    public Short getTiempoEmpleo() {
        return tiempoEmpleo;
    }

    public void setTiempoEmpleo(Short tiempoEmpleo) {
        this.tiempoEmpleo = tiempoEmpleo;
    }

    public Short getMedioTrabajo() {
        return medioTrabajo;
    }

    public void setMedioTrabajo(Short medioTrabajo) {
        this.medioTrabajo = medioTrabajo;
    }

    public Short getGenteCargo() {
        return genteCargo;
    }

    public void setGenteCargo(Short genteCargo) {
        this.genteCargo = genteCargo;
    }

    public Short getSituacionEconomica() {
        return situacionEconomica;
    }

    public void setSituacionEconomica(Short situacionEconomica) {
        this.situacionEconomica = situacionEconomica;
    }

    public Short getFactorA() {
        return factorA;
    }

    public void setFactorA(Short factorA) {
        this.factorA = factorA;
    }

    public Short getFactorB() {
        return factorB;
    }

    public void setFactorB(Short factorB) {
        this.factorB = factorB;
    }

    public Short getFactorC() {
        return factorC;
    }

    public void setFactorC(Short factorC) {
        this.factorC = factorC;
    }

    public Short getFactorD() {
        return factorD;
    }

    public void setFactorD(Short factorD) {
        this.factorD = factorD;
    }

    public Short getFactorE() {
        return factorE;
    }

    public void setFactorE(Short factorE) {
        this.factorE = factorE;
    }

    public Short getFactorF() {
        return factorF;
    }

    public void setFactorF(Short factorF) {
        this.factorF = factorF;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getCalleNoEmpresa() {
        return calleNoEmpresa;
    }

    public void setCalleNoEmpresa(String calleNoEmpresa) {
        this.calleNoEmpresa = calleNoEmpresa;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getCorreoElectronicoEmpresa() {
        return correoElectronicoEmpresa;
    }

    public void setCorreoElectronicoEmpresa(String correoElectronicoEmpresa) {
        this.correoElectronicoEmpresa = correoElectronicoEmpresa;
    }

    public Short getAspectoA() {
        return aspectoA;
    }

    public void setAspectoA(Short aspectoA) {
        this.aspectoA = aspectoA;
    }

    public Short getAspectoB() {
        return aspectoB;
    }

    public void setAspectoB(Short aspectoB) {
        this.aspectoB = aspectoB;
    }

    public Short getAspectoC() {
        return aspectoC;
    }

    public void setAspectoC(Short aspectoC) {
        this.aspectoC = aspectoC;
    }

    public Short getAspectoD() {
        return aspectoD;
    }

    public void setAspectoD(Short aspectoD) {
        this.aspectoD = aspectoD;
    }

    public Short getAspectoE() {
        return aspectoE;
    }

    public void setAspectoE(Short aspectoE) {
        this.aspectoE = aspectoE;
    }

    public Short getAspectoF() {
        return aspectoF;
    }

    public void setAspectoF(Short aspectoF) {
        this.aspectoF = aspectoF;
    }

    public Short getAspectoG() {
        return aspectoG;
    }

    public void setAspectoG(Short aspectoG) {
        this.aspectoG = aspectoG;
    }

    public Short getAspectoH() {
        return aspectoH;
    }

    public void setAspectoH(Short aspectoH) {
        this.aspectoH = aspectoH;
    }

    public Short getAspectoI() {
        return aspectoI;
    }

    public void setAspectoI(Short aspectoI) {
        this.aspectoI = aspectoI;
    }

    public Short getAspectoJ() {
        return aspectoJ;
    }

    public void setAspectoJ(Short aspectoJ) {
        this.aspectoJ = aspectoJ;
    }

    public Short getAspectoK() {
        return aspectoK;
    }

    public void setAspectoK(Short aspectoK) {
        this.aspectoK = aspectoK;
    }

    public Short getAspectoL() {
        return aspectoL;
    }

    public void setAspectoL(Short aspectoL) {
        this.aspectoL = aspectoL;
    }

    public Short getAspectoM() {
        return aspectoM;
    }

    public void setAspectoM(Short aspectoM) {
        this.aspectoM = aspectoM;
    }

    public Short getFormacionExpectativa() {
        return formacionExpectativa;
    }

    public void setFormacionExpectativa(Short formacionExpectativa) {
        this.formacionExpectativa = formacionExpectativa;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (encuestaSeguimientoEgresadosPK != null ? encuestaSeguimientoEgresadosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaSeguimientoEgresados)) {
            return false;
        }
        EncuestaSeguimientoEgresados other = (EncuestaSeguimientoEgresados) object;
        if ((this.encuestaSeguimientoEgresadosPK == null && other.encuestaSeguimientoEgresadosPK != null) || (this.encuestaSeguimientoEgresadosPK != null && !this.encuestaSeguimientoEgresadosPK.equals(other.encuestaSeguimientoEgresadosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaSeguimientoEgresados[ encuestaSeguimientoEgresadosPK=" + encuestaSeguimientoEgresadosPK + " ]";
    }
    
}
