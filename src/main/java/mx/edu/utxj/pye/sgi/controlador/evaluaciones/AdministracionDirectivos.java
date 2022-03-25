/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.evaluaciones;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.DtoAdministracionEvaluacionesRolPersonal;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacion360Promedios;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacionDesempenioPromedios;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionDirectivos;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionDesempenio;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.util.ExcelWritter;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author Planeacion
 */
@Named
@ViewScoped
public class AdministracionDirectivos extends ViewScopedRol implements Desarrollable{
    @Getter @Setter DtoAdministracionEvaluacionesRolPersonal rol;
    @Getter @Setter private Boolean tieneAcceso;
    @Getter @Setter private Boolean cargado = Boolean.FALSE;
    
    @EJB EjbPropiedades ep;
    @EJB EjbEvaluacionDesempenio ejbED;
    @EJB EjbAdministracionDirectivos ejbAdmin;
    @EJB EjbAdministracionEncuestas ejbAdminE;
    @EJB EJBSelectItems ejbSI;
    
    @Inject LogonMB logonMB;
    
    @PostConstruct
    public void init(){
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = Boolean.TRUE;
        setVistaControlador(ControlEscolarVistaControlador.ADMINISTRACION_EV_DIRECTIVOS);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbAdmin.validarDirectivo(logonMB.getPersonal().getClave());
            if(!resAcceso.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}
            
            Filter<PersonalActivo> filtro = resAcceso.getValor();
            PersonalActivo directivo = filtro.getEntity();
            rol = new DtoAdministracionEvaluacionesRolPersonal(filtro, directivo);
            tieneAcceso = rol.tieneAcceso(directivo);
            
            if(!tieneAcceso){return;}
            rol.setPersonalActivo(directivo);
                        
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.CONSULTA);
            rol.setRenderDTB360(Boolean.FALSE);
            rol.setRenderDTBDesempenio(Boolean.FALSE);
            rol.setRenderBtnDownloadCedulas360(Boolean.FALSE);
            rol.setRenderBtnDownloadCedulasDes(Boolean.FALSE);
            
    }
    
    public List<SelectItem> obtenerEvaluaciones(){
        List<SelectItem> lista = ejbSI.itemsEvaluacionesDirectivos();
        return lista;
    }
    
    public void obtenerPeriodosEvaluacion(){
        rol.setRenderBtnResultados(Boolean.FALSE);
        rol.setRenderBtnDownloadCedulasDes(Boolean.FALSE);
        rol.setRenderDTB360(Boolean.FALSE);
        rol.setRenderDTBDesempenio(Boolean.FALSE);
        rol.setDtoListaEv360(new ArrayList<>());
        rol.setDtoListaEvDes(new ArrayList<>());
        rol.setListaEvaluaciones(new ArrayList<>());
        rol.setPeSelect(0);
        switch(rol.getEvSelect()){
            case 1:
                rol.setTipoEvalaucion("360°");
                rol.setListaEvaluaciones(ejbAdminE.obtenerEvaluaciones360().getValor());
                rol.setRenderBtnResultados(Boolean.TRUE);
                break;
            case 2:
                rol.setTipoEvalaucion("Desempenio");
                rol.setListaEvaluaciones(ejbAdminE.obtenerEvaluacionesDesempenio().getValor());
                rol.setRenderBtnResultados(Boolean.TRUE);
                break;
        }
    }
    
    public void generarResultados(){
        if(rol.getPeSelect().equals(0)) return;
        rol.setDtoListaEv360(new ArrayList<>());
        rol.setDtoListaEvDes(new ArrayList<>());
        switch(rol.getEvSelect()){
            case 1:
                rol.setRenderDTB360(Boolean.TRUE);
                rol.setRenderDTBDesempenio(Boolean.FALSE);
                rol.setRenderBtnDownloadCedulas360(Boolean.TRUE);
                rol.setRenderBtnDownloadCedulasDes(Boolean.FALSE);
                resultadoEvaluacion360Personal();
                break;
            case 2:
                rol.setRenderDTB360(Boolean.FALSE);
                rol.setRenderDTBDesempenio(Boolean.TRUE);
                rol.setRenderBtnDownloadCedulas360(Boolean.FALSE);
                rol.setRenderBtnDownloadCedulasDes(Boolean.TRUE);
                ////Sysm.out.println("Evaluacion seleccionada: " + rol.getTipoEvalaucion());
                resultadoEvaluacionDesempenioPersonal();
                break;
        }
    }
    
    public void resultadoEvaluacion360Personal() {
        rol.setDtoListaEv360(new ArrayList<>());
        ResultadoEJB<Evaluaciones360> resEv = ejbAdminE.obtenerEv360(rol.getPeSelect());
        if(!resEv.getCorrecto()) return;
        Evaluaciones360 evaluacion = resEv.getValor();
        //Sysm.out.println("Evaluación 360° obtenida: " + evaluacion);
        List<ListaEvaluacion360Promedios> resDto = ejbAdmin.obtenerResultadosEv360(evaluacion, rol.getPersonalActivo().getAreaOperativa().getArea()).getValor();
        if(resDto.isEmpty()) return;
        rol.setDtoListaEv360(resDto);
    }
    
    public void resultadoEvaluacionDesempenioPersonal(){
        rol.setDtoListaEvDes(new ArrayList<>());
        ResultadoEJB<DesempenioEvaluaciones> resEv = ejbAdminE.obtenerEvDes(rol.getPeSelect());
        if(!resEv.getCorrecto()) return;
        DesempenioEvaluaciones evaluacion = resEv.getValor();
        //Sysm.out.println("Evaluación Desempeño obtenida: " + evaluacion);
        List<ListaEvaluacionDesempenioPromedios> resDtoLista = ejbAdmin.obtenerResultadosEvDesempenio(evaluacion, rol.getPersonalActivo().getPersonal().getClave()).getValor();
        if(resDtoLista.isEmpty()) return;
        rol.setDtoListaEvDes(resDtoLista);
    }
    
    public void descargarCedulasDesempenio() {
        ResultadoEJB<DesempenioEvaluaciones> resEv = ejbAdminE.obtenerEvDes(rol.getPeSelect());
        if(!resEv.getCorrecto()) return;
        DesempenioEvaluaciones evaluacion = resEv.getValor();
        ////Sysm.out.println("Evaluacion" + evaluacion);
        PeriodosEscolares pe = ejbAdminE.obtenerPeriodo(evaluacion.getPeriodo()).getValor();
        ////Sysm.out.println("Periodo" + pe);
        ResultadoEJB<List<ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio>> resLista = ejbAdmin.obtenerReultadosCedulas(evaluacion.getEvaluacion(), rol.getPersonalActivo().getPersonal().getClave());
        if(!resLista.getCorrecto()) return;
        List<ListaEvaluacionDesempenioPromedios.DtoListaResultadosEvaluacionDesempenio> listaResultados = resLista.getValor();
        
        ExcelWritter ew = new ExcelWritter(evaluacion, listaResultados, pe);
        ew.obtenerLibro();
        ew.editarLibro1();
        ew.escribirLibro();
//        String ruta = ew.enviarLibro();
        Ajax.oncomplete("descargar('" + ew.enviarLibro() + "');");

//        ////Sysm.out.println("mx.edu.utxj.pye.sgi.controlador.evaluaciones.EvaluacionDesempenioAdmin.descargarCedulas() ruta: " + ruta);
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administracion directivos";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
////        map.entrySet().forEach(//Sysm.out::println);
        return mostrar(request, map.containsValue(valor));
    }
    
    
}
