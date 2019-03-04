package mx.edu.utxj.pye.sgi.enums.rol;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TramitesRol {
  OPERATIVO("Operativo"), SUPERIOR("Superior"), FISCALIZACION("Fiscalización");
  @Getter @NonNull private final String label;
}
