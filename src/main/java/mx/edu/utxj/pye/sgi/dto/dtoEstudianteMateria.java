package mx.edu.utxj.pye.sgi.dto;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.*;

/**
 * Dto para cidentificar las materias que estan cursando los estudiantes ya sean Sauiit o control escolar
 */
public class dtoEstudianteMateria {
    @Getter @Setter String claveMateria;
    @Getter @Setter String nombreMateria;
    @Getter @Setter Personal docenteImparte;
    @Getter @Setter EvaluacionDocentesMateriaResultados resultados;
    @Getter @Setter EvaluacionDocentesMateriaResultados2 resultados2;
    @Getter @Setter EvaluacionDocentesMateriaResultados3 resultadosTipo2;
    @Getter @Setter EvaluacionDocentesMateriaResultados4 resultadosTipo4;
    @Getter @Setter EvaluacionDocentesMateriaResultados5 resultadosTipo5;


    public dtoEstudianteMateria(String claveMateria, String nombreMateria, Personal docenteImparte) {
        this.claveMateria = claveMateria;
        this.nombreMateria = nombreMateria;
        this.docenteImparte = docenteImparte;
    }

    public dtoEstudianteMateria() {
    }

    @Override
    public String toString() {
        return "dtoEstudianteMateria{" +
                "claveMateria='" + claveMateria + '\'' +
                ", nombreMateria='" + nombreMateria + '\'' +
                ", docenteImparte=" + docenteImparte +
                '}';
    }
}
