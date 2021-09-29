/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.EntregaFotografiasRolEscolares;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionRolesEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbEstadiasServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Collections;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEntregaFotografiasEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPorcentajeEntregaFotografias;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EntregaFotografiasEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class EntregaFotografiasEscolares extends ViewScopedRol implements Desarrollable{
    @Getter @Setter EntregaFotografiasRolEscolares rol;
    
    @EJB EjbEstadiasServiciosEscolares ejb;
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
    @EJB EjbSeguimientoEstadia ejbSeguimientoEstadia;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por dirección de carrera<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
    if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.ENTREGA_FOTOGRAFIAS_ESTADIA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es director

            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personal = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new EntregaFotografiasRolEscolares(filtro, personal);
            tieneAcceso = rol.tieneAcceso(personal);
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setUsuario(personal);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setDesactivarRegistro(Boolean.FALSE);
//            rol.setSoloLectura(true);
            
            rol.getInstrucciones().add("Seleccione generación.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("Ingrese la matricula o el nombre del estudiante para realizar la búsqueda.");
            rol.getInstrucciones().add("Seleccionar de la lista el registro del estudiante que corresponda.");
            rol.getInstrucciones().add("De clic en el botón para registrar entrega de fotografías.");
            rol.getInstrucciones().add("Si se equivocó puede eliminar el registro en la tabla que se muestra en la parte inferior.");
           
            generacionesEventosRegistrados();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "entrega fotografias estadia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de generaciones en los que existen eventos de estadía registrados
     */
    public void generacionesEventosRegistrados(){
        ResultadoEJB<List<Generaciones>> res = ejbAsignacionRolesEstadia.getGeneracionesEventosRegistrados();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                listaNivelesGeneracion();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de bajas registradas en el periodo seleccionado
     */
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejbAsignacionRolesEstadia.getNivelesGeneracionEventosRegistrados(rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            listaEstudiantesEntregaronFotografias();
            listaPorcentajeEntregaFotografias();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Método para proporcionar lista de docentes sugeridos en un autocomplete donde se puede ingresar el número de nómina, nombre o área del docente
     * @param pista
     * @return Lista de sugerencias
     */
    public List<DtoEstudianteComplete> completeEstudiantes(String pista){
        ResultadoEJB<List<DtoEstudianteComplete>> res = ejb.buscarEstudiante(rol.getGeneracion(), rol.getNivelEducativo(), pista);
            if(res.getCorrecto()){
                return res.getValor();
            }else{
                mostrarMensajeResultadoEJB(res);
                return Collections.emptyList();
            }
    }
    
    /**
     * Permite que al cambiar o seleccionar un estudiante se pueda actualizar la información
     * @param e Evento del cambio de valor
     */
    public void cambiarEstudiante(ValueChangeEvent e){
        if(e.getNewValue() instanceof DtoEstudianteComplete){
            DtoEstudianteComplete estudiante = (DtoEstudianteComplete)e.getNewValue();
            rol.setEstudianteSeleccionado(estudiante);
            buscarDatosEstudiante(rol.getEstudianteSeleccionado().getEstudiantes().getIdEstudiante());
            Ajax.update("frm");
        }else mostrarMensaje("El valor seleccionado como estudiante no es del tipo necesario.");
    }
    
    
    public void buscarDatosEstudiante(Integer claveEstudiante){
        ResultadoEJB<DtoDatosEstudiante> res = ejbAsignacionRolesEstadia.buscarDatosEstudiante(claveEstudiante);
        if(res.getCorrecto()){
        rol.setEstudianteRegistrado(res.getValor());
        existeRegistro();
        Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de estudiantes asignados al asesor academico y evento seleccionado
     */
    public void listaEstudiantesEntregaronFotografias(){
        ResultadoEJB<List<DtoEntregaFotografiasEstadia>> res = ejb.getListaEstudiantesEntregaronFotografias(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
            if(res.getValor().isEmpty()){
                rol.setEstudiantesFotografias(Collections.EMPTY_LIST);
            }else{
                rol.setEstudiantesFotografias(res.getValor());
            }
                Ajax.update("tbListaEntregaFotografias");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    
            /**
     * Permite obtener la lista de estudiantes asignados al asesor academico y evento seleccionado
     */
    public void listaPorcentajeEntregaFotografias(){
        ResultadoEJB<List<DtoPorcentajeEntregaFotografias>> res = ejb.getListaPorcentajeEntregaFotografias(rol.getGeneracion(), rol.getNivelEducativo(), rol.getEstudiantesFotografias());
        if(res.getCorrecto()){
            if(res.getValor().isEmpty()){
                rol.setPorcentajesEntrega(Collections.EMPTY_LIST);
                rol.setTotalEstudiantes(0);
                rol.setTotalEntregaronFotografias(0);
                rol.setTotalPendienteEntrega(0);
                rol.setTotalPorcentajeEntrega("0");
            
            }else{
                rol.setPorcentajesEntrega(res.getValor());
                rol.setTotalEstudiantes(rol.getPorcentajesEntrega().stream().mapToInt(p->p.getTotalEstudiantes()).sum());
                rol.setTotalEntregaronFotografias(rol.getPorcentajesEntrega().stream().mapToInt(p->p.getTotalEntrega()).sum());
                rol.setTotalPendienteEntrega(rol.getPorcentajesEntrega().stream().mapToInt(p->p.getTotalPendientes()).sum());
                rol.setTotalPorcentajeEntrega(String.format("%.2f",rol.getPorcentajesEntrega().stream().mapToDouble(p->p.getPorcentajeEntrega()).average().getAsDouble()));
            }
                Ajax.update("tbListaPorcentajeEntregaFotografias");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite que al cambiar o seleccionar un periodo escolar se pueda actualizar la lista de bajas del periodo
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones generacion = (Generaciones)e.getNewValue();
            rol.setGeneracion(generacion);
            listaNivelesGeneracion();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un programa educativo se pueda actualizar la lista de bajas por programa educativo
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            listaPorcentajeEntregaFotografias();
            listaEstudiantesEntregaronFotografias();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite asignar un estudiante al seguimiento de estadía del asesor académico
     */
    public void registrarEntrega(){
        ResultadoEJB<EntregaFotografiasEstudiante> resAsignar = ejb.registrarEntregaFotografias(rol.getGeneracion(), rol.getNivelEducativo(), rol.getUsuario().getPersonal(), rol.getEstudianteRegistrado());
        mostrarMensajeResultadoEJB(resAsignar);
        listaEstudiantesEntregaronFotografias();
        listaPorcentajeEntregaFotografias();
        rol.setEstudianteSeleccionado(null);
    }
    
     /**
     * Permite eliminar asignación del estudiante
     * @param dtoEntregaFotografiasEstadia
     */
    public void eliminarEntrega(DtoEntregaFotografiasEstadia dtoEntregaFotografiasEstadia){
        ResultadoEJB<Integer> resEliminar = ejb.eliminarEntregaFotografias(dtoEntregaFotografiasEstadia.getEntregaFotografiasEstudiante());
        mostrarMensajeResultadoEJB(resEliminar);
        listaEstudiantesEntregaronFotografias();
        listaPorcentajeEntregaFotografias();
    }
    
     /**
     * Permite buscar si existe registro de entrega de fotografías del estudiante seleccionado
     */
    public void existeRegistro(){
        ResultadoEJB<EntregaFotografiasEstudiante> res = ejb.buscarRegistroEntregaFotografias(rol.getGeneracion(), rol.getNivelEducativo(), rol.getEstudianteRegistrado());
        if(res.getValor() != null){
            rol.setDesactivarRegistro(Boolean.TRUE);
        }else{
            rol.setDesactivarRegistro(Boolean.FALSE);
        }
    }
    
}
