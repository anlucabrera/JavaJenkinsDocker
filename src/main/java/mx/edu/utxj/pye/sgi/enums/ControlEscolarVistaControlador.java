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
    REPORTE_BECAS("/becas/reporteBecas.xhtml", reporteBecas.class);
    @Getter @NonNull private final String vista;
    @Getter @NonNull private final Class controlador;
}
