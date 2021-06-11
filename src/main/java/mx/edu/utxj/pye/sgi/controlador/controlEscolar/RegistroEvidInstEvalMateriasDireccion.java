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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRegistroEvidInstEvaluacionMateria;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroEvidInstEvalMateriasRolDirector;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroEvidInstEvalMaterias;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.ServicioRegEvidInsEval;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Criterio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionSugerida;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvidenciaEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.InstrumentoEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import org.omnifaces.util.Ajax;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class RegistroEvidInstEvalMateriasDireccion extends ViewScopedRol implements Desarrollable {
    @Getter @Setter RegistroEvidInstEvalMateriasRolDirector rol;

    @EJB EjbRegistroEvidInstEvalMaterias ejb;
    @EJB EjbAsignacionIndicadoresCriterios ejbAsignacionIndicadoresCriterios;
    @EJB ServicioRegEvidInsEval ejbServicioRegEvidInsEval;
    
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    
     /**
     * Inicializa:<br/>
     *      El filtro de rol por area superior y categiría operativa<br/>
     *      La referencia al director si es que el usuario logueado es efectivamente un director por medio del filtro de rol<br/>
     *      El programa educativo al que pertenece el director por medio de operación segura antierror<br/>
     *      El DTO del rol<br/>
     *      La lista de periodos escolares en forma descendente por medio de operación segura antierror<br/>
     *      EL mapa de programas con grupos por medio de operación segura antierror ordenando programas por areas, niveles y nombre del programa y los grupos por grado y letra
     */
    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init(){
        try{
        if(!logon.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_EVIDINST_EVALUACION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDirector(logon.getPersonal().getClave());//validar si es director
//            System.out.println("resAcceso = " + resAcceso);

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarEncargadoDireccion(logon.getPersonal().getClave());
//            System.out.println("resValidacion = " + resValidacion);
            if(!resValidacion.getCorrecto() && !resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new RegistroEvidInstEvalMateriasRolDirector(filtro, director);
            tieneAcceso = rol.tieneAcceso(director);
//            System.out.println("tieneAcceso1 = " + tieneAcceso);
            if(!tieneAcceso){
                rol.setFiltro(resValidacion.getValor());
                tieneAcceso = rol.tieneAcceso(director);
            }
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setDirector(director);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbAsignacionIndicadoresCriterios.getPeriodoActual());
//            rol.setSoloLectura(true);
            
            listaProgramasEducativos();

            rol.getInstrucciones().add("Para poder realizar la Asignación de Indicadores por Criterio debe haber realizado previamente la Configuración de la Unidad Materia.");
            rol.getInstrucciones().add("Seleccionar periodo escolar activo, de lo contrario solo podrá consultar asignaciones anteriores.");
            rol.getInstrucciones().add("Seleccionar Materia - Grupo - Programa Educativo que va a configurar.");
            rol.getInstrucciones().add("Seleccionar si asignará indicadores de forma INDIVIDUAL(por unidad) o MASIVA (aplicar para todas las unidades).");
            rol.getInstrucciones().add("Si seleccionó INDIVIDUAL, deberá seleccionar Unidad a la que aplicará la asignación que configurará.");
            rol.getInstrucciones().add("Seleccionar la pestaña del criterio: Ser, Saber o Saber - Hacer.");
            rol.getInstrucciones().add("Capturar porcentaje que valdrá el o los indicadores que utilizará por criterio.");
            rol.getInstrucciones().add("La suma de los porcentajes por criterio deben sumar 100%, de lo contrario el sistema no le permitirá guardar.");
            rol.getInstrucciones().add("Una vez que haya seleccionado indicadores de los TRES criterios puede proceder a GUARDAR la información.");
            rol.getInstrucciones().add("Usted podrá actualizar la asignación de indicadores de la unidad siempre y cuando no haya terminado de configurar todas las unidades.");
            rol.getInstrucciones().add("Una vez que realice la asignación de indicadores de todas las unidades podrá VISUALIZAR la asignación general de la materia, y podrá ELIMINARLA siempre y cuando no esté VALIDADA.");

            rol.setAgregarEvidencia(Boolean.FALSE);
            rol.setTipoAgregarEvid("masivaEvid");
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registro evidencias e instrumetnos evaluacion materia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    /**
     * Permite actualizar la carga académica del docente correspondiente al periodo seleccionado
     */
//    public void actualizarCargaAcademica(){
//        setAlertas(Collections.EMPTY_LIST);
//        if(rol.getPeriodo() == null) return;
//        if(rol.getDocente()== null) return;
//
//        ResultadoEJB<List<DtoCargaAcademica>> res = ejb.getCargaAcademicaDocente(rol.getDocente(), rol.getPeriodo());
//        if(res.getCorrecto()){
//            rol.setCargas(res.getValor());
//            existeConfiguracion();
//        }else mostrarMensajeResultadoEJB(res);
//        
//    }
    
    public void listaProgramasEducativos(){
        ResultadoEJB<List<AreasUniversidad>> res = ejb.getProgramasEducativos(rol.getDirector());
        if(res.getCorrecto()){
            rol.setProgramasEducativos(res.getValor());
            rol.setProgramaEducativo(rol.getProgramasEducativos().get(0));
            listaPeriodosEscolares();
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaPeriodosEscolares(){
        ResultadoEJB<List<PeriodosEscolares>> res = ejb.getPeriodosEscolares(rol.getProgramaEducativo());
        if(res.getCorrecto()){
            rol.setPeriodosEscolares(res.getValor());
            rol.setPeriodoEscolar(rol.getPeriodosEscolares().get(0));
            listaEvaluacionesRegistradas();
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaEvaluacionesRegistradas(){
        ResultadoEJB<List<DtoRegistroEvidInstEvaluacionMateria>> res = ejb.buscarEvaluacionSugerida(rol.getProgramaEducativo(), rol.getPeriodoEscolar());
        if(res.getCorrecto()){
            rol.setListaEvidenciasInstrumentos(res.getValor());
            if(!rol.getListaEvidenciasInstrumentos().isEmpty()|| rol.getListaEvidenciasInstrumentos().size()>0){                   
                rol.setPlanEstudioRegistrado(rol.getListaEvidenciasInstrumentos().stream().map(p->p.getPlanEstudioMateria().getIdPlan()).findFirst().orElse(new PlanEstudio()));
            }
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaGrados(){
            ResultadoEJB<List<Integer>> res = ejb.getGradosProgramaEducativo(rol.getPlanEstudioRegistrado());
            if(res.getCorrecto()){
                rol.setGrados(res.getValor());
                rol.setGrado(rol.getGrados().get(0));
                listaMateriasGrado();
            }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaMateriasGrado(){
            ResultadoEJB<List<Materia>> res = ejb.getMateriasGrado(rol.getPlanEstudioRegistrado(), rol.getGrado());
            if(res.getCorrecto()){
                rol.setMaterias(res.getValor());
                rol.setMateria(rol.getMaterias().get(0));
                listaUnidadesMateria();
                listaCategorias();
                listaInstrumentosEvaluacion();
            }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaUnidadesMateria(){
            ResultadoEJB<List<UnidadMateria>> res = ejb.getUnidadesMateria(rol.getMateria());
            if(res.getCorrecto()){
                rol.setUnidadMaterias(res.getValor());
                rol.setUnidadMateria(rol.getUnidadMaterias().get(0));
            }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaCategorias(){
            ResultadoEJB<List<Criterio>> res = ejb.getCategoriasNivel(rol.getProgramaEducativo());
            if(res.getCorrecto()){
                rol.setCategorias(res.getValor());
                rol.setCategoria(rol.getCategorias().get(0));
                listaEvidenciasEvaluacionCategoria();
            }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaEvidenciasEvaluacionCategoria(){
            ResultadoEJB<List<EvidenciaEvaluacion>> res = ejbAsignacionIndicadoresCriterios.getEvidenciasCategoria(rol.getCategoria());
            if(res.getCorrecto()){
                rol.setEvidencias(res.getValor());
                rol.setEvidencia(rol.getEvidencias().get(0));
            }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaInstrumentosEvaluacion(){
            ResultadoEJB<List<InstrumentoEvaluacion>> res = ejbAsignacionIndicadoresCriterios.getInstrumentosEvaluacion();
            if(res.getCorrecto()){
                rol.setInstrumentos(res.getValor());
                rol.setInstrumento(rol.getInstrumentos().get(0));
                rol.setMetaInstrumento(80);
            }else mostrarMensajeResultadoEJB(res);  
    }
    
    
     public void cambiarPrograma(ValueChangeEvent event){
        rol.setProgramaEducativo((AreasUniversidad)event.getNewValue());
        listaPeriodosEscolares();
        Ajax.update("frm");
    }
        
    
    public void cambiarPeriodo(ValueChangeEvent event){
        rol.setPeriodoEscolar((PeriodosEscolares)event.getNewValue());
        listaEvaluacionesRegistradas();
    }
 
    public void cambiarAgregarEvid(ValueChangeEvent event){
        if(rol.getAgregarEvidencia()){
            rol.setAgregarEvidencia(false);
            Ajax.update("frm");
        }else{
            rol.setAgregarEvidencia(true);
            listaGrados();
            Ajax.update("frm");
        }
    }
    
    public void cambiarTipoAgregarEvid(ValueChangeEvent event){
        rol.setTipoAgregarEvid((String)event.getNewValue());
        Ajax.update("frm");
    }
    
    public void cambiarGrado(ValueChangeEvent event){
        rol.setGrado((Integer)event.getNewValue());
        listaMateriasGrado();
        Ajax.update("frm");
    }
    
    public void cambiarMateria(ValueChangeEvent event){
        rol.setCategoria((Criterio)event.getNewValue());
        listaUnidadesMateria();
        Ajax.update("frm");
    }
    
    public void cambiarUnidad(ValueChangeEvent event){
        rol.setUnidadMateria((UnidadMateria)event.getNewValue());
        Ajax.update("frm");
    }
    
    public void cambiarCategoria(ValueChangeEvent event){
        rol.setCategoria((Criterio)event.getNewValue());
        listaEvidenciasEvaluacionCategoria();
        Ajax.update("frm");
    }
    
    public void cambiarEvidencia(ValueChangeEvent event){
        rol.setEvidencia((EvidenciaEvaluacion)event.getNewValue());
        Ajax.update("frm");
    }
    
    public void cambiarInstrumento(ValueChangeEvent event){
        rol.setInstrumento((InstrumentoEvaluacion)event.getNewValue());
        Ajax.update("frm");
    }
//    
     /**
     * Permite registrar una evaluación a un evento de estadía
     */
    public void agregarNuevaEvidencia(){
        if("masivaEvid".equals(rol.getTipoAgregarEvid())){
            ResultadoEJB<Boolean> res = ejb.buscarEvidenciaListaEvidenciasInstrumentos(rol.getListaEvidenciasInstrumentos(), rol.getEvidencia());
            if(res.getCorrecto() && res.getValor()){
                ResultadoEJB<List<DtoRegistroEvidInstEvaluacionMateria>> agregar = ejb.agregarEvidenciaListaEvidenciasInstrumentos(rol.getListaEvidenciasInstrumentos(), rol.getEvidencia(), rol.getInstrumento(), rol.getMetaInstrumento());
                if(agregar.getCorrecto()){
                    rol.setListaEvidenciasInstrumentos(agregar.getValor());
                    Ajax.update("frm");
                    mostrarMensajeResultadoEJB(agregar);
                }
            }else if(res.getCorrecto() && !res.getValor()){
                Messages.addGlobalWarn("La evidencia ya está en lista de evidencias sugeridas.");
            }else{
                mostrarMensajeResultadoEJB(res);
            }
        }else{
            ResultadoEJB<Boolean> res = ejb.buscarEvidenciaUnidadListaEvidenciasInstrumentos(rol.getUnidadMateria(), rol.getListaEvidenciasInstrumentos(), rol.getEvidencia());
            if(res.getCorrecto() && res.getValor()){
                ResultadoEJB<List<DtoRegistroEvidInstEvaluacionMateria>> agregar = ejb.agregarEvidenciaUnidadListaEvidenciasInstrumentos(rol.getListaEvidenciasInstrumentos(), rol.getUnidadMateria(), rol.getEvidencia(), rol.getInstrumento(), rol.getMetaInstrumento());
                if(agregar.getCorrecto()){
                    rol.setListaEvidenciasInstrumentos(agregar.getValor());
                    Ajax.update("frm");
                    mostrarMensajeResultadoEJB(agregar);
                }
            }else if(res.getCorrecto() && !res.getValor()){
                Messages.addGlobalWarn("La evidencia ya está en lista de evidencias sugeridas para esa unidad.");
            }else{
                mostrarMensajeResultadoEJB(res);
            }
        }
    }
//    
     /**
     * Permite eliminar una evidencia e instrumentos de evaluación registrado
     * @param dtoRegistroEvidInstEvaluacionMateria
     */
    public void eliminarEvidencia(DtoRegistroEvidInstEvaluacionMateria dtoRegistroEvidInstEvaluacionMateria){
        ResultadoEJB<Integer> resEliminar = ejb.eliminarEvidenciaUnidadListaEvidenciasInstrumentos(dtoRegistroEvidInstEvaluacionMateria);
        mostrarMensajeResultadoEJB(resEliminar);
        listaEvaluacionesRegistradas();
        Ajax.update("frm");
    }
//    
//    public void guardarEvidenciasInstrumentos(){
//        Integer totalUnidades = (int) (long) rol.getListaEvidenciasSugeridas().stream().mapToInt(p->p.getUnidadMateriaConfiguracion().getIdUnidadMateria().getIdUnidadMateria()).distinct().count();
//        
//        Integer sumaTotalUnidades = totalUnidades * 100;
//        
//        Integer sumaSer = rol.getListaEvidenciasSugeridas().stream().filter(p->p.getEvidenciaEvaluacion().getCriterio().getTipo().equals("Ser")).mapToInt(p->p.getValorPorcentual()).sum();
//       
//        if(sumaSer < sumaTotalUnidades){
//            Messages.addGlobalWarn("Criterios SER: La suma de los porcentajes por indicador es menor a 100%.");
//        }else if(sumaSer > sumaTotalUnidades){
//            Messages.addGlobalWarn("Criterios SER: La suma de los porcentajes por indicador es mayor a 100%.");
//        }
//        
//        Integer sumaSaber = rol.getListaEvidenciasSugeridas().stream().filter(p->p.getEvidenciaEvaluacion().getCriterio().getTipo().equals("Saber")).mapToInt(p->p.getValorPorcentual()).sum();
//        if(sumaSaber < sumaTotalUnidades){
//             Messages.addGlobalWarn("Criterios SABER: La suma de los porcentajes por indicador es menor a 100%.");
//        }else if(sumaSaber > sumaTotalUnidades){
//             Messages.addGlobalWarn("Criterios SABER: La suma de los porcentajes por indicador es mayor a 100%.");
//        }
//        
//        Integer sumaSaberHacer = rol.getListaEvidenciasSugeridas().stream().filter(p->p.getEvidenciaEvaluacion().getCriterio().getTipo().equals("Saber hacer")).mapToInt(p->p.getValorPorcentual()).sum();
//        if(sumaSaberHacer < sumaTotalUnidades){
//            Messages.addGlobalWarn("Criterios SABER - HACER: La suma de los porcentajes por indicador es menor a 100%.");
//        }else if(sumaSaberHacer > sumaTotalUnidades){
//            Messages.addGlobalWarn("Criterios SABER - HACER: La suma de los porcentajes por indicador es mayor a 100%.");
//        }
//        
//        Integer comparador = sumaTotalUnidades * 3;
//        
//        Integer sumaTotalCat = sumaSer + sumaSaber + sumaSaberHacer;
//        
//        if(sumaTotalCat.equals(comparador))
//        {
//            ResultadoEJB<List<UnidadMateriaConfiguracionEvidenciaInstrumento>> resGuardar = ejb.guardarListaEvidenciasInstrumentos(rol.getListaEvidenciasSugeridas());
//            if (resGuardar.getCorrecto()) {
//                mostrarMensajeResultadoEJB(resGuardar);
//                Ajax.update("frm");
//                existeAsignacion();
//            } else {
//                mostrarMensajeResultadoEJB(resGuardar);
//            }
//        
//        }
//    }
//    
//    public void eliminarEvidenciasInstrumentos(){
//        ResultadoEJB<UnidadMateriaConfiguracion> configuracion = ejb.verificarValidacionConfiguracion(rol.getCarga().getCargaAcademica());
//        if (configuracion.getCorrecto()) {
//            rol.setDirectorValido(configuracion.getValor().getDirector());
//            if (rol.getDirectorValido() == null){
//                ResultadoEJB<Integer> resEliminar = ejb.eliminarEvidenciasInstrumentos(rol.getCarga().getCargaAcademica());
//                existeAsignacion();
//                mostrarMensajeResultadoEJB(resEliminar);
//                
//            } else {
//               Messages.addGlobalWarn("La configuración ya ha sido validada por el director de carrera, por lo que no puede eliminarla");
//            }
//            
//        } else {
//            mostrarMensajeResultadoEJB(configuracion);
//        }
//    }
    public void descargarPlantilla() throws IOException, Throwable{
        File f = new File(ejb.getPlantillaEvidInstMateria(rol.getPlanEstudioRegistrado(), rol.getProgramaEducativo()));
        Faces.sendFile(f, true);
    }
    
    public void listaPreviaEvidenciasInstrumentos(String rutaArchivo) {
       try {
            if(rutaArchivo != null){
                rol.setRutaArchivo(rutaArchivo);
                rol.setListaPreviaEvidenciasInstrumentos(ejbServicioRegEvidInsEval.getListaRegEvidInstEvaluacion(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
            Logger.getLogger(RegistroEvidInstEvalMateriasDireccion.class.getName()).log(Level.SEVERE, null, ex);
            if(rutaArchivo != null){
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
    
    public void guardarEvidInstEval() {
        if (rol.getListaPreviaEvidenciasInstrumentos() != null) {
            try {
                ResultadoEJB<List<EvaluacionSugerida>> resGuardar = ejbServicioRegEvidInsEval.guardarEvidInstEval(rol.getListaPreviaEvidenciasInstrumentos(), rol.getPeriodoActivo());
                if (resGuardar.getCorrecto()) {
                    rol.setListaEvaluacioneGuardadas(resGuardar.getValor());
                    mostrarMensajeResultadoEJB(resGuardar);
                } else {
                    mostrarMensajeResultadoEJB(resGuardar);
                }
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                Logger.getLogger(RegistroEvidInstEvalMateriasDireccion.class.getName()).log(Level.SEVERE, null, ex);
                if (rol.getRutaArchivo() != null) {
                    ServicioArchivos.eliminarArchivo(rol.getRutaArchivo());
                }
            } finally {
                rol.getListaPreviaEvidenciasInstrumentos().clear();
                rol.setRutaArchivo(null);
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }
    
    public void cancelarArchivo(){
        rol.getListaPreviaEvidenciasInstrumentos().clear();
        if (rol.getRutaArchivo() != null) {
            ServicioArchivos.eliminarArchivo(rol.getRutaArchivo());
            rol.setRutaArchivo(null);
        }
    }
}
