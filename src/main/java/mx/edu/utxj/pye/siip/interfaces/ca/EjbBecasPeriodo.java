/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.BecasPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaBecasPeriodo;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbBecasPeriodo {
    
    public ListaBecasPeriodo getListaBecasPeriodo(String rutaArchivo) throws Throwable;
    public void guardaBecasPeriodo(ListaBecasPeriodo listaBecasPeriodo, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    public BecasPeriodosEscolares getRegistroBecasPeriodosEscolares(BecasPeriodosEscolares becasPeriodosEscolares);
}
