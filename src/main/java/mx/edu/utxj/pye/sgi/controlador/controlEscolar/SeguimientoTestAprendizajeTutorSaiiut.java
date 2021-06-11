/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeuimientoTestAprendizajeRolMultiple;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoTestDiagnosticoAprendizaje;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestDiagnosticoAprendizaje;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;
import mx.edu.utxj.pye.sgi.util.GenerateExcel;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author Planeacion
 */
@Named
@ViewScoped
public class SeguimientoTestAprendizajeTutorSaiiut extends ViewScopedRol implements Desarrollable{
    
    @Getter @Setter private DtoSeuimientoTestAprendizajeRolMultiple rol = new DtoSeuimientoTestAprendizajeRolMultiple();
    @Getter @Setter private Boolean cargado, tieneAcceso = false;
    @EJB private EjbPropiedades ep;
    @EJB private EjbSeguimientoTestDiagnosticoAprendizaje ejbTest;
    @Inject GenerateExcel gExcel;
    @Inject private LogonMB logonMB;
    
    @PostConstruct
    public void init(){
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_TEST_DIAGNOSTICO_APRENDIZAJE_TUTOR);
        ResultadoEJB<Boolean> resApertura = ejbTest.darAccesoSeguimiento();
        if(resApertura.getValor().equals(Boolean.FALSE))return;
        
        ResultadoEJB<Personas> resPersona = ejbTest.validarPersona(logonMB.getListaUsuarioClaveNomina().getCvePersona());
        if(!resPersona.getCorrecto()) return;
        rol.setTutor(resPersona.getValor());
        
        ResultadoEJB<Boolean> resEsTutor = ejbTest.esTutor(rol.getTutor().getPersonasPK().getCvePersona());
        if(!resEsTutor.getCorrecto()) return;
        rol.setEsTutor(resEsTutor.getValor());
        
        tieneAcceso = rol.tieneAcceso(rol.getEsTutor());
        if(!tieneAcceso) {mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
        
        if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
        if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.CONSULTA);
            ResultadoEJB<List<DtoAlumnosEncuesta.DtoGruposTutor>> dtoGruposCE = ejbTest.obtenerGruposSaiiut(rol.getTutor().getPersonasPK().getCvePersona());
            rol.setDtoGruposS(dtoGruposCE.getValor());
            ResultadoEJB<Integer> periodo = ejbTest.obtenerPeriodoTest();
            rol.setPeriodoActivo(periodo.getValor());
            
            rol.setNombre("plantilla_test.xlsx");
            rol.setCarpeta("resultados_test");
            rol.setNombreWord("formato_informe.docx");
            rol.setNombre2("test_completo_incompleto.xlsx");
            
