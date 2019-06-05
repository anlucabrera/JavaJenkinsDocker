package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AsignacionAcademicaRolDirector;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionAcademica;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * La selección del grupo, docente y del periodo deben ser directos de un control de entrada
 */
@Named
@ViewScoped
public class AsignacionAcademicaDirector extends ViewScopedRol implements Desarrollable {
    @Getter @Setter AsignacionAcademicaRolDirector rol;

    @EJB EjbAsignacionAcademica ejb;
    @EJB EjbPropiedades ep;
    @EJB EjbPersonalBean ejbPersonalBean;
    @Inject Caster caster;
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
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDirector(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarDirector(logon.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AsignacionAcademicaRolDirector(filtro, director, director.getAreaOficial());
            tieneAcceso = rol.tieneAcceso(director);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución

            rol.setDirector(director);
            ResultadoEJB<EventoEscolar> resEvento = ejb.verificarEvento(rol.getDirector());
            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
            rol.setEventoActivo(resEvento.getValor());

            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());

            ResultadoEJB<Map<AreasUniversidad, List<Grupo>>> resProgramas = ejb.getProgramasActivos(rol.getDirector(), rol.getPeriodo());
            if(!resProgramas.getCorrecto()) mostrarMensajeResultadoEJB(resProgramas);
            rol.setProgramasGruposMap(resProgramas.getValor());

            actualizarMaterias();
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "asignación académica";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }

    /**
     * Método para proporcionar lista de docentes sugeridos en un autocomplete donde se puede ingresar el número de nómina, nombre o área del docente
     * @param pista
     * @return Lista de sugerencias
     */
    public List<PersonalActivo> completeDocentes(String pista){
        ResultadoEJB<List<PersonalActivo>> res = ejb.buscarDocente(pista);
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return Collections.emptyList();
        }
    }

    /**
     * Permite seleccionar un programa desde un evento de selección única, se recomienda actualizar al control de grupos ya que será sincronizado
     * de forma inmediata y se seleccionará el primer grupo de esa lista para que también se actualicen los controles que visualicen al grupo
     * @param e Evento de selección única, se recomienda un selectOneMenu
     */
    public void seleccionarPrograma(ValueChangeEvent e){
        if(e.getNewValue() instanceof ProgramasEducativos){
            rol.setPrograma((AreasUniversidad) e.getNewValue());
            Ajax.update("grupo");//actualizaría al selector de grupos si lleva el id especificado
        }
    }

    /**
     * Permite invocar la asignaciòn de la materia, para que se lleve acabo, se debió haber seleccionado un docente, una materia, un periodo y un grupo
     * @param materia
     */
    public void asignarMateria(Materia materia){
        rol.setMateria(materia);
        ResultadoEJB<CargaAcademica> resAsignacion = ejb.asignarMateriaDocente(rol.getMateria(), rol.getDocente(), rol.getGrupo(), rol.getPeriodo(), Operacion.PERSISTIR);
        mostrarMensajeResultadoEJB(resAsignacion);
    }

    /**
     * Perimite invocar la eliminaciòn de una asignaciòn
     * @param materia
     */
    public void eliminarAsignacion(Materia materia){
        rol.setMateria(materia);
        ResultadoEJB<CargaAcademica> resAsignacion = ejb.asignarMateriaDocente(rol.getMateria(), rol.getDocente(), rol.getGrupo(), rol.getPeriodo(), Operacion.PERSISTIR);
        mostrarMensajeResultadoEJB(resAsignacion);
    }

    public void actualizarMaterias(){
        if(rol.getPeriodo() == null) return;
        if(rol.getPrograma() == null) return;
        if(rol.getGrupo() == null) return;

        ResultadoEJB<List<Materia>> res = ejb.getMateriasPorAsignar(rol.getPrograma(), rol.getGrupo());
        if(res.getCorrecto()){
            rol.setMateriasSinAsignar(res.getValor());
        }else mostrarMensajeResultadoEJB(res);

        repetirUltimoMensaje();
    }
}
