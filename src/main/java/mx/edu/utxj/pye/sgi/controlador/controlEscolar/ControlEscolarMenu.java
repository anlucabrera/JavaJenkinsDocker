package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.funcional.Desplegable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Named
@ViewScoped
public class ControlEscolarMenu implements Desarrollable, Desplegable {
    @EJB EjbPropiedades ep;
//    @Inject AsignacionAcademicaDirector asignacionAcademicaDirector;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "Control Escolar";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }

    @Override
    public Boolean tieneElementos() {
        Boolean get = Faces.evaluateExpressionGet("#{asignacionAcademicaDirector.tieneAcceso}");if(get) return true;
        get = Faces.evaluateExpressionGet("#{reincorporacionServiciosEscolares.tieneAcceso}");if(get)return true;
        get = Faces.evaluateExpressionGet("#{administracionAlineacionEducativaIdiomas.tieneAcceso}");if(get)return true;
        get = Faces.evaluateExpressionGet("#{asignacionTutorAcademico.tieneAcceso}");if(get)return true;
        get = Faces.evaluateExpressionGet("#{generacionGruposServiciosEscolares.tieneAcceso}");if(get)return true;
        get = Faces.evaluateExpressionGet("#{asignacionAcademicaDirector.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{capturaCalificacionesDocente.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{configuracionUnidadMateriaDocente.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{asignacionIndicadoresCriteriosDocente.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{permisoAperturaExtemporaneaAdministrador.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registrarBajaServiciosEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{tramitarBajaTutor.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{dictamenBajaPsicopedagogia.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{validacionBajaDirector.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{reporteBajasServiciosEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{credencializacionSE.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{paseListaDoc.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{paseListaSegTutor.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{listasAsistenciaDirector.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{reporteAsistenciasTutor.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{reporteAsistenciasDirector.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{reporteAsistenciasDirector.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{consultaCalificacionesEstudiante.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cedulaIdentificacionPsicopedagogia.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{historialMovEstServiciosEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{fusionGruposDirector.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{fusionGruposServiciosEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cuestionarioPsicopedagogicoEstudiante.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{avanceProgramaticoDirector.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{avanceProgramaticoDocente.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{avanceProgramaticoSecAca.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cedulaIdentificacionDirector.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cedulaIdentificacionTutor.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cambioPwdEstudiante.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{perfilEstudiante.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cedulaIdentificacionSE.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroAsesoriaDocente.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroPlanTutoriaTutor.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroTutoriaGrupal.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroTutoriaIndividual.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoCasoCriticoEspecialista.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{validacionPlanAccionTutorialDirector.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{consultaCalificaciones.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{validacionTutoriaGrupalEstudiante.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{prestamoDocumentoServiciosEscolares.tieneAcceso}"); if(get) return true;  
        get = Faces.evaluateExpressionGet("#{reinscripcionAutonomaEstudiante.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{consultaPlanesAccionTutoralCoordinadorTutores.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{validacionPlanAccionTutorialPsicopedagogia.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{fichaAdmisionReporte.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{casosCriticosEstudiante.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{evaluacionDesempenioAmbiental.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{administracionEvaluacionAmbiental.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{reinscripcionExtemporaneaSE.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{plantillaPlanAccionTutorialPsicopedagogia.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroNotificacionesGeneral.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroAsesoriaEstudianteGeneral.tieneAcceso}");if(get) return true;
        get = Faces.evaluateExpressionGet("#{concentradoBajasAreasVarias.tieneAcceso}");if(get) return true;
        get = Faces.evaluateExpressionGet("#{agendaCitaInscripcionAspirante.tieneAcceso}");if(get) return true;
        get = Faces.evaluateExpressionGet("#{cuestionarioComplementarioInfPersonal.tieneAcceso}");if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoCuestionariopsiPsicopedagogia.tieneAcceso}");if(get) return true;
        get = Faces.evaluateExpressionGet("#{administracionEvaluacionesPsicopedagogia.tieneAcceso}");if(get) return true;
        get = Faces.evaluateExpressionGet("#{administracionEvaluacionesFortalecimiento.tieneAcceso}");if(get) return true;
        get = Faces.evaluateExpressionGet("#{reporteEvaluacionesDocente.tieneAcceso}");if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoExpedienteGeneracionTitulacion.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoExpedienteMatriculaTitulacion.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{fechasTerminacionTitulacion.tieneAcceso}"); if(get) return true;        
        get = Faces.evaluateExpressionGet("#{registroExpedienteTitulacion.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registrolDelEstudiante.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroDeEstudianteConsulta.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{resultadosPlaneacionesConsulta.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{segimientoAcademicoConsulta.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroFichaAdmisionSE.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{asignacionRolesEstadiaDirector.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{asignacionEstudiantesEstadiaDocente.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoEstadiaAsesor.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoEstadiaPorEstudiante.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoEstadiaCoordinador.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoEstadiaDirector.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{letreroFotografiasEstudiante.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoEstadiaVinculacion.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{reportesEstadia.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroEventosEstadiaEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{entregaFotografiasEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{aperturaExtemporaneaEstadia.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{gestorEvaluacionesEstadiaEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{evaluacionParesAcademicosFortalecimiento.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{evaluacionParesAcademicosDocente.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoEvParesAcademicos.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{resultadosEvParesAcademicos.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroEvidInstEvalMateriasDireccion.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{reportesAcademicos.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoTestAprendizajeTutor.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoTestAprendizajePsicopedagogia.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroFichaAdmisionAspiranteIngSaiiut.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{validacionFichaIngenieria.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroDocumentosOficialesEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{consultaDocumentosOficialesEstudiante.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroEventosEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{calendarioEventosEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{aperturaEventosEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroEventosTitulacion.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{inscripcionIngenieriaSE.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroFichaAdmisionAspiranteIng.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoEvaluacionEstadiaTutor.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoEvaluacionEstadiaMultiple.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoEvaluacionEstadiaDirector.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cartaNoAdeudoBiblioteca.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cartaNoAdeudoCordinacionEstadia.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cartaNoAdeudoDireccionCarrera.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cartaNoAdeudoEstadistica.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cartaNoAdeudoEstudiante.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cartaNoAdeudoRecursosMateriales.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cartaNoAdeudoSE.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cartaNoAdeudoSeguimientoEgresados.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{cartaNoAdeudoTitulacion.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoEstadiaEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{consultaEmpresasEstadiaVinculacion.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{registroEgresadoTerminacionEstudioServiciosEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{informeIntegralDocente.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{evaluacionCodigoEticaConducta.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{eliminarInscripcionServiciosEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{reportesExpedientesTitulacion.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{seguimientoEvaluacionCodigoEticaDirectivos.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{administracionCatEvidInstEvalEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{administracionCatalogosDocumentosProceso.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{administracionCiclosPeriodosEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{evaluacionDocenteEstudiante.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{testDiagnosticoAprendizajeEstudiante.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{evaluacion360Admin1.tieneAcceso}"); if(get) return true;

        return false;
    }
}
