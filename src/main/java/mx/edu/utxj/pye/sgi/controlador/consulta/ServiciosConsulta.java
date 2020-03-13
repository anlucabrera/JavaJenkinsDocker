package mx.edu.utxj.pye.sgi.controlador.consulta;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import edu.mx.utxj.pye.seut.util.preguntas.Pregunta;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.DtoAreaAcademica;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.consulta.DtoSatisfaccionServiciosCuestionario;
import mx.edu.utxj.pye.sgi.dto.consulta.DtoSatisfaccionServiciosEncuesta;
import mx.edu.utxj.pye.sgi.dto.consulta.ServiciosConsultaDto;
import mx.edu.utxj.pye.sgi.ejb.consulta.EjbSatisfaccionServiciosConsulta;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.SatisfaccionServiciosApartado;
import mx.edu.utxj.pye.sgi.enums.converter.SatisfaccionServiciosApartadoConverter;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.util.Serializador;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
            ResultadoEJB<Filter<PersonalActivo>> validarPersonalActivo = ejbValidacionRol.validarPersonalActivo(logon.getPersonal().getClave());
            if(!validarPersonalActivo.getCorrecto()) {mostrarMensajeResultadoEJB(validarPersonalActivo); return;}

            dto = new ServiciosConsultaDto(validarPersonalActivo.getValor());
            tieneAcceso = dto.tieneAcceso(validarPersonalActivo.getValor().getEntity());

            ////////////////////////////////

            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            ResultadoEJB<DtoSatisfaccionServiciosCuestionario> generarCuestionario = ejbSatisfaccionServiciosConsulta.generarCuestionario();
            if(!generarCuestionario.getCorrecto()){mostrarMensajeResultadoEJB(generarCuestionario); return;}
            dto.setCuestionario(generarCuestionario.getValor());

            ResultadoEJB<List<Evaluaciones>> buscarEvaluaciones = ejbSatisfaccionServiciosConsulta.buscarEvaluaciones();
//            System.out.println("buscarEvaluaciones = " + buscarEvaluaciones);
            if(!buscarEvaluaciones.getCorrecto()) {mostrarMensajeResultadoEJB(buscarEvaluaciones); return;}
            else dto.setEvaluaciones(buscarEvaluaciones.getValor());
            cambiarEvaluacion();

