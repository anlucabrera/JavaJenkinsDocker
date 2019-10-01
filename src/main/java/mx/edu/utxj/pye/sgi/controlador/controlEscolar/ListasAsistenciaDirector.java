/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ConsultaListasAsistenciaDirector;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaPlanEstudio;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaRegistro;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaUnidades;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPlanEstudio;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPlanEstudioMateriaCompetencias;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AreaConocimiento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Competencia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

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
            
    @PostConstruct
    public void init(){
        try {
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

            ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> resProgramaPlan = ejb.getProgramasEducativos(director);
            if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
            rol.setAreaPlanEstudioMap(resProgramaPlan.getValor());
            
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());            
            
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
        String valor = "Admin Plan Estudio";
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
