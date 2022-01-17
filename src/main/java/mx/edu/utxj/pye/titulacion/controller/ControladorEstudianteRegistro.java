/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.controller;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSelectItemCE;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.titulacion.Egresados;
import mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.DatosContacto;
import mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosExpediente;
import mx.edu.utxj.pye.sgi.entity.titulacion.DomiciliosExpediente;
import mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosNivel;
import mx.edu.utxj.pye.sgi.entity.titulacion.AntecedentesAcademicos;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosIntexp;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosGeneraciones;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Comunicaciones;
import mx.edu.utxj.pye.sgi.saiiut.entity.Domicilios;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import mx.edu.utxj.pye.titulacion.interfaces.EjbEstudianteRegistro;
import mx.edu.utxj.pye.titulacion.dto.DtoDatosTitulacion;
import mx.edu.utxj.pye.titulacion.interfaces.EjbTitulacionSeguimiento;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author ALCS
 */
@Named(value = "controladorEstReg")
@ManagedBean
@ViewScoped
public class ControladorEstudianteRegistro implements Serializable{

    private static final long serialVersionUID = -7488066163464152954L;
    
    @Getter private Boolean estOnceavo, valDatPer = false, valDatCont = false, valDatAcad = false, expValidado = false, expValidadoContinuacion = false, cargada = false;
    @Getter private String matricula;
    @Getter @Setter private Integer pestaniaActiva;
    
    @Getter @Setter private Alumnos estudiante, estudianteIngLic;
   
    @EJB private EjbEstudianteRegistro ejbEstudianteRegistro;
    
    @Inject LogonMB logonMB;
    
    /* Objetos para Datos Personales */
    @Getter @Setter private Personas nuevoOBJpersona;
    @Getter @Setter private Egresados nuevoOBJegresado;
    @Getter @Setter private ExpedientesTitulacion nuevoOBJexpediente, expedienteRegistrado, expedienteRegContinuacion;
    @Getter @Setter private DtoDatosTitulacion nuevoDTOdatTit;
    @Getter @Setter private List<String> listaGeneros;
    
    /* Objetos para Datos de Contacto y Domicilio */
    @Getter @Setter private Comunicaciones consultaOBJcelular, consultaOBJemail;
    @Getter @Setter private DatosContacto nuevoOBJdatosCont, nuevoOBJdatosContConf, datosContactoReg;
    @Getter @Setter private Domicilios consultaOBJdomicilio;
    @Getter @Setter private DomiciliosExpediente nuevoOBJdomicilio, domicilioReg;
    @Getter @Setter private String email;
    
    /* Para selectOneMenu de Estado, Municipio y Localidad */
    @Getter @Setter private List<SelectItem> listaEstadosDomicilioRadica, listaMunicipiosRadica, listaAsentamientos, listaEstadosIEMS, listaMunicipiosIEMS, listaLocalidadesIEMS, listaIEMS;
    @EJB EJBSelectItems eJBSelectItems;
    
    /* Objetos para Documentos del Expediente */
    @Getter @Setter private List<DocumentosNivel> listaDocsPorNivel;
    @Getter @Setter private DocumentosExpediente nuevoOBJdocExp, fotografiaCargada;
    @Inject UtilidadesCH utilidadesCH;
    @Getter @Setter private Integer claveDoc;
    @Getter @Setter private Part file; 
    @EJB private EjbTitulacionSeguimiento ejbTitulacionSeguimiento;
    
    /* Objetos para Antecedentes Académicos */
    @Getter @Setter private AntecedentesAcademicos nuevoOBJantAcad;
    @Getter @Setter private Integer estadoIEMS, municipioIEMS, localidadIEMS;
    @EJB EjbSelectItemCE ejbItemCE;
    @EJB EjbFichaAdmision ejbFichaAdmision;
    
    /* ProgressBar y Cantidad de Documentos que debe contener el expediente */
    @Getter @Setter Integer progresoExpediente;
    @Getter @Setter Integer numTotalDocs;
    
    /* Proceso Activo */
    @Getter @Setter private ProcesosIntexp  procesosIntexp;
   
