/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ch;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ParticipantesPersonalCapacitado;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaPerCapParticipantes;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbPerCapParticipantes {
    
   public ListaPerCapParticipantes getListaPerCapParticipantes(String rutaArchivo) throws Throwable;
   public void guardaPerCapParticipantes(ListaPerCapParticipantes listaPerCapParticipantes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   public ParticipantesPersonalCapacitado getRegistroParticipantesPersonalCapacitado(ParticipantesPersonalCapacitado participantesPersonalCapacitado);
}
