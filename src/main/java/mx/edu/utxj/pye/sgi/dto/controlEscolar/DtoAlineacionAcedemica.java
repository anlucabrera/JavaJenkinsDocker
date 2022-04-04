/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AtributoEgreso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CriterioDesempenio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.IndicadorAlineacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ObjetivoEducacional;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
//import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosContinuidad;

/**
 *
 * @author UTXJ
 */
@ToString
@EqualsAndHashCode(of = {"alineacionAcademica"})
public class DtoAlineacionAcedemica implements Serializable {

    @ToString    @EqualsAndHashCode
    public static class Presentacion {
        @Getter        @Setter        private Integer ide;
        @Getter        @Setter        private String clave;
        @Getter        @Setter        private String descripcion;
        @Getter        @Setter        private String nivelA;
        @Getter        @Setter        private Double meta;
        @Getter        @Setter        private PlanEstudio planEstudio;
        @Getter        @Setter        private PlanEstudioMateria planEstudioMateria;
        @Getter        @Setter        private AreasUniversidad universidadPe;
        @Getter        @Setter        private String tipoR;

        public Presentacion(Integer ide, String clave, String descripcion, String nivelA, Double meta, PlanEstudio planEstudio, PlanEstudioMateria planEstudioMateria, AreasUniversidad universidadPe, String tipoR) {
            this.ide = ide;
            this.clave = clave;
            this.descripcion = descripcion;
            this.nivelA = nivelA;
            this.meta = meta;
            this.planEstudio = planEstudio;
            this.planEstudioMateria = planEstudioMateria;
            this.universidadPe = universidadPe;
            this.tipoR = tipoR;
        }

        public int compareTo(Presentacion o) {
            return toLabel(this).compareTo(toLabel(o));
        }

        public static String toLabel(Presentacion p) {
            return String.valueOf(p.getPlanEstudio().getIdPlanEstudio()).concat(" ")
                    .concat(p.getTipoR().concat(" "))
                    .concat(p.getClave());
        }

        public int compareToALC(Presentacion o) {
            return toLabelALC(this).compareTo(toLabelALC(o));
        }

        public static String toLabelALC(Presentacion p) {
            return String.valueOf(p.getPlanEstudio().getIdPlanEstudio()).concat(" ")
                    .concat(p.getPlanEstudioMateria().getClaveMateria().concat(" "))
                    .concat(p.getTipoR().concat(" "))
                    .concat(p.getClave());
        }
    }

    @ToString    @EqualsAndHashCode
    public static class LecturaAlineacion {
        @Getter        @Setter        private PlanEstudioMateria planEstudioMateria;
        @Getter        @Setter        private ObjetivoEducacional objetivoEducacional;
        @Getter        @Setter        private AtributoEgreso atributoEgreso;
        @Getter        @Setter        private IndicadorAlineacion indicador;
        @Getter        @Setter        private CriterioDesempenio criterioDesempenio;

        public LecturaAlineacion(PlanEstudioMateria planEstudioMateria, ObjetivoEducacional objetivoEducacional, AtributoEgreso atributoEgreso, IndicadorAlineacion indicador, CriterioDesempenio criterioDesempenio) {
            this.planEstudioMateria = planEstudioMateria;
            this.objetivoEducacional = objetivoEducacional;
            this.atributoEgreso = atributoEgreso;
            this.indicador = indicador;
            this.criterioDesempenio = criterioDesempenio;
        }
    }

    @ToString    @EqualsAndHashCode
    public static class PlanesEstudioDto {
        @Getter        @Setter        private PlanEstudio planEstudio;
        @Getter        @Setter        private AreasUniversidad universidad;
//        @Getter        @Setter        private ProgramasEducativosContinuidad continuidad;

        public PlanesEstudioDto(PlanEstudio planEstudio, AreasUniversidad universidad) {
//        public PlanesEstudioDto(PlanEstudio planEstudio, AreasUniversidad universidad, ProgramasEducativosContinuidad continuidad) {
            this.planEstudio = planEstudio;
            this.universidad = universidad;
//            this.continuidad = continuidad;
        }
    }

