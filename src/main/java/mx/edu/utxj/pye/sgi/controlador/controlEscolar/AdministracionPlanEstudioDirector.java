/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPlanEstudio;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroPlanesEstudioRolDirector;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author UTXJ
 */

@Named
@ViewScoped
public class AdministracionPlanEstudioDirector extends ViewScopedRol implements Desarrollable{
    @Getter @Setter RegistroPlanesEstudioRolDirector rol;
    
    @EJB EjbPropiedades ep;
    @EJB EjbRegistroPlanEstudio ejb;
    @Inject LogonMB logon;
    
    @Getter Boolean tieneAcceso = false;
            
    @PostConstruct
    public void init(){
        try {
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDirector(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
            
            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarDirector(logon.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
            
            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new RegistroPlanesEstudioRolDirector(filtro, director, director.getAreaOficial());
            tieneAcceso = rol.tieneAcceso(director);
            
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            
            ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> resProgramaPlan = ejb.getProgramasEducativos(director);
            if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
            rol.setAreaPlanEstudioMap(resProgramaPlan.getValor());
            
            rol.setPlanEstudio1(new DtoPlanEstudio(new PlanEstudio(), rol.getPrograma()));
            
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
    
    public void guardar(){
       
    }
}
