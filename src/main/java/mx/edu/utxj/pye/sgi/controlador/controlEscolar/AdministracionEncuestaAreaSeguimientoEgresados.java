/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteEncuesta;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SeguimientoEncuestaRolSeguimientoEgresados;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAdmEncuestaSeguimientoEgresados;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaEmpleadores;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaSeguimientoEgresados;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaEmpleadores;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaSeguimientoEgresados;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.util.GenerateExcel;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author Planeacion
 */
@Named
@ViewScoped
public class AdministracionEncuestaAreaSeguimientoEgresados  extends ViewScopedRol implements Desarrollable{
    
    @Getter @Setter private SeguimientoEncuestaRolSeguimientoEgresados rol;
    @Getter private Boolean cargado = false;
    @Getter private Boolean tieneAcceso = false;
    
    @EJB EjbPropiedades ep;
    @EJB EjbAdmEncuestaSeguimientoEgresados ejb;
    @Inject LogonMB logonMB;
    @Inject GenerateExcel gExcel;
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_ENCUESTAS_SEGUIMIENTO_EGRESADOS);
            
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarResponsableAdministracion(logonMB.getPersonal().getClave());
            
            if(!resAcceso.getCorrecto()){mostrarMensajeResultadoEJB(resAcceso);return;}
            
            Filter<PersonalActivo> filtro = resAcceso.getValor();
            PersonalActivo responsable = filtro.getEntity();
            rol = new SeguimientoEncuestaRolSeguimientoEgresados(filtro, responsable);
            tieneAcceso = rol.tieneAcceso(responsable);
            if(!tieneAcceso){return;}
            rol.setPersonalActivo(responsable);
            
            
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setEstaActiva(Boolean.FALSE);
            rol.setActivarInput(Boolean.FALSE);
        } catch (Exception e) {
        }
    }
    
    public void obtenerEvaluacion(){
        if(rol.getPeriodo() == null) return;
        if(rol.getTipoEvaluacion().equals("")) return;
        ResultadoEJB<Evaluaciones> resE = ejb.obtenerEncuesta(rol.getPeriodo(), rol.getTipoEvaluacion());
        if(resE.getCorrecto()){
            rol.setEvaluacionA(resE.getValor());
            rol.setFechaIn(rol.getEvaluacionA().getFechaInicio());
            rol.setFechaFin(rol.getEvaluacionA().getFechaFin());
            rol.setEstaActiva(estaActiva(rol.getEvaluacionA()));
        }else{
            rol.setEvaluacionA(new Evaluaciones());
            rol.setFechaIn(null);
            rol.setFechaFin(null);
            rol.setEstaActiva(Boolean.FALSE);
        }
    }
    
    public Boolean estaActiva(Evaluaciones evaluacion){
        ResultadoEJB<Boolean> resBol = ejb.verificarEncuestaActiva(evaluacion.getEvaluacion(), evaluacion.getTipo());
        return resBol.getValor();
    }
    
    public String dateToString(Date fecha){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(fecha);
    }
    
    public void guardarNuevoRegistro(){
        //System.out.println("Periodo:" + rol.getPeriodo() +" Fecha inicio: " + rol.getFechaIn() +" Fecha fin: "+ rol.getFechaFin());
        rol.getPeriodo();
        rol.getTipoEvaluacion();
        rol.getFechaIn();
        rol.getFechaFin();
        ResultadoEJB<Evaluaciones> resEvaluacion = ejb.guardarEncuesta(rol.getPeriodo(), rol.getTipoEvaluacion(), rol.getFechaIn(), rol.getFechaFin(), Operacion.PERSISTIR);
        
        if(resEvaluacion.getCorrecto()){
            mostrarMensaje("Se ha realizado el registro");
            rol.setEvaluacionA(resEvaluacion.getValor());
            rol.setEstaActiva(estaActiva(rol.getEvaluacionA()));
        }else{
            mostrarMensajeError("Error al intentar la operación");
        }
    }
    
    public void actualizarFechasEncuesta(){
        //System.out.println("Periodo:" + rol.getPeriodo() +" Fecha inicio: " + rol.getFechaIn() +" Fecha fin: "+ rol.getFechaFin());
        rol.getPeriodo();
        rol.getTipoEvaluacion();
        rol.getFechaIn();
        rol.getFechaFin();
        ResultadoEJB<Evaluaciones> resEvaluacion = ejb.guardarEncuesta(rol.getPeriodo(), rol.getTipoEvaluacion(), rol.getFechaIn(), rol.getFechaFin(), Operacion.ACTUALIZAR);
        if(resEvaluacion.getCorrecto()){
            mostrarMensaje("Se ha actualizado la información.");
            rol.setEvaluacionA(resEvaluacion.getValor());
            rol.setEstaActiva(estaActiva(rol.getEvaluacionA()));
        }else{
            mostrarMensajeError("Error al intentar la operación");
        }
    }
    
    public void buscarEstudiante(){
        rol.getMatricula(); 
        //rol.setActivarBtn(Boolean.TRUE);
        if(rol.getPeriodoB() == null) {mostrarMensajeError("Debe seleccionar la generación, para poder realizar la búsqueda"); return;}
        ResultadoEJB<Evaluaciones> resE = ejb.obtenerEncuesta(rol.getPeriodoB(), "Encuesta Seguimiento de Egresados");
        if(!resE.getCorrecto()) return;
        Integer periodo = resE.getValor().getPeriodo();
        rol.setEvaluacionR(resE.getValor().getEvaluacion());
        //System.out.println("Periodo" + periodo);
        if(rol.getMatricula().equals("")) {mostrarMensajeError("Para realizar la busqueda, debe ingresar la matrícula."); return;}
        ResultadoEJB<DtoEstudianteEncuesta.DtoEstudianteControlEscolar> resCE = ejb.obtenerInformacionEstudiante(Integer.parseInt(rol.getMatricula()), periodo);
        if(resCE.getCorrecto()){
            rol.setDtoEstudianteInf(pack(resCE.getValor()));
            rol.setMatriculaR(rol.getDtoEstudianteInf().getMatricula());
            //System.out.println("Control Escolar: Evaluación:" + rol.getEvaluacionR() +" Evaluador:"+ rol.getMatriculaR());
            rol.setMatricula("");
        }
        ResultadoEJB<DtoEstudianteEncuesta.DtoEstudianteSaiiut> resSA = ejb.obtenerInformacionEstudiante(rol.getMatricula(), periodo);
        if(resSA.getCorrecto()){
            rol.setDtoEstudianteInf(pack(resSA.getValor()));
            rol.setMatriculaR(rol.getDtoEstudianteInf().getMatricula());
            //System.out.println("SAIIUT: Evaluación:" + rol.getEvaluacionR() +" Evaluador:"+ rol.getMatriculaR());
            rol.setMatricula("");
        }
    }
    
    public void realizarRegistro(){
        rol.getMatriculaR();
        //System.out.println("Evaluacion:" + rol.getEvaluacionR()+" Evaluador:"+ rol.getMatriculaR());
        ResultadoEJB<EncuestaSeguimientoEgresados> resEncuesta = ejb.realizarRegistro(rol.getMatriculaR(), rol.getEvaluacionR(), Operacion.PERSISTIR);
        mostrarMensaje(resEncuesta.getMensaje());
        rol.setDtoEstudianteInf(new DtoEstudianteEncuesta.DtoEstudianteInformacion());
        rol.setMatriculaR(0);
        rol.setEvaluacionR(0);
        rol.setActivarBtn(Boolean.FALSE);
    }
    
    public DtoEstudianteEncuesta.DtoEstudianteInformacion pack(DtoEstudianteEncuesta.DtoEstudianteControlEscolar dtoCE){
        return new DtoEstudianteEncuesta.DtoEstudianteInformacion(
                dtoCE.getEstudiante().getMatricula(), 
                dtoCE.getPersona().getNombre(), 
                dtoCE.getPersona().getApellidoPaterno(), 
                dtoCE.getPersona().getApellidoMaterno(), 
                dtoCE.getGrupo().getLiteral().toString(), 
                Short.parseShort(String.valueOf(dtoCE.getGrupo().getGrado())), 
                dtoCE.getGrupo().getPlan().getDescripcion());
    }
    
    public DtoEstudianteEncuesta.DtoEstudianteInformacion pack(DtoEstudianteEncuesta.DtoEstudianteSaiiut dtoSA){
        return new DtoEstudianteEncuesta.DtoEstudianteInformacion(
                Integer.parseInt(dtoSA.getAlumno().getMatricula()), 
                dtoSA.getPersona().getNombre(), 
                dtoSA.getPersona().getApellidoPat(), 
                dtoSA.getPersona().getApellidoMat(), 
                dtoSA.getGrupo().getIdGrupo(), 
                dtoSA.getAlumno().getGradoActual(),
                dtoSA.getCarrera().getNombre());
    }
    
    public List<DtoEstudianteEncuesta.DtoGeneraciones> obtenerListaGeneraciones(){
        ResultadoEJB<List<DtoEstudianteEncuesta.DtoGeneraciones>> resLista = ejb.obtenerListaGeneraciones();
        if(!resLista.getCorrecto()) return Collections.EMPTY_LIST;
        rol.setTipoEvaluacion("");
        rol.setFechaIn(new Date());
        rol.setFechaFin(new Date());
        return resLista.getValor();
    }
    
    public List<Estudiante> obtenerEstudiantes(){
        ResultadoEJB<List<Estudiante>> resLista = ejb.obtenerLista(rol.getEvaluacionA());
        if(!resLista.getCorrecto()) return Collections.EMPTY_LIST;
        return resLista.getValor();
    }
    
    public void obtenerListaEstudiantes(){
        ////System.out.println("Periodo: "+ rol.getPeriodoB());
        ResultadoEJB<List<DtoEstudianteEncuesta.DtoEstudiante>> resList = ejb.obtenerEstudiante(rol.getPeriodoB());
        if(!resList.getCorrecto()) return;
        rol.setListaDtoE(resList.getValor());
        rol.setActivarBtn2(Boolean.TRUE);
    }
    
    public void realizarRegistroMultiple(){
        ResultadoEJB<Evaluaciones> resE = ejb.obtenerEncuesta(rol.getPeriodoB(), "Encuesta Seguimiento de Egresados");
        if(!resE.getCorrecto()) return;
        rol.setEvaluacionA(resE.getValor());
        //System.out.println("Evaluacion a registrar" + rol.getEvaluacionA());
        //System.out.println("Lista de estudiantes a registrar" + rol.getListaRegistro());
        rol.getListaDtoE().forEach(dto -> {
            ResultadoEJB<EncuestaSeguimientoEgresados> resEncuesta = ejb.realizarRegistro(dto.getMatricula(), rol.getEvaluacionA().getEvaluacion(), Operacion.PERSISTIR);
            
        });
        mostrarMensaje("Se ha concluido con el registro");
        
    }
    
    public void verificarEvaluacion(){
        if(rol.getPeriodoB() == null) return;
        ResultadoEJB<Evaluaciones> resE = ejb.obtenerEncuesta(rol.getPeriodoB(), "Encuesta Seguimiento de Egresados");
        if(!resE.getCorrecto()) { mostrarMensajeError("No hay registro de evaluación en la Generación seleccionada, para continuar con el "
                + "proceso debe realizar la apertura de evaluación.");}
        rol.setActivarInput(Boolean.TRUE);
        
    }
    
    public void consultarResultadosEvaluacion(){
        rol.getPeriodoConsulta();
        ResultadoEJB<Evaluaciones> resBusqueda = ejb.obtenerEncuesta(rol.getPeriodoConsulta(), "Encuesta Seguimiento de Egresados");
        if(!resBusqueda.getCorrecto()){mostrarMensajeError("No hay evaluación asignada para la Generación seleccionada."); return;}
        Evaluaciones evaluacion = resBusqueda.getValor();
        ResultadoEJB<List<DtoEstudianteEncuesta.DtoInformacionResultados>> resListaRes = ejb.obtenerListaResultadosEJB(evaluacion);
        if(!resListaRes.getCorrecto()) return;
        rol.setListaDtoResultados(resListaRes.getValor());
        rol.setActivarBtn(Boolean.TRUE);
    }
    
    public void consultarResultadosEmpleadores(){
        rol.getPeriodoConsulta2();
        rol.getTipoEvaluacion2();
        ResultadoEJB<Evaluaciones> resBusqueda = ejb.obtenerEncuesta(rol.getPeriodoConsulta2(), rol.getTipoEvaluacion2());
        if(!resBusqueda.getCorrecto()){mostrarMensajeError("No hay evaluación asignada para la Generación seleccionada."); return;}
        Evaluaciones evaluacion = resBusqueda.getValor();
        ResultadoEJB<List<DtoEstudianteEncuesta.DtoInformacionResultados2>> resListaRes = ejb.obtenerListaResultadosEmpleadores(evaluacion);
        if(!resListaRes.getCorrecto()) return;
        rol.setListaDtoResultados2(resListaRes.getValor());
        rol.setActivarBtn2(Boolean.TRUE);
    }
    
    public void descargarResultados(){
//        try{
            rol.setNombreExcel1("plantilla_reporte_encuesta_seguimiento_egresados.xlsx");
            gExcel.obtenerLibro(rol.getNombreExcel1());
            rol.setExcelSalida("resultados_encuesta_seguimiento_egresados_"+ServicioArchivos.sdf.format(new Date())+".xlsx");
            rol.setCarpeta("resultados_evaluacion");
            rol.setSubCarpeta("seguimiento_egresados");
            rol.setLibro(gExcel.getLibro().getSheetAt(0).getSheetName());
            for (int i = 0; i <= rol.getListaDtoResultados().size() - 1; i++) {
                DtoEstudianteEncuesta.DtoInformacionResultados dto = rol.getListaDtoResultados().get(i);
                String[] datos = {
                        dto.getDtoEstudiante().getMatricula().toString(), dto.getDtoEstudiante().getNombre(), dto.getDtoEstudiante().getCarrera(),
                                verificarValor(dto.getEncuesta().getNumTelFijo()), verificarValor(dto.getEncuesta().getNumTelMovil()),
                                verificarValor(dto.getEncuesta().getCorreoElectronico()), verificarValor(dto.getEncuesta().getRedSocial()), 
                                obtenerNivelEscolaridad(dto.getEncuesta().getUltNivelEscolaridad()), 
                                obtenerInstitucion(dto.getEncuesta().getInstitucionEstudias()), 
                                obtenerActividadActual(dto.getEncuesta().getActividadActual()), 
                                obtenerActividadEconomico(dto.getEncuesta().getActividadEconomico()), 
                                obtenerPuesto(dto.getEncuesta().getPuestoActual()), 
                                obtenerCoincideFormacion(dto.getEncuesta().getCoincideFormacionUt()), 
                                obtenerIngresoMensual(dto.getEncuesta().getIngresoMensual()), 
                                obtenerRegimen(dto.getEncuesta().getRegimenJuridico()), 
                                obtenerTamanioEmpresa(dto.getEncuesta().getTamanioEmpresa()), 
                                obtenerTiempo(dto.getEncuesta().getTiempoEmpleo()), 
                                obtenerMedios(dto.getEncuesta().getMedioTrabajo()), 
                                obtenerGenteCargo(dto.getEncuesta().getGenteCargo()), 
                                obtenerSituacion(dto.getEncuesta().getSituacionEconomica()), 
                                verificarValor(dto.getEncuesta().getFactorA()), 
                                verificarValor(dto.getEncuesta().getFactorB()), 
                                verificarValor(dto.getEncuesta().getFactorC()), 
                                verificarValor(dto.getEncuesta().getFactorD()), 
                                verificarValor(dto.getEncuesta().getFactorE()), 
                                verificarValor(dto.getEncuesta().getFactorF()), 
                                verificarValor(dto.getEncuesta().getNombreEmpresa()), verificarValor(dto.getEncuesta().getCalleNoEmpresa()), 
                                verificarValor(dto.getEncuesta().getColonia()), verificarValor(dto.getEncuesta().getMunicipio()), 
                                verificarValor(dto.getEncuesta().getEstado()), verificarValor(dto.getEncuesta().getTelefono()), 
                                verificarValor(dto.getEncuesta().getExtension()), verificarValor(dto.getEncuesta().getCorreoElectronicoEmpresa()), 
                                verificarValor(dto.getEncuesta().getAspectoA()), verificarValor(dto.getEncuesta().getAspectoB()), 
                                verificarValor(dto.getEncuesta().getAspectoC()), verificarValor(dto.getEncuesta().getAspectoD()), 
                                verificarValor(dto.getEncuesta().getAspectoE()), verificarValor(dto.getEncuesta().getAspectoF()), 
                                verificarValor(dto.getEncuesta().getAspectoG()), verificarValor(dto.getEncuesta().getAspectoH()), 
                                verificarValor(dto.getEncuesta().getAspectoI()), verificarValor(dto.getEncuesta().getAspectoJ()), 
                                verificarValor(dto.getEncuesta().getAspectoK()), verificarValor(dto.getEncuesta().getAspectoL()), 
                                verificarValor(dto.getEncuesta().getAspectoM()), obtenerExpectativa(dto.getEncuesta().getFormacionExpectativa()),
                                verificarValor(dto.getEncuesta().getCurso()), verificarValor(dto.getEncuesta().getComentarios()), verificarEstatus(dto.getEncuesta())};
                gExcel.escribirDatosExcel(datos, i, 3, rol.getLibro());
            }
            gExcel.escribirLibro(rol.getCarpeta(), rol.getSubCarpeta(), rol.getExcelSalida());
            String ruta = gExcel.enviarLibro();
            Ajax.oncomplete("descargar('" + gExcel.enviarLibro() + "');");
            //gExcel.eliminarLibroExistente(rol.getExcelSalida(), rol.getCarpeta(), rol.getSubCarpeta());
//        }catch (Exception e){mostrarExcepcion(e); }
    }
    
    public void descargarResultadosEmpleadores(){
            rol.setNombreExcel2("plantilla_reporte_encuesta_empleadores.xlsx");
            rol.setCarpeta("resultados_evaluacion");
            gExcel.obtenerLibro(rol.getNombreExcel2());
            rol.setExcelSalida("resultados_encuesta_empleadores_"+ServicioArchivos.sdf.format(new Date())+".xlsx");
            rol.setSubCarpeta("empleadores");
            rol.setLibro(gExcel.getLibro().getSheetAt(0).getSheetName());
            for (int i = 0; i <= rol.getListaDtoResultados2().size() - 1; i++) {
                DtoEstudianteEncuesta.DtoInformacionResultados2 dto = rol.getListaDtoResultados2().get(i);
                String[] datos = {
                        dto.getEncuesta().getEncuestado(), dto.getEncuesta().getNombreEgresado(), dto.getEncuesta().getCarrera(),
                                verificarValor(dto.getEncuesta().getNombreEmpresa()),
                                obtenerNivelSatisfaccion(dto.getEncuesta().getR6()), 
                                obtenerNivelSatisfaccion(dto.getEncuesta().getR7()), 
                                obtenerNivelSatisfaccion(dto.getEncuesta().getR8()), 
                                obtenerNivelSatisfaccion(dto.getEncuesta().getR9()), 
                                obtenerNivelSatisfaccion(dto.getEncuesta().getR10()), 
                                obtenerNivelSatisfaccion(dto.getEncuesta().getR11()), 
                                obtenerNivelSatisfaccion(dto.getEncuesta().getR12()), 
                                obtenerNivelSatisfaccion(dto.getEncuesta().getR13()), 
                                verificarValor(dto.getEncuesta().getR14()),
                                verificarValor(dto.getEncuesta().getR15()),
                                obtenerNivelRequerido(dto.getEncuesta().getR16()), 
                                obtenerNivelRequerido(dto.getEncuesta().getR17()), 
                                obtenerNivelRequerido(dto.getEncuesta().getR18()), 
                                obtenerNivelRequerido(dto.getEncuesta().getR19()), 
                                obtenerOportunidades(dto.getEncuesta().getR20()), 
                                obtenerMejoras(dto.getEncuesta().getR21()), 
                                obtenerOpciones(dto.getEncuesta().getR22()), 
                                verificarValor(dto.getEncuesta().getR23()),
                                verificarEstatus(dto.getEncuesta())};
                gExcel.escribirDatosExcel(datos, i, 3, rol.getLibro());
            }
            gExcel.escribirLibro(rol.getCarpeta(), rol.getSubCarpeta(), rol.getExcelSalida());
            String ruta = gExcel.enviarLibro();
            Ajax.oncomplete("descargar('" + gExcel.enviarLibro() + "');");
    }
    
    public String obtenerNivelEscolaridad(Short opcion){
        List<SelectItem> items = ejb.getRespuestasNivelEscolaridad().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerInstitucion(Short opcion){
        List<SelectItem> items = ejb.getRespuestasEstudias().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerActividadActual(Short opcion){
        List<SelectItem> items = ejb.getRespuestasActActual().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerActividadEconomico(Short opcion){
        List<SelectItem> items = ejb.getRespuestasActEconomico().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerPuesto(Short opcion){
        List<SelectItem> items = ejb.getRespuestasPuesto().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerCoincideFormacion(Short opcion){
        List<SelectItem> items = ejb.getRespuestasCoincidencia().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerIngresoMensual(Short opcion){
        List<SelectItem> items = ejb.getRespuestasIngreso().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerRegimen(Short opcion){
        List<SelectItem> items = ejb.getRespuestasRegimen().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerTamanioEmpresa(Short opcion){
        List<SelectItem> items = ejb.getRespuestasTamanioEmpresa().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerTiempo(Short opcion){
        List<SelectItem> items = ejb.getRespuestasTiempo().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerMedios(Short opcion){
        List<SelectItem> items = ejb.getRespuestasMedio().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerGenteCargo(Short opcion){
        List<SelectItem> items = ejb.getRespuestasPersonalCargo().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerSituacion(Short opcion){
        List<SelectItem> items = ejb.getRespuestasSituacionEcon().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerExpectativa(Short opcion){
        List<SelectItem> items = ejb.getRespuestasSiNo().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String verificarValor(Short valor){
        if(valor == null){
            return ""; 
        }else{
            return valor.toString();
        }
    }
    
    public String verificarValor(String valor){
        if(valor == null){
            return "";
        }else{
            return valor;
        }
    }
    
    public String verificarEstatus(EncuestaSeguimientoEgresados encuesta){
        Comparador<EncuestaSeguimientoEgresados> comparador = new ComparadorEncuestaSeguimientoEgresados();
        String estatus = "";
        if (comparador.isCompleto(encuesta)) {
            estatus = "Completo";
        }
        if (!comparador.isCompleto(encuesta)) {
            estatus = "Inompleto";
        }
        return estatus;
    }
    
    public String verificarEstatus(EncuestaEmpleadores encuesta){
        Comparador<EncuestaEmpleadores> comparador = new ComparadorEncuestaEmpleadores();
        String estatus = "";
        if (comparador.isCompleto(encuesta)) {
            estatus = "Completo";
        }
        if (!comparador.isCompleto(encuesta)) {
            estatus = "Inompleto";
        }
        return estatus;
    }
    
    public String obtenerNivelSatisfaccion(Short opcion){
        List<SelectItem> items = ejb.getNivelSatisfaccion().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerNivelRequerido(Short opcion){
        List<SelectItem> items = ejb.getNivelRequerido().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerOportunidades(Short opcion){
        List<SelectItem> items = ejb.getOportunidades().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerMejoras(Short opcion){
        List<SelectItem> items = ejb.getMejoras().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public String obtenerOpciones(Short opcion){
        List<SelectItem> items = ejb.getOpciones().getValor();
        if(opcion == null){
            return "";
        }else{
            return items.stream().filter(item -> item.getValue().equals(opcion.toString())).findFirst().get().getLabel();
        }
    }
    
    public List<SelectItem> obtenerRespuestasFactores(){
        ResultadoEJB<List<SelectItem>> resSelect = ejb.getRespuestasFactores();
        if(!resSelect.getCorrecto()) return Collections.EMPTY_LIST;
        return resSelect.getValor();
    }
    
    public List<SelectItem> obtenerRespuestasAspectos(){
        ResultadoEJB<List<SelectItem>> resSelect = ejb.getRespuestasAspectos();
        if(!resSelect.getCorrecto()) return Collections.EMPTY_LIST;
        return resSelect.getValor();
    }
    
    public List<SelectItem> obtenerNivel(){
        ResultadoEJB<List<SelectItem>> resSelect = ejb.getNivelRequerido();
        if(!resSelect.getCorrecto()) return Collections.EMPTY_LIST;
        return resSelect.getValor();
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administracion encuesta seguimiento egresados";
         Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(//System.out::println);
        return mostrar(request, map.containsValue(valor));

    }
    
}