    @ToString    @EqualsAndHashCode
    public static class PlanesEstudioObjtivo {
        @Getter        @Setter        private PlanEstudio planEstudio;
        @Getter        @Setter        private ObjetivoEducacional educacional;
//        @Getter        @Setter        private String continuidad;
//        @Getter        @Setter        private String nivel;
//        @Getter        @Setter        private String cvl;

        public PlanesEstudioObjtivo(PlanEstudio planEstudio, ObjetivoEducacional educacional) {
//        public PlanesEstudioObjtivo(PlanEstudio planEstudio, ObjetivoEducacional educacional, String continuidad, String nivel, String cvl) {
            this.planEstudio = planEstudio;
            this.educacional = educacional;
//            this.continuidad = continuidad;
//            this.nivel = nivel;
//            this.cvl = cvl;
        }
    }

    @ToString    @EqualsAndHashCode
    public static class PlanesEstudioMateriaCatalogo {
        @Getter        @Setter        private PlanEstudio planEstudio;
        @Getter        @Setter        private PlanEstudioMateria materia;
        @Getter        @Setter        private String filtro;

        public PlanesEstudioMateriaCatalogo(PlanEstudio planEstudio, PlanEstudioMateria materia, String filtro) {
            this.planEstudio = planEstudio;
            this.materia = materia;
            this.filtro = filtro;
        }
    }

    @ToString    @EqualsAndHashCode
    public static class PlanesEstudioAtributo {
        @Getter        @Setter        private PlanEstudio planEstudio;
        @Getter        @Setter        private AtributoEgreso egreso;
//        @Getter        @Setter        private String continuidad;
//        @Getter        @Setter        private String nivel;
//        @Getter        @Setter        private String cvl;

        public PlanesEstudioAtributo(PlanEstudio planEstudio, AtributoEgreso egreso) {
//        public PlanesEstudioAtributo(PlanEstudio planEstudio, AtributoEgreso egreso, String continuidad, String nivel, String cvl) {
            this.planEstudio = planEstudio;
            this.egreso = egreso;
//            this.continuidad = continuidad;
//            this.nivel = nivel;
//            this.cvl = cvl;
        }
    }

    @ToString    @EqualsAndHashCode
    public static class PlanesEstudioCriterio {
        @Getter        @Setter        private PlanEstudio planEstudio;
        @Getter        @Setter        private CriterioDesempenio desempenio;
//        @Getter        @Setter        private String continuidad;
//        @Getter        @Setter        private String nivel;
//        @Getter        @Setter        private String cvl;

        public PlanesEstudioCriterio(PlanEstudio planEstudio, CriterioDesempenio desempenio) {
//        public PlanesEstudioCriterio(PlanEstudio planEstudio, CriterioDesempenio desempenio, String continuidad, String nivel, String cvl) {
            this.planEstudio = planEstudio;
            this.desempenio = desempenio;
//            this.continuidad = continuidad;
//            this.nivel = nivel;
//            this.cvl = cvl;
        }
    }

    @ToString    @EqualsAndHashCode
    public static class PlanesEstudioIndicador {
        @Getter        @Setter        private PlanEstudio planEstudio;
        @Getter        @Setter        private IndicadorAlineacion alineacion;
//        @Getter        @Setter        private String continuidad;
//        @Getter        @Setter        private String nivel;
//        @Getter        @Setter        private String cvl;

        public PlanesEstudioIndicador(PlanEstudio planEstudio, IndicadorAlineacion alineacion) {
//        public PlanesEstudioIndicador(PlanEstudio planEstudio, IndicadorAlineacion alineacion, String continuidad, String nivel, String cvl) {
            this.planEstudio = planEstudio;
            this.alineacion = alineacion;
//            this.continuidad = continuidad;
//            this.nivel = nivel;
//            this.cvl = cvl;
        }
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Grafica {

        @Getter        @Setter        private String indicador;
        @Getter        @Setter        private Double avance;
        @Getter        @Setter        private Double avanceOF;

        public Grafica(String indicador, Double avance, Double avanceOF) {
            this.indicador = indicador;
            this.avance = avance;
            this.avanceOF = avanceOF;
        }
    }
}
