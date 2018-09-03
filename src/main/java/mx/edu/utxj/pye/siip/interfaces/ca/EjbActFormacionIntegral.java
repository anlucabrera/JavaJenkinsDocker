/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import javax.ejb.Local;
import java.util.List;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesFormacionIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaActFormacionIntegral;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbActFormacionIntegral {
    
   public ListaActFormacionIntegral getListaActFormacionIntegral(String rutaArchivo) throws Throwable;
   
   public void guardaActFormacionIntegral(ListaActFormacionIntegral listaActFormacionIntegral, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   
   public ActividadesFormacionIntegral getRegistroActividadesFormacionIntegral(String actividadFormacionIntegral);
   
   public Integer getRegistroActFormacionIntegralEspecifico(String actividadFormacionIntegral);
   
   public List<ActividadesTipos> getActividadesTiposAct();
    
   public List<EventosTipos> getEventosTiposAct();
}
