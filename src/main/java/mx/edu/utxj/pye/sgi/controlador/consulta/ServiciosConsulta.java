package mx.edu.utxj.pye.sgi.controlador.consulta;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import edu.mx.utxj.pye.seut.util.preguntas.Pregunta;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.consulta.DtoSatisfaccionServiciosCuestionario;
import mx.edu.utxj.pye.sgi.dto.consulta.DtoSatisfaccionServiciosEncuesta;
import mx.edu.utxj.pye.sgi.dto.consulta.ServiciosConsultaDto;
import mx.edu.utxj.pye.sgi.ejb.consulta.EjbSatisfaccionServiciosConsulta;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class ServiciosConsulta extends ViewScopedRol implements Desarrollable {
    @Getter @Setter private ServiciosConsultaDto dto;
    @EJB EjbPropiedades ep;
    @EJB EjbValidacionRol ejbValidacionRol;
    @EJB EjbSatisfaccionServiciosConsulta ejbSatisfaccionServiciosConsulta;
    @Inject LogonMB logon;
    @Inject Caster caster;
    @Getter Boolean tieneAcceso = false;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "servicios consulta";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    @PostConstruct
    public void init(){
        try{
            setVistaControlador(ControlEscolarVistaControlador.SATISFACCION_SERVICIOS_CONSULTA);
            ResultadoEJB<Filter<PersonalActivo>> validarTienePOA = ejbValidacionRol.validarTienePOA(logon.getPersonal().getClave());
            if(!validarTienePOA.getCorrecto()) {mostrarMensajeResultadoEJB(validarTienePOA); return;}

            dto = new ServiciosConsultaDto(validarTienePOA.getValor());
            tieneAcceso = dto.tieneAcceso(validarTienePOA.getValor().getEntity());

            ////////////////////////////////

            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            ResultadoEJB<DtoSatisfaccionServiciosCuestionario> generarCuestionario = ejbSatisfaccionServiciosConsulta.generarCuestionario();
            if(!generarCuestionario.getCorrecto()){mostrarMensajeResultadoEJB(generarCuestionario); return;}
            dto.setCuestionario(generarCuestionario.getValor());

            ResultadoEJB<List<Evaluaciones>> buscarEvaluaciones = ejbSatisfaccionServiciosConsulta.buscarEvaluaciones();
            System.out.println("buscarEvaluaciones = " + buscarEvaluaciones);
            if(!buscarEvaluaciones.getCorrecto()) {mostrarMensajeResultadoEJB(buscarEvaluaciones); return;}
            else dto.setEvaluaciones(buscarEvaluaciones.getValor());

            System.out.println("dto.hayEvaluacion() = " + dto.hayEvaluacion());
            if(dto.hayEvaluacion()) getContenedor(dto.getEvaluacionSeleccionada());
            /*ResultadoEJB<DtoSatisfaccionServiciosEncuesta> calcularConcentrado = ejbSatisfaccionServiciosConsulta.calcularConcentrado(dto.getEvaluacionSeleccionada());
            System.out.println("calcularConcentrado = " + calcularConcentrado);
            if(!calcularConcentrado.getCorrecto()){mostrarMensajeResultadoEJB(calcularConcentrado); return;}
            dto.getContenedores().put(dto.getEvaluacionSeleccionada(), calcularConcentrado.getValor());*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized DtoSatisfaccionServiciosEncuesta getContenedor(Evaluaciones evaluacion){
        System.out.println("ServiciosConsulta.getContenedor");
        System.out.println("dto.hayContenedor(evaluacion) = " + dto.hayContenedor(evaluacion));
        if(dto.hayContenedor(evaluacion)) return dto.getContenedor();

        ResultadoEJB<DtoSatisfaccionServiciosEncuesta> calcularConcentrado = ejbSatisfaccionServiciosConsulta.calcularConcentrado(dto.getEvaluacionSeleccionada(), dto.getCuestionario());
        if(!calcularConcentrado.getCorrecto()){mostrarMensajeResultadoEJB(calcularConcentrado); return null;}
        dto.getContenedores().put(evaluacion, calcularConcentrado.getValor());
        dto.hayContenedor(evaluacion);//preguntar si hay contenedor y cambiar el contenedor seleccionado
        return dto.getContenedor();
    }

    public void cambiarEvaluacion(){
        if(dto.hayEvaluacion()){
            getContenedor(dto.getEvaluacionSeleccionada());
        }
    }

    public List<BigDecimal> getRespuestas(){
        return Arrays.asList(new BigDecimal(5), new BigDecimal(4), new BigDecimal(3), new BigDecimal(2), new BigDecimal(1), new BigDecimal(0));
    }

    public Long calcularFrecuencia(Evaluaciones evaluacion, Pregunta pregunta, BigDecimal respuesta){
        if(!dto.hayContenedor(evaluacion)){
            mostrarMensaje("No se encontró contenedor para la evaluación seleccionada.");
            return 0L;
        }
        ResultadoEJB<DtoSatisfaccionServiciosEncuesta.ConteoInstitucional> calcularFrecuenciaInstitucional = ejbSatisfaccionServiciosConsulta.calcularFrecuenciaInstitucional(dto.getContenedor(), pregunta, respuesta);
        if(!calcularFrecuenciaInstitucional.getCorrecto()) mostrarMensajeResultadoEJB(calcularFrecuenciaInstitucional);
        return calcularFrecuenciaInstitucional.getValor().getFrecuencia();
    }
}
