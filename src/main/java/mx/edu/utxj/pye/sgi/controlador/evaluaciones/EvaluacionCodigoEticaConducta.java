package mx.edu.utxj.pye.sgi.controlador.evaluaciones;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.EvaluacionCodigoEticaRolPersonal;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionCodigoEtica;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionConocimientoCodigoEticaResultados2;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionCodigoEticaConducta;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Named
@ViewScoped
public class EvaluacionCodigoEticaConducta extends ViewScopedRol implements Desarrollable {

    @EJB EjbPropiedades ep;
    @EJB EjbEvaluacionCodigoEtica ejb;
    @Inject LogonMB logonMB;
    @Getter @Setter private EvaluacionCodigoEticaRolPersonal rol;
    @Getter private Boolean cargado = false;
    @Getter @Setter private Boolean tieneAcceso = false;
    @Getter @Setter private Boolean finalizado= false;
    @Getter @Setter String valor;


    @PostConstruct
    public void init() {
        try{
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.EVALUACION_CONOCIMIENTO_CODIGO_ETICA_CONDUCTA);
//            System.out.println(logonMB.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarPersonal(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarPersonal(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personalActivo = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new EvaluacionCodigoEticaRolPersonal(filtro, personalActivo);
            tieneAcceso = rol.tieneAcceso(personalActivo);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
          // System.out.println(tieneAcceso);
            ResultadoEJB<Evaluaciones> resEvento = ejb.getEvaluacionActiva();
           //System.out.println(resEvento.getValor());
            if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú

            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            //if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setEvaluacionActiva(resEvento.getValor());
            finalizado =Boolean.FALSE;
            cargarApartados();
            ResultadoEJB <EvaluacionConocimientoCodigoEticaResultados2> resResultados = ejb.getResultados(rol.getPersonal(),rol.getEvaluacionActiva());
            if(resResultados.getCorrecto()){rol.setResultados(resResultados.getValor());}
            else {mostrarMensajeResultadoEJB(resResultados);}
            comprobar();

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    /**
     * Guarda resultado seleccionado
     * @param e
     * Evaluacion Tutur
     * @throws ELException
     */
    public void guardar(ValueChangeEvent e) throws ELException {

        UIComponent id = (UIComponent)e.getSource();

        if(e.getNewValue() != null){
            valor = e.getNewValue().toString();
        }else{
            valor = e.getOldValue().toString();
        }
       // System.out.println("EvaluacionCodigoEticaConducta.guardar "+id.getId() +"  valor "+ valor);
        ResultadoEJB<EvaluacionConocimientoCodigoEticaResultados2> guardar=ejb.cargarResultados(id.getId(), valor, rol.getResultados(), Operacion.REFRESCAR);
        ResultadoEJB<EvaluacionConocimientoCodigoEticaResultados2> update=ejb.cargarResultados(id.getId(), valor, rol.getResultados(), Operacion.ACTUALIZAR);
        rol.setResultados(update.getValor());
        comprobar();

    }

    /*
    Caraga el cuestionario y las posibles respuestas
     */
    public void cargarApartados (){
        try{
            rol.setApartados(ejb.apartados().getValor());
            rol.setSiNo(ejb.siNo().getValor());
            rol.setP3(ejb.respuestasPregunta3().getValor());
            rol.setP4(ejb.respuestasPregunta4().getValor());
            rol.setP5(ejb.respuestasPregunta5().getValor());
            rol.setP6(ejb.respuestasPregunta6().getValor());
            rol.setP7(ejb.respuestasPregunta7().getValor());
            rol.setP8(ejb.respuestasPregunta8().getValor());
            rol.setP9(ejb.respuestasPregunta9().getValor());
            rol.setP10(ejb.respuestasPregunta10().getValor());
            rol.setP11(ejb.respuestasPregunta11().getValor());
            rol.setP12(ejb.respuestasPregunta12().getValor());
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
  Comprueba si ya termino la evaluacion
   */
    public void comprobar(){
        Comparador<EvaluacionConocimientoCodigoEticaResultados2> comparador = new ComparadorEvaluacionCodigoEticaConducta();
        finalizado = comparador.isCompleto(rol.getResultados());
       // System.out.println("EvaluacionCodigoEticaConducta.comprobar "+comparador.isCompleto(rol.getResultados()));
        if(finalizado ==true){
            totalCorrectas();
            rol.getResultados().setTotalCorrectas(rol.getTotalCorrectas());
            ResultadoEJB<EvaluacionConocimientoCodigoEticaResultados2> resUpdate = ejb.updateCompleto(rol.getResultados());
            rol.setResultados(resUpdate.getValor());
            if(!resUpdate.getCorrecto()){mostrarMensajeResultadoEJB(resUpdate);}
        }
    }

    public void totalCorrectas(){
        try {
            ResultadoEJB<Integer> resTotalCorrectas= ejb.totalCorrectas(rol.getResultados());
            if(resTotalCorrectas.getCorrecto()){
                rol.setTotalCorrectas(resTotalCorrectas.getValor());
            }else {mostrarMensajeResultadoEJB(resTotalCorrectas);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "evaluacionCodigoEticaConducta";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
////        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
}