    @PostConstruct
    public void init() {
        try {
            matricula = logonMB.getCurrentUser();
            System.err.println("init - matricula " + matricula);
            estudiante = ejbEstudianteRegistro.obtenerInformacionAlumno(matricula);
            System.err.println("init - estudiante " + estudiante.getAlumnosPK().getCveAlumno());
            selectGeneros();
//            if (estudiante.getGradoActual() == 6 || estudiante.getGradoActual() == 11) {
//                procesosIntexp = ejbEstudianteRegistro.obtenerClaveProcesoIntExp(estudiante);
//
//                if (procesosIntexp == null) {
//                    cargada = false;
//                } else {
//                    cargada = true;
//
//                    progresoExpediente = 0;
//                    datosPerVal();
//                    datosContVal();
//                    datosAntAcad();
//                    consultarStatusExpediente();
//                    listaEstadosDomicilioRadica = eJBSelectItems.itemEstados();
//                    listaEstadosIEMS = eJBSelectItems.itemEstados();
//                   
//                }
//
//            } else {
//                cargada = false;
//            }
            
            
            //if (estudiante.getGradoActual() == 5 || estudiante.getGradoActual() == 6 || estudiante.getGradoActual() == 11 || estudiante.getGradoActual() == 10) {
            if (estudiante.getGradoActual() == 5 || estudiante.getGradoActual() == 6 || (estudiante.getGradoActual() == 11 && estudiante.getGrupos().getGruposPK().getCvePeriodo() <56)) {
                procesosIntexp = ejbEstudianteRegistro.obtenerClaveProcesoIntExp(estudiante);

                if (procesosIntexp == null) {
                    cargada = false;
                } else {
                    cargada = true;

                    progresoExpediente = 0;
                    datosPerVal();
                    datosContVal();
                    datosAntAcad();
                    consultarStatusExpediente();
                    listaEstadosDomicilioRadica = eJBSelectItems.itemEstados();
                    listaEstadosIEMS = eJBSelectItems.itemEstados();
                   
                }
            } else if (estudiante.getGradoActual() == 7 || estudiante.getGradoActual() == 8 || estudiante.getGradoActual() == 9) {
                estudiante = ejbEstudianteRegistro.obtenerInformacionTSUAlumno(matricula);
                if (estudiante == null) {
                    cargada = false;
                } else {
                    procesosIntexp = ejbEstudianteRegistro.obtenerClaveProcesoIntExp(estudiante);

                    if (procesosIntexp == null) {
                        cargada = false;
                    } else {
                        cargada = true;

                        progresoExpediente = 0;
                        datosPerVal();
                        datosContVal();
                        datosAntAcad();
                        consultarStatusExpediente();
                        listaEstadosDomicilioRadica = eJBSelectItems.itemEstados();
                        listaEstadosIEMS = eJBSelectItems.itemEstados();

                    }
                }
                
            } 
            
            else if (estudiante.getGradoActual() == 11 && estudiante.getGrupos().getGruposPK().getCvePeriodo() == 56) {
                expedienteRegistrado = ejbEstudianteRegistro.buscarExpedienteTSU(estudiante);
                if(expedienteRegistrado == null) {
                    estudiante = ejbEstudianteRegistro.obtenerInformacionTSUAlumno(matricula);
                    
                    if (estudiante == null) {
                        cargada = false;
                    } else {
                        procesosIntexp = ejbEstudianteRegistro.obtenerClaveProcesoIntExp(estudiante);

                        if (procesosIntexp == null) {
                        cargada = false;
                        } else {
                            cargada = true;

                            progresoExpediente = 0;
                            datosPerVal();
                            datosContVal();
                            datosAntAcad();
                            consultarStatusExpediente();
                            listaEstadosDomicilioRadica = eJBSelectItems.itemEstados();
                            listaEstadosIEMS = eJBSelectItems.itemEstados();

                        }
                    }
                
                }else {
                    fotografiaCargada = ejbEstudianteRegistro.buscarFotografiaExpedienteTSU(expedienteRegistrado);
                    if (fotografiaCargada == null) {
                        estudiante = ejbEstudianteRegistro.obtenerInformacionTSUAlumno(matricula);

                        if (estudiante == null) {
                            cargada = false;
                        } else {
                            procesosIntexp = ejbEstudianteRegistro.obtenerClaveProcesoIntExp(estudiante);

                            if (procesosIntexp == null) {
                                cargada = false;
                            } else {
                                cargada = true;

                                progresoExpediente = 0;
                                datosPerVal();
                                datosContVal();
                                datosAntAcad();
                                consultarStatusExpediente();
                                listaEstadosDomicilioRadica = eJBSelectItems.itemEstados();
                                listaEstadosIEMS = eJBSelectItems.itemEstados();

                            }
                        }

                    } else {
                        estudiante = ejbEstudianteRegistro.obtenerInformacionAlumno(matricula);
                        procesosIntexp = ejbEstudianteRegistro.obtenerClaveProcesoIntExp(estudiante);

                        if (procesosIntexp == null) {
                            cargada = false;
                        } else {
                            cargada = true;
                            expedienteRegContinuacion = ejbEstudianteRegistro.buscarExpedienteContinuacion(estudiante, procesosIntexp.getProceso());
                            if(expedienteRegContinuacion==null){
                                nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);
                                guardarExpedienteContinuacion(nuevoOBJegresado,procesosIntexp,estudiante,expedienteRegistrado);
                                progresoExpediente = 80;
                                consultarExpedienteContinuacion();
//                                consultarStatusExpedienteContinuacion();
//                                consultarRegistroDatosPer(matricula);
//                                nuevoOBJexpediente = expedienteRegContinuacion;
//                                nuevoOBJdomicilio = ejbEstudianteRegistro.buscarDomicilioExpediente(expedienteRegContinuacion);
//                                nuevoOBJdatosCont = ejbEstudianteRegistro.buscarDatosContactoExpediente(expedienteRegContinuacion);
//                                consultarRegistroAntAcad(matricula);
//                                listaEstadosDomicilioRadica = eJBSelectItems.itemEstados();
//                                listaEstadosIEMS = eJBSelectItems.itemEstados();
//                                selectMunicipio();
//                                selectAsentamiento();
                                
                            }else{
                                if(ejbEstudianteRegistro.buscarFotografiaExpedienteContinuidad(expedienteRegContinuacion)== null){
                                    progresoExpediente = 80;
                                }else{
                                    progresoExpediente = 100;
                                }
//                                consultarStatusExpedienteContinuacion();
                                nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);
                                consultarExpedienteContinuacion();
//                                consultarRegistroDatosPer(matricula);
//                                nuevoOBJexpediente = expedienteRegContinuacion;
//                                nuevoOBJdomicilio = ejbEstudianteRegistro.buscarDomicilioExpediente(expedienteRegContinuacion);
//                                nuevoOBJdatosCont = ejbEstudianteRegistro.buscarDatosContactoExpediente(expedienteRegContinuacion);
//                                consultarRegistroAntAcad(matricula);  
//                                listaEstadosDomicilioRadica = eJBSelectItems.itemEstados();
//                                listaEstadosIEMS = eJBSelectItems.itemEstados();    
//                                selectMunicipio();
//                                selectAsentamiento();
                                    
                            }

                        }
                    }
                
                }
                
            } 
            
