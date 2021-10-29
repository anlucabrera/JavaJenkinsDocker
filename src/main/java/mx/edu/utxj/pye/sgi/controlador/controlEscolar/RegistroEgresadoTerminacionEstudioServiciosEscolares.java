/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoInscripcion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroEgresadoRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroEgresadoTerminacionEstudios;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.RegistroEgresadosTerminacionEstudios;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.*;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
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
public class RegistroEgresadoTerminacionEstudioServiciosEscolares extends ViewScopedRol implements Desarrollable{
    @Getter @Setter RegistroEgresadoRolServiciosEscolares rol;
    @Getter @Setter Boolean cargado, tieneAcceso = false;
    @Getter private Boolean bloquear = false;
    @Getter private Boolean aeep = false;
    @Getter private Boolean css = false;
    @Getter private Boolean certificado = false;
    @Getter private Boolean desbloquear = true;
    @Getter @Setter String nombre2, wordPlantilla, wordPlantilla2, wordPlantilla3, wordDestino, pdfDestino, nombreWord, excel, subCarpeta, libro, carpeta2;
    @Getter @Setter SimpleDateFormat sdf1 = new SimpleDateFormat("d 'días del mes de' MMMM 'del año' yyyy", new Locale("ES", "MX"));
    
    
    @EJB EjbPropiedades ep;
    @EJB EjbRegistroEgresadoTerminacionEstudios ejbRegistro;
    @EJB EjbConsultaCalificacion ejb;
    @Inject LogonMB logonMB;
    @Inject GenerateExcel gExcel;
    
    @PostConstruct
    public void init(){
        try{
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_EGRESADO_TERMINACION_ESTUDIOS);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbRegistro.validarServiciosEscolares(logonMB.getPersonal().getClave());//validar si es director
//            //System.out.println("resAcceso = " + resAcceso);

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbRegistro.validarServiciosEscolares(logonMB.getPersonal().getClave());
//            //System.out.println("resValidacion = " + resValidacion);
            if(!resValidacion.getCorrecto() && !resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo serviciosEscolares = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new RegistroEgresadoRolServiciosEscolares(filtro, serviciosEscolares);
            tieneAcceso = rol.tieneAcceso(serviciosEscolares);
//            //System.out.println("tieneAcceso1 = " + tieneAcceso);
            if(!tieneAcceso){
                rol.setFiltro(resValidacion.getValor());
                tieneAcceso = rol.tieneAcceso(serviciosEscolares);
            }
//            //System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setServiciosEscolares(serviciosEscolares);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            rol.setNivelRol(NivelRol.OPERATIVO);
            
            ResultadoEJB<List<DtoEstudiante>> resultadoEJB = ejbRegistro.obtenerEstudiantes();
            ResultadoEJB<List<RegistroEgresadosTerminacionEstudios>> resLista = ejbRegistro.obtenerRegistros();
            rol.setListaHistoricoRegistro(resLista.getValor());
            if(resultadoEJB.getCorrecto()) {
                rol.setListaEstudiantes(resultadoEJB.getValor());
            }
            carpeta2 = "documentos_terminacion_estudios";
            wordPlantilla = "plantilla_acta_examen_profesional.docx";
            wordPlantilla2 = "plantilla_acta_servicio_social.docx";
            wordPlantilla3 = "plantilla_certificado_estudios.docx";
            rol.setOcultarPanelRegistro(Boolean.TRUE);
            consultarListaEstudiantes();
            
        } catch (Exception e) {mostrarExcepcion(e);}
    }
    
    public void buscarEstudiante(ValueChangeEvent event){
        rol.setMatricula((Integer) event.getNewValue());
        ////System.out.println("Matricula:"+ rol.getMatricula());
        consultarInformacion(rol.getMatricula());
    }
    
