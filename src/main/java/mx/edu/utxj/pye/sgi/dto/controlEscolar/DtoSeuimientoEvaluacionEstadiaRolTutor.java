/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;

/**
 *
 * @author Planeacion
 */
public class DtoSeuimientoEvaluacionEstadiaRolTutor implements Serializable{
    
    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.CONSULTA;
    @Getter @Setter @NonNull private PersonalActivo personalActivo;
    @Getter @Setter @NonNull private Personas tutor;
    @Getter @Setter @NonNull private Grupo grupo;
    @Getter @Setter @NonNull private Grupos grupos;
    
    @Getter @Setter @NonNull private Boolean esTutor;
    @Getter @Setter @NonNull private Integer periodoActivo;
    @Getter @Setter @NonNull private Integer idGrupo;
    @Getter @Setter @NonNull private String nombre;
    @Getter @Setter @NonNull private String nombre2;
    @Getter @Setter @NonNull private String carpeta;
    @Getter @Setter @NonNull private String subcarpeta;
    @Getter @Setter @NonNull private String libro;
    @Getter @Setter @NonNull private String excel;
    @Getter @Setter @NonNull private String nombreWord;
    @Getter @Setter @NonNull private String word;
    @Getter @Setter @NonNull private String nameWord;
    @Getter @Setter private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    public Boolean tieneAcceso(PersonalActivo personalActivo){
        if(personalActivo == null) return false;
        return true;
    }
    
    public Boolean tieneAcceso(Boolean esTutor){
        return esTutor != false;
    }
    
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoGruposTutor> dtoGrupos = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoGruposTutor> dtoGruposS = new ArrayList<>();
    @Getter @Setter @NonNull private List<String> instrucciones = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> listaDtoEstudiante = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> listaTestCompleto = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> listaTestIncompleto = new ArrayList<>();
    
}