            else if (estudiante.getGradoActual() == 11 && estudiante.getGrupos().getGruposPK().getCvePeriodo() == 59) {
                expedienteRegistrado = ejbEstudianteRegistro.buscarExpedienteTSU1820(estudiante);
               
                if(expedienteRegistrado == null) {
                    estudiante = ejbEstudianteRegistro.obtenerInformacionTSUAlumno(matricula);
                     
                    if (estudiante == null) {
                        cargada = false;
                    } else {
                        
                        if(estudiante.getGrupos().getGruposPK().getCvePeriodo() >= 51){
                            procesosIntexp = ejbEstudianteRegistro.obtenerClaveProcesoIntExp(estudiante);

                            if (procesosIntexp == null) {
                                cargada = false;
                            } else {
                                cargada = true;

                                progresoExpediente = 0;
                                datosPerVal();
                                datosContVal();
                                datosAntAcad();
                                consultarStatusExpediente();
                                listaEstadosDomicilioRadica = eJBSelectItems.itemEstados();
                                listaEstadosIEMS = eJBSelectItems.itemEstados();

                            }
                        }else{
                            estudiante = ejbEstudianteRegistro.obtenerInformacionAlumno(matricula);
                            procesosIntexp = ejbEstudianteRegistro.obtenerClaveProcesoIntExp(estudiante);

                            if (procesosIntexp == null) {
                                cargada = false;
                            } else {
                                cargada = true;
                                progresoExpediente = 0;
                                datosPerVal();
                                datosContVal();
                                datosAntAcad();
                                consultarStatusExpediente();
                                listaEstadosDomicilioRadica = eJBSelectItems.itemEstados();
                                listaEstadosIEMS = eJBSelectItems.itemEstados();
                                
                            }
                        }
                    }
                    
                    
                
                }else {
                    fotografiaCargada = ejbEstudianteRegistro.buscarFotografiaExpedienteTSU(expedienteRegistrado);
                    if (fotografiaCargada == null) {
                        estudiante = ejbEstudianteRegistro.obtenerInformacionTSUAlumno(matricula);

                        if (estudiante == null) {
                            cargada = false;
                        } else {
                            procesosIntexp = ejbEstudianteRegistro.obtenerClaveProcesoIntExp(estudiante);

                            if (procesosIntexp == null) {
                                cargada = false;
                            } else {
                                cargada = true;

                                progresoExpediente = 0;
                                datosPerVal();
                                datosContVal();
                                datosAntAcad();
                                consultarStatusExpediente();
                                listaEstadosDomicilioRadica = eJBSelectItems.itemEstados();
                                listaEstadosIEMS = eJBSelectItems.itemEstados();

                            }
                        }

                    } else {
                        estudiante = ejbEstudianteRegistro.obtenerInformacionAlumno(matricula);
                        procesosIntexp = ejbEstudianteRegistro.obtenerClaveProcesoIntExp(estudiante);

                        if (procesosIntexp == null) {
                            cargada = false;
                        } else {
                            cargada = true;
                            expedienteRegContinuacion = ejbEstudianteRegistro.buscarExpedienteContinuacion(estudiante, procesosIntexp.getProceso());
                            if(expedienteRegContinuacion==null){
                                nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);
                                guardarExpedienteContinuacion(nuevoOBJegresado,procesosIntexp,estudiante,expedienteRegistrado);
                                progresoExpediente = 80;
                                consultarExpedienteContinuacion();
                                
                            }else{
                                if(ejbEstudianteRegistro.buscarFotografiaExpedienteContinuidad(expedienteRegContinuacion)== null){
                                    progresoExpediente = 80;
                                }else{
                                    progresoExpediente = 100;
                                }
                                nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);
                                consultarExpedienteContinuacion();
                                    
                            }

                        }
                    }
                
                }
                
            }
            
           else {
                 cargada = false;
            }
        

        } catch (Exception e) {
            cargada = false;
        }
    }
    
    /* Métodos para verificar si el estudiante ha validado cada pestaña de datos */
    
    public void datosPerVal(){
        if(ejbEstudianteRegistro.mostrarDatosPersonales(matricula) != null){
            valDatPer= true;
            consultarRegistroDatosPer(matricula);
            progresoExpediente = 15;
        }
        else{
            valDatPer= false;
            consultarSAIIUTDatosPer(estudiante);
            progresoExpediente =0;
        }
    }
    
     public void datosContVal(){
        if(ejbEstudianteRegistro.mostrarRegistroDatosContacto(estudiante) != null && ejbEstudianteRegistro.mostrarDomicilio(estudiante) != null){
            valDatCont= true;
            consultarRegistroDatosCont(estudiante);
            progresoExpediente = 30;
        }
        else{
            valDatCont= false;
            nuevoOBJdomicilio = new DomiciliosExpediente();
            consultarSAIIUTDatosCont(estudiante);
            progresoExpediente = 15;
        }
    }
     
     public void datosAntAcad(){
        if(ejbEstudianteRegistro.mostrarAntecedentesAcademicos(matricula) != null){
            valDatAcad= true;
            consultarRegistroAntAcad(matricula);
            progresoExpediente = 45;
        }
        else{
            valDatAcad= false;
            nuevoOBJantAcad = new  AntecedentesAcademicos();
            nuevoOBJantAcad.setGradoAcademico("Bachillerato");
            progresoExpediente = 30;
        }
    }
   
    public void docsPorNivel(){
        listaDocsPorNivel = ejbEstudianteRegistro.getListaDocsPorNivel(estudiante);
        if(listaDocsPorNivel == null || listaDocsPorNivel.isEmpty()){
            Messages.addGlobalWarn("No se han definido documentos relacionados a tu nivel Educativo");
        }
    }
    
    /* Combos para lista de Géneros */
    public void selectGeneros(){
        listaGeneros = Arrays.asList("M", "F");
    }
    
    /* Combos para domicilio */
    public void selectMunicipio(){
        listaMunicipiosRadica = eJBSelectItems.itemMunicipiosByClave(this.nuevoOBJdomicilio.getEstado());
    }
    
    public void selectAsentamiento(){
        listaAsentamientos = eJBSelectItems.itemAsentamientoByClave(nuevoOBJdomicilio.getEstado(), nuevoOBJdomicilio.getMunicipio());
    }
    
    /* Combos para IEMS */
    public void selectMunicipioIems(){
        listaMunicipiosIEMS = eJBSelectItems.itemMunicipiosByClave(this.estadoIEMS);
        selectLocalidadIems();
    }
    
  
    public void cambiarEstado(ValueChangeEvent e){
       setEstadoIEMS((Integer)e.getNewValue());
       municipioIEMS = 1;
       localidadIEMS = 1;
       nuevoOBJantAcad.setIems(0);
       selectMunicipioIems();
    }
    
    public void cambiarMunicipio(ValueChangeEvent e){
       setMunicipioIEMS((Integer)e.getNewValue());
       selectLocalidadIems();
    }
    
    public void cambiarLocalidad(ValueChangeEvent e){
       setLocalidadIEMS((Integer)e.getNewValue());
       selectIems();
    }
    

    public void selectLocalidadIems(){
        listaLocalidadesIEMS = eJBSelectItems.itemLocalidadesByClave(this.estadoIEMS,this.municipioIEMS);
        selectIems();
    }
    
    public void selectIems(){
            listaIEMS = ejbItemCE.itemIems(this.estadoIEMS, this.municipioIEMS, this.localidadIEMS);  
    }
   
    /* Métodos de la pestaña Datos Personales */
    
    public void consultarSAIIUTDatosPer(Alumnos estudiante) {
        try {
            nuevoDTOdatTit = ejbEstudianteRegistro.obtenerDatosAcadSAIIUT(estudiante);
            nuevoOBJpersona = ejbEstudianteRegistro.mostrarDatosPersonalesSAIIUT(estudiante);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstudianteRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void consultarRegistroDatosPer(String matricula) {
        try {
            nuevoDTOdatTit = ejbEstudianteRegistro.obtenerDatosAcadSAIIUT(estudiante);
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstudianteRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardarDatosPersonales() {
        ProcesosGeneraciones procGen = ejbEstudianteRegistro.obtenerGeneracionProcIntExp(procesosIntexp.getProceso());
        nuevoOBJegresado = new Egresados();
        nuevoOBJexpediente = new ExpedientesTitulacion();
        try {
            nuevoOBJegresado.setMatricula(matricula);
            nuevoOBJegresado.setNombre(nuevoOBJpersona.getNombre());
            nuevoOBJegresado.setApellidoPaterno(nuevoOBJpersona.getApellidoPat());
            nuevoOBJegresado.setApellidoMaterno(nuevoOBJpersona.getApellidoMat());
            nuevoOBJegresado.setCurp(nuevoOBJpersona.getCurp());
            nuevoOBJegresado.setGenero(nuevoOBJpersona.getSexo().toString());
            nuevoOBJegresado.setFechaNacimiento(nuevoOBJpersona.getFechaNacimiento());
            nuevoOBJegresado = ejbEstudianteRegistro.guardarDatosPersonales(nuevoOBJegresado);
            
            nuevoOBJexpediente.setProceso(procesosIntexp);
            nuevoOBJexpediente.setFecha(new Date());
            nuevoOBJexpediente.setMatricula(nuevoOBJegresado);
            nuevoOBJexpediente.setNivel(ejbEstudianteRegistro.obtenerNivelyProgEgresado(estudiante).getNivel());
            nuevoOBJexpediente.setProgramaEducativo(ejbEstudianteRegistro.obtenerNivelyProgEgresado(estudiante).getPrograma());
            nuevoOBJexpediente.setGeneracion(procGen.getProcesosGeneracionesPK().getGeneracion());
            nuevoOBJexpediente = ejbEstudianteRegistro.guardarExpedienteTitulacion(nuevoOBJexpediente);
            
            pestaniaActiva = 1;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstudianteRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
        datosPerVal();
    }
    
    public void actualizarDatosPersonales() {
        try {
            nuevoOBJegresado.setMatricula(matricula);
            nuevoOBJegresado.setNombre(nuevoOBJegresado.getNombre());
            nuevoOBJegresado.setApellidoPaterno(nuevoOBJegresado.getApellidoPaterno());
            nuevoOBJegresado.setApellidoMaterno(nuevoOBJegresado.getApellidoMaterno());
            nuevoOBJegresado.setCurp(nuevoOBJegresado.getCurp());
            nuevoOBJegresado.setGenero(nuevoOBJegresado.getGenero());
            nuevoOBJegresado.setFechaNacimiento(nuevoOBJegresado.getFechaNacimiento());
            nuevoOBJegresado = ejbEstudianteRegistro.actualizarDatosPersonales(nuevoOBJegresado);
            
            pestaniaActiva = 0;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstudianteRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /* Métodos de la pestaña Datos de Contacto y Domicilio */
    
    public void consultarSAIIUTDatosCont(Alumnos estudiante) {
        try {
            consultaOBJcelular = ejbEstudianteRegistro.mostrarCelularSAIIUT(estudiante);
            consultaOBJemail = ejbEstudianteRegistro.mostrarEmailSAIIUT(estudiante);
            consultaOBJdomicilio = ejbEstudianteRegistro.mostrarDomicilioSAIIUT(estudiante);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstudianteRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void consultarRegistroDatosCont(Alumnos estudiante) {
        try {
            nuevoOBJdatosCont = ejbEstudianteRegistro.mostrarRegistroDatosContacto(estudiante);
            nuevoOBJdomicilio = ejbEstudianteRegistro.mostrarDomicilio(estudiante);
            selectMunicipio();
            selectAsentamiento();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstudianteRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void actualizarDatosContDom(){
        try{
            nuevoOBJdatosCont.setDato(nuevoOBJdatosCont.getDato());
            nuevoOBJdatosCont.setExpediente(nuevoOBJdatosCont.getExpediente());
            nuevoOBJdatosCont.setCelular(ejbEstudianteRegistro.extraerNumeros(nuevoOBJdatosCont.getCelular()));
            nuevoOBJdatosCont.setEmail(nuevoOBJdatosCont.getEmail());
            nuevoOBJdatosCont = ejbEstudianteRegistro.actualizarDatosContacto(nuevoOBJdatosCont);
            
            nuevoOBJdomicilio = ejbEstudianteRegistro.actualizarDomicilio(nuevoOBJdomicilio);
            pestaniaActiva = 1;
         } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstudianteRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void guardaComunicacionDomicilio(){
        nuevoOBJdatosCont = new DatosContacto();
        try {
            nuevoOBJdomicilio.setExpediente(ejbEstudianteRegistro.obtenerClaveExpediente(estudiante));
            nuevoOBJdomicilio = ejbEstudianteRegistro.guardarDomicilio(nuevoOBJdomicilio);
           
            nuevoOBJdatosCont.setExpediente(ejbEstudianteRegistro.obtenerClaveExpediente(estudiante));
            nuevoOBJdatosCont.setCelular(ejbEstudianteRegistro.extraerNumeros(consultaOBJcelular.getDato()));
            nuevoOBJdatosCont.setEmail(consultaOBJemail.getDato());
            nuevoOBJdatosCont = ejbEstudianteRegistro.guardarDatosContacto(nuevoOBJdatosCont);
            
            pestaniaActiva = 2;
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstudianteRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
        datosContVal();
    }
     
     public void consultarRegistroAntAcad(String matricula) {
        try {
            nuevoOBJantAcad = ejbEstudianteRegistro.mostrarAntecedentesAcademicos(matricula);
            Iems iems = new Iems();
            iems = ejbFichaAdmision.buscaIemsByClave(nuevoOBJantAcad.getIems());
            estadoIEMS = iems.getLocalidad().getLocalidadPK().getClaveEstado();
            municipioIEMS = iems.getLocalidad().getLocalidadPK().getClaveMunicipio();
            localidadIEMS = iems.getLocalidad().getLocalidadPK().getClaveLocalidad();
            selectMunicipioIems();
            selectLocalidadIems();
            selectIems();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstudianteRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
     public void guardaDatosAcademicos(){
        try {
            nuevoOBJantAcad.setMatricula(nuevoOBJegresado);
            ejbEstudianteRegistro.guardarAntecedentesAcad(nuevoOBJantAcad);
           
            pestaniaActiva = 3;
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstudianteRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
        datosAntAcad();     
        
    }
     
    public void actualizarDatosAcademicos(){
        try{
            nuevoOBJantAcad = ejbEstudianteRegistro.actualizarAntecedentesAcad(nuevoOBJantAcad);
            pestaniaActiva = 2;
         } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstudianteRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<DocumentosExpediente> consultarStatusDocumento(Integer claveDoc){
        return ejbTitulacionSeguimiento.getListaStatusPorDocumento(claveDoc);
    }
    
    public void consultarStatusExpediente(){
        if(ejbEstudianteRegistro.consultarStatusExpediente(matricula) != null){
            expValidado= true;
        }
        else{
            expValidado= false;
        }
    }
    
     public void consultarStatusExpedienteContinuacion(){
        if(ejbEstudianteRegistro.consultarStatusExpedienteContinuacion(matricula, procesosIntexp.getProceso()) != null){
            expValidadoContinuacion= true;
        }
        else{
            expValidadoContinuacion= false;
        }
    }
    
     public void buscarExpedienteContinuacion(){
        if(ejbEstudianteRegistro.consultarStatusExpedienteContinuacion(matricula, procesosIntexp.getProceso()) != null){
            expValidadoContinuacion= true;
        }
        else{
            expValidadoContinuacion= false;
        }
    } 
     
//    public void progressBarDocumentos(List<DocumentosExpediente> listaDocsExp, ExpedientesTitulacion expediente){
//        if ("LTF".equals(expediente.getProgramaEducativo())) {
//            
//            if (listaDocsExp.size() == 10) {
//
//                progresoExpediente = 100;
//            } else {
//
//                progresoExpediente = 45;
//            }
//            numTotalDocs = 10;
//        } else {
//
//            if (listaDocsExp.size() == 9) {
//
//                progresoExpediente = 100;
//            } else {
//
//                progresoExpediente = 45;
//            }
//            
//            numTotalDocs = 9;
//        }
//    }
    
    public void progressBarDocumentos(List<DocumentosExpediente> listaDocsExp, ExpedientesTitulacion expediente){
        
        if (valDatPer == true && valDatCont == true && valDatAcad == true) {
            if (listaDocsExp.size() == 1) {

                progresoExpediente = 100;
            } else {

                progresoExpediente = 45;
            }
        }
            numTotalDocs = 1;
    }
    
    public void guardarExpedienteContinuacion(Egresados egresado, ProcesosIntexp procesoInt, Alumnos estudianteCont, ExpedientesTitulacion expedienteInicio) {
        System.err.println("guardarExpedienteContinuacion - egresado " + egresado.getMatricula());
        System.err.println("guardarExpedienteContinuacion - procesoInt " + procesoInt.getProceso());
        System.err.println("guardarExpedienteContinuacion - estudianteCont " + estudianteCont.getAlumnosPK().getCveAlumno());
        System.err.println("guardarExpedienteContinuacion - expedienteInicio " + expedienteInicio.getExpediente());
        
        ProcesosGeneraciones procGen = ejbEstudianteRegistro.obtenerGeneracionProcIntExp(procesoInt.getProceso());
        nuevoOBJexpediente = new ExpedientesTitulacion();
        try {
            nuevoOBJexpediente.setProceso(procesoInt);
            nuevoOBJexpediente.setFecha(new Date());
            nuevoOBJexpediente.setMatricula(egresado);
            nuevoOBJexpediente.setNivel(ejbEstudianteRegistro.obtenerNivelyProgEgresado(estudianteCont).getNivel());
            nuevoOBJexpediente.setProgramaEducativo(ejbEstudianteRegistro.obtenerNivelyProgEgresado(estudianteCont).getPrograma());
            nuevoOBJexpediente.setGeneracion(procGen.getProcesosGeneracionesPK().getGeneracion());
            nuevoOBJexpediente = ejbEstudianteRegistro.guardarExpedienteTitulacion(nuevoOBJexpediente);
            
            guardaComunicacionDomicilioContinuacion(nuevoOBJexpediente, expedienteInicio);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstudianteRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardaComunicacionDomicilioContinuacion(ExpedientesTitulacion expedientesTitulacion, ExpedientesTitulacion expedienteInicio){
        nuevoOBJdomicilio = new  DomiciliosExpediente();
        nuevoOBJdatosCont = new DatosContacto();
        try {
            domicilioReg = ejbEstudianteRegistro.buscarDomicilioExpediente(expedienteInicio);
            nuevoOBJdomicilio.setExpediente(expedientesTitulacion);
            nuevoOBJdomicilio.setCalle(domicilioReg.getCalle());
            nuevoOBJdomicilio.setColonia(domicilioReg.getColonia());
            nuevoOBJdomicilio.setNumExterior(domicilioReg.getNumExterior());
            nuevoOBJdomicilio.setNumInterior(domicilioReg.getNumInterior());
            nuevoOBJdomicilio.setEstado(domicilioReg.getEstado());
            nuevoOBJdomicilio.setMunicipio(domicilioReg.getMunicipio());
            nuevoOBJdomicilio.setLocalidad(domicilioReg.getLocalidad());
            nuevoOBJdomicilio = ejbEstudianteRegistro.guardarDomicilio(nuevoOBJdomicilio);
            
            datosContactoReg = ejbEstudianteRegistro.buscarDatosContactoExpediente(expedienteInicio);
            nuevoOBJdatosCont.setExpediente(expedientesTitulacion);
            nuevoOBJdatosCont.setCelular(datosContactoReg.getCelular());
            nuevoOBJdatosCont.setEmail(datosContactoReg.getEmail());
            nuevoOBJdatosCont = ejbEstudianteRegistro.guardarDatosContacto(nuevoOBJdatosCont);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstudianteRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void consultarExpedienteContinuacion(){
        try {
            expedienteRegContinuacion = ejbEstudianteRegistro.buscarExpedienteContinuacion(estudiante, procesosIntexp.getProceso());
            consultarStatusExpedienteContinuacion();
            consultarRegistroDatosPer(matricula);
            datosPerVal();
            datosContVal();
            datosAntAcad();
            nuevoOBJexpediente = expedienteRegContinuacion;
            nuevoOBJdomicilio = ejbEstudianteRegistro.buscarDomicilioExpediente(expedienteRegContinuacion);
            nuevoOBJdatosCont = ejbEstudianteRegistro.buscarDatosContactoExpediente(expedienteRegContinuacion);
            consultarRegistroAntAcad(matricula);
            listaEstadosDomicilioRadica = eJBSelectItems.itemEstados();
            listaEstadosIEMS = eJBSelectItems.itemEstados();
            selectMunicipio();
            selectAsentamiento();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEstudianteRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
