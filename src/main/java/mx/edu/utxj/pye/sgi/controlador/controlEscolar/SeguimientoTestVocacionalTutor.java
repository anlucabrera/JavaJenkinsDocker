/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.DtoSeguimientoTestVocacional;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoTestVocacional;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class SeguimientoTestVocacionalTutor extends ViewScopedRol implements Desarrollable{
    private static final long serialVersionUID = -7838773826952642847L;
    @Getter             @Setter                                         private                                         DtoSeguimientoTestVocacional                rol;
    @EJB                EjbSeguimientoTestVocacional                    ejb;
    @EJB                EjbPropiedades                                  ep;

    @Inject             LogonMB                                         logon;
    @Inject             Caster                                          caster;
    @Getter             Boolean                                         tieneAcceso = false;
    
    @Inject             LogonMB logonMB;
    @Getter             private                                         Boolean cargado = false;
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimiento test vocacional tutor";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init() {
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_TEST_VOCACIONAL_TUTOR);
            ResultadoEJB<Filter<PersonalActivo>> resAccesoPersonalTutor = ejb.validarTutor(logon.getPersonal().getClave());
            if(!resAccesoPersonalTutor.getCorrecto()){mostrarMensajeResultadoEJB(resAccesoPersonalTutor);return;}
            Filter<PersonalActivo> filtro = resAccesoPersonalTutor.getValor();
            PersonalActivo personal = filtro.getEntity();
            rol = new DtoSeguimientoTestVocacional(filtro);
            tieneAcceso = rol.tieneAcceso(personal);
            if(!tieneAcceso){return;}
            rol.setPersonalPsicopedagogia(personal);
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.obtenerPeriodosEscolaresGruposTestsTutores(rol.getPersonalPsicopedagogia().getPersonal().getClave());
            if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.SUPERIOR);
            rol.setListaPeriodosEscolares(resPeriodos.getValor());
            cambiarPeriodo();
        }catch (Exception e){mostrarExcepcion(e); }
    }
    
    /*********************************************** Inicializadores *********************************************************/
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public void inicializarListaDtoTestVocacionalResultadoEstudiante(){
        rol.setListaDtoTestVocacionalResultadoEstudiante(new ArrayList<>());
        rol.setListaDtoTestVocacionalResultadoEstudiante(Collections.EMPTY_LIST);
    }
    
    public void inicializarListaDtoTestVocacionalAvanceGrupal(){
        rol.setListaDtoTestVocacionalAvanceGrupal(new ArrayList<>());
        rol.setListaDtoTestVocacionalAvanceGrupal(Collections.EMPTY_LIST);
    }
    
    /*********************************************** Filtros *********************************************************/
    public void cambiarPeriodo(){
        if(rol.getPeriodoEscolarSeleccionado() == null){
            mostrarMensaje("No hay periodo escolar seleccionado");
            rol.setListaProgramasEducativos(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<AreasUniversidad>> resListaProgramasEducativos = ejb.obtenerProgramasEducativosTestsPorPeriodoTutores(rol.getPeriodoEscolarSeleccionado(), rol.getPersonalPsicopedagogia().getPersonal().getClave());
        if(!resListaProgramasEducativos.getCorrecto()) mostrarMensajeResultadoEJB(resListaProgramasEducativos);
        else rol.setListaProgramasEducativos(resListaProgramasEducativos.getValor());
        
        cambiarProgramaEducativo();
    }
    
    public void cambiarProgramaEducativo(){
        if(rol.getProgramaEducativoSeleccionado() == null){
            mostrarMensaje("No hay programa educativo seleccionado");
            rol.setListaGrupos(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<Grupo>> resListaGrupos = ejb.obtenerGruposTestsPorPeriodoProgramaTutor(rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativoSeleccionado(), rol.getPersonalPsicopedagogia().getPersonal().getClave());
        if(!resListaGrupos.getCorrecto()) mostrarMensajeResultadoEJB(resListaGrupos);
        else rol.setListaGrupos(resListaGrupos.getValor());
        
        inicializarListaDtoTestVocacionalAvanceGrupal();
        ResultadoEJB<List<DtoSeguimientoTestVocacional.DtoTestVocacionalAvanceGrupal>> resListaResumenGrupal = ejb.listaResultadosTestVocacionalResumenGrupal(rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativoSeleccionado());
        if(!resListaResumenGrupal.getCorrecto()) mostrarMensajeResultadoEJB(resListaResumenGrupal);
        else rol.setListaDtoTestVocacionalAvanceGrupal(resListaResumenGrupal.getValor());
        
        cambiarGrupo();
    }
    
    public void cambiarGrupo(){
        if(rol.getGrupoSeleccionado() == null){
            mostrarMensaje("No hay ningún grupo seleccionado");
            rol.setListaGrupos(Collections.EMPTY_LIST);
            return;
        }
        actualizarListaDtoTestVocacionalResultadoEstudiante();
    }
    
    public void actualizarListaDtoTestVocacionalResultadoEstudiante(){
        ResultadoEJB<List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante>> resListaDtoResultadosEstudiante = ejb.obtenerListaTestVocacionalPorPeriodoProgramaGrupo(rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativoSeleccionado(), rol.getGrupoSeleccionado());
        if(resListaDtoResultadosEstudiante.getCorrecto()){
            inicializarListaDtoTestVocacionalResultadoEstudiante();
            mostrarMensajeResultadoEJB(resListaDtoResultadosEstudiante);
            rol.setListaDtoTestVocacionalResultadoEstudiante(resListaDtoResultadosEstudiante.getValor());
        }else{
            mostrarMensajeResultadoEJB(resListaDtoResultadosEstudiante);
            inicializarListaDtoTestVocacionalResultadoEstudiante();
        }
        
        if(!rol.getListaDtoTestVocacionalAvanceGrupal().isEmpty() || rol.getListaDtoTestVocacionalAvanceGrupal() != null){
            rol.getListaDtoTestVocacionalAvanceGrupal().removeIf(grupo -> !grupo.getGrupo().getIdGrupo().equals(rol.getGrupoSeleccionado().getIdGrupo()));
        }
    }
}
