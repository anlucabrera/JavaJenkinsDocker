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
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeuimientoTestAprendizajeRolMultiple;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoTestDiagnosticoAprendizaje;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestDiagnosticoAprendizaje;
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
public class SeguimientoTestAprendizajeTutor extends ViewScopedRol implements Desarrollable{
    
    @Getter @Setter private DtoSeuimientoTestAprendizajeRolMultiple rol = new DtoSeuimientoTestAprendizajeRolMultiple();
    @Getter @Setter Boolean cargado, tieneAcceso = false;
    
    @EJB EjbPropiedades ep;
    @EJB EjbSeguimientoTestDiagnosticoAprendizaje ejbSTDA;
    @Inject LogonMB logonMB;
    @Inject GenerateExcel gExcel;
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_TEST_DIAGNOSTICO_APRENDIZAJE_TUTOR);
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
    
    public void generarExcelTest() throws IOException{
        gExcel.obtenerLibro(rol.getNombre());
        rol.setExcel("Resultados_Test.xlsx");
        rol.setLibro(gExcel.getLibro().getSheetAt(0).getSheetName());
        //System.out.println("Grado Seleccionado:"+ rol.getIdGrupo());
        ResultadoEJB<Grupo> resGrupo = ejbSTDA.obtenerGrupoSeleccionado(rol.getIdGrupo());
        rol.setGrupo(resGrupo.getValor());
        rol.setSubcarpeta("Grupo_"+rol.getGrupo().getGrado()+rol.getGrupo().getLiteral().toString()+"_"+rol.getGrupo().getPeriodo());
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
        gExcel.escribirDatosExcelHoja0(dtoAECompleto, rol.getLibro());
        
        for (int i = 0; i < dtoAECompleto.size(); i++) {
            DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dto = dtoAECompleto.get(i);
            TestDiagnosticoAprendizaje test = ejbSTDA.obtenerTest(dto).getValor();
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
        ResultadoEJB<Grupo> resGrupo = ejbSTDA.obtenerGrupoSeleccionado(rol.getIdGrupo());
        rol.setGrupo(resGrupo.getValor());
        rol.setSubcarpeta("Grupo_"+rol.getGrupo().getGrado()+rol.getGrupo().getLiteral().toString()+"_"+rol.getGrupo().getPeriodo());
        ////////////Se obtiene la lista de estudiantes activos en el grupo seleccionado
        ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> lista = ejbSTDA.obtenerEstudiantes(rol.getGrupo());
        rol.setListaDtoEstudiante(lista.getValor());
        ////////////Se busca la lista de alumnos que han culminado el Test
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAECompleto = rol.getListaDtoEstudiante()
                .stream()
                .map(dtoEstudiante -> ejbSTDA.packEncuestasCompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        String fecha = rol.getSdf().format(new Date());
        gExcel.escribirDatosPrincipalesWord(rol.getListaDtoEstudiante(), dtoAECompleto.size(), fecha);
        try {
            gExcel.agregarTablaWord(dtoAECompleto);
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
        String valor = "seguimiento test de diagnositco de aprendizaje";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));

    }
    
}
