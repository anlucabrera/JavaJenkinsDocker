package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.io.Serializable;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoCargaAcademica implements Serializable, Comparable<DtoCargaAcademica> {
    @Getter @Setter @NonNull CargaAcademica cargaAcademica;
    @Getter @Setter @NonNull PeriodosEscolares periodo;
    @Getter @Setter @NonNull PersonalActivo docente;
    @Getter @Setter @NonNull Grupo grupo;
    @Getter @Setter @NonNull Materia materia;
    @Getter @Setter @NonNull AreasUniversidad programa;
    @Getter @Setter @NonNull PlanEstudio planEstudio;
    @Getter @Setter @NonNull PlanEstudioMateria planEstudioMateria; 
    
    @Override
    public int compareTo(DtoCargaAcademica o){
        return toLabel(this).compareTo(toLabel(o));
    }
    
    public static String toLabel(DtoCargaAcademica dtoCargaAcademica){
         return dtoCargaAcademica.getPrograma().getSiglas().concat(" - ")
                 .concat(String.valueOf(dtoCargaAcademica.getGrupo().getGrado())).concat(dtoCargaAcademica.getGrupo().getLiteral().toString()).concat(" - ")
                 .concat(dtoCargaAcademica.getMateria().getNombre());
    
    }
    
}