    public void consultarInformacion(Integer matricula){
        //if(rol.getMatricula().equals(null)) return;
        rol.setListaRegistros(new ArrayList<>());
        ResultadoEJB<DtoEstudiante> resEstudiante = ejbRegistro.validarEstudiante(matricula);
        if(!resEstudiante.getCorrecto()) return;
        rol.setEstudianteDto(resEstudiante.getValor());
        bloquear =  Boolean.TRUE;
        desbloquear =  Boolean.FALSE;
        //System.out.println("Generación" + rol.getEstudianteDto().getInscripcionActiva().getGeneracion().getInicio() +"-"+ rol.getEstudianteDto().getInscripcionActiva().getGeneracion().getInicio());
        rol.setHayRegistro(verificarRegistroExistente(rol.getEstudianteDto().getInscripcionActiva().getInscripcion().getMatricula(), rol.getGeneracion()));
        //System.out.println("Hay registro?"+ rol.getHayRegistro());
        ResultadoEJB<List<RegistroEgresadosTerminacionEstudios>> resLista = ejbRegistro.obtenerRegistros(rol.getEstudianteDto().getInscripcionActiva().getInscripcion().getMatricula());
            rol.setListaRegistros(resLista.getValor());
            rol.setTotalRegistro(rol.getListaRegistros().size());
        if(!rol.getHayRegistro().equals(Boolean.TRUE)){
            //System.out.println("No hay registro");
            //rol.setOcultarPanelRegistro(Boolean.FALSE);
            rol.setMatricula(rol.getEstudianteDto().getInscripcionActiva().getInscripcion().getMatricula());
            String generacion = rol.getEstudianteDto().getInscripcionActiva().getGeneracion().getInicio()+"-"+rol.getEstudianteDto().getInscripcionActiva().getGeneracion().getFin();
            //System.out.println("Se inicializan las variables:" + rol.getMatricula()+"-"+ generacion);
            ResultadoEJB<RegistroEgresadosTerminacionEstudios> registroUltimoPorGeneracion = ejbRegistro.obtenerUltimoRegistro(generacion);
            if(registroUltimoPorGeneracion.getValor().getIdRegistro() == null){
                //System.out.println("No hay registro con la generación del estudiante");
                rol.setGeneracion(generacion);
                rol.setFechaEmision(new Date());
            }else{
                //System.out.println("Ya hay un registro con la generación del estudiante");
                rol.setGeneracion(registroUltimoPorGeneracion.getValor().getGeneracion());
                rol.setFechaEmision(registroUltimoPorGeneracion.getValor().getFechaEmision());
            }
            ResultadoEJB<RegistroEgresadosTerminacionEstudios> registroUltimo = ejbRegistro.obtenerUltimoRegistro();
            if(registroUltimo.getValor().getIdRegistro() != null){
                //System.out.println("Se obtuvo el ultimo registro");
                RegistroEgresadosTerminacionEstudios rete = registroUltimo.getValor();
                rol.setFolio(1 + rete.getFolio());
                if(rete.getFoja() == 192){
                    //System.out.println("El registro seleccionado muestra que el numero de foja ha alcanzado el limite");
                    rol.setLibro(1 + rete.getLibro());
                    rol.setFoja(1);
                }else{
                    //System.out.println("El libro aun no ha alcanzado el numero de fojas requeridos");
                    rol.setLibro(1);
                    rol.setFoja(1 + rete.getFoja());
                }
            }else{
                //System.out.println("No hay ultimo registro registro");
                rol.setFolio(1);
                rol.setLibro(1);
                rol.setFoja(1);
            }
            
        }
        
    }
    
    public Boolean verificarRegistroExistente(Integer matricula, String generacion){
        ResultadoEJB<Boolean> resBool = ejbRegistro.verificarRegistro(matricula, generacion);
        return resBool.getValor();
    }
    
   public void obtenerHistoricoDeRegistro(){
       ResultadoEJB<List<RegistroEgresadosTerminacionEstudios>> resLista = ejbRegistro.obtenerRegistros();
       if(!resLista.getCorrecto()) return;
       rol.setListaHistoricoRegistro(resLista.getValor());
   }
   
