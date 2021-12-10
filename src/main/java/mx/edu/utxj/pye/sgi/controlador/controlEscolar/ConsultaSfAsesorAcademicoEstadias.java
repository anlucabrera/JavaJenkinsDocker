/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguroFacultativo;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ValidacionSeguroFacultativoRolEstudiantiles;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaSegurosFacultativos;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPeriodoEventoRegistro;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReporteExcelSeguroFacultativo;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionSeguroFacultativo;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.RolesConsultaSeguroFacultativo;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

/**
 * - Acceso para:
 * 
 *  Rol especificoAsesorAcademico
 *      6.- Asesor Académico estadía - Muestra solo sus estudiantes asesorados 
 *          - Consulta mediante periodos escolares obtenidos desde ejbConsultaSeguroFacultativos
 * 
 * TODO: Mostrar solo estudiantes dentro de la lista de sus asesorados
 * @author UTXJ
 */
@Named
@ViewScoped
public class ConsultaSfAsesorAcademicoEstadias extends ViewScopedRol implements Desarrollable{
    private static final long serialVersionUID = -4410366720388276748L;
    @Getter             @Setter                                         private                                         ValidacionSeguroFacultativoRolEstudiantiles                rol;
    @EJB                EjbPropiedades                                  ep;
    @EJB                EjbPeriodoEventoRegistro                        ejbPeriodoEventoRegistro;
    @EJB                EjbConsultaSegurosFacultativos                  ejb;
    @EJB                EjbValidacionSeguroFacultativo                  ejbValidacionSeguroFacultativo;
    @EJB                EjbReporteExcelSeguroFacultativo                ejbReporteExcel;
    @EJB                EjbValidacionRol                                ejbValidacionRol;
    
    @Inject             LogonMB                                         logon;
    @Inject             Caster                                          caster;
    @Getter             Boolean                                         tieneAcceso = false;
    
    @Inject             LogonMB logonMB;
    @Getter             private                                         Boolean cargado = false;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "consulta seguro facultativo asesor academico estadia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.CONSULTA_S_F_ASESOR_ACADEMICO_ESTADIA);
            
            ResultadoEJB<Filter<PersonalActivo>> resAccesoAsesorAcademicoEstadia = ejb.validarAsesorAcademicoEstadia(logonMB.getPersonal().getClave());
            
