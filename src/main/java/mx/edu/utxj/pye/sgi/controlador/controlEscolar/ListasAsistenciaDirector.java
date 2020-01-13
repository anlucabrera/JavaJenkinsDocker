/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ConsultaListasAsistenciaDirector;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;


import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;


/**
 *
 * @author UTXJ
 */

@Named
@ViewScoped
public class ListasAsistenciaDirector extends ViewScopedRol implements Desarrollable{
    @Getter @Setter ConsultaListasAsistenciaDirector rol;
    
    @EJB EjbPropiedades ep;
    @EJB EjbRegistroPlanEstudio ejb;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @Inject LogonMB logon;
    
    @Getter Boolean tieneAcceso = false;
            
    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
        try {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.LISTAS_ASISTENCIA);

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarDirector(logon.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resValidaEnc = ejb.validarEncargadoDirector(logon.getPersonal().getClave());//validar si es director

            if (!resValidaEnc.getCorrecto() && !resValidacion.getCorrecto()) {
                mostrarMensajeResultadoEJB(resValidacion);
                return;
            }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ConsultaListasAsistenciaDirector(filtro, director, director.getAreaOficial());
            tieneAcceso = rol.tieneAcceso(director);

            if (!tieneAcceso) {
                rol.setFiltro(resValidaEnc.getValor());
                tieneAcceso = rol.tieneAcceso(director);
            }

            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu

            ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> resProgramaPlan = ejb.getProgramasEducativos(director);
            if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
            rol.setAreaPlanEstudioMap(resProgramaPlan.getValor());
            
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());            
            rol.setPeriodo(ejb.getPeriodoActual());
            
            rol.setPlanEstudio(rol.getPlanesEstudios().get(0));
            
            ResultadoEJB<List<Grupo>> resgrupos = ejb.getListaGrupoPlanEstudio(rol.getPlanEstudio(),rol.getPeriodo());
            if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
            rol.setGrupos(resgrupos.getValor()); 
            
            rol.setGrupoSelec(rol.getGrupos().get(0));
            ResultadoEJB<List<Listaalumnosca>> rejb = ejb.getListaAlumnosPorGrupo(rol.getGrupoSelec());
            if(!rejb.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
            rol.setListaalumnoscas(rejb.getValor());  
            rol.setFechaInpresion(new Date());
            rol.setNewCompetencia(false);
        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "Reporte Asistencias";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    public String buscarPersonal(Integer clave) {
        try {
            System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.ListasAsistenciaDirector.buscarPersonal()" + clave);
            Personal p = new Personal();
            if (clave != null) {
                p = ejbPersonal.mostrarPersonalLogeado(clave);
                return p.getNombre();
            } else {
                return "Nombre del tutor";
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PaseListaDoc.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public void cambiarPlanestudio(ValueChangeEvent event) {
        rol.setGrupos(new ArrayList<>());
        rol.setListaalumnoscas(new ArrayList<>()); 
        rol.setGrupoSelec(new Grupo());
        rol.setPlanEstudio((PlanEstudio) event.getNewValue());
        ResultadoEJB<List<Grupo>> resgrupos = ejb.getListaGrupoPlanEstudio(rol.getPlanEstudio(),rol.getPeriodo());
        if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
        rol.setGrupos(resgrupos.getValor());  
    }
    public void consultarAlumnos(ValueChangeEvent event) {
        rol.setListaalumnoscas(new ArrayList<>());
        rol.setGrupoSelec((Grupo) event.getNewValue());
        ResultadoEJB<List<Listaalumnosca>> rejb = ejb.getListaAlumnosPorGrupo(rol.getGrupoSelec());
        if(!rejb.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
        rol.setListaalumnoscas(rejb.getValor());   
    }


    public void metodoBase() {}
}
