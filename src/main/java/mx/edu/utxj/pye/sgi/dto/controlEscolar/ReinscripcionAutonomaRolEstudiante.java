package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.dtoEstudianteMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReinscripcionAutonomaRolEstudiante {

    @Getter PeriodosEscolares periodoReinscripcion;

    @Getter EventoEscolar eventoReinscripcion;
    @Getter Estudiante estudiante;
    @Getter Grupo nuevoGrupo;
    @Getter Grupo grupoAnterior;
    @Getter Boolean reinscrito, aprobo;
    @Getter List<dtoEstudianteMateria> nuevasMaterias;
    //Datos de contacto
    @Getter MedioComunicacion datosContacto;
    //Datos Familiares
    @Getter DatosFamiliares datosFamiliares;
    //Datos del domicilio del estudiante
    @Getter Domicilio domicilio;

    @Getter List<Estado> estados;
    @Getter List<Municipio> municipios;
    @Getter List<Asentamiento> asentamientos;

    //---
    @Getter @NonNull private List<DtoCalificacionEstudiante.MapUnidadesTematicas> mapUnidadesTematicas;
    @Getter @NonNull private List<DtoCargaAcademica> cargasEstudiante;
    @Getter private Map<DtoCargaAcademica, List<DtoUnidadConfiguracion>> dtoUnidadConfiguracionesMap = new HashMap<>();
    @Getter @NonNull private DtoUnidadesCalificacionEstudiante dtoUnidadesCalificacionEstudiante;
    @Getter private Map<DtoCargaAcademica, DtoUnidadesCalificacionEstudiante> dtoUnidadesCalificacionMap = new HashMap<>();
    @Getter @NonNull private List<DtoCalificacionEstudiante.UnidadesPorMateria> unidadesPorMateria;
    @Getter @Setter private Boolean tieneIntegradora = false;
    @Getter @Setter private Map<DtoCargaAcademica, Boolean> tieneIntegradoraMap = new HashMap<>();
    @Getter @Setter private Map<DtoCargaAcademica, TareaIntegradora> tareaIntegradoraMap = new HashMap<>();

    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;


    @Getter private List<String> instrucciones = new ArrayList<>();

    public Boolean tieneAcceso(Estudiante estudiante, UsuarioTipo usuarioTipo){
        if(estudiante == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        return true;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    public void setNuevoGrupo(Grupo nuevoGrupo) {
        this.nuevoGrupo = nuevoGrupo;
    }

    public void setDatosContacto(MedioComunicacion datosContacto) {
        this.datosContacto = datosContacto;
    }

    public void setDatosFamiliares(DatosFamiliares datosFamiliares) {
        this.datosFamiliares = datosFamiliares;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }

    public void setMunicipios(List<Municipio> municipios) {
        this.municipios = municipios;
    }

    public void setAsentamientos(List<Asentamiento> asentamientos) {
        this.asentamientos = asentamientos;
    }

    public void setInstrucciones(List<String> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public void setEventoReinscripcion(EventoEscolar eventoReinscripcion) { this.eventoReinscripcion = eventoReinscripcion; }

    public void setPeriodoReinscripcion(PeriodosEscolares periodoReinscripcion) {this.periodoReinscripcion = periodoReinscripcion; }

    public void setNuevasMaterias(List<dtoEstudianteMateria> nuevasMaterias) { this.nuevasMaterias = nuevasMaterias; }

    public void setReinscrito(Boolean reinscrito) { this.reinscrito = reinscrito; }

    public void setGrupoAnterior(Grupo grupoAnterior) { this.grupoAnterior = grupoAnterior; }

    public void setAprobo(Boolean aprobo) { this.aprobo = aprobo;
    }

    public void setMapUnidadesTematicas(List<DtoCalificacionEstudiante.MapUnidadesTematicas> mapUnidadesTematicas) {
        this.mapUnidadesTematicas = mapUnidadesTematicas;
    }

    public void setCargasEstudiante(List<DtoCargaAcademica> cargasEstudiante) {
        this.cargasEstudiante = cargasEstudiante;
    }

    public void setDtoUnidadConfiguracionesMap(Map<DtoCargaAcademica, List<DtoUnidadConfiguracion>> dtoUnidadConfiguracionesMap) {
        this.dtoUnidadConfiguracionesMap = dtoUnidadConfiguracionesMap;
    }

    public void setDtoUnidadesCalificacionEstudiante(DtoUnidadesCalificacionEstudiante dtoUnidadesCalificacionEstudiante) {
        this.dtoUnidadesCalificacionEstudiante = dtoUnidadesCalificacionEstudiante;
    }

    public void setDtoUnidadesCalificacionMap(Map<DtoCargaAcademica, DtoUnidadesCalificacionEstudiante> dtoUnidadesCalificacionMap) {
        this.dtoUnidadesCalificacionMap = dtoUnidadesCalificacionMap;
    }

    public void setUnidadesPorMateria(List<DtoCalificacionEstudiante.UnidadesPorMateria> unidadesPorMateria) {
        this.unidadesPorMateria = unidadesPorMateria;
    }

    public void setTieneIntegradora(Boolean tieneIntegradora) {
        this.tieneIntegradora = tieneIntegradora;
    }

    public void setTieneIntegradoraMap(Map<DtoCargaAcademica, Boolean> tieneIntegradoraMap) {
        this.tieneIntegradoraMap = tieneIntegradoraMap;
    }

    public void setTareaIntegradoraMap(Map<DtoCargaAcademica, TareaIntegradora> tareaIntegradoraMap) {
        this.tareaIntegradoraMap = tareaIntegradoraMap;
    }
}
