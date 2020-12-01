package mx.edu.utxj.pye.sgi.controlador.evaluaciones;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.AdministracionEvaluacionesRolPsicopedagogia;
import mx.edu.utxj.pye.sgi.dto.DtoAdministracionEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbAdministracionEvaluaciones;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesAreas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class AdministracionEvaluacionesPsicopedagogia extends ViewScopedRol implements Desarrollable {
    @EJB private EjbPropiedades ep;
    @EJB private EjbAdministracionEvaluaciones ejdAdminEvaluaciones;
    @Getter @Setter private AdministracionEvaluacionesRolPsicopedagogia rol;
    @Inject LogonMB logonMB;
    @Getter boolean cargado;
    @Getter Boolean tieneAcceso;


    @PostConstruct
    public void init(){
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.ADMINISTRACION_EVALUACIONES_PSICOPEDAGOGIA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejdAdminEvaluaciones.validaPersonalActivo(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejdAdminEvaluaciones.validaPersonalActivo(logonMB.getPersonal().getClave());

            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personalPsicopedagogia = filtro.getEntity();//            ejbPersonalBean.pack(logonMB.getPersonal());
            rol = new AdministracionEvaluacionesRolPsicopedagogia(filtro, personalPsicopedagogia, personalPsicopedagogia.getAreaOperativa());
            tieneAcceso = rol.tieneAcceso(personalPsicopedagogia);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            rol.setPersonalPsicopedagogia(personalPsicopedagogia);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.OPERATIVO);
            //Obtiene la lista de Periodos escolares
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos= ejdAdminEvaluaciones.getPeriodosEscores();
            if(resPeriodos.getCorrecto()){rol.setPeriodosEscolares(resPeriodos.getValor());}
            else {mostrarMensajeResultadoEJB(resPeriodos);}
            //Obtiene el periodo activo
            ResultadoEJB<PeriodosEscolares> resPeriodoA= ejdAdminEvaluaciones.getPeriodoEscolarActivo();
            if(resPeriodoA.getCorrecto()){rol.setPeriodoActivo(resPeriodoA.getValor());rol.setPeriodoSeleccionado(resPeriodoA.getValor());}
            else {mostrarMensajeResultadoEJB(resPeriodoA);}
            ResultadoEJB<PeriodoEscolarFechas> resPeriodoFechas= ejdAdminEvaluaciones.getPeriodoFecha(rol.getPeriodoActivo());
            if(resPeriodoFechas.getCorrecto()){rol.setPeriodoActivoFechas(resPeriodoFechas.getValor());}else {mostrarMensajeResultadoEJB(resPeriodoFechas);}
            getEvaluacionesbyPeriodo();
            getTiposEvaluacionArea();
            //Instrucciones


        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    /**
     * Cambiar de periodo para obtener la lista de evaluciones que estuvieron activas
     * @param e
     */
    public void cambiarEvaluacionesPeriodo(ValueChangeEvent e){
        try{
            if(e.getNewValue() instanceof PeriodosEscolares){
                PeriodosEscolares periodo = (PeriodosEscolares)e.getNewValue();
                rol.setPeriodoSeleccionado(periodo);
                getEvaluacionesbyPeriodo();
                Ajax.update("frmEvaluaciones");
            }else mostrarMensaje("");

        }catch (Exception ex){mostrarExcepcion(ex);}

    }


    public void getAperturabyTipoSeleccionado(){
        try{
            ResultadoEJB<AperturaVisualizacionEncuestas> resApertura = ejdAdminEvaluaciones.getAperturabyTipo(rol.getTipoEvalucionSeleecionada().getTipoEvaluacion());
            rol.setNuevaEvaluacion(new Evaluaciones());
            if(resApertura.getCorrecto()){
                rol.setApertura(resApertura.getValor());
            }else {mostrarMensajeResultadoEJB(resApertura);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void getAperturabyEvaluacion(){
        try{
            ResultadoEJB<AperturaVisualizacionEncuestas> resAper= ejdAdminEvaluaciones.getAperturabyTipo(rol.getEvaluacionSeleccionada().getTipo());
            if(resAper.getCorrecto()){
                rol.setAperturaEdit(resAper.getValor());
            }else {mostrarMensajeResultadoEJB(resAper);}

        }catch (Exception e){mostrarExcepcion(e
        );}
    }

    public void getEvaluacionesbyPeriodo(){
        try{
            //System.out.println("Obenter evaluaciones -- area" + rol.getPrograma());
            ResultadoEJB<List<DtoAdministracionEvaluaciones>> resEv= ejdAdminEvaluaciones.getEvaluacionesbyArea(rol.getPrograma(),rol.getPeriodoSeleccionado());
            //System.out.println(resEv.getValor());
            if(resEv.getCorrecto()){ rol.setEvaluaciones(resEv.getValor()); }
            else {mostrarMensajeResultadoEJB(resEv);}
        }catch (Exception e){mostrarExcepcion(e); }
    }

    public  void getTiposEvaluacionArea() {
        try{
            ResultadoEJB<List<EvaluacionesAreas>> resListEv= ejdAdminEvaluaciones.getEvaluacionesPsicopedagogia(rol.getPrograma());
            if(resListEv.getCorrecto()){ rol.setListaTiposEv(resListEv.getValor());rol.setTipoEvalucionSeleecionada(resListEv.getValor().get(1));}
            else {mostrarMensajeResultadoEJB(resListEv);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void  getEvEdit(Evaluaciones evaluaciones){
        try{
            rol.setEvaluacionSeleccionada(evaluaciones);
            getAperturabyEvaluacion();
            Ajax.update("frmEditEvaluacion");
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }


    /**
     * Guarda la evaluación
     */
    public  void  saveEvaluacion(){
        try{
            rol.getNuevaEvaluacion().setPeriodo(rol.getPeriodoActivo().getPeriodo());
            rol.getNuevaEvaluacion().setTipo(rol.getTipoEvalucionSeleecionada().getTipoEvaluacion());
            rol.getApertura().setFechaInicial(rol.getNuevaEvaluacion().getFechaInicio());
            ResultadoEJB<Evaluaciones> resSaveEv = ejdAdminEvaluaciones.operacionesEvaluacion(rol.getNuevaEvaluacion(), Operacion.PERSISTIR);
            if(resSaveEv.getCorrecto()){
                rol.setNuevaEvaluacion(resSaveEv.getValor());
                rol.setPeriodoSeleccionado(rol.getPeriodoActivo());
                updateAperturaSeguimiento();
                getEvaluacionesbyPeriodo();
                rol.setNuevaEvaluacion(new Evaluaciones());
            }else {mostrarMensajeResultadoEJB(resSaveEv);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    /*
    Actualiza la evaluacion
     */
    public void  updateEvaluaciones(){
        try{

            ResultadoEJB<Evaluaciones> resUpdateEv= ejdAdminEvaluaciones.operacionesEvaluacion(rol.getEvaluacionSeleccionada(), Operacion.ACTUALIZAR);
            if(resUpdateEv.getCorrecto()){
                rol.setEvaluacionSeleccionada(resUpdateEv.getValor());
                rol.setPeriodoSeleccionado(rol.getPeriodoActivo());
                updateAperturaSeguimientoE();
                getEvaluacionesbyPeriodo();
            }
        }catch (Exception e){mostrarExcepcion(e);}
    }

    /*
    Verfica que el tipo de avaluacion no esté repetida en el periodo activo y busca apertura de seguimiento
     */
    public  void  verificarEvaluacion(){
        try{
            ResultadoEJB<Boolean> resVer= ejdAdminEvaluaciones.verficarEvaluacion(rol.getTipoEvalucionSeleecionada().getTipoEvaluacion(),rol.getPeriodoActivo());
            if(resVer.getCorrecto()){
                rol.setEvalucionEx(resVer.getValor());
                getAperturabyTipoSeleccionado();
                //System.out.println("Activa ->"+ resVer.getValor()+ " apertura "+ rol.getApertura());
            }else {rol.setEvalucionEx(true);mostrarMensajeResultadoEJB(resVer);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void updateAperturaSeguimiento(){
        try{
            ResultadoEJB<AperturaVisualizacionEncuestas> resUpdateApertura = ejdAdminEvaluaciones.updateAperturaSeguimiento(rol.getApertura());
            if(resUpdateApertura.getCorrecto()){
                rol.setApertura(resUpdateApertura.getValor());
            }mostrarMensajeResultadoEJB(resUpdateApertura);
        }catch (Exception e){mostrarExcepcion(e);}
    }
    public void updateAperturaSeguimientoE(){
        try{
            ResultadoEJB<AperturaVisualizacionEncuestas> resUpdateApertura = ejdAdminEvaluaciones.updateAperturaSeguimiento(rol.getAperturaEdit());
            if(resUpdateApertura.getCorrecto()){
                rol.setAperturaEdit(resUpdateApertura.getValor());
            }mostrarMensajeResultadoEJB(resUpdateApertura);
        }catch (Exception e){mostrarExcepcion(e);}
    }


    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administracionEvaluacionPsicopedagogia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));

    }

}
