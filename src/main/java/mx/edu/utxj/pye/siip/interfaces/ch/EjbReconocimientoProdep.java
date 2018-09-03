/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ReconocimientoProdepRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ReconocimientoProdepTiposApoyo;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaReconocimientoProdep;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbReconocimientoProdep {
   
   public ListaReconocimientoProdep getListaReconocimientoProdep(String rutaArchivo) throws Throwable;
   public void guardaReconocimientoProdep(ListaReconocimientoProdep listaReconocimientoProdep, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   public ReconocimientoProdepRegistros getRegistroReconocimientoProdepRegistros(ReconocimientoProdepRegistros reconocimientoProdepRegistros);
   public List<ReconocimientoProdepTiposApoyo> getReconocimientoProdepTiposApoyoAct();
}
