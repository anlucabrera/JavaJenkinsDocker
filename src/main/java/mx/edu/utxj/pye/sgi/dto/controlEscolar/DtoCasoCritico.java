package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CasoCritico;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoEstado;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoTipo;

import java.io.Serializable;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoCasoCritico implements Serializable {
    @Getter @Setter @NonNull private CasoCritico casoCritico;
    @Getter @NonNull private CasoCriticoTipo tipo;
    @Getter @Setter @NonNull private CasoCriticoEstado estado;
    @Getter @Setter @NonNull private DtoEstudiante dtoEstudiante;
    @Getter @Setter @NonNull private DtoCargaAcademica dtoCargaAcademica;
    @Getter @Setter @NonNull private DtoUnidadConfiguracion dtoUnidadConfiguracion;

    public DtoCasoCritico(CasoCritico casoCritico, CasoCriticoTipo tipo, CasoCriticoEstado estado, DtoEstudiante dtoEstudiante) {
        this.casoCritico = casoCritico;
        this.tipo = tipo;
        this.estado = estado;
        this.dtoEstudiante = dtoEstudiante;
    }
    
    public void setTipo(CasoCriticoTipo tipo) {
        this.tipo = tipo;
        casoCritico.setTipo(tipo.getLabel());
    }
    
    public static String toLabel(DtoCasoCritico dtoCasoCritico){
        if(dtoCasoCritico.getCasoCritico().getConfiguracion() == null || dtoCasoCritico.getCasoCritico().getCarga() == null){
            return String.valueOf(dtoCasoCritico.getCasoCritico().getCaso())
                    .concat("-")
                    .concat(dtoCasoCritico.getCasoCritico().getEstado())
                    .concat("-")
                    .concat(dtoCasoCritico.getCasoCritico().getTipo())
                    .concat("- Descripción: -")
                    .concat(dtoCasoCritico.getCasoCritico().getDescripcion());
        }else{
            return String.valueOf(dtoCasoCritico.getCasoCritico().getCaso())
                    .concat("-")
                    .concat(dtoCasoCritico.getCasoCritico().getEstado())
                    .concat("-")
                    .concat(dtoCasoCritico.getCasoCritico().getTipo())
                    .concat("- Unidad: -")
                    .concat(String.valueOf(dtoCasoCritico.getCasoCritico().getConfiguracion().getIdUnidadMateria().getNoUnidad())
                            .concat(" - Nombre: -")
                            .concat(dtoCasoCritico.getCasoCritico().getConfiguracion().getIdUnidadMateria().getNombre())
                            .concat(" - Materia: -")
                            .concat(dtoCasoCritico.getCasoCritico().getConfiguracion().getIdUnidadMateria().getIdMateria().getNombre()));
        }       
    }
    
    public static String toLabelTutoriaIndividual(DtoCasoCritico dtoCasoCritico) {
        if (dtoCasoCritico.getCasoCritico().getConfiguracion() == null || dtoCasoCritico.getCasoCritico().getCarga() == null) {
            return String.valueOf(dtoCasoCritico.getCasoCritico().getCaso())
                    .concat("-")
                    .concat(dtoCasoCritico.getCasoCritico().getEstado())
                    .concat("-")
                    .concat(dtoCasoCritico.getCasoCritico().getTipo())
                    .concat(" - Descripción: - ")
                    .concat(dtoCasoCritico.getCasoCritico().getDescripcion())
                    .concat(" - Matricula - ")
                    .concat(String.valueOf(dtoCasoCritico.getCasoCritico().getIdEstudiante().getMatricula()))
                    .concat(" - Nombre - ")
                    .concat(dtoCasoCritico.getCasoCritico().getIdEstudiante().getAspirante().getIdPersona().getApellidoPaterno())
                    .concat(" ")
                    .concat(dtoCasoCritico.getCasoCritico().getIdEstudiante().getAspirante().getIdPersona().getApellidoMaterno())
                    .concat(" ")
                    .concat(dtoCasoCritico.getCasoCritico().getIdEstudiante().getAspirante().getIdPersona().getNombre());
        } else {
            return String.valueOf(dtoCasoCritico.getCasoCritico().getCaso())
                    .concat("-")
                    .concat(dtoCasoCritico.getCasoCritico().getEstado())
                    .concat("-")
                    .concat(dtoCasoCritico.getCasoCritico().getTipo())
                    .concat(" - Unidad: - ")
                    .concat(String.valueOf(dtoCasoCritico.getCasoCritico().getConfiguracion().getIdUnidadMateria().getNoUnidad())
                            .concat(" - Nombre: - ")
                            .concat(dtoCasoCritico.getCasoCritico().getConfiguracion().getIdUnidadMateria().getNombre())
                            .concat(" - Materia: - ")
                            .concat(dtoCasoCritico.getCasoCritico().getConfiguracion().getIdUnidadMateria().getIdMateria().getNombre()))
                    .concat(" - Matricula - ")
                    .concat(String.valueOf(dtoCasoCritico.getCasoCritico().getIdEstudiante().getMatricula()))
                    .concat(" - Nombre - ")
                    .concat(dtoCasoCritico.getCasoCritico().getIdEstudiante().getAspirante().getIdPersona().getApellidoPaterno())
                    .concat(" ")
                    .concat(dtoCasoCritico.getCasoCritico().getIdEstudiante().getAspirante().getIdPersona().getApellidoMaterno())
                    .concat(" ")
                    .concat(dtoCasoCritico.getCasoCritico().getIdEstudiante().getAspirante().getIdPersona().getNombre());
        }
    }
}
