package mx.edu.utxj.pye.sgi.dto.controlEscolar;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CuestionarioPsicopedagogicoResultados;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CuestionarioPsicopedagogicoRolEstudiante  {

    @Getter @NonNull private Estudiante estudiante;
    @Getter @Setter private List<Apartado> apartados;
    @Getter @Setter private  List<SelectItem>  sino, estadoCivilPadres,famFinado,tipoProblemaFam,tecnicasEstudio,gruposVunerabilidad;
    @Getter @NonNull private Evaluaciones cuestionario;
    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;
    @Getter @Setter private  boolean cargada;
    @Getter @Setter private  Evaluaciones evaluacion;
    @Getter @Setter private CuestionarioPsicopedagogicoResultados resultados;
    @Getter @Setter private boolean finalizado;
    @Getter @Setter private List<String> instrucciones = new ArrayList<>();

    public Boolean tieneAcceso(Estudiante estudiante, UsuarioTipo usuarioTipo){
        if(estudiante == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        return true;
    }
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public void setCuestionario(Evaluaciones cuestionario) {
        this.cuestionario = cuestionario;
    }
    public void setResultados(CuestionarioPsicopedagogicoResultados resultados) {
        this.resultados = resultados;
    }
}
