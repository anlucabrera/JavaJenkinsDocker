package mx.edu.utxj.pye.sgi.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RolNotificacion {
    TODOS(1D, "Todos"),     
    PERSONAL(1D, "Personal"),
    ALUMNOS(1D, "Alumnos"),
    NADIEOCULTA(1D, "Nadie / Oculta");
    @Getter @NonNull    private Double nivel;
    @Getter @NonNull    private String label;
    
    public static List<RolNotificacion> ListaNotificacion(){
        return Arrays.asList(RolNotificacion.values())
                .stream()
                .collect(Collectors.toList());
    }
    
    public static List<String> ListaValoresLabel(){
        return Arrays.asList(RolNotificacion.values())
                .stream()
                .map(label -> label.getLabel())
                .collect(Collectors.toList());
    }
}
