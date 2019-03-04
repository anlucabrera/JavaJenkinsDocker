package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum PersonalFiltro {
    CLAVE("clave"),
    AREA_OPERATIVA("Área_operativa"),
    AREA_OFICIAL("Área_oficial"),
    AREA_SUPERIOR("Área_superior"),
    ACTIIVIDAD("Actividad"),
    CATEGORIA_OPERATIVA("Categoria_operativa"),
    CATEGORIA_OFICIAL("Categoría_oficial"),
    TIENE_POA("Tiene_POA"),
    NO_TIENE_POA("No_tiene_POA");
    @Getter @Setter @NonNull private String label;
}
