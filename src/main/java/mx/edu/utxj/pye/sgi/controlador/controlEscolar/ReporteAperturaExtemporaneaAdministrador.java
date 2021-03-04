/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPermisoAperturaExtemporanea;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ValidacionAperturaExtRolAdministrador;
import org.omnifaces.util.Ajax;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoValidarPermisoCapExt;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class ReporteAperturaExtemporaneaAdministrador extends ViewScopedRol implements Desarrollable{
    @Getter @Setter ValidacionAperturaExtRolAdministrador rol;

    @EJB EjbPermisoAperturaExtemporanea ejb;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por area superior<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
        try{
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.REPORTE_APERTURA_EXTEMPORANEA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarAdministrador(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarAdministrador(logon.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo administrador = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ValidacionAperturaExtRolAdministrador(filtro, administrador);
            tieneAcceso = rol.tieneAcceso(administrador);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setAdministrador(administrador);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
          
            rol.getInstrucciones().add("Ingrese nombre o clave del docente al que se le aperturará sistema.");
            rol.getInstrucciones().add("Seleccionar Periodo Escolar.");
            rol.getInstrucciones().add("Seleccionar Materia - Grupo - Programa Educativo.");
            rol.getInstrucciones().add("Seleccionar Tipo de Evaluación: Ordinaria o Nivelación Final.");
            rol.getInstrucciones().add("En caso de que haya seleccionado ORDINARIA en tipo de evaluación, DEBERÁ seleccionar la unidad correspondiente, en caso contrario NO es necesario seleccionar UNIDAD");
            rol.getInstrucciones().add("Ingresar fecha de inicio y fin en la que estará habilitada la captura extemporanea.");
            rol.getInstrucciones().add("Seleccionar la justificación por la cual el docente solicitó el permiso.");
            rol.getInstrucciones().add("Una vez que haya ingresado la información, puede proceder a REGISTRAR el permiso.");
            rol.getInstrucciones().add("En la tabla inferior podrá VISUALIZAR los permisos de captura extemporanea vigentes del docente seleccionado.");
            rol.getInstrucciones().add("En caso de existir un error puede ELIMINAR el permiso de captura, al dar clic en el botón ubicado en la columna opciones de la tabla.");
           
            periodosPermisosRegistrados();
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "reporte apertura extemporanea";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de periodo escolares en los que existen bajas registradas
     */
    public void periodosPermisosRegistrados(){
        ResultadoEJB<List<PeriodosEscolares>> res = ejb.getPeriodosPermisosRegistrados();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setPeriodos(res.getValor());
                rol.setPeriodo(rol.getPeriodos().get(0));
                programasEducativosPermisosRegistrados();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite obtener la lista de programas educativos que tienen bajas registradas dependiendo del área de la que es el director
     */
    public void programasEducativosPermisosRegistrados(){
        ResultadoEJB<List<AreasUniversidad>> res = ejb.getProgramasEducativosPermisosRegistrados(rol.getPeriodo());
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setProgramasEducativos(res.getValor());
                rol.setProgramaEducativo(rol.getProgramasEducativos().get(0));
                listaPermisosProgramaEducativo();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de permisos de aperturas extemporáneas en el periodo y del programa educativo seleccionado
     */
    public void listaPermisosProgramaEducativo(){
        ResultadoEJB<List<DtoValidarPermisoCapExt>> res = ejb.obtenerListaPermisosPeriodoProgramaEducativo(rol.getPeriodo(), rol.getProgramaEducativo());
        if(res.getCorrecto()){
            rol.setPermisosProgramaEducativo(res.getValor());
            Ajax.update("tbListaPermisosSolicitados");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
   
    /**
     * Permite que al cambiar o seleccionar un periodo escolar se pueda actualizar la lista de bajas del periodo
     * @param e Evento del cambio de valor
     */
    public void cambiarPeriodo(ValueChangeEvent e){
        if(e.getNewValue() instanceof PeriodosEscolares){
            PeriodosEscolares periodo = (PeriodosEscolares)e.getNewValue();
            rol.setPeriodo(periodo);
            programasEducativosPermisosRegistrados();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un programa educativo se pueda actualizar la lista de bajas por programa educativo
     * @param e Evento del cambio de valor
     */
    public void cambiarProgramaEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  AreasUniversidad){
            AreasUniversidad programa = (AreasUniversidad)e.getNewValue();
            rol.setProgramaEducativo(programa);
            listaPermisosProgramaEducativo();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite modificar la situación de la baja
     * @param permiso Registro de la baja
     */
    public void validarPermiso(DtoValidarPermisoCapExt permiso){
        ResultadoEJB<Integer> resValidar = ejb.validarPermiso(permiso, rol.getAdministrador());
        mostrarMensajeResultadoEJB(resValidar);
        listaPermisosProgramaEducativo();
        Ajax.update("frm");
    }
    
//      /**
//     * Permite eliminar el registro de baja seleccionado
//     * @param permiso Registro de baja que se desea eliminar
//     */
//    public void eliminarPermiso(DtoValidarPermisoCapExt permiso){
//        ResultadoEJB<Integer> resEliminar = ejb.eliminarRegistroBaja(registro);
//        mostrarMensajeResultadoEJB(resEliminar);
//         periodosPermisosSolicitados();
//        Ajax.update("frm");
//    }
    
    /**
     * Permite descargar aperturas extemporáneas registradas en el periodo escolar seleccionado
     * @throws java.io.IOException
     */
    public void descargarReportePeriodo() throws IOException, Throwable{
        File f = new File(ejb.getReportePeriodo(rol.getPeriodo()));
        Faces.sendFile(f, true);
    }
}