   public void obtenerRegistrosEstudiante(){
       ////System.out.println("Estudiante:"+ rol.getEstudianteDto().getInscripcionActiva().getInscripcion().getMatricula());
       ResultadoEJB<List<RegistroEgresadosTerminacionEstudios>> resLista = ejbRegistro.obtenerRegistros(rol.getEstudianteDto().getInscripcionActiva().getInscripcion().getMatricula());
            rol.setListaRegistros(resLista.getValor());
   }
   
   public void guardarRegistto(){
       ResultadoEJB<RegistroEgresadosTerminacionEstudios> res;
       rol.setRegistroEgresadosTerminacionEstudio(new RegistroEgresadosTerminacionEstudios());
       rol.getRegistroEgresadosTerminacionEstudio().setIdEstudiante(rol.getEstudianteDto().getInscripcionActiva().getInscripcion());
       rol.getRegistroEgresadosTerminacionEstudio().setGeneracion(rol.getGeneracion());
       rol.getRegistroEgresadosTerminacionEstudio().setFolio(rol.getFolio());
       rol.getRegistroEgresadosTerminacionEstudio().setLibro(rol.getLibro());
       rol.getRegistroEgresadosTerminacionEstudio().setFoja(rol.getFoja());
       rol.getRegistroEgresadosTerminacionEstudio().setFechaEmision(rol.getFechaEmision());
       res = ejbRegistro.guardarRegistro(rol.getRegistroEgresadosTerminacionEstudio(), rol.getEstudianteDto().getInscripcionActiva().getInscripcion(), Operacion.PERSISTIR);
            mostrarMensajeResultadoEJB(res);
            //obtenerHistoricoDeRegistro();
            obtenerRegistrosEstudiante();
   }
   
   public void generarActaExamenProfesional(Integer idEstudiante) throws IOException {
       aeep = true;
        gExcel.obtenerWord(wordPlantilla);
        nombreWord = gExcel.getWord().toString();
        RegistroEgresadosTerminacionEstudios r = ejbRegistro.obtenerRegistro(idEstudiante).getValor();
        wordDestino = "aeep-"+r.getIdEstudiante().getMatricula()+r.getFolio()+r.getLibro()+r.getFoja()+".docx";
        pdfDestino = "aeep-"+r.getIdEstudiante().getMatricula()+r.getFolio()+r.getLibro()+r.getFoja()+".pdf";
        subCarpeta = "archivo-"+r.getIdEstudiante().getMatricula()+r.getFolio()+r.getLibro()+r.getFoja();
        ////////////Se obtiene la lista de estudiantes activos en el grupo seleccionado
        ResultadoEJB<DtoEstudiante> dtoEstudiante = ejb.validadEstudianteK(r.getIdEstudiante().getMatricula());
       //System.out.println("DtoEstudiante:"+ dtoEstudiante.getValor().getInscripcionActiva().getInscripcion().getPeriodo());
        ResultadoEJB<SeguimientoEstadiaEstudiante> s = ejbRegistro.obtenerEstadia(idEstudiante);
        ResultadoEJB<AreasUniversidad> au = ejbRegistro.obtenerProgramaEducativo(dtoEstudiante.getValor().getInscripciones().stream()
                .filter(x -> x.getGrupo().getGrado() == 6).findFirst().orElse(new DtoInscripcion(new Estudiante(), new Grupo(), new PeriodosEscolares(), new Generaciones(), Boolean.FALSE))
                .getInscripcion().getCarrera());
        ResultadoEJB<Personal> jefeSE = ejbRegistro.obtenerJefeServciosEscolares();
        ResultadoEJB<Personal> director = ejbRegistro.obtenerDirectorCarrera(au.getValor().getAreaSuperior());
        ResultadoEJB<VariablesProntuario> nivelSE = ejbRegistro.obtenerNivelEducativo(jefeSE.getValor().getClave());
        ResultadoEJB<VariablesProntuario> nivelD = ejbRegistro.obtenerNivelEducativo(director.getValor().getClave());
       ResultadoEJB<VariablesProntuario> cargoSE = ejbRegistro.obtenerCargo(jefeSE.getValor().getClave());
       ResultadoEJB<VariablesProntuario> cargoD = ejbRegistro.obtenerCargo(director.getValor().getClave());
        String fecha = sdf1.format(r.getFechaEmision());
        gExcel.escribirDatosWordAEP(nivelSE.getValor(), nivelD.getValor(), cargoSE.getValor(), cargoD.getValor(), dtoEstudiante.getValor(), au.getValor(), s.getValor().getProyecto(), fecha, r);
        gExcel.escribirWord(carpeta2, subCarpeta, wordDestino);
        //gExcel.abrirArchivo(carpeta2, subCarpeta, wordDestino);
        String ruta = gExcel.enviarLibro();
        Ajax.oncomplete("descargar('" + gExcel.enviarLibro() + "');");
    }
   
