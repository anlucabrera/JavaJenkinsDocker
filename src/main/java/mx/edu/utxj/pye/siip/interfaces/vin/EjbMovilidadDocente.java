/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistroMovilidadDocente;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaMovilidadDocente;


/**
 *
 * @author UTXJ
 */
@Local
public interface EjbMovilidadDocente {
    
   public ListaMovilidadDocente getListaMovilidadDocente(String rutaArchivo) throws Throwable;
   public void guardaMovilidadDocente(ListaMovilidadDocente listaMovilidadDocente, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   public RegistroMovilidadDocente getRegistroMovilidadDocente(RegistroMovilidadDocente registroMovilidadDocente); 
}
