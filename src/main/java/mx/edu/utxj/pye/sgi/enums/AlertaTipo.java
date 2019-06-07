package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum AlertaTipo {
    CORRECTO        ("alert alert-success", "alert", 5, 10, 2),
    INFORMATIVO     ("alert alert-info",    "alert", 5, 10, 2),
    SUGERENCIA      ("alert alert-warning", "alert", 5, 10, 2),
    ERROR           ("alert alert-danger",  "alert", 5, 10, 2);
    @Setter @Getter @NonNull private String label, role;
    @Setter @Getter @NonNull private Integer vertical, horizontal, margen;
}
