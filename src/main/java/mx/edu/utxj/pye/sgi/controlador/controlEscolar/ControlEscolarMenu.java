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
        get = Faces.evaluateExpressionGet("#{concentradoCalificacionesTutor.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{fusionGruposDirector.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{fusionGruposServiciosEscolares.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{concentradoCalificacionesSecAca.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{concentradoCalificacionesDirector.tieneAcceso}"); if(get) return true;
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
        get = Faces.evaluateExpressionGet("#{consultaCalificacionesCoordinadorAD.tieneAcceso}"); if(get) return true;
        get = Faces.evaluateExpressionGet("#{validacionTutoriaGrupalEstudiante.tieneAcceso}"); if(get) return true;
        
        return false;
    }
}
