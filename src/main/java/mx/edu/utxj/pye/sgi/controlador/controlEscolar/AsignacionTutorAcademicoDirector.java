/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
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
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionTutores;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AsignacionTutorRolDirector;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoListadoTutores;
import mx.edu.utxj.pye.sgi.dto.vista.DtoAlerta;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author UTXJ
 */
@Named(value = "asignacionTutorAcademico")
@ViewScoped
public class AsignacionTutorAcademicoDirector extends ViewScopedRol implements Desarrollable{
    @Getter @Setter AsignacionTutorRolDirector rol;
    
    @EJB EjbAsignacionTutores ejb;
    @Inject LogonMB logon;
    @EJB EjbPropiedades ep;
    
    @Getter Boolean tieneAcceso = false;
    
    /**
     * Inicializa
     *      El filtro de rol por área superior y categoría operativa<br/>
     *      La referencia al director si es que el usuario logueado es efectivamente un director por medio del filtro de rol<br/>
     *      El programa educativo al que pertenece el director por medio de operación segura antierror<br/>
     *      El DTO del rol<br/>
     *      La lista de periodos escolares en forma descendente por medio de operación segura antierror<br/>
     *      EL mapa de programas con el DTO de grupos y su refencia al tutor de grupo
     */
    @PostConstruct
    public void init(){
        try {
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDirector(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
                        
            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarDirector(logon.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
            
            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AsignacionTutorRolDirector(filtro, director, director.getAreaOficial());
            tieneAcceso = rol.tieneAcceso(director);
            
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            
            rol.setDirector(director);
            ResultadoEJB<EventoEscolar> resEvento = ejb.verificarEvento(rol.getDirector());
            if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
            if(verificarInvocacionMenu()) return;
            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setEventoActivo(resEvento.getValor());
            
            
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDesendentes();
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());
            
            ResultadoEJB<Map<AreasUniversidad, List<DtoListadoTutores >>> resProgramas = ejb.getProgramasEducativosGrupoTutor(rol.getDirector(), rol.getPeriodo());
            if(!resProgramas.getCorrecto()) mostrarMensajeResultadoEJB(resProgramas);
            rol.setTutoresGruposMap(resProgramas.getValor());
            
            rol.getInstrucciones().add("Seleccionar el periodo escolar activo");
            rol.getInstrucciones().add("Seleccionar el programa educativo en el cual pertenece el grupo al que se le asignará el tutor");
            rol.getInstrucciones().add("Usted podrá visualizar en la tabla de consulta todos los grupos que se encuentran disponibles en el programa educativo previamente seleccionado");
            rol.getInstrucciones().add("Para asignar al tutor deberá dar clic en el campo vacío de la tabla y usted podrá escribir parte del nombre o número de nómina y elegir al docente en la lista desplegable que aparecerá después de una pausa de un segundo.");
            rol.getInstrucciones().add("En caso de reasignar o modificar al tutor de grupo usted deberá repetir el paso anterior cuantas veces sean necesarias");
            
        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "Asignación Tutor de Grupo";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
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
            return  res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return Collections.EMPTY_LIST;
        }
    }
    
    /**
     * Permite seleccionar un programa desde un evento de selección única, se recomienda actualizar al control de grupos ya que será sincronizado
     * de forma inmediata y se seleccionará el primer grupo de esa lista para que también se actualicen los controles que visualicen al grupo
     * @param e Evento de selección única, se recomienda un selectOneMenu
     */
    public void seleccionarPrograma(ValueChangeEvent e){
        if(e.getNewValue() instanceof AreasUniversidad){
            rol.setPrograma((AreasUniversidad) e.getNewValue());
            alertas();
            Ajax.update("grupo");//actualizaría al selector de grupos si lleva el id especificado
        }
        
    }
    
    public void alertas() {
        setAlertas(Collections.EMPTY_LIST);
        ResultadoEJB<List<DtoAlerta>> resMensajes = ejb.identificarMensajes(rol);
        if (resMensajes.getCorrecto()) {
            setAlertas(resMensajes.getValor());
        } else {
            mostrarMensajeResultadoEJB(resMensajes);
        }
        repetirUltimoMensaje();
    }

    /**
     * Permite seleccionar un periodo escolar desde un evento unico, se recomienda actualizar al control de programas de estudio y listado de grupos y que
     * sera sincronozado inmediatamente
     * @param e Evento de selección única, se recomienda un selectOneMenu
     */
    public void seleccionarPeriodoEscolar(ValueChangeEvent e){
        if(e.getNewValue() instanceof PeriodosEscolares){
            rol.setPeriodo((PeriodosEscolares) e.getNewValue());
            ResultadoEJB<Map<AreasUniversidad, List<DtoListadoTutores >>> resProgramas = ejb.getProgramasEducativosGrupoTutor(rol.getDirector(), rol.getPeriodo());
            if(!resProgramas.getCorrecto()) mostrarMensajeResultadoEJB(resProgramas);
            rol.setTutoresGruposMap(resProgramas.getValor());
            Ajax.update("grupo");
            Ajax.update("programa");
        }
    }
    
    /**
     * Permite habilitar el control de buscar un docente para realizar la asignación de un tutor de grupo
     * @param event Evento de edicion de celda de un DataTable
     */
    public void onCellEditTutorGrupo(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoListadoTutores grupoTutor = (DtoListadoTutores) dataTable.getRowData();
        if(grupoTutor.getTutor() == null){
            grupoTutor.setTutor(new PersonalActivo(new Personal()));
        }else{
            ejb.asignarTutorGrupo(grupoTutor, Operacion.PERSISTIR);
        }
        alertas();
    }
}
