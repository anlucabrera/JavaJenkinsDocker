/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasEstimulos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasEstimulosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaProgramasEstimulos;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbProgramasEstimulos {
   
   public ListaProgramasEstimulos getListaProgramaEstimulos(String rutaArchivo) throws Throwable;
   public void guardaProgramasEstimulos(ListaProgramasEstimulos listaProgramaEstimulos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   public ProgramasEstimulos getRegistroProgramasEstimulos(ProgramasEstimulos programasEstimulos);
   public List<ProgramasEstimulosTipos> getProgramasEstimulosTiposAct();
}
