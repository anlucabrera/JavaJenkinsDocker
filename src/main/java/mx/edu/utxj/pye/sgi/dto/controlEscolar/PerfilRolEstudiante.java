package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosFamiliares;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutorFamiliar;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

import java.util.ArrayList;
import java.util.List;

public class PerfilRolEstudiante {

    @Getter @NonNull private Estudiante estudiante;
    @Getter @Setter private List<String> instrucciones = new ArrayList<>();

    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;
    @Getter @Setter DatosFamiliares datosFamiliares;
    @Getter @Setter MedioComunicacion medioComunicacion;
    @Getter @Setter TutorFamiliar tutorFamiliar;
    @Getter @Setter String pwdActual,pwdNueva, pwdNuevaEncript;
    @Getter @Setter List<Apartado> apartados;
    @Getter @Setter DtoCedulaIdentificacion cedulaIdentificacion;

    public Boolean tieneAcceso(Estudiante estudiante, UsuarioTipo usuarioTipo){
        if(estudiante == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        return true;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}
