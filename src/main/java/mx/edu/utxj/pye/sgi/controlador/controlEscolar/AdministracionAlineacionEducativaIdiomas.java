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
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAlineacionAcedemica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaMetasPropuestas;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroEvidInstEvalMaterias;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.ServicioRegAliEdu;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MetasPropuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosContinuidad;
import mx.edu.utxj.pye.sgi.enums.RegistroSiipEtapa;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */

@Named
@ViewScoped
public class AdministracionAlineacionEducativaIdiomas extends ViewScopedRol implements Desarrollable{
    @Getter @Setter RegistroPlanesEstudioRolDirector rol;
    @Getter @Setter PlanEstudioMateria pem;
    
    @EJB EjbPropiedades ep;
    @EJB EjbRegistroPlanEstudio ejb;
    @EJB EjbRegistroEvidInstEvalMaterias ereiem;
    @EJB EjbAreasLogeo ejbAreasLogeo;
    @Inject LogonMB logon;
    
    @Getter Boolean tieneAcceso = false;
    @Getter @Setter Integer activaPestania = 0;
            
    
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
            setVistaControlador(ControlEscolarVistaControlador.ADMINISTRACION_ALINEACION_ACADEMICA_IDIOMAS);

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ereiem.validarRolesRegistroEvidInstEvaluacion(logon.getPersonal().getClave());

            if (!resValidacion.getCorrecto()) {
                mostrarMensajeResultadoEJB(resValidacion);
                return;
            }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new RegistroPlanesEstudioRolDirector(filtro, director, director.getAreaOficial());
            tieneAcceso = rol.tieneAcceso(director);
            if(director.getAreaOperativa().getArea()!=Short.parseShort("23")){
                tieneAcceso=Boolean.FALSE;
            }
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu

            ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> resProgramaPlan = ejb.getProgramasEducativostotal();
            
            
            if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
            rol.setAreaPlanEstudioMap(resProgramaPlan.getValor());
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.AdministracionAlineacionEducativaIdiomas.init()"+rol.getAreaPlanEstudioMap());
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.AdministracionAlineacionEducativaIdiomas.init()"+rol.getPlanEstudio());
            rol.setAlineacionAcedemica(new DtoAlineacionAcedemica.Presentacion(0, "", "", "", 0D, new PlanEstudio(), new PlanEstudioMateria(),new AreasUniversidad(),""));
            rol.setAlineacionesDescripociones(new ArrayList<>());
            rol.setTipoReg("Ob");
            rol.setAcedemicas(new ArrayList<>());
            rol.setMateriaMetasPropuestas(new DtoMateriaMetasPropuestas(new MetasPropuestas(), new PlanEstudioMateria(), new PlanEstudio()));
            rol.setPropuestases(new ArrayList<>());

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
    
     public void nivelesDesempenioPlanEstudioMateriaConsulta() {
        rol.setPropuestases(new ArrayList<>());
        ResultadoEJB<List<MetasPropuestas>> resMetas = ejb.getMateriasMetasIdiomas(rol.getMateriaMetasPropuestas().getPlanEstudio());
        if (!resMetas.getCorrecto()) {
            mostrarMensajeResultadoEJB(resMetas);
        }
        rol.setPropuestases(resMetas.getValor());   
    }
    
     public void comprobarTipoRegistro() throws Throwable {
       rol.setAcedemicas(new ArrayList<>());
       rol.getAcedemicas().addAll(ejb.generarDtoAlineacionAcedemicaIdiomas(rol.getAlineacionAcedemica().getPlanEstudio(),rol.getTipoReg()));
    }
     
    public void metodoBase() {}
}
