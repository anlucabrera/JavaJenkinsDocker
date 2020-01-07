/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Baja;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistrarBajaRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaReprobada;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRangoFechasPermiso;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRegistroBajaEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.BajaReprobacion;
import org.omnifaces.util.Ajax;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;


/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class RegistrarBajaServiciosEscolares extends ViewScopedRol implements Desarrollable{
    @Getter @Setter RegistrarBajaRolServiciosEscolares rol;

    @EJB EjbRegistroBajas ejb;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    @Inject GeneracionFormatoBaja generacionFormatoBaja;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por personal de servicios escolares<br/>
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
            setVistaControlador(ControlEscolarVistaControlador.REGISTRAR_BAJAS);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es personal de servicios escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarServiciosEscolares(logon.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo serviciosEscolares = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new RegistrarBajaRolServiciosEscolares(filtro, serviciosEscolares);
            tieneAcceso = rol.tieneAcceso(serviciosEscolares);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(serviciosEscolares);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
            rol.setForzarAperturaDialogo(Boolean.FALSE);
          
            rol.getInstrucciones().add("Ingrese nombre o matricula del o de la estudiante que dará de baja.");
            rol.getInstrucciones().add("Seleccionar causa de baja.");
            rol.getInstrucciones().add("Seleccionar tipo de baja.");
            rol.getInstrucciones().add("Seleccionar fecha de la baja.");
            rol.getInstrucciones().add("Dar clic en el botón de Guardar para registrar la baja del estudiante.");
            rol.getInstrucciones().add("Una vez que se haya registrado la baja en la parte inferior podrá visualizar una tabla con la información registrada.");
            rol.getInstrucciones().add("En la columna OPCIONES, usted puede: Consultar materias reprobadas, Generar formato y Eliminar baja.");
            rol.getInstrucciones().add("El botón de Consultar materias reprobadas se habilita únicamente en el caso de que la baja haya sido por reprobación.");
            rol.getInstrucciones().add("Para generar el formato de baja de clic en el botón Generar formato.");
            rol.getInstrucciones().add("En caso de que se haya equivocado podrá eliminar el registro dando clic en el botón Eliminar baja y cambiar la situación actual del estudiante.");
            rol.getInstrucciones().add("Puede modificar las acciones tomadas por el tutor y el dictamen de psicopedagogía.");
           
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registro de bajas";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Método para proporcionar lista de docentes sugeridos en un autocomplete donde se puede ingresar el número de nómina, nombre o área del docente
     * @param pista
     * @return Lista de sugerencias
     */
    public List<DtoEstudianteComplete> completeEstudiantes(String pista){
        ResultadoEJB<List<DtoEstudianteComplete>> res = ejb.buscarEstudiante(pista);
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
            rol.setRegistroBajaEstudiante(null);
            buscarDatosEstudiante(rol.getEstudianteSeleccionado().getEstudiantes().getIdEstudiante());
            Ajax.update("tbRegBaja");
        }else mostrarMensaje("El valor seleccionado como estudiante no es del tipo necesario.");
    }
    
    
    public void buscarDatosEstudiante(Integer claveEstudiante){
        ResultadoEJB<DtoDatosEstudiante> res = ejb.buscarDatosEstudiante(claveEstudiante);
        if(res.getCorrecto()){
        rol.setDatosEstudiante(res.getValor());
        tiposBaja();
        causasBaja();
        rangoFechasPermiso();
        existeRegistro(claveEstudiante);
        Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
     
    
    /**
     * Permite obtener la lista de tipos de baja 
     */
    public void tiposBaja(){
        if(rol.getEstudianteSeleccionado()== null) return;
        ResultadoEJB<List<BajasTipo>> res = ejb.getTiposBaja();
        if(res.getCorrecto()){
            rol.setTiposBaja(res.getValor());
            rol.setTipoBaja(rol.getTiposBaja().get(0));
        }else mostrarMensajeResultadoEJB(res);
    }
  
    /**
     * Permite obtener la lista de causa de baja
     */
    public void causasBaja(){
        if(rol.getEstudianteSeleccionado()== null) return;
        ResultadoEJB<List<BajasCausa>> res = ejb.getCausasBaja();
        if(res.getCorrecto()){
            rol.setCausasBaja(res.getValor());
            rol.setCausaBaja(rol.getCausasBaja().get(0));
        }else mostrarMensajeResultadoEJB(res);
    }
     /**
     * Permite cambiar tipo de baja
     * @param e Evento del cambio de valor
     */
    public void cambiarTipoBaja(ValueChangeEvent e){
       rol.setTipoBaja((BajasTipo)e.getNewValue());
    }
    
     /**
     * Permite cambiar causa de baja
     * @param e Evento del cambio de valor
     */
    public void cambiarCausaBaja(ValueChangeEvent e){
       rol.setCausaBaja((BajasCausa)e.getNewValue());
    }
    
     /**
     * Permite obtener el rango de fechas disponibles para registrar la baja, dependiendo del periodo escolar activo
     * periodo escolar
     */
    public void rangoFechasPermiso(){
        ResultadoEJB<DtoRangoFechasPermiso> res = ejb.getRangoFechas(rol.getPeriodoActivo());
        if(res.getCorrecto()){
            rol.setFechaBaja(new Date());
            rol.setRangoFechas(res.getValor());
            rol.setFechaInicio(rol.getRangoFechas().getRangoFechaInicial());
            rol.setFechaFin(rol.getRangoFechas().getRangoFechaFinal());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
       
    }
    
     /**
     * Permite guardar el permiso de captura extemporánea ordinaria, para ello el usuario debió haber llenado todos los datos correspondientes y haber seleccionado
     * en tipo de evaluacion "Ordinaria"
     */
    public void guardarRegistroBaja(){
        ResultadoEJB<Baja> res = ejb.guardarRegistroBaja(rol.getDatosEstudiante().getPeriodoEscolar().getPeriodo(), rol.getDatosEstudiante(), rol.getTipoBaja(), rol.getCausaBaja(), rol.getPersonal(), rol.getFechaBaja());
        if(res.getCorrecto()){
            rol.setBajaRegistrada(res.getValor());
            mostrarMensajeResultadoEJB(res);
            buscarDatosEstudiante(rol.getBajaRegistrada().getEstudiante().getIdEstudiante());
            Ajax.update("frm");
            Ajax.update("tbRegBaja");
        }else mostrarMensajeResultadoEJB(res);
        
    }
    
     /**
     * Permite verificar si existe registro de baja del estudiante seleccionado
     * @param clave Clave del estudiante para buscar registro
     */
    public void existeRegistro(Integer clave){
       ResultadoEJB<DtoRegistroBajaEstudiante> res = ejb.buscarRegistroBajaEstudiante(clave);
       if(res.getCorrecto()){
            rol.setRegistroBajaEstudiante(res.getValor());
            mostrarMensajeResultadoEJB(res);
            Ajax.update("frm");
        }
     }
   
      /**
     * Permite eliminar el registro de baja seleccionado
     * @param registro Registro de baja que se desea eliminar
     */
    public void eliminarRegistroBaja(Baja registro){
        ResultadoEJB<Integer> resEliminar = ejb.eliminarRegistroBaja(registro);
        mostrarMensajeResultadoEJB(resEliminar);
        buscarDatosEstudiante(registro.getEstudiante().getIdEstudiante());
        rol.setRegistroBajaEstudiante(null);
        Ajax.update("tbRegBaja");
    }
    
      /**
     * Permite buscar materias reprobadas en caso de ser baja por reprobación
     * @param registro Registro de baja del que se realizará búsqueda
     */
    public void buscarMateriasReprobadas(Baja registro){
        ResultadoEJB<List<DtoMateriaReprobada>> resbusqueda = ejb.buscarMateriasReprobadas(registro);
        if(resbusqueda.getCorrecto()){
            rol.setListaMateriasReprobadas(resbusqueda.getValor());
            mostrarMensajeResultadoEJB(resbusqueda);
            Ajax.update("frmModalMateriasReprobadas");
            Ajax.oncomplete("skin();");
            rol.setForzarAperturaDialogo(Boolean.TRUE);
            forzarAperturaDialogo();
        }
    }
    
     public void forzarAperturaDialogo(){
        if(rol.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalMateriasReprobadas').show();");
            rol.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
     
      /**
     * Permite eliminar el registro de la materia reprobada seleccionado
     * @param registro Registro de materia reprobada que se desea eliminar
     */
    public void eliminarRegistroMateriaReprobada(BajaReprobacion registro){
        ResultadoEJB<Integer> resEliminar = ejb.eliminarRegistroMateriaReprobada(registro);
        mostrarMensajeResultadoEJB(resEliminar);
        buscarMateriasReprobadas(registro.getRegistroBaja());
        rol.setRegistroBajaEstudiante(null);
        Ajax.update("tblModalMatRep");
    }
    
      /**
     * Permite generar el formato de baja del registro seleccionado
     * @param registro Registro de la baja
     */
    public void generarFormatoBaja(Baja registro){
       generacionFormatoBaja.generarFormatoBaja(registro);
    }
    
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoRegistroBajaEstudiante registroNew = (DtoRegistroBajaEstudiante) dataTable.getRowData();
        ejb.actualizarRegistroBaja(registroNew);
        ejb.actualizarStatusEstudiante(registroNew);
        rol.setDatosEstudiante(ejb.buscarDatosEstudiante(registroNew.getRegistroBaja().getEstudiante().getIdEstudiante()).getValor());
        rol.setRegistroBajaEstudiante(ejb.buscarRegistroBajaEstudiante(registroNew.getRegistroBaja().getEstudiante().getIdEstudiante()).getValor());

    }
}