//            System.out.println("dto.hayEvaluacion() = " + dto.hayEvaluacion());
//            if(dto.hayEvaluacion()) getContenedor(dto.getEvaluacionSeleccionada());
            /*ResultadoEJB<DtoSatisfaccionServiciosEncuesta> calcularConcentrado = ejbSatisfaccionServiciosConsulta.calcularConcentrado(dto.getEvaluacionSeleccionada());
            System.out.println("calcularConcentrado = " + calcularConcentrado);
            if(!calcularConcentrado.getCorrecto()){mostrarMensajeResultadoEJB(calcularConcentrado); return;}
            dto.getContenedores().put(dto.getEvaluacionSeleccionada(), calcularConcentrado.getValor());*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized DtoSatisfaccionServiciosEncuesta getContenedor(Evaluaciones evaluacion){
//        System.out.println();
//        System.out.println("ServiciosConsulta.getContenedor");
//        System.out.println("dto.hayContenedor(evaluacion) = " + dto.hayContenedor(evaluacion));
        if(dto.hayContenedor(evaluacion)) return dto.getContenedores().get(evaluacion);

        String identificadorEnSesion = "contenedor_".concat(evaluacion.getEvaluacion().toString());
        DtoSatisfaccionServiciosEncuesta contenedor = Faces.getApplicationAttribute(identificadorEnSesion);
        if(contenedor != null){
            dto.getContenedores().put(evaluacion, contenedor);
            return contenedor;
        }

        ResultadoEJB<DtoSatisfaccionServiciosEncuesta> calcularConcentrado = ejbSatisfaccionServiciosConsulta.calcularConcentrado(evaluacion, dto.getCuestionario(), dto);
//        System.out.println("evaluacion en parámetro = " + evaluacion);
//        System.out.println("evaluacion en concentrado calcularConcentrado = " + calcularConcentrado.getValor().getEvaluacion());
        if(!calcularConcentrado.getCorrecto()){mostrarMensajeResultadoEJB(calcularConcentrado); return null;}
        dto.getContenedores().put(evaluacion, calcularConcentrado.getValor());
        Faces.setApplicationAttribute(identificadorEnSesion, calcularConcentrado.getValor());
//        System.out.println("dto.getContenedores().size() = " + dto.getContenedores().size());
//        System.out.println("Evaluaciones como claves");
//        dto.getContenedores().keySet().forEach(System.out::println);
//        System.out.println("Evaluaciones en valores");
//        dto.getContenedores().values().stream().map(DtoSatisfaccionServiciosEncuesta::getEvaluacion).forEach(System.out::println);
//        System.out.println("dto.getContenedores() = " + dto.getContenedores());
//        dto.hayContenedor(evaluacion);//preguntar si hay contenedor y cambiar el contenedor seleccionado
//        System.out.println("Devolviendo valor");
        return dto.getContenedores().get(evaluacion);
    }

    public void limpiarContexto(){
        dto.getEvaluaciones().forEach(evaluacion -> {
            String identificadorEnSesion = "contenedor_".concat(evaluacion.getEvaluacion().toString());
            Faces.removeApplicationAttribute(identificadorEnSesion);
        });
    }

    public boolean mostrarBotonLimpiarContexto(){
        return dto.getFiltro().getEntity().getAreaOperativa().getArea().intValue() == 9;
    }

    public void cambiarEvaluacion(){
//        System.out.println();
//        System.out.println("ServiciosConsulta.cambiarEvaluacion");
//        System.out.println("dto.getEvaluacionSeleccionada() = " + caster.clavePeriodoToString(dto.getEvaluacionSeleccionada().getPeriodo()));
        if(dto.hayEvaluacion()){
            DtoSatisfaccionServiciosEncuesta contenedor = getContenedor(dto.getEvaluacionSeleccionada());
            ResultadoEJB<List<DtoAreaAcademica>> getProgramasEvaluacion = ejbSatisfaccionServiciosConsulta.getProgramasEvaluacion(dto.getEvaluacionSeleccionada(), contenedor);
//            System.out.println("getProgramasEvaluacion = " + getProgramasEvaluacion);
            if(getProgramasEvaluacion.getCorrecto()) dto.setAreasAcademicas(getProgramasEvaluacion.getValor());
            else mostrarMensajeResultadoEJB(getProgramasEvaluacion);
        }
    }

    public List<BigDecimal> getRespuestas(){
        return dto.getRespuestas();
    }

    /*public Long calcularFrecuencia(Evaluaciones evaluacion, Pregunta pregunta, BigDecimal respuesta){
        if(!dto.hayContenedor(evaluacion)){
            mostrarMensaje("No se encontró contenedor para la evaluación seleccionada.");
            return 0L;
        }
        ResultadoEJB<DtoSatisfaccionServiciosEncuesta.ConteoInstitucional> calcularFrecuenciaInstitucional = ejbSatisfaccionServiciosConsulta.calcularFrecuenciaInstitucional(dto.getContenedor(), pregunta, respuesta);
        if(!calcularFrecuenciaInstitucional.getCorrecto()) mostrarMensajeResultadoEJB(calcularFrecuenciaInstitucional);
        return calcularFrecuenciaInstitucional.getValor().getFrecuencia();
    }*/

    public DtoSatisfaccionServiciosEncuesta.FilaProgramaPK getFilaProgramaPK(AreasUniversidad programa, Pregunta pregunta){
        return new DtoSatisfaccionServiciosEncuesta.FilaProgramaPK(programa, pregunta);
    }

    public DtoSatisfaccionServiciosEncuesta.FilaProgramaApartadoPK getFilaProgramaApartadoPK(AreasUniversidad programa, Apartado apartado){
        return new DtoSatisfaccionServiciosEncuesta.FilaProgramaApartadoPK(programa, apartado);
    }

    public DtoSatisfaccionServiciosEncuesta.GraficaSerieAreaHistoricoPK getGraficaSerieAreaHistoricoPK(AreasUniversidad area, SatisfaccionServiciosApartado satisfaccionServiciosApartado){
//        System.out.println("area = " + area + ", satisfaccionServiciosApartado = " + satisfaccionServiciosApartado);
        if(area == null) area = new AreasUniversidad((short)0);
        return new DtoSatisfaccionServiciosEncuesta.GraficaSerieAreaHistoricoPK(area, satisfaccionServiciosApartado);
    }

    public AreasUniversidad getAreaPorPrograma(AreasUniversidad programa){
        DtoAreaAcademica areaAcademica = dto.getAreasAcademicas().stream().filter(dtoAreaAcademica -> dtoAreaAcademica.getProgramas().contains(programa)).findFirst().orElse(null);
        if(areaAcademica != null) return areaAcademica.getAreaAcademica();
        else return new AreasUniversidad((short)0);
    }

    public List<DtoAreaAcademica> getAreasAcademicas(){
//        dto.getAreasAcademicas().forEach(System.out::println);
        return dto.getAreasAcademicas().stream().filter(dtoAreaAcademica -> dtoAreaAcademica.getAreaAcademica().getArea() != null).sorted(Comparator.comparing(dtoAreaAcademica -> dtoAreaAcademica.getAreaAcademica().getNombre())).collect(Collectors.toList());
    }

    public SatisfaccionServiciosApartado apartadoToEnum(Apartado apartado){
        if(apartado == null) return null;

        return SatisfaccionServiciosApartadoConverter.of(apartado.getId().doubleValue());
    }

    public List<DtoSatisfaccionServiciosEncuesta.FilaInstitucionalApartado> ordenarFilaInstitucionalApartado(DtoSatisfaccionServiciosEncuesta contenedor){
        return contenedor.getApartadoFilaInstitucionalMap().values().stream().sorted(Comparator.comparing(filaInstitucionalApartado -> filaInstitucionalApartado.getApartado().getId())).collect(Collectors.toList());
    }

    public DtoSatisfaccionServiciosEncuesta getContenedorAnterior(DtoSatisfaccionServiciosEncuesta contenedor){
        int index = dto.getEvaluaciones().indexOf(contenedor.getEvaluacion());
        if(index < dto.getEvaluaciones().size() - 1){
            Evaluaciones evaluacion = dto.getEvaluaciones().get(index + 1);
            if(dto.getContenedores().containsKey(evaluacion))
                return dto.getContenedores().get(evaluacion);
        }

        return null;
    }
}
