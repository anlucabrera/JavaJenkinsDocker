package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ConfiguracionUnidadMateriaRolDocente;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoConfiguracionUnidadMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import org.omnifaces.util.Ajax;

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
            rol.getInstrucciones().add("Seleccionar la materia de la cual se configura las unidades.");

            existeConfiguracion();
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "configuración unidad materia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
    
    /**
     * Permite actualizar la carga académica del docente correspondiente al periodo seleccionado
     */
    public void actualizarCargaAcademica(){
        if(rol.getPeriodo() == null) return;
        if(rol.getDocente()== null) return;

        ResultadoEJB<List<DtoCargaAcademica>> res = ejb.getCargaAcademicaPorDocente(rol.getDocente(), rol.getPeriodo());
        if(res.getCorrecto()){
            rol.setCargas(res.getValor());
            existeConfiguracion();
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void existeConfiguracion(){
        if(rol.getCarga()== null) return;
        System.err.println("existeConfiguracion - carga " + rol.getCarga().getCargaAcademica().getCarga());
        ResultadoEJB<List<UnidadMateriaConfiguracion>> res = ejb.buscarConfiguracionUnidadMateria(rol.getCarga());
        if(res.getValor().isEmpty() || res.getValor().equals("")){
            rol.setExiste(false);
            mostrarConfiguracionSugerida();
        }else  rol.setExiste(true); mostrarConfiguracionSugerida(); mostrarConfiguracionGuardada();
    }
    
    public void mostrarConfiguracionSugerida(){
        if(rol.getCarga() == null) return;
        ResultadoEJB<List<DtoConfiguracionUnidadMateria>> res = ejb.getConfiguracionUnidadMateriaSugerida(rol.getCarga());
        if(res.getCorrecto()){
            rol.setConfUniMateriasSug(res.getValor());
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite invocar el guardado de la configuración de la unidad materia, para que se lleve acabo, se debió haber seleccionado una carga académica, e ingresado fecha de inicio y fin de cada unidad.
     */
    public void guardarConfigUnidadMat(){
        ResultadoEJB<List<DtoConfiguracionUnidadMateria>> resGuardar = ejb.guardarConfUnidadMateria(rol.getConfUniMateriasSug(), rol.getCarga().getCargaAcademica());
//        if(resGuardar.getCorrecto()){
//           existeConfiguracion();
//        }else 
        rol.setExiste(true);
        mostrarMensajeResultadoEJB(resGuardar);
    }
    
    public void mostrarConfiguracionGuardada(){
        if(rol.getCarga() == null) return;
        ResultadoEJB<List<DtoConfiguracionUnidadMateria>> res = ejb.getConfiguracionUnidadMateria(rol.getCarga());
        if(res.getCorrecto()){
            rol.setConfUniMateriasGuard(res.getValor());
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void eliminarConfigUnidadMat(){
        if(rol.getConfUniMateriasGuard().isEmpty()) mostrarMensaje("No se puede eliminar una asignación que no existe.");
        ResultadoEJB<Integer> resEliminar = ejb.eliminarConfUnidadMateria(rol.getCarga().getCargaAcademica());
         rol.setExiste(false);
        mostrarMensajeResultadoEJB(resEliminar);
    }
}
