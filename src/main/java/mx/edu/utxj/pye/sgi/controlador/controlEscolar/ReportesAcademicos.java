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
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAprovechamientoEscolar;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAprovechamientoEscolarEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDistribucionMatricula;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteIrregular;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMatricula;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReportePlaneacionDocente;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReprobacionAsignatura;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ReportesAcademicosRolMultiple;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoTramitarBajas;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoValidacionesBaja;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbEventoEscolar;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReportesAcademicos;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class ReportesAcademicos extends ViewScopedRol implements Desarrollable{
    @Getter @Setter ReportesAcademicosRolMultiple rol;
    
    @EJB EjbReportesAcademicos ejb;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @EJB EjbRegistroBajas ejbRegistroBajas;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por dirección de carrera<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
@PostConstruct
    public void init(){
    if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.REPORTES_ACADEMICOS);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarRolesReportesAcademicos(logon.getPersonal().getClave());//validar si es director
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ReportesAcademicosRolMultiple(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setUsuario(usuario);
            
            if(rol.getUsuario().getAreaOperativa().getArea()==10 || rol.getUsuario().getAreaOperativa().getArea()== 9){
                rol.setHabilitarListadoPromedios(true);
            }else{
                rol.setHabilitarListadoPromedios(false);
            }
            
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            
            rol.getInstrucciones().add("Seleccione periodo, nivel y programa educativo.");
            rol.getInstrucciones().add("Seleccione el reporte que desea consultar.");
            rol.getInstrucciones().add("Se visualizará el reporte correspondiente.");
            rol.getInstrucciones().add("Dar clic en el botón de Descargar Reporte en Excel, para generar un archivo que contiene la información de los reportes disponibles.");
           
            rol.setPeriodoActivo(ejbEventoEscolar.getPeriodoActual().getPeriodo());
            rol.setTipoBusqueda("busquedaPrograma");
            listaPeriodosEscolares();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "reportes academicos";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de generaciones en los que existen eventos de estadía registrados
     */
    public void listaPeriodosEscolares(){
        ResultadoEJB<List<PeriodosEscolares>> res =  ejb.getPeriodosEscolaresRegistro();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setPeriodos(res.getValor());
                rol.setPeriodo(rol.getPeriodos().get(0));
                listaNivelesPeriodo();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de nivele educativos registradas en la generación seleccionada
     */
    public void listaNivelesPeriodo(){
        if(rol.getPeriodo()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejb.getNivelesPeriodosEscolares(rol.getPeriodo());
        if(res.getCorrecto()){
            rol.setNiveles(res.getValor());
            rol.setNivel(rol.getNiveles().get(0));
            listaProgramasEducativosNivel();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de nivele educativos registradas en la generación seleccionada
     */
    public void listaProgramasEducativosNivel(){
        if(rol.getNivel()== null) return;
        ResultadoEJB<List<AreasUniversidad>> res = ejb.getProgramasEducativosNivel(rol.getPeriodo(), rol.getNivel());
        if(res.getCorrecto()){
            if((rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==18 && rol.getUsuario().getPersonal().getAreaSuperior()==2) || (rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==48  && rol.getUsuario().getPersonal().getAreaSuperior()==2)){
                rol.setProgramas(res.getValor().stream().filter(p->p.getAreaSuperior().equals(rol.getUsuario().getAreaOperativa().getArea())).collect(Collectors.toList()));
            }else if(rol.getUsuario().getPersonal().getAreaOperativa()==10 || rol.getUsuario().getPersonal().getAreaOperativa()==6 || rol.getUsuario().getPersonal().getAreaOperativa()==9 || rol.getUsuario().getPersonal().getAreaOperativa()==23){
                rol.setProgramas(res.getValor());
            }
            if(!rol.getProgramas().isEmpty()){
                rol.setPrograma(rol.getProgramas().get(0));
            }else{
                rol.setPrograma(new AreasUniversidad());
            }
            listaReportes();
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de reportes de estadía
     */
    public void listaReportes(){
        List<String> listaReportes = new ArrayList<>();
        
        if((rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==18 && rol.getUsuario().getPersonal().getAreaSuperior()==2) || (rol.getUsuario().getPersonal().getCategoriaOperativa().getCategoria()==48  && rol.getUsuario().getPersonal().getAreaSuperior()==2) || rol.getUsuario().getPersonal().getAreaOperativa()==23){
            listaReportes.add("Estudiantes irregulares");
            listaReportes.add("Planeación docente");
        }
        else if(rol.getUsuario().getPersonal().getAreaOperativa()==10 || rol.getUsuario().getPersonal().getAreaOperativa()==6 || rol.getUsuario().getPersonal().getAreaOperativa()==9){
            listaReportes.add("Estudiantes irregulares");
            listaReportes.add("Planeación docente");
            listaReportes.add("Aprovechamiento escolar");
            listaReportes.add("Aprovechamiento escolar por estudiante");
            listaReportes.add("Reprobación por asignatura");
            listaReportes.add("Matricula");
            listaReportes.add("Distribución de matricula");
            listaReportes.add("Deserción académica");
        }
        
        rol.setReportes(listaReportes);
        rol.setReporte(rol.getReportes().get(0));
        generarReportes();
    }
    
    /**
     * Permite generar el reporte seleccionado dependiendo la opción seleccionada
     */
    public void generarReportes(){
        if("Estudiantes irregulares".equals(rol.getReporte())){
           generarEstudiantesIrregulares();
        }else if("Planeación docente".equals(rol.getReporte())){
           generarPlaneacionDocente();
        }else if("Aprovechamiento escolar".equals(rol.getReporte())){
           generarAprovechamientoEscolar();
        }else if("Aprovechamiento escolar por estudiante".equals(rol.getReporte())){
           generarListaAprovechamientoEscolar();
        }else if("Reprobación por asignatura".equals(rol.getReporte())){
           generarReprobacionAsignatura();
        }else if("Matricula".equals(rol.getReporte())){
           generarMatricula();
        }else if("Distribución de matricula".equals(rol.getReporte())){
           generarDistribucionMatricula();  
        }else if("Deserción académica".equals(rol.getReporte())){
           generarDesercionAcademica();
        }
    }
    
    /**
     * Permite que al cambiar o seleccionar un periodo se pueda actualizar la lista de ´niveles educativos
     * @param e Evento del cambio de valor
     */
    public void cambiarPeriodo(ValueChangeEvent e){
        if(e.getNewValue() instanceof PeriodosEscolares){
            PeriodosEscolares periodo = (PeriodosEscolares)e.getNewValue();
            rol.setPeriodo(periodo);
            listaNivelesPeriodo();
            generarReportes();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la lista de programas educativos
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivel(nivel);
            listaProgramasEducativosNivel();
            generarReportes();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un programa educativo se actualice la información
     * @param e Evento del cambio de valor
     */
    public void cambiarProgramaEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  AreasUniversidad){
            AreasUniversidad programa = (AreasUniversidad)e.getNewValue();
            rol.setPrograma(programa);
            generarReportes();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un reporte se pueda actualizar la información
     * @param e Evento del cambio de valor
     */
    public void cambiarReporte(ValueChangeEvent e){
        if(e.getNewValue() instanceof  String){
            String reporte = (String)e.getNewValue();
            rol.setReporte(reporte);
            generarReportes();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite seleccionar y cambiar valor del tipo de búsqueda
     * @param e Evento del cambio de valor
     */
    public void cambiarTipoBusqueda(ValueChangeEvent e){
        rol.setTipoBusqueda((String)e.getNewValue());
        generarReportes();
        Ajax.update("frm");
    }
    
    /**
     * Método que permite descargar en excel el reporte correspondiente del periodo seleccionado
     * @throws java.io.IOException
     */
     public void descargarReporteAcademico() throws IOException, Throwable{
         if ("Estudiantes irregulares".equals(rol.getReporte())) {
             File f = new File(ejb.getReporteEstIrregulares(rol.getPeriodo(), rol.getUsuario()));
             Faces.sendFile(f, true);
         } else if ("Planeación docente".equals(rol.getReporte())) {
             File f = new File(ejb.getReportePlaneacionDoc(rol.getPeriodo(), rol.getUsuario()));
             Faces.sendFile(f, true);
         } else if ("Aprovechamiento escolar".equals(rol.getReporte())) {
             File f = new File(ejb.getReporteAprovEscolar(rol.getPeriodo(), rol.getUsuario().getPersonal()));
             Faces.sendFile(f, true);
         } else if ("Aprovechamiento escolar por estudiante".equals(rol.getReporte())) {
             File f = new File(ejb.getReporteAprovEstudiantes(rol.getPeriodo(), rol.getUsuario().getPersonal()));
             Faces.sendFile(f, true);
         } else if ("Reprobación por asignatura".equals(rol.getReporte())) {
             File f = new File(ejb.getReporteRepAsignatura(rol.getPeriodo(), rol.getUsuario().getPersonal()));
             Faces.sendFile(f, true);
         } else if ("Matricula".equals(rol.getReporte())) {
             File f = new File(ejb.getReporteMatricula(rol.getPeriodo(), rol.getUsuario().getPersonal()));
             Faces.sendFile(f, true);
         } else if ("Distribución de matricula".equals(rol.getReporte())) {
             File f = new File(ejb.getReporteDistMatricula(rol.getPeriodo(), rol.getUsuario().getPersonal()));
             Faces.sendFile(f, true);
         } else if ("Deserción académica".equals(rol.getReporte())) {
             File f = new File(ejb.getReporteDesercionAcad(rol.getPeriodo(), rol.getUsuario().getPersonal()));
             Faces.sendFile(f, true);
         }
    }
     
    /**
     * Método que permite descargar en excel los reportes del periodo seleccionado
     * @throws java.io.IOException
     */
     public void descargarPromediosMateriaEstudiante() throws IOException, Throwable{
        if(rol.getUsuario().getPersonal().getAreaOperativa()==10 || rol.getUsuario().getPersonal().getAreaOperativa()==6 || rol.getUsuario().getPersonal().getAreaOperativa()==9){     
            File f = new File(ejb.getReportePromediosMateriaEstudiante(rol.getPeriodo(), rol.getUsuario().getPersonal()));
            Faces.sendFile(f, true);
        }
    }
    
    
    /**
     * Permite generar el listado de estudiantes con registro de baja
     */
    public void generarDesercionAcademica(){
        if(rol.getTipoBusqueda().equals("busquedaPrograma")){
            ResultadoEJB<List<DtoTramitarBajas>> res1 = ejbRegistroBajas.obtenerListaBajasPeriodo(rol.getPeriodo());
            if(res1.getCorrecto()){
                ResultadoEJB<List<DtoTramitarBajas>> res2 = ejbRegistroBajas.obtenerListaBajasProgramaEducativo(res1.getValor(), rol.getPrograma());
                if(res1.getCorrecto()){
                    rol.setDesercionAcademica(res2.getValor().stream().filter(p->p.getDtoRegistroBaja().getRegistroBaja().getValidada()==1).collect(Collectors.toList()));
                    Ajax.update("tbDesercionAcademica");
                }else mostrarMensajeResultadoEJB(res2);
            }else mostrarMensajeResultadoEJB(res1);
        }else{
            ResultadoEJB<List<DtoTramitarBajas>> res = ejbRegistroBajas.obtenerListaBajasPeriodo(rol.getPeriodo());
            if(res.getCorrecto()){
                rol.setDesercionAcademica(res.getValor().stream().filter(p->p.getDtoRegistroBaja().getRegistroBaja().getValidada()==1).collect(Collectors.toList()));
                Ajax.update("tbDesercionAcademica");
            }else mostrarMensajeResultadoEJB(res);
        }
    }
    
     /**
     * Permite verificar el status de la baja
     * @param baja Registro de la baja
     * @return valor boolean según sea el caso
     */
    public DtoValidacionesBaja consultarStatus(DtoTramitarBajas baja){
        rol.setDtoValidacionesBaja(ejbRegistroBajas.buscarValidacionesBaja(baja.getDtoRegistroBaja().getRegistroBaja()).getValor());
        return rol.getDtoValidacionesBaja();
    }
    
    /**
     * Permite generar el listado de matricula activa (situación académica regular)
     */
    public void generarMatricula(){
        if(rol.getTipoBusqueda().equals("busquedaPrograma")){
            ResultadoEJB<List<DtoMatricula>> res = ejb.getMatriculaPrograma(rol.getPeriodo(), rol.getPrograma());
            if(res.getCorrecto()){
             rol.setMatricula(res.getValor());
             Ajax.update("tbMatricula");
            }else mostrarMensajeResultadoEJB(res);
        }else{
            ResultadoEJB<List<DtoMatricula>> res = ejb.getMatricula(rol.getPeriodo(), rol.getUsuario().getPersonal());
            if(res.getCorrecto()){
             rol.setMatricula(res.getValor());
             Ajax.update("tbMatricula");
            }else mostrarMensajeResultadoEJB(res);
        }
    }
    
    /**
     * Permite generar distribución de matricula por grado
     */
    public void generarDistribucionMatricula(){
        if(rol.getTipoBusqueda().equals("busquedaPrograma")){
            ResultadoEJB<List<DtoDistribucionMatricula>> res = ejb.getDistribucionMatriculaPrograma(rol.getPeriodo(), rol.getPrograma());
            if(res.getCorrecto()){
                rol.setDistribucionMatricula(res.getValor());
                Ajax.update("tbDistribucionMatricula");
            }else mostrarMensajeResultadoEJB(res);
       }else{
           ResultadoEJB<List<DtoDistribucionMatricula>> res = ejb.getDistribucionMatricula(rol.getPeriodo(), rol.getUsuario().getPersonal());
            if(res.getCorrecto()){
                rol.setDistribucionMatricula(res.getValor());
                Ajax.update("tbDistribucionMatricula");
            }else mostrarMensajeResultadoEJB(res);
        }
    }
    
    /**
     * Permite generar reporte de planeación cuatrimestral docente 
     */
    public void generarPlaneacionDocente(){
        if(rol.getTipoBusqueda().equals("busquedaPrograma")){
            ResultadoEJB<List<DtoReportePlaneacionDocente>> res = ejb.getPlaneacionesDocentePrograma(rol.getPeriodo(), rol.getPrograma());
            if(res.getCorrecto()){
                if(rol.getUsuario().getAreaOperativa().getArea()==23){
                    rol.setPlaneacionDocente(res.getValor().stream().filter(p->p.getPlanEstudioMateria().getIdMateria().getNombre().contains("Inglés") || p.getPlanEstudioMateria().getIdMateria().getNombre().contains("Francés")).collect(Collectors.toList()));
                }else{
                    rol.setPlaneacionDocente(res.getValor());
                }
                Ajax.update("tbPlaneacionDocente");
            }else mostrarMensajeResultadoEJB(res);
        }else{
            ResultadoEJB<List<DtoReportePlaneacionDocente>> res = ejb.getPlaneacionesDocente(rol.getPeriodo(), rol.getUsuario().getPersonal());
            if(res.getCorrecto()){
                    rol.setPlaneacionDocente(res.getValor());
                Ajax.update("tbPlaneacionDocente");
            }else mostrarMensajeResultadoEJB(res);
        }
    }
    
     /**
     * Permite generar aprovechamiento escolar por asignatura
     */
    public void generarAprovechamientoEscolar(){
        if(rol.getTipoBusqueda().equals("busquedaPrograma")){
            ResultadoEJB<List<DtoAprovechamientoEscolar>> res = ejb.getAprovechamientoEscolarPrograma(rol.getPeriodo(), rol.getPrograma());
            if(res.getCorrecto()){
                rol.setAprovechamientoEscolar(res.getValor());
                Ajax.update("tbAprovechamientoEscolar");
            }else mostrarMensajeResultadoEJB(res);
        }else{
            ResultadoEJB<List<DtoAprovechamientoEscolar>> res = ejb.getAprovechamientoEscolar(rol.getPeriodo(), rol.getUsuario().getPersonal());
            if(res.getCorrecto()){
                rol.setAprovechamientoEscolar(res.getValor());
                Ajax.update("tbAprovechamientoEscolar");
            }else mostrarMensajeResultadoEJB(res);
        }
    }
    
    /**
     * Permite generar lista de matricula activa con promedio cuatrimestral
     */
    public void generarListaAprovechamientoEscolar(){
        if(rol.getTipoBusqueda().equals("busquedaPrograma")){
            ResultadoEJB<List<DtoAprovechamientoEscolarEstudiante>> res = ejb.getListaAprovechamientoEscolarPrograma(rol.getPeriodo(), rol.getPrograma());
            if(res.getCorrecto()){
                rol.setListaAprovechamientoEscolar(res.getValor());
                Ajax.update("tbListaAprovechamientoEscolar");
            }else mostrarMensajeResultadoEJB(res);
        }else{
            ResultadoEJB<List<DtoAprovechamientoEscolarEstudiante>> res = ejb.getListaAprovechamientoEscolar(rol.getPeriodo(), rol.getUsuario().getPersonal());
            if(res.getCorrecto()){
                rol.setListaAprovechamientoEscolar(res.getValor());
                Ajax.update("tbListaAprovechamientoEscolar");
            }else mostrarMensajeResultadoEJB(res);
        }
    }
    
    /**
     * Permite generar reprobación por asignatura
     */
    public void generarReprobacionAsignatura(){
        if(rol.getTipoBusqueda().equals("busquedaPrograma")){
            ResultadoEJB<List<DtoReprobacionAsignatura>> res = ejb.getReprobacionAsignaturaPrograma(rol.getPeriodo(), rol.getPrograma());
            if(res.getCorrecto()){
                rol.setReprobacionAsignatura(res.getValor());
                Ajax.update("tbReprobacionAsignatura");
            }else mostrarMensajeResultadoEJB(res);
        }else{
            ResultadoEJB<List<DtoReprobacionAsignatura>> res = ejb.getReprobacionAsignatura(rol.getPeriodo(), rol.getUsuario().getPersonal());
            if(res.getCorrecto()){
                rol.setReprobacionAsignatura(res.getValor());
                Ajax.update("tbReprobacionAsignatura");
            }else mostrarMensajeResultadoEJB(res);
        }
    }
    
    /**
     * Permite generar listado de estudiantes irregulares
     */
    public void generarEstudiantesIrregulares(){
        if(rol.getTipoBusqueda().equals("busquedaPrograma")){
            ResultadoEJB<List<DtoEstudianteIrregular>> res = ejb.getEstudiantesIrregularesPrograma(rol.getPeriodo(), rol.getPrograma());
            if(res.getCorrecto()){
                if(rol.getUsuario().getAreaOperativa().getArea()==23){
                    rol.setEstudiantesIrregulares(res.getValor().stream().filter(p->p.getPlanEstudioMateria().getIdMateria().getNombre().contains("Inglés") || p.getPlanEstudioMateria().getIdMateria().getNombre().contains("Francés")).collect(Collectors.toList()));
                }else{
                    rol.setEstudiantesIrregulares(res.getValor());
                }
                Ajax.update("tbEstudiantesIrregulares");
            }else mostrarMensajeResultadoEJB(res);
        }else{
            ResultadoEJB<List<DtoEstudianteIrregular>> res = ejb.getEstudiantesIrregulares(rol.getPeriodo(), rol.getUsuario().getPersonal());
            if(res.getCorrecto()){
                    rol.setEstudiantesIrregulares(res.getValor());
                Ajax.update("tbEstudiantesIrregulares");
            }else mostrarMensajeResultadoEJB(res);
        }
    }
   
}

