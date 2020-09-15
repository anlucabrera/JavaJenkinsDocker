package mx.edu.utxj.pye.sgi.controladores.cmi;

import java.util.List;
import lombok.*;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoCmi {
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Cmi {
        @Getter        @Setter        @NonNull       EjesRegistro EjeR; 
        @Getter        @Setter        @NonNull       List<ActividadesEs> ActividadesE;    
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ActividadesEs {
        @Getter        @Setter        @NonNull        ActividadesPoa Pricipal;
        @Getter        @Setter        @NonNull       List<ActividadesPoa> hijas;  
    }

}