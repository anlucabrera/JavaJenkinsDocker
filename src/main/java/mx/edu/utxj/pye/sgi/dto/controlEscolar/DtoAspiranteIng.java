package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.UniversidadesUT;
import mx.edu.utxj.pye.sgi.enums.AspiranteTipoIng;
import mx.edu.utxj.pye.sgi.enums.TipoRegistroEstudiante;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;

import java.util.List;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoAspiranteIng {

    @Getter @Setter AspiranteTipoIng tipoAspirante;
    @Getter @Setter Estudiante estudianteCE;
    @Getter @Setter Alumnos estudianteSAIIUT;
    @Getter @Setter String matricula;
    ///////////////////Datos del aspirante///////////////////////
    @Getter @Setter Aspirante aspirante;
    @Getter @Setter UniversidadesUT universidadEgreso;
    @Getter @Setter DatosAcademicos datosAcademicos;
    @Getter @Setter MedioComunicacion medioComunicacion;
    @Getter @Setter DatosMedicos datosMedicos;
    @Getter @Setter DtoCitaAspirante datosCita;
    @Getter @Setter String documentosEstadia;
    ///////////////Datos para la inscripción///////////////////
    @Getter @Setter TipoRegistroEstudiante tipoInscripción;
    @Getter @Setter List<DocumentoEstudianteProceso> documentos;
    @Getter @Setter AreasUniversidad peElegido, peIncrito;
    @Getter @Setter DtoGrupo grupo;
    ///////////////////////////////////
    @Getter @Setter Estudiante estudianteIncrito;
    @Getter @Setter Boolean inscrito,documentosEstadiaCompletos;
}
