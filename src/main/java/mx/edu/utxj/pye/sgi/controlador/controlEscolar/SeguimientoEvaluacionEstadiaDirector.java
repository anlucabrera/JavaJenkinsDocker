/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.IOException;
import java.util.ArrayList;
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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.util.GenerateExcel;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author Planeacion
 */
@Named
@ViewScoped
public class SeguimientoEvaluacionEstadiaDirector extends ViewScopedRol implements Desarrollable{
    @Getter @Setter private DtoSeguimientoEvaluacionEstadiaRolMultiple rol;
    @Getter @Setter Boolean cargado = false;
    @Getter @Setter Boolean tieneAcceso = false;
    
    @EJB EjbPropiedades ep;
    @EJB EjbSeguimientoEvaluacionEstadia ejbSEE;
    @Inject LogonMB logonMB;
    @Inject GenerateExcel gExcel;
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_EVALUACION_ESTADIA_DIRECTOR);
            
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbSEE.validarDirector(logonMB.getPersonal().getClave());//validar si es director
//            System.out.println("resAcceso = " + resAcceso);

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbSEE.validarEncargadoDireccion(logonMB.getPersonal().getClave());
//            System.out.println("resValidacion = " + resValidacion);
            if(!resValidacion.getCorrecto() && !resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new DtoSeguimientoEvaluacionEstadiaRolMultiple(filtro, director);
            tieneAcceso = rol.tieneAcceso(director);
//            System.out.println("tieneAcceso1 = " + tieneAcceso);
            if(!tieneAcceso){
                rol.setFiltro(resValidacion.getValor());
                tieneAcceso = rol.tieneAcceso(director);
            }
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setPersonalActivo(director);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            rol.setNivelRol(NivelRol.CONSULTA);
            
            ResultadoEJB<Integer> periodo = ejbSEE.obtenerPeriodoTest();
            rol.setPeriodoActivo(periodo.getValor());
            rol.setTest(new Evaluaciones());
            
            rol.setCarpeta("resultados_test");
            rol.setNombre2("evaluacion_estadia_reporte_general.xlsx");
            
            ResultadoEJB<List<AreasUniversidad>> resList = ejbSEE.obtenerAreaDirector(rol.getPersonalActivo().getPersonal().getClave());
            rol.setListaAreas(resList.getValor());
            ResultadoEJB<VariablesProntuario> vp = ejbSEE.obtenerEncargadoMecaa();
            if(rol.getPersonalActivo().getPersonal().getClave().equals(Integer.parseInt(vp.getValor().getValor()))){
                ResultadoEJB<AreasUniversidad> resArea = ejbSEE.obtenerAreaEspecifica();
                rol.getListaAreas().add(resArea.getValor());
            }
            rol.setAreaAcademica(rol.getListaAreas().get(0).getArea());
            System.out.println("Area academica:"+ rol.getListaAreas());
            obtenerProgramasEducativos();
            obtenerEstudiantes();
        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }
    
    public void obtenerProgramasEducativos(){
        rol.setListaAlumnosTestCompleto(new ArrayList<>());
        rol.setListaAlumnosTestCompletoF(new ArrayList<>());
        rol.setListaAlumnosTestIncompleto(new ArrayList<>());
        rol.setListaAlumnosTestIncompletoF(new ArrayList<>());
        rol.setListaProgramas(new ArrayList<>());
        List<AreasUniversidad> listaPrograma = ejbSEE.obtenerProgramasEducativos(rol.getAreaAcademica()).getValor();
        rol.setListaProgramas(listaPrograma);
        rol.setCveCarrera(rol.getListaProgramas().get(0).getArea());
        obtenerEstudiantes();
    }
    
    public void obtenerEstudiantes(){
//        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> lista = rol.getListaAreas().stream().map(a -> pack(a))
//                    .map(au -> ejbSEE.packEstudiante(au, rol.getPeriodoActivo()))
//                .filter(ResultadoEJB::getCorrecto)
//                .map(ResultadoEJB::getValor)
//                    .collect(Collectors.toList());
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> lista = ejbSEE.obtenerEstudiantes(rol.getCveCarrera(), rol.getPeriodoActivo()).getValor();
        rol.setListaDtoEstudiante(lista);
        //rol.getListaDtoEstudiante().forEach(System.out::println);
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAECompleto = rol.getListaDtoEstudiante()
                .stream()
                .map(dtoEstudiante -> ejbSEE.packEncuestasCompletas(dtoEstudiante))
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
                .map(dtoEstudiante -> ejbSEE.packEncuestasIncompletas(dtoEstudiante))
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
    
    public AreasUniversidad pack(AreasUniversidad division){
        AreasUniversidad programa = ejbSEE.obtenerPrograma(division.getArea()).getValor();
        //System.out.println("Programa educativo:"+ programa);
        return programa;
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
        ResultadoEJB<List<DtoAlumnosEncuesta.DtoProgramaEducativo>> dtoPEs = ejbSEE.obtenerProgramasEducativo(rol.getPeriodoActivo());
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
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> lista = ejbSEE.obtenerEstudiantesCE(area, periodo).getValor();
        //System.out.println("Total"+ lista.size());
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAECompleto = lista
                .stream()
                .map(dtoEstudiante -> ejbSEE.packEncuestasCompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAEIncompleto = lista
                .stream()
                .map(dtoEstudiante -> ejbSEE.packEncuestasIncompletas(dtoEstudiante))
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
        rol.setSubcarpeta("Reporte_General_Evaluacion"+"_"+rol.getPersonalActivo().getPersonal().getClave());
        ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> lista = ejbSEE.obtenerEstudiantes(rol.getCveCarrera(), rol.getPeriodoActivo());
        rol.setListaDtoEstudiante(lista.getValor());
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAECompleto = rol.getListaDtoEstudiante()
                .stream()
                .map(dtoEstudiante -> ejbSEE.packEncuestasCompletas(dtoEstudiante))
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
                .map(dtoEstudiante -> ejbSEE.packEncuestasIncompletas(dtoEstudiante))
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
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimiento test de diagnositco de aprendizaje";
         Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));

    }
    
}
