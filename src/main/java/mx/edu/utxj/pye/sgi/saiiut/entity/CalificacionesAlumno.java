/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "calificaciones_alumno", catalog = "saiiut", schema = "SAIIUT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalificacionesAlumno.findAll", query = "SELECT c FROM CalificacionesAlumno c")
    , @NamedQuery(name = "CalificacionesAlumno.findByCveGrupo", query = "SELECT c FROM CalificacionesAlumno c WHERE c.calificacionesAlumnoPK.cveGrupo = :cveGrupo")
    , @NamedQuery(name = "CalificacionesAlumno.findByCveTurno", query = "SELECT c FROM CalificacionesAlumno c WHERE c.calificacionesAlumnoPK.cveTurno = :cveTurno")
    , @NamedQuery(name = "CalificacionesAlumno.findByCvePlan", query = "SELECT c FROM CalificacionesAlumno c WHERE c.calificacionesAlumnoPK.cvePlan = :cvePlan")
    , @NamedQuery(name = "CalificacionesAlumno.findByCveCarrera", query = "SELECT c FROM CalificacionesAlumno c WHERE c.calificacionesAlumnoPK.cveCarrera = :cveCarrera")
    , @NamedQuery(name = "CalificacionesAlumno.findByCveDivision", query = "SELECT c FROM CalificacionesAlumno c WHERE c.calificacionesAlumnoPK.cveDivision = :cveDivision")
    , @NamedQuery(name = "CalificacionesAlumno.findByCveUnidadAcademica", query = "SELECT c FROM CalificacionesAlumno c WHERE c.calificacionesAlumnoPK.cveUnidadAcademica = :cveUnidadAcademica")
    , @NamedQuery(name = "CalificacionesAlumno.findByCveUniversidad", query = "SELECT c FROM CalificacionesAlumno c WHERE c.calificacionesAlumnoPK.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "CalificacionesAlumno.findByCvePeriodo", query = "SELECT c FROM CalificacionesAlumno c WHERE c.calificacionesAlumnoPK.cvePeriodo = :cvePeriodo")
    , @NamedQuery(name = "CalificacionesAlumno.findByCveAlumno", query = "SELECT c FROM CalificacionesAlumno c WHERE c.calificacionesAlumnoPK.cveAlumno = :cveAlumno")
    , @NamedQuery(name = "CalificacionesAlumno.findByCveMateria", query = "SELECT c FROM CalificacionesAlumno c WHERE c.calificacionesAlumnoPK.cveMateria = :cveMateria")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcf", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcf = :fcf")
    , @NamedQuery(name = "CalificacionesAlumno.findByCf", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cf = :cf")
    , @NamedQuery(name = "CalificacionesAlumno.findByValida", query = "SELECT c FROM CalificacionesAlumno c WHERE c.valida = :valida")
    , @NamedQuery(name = "CalificacionesAlumno.findByCm1", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cm1 = :cm1")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcm1", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcm1 = :fcm1")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr11", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr11 = :cr11")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro11", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro11 = :caro11")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr11", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr11 = :fcr11")
    , @NamedQuery(name = "CalificacionesAlumno.findByCm2", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cm2 = :cm2")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcm2", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcm2 = :fcm2")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr21", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr21 = :cr21")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro21", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro21 = :caro21")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr21", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr21 = :fcr21")
    , @NamedQuery(name = "CalificacionesAlumno.findByCm3", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cm3 = :cm3")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcm3", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcm3 = :fcm3")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr31", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr31 = :cr31")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro31", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro31 = :caro31")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr31", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr31 = :fcr31")
    , @NamedQuery(name = "CalificacionesAlumno.findByExento", query = "SELECT c FROM CalificacionesAlumno c WHERE c.exento = :exento")
    , @NamedQuery(name = "CalificacionesAlumno.findByCfr1", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cfr1 = :cfr1")
    , @NamedQuery(name = "CalificacionesAlumno.findByCarf1", query = "SELECT c FROM CalificacionesAlumno c WHERE c.carf1 = :carf1")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcfr1", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcfr1 = :fcfr1")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr13", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr13 = :cr13")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro13", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro13 = :caro13")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr13", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr13 = :fcr13")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr23", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr23 = :cr23")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro23", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro23 = :caro23")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr23", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr23 = :fcr23")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr33", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr33 = :cr33")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro33", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro33 = :caro33")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr33", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr33 = :fcr33")
    , @NamedQuery(name = "CalificacionesAlumno.findByCm4", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cm4 = :cm4")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcm4", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcm4 = :fcm4")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr41", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr41 = :cr41")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro41", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro41 = :caro41")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr41", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr41 = :fcr41")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr12", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr12 = :cr12")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro12", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro12 = :caro12")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr12", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr12 = :fcr12")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr14", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr14 = :cr14")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro14", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro14 = :caro14")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr14", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr14 = :fcr14")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr15", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr15 = :cr15")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro15", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro15 = :caro15")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr15", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr15 = :fcr15")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr16", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr16 = :cr16")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro16", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro16 = :caro16")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr16", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr16 = :fcr16")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr17", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr17 = :cr17")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro17", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro17 = :caro17")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr17", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr17 = :fcr17")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr18", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr18 = :cr18")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro18", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro18 = :caro18")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr18", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr18 = :fcr18")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr19", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr19 = :cr19")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro19", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro19 = :caro19")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr19", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr19 = :fcr19")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr110", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr110 = :cr110")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro110", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro110 = :caro110")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr110", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr110 = :fcr110")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr22", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr22 = :cr22")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro22", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro22 = :caro22")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr22", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr22 = :fcr22")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr24", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr24 = :cr24")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro24", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro24 = :caro24")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr24", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr24 = :fcr24")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr25", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr25 = :cr25")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro25", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro25 = :caro25")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr25", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr25 = :fcr25")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr26", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr26 = :cr26")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro26", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro26 = :caro26")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr26", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr26 = :fcr26")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr27", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr27 = :cr27")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro27", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro27 = :caro27")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr27", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr27 = :fcr27")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr28", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr28 = :cr28")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro28", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro28 = :caro28")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr28", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr28 = :fcr28")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr29", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr29 = :cr29")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro29", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro29 = :caro29")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr29", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr29 = :fcr29")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr210", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr210 = :cr210")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro210", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro210 = :caro210")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr210", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr210 = :fcr210")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr32", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr32 = :cr32")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro32", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro32 = :caro32")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr32", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr32 = :fcr32")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr34", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr34 = :cr34")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro34", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro34 = :caro34")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr34", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr34 = :fcr34")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr35", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr35 = :cr35")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro35", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro35 = :caro35")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr35", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr35 = :fcr35")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr36", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr36 = :cr36")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro36", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro36 = :caro36")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr36", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr36 = :fcr36")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr37", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr37 = :cr37")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro37", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro37 = :caro37")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr37", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr37 = :fcr37")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr38", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr38 = :cr38")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro38", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro38 = :caro38")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr38", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr38 = :fcr38")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr39", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr39 = :cr39")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro39", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro39 = :caro39")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr39", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr39 = :fcr39")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr310", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr310 = :cr310")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro310", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro310 = :caro310")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr310", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr310 = :fcr310")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr42", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr42 = :cr42")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro42", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro42 = :caro42")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr42", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr42 = :fcr42")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr43", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr43 = :cr43")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro43", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro43 = :caro43")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr43", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr43 = :fcr43")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr44", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr44 = :cr44")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro44", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro44 = :caro44")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr44", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr44 = :fcr44")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr45", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr45 = :cr45")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro45", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro45 = :caro45")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr45", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr45 = :fcr45")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr46", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr46 = :cr46")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro46", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro46 = :caro46")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr46", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr46 = :fcr46")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr47", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr47 = :cr47")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro47", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro47 = :caro47")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr47", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr47 = :fcr47")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr48", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr48 = :cr48")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro48", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro48 = :caro48")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr48", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr48 = :fcr48")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr49", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr49 = :cr49")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro49", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro49 = :caro49")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr49", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr49 = :fcr49")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr410", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr410 = :cr410")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro410", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro410 = :caro410")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr410", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr410 = :fcr410")
    , @NamedQuery(name = "CalificacionesAlumno.findByCm5", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cm5 = :cm5")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcm5", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcm5 = :fcm5")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr51", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr51 = :cr51")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro51", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro51 = :caro51")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr51", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr51 = :fcr51")
    , @NamedQuery(name = "CalificacionesAlumno.findByCm6", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cm6 = :cm6")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcm6", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcm6 = :fcm6")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr61", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr61 = :cr61")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro61", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro61 = :caro61")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr61", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr61 = :fcr61")
    , @NamedQuery(name = "CalificacionesAlumno.findByCm7", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cm7 = :cm7")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcm7", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcm7 = :fcm7")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr71", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr71 = :cr71")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro71", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro71 = :caro71")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr71", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr71 = :fcr71")
    , @NamedQuery(name = "CalificacionesAlumno.findByCm8", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cm8 = :cm8")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcm8", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcm8 = :fcm8")
    , @NamedQuery(name = "CalificacionesAlumno.findByCr81", query = "SELECT c FROM CalificacionesAlumno c WHERE c.cr81 = :cr81")
    , @NamedQuery(name = "CalificacionesAlumno.findByCaro81", query = "SELECT c FROM CalificacionesAlumno c WHERE c.caro81 = :caro81")
    , @NamedQuery(name = "CalificacionesAlumno.findByFcr81", query = "SELECT c FROM CalificacionesAlumno c WHERE c.fcr81 = :fcr81")})
public class CalificacionesAlumno implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CalificacionesAlumnoPK calificacionesAlumnoPK;
    @Column(name = "fcf")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcf;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cf")
    private BigDecimal cf;
    @Column(name = "valida")
    private Boolean valida;
    @Column(name = "cm1")
    private BigDecimal cm1;
    @Column(name = "fcm1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcm1;
    @Column(name = "cr1_1")
    private BigDecimal cr11;
    @Column(name = "caro1_1")
    private Integer caro11;
    @Column(name = "fcr1_1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr11;
    @Column(name = "cm2")
    private BigDecimal cm2;
    @Column(name = "fcm2")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcm2;
    @Column(name = "cr2_1")
    private BigDecimal cr21;
    @Column(name = "caro2_1")
    private Integer caro21;
    @Column(name = "fcr2_1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr21;
    @Column(name = "cm3")
    private BigDecimal cm3;
    @Column(name = "fcm3")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcm3;
    @Column(name = "cr3_1")
    private BigDecimal cr31;
    @Column(name = "caro3_1")
    private Integer caro31;
    @Column(name = "fcr3_1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr31;
    @Column(name = "exento")
    private Boolean exento;
    @Column(name = "cfr1")
    private BigDecimal cfr1;
    @Column(name = "carf1")
    private Integer carf1;
    @Column(name = "fcfr1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcfr1;
    @Column(name = "cr1_3")
    private BigDecimal cr13;
    @Column(name = "caro1_3")
    private Integer caro13;
    @Column(name = "fcr1_3")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr13;
    @Column(name = "cr2_3")
    private BigDecimal cr23;
    @Column(name = "caro2_3")
    private Integer caro23;
    @Column(name = "fcr2_3")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr23;
    @Column(name = "cr3_3")
    private BigDecimal cr33;
    @Column(name = "caro3_3")
    private Integer caro33;
    @Column(name = "fcr3_3")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr33;
    @Column(name = "cm4")
    private BigDecimal cm4;
    @Column(name = "fcm4")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcm4;
    @Column(name = "cr4_1")
    private BigDecimal cr41;
    @Column(name = "caro4_1")
    private Integer caro41;
    @Column(name = "fcr4_1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr41;
    @Column(name = "cr1_2")
    private BigDecimal cr12;
    @Column(name = "caro1_2")
    private Integer caro12;
    @Column(name = "fcr1_2")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr12;
    @Column(name = "cr1_4")
    private BigDecimal cr14;
    @Column(name = "caro1_4")
    private Integer caro14;
    @Column(name = "fcr1_4")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr14;
    @Column(name = "cr1_5")
    private BigDecimal cr15;
    @Column(name = "caro1_5")
    private Integer caro15;
    @Column(name = "fcr1_5")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr15;
    @Column(name = "cr1_6")
    private BigDecimal cr16;
    @Column(name = "caro1_6")
    private Integer caro16;
    @Column(name = "fcr1_6")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr16;
    @Column(name = "cr1_7")
    private BigDecimal cr17;
    @Column(name = "caro1_7")
    private Integer caro17;
    @Column(name = "fcr1_7")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr17;
    @Column(name = "cr1_8")
    private BigDecimal cr18;
    @Column(name = "caro1_8")
    private Integer caro18;
    @Column(name = "fcr1_8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr18;
    @Column(name = "cr1_9")
    private BigDecimal cr19;
    @Column(name = "caro1_9")
    private Integer caro19;
    @Column(name = "fcr1_9")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr19;
    @Column(name = "cr1_10")
    private BigDecimal cr110;
    @Column(name = "caro1_10")
    private Integer caro110;
    @Column(name = "fcr1_10")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr110;
    @Column(name = "cr2_2")
    private BigDecimal cr22;
    @Column(name = "caro2_2")
    private Integer caro22;
    @Column(name = "fcr2_2")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr22;
    @Column(name = "cr2_4")
    private BigDecimal cr24;
    @Column(name = "caro2_4")
    private Integer caro24;
    @Column(name = "fcr2_4")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr24;
    @Column(name = "cr2_5")
    private BigDecimal cr25;
    @Column(name = "caro2_5")
    private Integer caro25;
    @Column(name = "fcr2_5")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr25;
    @Column(name = "cr2_6")
    private BigDecimal cr26;
    @Column(name = "caro2_6")
    private Integer caro26;
    @Column(name = "fcr2_6")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr26;
    @Column(name = "cr2_7")
    private BigDecimal cr27;
    @Column(name = "caro2_7")
    private Integer caro27;
    @Column(name = "fcr2_7")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr27;
    @Column(name = "cr2_8")
    private BigDecimal cr28;
    @Column(name = "caro2_8")
    private Integer caro28;
    @Column(name = "fcr2_8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr28;
    @Column(name = "cr2_9")
    private BigDecimal cr29;
    @Column(name = "caro2_9")
    private Integer caro29;
    @Column(name = "fcr2_9")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr29;
    @Column(name = "cr2_10")
    private BigDecimal cr210;
    @Column(name = "caro2_10")
    private Integer caro210;
    @Column(name = "fcr2_10")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr210;
    @Column(name = "cr3_2")
    private BigDecimal cr32;
    @Column(name = "caro3_2")
    private Integer caro32;
    @Column(name = "fcr3_2")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr32;
    @Column(name = "cr3_4")
    private BigDecimal cr34;
    @Column(name = "caro3_4")
    private Integer caro34;
    @Column(name = "fcr3_4")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr34;
    @Column(name = "cr3_5")
    private BigDecimal cr35;
    @Column(name = "caro3_5")
    private Integer caro35;
    @Column(name = "fcr3_5")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr35;
    @Column(name = "cr3_6")
    private BigDecimal cr36;
    @Column(name = "caro3_6")
    private Integer caro36;
    @Column(name = "fcr3_6")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr36;
    @Column(name = "cr3_7")
    private BigDecimal cr37;
    @Column(name = "caro3_7")
    private Integer caro37;
    @Column(name = "fcr3_7")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr37;
    @Column(name = "cr3_8")
    private BigDecimal cr38;
    @Column(name = "caro3_8")
    private Integer caro38;
    @Column(name = "fcr3_8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr38;
    @Column(name = "cr3_9")
    private BigDecimal cr39;
    @Column(name = "caro3_9")
    private Integer caro39;
    @Column(name = "fcr3_9")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr39;
    @Column(name = "cr3_10")
    private BigDecimal cr310;
    @Column(name = "caro3_10")
    private Integer caro310;
    @Column(name = "fcr3_10")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr310;
    @Column(name = "cr4_2")
    private BigDecimal cr42;
    @Column(name = "caro4_2")
    private Integer caro42;
    @Column(name = "fcr4_2")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr42;
    @Column(name = "cr4_3")
    private BigDecimal cr43;
    @Column(name = "caro4_3")
    private Integer caro43;
    @Column(name = "fcr4_3")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr43;
    @Column(name = "cr4_4")
    private BigDecimal cr44;
    @Column(name = "caro4_4")
    private Integer caro44;
    @Column(name = "fcr4_4")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr44;
    @Column(name = "cr4_5")
    private BigDecimal cr45;
    @Column(name = "caro4_5")
    private Integer caro45;
    @Column(name = "fcr4_5")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr45;
    @Column(name = "cr4_6")
    private BigDecimal cr46;
    @Column(name = "caro4_6")
    private Integer caro46;
    @Column(name = "fcr4_6")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr46;
    @Column(name = "cr4_7")
    private BigDecimal cr47;
    @Column(name = "caro4_7")
    private Integer caro47;
    @Column(name = "fcr4_7")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr47;
    @Column(name = "cr4_8")
    private BigDecimal cr48;
    @Column(name = "caro4_8")
    private Integer caro48;
    @Column(name = "fcr4_8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr48;
    @Column(name = "cr4_9")
    private BigDecimal cr49;
    @Column(name = "caro4_9")
    private Integer caro49;
    @Column(name = "fcr4_9")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr49;
    @Column(name = "cr4_10")
    private BigDecimal cr410;
    @Column(name = "caro4_10")
    private Integer caro410;
    @Column(name = "fcr4_10")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr410;
    @Column(name = "cm5")
    private BigDecimal cm5;
    @Column(name = "fcm5")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcm5;
    @Column(name = "cr5_1")
    private BigDecimal cr51;
    @Column(name = "caro5_1")
    private Integer caro51;
    @Column(name = "fcr5_1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr51;
    @Column(name = "cm6")
    private BigDecimal cm6;
    @Column(name = "fcm6")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcm6;
    @Column(name = "cr6_1")
    private BigDecimal cr61;
    @Column(name = "caro6_1")
    private Integer caro61;
    @Column(name = "fcr6_1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr61;
    @Column(name = "cm7")
    private BigDecimal cm7;
    @Column(name = "fcm7")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcm7;
    @Column(name = "cr7_1")
    private BigDecimal cr71;
    @Column(name = "caro7_1")
    private Integer caro71;
    @Column(name = "fcr7_1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr71;
    @Column(name = "cm8")
    private BigDecimal cm8;
    @Column(name = "fcm8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcm8;
    @Column(name = "cr8_1")
    private BigDecimal cr81;
    @Column(name = "caro8_1")
    private Integer caro81;
    @Column(name = "fcr8_1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fcr81;

    public CalificacionesAlumno() {
    }

    public CalificacionesAlumno(CalificacionesAlumnoPK calificacionesAlumnoPK) {
        this.calificacionesAlumnoPK = calificacionesAlumnoPK;
    }

    public CalificacionesAlumno(int cveGrupo, int cveTurno, int cvePlan, int cveCarrera, int cveDivision, int cveUnidadAcademica, int cveUniversidad, int cvePeriodo, int cveAlumno, String cveMateria) {
        this.calificacionesAlumnoPK = new CalificacionesAlumnoPK(cveGrupo, cveTurno, cvePlan, cveCarrera, cveDivision, cveUnidadAcademica, cveUniversidad, cvePeriodo, cveAlumno, cveMateria);
    }

    public CalificacionesAlumnoPK getCalificacionesAlumnoPK() {
        return calificacionesAlumnoPK;
    }

    public void setCalificacionesAlumnoPK(CalificacionesAlumnoPK calificacionesAlumnoPK) {
        this.calificacionesAlumnoPK = calificacionesAlumnoPK;
    }

    public Date getFcf() {
        return fcf;
    }

    public void setFcf(Date fcf) {
        this.fcf = fcf;
    }

    public BigDecimal getCf() {
        return cf;
    }

    public void setCf(BigDecimal cf) {
        this.cf = cf;
    }

    public Boolean getValida() {
        return valida;
    }

    public void setValida(Boolean valida) {
        this.valida = valida;
    }

    public BigDecimal getCm1() {
        return cm1;
    }

    public void setCm1(BigDecimal cm1) {
        this.cm1 = cm1;
    }

    public Date getFcm1() {
        return fcm1;
    }

    public void setFcm1(Date fcm1) {
        this.fcm1 = fcm1;
    }

    public BigDecimal getCr11() {
        return cr11;
    }

    public void setCr11(BigDecimal cr11) {
        this.cr11 = cr11;
    }

    public Integer getCaro11() {
        return caro11;
    }

    public void setCaro11(Integer caro11) {
        this.caro11 = caro11;
    }

    public Date getFcr11() {
        return fcr11;
    }

    public void setFcr11(Date fcr11) {
        this.fcr11 = fcr11;
    }

    public BigDecimal getCm2() {
        return cm2;
    }

    public void setCm2(BigDecimal cm2) {
        this.cm2 = cm2;
    }

    public Date getFcm2() {
        return fcm2;
    }

    public void setFcm2(Date fcm2) {
        this.fcm2 = fcm2;
    }

    public BigDecimal getCr21() {
        return cr21;
    }

    public void setCr21(BigDecimal cr21) {
        this.cr21 = cr21;
    }

    public Integer getCaro21() {
        return caro21;
    }

    public void setCaro21(Integer caro21) {
        this.caro21 = caro21;
    }

    public Date getFcr21() {
        return fcr21;
    }

    public void setFcr21(Date fcr21) {
        this.fcr21 = fcr21;
    }

    public BigDecimal getCm3() {
        return cm3;
    }

    public void setCm3(BigDecimal cm3) {
        this.cm3 = cm3;
    }

    public Date getFcm3() {
        return fcm3;
    }

    public void setFcm3(Date fcm3) {
        this.fcm3 = fcm3;
    }

    public BigDecimal getCr31() {
        return cr31;
    }

    public void setCr31(BigDecimal cr31) {
        this.cr31 = cr31;
    }

    public Integer getCaro31() {
        return caro31;
    }

    public void setCaro31(Integer caro31) {
        this.caro31 = caro31;
    }

    public Date getFcr31() {
        return fcr31;
    }

    public void setFcr31(Date fcr31) {
        this.fcr31 = fcr31;
    }

    public Boolean getExento() {
        return exento;
    }

    public void setExento(Boolean exento) {
        this.exento = exento;
    }

    public BigDecimal getCfr1() {
        return cfr1;
    }

    public void setCfr1(BigDecimal cfr1) {
        this.cfr1 = cfr1;
    }

    public Integer getCarf1() {
        return carf1;
    }

    public void setCarf1(Integer carf1) {
        this.carf1 = carf1;
    }

    public Date getFcfr1() {
        return fcfr1;
    }

    public void setFcfr1(Date fcfr1) {
        this.fcfr1 = fcfr1;
    }

    public BigDecimal getCr13() {
        return cr13;
    }

    public void setCr13(BigDecimal cr13) {
        this.cr13 = cr13;
    }

    public Integer getCaro13() {
        return caro13;
    }

    public void setCaro13(Integer caro13) {
        this.caro13 = caro13;
    }

    public Date getFcr13() {
        return fcr13;
    }

    public void setFcr13(Date fcr13) {
        this.fcr13 = fcr13;
    }

    public BigDecimal getCr23() {
        return cr23;
    }

    public void setCr23(BigDecimal cr23) {
        this.cr23 = cr23;
    }

    public Integer getCaro23() {
        return caro23;
    }

    public void setCaro23(Integer caro23) {
        this.caro23 = caro23;
    }

    public Date getFcr23() {
        return fcr23;
    }

    public void setFcr23(Date fcr23) {
        this.fcr23 = fcr23;
    }

    public BigDecimal getCr33() {
        return cr33;
    }

    public void setCr33(BigDecimal cr33) {
        this.cr33 = cr33;
    }

    public Integer getCaro33() {
        return caro33;
    }

    public void setCaro33(Integer caro33) {
        this.caro33 = caro33;
    }

    public Date getFcr33() {
        return fcr33;
    }

    public void setFcr33(Date fcr33) {
        this.fcr33 = fcr33;
    }

    public BigDecimal getCm4() {
        return cm4;
    }

    public void setCm4(BigDecimal cm4) {
        this.cm4 = cm4;
    }

    public Date getFcm4() {
        return fcm4;
    }

    public void setFcm4(Date fcm4) {
        this.fcm4 = fcm4;
    }

    public BigDecimal getCr41() {
        return cr41;
    }

    public void setCr41(BigDecimal cr41) {
        this.cr41 = cr41;
    }

    public Integer getCaro41() {
        return caro41;
    }

    public void setCaro41(Integer caro41) {
        this.caro41 = caro41;
    }

    public Date getFcr41() {
        return fcr41;
    }

    public void setFcr41(Date fcr41) {
        this.fcr41 = fcr41;
    }

    public BigDecimal getCr12() {
        return cr12;
    }

    public void setCr12(BigDecimal cr12) {
        this.cr12 = cr12;
    }

    public Integer getCaro12() {
        return caro12;
    }

    public void setCaro12(Integer caro12) {
        this.caro12 = caro12;
    }

    public Date getFcr12() {
        return fcr12;
    }

    public void setFcr12(Date fcr12) {
        this.fcr12 = fcr12;
    }

    public BigDecimal getCr14() {
        return cr14;
    }

    public void setCr14(BigDecimal cr14) {
        this.cr14 = cr14;
    }

    public Integer getCaro14() {
        return caro14;
    }

    public void setCaro14(Integer caro14) {
        this.caro14 = caro14;
    }

    public Date getFcr14() {
        return fcr14;
    }

    public void setFcr14(Date fcr14) {
        this.fcr14 = fcr14;
    }

    public BigDecimal getCr15() {
        return cr15;
    }

    public void setCr15(BigDecimal cr15) {
        this.cr15 = cr15;
    }

    public Integer getCaro15() {
        return caro15;
    }

    public void setCaro15(Integer caro15) {
        this.caro15 = caro15;
    }

    public Date getFcr15() {
        return fcr15;
    }

    public void setFcr15(Date fcr15) {
        this.fcr15 = fcr15;
    }

    public BigDecimal getCr16() {
        return cr16;
    }

    public void setCr16(BigDecimal cr16) {
        this.cr16 = cr16;
    }

    public Integer getCaro16() {
        return caro16;
    }

    public void setCaro16(Integer caro16) {
        this.caro16 = caro16;
    }

    public Date getFcr16() {
        return fcr16;
    }

    public void setFcr16(Date fcr16) {
        this.fcr16 = fcr16;
    }

    public BigDecimal getCr17() {
        return cr17;
    }

    public void setCr17(BigDecimal cr17) {
        this.cr17 = cr17;
    }

    public Integer getCaro17() {
        return caro17;
    }

    public void setCaro17(Integer caro17) {
        this.caro17 = caro17;
    }

    public Date getFcr17() {
        return fcr17;
    }

    public void setFcr17(Date fcr17) {
        this.fcr17 = fcr17;
    }

    public BigDecimal getCr18() {
        return cr18;
    }

    public void setCr18(BigDecimal cr18) {
        this.cr18 = cr18;
    }

    public Integer getCaro18() {
        return caro18;
    }

    public void setCaro18(Integer caro18) {
        this.caro18 = caro18;
    }

    public Date getFcr18() {
        return fcr18;
    }

    public void setFcr18(Date fcr18) {
        this.fcr18 = fcr18;
    }

    public BigDecimal getCr19() {
        return cr19;
    }

    public void setCr19(BigDecimal cr19) {
        this.cr19 = cr19;
    }

    public Integer getCaro19() {
        return caro19;
    }

    public void setCaro19(Integer caro19) {
        this.caro19 = caro19;
    }

    public Date getFcr19() {
        return fcr19;
    }

    public void setFcr19(Date fcr19) {
        this.fcr19 = fcr19;
    }

    public BigDecimal getCr110() {
        return cr110;
    }

    public void setCr110(BigDecimal cr110) {
        this.cr110 = cr110;
    }

    public Integer getCaro110() {
        return caro110;
    }

    public void setCaro110(Integer caro110) {
        this.caro110 = caro110;
    }

    public Date getFcr110() {
        return fcr110;
    }

    public void setFcr110(Date fcr110) {
        this.fcr110 = fcr110;
    }

    public BigDecimal getCr22() {
        return cr22;
    }

    public void setCr22(BigDecimal cr22) {
        this.cr22 = cr22;
    }

    public Integer getCaro22() {
        return caro22;
    }

    public void setCaro22(Integer caro22) {
        this.caro22 = caro22;
    }

    public Date getFcr22() {
        return fcr22;
    }

    public void setFcr22(Date fcr22) {
        this.fcr22 = fcr22;
    }

    public BigDecimal getCr24() {
        return cr24;
    }

    public void setCr24(BigDecimal cr24) {
        this.cr24 = cr24;
    }

    public Integer getCaro24() {
        return caro24;
    }

    public void setCaro24(Integer caro24) {
        this.caro24 = caro24;
    }

    public Date getFcr24() {
        return fcr24;
    }

    public void setFcr24(Date fcr24) {
        this.fcr24 = fcr24;
    }

    public BigDecimal getCr25() {
        return cr25;
    }

    public void setCr25(BigDecimal cr25) {
        this.cr25 = cr25;
    }

    public Integer getCaro25() {
        return caro25;
    }

    public void setCaro25(Integer caro25) {
        this.caro25 = caro25;
    }

    public Date getFcr25() {
        return fcr25;
    }

    public void setFcr25(Date fcr25) {
        this.fcr25 = fcr25;
    }

    public BigDecimal getCr26() {
        return cr26;
    }

    public void setCr26(BigDecimal cr26) {
        this.cr26 = cr26;
    }

    public Integer getCaro26() {
        return caro26;
    }

    public void setCaro26(Integer caro26) {
        this.caro26 = caro26;
    }

    public Date getFcr26() {
        return fcr26;
    }

    public void setFcr26(Date fcr26) {
        this.fcr26 = fcr26;
    }

    public BigDecimal getCr27() {
        return cr27;
    }

    public void setCr27(BigDecimal cr27) {
        this.cr27 = cr27;
    }

    public Integer getCaro27() {
        return caro27;
    }

    public void setCaro27(Integer caro27) {
        this.caro27 = caro27;
    }

    public Date getFcr27() {
        return fcr27;
    }

    public void setFcr27(Date fcr27) {
        this.fcr27 = fcr27;
    }

    public BigDecimal getCr28() {
        return cr28;
    }

    public void setCr28(BigDecimal cr28) {
        this.cr28 = cr28;
    }

    public Integer getCaro28() {
        return caro28;
    }

    public void setCaro28(Integer caro28) {
        this.caro28 = caro28;
    }

    public Date getFcr28() {
        return fcr28;
    }

    public void setFcr28(Date fcr28) {
        this.fcr28 = fcr28;
    }

    public BigDecimal getCr29() {
        return cr29;
    }

    public void setCr29(BigDecimal cr29) {
        this.cr29 = cr29;
    }

    public Integer getCaro29() {
        return caro29;
    }

    public void setCaro29(Integer caro29) {
        this.caro29 = caro29;
    }

    public Date getFcr29() {
        return fcr29;
    }

    public void setFcr29(Date fcr29) {
        this.fcr29 = fcr29;
    }

    public BigDecimal getCr210() {
        return cr210;
    }

    public void setCr210(BigDecimal cr210) {
        this.cr210 = cr210;
    }

    public Integer getCaro210() {
        return caro210;
    }

    public void setCaro210(Integer caro210) {
        this.caro210 = caro210;
    }

    public Date getFcr210() {
        return fcr210;
    }

    public void setFcr210(Date fcr210) {
        this.fcr210 = fcr210;
    }

    public BigDecimal getCr32() {
        return cr32;
    }

    public void setCr32(BigDecimal cr32) {
        this.cr32 = cr32;
    }

    public Integer getCaro32() {
        return caro32;
    }

    public void setCaro32(Integer caro32) {
        this.caro32 = caro32;
    }

    public Date getFcr32() {
        return fcr32;
    }

    public void setFcr32(Date fcr32) {
        this.fcr32 = fcr32;
    }

    public BigDecimal getCr34() {
        return cr34;
    }

    public void setCr34(BigDecimal cr34) {
        this.cr34 = cr34;
    }

    public Integer getCaro34() {
        return caro34;
    }

    public void setCaro34(Integer caro34) {
        this.caro34 = caro34;
    }

    public Date getFcr34() {
        return fcr34;
    }

    public void setFcr34(Date fcr34) {
        this.fcr34 = fcr34;
    }

    public BigDecimal getCr35() {
        return cr35;
    }

    public void setCr35(BigDecimal cr35) {
        this.cr35 = cr35;
    }

    public Integer getCaro35() {
        return caro35;
    }

    public void setCaro35(Integer caro35) {
        this.caro35 = caro35;
    }

    public Date getFcr35() {
        return fcr35;
    }

    public void setFcr35(Date fcr35) {
        this.fcr35 = fcr35;
    }

    public BigDecimal getCr36() {
        return cr36;
    }

    public void setCr36(BigDecimal cr36) {
        this.cr36 = cr36;
    }

    public Integer getCaro36() {
        return caro36;
    }

    public void setCaro36(Integer caro36) {
        this.caro36 = caro36;
    }

    public Date getFcr36() {
        return fcr36;
    }

    public void setFcr36(Date fcr36) {
        this.fcr36 = fcr36;
    }

    public BigDecimal getCr37() {
        return cr37;
    }

    public void setCr37(BigDecimal cr37) {
        this.cr37 = cr37;
    }

    public Integer getCaro37() {
        return caro37;
    }

    public void setCaro37(Integer caro37) {
        this.caro37 = caro37;
    }

    public Date getFcr37() {
        return fcr37;
    }

    public void setFcr37(Date fcr37) {
        this.fcr37 = fcr37;
    }

    public BigDecimal getCr38() {
        return cr38;
    }

    public void setCr38(BigDecimal cr38) {
        this.cr38 = cr38;
    }

    public Integer getCaro38() {
        return caro38;
    }

    public void setCaro38(Integer caro38) {
        this.caro38 = caro38;
    }

    public Date getFcr38() {
        return fcr38;
    }

    public void setFcr38(Date fcr38) {
        this.fcr38 = fcr38;
    }

    public BigDecimal getCr39() {
        return cr39;
    }

    public void setCr39(BigDecimal cr39) {
        this.cr39 = cr39;
    }

    public Integer getCaro39() {
        return caro39;
    }

    public void setCaro39(Integer caro39) {
        this.caro39 = caro39;
    }

    public Date getFcr39() {
        return fcr39;
    }

    public void setFcr39(Date fcr39) {
        this.fcr39 = fcr39;
    }

    public BigDecimal getCr310() {
        return cr310;
    }

    public void setCr310(BigDecimal cr310) {
        this.cr310 = cr310;
    }

    public Integer getCaro310() {
        return caro310;
    }

    public void setCaro310(Integer caro310) {
        this.caro310 = caro310;
    }

    public Date getFcr310() {
        return fcr310;
    }

    public void setFcr310(Date fcr310) {
        this.fcr310 = fcr310;
    }

    public BigDecimal getCr42() {
        return cr42;
    }

    public void setCr42(BigDecimal cr42) {
        this.cr42 = cr42;
    }

    public Integer getCaro42() {
        return caro42;
    }

    public void setCaro42(Integer caro42) {
        this.caro42 = caro42;
    }

    public Date getFcr42() {
        return fcr42;
    }

    public void setFcr42(Date fcr42) {
        this.fcr42 = fcr42;
    }

    public BigDecimal getCr43() {
        return cr43;
    }

    public void setCr43(BigDecimal cr43) {
        this.cr43 = cr43;
    }

    public Integer getCaro43() {
        return caro43;
    }

    public void setCaro43(Integer caro43) {
        this.caro43 = caro43;
    }

    public Date getFcr43() {
        return fcr43;
    }

    public void setFcr43(Date fcr43) {
        this.fcr43 = fcr43;
    }

    public BigDecimal getCr44() {
        return cr44;
    }

    public void setCr44(BigDecimal cr44) {
        this.cr44 = cr44;
    }

    public Integer getCaro44() {
        return caro44;
    }

    public void setCaro44(Integer caro44) {
        this.caro44 = caro44;
    }

    public Date getFcr44() {
        return fcr44;
    }

    public void setFcr44(Date fcr44) {
        this.fcr44 = fcr44;
    }

    public BigDecimal getCr45() {
        return cr45;
    }

    public void setCr45(BigDecimal cr45) {
        this.cr45 = cr45;
    }

    public Integer getCaro45() {
        return caro45;
    }

    public void setCaro45(Integer caro45) {
        this.caro45 = caro45;
    }

    public Date getFcr45() {
        return fcr45;
    }

    public void setFcr45(Date fcr45) {
        this.fcr45 = fcr45;
    }

    public BigDecimal getCr46() {
        return cr46;
    }

    public void setCr46(BigDecimal cr46) {
        this.cr46 = cr46;
    }

    public Integer getCaro46() {
        return caro46;
    }

    public void setCaro46(Integer caro46) {
        this.caro46 = caro46;
    }

    public Date getFcr46() {
        return fcr46;
    }

    public void setFcr46(Date fcr46) {
        this.fcr46 = fcr46;
    }

    public BigDecimal getCr47() {
        return cr47;
    }

    public void setCr47(BigDecimal cr47) {
        this.cr47 = cr47;
    }

    public Integer getCaro47() {
        return caro47;
    }

    public void setCaro47(Integer caro47) {
        this.caro47 = caro47;
    }

    public Date getFcr47() {
        return fcr47;
    }

    public void setFcr47(Date fcr47) {
        this.fcr47 = fcr47;
    }

    public BigDecimal getCr48() {
        return cr48;
    }

    public void setCr48(BigDecimal cr48) {
        this.cr48 = cr48;
    }

    public Integer getCaro48() {
        return caro48;
    }

    public void setCaro48(Integer caro48) {
        this.caro48 = caro48;
    }

    public Date getFcr48() {
        return fcr48;
    }

    public void setFcr48(Date fcr48) {
        this.fcr48 = fcr48;
    }

    public BigDecimal getCr49() {
        return cr49;
    }

    public void setCr49(BigDecimal cr49) {
        this.cr49 = cr49;
    }

    public Integer getCaro49() {
        return caro49;
    }

    public void setCaro49(Integer caro49) {
        this.caro49 = caro49;
    }

    public Date getFcr49() {
        return fcr49;
    }

    public void setFcr49(Date fcr49) {
        this.fcr49 = fcr49;
    }

    public BigDecimal getCr410() {
        return cr410;
    }

    public void setCr410(BigDecimal cr410) {
        this.cr410 = cr410;
    }

    public Integer getCaro410() {
        return caro410;
    }

    public void setCaro410(Integer caro410) {
        this.caro410 = caro410;
    }

    public Date getFcr410() {
        return fcr410;
    }

    public void setFcr410(Date fcr410) {
        this.fcr410 = fcr410;
    }

    public BigDecimal getCm5() {
        return cm5;
    }

    public void setCm5(BigDecimal cm5) {
        this.cm5 = cm5;
    }

    public Date getFcm5() {
        return fcm5;
    }

    public void setFcm5(Date fcm5) {
        this.fcm5 = fcm5;
    }

    public BigDecimal getCr51() {
        return cr51;
    }

    public void setCr51(BigDecimal cr51) {
        this.cr51 = cr51;
    }

    public Integer getCaro51() {
        return caro51;
    }

    public void setCaro51(Integer caro51) {
        this.caro51 = caro51;
    }

    public Date getFcr51() {
        return fcr51;
    }

    public void setFcr51(Date fcr51) {
        this.fcr51 = fcr51;
    }

    public BigDecimal getCm6() {
        return cm6;
    }

    public void setCm6(BigDecimal cm6) {
        this.cm6 = cm6;
    }

    public Date getFcm6() {
        return fcm6;
    }

    public void setFcm6(Date fcm6) {
        this.fcm6 = fcm6;
    }

    public BigDecimal getCr61() {
        return cr61;
    }

    public void setCr61(BigDecimal cr61) {
        this.cr61 = cr61;
    }

    public Integer getCaro61() {
        return caro61;
    }

    public void setCaro61(Integer caro61) {
        this.caro61 = caro61;
    }

    public Date getFcr61() {
        return fcr61;
    }

    public void setFcr61(Date fcr61) {
        this.fcr61 = fcr61;
    }

    public BigDecimal getCm7() {
        return cm7;
    }

    public void setCm7(BigDecimal cm7) {
        this.cm7 = cm7;
    }

    public Date getFcm7() {
        return fcm7;
    }

    public void setFcm7(Date fcm7) {
        this.fcm7 = fcm7;
    }

    public BigDecimal getCr71() {
        return cr71;
    }

    public void setCr71(BigDecimal cr71) {
        this.cr71 = cr71;
    }

    public Integer getCaro71() {
        return caro71;
    }

    public void setCaro71(Integer caro71) {
        this.caro71 = caro71;
    }

    public Date getFcr71() {
        return fcr71;
    }

    public void setFcr71(Date fcr71) {
        this.fcr71 = fcr71;
    }

    public BigDecimal getCm8() {
        return cm8;
    }

    public void setCm8(BigDecimal cm8) {
        this.cm8 = cm8;
    }

    public Date getFcm8() {
        return fcm8;
    }

    public void setFcm8(Date fcm8) {
        this.fcm8 = fcm8;
    }

    public BigDecimal getCr81() {
        return cr81;
    }

    public void setCr81(BigDecimal cr81) {
        this.cr81 = cr81;
    }

    public Integer getCaro81() {
        return caro81;
    }

    public void setCaro81(Integer caro81) {
        this.caro81 = caro81;
    }

    public Date getFcr81() {
        return fcr81;
    }

    public void setFcr81(Date fcr81) {
        this.fcr81 = fcr81;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calificacionesAlumnoPK != null ? calificacionesAlumnoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalificacionesAlumno)) {
            return false;
        }
        CalificacionesAlumno other = (CalificacionesAlumno) object;
        if ((this.calificacionesAlumnoPK == null && other.calificacionesAlumnoPK != null) || (this.calificacionesAlumnoPK != null && !this.calificacionesAlumnoPK.equals(other.calificacionesAlumnoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.CalificacionesAlumno[ calificacionesAlumnoPK=" + calificacionesAlumnoPK + " ]";
    }
    
}
