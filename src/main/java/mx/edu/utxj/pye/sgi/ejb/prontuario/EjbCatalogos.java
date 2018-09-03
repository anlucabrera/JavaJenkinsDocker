/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.prontuario;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.OrganismosEvaluadores;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;



/**
 *
 * @author UTXJ
 */
@Local
public interface EjbCatalogos {
    
    public List<Generaciones> getGeneracionesAct();
    
    public List<AreasUniversidad> getProgramasEducativos();
    
    public List<CiclosEscolares> getCiclosEscolaresAct();
    
    public List<PeriodosEscolares> getPeriodosEscolaresAct();
    
    public List<OrganismosEvaluadores> getOrganismosEvaluadoresAct();
    
    public List<AreasUniversidad> getAreasAcademicas();
    
}
