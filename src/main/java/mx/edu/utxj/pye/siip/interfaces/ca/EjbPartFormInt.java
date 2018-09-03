/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ParticipantesActividadesFormacionIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaPartActFormInt;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbPartFormInt {
   
   public ListaPartActFormInt getListaPartActFormInt(String rutaArchivo) throws Throwable;
   
   public void guardaPartActFormInt(ListaPartActFormInt listaPartActFormInt, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   
   public ParticipantesActividadesFormacionIntegral getRegistroParticipantesActividadesFormacionIntegral(ParticipantesActividadesFormacionIntegral participantesActividadesFormacionIntegral);
}