            if(!resAccesoAsesorAcademicoEstadia.getCorrecto()){mostrarMensajeResultadoEJB(resAccesoAsesorAcademicoEstadia);return;}
            Filter<PersonalActivo> filtro = resAccesoAsesorAcademicoEstadia.getValor();
            PersonalActivo personal = filtro.getEntity();
            rol = new ValidacionSeguroFacultativoRolEstudiantiles(filtro);
            tieneAcceso = rol.tieneAcceso(personal);
            rol.setRolConsultaSeguroFacultativo(RolesConsultaSeguroFacultativo.ESPECIFICO_ASESOR_ACADEMICO.getLabel());
            if(!tieneAcceso){return;}
            rol.setPersonalEstudiantiles(personal);
            ResultadoEJB<List<Integer>> resultadolistaEstudiantes = ejb.obtenerEstudiantesPorAsesorEstadia(rol.getPersonalEstudiantiles());
            if(!resultadolistaEstudiantes.getCorrecto()){mostrarMensajeResultadoEJB(resultadolistaEstudiantes); tieneAcceso = false;}
            rol.setListaIdEstudiantes(resultadolistaEstudiantes);
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.obtenerPeriodosEscolaresConSFPorAsesorAcademicoEstadia(rol.getPersonalEstudiantiles(), rol.getListaIdEstudiantes());
            if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
            rol.setListaPeriodosEscolares(resPeriodos.getValor());
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.CONSULTA);
            rol.setPeriodoEscolarActivo(ejbPeriodoEventoRegistro.getPeriodoEscolarActivo().getValor());
            cambiarPeriodo();
        }catch (Exception e){mostrarExcepcion(e); }
    }
    
     /*********************************************** Inicializadores *********************************************************/
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
     public void inicializarListaDtoSegurosFacultativos(){
        rol.setListaDtoSeguroFacultativo(new ArrayList<>());
        rol.setListaDtoSeguroFacultativo(Collections.EMPTY_LIST);
    }
     
     public void inicializarCasoFiltro(){
        rol.setEstudianteSeguimientoSeleccionado(null);
        rol.setEstudianteSeleccionado(null);
    }
    
    public void inicializarCasoEstudiante(){
        rol.setPeriodoEscolarSeleccionado(null);
    }
     
     /*********************************************** Filtros *********************************************************/
     
     public void cambiarPeriodo(){
        if(rol.getPeriodoEscolarSeleccionado() == null){
            mostrarMensaje("No hay periodo escoalar seleccionado");
            rol.setListaProgramasEducativos(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<AreasUniversidad>> resProgramasEducativos = ejb.obtenerProgramasEducativosConSFPorAsesorAcademicoEstadia(rol.getPersonalEstudiantiles(), rol.getListaIdEstudiantes(), rol.getPeriodoEscolarSeleccionado());
        if(!resProgramasEducativos.getCorrecto()) mostrarMensajeResultadoEJB(resProgramasEducativos);
        else rol.setListaProgramasEducativos(resProgramasEducativos.getValor());     
        cambiarProgramaEducativo();
        inicializarCasoFiltro();
    }
     
     public void cambiarProgramaEducativo(){
        if(rol.getProgramaEducativo() == null){
            mostrarMensaje("No hay ningún programa educativo seleccionado");
            rol.setListaGrupos(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<Grupo>> resGrupos = ejb.obtenerGruposConSFPorAsesorAcademicoEstadia(rol.getPersonalEstudiantiles(), rol.getListaIdEstudiantes(), rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativo());
        if(!resGrupos.getCorrecto()) mostrarMensajeResultadoEJB(resGrupos);
        else rol.setListaGrupos(resGrupos.getValor());
        cambiarGrupo();
    }
     
     public void cambiarGrupo(){
        if(rol.getGrupo() == null){
            mostrarMensaje("No hay ningún grupo seleccionado");
            rol.setListaDtoSeguroFacultativo(Collections.EMPTY_LIST);
            return;
        }
        actualizarListaDtoSegurosFacultativos();
    }
     
     public void actualizarListaDtoSegurosFacultativos(){         
//            ESPECIFICO_ASESOR_ACADEMICO 
        ResultadoEJB<List<DtoSeguroFacultativo>> resListaDtoSF = ejb.obtenerSFPorPeriodoProgramaEducativoGrupo(rol.getListaIdEstudiantes(), rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativo(), rol.getGrupo());
        if(resListaDtoSF.getCorrecto()){
            inicializarListaDtoSegurosFacultativos();
            mostrarMensajeResultadoEJB(resListaDtoSF);
            rol.setListaDtoSeguroFacultativo(resListaDtoSF.getValor());
        }else{
            mostrarMensajeResultadoEJB(resListaDtoSF);
            inicializarListaDtoSegurosFacultativos();
        }
    }
     
     public void actualzarListaDtoSegurosFacultativosEstudiante(){
        ResultadoEJB<List<DtoSeguroFacultativo>> resListaDtoSf = ejbValidacionSeguroFacultativo.obtenerSeguroFacultativoPorEstudiante(rol.getEstudianteSeleccionado().getMatricula());
        if(resListaDtoSf.getCorrecto()){
            inicializarListaDtoSegurosFacultativos();
            mostrarMensajeResultadoEJB(resListaDtoSf);
            rol.setListaDtoSeguroFacultativo(resListaDtoSf.getValor());
        }else{
            mostrarMensajeResultadoEJB(resListaDtoSf);
            inicializarListaDtoSegurosFacultativos();
        }
    }
    
    public List<DtoEstudianteComplete> completeEstudiantes(String pista){
        ResultadoEJB<List<DtoEstudianteComplete>> reslistaEstudiantes = ejbValidacionSeguroFacultativo.buscarEstudiantePorAsesoradosEstadia(pista, rol.getListaIdEstudiantes().getValor());
        if(reslistaEstudiantes.getCorrecto()){
            mostrarMensajeResultadoEJB(reslistaEstudiantes);
            return reslistaEstudiantes.getValor();
        }else{
            mostrarMensajeResultadoEJB(reslistaEstudiantes);
            return Collections.EMPTY_LIST;
        }
    }
    
    public void cambiarEstudianteSeleccionado(ValueChangeEvent event){
        if(event.getNewValue() instanceof DtoEstudianteComplete){
            DtoEstudianteComplete estudiante = (DtoEstudianteComplete) event.getNewValue();
            ResultadoEJB<List<DtoSeguroFacultativo>> resListaSF = ejbValidacionSeguroFacultativo.obtenerSeguroFacultativoPorEstudiante(estudiante.getEstudiantes().getMatricula());
            if(resListaSF.getCorrecto()){
                inicializarCasoEstudiante();
                rol.setEstudianteSeleccionado(estudiante.getEstudiantes());
                actualzarListaDtoSegurosFacultativosEstudiante();
            }else{
                mostrarMensajeResultadoEJB(resListaSF);
            }
        }else mostrarMensaje("El valor seleccionado no es valido");
    }
     
     /*********************************************** Método y funciones *********************************************************/
     
     public void descargarArchivoEstudiante(String rutaArchivo) throws IOException{
        if(rutaArchivo == null) {mostrarMensaje("No se cargó ningún archivo"); return;}
        if("".equals(rutaArchivo)){mostrarMensaje("No se cargó ningún archivo"); return;}
        File f = new File(rutaArchivo);
        Faces.sendFile(f, false);
    }
     
     public void descargarReportePorPeriodo() {
        try {
            ResultadoEJB<String> res = ejbReporteExcel.getReporteSfCuatrimestralAsesorAcademicoEstadia(rol.getListaIdEstudiantes(),rol.getPeriodoEscolarSeleccionado(),rol.getPersonalEstudiantiles());
            if (res.getCorrecto()) {
                File f = new File(res.getValor());
                Faces.sendFile(f, true);
            }
            mostrarMensajeResultadoEJB(res);
        } catch (IOException ex) {
            mostrarMensaje(ex.getMessage());
        }
    }
}
