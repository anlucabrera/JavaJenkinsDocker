package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mx.edu.utxj.pye.sgi.controlador.*;
import mx.edu.utxj.pye.sgi.controlador.consulta.ServiciosConsulta;
import mx.edu.utxj.pye.sgi.controlador.controlEscolar.*;
import mx.edu.utxj.pye.sgi.controlador.evaluaciones.AdministracionEvaluacionDocente;
import mx.edu.utxj.pye.sgi.controlador.evaluaciones.AdministracionEvaluacionTutor;
import mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionTutor2;
import mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionTutorEstudiante;
import mx.edu.utxj.pye.sgi.controlador.reporteBecas;
import mx.edu.utxj.pye.sgi.controlador.controlEscolar.ValidacionTutoriaGrupalEstudiante;
import mx.edu.utxj.pye.sgi.entity.ch.CuestionarioComplementarioInformacionPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados2;

@RequiredArgsConstructor
public enum ControlEscolarVistaControlador {
    PERMISO_APERTURA_EXTEMPORANEA("/controlEscolar/pye/permisoAperturaExtemporanea.xhtml", PermisoAperturaExtemporaneaAdministrador.class),
    
    CONCENTRADO_CALIFICACIONES_SECACA("/controlEscolar/secAcademica/concentradoCal.xhtml",  ConcentradoCalificacionesSecAca.class),
    AVANCE_PROGRAMATICO_SECACA("/controlEscolar/secAcademica/avanceP.xhtml",  AvanceProgramaticoSecAca.class),
    
    ASIGNACION_ACADEMICA("/controlEscolar/director/asignacion_academica.xhtml", AsignacionAcademicaDirector.class),
    ADMINISTRACION_PLAN_ESTUDIOS("/controlEscolar/director/plan_estudio.xhtml", AdministracionPlanEstudioDirector.class),
    ASIGNACION_TUTOR_ACADEMICO("/controlEscolar/director/asignacion_tutores.xhtml", AsignacionTutorAcademicoDirector.class),
    VALIDACION_CAPTURA_CALIFICACIONES("/controlEscolar/director/validacion_captura_calificaciones.xhtml", ValidacionCapturaCalificacionDirector.class),
    VALIDACION_BAJAS("/controlEscolar/director/validacionBaja.xhtml",  ValidacionBajaDirector.class),
    LISTAS_ASISTENCIA("/controlEscolar/director/ListasAsistencias.xhtml",  ListasAsistenciaDirector.class),
    REPORTE_ASISTENCIA_DIRECTOR("/controlEscolar/director/reporteAsistenciasDirector.xhtml",  ReporteAsistenciasDirector.class),
    FUSION_GRUPOS_DIRECTOR("/controlEscolar/director/fusion_grupos.xhtml",  FusionGruposDirector.class),    
    CONCENTRADO_CALIFICACIONES_DIRECTOR("/controlEscolar/director/concentradoCal.xhtml",  ConcentradoCalificacionesDirector.class),
    AVANCE_PROGRAMATICO_DIRECTOR("/controlEscolar/director/avanceP.xhtml",  AvanceProgramaticoDirector.class),
    CEDULA_IDENTIFICACION_DIRECTOR("/controlEscolar/director/cedulaIdentificacionDirector.xhtml",CedulaIdentificacionDirector.class),
    VALIDACION_PLANEACION_CUATRIMESTRAL("/controlEscolar/director/planeacionCuatrimestralValidacion.xhtml", PlaneacionCuatrimestralValidacion.class),
    VALIDACION_PLAN_ACCION_TUTORIAL_DIRECTOR("/controlEscolar/director/validacion_plan_accion_tutorial.xhtml", ValidacionPlanAccionTutorialDirector.class),
    SEGUIMIENTO_CUESTIONARIO_PSICOPEDAGOGICO_DIRECTOR("/controlEscolar/director/seguimientoCuestionarioPsicopedagogicoDirector.xhtml", SeguimientoCuestionariopsiDirector.class),


    REASIGNACION_ACADEMICA("/controlEscolar/se/asignacion_academica.xhtml",  AsignacionAcademicaEscolares.class),
    REINCORPORACION("/controlEscolar/se/reincorporaciones.xhtml", ReincorporacionServiciosEscolares.class),
    GENERACION_GRUPOS("/controlEscolar/se/generacionGrupos.xhtml", GeneracionGruposServiciosEscolares.class),
    INSCRIPCION("/controlEscolar/se/procesoInscripcion.xhtml", ProcesoInscripcion.class),
    REGISTRAR_BAJAS("/controlEscolar/se/registrarBajas.xhtml", RegistrarBajaServiciosEscolares.class),
    CREDENCIALIZACION("/controlEscolar/se/credencializacion.xhtml",credencializacionSE.class),
    REPORTE_BAJAS("/controlEscolar/se/reporteBajas.xhtml",  ReporteBajasServiciosEscolares.class),
    HISTORIAL_MOVEST("/controlEscolar/se/historialMovEstudiante.xhtml",  HistorialMovEstServiciosEscolares.class),
    FUSION_GRUPOS_SERVICIOS_ESCOLARES("/controlEscolar/se/fusion_grupos.xhtml",  FusionGruposServiciosEscolares.class),
    CEDULA_IDENTIFICACION_SE("/controlEscolar/se/cedulaIdentificacionSE.xhtm",  CedulaIdentificacionSE.class),
    PRESTAMO_DOCUMENTOS("/controlEscolar/se/prestamoDocumentos.xhtml", PrestamoDocumentoServiciosEscolares.class),
    REINSCRIPCION_SEGUIMIENTO("/controlEscolar/se/reinscripcionSeguimientoSE.xhtml",ReinscripcionExtemporaneaSE.class),
    CONCENTRADO_BAJAS("/controlEscolar/se/concentradorBajas.xhtml",  ConcentradoBajasAreasVarias.class),

