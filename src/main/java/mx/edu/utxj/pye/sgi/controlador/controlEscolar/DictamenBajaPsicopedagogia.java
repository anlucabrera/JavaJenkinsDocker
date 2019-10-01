/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaReprobada;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoTramitarBajas;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DictamenBajaRolPsicopedagogia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoValidacionesBaja;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Baja;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class DictamenBajaPsicopedagogia extends ViewScopedRol implements Desarrollable{
    @Getter @Setter DictamenBajaRolPsicopedagogia rol;
    
    @EJB EjbRegistroBajas ejb;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    @Inject GeneracionFormatoBaja generacionFormatoBaja;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por área de psicopedagogía<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    @PostConstruct
    public void init(){
        try{
            setVistaControlador(ControlEscolarVistaControlador.DICTAMEN_BAJAS);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarPsicopedagogia(logon.getPersonal().getClave());//validar si es personal del área de psicopedagogía
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personalPsicopedagogia = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new DictamenBajaRolPsicopedagogia(filtro, personalPsicopedagogia);
            tieneAcceso = rol.tieneAcceso(personalPsicopedagogia);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setPersonalPsicopedagogia(personalPsicopedagogia);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
            rol.setForzarAperturaDialogo(Boolean.FALSE);
            
            rol.getInstrucciones().add("Seleccione periodo escolar para consultar bajas registradas durante ese periodo.");
            rol.getInstrucciones().add("Seleccione programa educativo.");
            rol.getInstrucciones().add("En la columna OPCIONES, usted puede: Registrar dictamen, Consultar materias reprobadas y generar formato de baja.");
            rol.getInstrucciones().add("Dar clic en el botón de Registrar dictamen, usted podrá ingresar el dictamen realizado por el área de psicopedagogía.");
            rol.getInstrucciones().add("El botón de Consultar materias reprobadas se habilita únicamente en el caso de que la baja haya sido por reprobación.");
            rol.getInstrucciones().add("Para generar el formato de baja de clic en el botón Generar formato.");
            rol.getInstrucciones().add("Puede modificar la información el dictamen de la baja siempre y cuando no haya sido validada por el director de carrera.");
           
            periodosBajasRegistradas();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "dictamen baja psicopedagogia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de periodo escolares en lo que se hay bajas registradas
     */
    public void periodosBajasRegistradas(){
        ResultadoEJB<List<PeriodosEscolares>> res = ejb.getPeriodosBajas();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setPeriodos(res.getValor());
                rol.setPeriodo(rol.getPeriodos().get(0));
                listaBajasPeriodo();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de bajas registradas en el periodo seleccionado
     */
    public void listaBajasPeriodo(){
        if(rol.getPeriodo() == null) return;
        ResultadoEJB<List<DtoTramitarBajas>> res = ejb.obtenerListaBajasPeriodo(rol.getPeriodo());
        if(res.getCorrecto()){
            rol.setBajas(res.getValor());
            programasEducativosBajasRegistradas();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite obtener la lista de programas educativos que tienen bajas registradas
     */
    public void programasEducativosBajasRegistradas(){
        ResultadoEJB<List<AreasUniversidad>> res = ejb.getProgramasEducativos(rol.getBajas());
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setProgramasEducativos(res.getValor());
                rol.setProgramaEducativo(rol.getProgramasEducativos().get(0));
                listaBajasProgramaEducativo();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de estudiantes con registro de baja del periodo y programa educativo seleccionado previamente
     */
    public void listaBajasProgramaEducativo(){
        ResultadoEJB<List<DtoTramitarBajas>> res = ejb.obtenerListaBajasProgramaEducativo(rol.getBajas(), rol.getProgramaEducativo());
        if(res.getCorrecto()){
            rol.setBajasProgramaEducativo(res.getValor());
            Ajax.update("tbListaRegistroBajas");
        }else mostrarMensajeResultadoEJB(res);
    
    }
   
    /**
     * Permite que al cambiar o seleccionar un periodo escolar se pueda actualizar la lista de bajas por periodo
     * @param e Evento del cambio de valor
     */
    public void cambiarPeriodo(ValueChangeEvent e){
        if(e.getNewValue() instanceof PeriodosEscolares){
            PeriodosEscolares periodo = (PeriodosEscolares)e.getNewValue();
            rol.setPeriodo(periodo);
            listaBajasPeriodo();
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
            listaBajasProgramaEducativo();
            Ajax.update("frm");
        }else mostrarMensaje("");
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
     * Permite verificar si existe dictamen registrado
     * @param baja Registro de la baja
     * @return valor boolean según sea el caso
     */
    public Boolean consultarDictamen(DtoTramitarBajas baja){
        rol.setDictamenBaja(ejb.buscarDictamenBajaPsicopedagogia(baja.getDtoRegistroBaja().getRegistroBaja()).getValor());
        if(rol.getDictamenBaja().equals("Sin información")){
            rol.setExisteDictamen(Boolean.FALSE);
        }else{
            rol.setExisteDictamen(Boolean.TRUE);
        }
        return rol.getExisteDictamen();
    }
    
    /**
     * Permite editar dictamen de baja del estudiante seleccionado
     * @param baja Registro de la baja
     */
    public void editarBaja(DtoTramitarBajas baja){
        rol.setBaja(baja);
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
     * Permite guardar el dictamen realizado por el área de psicopedagogía de la baja seleccionada
     */
    public void guardarDictamenBaja(){
        ResultadoEJB<Baja> res = ejb.actualizarDictamenBaja(rol.getBaja().getDtoRegistroBaja().getRegistroBaja(), rol.getDictamenBaja());
        if(res.getCorrecto()){
             rol.setBajaRegistrada(res.getValor());
             mostrarMensajeResultadoEJB(res);
        }else mostrarMensajeResultadoEJB(res);
        listaBajasPeriodo();
        Ajax.update("frm");
    } 
     
      /**
     * Permite generar el formato de baja del registro seleccionado
     * @param registro Registro de la baja
     */
    public void generarFormatoBaja(Baja registro){
       generacionFormatoBaja.generarFormatoBaja(registro);
    }
    
     /**
     * Permite verificar el status de la baja
     * @param baja Registro de la baja
     * @return valor boolean según sea el caso
     */
    public DtoValidacionesBaja consultarStatusBaja(DtoTramitarBajas baja){
        rol.setDtoValidacionesBaja(ejb.buscarValidacionesBaja(baja.getDtoRegistroBaja().getRegistroBaja()).getValor());
        return rol.getDtoValidacionesBaja();
    }
    
    
}
