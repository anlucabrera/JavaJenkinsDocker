package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mx.edu.utxj.pye.sgi.controlador.controlEscolar.*;
import mx.edu.utxj.pye.sgi.controlador.reporteBecas;

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
    LISTAS_ASISTENCIA("/controlEscolar/director/ListasAsistencias.xhtml",  ValidacionBajaDirector.class),
    REPORTE_ASISTENCIA_DIRECTOR("/controlEscolar/director/reporteAsistenciasDirector.xhtml",  ReporteAsistenciasDirector.class),
    FUSION_GRUPOS_DIRECTOR("/controlEscolar/director/fusion_grupos.xhtml",  FusionGruposDirector.class),    
    CONCENTRADO_CALIFICACIONES_DIRECTOR("/controlEscolar/director/concentradoCal.xhtml",  ConcentradoCalificacionesDirector.class),
    AVANCE_PROGRAMATICO_DIRECTOR("/controlEscolar/director/avanceP.xhtml",  AvanceProgramaticoDirector.class),
    CEDULA_IDENTIFICACION_DIRECTOR("/controlEscolar/director/cedulaIdentificacionDirector.xhtml",CedulaIdentificacionDirector.class),
    
    REINCORPORACION("/controlEscolar/se/reincorporaciones.xhtml", ReincorporacionServiciosEscolares.class),
    GENERACION_GRUPOS("/controlEscolar/se/generacionGrupos.xhtml", GeneracionGruposServiciosEscolares.class),
    INSCRIPCION("/controlEscolar/se/procesoInscripcion.xhtml", ProcesoInscripcion.class),
    REGISTRAR_BAJAS("/controlEscolar/se/registrarBajas.xhtml", RegistrarBajaServiciosEscolares.class),
    CREDENCIALIZACION("/controlEscolar/se/credencializacion.xhtml",credencializacionSE.class),
    REPORTE_BAJAS("/controlEscolar/se/reporteBajas.xhtml",  ReporteBajasServiciosEscolares.class),
    HISTORIAL_MOVEST("/controlEscolar/se/historialMovEstudiante.xhtml",  HistorialMovEstServiciosEscolares.class),
    FUSION_GRUPOS_SERVICIOS_ESCOLARES("/controlEscolar/se/fusion_grupos.xhtml",  FusionGruposServiciosEscolares.class),
    CEDULA_IDENTIFICACION_SE("/controlEscolar/se/cedulaIdentificacionSE.xhtm",  CedulaIdentificacionSE.class),
    
    DICTAMEN_BAJAS("/controlEscolar/psicopedagogia/dictamenBaja.xhtml",  DictamenBajaPsicopedagogia.class),
    CEDULA_IDENTIFICACION("/controlEscolar/psicopedagogia/cedulaIdentificacion.xhtml",cedulaIdentificacionPsicopedagogia.class),
    
    PASE_DE_LISTA_TUTOR("/controlEscolar/tutor/seguimientoPaseListaTutor.xhtml", PaseListaSegTutor.class),
    VALIDACION_COMENTARIOS_TUTOR("/controlEscolar/tutor/validacion_comentarios.xhtml", ValidaComentariosTutor.class),
    TRAMITAR_BAJAS("/controlEscolar/tutor/tramitarBajas.xhtml", TramitarBajaTutor.class),
    REPORTE_ASISTENCIA_TUTOR("/controlEscolar/tutor/reporteAsistenciasTutor.xhtml",  ReporteAsistenciasTutor.class),
    CONCENTRADO_CALIFICACIONES_TUTOR("/controlEscolar/tutor/concentradoCal.xhtml",  ConcentradoCalificacionesTutor.class),
    CEDULA_IDENTIFICACION_TUTOR("/controlEscolar/tutor/cedulaIdentificacionTutor.xhtml",CedulaIdentificacionTutor.class),
    
    CAPTURA_CALIFICACIONES("/controlEscolar/docente/captura_calificaciones.xhtml", CapturaCalificacionesDocente.class),
    ASIGNACION_INDICADORES("/controlEscolar/docente/asignacionIndicadoresCriterios.xhtml", AsignacionIndicadoresCriteriosDocente.class),
    CONFIGURACION_UNIDAD_MATERIA("/controlEscolar/docente/configuracionUnidadMateria.xhtml", ConfiguracionUnidadMateriaDocente.class),
    REPORTE_PLANEACION_CUATRIMESTRAL("/controlEscolar/docente/planeacionCuatrimestral.xhtml", PlaneacionCuatrimestralImpresion.class),
    PASE_DE_LISTA("/controlEscolar/docente/paseLista.xhtml", PaseListaDoc.class),
    CAPTURA_COMENTARIOS_DOCENTE("/controlEscolar/docente/captura_comentarios.xhtml", CapturaComentariosDocente.class),
    VALIDACION_PLANEACION_CUATRIMESTRAL("/controlEscolar/docente/planeacionCuatrimestralValidacion.xhtml", PlaneacionCuatrimestralValidacion.class),
    AVANCE_PROGRAMATICO_DOCENTE("/controlEscolar/docente/avanceP.xhtml",  AvanceProgramaticoDocente.class),
    
    REPORTE_BECAS("/becas/reporteBecas.xhtml", reporteBecas.class),
    CONSULTA_CALIFICACION_ESTUDIANTE("/controlEscolar/estudiante/consultarCalificaciones.xhtml",  ConsultaCalificacionesEstudiante.class),
    CUESTIONARIO_PSICOPEDAGOGICO("/controlEscolar/estudiante/cuestionarioPsicopedagogico.xhtml",CuestionarioPsicopedagogicoEstudiante.class),
    CAMBIO_PWD("/controlEscolar/estudiante/CambioPwdEstudiante.xhtml",CambioPwdEstudiante.class),
    PERFIL_ESTUDIANTE("/controlEscolar/estudiante/modificacionDatosEstudiante.xhtml",PerfilEstudiante.class);
    @Getter @NonNull private final String vista;
    @Getter @NonNull private final Class controlador;
}
