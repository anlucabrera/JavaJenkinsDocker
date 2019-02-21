/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaAlumnosPye;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbAdministracionTutores {
    
    public PeriodosEscolares getPeriodoActual();
    
    public List<Grupos> esTutor(Integer maestro, Integer periodo);
    
    
    
    public List<Alumnos> getListaDeAlumnosPorDocente (Integer grupo);
    
    public List<VistaAlumnosPye>findAllByMatricula(String matricula);
    
    public List<Grupos> estTutordeGrupo(Integer cvePersona);
    
//    public List<String> getResultadosEvaluacion(String matricula, Integer periodo);
//    
//    public List<String> getDatosSinEvaluar(String matricula, Integer periodo);
//    
    
}
