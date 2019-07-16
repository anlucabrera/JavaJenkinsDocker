package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Collections;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ConfiguracionUnidadMateriaRolDocente;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.vista.DtoAlerta;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConfiguracionUnidadMateria;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoConfiguracionUnidadMateria;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDiasPeriodoEscolares;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradora;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracionCriterio;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

/**
 * La selección del grupo, docente y del periodo deben ser directos de un control de entrada
 */
@Named
@ViewScoped
public class ConfiguracionUnidadMateriaDocente extends ViewScopedRol implements Desarrollable {
    @Getter @Setter ConfiguracionUnidadMateriaRolDocente rol;

    @EJB EjbConfiguracionUnidadMateria ejb;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por area superior y categiría operativa<br/>
     *      La referencia al director si es que el usuario logueado es efectivamente un director por medio del filtro de rol<br/>
     *      El programa educativo al que pertenece el director por medio de operación segura antierror<br/>
     *      El DTO del rol<br/>
     *      La lista de periodos escolares en forma descendente por medio de operación segura antierror<br/>
     *      EL mapa de programas con grupos por medio de operación segura antierror ordenando programas por areas, niveles y nombre del programa y los grupos por grado y letra
     */
    @PostConstruct
    public void init(){
        try{
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDocente(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarDocente(logon.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo docente = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ConfiguracionUnidadMateriaRolDocente(filtro, docente);
            tieneAcceso = rol.tieneAcceso(docente);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setDocente(docente);
            ResultadoEJB<EventoEscolar> resEvento = ejb.verificarEvento(rol.getDocente());
            if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(resEvento.getValor().getPeriodo());

            rol.setEventoActivo(resEvento.getValor());

            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());
            
            ResultadoEJB<List<DtoCargaAcademica>> resCarga = ejb.getCargaAcademicaPorDocente(docente, rol.getPeriodo());
            if(!resCarga.getCorrecto()) mostrarMensajeResultadoEJB(resCarga);
            rol.setCargas(resCarga.getValor());
            
            rol.getInstrucciones().add("Seleccionar periodo escolar activo, de lo contrario solo podrá consultar configuraciones anteriores.");
            rol.getInstrucciones().add("Seleccionar Materia - Grupo - Programa Educativo que va a configurar.");
            rol.getInstrucciones().add("Seleccionar si o no aplicará Tarea Integradora en la configuración.");
            rol.getInstrucciones().add("Actualizar fecha de inicio y fin por cada unidad de la materia si no desea utilizar las fechas sugeridas por el sistema.");
            rol.getInstrucciones().add("En caso de que aplicará Tarea Integradora deberá ingresar nombre y fecha de entrega.");
            rol.getInstrucciones().add("Una vez que capture toda la información solicitada puede GUARDAR la configuración.");
            rol.getInstrucciones().add("Usted podrá visualizar la Configuración Guardada en sistema.");
            rol.getInstrucciones().add("Si desea ELIMINAR la configuración deberá seleccionar que desea realizar esta accción para que se active el botón de eliminar ubicado en la parte inferior.");
            rol.getInstrucciones().add("Al eliminar la configuración de la materia se eliminarán también los criterios de evaluación que se encuentren registrados.");

            existeConfiguracion();
            rol.setAddTareaInt(true);
            rol.setAutorizoEliminar(false);
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "configuración unidad materia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    /**
     * Permite actualizar la carga académica del docente correspondiente al periodo seleccionado
     */
    public void actualizarCargaAcademica(){
        setAlertas(Collections.EMPTY_LIST);
        if(rol.getPeriodo() == null) return;
        if(rol.getDocente()== null) return;

        ResultadoEJB<List<DtoCargaAcademica>> res = ejb.getCargaAcademicaPorDocente(rol.getDocente(), rol.getPeriodo());
        if(res.getCorrecto()){
            rol.setCargas(res.getValor());
            existeConfiguracion();
        }else mostrarMensajeResultadoEJB(res);
        
        ResultadoEJB<List<DtoAlerta>> resMensajes = ejb.identificarMensajes(rol);
        System.out.println("resMensajes = " + resMensajes);
        if(resMensajes.getCorrecto()){
            setAlertas(resMensajes.getValor());
        }else {
            mostrarMensajeResultadoEJB(resMensajes);
        }

        repetirUltimoMensaje();
    }
    
    public void existeConfiguracion(){
        if(rol.getCarga()== null) return;
        ResultadoEJB<List<UnidadMateriaConfiguracion>> res = ejb.buscarConfiguracionUnidadMateria(rol.getCarga());
        if(res.getValor().isEmpty() || res.getValor().equals("")){
            rol.setExiste(false);
            mostrarConfiguracionSugerida();
            mostrarConfiguracionGuardada();
            }else {
            rol.setExiste(true); 
            mostrarConfiguracionSugerida();
            mostrarConfiguracionGuardada();  
        }  
    }
    
    public void mostrarConfiguracionSugerida(){
        if(rol.getCarga() == null) return;
        ResultadoEJB<List<DtoConfiguracionUnidadMateria>> res = ejb.getConfiguracionSugerida(rol.getCarga());
        TareaIntegradora tareaInt = new TareaIntegradora();
        if(res.getCorrecto()){
            rol.setConfUniMateriasSug(res.getValor());
            rol.setTareaIntegradora(tareaInt);
        }else mostrarMensajeResultadoEJB(res);     
    }
    
     public void mostrarTareaIntPrueba(){
        TareaIntegradora tareaInt = new TareaIntegradora();
        DtoDiasPeriodoEscolares dtoFechas = ejb.getCalculoDiasPeriodoEscolar(rol.getPeriodoActivo());
        tareaInt.setDescripcion("Ingresar nombre");
        tareaInt.setFechaEntrega(dtoFechas.getFechaFin());
         rol.setTareaIntegradora(tareaInt);
    }
    
    /**
     * Permite invocar el guardado de la configuración de la unidad materia, para que se lleve acabo, se debió haber seleccionado una carga académica, e ingresado fecha de inicio y fin de cada unidad.
     */
    public void guardarConfigUnidadMat(){
        ResultadoEJB<List<DtoConfiguracionUnidadMateria>> resGuardarConf = ejb.guardarConfUnidadMateria(rol.getConfUniMateriasSug(), rol.getCarga().getCargaAcademica());
        if(resGuardarConf.getCorrecto()){
            rol.setExiste(true);
            if(rol.getAddTareaInt()){
                ResultadoEJB<TareaIntegradora> resGuardarTI = ejb.guardarTareaIntegradora(rol.getTareaIntegradora(), rol.getCarga().getCargaAcademica());
                mostrarMensajeResultadoEJB(resGuardarTI);
                rol.setAddTareaInt(false);
            }
            System.err.println("guardarConfigUnidadMat " + resGuardarConf.getValor().toString() + " carga " + rol.getCarga());
            ResultadoEJB<List<UnidadMateriaConfiguracionCriterio>> resUniMatCrit = ejb.guardarConfiguracionUnidadMateriaCriterios(resGuardarConf.getValor(), rol.getCarga());
            mostrarMensajeResultadoEJB(resUniMatCrit);
//            mostrarConfiguracionGuardada();
        }else  mostrarMensajeResultadoEJB(resGuardarConf);
    }
    
   
    public void mostrarConfiguracionGuardada(){
        if(rol.getCarga() == null) return;
        ResultadoEJB<List<DtoConfiguracionUnidadMateria>> res = ejb.getConfiguracionUnidadMateria(rol.getCarga());
        if(res.getCorrecto()){
            rol.setConfUniMateriasGuard(res.getValor());
            mostrarTareaIntegradora();
        }else mostrarMensajeResultadoEJB(res);
    }
    
     public void mostrarTareaIntegradora(){
        ResultadoEJB<TareaIntegradora> resTI = ejb.getTareaIntegradora(rol.getCarga());
        if(resTI.getCorrecto()){
            rol.setTareaIntGuardada(resTI.getValor());
            Ajax.update("tb4");
        }
        else{
              ResultadoEJB<TareaIntegradora> resSinTI = ejb.getTareaIntegradora(rol.getCarga());
              rol.setTareaIntGuardada(resSinTI.getValor());
        } 
    }
    
    public void eliminarConfigUnidadMat(DtoCargaAcademica cargaAcademica){
        ResultadoEJB<Integer> resEliminarCUM = ejb.eliminarConfUnidadMateria(rol.getCargaEliminar().getCargaAcademica());
        ResultadoEJB<Integer> resEliminarTI = ejb.eliminarTareaIntegradora(rol.getCargaEliminar().getCargaAcademica());
        rol.setExiste(false);
        mostrarMensajeResultadoEJB(resEliminarCUM);
        mostrarMensajeResultadoEJB(resEliminarTI);
    }
    
   
    public void cambiarCarga(ValueChangeEvent event){
        rol.setCarga((DtoCargaAcademica)event.getNewValue());
        existeConfiguracion();
        rol.setAddTareaInt(true);
        rol.setAutorizoEliminar(false);
    }
    
    public void cambiarAddTareaInt(ValueChangeEvent event){
        if(rol.getAddTareaInt()){
            rol.setAddTareaInt(false);
            Ajax.update("tb2");
        }else{
            rol.setAddTareaInt(true);
             Ajax.update("tb2");
        }
    }
    
    public void cambiarEliminar(ValueChangeEvent event){
        rol.setCargaEliminar(rol.getCarga());
        if(rol.getAutorizoEliminar()){
            rol.setAutorizoEliminar(false);
        }else{
            rol.setAutorizoEliminar(true);
            mostrarTareaIntPrueba();
            rol.setAddTareaInt(false);
        }
    }
    
     /**
     * Permite invocar la eliminaciòn de una asignaciòn
     * @param tareaIntegradora
     */
    public void eliminarTareaInt(){
          System.err.println("eliminarTareaInt - tarea ");
//        ResultadoEJB<TareaIntegradora> resElimTI = ejb.eliminarTareaIntegradora(tareaIntegradora);
//        mostrarMensajeResultadoEJB(resElimTI);
    }
}
