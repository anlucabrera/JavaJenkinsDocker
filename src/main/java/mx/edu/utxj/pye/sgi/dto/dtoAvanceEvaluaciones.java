package mx.edu.utxj.pye.sgi.dto;


import lombok.Getter;
import lombok.Setter;

public class dtoAvanceEvaluaciones {
    @Getter @Setter int totalCompletos;
    @Getter @Setter int totalIncompletos;
    @Getter @Setter int totalNoIngreso;
    @Getter @Setter Double porcentaje;
    @Getter @Setter int totalEstudiantes;
    @Getter @Setter String pe;

    public dtoAvanceEvaluaciones() {
    }

    public dtoAvanceEvaluaciones(int totalCompletos, int totalIncompletos, int totalNoIngreso, Double porcentaje, int totalEstudiantes, String pe) {
        this.totalCompletos = totalCompletos;
        this.totalIncompletos = totalIncompletos;
        this.totalIncompletos = totalIncompletos;
        this.totalNoIngreso = totalNoIngreso;
        this.porcentaje = porcentaje;
        this.totalEstudiantes = totalEstudiantes;
        this.pe = pe;
    }

    @Override
    public String toString() {
        return "dtoAvanceEvaluaciones{" +
                "totalCompletos=" + totalCompletos +
                ", totalIncompletos=" + totalIncompletos +
                ", totalNoIngreso=" + totalNoIngreso +
                ", porcentaje=" + porcentaje +
                ", totalEstudiantes=" + totalEstudiantes +
                ", pe='" + pe + '\'' +
                '}';
    }
}
