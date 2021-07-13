/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaPlanEstudio;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaRegistro;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaUnidades;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPlanEstudio;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPlanEstudioMateriaCompetencias;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroPlanesEstudioRolDirector;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AreaConocimiento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Competencia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;


import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.IOException;
import java.util.Objects;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAlineacionAcedemica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaMetasPropuestas;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.ServicioRegAliEdu;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MetasPropuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosContinuidad;
import mx.edu.utxj.pye.sgi.enums.RegistroSiipEtapa;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;

/**
 *
 * @author UTXJ
 */

@Named
@ViewScoped
public class AdministracionPlanEstudioDirector extends ViewScopedRol implements Desarrollable{
    @Getter @Setter RegistroPlanesEstudioRolDirector rol;
    @Getter @Setter PlanEstudioMateria pem;
    
    @EJB EjbPropiedades ep;
    @EJB EjbRegistroPlanEstudio ejb;
    @EJB EjbAreasLogeo ejbAreasLogeo;
    @Inject LogonMB logon;
    
    @Getter Boolean tieneAcceso = false;
            
    
    //    Variables de Lectura
    @Getter private RegistroSiipEtapa etapa;
    
    //    Variables de Lectura y Escritura
    @Getter @Setter private String rutaArchivo;
    @Getter @Setter private String plan;
    @Getter @Setter private String programa;
    @Getter @Setter private Part file;  
    List<PlanEstudioMateria> materiasp = new ArrayList<>();
    


    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @EJB EjbCarga ejbCarga;
    @EJB EjbModulos ejbModulos;
    @EJB ServicioRegAliEdu edu;

@PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.ADMINISTRACION_PLAN_ESTUDIOS);

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarDirector(logon.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resValidaEnc = ejb.validarEncargadoDirector(logon.getPersonal().getClave());//validar si es director

            if (!resValidaEnc.getCorrecto() && !resValidacion.getCorrecto()) {
                mostrarMensajeResultadoEJB(resValidacion);
                return;
            }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new RegistroPlanesEstudioRolDirector(filtro, director, director.getAreaOficial());
            tieneAcceso = rol.tieneAcceso(director);

            if (!tieneAcceso) {
                rol.setFiltro(resValidaEnc.getValor());
                tieneAcceso = rol.tieneAcceso(director);
            }

            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu

            ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> resProgramaPlan = ejb.getProgramasEducativos(director);
            ResultadoEJB<List<AreaConocimiento>> resAreasConocimiento = ejb.getAreasConocimiento();
            ResultadoEJB<List<PeriodosEscolares>> resperiodos = ejb.getPeriodosDescendentes();
            
            if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
            rol.setAreaPlanEstudioMap(resProgramaPlan.getValor());
            
            if(!resAreasConocimiento.getCorrecto()) mostrarMensajeResultadoEJB(resAreasConocimiento);
            rol.setConocimientos(resAreasConocimiento.getValor());
             
            if(!resperiodos.getCorrecto()) mostrarMensajeResultadoEJB(resperiodos);
            rol.setPeriodosEscolareses(resperiodos.getValor());
            
            rol.setPlanEstudio1(new DtoPlanEstudio(new PlanEstudio(), rol.getPrograma()));
            rol.setMateriaRegistro1(new DtoMateriaRegistro(new Materia(), rol.getConocimiento()));
            rol.setMateriaMetasPropuestas(new DtoMateriaMetasPropuestas(new MetasPropuestas(), new PlanEstudioMateria(), new PlanEstudio()));
            rol.setMateriaUnidades1(new DtoMateriaUnidades(new UnidadMateria(), rol.getMateria()));
            rol.setMateriaPlanEstudio1(new DtoMateriaPlanEstudio(new PlanEstudioMateria(),rol.getPlanEstudio(), rol.getMateria()));
            rol.setPlanEstudioMateriaCompetencias1(new DtoPlanEstudioMateriaCompetencias(new Competencia(),new Competencia(),rol.getPlanEstudioMateria(),rol.getPlanEstudio()));
            rol.setUnidadesMateria(new ArrayList<>());
            rol.setNewCompetencia(false);
            rol.setPlanestudioMateriasSelect(new ArrayList<>());
            rol.setAcedemicas(new ArrayList<>());
            rol.setAlineacionAcedemica(new DtoAlineacionAcedemica(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria()));
            rol.setAlineacionesDescripociones(new ArrayList<>());
            rol.setTipoReg("Ob");

            setEtapa(RegistroSiipEtapa.MOSTRAR);
//            plan = String.valueOf(rol.getPlanEstudio().getAnio());
//            programa = rol.getPrograma().getSiglas();

        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "Admin Plan Estudio";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
// eventos de registro

    public void recibirArchivo(ValueChangeEvent e){
        file = (Part)e.getNewValue();
    }

    public void setEtapa(RegistroSiipEtapa etapa) {
        this.etapa = etapa;
    }
    
    public void subirExcelEvidInstMateria() throws IOException {
        if (file != null) {
            rutaArchivo = ejbCarga.subirPlantillaAlineacionMaterias(plan, programa, file);
            if (!"Error: No se pudo leer el archivo".equals(rutaArchivo)) {
                setEtapa(RegistroSiipEtapa.CARGAR);
                listaPreviaEvidenciasInstrumentos(rutaArchivo);
                rutaArchivo = null;
                file.delete();
            } else {
                rutaArchivo = null;
                file.delete();
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intentelo nuevamente");
            }
        } else {
            System.err.println("subirExcelEvidInstMateria - file es null ");
             Messages.addGlobalWarn("Es necesario seleccionar un archivo");
        }
    }
    
    public void listaPreviaEvidenciasInstrumentos(String rutaArchivo) {
       try {
            if(rutaArchivo != null){
                rol.setRutaArchivo(rutaArchivo);
                rol.setListaPreviaAlineacionEducativa(edu.getListaRegEvidInstEvaluacion(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
            Logger.getLogger(RegistroEvidInstEvalMateriasDireccion.class.getName()).log(Level.SEVERE, null, ex);
            if(rutaArchivo != null){
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
    
    
    
    public void guardarPlanEstudio() {
        rol.getPlanEstudio1().getPlanEstudio().setIdPe(rol.getPlanEstudio1().getPrograma().getArea());
        ejb.registrarPlanEstudio(rol.getPrograma(), rol.getPlanEstudio1().getPlanEstudio(), Operacion.PERSISTIR);
    }

    public void guardarMateria() {
        ejb.registrarMateria(rol.getMateriaRegistro1(), Operacion.PERSISTIR);
        guardarPlanEstudioMateria();
        rol.setMateriaRegistro1(new DtoMateriaRegistro(new Materia(), rol.getConocimiento()));
    }

    public void guardarUnidad() {
        rol.getMateriaUnidades1().getUnidadMateria().setIntegradora(false);
        ejb.registrarUnidadMateria(rol.getMateriaUnidades1(), Operacion.PERSISTIR);
        unidadesPorMateriaConsulta();
        rol.setMateriaUnidades1(new DtoMateriaUnidades(new UnidadMateria(), rol.getMateriaUnidades1().getMateria()));
    }
    
    public void guardarMetas() {
        PeriodosEscolares escolares=rol.getPeriodosEscolareses().get(0);
        rol.getMateriaMetasPropuestas().getMetasPropuestas().setPeriodo(escolares.getPeriodo());
        ejb.registrarMetaMateria(rol.getMateriaMetasPropuestas(), Operacion.PERSISTIR);
        rol.getMateriaMetasPropuestas().setMateria(new PlanEstudioMateria());
        rol.getMateriaMetasPropuestas().setMetasPropuestas(new MetasPropuestas());
        nivelesDesempenioPlanEstudioMateriaConsulta();
    }

    public void guardarPlanEstudioMateria() {
        rol.getMateriaPlanEstudio1().setMateria(rol.getMateriaRegistro1().getMateria());
        ejb.registrarPlanEstudioMateria(rol.getMateriaPlanEstudio1().getMateria(), rol.getMateriaPlanEstudio1().getPlanEstudio(), rol.getMateriaPlanEstudio1().getPlanEstudioMateria(), Operacion.PERSISTIR);
        materiasPorPlanEstudioConsulta(rol.getMateriaPlanEstudio1().getPlanEstudio());
        rol.setMateriaPlanEstudio1(new DtoMateriaPlanEstudio(new PlanEstudioMateria(), rol.getMateriaPlanEstudio1().getPlanEstudio(), new Materia()));
    }

    public void guardarPlanEstudioMateriaCompetencia() {
        if (rol.getPlanEstudioMateriaCompetencias1().getCompetencia().getIdCompetencia().equals(0)) {
            ejb.registrarPlanEstudioMateriaCompetencias(rol.getPlanEstudioMateriaCompetencias1(), Operacion.PERSISTIR);
        } else {
            ejb.registrarPlanEstudioMateriaCompetencias(rol.getPlanEstudioMateriaCompetencias1(), Operacion.ACTUALIZAR);
        }
        competenciasPorPlanEstudioMateriaConsulta();
        rol.setNewCompetencia(false);
        rol.setPlanEstudioMateriaCompetencias1(new DtoPlanEstudioMateriaCompetencias(new Competencia(), new Competencia(), new PlanEstudioMateria(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio()));
    }
  
    public void guardarAlineacion() throws Throwable {
        if (!rol.getPlanestudioMateriasSelect().isEmpty()) {
            ejb.accionesAlineacion(rol.getPlanestudioMateriasSelect(), rol.getAlineacionAcedemica(), rol.getTipoReg(), Operacion.PERSISTIR);
        }
        comprobarTipoRegistro();
        rol.setNewCompetencia(true);
        rol.getAlineacionAcedemica().setClave("");
        rol.getAlineacionAcedemica().setDescripcion("");
        rol.getAlineacionAcedemica().setIde(0);
        rol.getAlineacionAcedemica().setMeta(0D);
        rol.getAlineacionAcedemica().setNivelA("");
        rol.getAlineacionAcedemica().setPlanEstudioMateria(new PlanEstudioMateria());
    }
// eventos de Actualizacion
    
     public void onRowEditPlanEstudio(RowEditEvent event) {
        try {
            ejb.registrarPlanEstudio(rol.getPrograma(),(PlanEstudio) event.getObject(), Operacion.ACTUALIZAR);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    public void onRowEditMateria(RowEditEvent event) {
        try {
            PlanEstudioMateria m=(PlanEstudioMateria) event.getObject();
            rol.setMateriaRegistro1(new DtoMateriaRegistro(m.getIdMateria(), m.getIdMateria().getIdAreaConocimiento()));
            ejb.registrarMateria(rol.getMateriaRegistro1(), Operacion.ACTUALIZAR);
//            materiasRegistradasConsulta();
            rol.setMateriaRegistro1(new DtoMateriaRegistro(new Materia(), rol.getConocimiento()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditUnidad(RowEditEvent event) {
       try {
            rol.setMateriaUnidades1(new DtoMateriaUnidades((UnidadMateria) event.getObject(), rol.getMateriaUnidades1().getMateria()));
            ejb.registrarUnidadMateria(rol.getMateriaUnidades1(), Operacion.ACTUALIZAR);
            unidadesPorMateriaConsulta();
            rol.setMateriaUnidades1(new DtoMateriaUnidades(new UnidadMateria(), rol.getMateriaUnidades1().getMateria()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditPlanEstudioMateria(RowEditEvent event) {
        try {            
            rol.setMateriaPlanEstudio1(new DtoMateriaPlanEstudio((PlanEstudioMateria) event.getObject(), rol.getMateriaPlanEstudio1().getPlanEstudio(), new Materia()));
            ejb.registrarPlanEstudioMateria(rol.getMateriaPlanEstudio1().getMateria(), rol.getMateriaPlanEstudio1().getPlanEstudio(), rol.getMateriaPlanEstudio1().getPlanEstudioMateria(), Operacion.ACTUALIZAR);
            onRowEditMateria(event);
            materiasPorPlanEstudioConsulta(rol.getMateriaPlanEstudio1().getPlanEstudio());
            rol.setMateriaPlanEstudio1(new DtoMateriaPlanEstudio(new PlanEstudioMateria(), rol.getMateriaPlanEstudio1().getPlanEstudio(), new Materia()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditPlanEstudioMateriaCompetencia(RowEditEvent event) {
        try {
            DtoPlanEstudioMateriaCompetencias dpemc=(DtoPlanEstudioMateriaCompetencias) event.getObject();
            
            rol.setPlanEstudioMateriaCompetencias1(new DtoPlanEstudioMateriaCompetencias(dpemc.getCompetencia(),new Competencia(), dpemc.getPlanEstudioMateria(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio()));
            ejb.registrarPlanEstudioMateriaCompetencias(rol.getPlanEstudioMateriaCompetencias1(), Operacion.ACTUALIZAR);
            competenciasPorPlanEstudioMateriaConsulta();
            rol.setPlanEstudioMateriaCompetencias1(new DtoPlanEstudioMateriaCompetencias(new Competencia(),new Competencia(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudioMateria(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
    
    public void onRowEditMetasPrupuestas(RowEditEvent event) {
        try {
            MetasPropuestas metasPropuestas=(MetasPropuestas) event.getObject();
            DtoMateriaMetasPropuestas dpemc=new DtoMateriaMetasPropuestas(metasPropuestas, metasPropuestas.getIdPlanMateria(), metasPropuestas.getIdPlanMateria().getIdPlan());   
            ejb.registrarMetaMateria(dpemc, Operacion.ACTUALIZAR);
            nivelesDesempenioPlanEstudioMateriaConsulta();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
    public void onRowEditAlineacion(RowEditEvent event) {
        try {
            DtoAlineacionAcedemica dtoAlineacionAcademica=(DtoAlineacionAcedemica) event.getObject();
            ejb.accionesAlineacion(new ArrayList<>(), dtoAlineacionAcademica, rol.getTipoReg(), Operacion.ACTUALIZAR);
            comprobarTipoRegistro();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

// eventos de Eliminacion    
    public void eliminarPlanEstudio(PlanEstudio pe) {
        try {
            ejb.registrarPlanEstudio(rol.getPrograma(), pe, Operacion.ELIMINAR);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarMateria(Materia m) {
        try {
            rol.setMateriaRegistro1(new DtoMateriaRegistro(m, rol.getConocimiento()));
            ejb.registrarMateria(rol.getMateriaRegistro1(), Operacion.ELIMINAR);
//            materiasRegistradasConsulta();
            rol.setMateriaRegistro1(new DtoMateriaRegistro(new Materia(), rol.getConocimiento()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarUnidadMateria(UnidadMateria um) {
        try {
            rol.setMateriaUnidades1(new DtoMateriaUnidades(um, rol.getMateriaUnidades1().getMateria()));
            ejb.registrarUnidadMateria(rol.getMateriaUnidades1(), Operacion.ELIMINAR);
            unidadesPorMateriaConsulta();
            rol.setMateriaUnidades1(new DtoMateriaUnidades(new UnidadMateria(), rol.getMateriaUnidades1().getMateria()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarMetaMateria(MetasPropuestas um) {
        try {
            rol.setMateriaMetasPropuestas(new DtoMateriaMetasPropuestas(um, rol.getMateriaMetasPropuestas().getMateria(),um.getIdPlanMateria().getIdPlan()));
            ejb.registrarMetaMateria(rol.getMateriaMetasPropuestas(), Operacion.ELIMINAR);
            metasPorMateriaConsulta();
//            rol.setMateriaMetasPropuestas(new DtoMateriaMetasPropuestas(new MetasPropuestas(), rol.getMateriaUnidades1().getMateria(),new PlanEstudio()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarPlanEstudioMateria(PlanEstudioMateria pem) {
        try {
            rol.setMateriaPlanEstudio1(new DtoMateriaPlanEstudio(pem, rol.getMateriaPlanEstudio1().getPlanEstudio(), new Materia()));
            ejb.registrarPlanEstudioMateria(rol.getMateriaPlanEstudio1().getMateria(), rol.getMateriaPlanEstudio1().getPlanEstudio(), rol.getMateriaPlanEstudio1().getPlanEstudioMateria(), Operacion.ELIMINAR);
            materiasPorPlanEstudioConsulta(rol.getMateriaPlanEstudio1().getPlanEstudio());
            rol.setMateriaPlanEstudio1(new DtoMateriaPlanEstudio(new PlanEstudioMateria(), rol.getMateriaPlanEstudio1().getPlanEstudio(), new Materia()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarPlanEstudioMateriaCompetencia(Competencia c) {
        try {
            rol.setPlanEstudioMateriaCompetencias1(new DtoPlanEstudioMateriaCompetencias(c,new Competencia(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudioMateria(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio()));
            ejb.registrarPlanEstudioMateriaCompetencias(rol.getPlanEstudioMateriaCompetencias1(), Operacion.ELIMINAR);
            competenciasPorPlanEstudioMateriaConsulta();
            rol.setPlanEstudioMateriaCompetencias1(new DtoPlanEstudioMateriaCompetencias(new Competencia(),new Competencia(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudioMateria(), rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
// Eventos Ajax en interfaz
//    public void materiasRegistradasConsulta() {
//        ResultadoEJB<List<Materia>> resMaterias = ejb.getListadoMaterias();
//        if (!resMaterias.getCorrecto()) {
//            mostrarMensajeResultadoEJB(resMaterias);
//        }
//        rol.setMaterias(resMaterias.getValor());
//    }

    public void unidadesPorMateriaConsulta() {
        ResultadoEJB<Map<Materia, List<UnidadMateria>>> resUnidadesMateria = ejb.getListaUnidadesMateria();
        if (!resUnidadesMateria.getCorrecto()) {
            mostrarMensajeResultadoEJB(resUnidadesMateria);
        }
        rol.setMateriasUnidadesMap(resUnidadesMateria.getValor());
    }
    
    public void metasPorMateriaConsulta() {
        ResultadoEJB<Map<Materia, List<MetasPropuestas>>> resUnidadesMateria = ejb.getListaMetasMateria();
        if (!resUnidadesMateria.getCorrecto()) {
            mostrarMensajeResultadoEJB(resUnidadesMateria);
        }
        rol.setmateriasMetasMap(resUnidadesMateria.getValor());
    }

    public void materiasPorPlanEstudioConsulta(PlanEstudio plan) {
        rol.setPlanEstudioMaterias(new ArrayList<>());
        rol.getMateriaUnidades1().setMateria(new Materia());
        List<PlanEstudioMateria> pems = new ArrayList<>();;

        pems = ejb.generarPlanEstuidoMaterias(plan);
        if (!pems.isEmpty()) {
            rol.setPlanEstudioMaterias(pems);

            ResultadoEJB<List<Materia>> resMaterias = ejb.getListadoMaterias(plan);
            if (!resMaterias.getCorrecto()) {
                mostrarMensajeResultadoEJB(resMaterias);
            }
            rol.setMaterias(resMaterias.getValor());
        }
    }

    public void materiasPorPlanEstudioConsultaUnica() {
        rol.setPlanEstudioMaterias(new ArrayList<>());
        rol.getMateriaUnidades1().setMateria(new Materia());
        List<PlanEstudioMateria> pems = new ArrayList<>();
        PlanEstudio plan = new PlanEstudio();

        if (rol.getMateriaPlanEstudio1().getPlanEstudio() != null) {
            plan = rol.getMateriaPlanEstudio1().getPlanEstudio();
        } else {
            plan = rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio();
        }
        pems = ejb.generarPlanEstuidoMaterias(plan);
        if (!pems.isEmpty()) {
            rol.setPlanEstudioMaterias(pems);

            ResultadoEJB<List<Materia>> resMaterias = ejb.getListadoMaterias(plan);
            if (!resMaterias.getCorrecto()) {
                mostrarMensajeResultadoEJB(resMaterias);
            }
            rol.setMaterias(resMaterias.getValor());
        }
    }
    
    public void competenciasPorPlanEstudioMateriaConsulta() {
        rol.setCompetencias(new ArrayList<>());
        ResultadoEJB<List<Competencia>> resCompetencia = ejb.getCompetenciasPlan(rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio());
        if (!resCompetencia.getCorrecto()) {
            mostrarMensajeResultadoEJB(resCompetencia);
        }
        rol.setCompetencias(resCompetencia.getValor());        
        materiasPorPlanEstudioConsulta(rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio());
        listaCompetenciasPorMateria();
    }
    
    public void listaCompetenciasPorMateria() {
        rol.setPlanEstudioMateriaCompetenciasesList(new ArrayList<>());
        ResultadoEJB<List<DtoPlanEstudioMateriaCompetencias>> resCompetenciaPEM = ejb.obtenerDtoPEMC(rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio());
        if (!resCompetenciaPEM.getCorrecto()) {
            mostrarMensajeResultadoEJB(resCompetenciaPEM);
        }
        rol.setPlanEstudioMateriaCompetenciasesList(resCompetenciaPEM.getValor());
        materiasPorPlanEstudioConsulta(rol.getPlanEstudioMateriaCompetencias1().getPlanEstudio());
    }
    
     public void nivelesDesempenioPlanEstudioMateriaConsulta() {
        rol.setPropuestases(new ArrayList<>());
        ResultadoEJB<List<MetasPropuestas>> resMetas = ejb.getMateriasMetas(rol.getMateriaMetasPropuestas().getPlanEstudio());
        if (!resMetas.getCorrecto()) {
            mostrarMensajeResultadoEJB(resMetas);
        }
        rol.setPropuestases(resMetas.getValor());        
        materiasPorPlanEstudioConsulta(rol.getMateriaMetasPropuestas().getPlanEstudio());
    }
    
    
    
// validaciones realizadas  desde la interfaz
    public void comprobarMateria() {
        final List<Materia> materias = new ArrayList<>();
        rol.getPlanEstudioMaterias().forEach((t) -> {
            materias.add(t.getIdMateria());
        });

        if (materias.contains(rol.getMateriaPlanEstudio1().getMateria())) {
            mostrarMensaje("La materia ya se encuentra asignada al plan de estudio");
        }
    }

    public void comprobarRegistroDeCompetencias() {
        if (rol.getPlanEstudioMateriaCompetencias1().getCompetencia().getIdCompetencia().equals(0)) {
            rol.setNewCompetencia(true);
        }
    }
    
    public void comprobarTipoRegistro() throws Throwable {
        rol.setAlineacionesDescripociones(new ArrayList<>());
        rol.setAcedemicas(new ArrayList<>());
        rol.setPlanestudioMateriasSelect(new ArrayList<>());
        rol.getAlineacionAcedemica().setIde(0);
        System.out.println("comprobarTipoRegistro()"+rol.getTipoReg());
        String cvN="";
        System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.AdministracionPlanEstudioDirector.comprobarTipoRegistro()"+rol.getAlineacionAcedemica().getPlanEstudio().getIdPe());
        
        AreasUniversidad au = ejbAreasLogeo.mostrarAreasUniversidad(rol.getAlineacionAcedemica().getPlanEstudio().getIdPe());
        if (au.getNivelEducativo().getNivel().equals("TSU")) {
            cvN = "T";
        } else if (au.getNivelEducativo().getNivel().equals("5B")) {
            cvN = "L";
        } else {
            cvN = "I";
        }
        List<PlanEstudio> pes=continuidades(rol.getAlineacionAcedemica().getPlanEstudio());
       
        rol.getAlineacionesDescripociones().add(new DtoAlineacionAcedemica(0, "", "Nuevo Registro", "", 0D, rol.getAlineacionAcedemica().getPlanEstudio(), new PlanEstudioMateria()));
       rol.getAcedemicas().addAll(ejb.generarDtoAlineacionAcedemica(rol.getAlineacionAcedemica().getPlanEstudio(),rol.getTipoReg()));
        switch (rol.getTipoReg()) {
            case "Ob":
                rol.getAlineacionAcedemica().setClave("OE'IngresaNumero'-" + cvN);
                rol.getAlineacionesDescripociones().addAll(ejb.generarCatalogoObjetivosEducacionales(rol.getAlineacionAcedemica().getPlanEstudio()));
                break;
            case "Ae":
                rol.getAlineacionAcedemica().setClave("AE'IngresaNumero'-" + cvN);
                rol.getAlineacionesDescripociones().addAll(ejb.generarCatalogoAtributosEgreso(rol.getAlineacionAcedemica().getPlanEstudio()));
                break;
            case "In":
                rol.getAlineacionAcedemica().setClave("I'IngresaNumero'-" + cvN);
                rol.getAlineacionesDescripociones().addAll(ejb.generarCatalogoIndicadoresAlineacion(rol.getAlineacionAcedemica().getPlanEstudio()));
                break;
            case "Cr":
                rol.getAlineacionAcedemica().setClave("CD'IngresaNumero'-" + cvN);
                rol.getAlineacionesDescripociones().addAll(ejb.generarCatalogoCriteriosDesempenio(rol.getAlineacionAcedemica().getPlanEstudio()));
                break;
        }

        comprobarClaveDeRegistro();
        materiasp = new ArrayList<>();
        pes.forEach((t) -> {
            materiasPorPlanEstudioConsulta(t);
            materiasp.addAll(rol.getPlanEstudioMaterias());
        });
        rol.setPlanEstudioMaterias(new ArrayList<>());
        rol.getPlanEstudioMaterias().addAll(materiasp);
    }
    
    public List<PlanEstudio> continuidades(PlanEstudio pe) throws Throwable{
        List<PlanEstudio> estudios= new ArrayList<>();
        estudios.add(pe);
        AreasUniversidad au = ejbAreasLogeo.mostrarAreasUniversidad(pe.getIdPe());
        if (!au.getProgramasEducativosContinuidadList().isEmpty()) {
            au.getProgramasEducativosContinuidadList().forEach((t) -> {
                if(t.getActivo()){
                AreasUniversidad auc= new AreasUniversidad();
                try {
                    auc = ejbAreasLogeo.mostrarAreasUniversidad(t.getProgramaContinuidad());
                } catch (Throwable ex) {
                    Logger.getLogger(AdministracionPlanEstudioDirector.class.getName()).log(Level.SEVERE, null, ex);
                }
                List<PlanEstudio> pes = ejb.generarPlanesEstudio(auc);
                estudios.addAll(pes);
            }
            });
        }else{
            List<ProgramasEducativosContinuidad> continuidads=ejbAreasLogeo.listaProgramasEducativosContinuidad(pe.getIdPe());
            if(!continuidads.isEmpty()){
                continuidads.forEach((t) -> {
                    List<PlanEstudio> pes = ejb.generarPlanesEstudio(t.getProgramaTSU());
                    estudios.addAll(pes);
                });
            }
        }
        System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.AdministracionPlanEstudioDirector.continuidades()"+estudios.size());
        return estudios;
    }
    
    public void comprobarClaveDeRegistro() {
        if (rol.getAlineacionAcedemica().getIde().equals(0)) {
            rol.setNewCompetencia(Boolean.TRUE);
            if (rol.getTipoReg().equals("Ob")) {
                rol.setNewRegiistroOe(Boolean.TRUE);
                rol.setNewRegiistroIn(Boolean.FALSE);
            } else if (rol.getTipoReg().equals("In")) {
                rol.setNewRegiistroOe(Boolean.FALSE);
                rol.setNewRegiistroIn(Boolean.TRUE);
            }else{
                rol.setNewRegiistroOe(Boolean.FALSE);
                rol.setNewRegiistroIn(Boolean.FALSE);
            }
        }else{
            rol.setNewCompetencia(Boolean.FALSE);
        }
    }
    
        
        

// eventos de tablas
    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!");
    }
    
    

    
    public void metodoBase() {}
}
