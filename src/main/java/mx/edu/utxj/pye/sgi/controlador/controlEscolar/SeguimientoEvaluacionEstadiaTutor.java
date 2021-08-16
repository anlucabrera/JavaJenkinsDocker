/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeuimientoEvaluacionEstadiaRolTutor;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEvaluacionEstadia;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
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
public class SeguimientoEvaluacionEstadiaTutor extends ViewScopedRol implements Desarrollable{
    
    @Getter @Setter private DtoSeuimientoEvaluacionEstadiaRolTutor rol = new DtoSeuimientoEvaluacionEstadiaRolTutor();
    @Getter @Setter Boolean cargado, tieneAcceso = false;
    
    @EJB EjbPropiedades ep;
    @EJB EjbSeguimientoEvaluacionEstadia ejbSTDA;
    @Inject LogonMB logonMB;
    @Inject GenerateExcel gExcel;
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_EVALUACION_ESTADIA_TUTOR);
            ResultadoEJB<Boolean> resApertura = ejbSTDA.darAccesoSeguimiento();
            if(resApertura.getValor().equals(Boolean.FALSE))return;
            ResultadoEJB<PersonalActivo> resAcceso = ejbSTDA.validarTutor(logonMB.getPersonal().getClave()); //Validar si cuenta con grupos tutorados
            if(!resAcceso.getCorrecto()){ return;}//cortar el flujo si no se pudo verificar el acceso
            ResultadoEJB<PersonalActivo> resValidacion = ejbSTDA.validarTutor(logonMB.getPersonal().getClave()); //Validar si cuenta con grupos tutorados
            if(!resValidacion.getCorrecto()){ return;}//cortar el flujo si no se pudo verificar el acceso
            PersonalActivo tutor = resValidacion.getValor();//            ejbPersonalBean.pack(logonMB.getPersonal());
            ResultadoEJB<List<DtoAlumnosEncuesta.DtoGruposTutor>> dtoGruposCE = ejbSTDA.obtenerGrupos(tutor.getPersonal().getClave());
            List<DtoAlumnosEncuesta.DtoGruposTutor> grupo = dtoGruposCE.getValor();
            if(grupo.isEmpty()) return;
            tieneAcceso = rol.tieneAcceso(tutor);
            
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            rol.setPersonalActivo(tutor);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.CONSULTA);
            
            rol.setDtoGrupos(dtoGruposCE.getValor());
            
            ResultadoEJB<Integer> periodo = ejbSTDA.obtenerPeriodoTest();
            rol.setPeriodoActivo(periodo.getValor());
            
            rol.setCarpeta("resultados_test");
            rol.setNombre2("evaluacion_completo_incompleto.xlsx");
            
        } catch (Exception e) {mostrarExcepcion(e);}
    }
    
    public void obtenerEstudiantesGrupoSeleccionado(){
        //System.out.println("Grado Seleccionado:"+ rol.getIdGrupo());
        ResultadoEJB<Grupo> resGrupo = ejbSTDA.obtenerGrupoSeleccionado(rol.getIdGrupo());
        rol.setGrupo(resGrupo.getValor());
        
        ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> lista = ejbSTDA.obtenerEstudiantes(rol.getGrupo());
        rol.setListaDtoEstudiante(lista.getValor());
        //System.out.println("Lista estudiantes encontrados");
        //rol.getListaDtoEstudiante().stream().forEach(System.out::println);
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAECompleto = rol.getListaDtoEstudiante()
                .stream()
                .map(dtoEstudiante -> ejbSTDA.packEncuestasCompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        //if(dtoAECompleto.isEmpty()) return;
        rol.setListaTestCompleto(dtoAECompleto);
        //System.out.println("Encuesta completa");
        //dtoAECompleto.stream().forEach(System.out::println);
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAEIncompleto = rol.getListaDtoEstudiante()
                .stream()
                .map(dtoEstudiante -> ejbSTDA.packEncuestasIncompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        rol.setListaTestIncompleto(dtoAEIncompleto);
        //System.out.println("Encuesta incompleta");
        //dtoAEIncompleto.stream().forEach(System.out::println);
        
    }
    
    public void generateResultadosTestCompleto()throws IOException{
        gExcel.obtenerLibro(rol.getNombre2());
        rol.setExcel("Evaluacion_Completo_Incompleto.xlsx");
        ResultadoEJB<Grupo> resGrupo = ejbSTDA.obtenerGrupoSeleccionado(rol.getIdGrupo());
        rol.setGrupo(resGrupo.getValor());
        rol.setSubcarpeta("Grupo_"+rol.getGrupo().getGrado()+rol.getGrupo().getLiteral().toString()+"_"+rol.getGrupo().getPeriodo());
        ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> lista = ejbSTDA.obtenerEstudiantes(rol.getGrupo());
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
                dto.getAreasUniversidad().getNombre(), dto.getGrado().toString(), dto.getGrupo()};
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
                dto.getAreasUniversidad().getNombre(), dto.getGrado().toString(), dto.getGrupo()};
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
        String valor = "seguimiento evaluacion de estadia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));

    }
    
}
