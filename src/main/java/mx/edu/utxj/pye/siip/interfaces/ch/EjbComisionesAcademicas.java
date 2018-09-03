/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicas;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicasTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaComisionesAcademicas;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbComisionesAcademicas {
    
   public ListaComisionesAcademicas getListaComisionesAcademicas(String rutaArchivo) throws Throwable;
   public void guardaComisionesAcademicas(ListaComisionesAcademicas listaComisionesAcademicas, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   public Integer getRegistroComisionesAcademicasEspecifico(String comisionAcademica);
   public ComisionesAcademicas getRegistroComisionesAcademicas(ComisionesAcademicas comisionesAcademicas); 
   public List<ComisionesAcademicasTipos> getComisionesAcademicasTiposAct();
}
