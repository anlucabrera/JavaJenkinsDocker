package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Login;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

import java.util.ArrayList;
import java.util.List;

public class CambioPwdRolEstudiante {

    @Getter @NonNull private Estudiante estudiante;
    @Getter @Setter private List<String> instrucciones = new ArrayList<>();

    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;

    @Getter @Setter String pwdActual,pwdNueva, pwdNuevaEncript;

    public Boolean tieneAcceso(Estudiante estudiante, UsuarioTipo usuarioTipo){
        if(estudiante == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        return true;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}