            rol.getInstrucciones().add("Seguimiento del Test de Diagnóstico de Estilos de Aprendizaje");
            rol.getInstrucciones().add("CONSULTA DE INFORMACIÓN:");
            rol.getInstrucciones().add("-Seleccionar grupo a visualizar.");
            rol.getInstrucciones().add("NOTA: En caso de contar con grupos tutorados pertenecientes a SAIIUT, se le mostrará un segundo apartado de selección, "
                    + "el cual habilitará 2 tablas de estudiante con el Test completo e incompleto.");
            rol.getInstrucciones().add("");
            rol.getInstrucciones().add("DESCARGA DE INFORMACIÓN:");
            rol.getInstrucciones().add("Para generar los resultados del Test en formato Excel así como tambien el Informe Grupal "
                    + "del Test debe seleccionar el grupo a su cargo");
            rol.getInstrucciones().add("Para descargar la base de información de los estudiantes con Test Completo e Incompleto, "
                    + "en la parte superior derecha de la Tabla se encuentra un icono de descarga, en el cuál deben dar clic para iniciar dicha descarga");
            
    }
    
    public void obtenerEstudiantesGrupoSeleccionado(){
        //System.out.println("Grado Seleccionado:"+ rol.getIdGrupo());
        ResultadoEJB<Grupos> resGrupo = ejbTest.obtenerGrupoSeleccionadoS(rol.getIdGrupo());
        rol.setGrupos(resGrupo.getValor());
        
        ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral>> lista = ejbTest.obtenerEstudiantes(rol.getGrupos());
        rol.setListaDtoEstudianteS(lista.getValor());
        //System.out.println("Lista estudiantes encontrados");
        //rol.getListaDtoEstudiante().stream().forEach(System.out::println);
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAECompleto = rol.getListaDtoEstudianteS()
                .stream()
                .map(dtoEstudiante -> ejbTest.packEncuestasCompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        //if(dtoAECompleto.isEmpty()) return;
        rol.setListaTestCompletoS(dtoAECompleto);
        //System.out.println("Encuesta completa");
        //dtoAECompleto.stream().forEach(System.out::println);
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAEIncompleto = rol.getListaDtoEstudianteS()
                .stream()
                .map(dtoEstudiante -> ejbTest.packEncuestasIncompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        rol.setListaTestIncompletoS(dtoAEIncompleto);
        //System.out.println("Encuesta incompleta");
        //dtoAEIncompleto.stream().forEach(System.out::println);
        
    }
    
    public void generarExcelTest() throws IOException{
        gExcel.obtenerLibro(rol.getNombre());
        rol.setExcel("Resultados_Test.xlsx");
        rol.setLibro(gExcel.getLibro().getSheetAt(0).getSheetName());
        //System.out.println("Grado Seleccionado:"+ rol.getIdGrupo());
        ResultadoEJB<Grupos> resGrupo = ejbTest.obtenerGrupoSeleccionadoS(rol.getIdGrupo());
        rol.setGrupos(resGrupo.getValor());
        rol.setSubcarpeta("Grupo_"+rol.getGrupos().getGrado()+rol.getGrupos().getIdGrupo()+"_"+rol.getGrupos().getGruposPK().getCvePeriodo());
        ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral>> lista = ejbTest.obtenerEstudiantes(rol.getGrupos());
        rol.setListaDtoEstudianteS(lista.getValor());
        //System.out.println("Lista estudiantes encontrados");
        //rol.getListaDtoEstudiante().stream().forEach(System.out::println);
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAECompleto = rol.getListaDtoEstudianteS()
                .stream()
                .map(dtoEstudiante -> ejbTest.packEncuestasCompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        gExcel.escribirDatosExcelHoja0S(dtoAECompleto, rol.getLibro());
        
        for (int i = 0; i < dtoAECompleto.size(); i++) {
            DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dto = dtoAECompleto.get(i);
            TestDiagnosticoAprendizaje test = ejbTest.obtenerTest(dto).getValor();
            //System.out.println("Index:"+i+" Estudiante:"+dto+" Test:"+test);
            rol.setLibro(gExcel.getLibro().getSheetAt(i+1).getSheetName());
            //System.out.println("Nombre:"+ rol.getLibro());
            //gExcel.eliminarLibros(rol.getLibro());
            if(test.equals(new TestDiagnosticoAprendizaje())) return;
            gExcel.escribirDatosExcel(dto, test, rol.getLibro());
        }
        gExcel.escribirLibro(rol.getCarpeta(), rol.getSubcarpeta(), rol.getExcel());
        
        String ruta = gExcel.enviarLibro();
        Ajax.oncomplete("descargar('" + gExcel.enviarLibro() + "');");
        
    }
    
    public void generarInformeWord() throws IOException{
        gExcel.obtenerWord(rol.getNombreWord());
        
        rol.setWord("Informe_Test.docx");
        rol.setNameWord(gExcel.getWord().toString());
        ResultadoEJB<Grupos> resGrupo = ejbTest.obtenerGrupoSeleccionadoS(rol.getIdGrupo());
        rol.setGrupos(resGrupo.getValor());
        rol.setSubcarpeta("Grupo_"+rol.getGrupos().getGrado()+rol.getGrupos().getIdGrupo()+"_"+rol.getGrupos().getGruposPK().getCvePeriodo());
        ////////////Se obtiene la lista de estudiantes activos en el grupo seleccionado
        ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral>> lista = ejbTest.obtenerEstudiantes(rol.getGrupos());
        rol.setListaDtoEstudianteS(lista.getValor());
        ////////////Se busca la lista de alumnos que han culminado el Test
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAECompleto = rol.getListaDtoEstudianteS()
                .stream()
                .map(dtoEstudiante -> ejbTest.packEncuestasCompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        String fecha = rol.getSdf().format(new Date());
        gExcel.escribirDatosPrincipalesWordS(rol.getListaDtoEstudianteS(), dtoAECompleto.size(), fecha);
        try {
            gExcel.agregarTablaWordS(dtoAECompleto);
        } catch (Exception ex) {
            Logger.getLogger(SeguimientoTestAprendizajeTutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        gExcel.escribirWord(rol.getCarpeta(), rol.getSubcarpeta(), rol.getWord());
        //System.out.println("Se obtuvo el documento word"+ rol.getNameWord());
        String ruta = gExcel.enviarLibro();
        Ajax.oncomplete("descargar('" + gExcel.enviarLibro() + "');");
    }
    
    public void generateResultadosTestCompleto()throws IOException{
        gExcel.obtenerLibro(rol.getNombre2());
        rol.setExcel("Test_Completo_Incompleto.xlsx");
        ResultadoEJB<Grupos> resGrupo = ejbTest.obtenerGrupoSeleccionadoS(rol.getIdGrupo());
        rol.setGrupos(resGrupo.getValor());
        rol.setSubcarpeta("Grupo_"+rol.getGrupos().getGrado()+rol.getGrupos().getIdGrupo()+"_"+rol.getGrupos().getGruposPK().getCvePeriodo());
        ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral>> lista = ejbTest.obtenerEstudiantes(rol.getGrupos());
        rol.setListaDtoEstudianteS(lista.getValor());
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAECompleto = rol.getListaDtoEstudianteS()
                .stream()
                .map(dtoEstudiante -> ejbTest.packEncuestasCompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(!dtoAECompleto.isEmpty()){
            rol.setLibro(gExcel.getLibro().getSheetAt(0).getSheetName());
            for (int i = 0; i <= dtoAECompleto.size() - 1; i++) {
            DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dto = dtoAECompleto.get(i);
            String[] datos = {
                    String.valueOf(dto.getAlumnos().getMatricula()), dto.getPersonas().getNombre()+" "+dto.getPersonas().getApellidoPat()+" "+dto.getPersonas().getApellidoMat(), 
                dto.getCarrerasCgut().getNombre(), dto.getGrado().toString(), dto.getGrupo()};
            gExcel.escribirDatosExcel(datos, i, 2, rol.getLibro());
            }
        }
        //excel.eliminarLibros(dto.excel);
        //dto.dtoEXANI = ejbExani.getResultadosExani();
        
        
        
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAEInCompleto = rol.getListaDtoEstudianteS()
                .stream()
                .map(dtoEstudiante -> ejbTest.packEncuestasIncompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        if(!dtoAEInCompleto.isEmpty()) {
            rol.setLibro(gExcel.getLibro().getSheetAt(1).getSheetName());
            for (int i = 0; i <= dtoAEInCompleto.size() - 1; i++) {
            DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dto = dtoAEInCompleto.get(i);
            String[] datos = {
                    String.valueOf(dto.getAlumnos().getMatricula()), dto.getPersonas().getNombre()+" "+dto.getPersonas().getApellidoPat()+" "+dto.getPersonas().getApellidoMat(), 
                dto.getCarrerasCgut().getNombre(), dto.getGrado().toString(), dto.getGrupo()};
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
