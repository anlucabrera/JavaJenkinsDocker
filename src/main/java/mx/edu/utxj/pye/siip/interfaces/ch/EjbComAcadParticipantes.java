/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ch;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicasParticipantes;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaComAcadParticipantes;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbComAcadParticipantes {
    
   public ListaComAcadParticipantes getListaComAcadParticipantes(String rutaArchivo) throws Throwable;
   public void guardaComAcadParticipantes(ListaComAcadParticipantes listaComAcadParticipantes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   public ComisionesAcademicasParticipantes getRegistroComisionesAcademicasParticipantes(ComisionesAcademicasParticipantes comisionesAcademicasParticipantes);

}
