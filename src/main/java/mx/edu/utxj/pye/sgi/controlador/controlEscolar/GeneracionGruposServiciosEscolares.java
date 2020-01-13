package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoConteoGrupos;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.GeneracionGruposRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.dto.vista.DtoAlerta;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbGeneracionGrupos;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



@Named
@ViewScoped
public class GeneracionGruposServiciosEscolares extends ViewScopedRol implements Desarrollable {
    @Getter @Setter  GeneracionGruposRolServiciosEscolares rol;
    @EJB private EjbPropiedades ep;
    @EJB private EjbGeneracionGrupos ejb;
    @EJB private EJBSelectItems ejbSI;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;

    


@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
        try {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.GENERACION_GRUPOS);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarServiciosEscolares(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarServiciosEscolares(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo serviciosEscolares = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new GeneracionGruposRolServiciosEscolares(filtro, serviciosEscolares, serviciosEscolares.getAreaOperativa());
            tieneAcceso = rol.tieneAcceso(serviciosEscolares);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setServiciosEscolares(serviciosEscolares);
            ResultadoEJB<EventoEscolar> resEvento = ejb.verificarEvento();
            if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
            rol.setNivelRol(NivelRol.OPERATIVO);

            rol.setPeriodoActivo(resEvento.getValor().getPeriodo());
            rol.setPeriodo(ejb.obtenerPeriodo(rol.getPeriodoActivo()).getValor());
            rol.setEventoActivo(resEvento.getValor());

            rol.setGrupo(new Grupo());
            ResultadoEJB<List<SelectItem>> resPeriodos = ejb.obtenerListaPeriodos();
            rol.setPeriodos(resPeriodos.getValor());
            obtenerAreasUniversidad();
            obtenerGeneraciones();
            obtenerSugerencia();
            rol.getInstrucciones().add("Seleccionar el periodo activo");
            rol.getInstrucciones().add("Realizar la selección del programa educativo.");
            rol.getInstrucciones().add("Realizar la selección del plan de estudio.");
            rol.getInstrucciones().add("Realizar la selección del sistema (Semanal o Sabatino)");
            rol.getInstrucciones().add("Ingresar el total de alumnos pertenecientes al grupo");
            rol.getInstrucciones().add("Realizar el registro de la información");
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    public void obtenerGrupos(){
        ResultadoEJB<List<Grupo>> res = ejb.obtenerGruposPorPeriodo(rol.getPeriodoAct());
        if(res.getCorrecto()){
            rol.setGrupos(res.getValor());
            //mostrarMensajeResultadoEJB(res);
        }
        alertas();
    }

    public void obtenerPlanesEstudioPorPE(){
        ResultadoEJB<List<PlanEstudio>> res = ejb.obtenerPlanesEstudioPorCarrera(rol.getGrupo().getIdPe());
        //System.out.println("Planes de estudio:"+ res.getValor());
        if(res.getCorrecto()){
            rol.setPlanEstudios(res.getValor());
            //mostrarMensajeResultadoEJB(res);
        }
    }

    public void obtenerAreasUniversidad(){
        ResultadoEJB<List<AreasUniversidad>> resAU = ejb.obtenerAreasUniversidad();
        //System.out.println("Areas de universidad:"+resAU.getValor());
        if(resAU.getCorrecto()){
            rol.setAreasUniversidades(resAU.getValor());
            //mostrarMensajeResultadoEJB(resAU);
        }
    }

    public void obtenerGeneraciones(){
        ResultadoEJB<Generaciones> res = ejb.obtenerGeneraciones();
        //System.out.println("Generacion:"+res.getValor());
        if(res.getCorrecto()){
            rol.setGeneraciones(res.getValor());
            //mostrarMensajeResultadoEJB(res);
        }
    }

    public void obtenerSugerencia(){
        ResultadoEJB<List<DtoConteoGrupos>> res = ejb.obtenerSugerenciaGeneracionGrupos();
        if(res.getCorrecto()){
            rol.setListaSugerencia(res.getValor());
        }
    }

    public void generarGrupo(){
        ResultadoEJB<Grupo> res;
        if(rol.getGrupo().getIdGrupo() == null){
            rol.setCapMax(30);
            rol.getGrupo().setPeriodo(rol.getPeriodoAct());
            rol.getGrupo().setPlan(rol.getPlanEstudio());
            rol.getGrupo().setGeneracion(rol.getGeneraciones().getGeneracion());
            res = ejb.guardarGrupo(rol.getGrupo(), rol.getPeriodoAct(), rol.getNoGrupos(), rol.getCapMax(), rol.getPlanEstudio(), rol.getGeneraciones(), Operacion.PERSISTIR);
            mostrarMensajeResultadoEJB(res);
            obtenerGrupos();
            obtenerSugerencia();
            rol.setGrupo(new Grupo());
            rol.setNoGrupos(0);
        }
    }

    public void actualizarGrupo(CellEditEvent event) {
        rol.setCapMax(30);
        DataTable dataTable = (DataTable) event.getSource();
        Grupo grupoNew = (Grupo) dataTable.getRowData();
        ejb.guardarGrupo(grupoNew, rol.getPeriodoAct(), rol.getNoGrupos(), rol.getCapMax(), rol.getPlanEstudio(), rol.getGeneraciones(), Operacion.ACTUALIZAR);
        mostrarMensaje("Se ha actualizado la información del grupo seleccionado");
    }

    public void eliminarGrupo(Grupo grupo){
        ResultadoEJB<Grupo> res = ejb.eliminarGrupo(grupo, Operacion.ELIMINAR);
        mostrarMensajeResultadoEJB(res);
        obtenerGrupos();
        //alertas();
    }

    public void alertas(){
        setAlertas(Collections.EMPTY_LIST);
        ResultadoEJB<List<DtoAlerta>> resMensajes = ejb.identificarAlertas(rol);
        if (resMensajes.getCorrecto()) {
            setAlertas(resMensajes.getValor());
        } else {
            mostrarMensajeResultadoEJB(resMensajes);
        }
        repetirUltimoMensaje();
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "generación grupos";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }

}
