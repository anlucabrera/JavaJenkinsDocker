package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mx.edu.utxj.pye.sgi.controlador.controlEscolar.*;
import mx.edu.utxj.pye.sgi.controlador.reporteBecas;

@RequiredArgsConstructor
public enum ControlEscolarVistaControlador {
    
    ASIGNACION_ACADEMICA("/controlEscolar/director/asignacion_academica.xhtml", AsignacionAcademicaDirector.class),
    CAPTURA_CALIFICACIONES("/controlEscolar/docente/captura_calificaciones.xhtml", CapturaCalificacionesDocente.class),
    REINCORPORACION("/controlEscolar/se/reincorporaciones.xhtml", ReincorporacionServiciosEscolares.class),
    GENERACION_GRUPOS("/controlEscolar/se/generacionGrupos.xhtml", GeneracionGruposServiciosEscolares.class),
    ADMINISTRACION_PLAN_ESTUDIOS("/controlEscolar/director/plan_estudio.xhtml", AdministracionPlanEstudioDirector.class),
    ASIGNACION_TUTOR_ACADEMICO("/controlEscolar/director/asignacion_tutores.xhtml", AsignacionTutorAcademicoDirector.class),
    INSCRIPCION("/controlEscolar/se/procesoInscripcion.xhtml", ProcesoInscripcion.class),
    ASIGNACION_INDICADORES("/controlEscolar/docente/asignacionIndicadoresCriterios.xhtml", AsignacionIndicadoresCriteriosDocente.class),
    CONFIGURACION_UNIDAD_MATERIA("/controlEscolar/docente/configuracionUnidadMateria.xhtml", ConfiguracionUnidadMateriaDocente.class),
    REPORTE_PLANEACION_CUATRIMESTRAL("/controlEscolar/docente/planeacionCuatrimestral.xhtml", PlaneacionCuatrimestralImpresion.class),
    PERMISO_APERTURA_EXTEMPORANEA("/controlEscolar/pye/permisoAperturaExtemporanea.xhtml", PermisoAperturaExtemporaneaAdministrador.class),
    PASE_DE_LISTA("/controlEscolar/docente/paseLista.xhtml", PaseListaDoc.class),
    PASE_DE_LISTA_TUTOR("/controlEscolar/tutor/seguimientoPaseListaTutor.xhtml", PaseListaSegTutor.class),
    REGISTRAR_BAJAS("/controlEscolar/se/registrarBajas.xhtml", RegistrarBajaServiciosEscolares.class),
    REPORTE_BECAS("/becas/reporteBecas.xhtml", reporteBecas.class),
    CREDENCIALIZACION("/controlEscolar/se/credencializacion.xhtml",credencializacionSE.class),
    VALIDACION_CAPTURA_CALIFICACIONES("/controlEscolar/director/validacion_captura_calificaciones.xhtml", ValidacionCapturaCalificacionDirector.class),
    VALIDACION_COMENTARIOS_TUTOR("/controlEscolar/tutor/validacion_comentarios.xhtml", ValidaComentariosTutor.class),
    CAPTURA_COMENTARIOS_DOCENTE("/controlEscolar/docente/captura_comentarios.xhtml", CapturaComentariosDocente.class),
    TRAMITAR_BAJAS("/controlEscolar/tutor/tramitarBajas.xhtml", TramitarBajaTutor.class),
    DICTAMEN_BAJAS("/controlEscolar/psicopedagogia/dictamenBaja.xhtml",  DictamenBajaPsicopedagogia.class),
    VALIDACION_BAJAS("/controlEscolar/director/validacionBaja.xhtml",  ValidacionBajaDirector.class),
    LISTAS_ASISTENCIA("/controlEscolar/director/ListasAsistencias.xhtml",  ValidacionBajaDirector.class),
    REPORTE_BAJAS("/controlEscolar/se/reporteBajas.xhtml",  ReporteBajasServiciosEscolares.class),
    REPORTE_ASISTENCIA_TUTOR("/controlEscolar/tutor/reporteAsistenciasTutor.xhtml",  ReporteAsistenciasTutor.class),
    CONSULTA_CALIFICACION_ESTUDIANTE("/controlEscolar/estudiante/consultarCalificaciones.xhtml",  ConsultaCalificacionesEstudiante.class),
    VALIDACION_PLANEACION_CUATRIMESTRAL("/controlEscolar/docente/planeacionCuatrimestralValidacion.xhtml", PlaneacionCuatrimestralValidacion.class),
    REPORTE_ASISTENCIA_DIRECTOR("/controlEscolar/director/reporteAsistenciasDirector.xhtml",  ReporteAsistenciasDirector.class),
    CEDULA_IDENTIFICACION("/controlEscolar/psicopedagogia/cedulaIdentificacion.xhtml",cedulaIdentificacionPsicopedagogia.class),
    HISTORIAL_MOVEST("/controlEscolar/se/historialMovEstudiante.xhtml",  HistorialMovEstServiciosEscolares.class),
    CONCENTRADO_CALIFICACIONES_TUTOR("/controlEscolar/tutor/concentradoCal.xhtml",  ConcentradoCalificacionesTutor.class),
    CONCENTRADO_CALIFICACIONES_SECACA("/controlEscolar/secAcademica/concentradoCal.xhtml",  ConcentradoCalificacionesSecAca.class),
    FUSION_GRUPOS_DIRECTOR("/controlEscolar/director/fusion_grupos.xhtml",  FusionGruposDirector.class),
    FUSION_GRUPOS_SERVICIOS_ESCOLARES("/controlEscolar/se/fusion_grupos.xhtml",  FusionGruposServiciosEscolares.class),
    CUESTIONARIO_PSICOPEDAGOGICO("/controlEscolar/estudiante/cuestionarioPsicopedagogico.xhtml",CuestionarioPsicopedagogicoEstudiante.class);
    @Getter @NonNull private final String vista;
    @Getter @NonNull private final Class controlador;
}
