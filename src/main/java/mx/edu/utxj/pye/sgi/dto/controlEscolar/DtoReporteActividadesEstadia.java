/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoReporteActividadesEstadia implements Serializable, Comparable<DtoReporteActividadesEstadia>{
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter @NonNull Integer estudiantesIniciaron;
    @Getter @Setter @NonNull Integer estudiantesActivos;
    @Getter @Setter Integer estudiantesAsignados;
    @Getter @Setter Integer estudiantesSinAsignar;
    @Getter @Setter Double porcentajeAsignacion;
    @Getter @Setter Integer estudiantesInformacion;
    @Getter @Setter Integer estudiantesSinInformacion;
    @Getter @Setter Double porcentajeRegistro;
    @Getter @Setter Integer estudiantesValidadosCoordinador;
    @Getter @Setter Integer estudiantesSinValidarCoordinador;
    @Getter @Setter Double porcentajeValidacionCoordinador;
    @Getter @Setter Integer estudiantesValidadosDirector;
    @Getter @Setter Integer estudiantesSinValidarDirector;
    @Getter @Setter Double porcentajeValidacionDirector;

    public DtoReporteActividadesEstadia(AreasUniversidad programaEducativo, Integer estudiantesIniciaron, Integer estudiantesActivos, Integer estudiantesAsignados, Integer estudiantesSinAsignar, Double porcentajeAsignacion, Integer estudiantesInformacion, Integer estudiantesSinInformacion, Double porcentajeRegistro, Integer estudiantesValidadosCoordinador, Integer estudiantesSinValidarCoordinador, Double porcentajeValidacionCoordinador, Integer estudiantesValidadosDirector, Integer estudiantesSinValidarDirector, Double porcentajeValidacionDirector) {
        this.programaEducativo = programaEducativo;
        this.estudiantesIniciaron = estudiantesIniciaron;
        this.estudiantesActivos = estudiantesActivos;
        this.estudiantesAsignados = estudiantesAsignados;
        this.estudiantesSinAsignar = estudiantesSinAsignar;
        this.porcentajeAsignacion = porcentajeAsignacion;
        this.estudiantesInformacion = estudiantesInformacion;
        this.estudiantesSinInformacion = estudiantesSinInformacion;
        this.porcentajeRegistro = porcentajeRegistro;
        this.estudiantesValidadosCoordinador = estudiantesValidadosCoordinador;
        this.estudiantesSinValidarCoordinador = estudiantesSinValidarCoordinador;
        this.porcentajeValidacionCoordinador = porcentajeValidacionCoordinador;
        this.estudiantesValidadosDirector = estudiantesValidadosDirector;
        this.estudiantesSinValidarDirector = estudiantesSinValidarDirector;
        this.porcentajeValidacionDirector = porcentajeValidacionDirector;
    }

    @Override
    public int compareTo(DtoReporteActividadesEstadia o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoReporteActividadesEstadia dtoAsigAsesoresAcadEstadia){
         return dtoAsigAsesoresAcadEstadia.getProgramaEducativo().getNombre();
    }
}