   public void generarActaServicioSocial(Integer idEstudiante) throws IOException{
       css = true;
        gExcel.obtenerWord(wordPlantilla2);
        nombreWord = gExcel.getWord().toString();
        RegistroEgresadosTerminacionEstudios r = ejbRegistro.obtenerRegistro(idEstudiante).getValor();
        wordDestino = "carta_ss-"+r.getIdEstudiante().getMatricula()+r.getFolio()+r.getLibro()+r.getFoja()+".docx";
        subCarpeta = "archivo-"+r.getIdEstudiante().getMatricula()+r.getFolio()+r.getLibro()+r.getFoja();
        ////////////Se obtiene la lista de estudiantes activos en el grupo seleccionado
        ResultadoEJB<DtoEstudiante> dtoEstudiante = ejb.validadEstudianteK(r.getIdEstudiante().getMatricula());
        ResultadoEJB<SeguimientoEstadiaEstudiante> s = ejbRegistro.obtenerEstadia(idEstudiante);
        ResultadoEJB<AreasUniversidad> au = ejbRegistro.obtenerProgramaEducativo(dtoEstudiante.getValor().getInscripciones().stream().filter(x -> x.getGrupo().getGrado() == 6).findFirst().get().getInscripcion().getCarrera());
        ResultadoEJB<Personal> jefeSE = ejbRegistro.obtenerJefeServciosEscolares();
        ResultadoEJB<Personal> director = ejbRegistro.obtenerDirectorCarrera(Short.parseShort("5"));
        ResultadoEJB<VariablesProntuario> nivelSE = ejbRegistro.obtenerNivelEducativo(jefeSE.getValor().getClave());
        ResultadoEJB<VariablesProntuario> nivelD = ejbRegistro.obtenerNivelEducativo(director.getValor().getClave());
       ResultadoEJB<VariablesProntuario> cargoSE = ejbRegistro.obtenerCargo(jefeSE.getValor().getClave());
       ResultadoEJB<VariablesProntuario> cargoD = ejbRegistro.obtenerCargo(director.getValor().getClave());
        String fecha = sdf1.format(r.getFechaEmision());
        gExcel.escribirDatosWordASS(nivelSE.getValor(), nivelD.getValor(), cargoSE.getValor(), cargoD.getValor(), dtoEstudiante.getValor(), au.getValor(), fecha, r);
        gExcel.escribirWord(carpeta2, subCarpeta, wordDestino);
        //gExcel.abrirArchivo(carpeta2, subCarpeta, wordDestino);
        ////System.out.println("Se obtuvo el documento word"+ rol.getNameWord());
        String ruta = gExcel.enviarLibro();
        Ajax.oncomplete("descargar('" + gExcel.enviarLibro() + "');");
    }
   
