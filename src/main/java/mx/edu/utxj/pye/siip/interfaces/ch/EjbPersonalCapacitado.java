/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.PersonalCapacitado;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.PercapModalidad;
import mx.edu.utxj.pye.sgi.entity.pye2.PercapTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaPersonalCapacitado;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbPersonalCapacitado {
    
   public ListaPersonalCapacitado getListaPersonalCapacitado(String rutaArchivo) throws Throwable;
   public void guardaPersonalCapacitado(ListaPersonalCapacitado listaPersonalCapacitado, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   public Integer getRegistroPersonalCapacitadoEspecifico(String curso);
   public PersonalCapacitado getRegistroPersonalCapacitado(PersonalCapacitado personalCapacitado); 
   public List<PercapTipo> getPerCapTipoAct();
   public List<PercapModalidad> getPerCapModalidadAct();
}
