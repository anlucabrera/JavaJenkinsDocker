/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.pa;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.IngresosPropiosCaptados;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.finanzas.list.ListaIngPropios;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbIngPropios {
    
   public ListaIngPropios getListaIngPropios(String rutaArchivo) throws Throwable;
   public void guardaIngPropios(ListaIngPropios listaIngPropios, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   public IngresosPropiosCaptados getRegistroIngresosPropiosCaptados(IngresosPropiosCaptados ingresosPropiosCaptados);
}
