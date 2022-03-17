/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbEstudiantesActivos;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class EstudiantesActivosCESaiiut extends ViewScopedRol{
    @EJB private EjbEstudiantesActivos ejb;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    @Getter private Boolean tieneAcceso = false;
    
    @PostConstruct
    public void init() {
        try{
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19) && !logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE)) return;
            cargado = true;
            
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)){
                ResultadoEJB<Estudiante> resAccesoCE = ejb.verificarEstudianteCE(Integer.parseInt(logonMB.getCurrentUser()));
                if(resAccesoCE.getCorrecto()){ 
                    tieneAcceso = true;
                }
            }else{
                ResultadoEJB<Alumnos> resAccesoSAIIUT = ejb.verificarEstudianteSAIIUT(logonMB.getCurrentUser());
                if(resAccesoSAIIUT.getCorrecto()){ 
                    tieneAcceso = true;
                }
            }
            
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso
            
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
}
