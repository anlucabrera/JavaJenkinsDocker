/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;

/**
 *
 * @author Planeacion
 */
@Local
public interface EjbAdministracionEncuestaIng {
    
    public AperturaVisualizacionEncuestas getAperturaActiva();
    
    public List<AlumnosEncuestas> obtenerAlumnosNoAccedieron();
    
    public EncuestaSatisfaccionEgresadosIng obtenerResultadosEncServXMatricula(Integer matricula);
    
    public List<AlumnosEncuestas> obtenerResultadosXDirector(String cveDirector);
    
    public List<AlumnosEncuestas> obtenerAlumnosOnceavo();
    
    public List<AlumnosEncuestas> obtenerResultadosXTutor(Integer tutor);
}
