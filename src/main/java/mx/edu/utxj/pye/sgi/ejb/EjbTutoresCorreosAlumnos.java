/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.prontuario.AlumnosCorreoinstitucional;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;


/**
 *
 * @author Taatiz :)
 */
@Local
public interface EjbTutoresCorreosAlumnos {
    
    public List<AlumnosCorreoinstitucional> getCorreos();
    
    public List<AlumnosEncuestas> getAlumnosporTutor(Integer cveTutor);
    
    public ResultadoEJB saveCorreo(AlumnosCorreoinstitucional alumn);
    
    
    
}
