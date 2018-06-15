/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import javax.ejb.Local;
import javax.faces.model.SelectItem;

/**
 *
 * @author UTXJ
 */
@Local
public interface EJBSelectItems {
    
    public List<SelectItem> itemsEvaluacionesDirectivos();
    
    public List<SelectItem> itemsEvaluacionDirectores();
    
    public List<SelectItem> itemsEvaluacionPersonal();
    
    public List<SelectItem> itemsEvaluacionSecretariaAcademica();
    
    public List<SelectItem> itemsPeriodos();
    
    public List<SelectItem> itemsPeriodos360();
    
    public List<SelectItem> itemPeriodosDesempenio();
    
    public List<SelectItem> itemPeriodosEvaluaciones(String tipo);
    
    public List<SelectItem> itemPeriodosDocenteMateria();
    
    public List<SelectItem> itemAreasAcademicas();
    
}
