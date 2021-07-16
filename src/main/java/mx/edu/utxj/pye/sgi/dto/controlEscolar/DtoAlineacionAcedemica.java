/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AtributoEgreso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CriterioDesempenio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Indicador;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.IndicadorAlineacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ObjetivoEducacional;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

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

        public PlanesEstudioDto(PlanEstudio planEstudio, AreasUniversidad universidad) {
            this.planEstudio = planEstudio;
            this.universidad = universidad;
        }
    }
    
    @ToString    @EqualsAndHashCode
    public static class PlanesEstudioObjtivo {
        @Getter        @Setter        private PlanEstudio planEstudio;
        @Getter        @Setter        private ObjetivoEducacional educacional;

        public PlanesEstudioObjtivo(PlanEstudio planEstudio, ObjetivoEducacional educacional) {
            this.planEstudio = planEstudio;
            this.educacional = educacional;
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

        public PlanesEstudioAtributo(PlanEstudio planEstudio, AtributoEgreso egreso) {
            this.planEstudio = planEstudio;
            this.egreso = egreso;
        }
    }
    
    @ToString    @EqualsAndHashCode
    public static class PlanesEstudioCriterio {
        @Getter        @Setter        private PlanEstudio planEstudio;
        @Getter        @Setter        private CriterioDesempenio desempenio;

        public PlanesEstudioCriterio(PlanEstudio planEstudio, CriterioDesempenio desempenio) {
            this.planEstudio = planEstudio;
            this.desempenio = desempenio;
        }
    }
    
    @ToString    @EqualsAndHashCode
    public static class PlanesEstudioIndicador {
        @Getter        @Setter        private PlanEstudio planEstudio;
        @Getter        @Setter        private IndicadorAlineacion alineacion;

        public PlanesEstudioIndicador(PlanEstudio planEstudio, IndicadorAlineacion alineacion) {
            this.planEstudio = planEstudio;
            this.alineacion = alineacion;
        }
    }
}
