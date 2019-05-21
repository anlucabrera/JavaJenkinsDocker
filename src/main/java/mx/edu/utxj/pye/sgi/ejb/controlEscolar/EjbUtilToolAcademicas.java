/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;

import javax.ejb.Local;
import java.util.List;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Login;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbUtilToolAcademicas {
    
    public Login autenticarUser(String usuario, String password);
    public void guardaGrupo(Grupo grupo, Integer noGrupos,Integer periodo);
    public void actualizaGrupo(Grupo grupo);
    public void eliminaGrupo(Grupo grupo);
    public List<Grupo> listaByPeriodo(Integer cve_periodo);
    
}
