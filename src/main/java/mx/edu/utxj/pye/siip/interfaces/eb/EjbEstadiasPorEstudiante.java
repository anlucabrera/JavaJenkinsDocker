/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.eb;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EstadiasPorEstudiante;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaEstadiasPorEstudiante;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEstadiasPorEstudiante {
    
    public ListaEstadiasPorEstudiante getListaEstadiasPorEstudiante(String rutaArchivo) throws Throwable;
    public void guardaEstadiasPorEstudiante(ListaEstadiasPorEstudiante listaEstadiasPorEstudiante, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    public EstadiasPorEstudiante getEstadiaPorEstudiante(EstadiasPorEstudiante estadiasPorEstudiante);
    
}
