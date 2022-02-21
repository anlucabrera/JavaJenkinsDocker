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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeuimientoTestAprendizajeRolPsicopedagogia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoTestDiagnosticoAprendizaje;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CordinadoresTutores;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestDiagnosticoAprendizaje;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.util.GenerateExcel;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author Planeacion
 */
@Named
@ViewScoped
public class SeguimientoTestAprendizajePsicopedagogia extends ViewScopedRol implements Desarrollable{
    @Getter @Setter private DtoSeuimientoTestAprendizajeRolPsicopedagogia rol;
    @Getter @Setter Boolean cargado, tieneAcceso = false, coordinadorTutor = false;
    
    @EJB EjbPropiedades ep;
    @EJB EjbSeguimientoTestDiagnosticoAprendizaje ejbSTDA;
    @Inject LogonMB logonMB;
    @Inject GenerateExcel gExcel;
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_TEST_APRENDIZAJE_PSICOPEDAGOGIA);
            
            //ResultadoEJB<Filter<PersonalActivo>> resAccesoPsicoPedagodia = ejbSTDA.validarPsicopedagogia(logonMB.getPersonal().getClave());
            //ResultadoEJB<Filter<PersonalActivo>> resAccesoCoordinadorTutores = ejbSTDA.validarCoordinadorTutor(logonMB.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbSTDA.validarConsultaTest(logonMB.getPersonal().getClave());
            
            if(!resAcceso.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}
            
            Filter<PersonalActivo> filtro = resAcceso.getValor();
            PersonalActivo psicopedagogia = filtro.getEntity();
            rol = new DtoSeuimientoTestAprendizajeRolPsicopedagogia(filtro, psicopedagogia);
            tieneAcceso = rol.tieneAcceso(psicopedagogia);
            
            
            if(!tieneAcceso){return;}
            rol.setPersonalActivo(psicopedagogia);
            
            
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.CONSULTA);
            
            ResultadoEJB<List<DtoAlumnosEncuesta.DtoPeriodoEscolar>> dtoPeriodos = ejbSTDA.obtenerPeriodosEscolares();
            rol.setListaPeriodosEscolares(dtoPeriodos.getValor());
            
            ResultadoEJB<PeriodosEscolares> periodo = ejbSTDA.obtenerPeriodoActual();
            rol.setPeriodoActivo(periodo.getValor().getPeriodo());
            rol.setTest(new Evaluaciones());
            obtenerHistoricoTest();
            ResultadoEJB<VariablesProntuario> variable1 = ejbSTDA.obtenerVariable1();
            ResultadoEJB<VariablesProntuario> variable2 = ejbSTDA.obtenerVariable2();
            ResultadoEJB<VariablesProntuario> variable3 = ejbSTDA.obtenerVariable3();
            ResultadoEJB<VariablesProntuario> variable4 = ejbSTDA.obtenerVariable4();
            rol.setVariable1(variable1.getValor());
            rol.setVariable2(variable2.getValor());
            rol.setVariable3(variable3.getValor());
            rol.setVariable4(variable4.getValor());
            ResultadoEJB<CordinadoresTutores> resAccesoCoordinadorTutores = ejbSTDA.validarCoordinadorTutor(rol.getPersonalActivo().getPersonal().getClave(), rol.getPeriodoActivo());
            if(resAccesoCoordinadorTutores.getCorrecto()){
                coordinadorTutor = Boolean.TRUE;
                rol.setCordinadorTutor(resAccesoCoordinadorTutores.getValor());
            }
            
            /*ResultadoEJB<List<DtoAlumnosEncuesta.DtoProgramaEducativo>> dtoPEs = ejbSTDA.obtenerProgramasEducativo(rol.getPeriodoActivo());
            rol.setListaProgramasEducativos(dtoPEs.getValor());
            rol.setCveCarrera(rol.getListaProgramasEducativos().get(0).getArea());
            rol.setBd(rol.getListaProgramasEducativos().get(0).getBd());
            
            ResultadoEJB<List<DtoAlumnosEncuesta.DtoGruposTutor>> dtoGruposCE = ejbSTDA.obtenerGrupos(rol.getCveCarrera(), rol.getPeriodoActivo(), rol.getBd());
            rol.setDtoGrupos(dtoGruposCE.getValor());*/
            
            
            rol.setNombre("plantilla_test.xlsx");
            rol.setCarpeta("resultados_test");
            rol.setNombreWord("formato_informe.docx");
            
            
            rol.getInstrucciones().add("Seguimiento del Test de Diagnóstico de Estilos de Aprendizaje");
            rol.getInstrucciones().add("CONSULTA DE INFORMACIÓN:");
            rol.getInstrucciones().add("-Seleccionar Periodo Escolar.");
            rol.getInstrucciones().add("-Seleccionar Programa Educativo.");
            rol.getInstrucciones().add("-Seleccionar Grupo.");
            rol.getInstrucciones().add("NOTA: En la parte superior izquierda del formulario se muestra el Periodo Escolar activo.");
            
        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }
    
    public void obtenerProgramasEducativos(){
        if(coordinadorTutor == true){
            //System.out.println("Periodo escolar:"+ rol.getPeriodo());
            ResultadoEJB<List<DtoAlumnosEncuesta.DtoProgramaEducativo>> dtoPEs = ejbSTDA.obtenerProgramasEducativo(rol.getCordinadorTutor().getAreaAcademica());
            rol.setListaProgramasEducativos(dtoPEs.getValor());
            //rol.getListaProgramasEducativos().forEach(System.out::println);
            rol.setCveCarrera(Short.parseShort("0"));
            rol.setIdGrupo(0);
            rol.setListaAlumnosTestCompleto(new ArrayList<>());
            rol.setListaAlumnosTestIncompleto(new ArrayList<>());
        }else{
            //System.out.println("Periodo escolar:"+ rol.getPeriodo());
            ResultadoEJB<List<DtoAlumnosEncuesta.DtoProgramaEducativo>> dtoPEs = ejbSTDA.obtenerProgramasEducativo(rol.getPeriodo());
            rol.setListaProgramasEducativos(dtoPEs.getValor());
            //rol.getListaProgramasEducativos().forEach(System.out::println);
            rol.setCveCarrera(Short.parseShort("0"));
            rol.setIdGrupo(0);
            rol.setListaAlumnosTestCompleto(new ArrayList<>());
            rol.setListaAlumnosTestIncompleto(new ArrayList<>());
            //consultarAvance();
        }
        
    }
    
    public void obtenerGruposPE(){
        DtoAlumnosEncuesta.DtoProgramaEducativo dtoPrograma = rol.getListaProgramasEducativos()
                .stream()
                .filter(p -> p.getArea().equals(rol.getCveCarrera()))
                .findFirst()
                .orElse(new DtoAlumnosEncuesta.DtoProgramaEducativo());
        //System.out.println("Programa:" + dtoPrograma);
        ResultadoEJB<List<DtoAlumnosEncuesta.DtoGruposTutor>> dtoGruposCE = ejbSTDA.obtenerGrupos(dtoPrograma.getArea(), rol.getPeriodo(), dtoPrograma.getBd());
        rol.setDtoGrupos(dtoGruposCE.getValor());
        rol.setIdGrupo(0);
        //rol.getDtoGrupos().forEach(System.out::println);
        rol.setListaAlumnosTestCompleto(new ArrayList<>());
        rol.setListaAlumnosTestIncompleto(new ArrayList<>());
    }
    
    public void consultarEstudiantes(){
        DtoAlumnosEncuesta.DtoProgramaEducativo dtoPrograma = rol.getListaProgramasEducativos()
                .stream()
                .filter(p -> p.getArea().equals(rol.getCveCarrera()))
                .findFirst()
                .orElse(new DtoAlumnosEncuesta.DtoProgramaEducativo());
        DtoAlumnosEncuesta.DtoGruposTutor dtoGrupo = rol.getDtoGrupos()
                .stream()
                .filter(g -> g.getIdGrupo().equals(rol.getIdGrupo()))
                .findFirst()
                .orElse(new DtoAlumnosEncuesta.DtoGruposTutor());
        rol.setBd(dtoPrograma.getBd());
        if(dtoPrograma.getBd().equals("Control Escolar")){
            ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> lista = ejbSTDA.obtenerEstudiantesCE(dtoGrupo.getIdGrupo());
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
            List<DtoAlumnosEncuesta.DtoAlumnosTest> dtoTestCompleto = dtoAECompleto
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
            List<DtoAlumnosEncuesta.DtoAlumnosTest> dtoTestIncompleto = dtoAEIncompleto
                    .stream()
                    .map(dto -> pack(dto))
                    .collect(Collectors.toList());
            rol.setListaAlumnosTestIncompleto(dtoTestIncompleto);
            if(rol.getListaAlumnosTestCompleto().isEmpty()){
                mostrarMensaje("No hay lista de Estudiantes con el Test Completo");
            }
            if(rol.getListaAlumnosTestIncompleto().isEmpty()){
                mostrarMensaje("No hay lista de Estudiantes con el Test Incompleto");
            }
            //System.out.println("Encuesta incompleta");
            //dtoAEIncompleto.stream().forEach(System.out::println);
        }
        if(dtoPrograma.getBd().equals("SAIIUT")){
            ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral>> lista = ejbSTDA.obtenerEstudiantesSaiiut(dtoGrupo.getIdGrupo());
            rol.setListaDtoEstudianteS(lista.getValor());
            //rol.getListaDtoEstudiante().forEach(System.out::println);
            List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAECompleto = rol.getListaDtoEstudianteS()
                .stream()
                .map(dtoEstudiante -> ejbSTDA.packEncuestasCompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
            //if(dtoAECompleto.isEmpty()) return;
            rol.setListaTestCompletoS(dtoAECompleto);
            List<DtoAlumnosEncuesta.DtoAlumnosTest> dtoTestCompleto = dtoAECompleto
                    .stream()
                    .map(dto -> pack(dto))
                    .collect(Collectors.toList());
            rol.setListaAlumnosTestCompleto(dtoTestCompleto);
            //System.out.println("Encuesta completa");
            //dtoAECompleto.stream().forEach(System.out::println);
            List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAEIncompleto = rol.getListaDtoEstudianteS()
                    .stream()
                    .map(dtoEstudiante -> ejbSTDA.packEncuestasIncompletas(dtoEstudiante))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            rol.setListaTestIncompletoS(dtoAEIncompleto);
            List<DtoAlumnosEncuesta.DtoAlumnosTest> dtoTestIncompleto = dtoAEIncompleto
                    .stream()
                    .map(dto -> pack(dto))
                    .collect(Collectors.toList());
            rol.setListaAlumnosTestIncompleto(dtoTestIncompleto);
            if(rol.getListaAlumnosTestCompleto().isEmpty()){
                mostrarMensaje("No hay lista de Estudiantes con el Test Completo");
            }
            if(rol.getListaAlumnosTestIncompleto().isEmpty()){
                mostrarMensaje("No hay lista de Estudiantes con el Test Incompleto");
            }
            //System.out.println("Encuesta incompleta");
            //dtoAEIncompleto.stream().forEach(System.out::println);
        }
        
    }
    
    public DtoAlumnosEncuesta.DtoAlumnosTest pack(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar dtoAEGCE){
        DtoAlumnosEncuesta.DtoAlumnosTest dto;
        dto = new DtoAlumnosEncuesta.DtoAlumnosTest(dtoAEGCE.getAlumnos().getMatricula(),
                dtoAEGCE.getPersonas().getNombre()+" "+dtoAEGCE.getPersonas().getApellidoPaterno()+" "+dtoAEGCE.getPersonas().getApellidoMaterno(),
                dtoAEGCE.getGrado(), dtoAEGCE.getGrupo(), dtoAEGCE.getSiglas());
        return dto;
    }
    
    public DtoAlumnosEncuesta.DtoAlumnosTest pack(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dtoAEGCE){
        DtoAlumnosEncuesta.DtoAlumnosTest dto;
        dto = new DtoAlumnosEncuesta.DtoAlumnosTest(Integer.parseInt(dtoAEGCE.getAlumnos().getMatricula()),
                dtoAEGCE.getPersonas().getNombre()+" "+dtoAEGCE.getPersonas().getApellidoPat()+" "+dtoAEGCE.getPersonas().getApellidoMat(),
                dtoAEGCE.getGrado(), dtoAEGCE.getGrupo(), dtoAEGCE.getSiglas());
        return dto;
    }
    
    public void generarExcelTest() throws IOException{
        DtoAlumnosEncuesta.DtoProgramaEducativo dtoPrograma = rol.getListaProgramasEducativos()
                .stream()
                .filter(p -> p.getArea().equals(rol.getCveCarrera()))
                .findFirst()
                .orElse(new DtoAlumnosEncuesta.DtoProgramaEducativo());
        if(dtoPrograma.getBd().equals("Control Escolar")){
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
                rol.setLibro(gExcel.getLibro().getSheetAt(i + 1).getSheetName());
                if(test.equals(new TestDiagnosticoAprendizaje())) return;
                gExcel.escribirDatosExcel(dto, test, rol.getLibro());
            }
            gExcel.escribirLibro(rol.getCarpeta(), rol.getSubcarpeta(), rol.getExcel());

            String ruta = gExcel.enviarLibro();
            Ajax.oncomplete("descargar('" + gExcel.enviarLibro() + "');");
        }
        if(dtoPrograma.getBd().equals("SAIIUT")){
            gExcel.obtenerLibro(rol.getNombre());
            rol.setExcel("Resultados_Test.xlsx");
            rol.setLibro(gExcel.getLibro().getSheetAt(0).getSheetName());
            //System.out.println("Grado Seleccionado:"+ rol.getIdGrupo());
            ResultadoEJB<Grupos> resGrupo = ejbSTDA.obtenerGrupoSeleccionadoS(rol.getIdGrupo());
            rol.setGrupos(resGrupo.getValor());
            rol.setSubcarpeta("Grupo_"+rol.getGrupos().getGrado()+rol.getGrupos().getIdGrupo()+"_"+rol.getGrupos().getGruposPK().getCvePeriodo());
            ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral>> lista = ejbSTDA.obtenerEstudiantes(rol.getGrupos());
            rol.setListaDtoEstudianteS(lista.getValor());
            //System.out.println("Lista estudiantes encontrados");
            //rol.getListaDtoEstudiante().stream().forEach(System.out::println);
            List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAECompleto = rol.getListaDtoEstudianteS()
                    .stream()
                    .map(dtoEstudiante -> ejbSTDA.packEncuestasCompletas(dtoEstudiante))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            gExcel.escribirDatosExcelHoja0S(dtoAECompleto, rol.getLibro());

            for (int i = 0; i < dtoAECompleto.size(); i++) {
                DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral dto = dtoAECompleto.get(i);
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
        
       
        
    }
    
    public void generarInformeWord() throws IOException{
        DtoAlumnosEncuesta.DtoProgramaEducativo dtoPrograma = rol.getListaProgramasEducativos()
                .stream()
                .filter(p -> p.getArea().equals(rol.getCveCarrera()))
                .findFirst()
                .orElse(new DtoAlumnosEncuesta.DtoProgramaEducativo());
        if(dtoPrograma.getBd().equals("Control Escolar")){
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
                Logger.getLogger(SeguimientoTestAprendizajePsicopedagogia.class.getName()).log(Level.SEVERE, null, ex);
            }

            gExcel.escribirWord(rol.getCarpeta(), rol.getSubcarpeta(), rol.getWord());
            //System.out.println("Se obtuvo el documento word"+ rol.getNameWord());
            String ruta = gExcel.enviarLibro();
            Ajax.oncomplete("descargar('" + gExcel.enviarLibro() + "');");
        }
        if(dtoPrograma.getBd().equals("SAIIUT")){
            gExcel.obtenerWord(rol.getNombreWord());

            rol.setWord("Informe_Test.docx");
            rol.setNameWord(gExcel.getWord().toString());
            ResultadoEJB<Grupos> resGrupo = ejbSTDA.obtenerGrupoSeleccionadoS(rol.getIdGrupo());
            rol.setGrupos(resGrupo.getValor());
            rol.setSubcarpeta("Grupo_"+rol.getGrupos().getGrado()+rol.getGrupos().getIdGrupo()+"_"+rol.getGrupos().getGruposPK().getCvePeriodo());
            ////////////Se obtiene la lista de estudiantes activos en el grupo seleccionado
            ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral>> lista = ejbSTDA.obtenerEstudiantes(rol.getGrupos());
            rol.setListaDtoEstudianteS(lista.getValor());
            ////////////Se busca la lista de alumnos que han culminado el Test
            List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAECompleto = rol.getListaDtoEstudianteS()
                    .stream()
                    .map(dtoEstudiante -> ejbSTDA.packEncuestasCompletas(dtoEstudiante))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            String fecha = rol.getSdf().format(new Date());
            gExcel.escribirDatosPrincipalesWordS(rol.getListaDtoEstudianteS(), dtoAECompleto.size(), fecha);
            try {
                gExcel.agregarTablaWordS(dtoAECompleto);
            } catch (Exception ex) {
                Logger.getLogger(SeguimientoTestAprendizajePsicopedagogia.class.getName()).log(Level.SEVERE, null, ex);
            }

            gExcel.escribirWord(rol.getCarpeta(), rol.getSubcarpeta(), rol.getWord());
            //System.out.println("Se obtuvo el documento word"+ rol.getNameWord());
            String ruta = gExcel.enviarLibro();
            Ajax.oncomplete("descargar('" + gExcel.enviarLibro() + "');");
        }
        
    }
    
    public void consultarAvance(){
        if(rol.getPeriodo() == null) {mostrarMensaje("Para consultar el avanve, debe seleccionar un periodo escolar");}
        List<DtoAlumnosEncuesta.DtoAvaceTestProgramaEducativo> listaAvance;
        ResultadoEJB<List<DtoAlumnosEncuesta.DtoProgramaEducativo>> dtoPEs = ejbSTDA.obtenerProgramasEducativo(rol.getPeriodo());
        rol.setListaProgramasEducativos(dtoPEs.getValor());
        //rol.getListaProgramasEducativos().forEach(System.out::println);
        
        listaAvance = rol.getListaProgramasEducativos().stream()
                .map(dtoPE -> pack(dtoPE, rol.getPeriodo()))
                .collect(Collectors.toList())
                .stream().filter(x -> x.getTotalMatricula() != 0)
                .collect(Collectors.toList());
        rol.setListaAvanceTest(listaAvance);
            //rol.getListaProgramasEducativos().forEach(System.out::println);
            
            
    }
    
    public DtoAlumnosEncuesta.DtoAvaceTestProgramaEducativo pack(DtoAlumnosEncuesta.DtoProgramaEducativo dtoPE, Integer periodo){
        DtoAlumnosEncuesta.DtoAvaceTestProgramaEducativo dto = new DtoAlumnosEncuesta.DtoAvaceTestProgramaEducativo();
        rol.setBd(dtoPE.getBd());
        if(rol.getBd().equals("Control Escolar")){
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
            
        }
        if(rol.getBd().equals("SAIIUT")){
            Short area = dtoPE.getArea();
            List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> lista = ejbSTDA.obtenerEstudiantesSaiiut(area, rol.getPeriodo()).getValor();
            List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAECompleto = lista
                .stream()
                .map(dtoEstudiante -> ejbSTDA.packEncuestasCompletas(dtoEstudiante))
                .filter(ResultadoEJB::getCorrecto)
                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
            List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAEIncompleto = lista
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
        }
        
        
        return dto;
    }
    
    public void crearTest(){
        ResultadoEJB<Evaluaciones> resTest;
            //System.out.println(rol.getPeriodoEvaluacion()+"-"+rol.getFechaInicio()+"-"+rol.getFechaFin()+"-"+rol.getTipo());
            //rol.getTest().setEvaluacion(rol.getEvaluacion());
            resTest = ejbSTDA.guardarTest(new Evaluaciones(), rol.getPeriodoEvaluacion(), rol.getFechaInicio(), rol.getFechaFin(), rol.getTipo(), Operacion.PERSISTIR);
            mostrarMensajeResultadoEJB(resTest);
            obtenerHistoricoTest();
            rol.setGrupo(new Grupo());
            rol.setPeriodoEvaluacion(0);
            rol.setFechaInicio(new Date());
            rol.setFechaFin(new Date());
            rol.setTipo("");
    }
    
    public void actualizarGrupo(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        Evaluaciones updateE = (Evaluaciones) dataTable.getRowData();
        ejbSTDA.guardarTest(updateE, rol.getPeriodoEvaluacion(), rol.getFechaInicio(), rol.getFechaFin(), rol.getTipo(), Operacion.ACTUALIZAR);
        mostrarMensaje("Se ha actualizado la información del grupo seleccionado");
    }
    
    public void obtenerHistoricoTest(){
        ResultadoEJB<List<Evaluaciones>> lista = ejbSTDA.obtenerListaTest();
        rol.setListaEvaluacines(lista.getValor());
    }
    
    public void actualizarVariable1(){
        ejbSTDA.actualizarVariable(rol.getVariable1().getNombre(), rol.getVar1(), Operacion.ACTUALIZAR);
        ResultadoEJB<VariablesProntuario> resVP = ejbSTDA.obtenerVariable1();
        rol.setVariable1(resVP.getValor());
        mostrarMensaje("La variable ha sido actualizada");
    }
    
    public void actualizarVariable2(){
        ejbSTDA.actualizarVariable(rol.getVariable2().getNombre(), rol.getVar2(), Operacion.ACTUALIZAR);
        ResultadoEJB<VariablesProntuario> resVP = ejbSTDA.obtenerVariable2();
        rol.setVariable2(resVP.getValor());
        mostrarMensaje("La variable ha sido actualizada");
    }
    
    public void actualizarVariable3(){
        ejbSTDA.actualizarVariable(rol.getVariable3().getNombre(), rol.getVar3(), Operacion.ACTUALIZAR);
        ResultadoEJB<VariablesProntuario> resVP = ejbSTDA.obtenerVariable3();
        rol.setVariable3(resVP.getValor());
        mostrarMensaje("La variable ha sido actualizada");
    }
    
    public void actualizarVariable4(){
        ejbSTDA.actualizarVariable(rol.getVariable4().getNombre(), rol.getVar4(), Operacion.ACTUALIZAR);
        ResultadoEJB<VariablesProntuario> resVP = ejbSTDA.obtenerVariable4();
        rol.setVariable4(resVP.getValor());
        mostrarMensaje("La variable ha sido actualizada");
    }
    
     @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimiento test de diagnositco de aprendizaje";
         Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));

    }
}
