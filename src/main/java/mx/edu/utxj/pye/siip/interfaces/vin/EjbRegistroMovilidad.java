/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosMovilidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasMovilidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaRegistroMovilidad;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbRegistroMovilidad {
    
   public ListaRegistroMovilidad getListaRegistroMovilidad(String rutaArchivo) throws Throwable;
   
   public void guardaRegistroMovilidad(ListaRegistroMovilidad listaRegistroMovilidad, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   
   public Integer getRegistroMovilidadEspecifico(String registroMovilidad); 
   
   public RegistrosMovilidad getRegistrosMovilidad(RegistrosMovilidad registrosMovilidad);
   
   public List<ProgramasMovilidad> getProgramasMovilidadAct();
}