    DICTAMEN_BAJAS("/controlEscolar/psicopedagogia/dictamenBaja.xhtml",  DictamenBajaPsicopedagogia.class),
    CEDULA_IDENTIFICACION("/controlEscolar/psicopedagogia/cedulaIdentificacion.xhtml",cedulaIdentificacionPsicopedagogia.class),
    VALIDACION_PLAN_ACCION_TUTORIAL_PSICOPEDAGOGIA("/controlEscolar/psicopedagogia/validacion_plan_accion_tutorial_psicopedagogia.xhtml",ValidacionPlanAccionTutorialPsicopedagogia.class),
    REGISTRO_PlANTILLA_PLAN_ACCION_TUTORIAL_PSICOPEDAGOGIA("/controlEscolar/psicopedagogia/plantilla_plan_accion_tutorial.xhtml", PlantillaPlanAccionTutorialPsicopedagogia.class),
    SEGUIMIENTO_CUESTIONARIO_PSICOPEDAGOGICO("/controlEscolar/psicopedagogia/seguimientoCuestionarioPsicopedagogico.xhtml", SeguimientoCuestionariopsiPsicopedagogia.class),


    PASE_DE_LISTA_TUTOR("/controlEscolar/tutor/seguimientoPaseListaTutor.xhtml", PaseListaSegTutor.class),
    VALIDACION_COMENTARIOS_TUTOR("/controlEscolar/tutor/validacion_comentarios.xhtml", ValidaComentariosTutor.class),
    TRAMITAR_BAJAS("/controlEscolar/tutor/tramitarBajas.xhtml", TramitarBajaTutor.class),
    REPORTE_ASISTENCIA_TUTOR("/controlEscolar/tutor/reporteAsistenciasTutor.xhtml",  ReporteAsistenciasTutor.class),
    CONCENTRADO_CALIFICACIONES_TUTOR("/controlEscolar/tutor/concentradoCal.xhtml",  ConcentradoCalificacionesTutor.class),
    CEDULA_IDENTIFICACION_TUTOR("/controlEscolar/tutor/cedulaIdentificacionTutor.xhtml",CedulaIdentificacionTutor.class),
    PLAN_ACCION_TUTORIAL("/controlEscolar/tutor/plan_accion_tutorial.xhtml", RegistroPlanTutoriaTutor.class),
    REGISTRO_TUTORIA_GRUPAL("/controlEscolar/tutor/registro_tutoria_grupal.xhtml", RegistroTutoriaGrupal.class),
    REGISTRO_TUTORIA_INDIVIDUAL("/controlEscolar/tutor/registro_tutoria_individual.xhtml", RegistroTutoriaIndividual.class),
    CONSULTA_PLANES_ACCION_TUTORIAL_COORDINADOR_TUTORES("/controlEscolar/tutor/consulta_plan_accion_tutorial_tutores.xhtml", ConsultaPlanesAccionTutoralCoordinadorTutores.class),
    SEGUIMIENTO_CUESTIONARIO_PSICOPEDAGOGICO_TUTOR("/controlEscolar/tutor/seguimientoCuestionarioPsicopedagogicoTutor.xhtml", SeguimientoCuestionariopsiTutor.class),


    CAPTURA_CALIFICACIONES("/controlEscolar/docente/captura_calificaciones.xhtml", CapturaCalificacionesDocente.class),
    CAPTURA_TAREA_INTEGRADORA("/controlEscolar/docente/captura_integradora.xhtml", CapturaTareaIntegradoraDocente.class),
    ASIGNACION_INDICADORES("/controlEscolar/docente/asignacionIndicadoresCriterios.xhtml", AsignacionIndicadoresCriteriosDocente.class),
    CONFIGURACION_UNIDAD_MATERIA("/controlEscolar/docente/configuracionUnidadMateria.xhtml", ConfiguracionUnidadMateriaDocente.class),
    REPORTE_PLANEACION_CUATRIMESTRAL("/controlEscolar/docente/planeacionCuatrimestral.xhtml", PlaneacionCuatrimestralImpresion.class),
    PASE_DE_LISTA("/controlEscolar/docente/paseLista.xhtml", PaseListaDoc.class),
    CAPTURA_COMENTARIOS_DOCENTE("/controlEscolar/docente/captura_comentarios.xhtml", CapturaComentariosDocente.class),
    AVANCE_PROGRAMATICO_DOCENTE("/controlEscolar/docente/avanceP.xhtml",  AvanceProgramaticoDocente.class),
    REGISTRO_ASESORIA("/controlEscolar/docente/registro_asesorias.xhtml", RegistroAsesoriaDocente.class),
    