   public void generarCertificadoEstudios(Integer idEstudiante)throws IOException{
       certificado = true;
        gExcel.obtenerWord(wordPlantilla3);
        nombreWord = gExcel.getWord().toString();
        RegistroEgresadosTerminacionEstudios r = ejbRegistro.obtenerRegistro(idEstudiante).getValor();
        wordDestino = "certificado-"+r.getIdEstudiante().getMatricula()+r.getFolio()+r.getLibro()+r.getFoja()+".docx";
        subCarpeta = "archivo-"+r.getIdEstudiante().getMatricula()+r.getFolio()+r.getLibro()+r.getFoja();
        ////////////Se obtiene la lista de estudiantes activos en el grupo seleccionado
        ResultadoEJB<DtoEstudiante> dtoEstudiante = ejb.validadEstudianteK(r.getIdEstudiante().getMatricula());
        Grupo grupo = dtoEstudiante.getValor().getInscripciones().stream().filter(x -> x.getGrupo().getGrado() == 6).findFirst().get().getGrupo();
        ResultadoEJB<SeguimientoEstadiaEstudiante> s = ejbRegistro.obtenerEstadia(idEstudiante);
        ResultadoEJB<AreasUniversidad> au = ejbRegistro.obtenerProgramaEducativo(dtoEstudiante.getValor().getInscripciones().stream().filter(x -> x.getGrupo().getGrado() == 6).findFirst().get().getInscripcion().getCarrera());
        ResultadoEJB<Personal> jefeSE = ejbRegistro.obtenerJefeServciosEscolares();
        ResultadoEJB<Personal> director = ejbRegistro.obtenerDirectorCarrera(au.getValor().getAreaSuperior());
        ResultadoEJB<List<PlanCompetencias>> listCompetenciasEspecificas = ejbRegistro.obtenerListaCompetencias(grupo.getPlan(), "Específica");
        ResultadoEJB<List<PlanCompetencias>> listCompetenciasGenericas = ejbRegistro.obtenerListaCompetencias(grupo.getPlan(), "Genérica");
        ResultadoEJB<VariablesProntuario> nivelSE = ejbRegistro.obtenerNivelEducativo(jefeSE.getValor().getClave());
        ResultadoEJB<VariablesProntuario> nivelD = ejbRegistro.obtenerNivelEducativo(director.getValor().getClave());
       ResultadoEJB<VariablesProntuario> cargoSE = ejbRegistro.obtenerCargo(jefeSE.getValor().getClave());
       ResultadoEJB<VariablesProntuario> cargoD = ejbRegistro.obtenerCargo(director.getValor().getClave());
        String fecha = sdf1.format(r.getFechaEmision());
        
        gExcel.escribirDatosWordCE(nivelSE.getValor(), nivelD.getValor(), cargoSE.getValor(), cargoD.getValor(), dtoEstudiante.getValor(), au.getValor(), fecha, director.getValor(),
                r, listCompetenciasEspecificas.getValor(), listCompetenciasGenericas.getValor());
        gExcel.escribirWord(carpeta2, subCarpeta, wordDestino);
        //gExcel.abrirArchivo(carpeta2, subCarpeta, wordDestino);
        ////System.out.println("Se obtuvo el documento word"+ rol.getNameWord());
        String ruta = gExcel.enviarLibro();
        Ajax.oncomplete("descargar('" + gExcel.enviarLibro() + "');");
    }
    
   public void consultarListaEstudiantes(){
       ResultadoEJB<List<Estudiante>> lista = ejbRegistro.obtenerEstudiante();
       
//       ResultadoEJB<List<Estudiante>> listaIn = ejbRegistro.obtenerEstudiante("Reincorporación otra generación");
//       ResultadoEJB<List<Estudiante>> listaRein = ejbRegistro.obtenerEstudiante("Reinscripción");
       if(!lista.getCorrecto()) return;
       rol.setEstudianteRA(lista.getValor());
       
//       rol.setEstudianteREIN(listaIn.getValor());
//       rol.setEstudianteREIN(listaRein.getValor());
   }
   
   public void realizarRegistroMultiple(){
       rol.getEstudiantesRegistro().forEach(this::guardarRegistroEstudiante);
       consultarListaEstudiantes();
       obtenerHistoricoDeRegistro();
   }
   
