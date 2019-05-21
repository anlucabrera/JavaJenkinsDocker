/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoAlumnosCorreosTutor;
import mx.edu.utxj.pye.sgi.entity.prontuario.AlumnosCorreoinstitucional;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author Tatisz :)
 */
@Stateful
public class ServicioTutoresCorreosAlumnos implements EjbTutoresCorreosAlumnos{
    @EJB Facade f;
    @EJB Facade2 fs;

   
    
    @Override
    public List<AlumnosCorreoinstitucional> getCorreos() {
        List<AlumnosCorreoinstitucional> listCorreosAlumnos = new ArrayList<>();
        TypedQuery<AlumnosCorreoinstitucional> ac= f.getEntityManager().createQuery("SELECT a FROM AlumnosCorreoinstitucional a", AlumnosCorreoinstitucional.class);
       return listCorreosAlumnos = ac.getResultList();
    }

    @Override
    public List<AlumnosEncuestas> getAlumnosporTutor(Integer cveTutor) {
        System.out.println("Clave de maestro que recibe en ejb -->" + cveTutor);
        List<AlumnosEncuestas> listAlum = new ArrayList<>();
        TypedQuery<AlumnosEncuestas> as= fs.getEntityManager().createQuery("SELECT a FROM AlumnosEncuestas a WHERE a.cveMaestro=:cveTutor",AlumnosEncuestas.class);
        as.setParameter("cveTutor",cveTutor);
        listAlum = as.getResultList();
        System.out.println("LISTA DE ALUMNOS -->" + listAlum.size());
        return listAlum;


    }

    @Override
    public ResultadoEJB saveCorreo(AlumnosCorreoinstitucional alumn) {
        
        if (alumn == null){
            return ResultadoEJB.crearErroneo(3, "El objeto es nulo",AlumnosCorreoinstitucional.class);
        }else {
            AlumnosCorreoinstitucional newAlum = new AlumnosCorreoinstitucional();
            newAlum.setMatricula(alumn.getMatricula());
            newAlum.setCorreoInstitucional(alumn.getCorreoInstitucional());
            f.create(newAlum);
            return ResultadoEJB.crearCorrecto(newAlum, "¡Se ha agregado el correo al alumno con matricula " + newAlum.getMatricula()+ "!");
        }
    }

    
}
