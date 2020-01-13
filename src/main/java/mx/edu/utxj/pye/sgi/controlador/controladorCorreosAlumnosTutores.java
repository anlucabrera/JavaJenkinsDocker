/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoAlumnosCorreosTutor;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestas;
import mx.edu.utxj.pye.sgi.ejb.EjbTutoresCorreosAlumnos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AlumnosCorreoinstitucional;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



/**
 *
 * @author Taatisz
 */
@Named
@ViewScoped
@ManagedBean

public class controladorCorreosAlumnosTutores extends ViewScopedRol implements Serializable{
    
    @Inject
    LogonMB logonMB;
    
   
    @EJB EjbTutoresCorreosAlumnos ejbCorreos;
    @EJB EjbAdministracionEncuestas ejbAdminEncuestas;
    
    @Getter @Setter boolean tutor;
    @Getter @Setter Integer nominaTrabajadorTutor;
    @Getter @Setter Integer claveTutor,matricula;
    @Getter @Setter List<AlumnosCorreoinstitucional> listCorreosAlumnos;
    @Getter @Setter List<AlumnosEncuestas> listAlumnosporTutor;
    @Getter @Setter List<dtoAlumnosCorreosTutor> listCorreosAlumnosporTutor = new ArrayList<>();
    @Getter @Setter String correo;
    

@Getter private Boolean cargado = false;



    @PostConstruct
    public void init(){
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        esTutor();
        listCorreosAlumnosporTutor= getListaCorreosAlumnos(); 
    }
    
    public boolean esTutor(){
        if (logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            nominaTrabajadorTutor = Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina());
            System.out.println("Numero de nómina-->"+ nominaTrabajadorTutor);
            claveTutor = logonMB.getListaUsuarioClaveNomina().getCvePersona();
              System.out.println("CLAVE PERSONA-->"+ claveTutor);
            }
        return tutor;
    }
    public List<dtoAlumnosCorreosTutor> getListaCorreosAlumnos(){
        System.out.println("---> ENTRA A GETLISTA CORREOS controlador");
        System.out.println("Numero de persona-->"+ claveTutor);
       listAlumnosporTutor = ejbCorreos.getAlumnosporTutor(claveTutor);
        if (listAlumnosporTutor.isEmpty()) {
            Messages.addGlobalInfo("No tiene alumnos tutorados a su cargo");
        }else{
            
        }
       
       listCorreosAlumnos = ejbCorreos.getCorreos();
       listCorreosAlumnosporTutor = new ArrayList<dtoAlumnosCorreosTutor>();
       listAlumnosporTutor.forEach(a ->{
           listCorreosAlumnos.forEach(c -> {
               if(a.getMatricula().equals(c.getMatricula().toString())) {
                   dtoAlumnosCorreosTutor dto = new dtoAlumnosCorreosTutor();
                   dto.setMatricula(c.getMatricula());
                   dto.setNombre(a.getNombre()+ " " + a.getApellidoPat()+ " "+ a.getApellidoMat());
                   dto.setGrado(a.getGrado());
                   dto.setGrupo(a.getIdGrupo());
                   dto.setCorreoinstitucional(c.getCorreoInstitucional());
                   listCorreosAlumnosporTutor.add(dto);
               }
           });
           
        
    });
       return listCorreosAlumnosporTutor;
    }
    
    public void saveCorreo(){
        AlumnosCorreoinstitucional newAlum = new AlumnosCorreoinstitucional();
        newAlum.setMatricula(matricula);
        newAlum.setCorreoInstitucional(correo);
        if(matricula == null || correo == null){
            Messages.addGlobalError("Debe ingresar la matricula y el correo del estudiante,", matricula);
        }else{
             ResultadoEJB <AlumnosCorreoinstitucional> res = ejbCorreos.saveCorreo(newAlum);
             mostrarMensajeResultadoEJB(res);
        } 
    }
    
      
        
    
   
    
}
