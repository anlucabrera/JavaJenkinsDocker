/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguimientoEvaluacionEstadiaRolMultiple;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEvaluacionEstadia;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.util.GenerateExcel;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.primefaces.util.LangUtils;

/**
 *
 * @author Planeacion
 */
@Named
@ViewScoped
public class SeguimientoEvaluacionEstadiaMultiple extends ViewScopedRol implements Desarrollable{
    @Getter @Setter private DtoSeguimientoEvaluacionEstadiaRolMultiple rol;
    @Getter @Setter Boolean cargado, tieneAcceso = false, coordinadorTutor = false;
    
    @EJB EjbPropiedades ep;
    @EJB EjbSeguimientoEvaluacionEstadia ejbSTDA;
    @Inject LogonMB logonMB;
    @Inject GenerateExcel gExcel;
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_EVALUACION_ESTADIA_MULTIPLE);
            
            //ResultadoEJB<Filter<PersonalActivo>> resAccesoPsicoPedagodia = ejbSTDA.validarPsicopedagogia(logonMB.getPersonal().getClave());
            //ResultadoEJB<Filter<PersonalActivo>> resAccesoCoordinadorTutores = ejbSTDA.validarCoordinadorTutor(logonMB.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbSTDA.validarConsultaTest(logonMB.getPersonal().getClave());
            
            if(!resAcceso.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}
            
            Filter<PersonalActivo> filtro = resAcceso.getValor();
            PersonalActivo usuario = filtro.getEntity();
            rol = new DtoSeguimientoEvaluacionEstadiaRolMultiple(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);
            
            
            if(!tieneAcceso){return;}
            rol.setPersonalActivo(usuario);
            
            
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.CONSULTA);
            
            ResultadoEJB<Integer> periodo = ejbSTDA.obtenerPeriodoTest();
            rol.setPeriodoActivo(periodo.getValor());
            rol.setTest(new Evaluaciones());
            
            rol.setCarpeta("resultados_test");
            rol.setNombre2("evaluacion_estadia_reporte_general.xlsx");
        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }
    
    public void consultarEstudiantes(){
        ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> lista = ejbSTDA.obtenerEstudiantes(rol.getPeriodoActivo());
        rol.setListaDtoEstudiante(lista.getValor());
        //rol.getListaDtoEstudiante().forEach(System.out::println);
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAECompleto = rol.getListaDtoEstudiante()
                .stream()
                .map(dtoEstudiante -> ejbSTDA.packEncuestasCompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        //if(dtoAECompleto.isEmpty()) return;
        rol.setListaTestCompleto(dtoAECompleto);
        List<DtoAlumnosEncuesta.DtoAlumnosEvaluacion> dtoTestCompleto = dtoAECompleto
                .stream()
                .map(dto -> pack(dto))
                .collect(Collectors.toList());
        rol.setListaAlumnosTestCompleto(dtoTestCompleto);
        //System.out.println("Encuesta completa");
        //dtoAECompleto.stream().forEach(System.out::println);
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAEIncompleto = rol.getListaDtoEstudiante()
                .stream()
                .map(dtoEstudiante -> ejbSTDA.packEncuestasIncompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        rol.setListaTestIncompleto(dtoAEIncompleto);
        List<DtoAlumnosEncuesta.DtoAlumnosEvaluacion> dtoTestIncompleto = dtoAEIncompleto
                .stream()
                .map(dto -> pack(dto))
                .collect(Collectors.toList());
        rol.setListaAlumnosTestIncompleto(dtoTestIncompleto);
        if (rol.getListaAlumnosTestCompleto().isEmpty()) {
            mostrarMensaje("No hay lista de Estudiantes con el Test Completo");
        }
        if (rol.getListaAlumnosTestIncompleto().isEmpty()) {
            mostrarMensaje("No hay lista de Estudiantes con el Test Incompleto");
        }
        
    }
    
    public DtoAlumnosEncuesta.DtoAlumnosEvaluacion pack(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dtoAEGCE){
        DtoAlumnosEncuesta.DtoAlumnosEvaluacion dto;
        dto = new DtoAlumnosEncuesta.DtoAlumnosEvaluacion(dtoAEGCE.getAlumnos().getMatricula(),
                dtoAEGCE.getPersonas().getNombre()+" "+dtoAEGCE.getPersonas().getApellidoPaterno()+" "+dtoAEGCE.getPersonas().getApellidoMaterno(),
                dtoAEGCE.getGrado(), dtoAEGCE.getGrupo(), dtoAEGCE.getSiglas(), dtoAEGCE.getTutor().getNombre());
        return dto;
    }
    
    public void consultarAvance(){
        //if(rol.getPeriodoActivo()== null) {mostrarMensaje("Para consultar el avanve, debe seleccionar un periodo escolar");}
        List<DtoAlumnosEncuesta.DtoAvaceTestProgramaEducativo> listaAvance;
        ResultadoEJB<List<DtoAlumnosEncuesta.DtoProgramaEducativo>> dtoPEs = ejbSTDA.obtenerProgramasEducativo(rol.getPeriodoActivo());
        rol.setListaProgramasEducativos(dtoPEs.getValor());
        //rol.getListaProgramasEducativos().forEach(System.out::println);
        
        listaAvance = rol.getListaProgramasEducativos().stream()
                .map(dtoPE -> pack(dtoPE, rol.getPeriodoActivo()))
                .collect(Collectors.toList())
                .stream().filter(x -> x.getTotalMatricula() != 0)
                .collect(Collectors.toList());
        rol.setListaAvanceTest(listaAvance);    
    }
    
    public DtoAlumnosEncuesta.DtoAvaceTestProgramaEducativo pack(DtoAlumnosEncuesta.DtoProgramaEducativo dtoPE, Integer periodo){
        DtoAlumnosEncuesta.DtoAvaceTestProgramaEducativo dto;
        Short area = dtoPE.getArea();
        //System.out.println("Programa educativo"+ area);
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> lista = ejbSTDA.obtenerEstudiantesCE(area, periodo).getValor();
        //System.out.println("Total"+ lista.size());
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAECompleto = lista
                .stream()
                .map(dtoEstudiante -> ejbSTDA.packEncuestasCompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAEIncompleto = lista
                .stream()
                .map(dtoEstudiante -> ejbSTDA.packEncuestasIncompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        Integer totalMatricula = lista.size();
        Integer totalCompletos = dtoAECompleto.size();
        Integer totalIncopletos = dtoAEIncompleto.size();
        Double porcentaje = (totalCompletos.doubleValue() * 100) / totalMatricula;
        dto = new DtoAlumnosEncuesta.DtoAvaceTestProgramaEducativo(dtoPE.getNombre(), totalMatricula, totalCompletos, totalIncopletos, porcentaje);

        return dto;
    }
    
    public void generateResultadosTestCompleto()throws IOException{
        gExcel.obtenerLibro(rol.getNombre2());
        rol.setExcel("Evaluacion_Completo_Incompleto.xlsx");
        rol.setSubcarpeta("Reporte_General_Evaluacion");
        ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> lista = ejbSTDA.obtenerEstudiantes(rol.getPeriodoActivo());
        rol.setListaDtoEstudiante(lista.getValor());
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAECompleto = rol.getListaDtoEstudiante()
                .stream()
                .map(dtoEstudiante -> ejbSTDA.packEncuestasCompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(!dtoAECompleto.isEmpty()){
            rol.setLibro(gExcel.getLibro().getSheetAt(0).getSheetName());
            for (int i = 0; i <= dtoAECompleto.size() - 1; i++) {
            DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dto = dtoAECompleto.get(i);
            String[] datos = {
                    String.valueOf(dto.getAlumnos().getMatricula()), dto.getPersonas().getNombre()+" "+dto.getPersonas().getApellidoPaterno()+" "+dto.getPersonas().getApellidoMaterno(), 
                dto.getAreasUniversidad().getNombre(), dto.getGrado().toString(), dto.getGrupo(), dto.getTutor().getNombre()};
            gExcel.escribirDatosExcel(datos, i, 2, rol.getLibro());
            }
        }
        //excel.eliminarLibros(dto.excel);
        //dto.dtoEXANI = ejbExani.getResultadosExani();
        
        
        
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAEInCompleto = rol.getListaDtoEstudiante()
                .stream()
                .map(dtoEstudiante -> ejbSTDA.packEncuestasIncompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(!dtoAEInCompleto.isEmpty()) {
            rol.setLibro(gExcel.getLibro().getSheetAt(1).getSheetName());
            for (int i = 0; i <= dtoAEInCompleto.size() - 1; i++) {
            DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dto = dtoAEInCompleto.get(i);
            String[] datos = {
                    String.valueOf(dto.getAlumnos().getMatricula()), dto.getPersonas().getNombre()+" "+dto.getPersonas().getApellidoPaterno()+" "+dto.getPersonas().getApellidoMaterno(), 
                dto.getAreasUniversidad().getNombre(), dto.getGrado().toString(), dto.getGrupo(), dto.getTutor().getNombre()};
            gExcel.escribirDatosExcel(datos, i, 2, rol.getLibro());
            }
        }
        //excel.eliminarLibros(dto.excel);
        //dto.dtoEXANI = ejbExani.getResultadosExani();
        
        
        gExcel.escribirLibro(rol.getCarpeta(), rol.getSubcarpeta(), rol.getExcel());
        String ruta = gExcel.enviarLibro();
        Ajax.oncomplete("descargar('" + gExcel.enviarLibro() + "');");
    }
    
    public boolean globalFilterFunction(Object value, Object filter) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);

        DtoAlumnosEncuesta.DtoAlumnosEvaluacion customer = (DtoAlumnosEncuesta.DtoAlumnosEvaluacion) value;
        return customer.getMatricula() == filterInt
                || customer.getNombre().toLowerCase().contains(filterText)
                || customer.getGrupo().toLowerCase().contains(filterText)
                || customer.getSiglas().toLowerCase().contains(filterText);
    }
    
    private int getInteger(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException ex) {
            return 0;
        }
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimiento test de diagnositco de aprendizaje";
         Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));

    }
}
