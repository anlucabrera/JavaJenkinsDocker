/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.DatosGraficaEncuestaEgresados;
import mx.edu.utxj.pye.sgi.entity.ch.DatosGraficaEncuestaServicio;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;

/**
 *
 * @author Planeación
 */
@Local
public interface EjbAdministracionEncuesta {
     
    public List<Personal> esDirectorDeCarrera(Integer areaSup, Integer actividad, Integer catOp, Integer clave)  ;

    public PeriodosEscolares getPeriodo(Evaluaciones evaluacion) ;
    
    public Alumnos getAlumnoEvaluador(String matricula) ;
    
    public Alumnos getAlumnoEvaluadorTsu(String matricula) ;

    public List<Alumnos> obtenerAlumnosPorTutor(Integer grupo) ;
    
    public List<DatosGraficaEncuestaEgresados> listaDatosAvanceEncEgresados();
    
    public List<DatosGraficaEncuestaServicio> listaAvanceEncServ();
    
    public Grupos obtenerCuatriPorTutor(Integer cveMaestro);
    /**
     * obtiene todos los datos de la encuesta de servicios
     * @return 
     */
    public List<EncuestaServiciosResultados> getEncuestaServicios();
    
    public List<EncuestaSatisfaccionEgresadosIng> getEncuestaSatisfEgre();
    
    public EncuestaServiciosResultados getEncuestaporevaluador(Integer matricula);
    
    public EncuestaSatisfaccionEgresadosIng getEncuestaEgreporEvaluador(Integer matricula) ;
    
    public List<AlumnosEncuestas> obtenerAlumno(String cveMaestro) ;
    
    public List<AlumnosEncuestas> obtenerTodoAlumno(Integer cveTutor);

    public List<AlumnosEncuestas> obtenerListaAlumnosNoAccedieron();
    
    public List<Personal> esSecretarioAcademico(Integer areaSup,Short actividad,Short catOp,Integer clave);
    
    public List<Personal> esPlaneacion(Integer areaSup, Short actividad, Short catOp, Integer clave);
}