   public void guardarRegistroEstudiante(Estudiante estudiante){
       ResultadoEJB<DtoEstudiante> resEstudiante = ejbRegistro.validarEstudiante(estudiante.getMatricula());
        if(!resEstudiante.getCorrecto()) return;
        rol.setEstudianteDtoRegistro(resEstudiante.getValor());
        bloquear =  Boolean.TRUE;
        desbloquear =  Boolean.FALSE;
        //System.out.println("Generación" + rol.getEstudianteDtoRegistro().getInscripcionActiva().getGeneracion().getInicio() +"-"+ rol.getEstudianteDtoRegistro().getInscripcionActiva().getGeneracion().getInicio());
        rol.setHayRegistro(verificarRegistroExistente(rol.getEstudianteDtoRegistro().getInscripcionActiva().getInscripcion().getMatricula(), rol.getGeneracion()));
        //System.out.println("Hay registro?"+ rol.getHayRegistro());
        if(!rol.getHayRegistro().equals(Boolean.TRUE)){
            //System.out.println("No hay registro");
            //rol.setOcultarPanelRegistro(Boolean.FALSE);
            rol.setMatricula(rol.getEstudianteDtoRegistro().getInscripcionActiva().getInscripcion().getMatricula());
            String generacion = rol.getEstudianteDtoRegistro().getInscripcionActiva().getGeneracion().getInicio()+"-"+rol.getEstudianteDtoRegistro().getInscripcionActiva().getGeneracion().getFin();
            //System.out.println("Se inicializan las variables:" + rol.getMatricula()+"-"+ generacion);
            ResultadoEJB<RegistroEgresadosTerminacionEstudios> registroUltimoPorGeneracion = ejbRegistro.obtenerUltimoRegistro(generacion);
            if(registroUltimoPorGeneracion.getValor().getIdRegistro() == null){
                //System.out.println("No hay registro con la generación del estudiante");
                rol.setGeneracion(generacion);
                rol.setFechaEmision(new Date());
            }else{
                //System.out.println("Ya hay un registro con la generación del estudiante");
                rol.setGeneracion(registroUltimoPorGeneracion.getValor().getGeneracion());
                rol.setFechaEmision(registroUltimoPorGeneracion.getValor().getFechaEmision());
            }
            ResultadoEJB<RegistroEgresadosTerminacionEstudios> registroUltimo = ejbRegistro.obtenerUltimoRegistro();
            if(registroUltimo.getValor().getIdRegistro() != null){
                //System.out.println("Se obtuvo el ultimo registro");
                RegistroEgresadosTerminacionEstudios rete = registroUltimo.getValor();
                rol.setFolio(1 + rete.getFolio());
                if(rete.getFoja() == 192){
                    //System.out.println("El registro seleccionado muestra que el numero de foja ha alcanzado el limite");
                    rol.setLibro(1 + rete.getLibro());
                    rol.setFoja(1);
                }else{
                    //System.out.println("El libro aun no ha alcanzado el numero de fojas requeridos");
                    rol.setLibro(rete.getLibro());
                    rol.setFoja(1 + rete.getFoja());
                }
            }else{
                //System.out.println("No hay ultimo registro registro");
                rol.setFolio(1);
                rol.setLibro(1);
                rol.setFoja(1);
            }
            ResultadoEJB<RegistroEgresadosTerminacionEstudios> res;
            rol.setRegistroEgresadosTerminacionEstudio(new RegistroEgresadosTerminacionEstudios());
            rol.getRegistroEgresadosTerminacionEstudio().setIdEstudiante(estudiante);
            rol.getRegistroEgresadosTerminacionEstudio().setGeneracion(rol.getGeneracion());
            rol.getRegistroEgresadosTerminacionEstudio().setFolio(rol.getFolio());
            rol.getRegistroEgresadosTerminacionEstudio().setLibro(rol.getLibro());
            rol.getRegistroEgresadosTerminacionEstudio().setFoja(rol.getFoja());
            rol.getRegistroEgresadosTerminacionEstudio().setFechaEmision(rol.getFechaEmision());
            res = ejbRegistro.guardarRegistro(rol.getRegistroEgresadosTerminacionEstudio(), estudiante, Operacion.PERSISTIR);
            mostrarMensajeResultadoEJB(res);
        }
   }
   
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registro de egresados por terminación de estudios";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(//System.out::println);
        return mostrar(request, map.containsValue(valor));

    }
    
}
