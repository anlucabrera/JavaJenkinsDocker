/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;

import javax.ejb.Local;
import java.util.List;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Inscripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Login;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbUtilToolAcademicas {
    
    public Login autenticarUser(String usuario, String password);
    public void guardaGrupo(Grupo grupo, Integer noGrupos,Integer periodo,PlanEstudio planEstudio);
    public void actualizaGrupo(Grupo grupo);
    public void eliminaGrupo(Grupo grupo);
    public List<Grupo> listaByPeriodo(Integer cve_periodo);
    public List<Grupo> listaByPeriodoCarrera(Short carrera, Integer periodo);
    public List<PlanEstudio> listarPlanesXCarrera(Short carrera);
    public List<Inscripcion> getEstudianteXMatricula(String matricula);
    public List<Inscripcion> getEstudianteXAP(String apellidoPaterno);
    public AreasUniversidad buscaAreaByClave(Short area);
    
}
