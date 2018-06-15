/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
 import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestasTsu;

/**
 *
 * @author Planeacion
 */
@Local
public interface EjbAdministracionEncuestaTsu {
    
    public ResultadosEncuestaSatisfaccionTsu getResultadoEncPorEvaluador(Integer matricula);
    
    public List<AlumnosEncuestasTsu> obtenerListaAlumnosNoAccedieron();
    
    public List<AlumnosEncuestasTsu> obtenerAlumnosPorDirector(String cveDirector);
    
    public Alumnos getAlumnoEvaluadorTsu(String matricula) ;
}
