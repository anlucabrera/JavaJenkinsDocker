/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Registro;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.*;

import java.util.List;

/**
 * Representa todos los datos necesarios para la cedula de idetificacion del estudiante buscado
 * @author UTXJ
 */

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoCedulaIdentificacion {
    //Datos generales
    @Getter @Setter private dtoCedulaIdentificacionDatosGenerales datosGenerales;
    @Getter @Setter private dtoCedulaIdentificacionDatosPersonales datosPersonales;
    //TODO: Datos socieconomicos
    @Getter @Setter private  DtoCedulaIdentificacionDatosSocioeconomicos datosSocioeconomicos;
    //TODO:Datos Escolares
    @Getter @Setter private  DtoCedulaIdentidicacionDatosEscolares datosEscolares;
    //TODO:Datos Familiares
    @Getter @Setter private  DtoCedulaIdentificacionDatosFamiliares datosFamiliares;
    //TODO: Datos de salud
    @Getter @Setter private DtoCedulaIdentifiacionDatosDeSalud datosDeSalud;
    
    
    //DATOS PERSONALES
    @Getter @Setter  private Estudiante estudiante;
    @Getter @Setter private AreasUniversidad programaEducativo;
    @Getter @Setter private String edad;
    @Getter @Setter private Generos sexo;
    @Getter @Setter private Domicilio datosDomicilio;
    //DOMICILIO RESIDENCIA ---
    @Getter @Setter
    Municipio municipioResidencia;
    @Getter @Setter
    Localidad localidadResidencia;
    @Getter @Setter
    Estado estadoResidencia;
    @Getter @Setter
    Asentamiento asentamientoResidencia;
    //Domicilio Procedencia 
    @Getter @Setter
    Municipio municipioProcedencia;
    @Getter @Setter
    Localidad localidadProcedencia;
    @Getter @Setter
    Estado estadoProcedencia;
    @Getter @Setter
    Asentamiento asentamientoProcedencia;
    
    //Datos socioeconomicos
    @Getter @Setter
    EncuestaAspirante datosEncuesta;
    //Datos familiares
    @Getter @Setter
    TutorFamiliar tutorFamiliar;
    @Getter @Setter
    Estado estadoTutor;
    @Getter @Setter
    Municipio municipioTutor;
    @Getter @Setter
    Localidad localidadTutor;
    @Getter @Setter
    Asentamiento asentamientoTutor;
    
    //Datos Escolares
    @Getter @Setter
    PeriodosEscolares periodo;
    @Getter @Setter
    Generaciones generacion;
    @Getter @Setter
    AreasUniversidad carreraPrimeraOpcion;
    @Getter @Setter
    AreasUniversidad carreraSegundaOpcion;
    @Getter @Setter
    Iems bachillerato;
    @Getter @Setter
    Estado estadoBachillerato;
    @Getter @Setter
    Municipio municipioBachillerato;
    @Getter @Setter
    Localidad localidadBachillerato;
        //--Colegiaturas
    @Getter @Setter List<Registro> pagosColegiatura;
    @Getter @Setter List<Registro> pagoTitulacion;
    //--Tutor del grupo
    @Getter @Setter
    Personal tutorGrupo;
    //-- Carga Academica
    @Getter @Setter List<CargaAcademica> cargaAcademica;
    //Asistencia a tutoria
//    @Getter @Setter List<TutoriasIndividuales> asistenciaTutorias;
        //Asitencia a asesorias
    @Getter @Setter List<Asesoria> asistenciaAsesoria;
    
    //--Calificaciones
    
     
    //Datos de asistencia de eventos (Culturales- deportivas - emprendedores
    @Getter @Setter List<Viewparticipantesactformintegral>actividadesDeportivas;
    @Getter @Setter List<Viewparticipantesactformintegral> actividadesCulturales;
    @Getter @Setter List<Viewparticipantesactformintegral> actividadesEmprendedores;
    
    //--Datos de Salud
    @Getter @Setter
    DatosMedicos datosMedicos;
    
    
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
