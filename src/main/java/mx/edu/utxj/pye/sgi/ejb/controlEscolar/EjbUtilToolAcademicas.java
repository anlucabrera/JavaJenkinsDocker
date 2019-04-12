/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;

import javax.ejb.Local;
import java.util.List;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbUtilToolAcademicas {

    public void guardaGrupo(Grupo grupo, Integer noGrupos);
    public List<Grupo> listaByPeriodo(Integer cve_periodo);
    
}
