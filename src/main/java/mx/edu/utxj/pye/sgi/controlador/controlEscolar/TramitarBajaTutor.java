/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoListadoTutores;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaReprobada;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRangoFechasPermiso;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoTramitarBajas;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.TramitarBajaRolTutor;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Baja;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.BajaReprobacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class TramitarBajaTutor extends ViewScopedRol implements Desarrollable{
    @Getter @Setter TramitarBajaRolTutor rol;
    
    @EJB EjbRegistroBajas ejb;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    @Inject GeneracionFormatoBaja generacionFormatoBaja;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por area superior<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    @PostConstruct
    public void init(){
        try{
            setVistaControlador(ControlEscolarVistaControlador.TRAMITAR_BAJAS);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDocente(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personalDocente = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new TramitarBajaRolTutor(filtro, personalDocente);
            tieneAcceso = rol.tieneAcceso(personalDocente);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setTutor(personalDocente);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
            rol.setForzarAperturaDialogo(Boolean.FALSE);
            
            rol.getInstrucciones().add("Ingrese nombre o clave del o de la estudiante que dará de baja.");
            rol.getInstrucciones().add("Seleccionar causa de baja.");
            rol.getInstrucciones().add("Seleccionar tipo de baja.");
            rol.getInstrucciones().add("Seleccionar fecha de la baja.");
            rol.getInstrucciones().add("Dar clic en el botón de Guardar para registrar la baja del estudiante.");
            rol.getInstrucciones().add("Seleccionar la justificación por la cual el docente solicitó el permiso.");
            rol.getInstrucciones().add("Una vez que haya ingresado la información, puede proceder a REGISTRAR el permiso.");
            rol.getInstrucciones().add("En la tabla inferior podrá VISUALIZAR los permisos de captura extemporanea vigentes del docente seleccionado.");
            rol.getInstrucciones().add("En caso de existir un error puede ELIMINAR el permiso de captura, al dar clic en el botón ubicado en la columna opciones de la tabla.");
           
            rol.setEsTutor(Boolean.FALSE);
            periodosGruposTutorados();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "tramitar baja tutor";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de periodo escolares en los que el docente tiene carga académica
     */
    public void periodosGruposTutorados(){
        ResultadoEJB<List<PeriodosEscolares>> res = ejb.getPeriodosGruposTutorados(rol.getTutor());
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setPeriodos(res.getValor());
                rol.setPeriodo(rol.getPeriodos().get(0));
                rol.setEsTutor(Boolean.TRUE);
                gruposTutorados();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de cargas académicas del docente y el periodo seleccionado previamente
     */
    public void gruposTutorados(){
        if(rol.getPeriodo() == null) return;
        if(rol.getTutor()== null) return;
        ResultadoEJB<List<DtoListadoTutores>> res = ejb.getGruposTutorados(rol.getTutor(), rol.getPeriodo());
        if(res.getCorrecto()){
            rol.setGrupos(res.getValor());
            rol.setGrupo(rol.getGrupos().get(0));
            listaEstudiantes();
            tiposBaja();
            causasBaja();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Método para proporcionar lista de docentes sugeridos en un autocomplete donde se puede ingresar el número de nómina, nombre o área del docente
     */
    public void listaEstudiantes(){
        ResultadoEJB<List<DtoTramitarBajas>> res = ejb.obtenerListaEstudiantes(rol.getGrupo().getGrupo());
        if(res.getCorrecto()){
            rol.setEstudiantesGrupo(res.getValor());
        }else mostrarMensajeResultadoEJB(res);
            
    }
    
    /**
     * Permite que al cambiar o seleccionar un docente se puedan actualizar las materias asignadas a este docente
     * @param e Evento del cambio de valor
     */
    public void cambiarPeriodo(ValueChangeEvent e){
        if(e.getNewValue() instanceof PeriodosEscolares){
            PeriodosEscolares periodo = (PeriodosEscolares)e.getNewValue();
            rol.setPeriodo(periodo);
            gruposTutorados();
            Ajax.update("frm");
        }else mostrarMensaje("El valor seleccionado como estudiante no es del tipo necesario.");
    }
    
     /**
     * Permite que al cambiar o seleccionar un docente se puedan actualizar las materias asignadas a este docente
     * @param e Evento del cambio de valor
     */
    public void cambiarGrupo(ValueChangeEvent e){
        if(e.getNewValue() instanceof DtoListadoTutores){
            DtoListadoTutores grupo = (DtoListadoTutores)e.getNewValue();
            rol.setGrupo(grupo);
            listaEstudiantes();
            Ajax.update("frm");
        }else mostrarMensaje("El valor seleccionado como estudiante no es del tipo necesario.");
    }
   
    /**
     * Permite obtener la lista de tipos de baja 
     */
    public void tiposBaja(){
        if(rol.getGrupo()== null) return;
        ResultadoEJB<List<BajasTipo>> res = ejb.getTiposBaja();
        if(res.getCorrecto()){
            rol.setTiposBaja(res.getValor());
        }else mostrarMensajeResultadoEJB(res);
    }
  
    /**
     * Permite obtener la lista de causa de baja
     */
    public void causasBaja(){
        if(rol.getGrupo()== null) return;
        ResultadoEJB<List<BajasCausa>> res = ejb.getCausasBaja();
        if(res.getCorrecto()){
            rol.setCausasBaja(res.getValor());
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
            Ajax.update("frmModalTramitarBaja");
        }else mostrarMensajeResultadoEJB(res);
       
    }
    
     /**
     * Permite guardar el permiso de captura extemporánea ordinaria, para ello el usuario debió haber llenado todos los datos correspondientes y haber seleccionado
     * en tipo de evaluacion "Ordinaria"
     */
    public void guardarTramitarBaja(){
        if(rol.getExisteRegistroBaja() == null)
        {  
            ResultadoEJB<Baja> res = ejb.guardarTramitarBaja(rol.getPeriodoActivo(), rol.getEstudiante().getDtoEstudiante(), rol.getTipoBaja(), rol.getCausaBaja(), rol.getAccionesTutor(), rol.getTutor(), rol.getFechaBaja());
            if(res.getCorrecto()){
                rol.setBajaRegistrada(res.getValor());
                mostrarMensajeResultadoEJB(res);
            }else mostrarMensajeResultadoEJB(res);
             
        }else{
            ResultadoEJB<Baja> res = ejb.actualizarTramitarBaja(rol.getPeriodoActivo(), rol.getRegistroBajaEstudiante(), rol.getTipoBaja(), rol.getCausaBaja(), rol.getAccionesTutor(), rol.getTutor(), rol.getFechaBaja());
            if(res.getCorrecto()){
                rol.setBajaRegistrada(res.getValor());
                mostrarMensajeResultadoEJB(res);
                
            }else mostrarMensajeResultadoEJB(res);
        }
        listaEstudiantes();
        Ajax.update("frm");
    }
    
     /**
     * Permite verificar si existe registro de baja del estudiante seleccionado
     * @param clave Clave del estudiante para buscar registro
     */
    public void existeRegistro(Integer clave){
        ResultadoEJB<Boolean> res = ejb.existeRegistroBajaEstudiante(clave);
        rol.setExisteRegistroBaja(res.getValor());
        if (rol.getExisteRegistroBaja() == null) {
            rol.setCausaBaja(rol.getCausasBaja().get(0));
            rol.setTipoBaja(rol.getTiposBaja().get(0));
            rol.setFechaBaja(new Date());
            rol.setAccionesTutor("");

        } else {
            rol.setRegistroBajaEstudiante(ejb.buscarRegistroBajaEstudiante(clave).getValor());
            rol.setCausaBaja(rol.getRegistroBajaEstudiante().getCausaBaja());
            rol.setTipoBaja(rol.getRegistroBajaEstudiante().getTipoBaja());
            rol.setFechaBaja(rol.getRegistroBajaEstudiante().getRegistroBaja().getFechaBaja());
            rol.setAccionesTutor(rol.getRegistroBajaEstudiante().getRegistroBaja().getAccionesTutor());
        }
        mostrarMensajeResultadoEJB(res);
     }
   
      /**
     * Permite eliminar el registro de baja seleccionado
     * @param registro Registro de baja que se desea eliminar
     */
    public void eliminarRegistroBaja(Baja registro){
        ResultadoEJB<Integer> resEliminar = ejb.eliminarRegistroBaja(registro);
        mostrarMensajeResultadoEJB(resEliminar);
        
        listaEstudiantes();
        Ajax.update("frm");
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
            forzarAperturaDialogoMateriasReprobadas();
        }
    }
    
    /**
     * Permite tramitar baja del estudiante seleccionado
     * @param estudiante Estudinte del que se tramitará baja
     */
    public void editarBaja(DtoTramitarBajas estudiante){
        rol.setEstudiante(estudiante);
        tiposBaja();
        causasBaja();
        rangoFechasPermiso();
        existeRegistro(rol.getEstudiante().getDtoEstudiante().getEstudiante().getIdEstudiante());
        Ajax.update("frmModalTramitarBaja");
        Ajax.oncomplete("skin();");
        rol.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaDialogoTramitarBaja();
    }
    
     public void forzarAperturaDialogoTramitarBaja(){
        if(rol.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalTramitarBaja').show();");
            rol.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
     
     public void forzarAperturaDialogoMateriasReprobadas(){
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
        DtoTramitarBajas registroNew = (DtoTramitarBajas) dataTable.getRowData();
        ejb.actualizarRegistroBaja(registroNew.getDtoRegistroBaja());
    }
    
}
