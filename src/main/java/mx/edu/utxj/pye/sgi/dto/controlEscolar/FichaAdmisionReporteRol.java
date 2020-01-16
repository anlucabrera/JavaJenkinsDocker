package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

import java.util.List;

public class FichaAdmisionReporteRol {

    @Getter @NonNull private PersonalActivo personalActivo;
    @Getter @NonNull private AreasUniversidad programa;
    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.CONSULTA;

    //Ultimo proceso de inscripción
    @Getter @NonNull private ProcesosInscripcion procesosInscripcion;
    @Getter @NonNull private List<AreasUniversidad> peActivas;
    // Concentrado
    @Getter @NonNull List<DtoReporteFichaAdmision> concentradoFichas;

    public Boolean tieneAcceso(PersonalActivo personalActivo, UsuarioTipo usuarioTipo){
        if(personalActivo == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.TRABAJADOR)) return false;
        return true;
    }

    public void setPersonalActivo(PersonalActivo personalActivo) {
        this.personalActivo = personalActivo;
    }

    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
    }

    public void setProcesosInscripcion(ProcesosInscripcion procesosInscripcion) {
        this.procesosInscripcion = procesosInscripcion;
    }

    public void setPeActivas(List<AreasUniversidad> peActivas) {
        this.peActivas = peActivas;
    }

    public void setNivelRol(NivelRol nivelRol) { this.nivelRol = nivelRol; }

    public void setConcentradoFichas(List<DtoReporteFichaAdmision> concentradoFichas) {
        this.concentradoFichas = concentradoFichas;
    }
}