    REPORTE_BECAS("/becas/reporteBecas.xhtml", reporteBecas.class),
    SEGUIMIENTO_EV_TUTOR("/evaluaciones/administracion/administracionEvaluacionTutor.xhtml", AdministracionEvaluacionTutor.class),
    SEGUIMIENTO_EV_DOCENTE("/evaluaciones/administracion/administracionEvaluacionDocente.xhtml", AdministracionEvaluacionDocente.class),
    EVALAUCION_TUTOR("/evaluaciones/evaluacion/tutores2.xhtml", EvaluacionTutor2.class),
    EVALUACION_DOCENTE("/evaluaciones/evaluacion/tutores2.xhtml", EvaluacionDoncenteMateriaControler.class),
    CONSULTA_CALIFICACION_ESTUDIANTE("/controlEscolar/estudiante/consultarCalificaciones.xhtml",  ConsultaCalificacionesEstudiante.class),
    CUESTIONARIO_PSICOPEDAGOGICO("/controlEscolar/estudiante/cuestionarioPsicopedagogico.xhtml",CuestionarioPsicopedagogicoEstudiante.class),
    CAMBIO_PWD("/controlEscolar/estudiante/CambioPwdEstudiante.xhtml",CambioPwdEstudiante.class),
    PERFIL_ESTUDIANTE("/controlEscolar/estudiante/modificacionDatosEstudiante.xhtml",PerfilEstudiante.class),        
    TUTORIAS_GRUPALES_ESTUDIANTE("/controlEscolar/estudiante/tutorias_grupales.xhtml",ValidacionTutoriaGrupalEstudiante.class),
    REINSCRIPCION_AUTONOMA("/controlEscolar/estudiante/reinscripcionAutonoma.xhtml",ReinscripcionAutonomaEstudiante.class),
    CONSULTA_CASOS_CRITICOS("/controlEscolar/estudiante/casosCriticos.xhtml",CasosCriticosEstudiante.class),
    EVALAUCION_TUTOR_NUEVA("/evaluaciones/evaluacion/nuevaTutor.xhtml", EvaluacionTutorEstudiante.class),
    EVALUACION_DOCENTE2("/evaluaciones/evaluacion/evaluacionDocente.xhtml", EvaluacionDocentesMateriaResultados2.class),

    REGISTRO_FICHA_ASPIRANTE("/controlEscolar/aspirante/registroFichaAspirante.xhtml", RegistroFichaAdmisionAspirante.class),
    CARGA_DOCUMENTOS_ASPIRANTE("/controlEscolar/aspirante/cargaDocumentos.xhtml", CargaDocumentosAspirante.class),
    AGENDA_CITA_ASPIRANTE("/controlEscolar/aspirante/agendaCita.xhtml", AgendaCitaInscripcionAspirante.class),

    SEGUIMIENTO_CASO_CRITICO_ESPECIALISTA("/controlEscolar/especialista/seguimiento_caso_critico_especialista.xhtml", SeguimientoCasoCriticoEspecialista.class),

    CONSULTA_CALIFICACION("/controlEscolar/consultarCalificaciones.xhtml", ConsultaCalificaciones.class),

    FICHA_ADMISION_REPORTE("/controlEscolar/reporteFichaAdmision.xhtml",FichaAdmisionReporte.class),

    EVALUACION_CONOCIMIENTO_CUMPLIMIENTO("/encuestas/personal/evaluacionCodigosDeEticaConducta.xhtml", EvaluacionConocimientoCodigoEtica.class),
    CUESTIONARIO_COMPLEMENTARIO_INF_PERSONAL("/cuestionarios/cuestionarioComplemenarioInfPersonal.xhtml", CuestionarioComplementarioInformacionPersonal.class),


    EVALUACION_DESEMPENIO_AMBIENTAL("/encuestas/evaluacionDesempenioAmbiental.xhtml", EvaluacionDesempenioAmbiental.class),

    ENCUESTA_SERVICIOS("/encuestas/Estudiantes/servicios.xhtml", EncuestaServiciosEstudiante19.class),

    REGISTRO_NOTIFICACIONES("/controlEscolar/registro_notificaciones.xhtml", RegistroNotificacionesGeneral.class),

    REGISTRO_ASESORIAS_GENERAL("/controlEscolar/registro_asesorias_general.xhtml", RegistroAsesoriaEstudianteGeneral.class),

    ADMINISTRACION_DESEMPENIO_AMBIENTAL("/encuestas/seguimiento/administracionEvaluacionAmbiental.xhtml", AdministracionEvaluacionAmbiental.class),
    /////////////////////////////////////////////////////////////////////////////
    SATISFACCION_SERVICIOS_CONSULTA("/encuestas/consulta/servicios.xhtml", ServiciosConsulta.class);
    /////////////////////////////////////////////////////////////////////////////
    @Getter @NonNull private final String vista;
    @Getter @NonNull private final Class controlador;
}
