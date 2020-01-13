/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.model.Filter;
import edu.mx.utxj.pye.seut.util.preguntas.Pregunta;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.EvaluacionRolPersonal;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacionConocimientoCumplimiento;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionConocimientoCodigoEticaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



/**
 *
 * @author Planeación
 */
@Named
@ViewScoped
public class EvaluacionConocimientoCodigoEtica extends ViewScopedRol implements Desarrollable {
    @Getter @Setter private EvaluacionRolPersonal rol;
    @Getter @Setter private Boolean tieneAcceso = false;
    @Getter @Setter private Boolean finalizado = false;
    @EJB private EjbEvaluacionConocimientoCumplimiento ejb;
    @EJB
    EjbPropiedades ep;
    @Inject
    LogonMB logonMB;
    

@Getter private Boolean cargado = false;



    @PostConstruct
    public void init() {
        try{
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.EVALUACION_CONOCIMIENTO_CUMPLIMIENTO);
//            System.out.println(logonMB.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarPersonal(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarPersonal(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personalActivo = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new EvaluacionRolPersonal(filtro, personalActivo);
            tieneAcceso = rol.tieneAcceso(personalActivo);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
//            System.out.println(tieneAcceso);
            ResultadoEJB<Evaluaciones> resEvento = ejb.verificarEvaluacion();
//            System.out.println(resEvento.getValor());
            if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
            rol.setNivelRol(NivelRol.OPERATIVO);

            rol.setPeriodoActivo(resEvento.getValor().getPeriodo());
            ResultadoEJB<EvaluacionConocimientoCodigoEticaResultados> resultados = ejb.getResultado(resEvento.getValor(), personalActivo.getPersonal().getClave(), rol.getRespuestas());
            rol.setResultados(resultados.getValor());
            if(rol.getResultados() != null){
                ResultadoEJB<List<Apartado>> resApartado = ejb.apartados();
                rol.setApartados(resApartado.getValor());
                finalizado = ejb.actualizarResultado(resultados.getValor());
            }

        }catch (Exception e){
            mostrarExcepcion(e);
        }
        /*try {
            dto.finalizado = false;
            dto.respuestas = new HashMap<>();
            dto.respuestasPosibles = ejb.getRespuestasPosibles();
            dto.evaluacion = ejb.getEvaluacionActiva();
            if (dto.evaluacion != null) {
                dto.evaluador = logonMB.getCurrentUser();
                dto.alumno=ejbES.obtenerAlumnos(dto.evaluador);
                if (dto.alumno.getGrupos().getGrado()==6) {
                    dto.estSexto=true;
                    dto.evaluadorr = Integer.parseInt(dto.evaluador);
                    if (dto.alumno != null) {
                        dto.resultadoREST = ejb.getResultado(dto.evaluacion, dto.evaluadorr, dto.respuestas);
                        if (dto.resultadoREST != null) {
                            dto.apartados = ejb.getApartados();
                            dto.finalizado = ejb.actualizarResultado(dto.resultadoREST);
                            dto.cargada = true;
                        }
                    }
                }else{
                    dto.estSexto=false;
                }

            }
        } catch (Exception e) {
            dto.cargada = false;
////            System.out.println("mx.edu.utxj.pye.sgi.controlador.EncuestaSatisfaccionEgresadosTsu.init() e: " + e.getMessage());
        }*/
    }


    public void guardarRespuesta(ValueChangeEvent cve) {
        UIComponent origen = (UIComponent) cve.getSource();
        if(cve.getNewValue().toString() != null){
            rol.setValor(cve.getNewValue().toString());
        }else{
            rol.setValor(cve.getOldValue().toString());
        }
        ejb.actualizarRespuestaPorPregunta(rol.getResultados(), origen.getId(), rol.getValor(), rol.getRespuestas());
        finalizado = ejb.actualizarResultado(rol.getResultados());
    }

    public void obtenerRespuestasPosibles(Pregunta pregunta){
        ResultadoEJB<List<SelectItem>> resSelectItem = ejb.getRespuestasPosibles(pregunta);
        rol.setRespuestaPosibles(resSelectItem.getValor());
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "evaluación de conocimiento y cumplimiento";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
////        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
}
