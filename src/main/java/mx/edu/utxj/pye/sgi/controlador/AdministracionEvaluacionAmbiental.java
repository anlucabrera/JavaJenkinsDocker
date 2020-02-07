package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.*;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.*;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacionDesempenioAmbiental;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCapturaCalificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCasoCritico;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDesempenioAmbientalUtxj;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoTipo;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Named
@ViewScoped
public class AdministracionEvaluacionAmbiental extends ViewScopedRol implements Desarrollable {
    @Getter @Setter private EvaluacionRolMultiple.EvaluacionRolPersonal rol;
    @EJB EjbEvaluacionDesempenioAmbiental ejb;
    @EJB EjbPropiedades ep;
    @Getter Boolean tieneAcceso = false;

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init(){
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR))return;
        cargado = true;
        setVistaControlador(ControlEscolarVistaControlador.ADMINISTRACION_DESEMPENIO_AMBIENTAL);

        ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarPersonal(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares

        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

        ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarPersonal(logonMB.getPersonal().getClave());

        if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

        Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
        PersonalActivo personalActivo = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
        rol = new EvaluacionRolMultiple.EvaluacionRolPersonal(filtro, personalActivo);

        tieneAcceso = rol.tieneAcceso(personalActivo);

        if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------
        if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución

        if(!tieneAcceso){mostrarMensajeNoAcceso();return;}

        rol.setNivelRol(NivelRol.OPERATIVO);

        //obtenerResultadosEstudiantes();
        //obtenerResultadosPersonal();
    }

    public void obtenerResultadosEstudiantes(){
        try{
            List<DtoAlumnosEncuesta.DtoEvaluaciones> dtoEvaluaciones = Stream
                    .concat(ejb.obtenerListaAlumnosSaiiut().getValor().stream(),
                            ejb.obtenerListaAlumnosControlEscolar().getValor().stream()).collect(Collectors.toList()
                    );
            dtoEvaluaciones.forEach(dtoEvaluacion -> {
                try {
                    EvaluacionDesempenioAmbientalUtxj listaCompletaRes = ejb.obtenerResultadoEvaluacion(dtoEvaluacion.getClave()).getValor();

                    if (listaCompletaRes != null) {
                        Integer clave = dtoEvaluacion.getClave();
                        String nombreAlumno = dtoEvaluacion.getNombreCompleto();
                        String area = dtoEvaluacion.getAreaOprogramaEducativo();
                        if (rol.getComparador().isCompleto(listaCompletaRes)) {
                            DtoAlumnosEncuesta.DtoEvaluaciones evaluacionCompleta = new DtoAlumnosEncuesta.DtoEvaluaciones(clave, nombreAlumno, area);
                            rol.getEvaluacionesCompletas().add(evaluacionCompleta);
                        }
                        if (!rol.getComparador().isCompleto(listaCompletaRes)) {
                            DtoAlumnosEncuesta.DtoEvaluaciones evaluacionInCompleta = new DtoAlumnosEncuesta.DtoEvaluaciones(clave, nombreAlumno, area);
                            rol.getEvaluacionesInCompletas().add(evaluacionInCompleta);

                        }
                    } else {
                        DtoAlumnosEncuesta.DtoEvaluaciones evaluacionNoContestaron = new DtoAlumnosEncuesta.DtoEvaluaciones(dtoEvaluacion.getClave(), dtoEvaluacion.getNombreCompleto(),
                                dtoEvaluacion.getAreaOprogramaEducativo());
                        rol.getEvaluacionesInCompletas().add(evaluacionNoContestaron);

                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEvaluacionAmbiental.class.getName()).log(Level.SEVERE, null, e);
                }
            });
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEvaluacionAmbiental.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void obtenerResultadosPersonal(){
        try{
            List<DtoAlumnosEncuesta.DtoEvaluaciones> dtoEvaluaciones = ejb.obtenerListaPersonalActivo().getValor();
            dtoEvaluaciones.forEach(dtoEvaluacion -> {
                try {
                    EvaluacionDesempenioAmbientalUtxj listaCompletaRes = ejb.obtenerResultadoEvaluacion(dtoEvaluacion.getClave()).getValor();

                    if (listaCompletaRes != null) {
                        Integer clave = dtoEvaluacion.getClave();
                        String nombreAlumno = dtoEvaluacion.getNombreCompleto();
                        String area = dtoEvaluacion.getAreaOprogramaEducativo();
                        if (rol.getComparador().isCompleto(listaCompletaRes)) {
                            DtoAlumnosEncuesta.DtoEvaluaciones evaluacionCompleta = new DtoAlumnosEncuesta.DtoEvaluaciones(clave, nombreAlumno, area);
                            rol.getEvaluacionesCompletasPersonal().add(evaluacionCompleta);
                        }
                        if (!rol.getComparador().isCompleto(listaCompletaRes)) {
                            DtoAlumnosEncuesta.DtoEvaluaciones evaluacionInCompleta = new DtoAlumnosEncuesta.DtoEvaluaciones(clave, nombreAlumno, area);
                            rol.getEvaluacionesInCompletasPersonal().add(evaluacionInCompleta);

                        }
                    } else {
                        DtoAlumnosEncuesta.DtoEvaluaciones evaluacionNoContestaron = new DtoAlumnosEncuesta.DtoEvaluaciones(dtoEvaluacion.getClave(), dtoEvaluacion.getNombreCompleto(),
                                dtoEvaluacion.getAreaOprogramaEducativo());
                        rol.getEvaluacionesInCompletasPersonal().add(evaluacionNoContestaron);

                    }
                } catch (Throwable e) {
                    Logger.getLogger(AdministracionEvaluacionAmbiental.class.getName()).log(Level.SEVERE, null, e);
                }
            });
        } catch (Throwable e) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(AdministracionEvaluacionAmbiental.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public BigDecimal porcentajeAvance(){
        BigDecimal porcentaje = BigDecimal.ZERO;
        List<DtoAlumnosEncuesta.DtoEvaluaciones> dtoEvaluaciones = Stream
                .concat(ejb.obtenerListaAlumnosSaiiut().getValor().stream(),
                        ejb.obtenerListaAlumnosControlEscolar().getValor().stream())
                .collect(Collectors.toList());
        List<DtoAlumnosEncuesta.DtoEvaluaciones> dtoEvaluaciones1 = Stream
                .concat(dtoEvaluaciones.stream(),
                        ejb.obtenerListaPersonalActivo().getValor().stream())
                .collect(Collectors.toList());
        BigDecimal totalRegistrosEnBD = new BigDecimal(dtoEvaluaciones1.size());
        System.out.println("Registros en DB:"+ totalRegistrosEnBD);
        BigDecimal totalEvaluaciones = new BigDecimal(ejb.obtenerTotalResultadosCompletos().getValor());
        System.out.println("Registros de evaluaciones:"+ totalEvaluaciones);
        porcentaje = totalEvaluaciones
                .multiply(new BigDecimal(100))
                .divide(totalRegistrosEnBD, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        System.out.println("Porcentaje:"+porcentaje);
        return porcentaje;
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administración evaluación ambiental";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
