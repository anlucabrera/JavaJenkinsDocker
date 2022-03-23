/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
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
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReporteExcelTestVocacional;
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
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class SeguimientoTestVocacionalPsicopedagogia extends ViewScopedRol implements Desarrollable{
    private static final long serialVersionUID = 6071118934290521524L;
    @Getter             @Setter                                         private                                         DtoSeguimientoTestVocacional                rol;
    @EJB                EjbSeguimientoTestVocacional                    ejb;
    @EJB                EjbPropiedades                                  ep;
    @EJB                EjbReporteExcelTestVocacional                   ejbReporteExcelTestVocacional;
    
    @Inject             LogonMB                                         logon;
    @Inject             Caster                                          caster;
    @Getter             Boolean                                         tieneAcceso = false;
    
    @Inject             LogonMB logonMB;
    @Getter             private                                         Boolean cargado = false;


    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimiento test vocacional";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    @PostConstruct
    public void init() {
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_TEST_VOCACIONAL);
            ResultadoEJB<Filter<PersonalActivo>> resAccesoPersonalPsicopegagogia = ejb.validarPsicopedagogia(logon.getPersonal().getClave());
            if(!resAccesoPersonalPsicopegagogia.getCorrecto()){mostrarMensajeResultadoEJB(resAccesoPersonalPsicopegagogia);return;}
            Filter<PersonalActivo> filtro = resAccesoPersonalPsicopegagogia.getValor();
            PersonalActivo personal = filtro.getEntity();
            rol = new DtoSeguimientoTestVocacional(filtro);
            tieneAcceso = rol.tieneAcceso(personal);
            if(!tieneAcceso){return;}
            rol.setPersonalPsicopedagogia(personal);
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.obtenerPeriodosEscolaresGruposTests();
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
    
//    public void inicializarListaDtoTestVocacionalGrupal(){
//        rol.setListaTestVocacional(new ArrayList<>());
//        rol.setListaTestVocacional(Collections.EMPTY_LIST);
//    }
    
    public void inicializarListaDtoTestVocacionalResultadoEstudiante(){
        rol.setListaDtoTestVocacionalResultadoEstudiante(new ArrayList<>());
        rol.setListaDtoTestVocacionalResultadoEstudiante(Collections.EMPTY_LIST);
    }
    
    public void inicializarListaDtoTestVocacionalAvanceGrupal(){
        rol.setListaDtoTestVocacionalAvanceGrupal(new ArrayList<>());
        rol.setListaDtoTestVocacionalAvanceGrupal(Collections.EMPTY_LIST);
    }
    
    public void inicializarListaDtoTestVocacionalAvanceProgramaEducativo(){
        rol.setListaDtoTestVocacionalAvanceProgramaEducativo(new ArrayList<>());
         rol.setListaDtoTestVocacionalAvanceProgramaEducativo(Collections.EMPTY_LIST);
    }
    
    /*********************************************** Filtros *********************************************************/
    public void cambiarPeriodo(){
        if(rol.getPeriodoEscolarSeleccionado() == null){
            mostrarMensaje("No hay periodo escolar seleccionado");
            rol.setListaProgramasEducativos(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<AreasUniversidad>> resListaProgramasEducativos = ejb.obtenerProgramasEducativosTestsPorPeriodo(rol.getPeriodoEscolarSeleccionado());
        ResultadoEJB<List<DtoSeguimientoTestVocacional.DtoTestVocacionalAvanceProgramaEducativo>> resListaAvanceProgramaEducativo = ejb.listaResultadosTestVocacionalResumenProgramaEducativo(rol.getPeriodoEscolarSeleccionado());
        
        if(!resListaProgramasEducativos.getCorrecto()) mostrarMensajeResultadoEJB(resListaProgramasEducativos);
        else rol.setListaProgramasEducativos(resListaProgramasEducativos.getValor());
        
        inicializarListaDtoTestVocacionalAvanceProgramaEducativo();
        if(!resListaAvanceProgramaEducativo.getCorrecto()) mostrarMensajeResultadoEJB(resListaAvanceProgramaEducativo);
        else rol.setListaDtoTestVocacionalAvanceProgramaEducativo(resListaAvanceProgramaEducativo.getValor());
        
        cambiarProgramaEducativo();
    }
    
    public void cambiarProgramaEducativo(){
        if(rol.getProgramaEducativoSeleccionado() == null){
            mostrarMensaje("No hay programa educativo seleccionado");
            rol.setListaGrupos(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<Grupo>> resListaGrupos = ejb.obtenerGruposTestsPorPeriodoPrograma(rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativoSeleccionado());
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
    }
    
//    Descarga de reportes
    
//    Descarga de reporte de tipo Avance Cuatrimestral
    public void descargarReporteAvance(){
        try {
            ResultadoEJB<String> res = ejbReporteExcelTestVocacional.getReporteAvanceTestVocacional(rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativoSeleccionado(), rol.getGrupoSeleccionado(), rol.getPersonalPsicopedagogia().getPersonal().getClave(), rol.getListaDtoTestVocacionalAvanceProgramaEducativo(), rol.getListaDtoTestVocacionalAvanceGrupal(), rol.getListaDtoTestVocacionalResultadoEstudiante());
            if(res.getCorrecto()){
                File f = new File(res.getValor());
                Faces.sendFile(f, true);
            }
            mostrarMensajeResultadoEJB(res);
        } catch (Exception e) {
            mostrarMensaje(e.getMessage());
        }
    }
    
    public void descargarReportePorPeriodoEscolar(){
        try {
            ResultadoEJB<String> res = ejbReporteExcelTestVocacional.getReporteAvanceTestVocacionalPorPeriodoEscolar(rol.getPeriodoEscolarSeleccionado(), rol.getPersonalPsicopedagogia().getPersonal().getClave());
            if(res.getCorrecto()){
                File f = new File(res.getValor());
                Faces.sendFile(f, true);
            }
            mostrarMensajeResultadoEJB(res);
        } catch (Exception e) {
            mostrarMensaje(e.getMessage());
        }
    }
    
}
