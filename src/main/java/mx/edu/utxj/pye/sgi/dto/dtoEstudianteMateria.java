package mx.edu.utxj.pye.sgi.dto;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;

/**
 * Dto para cidentificar las materias que estan cursando los estudiantes ya sean Sauiit o control escolar
 */
public class dtoEstudianteMateria {
    @Getter @Setter String claveMateria;
    @Getter @Setter String nombreMateria;
    @Getter @Setter Personal docenteImparte;
    @Getter @Setter EvaluacionDocentesMateriaResultados resultados;

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
