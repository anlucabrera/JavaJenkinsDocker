/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.VisitasIndustriales;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaVisitasIndustriales;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbVisitasIndustriales {
    
   public ListaVisitasIndustriales getListaVisitasIndustriales(String rutaArchivo) throws Throwable;
   public void guardaVisitasIndustriales(ListaVisitasIndustriales listaVisitasIndustriales, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   public VisitasIndustriales getRegistroVisitasIndustriales(VisitasIndustriales visitasIndustriales); 
}
